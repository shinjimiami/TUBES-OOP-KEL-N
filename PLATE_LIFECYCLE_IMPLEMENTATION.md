# Implementasi Siklus Hidup Piring (Plate Lifecycle)

## Overview
Dokumentasi lengkap untuk siklus hidup piring yang melibatkan 3 station utama:
1. **PlateStorage** - Menyimpan piring bersih & kotor
2. **ServingCounter** - Menyajikan order & mengembalikan piring kotor
3. **WashingStation** - Mencuci piring kotor menjadi bersih

---

## 📋 Flow Diagram

```
[PlateStorage: Clean Plates]
         |
         | (1) Chef ambil 1 piring bersih
         v
    [Chef: Plate]
         |
         | (2) Chef assembly makanan di AssemblyStation
         v
    [Chef: Plate + Dish]
         |
         | (3) Chef serve ke ServingCounter
         v
 [ServingCounter: Validate Order]
         |
         | (4) Timer 10 detik
         v
 [ServingCounter: Plate KOTOR]
         |
         | (5) Auto return ke PlateStorage (push ke top stack)
         v
[PlateStorage: Dirty Plates on Top]
         |
         | (6) Chef ambil SEMUA piring kotor
         v
    [Chef: DirtyPlateStack]
         |
         | (7) Chef bawa ke WashingStation
         v
 [WashingStation: Queue Dirty Plates]
         |
         | (8) Chef interact untuk mulai mencuci (BUSY 3 detik)
         v
 [WashingStation: Clean Plate Stack]
         |
         | (9) Chef ambil piring bersih
         v
    [Chef: Clean Plate]
         |
         | (10) LOOP ke step 2 atau kembalikan ke PlateStorage
         v
```

---

## 1️⃣ PLATE.JAVA

### Update yang Dibuat

#### Method: `wash()`
```java
public void wash() {
    this.isDirty = false;
    clearDish();
    System.out.println("[PLATE] Piring telah dicuci dan bersih.");
}
```

**Purpose**: Mengubah status piring dari KOTOR menjadi BERSIH dan menghapus isi dish.

### Properties
```java
private boolean isDirty = false;
private Dish dish;
```

### Methods Summary
| Method | Deskripsi |
|--------|-----------|
| `setDirty(boolean)` | Set status kotor/bersih |
| `isDirty()` | Cek apakah piring kotor |
| `wash()` | Cuci piring (set bersih + clear dish) |
| `clearDish()` | Hapus isi dish |
| `getContainedDish()` | Dapatkan dish yang ada di piring |

---

## 2️⃣ SERVING COUNTER

### Logic Flow

#### Step 1: Chef Serve Plate
```java
@Override
public void interact(Chef player) {
    Item heldItem = player.getInventory();
    
    if (heldItem instanceof Plate) {
        Plate servedPlate = (Plate) heldItem;
        Dish servedDish = servedPlate.getContainedDish();
        
        // Validate dengan OrderManager
        boolean success = OrderManager.getInstance()
            .validateDish(servedDish.getComponents());
        
        if (success) {
            // ✅ ORDER SELESAI
            // 1. Skor bertambah (handled by OrderManager)
            // 2. Clear isi piring
            servedPlate.clearDish();
            // 3. Set status kotor
            servedPlate.setDirty(true);
            // 4. Mulai timer 10 detik
            returnQueue.add(new PlateReturnTimer(servedPlate, 10000));
        }
    }
}
```

#### Step 2: Timer 10 Detik (Auto Return)
```java
public void updateDirtyPlate(int timePassed) {
    Iterator<PlateReturnTimer> iterator = returnQueue.iterator();
    while (iterator.hasNext()) {
        PlateReturnTimer timer = iterator.next();
        timer.remainingTime -= timePassed;
        
        if (timer.remainingTime <= 0) {
            // Kembalikan piring kotor ke PlateStorage
            plateStorage.receiveDirtyPlate(timer.plate);
            iterator.remove();
        }
    }
}
```

**PENTING**: Method `updateDirtyPlate()` HARUS dipanggil dari `GamePanel.update()` loop!

### Constructor Requirements
```java
public ServingCounter(GamePanel gp, PlateStorage ps) {
    super(gp);
    this.plateStorage = ps; // WAJIB: Inject PlateStorage reference
}
```

### Integration dengan GamePanel
Tambahkan di `GamePanel.update()`:
```java
// Update all serving counters
for (Station station : map.getAllStations()) {
    if (station instanceof ServingCounter) {
        ((ServingCounter) station).updateDirtyPlate(16); // ~16ms per frame
    }
}
```

---

## 3️⃣ PLATE STORAGE

### Sudah Implemented ✅

#### Method: `receiveDirtyPlate(Plate)`
```java
public void receiveDirtyPlate(Plate dirtyPlate) {
    plateStack.push(dirtyPlate); // Push ke TOP stack
    System.out.println("[PLATE] Dirty plate received.");
}
```

#### Method: `interact(Chef)`
```java
@Override
public void interact(Chef player) {
    if (player.getInventory() != null) {
        // REJECT: Tidak menerima drop item
        return;
    }
    
    Plate topPlate = plateStack.peek();
    
    if (topPlate.isDirty()) {
        // Ambil SEMUA piring kotor dari atas
        takeDirtyStack(player); // Returns DirtyPlateStack
    } else {
        // Ambil 1 piring bersih
        Plate clean = plateStack.removeFirst();
        player.setInventory(clean);
    }
}
```

---

## 4️⃣ WASHING STATION

### Properties
```java
private final int WashDuration = 3000; // 3 detik
private int savedTime = 0;
private Chef busyChef = null;

private final Deque<Plate> cleanPlateStack;
private final Deque<Plate> dirtyPlateStack;
```

### Logic Flow

#### Interact Scenario 1: Chef Membawa DirtyPlateStack
```java
if (heldItem instanceof DirtyPlateStack) {
    DirtyPlateStack stack = (DirtyPlateStack) heldItem;
    
    // Transfer semua piring ke dirtyPlateStack
    for (Plate plate : stack.getPlates()) {
        dirtyPlateStack.push(plate);
    }
    player.setInventory(null);
    
    // Auto-start washing jika area cuci kosong
    if (containedItem == null && !dirtyPlateStack.isEmpty()) {
        super.placeItem(dirtyPlateStack.pop());
        startWashing(player); // Set chef BUSY
    }
}
```

#### Interact Scenario 2: Chef Membawa 1 Plate Kotor
```java
if (heldItem instanceof Plate && ((Plate) heldItem).isDirty()) {
    dirtyPlateStack.push((Plate) heldItem);
    player.setInventory(null);
    
    // Auto-start washing jika area cuci kosong
    if (containedItem == null) {
        super.placeItem(dirtyPlateStack.pop());
        startWashing(player);
    }
}
```

#### Interact Scenario 3: Chef Tangan Kosong - Ambil Piring Bersih
```java
if (heldItem == null && !cleanPlateStack.isEmpty() && busyChef == null) {
    player.setInventory(cleanPlateStack.pop());
    System.out.println("[WASH] Mengambil 1 piring bersih.");
}
```

#### Interact Scenario 4: Chef Tangan Kosong - Mulai Mencuci
```java
if (containedItem instanceof Plate && 
    ((Plate) containedItem).isDirty() && 
    busyChef == null) {
    startWashing(player);
}
```

### Method: `processWash(int timeNeeded)`
**Dipanggil dari GamePanel update loop**

```java
public boolean processWash(int timeNeeded) {
    // Validasi: Harus ada piring kotor DAN ada chef BUSY
    if (!(containedItem instanceof Plate) || 
        !((Plate) containedItem).isDirty() || 
        busyChef == null) {
        return false;
    }
    
    savedTime += timeNeeded;
    
    if (savedTime >= WashDuration) {
        // Cuci piring
        Plate plateToWash = (Plate) containedItem;
        plateToWash.wash();
        
        // Release chef dari BUSY
        busyChef.setCurrentAction(ChefStatus.IDLE);
        busyChef = null;
        
        // Pindahkan ke clean stack
        super.takeItem();
        cleanPlateStack.push(plateToWash);
        
        savedTime = 0;
        return true;
    }
    
    return false;
}
```

### Method: `startWashing(Chef chef)`
```java
public void startWashing(Chef chef) {
    if (busyChef == null && 
        containedItem instanceof Plate && 
        ((Plate) containedItem).isDirty()) {
        busyChef = chef;
        chef.setCurrentAction(ChefStatus.BUSY);
        System.out.println("[WASH] Chef is now BUSY washing plate");
    }
}
```

### Helper Methods
```java
public int getDirtyPlateCount() { return dirtyPlateStack.size(); }
public int getCleanPlateCount() { return cleanPlateStack.size(); }

// Cleanup method (untuk reset game)
public void returnCleanPlatesToStorage(PlateStorage storage) {
    while (!cleanPlateStack.isEmpty()) {
        storage.addPlate(cleanPlateStack.pop());
    }
}
```

---

## 🔗 INTEGRATION GUIDE

### 1. GameMap.java
Pastikan ServingCounter mendapat reference PlateStorage:

```java
// Dalam initializeStationsWithGamePanel()
PlateStorage plateStorage = null;

// Find PlateStorage first
for (int row = 0; row < gridLayout.length; row++) {
    for (int col = 0; col < gridLayout[row].length; col++) {
        char tile = gridLayout[row][col];
        if (tile == 'P') {
            plateStorage = new PlateStorage(gp);
            break;
        }
    }
}

// Create ServingCounter with PlateStorage reference
for (int row = 0; row < gridLayout.length; row++) {
    for (int col = 0; col < gridLayout[row].length; col++) {
        char tile = gridLayout[row][col];
        if (tile == 'S') {
            stations[col][row] = new ServingCounter(gp, plateStorage);
        }
    }
}
```

### 2. GamePanel.java - Update Loop
```java
@Override
public void update() {
    // ... existing code ...
    
    // Update ServingCounter timers (10 sec return)
    for (int x = 0; x < maxWorldCol; x++) {
        for (int y = 0; y < maxWorldRow; y++) {
            Station station = map.getStationAt(x, y);
            if (station instanceof ServingCounter) {
                ((ServingCounter) station).updateDirtyPlate(16); // 16ms per frame (~60 FPS)
            }
        }
    }
    
    // Update WashingStation timers (3 sec wash)
    for (int x = 0; x < maxWorldCol; x++) {
        for (int y = 0; y < maxWorldRow; y++) {
            Station station = map.getStationAt(x, y);
            if (station instanceof WashingStation) {
                ((WashingStation) station).processWash(16);
            }
        }
    }
}
```

### 3. SetupGame.java - Reset Logic
```java
public static void resetGame(GamePanel gp) {
    // ... existing reset code ...
    
    // Kembalikan semua piring bersih dari WashingStation ke PlateStorage
    PlateStorage plateStorage = findPlateStorage(gp.map);
    for (Station station : getAllStations(gp.map)) {
        if (station instanceof WashingStation) {
            ((WashingStation) station).returnCleanPlatesToStorage(plateStorage);
        }
    }
}
```

---

## 🎮 GAMEPLAY FLOW EXAMPLE

### Scenario: Complete Order Cycle

```
1. Chef A → PlateStorage.interact()
   Result: Chef A inventory = Plate (clean)

2. Chef A → AssemblyStation.interact() (multiple times)
   Result: Plate + Dish (Bun + Meat + Lettuce)

3. Chef A → CookingStation.interact() (cook meat)
   Result: Meat = COOKED

4. Chef A → ServingCounter.interact()
   Result: 
   - OrderManager validates dish → SUCCESS
   - Score += 50
   - Plate.setDirty(true)
   - Timer 10s starts

5. [10 seconds pass automatically]
   Result: PlateStorage receives dirty plate (push to top)

6. Chef B → PlateStorage.interact()
   Result: Chef B inventory = DirtyPlateStack (if multiple dirty plates on top)

7. Chef B → WashingStation.interact()
   Result: 
   - Transfer all plates to dirtyPlateStack
   - Auto-start washing first plate
   - Chef B status = BUSY

8. [3 seconds pass automatically]
   Result:
   - First plate washed → cleanPlateStack
   - Chef B status = IDLE

9. Chef B → WashingStation.interact() (repeat untuk cuci lebih banyak)

10. Chef C → WashingStation.interact() (ambil piring bersih)
    Result: Chef C inventory = Plate (clean) from cleanPlateStack
```

---

## 🧪 TESTING CHECKLIST

### Unit Tests

#### Plate.java
- [ ] `wash()` mengubah isDirty menjadi false
- [ ] `wash()` memanggil clearDish()
- [ ] `setDirty(true)` mengubah status

#### PlateStorage.java
- [ ] `receiveDirtyPlate()` push ke top stack
- [ ] `interact()` ambil semua piring kotor berurutan
- [ ] `interact()` ambil 1 piring bersih
- [ ] `interact()` reject drop item

#### ServingCounter.java
- [ ] `interact()` validate dish dengan OrderManager
- [ ] `interact()` set plate dirty setelah serve
- [ ] `updateDirtyPlate()` countdown 10 detik
- [ ] `updateDirtyPlate()` kirim ke PlateStorage setelah 10s

#### WashingStation.java
- [ ] `interact()` terima DirtyPlateStack
- [ ] `interact()` terima single dirty Plate
- [ ] `interact()` reject item lain
- [ ] `interact()` ambil piring bersih
- [ ] `processWash()` countdown 3 detik
- [ ] `processWash()` set chef IDLE setelah selesai
- [ ] `processWash()` push ke cleanPlateStack

### Integration Tests
- [ ] Full cycle: Clean → Serve → Dirty → Wash → Clean
- [ ] Multiple chefs washing simultaneously
- [ ] Stack behavior: LIFO for dirty plates
- [ ] Timer accuracy: 10s serve, 3s wash
- [ ] Chef BUSY status during washing
- [ ] PlateStorage handle mixed clean/dirty stack

### Edge Cases
- [ ] Serve empty plate (validation fail)
- [ ] Serve wrong dish (validation fail)
- [ ] WashingStation penuh dengan dirty plates
- [ ] Multiple ServingCounters share 1 PlateStorage
- [ ] Chef disconnect while washing (busyChef = null)
- [ ] Reset game with plates in washing queue

---

## 🐛 COMMON ISSUES & SOLUTIONS

### Issue 1: Piring Tidak Kembali ke PlateStorage
**Cause**: `updateDirtyPlate()` tidak dipanggil dari GamePanel  
**Solution**: Tambahkan loop di `GamePanel.update()` untuk semua ServingCounter

### Issue 2: Chef Stuck BUSY Forever
**Cause**: `processWash()` tidak dipanggil atau busyChef tidak di-reset  
**Solution**: Pastikan `processWash()` loop di GamePanel dan release busyChef saat selesai

### Issue 3: DirtyPlateStack Tidak Diterima WashingStation
**Cause**: Missing `instanceof DirtyPlateStack` check  
**Solution**: Sudah diimplementasi di `interact()` scenario 1A

### Issue 4: Piring Bersih Tidak Muncul di WashingStation
**Cause**: `cleanPlateStack` tidak diupdate atau method `getCleanPlateCount()` tidak dipanggil  
**Solution**: Pastikan `cleanPlateStack.push()` dipanggil setelah washing selesai

---

## 📊 PERFORMANCE CONSIDERATIONS

- **Timer Precision**: Gunakan millisecond timer (16ms frame) bukan detik
- **Iterator Safety**: Gunakan `Iterator.remove()` untuk hapus dari returnQueue
- **Stack Operations**: Semua O(1) - push/pop/peek
- **Memory**: DirtyPlateStack temporary object, released setelah transfer
- **Thread Safety**: Tidak perlu mutex karena single-threaded game loop

---

## 🎯 SOLID PRINCIPLES APPLIED

1. **Single Responsibility**:
   - Plate: Status & dish management
   - ServingCounter: Validate & timer return
   - WashingStation: Queue & washing process

2. **Open/Closed**:
   - Extensible: Bisa tambah PlateStack variant
   - Closed: Core logic tidak perlu diubah

3. **Liskov Substitution**:
   - DirtyPlateStack extends Item, bisa digunakan di inventory
   - Plate extends KitchenUtensils, polymorphic

4. **Interface Segregation**:
   - Station interface cukup untuk interact()
   - Tidak perlu interface baru untuk washing

5. **Dependency Inversion**:
   - ServingCounter depend on PlateStorage abstraction
   - WashingStation independent dari PlateStorage

---

## ✅ COMPLETION STATUS

- [✅] Plate.wash() method
- [✅] ServingCounter timer logic (10s)
- [✅] PlateStorage receive dirty plates
- [✅] WashingStation handle DirtyPlateStack
- [✅] WashingStation washing process (3s)
- [✅] Chef BUSY status management
- [⏳] GamePanel integration (perlu update)
- [⏳] GameMap constructor integration (perlu update)
- [⏳] Testing & debugging

---

## 🚀 NEXT STEPS

1. **Update GameMap.java**: Pass PlateStorage ke ServingCounter constructor
2. **Update GamePanel.java**: Tambahkan loop updateDirtyPlate() dan processWash()
3. **Update SetupGame.java**: Tambahkan cleanup piring saat reset
4. **Testing**: Run full cycle dari clean → serve → wash
5. **UI/UX**: Tambahkan visual indicator untuk washing progress
6. **Sound Effects**: Tambahkan SFX untuk wash complete & serve success


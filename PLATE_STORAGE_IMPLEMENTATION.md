# Implementasi PlateStorage - Dokumentasi

## Overview
`PlateStorage` (Station P) adalah station yang menyimpan piring dalam bentuk tumpukan (stack). Station ini menggunakan struktur data `Deque<Plate>` untuk implementasi LIFO (Last In, First Out).

## Struktur Data
- **Type**: `Deque<Plate>` (LinkedList implementation)
- **Kapasitas Awal**: 10 piring bersih
- **Mekanisme**: Stack (LIFO - Last In First Out)

## Logika Interaksi

### 1. Mengambil Piring Bersih (KONDISI 2)
**Kondisi**: Piring paling atas adalah BERSIH
```java
// Chef mengambil HANYA 1 piring bersih
if (!topPlate.isDirty()) {
    Plate cleanPlate = plateStack.removeFirst();
    player.setInventory(cleanPlate);
}
```

**Behavior**:
- Chef hanya mengambil 1 piring
- Inventory chef menjadi `Plate` (single object)

### 2. Mengambil Piring Kotor (KONDISI 1)
**Kondisi**: Piring paling atas adalah KOTOR
```java
// Chef mengambil SEMUA piring kotor dari atas stack
if (topPlate.isDirty()) {
    takeDirtyStack(player);
}
```

**Behavior**:
- Chef mengambil SEMUA piring kotor yang berurutan dari atas
- Loop berhenti saat ketemu piring bersih atau stack habis
- Inventory chef menjadi `DirtyPlateStack` (object yang berisi List<Plate>)

### 3. Drop Item (KONDISI 3)
**Kondisi**: Chef mencoba menaruh item ke PlateStorage
```java
if (player.getInventory() != null) {
    System.out.println("Plate Storage hanya untuk mengambil piring.");
    return; // REJECT
}
```

**Behavior**:
- Station ini TIDAK MENERIMA drop item apapun
- Sesuai spesifikasi halaman 14-15

## Class Baru: DirtyPlateStack

### Purpose
Menampung tumpukan piring kotor yang diambil sekaligus oleh chef.

### Properties
```java
private final List<Plate> dirtyPlates;
```

### Methods

#### `addPlate(Plate plate)`
Menambahkan piring kotor ke tumpukan
```java
DirtyPlateStack stack = new DirtyPlateStack(gp);
stack.addPlate(dirtyPlate);
```

#### `getCount()`
Mendapatkan jumlah piring dalam tumpukan
```java
int count = stack.getCount(); // misal: 3
```

#### `getPlates()`
Mendapatkan semua piring dalam tumpukan
```java
List<Plate> plates = stack.getPlates();
```

#### `removeOne()`
Mengambil 1 piring dari tumpukan
```java
Plate plate = stack.removeOne();
```

#### `isEmpty()`
Cek apakah tumpukan kosong
```java
if (stack.isEmpty()) {
    // handle empty stack
}
```

## Update Chef.java

### Helper Methods

#### `isCarryingDirtyPlates()`
Cek apakah chef membawa tumpukan piring kotor
```java
if (chef.isCarryingDirtyPlates()) {
    // Chef membawa DirtyPlateStack
}
```

#### `getDirtyPlateStack()`
Mendapatkan DirtyPlateStack dari inventory
```java
DirtyPlateStack stack = chef.getDirtyPlateStack();
if (stack != null) {
    int count = stack.getCount();
    System.out.println("Chef membawa " + count + " piring kotor");
}
```

## Contoh Penggunaan

### Scenario 1: Ambil Piring Bersih
```java
// PlateStorage: [Clean, Clean, Clean, Clean, Clean]
chef.interact(plateStorage);
// Result: Chef inventory = Plate (clean)
// PlateStorage: [Clean, Clean, Clean, Clean]
```

### Scenario 2: Ambil Piring Kotor (3 piring)
```java
// PlateStorage: [Dirty, Dirty, Dirty, Clean, Clean]
chef.interact(plateStorage);
// Result: Chef inventory = DirtyPlateStack (3 plates)
// PlateStorage: [Clean, Clean]
```

### Scenario 3: Serving Counter Mengirim Piring Kotor
```java
// Setelah order selesai di ServingCounter:
Plate dirtyPlate = servingCounter.getUsedPlate();
dirtyPlate.setDirty(true);
plateStorage.receiveDirtyPlate(dirtyPlate);
// PlateStorage: [Dirty (new), Clean, Clean, Clean, Clean]
```

### Scenario 4: Drop Item (DITOLAK)
```java
// Chef membawa Tomato
chef.setInventory(new Tomato(gp));
chef.interact(plateStorage);
// Output: "Plate Storage hanya untuk mengambil piring, tidak bisa drop item."
// Chef masih membawa Tomato
```

## Integration dengan Station Lain

### 1. ServingCounter
ServingCounter harus memanggil `receiveDirtyPlate()` setelah order selesai:
```java
// Dalam ServingCounter.java
public void serveOrder(Order order, Plate plate) {
    if (orderMatches(order, plate)) {
        // ... serve logic
        plate.setDirty(true);
        plateStorage.receiveDirtyPlate(plate); // Kembalikan piring kotor
    }
}
```

### 2. WashingStation (jika ada)
WashingStation bisa menerima DirtyPlateStack dan membersihkannya:
```java
// Dalam WashingStation.java
public void interact(Chef player) {
    if (player.isCarryingDirtyPlates()) {
        DirtyPlateStack stack = player.getDirtyPlateStack();
        // Proses mencuci semua piring
        for (Plate plate : stack.getPlates()) {
            plate.setDirty(false);
            plateStorage.addPlate(plate); // Kembalikan sebagai piring bersih
        }
        player.setInventory(null);
    }
}
```

## Data Flow Diagram
```
[Chef] --ambil--> [PlateStorage: Clean Plates] --1 plate--> [Chef Inventory: Plate]
                  [PlateStorage: Dirty Plates] --N plates--> [Chef Inventory: DirtyPlateStack]

[ServingCounter] --serve order--> [Plate becomes Dirty] --push--> [PlateStorage: Top of Stack]

[Chef: DirtyPlateStack] --cuci--> [WashingStation] --push clean--> [PlateStorage: Top of Stack]
```

## Testing Checklist

### Unit Tests
- [ ] Test ambil 1 piring bersih
- [ ] Test ambil tumpukan piring kotor (2-5 piring)
- [ ] Test ambil piring kotor sampai habis
- [ ] Test reject drop item
- [ ] Test receiveDirtyPlate()
- [ ] Test stack kosong

### Integration Tests
- [ ] Test flow: Serve Order → Dirty Plate → PlateStorage
- [ ] Test flow: PlateStorage → DirtyPlateStack → WashingStation → PlateStorage
- [ ] Test flow: PlateStorage → Clean Plate → AssemblyStation → Serve

### Edge Cases
- [ ] Stack kosong saat chef interact
- [ ] Stack penuh dengan dirty plates
- [ ] Stack hanya berisi 1 plate (clean/dirty)
- [ ] Chef membawa item lain saat interact

## Performance Considerations
- **Deque Operations**: O(1) untuk push/pop/peek
- **takeDirtyStack()**: O(n) dimana n = jumlah piring kotor berurutan di atas
- **Memory**: Minimal, hanya menyimpan references ke Plate objects

## SOLID Principles Applied
1. **Single Responsibility**: PlateStorage hanya handle storage piring
2. **Open/Closed**: Extensible untuk logic cuci piring (via DirtyPlateStack)
3. **Liskov Substitution**: DirtyPlateStack extends Item, bisa digunakan di inventory
4. **Interface Segregation**: Station interface cukup untuk interact()
5. **Dependency Inversion**: Depend on Item abstraction, bukan concrete Plate

## Next Steps
1. ✅ Implementasi PlateStorage.java
2. ✅ Buat DirtyPlateStack.java
3. ✅ Update Chef.java helper methods
4. ⏳ Update ServingCounter untuk receiveDirtyPlate()
5. ⏳ Implementasi WashingStation (jika ada)
6. ⏳ Testing & debugging

# Plate Lifecycle - Quick Reference Card

## 🔄 COMPLETE FLOW

```
CLEAN PLATE → ASSEMBLY → SERVE → TIMER 10s → DIRTY PLATE → 
WASHING STATION → TIMER 3s → CLEAN PLATE (loop)
```

---

## 📝 IMPLEMENTATION CHECKLIST

### ✅ Sudah Selesai

- [✅] **Plate.java**
  - Method `wash()` untuk cuci piring
  - Properties `isDirty` & `dish`

- [✅] **PlateStorage.java**
  - `receiveDirtyPlate(Plate)` - terima dari ServingCounter
  - `interact(Chef)` - ambil piring (bersih/kotor)
  - `addPlate(Plate)` - general purpose

- [✅] **ServingCounter.java**
  - `interact(Chef)` - terima plate + validate order
  - `updateDirtyPlate(int)` - timer 10 detik return
  - Constructor `(GamePanel, PlateStorage)` - inject dependency

- [✅] **WashingStation.java**
  - `interact(Chef)` - handle DirtyPlateStack & single Plate
  - `processWash(int)` - timer 3 detik washing
  - `startWashing(Chef)` - set chef BUSY
  - Helper methods: `getDirtyPlateCount()`, `getCleanPlateCount()`

- [✅] **DirtyPlateStack.java**
  - Container untuk tumpukan piring kotor
  - Methods: `addPlate()`, `getPlates()`, `getCount()`

- [✅] **Chef.java**
  - `isCarryingDirtyPlates()` - cek apakah bawa DirtyPlateStack
  - `getDirtyPlateStack()` - ambil DirtyPlateStack dari inventory

### ⏳ Perlu Integrasi

- [ ] **GameMap.java**
  - Pass PlateStorage reference ke ServingCounter constructor
  - Lihat dokumentasi lengkap di PLATE_LIFECYCLE_IMPLEMENTATION.md

- [ ] **GamePanel.java**
  - Tambahkan loop `updateDirtyPlate()` untuk ServingCounter
  - Tambahkan loop `processWash()` untuk WashingStation
  - Lihat contoh code di dokumentasi

- [ ] **SetupGame.java**
  - Cleanup: `returnCleanPlatesToStorage()` saat reset game

---

## 🎮 USAGE EXAMPLES

### 1. Serve Order
```java
// Chef membawa Plate dengan Dish
chef.interact(servingCounter);
// Result: Dish validated, plate kotor, timer 10s starts
```

### 2. Take Dirty Plates
```java
// PlateStorage has dirty plates on top
chef.interact(plateStorage);
// Result: Chef inventory = DirtyPlateStack (semua piring kotor)
```

### 3. Wash Dirty Plates
```java
// Chef membawa DirtyPlateStack
chef.interact(washingStation);
// Result: Plates queued, auto-start washing, chef BUSY 3s
```

### 4. Take Clean Plate
```java
// WashingStation has clean plates
chef.interact(washingStation);
// Result: Chef inventory = Plate (clean)
```

---

## ⚠️ IMPORTANT NOTES

1. **ServingCounter WAJIB** punya reference ke PlateStorage
   ```java
   new ServingCounter(gp, plateStorage);
   ```

2. **GamePanel WAJIB** update timers:
   ```java
   servingCounter.updateDirtyPlate(16); // 16ms per frame
   washingStation.processWash(16);
   ```

3. **WashingStation** set chef BUSY, pastikan:
   - Chef tidak bisa move saat BUSY
   - Status di-reset setelah washing selesai

4. **PlateStorage** stack behavior:
   - LIFO (Last In First Out)
   - Dirty plates push ke TOP
   - Ambil semua dirty plates berurutan

---

## 🔧 INTEGRATION CODE SNIPPETS

### GameMap.java - Constructor Fix
```java
// 1. Find PlateStorage first
PlateStorage plateStorage = null;
for (Station station : getAllStations()) {
    if (station instanceof PlateStorage) {
        plateStorage = (PlateStorage) station;
        break;
    }
}

// 2. Create ServingCounter with reference
for (char tile : gridLayout) {
    if (tile == 'S') {
        stations[x][y] = new ServingCounter(gp, plateStorage);
    }
}
```

### GamePanel.java - Update Loop
```java
@Override
public void update() {
    // Existing code...
    
    // Update serving timers
    for (Station station : map.getAllStations()) {
        if (station instanceof ServingCounter) {
            ((ServingCounter) station).updateDirtyPlate(16);
        }
        if (station instanceof WashingStation) {
            ((WashingStation) station).processWash(16);
        }
    }
}
```

---

## 📊 PERFORMANCE TIPS

- **Timer**: Gunakan millisecond (16ms) bukan detik
- **Iterator**: Gunakan `Iterator.remove()` untuk thread safety
- **Stack**: Semua operasi O(1)
- **Memory**: DirtyPlateStack temporary, released setelah transfer

---

## 🐛 TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| Plate tidak kembali ke PlateStorage | Cek `updateDirtyPlate()` dipanggil di GamePanel |
| Chef stuck BUSY | Cek `processWash()` release busyChef setelah 3s |
| WashingStation tidak terima DirtyPlateStack | Cek `instanceof DirtyPlateStack` di interact() |
| Piring bersih tidak muncul | Cek `cleanPlateStack.push()` setelah wash |

---

## 📚 Full Documentation

Lihat [PLATE_LIFECYCLE_IMPLEMENTATION.md](./PLATE_LIFECYCLE_IMPLEMENTATION.md) untuk dokumentasi lengkap dengan:
- Flow diagram detail
- Integration guide lengkap
- Testing checklist
- SOLID principles analysis
- Common issues & solutions

---

**Last Updated**: December 12, 2025  
**Status**: ✅ Implementation Complete | ⏳ Integration Pending

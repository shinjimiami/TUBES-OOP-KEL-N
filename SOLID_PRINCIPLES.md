# SOLID Principles Implementation

## Dokumentasi Penerapan SOLID di Nimonscooked Game

### 1. **S - Single Responsibility Principle (SRP)** ✅

**Prinsip:** Setiap class hanya memiliki satu tanggung jawab/alasan untuk berubah.

**Implementasi:**

- **OrderManager.java** - Hanya bertanggung jawab untuk mengelola pesanan (orders) dan scoring
- **CollisionChecker.java** - Hanya mengurus deteksi collision
- **UI.java** - Hanya mengurus rendering visual ke layar
- **KeyHandler.java** - Hanya mengurus input keyboard
- **SetupGame.java** - Hanya mengurus inisialisasi dan reset game

**Contoh Kode:**
```java
// OrderManager hanya mengurus order logic
public class OrderManager {
    private List<Order> activeOrders;
    private int score;
    
    public void generateOrder() { ... }
    public boolean validateDish(...) { ... }
    public void update(...) { ... }
}

// CollisionChecker class terpisah untuk collision
public class CollisionChecker {
    public boolean checkTile(Chef chef) { ... }
    public Station checkStation(Chef chef, int direction) { ... }
}
```

---

### 2. **O - Open/Closed Principle (OCP)** ✅

**Prinsip:** Open for extension, closed for modification.

**Implementasi:**

- **Station (abstract class)** - Bisa di-extend untuk station baru tanpa modifikasi base class
- **Item hierarchy** - Item → Ingredient/Dish/KitchenUtensils bisa ditambah tipe baru
- **Command Pattern** - Bisa tambah command baru tanpa ubah existing commands

**Contoh Kode:**
```java
// Abstract Station - Open for extension
public abstract class Station extends Entity {
    public abstract void interact(Chef player);
    // ... common functionality
}

// Extension - tidak perlu modifikasi Station class
public class CuttingStation extends Station {
    @Override
    public void interact(Chef player) {
        // Cutting-specific logic
    }
}

public class CookingStation extends Station {
    @Override
    public void interact(Chef player) {
        // Cooking-specific logic
    }
}
```

---

### 3. **L - Liskov Substitution Principle (LSP)** ✅

**Prinsip:** Subclass harus bisa menggantikan parent class tanpa error.

**Implementasi:**

- Semua **Station subclasses** bisa digunakan sebagai Station
- Semua **Ingredient subclasses** bisa digunakan sebagai Preparable interface
- **List<Preparable>** bisa berisi Meat, Cheese, Lettuce, dll secara interchangeable

**Contoh Kode:**
```java
// Liskov: Semua station bisa diperlakukan sama
public Station getStationAt(int x, int y) {
    return stationGrid[y][x]; // Bisa CuttingStation, CookingStation, dll
}

// Liskov: Semua Preparable bisa diperlakukan sama
public boolean validateDish(List<Preparable> plateContents) {
    for (Preparable item : plateContents) {
        // Bisa Meat, Cheese, Lettuce - semua compatible
        if (item.getState() == IngredientState.COOKED) { ... }
    }
}
```

---

### 4. **I - Interface Segregation Principle (ISP)** ✅

**Prinsip:** Jangan paksa client implement interface yang tidak dipakai.

**Implementasi:**

- **Preparable interface** - Hanya method untuk item yang bisa diproses (chop, cook)
- **CookingDevice interface** - Hanya untuk device yang bisa memasak
- **Command interface** - Hanya execute() untuk command pattern

**Contoh Kode:**
```java
// Interface kecil dan focused
public interface Preparable {
    void chop();
    void cook();
    boolean canBeCooked();
    IngredientState getState();
    String getName();
}

// Interface terpisah untuk cooking device
public interface CookingDevice {
    boolean isPortable();
    int capacity();
    void addIngredient(Preparable ingredient);
    List<Preparable> getContents();
}

// Command interface minimal
public interface Command {
    void execute();
}
```

**Counter-example (ISP violation avoided):**
```java
// TIDAK dilakukan:
// public interface Item {
//     void chop();  // ❌ Tidak semua Item bisa di-chop
//     void cook();  // ❌ Tidak semua Item bisa di-cook
// }

// YANG DILAKUKAN: Interface segregation
public class Item { /* base properties only */ }
public interface Preparable { /* chop/cook methods */ }
```

---

### 5. **D - Dependency Inversion Principle (DIP)** ✅

**Prinsip:** Depend on abstractions, not concrete classes.

**Implementasi:**

- **Command pattern** - GamePanel depend on Command interface, bukan concrete commands
- **Preparable interface** - KitchenUtensils depend on Preparable, bukan concrete Ingredient
- **List/Collection** - Menggunakan interface List, bukan ArrayList konkret

**Contoh Kode:**
```java
// Depend on abstraction (List interface)
private List<Order> activeOrders = new CopyOnWriteArrayList<>();

// Depend on abstraction (Preparable interface)
public abstract class KitchenUtensils {
    protected List<Preparable> contents;  // ✅ Interface, bukan Meat/Cheese
    
    public List<Preparable> getContents() {
        return contents;
    }
}

// Command pattern - depend on Command interface
private void executeCommand() {
    Command cmd = new InteractCommand(...);  // Concrete implementation
    cmd.execute();  // ✅ Use through interface
}
```

---

## Summary Checklist

| SOLID Principle | Implemented | Example Class |
|----------------|-------------|---------------|
| **S**ingle Responsibility | ✅ | OrderManager, UI, CollisionChecker |
| **O**pen/Closed | ✅ | Station, Item hierarchy |
| **L**iskov Substitution | ✅ | Station subclasses, Preparable implementations |
| **I**nterface Segregation | ✅ | Preparable, CookingDevice, Command |
| **D**ependency Inversion | ✅ | List<Preparable>, Command pattern |

**Total: 5/5 SOLID Principles Implemented** ✅

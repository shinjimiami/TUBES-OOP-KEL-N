# OOP Concepts Implementation Checklist

## ✅ Ketentuan Teknis - SUDAH LENGKAP

### 1. **Inheritance** ✅
- `Entity` (abstract) → `Chef`, `Station`
- `Item` → `Ingredient`, `Dish`, `KitchenUtensils`
- `Station` → `CuttingStation`, `CookingStation`, `AssemblyStation`, dll
- `Ingredient` → `Meat`, `Cheese`, `Lettuce`, `Tomato`, `Bun`

**File Examples:**
- `/entity/Entity.java` (base abstract class)
- `/entity/Chef.java` (extends Entity)
- `/entity/station/Station.java` (abstract)
- `/entity/station/CuttingStation.java` (extends Station)

---

### 2. **Abstract Class / Interface** ✅

**Abstract Classes:**
- `Entity` - Base for all game entities
- `Station` - Base for all station types
- `Item` - Base for all items
- `Ingredient` - Base for all ingredients
- `KitchenUtensils` - Base for kitchen tools

**Interfaces:**
- `Preparable` - For items that can be chopped/cooked
- `CookingDevice` - For cooking tools (FryingPan)
- `Command` - Command pattern implementation

**Files:**
- `/entity/Entity.java`
- `/interfaces/Preparable.java`
- `/interfaces/CookingDevice.java`
- `/action/Command.java`

---

### 3. **Polymorphism** ✅

**Examples:**
```java
// Station polymorphism
Station station = gameMap.getStationAt(x, y);
station.interact(chef); // Calls different interact() per subclass

// Preparable polymorphism
List<Preparable> contents = plate.getContents();
for (Preparable item : contents) {
    item.chop(); // Different behavior per ingredient
}

// Command polymorphism
Command cmd = new InteractCommand(...);
cmd.execute();
```

**Files:**
- `/object/GameMap.java` (Station polymorphism)
- `/main/OrderManager.java` (Preparable polymorphism)
- `/action/` (Command polymorphism)

---

### 4. **Generics** ✅

**Built-in Generic Collections:**
```java
List<Chef> chefs = new ArrayList<>();
List<Order> activeOrders = new CopyOnWriteArrayList<>();
Map<String, Item> floorItems = new HashMap<>();
Deque<Plate> plateStack = new LinkedList<>();
```

**Custom Generic Class:**
```java
public class GenericStorage<T> {
    private final List<T> items;
    
    public boolean addItem(T item) { ... }
    public Optional<T> removeItem() { ... }
    public List<T> getAllItems() { ... }
}
```

**Generic Methods:**
```java
public List<Preparable> getContents() { ... }
public boolean validateDish(List<Preparable> plateContents) { ... }
```

**Files:**
- `/util/GenericStorage.java` - Custom generic class
- `/main/GamePanel.java` - Generic collections
- `/entity/item/kitchenutensil/KitchenUtensils.java` - Generic List<Preparable>

---

### 5. **Exceptions** ✅

**Custom Exception Hierarchy:**
```java
GameException (base)
├── InvalidStationInteractionException
├── IngredientProcessingException
└── OrderValidationException
```

**Exception Handling:**
```java
try {
    // Game operations
} catch (InvalidStationInteractionException e) {
    System.err.println("Station error: " + e.getMessage());
} catch (IngredientProcessingException e) {
    System.err.println("Processing error: " + e.getMessage());
}
```

**Files:**
- `/exceptions/GameException.java` (base)
- `/exceptions/InvalidStationInteractionException.java`
- `/exceptions/IngredientProcessingException.java`
- `/exceptions/OrderValidationException.java`

---

### 6. **Collections** ✅

**Usage:**
- `ArrayList<>` - Dynamic chef list, recipe list
- `CopyOnWriteArrayList<>` - Thread-safe order list
- `LinkedList<>` / `Deque<>` - Plate stacks (LIFO)
- `HashMap<>` - Floor items storage (key-value)

**Files:**
- `/main/GamePanel.java` - `List<Chef> chefs`
- `/main/OrderManager.java` - `CopyOnWriteArrayList<Order>`
- `/entity/station/PlateStorage.java` - `Deque<Plate>`
- `/object/GameMap.java` - `Map<String, Item>`

---

### 7. **Concurrency (Multithreading)** ✅

**Implementation:**

**Cutting Operation (3 seconds - Chef BUSY):**
```java
public class CuttingTask implements Runnable {
    @Override
    public void run() {
        chef.setCurrentAction(ChefStatus.BUSY);
        Thread.sleep(3000); // 3 seconds cutting
        item.chop();
        chef.setCurrentAction(ChefStatus.IDLE);
    }
}

// Usage:
Thread cuttingThread = new Thread(new CuttingTask(...));
cuttingThread.start();
```

**Cooking Operation (12s cook, 24s burn - Chef FREE):**
```java
public class CookingTask implements Runnable {
    @Override
    public void run() {
        while (!stopped) {
            if (elapsed >= cookDuration) {
                item.cook(); // Background cooking
            }
            Thread.sleep(100);
        }
    }
}

// Usage:
Thread cookingThread = new Thread(new CookingTask(...));
cookingThread.start();
```

**Files:**
- `/util/CuttingTask.java` - Multithreading for cutting
- `/util/CookingTask.java` - Multithreading for cooking
- `/entity/station/CuttingStation.java` - Uses threading
- `/entity/station/CookingStation.java` - Background process

---

### 8. **Design Patterns (Minimum 2)** ✅

#### **Pattern 1: Singleton**
```java
public class OrderManager {
    private static OrderManager instance;
    
    private OrderManager() { }
    
    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }
}
```
**File:** `/main/OrderManager.java`

#### **Pattern 2: Command**
```java
public interface Command {
    void execute();
}

public class InteractCommand implements Command {
    public void execute() {
        station.interact(chef);
    }
}
```
**Files:** 
- `/action/Command.java` (interface)
- `/action/InteractCommand.java`
- `/action/PickUpCommand.java`
- `/action/MoveCommand.java`

#### **Pattern 3: Factory Method**
```java
public Ingredient creteIngredientByType(IngredientType type) {
    switch (type) {
        case BUN: return new Bun(gp, RAW);
        case CHEESE: return new Cheese(gp, RAW);
        case MEAT: return new Meat(gp, RAW);
        // ...
    }
}
```
**File:** `/entity/station/IngredientStorage.java`

#### **Pattern 4: Observer** ✅
```java
// Subject interface
public interface GameSubject {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers();
}

// Observer interface
public interface GameObserver {
    void onOrderCompleted(int score);
    void onOrderExpired(int penalty);
    void onScoreChanged(int newScore);
    void onTimeUpdate(float timeRemaining);
}

// Concrete implementation
public class OrderManager implements GameSubject {
    private List<GameObserver> observers = new ArrayList<>();
    
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    public void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onScoreChanged(score);
            observer.onTimeUpdate(stageTimeRemaining);
        }
    }
}
```
**Files:**
- `/interfaces/GameObserver.java` - Observer interface
- `/interfaces/GameSubject.java` - Subject interface
- `/util/StatisticsObserver.java` - Concrete observer
- `/main/OrderManager.java` - Implements GameSubject

#### **Pattern 5: Strategy** ✅
```java
// Strategy interface
public interface ProcessingStrategy {
    boolean process(int durationMs);
    String getProcessType();
    int getRequiredDuration();
    void reset();
}

// Concrete strategies
public class CuttingStrategy implements ProcessingStrategy {
    public boolean process(int durationMs) {
        // Cutting logic
    }
}

public class CookingStrategy implements ProcessingStrategy {
    public boolean process(int durationMs) {
        // Cooking logic
    }
}
```
**Files:**
- `/util/ProcessingStrategy.java` - Strategy interface
- `/util/CuttingStrategy.java` - Cutting strategy
- `/util/CookingStrategy.java` - Cooking strategy

#### **Pattern 6: Builder** ✅
```java
public class DishBuilder {
    private Plate plate;
    
    public DishBuilder addBun() { return this; }
    public DishBuilder addMeat() { return this; }
    public DishBuilder addCheese() { return this; }
    public DishBuilder addLettuce() { return this; }
    public DishBuilder addTomato() { return this; }
    public Plate build() { return plate; }
}

// Usage:
Plate dish = new DishBuilder(plate)
    .addBun()
    .addMeat()
    .addCheese()
    .build();
```
**File:** `/util/DishBuilder.java`

#### **Pattern 7: Template Method**
```java
public abstract class Station extends Entity {
    // Template method
    public abstract void interact(Chef player);
    
    // Common implementations
    public Item takeItem() { ... }
    public boolean placeItem(Item item) { ... }
}
```
**File:** `/entity/station/Station.java`

---

### 9. **SOLID Principles (Minimum 3)** ✅

**All 5 Implemented - See `SOLID_PRINCIPLES.md`**

1. **S**ingle Responsibility - OrderManager, UI, CollisionChecker
2. **O**pen/Closed - Station hierarchy, Item hierarchy
3. **L**iskov Substitution - Station/Preparable substitutability
4. **I**nterface Segregation - Preparable, CookingDevice, Command
5. **D**ependency Inversion - List<Preparable>, Command pattern

**File:** `/SOLID_PRINCIPLES.md`

---

### 10. **Build Tool** ✅

**Gradle:**
- `build.gradle.kts` - Kotlin DSL configuration
- `settings.gradle.kts` - Project settings
- `gradlew` / `gradlew.bat` - Gradle wrapper

**Commands:**
```bash
./gradlew build
./gradlew run
./gradlew clean
```

---

## 📊 Summary Score

| Requirement | Status | Score |
|------------|--------|-------|
| Inheritance | ✅ Complete | 100% |
| Abstract/Interface | ✅ Complete | 100% |
| Polymorphism | ✅ Complete | 100% |
| **Generics** | ✅ Complete | 100% |
| **Exceptions** | ✅ Complete | 100% |
| Collections | ✅ Complete | 100% |
| **Concurrency** | ✅ Complete | 100% |
| Design Patterns (2) | ✅ **7 Patterns** | 100% |
| SOLID (3) | ✅ 5 Principles | 100% |
| Build Tool | ✅ Gradle | 100% |

**TOTAL: 10/10 Requirements ✅**

**Design Patterns Implemented:**
1. ✅ Singleton (OrderManager)
2. ✅ Command (8 command classes)
3. ✅ Factory Method (IngredientStorage)
4. ✅ **Observer** (GameObserver/GameSubject)
5. ✅ **Strategy** (ProcessingStrategy)
6. ✅ **Builder** (DishBuilder)
7. ✅ Template Method (Station abstract class)

---

## 🎮 Game Features Checklist

### Entitas:
- ✅ 2 Chef (switchable)
- ✅ Item hierarchy
- ✅ Ingredient states (5 states)
- ✅ Dish assembly
- ✅ Kitchen Utensils (Plate, FryingPan)
- ✅ 8 Station types
- ✅ Order system

### Gameplay:
- ✅ WASD Movement + Collision
- ✅ Pick Up / Drop
- ✅ Cutting (3s BUSY)
- ✅ Washing (3s BUSY)
- ✅ Cooking (12s COOKED, 24s BURNED - FREE)
- ✅ Serving + Validation
- ✅ Switch Chef (B key)

### Menu:
- ✅ Main Menu
- ✅ Stage Select
- ✅ Gameplay (3 min timer)
- ✅ Win/Lose Screen

### Map C (Burger):
- ✅ 4 Recipes (Classic, Cheese, BLT, Deluxe)
- ✅ 5 Ingredients (Bun, Meat, Cheese, Lettuce, Tomato)
- ✅ Cooking tools (FryingPan, Plate)

---

## 📁 Key Files Added

### Generics:
- `/util/GenericStorage.java` - Custom generic class

### Exceptions:
- `/exceptions/GameException.java`
- `/exceptions/InvalidStationInteractionException.java`
- `/exceptions/IngredientProcessingException.java`
- `/exceptions/OrderValidationException.java`

### Concurrency:
- `/util/CuttingTask.java` - Thread for cutting
- `/util/CookingTask.java` - Thread for cooking

### Documentation:
- `SOLID_PRINCIPLES.md` - SOLID implementation details
- `OOP_CHECKLIST.md` - This file

---

**Project Status: 100% Complete** ✅

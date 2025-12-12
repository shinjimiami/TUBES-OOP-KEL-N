package nimonscooked.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generic Storage Container class demonstrating GENERICS implementation
 * Type parameter T represents the type of items stored
 * 
 * @param <T> The type of items this container can hold
 */
public class GenericStorage<T> {
    private final List<T> items;
    private final int maxCapacity;
    private final String storageName;

    /**
     * Constructor for unlimited capacity storage
     */
    public GenericStorage(String name) {
        this(name, Integer.MAX_VALUE);
    }

    /**
     * Constructor with capacity limit
     */
    public GenericStorage(String name, int maxCapacity) {
        this.storageName = name;
        this.maxCapacity = maxCapacity;
        this.items = new ArrayList<>();
    }

    /**
     * Add item to storage
     * 
     * @return true if added successfully, false if storage is full
     */
    public boolean addItem(T item) {
        if (items.size() >= maxCapacity) {
            return false;
        }
        return items.add(item);
    }

    /**
     * Remove and return the first item
     */
    public Optional<T> removeItem() {
        if (items.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(items.remove(0));
    }

    /**
     * Get item at specific index without removing
     */
    public Optional<T> getItem(int index) {
        if (index < 0 || index >= items.size()) {
            return Optional.empty();
        }
        return Optional.of(items.get(index));
    }

    /**
     * Check if storage contains specific item
     */
    public boolean contains(T item) {
        return items.contains(item);
    }

    /**
     * Get current number of items
     */
    public int getCurrentSize() {
        return items.size();
    }

    /**
     * Check if storage is full
     */
    public boolean isFull() {
        return items.size() >= maxCapacity;
    }

    /**
     * Check if storage is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Clear all items
     */
    public void clear() {
        items.clear();
    }

    /**
     * Get all items (unmodifiable view)
     */
    public List<T> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Get storage name
     */
    public String getStorageName() {
        return storageName;
    }

    /**
     * Get max capacity
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public String toString() {
        return String.format("%s [%d/%d items]", storageName, items.size(),
                maxCapacity == Integer.MAX_VALUE ? "∞" : maxCapacity);
    }
}

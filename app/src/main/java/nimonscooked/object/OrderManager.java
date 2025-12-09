package nimonscooked.object;

import nimonscooked.entity.item.dish.Dish;

// placeholder doang, harusnya athar dah bikin
public class OrderManager {
    private static final OrderManager INSTANCE = new OrderManager();

    private OrderManager() {}

    public static OrderManager getInstance() {
        return INSTANCE;
    }

    public boolean submitOrder(Dish dish) {
        return true;
    }
}

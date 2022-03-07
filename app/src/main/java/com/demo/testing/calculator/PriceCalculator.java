package com.demo.testing.calculator;

import androidx.annotation.NonNull;

import java.util.List;

public class PriceCalculator {
    private Timer timer;

    public PriceCalculator(MetricFactory factory) {
        timer = factory.createTimer("PriceCalculator", "calculateTotalPrice");
    }

    public double calculateTotalPrice(int userId, Store store) {
        TimerContext timerContext = timer.time();
        try {
            return calcTotalPrice(userId, store);
        } finally {
            timerContext.stop();
        }
    }

    //todo : 5line 안으로 줄이기
    private double calcTotalPrice(int userId, Store store) {

        // Static method이므로 가짜 객체를 injection 할 수 없다.
        // Hard coding 되어 있으므로 Unit Test할 방법이 없다.
        // 그러므로 getUser를 protected method로 분리해 준다.
        User user = getUser(userId);

        if(user == null) {
            throw new UserNotFoundException();
        }

        List<Integer> itemIds = user.getItemCodes();
        double total = 0;
        double basePrice = 0;
        for (int skuCode : itemIds) {
            Item sku = store.getItemDetails(skuCode);

            basePrice = sku.getPrice();
            total += basePrice - (basePrice * sku.getApplicableDiscount()) / 100;
        }

        if (store.hasAsVipUser(user))
            total -= (total * store.getVipDiscountPercentage()) / 100;

        return total;
    }

    @NonNull
    protected User getUser(int userId) {
        return UserService.getUser(userId);
    }

}
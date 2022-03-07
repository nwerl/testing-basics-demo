package com.demo.testing.calculator;

import androidx.annotation.NonNull;

import java.util.List;

public class PriceCalculator2 {
    private Timer timer;

    public PriceCalculator2(MetricFactory factory) {
        timer = factory.createTimer("PriceCalculator", "calculateTotalPrice");
    }

    public double calculateTotalPrice(int userId, Store store) {
        TimerContext timerContext = timer.time();
        try {
            double total = 0;
            boolean isVipUser = false;

            // Static method이므로 가짜 객체를 injection 할 수 없다.
            // Hard coding 되어 있으므로 Unit Test할 방법이 없다.
            // 그러므로 getUser를 protected method로 분리해 준다.
            User user = getUser(userId);

            if (user != null) {
                for (Integer vipId : store.getVips()) {
                    if (vipId == user.getUserId()) {
                        isVipUser = true;
                        break;
                    }
                }
                List<Integer> itemIds = user.getItemCodes();

                double basePrice = 0;
                for (int skuCode : itemIds) {
                    Item sku = store.getItemDetails(skuCode);

                    basePrice = sku.getPrice();
                    total += basePrice - (basePrice * sku.getApplicableDiscount()) / 100;
                }

                if (isVipUser)
                    total -= (total * store.getVipDiscountPercentage()) / 100;

                return total;
            } else {
                throw new UserNotFoundException();
            }
        } finally {
            timerContext.stop();
        }
    }

    @NonNull
    protected User getUser(int userId) {
        return UserService.getUser(userId);
    }

}
package com.demo.testing.calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store {
    private final List<Integer> vipUsers = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    private double vipDiscountPercentage;

    public void addVip(User user) {
        vipUsers.add(user.getUserId());
    }

    public List<Integer> getVips() {
        return Collections.unmodifiableList(vipUsers);
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItemDetails(int skuCode) {
        for (Item item : items) {
            if (item.getSkuCode() == skuCode)
                return item;
        }
        return null;
    }

    public double getVipDiscountPercentage() {
        return vipDiscountPercentage;
    }

    public void setVipDiscountPercentage(double vipDiscountPercentage) {
        this.vipDiscountPercentage = vipDiscountPercentage;
    }

    //todo : one-liner
    public boolean hasAsVipUser(User user) {
        boolean isVipUser = false;
        for (Integer vipId : getVips()) {
            if (vipId == user.getUserId()) {
                isVipUser = true;
                break;
            }
        }
        return isVipUser;
    }
}

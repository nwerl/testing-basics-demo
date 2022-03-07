package com.demo.testing.calculator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PriceCalculatorTest {
    public static final int NON_REGISTERED_USER_ID = -1;
    public static final Store UNUSED_STORE = null;
    public static final User GUEST = null;
    public static final User ANOTHER_VIP_USER = new User(101);
    public PriceCalculator priceCalculator;

    private MockTimer mockTimer;
    private MockTimerContext mockTimerContext;
    private MockMetricFactory mockMetricFactory;
    private User returnedUser;
    private Store store;

    Item TV = new Item(1, 100d, 1, 10d);
    Item OVEN = new Item(2, 50d, 1, 5d);

    @Before
    public void setUp() {
        mockMetricFactory = new MockMetricFactory();
        mockTimer = new MockTimer();
        mockTimerContext = new MockTimerContext();
        store = getStore();

        priceCalculator = new MyPriceCalculator(mockMetricFactory);
    }

    @After
    public void tearDown() {
        assertThat(mockTimerContext.isStopCalled, is(true));
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_if_user_is_not_registered_user() {
        // Arrange (Given)
        returnedUser = GUEST;

        // Act (When)
        priceCalculator.calculateTotalPrice(NON_REGISTERED_USER_ID, UNUSED_STORE);
    }

    @Test
    public void should_return_total_with_only_regular_and_vip_discounts_applied() {
        // Arrange (Given)
        store.setVipDiscountPercentage(10d);

        User user = new User(1);
        user.addItemCode(TV.getSkuCode());
        user.addItemCode(OVEN.getSkuCode());

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(user.getUserId(), store);

        assertThat(total, is(closeTo(137.5d, 0.1)));
    }

    @Test
    public void should_return_total_with_both_regular_and_vip_discounts_applied() {
        // Arrange (Given)
        store.setVipDiscountPercentage(10d);
        store.addVip(ANOTHER_VIP_USER);

        User user = new User(1);
        user.addItemCode(TV.getSkuCode());
        user.addItemCode(OVEN.getSkuCode());

        returnedUser = user;

        // Act (When)
        double total = priceCalculator.calculateTotalPrice(user.getUserId(), store);

        assertThat(total, is(closeTo(137.5d, 0.1)));
    }

    @NonNull
    private Store getStore() {
        store = new Store();
        store.addItem(TV);
        store.addItem(OVEN);
        return store;
    }

    private class MockMetricFactory extends MetricFactory{
        @Override
        public Timer createTimer(String moduleName, String methodName) {
            return mockTimer;
        }
    }

    private class MockTimer extends Timer{
        @Override
        public TimerContext time() {
            return mockTimerContext;
        }
    }

    private class MockTimerContext extends TimerContext{
        boolean isStopCalled = false;

        @Override
        public void stop() {
            isStopCalled = true;
        }
    }

    private class MyPriceCalculator extends PriceCalculator {



        public MyPriceCalculator(MetricFactory factory) {
            super(factory);
        }

        @NonNull
        @Override
        protected User getUser(int userId) {
            return returnedUser;
        }
    }
}
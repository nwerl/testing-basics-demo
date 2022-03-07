package com.demo.testing.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsDivisibleBy extends TypeSafeMatcher<Integer> {
    private final int divisor;

    public IsDivisibleBy(int divisor) {
        this.divisor = divisor;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("divisible by " + divisor);
    }

    @Override
    protected boolean matchesSafely(Integer item) {
        return item%divisor == 0;
    }

    //Public API
    public static Matcher<Integer> divisibleBy(int divisor) {
        return new IsDivisibleBy(divisor);
    }
}

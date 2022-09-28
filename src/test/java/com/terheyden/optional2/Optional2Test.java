package com.terheyden.optional2;

import javax.annotation.Nullable;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * Optional2Test unit tests.
 */
public class Optional2Test {

    @Test
    public void tutorial() {

        // Common functional scenario — step1 leads to step2, etc.
        User1 user1 = Optional
            .of(lookupName("Cora"))
            .map(User1::new)
            .orElseThrow();

        // Let's say you need two values to continue... suddenly you can't use FP.
        // Optional2 aims to fill that gap.
        User2 user2 = Optional2
            .of(lookupName("Cora"))
            .andOf(name -> calculateAge(name))
            .reduce((name, age) -> new User2(name, age)) // Strong names, strong types ...
            .get();

        // Just like Optional, if any value is missing, nothing happens.
        User2 nullUser = Optional2
            .ofNullable(lookupName(null))
            .andOf(name -> calculateAge(name))              // Not evaluated.
            .reduce((name, age) -> new User2(name, age)) // Not evaluated.
            .orElse(null);

        // You can take action if any value is missing.
        Optional2
            .ofNullable(lookupName(null))
            .updateIfEmpty("Cora");
    }

    @Nullable
    private static String lookupName(@Nullable String nameToReturn) {
        return nameToReturn;
    }

    @Nullable
    private static Integer getUserAge(@Nullable Integer ageToReturn) {
        return ageToReturn;
    }

    @Nullable
    private static Integer calculateAge(@Nullable String userName) {
        return userName == null ? null : userName.length();
    }

    /**
     * Simple user obj for test.
     */
    private record User1(String name) { }

    /**
     * Simple user obj for test.
     */
    private record User2(String name, int age) { }
}

package com.terheyden.optional2;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * A dummy test service.
 */
public final class TestService {

    public static final String NULL = null;

    private TestService() {
        // Private since this class shouldn't be instantiated.
    }

    @Nullable
    public static UUID findUserId(@Nullable String userName) {
        return userName == null ? null : UUID.randomUUID();
    }

    public static UUID throwUserId(@Nullable String userName) {
        throw new IllegalStateException("IGNORE!");
    }

    @Nullable
    public static TestUser loginUser(@Nullable UUID userId, @Nullable String userName) {

        if (userId == null || userName == null) {
            return null;
        }

        return new TestUser(userId, userName);
    }

    public static TestUser throwLoginUser(@Nullable UUID userId, @Nullable String userName) {
        throw new IllegalStateException("IGNORE!");
    }
}

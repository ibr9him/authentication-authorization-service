package com.estore.authenticationauthorizationservice.util;

/**
 * Application constants.
 */

public final class Constants {

    // Regex for acceptable username
    public static final String USERNAME = "^[_.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final CharSequence SYSTEM_ADMIN = "SYSTEM_ADMIN";

    private Constants() {
    }
}

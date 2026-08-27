package com.rtravez.msa.util;

/**
 * Class utility for call web service.
 *
 * @author egudino
 * @version 2019-09-09
 */
public final class EnvironmentUtil {

    public static final String DOMAIN_LOCAL_HOST = "http://localhost:8081";
    // public static final String DOMAIN_LOCAL_HOST = "http://msc-service:8081";

    private EnvironmentUtil() {
    }

    /**
     * Functionality that builds the service url that is sent as a parameter.
     *
     * @param context work environment parameter
     * @return The context name
     */
    public static String getDomainNameContext(String context) {
        return DOMAIN_LOCAL_HOST + context;
    }
}

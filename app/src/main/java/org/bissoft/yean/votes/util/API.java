package org.bissoft.yean.votes.util;

/**
 * Created by Natalia on 18.03.2016.
 */
public class API {
    private static final String K_BASE_URL  = "http://votes-test.dev.gns-it.com";
    private static final String YEAN_BASE_URL  = "http://api-yean.bissoft.org:80/yean/v1";

    private static final String API_LOGIN  = "/api/login";

    private static final String API_INFO  = "/info?code=site43";

    public static String login() {
        return K_BASE_URL + API_LOGIN;
    }
    public static String info() {
        return YEAN_BASE_URL + API_INFO;
    }
}
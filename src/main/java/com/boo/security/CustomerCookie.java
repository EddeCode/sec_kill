package com.boo.security;

import javax.servlet.http.Cookie;

/**
 * @author song
 * @date 2022/4/18 16:08
 */
public class CustomerCookie  extends Cookie {

    public CustomerCookie(String name, String value) {
        super(name, value);
    }
}

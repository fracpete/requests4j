/*
 * CookieParser.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for extracting cookies from HTTP response headers.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class CookieParser {

  public static final String SET_COOKIE = "Set-Cookie";

  /**
   * Extracts the cookies from the headers.
   *
   * @param headers 	the headers to parse
   * @return 		the cookies (if any)
   */
  public static Map<String,String> parse(Map<String,List<String>> headers) {
    Map<String,String> 	result;
    List<String> 	cookies;
    String		name;
    String		value;

    result  = new HashMap<>();
    cookies = headers.get(SET_COOKIE);
    if (cookies == null)
      cookies = headers.get(SET_COOKIE.toLowerCase());
    if (cookies != null) {
      for (String cookie: cookies) {
        // remove expiry etc
        if (cookie.contains(";"))
	  cookie = cookie.substring(0, cookie.indexOf(";"));
        if (cookie.contains("=")) {
          name  = cookie.substring(0, cookie.indexOf("="));
          value = cookie.substring(cookie.indexOf("=") + 1);
          result.put(name, value);
	}
      }
    }

    return result;
  }

}

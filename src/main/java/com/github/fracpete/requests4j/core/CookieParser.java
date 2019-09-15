/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

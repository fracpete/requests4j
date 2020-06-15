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
 * URLBuilder.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.request;

import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for constructing a URL for GET requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class URLBuilder
  implements Serializable {

  public static final String ENCODING_UTF8 = "UTF-8";

  /** the (optional) base URL. */
  protected URL m_URL;

  /** the parts that make up the URL. */
  protected List<String[]> m_Parts;

  /**
   * Initializes the builder without URL.
   */
  public URLBuilder() {
    this(null);
  }

  /**
   * Initializes the builder.
   *
   * @param url		the URL to use, can be null
   */
  public URLBuilder(URL url) {
    m_URL   = url;
    m_Parts = new ArrayList();
  }

  /**
   * Returns the base URL, if any.
   *
   * @return		the URL, null if none set
   */
  public URL url() {
    return m_URL;
  }

  /**
   * Appends the key-value pair.
   *
   * @param key		the key
   * @param value	the value
   * @return		itself
   */
  public URLBuilder append(String key, String value) {
    m_Parts.add(new String[]{key, value});
    return this;
  }

  /**
   * Appends the key with multiple values.
   *
   * @param key		the key
   * @param values	the values
   * @return		itself
   */
  public URLBuilder append(String key, String[] values) {
    for (String value: values)
      m_Parts.add(new String[]{key, value});
    return this;
  }

  /**
   * Appends the key with multiple values.
   *
   * @param key		the key
   * @param values	the values
   * @return		itself
   */
  public URLBuilder append(String key, Collection values) {
    for (Object value: values)
      m_Parts.add(new String[]{key, "" + value});
    return this;
  }

  /**
   * Appends the map of key-value pairs.
   *
   * @param pairs	the key-value pairs, the keys get interpreted as strings
   *                    and the values can be String/String[]/java.util.List
   * @return		itself
   */
  public URLBuilder append(Map pairs) {
    Object	value;

    for (Object key: pairs.keySet()) {
      value = pairs.get(key);
      if (value instanceof String)
	append("" + key, (String) value);
      else if (value instanceof String[])
	append("" + key, (String[]) value);
      else if (value instanceof Collection)
	append("" + key, (Collection) value);
      else
        throw new IllegalArgumentException("Unhandled value type: " + value.getClass());
    }
    return this;
  }

  /**
   * Builds the URL using UTF-8 as encoding.
   *
   * @return		the URL
   * @throws Exception	if assembling of URL fails
   */
  public String build() throws Exception {
    return build(ENCODING_UTF8);
  }

  /**
   * Builds the URL.
   *
   * @param enc 	the encoding to use
   * @return		the URL
   * @throws Exception	if assembling of URL fails
   */
  public String build(String enc) throws Exception {
    StringBuilder	result;
    int			i;
    String[]		part;

    result = new StringBuilder();

    if (m_URL != null)
      result.append(m_URL.toString());

    for (i = 0; i < m_Parts.size(); i++) {
      if (i == 0)
	result.append("?");
      else
	result.append("&");
      part = m_Parts.get(i);
      result.append(URLEncoder.encode(part[0], enc));
      result.append("=");
      result.append(URLEncoder.encode(part[1], enc));
    }

    return result.toString();
  }

  /**
   * For testing only.
   *
   * @param args	ignored
   * @throws Exception	if test fails
   */
  public static void main(String[] args) throws Exception {
    Map pairs = new HashMap();
    pairs.put("m", "m1");
    URL url = new URL("http", "somehost.com", "/api");
    URLBuilder builder = new URLBuilder(url)
      .append("a", "a1")
      .append("b", new String[]{"b1", "b2", "b3"})
      .append("c", Arrays.asList("c1", "c2"))
      .append(pairs);
    System.out.println(builder.build());
  }
}

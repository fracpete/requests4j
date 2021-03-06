/*
 * Response.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.response;

import java.util.List;
import java.util.Map;

/**
 * Interface for Response classes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface Response {

  /**
   * Initializes the response object.
   *
   * @param response	the response
   */
  public void init(okhttp3.Response response);

  /**
   * Returns the underlying, raw response.
   *
   * @return		the response
   */
  public okhttp3.Response rawResponse();

  /**
   * Returns the HTTP status code.
   *
   * @return		the code
   */
  public int statusCode();

  /**
   * Returns the HTTP status message.
   *
   * @return		the message
   */
  public String statusMessage();

  /**
   * Returns the response headers.
   *
   * @return		the headers
   */
  public Map<String,List<String>> headers();

  /**
   * Whether the request has a code of less than 400.
   *
   * @return		true if <400
   */
  public boolean ok();
}

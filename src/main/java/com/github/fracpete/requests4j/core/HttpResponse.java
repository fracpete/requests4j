/*
 * HttpResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Interface for Response classes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface HttpResponse {

  /**
   * Initializes the response object.
   *
   * @param statusCode		the HTTP status code
   * @param statusMessage	the HTTP status message
   * @param headers		the received headers
   */
  public void init(int statusCode, String statusMessage, Map<String,List<String>> headers);

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
   * Returns the headers.
   *
   * @return		the headers
   */
  public Map<String,List<String>> headers();

  /**
   * Returns the received cookies ("Set-Cookie").
   *
   * @return		the cookies
   */
  public Map<String,String> cookies();

  /**
   * Whether the request has a code of less than 400.
   *
   * @return		true if <400
   */
  public boolean ok();

  /**
   * Appends the byte read from the response.
   *
   * @param b		the byte to append
   * @throws IOException	if appending failed
   */
  public void appendBody(byte b) throws IOException;

  /**
   * Called when all data from the response has been processed.
   *
   * @throws IOException	if finishing up fails
   */
  public void finishBody() throws IOException;
}

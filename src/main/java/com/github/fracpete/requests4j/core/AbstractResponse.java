/*
 * AbstractResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ancestor for HTTP responses.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractResponse
  implements Serializable, HttpResponse {

  /** the status code. */
  protected int m_StatusCode;

  /** the status message. */
  protected String m_StatusMessage;

  /** the headers. */
  protected Map<String,List<String>> m_Headers;

  /** the cookies. */
  protected Cookies m_Cookies;

  /**
   * Initializes the response.
   */
  public AbstractResponse() {
    m_StatusCode    = 200;
    m_StatusMessage = "";
    m_Headers       = new HashMap<>();
    m_Cookies       = new Cookies();
  }

  /**
   * Initializes the response object.
   *
   * @param statusCode		the HTTP status code
   * @param statusMessage	the HTTP status message
   * @param headers		the received headers
   */
  public void init(int statusCode, String statusMessage, Map<String,List<String>> headers) {
    if (statusMessage == null)
      statusMessage = "";

    m_StatusCode    = statusCode;
    m_StatusMessage = statusMessage;
    m_Headers       = new HashMap<>(headers);
    m_Cookies       = Cookies.parse(m_Headers);
  }

  /**
   * Returns the HTTP status code.
   *
   * @return		the code
   */
  public int statusCode() {
    return m_StatusCode;
  }

  /**
   * Returns the HTTP status message.
   *
   * @return		the message
   */
  public String statusMessage() {
    return m_StatusMessage;
  }

  /**
   * Returns the headers.
   *
   * @return		the headers
   */
  public Map<String,List<String>> headers() {
    return m_Headers;
  }

  /**
   * Returns the received cookies ("Set-Cookie").
   *
   * @return		the cookies
   */
  public Cookies cookies() {
    return m_Cookies;
  }

  /**
   * Whether the request has a code of less than 400.
   *
   * @return		true if <400
   */
  public boolean ok() {
    return (statusCode() < 400);
  }

  /**
   * Outputs a short description of the response.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "status code: " + statusCode() + ", status message: " + statusMessage() + ", headers: " + headers();
  }
}

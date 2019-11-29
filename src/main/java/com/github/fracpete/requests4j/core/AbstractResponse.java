/*
 * AbstractResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ancestor for Response classes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AbstractResponse
  implements Serializable, Response {

  /** the raw response. */
  protected CloseableHttpResponse m_RawResponse;

  /** the status code. */
  protected int m_StatusCode;

  /** the status message. */
  protected String m_StatusMessage;

  /** the headers. */
  protected Map<String,List<String>> m_Headers;

  /**
   * Initializes the response.
   */
  public AbstractResponse() {
    m_RawResponse   = null;
    m_StatusCode    = 200;
    m_StatusMessage = "";
    m_Headers       = new HashMap<>();
  }

  /**
   * Initializes the response object.
   *
   * @param response		the response
   */
  @Override
  public void init(CloseableHttpResponse response) {
    m_RawResponse   = response;
    m_StatusCode    = response.getStatusLine().getStatusCode();
    m_StatusMessage = response.getStatusLine().getReasonPhrase();
    m_Headers.clear();
    for (Header header: response.getAllHeaders()) {
      if (!m_Headers.containsKey(header.getName()))
        m_Headers.put(header.getName(), new ArrayList<>());
      m_Headers.get(header.getName()).add(header.getValue());
    }
  }

  /**
   * Returns the underlying, raw response.
   *
   * @return		the response
   */
  @Override
  public CloseableHttpResponse rawResponse() {
    return m_RawResponse;
  }

  /**
   * Returns the response headers.
   *
   * @return		the headers
   */
  public Map<String,List<String>> headers() {
    return m_Headers;
  }

  /**
   * Returns the HTTP status code.
   *
   * @return		the code
   */
  @Override
  public int statusCode() {
    return m_StatusCode;
  }

  /**
   * Returns the HTTP status message.
   *
   * @return		the message
   */
  @Override
  public String statusMessage() {
    return m_StatusMessage;
  }

  /**
   * Whether the request has a code of less than 400.
   *
   * @return		true if <400
   */
  @Override
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
    return "status code: " + statusCode() + ", status message: " + statusMessage();
  }
}

/*
 * Method.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.request;

/**
 * The types of HTTP methods.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public enum Method {
  GET(false),
  POST(true),
  PUT(true),
  PATCH(true),
  HEAD(false),
  DELETE(false),
  OPTIONS(false);

  /** whether it supports a body. */
  private boolean m_Body;

  /**
   * Initializes the enum.
   *
   * @param body	whether a body is supported
   */
  private Method(boolean body) {
    m_Body = body;
  }

  /**
   * Returns whether a body is supported.
   *
   * @return		true if supported
   */
  public boolean hasBody() {
    return m_Body;
  }
}

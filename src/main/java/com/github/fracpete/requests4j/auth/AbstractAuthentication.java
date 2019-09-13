/*
 * AbstractAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import com.github.fracpete.requests4j.core.Request;

import java.io.Serializable;

/**
 * Ancestor for authentication schemes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAuthentication
  implements Serializable {

  /**
   * Updates the request to include authentication.
   *
   * @param request	the request to update
   * @throws Exception  if updating fails
   */
  public abstract void apply(Request request) throws Exception;
}

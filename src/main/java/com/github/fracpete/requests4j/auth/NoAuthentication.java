/*
 * NoAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import com.github.fracpete.requests4j.core.Request;

/**
 * Dummy, does nothing.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NoAuthentication
  extends AbstractAuthentication {

  /**
   * Updates the request to include authentication.
   *
   * @param request	the request to update
   * @throws Exception  if updating fails
   */
  @Override
  public void apply(Request request) throws Exception {
  }
}

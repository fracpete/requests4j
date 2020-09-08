/*
 * NoAuthentication.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import okhttp3.Authenticator;

/**
 * Dummy, does nothing.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NoAuthentication
  extends AbstractAuthentication {

  /**
   * Generates the authenticator for the client.
   *
   * @return 		the generated authenticator
   * @throws Exception  if generation fails
   */
  public Authenticator build() throws Exception {
    return null;
  }
}

/*
 * AbstractAuthentication.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import okhttp3.Authenticator;

import java.io.Serializable;

/**
 * Ancestor for authentication schemes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAuthentication
  implements Serializable {

  /**
   * Generates the authenticator for the client.
   *
   * @return 		the generated authenticator
   * @throws Exception  if generation fails
   */
  public abstract Authenticator build() throws Exception;
}

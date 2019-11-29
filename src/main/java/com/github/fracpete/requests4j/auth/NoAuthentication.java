/*
 * NoAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import com.github.fracpete.requests4j.core.Request;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * Dummy, does nothing.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NoAuthentication
  extends AbstractAuthentication {

  /**
   * Generates the context for the client.
   *
   * @param request	the request to update
   * @return 		the generated context
   * @throws Exception  if context generation fails
   */
  public HttpClientContext build(Request request) throws Exception {
    return null;
  }
}

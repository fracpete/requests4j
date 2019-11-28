/*
 * AbstractAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.v2.auth;

import com.github.fracpete.requests4j.v2.core.Request;
import org.apache.http.client.protocol.HttpClientContext;

import java.io.Serializable;

/**
 * Ancestor for authentication schemes.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAuthentication
  implements Serializable {

  /**
   * Generates the context for the client.
   *
   * @param request	the request to update
   * @return 		the generated context
   * @throws Exception  if context generation fails
   */
  public abstract HttpClientContext build(Request request) throws Exception;
}

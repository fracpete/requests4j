/*
 * NoHostnameVerification.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.Serializable;

/**
 * For turning off hostname verification.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class NoHostnameVerification
  implements Serializable, HostnameVerifier {

  /**
   * Verify that the host name is an acceptable match with
   * the server's authentication scheme.
   *
   * @param hostname the host name
   * @param session SSLSession used on the connection to host
   * @return true if the host name is acceptable (always the case)
   */
  @Override
  public boolean verify(String hostname, SSLSession session) {
    return true;
  }
}

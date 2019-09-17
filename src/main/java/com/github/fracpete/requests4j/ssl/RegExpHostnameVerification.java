/*
 * RegExpHostnameVerification.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.Serializable;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Only hostnames that match the provided regular expression succeed.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RegExpHostnameVerification
  implements Serializable, HostnameVerifier {

  /** the regexp expression to match the hostnames against. */
  protected String m_RegExp;

  /** the compiled pattern. */
  protected transient Pattern m_Pattern;

  /**
   * Initializes the verification scheme with the regular expression to
   * match the hostnames against. Matching hostnames will succeed.
   *
   * @param regExp			the regular expression to use
   * @throws PatternSyntaxException	if regexp is invalid
   */
  public RegExpHostnameVerification(String regExp) throws PatternSyntaxException {
    m_RegExp  = regExp;
    m_Pattern = Pattern.compile(m_RegExp);
  }

  /**
   * Returns the regular expression in use.
   *
   * @return		the expression
   */
  public String regExp() {
    return m_RegExp;
  }

  /**
   * Verify that the host name is an acceptable match with
   * the server's authentication scheme.
   *
   * @param hostname the host name
   * @param session SSLSession used on the connection to host
   * @return true if the host name is acceptable (ie matches the regexp)
   */
  @Override
  public boolean verify(String hostname, SSLSession session) {
    if (m_Pattern == null)
      m_Pattern = Pattern.compile(m_RegExp);
    return m_Pattern.matcher(hostname).matches();
  }
}

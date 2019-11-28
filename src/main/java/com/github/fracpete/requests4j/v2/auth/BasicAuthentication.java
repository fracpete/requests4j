/*
 * BasicAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.v2.auth;

import com.github.fracpete.requests4j.v2.core.Request;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;

/**
 * Updates the headers with basic authorization.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class BasicAuthentication
  extends AbstractAuthentication {

  /** the user to use. */
  protected String m_User;

  /** the password to use. */
  protected String m_Password;

  /**
   * Initializes the authentication.
   *
   * @param user	the user
   * @param password	the password
   */
  public BasicAuthentication(String user, String password) {
    m_User     = user;
    m_Password = password;
  }

  /**
   * Generates the context for the client.
   *
   * @param request	the request to update
   * @return 		the generated context
   * @throws Exception  if context generation fails
   */
  public HttpClientContext build(Request request) throws Exception {
    HttpClientContext 	result;
    HttpHost  		host;
    AuthCache 		authCache;
    BasicScheme 	basicAuth;

    host = HttpHost.create(request.url().getHost());
    request.credentials().setCredentials(
      new AuthScope(host.getHostName(), host.getPort()),
      new UsernamePasswordCredentials(m_User, m_Password));
    authCache = new BasicAuthCache();
    basicAuth = new BasicScheme();
    authCache.put(host, basicAuth);
    result = HttpClientContext.create();
    result.setAuthCache(authCache);

    return result;
  }
}

/*
 * BasicAuthentication.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Response;
import okhttp3.Route;

import java.io.IOException;

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
   * Generates the authenticator for the client.
   *
   * @return 		the generated authenticator
   * @throws Exception  if generation fails
   */
  public Authenticator build() throws Exception {
    return new Authenticator() {
        @Override
        public okhttp3.Request authenticate(Route route, Response response) throws IOException {
          String credential = Credentials.basic(m_User, m_Password);
          return response.request().newBuilder().header("Authorization", credential).build();
        }
    };
  }
}

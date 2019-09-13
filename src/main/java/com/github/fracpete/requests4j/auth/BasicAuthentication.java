/*
 * BasicAuthentication.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.auth;

import com.github.fracpete.requests4j.core.Request;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;

import java.util.Base64;

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
   * Generates the authentication string.
   *
   * @return		the generated string
   * @throws Exception	if decoding of user/password as latin1 fails
   */
  protected String generate() throws Exception {
    TByteList buffer;

    buffer = new TByteArrayList();
    buffer.addAll(m_User.getBytes("latin1"));
    buffer.addAll(":".getBytes("latin1"));
    buffer.addAll(m_Password.getBytes("latin1"));
    return "Basic " + new String(Base64.getEncoder().encode(buffer.toArray()));
  }

  /**
   * Updates the request to include authentication.
   *
   * @param request	the request to update
   * @throws Exception  if updating fails
   */
  @Override
  public void apply(Request request) throws Exception {
    request.header("Authorization", generate());
  }
}

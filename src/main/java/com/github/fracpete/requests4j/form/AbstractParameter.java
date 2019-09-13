/*
 * AbstractParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Ancestor for Files parameters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractParameter {

  /** the parameter name. */
  public String m_Name;

  /**
   * Initializes the parameter.
   *
   * @param name	the name
   */
  protected AbstractParameter(String name) {
    m_Name = name;
  }

  /**
   * Returns the name of the parameter.
   *
   * @return		the name
   */
  public String name() {
    return m_Name;
  }

  /**
   * Writes out the parameter.
   *
   * @param conn 	the connection in use
   * @param writer	the writer to use
   * @param boundary 	the boundary to use
   * @throws IOException	if writing fails
   */
  public abstract void post(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException;

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public abstract Map<String,String> parameters();

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return name();
  }
}

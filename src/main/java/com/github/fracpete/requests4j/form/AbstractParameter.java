/*
 * AbstractParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.core.Resendable;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * Ancestor for Files parameters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractParameter
  implements Resendable {

  /** the parameter name. */
  public String m_Name;

  /**
   * Initializes the parameter.
   *
   * @param name	the name
   */
  protected AbstractParameter(String name) {
    if (name == null)
      throw new IllegalStateException("Parameter name cannot be null!");
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
   * Adds the parameter.
   *
   * @param multipart   	the multipart to add the parameter to
   * @throws IOException	if writing fails
   */
  public abstract void add(MultipartEntityBuilder multipart) throws IOException;

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

/*
 * StringParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates string parameters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StringParameter
  extends AbstractParameter {

  /** the parameter value. */
  protected String m_Value;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param value 	the value
   */
  public StringParameter(String name, String value) {
    super(name);
    if (value == null)
      throw new IllegalStateException("String parameter value cannot be null (name=" + name + ")!");
    m_Value = value;
  }

  /**
   * Returns the value.
   *
   * @return		the value
   */
  public String value() {
    return m_Value;
  }

  /**
   * Writes out the parameter.
   *
   * @param conn 	the connection in use
   * @param writer	the writer to use
   * @param boundary 	the boundary to use
   * @throws IOException	if writing fails
   */
  @Override
  public void post(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException {
    writer.write("\r\n");
    writer.write("--" + boundary + "\r\n");
    writer.write("Content-Disposition: form-data; name=\"" + name() + "\"\r\n");
    writer.write("\r\n");
    writer.write(value());
  }

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public Map<String,String> parameters() {
    Map<String,String>  result;

    result = new HashMap<>();
    result.put(name(), value());

    return result;
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return name() + "=" + value();
  }
}

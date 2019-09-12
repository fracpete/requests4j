/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
    writer.write("--" + boundary + "\r\n");
    writer.write("Content-Disposition: form-data; name=\"" + name() + "\"\r\n");
    writer.write("\r\n");
    writer.write(value());
    writer.write("\r\n");
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

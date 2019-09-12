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
 * AbstractParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;

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
  public abstract void write(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException;

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

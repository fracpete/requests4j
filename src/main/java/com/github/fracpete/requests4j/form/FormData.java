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
 * Files.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * For storing form data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FormData
  extends HashMap<String,AbstractParameter> {

  /**
   * Initializes empty form data.
   */
  public FormData() {
    super();
  }

  /**
   * Initializes the data with the provided form data.
   *
   * @param formData 	the map to initialize with
   */
  public FormData(Map<String,AbstractParameter> formData) {
    super(formData);
  }

  /**
   * Adds the parameter.
   *
   * @param param	the parameter to add
   * @return		itself
   */
  public FormData add(AbstractParameter param) {
    put(param.name(), param);
    return this;
  }

  /**
   * Adds the string form parameter.
   *
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public FormData add(String name, String value) {
    return add(new StringParameter(name, value));
  }

  /**
   * Adds the file form parameter.
   *
   * @param name	the name
   * @param filename	the file to add
   * @return		itself
   */
  public FormData addFile(String name, String filename) throws IOException {
    return add(new StreamParameter(name, filename));
  }

  /**
   * Adds the file form parameter.
   *
   * @param name	the name
   * @param file	the file to add
   * @return		itself
   */
  public FormData addFile(String name, File file) throws IOException {
    return add(new StreamParameter(name, file));
  }

  /**
   * Writes out the parameters.
   *
   * @param conn 	the connection in use
   * @param writer	the writer to use
   * @param boundary 	the boundary to use
   * @throws IOException	if writing fails
   */
  public void write(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException {
    if (size() > 0) {
      writer.write("\n\n");
      for (String key : keySet())
	get(key).write(conn, writer, boundary);
    }
  }
}

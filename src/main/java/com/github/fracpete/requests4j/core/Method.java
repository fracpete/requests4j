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
 * Method.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

/**
 * The types of HTTP methods.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public enum Method {
  GET(false),
  POST(true),
  PUT(true),
  PATCH(true),
  HEAD(false),
  DELETE(false);

  /** whether it supports a body. */
  private boolean m_Body;

  /**
   * Initializes the enum.
   *
   * @param body	whether a body is supported
   */
  private Method(boolean body) {
    m_Body = body;
  }

  /**
   * Returns whether a body is supported.
   *
   * @return		true if supported
   */
  public boolean hasBody() {
    return m_Body;
  }
}

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
 * ReadHtml.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.core.Response;

/**
 * Just reads an HTML page and outputs the HTML on stdout.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ReadHtml {

  public static void main(String[] args) throws Exception {
    Response r = Requests.get("http://github.com/")
      .allowRedirects(true)
      .execute();
    System.out.println("Response: " + r);
    System.out.println("Cookies: " + r.cookies());
    System.out.println(r.text());
  }
}

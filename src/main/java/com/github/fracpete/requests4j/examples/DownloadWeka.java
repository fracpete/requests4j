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
 * DownloadWeka.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.core.Response;

import java.io.File;

/**
 * Downloads a Weka zip file from sourceforge.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DownloadWeka {

  public static void main(String[] args) throws Exception {
    Response r = Requests.get("http://sourceforge.net/projects/weka/files/weka-3-9/3.9.3/weka-3-9-3.zip/download")
      .allowRedirects(true)
      .execute();
    File download = new File(System.getProperty("java.io.tmpdir") + File.separator + "weka-3-9-3.zip");
    System.out.println(r);
    System.out.println("Saving to " + download);
    r.saveBody(download);
  }
}

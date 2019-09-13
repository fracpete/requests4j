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
    System.out.println(r);
    if (r.ok()) {
      File download = new File(System.getProperty("java.io.tmpdir") + File.separator + "weka-3-9-3.zip");
      System.out.println("Saving to " + download);
      r.saveBody(download);
    }
  }
}

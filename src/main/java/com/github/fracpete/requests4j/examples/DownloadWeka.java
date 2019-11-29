/*
 * DownloadWeka.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.FileResponse;

import java.io.File;

/**
 * Downloads a Weka zip file from sourceforge, streams the response straight to
 * the output file.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DownloadWeka {

  public static void main(String[] args) throws Exception {
    FileResponse r = Requests.get("https://sourceforge.net/projects/weka/files/weka-3-9/3.9.3/weka-3-9-3.zip/download")
      .allowRedirects(true)
      .execute(new FileResponse(System.getProperty("java.io.tmpdir") + File.separator + "weka-3-9-3.zip", 1024*1024));
    System.out.println(r);
    if (r.ok())
      System.out.println("Saved to " + r.outputFile());
  }
}

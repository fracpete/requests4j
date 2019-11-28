/*
 * DownloadWekaAsStream.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.v2.examples;

import com.github.fracpete.requests4j.v2.Requests;
import com.github.fracpete.requests4j.v2.core.StreamResponse;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Downloads a Weka zip file from sourceforge, using a StreamResponse.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class DownloadWekaAsStream {

  public static void main(String[] args) throws Exception {
    File outputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "weka-3-9-3.zip");
    FileOutputStream fos = new FileOutputStream(outputFile);
    BufferedOutputStream bos = new BufferedOutputStream(fos, 64*1024);
    StreamResponse r = Requests.get("https://sourceforge.net/projects/weka/files/weka-3-9/3.9.3/weka-3-9-3.zip/download")
      .allowRedirects(true)
      .execute(new StreamResponse(bos));
    System.out.println(r);
    if (r.ok())
      System.out.println("Saved to " + outputFile);
    bos.close();
    fos.close();
  }
}

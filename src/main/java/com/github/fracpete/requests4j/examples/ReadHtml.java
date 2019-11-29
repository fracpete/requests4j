/*
 * ReadHtml.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.BasicResponse;

/**
 * Just reads an HTML page (stores response in memory) and outputs the HTML on stdout.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ReadHtml {

  public static void main(String[] args) throws Exception {
    BasicResponse r = Requests.get("http://github.com/")
      .allowRedirects(true)
      .execute();
    System.out.println("Response: " + r);
    System.out.println(r.headers());
    System.out.println(r.text());
  }
}

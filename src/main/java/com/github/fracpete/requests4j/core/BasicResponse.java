/*
 * BasicResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Basic Response class that stores content in memory.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class BasicResponse
  extends AbstractResponse {

  /** the body content. */
  protected TByteList m_Body;

  /**
   * Initializes the response.
   */
  public BasicResponse() {
    super();
    m_Body = new TByteArrayList();
  }

  /**
   * Initializes the response object.
   *
   * @param response		the HTTP status code
   */
  @Override
  public void init(CloseableHttpResponse response) {
    InputStream	in;
    int		b;

    super.init(response);

    m_Body.clear();
    if (response.getEntity() != null) {
      try {
	in = response.getEntity().getContent();
	while ((b = in.read()) != -1)
	  m_Body.add((byte) b);
      }
      catch (Exception e) {
	System.err.println("Failed to read response body!");
      }
    }
  }

  /**
   * Returns the body.
   *
   * @return		the body
   */
  public byte[] body() {
    return m_Body.toArray();
  }

  /**
   * Returns the body as text (UTF-8).
   *
   * @return		the text
   */
  public String text() throws UnsupportedEncodingException {
    return text("UTF-8");
  }

  /**
   * Returns the body as text using the specified encoding.
   *
   * @param encoding 	the encoding to use
   * @return		the text
   */
  public String text(String encoding) throws UnsupportedEncodingException {
    return new String(body(), encoding);
  }

  /**
   * Writes the received body bytes to the specified file.
   *
   * @param filename	the file to write to
   * @throws IOException	if writing fails
   */
  public void saveBody(String filename) throws IOException {
    saveBody(new File(filename));
  }

  /**
   * Writes the received body bytes to the specified file.
   *
   * @param file	the file to write to
   * @throws IOException	if writing fails
   */
  public void saveBody(File file) throws IOException {
    Files.write(file.toPath(), body(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   * Outputs a short description of the response.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString() + ", body length: " + body().length;
  }
}

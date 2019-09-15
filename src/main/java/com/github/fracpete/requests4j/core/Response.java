/*
 * Response.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates a (in-memory) response from a request.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Response
  extends AbstractResponse {

  /** the received content. */
  protected TByteList m_Body;

  /**
   * Initializes the response object.
   *
   * @param statusCode		the HTTP status code
   * @param statusMessage	the HTTP status message
   * @param headers		the received headers
   */
  @Override
  public void init(int statusCode, String statusMessage, Map<String, List<String>> headers) {
    super.init(statusCode, statusMessage, headers);
    m_Body = new TByteArrayList();
  }

  /**
   * Appends the byte read from the response.
   *
   * @param b		the byte to append
   * @throws IOException	if appending failed
   */
  public void appendBody(byte b) throws IOException {
    m_Body.add(b);
  }

  /**
   * Called when all data from the response has been processed.
   *
   * @throws IOException	if finishing up fails
   */
  public void finishBody() throws IOException {
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

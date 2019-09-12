/*
 * Response.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Encapsulates a response from a request.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Response
  implements Serializable {

  /** the status code. */
  protected int m_StatusCode;

  /** the status message. */
  protected String m_StatusMessage;

  /** the content. */
  protected byte[] m_Body;

  /**
   * Initializes the response.
   *
   * @param statusCode	the HTTP code
   * @param body	the body
   */
  public Response(int statusCode, String statusMessage, byte[] body) {
    if (body == null)
      body = new byte[0];
    if (statusMessage == null)
      statusMessage = "";

    m_StatusCode    = statusCode;
    m_StatusMessage = statusMessage;
    m_Body          = body;
  }

  /**
   * Returns the HTTP status code.
   *
   * @return		the code
   */
  public int statusCode() {
    return m_StatusCode;
  }

  /**
   * Returns the HTTP status message.
   *
   * @return		the message
   */
  public String statusMessage() {
    return m_StatusMessage;
  }

  /**
   * Whether the request has a code of less than 400.
   *
   * @return		true if <400
   */
  public boolean ok() {
    return (statusCode() < 400);
  }

  /**
   * Returns the body.
   *
   * @return		the body
   */
  public byte[] body() {
    return m_Body;
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
    return new String(m_Body, encoding);
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
    Files.write(file.toPath(), m_Body, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  /**
   * Outputs a short description of the response.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "status code: " + statusCode() + ", status message: " + statusMessage() + ", body length: " + body().length;
  }
}

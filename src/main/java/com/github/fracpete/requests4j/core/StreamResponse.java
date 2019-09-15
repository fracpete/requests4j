/*
 * StreamResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Forwards the incoming data to the configured stream.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StreamResponse
  extends AbstractResponse {

  /** the stream to write to. */
  protected OutputStream m_OutputStream;

  /** whether to close the stream when all data has been received. */
  protected boolean m_CloseOnFinish;

  /**
   * Initializes the response.
   * Will not close the stream once finished.
   *
   * @param outputStream 	the stream to write the response to
   */
  public StreamResponse(OutputStream outputStream) {
    this(outputStream, false);
  }

  /**
   * Initializes the response.
   *
   * @param outputStream 	the stream to write the response to
   * @param closeOnFinish 	whether to close the stream once all data has been received
   */
  public StreamResponse(OutputStream outputStream, boolean closeOnFinish) {
    super();
    m_OutputStream  = outputStream;
    m_CloseOnFinish = closeOnFinish;
  }

  /**
   * Returns the underlying stream.
   *
   * @return		the output stream
   */
  public OutputStream outputStream() {
    return m_OutputStream;
  }

  /**
   * Returns whether to close the stream once all data has been received.
   *
   * @return		true if to close
   */
  public boolean closeOnFinish() {
    return m_CloseOnFinish;
  }

  /**
   * Appends the byte read from the response.
   *
   * @param b			the byte to append
   * @throws IOException	if appending failed
   */
  @Override
  public void appendBody(byte b) throws IOException {
    m_OutputStream.write(b);
  }

  /**
   * Called when all data from the response has been processed.
   *
   * @throws IOException	if finishing up fails
   */
  public void finishBody() throws IOException {
    m_OutputStream.flush();
    if (m_CloseOnFinish)
      m_OutputStream.close();
  }

  /**
   * Outputs a short description of the response.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString() + ", closeOnFinish=" + closeOnFinish();
  }
}

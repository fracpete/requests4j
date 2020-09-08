/*
 * StreamResponse.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.response;

import com.github.fracpete.requests4j.request.Request;
import org.apache.tika.io.IOUtils;

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
   * Initializes the response object.
   *
   * @param response		the response
   */
  @Override
  public void init(okhttp3.Response response) {
    super.init(response);

    if (!Request.isRedirect(response.code())) {
      try {
        IOUtils.copy(response.body().byteStream(), m_OutputStream);
      }
      catch (Exception e) {
        // TODO error message?
      }
      finally {
        if (m_CloseOnFinish)
          IOUtils.closeQuietly(m_OutputStream);
      }
    }
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

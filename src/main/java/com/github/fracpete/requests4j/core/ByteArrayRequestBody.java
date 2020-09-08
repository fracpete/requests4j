/*
 * ByteArrayRequestBody.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * RequestBody for byte arrays.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ByteArrayRequestBody
  extends RequestBody {

  /** the media type. */
  protected MediaType m_MediaType;

  /** the data. */
  protected byte[] m_Data;

  /**
   * Initializes the the request body.
   *
   * @param mediaType	the media type
   * @param data	the data
   */
  public ByteArrayRequestBody(MediaType mediaType, byte[] data) {
    m_MediaType = mediaType;
    m_Data = data;
  }

  @Override
  public long contentLength() throws IOException {
    return m_Data.length;
  }

  @Nullable
  @Override
  public okhttp3.MediaType contentType() {
    return m_MediaType;
  }

  @Override
  public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
    bufferedSink.write(m_Data);
  }
}

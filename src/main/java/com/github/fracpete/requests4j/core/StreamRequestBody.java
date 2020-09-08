/*
 * FileRequestBody.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Request body for streams.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StreamRequestBody
  extends RequestBody {

  /** the media type. */
  protected MediaType m_MediaType;

  /** the filename. */
  protected InputStream m_Stream;

  /**
   * Initializes the the request body.
   *
   * @param mediaType	the media type
   * @param stream	the stream
   */
  public StreamRequestBody(MediaType mediaType, InputStream stream) {
    m_MediaType = mediaType;
    m_Stream = stream;
  }

  @Override
  public long contentLength() throws IOException {
    try {
      return m_Stream.available();
    }
    catch (IOException e) {
      return 0;
    }
  }


  @Nullable
  @Override
  public MediaType contentType() {
    return m_MediaType;
  }

  @Override
  public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
    Source source = null;
    try {
      source = Okio.source(m_Stream);
      bufferedSink.writeAll(source);
    }
    finally {
      Util.closeQuietly(source);
    }
  }
}

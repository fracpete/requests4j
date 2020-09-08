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

import java.io.File;
import java.io.IOException;

/**
 * Request body for files.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FileRequestBody
  extends RequestBody {

  /** the media type. */
  protected MediaType m_MediaType;

  /** the filename. */
  protected String m_Filename;

  /**
   * Initializes the the request body.
   *
   * @param mediaType	the media type
   * @param filename	the data
   */
  public FileRequestBody(MediaType mediaType, String filename) {
    m_MediaType = mediaType;
    m_Filename = filename;
  }

  @Override
  public long contentLength() throws IOException {
    try {
      return new File(m_Filename).length();
    }
    catch (Exception e) {
      return 0;
    }
  }

  @Nullable
  @Override
  public okhttp3.MediaType contentType() {
    return m_MediaType;
  }

  @Override
  public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
    Source source = null;
    try {
      source = Okio.source(new File(m_Filename));
      bufferedSink.writeAll(source);
    }
    finally {
      Util.closeQuietly(source);
    }
  }
}

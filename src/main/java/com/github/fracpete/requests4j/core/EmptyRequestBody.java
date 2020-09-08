/*
 * EmptyRequestBody.java
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
 * Empty RequestBody.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class EmptyRequestBody
  extends RequestBody {

  /**
   * Initializes the the request body.
   */
  public EmptyRequestBody() {
  }

  @Override
  public long contentLength() throws IOException {
    return 0;
  }

  @Nullable
  @Override
  public MediaType contentType() {
    return MediaTypeHelper.OCTECT_STREAM;
  }

  @Override
  public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
  }
}

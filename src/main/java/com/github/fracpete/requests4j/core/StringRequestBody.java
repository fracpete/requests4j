/*
 * StringRequestBody.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import okhttp3.MediaType;

/**
 * TODO: What class does.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StringRequestBody
  extends ByteArrayRequestBody {

  /**
   * Initializes the the request body. Uses UTF-8.
   *
   * @param mediaType 	the media type
   * @param data      	the data
   */
  public StringRequestBody(MediaType mediaType, String data) {
    this(mediaType, data, "UTF-8");
  }

  /**
   * Initializes the the request body.
   *
   * @param mediaType 	the media type
   * @param data      	the data
   * @param charset 	the character set to use
   */
  public StringRequestBody(MediaType mediaType, String data, String charset) {
    super(determineMediaType(mediaType, charset), toBytes(data, charset));
  }

  protected static MediaType determineMediaType(MediaType mediaType, String charset) {
    String	type;

    type = mediaType.toString();
    if (type.contains("; charset="))
      return MediaType.parse(type.substring(0, type.indexOf(';')) + "; charset=" + charset);
    else
      return MediaType.parse(type + "; charset=" + charset);
  }

  protected static byte[] toBytes(String data, String charset) {
    try {
      return data.getBytes(charset);
    }
    catch (Exception e) {
      throw new IllegalStateException("Failed to decode '" + data + "' as '" + charset + "'!", e);
    }
  }
}

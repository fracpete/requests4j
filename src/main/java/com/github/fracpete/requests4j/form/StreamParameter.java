/*
 * StringParameter.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.core.FileRequestBody;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.core.StreamRequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Encapsulates stream parameters.
 * Closes the stream automatically.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StreamParameter
  extends AbstractParameter {

  /** the filename. */
  protected String m_Filename;

  /** the media type. */
  protected MediaType m_MediaType;

  /** the stream. */
  protected InputStream m_Stream;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param filename 	the filename
   */
  public StreamParameter(String name, String filename) {
    this(name, filename, MediaType.parse(MediaTypeHelper.getMediaType(filename).toString()), null);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   */
  public StreamParameter(String name, File file) {
    this(name, file.getAbsolutePath(), MediaType.parse(MediaTypeHelper.getMediaType(file).toString()), null);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   * @param mediaType 	the media type
   * @param stream 	the stream, can be null, caller needs to close
   */
  public StreamParameter(String name, File file, MediaType mediaType, InputStream stream) {
    this(name, file.getAbsolutePath(), mediaType, stream);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param filename 	the filename
   * @param mediaType 	the media type
   * @param stream 	the stream, can be null, caller needs to close
   */
  public StreamParameter(String name, String filename, MediaType mediaType, InputStream stream) {
    super(name);
    m_Filename  = filename;
    m_MediaType = mediaType;
    m_Stream    = stream;
  }

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  @Override
  public boolean canResend() {
    return (m_Stream == null);
  }

  /**
   * Returns the filename.
   *
   * @return		the filename
   */
  public String filename() {
    return m_Filename;
  }

  /**
   * Returns the media type.
   *
   * @return		the media type
   */
  public MediaType mediaType() {
    return m_MediaType;
  }

  /**
   * Returns the stream.
   *
   * @return		the stream
   */
  public InputStream stream() {
    return m_Stream;
  }

  /**
   * Adds the parameter.
   *
   * @param multipart   	the multipart to add the parameter to
   * @throws IOException	if writing fails
   */
  @Override
  public void add(MultipartBody.Builder multipart) throws IOException {
    if (m_Stream != null)
      multipart.addFormDataPart(name(), null, new StreamRequestBody(m_MediaType, stream()));
    else
      multipart.addFormDataPart(name(), null, new FileRequestBody(m_MediaType, filename()));
  }

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public Map<String,String> parameters() {
    throw new IllegalStateException("File/Stream parameter only supports POST, not GET!");
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return name() + ", filename=" + filename() + ", media type=" + mediaType();
  }
}

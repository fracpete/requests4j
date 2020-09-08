/*
 * ByteArrayParameter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.core.ByteArrayRequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;

import java.io.IOException;
import java.util.Map;

/**
 * Encapsulates a byte array parameter.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ByteArrayParameter
  extends AbstractParameter {

  /** the filename. */
  protected String m_Filename;

  /** the media type. */
  protected MediaType m_MediaType;

  /** the data. */
  protected byte[] m_Data;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param mediaType 	the media type
   * @param data 	the data
   */
  public ByteArrayParameter(String name, MediaType mediaType, byte[] data) {
    super(name);

    if (data == null)
      throw new IllegalArgumentException("Byte array cannot be null!");

    m_MediaType = mediaType;
    m_Data      = data;
  }

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  @Override
  public boolean canResend() {
    return false;
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
   * Returns the data.
   *
   * @return		the data
   */
  public byte[] data() {
    return m_Data;
  }

  /**
   * Adds the parameter.
   *
   * @param multipart   	the multipart to add the parameter to
   * @throws IOException	if writing fails
   */
  @Override
  public void add(MultipartBody.Builder multipart) throws IOException {
    multipart.addFormDataPart(name(), null, new ByteArrayRequestBody(m_MediaType, m_Data));
  }

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public Map<String,String> parameters() {
    throw new IllegalStateException("Byte array parameter only supports POST, not GET!");
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return name() + ", #bytes=" + data().length + ", mediatype=" + mediaType();
  }
}

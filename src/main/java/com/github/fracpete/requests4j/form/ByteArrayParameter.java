/*
 * ByteArrayParameter.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.tika.mime.MediaType;

import java.io.ByteArrayInputStream;
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

  /** the mimetype. */
  protected MediaType m_MimeType;

  /** the data. */
  protected byte[] m_Data;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param mimeType 	the mimetype
   * @param data 	the data
   */
  public ByteArrayParameter(String name, MediaType mimeType, byte[] data) {
    super(name);

    if (data == null)
      throw new IllegalArgumentException("Byte array cannot be null!");

    m_MimeType = mimeType;
    m_Data     = data;
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
   * Returns the mimetype.
   *
   * @return		the mimetype
   */
  public MediaType mimeType() {
    return m_MimeType;
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
  public void add(MultipartEntityBuilder multipart) throws IOException {
    InputStreamBody 	streambody;

    streambody = new InputStreamBody(new ByteArrayInputStream(m_Data), ContentType.create(mimeType().toString()));
    multipart.addPart(name(), streambody);
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
    return name() + ", #bytes=" + data().length + ", mimetype=" + mimeType();
  }
}

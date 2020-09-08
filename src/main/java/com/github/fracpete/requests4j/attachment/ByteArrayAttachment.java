/*
 * ByteArrayAttachment.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.attachment;

import com.github.fracpete.requests4j.core.ByteArrayRequestBody;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Represents a byte-array attachment.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ByteArrayAttachment
  extends AbstractAttachment {

  /** the content. */
  protected byte[] m_Content;

  /** the media type. */
  protected MediaType m_MediaType;

  /**
   * Initializes the attachment.
   * Uses application/octet-stream.
   *
   * @param content	the data to attach
   */
  public ByteArrayAttachment(byte[] content) {
    this(content, MediaTypeHelper.OCTECT_STREAM);
  }

  /**
   * Initializes the attachment.
   *
   * @param content	the data to attach
   * @param mediaType	the media type of the data
   */
  public ByteArrayAttachment(byte[] content, MediaType mediaType) {
    m_Content = content;
    m_MediaType = mediaType;
  }

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  @Override
  public boolean canResend() {
    return true;
  }

  /**
   * Returns the data.
   *
   * @return		the data
   */
  public byte[] content() {
    return m_Content;
  }

  /**
   * Returns the media type.
   *
   * @return		the type
   */
  public MediaType mediaType() {
    return m_MediaType;
  }

  /**
   * Returns whether the attachment is correctly configured.
   *
   * @return		true if properly configured
   */
  @Override
  public boolean isValid() {
    return (m_Content != null)
      && (m_MediaType != null);
  }

  /**
   * Returns the content disposition to use.
   *
   * @return		the value for the 'Content-Disposition' header
   */
  @Override
  public String getContentDisposition() {
    return "attachment";
  }

  /**
   * Returns the HTTP entity of the attachment.
   *
   * @return		the entity
   */
  @Override
  public RequestBody getBody() {
    return new ByteArrayRequestBody(m_MediaType, m_Content);
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "#bytes=" + (content() != null ? content().length : 0) + ", mediaType=" + mediaType();
  }
}

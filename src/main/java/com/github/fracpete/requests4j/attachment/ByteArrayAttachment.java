/*
 * ByteArrayAttachment.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.attachment;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.ByteArrayInputStream;

/**
 * Represents a byte-array attachment.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ByteArrayAttachment
  extends AbstractAttachment {

  /** the content. */
  protected byte[] m_Content;

  /** the content type. */
  protected ContentType m_ContentType;

  /**
   * Initializes the attachment.
   * Uses {@link ContentType#APPLICATION_OCTET_STREAM}.
   *
   * @param content	the data to attach
   */
  public ByteArrayAttachment(byte[] content) {
    this(content, ContentType.APPLICATION_OCTET_STREAM);
  }

  /**
   * Initializes the attachment.
   *
   * @param content	the data to attach
   * @param contentType	the content type of the data
   */
  public ByteArrayAttachment(byte[] content, ContentType contentType) {
    m_Content = content;
    m_ContentType = contentType;
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
   * Returns the content type.
   *
   * @return		the type
   */
  public ContentType contentType() {
    return m_ContentType;
  }

  /**
   * Returns whether the attachment is correctly configured.
   *
   * @return		true if properly configured
   */
  @Override
  public boolean isValid() {
    return (m_Content != null)
      && (m_ContentType != null);
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
  public HttpEntity getEntity() {
    return new InputStreamEntity(new ByteArrayInputStream(m_Content), m_Content.length);
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "#bytes=" + (content() != null ? content().length : 0) + ", contentType=" + contentType();
  }
}

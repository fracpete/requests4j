/*
 * FileAttachment.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.attachment;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;

import java.io.File;

/**
 * Represents a file attachment.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FileAttachment
  extends AbstractAttachment {

  /** the file. */
  protected File m_File;

  /** the name to use. */
  protected String m_Name;

  /** the content type. */
  protected ContentType m_ContentType;

  /**
   * Initializes the file attachment.
   * Uses {@link ContentType#APPLICATION_OCTET_STREAM} and file.getName()
   * as the file name in the request.
   *
   * @param file	the file to attach
   */
  public FileAttachment(File file) {
    this(file, ContentType.APPLICATION_OCTET_STREAM);
  }

  /**
   * Initializes the file attachment.
   * Uses file.getName() as the file name in the request.
   *
   * @param file	the file to attach
   * @param contentType	the content type of the file
   */
  public FileAttachment(File file, ContentType contentType) {
    this(file, file.getName(), contentType);
  }

  /**
   * Initializes the file attachment.
   *
   * @param file	the file to attach
   * @param name 	the name to use in the request
   * @param contentType	the content type of the file
   */
  public FileAttachment(File file, String name, ContentType contentType) {
    m_File = file;
    m_Name = name;
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
   * Returns the file.
   *
   * @return		the file
   */
  public File file() {
    return m_File;
  }

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public String name() {
    return m_Name;
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
    return (m_File != null) && (m_File.exists()) && (!m_File.isDirectory())
      && (m_Name != null)
      && (m_ContentType != null);
  }

  /**
   * Returns the content disposition to use.
   *
   * @return		the value for the 'Content-Disposition' header
   */
  @Override
  public String getContentDisposition() {
    return "attachment; filename=" + m_Name;
  }

  /**
   * Returns the HTTP entity of the attachment.
   *
   * @return		the entity
   */
  @Override
  public HttpEntity getEntity() {
    return new FileEntity(m_File, m_ContentType);
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "file=" + file() + ", name=" + name() + ", contentType=" + contentType();
  }
}

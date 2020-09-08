/*
 * FileAttachment.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.attachment;

import com.github.fracpete.requests4j.core.FileRequestBody;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
  protected MediaType m_MediaType;

  /**
   * Initializes the file attachment.
   * Uses application/octet-stream and file.getName()
   * as the file name in the request.
   *
   * @param file	the file to attach
   */
  public FileAttachment(File file) {
    this(file, MediaTypeHelper.OCTECT_STREAM);
  }

  /**
   * Initializes the file attachment.
   * Uses file.getName() as the file name in the request.
   *
   * @param file	the file to attach
   * @param mediaType	the media type of the file
   */
  public FileAttachment(File file, MediaType mediaType) {
    this(file, file.getName(), mediaType);
  }

  /**
   * Initializes the file attachment.
   *
   * @param file	the file to attach
   * @param name 	the name to use in the request
   * @param mediaType	the media type of the file
   */
  public FileAttachment(File file, String name, MediaType mediaType) {
    m_File = file;
    m_Name = name;
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
    return (m_File != null) && (m_File.exists()) && (!m_File.isDirectory())
      && (m_Name != null)
      && (m_MediaType != null);
  }

  /**
   * Returns the content disposition to use.
   *
   * @return		the value for the 'Content-Disposition' header
   */
  @Override
  public String getContentDisposition() {
    return "attachment; name=\"file\"; filename=\"" + m_Name + "\"";
  }

  /**
   * Returns the HTTP entity of the attachment.
   *
   * @return		the entity
   */
  @Override
  public RequestBody getBody() {
    return new FileRequestBody(m_MediaType, m_File.getAbsolutePath());
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "file=" + file() + ", name=" + name() + ", mediaType=" + mediaType();
  }
}

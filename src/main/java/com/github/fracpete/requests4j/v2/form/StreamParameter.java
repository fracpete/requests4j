/*
 * StringParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.v2.form;

import com.github.fracpete.requests4j.core.MimeTypeHelper;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.tika.mime.MediaType;

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

  /** the mimetype. */
  protected MediaType m_MimeType;

  /** the stream. */
  protected InputStream m_Stream;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param filename 	the filename
   */
  public StreamParameter(String name, String filename) {
    this(name, filename, MimeTypeHelper.getMimeType(filename), null);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   */
  public StreamParameter(String name, File file) {
    this(name, file.getAbsolutePath(), MimeTypeHelper.getMimeType(file), null);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   * @param mimeType 	the mimetype
   * @param stream 	the stream, can be null, caller needs to close
   */
  public StreamParameter(String name, File file, MediaType mimeType, InputStream stream) {
    this(name, file.getAbsolutePath(), mimeType, stream);
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param filename 	the filename
   * @param mimeType 	the mimetype
   * @param stream 	the stream, can be null, caller needs to close
   */
  public StreamParameter(String name, String filename, MediaType mimeType, InputStream stream) {
    super(name);
    m_Filename = filename;
    m_MimeType = mimeType;
    m_Stream   = stream;
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
   * Returns the mimetype.
   *
   * @return		the mimetype
   */
  public MediaType mimeType() {
    return m_MimeType;
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
  public void add(MultipartEntityBuilder multipart) throws IOException {
    InputStreamBody 	streambody;
    FileBody		filebody;

    if (m_Stream != null) {
      streambody = new InputStreamBody(m_Stream, ContentType.create(mimeType().toString()), new File(filename()).getName());
      multipart.addPart(name(), streambody);
    }
    else {
      filebody = new FileBody(new File(m_Filename), ContentType.create(mimeType().toString()));
      multipart.addPart(name(), filebody);
    }
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
    return name() + ", filename=" + filename() + ", mimetype=" + mimeType();
  }
}

/*
 * StringParameter.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.core.MimeTypeHelper;
import org.apache.tika.mime.MediaType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
   * @throws IOException	if fails to open FileInputStream
   */
  public StreamParameter(String name, String filename) throws IOException {
    this(name, filename, MimeTypeHelper.getMimeType(filename), new FileInputStream(filename));
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   * @throws IOException	if fails to open FileInputStream
   */
  public StreamParameter(String name, File file) throws IOException {
    this(name, file.getAbsolutePath(), MimeTypeHelper.getMimeType(file), new FileInputStream(file));
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param file 	the file
   * @param mimeType 	the mimetype
   * @param stream 	the stream, will get closed automatically
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
   * @param stream 	the stream, will get closed automatically
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
   * Writes out the parameter.
   *
   * @param conn 	the connection in use
   * @param writer	the writer to use
   * @param boundary 	the boundary to use
   * @throws IOException	if writing fails
   */
  @Override
  public void post(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException {
    OutputStream 	os;
    int 		read;
    byte[] 		buffer;

    writer.write("\r\n");
    writer.write("--" + boundary + "\r\n");
    writer.write("Content-Disposition: form-data; name=\"" + name() + "\"; filename=\"" + new File(filename()).getName() + "\"\r\n");
    writer.write("Content-Type: " + mimeType().toString() + "\r\n");
    writer.write("\r\n");
    writer.flush();

    os = conn.getOutputStream();
    buffer = new byte[1024];
    while ((read = stream().read(buffer)) != -1)
      os.write(buffer, 0, read);
    os.flush();

    stream().close();
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

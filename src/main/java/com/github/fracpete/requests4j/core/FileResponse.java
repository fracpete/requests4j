/*
 * FileResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Streams the data directly to a file. Useful for large downloads.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FileResponse
  extends AbstractResponse {

  /** the file to write to. */
  protected File m_OutputFile;

  /** the buffer size (<= 0 for default). */
  protected int m_BufferSize;

  /** the file output stream. */
  protected transient FileOutputStream m_FileOutputStream;

  /** the buffered output stream. */
  protected transient BufferedOutputStream m_BufferedOutputStream;

  /**
   * Initializes the response.
   *
   * @param outputFilename 	the file to write the response to
   */
  public FileResponse(String outputFilename) {
    this(new File(outputFilename));
  }

  /**
   * Initializes the response.
   *
   * @param outputFilename 	the file to write the response to
   * @param bufferSize 	the buffer size, use <= 0 for default
   */
  public FileResponse(String outputFilename, int bufferSize) {
    this(new File(outputFilename), bufferSize);
  }

  /**
   * Initializes the response.
   *
   * @param outputFile 	the file to write the response to
   */
  public FileResponse(File outputFile) {
    this(outputFile, -1);
  }

  /**
   * Initializes the response.
   *
   * @param outputFile 	the file to write the response to
   * @param bufferSize 	the buffer size, use <= 0 for default
   */
  public FileResponse(File outputFile, int bufferSize) {
    if (bufferSize <= 0)
      bufferSize = -1;
    m_OutputFile = outputFile;
    m_BufferSize = bufferSize;
  }

  /**
   * Initializes the response object.
   *
   * @param statusCode		the HTTP status code
   * @param statusMessage	the HTTP status message
   * @param headers		the received headers
   */
  @Override
  public void init(int statusCode, String statusMessage, Map<String, List<String>> headers) {
    super.init(statusCode, statusMessage, headers);
    m_FileOutputStream = null;
  }

  /**
   * Returns the file that the response is being written to.
   *
   * @return		the output file
   */
  public File outputFile() {
    return m_OutputFile;
  }

  /**
   * Returns the buffer size.
   *
   * @return		the buffer size
   */
  public int bufferSize() {
    return m_BufferSize;
  }

  /**
   * Appends the byte read from the response.
   *
   * @param b		the byte to append
   * @throws IOException	if appending failed
   */
  @Override
  public void appendBody(byte b) throws IOException {
    if (m_FileOutputStream == null) {
      m_FileOutputStream = new FileOutputStream(m_OutputFile.getAbsolutePath());
      if (m_BufferSize <= 0)
	m_BufferedOutputStream = new BufferedOutputStream(m_FileOutputStream);
      else
	m_BufferedOutputStream = new BufferedOutputStream(m_FileOutputStream, m_BufferSize);
    }
    m_BufferedOutputStream.write(b);
  }

  /**
   * Called when all data from the response has been processed.
   *
   * @throws IOException	if finishing up fails
   */
  public void finishBody() throws IOException {
    if (m_BufferedOutputStream != null) {
      m_BufferedOutputStream.flush();
      m_BufferedOutputStream.close();
      m_BufferedOutputStream = null;
    }
    if (m_FileOutputStream != null) {
      m_FileOutputStream.flush();
      m_FileOutputStream.close();
      m_FileOutputStream = null;
    }
  }

  /**
   * Outputs a short description of the response.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return super.toString()
      + ", outputFile=" + outputFile()
      + ", bufferSize=" + (bufferSize() <= 0 ? "-default-" : "" + bufferSize());
  }
}

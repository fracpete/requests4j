/*
 * FileResponse.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.response;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.tika.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
   * @param response		the response
   */
  @Override
  public void init(CloseableHttpResponse response) {
    FileOutputStream 		fos;
    BufferedOutputStream 	bos;

    super.init(response);

    fos = null;
    bos = null;
    try {
      fos = new FileOutputStream(m_OutputFile.getAbsolutePath());
      if (m_BufferSize <= 0)
	bos = new BufferedOutputStream(fos);
      else
	bos = new BufferedOutputStream(fos, m_BufferSize);
      IOUtils.copy(response.getEntity().getContent(), bos);
    }
    catch (Exception e) {
      // TODO error message?
    }
    finally {
      IOUtils.closeQuietly(bos);
      IOUtils.closeQuietly(fos);
    }
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

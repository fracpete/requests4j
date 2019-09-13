/*
 * FormData.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import org.apache.tika.mime.MediaType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * For storing form data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FormData
  extends HashMap<String,AbstractParameter> {

  /**
   * Initializes empty form data.
   */
  public FormData() {
    super();
  }

  /**
   * Initializes the data with the provided form data.
   *
   * @param formData 	the map to initialize with
   */
  public FormData(Map<String,AbstractParameter> formData) {
    super(formData);
  }

  /**
   * Adds the parameter.
   *
   * @param param	the parameter to add
   * @return		itself
   */
  public FormData add(AbstractParameter param) {
    put(param.name(), param);
    return this;
  }

  /**
   * Adds the string form parameter.
   *
   * @param name	the parameter name
   * @param value	the value
   * @return		itself
   */
  public FormData add(String name, String value) {
    return add(new StringParameter(name, value));
  }

  /**
   * Adds the file as form parameter.
   *
   * @param name	the parameter name
   * @param filename	the file to add
   * @return		itself
   */
  public FormData addFile(String name, String filename) throws IOException {
    return add(new StreamParameter(name, filename));
  }

  /**
   * Adds the file as form parameter.
   *
   * @param name	the parameter name
   * @param file	the file to add
   * @return		itself
   */
  public FormData addFile(String name, File file) throws IOException {
    return add(new StreamParameter(name, file));
  }

  /**
   * Adds the stream as form parameter.
   *
   * @param name	the parameter name
   * @param file	the file name to use for the stream
   * @param mimetype 	the mimetype to use, eg {@link MediaType#OCTET_STREAM}
   * @param stream 	the stream to read from
   * @return		itself
   */
  public FormData addStream(String name, File file, MediaType mimetype, InputStream stream) throws IOException {
    return add(new StreamParameter(name, file, mimetype, stream));
  }

  /**
   * Adds the stream as form parameter.
   *
   * @param name	the parameter name
   * @param filename	the file name to use for the stream
   * @param mimetype 	the mimetype to use, eg {@link MediaType#OCTET_STREAM}
   * @param stream 	the stream to read from
   * @return		itself
   */
  public FormData addStream(String name, String filename, MediaType mimetype, InputStream stream) throws IOException {
    return add(new StreamParameter(name, filename, mimetype, stream));
  }

  /**
   * Writes out the parameters.
   *
   * @param conn 	the connection in use
   * @param writer	the writer to use
   * @param boundary 	the boundary to use
   * @throws IOException	if writing fails
   */
  public void post(HttpURLConnection conn, BufferedWriter writer, String boundary) throws IOException {
    if (size() > 0) {
      writer.write("\n\n");
      for (String key : keySet())
	get(key).post(conn, writer, boundary);
    }
  }

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public Map<String,String> parameters() {
    Map<String,String> 	result;

    result = new HashMap<>();
      for (String key : keySet())
	result.putAll(get(key).parameters());

    return result;
  }
}

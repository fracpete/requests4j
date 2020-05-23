/*
 * FormData.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.core.Resendable;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.tika.mime.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For storing form data.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class FormData
  extends HashMap<String,AbstractParameter>
  implements Resendable {

  /** the list of streams to clean up. */
  protected List<InputStream> m_Streams;

  /**
   * Initializes empty form data.
   */
  public FormData() {
    super();
    m_Streams = new ArrayList<>();
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
   * Adds the map as string form parameters.
   *
   * @param parameters	the parameters to add
   * @return		itself
   */
  public FormData add(Map<String,String> parameters) {
    for (String key: parameters.keySet())
      add(new StringParameter(key, parameters.get(key)));
    return this;
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
    m_Streams.add(stream);
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
    m_Streams.add(stream);
    return add(new StreamParameter(name, filename, mimetype, stream));
  }

  /**
   * Adds the parameter.
   *
   * @param post   		the request to add the form data to
   * @throws IOException	if writing fails
   */
  public void add(HttpPost post) throws IOException {
    MultipartEntityBuilder	builder;

    if (size() > 0) {
      builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      for (String key : keySet())
	get(key).add(builder);
      post.setEntity(builder.build());
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

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  @Override
  public boolean canResend() {
    boolean	result;

    result = true;

    for (String key: keySet()) {
      result = get(key).canResend();
      if (!result)
        break;
    }

    return result;
  }

  /**
   * Closes all the streams.
   */
  public void cleanUp() {
    for (InputStream stream: m_Streams) {
      try {
        stream.close();
      }
      catch (Exception e) {
        // ignored
      }
    }
  }
}

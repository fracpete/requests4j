/*
 * StringParameter.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates string parameters.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class StringParameter
  extends AbstractParameter {

  /** the parameter value. */
  protected String m_Value;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param value 	the value
   */
  public StringParameter(String name, String value) {
    super(name);
    if (value == null)
      throw new IllegalStateException("String parameter value cannot be null (name=" + name + ")!");
    m_Value = value;
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
   * Returns the value.
   *
   * @return		the value
   */
  public String value() {
    return m_Value;
  }

  /**
   * Adds the parameter.
   *
   * @param multipart   	the multipart to add the parameter to
   * @throws IOException	if writing fails
   */
  @Override
  public void add(MultipartEntityBuilder multipart) throws IOException {
    multipart.addPart(name(), new StringBody(value(), ContentType.MULTIPART_FORM_DATA));
  }

  /**
   * Collects the parameters.
   *
   * @return 		the parameters
   */
  public Map<String,String> parameters() {
    Map<String,String>  result;

    result = new HashMap<>();
    result.put(name(), value());

    return result;
  }

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return name() + "=" + value();
  }
}

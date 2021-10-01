/*
 * JsonParameter.java
 * Copyright (C) 2021 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.form;

import com.github.fracpete.requests4j.json.Array;
import com.github.fracpete.requests4j.json.Dictionary;
import com.github.fracpete.requests4j.json.Element;
import okhttp3.MultipartBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates string parameters that take json.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class JsonParameter
  extends AbstractParameter {

  /** the parameter value. */
  protected Element m_Value;

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param value 	the value
   */
  public JsonParameter(String name, Dictionary value) {
    super(name);
    if (value == null)
      throw new IllegalStateException("String parameter value cannot be null (name=" + name + ")!");
    m_Value = value;
  }

  /**
   * Initializes the parameter.
   *
   * @param name 	the name
   * @param value 	the value
   */
  public JsonParameter(String name, Array value) {
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
    return m_Value.dump();
  }

  /**
   * Adds the parameter.
   *
   * @param multipart   	the multipart to add the parameter to
   * @throws IOException	if writing fails
   */
  @Override
  public void add(MultipartBody.Builder multipart) throws IOException {
    multipart.addFormDataPart(name(), value());
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

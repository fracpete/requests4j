/*
 * Request.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import com.github.fracpete.requests4j.auth.AbstractAuthentication;
import com.github.fracpete.requests4j.auth.NoAuthentication;
import com.github.fracpete.requests4j.form.FormData;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Encapsulates a request.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Request
  implements Serializable {

  /** the method. */
  protected String m_Method;

  /** the URL to contact. */
  protected URL m_URL;

  /** the headers. */
  protected Map<String,String> m_Headers;

  /** the cookies. */
  protected Map<String,String> m_Cookies;

  /** the parameters. */
  protected Map<String,String> m_Parameters;

  /** the form data. */
  protected FormData m_FormData;

  /** the authentication to use. */
  protected AbstractAuthentication m_Authentication;

  /** the connection timeout (msec), default if < 0. */
  protected int m_ConnectionTimeout;

  /** the read timeout (msec), default if < 0. */
  protected int m_ReadTimeout;

  /**
   * Initializes the request.
   *
   * @param method	the method to use
   */
  public Request(String method) {
    m_Method            = method;
    m_URL               = null;
    m_Headers           = new HashMap<>();
    m_Cookies           = new HashMap<>();
    m_Parameters        = new HashMap<>();
    m_Authentication    = new NoAuthentication();
    m_FormData          = new FormData();
    m_ConnectionTimeout = -1;
    m_ReadTimeout       = -1;
  }

  /**
   * Returns the method.
   *
   * @return		the method
   */
  public String method() {
    return m_Method;
  }

  /**
   * Sets the URL to contact.
   *
   * @param url		the URL
   * @return		itself
   * @throws MalformedURLException	if invalid URL
   */
  public Request url(String url) throws MalformedURLException {
    m_URL = new URL(url);
    return this;
  }

  /**
   * Sets the URL to contact.
   *
   * @param url		the URL
   * @return		itself
   */
  public Request url(URL url) {
    m_URL = url;
    return this;
  }

  /**
   * Returns the URL to contact.
   *
   * @return		the URL, null if none set
   */
  public URL url() {
    return m_URL;
  }

  /**
   * Adds the HTTP header.
   *
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public Request header(String name, String value) {
    m_Headers.put(name, value);
    return this;
  }

  /**
   * Adds all the HTTP headers.
   *
   * @param headers	the headers
   * @return		itself
   */
  public Request headers(Map<String,String> headers) {
    m_Headers.putAll(headers);
    return this;
  }

  /**
   * Returns the current HTTP headers.
   *
   * @return		the headers
   */
  public Map<String,String> headers() {
    return m_Headers;
  }

  /**
   * Adds the cookie.
   *
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public Request cookie(String name, String value) {
    m_Cookies.put(name, value);
    return this;
  }

  /**
   * Adds all the cookies.
   *
   * @param cookies	the cookies
   * @return		itself
   */
  public Request cookies(Map<String,String> cookies) {
    m_Cookies.putAll(cookies);
    return this;
  }

  /**
   * Returns the current cookies.
   *
   * @return		the cookies
   */
  public Map<String,String> cookies() {
    return m_Cookies;
  }

  /**
   * Adds the query parameter.
   *
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public Request parameter(String name, String value) {
    m_Parameters.put(name, value);
    return this;
  }

  /**
   * Adds all the query parameters.
   *
   * @param parameters	the parameters
   * @return		itself
   */
  public Request parameters(Map<String,String> parameters) {
    m_Parameters.putAll(parameters);
    return this;
  }

  /**
   * Returns the current query parameters.
   *
   * @return		the parameters
   */
  public Map<String,String> parameters() {
    return m_Parameters;
  }

  /**
   * Adds all the form data.
   *
   * @param formData	the form data to add
   * @return		itself
   */
  public Request formData(FormData formData) {
    m_FormData.putAll(formData);
    return this;
  }

  /**
   * Returns the current form data.
   *
   * @return		the form data
   */
  public FormData formData() {
    return m_FormData;
  }

  /**
   * Sets the authentication to use.
   *
   * @param auth	the authentication, null to turn off
   * @return		itself
   */
  public Request auth(AbstractAuthentication auth) {
    if (auth == null)
      auth = new NoAuthentication();
    m_Authentication = auth;
    return this;
  }

  /**
   * Returns the current authentication scheme.
   *
   * @return		the authentication
   */
  public AbstractAuthentication auth() {
    return m_Authentication;
  }

  /**
   * Sets the timeouts.
   *
   * @param connection	the connection timeout, < 0 for default
   * @param read	the read timeout, < 0 for default
   * @return		itself
   */
  public Request timeouts(int connection, int read) {
    connectionTimeout(connection);
    readTimeout(read);
    return this;
  }

  /**
   * Sets the connection timeout.
   *
   * @param timeout	the timeout in msec, < 0 to use default
   * @return		itself
   */
  public Request connectionTimeout(int timeout) {
    m_ConnectionTimeout = timeout;
    return this;
  }

  /**
   * Return the connection timeout.
   *
   * @return		the timeout in msec, < 0 to use default
   */
  public int connectionTimeout() {
    return m_ConnectionTimeout;
  }

  /**
   * Sets the read timeout.
   *
   * @param timeout	the timeout in msec, < 0 to use default
   * @return		itself
   */
  public Request readTimeout(int timeout) {
    m_ReadTimeout = timeout;
    return this;
  }

  /**
   * Return the read timeout.
   *
   * @return		the timeout in msec, < 0 to use default
   */
  public int readTimeout() {
    return m_ReadTimeout;
  }

  /**
   * Assembles the full URL.
   *
   * @return		the URL
   * @throws MalformedURLException	if fails to assemble URL
   */
  protected URL assembleURL() throws Exception {
    URL 		result;
    StringBuilder	url;
    int			i;
    String		enc;

    if (m_Parameters.size() > 0) {
      enc = "UTF-8";
      url = new StringBuilder(m_URL.toString());
      i   = 0;
      for (String key: m_Parameters.keySet()) {
        if (i == 0)
          url.append("?");
        else
          url.append("&");
        url.append(URLEncoder.encode(key, enc));
        url.append("=");
        url.append(URLEncoder.encode(m_Parameters.get(key), enc));
        i++;
      }
      result = new URL(url.toString());
    }
    else {
      result = m_URL;
    }

    return result;
  }

  /**
   * Creates a random boundary string.
   *
   * @return		the random boundary string
   */
  protected String createBoundary() {
    String	result;
    Random rand;

    rand     = new Random();
    result = Integer.toHexString(rand.nextInt()) + Integer.toHexString(rand.nextInt()) + Integer.toHexString(rand.nextInt());

    return result;
  }

  /**
   * Executes the request.
   *
   * @return		the generated response
   * @throws Exception	if execution fails
   */
  public Response execute() throws Exception {
    Response		result;
    HttpURLConnection	conn;
    URL 		url;
    BufferedWriter	writer;
    InputStream		in;
    TByteList		content;
    int			b;
    String		boundary;

    if (m_URL == null)
      throw new IllegalStateException("No URL provided!");

    // assemble URL
    url = assembleURL();

    // apply authentiation
    m_Authentication.apply(this);

    // write request
    conn = (HttpURLConnection) url.openConnection();
    if (m_ConnectionTimeout >= 0)
      conn.setConnectTimeout(m_ConnectionTimeout);
    if (m_ReadTimeout >= 0)
      conn.setReadTimeout(m_ReadTimeout);
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setRequestMethod(m_Method);
    for (String key: m_Headers.keySet())
      conn.setRequestProperty(key, m_Headers.get(key));
    for (String key: m_Cookies.keySet())
      conn.setRequestProperty(key, m_Cookies.get(key));

    if (m_FormData.size() > 0) {
      boundary = createBoundary();
      conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
      writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
      m_FormData.write(conn, writer, boundary);
      writer.close();
    }

    // response
    in = conn.getInputStream();
    content = new TByteArrayList();
    while ((b = in.read()) != -1)
      content.add((byte) b);

    result = new Response(conn.getResponseCode(), conn.getResponseMessage(), content.toArray());

    conn.disconnect();

    return result;
  }
}

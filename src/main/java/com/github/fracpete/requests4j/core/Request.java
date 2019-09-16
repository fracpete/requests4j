/*
 * Request.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

import com.github.fracpete.requests4j.auth.AbstractAuthentication;
import com.github.fracpete.requests4j.auth.NoAuthentication;
import com.github.fracpete.requests4j.form.FormData;

import java.io.BufferedWriter;
import java.io.IOException;
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

import static com.github.fracpete.requests4j.core.Method.POST;

/**
 * Encapsulates a request.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Request
  implements Serializable {

  /** the method. */
  protected Method m_Method;

  /** the URL to contact. */
  protected URL m_URL;

  /** the headers. */
  protected Map<String,String> m_Headers;

  /** the cookies. */
  protected Map<String,String> m_Cookies;

  /** the parameters. */
  protected Map<String,String> m_Parameters;

  /** the body to send. */
  protected Object m_Body;

  /** the form data. */
  protected FormData m_FormData;

  /** the authentication to use. */
  protected AbstractAuthentication m_Authentication;

  /** the connection timeout (msec), default if < 0. */
  protected int m_ConnectionTimeout;

  /** the read timeout (msec), default if < 0. */
  protected int m_ReadTimeout;

  /** whether to allow redirects. */
  protected boolean m_AllowRedirects;

  /** the maximum number of redirects. */
  protected int m_MaxRedirects;

  /**
   * Initializes the request.
   *
   * @param method	the method to use
   */
  public Request(Method method) {
    m_Method            = method;
    m_URL               = null;
    m_Headers           = new HashMap<>();
    m_Cookies           = new HashMap<>();
    m_Parameters        = new HashMap<>();
    m_Authentication    = new NoAuthentication();
    m_Body              = null;
    m_FormData          = new FormData();
    m_ConnectionTimeout = -1;
    m_ReadTimeout       = -1;
    m_AllowRedirects    = false;
    m_MaxRedirects      = 3;
  }

  /**
   * Returns the method.
   *
   * @return		the method
   */
  public Method method() {
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
   * Sets the body to send (POST, PUT, PATCH).
   *
   * @param body	the body
   * @return		itself
   * @see		Method#hasBody()
   */
  public Request body(String body) {
    if (m_Method.hasBody())
      m_Body = body;
    else
      System.err.println("Method " + m_Method + " does not support a body!");
    return this;
  }

  /**
   * Sets the body to send (POST, PUT, PATCH).
   *
   * @param body	the body
   * @return		itself
   * @see		Method#hasBody()
   */
  public Request body(byte[] body) {
    if (m_Method.hasBody())
      m_Body = body;
    else
      System.err.println("Method " + m_Method + " does not support a body!");
    return this;
  }

  /**
   * Returns the body to send.
   *
   * @return		the body (string or byte array)
   */
  public Object body() {
    return m_Body;
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
   * Returns whether to allow redirects (3xx).
   *
   * @param allow	true if to allow
   * @return		itself
   */
  public Request allowRedirects(boolean allow) {
    m_AllowRedirects = allow;
    return this;
  }

  /**
   * Returns whether redirects are allowed.
   *
   * @return		true if allowed
   */
  public boolean allowRedirects() {
    return m_AllowRedirects;
  }

  /**
   * Sets the maximum number of redirects to follow.
   *
   * @param max		the maximum
   * @return		itself
   */
  public Request maxRedirects(int max) {
    if (max < 0)
      max = 0;
    m_MaxRedirects = max;
    return this;
  }

  /**
   * Returns the maximum number of redirects to follow.
   *
   * @return		the maximum
   */
  public int maxRedirects() {
    return m_MaxRedirects;
  }

  /**
   * Assembles the full URL.
   *
   * @return		the URL
   * @throws MalformedURLException	if fails to assemble URL
   */
  protected URL assembleURL() throws Exception {
    URL 		result;
    Map<String,String>	params;
    StringBuilder	url;
    int			i;
    String		enc;

    // collect parameters for URL
    params = new HashMap<>();
    if (m_Method != POST) {
      params.putAll(m_Parameters);
      params.putAll(formData().parameters());
    }

    if (params.size() > 0) {
      enc = "UTF-8";
      url = new StringBuilder(m_URL.toString());
      i   = 0;
      for (String key: params.keySet()) {
        if (i == 0)
          url.append("?");
        else
          url.append("&");
        url.append(URLEncoder.encode(key, enc));
        url.append("=");
        url.append(URLEncoder.encode(params.get(key), enc));
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
   * Opens the connection.
   *
   * @param url		the URL to use
   * @return		the connection
   * @throws IOException	if connection fails
   */
  protected HttpURLConnection openConnection(URL url) throws IOException {
    HttpURLConnection 	result;
    BufferedWriter	writer;
    String		boundary;
    boolean		writeBody;

    result = (HttpURLConnection) url.openConnection();
    if (m_ConnectionTimeout >= 0)
      result.setConnectTimeout(m_ConnectionTimeout);
    if (m_ReadTimeout >= 0)
      result.setReadTimeout(m_ReadTimeout);
    result.setDoInput(true);
    result.setDoOutput(true);
    result.setRequestMethod(m_Method.toString());
    for (String key: m_Headers.keySet())
      result.setRequestProperty(key, m_Headers.get(key));
    for (String key: m_Cookies.keySet())
      result.setRequestProperty(key, m_Cookies.get(key));

    writeBody = false;
    if (m_Method.hasBody()) {
      if (m_Method == POST) {
	if (m_FormData.size() > 0) {
	  boundary = createBoundary();
	  result.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	  writer = new BufferedWriter(new OutputStreamWriter(result.getOutputStream()));
	  m_FormData.post(result, writer, boundary);
	  writer.close();
	}
	else if (body() != null) {
	  writeBody = true;
	}
      }
      else {
	writeBody = (body() != null);
      }
    }

    if (writeBody) {
      if (body() instanceof String) {
	writer = new BufferedWriter(new OutputStreamWriter(result.getOutputStream()));
	writer.write((String) body());
	writer.close();
      }
      else if (body() instanceof byte[]) {
        result.getOutputStream().write((byte[]) body());
      }
      else {
        throw new IllegalStateException("Unhandled body type (expected String or byte[]), found: " + body().getClass());
      }
    }

    return result;
  }

  /**
   * Gets the connection, handles redirects.
   *
   * @param url		the URL to open
   * @return		the connection
   * @throws IOException        if opening fails
   */
  protected HttpURLConnection getConnection(URL url) throws IOException {
    HttpURLConnection 	result;
    int 		status;
    String 		newURL;
    int 		redirectCount;

    result = openConnection(url);

    status        = result.getResponseCode();
    redirectCount = 0;

    while ((status == HttpURLConnection.HTTP_MOVED_TEMP)
      || (status == HttpURLConnection.HTTP_MOVED_PERM)
      || (status == HttpURLConnection.HTTP_SEE_OTHER)) {

      if (!m_AllowRedirects)
	throw new IOException("Received a redirect and no redirects allowed!");

      redirectCount++;
      if (redirectCount >= m_MaxRedirects)
	throw new IOException(m_MaxRedirects + " redirects were generated when trying to access " + url);

      newURL = result.getHeaderField("Location");
      System.out.println("Redirect, trying to open: " + newURL);
      result = openConnection(new URL(newURL));
      status = result.getResponseCode();
    }

    return result;
  }

  /**
   * Executes the request and stores the response in memory.
   *
   * @return		the generated response
   * @throws Exception	if execution fails
   */
  public Response execute() throws Exception {
    return execute(new Response());
  }

  /**
   * Executes the request, uses the supplied response instance for receiving the data.
   *
   * @param response 	the configured response container to use
   * @return		the generated response
   * @throws Exception	if execution fails
   */
  public <T extends HttpResponse> T execute(T response) throws Exception {
    HttpURLConnection	conn;
    URL 		url;
    InputStream		in;
    int			b;

    if (m_URL == null)
      throw new IllegalStateException("No URL provided!");

    m_Authentication.apply(this);
    url  = assembleURL();
    conn = getConnection(url);

    response.init(conn.getResponseCode(), conn.getResponseMessage(), conn.getHeaderFields());

    in = conn.getInputStream();
    while ((b = in.read()) != -1)
      response.appendBody((byte) b);
    response.finishBody();

    conn.disconnect();

    return response;
  }
}

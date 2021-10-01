/*
 * Request.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.request;

import com.github.fracpete.requests4j.attachment.AbstractAttachment;
import com.github.fracpete.requests4j.auth.AbstractAuthentication;
import com.github.fracpete.requests4j.auth.NoAuthentication;
import com.github.fracpete.requests4j.core.ByteArrayRequestBody;
import com.github.fracpete.requests4j.core.EmptyRequestBody;
import com.github.fracpete.requests4j.core.MediaTypeHelper;
import com.github.fracpete.requests4j.core.Resendable;
import com.github.fracpete.requests4j.core.StringRequestBody;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.event.RequestFailureEvent;
import com.github.fracpete.requests4j.event.RequestFailureListener;
import com.github.fracpete.requests4j.form.FormData;
import com.github.fracpete.requests4j.json.Array;
import com.github.fracpete.requests4j.json.Dictionary;
import com.github.fracpete.requests4j.json.Element;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.Response;
import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.io.Serializable;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * For building a request.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Request
  implements Serializable, Resendable {

  /** the method to use. */
  protected Method m_Method;

  /** the URL to connect to. */
  protected URL m_URL;

  /** the HTTP client instance to reuse. */
  protected OkHttpClient m_Client;

  /** whether the client needs to be closed. */
  protected boolean m_CloseClient;

  /** the cookies. */
  protected CookieManager m_Cookies;

  /** the authentication to use. */
  protected AbstractAuthentication m_Authentication;

  /** the proxy to use. */
  protected Proxy m_Proxy;

  /** the headers. */
  protected Map<String,String> m_Headers;

  /** the parameters. */
  protected Map<String,Object> m_Parameters;

  /** the body string to send. */
  protected Object m_Body;

  /** the form data. */
  protected FormData m_FormData;

  /** the attachment. */
  protected AbstractAttachment m_Attachment;

  /** the body media type. */
  protected MediaType m_BodyMediaType;

  /** the read timeout. */
  protected int m_ReadTimeout;

  /** the connect timeout. */
  protected int m_ConnectTimeout;

  /** the write timeout. */
  protected int m_WriteTimeout;

  /** whether to allow redirects. */
  protected boolean m_AllowRedirects;

  /** the maximum number of redirects. */
  protected int m_MaxRedirects;

  /** the execution listeners. */
  protected transient Set<RequestExecutionListener> m_ExecutionListeners;

  /** the failure listeners. */
  protected transient Set<RequestFailureListener> m_FailureListeners;

  /** the current redirect count. */
  protected int m_RedirectCount;

  /**
   * Initializes the request.
   *
   * @param method	the method to use
   */
  public Request(Method method) {
    m_Method          = method;
    m_URL             = null;
    m_Client          = null;
    m_CloseClient     = true;
    m_Cookies         = null;
    m_Authentication  = new NoAuthentication();
    m_Proxy           = null;
    m_Headers         = new HashMap<>();
    m_Parameters      = new HashMap<>();
    m_Body            = null;
    m_BodyMediaType   = MediaTypeHelper.OCTECT_STREAM;
    m_FormData        = new FormData();
    m_ReadTimeout     = -1;
    m_ConnectTimeout  = -1;
    m_WriteTimeout    = -1;
    m_AllowRedirects  = false;
    m_MaxRedirects    = 3;
    m_RedirectCount   = 0;
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
   * Sets the client to use.
   *
   * @param value 	the client
   * @return		itself
   */
  public Request client(OkHttpClient value) {
    m_Client      = value;
    m_CloseClient = false;
    return this;
  }

  /**
   * Returns the client to use. Instantiates it if necessary.
   *
   * @return		the client
   */
  public OkHttpClient client() {
    OkHttpClient.Builder  	builder;
    Authenticator		authenticator;

    if (m_Client == null) {
      builder = new OkHttpClient.Builder()
        .cookieJar(new JavaNetCookieJar(cookies()))
        .followRedirects(false);
      try {
	authenticator = auth().build();
	if (authenticator != null)
	  builder.authenticator(authenticator);
      }
      catch (Exception e) {
        System.err.println("Failed to build/set authenticator!");
        e.printStackTrace();
      }
      if ((m_ReadTimeout != -1) || (m_ConnectTimeout != -1) || (m_WriteTimeout != -1)) {
        if (m_ConnectTimeout != -1)
          builder.connectTimeout(m_ConnectTimeout, TimeUnit.SECONDS);
        if (m_ReadTimeout != -1)
          builder.readTimeout(m_ReadTimeout, TimeUnit.SECONDS);
        if (m_WriteTimeout != -1)
          builder.writeTimeout(m_WriteTimeout, TimeUnit.SECONDS);
      }
      if (m_Proxy != null)
        builder.proxy(m_Proxy);
      m_Client = builder.build();
    }

    return m_Client;
  }

  /**
   * Sets the cookies to use.
   *
   * @param value	the cookies
   */
  public Request cookies(CookieManager value) {
    m_Cookies = value;
    return this;
  }

  /**
   * Returns the cookies to use. Instantiates them if necessary.
   *
   * @return		the cookies
   */
  public CookieManager cookies() {
    if (m_Cookies == null) {
      m_Cookies = new CookieManager();
      m_Cookies.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }
    return m_Cookies;
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
   * Adds the query parameter.
   *
   * @param name	the name
   * @param values	the values
   * @return		itself
   */
  public Request parameter(String name, String[] values) {
    m_Parameters.put(name, Arrays.asList(values));
    return this;
  }

  /**
   * Adds the query parameter.
   *
   * @param name	the name
   * @param values	the list of values (automatically get converted to String)
   * @return		itself
   */
  public Request parameter(String name, List values) {
    List<String>	strValues;

    strValues = new ArrayList<>();
    for (Object value: values)
      strValues.add("" + value);
    m_Parameters.put(name, strValues);
    return this;
  }

  /**
   * Adds all the query parameters.
   * Values in the map can only be String, String[] or List<String>.
   *
   * @param parameters	the parameters
   * @return		itself
   */
  public Request parameters(Map<String,Object> parameters) {
    Object 	value;

    for (String key: parameters.keySet()) {
      value = parameters.get(key);
      if (value instanceof String)
	parameter(key, (String) value);
      else if (value instanceof String[])
	parameter(key, (String[]) value);
      else if (value instanceof List)
	parameter(key, (List) value);
      else
        System.err.println("Value of parameter '" + key + "' should be "
	  + "String, String[] or List, but found: " + value.getClass().getName());
    }
    return this;
  }

  /**
   * Returns the current query parameters (the values are either String or List<String>).
   *
   * @return		the parameters
   */
  public Map<String,Object> parameters() {
    return m_Parameters;
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
   * Sets the proxy to use.
   *
   * @param value	the proxy, null to unset proxy
   * @return		itself
   */
  public Request proxy(Proxy value) {
    m_Proxy = value;
    return this;
  }

  /**
   * Unsets the proxy in use.
   *
   * @return		itself
   */
  public Request noProxy() {
    m_Proxy = null;
    return this;
  }

  /**
   * Returns the proxy, if any.
   *
   * @return		the proxy, null if none set
   */
  public Proxy proxy() {
    return m_Proxy;
  }

  /**
   * The body to send. Uses text/plain.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(String value) {
    return body(value, MediaTypeHelper.TEXT_PLAIN);
  }

  /**
   * The body to send.
   *
   * @param value	the body
   * @param mediaType 	the media type
   * @return		itself
   */
  public Request body(String value, MediaType mediaType) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value;
    m_BodyMediaType = mediaType;
    return this;
  }

  /**
   * The body to send. Uses application/octet-stream.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(byte[] value) {
    return body(value, MediaTypeHelper.OCTECT_STREAM);
  }

  /**
   * The body to send.
   *
   * @param value	the body
   * @param mediaType 	the media type
   * @return		itself
   */
  public Request body(byte[] value, MediaType mediaType) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value;
    m_BodyMediaType = mediaType;
    return this;
  }

  /**
   * The JSON body to send.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(Dictionary value) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value.dump();
    m_BodyMediaType = MediaTypeHelper.APPLICATION_JSON_UTF8;
    return this;
  }

  /**
   * The JSON body to send.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(Array value) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value.dump();
    m_BodyMediaType = MediaTypeHelper.APPLICATION_JSON_UTF8;
    return this;
  }

  /**
   * Returns the body.
   *
   * @return		the body, null if not available
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
   * Attaches the specified data.
   *
   * @param data	the attachment
   * @return		itself
   */
  public Request attachment(AbstractAttachment data) {
    m_Attachment = data;
    return this;
  }

  /**
   * Returns any attachment.
   *
   * @return		the attachment, null if none set
   */
  public AbstractAttachment attachment() {
    return m_Attachment;
  }

  /**
   * Sets the read timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Request readTimeout(int value) {
    if (value < 1)
      value = -1;
    m_ReadTimeout = value;
    return this;
  }

  /**
   * Returns the read timeout.
   * 
   * @return		the timeout in msec, -1 for default
   */
  public int readTimeout() {
    return m_ReadTimeout;
  }

  /**
   * Sets the connect timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Request connectTimeout(int value) {
    if (value < 1)
      value = -1;
    m_ConnectTimeout = value;
    return this;
  }

  /**
   * Returns the connect timeout.
   * 
   * @return		the timeout in msec, -1 for default
   */
  public int connectTimeout() {
    return m_ConnectTimeout;
  }

  /**
   * Sets the write timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Request writeTimeout(int value) {
    if (value < 1)
      value = -1;
    m_WriteTimeout = value;
    return this;
  }

  /**
   * Returns the write timeout.
   * 
   * @return		the timeout in msec, -1 for default
   */
  public int writeTimeout() {
    return m_WriteTimeout;
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
   * Adds the execution listener.
   *
   * @param l		the listener
   */
  public synchronized Request addExecutionListener(RequestExecutionListener l) {
    if (m_ExecutionListeners == null)
      m_ExecutionListeners = new HashSet<>();
    m_ExecutionListeners.add(l);
    return this;
  }

  /**
   * Removes the execution listener.
   *
   * @param l		the listener
   */
  public synchronized Request removeExecutionListener(RequestExecutionListener l) {
    if (m_ExecutionListeners != null)
      m_ExecutionListeners.remove(l);
    return this;
  }

  /**
   * Sends the event to all execution listeners.
   *
   * @param e		the event to send
   */
  protected synchronized void notifyExecutionListeners(RequestExecutionEvent e) {
    if (m_ExecutionListeners == null)
      return;
    for (RequestExecutionListener l: m_ExecutionListeners)
      l.requestExecuted(e);
  }

  /**
   * Adds the failure listener.
   *
   * @param l		the listener
   */
  public synchronized Request addFailureListener(RequestFailureListener l) {
    if (m_FailureListeners == null)
      m_FailureListeners = new HashSet<>();
    m_FailureListeners.add(l);
    return this;
  }

  /**
   * Removes the failure listener.
   *
   * @param l		the listener
   */
  public synchronized Request removeFailureListener(RequestFailureListener l) {
    if (m_FailureListeners != null)
      m_FailureListeners.remove(l);
    return this;
  }

  /**
   * Sends the event to all failure listeners.
   *
   * @param e		the event to send
   */
  protected synchronized void notifyFailureListeners(RequestFailureEvent e) {
    if (m_FailureListeners == null)
      return;
    for (RequestFailureListener l: m_FailureListeners)
      l.requestFailed(e);
  }

  /**
   * Returns whether the status code represents a redirect.
   *
   * @param statusCode	the code to check
   * @return		true if redirect
   */
  public static boolean isRedirect(int statusCode) {
    return (statusCode == HttpURLConnection.HTTP_MOVED_TEMP)
      || (statusCode == HttpURLConnection.HTTP_MOVED_PERM)
      || (statusCode == HttpURLConnection.HTTP_SEE_OTHER);
  }

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  @Override
  public boolean canResend() {
    boolean	result;

    result = m_FormData.canResend();

    if (result && (m_Attachment != null))
      result = m_Attachment.canResend();

    return result;
  }

  /**
   * Assembles the full URL.
   *
   * @return		the URL
   * @throws MalformedURLException	if fails to assemble URL
   */
  protected URL assembleURL() throws Exception {
    URL 		result;
    Map<String,Object>	params;
    Object		value;
    URLBuilder		builder;

    // collect parameters for URL
    params = new HashMap<>();
    if (m_Method != Method.POST) {
      params.putAll(m_Parameters);
      params.putAll(formData().parameters());
    }

    if (params.size() > 0) {
      builder = new URLBuilder(m_URL);
      for (String key: params.keySet()) {
        value = params.get(key);
        if (value instanceof List)
          builder.append(key, (List) value);
        else
          builder.append(key, "" + value);
      }
      result = new URL(builder.build());
    }
    else {
      result = m_URL;
    }

    return result;
  }

  /**
   * Executes the request.
   *
   * @throws Exception	if execution fails
   */
  public BasicResponse execute() throws Exception {
    return execute(new BasicResponse());
  }

  /**
   * Executes the request.
   *
   * @throws Exception	if execution fails
   */
  protected <T extends Response > T doExecute(T response) throws Exception {
    okhttp3.Request.Builder 	builder;
    okhttp3.Request		request;
    okhttp3.RequestBody		body;
    okhttp3.Response		resp;
    Call			call;
    URL				url;

    url  = assembleURL();
    body = null;
    try {
      builder = new okhttp3.Request.Builder()
	.url(url);

      switch (m_Method) {
	case GET:
	  builder.get();
	  break;
	case POST:
	  builder.post(new EmptyRequestBody());  // dummy, in case nothing is being posted
	  break;
	case DELETE:
	  builder.delete();
	  break;
	case HEAD:
	  builder.head();
	  break;
      }

      // headers
      for (String header: headers().keySet())
        builder.addHeader(header, headers().get(header));

      // form data
      if ((m_Method == Method.POST) && (m_FormData.size() > 0)) {
	m_FormData.add(builder);
      }
      else if ((m_Method == Method.POST) && (m_Attachment != null) && m_Attachment.isValid()) {
        builder.addHeader("Content-Disposition", m_Attachment.getContentDisposition());
	builder.post(m_Attachment.getBody());
      }
      else if ((m_Body != null) && (m_Method.hasBody())) {
	if (m_Body instanceof String)
	  body = new StringRequestBody(m_BodyMediaType, (String) m_Body);
	else if (m_Body instanceof byte[])
	  body = new ByteArrayRequestBody(m_BodyMediaType, (byte[]) m_Body);
	else
	  throw new IllegalStateException("Unhandled body type: " + m_Body.getClass().getName());
	switch (m_Method) {
	  case GET:
	    builder.method("GET", body);
	    break;
	  case POST:
	    builder.post(body);
	    break;
	  case PUT:
	    builder.put(body);
	    break;
	  case PATCH:
	    builder.patch(body);
	    break;
	  case DELETE:
	    builder.delete(body);
	    break;
	  default:
	    throw new IllegalStateException("Unhandled method: " + m_Method);
	}
      }

      request = builder.build();
      call    = client().newCall(request);
      resp    = call.execute();
      response.init(resp);

      try {
	resp.close();
      }
      catch (Exception e) {
        // ignored
      }

      notifyExecutionListeners(new RequestExecutionEvent(this, response));

      return response;
    }
    catch (Throwable t) {
      notifyFailureListeners(new RequestFailureEvent(this, t));
      throw t;
    }
    finally {
      // close any open streams
      m_FormData.cleanUp();
    }
  }

  /**
   * Executes the request.
   *
   * @throws Exception	if execution fails
   */
  public <T extends Response > T execute(T response) throws Exception {
    int 		status;
    String 		newURL;
    String 		server;

    response        = doExecute(response);
    status          = response.statusCode();
    m_RedirectCount = 0;

    while (isRedirect(status)) {
      if (!m_AllowRedirects)
	throw new IOException("Received a redirect and no redirects allowed!");

      m_RedirectCount++;
      if (m_RedirectCount >= m_MaxRedirects)
	throw new IOException(m_MaxRedirects + " redirects were generated when trying to access " + m_URL);

      newURL = response.rawResponse().headers("Location").get(0);
      // relative redirect?
      if (newURL.startsWith("/")) {
        server = m_URL.getProtocol() + "://" + m_URL.getHost();
        if (m_URL.getPort() != -1)
          server += ":" + m_URL.getPort();
        newURL = server + newURL;
      }
      System.out.println("Redirect, trying to open: " + newURL);
      url(newURL);
      response = doExecute(response);
      status   = response.statusCode();
    }

    return response;
  }
}

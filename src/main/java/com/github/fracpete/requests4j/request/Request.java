/*
 * Request.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.request;

import com.github.fracpete.requests4j.attachment.AbstractAttachment;
import com.github.fracpete.requests4j.auth.AbstractAuthentication;
import com.github.fracpete.requests4j.auth.NoAuthentication;
import com.github.fracpete.requests4j.core.Resendable;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.event.RequestFailureEvent;
import com.github.fracpete.requests4j.event.RequestFailureListener;
import com.github.fracpete.requests4j.form.FormData;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.response.Response;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  protected CloseableHttpClient m_Client;

  /** whether the client needs to be closed. */
  protected boolean m_CloseClient;

  /** the cookies. */
  protected CookieStore m_Cookies;

  /** for credentials. */
  protected CredentialsProvider m_Credentials;

  /** the authentication to use. */
  protected AbstractAuthentication m_Authentication;

  /** the proxy to use. */
  protected HttpHost m_Proxy;

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

  /** the body content type. */
  protected ContentType m_BodyContentType;

  /** the socket timeout. */
  protected int m_SocketTimeout;

  /** the connect timeout. */
  protected int m_ConnectTimeout;

  /** the connection request timeout. */
  protected int m_ConnectionRequestTimeout;

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
    m_Credentials     = null;
    m_Authentication  = new NoAuthentication();
    m_Proxy           = null;
    m_Headers         = new HashMap<>();
    m_Parameters      = new HashMap<>();
    m_Body            = null;
    m_BodyContentType = ContentType.APPLICATION_OCTET_STREAM;
    m_FormData        = new FormData();
    m_SocketTimeout   = -1;
    m_ConnectTimeout  = -1;
    m_ConnectionRequestTimeout = -1;
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
  public Request client(CloseableHttpClient value) {
    m_Client      = value;
    m_CloseClient = false;
    return this;
  }

  /**
   * Returns the client to use. Instantiates it if necessary.
   *
   * @return		the client
   */
  public CloseableHttpClient client() {
    if (m_Client == null)
      m_Client = HttpClients.custom()
	.setDefaultCookieStore(cookies())
	.setDefaultCredentialsProvider(credentials())
	.disableRedirectHandling()
	.build();
    return m_Client;
  }

  /**
   * Sets the cookies to use.
   *
   * @param value	the cookies
   */
  public Request cookies(CookieStore value) {
    m_Cookies = value;
    return this;
  }

  /**
   * Sets the cookies to use.
   *
   * @param value	the cookies
   */
  public Request cookies(Map<String,String> value) {
    for (String key: value.keySet())
      cookies().addCookie(new BasicClientCookie(key, value.get(key)));
    return this;
  }

  /**
   * Returns the cookies to use. Instantiates them if necessary.
   *
   * @return		the cookies
   */
  public CookieStore cookies() {
    if (m_Cookies == null)
      m_Cookies = new BasicCookieStore();
    return m_Cookies;
  }

  /**
   * Adds the cookie.
   *
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public Request cookie(String name, String value) {
    return cookie(null, null, name, value);
  }

  /**
   * Adds the cookie.
   *
   * @param domain 	the domain this cookie is for, ignored if null
   * @param path	the path for the cookie, ignored if null
   * @param name	the name
   * @param value	the value
   * @return		itself
   */
  public Request cookie(String domain, String path, String name, String value) {
    BasicClientCookie	cookie;

    cookie = new BasicClientCookie(name, value);
    if (domain != null)
      cookie.setDomain(domain);
    if (path != null)
      cookie.setPath(path);
    m_Cookies.addCookie(cookie);
    return this;
  }

  /**
   * Sets the credentials to use.
   *
   * @param value	the credentials
   * @return		itself
   */
  public Request credentials(CredentialsProvider value) {
    m_Credentials = value;
    return this;
  }

  /**
   * Returns the credentials to use. Instantiates them if necessary.
   *
   * @return		the credentials
   */
  public CredentialsProvider credentials() {
    if (m_Credentials == null)
      m_Credentials = new BasicCredentialsProvider();
    return m_Credentials;
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
   * @param host	the host, DNS name
   * @param port 	the port, eg 80
   * @param scheme 	the scheme, eg http
   * @return		itself
   */
  public Request proxy(String host, int port, String scheme) {
    m_Proxy = new HttpHost(host, port, scheme);
    return this;
  }

  /**
   * Sets the proxy to use.
   *
   * @param value	the proxy, null to unset proxy
   * @return		itself
   */
  public Request proxy(HttpHost value) {
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
  public HttpHost proxy() {
    return m_Proxy;
  }

  /**
   * The body to send. Uses {@link ContentType#TEXT_PLAIN}.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(String value) {
    return body(value, ContentType.TEXT_PLAIN);
  }

  /**
   * The body to send.
   *
   * @param value		the body
   * @param contentType 	the content type
   * @return			itself
   */
  public Request body(String value, ContentType contentType) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value;
    m_BodyContentType = contentType;
    return this;
  }

  /**
   * The body to send. Uses {@link ContentType#APPLICATION_OCTET_STREAM}.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(byte[] value) {
    return body(value, ContentType.APPLICATION_OCTET_STREAM);
  }

  /**
   * The body to send.
   *
   * @param value	the body
   * @return		itself
   */
  public Request body(byte[] value, ContentType contentType) {
    if (!m_Method.hasBody())
      throw new IllegalArgumentException("Method " + m_Method + " does not support a body!");
    m_Body = value;
    m_BodyContentType = contentType;
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
   * Sets the socket timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Request socketTimeout(int value) {
    if (value < 1)
      value = -1;
    m_SocketTimeout = value;
    return this;
  }

  /**
   * Returns the socket timeout.
   * 
   * @return		the timeout in msec, -1 for default
   */
  public int socketTimeout() {
    return m_SocketTimeout;
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
   * Sets the connection request timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Request connectionRequestTimeout(int value) {
    if (value < 1)
      value = -1;
    m_ConnectionRequestTimeout = value;
    return this;
  }

  /**
   * Returns the connection request timeout.
   * 
   * @return		the timeout in msec, -1 for default
   */
  public int connectionRequestTimeout() {
    return m_ConnectionRequestTimeout;
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
  protected boolean isRedirect(int statusCode) {
    return (statusCode == HttpURLConnection.HTTP_MOVED_TEMP)
      || (statusCode == HttpURLConnection.HTTP_MOVED_PERM)
      || (statusCode == HttpURLConnection.HTTP_SEE_OTHER);
  }

  /**
   * Returns whether the client can be closed.
   *
   * @param response	the current response, can be null
   * @return		true if client can be closed
   */
  protected boolean canCloseClient(Response response) {
    boolean	result;

    result = m_CloseClient;

    if (m_AllowRedirects && (response != null) && isRedirect(response.statusCode()))
      result = (m_RedirectCount >= m_MaxRedirects);

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
    StringBuilder	url;
    int			i;
    String		enc;
    Object		value;
    List		list;

    // collect parameters for URL
    params = new HashMap<>();
    if (m_Method != Method.POST) {
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
        value = params.get(key);
        if (value instanceof List) {
          list = (List) value;
	}
	else {
          list = new ArrayList();
          list.add(value);
	}
	for (Object item: list) {
	  url.append(URLEncoder.encode(key, enc));
	  url.append("=");
	  url.append(URLEncoder.encode("" + item, enc));
	  i++;
	}
      }
      result = new URL(url.toString());
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
    HttpRequestBase		request;
    CloseableHttpResponse	resp;
    HttpClientContext		context;
    URL				url;
    RequestConfig.Builder	configBuilder;

    url = assembleURL();
    try {
      switch (m_Method) {
	case GET:
	  request = new HttpGet(url.toURI());
	  break;
	case POST:
	  request = new HttpPost(url.toURI());
	  break;
	case DELETE:
	  request = new HttpDelete(url.toURI());
	  break;
	case PUT:
	  request = new HttpPut(url.toURI());
	  break;
	case HEAD:
	  request = new HttpHead(url.toURI());
	  break;
	case PATCH:
	  request = new HttpPatch(url.toURI());
	  break;
	case OPTIONS:
	  request = new HttpOptions(url.toURI());
	  break;
	default:
	  throw new IllegalStateException("Unsupported method: " + m_Method);
      }

      configBuilder = null;

      // set timeouts
      if ((m_SocketTimeout != -1) || (m_ConnectTimeout != -1) || (m_ConnectionRequestTimeout != -1)) {
        if (configBuilder == null)
	  configBuilder = RequestConfig.custom();
        if (m_SocketTimeout != -1)
          configBuilder.setSocketTimeout(m_SocketTimeout);
        if (m_ConnectTimeout != -1)
          configBuilder.setConnectTimeout(m_ConnectTimeout);
        if (m_ConnectionRequestTimeout != -1)
          configBuilder.setConnectionRequestTimeout(m_ConnectionRequestTimeout);
      }

      // proxy?
      if (m_Proxy != null) {
        if (configBuilder == null)
	  configBuilder = RequestConfig.custom();
        configBuilder.setProxy(m_Proxy);
      }

      if (configBuilder != null)
        request.setConfig(configBuilder.build());

      // headers
      for (String header: headers().keySet())
        request.addHeader(header, headers().get(header));

      // form data
      if ((request instanceof HttpPost) && (m_FormData.size() > 0)) {
	m_FormData.add((HttpPost) request);
      }
      else if ((request instanceof HttpPost) && (m_Attachment != null) && m_Attachment.isValid()) {
        request.addHeader("Content-Disposition", m_Attachment.getContentDisposition());
	((HttpPost) request).setEntity(m_Attachment.getEntity());
      }
      else if ((m_Body != null) && (request instanceof HttpEntityEnclosingRequest)) {
	if (m_Body instanceof String)
	  ((HttpEntityEnclosingRequest) request).setEntity(new StringEntity((String) m_Body, m_BodyContentType));
	else if (m_Body instanceof byte[])
	  ((HttpEntityEnclosingRequest) request).setEntity(new ByteArrayEntity((byte[]) m_Body, m_BodyContentType));
	else
	  throw new IllegalStateException("Unhandled body type: " + m_Body.getClass().getName());
      }

      context = m_Authentication.build(this);
      if (context == null)
	resp = client().execute(request);
      else
	resp = client().execute(request, context);
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

      // close client?
      if (canCloseClient(response)) {
        try {
          m_Client.close();
	}
	catch (Exception e) {
          // ignored
	}
      }
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

      newURL = response.rawResponse().getFirstHeader("Location").getValue();
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

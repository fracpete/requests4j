/*
 * Session.java
 * Copyright (C) 2019-2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j;

import com.github.fracpete.requests4j.auth.AbstractAuthentication;
import com.github.fracpete.requests4j.auth.NoAuthentication;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.event.RequestFailureEvent;
import com.github.fracpete.requests4j.event.RequestFailureListener;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.Response;
import okhttp3.Authenticator;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;

import java.io.Serializable;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Session object for making requests, maintains client/cookies and adds them automatically
 * to the requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Session
  implements Serializable, RequestExecutionListener, RequestFailureListener {

  /** the client. */
  protected OkHttpClient m_Client;

  /** the cookies. */
  protected CookieManager m_Cookies;

  /** for authentication. */
  protected AbstractAuthentication m_Authentication;

  /** the proxy to use. */
  protected Proxy m_Proxy;

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

  /**
   * Initializes the session with default values.
   */
  public Session() {
    m_Client          = null;
    m_Cookies         = null;
    m_Proxy           = null;
    m_ReadTimeout     = -1;
    m_ConnectTimeout  = -1;
    m_WriteTimeout    = -1;
    m_AllowRedirects  = false;
    m_MaxRedirects    = 3;
  }

  /**
   * Sets the proxy to use.
   *
   * @param value	the proxy, null to unset proxy
   * @return		itself
   */
  public Session proxy(Proxy value) {
    m_Proxy = value;
    return this;
  }

  /**
   * Unsets the proxy in use.
   *
   * @return		itself
   */
  public Session noProxy() {
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
   * Sets the read timeout.
   *
   * @param value	the timeout in msec, use -1 for default
   * @return		itself
   */
  public Session readTimeout(int value) {
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
  public Session connectTimeout(int value) {
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
  public Session writeTimeout(int value) {
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
  public Session allowRedirects(boolean allow) {
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
  public Session maxRedirects(int max) {
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
   * Sets the cookies and adds itself as execution listener to the request.
   *
   * @param request	the request to update
   * @return		the updated request
   */
  protected Request process(Request request) {
    request.client(client());
    request.cookies(cookies());
    request.auth(auth());
    request.allowRedirects(allowRedirects());
    request.maxRedirects(maxRedirects());
    return request;
  }

  /**
   * Gets called after a Request has been executed an a Response object generated.
   * Updates the cookies if the response has a status code < 400.
   * Removes itself again from the execution listeners.
   *
   * @param e		the event
   * @see		Response#ok()
   */
  public void requestExecuted(RequestExecutionEvent e) {
    e.getRequest().removeExecutionListener(this);
    e.getRequest().removeFailureListener(this);
  }

  /**
   * Gets called when a Request fails.
   * Removes itself as listener from the Request object.
   *
   * @param e		the event
   */
  public void requestFailed(RequestFailureEvent e) {
    e.getRequest().removeExecutionListener(this);
    e.getRequest().removeFailureListener(this);
  }

  /**
   * Returns the client to use.
   *
   * @return		the client
   */
  public synchronized OkHttpClient client() {
    OkHttpClient.Builder	builder;
    Authenticator		authenticator;

    if (m_Client == null) {
      builder = new OkHttpClient.Builder()
        .cookieJar(new JavaNetCookieJar(cookies()));
      if (m_ConnectTimeout != -1)
        builder.connectTimeout(m_ConnectTimeout, TimeUnit.SECONDS);
      if (m_ReadTimeout != -1)
        builder.readTimeout(m_ReadTimeout, TimeUnit.SECONDS);
      if (m_WriteTimeout != -1)
        builder.writeTimeout(m_WriteTimeout, TimeUnit.SECONDS);
      if (m_Proxy != null)
        builder.proxy(m_Proxy);
      try {
	authenticator = auth().build();
	if (authenticator != null)
	  builder.authenticator(authenticator);
      }
      catch (Exception e) {
        System.err.println("Failed to build/set authenticator!");
        e.printStackTrace();
      }
      m_Client = builder.build();
    }
    return m_Client;
  }

  /**
   * Returns the currently stored cookies.
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
   * Returns the credentials.
   *
   * @return		the credentials
   */
  public AbstractAuthentication auth() {
    if (m_Authentication == null)
      m_Authentication = new NoAuthentication();
    return m_Authentication;
  }

  /**
   * Instantiates a new GET request.
   *
   * @return		the request
   */
  public Request get() {
    return process(Requests.get());
  }

  /**
   * Instantiates a new GET request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request get(String url) throws MalformedURLException {
    return process(Requests.get(url));
  }

  /**
   * Instantiates a new GET request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request get(URL url) {
    return process(Requests.get(url));
  }

  /**
   * Instantiates a new POST request.
   *
   * @return		the request
   */
  public Request post() {
    return process(Requests.post());
  }

  /**
   * Instantiates a new POST request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request post(String url) throws MalformedURLException {
    return process(Requests.post(url));
  }

  /**
   * Instantiates a new POST request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request post(URL url) {
    return process(Requests.post(url));
  }

  /**
   * Instantiates a new PUT request.
   *
   * @return		the request
   */
  public Request put() {
    return process(Requests.put());
  }

  /**
   * Instantiates a new PUT request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request put(String url) throws MalformedURLException {
    return process(Requests.put(url));
  }

  /**
   * Instantiates a new PUT request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request put(URL url) {
    return process(Requests.put(url));
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @return		the request
   */
  public Request patch() {
    return process(Requests.patch());
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request patch(String url) throws MalformedURLException {
    return process(Requests.patch(url));
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request patch(URL url) {
    return process(Requests.patch(url));
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @return		the request
   */
  public Request head() {
    return process(Requests.head());
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request head(String url) throws MalformedURLException {
    return process(Requests.head(url));
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request head(URL url) {
    return process(Requests.head(url));
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @return		the request
   */
  public Request options() {
    return process(Requests.options());
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request options(String url) throws MalformedURLException {
    return process(Requests.options(url));
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request options(URL url) {
    return process(Requests.options(url));
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @return		the request
   */
  public Request delete() {
    return process(Requests.delete());
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request delete(String url) throws MalformedURLException {
    return process(Requests.delete(url));
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public Request delete(URL url) {
    return process(Requests.delete(url));
  }

  /**
   * Closes the client, if necessary.
   */
  public void close() {
  }

  /**
   * Returns a short description of the session.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "cookies=" + cookies();
  }
}

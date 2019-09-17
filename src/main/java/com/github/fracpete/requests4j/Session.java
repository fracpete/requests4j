/*
 * Session.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j;

import com.github.fracpete.requests4j.core.HttpResponse;
import com.github.fracpete.requests4j.core.Request;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.event.RequestFailureEvent;
import com.github.fracpete.requests4j.event.RequestFailureListener;
import com.github.fracpete.requests4j.ssl.NoHostnameVerification;

import javax.net.ssl.HostnameVerifier;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Session object for making requests, maintains and adds them automatically
 * to the requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Session
  implements Serializable, RequestExecutionListener, RequestFailureListener {

  /** the cookies. */
  protected Map<String,String> m_Cookies;

  /** the hostname verification to use for the session. */
  protected HostnameVerifier m_HostnameVerification;

  /**
   * Initializes the session.
   */
  public Session() {
    m_Cookies              = new HashMap<>();
    m_HostnameVerification = null;
  }

  /**
   * Sets the cookies and adds itself as execution listener to the request.
   *
   * @param request	the request to update
   * @return		the updated request
   */
  protected Request process(Request request) {
    request.cookies(m_Cookies);
    if (m_HostnameVerification != null)
      request.hostnameVerification(m_HostnameVerification);
    request.addExecutionListener(this);
    request.addFailureListener(this);
    return request;
  }

  /**
   * Gets called after a Request has been executed an a Response object generated.
   * Updates the cookies if the response has a status code < 400.
   * Removes itself again from the execution listeners.
   *
   * @param e		the event
   * @see		HttpResponse#ok()
   */
  public void requestExecuted(RequestExecutionEvent e) {
    if (e.getReponse().ok())
      m_Cookies.putAll(e.getReponse().cookies());
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
   * Sets the hostname verifier to use (https only).
   *
   * @param verifier	the verifier to use, null to use system default
   * @return		itself
   */
  public Session hostnameVerification(HostnameVerifier verifier) {
    m_HostnameVerification = verifier;
    return this;
  }

  /**
   * Disables hostname verification (https only).
   *
   * @return		itself
   */
  public Session disableHostnameVerification() {
    return hostnameVerification(new NoHostnameVerification());
  }

  /**
   * Returns the current hostname verification scheme.
   *
   * @return		the scheme, null if none set
   */
  public HostnameVerifier hostnameVerification() {
    return m_HostnameVerification;
  }

  /**
   * Returns the currently stored cookies.
   *
   * @return		the cookies
   */
  public Map<String,String> cookies() {
    return m_Cookies;
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
   * Returns a short description of the session.
   *
   * @return		the description
   */
  @Override
  public String toString() {
    return "cookies=" + cookies()
      + ", hostnameVerification=" + (hostnameVerification() == null ? "-none-" : hostnameVerification().toString());
  }
}

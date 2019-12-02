/*
 * Session.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j;

import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.Response;
import com.github.fracpete.requests4j.event.RequestExecutionEvent;
import com.github.fracpete.requests4j.event.RequestExecutionListener;
import com.github.fracpete.requests4j.event.RequestFailureEvent;
import com.github.fracpete.requests4j.event.RequestFailureListener;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Session object for making requests, maintains client/cookies and adds them automatically
 * to the requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Session
  implements Serializable, RequestExecutionListener, RequestFailureListener {

  /** the client. */
  protected CloseableHttpClient m_Client;

  /** the cookies. */
  protected CookieStore m_Cookies;

  /** for storing credentials. */
  protected CredentialsProvider m_Credentials;

  /**
   * Sets the cookies and adds itself as execution listener to the request.
   *
   * @param request	the request to update
   * @return		the updated request
   */
  protected Request process(Request request) {
    request.client(client());
    request.cookies(cookies());
    request.credentials(credentials());
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
  public synchronized CloseableHttpClient client() {
    if (m_Client == null)
      m_Client = HttpClients.custom()
	.setDefaultCookieStore(cookies())
	.setDefaultCredentialsProvider(credentials())
	.build();
    return m_Client;
  }

  /**
   * Returns the currently stored cookies.
   *
   * @return		the cookies
   */
  public CookieStore cookies() {
    if (m_Cookies == null)
       m_Cookies = new BasicCookieStore();
    return m_Cookies;
  }

  /**
   * Returns the credentials.
   *
   * @return		the credentials
   */
  public CredentialsProvider credentials() {
    if (m_Credentials == null)
      m_Credentials = new BasicCredentialsProvider();
    return m_Credentials;
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
    if (m_Client != null) {
      try {
	m_Client.close();
      }
      catch (Exception e) {
	// ignored
      }
    }
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

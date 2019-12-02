/*
 * Requests.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j;

import com.github.fracpete.requests4j.request.Request;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.fracpete.requests4j.request.Method.DELETE;
import static com.github.fracpete.requests4j.request.Method.GET;
import static com.github.fracpete.requests4j.request.Method.HEAD;
import static com.github.fracpete.requests4j.request.Method.OPTIONS;
import static com.github.fracpete.requests4j.request.Method.PATCH;
import static com.github.fracpete.requests4j.request.Method.POST;
import static com.github.fracpete.requests4j.request.Method.PUT;

/**
 * Main class for generating new requests.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Requests {

  /**
   * Instantiates a new GET request.
   *
   * @return		the request
   */
  public static Request get() {
    return new Request(GET);
  }

  /**
   * Instantiates a new GET request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request get(String url) throws MalformedURLException {
    return get().url(url);
  }

  /**
   * Instantiates a new GET request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request get(URL url) {
    return get().url(url);
  }

  /**
   * Instantiates a new POST request.
   *
   * @return		the request
   */
  public static Request post() {
    return new Request(POST);
  }

  /**
   * Instantiates a new POST request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request post(String url) throws MalformedURLException {
    return post().url(url);
  }

  /**
   * Instantiates a new POST request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request post(URL url) {
    return post().url(url);
  }

  /**
   * Instantiates a new PUT request.
   *
   * @return		the request
   */
  public static Request put() {
    return new Request(PUT);
  }

  /**
   * Instantiates a new PUT request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request put(String url) throws MalformedURLException {
    return put().url(url);
  }

  /**
   * Instantiates a new PUT request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request put(URL url) {
    return put().url(url);
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @return		the request
   */
  public static Request patch() {
    return new Request(PATCH);
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request patch(String url) throws MalformedURLException {
    return patch().url(url);
  }

  /**
   * Instantiates a new PATCH request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request patch(URL url) {
    return patch().url(url);
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @return		the request
   */
  public static Request head() {
    return new Request(HEAD);
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request head(String url) throws MalformedURLException {
    return head().url(url);
  }

  /**
   * Instantiates a new HEAD request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request head(URL url) {
    return head().url(url);
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @return		the request
   */
  public static Request options() {
    return new Request(OPTIONS);
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request options(String url) throws MalformedURLException {
    return options().url(url);
  }

  /**
   * Instantiates a new OPTIONS request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request options(URL url) {
    return options().url(url);
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @return		the request
   */
  public static Request delete() {
    return new Request(DELETE);
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request delete(String url) throws MalformedURLException {
    return delete().url(url);
  }

  /**
   * Instantiates a new DELETE request.
   *
   * @param url 	the URL to contact
   * @return		the request
   */
  public static Request delete(URL url) {
    return delete().url(url);
  }
}

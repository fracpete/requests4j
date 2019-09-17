/*
 * RequestExecutionEvent.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.event;

import com.github.fracpete.requests4j.core.HttpResponse;
import com.github.fracpete.requests4j.core.Request;

import java.util.EventObject;

/**
 * Event that gets sent after a Request execution.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RequestExecutionEvent
  extends EventObject {

  /** the response object. */
  protected HttpResponse m_Response;

  /**
   * Initializes the event.
   *
   * @param request 	the request that triggered the event
   * @param response 	the assoicated response object
   * @throws IllegalArgumentException if source is null
   */
  public RequestExecutionEvent(Request request, HttpResponse response) {
    super(request);
    m_Response = response;
  }

  /**
   * Returns the Request object.
   *
   * @return		the request
   */
  public Request getRequest() {
    return (Request) getSource();
  }

  /**
   * Returns the response.
   *
   * @return		the response
   */
  public HttpResponse getReponse() {
    return m_Response;
  }

  /**
   * Returns the response.
   *
   * @param response	the class to cast to
   * @param <T>		the type of response class to cast to
   * @return		the response
   */
  public <T extends HttpResponse> T getResponse(Class<T> response) {
    return (T) m_Response;
  }
}

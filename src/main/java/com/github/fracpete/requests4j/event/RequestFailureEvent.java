/*
 * RequestFailureEvent.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.event;

import com.github.fracpete.requests4j.request.Request;

import java.util.EventObject;

/**
 * Event that gets sent when a Request fails.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class RequestFailureEvent
  extends EventObject {

  /** the exception. */
  protected Throwable m_Exception;

  /**
   * Initializes the event.
   *
   * @param request 	the request that failed
   * @param exception 	the exception that got thrown
   * @throws IllegalArgumentException if source is null
   */
  public RequestFailureEvent(Request request, Throwable exception) {
    super(request);
    m_Exception = exception;
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
  public Throwable getException() {
    return m_Exception;
  }
}

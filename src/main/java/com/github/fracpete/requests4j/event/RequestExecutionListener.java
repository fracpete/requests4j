/*
 * RequestExecutionListener.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.event;

/**
 * Interface for listeners that get notified when Requests have been executed.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface RequestExecutionListener {

  /**
   * Gets called after a Request has been executed an a Response object generated.
   *
   * @param e		the event
   */
  public void requestExecuted(RequestExecutionEvent e);
}

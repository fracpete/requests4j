/*
 * RequestFailureListener.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.v2.event;

/**
 * Interface for listeners that get notified when Requests fail.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface RequestFailureListener {

  /**
   * Gets called when a Request fails.
   *
   * @param e		the event
   */
  public void requestFailed(RequestFailureEvent e);
}

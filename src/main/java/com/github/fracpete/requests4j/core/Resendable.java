/*
 * Resendable.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.core;

/**
 * Interface for objects to determine whether data can be resent.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public interface Resendable {

  /**
   * Returns true if the object can resend its data.
   *
   * @return		true if can be resent
   */
  public boolean canResend();
}

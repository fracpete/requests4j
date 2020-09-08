/*
 * AbstractAttachment.java
 * Copyright (C) 2020 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.attachment;

import com.github.fracpete.requests4j.core.Resendable;
import okhttp3.RequestBody;

import java.io.Serializable;

/**
 * Ancestor for attachments.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractAttachment
  implements Serializable, Resendable {

  /**
   * Returns whether the attachment is correctly configured.
   *
   * @return		true if properly configured
   */
  public abstract boolean isValid();

  /**
   * Returns the content disposition to use.
   *
   * @return		the value for the 'Content-Disposition' header
   */
  public abstract String getContentDisposition();

  /**
   * Returns the request body.
   *
   * @return		the body
   */
  public abstract RequestBody getBody();

  /**
   * Returns a short description.
   *
   * @return		the description
   */
  public abstract String toString();
}

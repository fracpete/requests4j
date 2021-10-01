/*
 * Element.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.json;

import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Ancestor for Json elements.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class Element<T extends com.google.gson.JsonElement>
  implements Serializable {

  /** stores the json data. */
  protected T m_Data;

  /**
   * Returns the underlying json element.
   *
   * @return the JSON element
   */
  public T toJson() {
    return m_Data;
  }

  /**
   * Returns a string representation with no pretty-printing.
   *
   * @return the string representation
   */
  public String dump() {
    return dump(-1);
  }

  /**
   * Returns a string representation with optional pretty-printing.
   *
   * @param indentation if >0 then pretty printed
   * @return the string representation
   */
  public String dump(int indentation) {
    GsonBuilder builder;

    builder = new GsonBuilder();
    if (indentation > 0)
      builder.setPrettyPrinting();

    return builder.create().toJson(m_Data);
  }

  /**
   * Returns the dictionary as json string.
   *
   * @return the json string
   * @see #dump()
   */
  public String toString() {
    return dump();
  }
}

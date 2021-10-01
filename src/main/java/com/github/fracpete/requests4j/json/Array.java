/*
 * Array.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

/**
 * Encapsulates a Json array.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Array
  extends Element<JsonArray> {

  /**
   * Initializes the array.
   */
  public Array() {
    m_Data = new JsonArray();
  }

  /**
   * Initializes the array.
   *
   * @param array the data to initialize with
   */
  public Array(JsonArray array) {
    m_Data = array;
  }

  /**
   * Initializes the dictionary from the JSON string.
   *
   * @param json the string to parse
   */
  public Array(String json) {
    this();
    Gson gson = new Gson();
    m_Data = gson.fromJson(json, JsonArray.class);
  }

  /**
   * Adds the character array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(char... values) {
    for (char value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the boolean array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(boolean... values) {
    for (boolean value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the byte array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(byte... values) {
    for (byte value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the short array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(short... values) {
    for (short value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the int array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(int... values) {
    for (int value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the long array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(long... values) {
    for (long value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the float array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(float... values) {
    for (float value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the double array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(double... values) {
    for (double value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Adds the string array.
   *
   * @param values the array values to store
   * @return itself
   */
  public Array add(String... values) {
    for (String value: values)
      m_Data.add(value);
    return this;
  }

  /**
   * Returns the number of array elements currently stored.
   *
   * @return the number of elements
   */
  public int size() {
    return m_Data.size();
  }

  /**
   * Returns a new array.
   *
   * @return the new instance
   */
  public static Array newArray() {
    return new Array();
  }

  /**
   * Returns a new array.
   *
   * @param json the json string to parse
   * @return the new instance
   */
  public static Array parse(String json) {
    return new Array(json);
  }
}

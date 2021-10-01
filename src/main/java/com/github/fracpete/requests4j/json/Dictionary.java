/*
 * Dictionary.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Set;

import static com.github.fracpete.requests4j.json.Array.newArray;

/**
 * Encapsulates a Json object.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Dictionary
  extends Element<JsonObject> {

  /**
   * Initializes the dictionary.
   */
  public Dictionary() {
    m_Data = new JsonObject();
  }

  /**
   * Initializes the dictionary.
   *
   * @param dict the object to initialize with
   */
  public Dictionary(JsonObject dict) {
    m_Data = dict;
  }

  /**
   * Initializes the dictionary from the JSON string.
   */
  public Dictionary(String json) {
    this();
    Gson gson = new Gson();
    m_Data = gson.fromJson(json, JsonObject.class);
  }

  /**
   * Adds the character.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, char value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the string.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, String value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the byte.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, byte value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the short.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, short value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the int.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, int value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the long.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, long value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the float.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, float value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the double.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, double value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the boolean.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, boolean value) {
    m_Data.addProperty(key, value);
    return this;
  }

  /**
   * Adds the dictionary.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, Dictionary value) {
    m_Data.add(key, value.toJson());
    return this;
  }

  /**
   * Adds the array.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return itself
   */
  public Dictionary add(String key, Array value) {
    m_Data.add(key, value.toJson());
    return this;
  }

  /**
   * Returns the current keys.
   *
   * @return the keys
   */
  public Set<String> keySet() {
    return m_Data.keySet();
  }

  /**
   * Returns the specified Json element.
   *
   * @param key the key to retrieve
   * @return the json element
   */
  public JsonElement get(String key) {
    return m_Data.get(key);
  }

  /**
   * Returns a new dictionary.
   *
   * @return the new instance
   */
  public static Dictionary newDict() {
    return new Dictionary();
  }

  /**
   * Returns a new dictionary.
   *
   * @param json the json string to parse
   * @return the new instance
   */
  public static Dictionary parse(String json) {
    return new Dictionary(json);
  }
}

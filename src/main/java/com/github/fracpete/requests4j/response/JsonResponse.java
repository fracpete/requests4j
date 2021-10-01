/*
 * JsonResponse.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.response;

import com.github.fracpete.requests4j.json.Array;
import com.github.fracpete.requests4j.json.Dictionary;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;

/**
 * For parsing the response as Json data structure.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class JsonResponse
  extends BasicResponse {

  /**
   * Checks whether the response is json (array or object).
   *
   * @return true if json
   */
  public boolean isJson() {
    try {
      json();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * Returns the body as json element (either array or object).
   *
   * @return		the element
   */
  public JsonElement json() throws UnsupportedEncodingException {
    String  	json;

    json = text("UTF-8").trim();
    if (json.startsWith("{"))
      return Dictionary.parse(json).toJson();
    else if (json.startsWith("["))
      return Array.parse(json).toJson();
    else
      throw new IllegalStateException("Neither array nor object: " + json);
  }

  /**
   * Returns the body as json object.
   *
   * @return		the object
   */
  public JsonObject jsonObject() throws UnsupportedEncodingException {
    return Dictionary.parse(text("UTF-8")).toJson();
  }

  /**
   * Returns the body as json object.
   *
   * @return		the object
   */
  public JsonArray jsonArray() throws UnsupportedEncodingException {
    return Array.parse(text("UTF-8")).toJson();
  }
}

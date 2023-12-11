/*
 * WebHookForm.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.request.Request;
import com.github.fracpete.requests4j.response.BasicResponse;

/**
 * Uses https://webhook.site/ to send URL-encoded form data (first argument is the API key).
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class WebhookForm {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("No WebHook key supplied! Go to the following site to get one: https://webhook.site/");
      return;
    }

    Request r = Requests.get("https://webhook.site/" + args[0]);
    r.formData().add("hello", "world");
    r.formData().add("pi", "3.1415926");
    r.header("Content-Type", "application/x-www-form-urlencoded");
    BasicResponse res = r.execute();
    System.out.println("\n--> Request:\n");
    System.out.println(res.rawResponse().request().url());
    System.out.println(res.rawResponse().request().headers());
    System.out.println("\n--> Response:\n");
    System.out.println(res.statusCode());
    System.out.println(res.text());
  }
}

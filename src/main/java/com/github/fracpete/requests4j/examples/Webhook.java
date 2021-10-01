/*
 * WebHook.java
 * Copyright (C) 2021 University of Waikato, Hamilton, New Zealand
 */

package com.github.fracpete.requests4j.examples;

import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.JsonResponse;

import static com.github.fracpete.requests4j.json.Array.newArray;
import static com.github.fracpete.requests4j.json.Dictionary.newDict;

/**
 * Uses https://webhook.site/ to send a JSON request (first argument is the API key).
 * If a JSON response has been defined on that website, then this gets parsed.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Webhook {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("No WebHook key supplied! Go to the following site to get one: https://webhook.site/");
      return;
    }

    String url = "https://webhook.site/" + args[0];
    JsonResponse r = Requests.post(url)
	.body(newDict()
	    .add("a", 1.234)
	    .add("b", true)
	    .add("c", newDict()
		.add("z", 1.2f))
	    .add("d", newArray()
		.add(1, 2, 3, 4)))
	.execute(new JsonResponse());
    System.out.println("Response: " + r);
    if (r.isJson()) {
      System.out.println(r.json());
    }
    else{
      System.err.println("Please define a custom response on https://webhook.site, via 'Edit'...");
      System.out.println(r.text());
    }
  }
}

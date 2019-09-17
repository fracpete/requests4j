/*
 * ConsoleHandler.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import gnu.trove.list.TByteList;
import gnu.trove.list.array.TByteArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Just dumps the received requests to stdout, always returns 200/OK.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class ConsoleHandler
  implements Serializable, HttpHandler {

  /** the date formatter to use. */
  protected transient SimpleDateFormat m_Formatter;

  /**
   * Returns the formatter in use.
   *
   * @return		the formatter
   */
  protected SimpleDateFormat getFormatter() {
    if (m_Formatter == null)
      m_Formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    return m_Formatter;
  }

  /**
   * Handle the given request and generate an appropriate response.
   * See {@link HttpExchange} for a description of the steps
   * involved in handling an exchange.
   * @param exchange the exchange containing the request from the
   *      client and used to send the response
   * @throws NullPointerException if exchange is <code>null</code>
   */
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    StringBuilder	output;
    String 		response;
    TByteList		body;
    InputStream		in;
    int			b;

    output = new StringBuilder();
    output.append("\n--- " + getFormatter().format(new Date()) + " ---\n");
    output.append(exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol()).append("\n");
    output.append("Remote address: " + exchange.getRemoteAddress()).append("\n");
    output.append("Headers:\n");
    for (String key: exchange.getRequestHeaders().keySet())
      output.append("- ").append(key).append(": ").append(exchange.getRequestHeaders().get(key)).append("\n");
    body = new TByteArrayList();
    in   = exchange.getRequestBody();
    while ((b = in.read()) != -1)
      body.add((byte) b);
    output.append("Body length: " + body.size()).append("\n");
    output.append("Body:\n" + new String(body.toArray()));

    System.out.println(output.toString());

    response = "OK";
    exchange.getResponseHeaders().add("Server", "requests4j/" + getClass().getName());
    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }
}

/*
 * Server.java
 * Copyright (C) 2019 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.requests4j.test;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

/**
 * Test server for receiving HTTP requests.
 * Takes up to three arguments:
 * - port (default: 8000)
 * - path (default: /)
 * - request handler (default: com.github.fracpete.requests4j.test.ConsoleHandler)
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class Server {

  /** the server in use. */
  protected HttpServer m_Server;

  /**
   * Initializes and starts the server.
   *
   * @param port	the port to listen on
   * @param path	the path to use (eg /upload)
   * @param handler	the handler class for the requests
   * @throws Exception	if instantiation fails
   */
  public Server(int port, String path, Class<? extends HttpHandler> handler) throws Exception  {
    m_Server = HttpServer.create(new InetSocketAddress(port), 0);
    m_Server.createContext(path, handler.newInstance());
    m_Server.setExecutor(null);
  }

  /**
   * Starts the server.
   */
  public void start() {
    m_Server.start();
  }

  /**
   * Stops the server.
   *
   * @param delay	the delay in seconds to wait for shutdown
   */
  public void stop(int delay) {
    m_Server.stop(delay);
  }

  /**
   * Starts the server: <port> <path> <handler-class>
   *
   * @param args	the options
   * @throws Exception	if starting server fails
   */
  public static void main(String[] args) throws Exception {
    int		port;
    String	path;
    Class	handler;
    Server	server;

    port = 8000;
    if (args.length > 0)
      port = Integer.parseInt(args[0]);

    path = "/";
    if (args.length > 1)
      path = args[1];

    handler = ConsoleHandler.class;
    if (args.length > 2)
      handler = Class.forName(args[2]);

    server = new Server(port, path, handler);
    server.start();
  }
}

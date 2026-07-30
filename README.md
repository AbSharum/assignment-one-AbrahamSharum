[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/fujAY9EH)
# Assignment One:  Building a Simple Web Server for Static File Serving in Java

### Introduction to HTTP Requests and Web Servers

In this assignment, you will be introduced to the fundamentals of HTTP (Hypertext Transfer Protocol),client server applications, browsers, and web servers by implementing a basic web server in Java. Understanding how web servers handle HTTP requests and serve static files will improve your understanding of the HTTP protocol and the operation of web sites and applications.

#### Learning Objectives

1. **Understanding HTTP Protocol:**
   - Learn the basics of HTTP requests and responses.
   - Explain the significance of different HTTP methods (GET, POST, etc.).
   - Increase your knowledge and understanding of network protocols.

2. **Building a Web Server in Java:**
   - Gain hands-on experience in creating a simple web server as an example of TCP/IP Client-Server applications.
   - Implement functionality for handling HTTP GET requests.
   - Design a web server that must adhere to the existing HTTP/1.1 protocol.
   - Learn how web servers serve static files like HTML, CSS, and images to browser clients.
   - Learn how the HTTP Request-Response affects the design and operation of web applications. 

### Instructions

Create a **Java HTTP Server** that listens on **port 8080**. Implement a basic HHTP server that can accept incoming connections from browsers and handle *HTTP Get* requests.  Your server should process the HTTP Request (request headers) and send a proper HTTP response with the appropriate response headers and the requested file (if available). The document root for storing your and serving your files on the web is the **docroot directory** located in your starter files.

The starter files, Server.java, implements a simple echo server.  This provides the basic framework for creating your web server.

**Refer to the [Mozilla Developer Network MDN HTTP Documentation] (https://developer.mozilla.org/en-US/docs/Web/HTTP) and the [HTTP Wikipedia page](https://en.wikipedia.org/wiki/HTTP) for HTTP/1.1 Request and Response headers and formatting guidelines.**

---

## Implementation Notes

`Server.java` is a small single-threaded TCP server built directly on `java.net.ServerSocket` (no external libraries/frameworks). What it actually does:

- Listens on **port 8080** (hardcoded in `main`).
- Accepts one client connection at a time (`serverSocket.accept()` in a loop); each request is handled fully before the next connection is accepted — there is no threading/concurrency.
- Reads the raw request line-by-line until it hits an empty line or the literal text `quit`, then pulls the requested path out of the first line by splitting on spaces (`GET /path HTTP/1.1` -> `/path`). Only the request line is parsed; headers are read but ignored, and there is no explicit check that the method is `GET` (any request line with a path in that position is treated as a file request).
- Requests for `/` are mapped to `/home.html`.
- Serves files by reading them from `<working directory>/docroot/<path>` with `Files.readAllBytes`, and responds with a `200 OK`, `Date`, `Content-Type: text/html` (always, regardless of actual file type), `Content-Length`, and `Connection: close` header, followed by the file bytes.
- If the file can't be read (missing file, bad path, etc.) it catches the `IOException` and returns `docroot/404.html` with a bare `HTTP/1.1 404: File Not Found` status line (no other headers).
- The `docroot/` directory contains `home.html`, `404.html`, a `favicon.ico`, and a `scripts/style.css`.

### Build & Run

```
./build.sh      # runs: javac Server.java
java Server     # starts the server on http://localhost:8080
```

Then visit `http://localhost:8080/` in a browser (serves `docroot/home.html`), or request any other path under `docroot/` (e.g. `http://localhost:8080/scripts/style.css`). Requesting a path that doesn't exist under `docroot/` returns the 404 page.

**Known limitations:** only handles one connection at a time, does not distinguish HTTP methods, always sends `Content-Type: text/html` even for non-HTML assets, and does minimal/no path validation.


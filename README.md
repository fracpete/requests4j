# requests4j
*HTTP for humans* using Java, inspired by the amazing [requests Python library](https://2.python-requests.org/en/master/).
Not as feature rich, but it gets a lot done.

## Why yet another Java HTTP client library? 
[JSoup](https://jsoup.org/) was missing some functionality that I needed and 
the [Apache HttpClient](http://hc.apache.org/httpcomponents-client-ga/)
library seemed a bit too cumbersome to use. Initially, I reused functionality
that I had written for my [ADAMS](https://adams.cms.waikato.ac.nz/) framework
over the years and turned it into a separate library, taking
the [requests](https://2.python-requests.org/en/master/) API as model to make
this library easy to use. 

## API Changes
Version 0.1.0 swapped out the underlying code, now using 
[Apache's HttpClient](http://hc.apache.org/httpcomponents-client-ga/) for doing 
the actual work. 


## Usage
The following sections describe how to use the library.

### Creating a request
The following methods are supported through the `com.github.fracpete.requests4j.Requests` class:
* GET -- `Requests.get()`
* POST -- `Requests.post()`
* PUT -- `Requests.put()`
* PATCH -- `Requests.patch()`
* HEAD -- `Requests.head()`
* DELETE -- `Requests.delete()`

The above mentioned methods can also take a URL string or a `java.net.URL` object,
initializing the URL straight away. Otherwise, you need to set the URL via 
the `url(String)` or `url(java.net.URL)` method. 

Calling one the request methods will generate an instance of the `Request` 
class (package `com.github.fracpete.requests4j.core`). This class supports
daisy-chaining to make it easy to configure a full request with minimal code 
to write.


### Headers and parameters
You can set custom HTTP headers using the following methods:
* `header(String,String)` -- setting a single header, name and value
* `headers(Map<String,String>)` -- setting multiple headers at once

Similarly, you can set parameters, eg to be encoded in the URL for `GET` 
requests, using the following methods:
* `parameter(String,String)` -- setting a single parameter, name and value
* `parameter(Map<String,String>)` -- setting multiple parameters at once


### HTML forms
Being able to upload data, be it simple parameters or files, is an important
feature for a HTTP client library. In case of `GET`, you can only use simple
parameters, as they get appended to the URL of the request. `POST`, on the
other hand, makes use of the `multipart/form-data` content type, allowing
you to upload files (or any binary data) as well.

In order to make use of form data, you need to call the `formData(FormData)`
method, with an instance of `FormData` (package `com.github.fracpete.requests4j.form`).
The `FormData` class allows you to either add parameter objects (derived from `AbstractParameter`)
or directly add simple parameters, files or input streams (eg a `java.io.ByteArrayInputStream` 
if you have the data only in its binary form).

The following example POSTs a file to the server with some credentials as part
of the form data: 

```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.form.FormData;

public class FormUpload {
  public static void main(String[] args) throws Exception {
    BasicResponse r = Requests.post("http://some.server.com/upload")
      .formData(
        new FormData()
          .addFile("file", "/some/where/important.doc")
          .add("user", "myuser")
          .add("password", "mysecretpassword")
      )
      .execute();
    System.out.printnl(r);
  }
}
```


### Execute and response
Once fully configured, you can execute a request with the `execute()` method,
which will either fail with an exception or return a `BasicResponse` object (package 
`com.github.fracpete.requests4j.response`).

With a `BasicResponse` object, you have access to:
* HTTP status code -- `statusCode()`
* HTTP status message -- `statusMessage()`
* HTTP headers -- `headers()`
* raw HTTPClient response -- `rawResponse()`
* the body of the response
  * the raw byte array -- `body()`
  * as (UTF-8) text -- `text()`
  * as text using a custom encoding -- `text(String)`

With the `saveBody` methods, you can save the binary response data as is to the 
supplied file.

## Sessions
To avoid having to string along and update any cookies for requests, you can
simply create a `Session` object which will take of that (package `com.github.fracpete.requests4j`).
The `Session` object has the same methods for instantiating request (though this 
time non-static) as the `Requests` class described above. The `Session` class
also supports hostname verification (see below), which it will apply to each
subsequent request. 

```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.Session;
import com.github.fracpete.requests4j.response.BasicResponse;

public class SessionExample {
  public static void main(String[] args) {
    Session session = new Session();
    BasicResponse login = session.post("http://some.server.com/login")
      .formData(
        new FormData()
          .add("user", "myuser")
          .add("password", "mysecretpassword")
      )
      .execute();
    if (login.ok()) {
      BasicResponse action = session.get("http://some.server.com/somethingelse")
        .execute();
      System.out.println(action.text());
    }
  }
}

``` 


## Advanced usage
### Different response objects
The `BasicResponse` object simply stores the received data in memory, which is fine
for receiving HTML pages or small binary objects. However, for downloading
large binary files, it is recommended to use one of the following response
classes instead (package `com.github.fracpete.requests4j.response`):
* `FileResponse` - streams the incoming data straight to the specified output file
* `StreamResponse` - uses the supplied `java.io.OutputStream` to forward the incoming data to  

Each of these classes implements the `Response` interface that all response
classes share, giving you access to the following methods:
* HTTP status code -- `statusCode()`
* HTTP status message -- `statusMessage()`
* HTTP headers -- `headers()`
* raw HTTPClient response -- `rawResponse()`

Instead of using the `execute()` method, you now use the `execute(Response)` 
method, supplying the fully configured response object. The following example
shows how to download a remote ZIP file straight to a file:
```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.FileResponse;

public class FileResponseDownload {
  public static void main(String[] args) {
    FileResponse r = Requests.get("http://some.server.com/largefile.zip")
      .execute(new FileResponse("/some/where/largefile.zip"));
    if (r.ok())
      System.out.println("Saved to " + r.outputFile());
  }
}
```

### Authentication
Some websites may require you to log in via password dialogs (eg Apache's htpasswd functionality).
In that case, you can use `BasicAuthentication` to provide these credentials:

```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.auth.BasicAuthentication;
import com.github.fracpete.requests4j.response.BasicResponse;

public class Auth {
  public static void main(String[] args) throws Exception {
    BasicResponse r = Requests.get("http://some.server.com/")
      .auth(new BasicAuthentication("USER", "PASSWORD"))
      .execute();
  }
}
```

### Redirects
Some websites, like sourceforge may perform redirects (eg from `http` to `https`).
By default redirects are not allowed, but you can enable them using the 
`allowRedirects(boolean)` method. With the `maxRedirects(int)` method you can 
set the upper limit to the number of redirects to follow through (default is 3).

The following example downloads a Weka zip file from sourceforge.net:

```java
public class Redirect {
  public static void main(String[] args) throws Exception {
    BasicResponse r = Requests.get("http://sourceforge.net/projects/weka/files/weka-3-9/3.9.3/weka-3-9-3.zip/download")
      .allowRedirects(true)
      .execute();
  }
}
```


### Proxies
Basic proxy support is available through the `proxy(...)` and `noProxy()`
methods. The following request configures a proxy (`proxy.domain.com:80`) for 
http connections:

```java
public class Redirect {
  public static void main(String[] args) throws Exception {
    BasicResponse r = Requests.get("http://some.server.com/")
      .proxy("proxy.domain.com", 80, "http")
      .execute();
  }
}
```


## Examples

* [ReadHtml](src/main/java/com/github/fracpete/requests4j/examples/ReadHtml.java) -- grabs the start
  page of *github.com* (storing the response in memory), dumps it to standard out, next to the cookies it received. 
* [DownloadWeka](src/main/java/com/github/fracpete/requests4j/examples/DownloadWeka.java) -- downloads 
  a Weka zip file and streams the downloaded file straight to disk
* [DownloadWekaAsStream](src/main/java/com/github/fracpete/requests4j/examples/DownloadWekaAsStream.java) -- downloads 
  a Weka zip file to a supplied output stream.
 

## Maven
Use the following dependency in your `pom.xml`:
```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>requests4j</artifactId>
      <version>0.0.5</version>
    </dependency>
```
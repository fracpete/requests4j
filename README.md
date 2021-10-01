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
* Version 0.1.0 swapped out the underlying code, now using 
  [Apache's HttpClient](http://hc.apache.org/httpcomponents-client-ga/) for doing 
  the actual work.
* Version 0.2.0 replaced [Apache's HttpClient](http://hc.apache.org/httpcomponents-client-ga/)
  with [Square's OkHttp](https://square.github.io/okhttp/). This was unfortunately
  necessary as Android ships with an outdated version of Apache's HttpClient, 
  resulting in method signature errors.


## Usage
The following sections describe how to use the library.

### Creating a request
The following methods are supported through the `com.github.fracpete.requests4j.Requests` class:
* GET -- `Requests.get()`
* POST -- `Requests.post()`
* PUT -- `Requests.put()`
* PATCH -- `Requests.patch()`
* HEAD -- `Requests.head()`
* OPTIONS -- `Requests.options()`
* DELETE -- `Requests.delete()`

The above mentioned methods can also take a URL string or a `java.net.URL` object,
initializing the URL straight away. Otherwise, you need to set the URL via 
the `url(String)` or `url(java.net.URL)` method. 

Calling one the request methods will generate an instance of the `Request` 
class (package `com.github.fracpete.requests4j.request`). This class supports
daisy-chaining to make it easy to configure a full request with minimal code 
to write.


### Headers and parameters
You can set custom HTTP headers using the following methods:
* `header(String,String)` -- setting a single header, name and value
* `headers(Map<String,String>)` -- setting multiple headers at once

Similarly, you can set parameters, eg to be encoded in the URL for `GET` 
requests, using the following methods:
* `parameter(String,String)` -- setting a single parameter, name and value.
* `parameter(String,String[])` or `parameter(String,List)` -- setting 
  multiple string values for the parameter.
* `parameters(Map<String,Object>)` -- setting multiple parameters at once, 
  ensures that the values of the map only contain `String`, `String[]` 
  or `java.util.List`.


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

### Posting data
You can easily *post* a single file or data (i.e., byte array) using the `attachment(AbstractAttachment)`
method. Available attachment types (package `com.github.fracpete.requests4j.attachment`) are:
* `ByteArrayAttachment`  
* `FileAttachment`

Such an attachment will add a `Content-Disposition` header to the request:
* `ByteArrayAttachment`: `attachment`
* `FileAttachment`: `attachment; name="file"; filename="NAME"`

The following example posts the file supplied as command-line argument:

```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.BasicResponse;
import com.github.fracpete.requests4j.attachment.FileAttachment;
import java.io.File;

public class PostData {
  public static void main(String[] args) throws Exception {
    File f = new File(args[0]);
    BasicResponse r = Requests.post("http://some.server.com/")
      .attachment(new FileAttachment(f))
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


### Json
You can also send and receive Json quite easily. For that purpose, requests4j 
offers the `Dictionary` and `Array` classes (package: `com.github.fracpete.requests4j.json`)
to allow method chaining and easily construct request. These two classes have
static methods, to shorten the instantiation:

```java
import com.github.fracpete.requests4j.Requests;
import com.github.fracpete.requests4j.response.JsonResponse;
import static com.github.fracpete.requests4j.json.Array.newArray;
import static com.github.fracpete.requests4j.json.Dictionary.newDict;

public class JsonPost {
  public static void main(String[] args) throws Exception {
    String url = "https://some.host.com/api/";
    JsonResponse r = Requests.post(url)
	  .body(newDict()
	    .add("a", 1.234)
	    .add("b", true)
	    .add("c", newDict()
		.add("z", 1.2f))
	    .add("d", newArray()
		.add(1, 2, 3, 4)))
	.execute(new JsonResponse());
    System.out.println(r.json());
  }
}
```


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


### URLs
The `URLBuilder` class (package `com.github.fracpete.requests4j.request`) is
used by the `Request` class internally to construct the URL for `GET` requests.
However, it can be used as standalone class as well to construct URLs, arguments 
only or with protocol/port/file. It allows you to append simply key-value pairs, 
keys with multiple values (String array or collection) and maps. The following code:

```java
import com.github.fracpete.requests4j.request.URLBuilder;
public class BuildURL {
  public static void main(String[] args) throws Exception {
    Map pairs = new HashMap();
    pairs.put("m", "m1");
    URL url = new URL("http", "somehost.com", "/api");
    URLBuilder builder = new URLBuilder(url)
      .append("a", "a1")
      .append("b", new String[]{"b1", "b2", "b3"})
      .append("c", Arrays.asList("c1", "c2"))
      .append(pairs);
    System.out.println(builder.build());
  }
}
```

Will generate the following output:

```
http://somehost.com/api?a=a1&b=b1&b=b2&b=b3&c=c1&c=c2&m=m1
```


## Examples

* [ReadHtml](src/main/java/com/github/fracpete/requests4j/examples/ReadHtml.java) -- grabs the start
  page of *github.com* (storing the response in memory), dumps it to standard out, next to the cookies it received. 
* [DownloadWeka](src/main/java/com/github/fracpete/requests4j/examples/DownloadWeka.java) -- downloads 
  a Weka zip file and streams the downloaded file straight to disk
* [DownloadWekaAsStream](src/main/java/com/github/fracpete/requests4j/examples/DownloadWekaAsStream.java) -- downloads 
  a Weka zip file to a supplied output stream.
* [WebHook](src/main/java/com/github/fracpete/requests4j/examples/Webhook.java) -- sends a POST request
  to [https://webhook.site] and outputs the response. You need to supply the API key as first argument
  (which you get by visiting the site).
 

## Maven
Use the following dependency in your `pom.xml`:
```xml
    <dependency>
      <groupId>com.github.fracpete</groupId>
      <artifactId>requests4j</artifactId>
      <version>0.2.2</version>
    </dependency>
```
package com.frejdh.util.networking.client.model;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import java.net.URI;

/**
 * Http methods with the support of a HTTP body
 */
public class HttpRequestEntityWithBody extends HttpEntityEnclosingRequestBase {

	private final String httpMethod;

	@Override
	public String getMethod() {
		return httpMethod.toUpperCase();
	}

	public HttpRequestEntityWithBody(String httpMethod, String uri) {
		super();
		setURI(URI.create(uri));
		this.httpMethod = httpMethod;
	}

	public HttpRequestEntityWithBody(String httpMethod, URI uri) {
		super();
		setURI(uri);
		this.httpMethod = httpMethod;
	}

	public HttpRequestEntityWithBody(String httpMethod) {
		super();
		this.httpMethod = httpMethod;
	}
}

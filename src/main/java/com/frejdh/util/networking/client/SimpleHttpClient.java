package com.frejdh.util.networking.client;

import com.frejdh.util.networking.client.model.ConnectionOptions;
import com.frejdh.util.networking.client.model.HttpMethod;
import com.frejdh.util.networking.client.model.HttpRequestEntityWithBody;
import com.frejdh.util.networking.client.model.KeyPair;
import com.frejdh.util.networking.client.model.SimpleHttpRequest;
import com.frejdh.util.networking.client.model.SimpleHttpResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Simply HTTP client. Nothing fancy and can be improved...
 */
public class SimpleHttpClient {

	private final RequestConfig connectionOptions;

	public SimpleHttpClient(ConnectionOptions connectionOptions) {
		this.connectionOptions = createTimeoutConfig(connectionOptions);
	}

	public SimpleHttpClient() {
		this(new ConnectionOptions());
	}

	/**
	 * Create a RequestConfig for the given timeout properties
	 * @return A new RequestConfig
	 */
	private RequestConfig createTimeoutConfig(ConnectionOptions connectionOptions) {
		return RequestConfig.custom()
				.setConnectTimeout(Math.max(0, connectionOptions.getConnectTimeout()))
				.setConnectionRequestTimeout(Math.max(0, connectionOptions.getRequestTimeout()))
				.setSocketTimeout(Math.max(0, connectionOptions.getRequestTimeout()))
				.build();
	}

	private String getUrl(SimpleHttpRequest request) {
		String url = request.getBaseUrl();
		if (!url.matches("(?i)http[s]?://.*")) {
			url = "http://" + url;
		}

		if (!StringUtils.isBlank(request.getPathVariable())) {
			if (!url.endsWith("/")) {
				url += "/";
			}
			url += request.getPathVariable();
		}

		if (!request.getParameters().isEmpty()) {
			url += request.getParameters().entrySet().stream().map(param -> {
				try {
					return param.getKey() + "=" + URLEncoder.encode(param.getValue(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return "ERROR=ENCODING_ISSUE";
				}
			}).collect(Collectors.joining("&", "?", ""));
		}

		return url;
	}

	public SimpleHttpResponse get(SimpleHttpRequest request) {
		if (!HttpMethod.GET.equals(request.getHttpMethod())) {
			request = request.toBuilder().withHttpMethod(HttpMethod.GET).build();
		}
		return doRequest(request);
	}

	public SimpleHttpResponse post(SimpleHttpRequest request) {
		if (!HttpMethod.POST.equals(request.getHttpMethod())) {
			request = request.toBuilder().withHttpMethod(HttpMethod.POST).build();
		}
		return doRequest(request);
	}

	public SimpleHttpResponse put(SimpleHttpRequest request) {
		if (!HttpMethod.PUT.equals(request.getHttpMethod())) {
			request = request.toBuilder().withHttpMethod(HttpMethod.PUT).build();
		}
		return doRequest(request);
	}

	public SimpleHttpResponse delete(SimpleHttpRequest request) {
		if (!HttpMethod.DELETE.equals(request.getHttpMethod())) {
			request = request.toBuilder().withHttpMethod(HttpMethod.DELETE).build();
		}
		return doRequest(request);
	}

	@SneakyThrows
	public SimpleHttpResponse doRequest(SimpleHttpRequest request) {
		if (request.getHttpMethod() == null) {
			throw new NullPointerException("Missing HTTP method in request");
		}
		String url = getUrl(request);
		HttpRequestEntityWithBody requestToSend = new HttpRequestEntityWithBody(request.getHttpMethod().name(), url);
		requestToSend.setEntity(request.getBody() != null ? new StringEntity(request.getBody()) : null); // Set the body; can be null
		request.getHeaders().forEach(header -> requestToSend.setHeader(header.getKey(), header.getValue()));

		// Get content
		CloseableHttpClient httpClient =  // Create client with timeout settings
				HttpClientBuilder.create()
						.setDefaultRequestConfig(connectionOptions)
						.build();
		CloseableHttpResponse rawResponse = httpClient.execute(requestToSend);

		SimpleHttpResponse response = SimpleHttpResponse.builder()
				.withBody(EntityUtils.toString(rawResponse.getEntity(), "UTF-8"))
				.withHeaders(Arrays.stream(rawResponse.getAllHeaders())
						.map(header -> new KeyPair<>(header.getName(), header.getValue()))
						.collect(Collectors.toList()))
				.withStatus(rawResponse.getStatusLine())
				.build();
		rawResponse.close();
		return response;
	}
}

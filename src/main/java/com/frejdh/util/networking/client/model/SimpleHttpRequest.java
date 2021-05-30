package com.frejdh.util.networking.client.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frejdh.util.networking.client.model.ConnectionOptions;
import com.frejdh.util.networking.client.model.HttpMethod;
import com.frejdh.util.networking.client.model.KeyPair;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder(setterPrefix = "with", toBuilder = true)
@Getter
public class SimpleHttpRequest {

	protected final HttpMethod httpMethod;
	protected final String baseUrl;
	protected final String pathVariable;
	protected final Map<String, String> parameters;
	protected final String body;
	protected final List<KeyPair<String, String>> headers;

	public static class SimpleHttpRequestBuilder {
		protected static final transient ObjectMapper MAPPER = new ObjectMapper();
		protected final Map<String, String> parameters = new HashMap<>();
		protected final List<KeyPair<String, String>> headers = new ArrayList<>();
		protected ConnectionOptions connectionOptions = new ConnectionOptions();
		protected String body;

		public SimpleHttpRequestBuilder withHeader(String key, String value) {
			this.headers.add(new KeyPair<>(key, value));
			return this;
		}

		public SimpleHttpRequestBuilder withHeader(KeyPair<String, String> header) {
			this.headers.add(header);
			return this;
		}

		public SimpleHttpRequestBuilder withHeaders(Collection<KeyPair<String, String>> headers) {
			headers.forEach(this::withHeader);
			return this;
		}

		public SimpleHttpRequestBuilder withHeaders(Map<String, String> headers) {
			headers.forEach(this::withHeader);
			return this;
		}

		public SimpleHttpRequestBuilder withParameters(Map<String, String> parameters) {
			parameters.forEach(this::withParameter);
			return this;
		}

		public SimpleHttpRequestBuilder withParameters(List<KeyPair<String, String>> parameters) {
			parameters.forEach(this::withParameter);
			return this;
		}

		public SimpleHttpRequestBuilder withParameters(Collection<String> parameters) {
			parameters.forEach(parameter -> {
				if (parameter.matches("[?&].+")) {
					parameter = parameter.split("[?&]")[1];
				}
				if (parameter.contains("=")) {
					String[] parameterSplitted = parameter.split("=");
					this.parameters.put(parameterSplitted[0], parameterSplitted[1]);
				}
			});

			return this;
		}

		public SimpleHttpRequestBuilder withParameter(String parameterString) {
			return withParameters(Collections.singletonList(parameterString));
		}

		public SimpleHttpRequestBuilder withParameter(String key, String value) {
			this.parameters.put(key, value);
			return this;
		}

		public SimpleHttpRequestBuilder withParameter(KeyPair<String, String> parameter) {
			withParameter(parameter.getKey(), parameter.getValue());
			return this;
		}

		public SimpleHttpRequestBuilder withConnectionOptions(ConnectionOptions connectionOptions) {
			this.connectionOptions = connectionOptions;
			return this;
		}

		public SimpleHttpRequestBuilder withBody(String body) {
			this.body = body;
			return this;
		}

		public SimpleHttpRequestBuilder withJsonBody(Object body) {
			try {
				this.body = (body instanceof String) ? (String) body : MAPPER.writeValueAsString(body);
				this.withHeader("Content-Type", "application/json");
			} catch (JsonProcessingException ignored) { }
			return this;
		}

	}
}


package com.frejdh.util.networking.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder(setterPrefix = "with", toBuilder = false)
public class SimpleHttpResponse {

	private String body;
	private List<KeyPair<String, String>> headers;
	private StatusLine status;

	public static class SimpleHttpResponseBuilder {
		private String body;
		private List<KeyPair<String, String>> headers = new ArrayList<>();
		private StatusLine status;

		public SimpleHttpResponseBuilder withBody(String body) {
			this.body = body;
			return this;
		}


		public SimpleHttpResponseBuilder withHeader(KeyPair<String, String> header) {
			this.headers.add(header);
			return this;
		}


		public SimpleHttpResponseBuilder withHeaders(List<KeyPair<String, String>> headers) {
			return withHeaders((Collection<KeyPair<String, String>>) headers);
		}

		public SimpleHttpResponseBuilder withHeaders(Collection<KeyPair<String, String>> headers) {
			this.headers.addAll(headers);
			return this;
		}

		public SimpleHttpResponseBuilder withStatus(StatusLine status) {
			this.status = status;
			return this;
		}
	}


}

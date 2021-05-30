package com.frejdh.util.networking.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(setterPrefix = "with", toBuilder = false)
@NoArgsConstructor
@Getter
public class ConnectionOptions {
	private static final transient int DEFAULT_TIMEOUT = 15000;

	/**
	 * Timeout for establishing connections. Default is {@value #DEFAULT_TIMEOUT} ms.
	 * Value 0 or less for infinite timeout or higher to set a custom one. Unit is milliseconds
	 */
	private final int connectTimeout = DEFAULT_TIMEOUT; 	// To establish a connection

	/**
	 * Timeout for requests being made. Default is {@value #DEFAULT_TIMEOUT} ms.
	 * Value 0 or less for infinite timeout or higher to set a custom one. Unit is milliseconds
	 */
	private final int requestTimeout = DEFAULT_TIMEOUT; 	// For a request
}

package com.frejdh.util.networking.client;

import com.frejdh.util.networking.NetworkUtils;
import java.io.BufferedReader;
import java.io.Reader;

public class ExtendedBufferedReader extends BufferedReader {
	public ExtendedBufferedReader(Reader in, int sz) {
		super(in, sz);
	}

	public ExtendedBufferedReader(Reader in) {
		super(in);
	}

	/**
	 * Utilizes {@link NetworkUtils#jsonToObject(BufferedReader, Class)}
	 * @return An object
	 */
	public <T> T jsonToObject(Class<T> objectClass) {
		return NetworkUtils.jsonToObject(this, objectClass);
	}

	/**
	 * Utilizes {@link NetworkUtils#bufferedReaderToString(BufferedReader)}
	 * @return A string
	 */
	@Override
	public String toString() {
		return NetworkUtils.bufferedReaderToString(this);
	}
}

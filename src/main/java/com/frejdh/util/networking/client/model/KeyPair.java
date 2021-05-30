package com.frejdh.util.networking.client.model;

/**
 * Contains HTTP parameters. Used for setting user-agent, accept, cookie etc.
 */
@SuppressWarnings("WeakerAccess")
public class KeyPair<K, V> {
	private final K key;
	private V value;

	public KeyPair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "HttpParam{" +
				"param='" + key + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}

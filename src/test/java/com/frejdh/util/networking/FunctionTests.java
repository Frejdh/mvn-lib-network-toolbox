package com.frejdh.util.networking;


import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FunctionTests {

	@Test
	public void getSchemeTest() throws Exception {
		assertEquals("https", NetworkUtils.getScheme("https://stackoverflow.com/questions"));
		assertEquals("http", NetworkUtils.getScheme("http://stackoverflow.com/questions"));
		assertEquals("git", NetworkUtils.getScheme("git://github.com"));
		assertEquals("git", NetworkUtils.getScheme("git@github.com"));
		assertThrows(URISyntaxException.class, () -> NetworkUtils.getScheme("missing-domain.com"));
		assertThrows(URISyntaxException.class, () -> NetworkUtils.getScheme(null));
	}

	@Test
	public void getHostTest() throws Exception {
		assertEquals("stackoverflow.com", NetworkUtils.getHost("https://stackoverflow.com/questions"));
		assertEquals("stackoverflow.com", NetworkUtils.getHost("stackoverflow.com/questions?user=AzureDiamond&password=hunter2"));
		assertEquals("127.0.0.1", NetworkUtils.getHost("https://127.0.0.1/welcome"));
		assertEquals("localhost", NetworkUtils.getHost("https://localhost/welcome"));
		assertEquals("subdomain.domain.com", NetworkUtils.getHost("https://subdomain.domain.com/index.html"));
	}

}

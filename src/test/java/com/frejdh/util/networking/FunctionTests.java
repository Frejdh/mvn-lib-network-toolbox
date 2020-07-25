package com.frejdh.util.networking;


import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FunctionTests {

	@Test
	public void baseUrlTest() throws Exception {
		short test = 1;
		assertEquals("Test " + test++, "stackoverflow.com", NetworkUtils.getBaseURL("https://stackoverflow.com/questions"));
		assertEquals("Test " + test++, "stackoverflow.com", NetworkUtils.getBaseURL("stackoverflow.com/questions?user=AzureDiamond&password=hunter2"));
		assertEquals("Test " + test++,"127.0.0.1", NetworkUtils.getBaseURL("https://127.0.0.1/welcome"));
		assertEquals("Test " + test++,"localhost", NetworkUtils.getBaseURL("https://localhost/welcome"));
		assertEquals("Test " + test,"subdomain.domain.com", NetworkUtils.getBaseURL("https://subdomain.domain.com/index.html"));
	}
}

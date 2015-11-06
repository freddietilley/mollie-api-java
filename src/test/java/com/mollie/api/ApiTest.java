package com.mollie.api;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ApiTest {
	private static String API_KEY = "test_dHar4XY7LxsDOtmnkVtjNVWXLSlXsM";

	protected MollieClient api;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		api = spy(new MollieClient());
		api.initResources(api); // make sure the resources also get the spied api

		api.setApiKey(API_KEY);
	}

	@Test
	public void testInvalidApiKeyFails() throws MollieException
	{
		api = new MollieClient();
		thrown.expect(MollieException.class);
		thrown.expectMessage("Invalid api key: \"invalid\". An API key must start with \"test_\" or \"live_\".");
		api.setApiKey("invalid");
	}

	@Test
	public void testCreatePaymentFailsEmptyHttpBody() throws MollieException
	{
		String msgBody = "{\"amount\":100,\"redirectUrl\":\"http://www.chucknorris.rhk/return.php\",\"description\":\"Order #1337 24 Roundhousekicks\"}";

		thrown.expect(MollieException.class);
		thrown.expectMessage("Unable to decode Mollie response: \"\"");

		doReturn("").when(api).performHttpCall(
			MollieClient.HTTP_POST, "payments", msgBody);

		try {
			api.payments().create(new BigDecimal(100),
				"Order #1337 24 Roundhousekicks",
				"http://www.chucknorris.rhk/return.php", null);
		} catch (MollieException e) {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_POST,
				"payments",
				msgBody);
			throw e;
		}
	}
}
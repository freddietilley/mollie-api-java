package com.mollie.api;

import com.mollie.api.objects.*;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
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
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_POST,
				"payments",
				msgBody);
		}
	}

	@Test
	public void testCreatePaymentFailsError() throws MollieException
	{
		String msgReturn = "{ \"error\":{ \"type\":\"request\", \"message\":\"Unauthorized request\", \"links\":{ \"documentation\":\"https://www.mollie.nl/api/docs/\" } } }";
		String msgBody = "{\"amount\":100,\"redirectUrl\":\"http://www.chucknorris.rhk/return.php\",\"description\":\"Order #1337 24 Roundhousekicks\"}";

		thrown.expect(MollieException.class);
		thrown.expectMessage("Error executing API call (request): Unauthorized request.");

		doReturn(msgReturn).when(api).performHttpCall(
			MollieClient.HTTP_POST, "payments", msgBody);

		try {
			api.payments().create(new BigDecimal(100),
				"Order #1337 24 Roundhousekicks",
				"http://www.chucknorris.rhk/return.php", null);
		} catch (MollieException e) {
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_POST,
				"payments",
				msgBody);
		}
	}

	/*
	@Test
	public void testCreatePaymentJsonFails() throws MollieException
	{
		thrown.expect(MollieException.class);
		thrown.expectMessage("Error executing API call (request): Unauthorized request.");

		try {
			api.payments().create(new BigDecimal(100),
				"Order #1337 24 Roundhousekicks \x80 15,-",
				"http://www.chucknorris.rhk/return.php", null);
		} catch (MollieException e) {
			verify(api, never()).performHttpCall(anyString(),
				anyString(), anyString());
			throw e;
		}
	}
	*/

	@Test
	public void testCreatePaymentWorksCorrectly() throws MollieException
	{
		String msgReturn = "{ \"id\":\"tr_d0b0E3EA3v\", \"mode\":\"test\", \"createdDatetime\":\"2013-11-21T09:57:08.0Z\", \"status\":\"open\", \"amount\":100, \"description\":\"Order #1225\", \"method\":null, \"details\":null, \"links\":{ \"paymentUrl\":\"https://www.mollie.nl/payscreen/pay/d0b0E3EA3v\" } }";
		String msgBody = "{\"amount\":100,\"redirectUrl\":\"http://www.chucknorris.rhk/return.php\",\"description\":\"Order #1337 24 Roundhousekicks\"}";

		Payment payment = null;

		doReturn(msgReturn).when(api).performHttpCall(
			MollieClient.HTTP_POST, "payments", msgBody);

		try {
			payment = api.payments().create(new BigDecimal(100),
				"Order #1337 24 Roundhousekicks",
				"http://www.chucknorris.rhk/return.php", null);
		} catch (MollieException e) {
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_POST,
				"payments", msgBody);
		}

		assertNotNull(payment);
		assertEquals("tr_d0b0E3EA3v", payment.id);
		assertEquals("Order #1225", payment.description);
		assertNull("Order #1225", payment.method);
		assertEquals("2013-11-21T09:57:08.0Z", payment.createdDatetime);
		assertEquals(Payment.STATUS_OPEN, payment.status);
		assertFalse(payment.isPaid());
		assertEquals("https://www.mollie.nl/payscreen/pay/d0b0E3EA3v",
			payment.getPaymentUrl());
		assertNull(payment.metadata);
	}

	@Test
	public void testGetPaymentWorksCorrectly() throws MollieException
	{
		String msgReturn = "{ \"id\":\"tr_d0b0E3EA3v\", \"mode\":\"test\", \"createdDatetime\":\"2013-11-21T09:57:08.0Z\", \"status\":\"open\", \"amount\":100, \"description\":\"Order #1225\", \"method\":null, \"details\":null, \"links\":{ \"paymentUrl\":\"https://www.mollie.nl/payscreen/pay/d0b0E3EA3v\" } }";
		String msgBody = "payments/tr_d0b0E3EA3v";

		Payment payment = null;

		doReturn(msgReturn).when(api).performHttpCall(
			MollieClient.HTTP_GET, msgBody, null);

		try {
			payment = api.payments().get("tr_d0b0E3EA3v");
		} catch (MollieException e) {
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_GET,
				msgBody, null);
		}

		assertNotNull(payment);
		assertEquals("tr_d0b0E3EA3v", payment.id);
		assertEquals("Order #1225", payment.description);
		assertNull("Order #1225", payment.method);
		assertEquals("2013-11-21T09:57:08.0Z", payment.createdDatetime);
		assertEquals(Payment.STATUS_OPEN, payment.status);
		assertFalse(payment.isPaid());
		assertEquals("https://www.mollie.nl/payscreen/pay/d0b0E3EA3v",
			payment.getPaymentUrl());
		assertNull(payment.metadata);
	}
}
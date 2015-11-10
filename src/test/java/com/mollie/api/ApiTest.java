/**
 * Copyright (c) 2015, Impending
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @license     Berkeley Software Distribution License (BSD-License 2) http://www.opensource.org/licenses/bsd-license.php
 * @author		Freddie Tilley <freddie.tilley@impending.nl>
 * @copyright	Impending
 * @link		http://www.impending.nl
 */

package com.mollie.api;

import com.mollie.api.objects.*;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;

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
	public void testNotSettingApiKeyGivesException() throws MollieException
	{
		api = new MollieClient();
		thrown.expect(MollieException.class);
		thrown.expectMessage("You have not set an api key. Please use setApiKey() to set the API key.");
		api.payments().all();
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
		assertNull(payment.method);
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
		String msgAction = "payments/tr_d0b0E3EA3v";

		Payment payment = null;

		doReturn(msgReturn).when(api).performHttpCall(
			MollieClient.HTTP_GET, msgAction, null);

		try {
			payment = api.payments().get("tr_d0b0E3EA3v");
		} catch (MollieException e) {
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_GET,
				msgAction, null);
		}

		assertNotNull(payment);
		assertEquals("tr_d0b0E3EA3v", payment.id);
		assertEquals("Order #1225", payment.description);
		assertNull(payment.method);
		assertEquals("2013-11-21T09:57:08.0Z", payment.createdDatetime);
		assertEquals(Payment.STATUS_OPEN, payment.status);
		assertFalse(payment.isPaid());
		assertEquals("https://www.mollie.nl/payscreen/pay/d0b0E3EA3v",
			payment.getPaymentUrl());
		assertNull(payment.metadata);
	}

	@Test
	public void testGetPaymentsWorksCorrectly() throws MollieException
	{
		String msgReturn = "{"+
		"\"totalCount\":1," +
		"\"offset\":0," +
		"\"data\":[" +
		"  {" +
		"    \"id\":\"tr_d0b0E3EA3v\",\"mode\":\"test\", \"createdDatetime\":\"2013-11-21T09:57:08.0Z\", \"status\":\"open\", \"amount\":100, \"description\":\"Order #1225\", \"method\":null, \"details\":null, \"links\":{ \"paymentUrl\":\"https://www.mollie.nl/payscreen/pay/d0b0E3EA3v\" }" +
		"  }" +
		"]," +
		"\"links\":{" +
		"  \"first\":null," +
		"  \"previous\":null," +
		"  \"next\":null," +
		"  \"last\":null" +
		"}" +
		"}";
		String msgAction = "payments?offset=0&count=0";

		ArrayList<Payment> collection = null;
		Payment payment = null;

		doReturn(msgReturn).when(api).performHttpCall(
			MollieClient.HTTP_GET, msgAction, null);

		try {
			collection = api.payments().all();
		} catch (MollieException e) {
			throw e;
		} finally {
			verify(api, times(1)).performHttpCall(MollieClient.HTTP_GET,
				msgAction, null);
		}

		assertNotNull(collection);
		assertEquals(collection.size(), 1);

		payment = collection.get(0);

		assertEquals("tr_d0b0E3EA3v", payment.id);
		assertEquals("Order #1225", payment.description);
		assertNull(payment.method);
		assertEquals("2013-11-21T09:57:08.0Z", payment.createdDatetime);
		assertEquals(Payment.STATUS_OPEN, payment.status);
		assertFalse(payment.isPaid());
		assertEquals("https://www.mollie.nl/payscreen/pay/d0b0E3EA3v",
			payment.getPaymentUrl());
		assertNull(payment.metadata);
	}
}
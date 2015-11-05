package com.mollie.api.resource;

import java.math.BigDecimal;
import java.util.HashMap;

import com.mollie.api.MollieClient;
import com.mollie.api.MollieException;
import com.mollie.api.objects.Payment;

public class Payments extends BaseResource<Payment> {
	public Payments(MollieClient api) {
		super(api);
	}

	public Payment create(BigDecimal amount, String description,
			String redirectUrl, HashMap<String, Object> meta) throws MollieException
	{
		return create(amount, null, description, redirectUrl, meta);
	}

	public Payment create(BigDecimal amount, String method, String description,
			String redirectUrl, HashMap<String, Object> meta) throws MollieException
	{
		HashMap<String, Object> payData = new HashMap<String, Object>();

		if (amount != null)
			payData.put("amount", amount);
		if (method != null)
			payData.put("method", method);
		if (description != null)
			payData.put("description", description);
		if (redirectUrl != null)
			payData.put("redirectUrl", redirectUrl);

		if (meta != null)
			payData.put("metadata", meta);

		return this.create(payData);
	}
}

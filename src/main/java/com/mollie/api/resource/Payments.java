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

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
package com.mollie.api.objects;

import java.math.BigDecimal;
import java.util.Map;

public class Payment {
	public static final String STATUS_OPEN = "open";
	public static final String STATUS_CANCELLED = "cancelled";
	public static final String STATUS_EXPIRED = "expired";
	public static final String STATUS_PAID = "paid";

	public String id;

	public String mode;

	public BigDecimal amount;

	public String description;

	public String method;

	public String status = STATUS_OPEN;

	public String createdDatetime;

	public String paidDatetime;

	public String cancelledDatetime;

	public String expiredDatetime;

	public Map<String, Object> metadata;

	public Map<String, Object> details;

	public Links links;

	public boolean isOpen() { return this.status.equals(STATUS_OPEN); }

	public boolean isPaid() {
		return (this.paidDatetime != null && !this.paidDatetime.trim().isEmpty());
	}

	public boolean isCancelled() {
		return (this.cancelledDatetime != null && !this.cancelledDatetime.trim().isEmpty());
	}

	public boolean isExpired() {
		return (this.expiredDatetime != null && !this.expiredDatetime.trim().isEmpty());
	}

	public String getPaymentUrl() {
		if (links != null && links.paymentUrl != null)
			return links.paymentUrl;
		else
			return null;
	}

	public static class Links
	{
		public String paymentUrl;
		public String redirectUrl;
	}
}

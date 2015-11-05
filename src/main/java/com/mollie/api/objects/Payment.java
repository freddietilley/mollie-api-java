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

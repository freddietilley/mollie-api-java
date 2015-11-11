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
	/**
	 * The payment has just been created, no action has happened on it yet.
	 */
	public static final String STATUS_OPEN = "open";

	/**
	 * The payment has just been started, no final confirmation yet.
	 */
	public static final String STATUS_PENDING = "pending";

	/**
	 * The customer has cancelled the payment.
	 */
	public static final String STATUS_CANCELLED = "cancelled";

	/**
	 * The payment has expired due to inaction of the customer.
	 */
	public static final String STATUS_EXPIRED = "expired";

	/**
	 * The payment has been paid.
	 */
	public static final String STATUS_PAID = "paid";

	/**
	 * The payment has been paidout and the money has been transferred to the bank account of the merchant.
	 */
	public static final String STATUS_PAIDOUT = "paidout";

	/**
	 * The payment has been refunded, either through Mollie or through the payment provider (in the case of PayPal).
	 */
	public static final String STATUS_REFUNDED = "refunded";

	/**
	 * Some payment methods provide your customers with the ability to dispute payments which could
	 * ultimately lead to a chargeback.
	 */
	public static final String STATUS_CHARGED_BACK = "charged_back";

	/**
	 * Id of the payment (on the Mollie platform).
	 */
	public String id;

	/**
	 * Mode of the payment, either "live" or "test" depending on the API Key that was used.
	 */
	public String mode;

	/**
	 * The amount of the payment in EURO with 2 decimals.
	 */
	public BigDecimal amount;

	/**
	 * The amount of the payment that has been refunded to the consumer, in EURO with 2 decimals. This field will be
	 * null if the payment can not be refunded.
	 */
	public BigDecimal amountRefunded;

	/**
	 * The amount of a refunded payment that can still be refunded, in EURO with 2 decimals. This field will be
	 * null if the payment can not be refunded.
	 *
	 * For some payment methods this amount can be higher than the payment amount. This is possible to reimburse
	 * the costs for a return shipment to your customer for example.
	 */
	public BigDecimal amountRemaining;

	/**
	 * Description of the payment that is shown to the customer during the payment, and
	 * possibly on the bank or credit card statement.
	 */
	public String description;

	/**
	 * If method is empty/null, the customer can pick his/her preferred payment method.
	 */
	public String method;

	/**
	 * The status of the payment.
	 */
	public String status = STATUS_OPEN;

	/**
	 * The period after which the payment will expire in ISO-8601 format.
	 *
	 * example P12DT11H30M45S (12 days, 11 hours, 30 minutes and 45 seconds)
	 */
	public String expiryPeriod;

	/**
	 * Date and time the payment was created in ISO-8601 format.
	 *
	 * example "2013-12-25T10:30:54.0Z"
	 */
	public String createdDatetime;

	/**
	 * Date and time the payment was paid in ISO-8601 format.
	 */
	public String paidDatetime;

	/**
	 * Date and time the payment was cancelled in ISO-8601 format.
	 */
	public String cancelledDatetime;

	/**
	 * Date and time the payment was cancelled in ISO-8601 format.
	 */
	public String expiredDatetime;

	/**
	 * The profile ID this payment belongs to.
	 *
	 * example pfl_xH2kP6Nc6X
	 */
	public String profileId;

	/**
	 * The locale used for this payment.
	 */
	public String locale;

	/**
	 * During creation of the payment you can set custom metadata that is stored with
	 * the payment, and given back whenever you retrieve that payment.
	 */
	public Map<String, Object> metadata;

	/**
	 * Details of a successfully paid payment are set here. For example, the iDEAL
	 * payment method will set details.get('consumerName') and details.get('consumerAccount').
	 */
	public Map<String, Object> details;

	public Links links;

	/**
	 * Is this payment still open / ongoing?
	 *
	 * @return {@link boolean}
	 */
	public boolean isOpen() { return this.status.equals(STATUS_OPEN); }

	/**
	 * Is this payment pending
	 *
	 * @return {@link boolean}
	 */
	public boolean isPending() { return this.status.equals(STATUS_PENDING); }

	/**
	 * Is this payment (partially) refunded?
	 *
	 * @return {@link boolean}
	 */
	public boolean isRefunded() { return this.status.equals(STATUS_REFUNDED); }

	/**
	 * Is this payment charged back?
	 *
	 * @return {@link boolean}
	 */
	public boolean isChargedBack() { return this.status.equals(STATUS_CHARGED_BACK); }

	/**
	 * Is this payment paid for?
	 *
	 * @return {@link boolean}
	 */
	public boolean isPaid() {
		return (this.paidDatetime != null && !this.paidDatetime.trim().isEmpty());
	}

	/**
	 * Is this payment cancelled?
	 *
	 * @return {@link boolean}
	 */
	public boolean isCancelled() { return this.status.equals(STATUS_CANCELLED); }

	/**
	 * Has this payment expired?
	 *
	 * @return {@link boolean}
	 */
	public boolean isExpired() { return this.status.equals(STATUS_EXPIRED); }

	/**
	 * Get the payment URL where the customer can complete the payment.
	 *
	 * @return {@link String}
	 */
	public String getPaymentUrl() {
		if (links != null && links.paymentUrl != null)
			return links.paymentUrl;
		else
			return null;
	}

	/**
	 * Can the payment be refunded?
	 *
	 * @return {@link boolean}
	 */
	public boolean canBeRefunded() {
		return (this.amountRemaining != null);
	}

	/**
	 * Can the payment be partially refunded?
	 *
	 * @return {@link boolean}
	 */
	public boolean canBePartiallyRefunded() {
		return (this.canBeRefunded() && !(this.method.equals(Method.CREDITCARD)));
	}

	/**
	 * Get the amount that is already refunded.
	 *
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getAmountRefunded() {
		if (this.amountRefunded != null) {
			return this.amountRefunded;
		}
		return new BigDecimal(0);
	}

	/**
	 * Get the remaining amount that can be refunded. For some payment methods this amount can be higher than
	 * the payment amount. This is possible to reimburse the costs for a return shipment to your customer for example.
	 *
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getAmountRemaining() {
		if (this.amountRemaining != null) {
			return this.amountRemaining;
		}
		return new BigDecimal(0);
	}

	public static class Links
	{
		public String paymentUrl;
		public String redirectUrl;
	}
}

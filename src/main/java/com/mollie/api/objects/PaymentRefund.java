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

public class PaymentRefund {
	/**
	 * The refund will be send to the bank on the next business day.
	 * You can still cancel the refund.
	 */
	public static final String STATUS_PENDING = "pending";

	/**
	 * The refund has been sent to the bank. The refund amount will be transferred
	 * to the consumer account as soon as possible.
	 */
	public static final String STATUS_PROCESSING = "processing";

	/**
	 * The refund amount has been transferred to the consumer.
	 */
	public static final String STATUS_REFUNDED = "refunded";

	/**
	 * Id of the payment method.
	 */
	public String id;

	/**
	 * The amount that was refunded.
	 */
	public BigDecimal amount;

	/**
	 * The payment that was refunded.
	 */
	public Payment payment;

	/**
	 * Date and time the payment was cancelled in ISO-8601 format.
	 */
	public String refundedDatetime;

	/**
	 * The refund status
	 */
	public String status;

	/**
	 * Is this refund pending?
	 *
	 * @return {@link boolean}
	 */
	public boolean isPending() { return this.status.equals(STATUS_PENDING); }

	/**
	 * Is this refund processing?
	 *
	 * @return {@link boolean}
	 */
	public boolean isProcessing() { return this.status.equals(STATUS_PROCESSING); }

	/**
	 * Has this refund been transferred to the consumer?
	 *
	 * @return {@link boolean}
	 */
	public boolean isTransferred() { return this.status.equals(STATUS_REFUNDED); }
}


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

public class Method {
	public static final String IDEAL = "ideal";
	public static final String PAYSAFECARD = "paysafecard";
	public static final String CREDITCARD = "creditcard";
	public static final String MISTERCASH = "mistercash";

	private String id;
	private String description;
	private Amount amount;

	public Method() {
	}

	public String id() { return id; }
	public void setId(String anId) { id = anId; }

	public String description() { return description; }
	public void setDescription(String aDescription) { description = aDescription; }

	public BigDecimal getMinimumAmount() {
		return (amount != null ? amount.minimum() : null);
	}

	public BigDecimal getMaximumAmount() {
		return (amount != null ? amount.maximum() : null);
	}

	public Amount amount() { return amount; }

	public static class Amount {
		private BigDecimal minimum;
		private BigDecimal maximum;

		public BigDecimal minimum() { return minimum; }
		public BigDecimal maximum() { return maximum; }
	};
}

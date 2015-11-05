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

package com.mollie.api.resource;

import com.mollie.api.MollieClient;
import com.mollie.api.objects.Issuer;

public class Issuers extends BaseResource<Issuer> {
	public Issuers(MollieClient api) {
		super(api);
	}
}

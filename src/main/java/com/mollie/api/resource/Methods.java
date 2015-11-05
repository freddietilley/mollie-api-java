package com.mollie.api.resource;

import com.mollie.api.MollieClient;
import com.mollie.api.objects.Method;

public class Methods extends BaseResource<Method>
{
	public Methods(MollieClient api) {
		super(api);
	}
}

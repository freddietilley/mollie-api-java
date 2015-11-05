package com.mollie.api;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.mollie.api.resource.Issuers;
import com.mollie.api.resource.Methods;
import com.mollie.api.resource.Payments;

public class MollieClient {
	/**
	 * Version of our client.
	 */
	public static final String CLIENT_VERSION = "1.0";

	/**
	 * Endpoint of the remote API.
	 */
	public static final String API_ENDPOINT = "https://api.mollie.nl";

	/**
	 * Version of the remote API.
	 */
	public static final String API_VERSION = "v1";

	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";
	public static final String HTTP_DELETE = "DELETE";

	protected Methods _methods;
	protected Payments _payments;
	protected Issuers _issuers;

	protected String _apiEndpoint = API_ENDPOINT;
	protected String _apiKey;

	public MollieClient()
	{
		_methods = new Methods(this);
		_payments = new Payments(this);
		_issuers = new Issuers(this);
	}

	public Methods methods() { return _methods; }
	public Payments payments() { return _payments; }
	public Issuers issuers() { return _issuers; }

	public String apiEndpoint() { return _apiEndpoint; }
	public void setApiEndpoint(String endpoint) { _apiEndpoint = endpoint; }

	public void setApiKey(String apikey) { _apiKey = apikey; }

	public String performHttpCall(String method, String apiMethod) {
		return performHttpCall(method, apiMethod, null);
	}

	public String performHttpCall(String method, String apiMethod, String httpBody)
	{
		URI uri = null;
		String result = null;

		try {
			URIBuilder ub = new URIBuilder(this._apiEndpoint + "/" + API_VERSION + "/" + apiMethod);
			uri = ub.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		if (uri != null)
		{
			//CloseableHttpClient httpclient = HttpClients.createDefault();
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpRequestBase action = null;
			//CloseableHttpResponse response = null;
			HttpResponse response = null;

			if (httpBody != null)
			{
				//EntityBuilder builder = EntityBuilder.create();
				StringEntity entity = new StringEntity(httpBody, ContentType.APPLICATION_JSON);

				//builder.setContentType(ContentType.APPLICATION_JSON);
				//builder.setContentEncoding("UTF-8");
				//builder.setText(httpBody);

				action = new HttpPost(uri);
				//((HttpPost)action).setEntity(builder.build());
				((HttpPost)action).setEntity(entity);
			} else {
				action = new HttpGet(uri);
			}

			action.setHeader("Authorization", "Bearer " + this._apiKey);
			action.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());

			try {
				response = httpclient.execute(action);

				HttpEntity entity = response.getEntity();
				StringWriter sw = new StringWriter();

				IOUtils.copy(entity.getContent(), sw, "UTF-8");
				result = sw.toString();
				EntityUtils.consume(entity);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			httpclient.getConnectionManager().shutdown();
		}

		return result;
	}
}

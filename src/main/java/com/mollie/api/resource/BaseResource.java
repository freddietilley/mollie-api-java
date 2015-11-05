package com.mollie.api.resource;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mollie.api.MollieClient;
import com.mollie.api.MollieException;

abstract public class BaseResource <T> {
	public static final String REST_CREATE = MollieClient.HTTP_POST;
	public static final String REST_UPDATE = MollieClient.HTTP_POST;
	public static final String REST_READ = MollieClient.HTTP_GET;
	public static final String REST_LIST = MollieClient.HTTP_GET;
	public static final String REST_DELETE = MollieClient.HTTP_DELETE;

	/**
	 * Default number of objects to retrieve when listing all objects.
	 */
	public static final int DEFAULT_LIMIT = 10;

	protected MollieClient _api;

	public BaseResource(MollieClient api)
	{
		_api = api;
	}

	protected String getResourceName() {
		String className = getClass().getName();
		String resourceName = className;
		String [] elements = className.split("\\.");

		if (elements.length > 0)
			resourceName = elements[elements.length-1];
		return resourceName.toLowerCase();
	}

	@SuppressWarnings("unchecked")
	protected Class<T> returnedClass() {
	     ParameterizedType parameterizedType = (ParameterizedType)getClass()
	                                                 .getGenericSuperclass();
	     return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	public ArrayList<T> all() throws MollieException { return this.all(0, 0); }
	public ArrayList<T> all(int offset, int limit) throws MollieException {
		return this.rest_list(this.getResourceName(), offset, limit);
	}

	public T get(String resourceId) throws MollieException {
		return this.rest_read(this.getResourceName(), resourceId);
	}

	public T create(Object data) throws MollieException {
		Gson gson = new Gson();
		String encoded = gson.toJson(data);
		
		if (encoded != null)
			return this.rest_create(this.getResourceName(), encoded);
		else
			return null;
	}

	private void _checkResponseResult(JsonObject result, String body) throws MollieException
	{
		if (result == null)
		{
			throw new MollieException("Unable to decode Mollie response: \""+body+"\"");
		} else {
			if (result.get("error") != null)
			{
				JsonObject error = result.get("error").getAsJsonObject();
				String type = error.get("type").getAsString();
				String message = error.get("message").getAsString();
				
				throw new MollieException(type, message);
			}
		}
	}

	private T rest_create(String restResource, String body) throws MollieException
	{
		JsonObject result = this.performApiCall(REST_CREATE, restResource, body);

		_checkResponseResult(result, body);

		if (result != null)
		{
			Gson gson = new Gson();
			return gson.fromJson(result, returnedClass());
		}

		return null;
	}

	private T rest_read(String restResource, String id) throws MollieException
	{
		String method = restResource + "/" + id;
		JsonObject result = this.performApiCall(REST_READ, method);

		_checkResponseResult(result, null);

		if (result != null)
		{
			Gson gson = new Gson();
			return gson.fromJson(result, returnedClass());
		}

		return null;
	}

	private ArrayList<T> rest_list(String restResource, int offset, int limit) throws MollieException
	{
		JsonObject result = this.performApiCall(REST_LIST, restResource);
		ArrayList<T> arraylist = new ArrayList<T>();

		_checkResponseResult(result, null);

		if (result != null)
		{
			Gson gson = new Gson();

			for (JsonElement object : result.get("data").getAsJsonArray())
				arraylist.add(gson.fromJson(object, returnedClass()));
		}

		return arraylist;
	}

	protected JsonObject performApiCall(String httpMethod, String apiMethod) {
		return performApiCall(httpMethod, apiMethod, null);
	}

	protected JsonObject performApiCall(String httpMethod,
									String apiMethod,
									String httpBody)
	{
		String result = _api.performHttpCall(httpMethod, apiMethod, httpBody);
		JsonParser parser = new JsonParser();
		JsonObject object = null;

		if ((object = parser.parse(result).getAsJsonObject()) != null)
			return object;
		else
			return null;
	}
}

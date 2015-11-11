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
package com.mollie.api.resource;

import java.net.URISyntaxException;
import org.apache.http.client.utils.URIBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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

	/**
	 * @return Default resource name used by this resource when performing api calls.
	 */
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

	/**
	 * Convenience method to copy all public properties from a src object into
	 * a dst object of the same type.
	 *
	 * @param src Source object to copy properties from
	 * @param dst Target object
	 */
	protected void copyInto(T src, T dst) {
		Field[] fromFields = returnedClass().getDeclaredFields();
		Object value = null;

		try {
			for (Field field : fromFields) {
				int modifiers = field.getModifiers();

				if ((modifiers & Modifier.PUBLIC) == Modifier.PUBLIC &&
					(modifiers & Modifier.FINAL) != Modifier.FINAL &&
					(modifiers & Modifier.STATIC) != Modifier.STATIC)
				{
					value = field.get(src);
					field.set(dst, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve all objects of a certain resource.
	 *
	 * @return {@link List} list of fetched objects
	 * @throws MollieException
	 */
	public List<T> all() throws MollieException {
		return this.all(0, 0);
	}

	/**
	 * Retrieve all objects of a certain resource.
	 *
	 * @param options {@link Map} additional options to include when fetching objects.
	 * @return {@link List} list of fetched objects
	 * @throws MollieException
	 */
	public List<T> all(Map<String,String> options) throws MollieException {
		return this.all(0, 0, options);
	}

	/**
	 * Retrieve all objects of a certain resource.
	 *
	 * @param offset {@link int} page offset of the objects to retrieve
	 * @param limit {@link int} maximimum number of objects to retrieve
	 * @return {@link List} list of fetched objects
	 * @throws MollieException
	 */
	public List<T> all(int offset, int limit) throws MollieException {
		return this.all(offset, limit, null);
	}

	/**
	 * Retrieve all objects of a certain resource.
	 *
	 * @param offset {@link int} page offset of the objects to retrieve
	 * @param limit {@link int} maximimum number of objects to retrieve
	 * @param options {@link Map} additional options to include when fetching objects.
	 * @return {@link List} list of fetched objects
	 * @throws MollieException
	 */
	public List<T> all(int offset, int limit, Map<String,String> options) throws MollieException {
		return this.rest_list(this.getResourceName(), offset, limit, options);
	}

	/**
	 * Retrieve information on a single resource from Mollie.
	 * 
	 * Will throw a MollieException if the resource cannot be found.
	 *
	 * @param resourceId {@link String} Id of the object to retrieve.
	 * @return object
	 * @throws MollieException
	 */
	public T get(String resourceId) throws MollieException {
		return this.rest_read(this.getResourceName(), resourceId);
	}

	/**
	 * Create a resource with the remote API.
	 * 
	 * @param data an object containing details on the resource. Fields supported
	 * depend on the resource created.
	 * @return object on success or null on failure.
	 * @throws MollieException
	 */
	public T create(Object data) throws MollieException {
		Gson gson = new Gson();
		String encoded = gson.toJson(data);
		
		if (encoded != null)
			return this.rest_create(this.getResourceName(), encoded);
		else
			return null;
	}

	/**
	 * Creates a resource with the REST API.
	 *
	 * @param restResource {@link String} resource name
	 * @param body {@link String} contents used for creation of object
	 * @return object on success or null on failure.
	 * @throws MollieException
	 */
	private T rest_create(String restResource, String body) throws MollieException
	{
		JsonObject result = this.performApiCall(REST_CREATE, restResource, body);

		if (result != null)
		{
			Gson gson = new Gson();
			return gson.fromJson(result, returnedClass());
		}

		return null;
	}

	/**
	 * Retrieves a single object from the REST API.
	 *
	 * @param restResource {@link String} resource name
	 * @param id {@link String} Id of the object to retrieve
	 * @return object
	 * @throws MollieException
	 */
	private T rest_read(String restResource, String id) throws MollieException
	{
		String method = restResource + "/" + id;
		JsonObject result = this.performApiCall(REST_READ, method);

		if (result != null)
		{
			Gson gson = new Gson();
			return gson.fromJson(result, returnedClass());
		}

		return null;
	}

	/**
	 * Creates a valid query string from the supplied options that can be used
	 * as url parameters. The method returns null if there was an error building
	 * the query string.
	 * 
	 * @param options {@link Map} options to build a query string from
	 * @return {@link String} a valid query string or null.
	 */
	private String buildQueryFromMap(Map<String,String> options)
	{
		URIBuilder ub = null;
		String queryString = null;

		try {
			ub = new URIBuilder();

			for (HashMap.Entry<String, String> entry : options.entrySet()) {
				ub.addParameter(entry.getKey(), entry.getValue());
			}

			queryString = ub.build().getQuery();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return queryString;
	}

	/**
	 * Get a collection of objects from the REST API.
	 *
	 * @param restResource {@link String} resource name
	 * @param offset {@link int} page offset of the objects to retrieve
	 * @param limit {@link int} maximimum number of objects to retrieve
	 * @param options {@link Map} additional options
	 * @return {@link List}
	 * @throws MollieException
	 */
	private List<T> rest_list(String restResource, int offset, int limit, Map<String,String> options) throws MollieException
	{
		String query = null;

		if (options == null) {
			options = new HashMap<String,String>();
		}

		options.putIfAbsent("offset", Integer.toString(offset));
		options.putIfAbsent("count", Integer.toString(limit));

		query = buildQueryFromMap(options);
		String apiPath = restResource + (query != null ? "?" + query : "");

		JsonObject result = this.performApiCall(REST_LIST, apiPath);
		ArrayList<T> arraylist = new ArrayList<T>();

		if (result != null)
		{
			Gson gson = new Gson();

			for (JsonElement object : result.get("data").getAsJsonArray())
				arraylist.add(gson.fromJson(object, returnedClass()));
		}

		return arraylist;
	}

	/**
	 * Perform an API call with an empty body contents, interpret the results
	 * and convert them to the correct object type. Throws a MollieException if
	 * there was an error performing the call or if the results could not
	 * be decoded into a {@link JsonObject}
	 *
	 * @param httpMethod
	 * @param apiMethod
	 *
	 * @return object {@link JsonObject}
	 * @throws MollieException
	 */
	protected JsonObject performApiCall(String httpMethod, String apiMethod) throws MollieException {
		return performApiCall(httpMethod, apiMethod, null);
	}

	/**
	 * Perform an API call, interpret the results and convert them to the
	 * correct object type. Throws a MollieException if there was an error
	 * performing the call or if the results could not be decoded into a
	 * {@link JsonObject}
	 *
	 * @param httpMethod
	 * @param apiMethod
	 * @param httpBody
	 *
	 * @return object {@link JsonObject}
	 * @throws MollieException
	 */
	protected JsonObject performApiCall(String httpMethod,
										String apiMethod,
										String httpBody) throws MollieException
	{
		String result = _api.performHttpCall(httpMethod, apiMethod, httpBody);
		JsonParser parser = new JsonParser();
		JsonElement element = null;
		JsonObject object = null;

		try {
			if ((element = parser.parse(result)) != null) {
				if (element.isJsonObject()) {
					object = element.getAsJsonObject();
				}
			}
		} catch (com.google.gson.JsonParseException e) {}

		if (object == null)
		{
			throw new MollieException("Unable to decode Mollie response: \""+result+"\"");
		} else {
			if (object.get("error") != null)
			{
				MollieException exception = null;
				JsonObject error = object.get("error").getAsJsonObject();
				String type = error.get("type").getAsString();
				String message = error.get("message").getAsString();

				exception = new MollieException("Error executing API call (" + type +"): " +
					message + ".");

				if (error.has("field")) {
					exception.setField(error.get("field").getAsString());
				}

				throw exception;
			}
		}

		return object;
	}
}

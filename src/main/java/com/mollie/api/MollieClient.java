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
 * @author      Freddie Tilley <freddie.tilley@impending.nl>
 * @copyright   Impending
 * @link        http://www.impending.nl
 */
package com.mollie.api;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.mollie.api.objects.Payment;
import com.mollie.api.resource.Issuers;
import com.mollie.api.resource.Methods;
import com.mollie.api.resource.Payments;
import com.mollie.api.resource.PaymentsRefunds;

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

    public MollieClient() {
        this.initResources(this);
    }

    protected void initResources(MollieClient client) {
        _methods = new Methods(client);
        _payments = new Payments(client);
        _issuers = new Issuers(client);
    }

    public Methods methods() { return _methods; }
    public Payments payments() { return _payments; }
    public Issuers issuers() { return _issuers; }
    public PaymentsRefunds refundsWithPayment(Payment payment) {
        return new PaymentsRefunds(this, payment.id);
    }

    public String apiEndpoint() { return _apiEndpoint; }
    public void setApiEndpoint(String endpoint) { _apiEndpoint = endpoint; }

    /**
     * Sets the api key
     *
     * @param apikey api key to set
     * @throws MollieException when the api key is invalid.
     */
    public void setApiKey(String apikey) throws MollieException
    {
        if (!apikey.matches("^(?:live|test)_\\w+$")) {
            throw new MollieException("Invalid api key: \"" + apikey + "\". An API key must start with \"test_\" or \"live_\".");
        }
        _apiKey = apikey;
    }

    /**
     * Perform a http call with an empty body. This method is used by the
     * resource specific classes. Please use the payments() method to perform
     * operations on payments.
     *
     * @param method the http method to use
     * @param apiMethod the api method to call
     * @return result of the http call
     * @throws MollieException when the api key is not set or when there is a
     * problem communicating with the mollie server.
     * @see #performHttpCall(String method, String apiMethod, String httpBody)
     */
    public String performHttpCall(String method, String apiMethod) throws MollieException
    {
        return performHttpCall(method, apiMethod, null);
    }

    /**
     * Perform a http call. This method is used by the resource specific classes.
     * Please use the payments() method to perform operations on payments.
     *
     * @param method the http method to use
     * @param apiMethod the api method to call
     * @param httpBody the contents to send to the server.
     * @return result of the http call
     * @throws MollieException when the api key is not set or when there is a
     * problem communicating with the mollie server.
     * @see #performHttpCall(String method, String apiMethod)
     */
    public String performHttpCall(String method, String apiMethod, String httpBody) throws MollieException
    {
        URI uri = null;
        String result = null;

        if (_apiKey == null || _apiKey.trim().equals(""))
        {
            throw new MollieException("You have not set an api key. Please use setApiKey() to set the API key.");
        }

        try {
            URIBuilder ub = new URIBuilder(this._apiEndpoint + "/" + API_VERSION + "/" + apiMethod);
            uri = ub.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri != null)
        {
            CloseableHttpClient httpclient = HttpClientBuilder.create().build();
            HttpRequestBase action = null;
            HttpResponse response = null;

            if (method.equals(HTTP_POST)) {
                action = new HttpPost(uri);
            } else if (method.equals(HTTP_DELETE)) {
                action = new HttpDelete(uri);
            } else {
                action = new HttpGet(uri);
            }

            if (httpBody != null && action instanceof HttpPost)
            {
                StringEntity entity = new StringEntity(httpBody, ContentType.APPLICATION_JSON);
                ((HttpPost)action).setEntity(entity);
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
            } catch (Exception e) {
                throw new MollieException("Unable to communicate with Mollie");
            }

            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}

package com.hcl.poc.rest.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Client {
	private static Logger logger = LogManager.getLogger(Client.class);
	private String baseUrl;
	
	public Client(final String url) {
		baseUrl = url;
	}

	public Response get() {
		return this.execute(Method.GET, null);
	}

	public Response post(String body) {
		return this.execute(Method.POST, body);
	}

	public Response put(String body) {
		return this.execute(Method.PUT, body);
	}

	public Response delete() {
		return this.execute(Method.DELETE, null);
	}

	public Response execute(Method method, final String body) {
		Response response = new Response();

		try {
			URL url = new URL(baseUrl);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		       
			connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON );
			connection.setReadTimeout(60000);
			if (method != Method.GET) {
	      	    connection.setDoOutput(true);
	      	    connection.setRequestMethod(method.toString());
	        	final OutputStream os = connection.getOutputStream();
	        	if(body != null){
	        		os.write(body.getBytes("UTF-8"));
	        	}
                os.flush();
                os.close();
	        }
			
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String line;
			while ((line = rd.readLine()) != null) {
				response.setBody(response.getBody() + line);
			}
			rd.close();

			response.setStatusCode(connection.getResponseCode());
			connection.disconnect();
		} catch (Exception e) {
			response.setException(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return response;
	}

}
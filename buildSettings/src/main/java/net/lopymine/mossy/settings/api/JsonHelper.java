package net.lopymine.mossy.settings.api;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class JsonHelper {

	public static JsonElement get(String url) throws Exception {
		URI uri = URI.create(url);
		HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
		connection.setRequestMethod("GET");
		JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
		return new Gson().fromJson(jsonReader, TypeToken.get(JsonElement.class));
	}

}

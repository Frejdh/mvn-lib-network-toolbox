package com.frejdh.util.networking;

import com.frejdh.util.common.AnsiColor;
import com.frejdh.util.common.AnsiLogger;
import com.frejdh.util.common.AnsiOutput;
import com.frejdh.util.common.toolbox.DateUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;

/**
 * Toolbox for networking. An environmental variable called 'captureTimes' may be used to print the times for converting the objects.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NetworkUtils {

	/**
	 * Convert a BufferedReader instance to a string
	 * @param bufferedReader Instance to convert
	 * @return A string with the object's readable content
	 */
	public static String bufferedReaderToString(BufferedReader bufferedReader) {
		long time = System.currentTimeMillis();

		// Build the HTML string.
		StringBuilder sb = new StringBuilder();
		String tempLine;
		try {
			while ((tempLine = bufferedReader.readLine()) != null) {
				sb.append(tempLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (System.getProperty("captureTimes") != null)
			AnsiLogger.information("BufferedReader to String took ", AnsiColor.BRIGHT_BLUE, (System.currentTimeMillis() - time), AnsiColor.DEFAULT, " ms");

		return sb.toString();
	}

	/**
	 * Converts a string containing a JSON string to an object. Example call: 'bufferedReaderJsonToObject(reader, ExampleClass.class)'.
	 * Can be used with objects that utilizes the GregorianCalendar class.
	 *
	 * @param jsonString The buffered reader that contains a JSON string
	 * @param objectClass Class to convert to
	 * @param <T> Dependent on the class to convert to
	 * @return An instance of the wanted object
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> objectClass) {
		long time = System.currentTimeMillis();
		Gson gson = new GsonBuilder().registerTypeAdapter(Calendar.class, new DateUtils.GregorianCalendarDeserializer()).create();
		T instance = gson.fromJson(jsonString, objectClass);
		if (System.getProperty("captureTimes") != null)
			AnsiLogger.information("Json to Object took ", AnsiColor.BRIGHT_BLUE, (System.currentTimeMillis() - time), AnsiColor.DEFAULT, " ms");
		return instance;
	}

	/**
	 * Converts a string containing a JSON string to an object. Example call: 'bufferedReaderJsonToObject(reader, ExampleClass.class)'.
	 * Can be used with objects that utilizes the GregorianCalendar class since it loads an appropriate deserializer by default.
	 *
	 * @param jsonString The buffered reader that contains a JSON string
	 * @param type Class to convert to
	 * @param typeAdapterFactories Nullable, adapter factories to use
	 * @param typeAdapterPackages Nullable, adapters to use
	 * @param <T> The class to convert to
	 * @return An instance of the wanted object
	 */
	public static <T> T jsonToObject(String jsonString, Type type, @Nullable List<TypeAdapterFactory> typeAdapterFactories, @Nullable List<TypeAdapterPackage> typeAdapterPackages) {
		long time = System.currentTimeMillis();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Calendar.class, new DateUtils.GregorianCalendarDeserializer());

		if (typeAdapterFactories != null) {
			for (TypeAdapterFactory factory : typeAdapterFactories) {
				gsonBuilder.registerTypeAdapterFactory(factory);
			}
		}

		if (typeAdapterPackages != null) {
			for (TypeAdapterPackage tap : typeAdapterPackages) {
				gsonBuilder.registerTypeAdapter(tap.getType(), tap.getTypeAdapter());
			}
		}

		Gson gson = gsonBuilder.create();
		T instance = gson.fromJson(jsonString, type);
		if (System.getProperty("captureTimes") != null)
			AnsiLogger.information("Json to Object took ", AnsiColor.BRIGHT_BLUE, (System.currentTimeMillis() - time), AnsiColor.DEFAULT, " ms");
		return instance;
	}

	/**
	 * Converts a BufferedReader containing a JSON string to an object. Example call: 'bufferedReaderJsonToObject(reader, ExampleClass.class)'.
	 * Can be used with objects that utilizes the GregorianCalendar class since it loads an appropriate deserializer by default.
	 *
	 * @param jsonBufferedReader The buffered reader that contains a JSON string
	 * @param objectClass Class to convert to
	 * @param <T> Dependent on the class to convert to
	 * @return An instance of the wanted object
	 */
	public static <T> T jsonToObject(BufferedReader jsonBufferedReader, Class<T> objectClass) {
		return jsonToObject(bufferedReaderToString(jsonBufferedReader), objectClass);
	}

	/**
	 * Converts a BufferedReader containing a JSON string to an object. Example call: 'bufferedReaderJsonToObject(reader, ExampleClass.class)'.
	 * Can be used with objects that utilizes the GregorianCalendar class since it loads an appropriate deserializer by default.
	 *
	 * @param jsonBufferedReader The buffered reader that contains a JSON string
	 * @param objectClass Class to convert to
	 * @param typeAdapterFactories Adapter factories to use
	 * @param typeAdapterPackages Adapters to use
	 * @param <T> Dependent on the class to convert to
	 * @return An instance of the wanted object
	 */
	public static <T> T jsonToObject(BufferedReader jsonBufferedReader, Class<T> objectClass, @Nullable List<TypeAdapterFactory> typeAdapterFactories, @Nullable List<TypeAdapterPackage> typeAdapterPackages) {
		return jsonToObject(bufferedReaderToString(jsonBufferedReader), objectClass, typeAdapterFactories, typeAdapterPackages);
	}

	/**
	 * Returns a "base" URL. Supports both normal and IPv4 addresses.
	 * Example: 'https://stackoverflow.com/questions/' returns 'stackoverflow.com'.
	 * @param url URL to cleanup
	 * @return A new simplified URL
	 * @throws URISyntaxException Thrown if the URL couldn't be formatted
	 */
	public static String getBaseURL(String url) throws URISyntaxException {
		try {
			if (url.matches(".*//.*")) { // Remove protocol part
				url = url.split("//")[1];
			}
			if (url.matches("(?i).*(www[.]).*")) { // Remove www
				url = url.split("(?i)(www[.])")[1];
			}
			if (url.matches(".*[?=&].*")) { // Remove parameters
				url = url.split("[?=&]")[0];
			}
			if (url.matches(".*/.*")) { // Remove slash
				url = url.split("/")[0];
			}

			// If IPv4
			if (url.matches("\\d+[.]\\d+[.]\\d+[.]\\d+")) { // Does not guarantee 0-255 range
				if (url.matches("\\d+[.]\\d+[.]\\d+[.]\\d+") && !isValidIpv4Address(url)) { // Invalid IP
					throw new URISyntaxException(url, "Invalid range of IPv4 address");
				}
			}
			return url;
		} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
			throw new URISyntaxException(url, "Could not find URL base");
		}
	}

	public static boolean isValidIpAddress(String ip) {
		return InetAddressValidator.getInstance().isValid(ip);
	}

	public static boolean isValidIpv4Address(String ip) {
		return InetAddressValidator.getInstance().isValidInet4Address(ip);
	}

	public static boolean isValidIpv6Address(String ip) {
		return InetAddressValidator.getInstance().isValidInet6Address(ip);
	}

	public static class TypeAdapterPackage {
		private Type type;
		private Object typeAdapter;

		public TypeAdapterPackage(@NonNull Type type, @Nullable Object typeAdapter) {
			this.type = type;
			this.typeAdapter = typeAdapter;
		}

		public Type getType() {
			return type;
		}

		public Object getTypeAdapter() {
			return typeAdapter;
		}
	}

}

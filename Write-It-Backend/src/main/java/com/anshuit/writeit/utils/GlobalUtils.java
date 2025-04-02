package com.anshuit.writeit.utils;

import java.util.Objects;

public class GlobalUtils {

	public static String getFormattedString(String message, Object... args) {
		if (Objects.isNull(args) || args.length == 0)
			return message;

		return String.format(message, args);
	}
}

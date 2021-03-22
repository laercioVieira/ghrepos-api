package br.com.laersondev.ghreposapi.utils;

import java.time.Duration;
import java.time.Instant;

public class TimerUtils {

	public static String elapsedTime(final long startTimeInMillis) {
		final Instant end = Instant.now();
		final Instant start = Instant.ofEpochMilli(startTimeInMillis);
		final Duration between = Duration.between(start, end);
		System.out.println(between); // PT1.001S
		return String.format("%02d:%02d:%02d.%04d", //
				between.toHours(), between.toMinutes(), between.getSeconds(), between.toMillis());
	}
}

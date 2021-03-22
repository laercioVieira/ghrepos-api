package br.com.laersondev.ghreposapi.service.mocks;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HtmlPagesScene {

	public static String getHmltPageFromReource(final String page) {
		final URL resource = Thread.currentThread().getContextClassLoader().getResource(page);
		try {
			return Files.readAllLines(Paths.get(resource.toURI())).stream()
					.collect(Collectors.joining(System.getProperty("line.separator")));
		} catch (final IOException e) {
			new RuntimeException(e);
		} catch (final URISyntaxException e) {
			new RuntimeException(e);
		}
		return null;
	}

	public static String htmlPageSCE01() {
		return getHmltPageFromReource("htmlPageSCE01.html");
	}

	public static ResponseEntity<String> htmlPageResponseREADMEFileSCE01() {
		return new ResponseEntity<>(getHmltPageFromReource("htmlPageResponseREADMEFileSCE01.html"), HttpStatus.OK);
	}

	public static ResponseEntity<String> htmlFindPageResponseSCE01() {
		return new ResponseEntity<>(getHmltPageFromReource("htmlFindPageResponseSCE01.html"), HttpStatus.OK);
	}
}

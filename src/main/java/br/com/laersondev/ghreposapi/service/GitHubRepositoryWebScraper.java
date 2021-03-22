package br.com.laersondev.ghreposapi.service;

import static java.util.stream.Collectors.toList;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.RequestEntity.HeadersBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.laersondev.ghreposapi.commons.file.DataStorageUnit;
import br.com.laersondev.ghreposapi.exception.InvalidParameterException;
import br.com.laersondev.ghreposapi.exception.RepositoryNotFoundException;
import br.com.laersondev.ghreposapi.utils.TimerUtils;

@Service
public class GitHubRepositoryWebScraper implements RepositoryWebScraper {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRepositoryWebScraper.class);

	@Autowired
	public RestTemplate client;

	@Value("${ghreposapi.github.url}")
	public String gitHubBaseUrl;

	private static final Pattern PATTERN_SIZEFILE = //
			Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*((?!sloc|line)[a-zA-Z_\\-]+)$");

	private static final Pattern PATTERN_LINES = Pattern.compile("(\\d+)\\s*line");

	public GitHubRepositoryWebScraper() {
	}

	@Override
	public List<FileDetail> listFileDetailsFromRepo(final URL url, final String fullRepoName) {
		this.checkPreconditions(url, fullRepoName);
		final long startTime = System.currentTimeMillis();
		LOGGER.info("Starting repository webscraping for URL: {}", url);

		final Document repoHomePage = Jsoup.parse(this.getRepositoryHomePage(url, fullRepoName));

		final String urlFindPage = repoHomePage.select("a[href*=\"" + fullRepoName + "/find/\"]").attr("href");
		final Document findPage = Jsoup
				.parse(this.getHtmlPage(this.gitHubBaseUrl.concat(urlFindPage), Optional.<HttpHeaders>empty()));

		final String pageListUrlFragment = findPage.select("fuzzy-list").attr("data-url");
		final String listFilesUrlPage = this.gitHubBaseUrl.concat(pageListUrlFragment);
		final FilePathList allFilesResponse = this.requestGET(listFilesUrlPage, FilePathList.class,
				Optional.of(this.createHeadersForRequest()));

		final List<FileDetail> fileDetails = allFilesResponse.getPaths().stream() //
				.map(path -> this.newFilePath(path, listFilesUrlPage)) //
				.collect(toList());

		LOGGER.info("Webscraping for repository: {} ({}) finished. Total time: {}, files count: {}.", //
				fullRepoName, url, TimerUtils.elapsedTime(startTime), fileDetails.size());

		return fileDetails;
	}

	private HttpHeaders createHeadersForRequest() {
		final HttpHeaders headers = new HttpHeaders();
		headers.put("Accept", Arrays.asList("application/json"));
		headers.put("X-Requested-With", Arrays.asList("XMLHttpRequest"));
		return headers;
	}

	private String getRepositoryHomePage(final URL url, final String fullRepoName) {
		try {
			return this.client.getForObject(url.toString(), String.class);
		} catch (final HttpClientErrorException.NotFound notFound) {
			throw new RepositoryNotFoundException(fullRepoName, notFound);
		}
	}

	private String getHtmlPage(final String url, final Optional<HttpHeaders> headers) {
		return this.requestGET(url, String.class, headers);
	}

	private <T> T requestGET(final String url, final Class<T> responseType, final Optional<HttpHeaders> headers) {
		final HeadersBuilder<?> reqBuilder = RequestEntity.get(url);
		if (headers.isPresent()) {
			reqBuilder.headers(headers.get());
		}

		return this.client.exchange(reqBuilder.build(), responseType).getBody();
	}

	private FileDetail newFilePath(final String filePath, final String listFilesUrlPage) {
		String url = listFilesUrlPage.substring(0, listFilesUrlPage.indexOf("/tree-list"));
		url = url.concat("/blob/master/").concat(filePath);
		final Document document = Jsoup.parse(this.getHtmlPage(url, Optional.empty()));

		String linesText = document.select("div.Box-header div.text-mono").text();
		linesText = linesText != null ? linesText.trim() : "";

		return new FileDetail(filePath, this.getLines(linesText), this.getSizeInBytes(linesText));
	}

	private double getSizeInBytes(final String textLine) {
		final Matcher matcher = PATTERN_SIZEFILE.matcher(textLine);
		if (matcher.find()) {
			final double value = Double.parseDouble(matcher.group(1));
			final String unit = matcher.group(2);
			return DataStorageUnit.fromDescOrAbbrev(unit) //
					.map(dsUnit -> dsUnit.toBytes(value)) //
					.orElse(0d);
		}
		return 0;
	}

	private int getLines(final String textLine) {
		final Matcher matcher = PATTERN_LINES.matcher(textLine);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
	}

	private void checkPreconditions(final URL url, final String fullRepositoryName) {
		if (url == null) {
			throw new InvalidParameterException("'URL' must not be null.");
		}

		if (fullRepositoryName == null || fullRepositoryName.trim().isEmpty()) {
			throw new InvalidParameterException("'FullRepositoryName' must not be empty.");
		}
	}

	void setGitHubBaseUrl(final String gitHubBaseUrl) {
		this.gitHubBaseUrl = gitHubBaseUrl;
	}

}
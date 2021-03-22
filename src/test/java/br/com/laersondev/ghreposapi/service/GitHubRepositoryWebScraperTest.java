package br.com.laersondev.ghreposapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestTemplate;

import br.com.laersondev.ghreposapi.exception.InvalidParameterException;
import br.com.laersondev.ghreposapi.exception.RepositoryNotFoundException;
import br.com.laersondev.ghreposapi.service.mocks.HtmlPagesScene;

class GitHubRepositoryWebScraperTest {

	private static final String EXAMPLE_GITHUB_URL = "https://example.github.com";
	private static final String USER01_REPONAME01 = "laercioVieira/git-fontes";
	private static final String USER01_REPONAME02 = "laercioVieira/devops";

	private static final String USER01_REPO01_URL = EXAMPLE_GITHUB_URL + "/" + USER01_REPONAME01;
	private static final String USER01_REPO_NOTFOUND_URL = EXAMPLE_GITHUB_URL + "/test/notfound";

	@InjectMocks
	GitHubRepositoryWebScraper webScraper = new GitHubRepositoryWebScraper();

	@Mock
	RestTemplate clientMock;

	private AutoCloseable closeable;

	@BeforeEach
	void setUp() throws Exception {
		this.closeable = MockitoAnnotations.openMocks(this);
		this.webScraper.setGitHubBaseUrl(EXAMPLE_GITHUB_URL);
		when(this.clientMock.getForObject(USER01_REPO01_URL, String.class)).thenReturn(HtmlPagesScene.htmlPageSCE01());
		when(this.clientMock.getForObject(USER01_REPO_NOTFOUND_URL, String.class)).thenThrow(NotFound.class);
	}

	private ResponseEntity<FilePathList> createFilePathsSCE01() {
		final FilePathList paths = new FilePathList(Arrays.<String>asList("README.md"));
		return new ResponseEntity<>(paths, HttpStatus.OK);
	}

	private ResponseEntity<FilePathList> createEmptyFilePathsSCE02() {
		final FilePathList paths = new FilePathList(Collections.emptyList());
		return new ResponseEntity<>(paths, HttpStatus.OK);
	}

	@AfterEach
	void tearDown() throws Exception {
		this.closeable.close();
	}

	@Test
	void testListOneFileDetailsFromRepo() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO01_URL).toURL();
		when(this.clientMock.exchange(Mockito.any(), Mockito.eq(String.class))) //
				.thenReturn(HtmlPagesScene.htmlFindPageResponseSCE01()) //
				.thenReturn(HtmlPagesScene.htmlPageResponseREADMEFileSCE01());
		when(this.clientMock.exchange(Mockito.any(), Mockito.eq(FilePathList.class)))
				.thenReturn(this.createFilePathsSCE01());

		final List<FileDetail> fileDetailsFromRepo = this.webScraper.listFileDetailsFromRepo(url, USER01_REPONAME01);

		final ObjectAssert<FileDetail> first = assertThat(fileDetailsFromRepo)//
				.isNotNull() //
				.isNotEmpty() //
				.first();

		first.extracting(FileDetail::getExtension, //
				extracting -> extracting.getFilePath(), //
				extracting -> extracting.getLines(), //
				extracting -> extracting.getSize()) //
				.containsExactly("md", "README.md", 2, 22.00);
	}

	@Test
	void testListFileDetailsFromRepoEmptyResult() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO01_URL).toURL();
		when(this.clientMock.exchange(Mockito.any(), Mockito.eq(String.class))) //
				.thenReturn(HtmlPagesScene.htmlFindPageResponseSCE01()) //
				.thenReturn(HtmlPagesScene.htmlPageResponseREADMEFileSCE01());
		when(this.clientMock.exchange(Mockito.any(), Mockito.eq(FilePathList.class)))
				.thenReturn(this.createEmptyFilePathsSCE02());

		final List<FileDetail> fileDetailsFromRepo = this.webScraper.listFileDetailsFromRepo(url, USER01_REPONAME01);

		assertThat(fileDetailsFromRepo).isNotNull().isEmpty();
	}

	@Test
	void testListFileDetailsRepositoryNotFound() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO_NOTFOUND_URL).toURL();
		assertThatExceptionOfType(RepositoryNotFoundException.class)
				.isThrownBy(() -> this.webScraper.listFileDetailsFromRepo(url, USER01_REPONAME01));
	}

	@Test
	void testListFileDetailsInvalidParams() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO_NOTFOUND_URL).toURL();
		assertThatExceptionOfType(InvalidParameterException.class)
				.isThrownBy(() -> this.webScraper.listFileDetailsFromRepo(null, USER01_REPONAME01));

		assertThatExceptionOfType(InvalidParameterException.class)
				.isThrownBy(() -> this.webScraper.listFileDetailsFromRepo(url, ""));

	}

}

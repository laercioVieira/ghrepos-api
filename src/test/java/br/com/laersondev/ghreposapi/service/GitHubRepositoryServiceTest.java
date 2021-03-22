package br.com.laersondev.ghreposapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.laersondev.ghreposapi.endpoint.SummarizedFile;
import br.com.laersondev.ghreposapi.exception.InvalidParameterException;

class GitHubRepositoryServiceTest {

	private static final String REPONAME_01_DEVOPS = "devops";
	private static final String USERNAME_01 = "laercioVieira";
	private static final String EXAMPLE_GITHUB_URL = "https://example.github.com";
	private static final String USER01_REPONAME01 = "laercioVieira/devops";

	private static final String USER01_REPO01_URL = EXAMPLE_GITHUB_URL + "/" + USER01_REPONAME01;
	private static final String USER01_REPO_NOTFOUND_URL = EXAMPLE_GITHUB_URL + "/test/notfound";

	@InjectMocks
	GitHubRepositoryService service = new GitHubRepositoryService();

	@Mock
	RepositoryWebScraper webScraperMock;

	private AutoCloseable closeable;

	@BeforeEach
	void setUp() throws Exception {
		this.closeable = MockitoAnnotations.openMocks(this);
		this.service.setGitHubBaseUrl(EXAMPLE_GITHUB_URL);
	}

	@AfterEach
	void tearDown() throws Exception {
		this.closeable.close();
	}

	@Test
	void testListFilesGroupByExtensionEmptyResult() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO01_URL).toURL();
		when(this.webScraperMock.listFileDetailsFromRepo(url, USER01_REPONAME01)) //
				.thenReturn(Collections.emptyList());

		final List<SummarizedFile> filesGroupByExtension = this.service.listFilesGroupByExtension(USERNAME_01,
				REPONAME_01_DEVOPS);

		assertThat(filesGroupByExtension).isNotNull().isEmpty();
	}

	@Test
	void testListFilesGroupByExtension() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO01_URL).toURL();
		when(this.webScraperMock.listFileDetailsFromRepo(url, USER01_REPONAME01)) //
				.thenReturn(this.listFileDetailsSCE01());

		final List<SummarizedFile> filesGroupByExtension = this.service.listFilesGroupByExtension(USERNAME_01,
				REPONAME_01_DEVOPS);

		final ListAssert<SummarizedFile> listAssert = assertThat(filesGroupByExtension)//
				.isNotNull() //
				.isNotEmpty();

		listAssert.element(0).extracting(SummarizedFile::getExtension, //
				extracting -> extracting.getCount(), //
				extracting -> extracting.getLines(), //
				extracting -> extracting.getBytes()) //
				.containsExactly("bat", 2, 35, 1064.96); //

		listAssert.element(1).extracting(SummarizedFile::getExtension, //
				extracting -> extracting.getCount(), //
				extracting -> extracting.getLines(), //
				extracting -> extracting.getBytes()) //
				.containsExactly("helmignore", 1, 22, 342.0);

		listAssert.element(2).extracting(SummarizedFile::getExtension, //
				extracting -> extracting.getCount(), //
				extracting -> extracting.getLines(), //
				extracting -> extracting.getBytes()) //
				.containsExactly("tpl", 1, 38, 1310.72);
	}

	@Test
	void testListFilesGroupByExtensionWithInvalidParams() {
		assertThatExceptionOfType(InvalidParameterException.class)
				.isThrownBy(() -> this.service.listFilesGroupByExtension(null, REPONAME_01_DEVOPS));

		assertThatExceptionOfType(InvalidParameterException.class)
				.isThrownBy(() -> this.service.listFilesGroupByExtension(USERNAME_01, null));

		assertThatExceptionOfType(InvalidParameterException.class)
				.isThrownBy(() -> this.service.listFilesGroupByExtension(USERNAME_01, ""));
	}

	@Test
	void testListFilesGroupByExtensionWithMalFormedUrl() throws MalformedURLException {
		this.service.setGitHubBaseUrl("github://github.com.br");
		assertThatExceptionOfType(RuntimeException.class)
				.isThrownBy(() -> this.service.listFilesGroupByExtension(USERNAME_01, REPONAME_01_DEVOPS))
				.withMessage("Malformed search URL for the full repository name: %s.", USER01_REPONAME01);
	}

	@Test
	void testClearCacheDoNothing() throws MalformedURLException {
		final URL url = URI.create(USER01_REPO01_URL).toURL();
		when(this.webScraperMock.listFileDetailsFromRepo(url, USER01_REPONAME01)) //
				.thenReturn(Collections.emptyList());

		this.service.clearCache(USERNAME_01, REPONAME_01_DEVOPS);

		assertThatNoException();
	}

	private List<FileDetail> listFileDetailsSCE01() {
		return Arrays.asList( //
				new FileDetail("test.tpl", 38, 1310.72), //
				new FileDetail("example.bat", 30, 1000.00), //
				new FileDetail("anotherexample.bat", 5, 64.96), //
				new FileDetail(".helmignore", 22, 342.0) //
		);
	}

}

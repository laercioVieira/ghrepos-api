package br.com.laersondev.ghreposapi.service;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.laersondev.ghreposapi.endpoint.SummarizedFile;
import br.com.laersondev.ghreposapi.exception.InvalidParameterException;
import br.com.laersondev.ghreposapi.utils.TimerUtils;

@Service
public class GitHubRepositoryService implements RepositoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GitHubRepositoryService.class);

	@Autowired
	private RepositoryWebScraper repositoryWebScraper;

	@Value("${ghreposapi.github.url}")
	public String gitHubBaseUrl;

	public GitHubRepositoryService() {
		super();
	}

	@Override
	@CacheEvict("SummarizedFile")
	public void clearCache(final String user, final String repository) {
		this.checkPreconditions(user, repository);
		LOGGER.info("Invoking 'clearCache' for the repository: {}/{}", user, repository);
	}

	@Override
	@Cacheable("SummarizedFile")
	public List<SummarizedFile> listFilesGroupByExtension(final String user, final String repository) {
		this.checkPreconditions(user, repository);
		final long startTime = System.currentTimeMillis();
		LOGGER.info("Starting listing files for the repository: {}/{}", user, repository);

		final String fullRepoName = user + "/" + repository;
		final List<FileDetail> fileDetailsFromRepo = this.repositoryWebScraper
				.listFileDetailsFromRepo(this.createRepositoryURL(fullRepoName), fullRepoName);

		final Map<String, List<FileDetail>> fileDetails = fileDetailsFromRepo.stream() //
				.collect(groupingBy(FileDetail::getExtension));

		final List<SummarizedFile> summarizedFiles = fileDetails.entrySet().stream().map(entrySet -> { //
			return entrySet.getValue().stream() //
					.map(filePath -> this.newSummarizedFile(entrySet.getKey(), filePath)) //
					.reduce(this.fileAccumulator()) //
					.get();
		}).collect(toList());

		summarizedFiles.sort(Comparator.comparing(SummarizedFile::getExtension));
		LOGGER.info("Listing files for the repository: {} finished. Total time: {}, file extension count: {}.", //
				fullRepoName, TimerUtils.elapsedTime(startTime), summarizedFiles.size());

		return summarizedFiles;
	}

	private BinaryOperator<SummarizedFile> fileAccumulator() {
		return (fileBefore, fileAfter) -> {
			fileBefore.combine(fileAfter);
			return fileBefore;
		};
	}

	private SummarizedFile newSummarizedFile(final String extension, final FileDetail fp) {
		return new SummarizedFile(extension, 1, fp.getLines(), fp.getSize());
	}

	private URL createRepositoryURL(final String fullRepoName) {
		try {
			return URI.create(format("%s/%s", this.gitHubBaseUrl, fullRepoName)).toURL();
		} catch (final MalformedURLException e) {
			throw new RuntimeException(
					String.format("Malformed search URL for the full repository name: %s.", fullRepoName), e);
		}
	}

	private void checkPreconditions(final String user, final String repository) {
		if (user == null || user.trim().isEmpty()) {
			throw new InvalidParameterException("'user' path variable must not be empty.");
		}

		if (repository == null || repository.trim().isEmpty()) {
			throw new InvalidParameterException("'repository' path variable must not be empty.");
		}
	}

	void setGitHubBaseUrl(final String gitHubBaseUrl) {
		this.gitHubBaseUrl = gitHubBaseUrl;
	}

}

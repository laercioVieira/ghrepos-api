package br.com.laersondev.ghreposapi.service;

import java.util.List;

import br.com.laersondev.ghreposapi.endpoint.SummarizedFile;

public interface RepositoryService {
	List<SummarizedFile> listFilesGroupByExtension(String fullRepoName, String repository);

	void clearCache(String user, String repository);
}
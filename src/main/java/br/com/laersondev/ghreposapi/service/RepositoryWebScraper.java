package br.com.laersondev.ghreposapi.service;

import java.net.URL;
import java.util.List;

public interface RepositoryWebScraper {

	List<FileDetail> listFileDetailsFromRepo(final URL url, final String fullRepoName);

}
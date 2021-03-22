package br.com.laersondev.ghreposapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RepositoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RepositoryNotFoundException(final String fullRepoName) {
		this(fullRepoName, null);
	}

	public RepositoryNotFoundException(final String fullRepoName, final Exception exception) {
		super("Repository not found with full name: '" + fullRepoName
				+ "'. Full Repository Name must be like: 'user/repository'", exception);
	}

}

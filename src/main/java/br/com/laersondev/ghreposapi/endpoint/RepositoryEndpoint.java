package br.com.laersondev.ghreposapi.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.laersondev.ghreposapi.service.RepositoryService;

@RestController
public class RepositoryEndpoint {

	@Autowired
	private RepositoryService repositoryService;

	@GetMapping("/repos/{user}/{repository}")
	public List<SummarizedFile> listFiles(@PathVariable("user") final String user,
			@PathVariable("repository") final String repository) {
		return this.repositoryService.listFilesGroupByExtension(user, repository);
	}

	@PostMapping("/repos/{user}/{repository}/clearcache")
	public void clearCache(@PathVariable("user") final String user,
			@PathVariable("repository") final String repository) {
		this.repositoryService.clearCache(user, repository);
	}
}

package br.com.laersondev.ghreposapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestTemplate;

import br.com.laersondev.ghreposapi.endpoint.SummarizedFile;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class GhreposApiApplicationTests {

	@LocalServerPort
	int randomServerPort;

	@Autowired
	RestTemplate restTemplate;

	URI getURIFor(final String path) {
		final String baseUrl = "http://localhost:" + this.randomServerPort + "/api" + path;
		return URI.create(baseUrl);
	}

	@Test
	void testReposSummarized_OneFile() {
		final ResponseEntity<SummarizedFile[]> responseEntity = this.restTemplate
				.getForEntity(this.getURIFor("/repos/laercioVieira/git-fontes"), SummarizedFile[].class);

		assertThat(responseEntity).isNotNull();//
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		final SummarizedFile[] files = new SummarizedFile[] { new SummarizedFile("md", 1, 2, 22.0), };
		assertThat(responseEntity.getBody()).isEqualTo(files);
	}

	@Test
	void testReposSummarized_RepositoryNotFound() {
		assertThatExceptionOfType(NotFound.class) //
				.isThrownBy(() -> this.restTemplate.getForEntity( //
						this.getURIFor("/repos/laercioVieira/testeNotFound"), //
						SummarizedFile[].class))
				.withMessageContaining("Not Found") //
				.matches(exe -> HttpStatus.NOT_FOUND.equals(exe.getStatusCode()));
	}

	@Test
	void testReposSummarized_OK() {
		final ResponseEntity<SummarizedFile[]> responseEntity = this.restTemplate
				.getForEntity(this.getURIFor("/repos/laercioVieira/aprendendoJavaFx"), SummarizedFile[].class);

		assertThat(responseEntity).isNotNull();//
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		final SummarizedFile[] files = new SummarizedFile[] { new SummarizedFile("classpath", 1, 7, 323.0),
				new SummarizedFile("fxbuild", 1, 8, 325.0), new SummarizedFile("gitignore", 1, 4, 26.0),
				new SummarizedFile("java", 2, 21, 304.0), new SummarizedFile("md", 1, 4, 137.0),
				new SummarizedFile("project", 1, 23, 560.0), new SummarizedFile("txt", 1, 0, 0.0), };

		assertThat(responseEntity.getBody()).isEqualTo(files);
	}

}

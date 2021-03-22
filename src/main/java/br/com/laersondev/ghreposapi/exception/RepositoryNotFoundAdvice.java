package br.com.laersondev.ghreposapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class RepositoryNotFoundAdvice extends ResponseEntityExceptionHandler {

	// @ExceptionHandler(RepositoryNotFoundException.class)
	public ResponseEntity<Object> repositoryNotFoundHandler(final RepositoryNotFoundException exception,
			final WebRequest request) {
		return this.handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND,
				request);
	}

}

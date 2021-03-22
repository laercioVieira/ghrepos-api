package br.com.laersondev.ghreposapi.service;

import java.io.Serializable;
import java.util.List;

public class FilePathList implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<String> paths;

	public FilePathList() {
		super();
	}

	public FilePathList(final List<String> paths) {
		super();
		this.paths = paths;
	}

	public List<String> getPaths() {
		return this.paths;
	}

	public void setPaths(final List<String> paths) {
		this.paths = paths;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.paths == null) ? 0 : this.paths.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FilePathList)) {
			return false;
		}
		final FilePathList other = (FilePathList) obj;
		if (this.paths == null) {
			if (other.paths != null) {
				return false;
			}
		} else if (!this.paths.equals(other.paths)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return String.format("FilePathList [paths: %s]",
				this.paths != null ? this.paths.subList(0, Math.min(this.paths.size(), maxLen)) : null);
	}

}

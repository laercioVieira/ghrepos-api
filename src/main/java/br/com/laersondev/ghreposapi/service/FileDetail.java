package br.com.laersondev.ghreposapi.service;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

public class FileDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	private String filePath;
	private int lines;
	private double size;

	public FileDetail(final String file, final int totalLines, final double size) {
		super();
		this.filePath = file;
		this.lines = totalLines;
		this.size = size;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getExtension() {
		final String safeFilePath = Optional.ofNullable(this.filePath)//
				.filter(file -> !file.trim().isEmpty()).orElse("");

		final int index = safeFilePath.lastIndexOf(".");
		return index == -1 ? "No Extension" : safeFilePath.substring(index + 1);
	}

	public void setFilePath(final String file) {
		this.filePath = file;
	}

	public int getLines() {
		return this.lines;
	}

	public void setLines(final int totalLines) {
		this.lines = totalLines;
	}

	public double getSize() {
		return this.size;
	}

	public void setSize(final double size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.filePath, this.lines, this.size);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof FileDetail)) {
			return false;
		}
		final FileDetail other = (FileDetail) obj;
		if (this.filePath == null) {
			if (other.filePath != null) {
				return false;
			}
		} else if (!this.filePath.equals(other.filePath)) {
			return false;
		}
		if (this.lines != other.lines) {
			return false;
		}
		if (this.size != other.size) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("FileDetail [file: %s, totalLines: %s, size: %s]", this.filePath, this.lines, this.size);
	}

}

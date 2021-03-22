package br.com.laersondev.ghreposapi.endpoint;

import java.io.Serializable;
import java.util.Objects;

public class SummarizedFile implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String extension;
	private int count;
	private int lines;
	private double bytes;

	public SummarizedFile(final String extension, final int count, final int lines, final double bytes) {
		super();
		this.extension = extension;
		this.count = count;
		this.lines = lines;
		this.bytes = bytes;
	}

	public void combine(final SummarizedFile other) {
		this.count += other.count;
		this.lines += other.lines;
		this.bytes += other.bytes;
	}

	public String getExtension() {
		return this.extension;
	}

	public int getCount() {
		return this.count;
	}

	public int getLines() {
		return this.lines;
	}

	public double getBytes() {
		return this.bytes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.bytes, this.count, this.extension, this.lines);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SummarizedFile)) {
			return false;
		}
		final SummarizedFile other = (SummarizedFile) obj;
		return Double.doubleToLongBits(this.bytes) == Double.doubleToLongBits(other.bytes) && this.count == other.count
				&& Objects.equals(this.extension, other.extension) && this.lines == other.lines;
	}

	@Override
	public String toString() {
		return String.format("SummarizedFile [extension: %s, count: %s, lines: %s, bytes: %s]", this.extension,
				this.count, this.lines, this.bytes);
	}

}

package br.com.laersondev.ghreposapi.commons.file;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public enum DataStorageUnit {

	BYTE(1, "B", "Byte") {//
		@Override
		public double toBytes(final double value) {
			return value;
		}

		@Override
		boolean matchesDescOrAbbrev(final String unitDesc) {
			return BYTE_PATTERN.matcher(unitDesc.toLowerCase()).matches();
		}
	}, //
	KILOBYTE(2, "KB", "Kilobyte") { //
		@Override
		public double toBytes(final double value) {
			return value * 1024;
		}

		@Override
		boolean matchesDescOrAbbrev(final String unitDesc) {
			return KILOBYTE_PATTERN.matcher(unitDesc.toLowerCase()).matches();
		}
	}, //
	MEGABYTE(3, "MB", "Megabyte") { //
		@Override
		public double toBytes(final double value) {
			return value * 1024 * 1024;
		}

		@Override
		boolean matchesDescOrAbbrev(final String unitDesc) {
			return MEGABYTE_PATTERN.matcher(unitDesc.toLowerCase()).matches();
		}
	}, //
	GIGABYTE(4, "GB", "Gigabyte") { //
		@Override
		public double toBytes(final double value) {
			return value * 1024 * 1024 * 1024;
		}

		@Override
		boolean matchesDescOrAbbrev(final String unitDesc) {
			return GIGABYTE_PATTERN.matcher(unitDesc.toLowerCase()).matches();
		}
	}, //
	TERABYTE(5, "TB", "Terabyte") { //
		@Override
		public double toBytes(final double value) {
			return value * 1024 * 1024 * 1024 * 1024;
		}

		@Override
		boolean matchesDescOrAbbrev(final String unitDesc) {
			return TERABYTE_PATTERN.matcher(unitDesc.toLowerCase()).matches();
		}
	};

	private static final Pattern BYTE_PATTERN = Pattern.compile("^(byte[s]?|b|by|byt|bt[es]?)$");
	private static final Pattern KILOBYTE_PATTERN = Pattern.compile("^(kilo\\s*byte[s]?|kb[syte]?|kilo|kbyte[s]?)$");
	private static final Pattern MEGABYTE_PATTERN = Pattern
			.compile("^(mega\\s*byte[s]?|mb[syte]?|mg[sa]?|mega[s]?|m[g]?[\\s]*byte[s]?|mga[\\s]*byte[s]?)$");
	private static final Pattern GIGABYTE_PATTERN = Pattern
			.compile("^(giga\\s*byte[s]?|gi[g]?|gb[syte]?|giga[s]?|g[gb]?\\s*byte[s]?|gig\\s*byte[s]?)$");
	private static final Pattern TERABYTE_PATTERN = Pattern
			.compile("^(tera\\s*byte[s]?|trb|tb[syte]?|tera[s]?|t[rb]?\\s*byte[s]?|tra\\s*byte[s]?)$");

	private int value;
	private String abbreviation;
	private String description;

	DataStorageUnit(final int value, final String abbreviation, final String description) {
		this.value = value;
		this.abbreviation = abbreviation;
		this.description = description;
	}

	public int getValue() {
		return this.value;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public String getDescription() {
		return this.description;
	}

	public abstract double toBytes(double value);

	abstract boolean matchesDescOrAbbrev(final String unitDesc);

	public static Optional<DataStorageUnit> fromDescOrAbbrev(final String unitDesc) {
		if (unitDesc == null || unitDesc.trim().isEmpty()) {
			return Optional.empty();
		}

		return Arrays.stream(values()).filter(unit -> unit.matchesDescOrAbbrev(unitDesc)).findFirst();
	}

}

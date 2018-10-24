package cli;

import java.util.function.Function;

public class Parser extends Command {
	protected Function<String[], String> action;
	protected String suffix;

	public Parser(
			final String name,
			final Function<String[], String> action,
			final int values,
			final String suffix) {
		this.setName(name);
		this.setAction(action);
		this.setValues(values);
		this.setSuffix(suffix);
	}

	public Parser(
			final String name,
			final Function<String[], String> action,
			final int values) {
		this(name, action, values, null);
	}

	public Parser(
			final String name,
			final Function<String[], String> action) {
		this(name, action, 1);
	}

	public void setAction(Function<String[], String> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	public Function<String[], String> getAction() {
		return this.action;
	}

	public void setSuffix(final String suffix) {
		if (suffix != null
				&& (!suffix.equals(suffix.trim())
					|| suffix.length() == 0)) {
			throw new IllegalArgumentException(
					"Invalid suffix \"" + suffix + "\"");
		}
		this.suffix = suffix;
	}

	public String getSuffix() {
		return this.suffix;
	}
}

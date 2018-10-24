package cli;

import java.util.function.Function;

public class Parser {
	private String name;
	private Function<String[], String> action;

	public Parser(
			final String name,
			final Function<String[], String> action) {
		this.setName(name);
		this.setAction(action);
	}

	public void setName(final String name) {
		if (name == null || name.contains(" ") || name.equals("")) {
			throw new IllegalArgumentException("Invalid name \"" + name + "\"");
		}
		this.name = name;
	}

	public String getName() {
		return this.name;
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
}

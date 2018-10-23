package cli;

import java.util.function.Consumer;

public class Command {
	private String name;
	private Consumer<String> action;

	public Command(String name, Consumer<String> action) {
		this.setName(name);
		this.setAction(action);
	}

	public void setName(String name) {
		if (name == null || name.contains(" ") || name.equals("")) {
			throw new IllegalArgumentException("Invalid name \"" + name + "\"");
		}
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setAction(Consumer<String> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	public Consumer<String> getAction() {
		return this.action;
	}
}

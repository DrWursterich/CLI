package cli;

import java.util.function.Consumer;

public class Command {
	private String name;
	private Consumer<String[]> action;
	private int values;
	private boolean allowMultiple;

	public Command(
			final String name,
			final Consumer<String[]> action,
			final int values,
			final boolean allowMultiple) {
		this.setName(name);
		this.setAction(action);
		this.setValues(values);
		this.setAllowMultiple(allowMultiple);
	}

	public Command(final String name, final Consumer<String[]> action) {
		this(name, action, 1, false);
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

	public void setAction(final Consumer<String[]> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	public Consumer<String[]> getAction() {
		return this.action;
	}

	public void setValues(final int values) {
		this.values = values;
	}

	public int getValues() {
		return this.values;
	}

	public void setAllowMultiple(final boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}

	public boolean allowMultiple() {
		return this.allowMultiple;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode()
				+ this.action.hashCode()
				+ this.values
				+ (this.allowMultiple ? 1 : 0);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Command)) {
			return false;
		}
		Command command = (Command)other;
		return command.name == this.name
				&& command.action.equals(this.action)
				&& command.values == this.values
				&& command.allowMultiple == this.allowMultiple;
	}

	@Override
	protected Object clone() {
		return new Command(
				this.name, this.action, this.values, this.allowMultiple);
	}
}

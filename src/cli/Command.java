package cli;

import java.util.function.Consumer;

public class Command {
	private String name;
	private Consumer<String> action;
	private boolean hasValue;
	private boolean allowMultiple;

	public Command(
			final String name,
			final Consumer<String> action,
			final boolean hasValue,
			final boolean allowMultiple) {
		this.setName(name);
		this.setAction(action);
		this.setHasValue(hasValue);
		this.setAllowMultiple(allowMultiple);
	}

	public Command(final String name, final Consumer<String> action) {
		this(name, action, true, false);
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

	public void setAction(final Consumer<String> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	public Consumer<String> getAction() {
		return this.action;
	}

	public void setHasValue(final boolean hasValue) {
		this.hasValue = hasValue;
	}

	public boolean hasValue() {
		return this.hasValue;
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
				+ (this.hasValue ? 1 : 0)
				+ (this.allowMultiple ? 2 : 0);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Command)) {
			return false;
		}
		Command command = (Command)other;
		return command.name == this.name
				&& command.action.equals(this.action)
				&& command.hasValue == this.hasValue
				&& command.allowMultiple == this.allowMultiple;
	}

	@Override
	protected Object clone() {
		return new Command(
				this.name, this.action, this.hasValue, this.allowMultiple);
	}
}

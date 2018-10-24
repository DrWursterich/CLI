package cli;

import java.util.function.Consumer;

public class Flag extends Command {
	protected Consumer<String[]> action;
	protected boolean allowMultiple;

	public Flag(
			final String name,
			final Consumer<String[]> action,
			final int values,
			final boolean allowMultiple) {
		this.setName(name);
		this.setAction(action);
		this.setValues(values);
		this.setAllowMultiple(allowMultiple);
	}

	public Flag(final String name, final Consumer<String[]> action) {
		this(name, action, 1, false);
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

	public void setAllowMultiple(final boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}

	public boolean allowMultiple() {
		return this.allowMultiple;
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
		if (other == null || !(other instanceof Flag)) {
			return false;
		}
		Flag command = (Flag)other;
		return command.name == this.name
				&& command.action.equals(this.action)
				&& command.values == this.values
				&& command.allowMultiple == this.allowMultiple;
	}

	@Override
	protected Object clone() {
		return new Flag(
				this.name, this.action, this.values, this.allowMultiple);
	}
}

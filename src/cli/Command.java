package cli;

public abstract class Command {
	protected String name;
	protected int values;

	public Command() {
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

	public void setValues(final int values) {
		if (values < 0) {
			throw new IllegalArgumentException("Invalid amount of values \"" + values + "\"");
		}
		this.values = values;
	}

	public int getValues() {
		return this.values;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() + this.values;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Flag)) {
			return false;
		}
		Command command = (Command)other;
		return command.name == this.name
				&& command.values == this.values;
	}
}

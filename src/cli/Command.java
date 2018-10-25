package cli;

/**
 * A abstract Class to represent the Model of CLI Input Commands
 * such as Flags or Parsers.<br/>
 * @author Mario Schäper
 */
public abstract class Command {
	protected static int defaultValues = 1;
	protected String name;
	protected int values;

	/**
	 * Determines the Amount of Values expected by the Action of Commands,
	 * that are invoked without a specific Value.
	 * @param defaultValues the Amount of Values expected
	 * @throws IllegalArgumentException if the Amount is invalid
	 */
	public static void setDefaultValues(final int defaultValues)
			throws IllegalArgumentException {
		Command.checkValues(defaultValues);
		Command.defaultValues = defaultValues;
	}

	/**
	 * Returns the Amount of Values expected by the Action of Commands,
	 * that are invoked without a specific Value.
	 * @return the Amount of Values expected
	 */
	public static int getDefaultValues() {
		return Command.defaultValues;
	}

	/**
	 * Determines the Name/Prefix of this Command.<br/>
	 * For example, a Command with the Name <em>"-c"</em>
	 * and the Values of <em>0</em>
	 * would have to be formated <em>"-c"</em> to be parsed correctly.
	 * @param name the Name/Prefix
	 * @throws IllegalArgumentException if the Name is invalid
	 */
	public void setName(final String name) throws IllegalArgumentException {
		if (name == null || name.contains(" ") || name.equals("")) {
			throw new IllegalArgumentException("Invalid name \"" + name + "\"");
		}
		this.name = name;
	}

	/**
	 * Returns the Name/Prefix of this Command.<br/>
	 * For example, a Command with the Name <em>"-c"</em>
	 * and the Values of <em>0</em>
	 * would have to be formated <em>"-c"</em> to be parsed correctly.
	 * @return the Name/Prefix
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Determines the Amount of Values expected by the Action of this Command.
	 * @param values the Amount of Values expected
	 * @throws IllegalArgumentException if the Amount is invalid
	 */
	public void setValues(final int values) throws IllegalArgumentException {
		Command.checkValues(values);
		this.values = values;
	}

	/**
	 * Returns the Amount of Values expected by the Action of this Command.
	 * @return the Amount of Values expected
	 */
	public int getValues() {
		return this.values;
	}

	/**
	 * Returns the Commands Name.
	 * @return the Name
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode() + this.values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		if (other == null || !(other instanceof Flag)) {
			return false;
		}
		Command command = (Command)other;
		return command.name == this.name
				&& command.values == this.values;
	}

	protected static void checkValues(final int values)
			throws IllegalArgumentException {
		if (values < 0) {
			throw new IllegalArgumentException(
					"Invalid amount of values \"" + values + "\"");
		}
	}
}

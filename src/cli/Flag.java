package cli;

import java.util.function.Consumer;

/**
 * The Model of a Flag of a CLI Input.<br/>
 * Can be used in Combination with the {@link cli.CommandHandler CommandHandler}
 * to handle CLI Input-like Arguments.
 * @author Mario Schäper
 */
public class Flag extends Command {
	protected static String defaultValueSeparator = "-";
	protected static boolean defaultAllowMultiple = false;
	protected Consumer<String[]> action;
	protected String valueSeparator;
	protected boolean allowMultiple;

	/**
	 * Determines the valueSeparator, with which new Flags are instanciated.
	 * Can only contain Whitespace, if thats the only Char.
	 * @param defaultValueSeparator the default valueSeparator
	 * @throws IllegalArgumentException if the valueSeparator is invalid
	 */
	public static void setDefaultValueSeparator(
			final String defaultValueSeparator)
			throws IllegalArgumentException {
		Flag.checkValueSeparator(defaultValueSeparator);
		Flag.defaultValueSeparator = defaultValueSeparator;
	}

	/**
	 * Returns the valueSeparator, with which new Flags are instanciated.
	 * @return the valueSeparator
	 */
	public static String getDefaultValueSeparator() {
		return Flag.defaultValueSeparator;
	}

	/**
	 * Determines, whether multiple Occurrences of newly instanciated Flags
	 * are tolerated by the {@link cli.CommandHandler CommandHandler}.
	 * @param allowMultiple whether multiple Occurences of new Flags are allowed
	 */
	public static void setDefaultAllowMultiple(final boolean allowMultiple) {
		Flag.defaultAllowMultiple = allowMultiple;
	}

	/**
	 * Returns, whether multiple Occurrences of newly instanciated Flags
	 * are tolerated by the {@link cli.CommandHandler CommandHandler}.
	 * @returns whether multiple Occurences of new Flags are allowed
	 */
	public static boolean getDefaultAllowMultiple() {
		return Flag.defaultAllowMultiple;
	}

	/**
	 * @param name the Name/Prefix (e.g. "-f", "--options")
	 * @param action the Action, to be called when handled
	 * @param valueSeparator separation of Key and Values
	 * (e.g. "-" -> "--options-value1-value2")
	 * @param values the Amount of Values expected
	 * @param allowMultiple whether multiple Occurences should be possible
	 */
	public Flag(
			final String name,
			final Consumer<String[]> action,
			final String valueSeparator,
			final int values,
			final boolean allowMultiple) {
		this.setName(name);
		this.setAction(action);
		this.setValueSeparator(valueSeparator);
		this.setValues(values);
		this.setAllowMultiple(allowMultiple);
	}

	/**
	 * Uses the default Value for<ul style="list-style:none;">
	 * <li><b>allowMultiple</b> whether multiple Occurences
	 * should be possible</li>
	 * </ul>
	 * @param name the Name/Prefix (e.g. "-f", "--options")
	 * @param action the Action, to be called when handled
	 * @param valueSeparator separation of Key and Values
	 * (e.g. "-" -> "--options-value1-value2")
	 * @param values the Amount of Values expected
	 */
	public Flag(
			final String name,
			final Consumer<String[]> action,
			final String valueSeparator,
			final int values) {
		this(name, action, valueSeparator, values, Flag.defaultAllowMultiple);
	}

	/**
	 * Uses the default Value for<ul style="list-style:none;">
	 * <li><b>allowMultiple</b> whether multiple Occurences
	 * should be possible</li>
	 * <li><b>values</b> the Amount of Values expected</li>
	 * </ul>
	 * @param name the Name/Prefix (e.g. "-f", "--options")
	 * @param action the Action, to be called when handled
	 * @param valueSeparator separation of Key and Values
	 * (e.g. "-" -> "--options-value1-value2")
	 */
	public Flag(
			final String name,
			final Consumer<String[]> action,
			final String valueSeparator) {
		this(name, action, valueSeparator, Command.defaultValues);
	}

	/**
	 * Uses the default Value for<ul style="list-style:none;">
	 * <li><b>allowMultiple</b> whether multiple Occurences
	 * should be possible</li>
	 * <li><b>values</b> the Amount of Values expected</li>
	 * <li><b>valueSeparator</b> separation of Key and Values
	 * (e.g. "-" -> "--options-value1-value2")</li>
	 * </ul>
	 * @param name the Name/Prefix (e.g. "-f", "--options")
	 * @param action the Action, to be called when handled
	 */
	public Flag(final String name, final Consumer<String[]> action) {
		this(name, action, Flag.defaultValueSeparator);
	}

	/**
	 * Determines the Action, that is called,
	 * when a Occurence of this Flag is handled.<br/>
	 * It will be invoked with its given Values
	 * and only if their Amount is as specified.
	 * @param action the Action
	 */
	public void setAction(final Consumer<String[]> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	/**
	 * Returns the Action, that is called,
	 * when a Occurence of this Flag is handled.<br/>
	 * It will be invoked with its given Values
	 * and only if their Amount is as specified.
	 * @return the Action
	 */
	public Consumer<String[]> getAction() {
		return this.action;
	}

	/**
	 * Determines the String, that separates Key and Values of this Flag.
	 * Can only contain Whitespace, if thats the only Char.
	 * @param valueSeparator the String, that seperate Key and Values
	 * @throws IllegalArgumentException if the valueSeparator is invalid
	 */
	public void setValueSeparator(final String valueSeparator)
			throws IllegalArgumentException {
		Flag.checkValueSeparator(valueSeparator);
		this.valueSeparator = valueSeparator;
	}

	/**
	 * Returns the String, that separates Key and Values of this Flag.
	 * @return the String, that seperate Key and Values
	 */
	public String getValueSeparator() {
		return this.valueSeparator;
	}

	/**
	 * Determines, whether multiple Occurrences of this Flag
	 * are tolerated by the {@link cli.CommandHandler CommandHandler}.
	 * @param allowMultiple wehter multiple Occurences of this are allowed
	 */
	public void setAllowMultiple(final boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}

	/**
	 * Returns, whether multiple Occurrences of this Flag
	 * are tolerated by the {@link cli.CommandHandler CommandHandler}.
	 * @return wehter multiple Occurences of this are allowed
	 */
	public boolean allowMultiple() {
		return this.allowMultiple;
	}

	/**
	 * Returns, whether the given String fits this Flag.
	 * @param str the String
	 * @return whether the given String fits this Flag
	 */
	public boolean isThis(final String str) {
		final int nameLength = this.name.length();
		return this.valueSeparator.equals(" ") || this.values == 0
				? this.name.equals(str)
				: str.substring(nameLength).startsWith(this.valueSeparator)
					&& str.length() > nameLength
					&& this.name.equals(str.substring(0, nameLength));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode()
				+ this.action.hashCode()
				+ this.values
				+ (this.allowMultiple ? 1 : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		if (other == null || !(other instanceof Flag)) {
			return false;
		}
		Flag command = (Flag)other;
		return command.name == this.name
				&& command.action.equals(this.action)
				&& command.values == this.values
				&& command.allowMultiple == this.allowMultiple;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object clone() {
		return new Flag(
				this.name,
				this.action,
				this.valueSeparator,
				this.values,
				this.allowMultiple);
	}

	protected static void checkValueSeparator(final String valueSeparator)
			throws IllegalArgumentException {
		if (valueSeparator == null
				|| valueSeparator.equals("")
				|| (valueSeparator.contains(" ")
					&& valueSeparator.length() > 1)) {
			throw new IllegalArgumentException(
					"Invalid valueSeparator \"" + valueSeparator + "\"");
		}
	}
}

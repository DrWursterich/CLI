package cli;

import java.util.function.Function;

/**
 * The Model of a Parser of a CLI Input.<br/>
 * Can be used in Combination with the {@link cli.CommandHandler CommandHandler}
 * to handle CLI Input-like Arguments.
 * @author Mario Schäper
 */
public class Parser extends Command {
	protected static String defaultSuffix = null;
	protected Function<String[], String> action;
	protected String suffix;

	/**
	 * Determines the Suffix, with which new Parsers are instanciated.
	 * Can be <b>null</b> if no Suffix should be required.
	 * @param defaultSuffix the default Suffix
	 * @throws IllegalArgumentException if the Suffix is invalid
	 */
	public static void setDefaultSuffix(final String defaultSuffix)
			throws IllegalArgumentException {
		Parser.checkSuffix(defaultSuffix);
		Parser.defaultSuffix = defaultSuffix;
	}

	/**
	 * Returns the Suffix, with which new Parsers are instanciated.
	 * Can be <b>null</b> if no Suffix is required.
	 * @returns the default Suffix
	 * @throws IllegalArgumentException if the Suffix is invalid
	 */
	public static String getDefaultSuffix() {
		return Parser.defaultSuffix;
	}

	/**
	 * @param name the Name/Prefix (e.g. "-us", "[")
	 * @param action the Action, to be called when parsed
	 * @param values the Amount of Values expected
	 * @param suffix the Suffix (e.g. "-ue", "]")
	 */
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

	/**
	 * Uses the default Value for<ul style="list-style:none;">
	 * <li><b>suffix</b> the Suffix (e.g. "-ue", "]")</li>
	 * </ul>
	 * @param name the Name/Prefix (e.g. "-us", "[")
	 * @param action the Action, to be called when parsed
	 * @param values the Amount of Values expected
	 */
	public Parser(
			final String name,
			final Function<String[], String> action,
			final int values) {
		this(name, action, values, Parser.defaultSuffix);
	}

	/**
	 * Uses the default Value for<ul style="list-style:none;">
	 * <li><b>suffix</b> the Suffix (e.g. "-ue", "]")</li>
	 * <li><b>values</b> the Amount of Values expected</li>
	 * </ul>
	 * @param name the Name/Prefix (e.g. "-us", "[")
	 * @param action the Action, to be called when parsed
	 */
	public Parser(
			final String name,
			final Function<String[], String> action) {
		this(name, action, Parser.defaultValues);
	}

	/**
	 * Determines the Action, that is called,
	 * when a Occurence of this Parser is parsed.<br/>
	 * It will be invoked with its given Values
	 * and only if their Amount is as specified.
	 * The Action returns a String,
	 * that replaces the Values and {@link cli.Flag Flags}
	 * it was invoked with in the Arguments.
	 * @param action the Action
	 */
	public void setAction(final Function<String[], String> action) {
		if (action == null) {
			throw new IllegalArgumentException("Invalid action");
		}
		this.action = action;
	}

	/**
	 * Returns the Action, that is called,
	 * when a Occurence of this Parser is parsed.<br/>
	 * It will be invoked with its given Values
	 * and only if their Amount is as specified.
	 * The Action returns a String,
	 * that replaces the Values and {@link cli.Flag Flags}
	 * it was invoked with in the Arguments.
	 * @param action the Action
	 */
	public Function<String[], String> getAction() {
		return this.action;
	}

	/**
	 * Determines the String, that limits the length of the Parser.
	 * Can be <b>null</b> if no Suffix is required.
	 * @param suffix the String, that limits the length of the Parser
	 * @throws IllegalArgumentException if the Suffix is invalid
	 */
	public void setSuffix(final String suffix) {
		Parser.checkSuffix(suffix);
		this.suffix = suffix;
	}

	/**
	 * Returns the String, that limits the length of the Parser.
	 * @return the Suffix
	 */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Returns, whether the given String fits this Parser.
	 * @param str the String
	 * @return whether the given String fits this Parser
	 */
	public boolean isThis(final String str) {
		return this.name.equals(str);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode()
				+ this.action.hashCode()
				+ this.values
				+ this.suffix.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object other) {
		if (other == null || !(other instanceof Parser)) {
			return false;
		}
		Parser parser = (Parser)other;
		return parser.name == this.name
				&& parser.action.equals(this.action)
				&& parser.values == this.values
				&& parser.suffix == this.suffix;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object clone() {
		return new Parser(
				this.name,
				this.action,
				this.values,
				this.suffix);
	}

	protected static void checkSuffix(final String suffix)
			throws IllegalArgumentException {
		if (suffix != null
				&& (!suffix.equals(suffix.trim())
					|| suffix.length() == 0)) {
			throw new IllegalArgumentException(
					"Invalid suffix \"" + suffix + "\"");
		}
	}
}

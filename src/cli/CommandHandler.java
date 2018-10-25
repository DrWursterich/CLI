package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Static Class for Parsing and Handling Arguments of a CLI Input.
 * @author Mario Schäper
 */
public class CommandHandler {
	protected static List<Flag> flags = new ArrayList<>();
	protected static List<Parser> parsers = new ArrayList<>();

	/**
	 * Sets the {@link cli.Flag Flags}, that are defaultly
	 * used for Parsing and Handling.
	 * @param flags the Flags
	 */
	public static void setFlags(final List<Flag> flags) {
		if (flags == null) {
			throw new IllegalArgumentException("Invalid flags");
		}
		CommandHandler.flags = flags;
	}

	/**
	 * Sets the {@link cli.Flag Flags}, that are defaultly
	 * used for Parsing and Handling.
	 * @param flags the Flags
	 */
	public static void setFlags(final Flag...flags) {
		CommandHandler.flags = Arrays.asList(flags);
	}

	/**
	 * Returns the {@link cli.Flag Flags}, that are defaultly
	 * used for Parsing and Handling.
	 * @return the default Flags
	 */
	public static List<Flag> getFlags() {
		return CommandHandler.flags;
	}

	/**
	 * Sets the {@link cli.Parser Parsers}, that are defaultly used for Parsing.
	 * @param parsers the Parser
	 */
	public static void setParsers(final List<Parser> parsers) {
		if (parsers == null) {
			throw new IllegalArgumentException("Invalid parser");
		}
		CommandHandler.parsers = parsers;
	}

	/**
	 * Sets the {@link cli.Parser Parsers}, that are defaultly used for Parsing.
	 * @param parsers the Parser
	 */
	public static void setParsers(final Parser...parsers) {
		CommandHandler.parsers = Arrays.asList(parsers);
	}

	/**
	 * Returns the {@link cli.Parser Parsers}, that are defaultly
	 * used for Parsing.
	 * @return the default Parsers
	 */
	public static List<Parser> getParsers() {
		return CommandHandler.parsers;
	}

	/**
	 * Resolves all {@link cli.Flag Flags} and their Values
	 * in the given Arguments.<br/>
	 * Returns the leftover Arguments.
	 * @param flags the Flags
	 * @param args the Arguments
	 * @return the Arguments, that could not be resolved
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] handleArguments(
			final List<Flag> flags,
			final String...args)
			throws IllegalArgumentException {
		final ArrayList<String> ignoredArgs = new ArrayList<>();
		final ArrayList<Flag> unusedFlags = new ArrayList<>();
		unusedFlags.addAll(flags);
		for (int i = 0;i < args.length; i++) {
			String arg = args[i];
			final Flag flag = CommandHandler.getFlagByString(arg, unusedFlags);
			if (flag != null) {
				final String separator = flag.getValueSeparator();
				final String[] values = new String[flag.getValues()];
				for (int j = 0; j < flag.getValues(); j++) {
					if (separator.equals(" ")) {
						if (args.length > ++i) {
							values[j] = args[i];
							break;
						} else {
							throw new IllegalArgumentException(
									"Missing parameter for " + arg);
						}
					} else {
						arg = arg.substring(1 + (j == 0
								? flag.getName().length()
								: arg.indexOf(separator)));
						final int index = arg.indexOf(separator);
						if (index == -1) {
							values[j] = arg;
						} else {
							values[j] = arg.substring(0, index);
							if (j + 1 == flag.getValues()) {
								throw new IllegalArgumentException(
										"Invalid amount of values supplied for the flag \""
												+ flag.getName() + "\"");
							}
						}
					}
				}
				if (!flag.allowMultiple()) {
					unusedFlags.remove(flag);
				}
				flag.getAction().accept(values);
			} else {
				ignoredArgs.add(arg);
			}
		}
		return ignoredArgs.toArray(new String[ignoredArgs.size()]);
	}

	/**
	 * Resolves all {@link cli.Flag Flags} and their Values
	 * in the given Arguments.<br/>
	 * Returns the leftover Arguments.
	 * @param flags the Flags
	 * @param args the Arguments
	 * @return the Arguments, that could not be resolved
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] handleArguments(
			final Flag[] flags,
			final String...args)
			throws IllegalArgumentException {
		return CommandHandler.handleArguments(Arrays.asList(flags), args);
	}

	/**
	 * Resolves all default {@link cli.Flag Flags} and their Values
	 * in the given Arguments.<br/>
	 * Returns the leftover Arguments.
	 * @param args the Arguments
	 * @return the Arguments, that could not be resolved
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] handleArguments(final String...args)
			throws IllegalArgumentException {
		return CommandHandler.handleArguments(CommandHandler.flags, args);
	}

	/**
	 * Resolves all {@link cli.Parser Parsers} in the given Arguments.<br/>
	 * Takes Account for all {@link cli.Flag Flags} given.
	 * Those should be all Flags, that may appear in the Arguments,
	 * including Flags, that are only handled by Parsers<br/>
	 * Returns the resulting Arguments.
	 * @param parsers the Parsers
	 * @param flags the Flags
	 * @param args the Arguments
	 * @return the resulting Arguments
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] parseArguments(
			final List<Parser> parsers,
			final List<Flag> flags,
			final String...args)
			throws IllegalArgumentException {
		String[] newArgs = Arrays.copyOf(args, args.length);
		for (int i = newArgs.length - 1; i >= 0; i--) {
			final Parser parser = CommandHandler.getParserByString(
					newArgs[i], parsers);
			if (parser == null) {
				continue;
			}
			final ArrayList<String> toParse = new ArrayList<>();
			int parsValues = 0;
			for (int j = i + 1;j < newArgs.length;j++) {
				if (newArgs[j].equals(parser.getSuffix())
						|| (parser.getSuffix() == null
							&& parsValues == parser.getValues())) {
					break;
				}
				toParse.add(newArgs[j]);
				final Flag flag = CommandHandler.getFlagByString(newArgs[j], flags);
				if (flag != null) {
					if (flag.getValueSeparator().equals(" ")) {
						int values = flag.getValues();
						if (j + values >= newArgs.length) {
							throw new IllegalArgumentException(
									"Invalid formatting");
						}
						while(--values >= 0) {
							toParse.add(newArgs[++j]);
						}
					}
				} else {
					parsValues++;
				}
			}
			newArgs[i] = parser.getAction().apply(
					toParse.toArray(new String[toParse.size()]));
			final int offset = toParse.size()
					+ (parser.getSuffix() == null ? 0 : 1);
			for (int j = i + offset + 1;j < newArgs.length; j++) {
				newArgs[j - offset] = newArgs[j];
			}
			newArgs = Arrays.copyOf(newArgs, newArgs.length - offset);
		}
		return newArgs;
	}

	/**
	 * Resolves all {@link cli.Parser Parsers} in the given Arguments.<br/>
	 * Takes Account for all {@link cli.Flag Flags} given.
	 * Those should be all Flags, that may appear in the Arguments,
	 * including Flags, that are only handled by Parsers<br/>
	 * Returns the resulting Arguments.
	 * @param parsers the Parsers
	 * @param flags the Flags
	 * @param args the Arguments
	 * @return the resulting Arguments
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] parseArguments(
			final Parser[] parsers,
			final Flag[] flags,
			final String...args)
			throws IllegalArgumentException {
		return CommandHandler.parseArguments(
				Arrays.asList(parsers), Arrays.asList(flags), args);
	}

	/**
	 * Resolves all default {@link cli.Parser Parsers}
	 * in the given Arguments.<br/>
	 * Takes Account for all default {@link cli.Flag Flags} given.<br/>
	 * Returns the resulting Arguments.
	 * @param args the Arguments
	 * @return the resulting Arguments
	 * @throws IllegalArgumentException if the Formatting
	 * of the Arguments is invalid
	 */
	public static String[] parseArguments(String...args)
			throws IllegalArgumentException {
		return CommandHandler.parseArguments(
				CommandHandler.parsers,
				CommandHandler.flags,
				args);
	}

	/**
	 * Return the {@link cli.Flag Flag}, that is represented by the given String
	 * or <b>null</b> if there is none.
	 * @param str the String
	 * @param flags the Flags
	 * @return the represented Flag or null
	 */
	public static Flag getFlagByString(
			final String str,
			final List<Flag> flags) {
		for (Flag flag : flags) {
			if (flag.isThis(str)) {
				return flag;
			}
		}
		return null;
	}

	/**
	 * Return the default {@link cli.Flag Flag},
	 * that is represented by the given String
	 * or <b>null</b> if there is none.
	 * @param str the String
	 * @return the represented Flag or null
	 */
	public static Flag getFlagByString(final String str) {
		return CommandHandler.getFlagByString(str, CommandHandler.flags);
	}

	/**
	 * Return the {@link cli.Parser Parser},
	 * that is represented by the given String
	 * or <b>null</b> if there is none.
	 * @param str the String
	 * @param parsers the Parsers
	 * @return the represented Parser or null
	 */
	public static Parser getParserByString(
			final String str,
			final List<Parser> parsers) {
		for (Parser par : parsers) {
			if (par.isThis(str)) {
				return par;
			}
		}
		return null;
	}

	/**
	 * Return the default {@link cli.Parser Parser},
	 * that is represented by the given String
	 * or <b>null</b> if there is none.
	 * @param str the String
	 * @return the represented Parser or null
	 */
	public static Parser getParserByString(final String str) {
		return CommandHandler.getParserByString(str, CommandHandler.parsers);
	}
}

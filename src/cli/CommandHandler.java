package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class CommandHandler {
	private static List<Flag> flags = new ArrayList<>();
	private static List<Parser> parser = new ArrayList<>();
	private static String flagIndicator = "--";
	private static String keyValueSeparator = "-";

	public static void setFlags(List<Flag> flags) {
		if (flags == null) {
			throw new IllegalArgumentException("Invalid flags");
		}
		CommandHandler.flags = flags;
	}

	public static void setFlags(Flag...flags) {
		CommandHandler.flags = Arrays.asList(flags);
	}

	public static List<Flag> getFlags() {
		return CommandHandler.flags;
	}

	public static void setParser(List<Parser> parser) {
		if (parser == null) {
			throw new IllegalArgumentException("Invalid parser");
		}
		CommandHandler.parser = parser;
	}

	public static void setParser(Parser...parser) {
		CommandHandler.parser = Arrays.asList(parser);
	}

	public static List<Parser> getParser() {
		return CommandHandler.parser;
	}

	public static void setFlagIndicator(String flagIndicator) {
		if (flagIndicator == null
				|| flagIndicator.contains(" ")
				|| flagIndicator.equals("")) {
			throw new IllegalArgumentException(
					"Invalid flagIndicator\"" + flagIndicator + "\"");
		}
		CommandHandler.flagIndicator = flagIndicator;
	}

	public static String getFlagIndicator() {
		return CommandHandler.flagIndicator;
	}

	public static void setKeyValueSeparator(String keyValueSeparator) {
		if (keyValueSeparator == null
				|| keyValueSeparator.equals("")
				|| (keyValueSeparator.contains(" ")
					&& keyValueSeparator.length() > 1)) {
			throw new IllegalArgumentException(
					"Invalid keyValueSeparator \"" + keyValueSeparator + "\"");
		}
		CommandHandler.keyValueSeparator = keyValueSeparator;
	}

	public static String getKeyValueSeparator() {
		return CommandHandler.keyValueSeparator;
	}

	public static String[] handleArguments(String...args) {
		final ArrayList<String> ignoredArgs = new ArrayList<>();
		final ArrayList<Flag> commands = new ArrayList<>();
		commands.addAll(CommandHandler.flags);
		for (int i = 0;i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(CommandHandler.flagIndicator.length());
				boolean exists = false;
				for (final Flag command : commands) {
					if (command.getName().equals(
							CommandHandler.keyValueSeparator.equals(" ")
								? arg
								: arg.substring(
									0, arg.indexOf(
										CommandHandler.keyValueSeparator)))) {
						exists = true;
						String[] values = new String[command.getValues()];
						for (int j = 0; j < command.getValues(); j++) {
							if (CommandHandler.keyValueSeparator.equals(" ")) {
								if (args.length > ++i) {
									values[j] = args[i];
									break;
								} else {
									throw new IllegalArgumentException(
											"Missing parameter for " + arg);
								}
							} else {
								arg = arg.substring(arg.indexOf(
									CommandHandler.keyValueSeparator) + 1);
								values[j] = arg;
							}
						}
						if (!command.allowMultiple()) {
							commands.remove(command);
						}
						command.getAction().accept(values);
						break;
					}
				}
				if (!exists) {
					throw new IllegalArgumentException(
							"Invalid command \"" + arg + "\"");
				}
			} else {
				ignoredArgs.add(arg);
			}
		}
		return ignoredArgs.toArray(new String[ignoredArgs.size()]);
	}

	public static List<String> handleArguments(List<String> args) {
		final ArrayList<String> ignoredArgs = new ArrayList<>();
		final ArrayList<Flag> commands = new ArrayList<>();
		commands.addAll(CommandHandler.flags);
		for (int i = 0;i < args.size(); i++) {
			String arg = args.get(i);
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(CommandHandler.flagIndicator.length());
				boolean exists = false;
				for (final Flag command : commands) {
					if (command.getName().equals(
							CommandHandler.keyValueSeparator.equals(" ")
								? arg
								: arg.substring(
									0, arg.indexOf(
										CommandHandler.keyValueSeparator)))) {
						exists = true;
						String[] values = new String[command.getValues()];
						for (int j = 0; j < command.getValues() - 1; j++) {
							if (CommandHandler.keyValueSeparator.equals(" ")) {
								if (args.size() > ++i) {
									values[j] = args.get(i);
									break;
								} else {
									throw new IllegalArgumentException(
											"Missing parameter for " + arg);
								}
							} else {
								values[j] = arg = arg.substring(arg.indexOf(
											CommandHandler.keyValueSeparator) + 1);
							}
						}
						if (!command.allowMultiple()) {
							commands.remove(command);
						}
						command.getAction().accept(values);
						break;
					}
				}
				if (!exists) {
					throw new IllegalArgumentException(
							"Invalid command \"" + arg + "\"");
				}
			} else {
				ignoredArgs.add(arg);
			}
		}
		return ignoredArgs;
	}

	public static String[] handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final Flag[] flags,
			final String...args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setFlags(flags);
			return CommandHandler.handleArguments(args);
		});
	}

	public static String[] handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final List<Flag> flags,
			final String...args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setFlags(flags);
			return CommandHandler.handleArguments(args);
		});
	}

	public static List<String> handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final List<Flag> flags,
			final List<String> args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setFlags(flags);
			return CommandHandler.handleArguments(args);
		});
	}

	public static List<String> handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final Flag[] flags,
			final List<String> args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setFlags(flags);
			return CommandHandler.handleArguments(args);
		});
	}

	public static String[] parseArgs(final String...args) {
		String[] newArgs = Arrays.copyOf(args, args.length);
		for (int i = newArgs.length - 1; i >= 0; i--) {
			for (Parser parser : CommandHandler.parser) {
				if (parser.getName().equals(newArgs[i])) {
					final ArrayList<String> toParse = new ArrayList<>();
					int parsValues = 0;
					for (int j = i + 1;j < newArgs.length;j++) {
						if (newArgs[j].equals(parser.getSuffix())
								|| (parser.getSuffix() == null
									&& parsValues == parser.getValues())) {
							break;
						}
						toParse.add(newArgs[j]);
						boolean exists = false;
						for (Flag flag : CommandHandler.flags) {
							if (newArgs[j].startsWith(
									CommandHandler.flagIndicator + flag.getName())) {
								exists = true;
								if (CommandHandler.keyValueSeparator.equals(" ")) {
									int values = flag.getValues();
									if (j + values >= newArgs.length) {
										throw new IllegalArgumentException(
												"Invalid formatting");
									}
									while(--values >= 0) {
										toParse.add(newArgs[++j]);
									}
								}
								break;
							}
						}
						if (!exists) {
							parsValues++;
						}
					}
					newArgs[i] = parser.getAction().apply(
							toParse.toArray(new String[toParse.size()]));
					final int offset = toParse.size()
							+ (parser.getSuffix() == null ? 0 : 1);
					for (int j = i + offset;j < newArgs.length; j++) {
						newArgs[j - offset] = newArgs[j];
					}
					newArgs = Arrays.copyOf(newArgs, newArgs.length - offset);
					break;
				}
			}
		}
		return newArgs;
	}

	private static <T> T cacheValues(Supplier<T> action) {
		final String prevFlagIndicator = CommandHandler.flagIndicator;
		final String prevKeyValueSeparator = CommandHandler.keyValueSeparator;
		final List<Flag> prevCommands = CommandHandler.flags;
		final List<Parser> prevParser = CommandHandler.parser;
		final T returnObject = action.get();
		CommandHandler.setFlagIndicator(prevFlagIndicator);
		CommandHandler.setKeyValueSeparator(prevKeyValueSeparator);
		CommandHandler.setFlags(prevCommands);
		CommandHandler.setParser(prevParser);
		return returnObject;
	}
}

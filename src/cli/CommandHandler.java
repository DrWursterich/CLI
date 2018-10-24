package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class CommandHandler {
	private static final int NO_PREV_VALUE = -1;
	private static List<Command> commands = new ArrayList<>();
	private static List<Parser> parser = new ArrayList<>();
	private static String flagIndicator = "--";
	private static String parsIndicator = "-";
	private static String keyValueSeparator = "-";

	public static void setCommands(List<Command> commands) {
		if (commands == null) {
			throw new IllegalArgumentException("Invalid commands");
		}
		CommandHandler.commands = commands;
	}

	public static void setCommands(Command...commands) {
		CommandHandler.commands = Arrays.asList(commands);
	}

	public static List<Command> getCommands() {
		return CommandHandler.commands;
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

	public static void setParseIndicator(String parsIndicator) {
		if (parsIndicator == null
				|| parsIndicator.contains(" ")
				|| parsIndicator.equals("")) {
			throw new IllegalArgumentException(
					"Invalid parsIndicator\"" + parsIndicator + "\"");
		}
		CommandHandler.parsIndicator = parsIndicator;
	}

	public static String getParsIndicator() {
		return CommandHandler.parsIndicator;
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
		final ArrayList<Command> commands = new ArrayList<>();
		commands.addAll(CommandHandler.commands);
		for (int i = 0;i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(CommandHandler.flagIndicator.length());
				boolean exists = false;
				for (final Command command : commands) {
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
		final ArrayList<Command> commands = new ArrayList<>();
		commands.addAll(CommandHandler.commands);
		for (int i = 0;i < args.size(); i++) {
			String arg = args.get(i);
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(CommandHandler.flagIndicator.length());
				boolean exists = false;
				for (final Command command : commands) {
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
			final Command[] commands,
			final String...args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setCommands(commands);
			return CommandHandler.handleArguments(args);
		});
	}

	public static String[] handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final List<Command> commands,
			final String...args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setCommands(commands);
			return CommandHandler.handleArguments(args);
		});
	}

	public static List<String> handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final List<Command> commands,
			final List<String> args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setCommands(commands);
			return CommandHandler.handleArguments(args);
		});
	}

	public static List<String> handleArguments(
			final String flagIndicator,
			final String keyValueSeparator,
			final Command[] commands,
			final List<String> args) {
		return CommandHandler.cacheValues(() -> {
			CommandHandler.setFlagIndicator(flagIndicator);
			CommandHandler.setKeyValueSeparator(keyValueSeparator);
			CommandHandler.setCommands(commands);
			return CommandHandler.handleArguments(args);
		});
	}

	public static String[] parseArgs(final String...args) {
		int prevValue = NO_PREV_VALUE;
		String[] newArgs = Arrays.copyOf(args, args.length);
		for (int i = args.length - 1;i >= 0;i--) {
			if (args[i].startsWith(CommandHandler.parsIndicator)) {
				if (prevValue == NO_PREV_VALUE
						&& !args[i].startsWith(CommandHandler.flagIndicator)) {
					throw new IllegalArgumentException("Invalid parser formatting");
				}
				final String name = args[i].substring(
						CommandHandler.parsIndicator.length());
				boolean exists = false;
				for (Parser parser : CommandHandler.parser) {
					if (parser.getName().equals(name)) {
						exists = true;
						final String[] tmp = new String[prevValue - i];
						for (int j = i - 1;j >= 0;j--) {
							tmp[j] = newArgs[j];
						}
						tmp[i] = parser.getAction().apply(
								Arrays.copyOfRange(args, i, prevValue));
						for (int j = i + 1;j < tmp.length;j++) {
							tmp[j] = newArgs[j + prevValue - i - 1];
						}
						newArgs = tmp;
						prevValue = NO_PREV_VALUE;
						break;
					}
				}
				if (!exists && !CommandHandler.flagIndicator.startsWith(CommandHandler.parsIndicator)) {
					throw new IllegalArgumentException("Invalid parser \"" + name + "\"");
				}
			} else if (!args[i].startsWith(CommandHandler.flagIndicator)
					&& prevValue == NO_PREV_VALUE) {
				prevValue = i;
			}
		}
		Arrays.stream(newArgs).forEach(e -> System.out.println("newArgs: " + e));
		return newArgs;
	}

	private static <T> T cacheValues(Supplier<T> action) {
		final String prevFlagIndicator = CommandHandler.flagIndicator;
		final String prevKeyValueSeparator = CommandHandler.keyValueSeparator;
		final List<Command> prevCommands = CommandHandler.commands;
		final T returnObject = action.get();
		CommandHandler.setFlagIndicator(prevFlagIndicator);
		CommandHandler.setKeyValueSeparator(prevKeyValueSeparator);
		CommandHandler.setCommands(prevCommands);
		return returnObject;
	}
}

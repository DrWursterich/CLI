package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class CommandHandler {
	private static List<Command> commands = new ArrayList<>();
	private static String flagIndicator = "-";
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
						String value = "";
						if (command.hasValue()) {
							if (CommandHandler.keyValueSeparator.equals(" ")) {
								if (args.length > ++i) {
									value = args[i];
									break;
								} else {
									throw new IllegalArgumentException(
											"Missing parameter for " + arg);
								}
							} else {
								value = arg.substring(arg.indexOf(
											CommandHandler.keyValueSeparator) + 1);
							}
						}
						if (!command.allowMultiple()) {
							commands.remove(command);
						}
						command.getAction().accept(value);
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
						String value = "";
						if (command.hasValue()) {
							if (CommandHandler.keyValueSeparator.equals(" ")) {
								if (args.size() > ++i) {
									value = args.get(i);
									break;
								} else {
									throw new IllegalArgumentException(
											"Missing parameter for " + arg);
								}
							} else {
								value = arg.substring(arg.indexOf(
											CommandHandler.keyValueSeparator) + 1);
							}
						}
						if (!command.allowMultiple()) {
							commands.remove(command);
						}
						command.getAction().accept(value);
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

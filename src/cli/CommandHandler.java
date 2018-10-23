package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		for (int i = 0;i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(1);
				boolean exists = false;
				for (final Command command : CommandHandler.commands) {
					if (command.getName().equals(
							CommandHandler.keyValueSeparator.equals(" ")
								? arg
								: arg.substring(
									0, arg.indexOf(
										CommandHandler.keyValueSeparator)))) {
						exists = true;
						if (CommandHandler.keyValueSeparator.equals(" ")) {
							if (args.length > ++i) {
								command.getAction().accept(args[i]);
								break;
							} else {
								throw new IllegalArgumentException(
										"Missing parameter for " + arg);
							}
						} else {
							command.getAction().accept(
									arg.substring(arg.indexOf(
										CommandHandler.keyValueSeparator) + 1));
						}
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
		return args;
	}

	public static List<String> handleArguments(List<String> args) {
		final ArrayList<String> ignoredArgs = new ArrayList<>();
		for (int i = 0;i < args.size(); i++) {
			String arg = args.get(i);
			if (arg.startsWith(CommandHandler.flagIndicator)) {
				arg = arg.substring(1);
				boolean exists = false;
				for (final Command command : CommandHandler.commands) {
					if (command.getName().equals(
							CommandHandler.keyValueSeparator.equals(" ")
								? arg
								: arg.substring(
									0, arg.indexOf(
										CommandHandler.keyValueSeparator)))) {
						exists = true;
						if (CommandHandler.keyValueSeparator.equals(" ")) {
							if (args.size() > ++i) {
								command.getAction().accept(args.get(i));
								break;
							} else {
								throw new IllegalArgumentException(
										"Missing parameter for " + arg);
							}
						} else {
							command.getAction().accept(
									arg.substring(arg.indexOf(
										CommandHandler.keyValueSeparator) + 1));
						}
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
		return args;
	}
}

package io.github.awesome90.crescent.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.awesome90.crescent.Crescent;
import io.github.awesome90.crescent.info.Profile;
import io.github.awesome90.crescent.learn.KnownCheating;

public class CommandCrescent implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("crescent")) {

			if (!sender.hasPermission("crescent.commands")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return true;
			}

			if (args.length == 0) {
				String[] message = { ChatColor.BLUE + "Crescent",
						ChatColor.WHITE + "/crescent learn - Enable learning mode.", ChatColor.WHITE
								+ "/crescent doescheat [player] [yes/no/undefined] - Mark a player as a cheater or as a legitimate user so we can use their information to add to the database. If you want to reset it, use the undefined option." };
				sender.sendMessage(message);
				return true;
			}

			final String subCommand = args[0];

			if (subCommand.equalsIgnoreCase("learn")) {

				final boolean current = Crescent.getInstance().getConfig().getBoolean("learnMode");

				Crescent.getInstance().getConfig().set("learnMode", !current);

				final ChatColor colour = current ? ChatColor.RED : ChatColor.GREEN;

				sender.sendMessage(colour + "You have " + ((current) ? "disabled" : "enabled") + " learning mode.");
				return true;
			} else if (subCommand.equalsIgnoreCase("doescheat")) {

				if (args.length < 3) {
					sender.sendMessage(ChatColor.RED + "You must include the player and whether they cheat or not.");
					return true;
				}

				final String player = args[1];

				final Player target = Bukkit.getPlayer(player);

				if (target == null) {
					sender.sendMessage(ChatColor.RED + "That player is not online.");
					return true;
				}

				final String doesCheat = args[2];
				final KnownCheating cheating;

				switch (doesCheat.toLowerCase()) {
				case "yes":
					cheating = KnownCheating.YES;
					break;
				case "no":
					cheating = KnownCheating.NO;
					break;
				case "undefined":
					cheating = KnownCheating.UNDEFINED;
					break;
				default:
					sender.sendMessage(ChatColor.RED + "That is not a valid option.");
					return true;
				}

				Profile.getProfile(target.getUniqueId()).setKnownCheating(cheating);

				sender.sendMessage(ChatColor.GREEN + "You have let the system know that " + target.getName()
						+ "'s cheating status is: " + cheating.toString() + ".");

				return true;
			}

			return true;
		}

		return true;
	}

}

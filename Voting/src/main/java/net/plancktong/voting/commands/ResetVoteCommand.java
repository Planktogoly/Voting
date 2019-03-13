package net.plancktong.voting.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.plancktong.voting.Voting;

public class ResetVoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
	
		if (args.length == 1) {
			String type = args[0];
			
			if (type.equalsIgnoreCase("all")) {
				Voting.getInstance().getVoteDatabase().resetAll();
				player.sendMessage(ChatColor.RED + "You have reset all the votes of all your players!");
				return false;
			}
			
			Player targetPlayer = Bukkit.getPlayer(type);
			if (targetPlayer == null) {
				player.sendMessage(ChatColor.RED + "Player is not online!");
				return false;
			}			
			
			Voting.getInstance().getVoteDatabase().reset(targetPlayer.getUniqueId());
			player.sendMessage(ChatColor.RED + "You have reset all the votes of " + type);
			return false;
		}

		
		player.sendMessage(ChatColor.RED + "Usage: /resetvote name/all");
		return false;
	}

}

package net.plancktong.voting.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.plancktong.voting.Voting;

public class VotesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			Voting.getInstance().getVoteSettings().reload();
			Voting.getInstance().getRewardsManager().loadRewards();			
			
			player.sendMessage(ChatColor.GRAY + "You have reloaded the config!");
			return false;
		}		
		return false;
	}

}

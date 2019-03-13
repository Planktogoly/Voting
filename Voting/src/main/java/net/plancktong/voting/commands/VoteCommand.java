package net.plancktong.voting.commands;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.plancktong.voting.Voting;

public class VoteCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		
		if (args.length >= 1 && args[0].equalsIgnoreCase("test")) {
			if (args.length == 3) {
				String playerName = args[1];
				String serviceName = args[2];
				
				Bukkit.getPluginManager().callEvent(new VotifierEvent(new Vote(serviceName, playerName, "test", new Date().toString())));
				player.sendMessage(ChatColor.GREEN + "Send a test vote out on " + playerName);
				return false;
			}
			
			player.sendMessage(ChatColor.RED + "Usage: /vote test <name> <site>");			
			return false;
		}
				
		Voting.getInstance().getVoteSettings().getVoteMessages()
			.forEach(message -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));		
		return false;
	}

}

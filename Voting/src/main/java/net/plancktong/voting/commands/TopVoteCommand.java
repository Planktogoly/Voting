package net.plancktong.voting.commands;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.plancktong.voting.Voting;

public class TopVoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;		
		
		Player player = (Player) sender;
		
		HashMap<String, Integer> topVotes = Voting.getInstance().getTopVoteManager().getTopVotes();		
		
		int index = 1;
		for (Entry<String, Integer> person : topVotes.entrySet()) {
			player.sendMessage("#" + index + " - " + person.getKey() + " Votes: " + person.getValue());
			index++;
		}
		
		return false;
	}

}

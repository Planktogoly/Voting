package net.plancktong.voting.rewards;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.plancktong.voting.Voting;
import net.plancktong.voting.config.ConfigFile;
import net.plancktong.voting.model.Reward;
import net.plancktong.voting.model.Vote;

public class RewardsManager {
		
	private ArrayList<Reward> rewards;
	
	private Reward defaultReward;
	@Getter private Reward allReward;
	
	public RewardsManager() {
		this.rewards = new ArrayList<>();		
	}	
	
	public void loadRewards() {
		rewards.clear();
		
		ConfigFile config = Voting.getInstance().getVoteSettings().getConfig();
		ConfigurationSection rewardsSection = config.getBukkitFile().getConfigurationSection("rewards");
		
		
		for (String configKey : rewardsSection.getKeys(false)) {
			ConfigurationSection rewardSection = rewardsSection.getConfigurationSection(configKey);
			
			if (!rewardSection.getBoolean("active")) continue;			
			
			Reward reward = new Reward(configKey, rewardSection.getString("permission"), rewardSection.getStringList("actions"));	
			
			if (configKey.equalsIgnoreCase("default")) {
				this.defaultReward = reward;
				continue;			
			} else if (configKey.equalsIgnoreCase("all")) {
				this.allReward = reward;
				continue;
			}
			
			rewards.add(reward);
		}		
	}
	
	public boolean checkIfPlayerVotedOnAllSites(Player player) {
		List<Vote> votes = Voting.getInstance().getVoteDatabase().getVotesOfToday(player.getUniqueId());
		List<Reward> checkList = new ArrayList<>(rewards);
		
		for (Vote vote : votes) {
			for (Reward reward : rewards) {
				if (vote.getVotingSite().equalsIgnoreCase(reward.getType())) checkList.remove(reward);
			}
		}
		
		return checkList.size() == 0;
	}
	
	public Reward getReward(Player player, String serviceName) {
		Reward reward = defaultReward;
		
		for(Reward rewardlist : rewards) {
			if (!rewardlist.getType().equalsIgnoreCase(serviceName)) continue;
			if (!player.hasPermission(rewardlist.getPermission())) continue;
			
			reward = rewardlist;
		}
		
		return reward;
	}

	public void runActions(Player player, Reward reward) {
		reward.getActions().forEach(action -> runAction(player, action));		
	}
	
	private void runAction(Player player, String action) {
		String actionType = StringUtils.substringBetween(action, "[", "]");
		String actionMessage = action.substring(action.lastIndexOf("]") + 1);
		
		actionMessage = actionMessage.substring(1, actionMessage.length());
		actionMessage = actionMessage.replace("%player%", player.getName());
		
		switch (actionType) {
		case "message":
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', actionMessage));
			break;
		case "consolecommand":
			Voting.getInstance().getServer().dispatchCommand(Voting.getInstance().getServer().getConsoleSender(), actionMessage);
			break;
		case "title":
			player.sendTitle(ChatColor.translateAlternateColorCodes('&', actionMessage), null, 20, 60, 20);
			break;
		case "subtitle":
			player.sendTitle(null, ChatColor.translateAlternateColorCodes('&', actionMessage), 20, 60, 20);	
			break;
		case "actionbarmessage":
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', actionMessage)));
			break;
		case "broadcast":
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', actionMessage));
			break;
		default:
			break;
		}
	}
}

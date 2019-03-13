package net.plancktong.voting.listeners;

import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import net.plancktong.voting.Voting;
import net.plancktong.voting.model.Reward;
import net.plancktong.voting.rewards.RewardsManager;

public class VoteListener implements Listener {

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		
		Player player = Bukkit.getPlayer(vote.getUsername());
		
		Voting.getInstance().getVoteDatabase().save(new net.plancktong.voting.model.Vote(
				player.getUniqueId(), 
				vote.getServiceName(), 
				new Timestamp(System.currentTimeMillis())));
		
		RewardsManager rewardsManager = Voting.getInstance().getRewardsManager();		
		Reward reward = rewardsManager.getReward(player, vote.getServiceName());
		
		if (reward == null) return;
		
		rewardsManager.runActions(player, reward);
		
		if (rewardsManager.checkIfPlayerVotedOnAllSites(player)) {
			rewardsManager.runActions(player, Voting.getInstance().getRewardsManager().getAllReward());
		}
	}
}

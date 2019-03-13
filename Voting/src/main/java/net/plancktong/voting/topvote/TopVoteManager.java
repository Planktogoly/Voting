package net.plancktong.voting.topvote;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.plancktong.voting.Voting;

@Getter
public class TopVoteManager {
	
	private HashMap<String, Integer> topvotes;
	
	private long lastUpdated;
	
	public TopVoteManager() {
		this.topvotes = new HashMap<>();
		
	}
	
	public HashMap<String, Integer> getTopVotes() {
		this.updateTopVotes();
		
		return topvotes;
	}
	
	private void updateTopVotes() {
		if ((System.currentTimeMillis() - lastUpdated) > TimeUnit.MINUTES.toMillis(Voting.getInstance().getVoteSettings().getUpdateTopVotesAfterMinutes())) {
			lastUpdated = System.currentTimeMillis();
			
			topvotes = Voting.getInstance().getVoteDatabase().topVotes();
		}
	}

}

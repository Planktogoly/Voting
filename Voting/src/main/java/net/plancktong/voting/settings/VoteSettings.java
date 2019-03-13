package net.plancktong.voting.settings;

import java.util.List;

import lombok.Getter;
import net.plancktong.voting.Voting;
import net.plancktong.voting.config.ConfigFile;

public class VoteSettings {
	
	@Getter private List<String> voteMessages;
	
	@Getter private int updateTopVotesAfterMinutes;
	
	@Getter private ConfigFile config;
	
	public VoteSettings() {
		this.config = new ConfigFile(Voting.getInstance(), "settings.yml");
	}
	
	public void load() {
		this.voteMessages = this.config.getBukkitFile().getStringList("messages.vote-command");		
		this.updateTopVotesAfterMinutes = this.config.getBukkitFile().getInt("update-topvotes-minutes");				
	}

	public void reload() {
		this.config = new ConfigFile(Voting.getInstance(), "settings.yml");		
		this.load();
	}

}

package net.plancktong.voting;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import net.plancktong.voting.commands.ResetVoteCommand;
import net.plancktong.voting.commands.TopVoteCommand;
import net.plancktong.voting.commands.VoteCommand;
import net.plancktong.voting.commands.VotesCommand;
import net.plancktong.voting.database.Database;
import net.plancktong.voting.database.SQLDatabase;
import net.plancktong.voting.listeners.VoteListener;
import net.plancktong.voting.model.Vote;
import net.plancktong.voting.rewards.RewardsManager;
import net.plancktong.voting.settings.VoteSettings;
import net.plancktong.voting.topvote.TopVoteManager;

public class Voting extends JavaPlugin {

	@Getter private static Voting instance;
	
	@Getter private Database<Vote> voteDatabase;		
	@Getter private VoteSettings voteSettings;
	
	@Getter private RewardsManager rewardsManager;
	@Getter private TopVoteManager topVoteManager;
	
	@Override
	public void onEnable() {
		instance = this;
		
		this.voteSettings = new VoteSettings();
		this.voteSettings.load();
		
		this.voteDatabase = new SQLDatabase();
		
		this.voteDatabase.setup();
		this.voteDatabase.createTables();
		
		this.rewardsManager = new RewardsManager();
		this.rewardsManager.loadRewards();
		
		this.topVoteManager = new TopVoteManager();
		
		initializePlugin();
	}
	
	@Override
	public void onDisable() {
		instance = null;
	}
	
	private void initializePlugin() {
		Bukkit.getPluginManager().registerEvents(new VoteListener(), this);
		
		this.getCommand("vote").setExecutor(new VoteCommand());
		this.getCommand("resetvote").setExecutor(new ResetVoteCommand());
		this.getCommand("topvote").setExecutor(new TopVoteCommand());
		this.getCommand("votes").setExecutor(new VotesCommand());
	}
}

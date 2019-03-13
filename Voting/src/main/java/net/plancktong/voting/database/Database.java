package net.plancktong.voting.database;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lombok.NonNull;
import net.plancktong.voting.model.Vote;

public interface Database<T> {
	
	void setup();	
	void createTables();	
	T load(@NonNull UUID uuid);	
	void save(T t);	
	boolean remove(T t);
	void resetAll();
	void reset(@NonNull UUID uuid);
	HashMap<String, Integer> topVotes();
	List<Vote> getVotesOfToday(@NonNull UUID uuid);

}

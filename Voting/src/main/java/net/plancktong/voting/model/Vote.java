package net.plancktong.voting.model;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vote {
	
	private UUID uuid;
	
	private String votingSite;
	
	private Timestamp whenVoted;
	
	
}

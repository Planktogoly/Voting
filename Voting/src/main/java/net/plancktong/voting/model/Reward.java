package net.plancktong.voting.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reward {
	
	private String type;
	
	private String permission;
	
	private List<String> actions;

}

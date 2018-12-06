package com.nba.baller.getyourring.helpers;

import com.nba.baller.getyourring.models.game.Team;

import java.util.Comparator;

public class TeamsComparator {

	public static Comparator compareForSorting() {
		return Comparator.comparing(Team::getWins).thenComparing(Team::getPlusMinus).reversed();
	}
}

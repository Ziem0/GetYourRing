package com.nba.baller.getyourring.helpers;

import com.nba.baller.getyourring.models.game.Player;
import com.nba.baller.getyourring.models.game.Team;

import java.util.Comparator;

public class HandleComparator {

	public static Comparator compareForSorting() {
		return Comparator.comparing(Team::getWins).thenComparing(Team::getPlusMinus).reversed();
	}

	public static Comparator<Player> comparePlayersByPosition() {
		return Comparator.comparing(p -> p.getPosition().getForOrder());
	}
}

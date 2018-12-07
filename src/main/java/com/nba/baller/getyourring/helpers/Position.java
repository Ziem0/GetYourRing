package com.nba.baller.getyourring.helpers;

public enum Position {

	PG(1), SG(2), SF(3), PF(4), C(5);

	private int forOrder;

	Position(int forOrder) {
		this.forOrder = forOrder;
	}

	public int getForOrder() {
		return forOrder;
	}
}

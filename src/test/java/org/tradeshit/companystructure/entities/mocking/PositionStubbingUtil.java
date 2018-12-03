package org.tradeshit.companystructure.entities.mocking;

import org.tradeshit.companystructure.entities.Position;

public class PositionStubbingUtil {
	
	public static String ROOT_KEY = "1";

	public static Position stubRoot() {
		return new Position(ROOT_KEY);
	}
	
	public static Position stub11(Position root) {
		Position p11 = new Position("11", root, ROOT_KEY, 1);
		root.addChild(p11);
		return p11;
	}
	
	public static Position stub12(Position root) {
		Position p12 = new Position("12", root, ROOT_KEY, 1);
		root.addChild(p12);
		return p12;
	}
	
	public static Position stub111(Position parent11) {
		Position p111 = new Position("111", parent11, ROOT_KEY, 2);
		parent11.addChild(p111);
		return p111;
	}
	
	public static Position stub112(Position parent11) {
		Position p112 = new Position("112", parent11, ROOT_KEY, 2);
		parent11.addChild(p112);
		return p112;
	}
	
	public static Position stub121(Position parent12) {
		Position p121 = new Position("121", parent12, ROOT_KEY, 2);
		parent12.addChild(p121);
		return p121;
	}
	
	public static Position stub122(Position parent12) {
		Position p122 = new Position("122", parent12, ROOT_KEY, 2);
		parent12.addChild(p122);
		return p122;
	}
}

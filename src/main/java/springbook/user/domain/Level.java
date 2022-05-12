package springbook.user.domain;

public enum Level {
	
	// 세 개의 이늄 오브젝트 정의
	GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);  
	
	private final int value;
	private final Level next;
	
	// DB에 저장할 값을 넣어줄 생성자를 만든다.
	Level(int value, Level next){
		this.value = value;
		this.next = next;
	}
	
	public int intValue() {
		return value;
	}
	
	public Level nextLevel() {
		return this.next;
	}
	
	public static Level valueOf(int value) {
		switch(value) {
		case 1 : return BASIC;
		case 2 : return SILVER;
		case 3 : return GOLD;
		default : throw new AssertionError("Unknown value : " + value);
		}
	}
}

package model;

public enum EventProgress {
	NONE(0, "未"), PLANNING(1, "企画中"), ONGOING(2, "開催"), FINISHED(3, "開催終了"), CANCELED(4, "中止");

	private final int code;
	private final String label;

	EventProgress(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	// DBのint → enum変換用
	public static EventProgress fromCode(int code) {
		for (EventProgress p : values()) {
			if (p.code == code) {
				return p;
			}
		}
		throw new IllegalArgumentException("Unknown EventProgress code: " + code);
	}
}

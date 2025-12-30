package action;

import java.time.LocalDateTime;

public abstract class BaseAction {

	protected LocalDateTime parseDateTimeOrNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return LocalDateTime.parse(value);
	}

	protected Integer parseIntOrNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return Integer.parseInt(value);
	}
}
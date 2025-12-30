package validator;

import java.time.LocalDateTime;

import exception.ValidationException;
import model.Event;

public class EventValidator {

	public static void validate(Event event) throws ValidationException {

		// ① 必須チェック
		if (event.getEventPlace() == null || event.getEventPlace().isBlank()) {
			throw new ValidationException("開催場所を入力してください", event);
		}

		if (event.getEventStartTime() == null || event.getEventEndTime() == null) {
			throw new ValidationException("開始日時と終了日時を入力してください", event);
		}

		// ② 時間ロジック
		validateTime(event);

		// ③ 定員
		if (event.getEventCapacity() <= 0) {
			throw new ValidationException("定員は1以上で入力してください", event);
		}
	}

	private static void validateTime(Event event) throws ValidationException {
		LocalDateTime start = event.getEventStartTime();
		LocalDateTime end = event.getEventEndTime();

		if (!end.isAfter(start)) {
			throw new ValidationException("終了日時は開始日時より後にしてください", event);
		}

		if (!start.toLocalDate().equals(end.toLocalDate())) {
			throw new ValidationException("日付を跨ぐイベントは登録できません", event);
		}
	}
}
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

		// ④ 場所（文字数チェック）
		validatePlace(event);
		
		// ⑤ その他情報（文字数チェック）
		validateOtherInfo(event);
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

	private static void validatePlace(Event event) throws ValidationException {
		String eventPlace = event.getEventPlace();
		
		if (eventPlace != null && eventPlace.length() > 30) {
			throw new ValidationException("場所は30文字以内で入力してください", event);
		}
	}
	
	private static void validateOtherInfo(Event event) throws ValidationException {
		String otherInfo = event.getEventOtherInfo();

		if (otherInfo != null && otherInfo.length() > 256) {
			throw new ValidationException("その他は256文字以内で入力してください", event);
		}
	}
}

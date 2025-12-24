package validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import exception.ValidationException;
import model.Answer;

public class AnswerTimeValidator {
	public static void validate(Answer answer) throws ValidationException {
		// ① 不参加ならすべて無視
		if (!answer.getEventAvailability()) {
			return;
		}

		// ② 第1希望は必須
		requireRange(answer, answer.getFirstChoiceStartTime(), answer.getFirstChoiceEndTime(), "第1希望");

		// ③ 第2・第3希望は任意
		validateRange(answer, answer.getSecondChoiceStartTime(), answer.getSecondChoiceEndTime(), "第2希望");

		validateRange(answer, answer.getThirdChoiceStartTime(), answer.getThirdChoiceEndTime(), "第3希望");

		// ④ 重複チェック
		checkOverlap(answer);
	}

	/**
	 * 必須チェック用（第1希望）
	 */
	private static void requireRange(Answer answer, LocalDateTime start, LocalDateTime end, String label) throws ValidationException {
		if (start == null || end == null) {
			throw new ValidationException(label + "は開始時刻と終了時刻を必ず入力してください", answer);
		}

		validateTimeLogic(answer, start, end, label);
	}

	/**
	 * 任意チェック用（第2・第3希望）
	 */
	private static void validateRange(Answer answer, LocalDateTime start, LocalDateTime end, String label) throws ValidationException {
		// 両方 null → OK
		if (start == null && end == null) {
			return;
		}

		// 片方だけ null → NG
		if (start == null || end == null) {
			throw new ValidationException(label + "は開始時刻と終了時刻を両方入力してください", answer);
		}

		validateTimeLogic(answer, start, end, label);
	}

	/**
	 * 時間ロジック共通
	 */
	private static void validateTimeLogic(Answer answer, LocalDateTime start, LocalDateTime end, String label) throws ValidationException {
		if (!end.isAfter(start)) {
			throw new ValidationException(label + "の終了時刻が開始時刻以前です", answer);
		}

		if (!start.toLocalDate().equals(end.toLocalDate())) {
			throw new ValidationException(label + "で日付を跨ぐ指定はできません", answer);
		}
	}

	private static void checkOverlap(Answer answer) throws ValidationException {
		List<TimeRange> ranges = new ArrayList<>();

		addRange(ranges,
				answer.getFirstChoiceStartTime(),
				answer.getFirstChoiceEndTime(),
				"第1希望");

		addRange(ranges,
				answer.getSecondChoiceStartTime(),
				answer.getSecondChoiceEndTime(),
				"第2希望");

		addRange(ranges,
				answer.getThirdChoiceStartTime(),
				answer.getThirdChoiceEndTime(),
				"第3希望");

		for (int i = 0; i < ranges.size(); i++) {
			for (int j = i + 1; j < ranges.size(); j++) {
				if (ranges.get(i).overlaps(ranges.get(j))) {
					throw new ValidationException(ranges.get(i).label + "と" + ranges.get(j).label + "の時間が重複しています", answer);
				}
			}
		}
	}

	private static void addRange(
			List<TimeRange> list,
			LocalDateTime start,
			LocalDateTime end,
			String label) {
		if (start != null && end != null) {
			list.add(new TimeRange(start, end, label));
		}
	}

	private static class TimeRange {
		LocalDateTime start;
		LocalDateTime end;
		String label;

		TimeRange(LocalDateTime start, LocalDateTime end, String label) {
			this.start = start;
			this.end = end;
			this.label = label;
		}

		boolean overlaps(TimeRange other) {
			return this.start.isBefore(other.end)
					&& other.start.isBefore(this.end);
		}
	}
}

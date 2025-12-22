package validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Answer;

public class AnswerTimeValidator {
	public static void validate(Answer answer) throws Exception {
		validateRange(
				answer.getFirstChoiceStartTime(),
				answer.getFirstChoiceEndTime(),
				"第1希望");

		validateRange(
				answer.getSecondChoiceStartTime(),
				answer.getSecondChoiceEndTime(),
				"第2希望");

		validateRange(
				answer.getThirdChoiceStartTime(),
				answer.getThirdChoiceEndTime(),
				"第3希望");

		checkOverlap(answer);
	}

	private static void validateRange(LocalDateTime start, LocalDateTime end, String label) throws Exception {
		// 両方 null → 未入力としてOK
		if (start == null && end == null) {
			return;
		}

		// 片方だけ null → NG
		if (start == null || end == null) {
			throw new Exception(
					label + "は開始時刻と終了時刻を両方入力してください");
		}

		// 終了が開始以前
		if (!end.isAfter(start)) {
			throw new Exception(
					label + "の終了時刻が開始時刻以前です");
		}

		// 日付跨ぎ禁止
		if (!start.toLocalDate().equals(end.toLocalDate())) {
			throw new Exception(
					label + "で日付を跨ぐ指定はできません");
		}
	}

	private static void checkOverlap(Answer answer) throws Exception {
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

		// 重複チェック
		for (int i = 0; i < ranges.size(); i++) {
			for (int j = i + 1; j < ranges.size(); j++) {
				TimeRange a = ranges.get(i);
				TimeRange b = ranges.get(j);

				if (a.overlaps(b)) {
					throw new Exception(
							a.label + "と" + b.label + "の時間が重複しています");
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

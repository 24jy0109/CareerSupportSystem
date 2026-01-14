package model;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.EventDTO;

public class MailBuilder {

	private static final DateTimeFormatter EVENT_FMT = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");

	private static final String EVENT_NOTICE_TEMPLATE =
"""
%s のイベントが開催されます。

■ 開催日時
  %s ～ %s

■ 開催場所
  %s

■ 定員
  %d 名

■ 担当スタッフ
  %s

  %s

ご不明な点がございましたら、上記担当職員までお問い合わせください。
""";

    private static final String EVENT_CANCEL_TEMPLATE =
"""
関係者各位

以下のイベントにつきまして、誠に勝手ながら開催を中止（キャンセル）とさせていただきます。

【イベント情報】
企業名：%s
開催日時：%s ～ %s
担当職員：%s（%s）

【参加予定だった卒業生】
%s

ご不明な点がございましたら、上記担当職員までお問い合わせください。

何卒ご理解のほど、よろしくお願いいたします。
""";
    
    private static final String EVENT_JOIN_TEMPLATE =
"""
%s のイベントに参加登録が完了しました。

■ 開催日時
  %s ～ %s

■ 開催場所
  %s

■ 定員
  %d 名

%s■ 担当スタッフ
  %s

  %s

ご不明な点がございましたら、上記担当職員までお問い合わせください。
""";
    
    private static final String ANSWER_TEMPLATE =
"""
%s 様より、以下の開催について回答がありました。

■ 参加可否
  %s

%s■ 回答者情報
  氏名: %s
  メール: %s

詳細は管理画面よりご確認ください。
""";
    
    private static final String ANSWER_SELF_TEMPLATE =
"""
%s 様

以下の内容で、イベント参加についてのご回答を受け付けました。

■ 参加可否
  %s

%s■ 担当者情報
  氏名: %s
  メール: %s


ご不明な点がございましたら、担当職員までお問い合わせください。
""";

    private static final String NO_ANSWER_TEMPLATE =
"""
%s 様

お世話になっております。
キャリアサポート担当です。

この度は、イベントへのご関心および
参加可否のご回答をいただき、誠にありがとうございました。

慎重に検討を行いました結果、
今回はやむを得ずご参加を見送らせていただくこととなりました。

ご期待に添えず誠に恐縮ではございますが、
何卒ご理解いただけますと幸いです。

今後のイベントにつきましては、
改めてご案内させていただく場合がございます。

引き続き、よろしくお願いいたします。

――――――――――――
キャリアサポート担当
%s
%s
""";

	/**
	 * イベント開催通知メール本文
	 */
	public static String buildEventNotice(Event event) {
		String body = String.format(
				EVENT_NOTICE_TEMPLATE,
				event.getCompany().getCompanyName(),
				event.getEventStartTime().format(EVENT_FMT),
				event.getEventEndTime().format(EVENT_FMT),
				event.getEventPlace(),
				event.getEventCapacity(),
				event.getStaff().getStaffName(),
				event.getStaff().getStaffEmail());

		// 備考はあるときだけ差し込む
		if (event.getEventOtherInfo() != null && !event.getEventOtherInfo().isBlank()) {
			body = body.replace(
					"\n■ 担当スタッフ\n",
					"\n■ 備考\n  " + event.getEventOtherInfo() + "\n\n■ 担当スタッフ\n");
		}

		return body;
	}

	/**
	 * 卒業生向けスケジュール調整メール本文生成
	 */
	public static String buildScheduleArrangeEmail(Graduate graduate, Answer answer, String baseBody) {
		String answerUrl = "http://localhost:8080/CareerSupportSystem/answer?command=AnswerForm&answerId="
				+ answer.getAnswerId();

		StringBuilder sb = new StringBuilder();
		sb.append(baseBody)
				.append("\n\n回答URL: ").append(answerUrl)
				.append("\n\n担当者名: ").append(graduate.getStaff().getStaffName())
				.append("\n担当者メールアドレス: ").append(graduate.getStaff().getStaffEmail());

		return sb.toString();
	}
	

    /**
     * イベント中止通知メール本文生成（printf 形式）
     */
    public static String buildEventCancelEmail(EventDTO eventDTO) {
		Event event = eventDTO.getEvent();
		Staff staff = eventDTO.getStaffs().getFirst();
		List<Graduate> graduates = eventDTO.getGraduates();

        // 参加予定卒業生リストを文字列化
        String gradList = "";
        StringBuilder sb = new StringBuilder("");
        if (graduates != null && !graduates.isEmpty()) {
            for (Graduate gra : graduates) {
                sb.append("・").append(gra.getGraduateName())
                  .append("（").append(gra.getGraduateJobCategory()).append("）\n");
            }
            gradList = sb.toString();
        } else {
        	gradList = "なし";
        }

        // String.format で置換
        return String.format(
            EVENT_CANCEL_TEMPLATE,
            event.getCompany().getCompanyName(),
            event.getEventStartTime().toString().replace("T", " "),
            event.getEventEndTime().toString().replace("T", " "),
            staff.getStaffName(),
            staff.getStaffEmail(),
            gradList
        );
    }
    
    /**
     * 参加登録完了通知メール本文
     */
    public static String buildEventJoinEmail(Event event) {
        // 備考はある場合だけ
        String notes = "";
        if (event.getEventOtherInfo() != null && !event.getEventOtherInfo().isBlank()) {
            notes = "■ 備考\n  " + event.getEventOtherInfo() + "\n\n";
        }

        return String.format(
            EVENT_JOIN_TEMPLATE,
            event.getCompany().getCompanyName(),
            event.getEventStartTime().format(EVENT_FMT),
            event.getEventEndTime().format(EVENT_FMT),
            event.getEventPlace(),
            event.getEventCapacity(),
            notes,
            event.getStaff().getStaffName(),
            event.getStaff().getStaffEmail()
        );
    }
    

    /**
     * 回答通知メール本文生成
     */
    public static String buildAnswerNotification(Answer answer) {
        String availability = Boolean.TRUE.equals(answer.getEventAvailability()) ? "出席する" : "出席しない";

        // 希望日時（出席する場合のみ）
        StringBuilder choices = new StringBuilder();
        if (Boolean.TRUE.equals(answer.getEventAvailability())) {

            if (answer.getFirstChoiceStartTime() != null) {
                choices.append("■ 希望日時\n");
                choices.append("  【第1希望】 ")
                       .append(answer.getFirstChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getFirstChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            if (answer.getSecondChoiceStartTime() != null) {
                if (choices.length() == 0) choices.append("■ 希望日時\n");
                choices.append("  【第2希望】 ")
                       .append(answer.getSecondChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getSecondChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            if (answer.getThirdChoiceStartTime() != null) {
                if (choices.length() == 0) choices.append("■ 希望日時\n");
                choices.append("  【第3希望】 ")
                       .append(answer.getThirdChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getThirdChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            choices.append("\n"); // 本文区切り
        }

        Graduate grad = answer.getGraduate();

        return String.format(
            ANSWER_TEMPLATE,
            grad.getGraduateName(),
            availability,
            choices.toString(),
            grad.getGraduateName(),
            grad.getGraduateEmail()
        );
    }
    
    /**
     * 回答者本人向け 回答内容確認メール本文生成
     */
    public static String buildAnswerSelfNotification(Answer answer) {

        String availability =
            Boolean.TRUE.equals(answer.getEventAvailability())
                ? "出席する"
                : "出席しない";

        // 希望日時（出席する場合のみ）
        StringBuilder choices = new StringBuilder();
        if (Boolean.TRUE.equals(answer.getEventAvailability())) {

            if (answer.getFirstChoiceStartTime() != null) {
                choices.append("■ 希望日時\n");
                choices.append("  【第1希望】 ")
                       .append(answer.getFirstChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getFirstChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            if (answer.getSecondChoiceStartTime() != null) {
                if (choices.length() == 0) choices.append("■ 希望日時\n");
                choices.append("  【第2希望】 ")
                       .append(answer.getSecondChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getSecondChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            if (answer.getThirdChoiceStartTime() != null) {
                if (choices.length() == 0) choices.append("■ 希望日時\n");
                choices.append("  【第3希望】 ")
                       .append(answer.getThirdChoiceStartTime().format(EVENT_FMT))
                       .append(" ～ ")
                       .append(answer.getThirdChoiceEndTime().format(EVENT_FMT))
                       .append("\n");
            }

            choices.append("\n");
        }

        Graduate grad = answer.getGraduate();

        return String.format(
            ANSWER_SELF_TEMPLATE,
            grad.getGraduateName(),
            availability,
            choices.toString(),
            answer.getEvent().getStaff().getStaffName(),
            answer.getEvent().getStaff().getStaffEmail()
        );
    }

    
    /**
     * 卒業生宛拒否通知メール本文
     */
    public static String buildNoAnswerNotification(Graduate graduate, Staff staff) {
        return String.format(NO_ANSWER_TEMPLATE,
            graduate.getGraduateName(),
            staff.getStaffName(),
            staff.getStaffEmail()
        );
    }
}
package action;

import java.time.LocalDateTime;

import model.Email;

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

	protected void sendMail(String toEmail, String subject, String body) {
		Email mail = new Email();
		mail.setTo("24jy0109@jec.ac.jp"); // テスト用
		mail.setSubject(subject);
		mail.setBody(body + "\n\n実際の送付先：" + toEmail);

		boolean result = mail.send();

		if (!result) {
			System.out.println("送信失敗: " + toEmail);
		} else {
			System.out.println("送信成功: " + toEmail);
		}
	}
}
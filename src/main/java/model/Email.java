package model;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class Email {

	private String from;
	private String to;
	
//	 コメントアウト切り替え
//	private String cc;
	private String cc = "24jy0104@jec.ac.jp,24jy0119@jec.ac.jp";

	private String subject;
	private String body;
	private String password;

	public Email() {
		Key key = new Key();
		from = key.getEmailAddress();
		password = key.getEemailPassword();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean send() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			// TO
			message.setRecipients(
				Message.RecipientType.TO,
				InternetAddress.parse(to)
			);

			// CC（指定されている場合のみ）
			if (cc != null && !cc.isBlank()) {
				message.setRecipients(
					Message.RecipientType.CC,
					InternetAddress.parse(cc)
				);
			}

			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
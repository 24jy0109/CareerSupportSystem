package dto;

import model.Company;
import model.Email;
import model.Graduate;
import model.Staff;

public class EmailDTO {
	private Graduate graduate;
	private Staff staff;
	private Company company;
	private Email email;
	private String inputBody;

	public EmailDTO() {

	}

	public Graduate getGraduate() {
		return graduate;
	}

	public void setGraduate(Graduate graduate) {
		this.graduate = graduate;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getInputBody() {
		return inputBody;
	}

	public void setInputBody(String inputBody) {
		this.inputBody = inputBody;
	}

}

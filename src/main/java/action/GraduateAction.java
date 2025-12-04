package action;

import dao.GraduateDBAccess;
import model.Graduate;
import model.Staff;

public class GraduateAction {
	public void execute(String[] data) throws Exception {
		// data[0] アクション
		// data[1] 学籍番号

		String action = data[0];
		GraduateDBAccess GraduateDBA = new GraduateDBAccess();
		Graduate graduate = new Graduate();
		switch (action) {
		case "AssignStaff":
			// data[2] graduateStudentNumber
			// data[3] staffId
			
			graduate.setGraduateStudentNumber(data[2]);
			Staff staff = new Staff();
			staff.setStaffId(Integer.parseInt(data[3]));
			graduate.setStaff(staff);
			GraduateDBA.setStaff(graduate);
			break;
		}
	}
}

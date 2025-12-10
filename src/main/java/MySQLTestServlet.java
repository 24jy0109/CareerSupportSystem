import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/showDatabase")
public class MySQLTestServlet extends HttpServlet {

    private static final String URL = "jdbc:mysql://10.64.144.5:3306/24jy0109";
    private static final String USER = "24jy0109";
    private static final String PASS = "24jy0109";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Map<String, ArrayList<String[]>> tableData = new LinkedHashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                    Statement stmt = conn.createStatement()) {

                String[] tables = { 
                    "company", "course", "staff", "event", "graduate", 
                    "join_graduate", "student", "request", "join_student",
                    "answer" // ← 追加
                };

                for (String table : tables) {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
                    ArrayList<String[]> rows = new ArrayList<>();
                    int columnCount = rs.getMetaData().getColumnCount();

                    // ヘッダー行
                    String[] headers = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        headers[i] = rs.getMetaData().getColumnName(i + 1);
                    }
                    rows.add(headers);

                    // データ行
                    while (rs.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            row[i] = rs.getString(i + 1);
                        }
                        rows.add(row);
                    }

                    tableData.put(table, rows);
                    rs.close();
                }

            }

        } catch (Exception e) {
            out.println("<h2>データベース接続エラー: " + e.getMessage() + "</h2>");
            return;
        }

        // HTML 出力
        out.println("<html><head><meta charset='UTF-8'>");
        out.println("<title>Careersupport DB 表示</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; margin-bottom: 30px; width: 100%; }");
        out.println("th, td { border: 1px solid #333; padding: 5px; text-align: left; }");
        out.println("th { background-color: #eee; }");
        out.println("h2 { margin-top: 40px; }");
        out.println("</style></head><body>");
        out.println("<h1>Careersupport データベースの内容</h1>");

        for (String tableName : tableData.keySet()) {
            out.println("<h2>" + tableName + "</h2>");
            out.println("<table>");

            ArrayList<String[]> rows = tableData.get(tableName);
            for (int i = 0; i < rows.size(); i++) {
                out.println("<tr>");
                for (String col : rows.get(i)) {
                    out.println("<td>" + (col != null ? col : "") + "</td>");
                }
                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("</body></html>");
    }
}

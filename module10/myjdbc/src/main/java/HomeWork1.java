import java.sql.*;

public class HomeWork1 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox?serverTimezone=UTC";
        String user = "root";
        String password = "Root";
        String home1Query ="SELECT (SELECT name FROM courses WHERE id=course_id) AS course" +
                        "   ,MONTH(subscription_date) AS monthNum" +
                        "   ,MONTHNAME(subscription_date) as monthName" +
                        "   ,COUNT(*) AS purchaseNum" +
                        "   FROM subscriptions" +
                        "   GROUP by course, monthNum" +
                        "   ORDER by course, monthNum" +
                        ";";

        try (
                Connection connection = DriverManager.getConnection(url, user, password);

        ){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(home1Query);

            while (resultSet.next() ) {
                String courseName = resultSet.getString("course");
                String monthName = resultSet.getString("monthName");
                int purchaseNum = resultSet.getInt("purchaseNum");
                System.out.println(courseName + "\t\t" + monthName + "\t\t" + purchaseNum);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

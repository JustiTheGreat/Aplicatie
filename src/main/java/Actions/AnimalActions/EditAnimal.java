package Actions.AnimalActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class EditAnimal extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");
            String disponibilitate = request.getParameter("disponibilitate");

            statement.executeUpdate("UPDATE animal SET " + "disponibilitate='" + disponibilitate + "' WHERE id='" + id + "'");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

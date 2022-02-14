package Actions.AnimalActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class AddAnimal extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String denumire = request.getParameter("denumire");
            String categorie = request.getParameter("categorie");
            String sex = request.getParameter("sex");
            String anul_nasterii = request.getParameter("anul_nasterii");
            String magazin = request.getParameter("magazin");
            String id = request.getParameter("id");

            statement.executeUpdate("insert into animal values('" + id + "','" + denumire + "','" + categorie
                    + "','" + sex + "'," + Integer.parseInt(anul_nasterii) + ",'" + magazin + "','disponibil','animal')");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
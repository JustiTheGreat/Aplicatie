package Actions.UserActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class AddCenter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String username = request.getParameter("username");
            String parola = request.getParameter("parola");
            String nume_centru = request.getParameter("nume_centru");
            String judet = request.getParameter("judet");
            String localitate = request.getParameter("localitate");
            String strada = request.getParameter("strada");
            String numar = request.getParameter("numar");
            String telefon = request.getParameter("telefon");
            String categorie = request.getParameter("categorie");

            statement.executeUpdate("insert into centru values('" + username + "','" + parola + "','" + nume_centru + "','" + judet + "','" + localitate
                    + "','" + strada + "'," + Integer.parseInt(numar) + "," + Integer.parseInt(telefon) + ",'" + categorie + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

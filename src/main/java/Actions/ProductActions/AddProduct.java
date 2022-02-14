package Actions.ProductActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class AddProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String denumire = request.getParameter("denumire");
            String categorie = request.getParameter("categorie");
            String pret = request.getParameter("pret");
            String cantitate = request.getParameter("cantitate");
            String magazin = request.getParameter("magazin");
            String id = request.getParameter("id");

            statement.executeUpdate("insert into product values('" + id + "','" + denumire + "','" + categorie
                    + "'," + pret + "," + cantitate + ",'" + magazin + "','disponibil','product')");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

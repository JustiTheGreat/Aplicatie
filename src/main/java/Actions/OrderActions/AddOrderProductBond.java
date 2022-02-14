package Actions.OrderActions;

import Classes.AllProducts;
import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

public class AddOrderProductBond extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id_comanda = request.getParameter("id_comanda");
            String id_produs = request.getParameter("id_produs");

            statement.executeUpdate("insert into order_product values('" + id_comanda + "','" + id_produs + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

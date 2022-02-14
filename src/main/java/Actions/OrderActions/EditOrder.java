package Actions.OrderActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class EditOrder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");
            String stare = request.getParameter("stare");
            String timp_livrare = request.getParameter("timp_livrare");

            statement.executeUpdate("UPDATE sql7372667.order SET " + "stare='" + stare + "' WHERE id_comanda='" + id + "'");
            statement.executeUpdate("UPDATE sql7372667.order SET " + "timp_livrare='" + timp_livrare + "' WHERE id_comanda='" + id + "'");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package Actions.OrderActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class DeleteOrder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");

            statement.executeUpdate("delete from sql7372667.order where id_comanda='" + id + "'");
            statement.executeUpdate("delete from order_product where id_comanda='" + id + "'");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

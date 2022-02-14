package Actions.ProductActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class DeleteProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");

            statement.executeUpdate("delete from product where id=" + id);
            statement.executeUpdate("delete from order_product where id_produs=" + id);

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

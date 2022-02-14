package Actions.OrderActions;

import Classes.AllProducts;
import Classes.Order;
import MyConnection.MyConnection;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetOrdersMagazin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String username = request.getParameter("username");

            ArrayList<Order> orders = new ArrayList<Order>();
            ResultSet rs = statement.executeQuery("select * from sql7372667.order where username_magazin='"+username+"'");
            while (rs.next())
                orders.add(new Order(rs.getLong("id_comanda"), rs.getString("username_client"), rs.getString("judet_client"),
                        rs.getString("localitate_client"), rs.getString("strada_client"), rs.getInt("numar_client"),
                        rs.getInt("telefon_client"),rs.getFloat("cost"), rs.getString("stare"),
                        rs.getString("username_magazin"),new ArrayList<AllProducts>(), rs.getString("timp_livrare"),rs.getString("tip_comanda")));
            connection.close();

            String JsonString = new Gson().toJson(orders);
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(JsonString);
            out.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

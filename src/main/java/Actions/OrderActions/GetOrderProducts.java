package Actions.OrderActions;

import Classes.AllProducts;
import Classes.Animal;
import Classes.Product;
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

public class GetOrderProducts extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();
            String id_comanda = request.getParameter("id_comanda");

            ArrayList<Long> products_ids = new ArrayList<Long>();
            ResultSet rs = statement.executeQuery("select * from order_product where id_comanda='"+id_comanda+"'");
            while (rs.next())
                products_ids.add(rs.getLong("id_produs"));
            ArrayList<AllProducts> ap = new ArrayList<AllProducts>();
            for(Long pi:products_ids)
            {
                rs = statement.executeQuery("select * from animal where id='"+pi+"'");
                while(rs.next())
                    ap.add(new Animal(rs.getString("denumire"), rs.getString("categorie"), rs.getString("sex"),
                            rs.getInt("anul_nasterii"), rs.getString("magazin"), rs.getString("disponibilitate"), rs.getLong("id")));
                rs = statement.executeQuery("select * from product where id='"+pi+"'");
                while(rs.next())
                    ap.add(new Product(rs.getString("denumire"), rs.getString("categorie"), rs.getFloat("pret"),
                            rs.getInt("cantitate"), rs.getString("magazin"), rs.getString("disponibilitate"), rs.getLong("id")));
            }
            connection.close();

            String JsonString = new Gson().toJson(ap);
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

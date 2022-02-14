package Actions.AnimalActions;

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

public class GetAnimal extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");

            ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
            ResultSet rs = statement.executeQuery("select * from animal where id='" + id + "'");
            while (rs.next())
                all_products.add(new Animal(rs.getString("denumire"), rs.getString("categorie"), rs.getString("sex"),
                        rs.getInt("anul_nasterii"), rs.getString("magazin"), rs.getString("disponibilitate"), rs.getLong("id")));

            String JsonString = new Gson().toJson(all_products);
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(JsonString);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

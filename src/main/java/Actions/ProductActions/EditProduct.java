package Actions.ProductActions;

import Classes.AllProducts;

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

public class EditProduct extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id = request.getParameter("id");
            String denumire = request.getParameter("denumire");
            String pret = request.getParameter("pret");
            String categorie = request.getParameter("categorie");
            String cantitate = request.getParameter("cantitate");
            String disponibilitate = Integer.parseInt(cantitate)==0?"indisponibil":"disponibil";

            statement.executeUpdate("UPDATE sql7372667.product SET " + "denumire='" + denumire + "' WHERE id='" + id + "'");
            statement.executeUpdate("UPDATE sql7372667.product SET " + "pret='" + pret + "' WHERE id='" + id + "'");
            statement.executeUpdate("UPDATE sql7372667.product SET " + "categorie='" + categorie + "' WHERE id='" + id + "'");
            statement.executeUpdate("UPDATE sql7372667.product SET " + "cantitate='" + cantitate + "' WHERE id='" + id + "'");
            statement.executeUpdate("UPDATE sql7372667.product SET " + "disponibilitate='" + disponibilitate + "' WHERE id='" + id + "'");

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

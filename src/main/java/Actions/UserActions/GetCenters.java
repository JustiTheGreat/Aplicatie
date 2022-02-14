package Actions.UserActions;

import Classes.AllProducts;
import Classes.Centru;
import Classes.Product;
import MyConnection.MyConnection;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetCenters extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            ArrayList<Centru> magazine = new ArrayList<Centru>();
            ResultSet rs = statement.executeQuery("select * from centru");
            while (rs.next())
                magazine.add(new Centru(rs.getString("nume_centru"), rs.getString("judet"), rs.getString("localitate"),
                        rs.getString("strada"), rs.getInt("numar"), rs.getInt("telefon"),
                        rs.getString("username"), rs.getString("parola")));

            String JsonString = new Gson().toJson(magazine);
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

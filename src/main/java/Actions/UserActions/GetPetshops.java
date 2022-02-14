package Actions.UserActions;

import Classes.Centru;
import Classes.Petshop;
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

public class GetPetshops extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            ArrayList<Petshop> magazine = new ArrayList<Petshop>();
            ResultSet rs = statement.executeQuery("select * from petshop");
            while (rs.next())
                magazine.add(new Petshop(rs.getString("nume_petshop"), rs.getString("judet"), rs.getString("localitate"),
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

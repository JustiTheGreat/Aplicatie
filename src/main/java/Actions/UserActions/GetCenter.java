package Actions.UserActions;

import Classes.Centru;
import Classes.Utilizator;
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

public class GetCenter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String username = request.getParameter("username");

            ArrayList<Utilizator> u = new ArrayList<Utilizator>();
            ResultSet rs = statement.executeQuery("select * from centru where username='" + username + "'");
            while (rs.next())
                u.add(new Centru(rs.getString("nume_centru"), rs.getString("judet"), rs.getString("localitate"),
                        rs.getString("strada"), Integer.parseInt(rs.getString("numar")), Integer.parseInt(rs.getString("telefon")),
                        rs.getString("username"), rs.getString("parola"))
                );

            String JsonString = new Gson().toJson(u);
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

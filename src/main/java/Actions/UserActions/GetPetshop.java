package Actions.UserActions;

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

public class GetPetshop extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String username = request.getParameter("username");

            ArrayList<Petshop> u = new ArrayList<Petshop>();
            ResultSet rs = statement.executeQuery("select * from petshop where username='" + username + "'");
            while (rs.next())
                u.add(new Petshop(rs.getString("nume_petshop"), rs.getString("judet"), rs.getString("localitate"),
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

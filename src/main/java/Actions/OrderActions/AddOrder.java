package Actions.OrderActions;

import MyConnection.MyConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Statement;

public class AddOrder extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String id_comanda = request.getParameter("id_comanda");
            String username = request.getParameter("username");
            String judet = request.getParameter("judet");
            String localitate = request.getParameter("localitate");
            String strada = request.getParameter("strada");
            String numar = request.getParameter("numar");
            String telefon = request.getParameter("telefon");
            String pret = request.getParameter("pret");
            String magazin = request.getParameter("magazin");
            String tip_comanda = request.getParameter("tip_comanda");

            statement.executeUpdate("insert into sql7372667.order values('" + id_comanda + "','" + username + "','" + judet
                    + "','" + localitate + "','" + strada + "'," + numar + "," + telefon + "," + pret + ",'trimisa','" + magazin + "','nespecificat','" + tip_comanda + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

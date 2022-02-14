package Actions.ProductActions;

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

public class GetProducts extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            boolean[] f = {request.getParameter("f1").equals("true"), request.getParameter("f2").equals("true"), request.getParameter("f3").equals("true")};
            boolean[] s = {request.getParameter("s1").equals("true"), request.getParameter("s2").equals("true"),
                    request.getParameter("s3").equals("true"), request.getParameter("s4").equals("true")};

            ResultSet rs = statement.executeQuery("select * from product");
            ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
            while (rs.next())
                all_products.add(new Product(rs.getString("denumire"), rs.getString("categorie"), rs.getFloat("pret"),
                        rs.getInt("cantitate"), rs.getString("magazin"), rs.getString("disponibilitate"), rs.getLong("id")));
            connection.close();
            filt_and_sort(all_products, f, s);

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

    private void filt_and_sort(ArrayList<AllProducts> all_products, boolean[] f, boolean[] s) {
        String[] c = {"HRANA", "INGRIJIRE", "JUCARII"};

        ArrayList<AllProducts> filt = new ArrayList<AllProducts>();
        ArrayList<AllProducts> filt_and_sort = new ArrayList<AllProducts>();

        if (f[0] || f[1] || f[2])
            for (AllProducts p : all_products) {
                if (p.getCategorie().equalsIgnoreCase(c[0]) && f[0]
                        || p.getCategorie().equalsIgnoreCase(c[1]) && f[1]
                        || p.getCategorie().equalsIgnoreCase(c[2]) && f[2])
                    filt.add(p);
            }

        else filt.addAll(all_products);
        if (!filt.isEmpty()) {
            if (s[0] || s[1] || s[2] || s[3]) {
                int i, n = filt.size();
                for (i = 0; i < n; i++) {
                    AllProducts aux = filt.get(0);
                    for (AllProducts p : filt)
                        if (s[0] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) < 0
                                || s[1] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) > 0
                                || s[2] && ((Product) p).getPret() < ((Product) aux).getPret()
                                || s[3] && ((Product) p).getPret() > ((Product) aux).getPret())
                            aux = p;
                    filt_and_sort.add(aux);
                    filt.remove(aux);
                }
            } else filt_and_sort.addAll(filt);
        }
        all_products.clear();
        all_products.addAll(filt_and_sort);
    }
}

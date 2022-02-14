package Actions.MagazineActions;

import Classes.AllProducts;
import Classes.Animal;
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

public class GetAnimalsMagazin extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection connection = MyConnection.getConnection();
            Statement statement = connection.createStatement();

            String magazin = request.getParameter("magazin");
            boolean[] f = {request.getParameter("f1").equals("true"), request.getParameter("f2").equals("true"), request.getParameter("f3").equals("true"),
                    request.getParameter("f4").equals("true"), request.getParameter("f5").equals("true"), request.getParameter("f6").equals("true"),
                    request.getParameter("f7").equals("true")};
            boolean[] s = {request.getParameter("s1").equals("true"), request.getParameter("s2").equals("true"),
                    request.getParameter("s3").equals("true"), request.getParameter("s4").equals("true")};

            ResultSet rs = statement.executeQuery("select * from animal where magazin='"+magazin+"'");
            ArrayList<AllProducts> all_products = new ArrayList<AllProducts>();
            while (rs.next())
                all_products.add(new Animal(rs.getString("denumire"), rs.getString("categorie"), rs.getString("sex"),
                        rs.getInt("anul_nasterii"), rs.getString("magazin"), rs.getString("disponibilitate"), rs.getLong("id")));
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
        String[] c = {"FEMININ", "MASCULIN", "PISICI", "CAINI", "PAPAGALI", "PESTISORI", "HAMSTERI"};

        ArrayList<AllProducts> filt_sex = new ArrayList<AllProducts>();
        ArrayList<AllProducts> filt_sex_tip = new ArrayList<AllProducts>();
        ArrayList<AllProducts> filt_sex_tip_and_sort = new ArrayList<AllProducts>();

        if (f[0] || f[1])
            for (AllProducts p : all_products) {
                if (((Animal) p).getSex().equalsIgnoreCase(c[0]) && f[0] || ((Animal) p).getSex().equalsIgnoreCase(c[1]) && f[1])
                    filt_sex.add(p);
            }
        else filt_sex.addAll(all_products);
        if (f[2] || f[3] || f[4] || f[5] || f[6])
            for (AllProducts p : filt_sex) {
                if (p.getCategorie().equalsIgnoreCase(c[2]) && f[2]
                        || p.getCategorie().equalsIgnoreCase(c[3]) && f[3] || p.getCategorie().equalsIgnoreCase(c[4]) && f[4]
                        || p.getCategorie().equalsIgnoreCase(c[5]) && f[5] || p.getCategorie().equalsIgnoreCase(c[6]) && f[6])
                    filt_sex_tip.add(p);
            }
        else filt_sex_tip.addAll(filt_sex);
        if (!filt_sex_tip.isEmpty()) {
            if (s[0] || s[1] || s[2] || s[3]) {
                int i, n = filt_sex_tip.size();
                for (i = 0; i < n; i++) {
                    AllProducts aux = filt_sex_tip.get(0);
                    for (AllProducts p : filt_sex_tip)
                        if (s[0] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) < 0
                                || s[1] && p.getDenumire().toUpperCase().compareTo(aux.getDenumire().toUpperCase()) > 0
                                || s[2] && ((Animal) p).getAnul_nasterii() < ((Animal) aux).getAnul_nasterii()
                                || s[3] && ((Animal) p).getAnul_nasterii() > ((Animal) aux).getAnul_nasterii())
                            aux = p;
                    filt_sex_tip_and_sort.add(aux);
                    filt_sex_tip.remove(aux);
                }
            } else filt_sex_tip_and_sort.addAll(filt_sex_tip);
        }
        all_products.clear();
        all_products.addAll(filt_sex_tip_and_sort);
    }
}

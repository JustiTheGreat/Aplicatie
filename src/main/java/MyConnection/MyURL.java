package MyConnection;

import java.net.URL;

public class MyURL {
    public static URL getURL(String s) {
        URL url=null;
        try {
            url = new URL("http://localhost:8085" + s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}

package mypackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Iterator;
import javax.servlet.*;
import javax.servlet.http.*;

public class CarRentalList extends HttpServlet {

    private static final String USERID = "admin";
    private static final String PASSWORD = "admin";

    JSONObject dameObjeto(String ruta) {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try {
            Object obj = parser.parse(new FileReader(ruta));

            jsonObject = (JSONObject) obj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    void printaObjeto(JSONObject object, PrintWriter out) {
        out.println("Model: " + object.get("model_vehicle"));
        out.println("<br>");
        out.println("Submodel: " + object.get("sub_model_vehicle"));
        out.println("<br>");
        out.println("Nombre de dies: " + object.get("dies_lloguer"));
        out.println("<br>");
        out.println("Nombre d'unitats: " + object.get("num_vehicles"));
        out.println("<br>");
        out.println("Descompte: " + object.get("descompte"));
        out.println("<br>");
        out.println("Preu: " + object.get("precio_final"));
        out.println("<hr>");
    }

    void loginCorrecto(PrintWriter out) {
        out.println("<TITLE>Llistat de lloguers</TITLE>");
        out.println("<a href=carrental_home.html>Home </a>");
        out.println("<H1>Llistat de lloguers</H1>");

        JSONArray alquileres = (JSONArray) dameObjeto("/home/caps/Desktop/lloguer.json").get("alquileres");

        Iterator<JSONObject> iterator = alquileres.iterator();

        while (iterator.hasNext()) {
            printaObjeto(iterator.next(), out);
        }
        out.println("<a href=carrental_home.html>Home </a>");
    }

    void loginIncorrecto(PrintWriter out) {
        out.println("<TITLE>Error</TITLE>");
        out.println("<H1>S'ha produit un error</H1>");
        out.println("<HR>");

        out.print("<form action=\"list\" method=\"POST\">\n" +
                "<table summary=\"\">\n" +
                "<tr>\n" +
                "<td>UserId:\n" +
                "<td><input name=userid size=16 maxlength=16>\n" +
                "</tr><tr>\n" +
                "<td>Password:\n" +
                "<td><input type=\"password\" name=\"password\" size=\"16\" maxlength=\"16\">\n" +
                "</tr>\n" +
                "</table>\n" +
                "<br>\n" +
                "<input name=llistar_lloguers type=submit value=\"Submit (POST)\">\n" +
                "<br>\n" +
                "</form>\n" +
                "<br>\n" +
                "<a href=\"carrental_home.html\">Home</a>");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        String user = req.getParameter("userid");
        String password = req.getParameter("password");

        if (user.equals(USERID) && password.equals(PASSWORD)) {
            loginCorrecto(out);
        }
        else {
            loginIncorrecto(out);
        }

    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doGet(req, res);
    }
}

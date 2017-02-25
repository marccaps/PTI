package mypackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class CarRentalNew extends HttpServlet {

  private static final double PRECIO_LUXE = 500.0;
  private static final double PRECIO_SEMI = 200.0;
  private static final double PRECIO_ECO = 50.0;
  private static final double PRECIO_LIMUSINA = 100.0;

  void datosIncorrectos(PrintWriter out) {
    out.println("<TITLE>Error</TITLE>");
    out.println("<H1>S'ha produit un error</H1>");
    out.println("<HR>");


    out.println("<h1>New rental</h1>");
    out.println("<FORM ACTION=\"new\" METHOD=\"GET\">\n");
    out.println("<table summary=\"\">\n");
    out.println("<tr>");
    out.println("<td>Car Model:</td>");
    out.println("<td><select name=model_vehicle>");
    out.println("<option selected VALUE=54>Econòmic");
    out.println("<option VALUE=71>Semi-Luxe");
    out.println("<option VALUE=82>Luxe");
    out.println("<option VALUE=139>Limusina");
    out.println("</select>");
    out.println("</td>");
    out.println("<td>Engine:</td>");
    out.println("<td><select name=sub_model_vehicle>");
    out.println("<option selected VALUE=Diesel>Diesel");
    out.println("<option VALUE=Gasolina>Gasolina");
    out.println("</select>");
    out.println("</td>");
    out.println("</tr>");

    out.println("<tr>");
    out.println("<td>Number of days:</td>");
    out.println("<td><input name=dies_lloguer size=3 maxlength=3 value=1></td>");
    out.println("<td>Number of units:</td>");
    out.println("<td><input name=num_vehicles size=3 maxlength=3 value=1></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>Descompte(%):</td>");
    out.println("<td><input name=descompte size=3 maxlength=3 value=0.0></td>");
    out.println("</tr>");

    out.println("</table>");
    out.println("&nbsp; <br>");
    out.println("<input name=llogar type=submit value=\"Submit (GET)\">\n");
    out.println("<br>");
    out.println("</form>");
    out.println("&nbsp; <br>");
    out.println( "<a href=\"/p1/carrental_home.html\">Home </a> \n");
  }

  JSONObject dameObjeto(String ruta) {
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = new JSONObject();

      try {
            Object obj = parser.parse(new FileReader(ruta));

            jsonObject = (JSONObject) obj;

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

  void addCorrect(PrintWriter out, String modelo, String submodelo, String dias, String numV, String des, String precio) {
      out.println("<TITLE>Nou lloguer afegit correctament</TITLE>");
      out.println("<H1>Nou lloguer afegit correctament</H1>");
      out.println("La teva comanda era: <br><br>");

      out.print("<HR>" +
              "Model: " + modelo + "<HR>" +
              "Submodel: " + submodelo + "<HR>" +
              "Nombre de dies: " + dias + "<HR>" +
              "Nombre d'unitats: " + numV + "<HR>" +
              "Descompte: " + des + "<HR>" +
              "Preu total: " + precio + " euros" + "<HR>");

      out.println("<a href=carrental_home.html>Home </a>");
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    String model_vehicle = req.getParameter("model_vehicle");
    String sub_model_vehicle = req.getParameter("sub_model_vehicle");
    String dies_lloguer = req.getParameter("dies_lloguer");
    String num_vehicles = req.getParameter("num_vehicles");
    String descompte = req.getParameter("descompte");



    double des = Double.parseDouble(descompte);
    int numVehicles =  Integer.parseInt(num_vehicles);
    int modelVehicle = Integer.parseInt(model_vehicle);
    int numberOfDays = Integer.parseInt(dies_lloguer);

    des /= 100;

    double precioFinal = 0.0;


    switch(modelVehicle){
        case 54:
            precioFinal = PRECIO_ECO - PRECIO_ECO*des;
            model_vehicle = "Economic";
            break;
        case 71:
            precioFinal = PRECIO_SEMI - PRECIO_SEMI*des;
            model_vehicle = "Semi-Luxe";
            break;
        case 82:
            precioFinal = PRECIO_LUXE - PRECIO_LUXE*des;
            model_vehicle = "Luxe";
            break;
        case 139:
            precioFinal = PRECIO_LIMUSINA - PRECIO_LIMUSINA*des;
            model_vehicle = "Limusina";
            break;
        default:
            break;
    }




    precioFinal = precioFinal *numberOfDays*numVehicles;

    JSONObject padre = new JSONObject();

    JSONArray alquileres = (JSONArray) dameObjeto("/home/caps/Desktop/lloguer.json").get("alquileres");

    if(alquileres == null) alquileres = new JSONArray();

    if(numVehicles < 1 || numberOfDays < 1 || des < 0) {
      datosIncorrectos(out);
    }
    else {

      JSONObject alquiler = new JSONObject();
      alquiler.put("model_vehicle", model_vehicle);
      alquiler.put("sub_model_vehicle", sub_model_vehicle);
      alquiler.put("dies_lloguer", dies_lloguer);
      alquiler.put("num_vehicles", num_vehicles);
      alquiler.put("descompte", descompte);
      alquiler.put("precio_final", Double.toString(precioFinal));
      alquileres.add(alquiler);

      padre.put("alquileres", alquileres);

      try {
          FileWriter file = new FileWriter("/home/caps/Desktop/lloguer.json");
          file.write(padre.toJSONString());
          file.flush();
      }catch (IOException e) {
          e.printStackTrace();
      }

      //out.println("<html><big>Afegit correctament</big><br></html>");

      addCorrect(out, model_vehicle, sub_model_vehicle, dies_lloguer, num_vehicles, descompte, Double.toString(precioFinal));
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
                    throws ServletException, IOException {
    doGet(req, res);
  }
}

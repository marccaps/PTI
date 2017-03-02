import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antonio.arellano
 */
public class CarRental {

  public static void ResetFile(Document myDocument) {
    try {
        // XMLOutputter outputter = new XMLOutputter("  ", true);
        XMLOutputter outputter = new XMLOutputter();

        //output to a file
        FileWriter writer = new FileWriter("carrental.xml");
        outputter.output(myDocument, writer);
        writer.close();

    } catch(java.io.IOException e) {
        e.printStackTrace();
    }
  }

    public static void outputDocumentToFile(Document myDocument) {
        //setup this like outputDocument
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter();

            //output to a file
            FileWriter writer = new FileWriter("carrental.xml");
            outputter.output(myDocument, writer);
            writer.close();

        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void outputDocument(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(myDocument, System.out);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static Document readDocument() {
        try {
            SAXBuilder builder = new SAXBuilder();
            Document anotherDocument = builder.build(new File("carrental.xml"));
            return anotherDocument;
        } catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printElement(Document myDocument) {
        //some setup
        Element carrentalElementPadre = myDocument.getRootElement();

        Element carrentalElement = carrentalElementPadre.getChild("rental");

        //Access a child element
        Element marcaElement = carrentalElement.getChild("marca");
        Element modelElement = carrentalElement.getChild("model");
        Element startElement = carrentalElement.getChild("start");
        Element endElement = carrentalElement.getChild("end");

        //show success or failure
        if(marcaElement != null && modelElement != null && startElement != null && endElement != null) {

            System.out.println(marcaElement.getName() + ": " + marcaElement.getText());
            System.out.println(modelElement.getName() + ": " + modelElement.getText());
            System.out.println(startElement.getName() + ": " + startElement.getText());
            System.out.println(endElement.getName() + ": " + endElement.getText());
        } else {
            System.out.println("Something is wrong.  We did not find a year Element");
        }
    }

    public static Document newCarrental(String marca, String modelo, String fechaIni, String fechaFi) {




        Document myDocument = readDocument();

        Element carrentalElement = myDocument.getRootElement();


        Element rentalElement = new Element("rental");
        Random randomGenerator = new Random();
        int VIN = randomGenerator.nextInt(1000000);
        rentalElement.setAttribute("id",String.valueOf(VIN));

        rentalElement.addContent(new Element("marca").addContent(marca));
        rentalElement.addContent(new Element("model").addContent(modelo));
        rentalElement.addContent(new Element("start").addContent(fechaIni));
        rentalElement.addContent(new Element("end").addContent(fechaFi));

        carrentalElement.addContent(rentalElement);

        return myDocument;
    }

    public static Document resetCarrental() {

        Element carrentalElement = new Element("carrental");
        Document myDocument = new Document(carrentalElement);

        return myDocument;
    }

    public static void executeXSLT(Document myDocument) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
            // Make the input sources for the XML and XSLT documents
            org.jdom.output.DOMOutputter outputter = new org.jdom.output.DOMOutputter();
            org.w3c.dom.Document domDocument = outputter.output(myDocument);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
            StreamSource xsltSource = new StreamSource(new FileInputStream("carrental.xslt"));
			//Make the output result for the finished document
            StreamResult xmlResult = new StreamResult(System.out);
			//Get a XSLT transformer
			Transformer transformer = tFactory.newTransformer(xsltSource);
			//do the transform
			transformer.transform(xmlSource, xmlResult);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
		} catch(TransformerException e) {
            e.printStackTrace();
        } catch(org.jdom.JDOMException e) {
            e.printStackTrace();
        }
	}

    public static Document readNewElement() {
        Scanner teclado = new Scanner(System.in);
        String marca, modelo, fechaIni, fechaFi;
        System.out.print("Introdueixi la marca del vehicle:");
        marca = teclado.nextLine();
        System.out.print("Introdueixi el model del vehicle:");
        modelo = teclado.nextLine();
        System.out.print("Introdueixi la data d'inici del lloguer (format dd/mm/yyyy):");
        fechaIni = teclado.nextLine();
        System.out.print("Introdueixi la data de fi del lloguer (format dd/mm/yyyy):");
        fechaFi = teclado.nextLine();

        return newCarrental(marca, modelo, fechaIni, fechaFi);
    }

    public static void main(String argv[]) {

        if(argv.length == 1) {
            String command = argv[0];
            if(command.equals("reset")) ResetFile(resetCarrental());
            else if(command.equals("new")) outputDocumentToFile(readNewElement());
            else if(command.equals("list")) outputDocument(readDocument());
            else if(command.equals("xslt")) executeXSLT(readDocument());
            else {
                System.out.println(command + " is not a valid option.");
                printUsage();
            }
        } else {
            printUsage();
        }

    }

    public static void printUsage() {
        System.out.println("Usage: CarRental [option] \n where option is one of the following:");
        System.out.println("  reset - The application will create a new XML document with a basic structure"
                + "and will be save in carrental.xml");
        System.out.println("  new - create a new document with the parameters that you are asked to provide"
                + "and will be save in carrental.xml");
        System.out.println("  list - It prints carrental.xml");
        System.out.println("  xslt   - It converts carrental.xml in a HTML document and prints it.");
    }

}

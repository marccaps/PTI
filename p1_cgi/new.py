#! /usr/bin/python
# -*- coding: iso-8859-1 -*-

# Llibreries
import cgi, os, re, sys, string, time, csv, random

# Constants i variables globals

# Funcions

def addRental(modelo, submodelo, dias, numV, des, precio):
    c = csv.writer(open("rentals.csv","a"))
    c.writerow([modelo, submodelo, dias, numV, des, precio])

def informAdd(modelo, submodelo, dias, numV, des):
    print "<TITLE>Nou lloguer afegit correctament</TITLE>\n"
    print "<H1>Nou lloguer afegit correctament</H1>\n"
    print "La teva comanda era: \n"
    print "<br> \n"
    print "<br> \n"
    global precio

    descuento = des/100
    
    if modelo == "139":
        print "Model: Limusina \n"
        precio = 100
    elif modelo == "54":
        print "Model: Econòmic \n"
        precio = 50
    elif modelo == "71":
        print "Model: Semi-Luxe"
        precio = 200
    elif modelo == "82":
        print "Model: Luxe"
        precio = 500

    precio = precio-(precio*descuento)

    print "<HR>\n"
    print "Submodel: " + submodelo + "\n"
    print "<HR>\n"
    print "Nombre de dies: " + dias + "\n"
    print "<HR>\n"
    print "Nombre d'unitats: " + numV + "\n"
    print "<HR>\n"
    print "Descompte: " + str(des) + "\n"
    print "<HR>\n"
    print "Preu total: "
    print precio
    print "euros"
    print "<HR>\n"
    print "<a href=\"/p1/carrental_home.html\">Home </a> \n"

def formIfError():

    print "<TITLE>Error</TITLE>\n"
    print "<H1>S'ha produit un error</H1>\n"
    print"<h1>New rental</h1>"
    print"<FORM ACTION=\"/cgi-bin/%s\" METHOD=\"GET\">\n"%'new.py'
    print"<table summary="">"
    print"<tr>"
    print"<td>Car Model:</td>"
    print"<td><select name=model_vehicle>"
    print"<option selected VALUE=54>Econòmic"
    print"<option VALUE=71>Semi-Luxe"
    print"<option VALUE=82>Luxe"
    print"<option VALUE=139>Limusina"
    print"</select>"
    print"</td>"
    print"<td>Engine:</td>"
    print"<td><select name=sub_model_vehicle>"
    print"<option selected VALUE=Diesel>Diesel"
    print"<option VALUE=Gasolina>Gasolina"
    print"</select>"
    print"</td>"
    print"</tr>"

    print"<tr>"
    print"<td>Number of days:</td>"
    print"<td><input name=dies_lloguer size=3 maxlength=3 value=1></td>"
    print"<td>Number of units:</td>"
    print"<td><input name=num_vehicles size=3 maxlength=3 value=1></td>"
    print"</tr>"
    print"<tr>"
    print"<td>Descompte(%):</td>"
    print"<td><input name=descompte size=3 maxlength=3 value=0.0></td>"
    print"</tr>"

    print"</table>"
    print"&nbsp; <br>"
    print"<input name=llogar type=submit value=\"Submit (GET)\">\n"
    print"<br>"
    print"</form>"
    print"&nbsp; <br>"
    print "<a href=\"/p1/carrental_home.html\">Home </a> \n"
    
    sys.exit()

# Programa principal
print "Content-type: text/html\n\n"
form = cgi.FieldStorage()

# Para debugar invocar
# http://......../jj-python.py?debug=1

if form.has_key('debug'):
    sys.stderr = sys.stdout
    for k in os.environ:
        print "%s = %s<br>"%(k,os.environ[k])
    for k in form.keys():
        value = form.getvalue(k, "")
        if isinstance(value, list):
            value = ",".join(value)
        print "<p> Form field %s = %s</p>"%(k, value)

if (os.environ['REQUEST_METHOD'] == "GET"):

    if (float(form['dies_lloguer'].value) < 1) | (float(form['num_vehicles'].value) < 1) | (float(form['descompte'].value) < 0):
    
        formIfError()

    else:
        
        informAdd(form['model_vehicle'].value, form['sub_model_vehicle'].value, form['dies_lloguer'].value, form['num_vehicles'].value, float(form['descompte'].value))
          
        addRental(form['model_vehicle'].value, form['sub_model_vehicle'].value, form['dies_lloguer'].value, form['num_vehicles'].value, form['descompte'].value, precio)
        
        sys.exit()

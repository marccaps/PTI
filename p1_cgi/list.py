#! /usr/bin/python
# -*- coding: iso-8859-1 -*-

# Llibreries
import cgi, os, re, sys, string, time, csv

# Constants i variables globals
USER = "admin"
PASSWORD = "admin"

# Funcions
def print_error(reason):
    print "<TITLE>Form for Submarine Order</TITLE>\n"
    print "<H1>Error: You did not provide a %s\n"%reason
    sys.exit()

def print_list():
    cr = csv.reader(open("rentals.csv","rb"))
    
    print "<TITLE>Llistat de lloguers</TITLE>\n"
    print "<a href=\"/p1/carrental_home.html\">Home </a> \n"
    print "<H1>Llistat de lloguers</H1>\n"
    
    
    for row in cr:
        
        if row[0] == "139":
            print "Model: Limusina \n"
        elif row[0] == "54":
            print "Model: Econòmic \n"
        elif row[0] == "71":
            print "Model: Semi-Luxe"
        elif row[0] == "82":
            print "Model: Luxe"

        #print "<br> \n"
        #print "Model: " + row[0]
        print "<br> \n"
        print "Submodel: " + row[1]
        print "<br> \n"
        print "Nombre de dies: " + row[2]
        print "<br> \n"
        print "Nombre d'unitats: " + row[3]
        print "<br> \n"
        print "Descompte: " + row[4]
        print "<br> \n"
        print "Preu: " + row[5] + " euros"
        print "<HR>\n"
    
    print "<a href=\"/p1/carrental_home.html\">Home </a> \n"

def error(cause):
    if cause == 'password' or cause == 'userid':
        print "Falta introduir UserId o Pass"
    else:
        print "UserId o Password incorrecte"
    
def formIfError(cause):
    print "<TITLE>Error</TITLE>\n"
    print "<H1>S'ha produit un error</H1>\n"
    error(cause)
    print "<HR>\n"
    print "<FORM ACTION=\"/cgi-bin/%s\" METHOD=\"POST\">\n"%'list.py'
    print "<table summary=\"\"> \n"
    
    print "<tr> \n"
    print "<td> UserId:\n"
    print "<td><INPUT TYPE=\"text\" NAME=\"userid\" MAXLENGTH=\"16\"><P>\n"
    print "</tr> \n"
    
    print "<tr> \n"
    print "<td> Password:\n"
    print "<td><INPUT TYPE=\"password\" NAME=\"password\" MAXLENGTH=\"16\"><P>\n"
    print "</tr> \n"
    print "</table> \n"
    print "<INPUT type=\"submit\" NAME=\"llistar_lloguers\" VALUE=\"Submit\">\n"
    print "</form> \n"
    
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

if (os.environ['REQUEST_METHOD'] == "POST"):
    
    required_fields = ('password', 'userid')
    
    for k in required_fields:
        if not form.has_key(k):
            formIfError(k)
    
    allow = (form['password'].value == PASSWORD) and (form['userid'].value == USER)
    
    if allow:
        print_list()
    else:
        formIfError('wrongPass')

# Java Servlets

## 1. Quick Start

NOTE: 
    Official lab description at: http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/servlets.pdf
    Examples at http://docencia.ac.upc.es/FIB/grau/PTI/lab/_servlet/p2codigo.tgz


### 1.1 Booting the machine

Select the latest Ubuntu imatge (e.g. Ubuntu 14)

    user: alumne
    pwd: sistemes


### 1.2 Install Tomcat 7

Open a terminal (CTRL+ALT+T).

Check if java is installed (if not you will have to install it):

    java -version

If not installed do the following:

    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update
    sudo apt-get install oracle-java8-installer
    sudo apt-get install oracle-java8-set-default
    sudo ln -s /usr/lib/jvm/java-8-oracle /usr/lib/jvm/default-java    
	

Install Tomcat 7:

    sudo apt-get update
    sudo apt-get install tomcat7 
    sudo apt-get install tomcat7-docs tomcat7-admin

    (this should be enough but if it cannot find Java try:
    	sudo gedit /etc/default/tomcat7
 
		JAVA_HOME=/usr/lib/jvm/java-8-oracle

    	sudo service tomcat7 start

Check if it's running (with the browser): http://localhost:8080/   

See configuration at: /etc/tomcat7/

Webapps at: /var/lib/tomcat7

Restart Tomcat with:

    sudo service tomcat7 stop
    sudo service tomcat7 start


### 1.3 Create and display a simple HTML page

    sudo mkdir /var/lib/tomcat7/webapps/my_webapp
    sudo vi /var/lib/tomcat7/webapps/my_webapp/index.html

        <html>
            <h1>Hello World!</h1>
        </html>

    sudo service tomcat7 stop
    sudo service tomcat7 start

Check: http://localhost:8080/my_webapp


### 1.4 Create and simple servlet

    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF
    sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/web.xml

        <web-app>
            <servlet>
              <servlet-name>my_servlet</servlet-name>
              <servlet-class>mypackage.MyServlet</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>my_servlet</servlet-name>
              <url-pattern>/my_servlet</url-pattern>
            </servlet-mapping>
        </web-app>
    
    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes
    sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage
    sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/MyServlet.java

        package mypackage;
        import java.io.*;
        import javax.servlet.*;
        import javax.servlet.http.*;
        public class MyServlet extends HttpServlet {
          public void doGet(HttpServletRequest req, HttpServletResponse res)
                            throws ServletException, IOException {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<html><big>I'm a servlet!!</big></html>");
          }
        }

    sudo javac -cp /usr/share/tomcat7/lib/servlet-api.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/*.java
    sudo service tomcat7 stop
    sudo service tomcat7 start

    Check errors: 

         sudo tail -n 200 /var/lib/tomcat7/logs/catalina.out

    I'ts useful to open a dedicated terminal and check errors continuously:

        sudo tail -f 200 /var/lib/tomcat7/logs/catalina.out

    Check browser:

        http://localhost:8080/my_webapp/my_servlet

## 2 Lab assignment 

You have to program a web application that does exactly the same as in session 1 (CGIs) but this time using Tomcat and servlets.

In order to help you, some files are provided:

- One index and the two HTML forms
- Two servlets (partially programmed)

### 2.1 Install the provided sources

Install git:

    sudo apt-get install git

Download the sources (if you already have the pti repository, just do a git pull):

    cd $HOME       
    git clone https://gitlab.fib.upc.edu/pti/pti.git

Copy the sources to Tomcat and compile the servlets:
    
        sudo cp ./pti/p2_servlets/*.html /var/lib/tomcat7/webapps/my_webapp/
        sudo cp ./pti/p2_servlets/*.java /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage
        sudo mkdir /var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib
        sudo cp ./pti/p2_servlets/*.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib        
        sudo javac -cp /usr/share/tomcat7/lib/servlet-api.jar:/var/lib/tomcat7/webapps/my_webapp/WEB-INF/lib/json-simple-1.1.1.jar /var/lib/tomcat7/webapps/my_webapp/WEB-INF/classes/mypackage/*.java

Add two new servlet definitions to web.xml:

        sudo vi /var/lib/tomcat7/webapps/my_webapp/WEB-INF/web.xml

        <web-app>
            <servlet>
              <servlet-name>new</servlet-name>
              <servlet-class>mypackage.CarRentalNew</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>new</servlet-name>
              <url-pattern>/new</url-pattern>
            </servlet-mapping>
            <servlet>
              <servlet-name>list</servlet-name>
              <servlet-class>mypackage.CarRentalList</servlet-class>
            </servlet>
            <servlet-mapping>
              <servlet-name>list</servlet-name>
              <url-pattern>/list</url-pattern>
            </servlet-mapping>
        </web-app>

        sudo service tomcat7 stop
        sudo service tomcat7 start

Check the following link and submit:
	
	http://localhost:8080/my_webapp/carrental_form_list.html


We recommend you using JSON for writing/reading rental orders to disk. We have included json-simple-1.1.1.jar (http://www.mkyong.com/java/json-simple-example-read-and-write-json/).

## 3. Advanced Tomcat configuration (not necessary to complete this lab)

Open ports for external access with:

    sudo ufw allow 8080/tcp
    sudo ufw allow 8443/tcp

Enabling webapp deployment with the manager:

    sudo vi /etc/tomcat7/tomcat-users.xml
        <role rolename="manager-gui"/>
        <role rolename="admin-gui"/>
        <user username="john" password="1234" roles="manager-gui,admin-gui"/>

Check manager with: http://localhost:8080/manager

Enabling HTTPS. 

    sudo keytool -genkey -alias tomcat -keyalg RSA #use MYPASSWORD
    sudo chmod a+r /root/.keystore
    sudo chmod a+x /root
    sudo vi /etc/tomcat7/server.xml
        <Connector port="8443" protocol="HTTP/1.1" SSLEnabled="true"
                maxThreads="150" scheme="https" secure="true"
            keystoreFile="/root/.keystore" keystorePass="MYPASSWORD" 
                   clientAuth="false" sslProtocol="TLS" />
    sudo service tomcat7 stop
    sudo service tomcat7 start

Check:

     sudo tail -n 200 /var/lib/tomcat7/logs/catalina.out

Enable MYSQL access:

    wget http://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.35.tar.gz
    tar -xvzf mysql-connector-java-5.1.35.tar.gz
    sudo cp mysql-connector-java-5.1.35/mysql-connector-java-5.1.35-bin.jar /usr/share/tomcat7/lib/
    sudo chmod a+r /usr/share/tomcat7/lib/mysql-connector-java-5.1.35-bin.jar 

    



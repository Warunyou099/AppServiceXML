package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    
    public static void main(String[] args) {
        Main m = new Main();
        m.getResponse( "Add" ,2, 1);
        m.getResponse( "Divide" ,2, 2);
        m.getResponse( "Multiply" ,2, 2);
        m.getResponse( "Subtract" ,2, 1);


    }

    public void getResponse(String method , int a, int b) {
        String wsURL =  "http://www.dneonline.com/calculator.asmx?wsdl";
        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = null;
        String outputString="";
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;

        String xmlInput =
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">" +
                        "<soapenv:Header/>" +
                        "<soapenv:Body>" +
                        "<tem:"+method+">" +
                        "<tem:intA>" + a + "</tem:intA>" +
                        "<tem:intB>" + b + "</tem:intB>" +
                        "</tem:"+method+">" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";

//        System.out.println("xmlInput\n" + xmlInput);

        try
        {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;

            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();

//            String SOAPAction = "http://tempuri.org/Add";
//            String SOAPAction = "http://tempuri.org/Subtract";
            String SOAPAction = "http://tempuri.org/"+method;
            // Set the appropriate HTTP parameters.
            httpConn.setRequestProperty("Content-Length", String
                    .valueOf(buffer.length));

            httpConn.setRequestProperty("Content-Type", "text/xml; charset=iso-8859-1");


            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();

            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);

            while ((responseString = in.readLine()) != null)
            {
                outputString = outputString + responseString;
            }
            System.out.println(outputString);


            // Get the response from the web service call
            Document document = parseXmlFile(outputString);

            NodeList nodeLst = document.getElementsByTagName(method+"Response");
            String webServiceResponse = nodeLst.item(0).getTextContent();

//            System.out.println("The response from the web service call is : " + webServiceResponse);

            System.out.println(method+"Response = " + webServiceResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

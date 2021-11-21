package com.webapplication.crossport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Class connecting to auth service
 * @author Herzig Melvyn
 */
public class AuthService {

    /**
     * Service address
     */
    private String address;

    /**
     * Service port
     */
    private int port;

    /**
     * Unique class instance
     */
    private static AuthService instance;

    /**
     * Use singleton pattern to get service
     */
    private AuthService()
    {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "authentication.properties";

        appConfigPath = appConfigPath.replace("%20", " ");

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = appProps.getProperty("address");
        port = Integer.parseInt(appProps.getProperty("port"));
    }

    /**
     * Getting instance
     * @return Unique class instance
     */
    public static AuthService getInstance() {
        if (instance == null)
            instance = new AuthService();
        return instance;
    }

    /**
     * Sets a new address and a new port.
     * @param address New address.
     * @param port New port.
     */
    public static void setAddressAndPort(String address, int port){
        getInstance().address = address;
        getInstance().port = port;
    }

    /**
     * Logins/registers a user sending a json made of username and password with same tags
     * @param requestType Type of request (register or login)
     * @param username Name of user
     * @param password Password of user
     * @return Server response/error
     */
    public String makeRequest(RequestType requestType, String username, String password) {

        String query = "";
        int expectedResponseCode = 0;

        switch (requestType) {
            case LOGIN   : query = "/auth/login";
                           expectedResponseCode = 200; break;
            case REGISTER: query = "/accounts/register";
                           expectedResponseCode = 201; break;
        }

        String body = makeInputString(username, password);
        HttpURLConnection con = makeConnection(query, body.length());

        sendRequest(con, body);

        try {
            return readResponse(con, expectedResponseCode);
        }
        catch (Exception e) {
            return readError(con);
        }
    }

    /**
     * Creates a http connection to post Json body
     * @param query Either REGISTER_QUERY or LOGIN_QUERY
     * @param contentLength Content length
     * @return The connection
     */
    private HttpURLConnection makeConnection(String query, int contentLength)
    {
        HttpURLConnection con = null;

        try {

            // Prepare
            URL url = new URL("http://" + address + ":" + port + query);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Content-Length", Integer.toString(contentLength));
            con.setDoOutput(true);
        }
        catch(Exception e) {
                e.printStackTrace();
        }

        return con;
    }

    /**
     * Uses connection to send content
     * @param con Connection to use
     * @param body Body to send
     */
    private void sendRequest(HttpURLConnection con, String body) {
        // Send
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Reads server response
     * @param con Connection to read
     * @return String containing response
     */
    private String readResponse(HttpURLConnection con, int expectedCode) {

        // If not positive repsonse throw exception
        try {
            if(expectedCode != con.getResponseCode()) {
                System.out.println(con.getResponseCode());
                throw new RuntimeException("Unexpected code for positive response");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = null;

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        responseBuilder.append(responseLine.trim());
            }
            response = responseBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Reads server error response
     * @param con Connection to read
     * @return String containing error response
     */
    private String readError(HttpURLConnection con) {
        String response = null;

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getErrorStream(), "utf-8"))) {
            StringBuilder responseBuilder = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                responseBuilder.append(responseLine.trim());
            }
            response = responseBuilder.toString();
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Formats in JSON an input string based on username and password
     * @param username Username
     * @param password Password
     * @return Built string as JSON format
     */
    private String makeInputString(String username, String password) {
        return "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
    }

}

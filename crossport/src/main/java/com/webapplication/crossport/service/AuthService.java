package com.webapplication.crossport.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Class connecting to auth service
 * @author Herzig Melvyn
 */
public class AuthService {

    /**
     * Service address
     */
    private final String ADDRESS = "localhost";

    /**
     * Service port
     */
    private final int PORT = 8081;

    /**
     * Unique class instance
     */
    private static AuthService instance;

    /**
     * Use singleton pattern to get service
     */
    private AuthService(){}

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
     * Logins/registers a user
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
            URL url = new URL("http://" + ADDRESS + ":" + PORT + query);
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

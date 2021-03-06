/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Radek
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn5220"})
public class SignIn extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            BufferedReader bufferedReader = request.getReader();
            JSONObject jsonObject = (JSONObject) JSONValue.parse(bufferedReader);

            InitialContext ic = new InitialContext();
            Context initialContext = (Context) ic.lookup("java:comp/env");
            DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
            connection = datasource.getConnection();

            String checkEmailQuery = "select * from user where EMAIL='" + jsonObject.get("email").toString() + "'";
            ps = connection.prepareStatement(checkEmailQuery);
            ResultSet rs = ps.executeQuery();
            
            Long userId = 0L;
            
            if (rs.next()) {
                if (rs.getString(4).equals(jsonObject.get("password").toString())){
                    
                    userId = rs.getLong(1);
                   
                    
                    JSONObject json = new JSONObject();
                    JSONObject jsonSettings = new JSONObject();
                    JSONObject jsonMessages = new JSONObject();
                    JSONArray jSONArrayPeoples = new JSONArray();
                    JSONArray jSONArrayPlaces = new JSONArray();
                    JSONArray jSONArrayPlace2People = new JSONArray();
                    
                    String getSettingsQuery = "select * from settings where USER_ID='" + userId + "'";
                    ps = connection.prepareStatement(getSettingsQuery);
                    rs = ps.executeQuery();
                    
                    if (rs.next()) {
                        jsonSettings.put("id", rs.getLong(1));
                        jsonSettings.put("familyChange", rs.getInt(2));
                        jsonSettings.put("placesChange", rs.getInt(3));
                        jsonSettings.put("gpsRefresh", rs.getInt(4));
                        jsonSettings.put("notifications", rs.getString(5));
                        jsonSettings.put("userId", rs.getLong(6));
                        jsonSettings.put("msgCounter", rs.getInt(7));
                        jsonSettings.put("premium", rs.getInt(8));
                    }
                    
                    String getMessagesQuery = "select * from messages where USER_ID='" + userId + "'";
                    ps = connection.prepareStatement(getMessagesQuery);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        jsonMessages.put("id", rs.getLong(1));
                        jsonMessages.put("msg", rs.getString(2));
                    }
                   
                    String getPeopleQuery = "select * from people where USER_ID='" + userId + "'";
                    ps = connection.prepareStatement(getPeopleQuery);
                    rs = ps.executeQuery();
                    
                    
                    while (rs.next()) {
                        JSONObject people = new JSONObject();
                        people.put("id", rs.getLong(1));
                        people.put("name", rs.getString(2));
                        people.put("localizationId", rs.getLong(4));
                        people.put("active", rs.getInt(5));
                        people.put("image", rs.getString(6));
                        people.put("password", rs.getString(7));
                        people.put("context", rs.getInt(8));
                        people.put("authorizedSpeed", rs.getInt(9));
                        people.put("messagesId", rs.getInt(10));
                        people.put("avatar", rs.getInt(11));
                        jSONArrayPeoples.add(people);
                    }

                    String getPlacesQuery = "select * from places where USER_ID='" + userId + "'";
                    ps = connection.prepareStatement(getPlacesQuery);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        JSONObject places = new JSONObject();
                        places.put("id", rs.getLong(1));
                        places.put("name", rs.getString(2));
                        places.put("radius", rs.getInt(3));
                        places.put("longitude", rs.getDouble(4));
                        places.put("latitude", rs.getDouble(5));

                        jSONArrayPlaces.add(places);
                    }
                    
                    String getPlaces2PeopleQuery = "select * from place2people where USER_ID='" + userId + "'";
                    ps = connection.prepareStatement(getPlaces2PeopleQuery);
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        JSONObject place2People = new JSONObject();
                        place2People.put("id", rs.getLong(1));
                        place2People.put("placeId", rs.getLong(2));
                        place2People.put("peopleId", rs.getLong(3));

                        jSONArrayPlace2People.add(place2People);
                    }
                    json.put("settings", jsonSettings);
                    json.put("peoples", jSONArrayPeoples);
                    json.put("places", jSONArrayPlaces);
                    json.put("messages", jsonMessages);
                    json.put("place2Peoples", jSONArrayPlace2People);
                    json.put("error", 0);
                    response.getWriter().write(json.toString());
                }else{
                    JSONObject json = new JSONObject();
                    json.put("error", 1);
                    json.put("desc", "");
                    response.getWriter().write(json.toString());
                }
            } else {
                JSONObject json = new JSONObject();
                json.put("error", 1);
                json.put("desc", "");
                response.getWriter().write(json.toString());
            }

        } catch (IOException ex) {
            JSONObject json = new JSONObject();
            json.put("error", 2);
            json.put("desc", ex.getMessage());
            response.getWriter().write(json.toString());
        } catch (NamingException ex) {
            JSONObject json = new JSONObject();
            json.put("error", 2);
            json.put("desc", ex.getMessage());
            response.getWriter().write(json.toString());
        } catch (SQLException ex) {
            JSONObject json = new JSONObject();
            json.put("error", 2);
            json.put("desc", ex.getMessage());
            response.getWriter().write(json.toString());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (out != null) {
                    out.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                JSONObject json = new JSONObject();
                json.put("error", 2);
                json.put("desc", ex.getMessage());
                response.getWriter().write(json.toString());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

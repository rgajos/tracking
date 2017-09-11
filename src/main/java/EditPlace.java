/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
@WebServlet(name = "EditPlace", urlPatterns = {"/EditPlace5220"})
public class EditPlace extends HttpServlet {

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

            String replacePlaceQuery = "update places set name='" + jsonObject.get("name").toString() +"' , radius=" +(Long) jsonObject.get("radius") +" , longitude="+ (Double) jsonObject.get("longitude")+ " , latitude="+ (Double) jsonObject.get("latitude") +" where ID=" + (Long) jsonObject.get("placeId");
            ps = connection.prepareStatement(replacePlaceQuery);
            ps.executeUpdate();

            JSONArray jsonArray = (JSONArray) jsonObject.get("peopleIds");

            String deletePlace2PeopleQuery = "delete from place2people where PLACES_ID=" + (Long) jsonObject.get("placeId");
            ps = connection.prepareStatement(deletePlace2PeopleQuery);
            ps.executeUpdate();
            
            JSONArray place2peopleJSONArray = new JSONArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                String insertPeople2PlaceQuery = "insert into place2people (places_id, people_id, user_id) values (?,?,?)";
                ps = connection.prepareStatement(insertPeople2PlaceQuery, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, (Long) jsonObject.get("placeId"));
                ps.setLong(2, Long.valueOf(jsonArray.get(i).toString()));
                ps.setLong(3, (Long) jsonObject.get("userId"));

                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                long places2peopleId = 0;
                if (generatedKeys.next()) {
                    places2peopleId = generatedKeys.getInt(1);
                }
                JSONObject place2people = new JSONObject();
                place2people.put("id", places2peopleId);
                place2people.put("placeId", (Long) jsonObject.get("placeId"));
                place2people.put("peopleId", Long.valueOf(jsonArray.get(i).toString()));

                place2peopleJSONArray.add(place2people);
            }

            String getSettingsQuery = "select * from settings where ID=" + (Long) jsonObject.get("settingsId");
            ps = connection.prepareStatement(getSettingsQuery);
            ResultSet rs = ps.executeQuery();

            int placeChange = 0;

            if (rs.next()) {
                placeChange = rs.getInt(3);
                placeChange++;
            }

            String updateFamilyChangeQuery = "update settings set PLACE_CHANGE=" + placeChange + " where ID=" + (Long) jsonObject.get("settingsId");
            ps = connection.prepareStatement(updateFamilyChangeQuery);
            ps.executeUpdate();

            JSONObject json = new JSONObject();
            json.put("error", 0);
            json.put("place2peoples", place2peopleJSONArray);
            response.getWriter().write(json.toString());

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
        } catch (Exception ex) {
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

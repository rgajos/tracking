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
@WebServlet(name = "EditMyAccount", urlPatterns = {"/EditMyAccount5220"})
public class EditMyAccount extends HttpServlet {

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
            
            if((Long) jsonObject.get("context") == 0){
                String updatePeopleQuery = "update people set name='" + jsonObject.get("name").toString() + "' where ID=" + (Long) jsonObject.get("peopleId");
                ps = connection.prepareStatement(updatePeopleQuery);
                ps.executeUpdate();
            }else if ((Long) jsonObject.get("context") == 1) {
                if((Long) jsonObject.get("active") == 1){
                    String updatePeopleQuery = "update people set active=" + (Long) jsonObject.get("active") + " , name='" + jsonObject.get("name").toString() + "' , password='" + jsonObject.get("myLocationPassword").toString() + "' where ID=" + (Long) jsonObject.get("peopleId");
                    ps = connection.prepareStatement(updatePeopleQuery);
                    ps.executeUpdate();
                }else{
                    String updatePeopleQuery = "update people set active=" + (Long) jsonObject.get("active") + " , name='" + jsonObject.get("name").toString() + "' where ID=" + (Long) jsonObject.get("peopleId");
                    ps = connection.prepareStatement(updatePeopleQuery);
                    ps.executeUpdate();
                }
            }else{
                String updateUserQuery = "update user set email='"+ jsonObject.get("email").toString() + "' , name='" + jsonObject.get("name").toString() + "' , password='" + jsonObject.get("password").toString() +  "' where ID=" + (Long) jsonObject.get("userId");
                ps = connection.prepareStatement(updateUserQuery);
                ps.executeUpdate();
                
                if ((Long) jsonObject.get("active") == 1) {
                    String updatePeopleQuery = "update people set active='" + jsonObject.get("active").toString() + "' , name='" + jsonObject.get("name").toString() + "' , password='" + jsonObject.get("myLocationPassword").toString() + "' where ID=" + (Long) jsonObject.get("peopleId");
                    ps = connection.prepareStatement(updatePeopleQuery);
                    ps.executeUpdate();
                }else{
                    String updatePeopleQuery = "update people set active='" + jsonObject.get("active").toString() + "' , name='" + jsonObject.get("name").toString() + "' where ID=" + (Long) jsonObject.get("peopleId");
                    ps = connection.prepareStatement(updatePeopleQuery);
                    ps.executeUpdate();
                }
            }
            
            String getSettingsQuery = "select * from settings where ID='" + (Long) jsonObject.get("settingsId") + "'";
            ps = connection.prepareStatement(getSettingsQuery);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int familyChange = rs.getInt(2);
                familyChange++;
                String updateFamilyChangeQuery = "update settings set FAMILY_CHANGE=" + familyChange + " where ID=" + (Long) jsonObject.get("settingsId");
                ps = connection.prepareStatement(updateFamilyChangeQuery);
                ps.executeUpdate();
            }

            JSONObject json = new JSONObject();
            json.put("error", 0);
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

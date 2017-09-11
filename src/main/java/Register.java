/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Radek
 */
@WebServlet(name = "Register", urlPatterns = {"/Register5220"})
public class Register extends HttpServlet {
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

                String checkEmailquery = "select EMAIL from user where EMAIL='" + jsonObject.get("email").toString() + "'";
                
                ps = connection.prepareStatement(checkEmailquery);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JSONObject json = new JSONObject();
                    json.put("error", 1);
                    json.put("desc", "");
                    response.getWriter().write(json.toString());
                } else {
                    
                    String insertUserQuery = "insert into user (name, email, password, reset_password) values (?,?,?,?)";
                    ps = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, jsonObject.get("name").toString());
                    ps.setString(2, jsonObject.get("email").toString());
                    ps.setString(3, jsonObject.get("password").toString());
                    ps.setString(4, "0");
                    ps.executeUpdate();

                    ResultSet generatedKeys = ps.getGeneratedKeys();
                    long userId = 0;
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    }
                    
                    String insertLocalizationQuery = "insert into localizations values ()";
                    ps = connection.prepareStatement(insertLocalizationQuery, Statement.RETURN_GENERATED_KEYS);
                    ps.executeUpdate();

                    generatedKeys = ps.getGeneratedKeys();
                    long localizationId = 0;
                    if (generatedKeys.next()) {
                        localizationId = generatedKeys.getInt(1);
                    }
                    Gson gson = new Gson();
                    List<Message> messages = new ArrayList<Message>();
                    
                    String insertMessagesQuery = "insert into messages (msg, user_id) values (?,?)";
                    ps = connection.prepareStatement(insertMessagesQuery, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, gson.toJson(messages));
                    ps.setLong(2, userId);
                    ps.executeUpdate();

                    generatedKeys = ps.getGeneratedKeys();
                    long messagesId = 0;
                    if (generatedKeys.next()) {
                        messagesId = generatedKeys.getInt(1);
                    }
                    
                    String insertPeopleQuery = "insert into people (name, user_id, localization_id, context, messages_id) values (?,?,?,?,?)";
                    ps = connection.prepareStatement(insertPeopleQuery, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, jsonObject.get("name").toString());
                    ps.setLong(2, userId);
                    ps.setLong(3, localizationId);
                    ps.setInt(4,2);
                    ps.setLong(5, messagesId);
                    ps.executeUpdate();

                    generatedKeys = ps.getGeneratedKeys();
                    long peopleId = 0;
                    if (generatedKeys.next()) {
                        peopleId = generatedKeys.getInt(1);
                    }
                    
                    String updateLocalizationQuery = "update localizations set "
                            + "PEOPLE_ID='" + peopleId + "' where ID=" + localizationId;
                    ps = connection.prepareStatement(updateLocalizationQuery);
                    ps.executeUpdate();

                    String insertSettingsQuery = "insert into settings (notifications, user_id) values (?, ?)";
                    ps = connection.prepareStatement(insertSettingsQuery, Statement.RETURN_GENERATED_KEYS);
                    
                    List<Notification> notifications = new ArrayList<Notification>();

                    ps.setString(1, gson.toJson(notifications));
                    ps.setLong(2, userId);
                    ps.executeUpdate();

                    generatedKeys = ps.getGeneratedKeys();
                    long settingsId = 0;
                    if (generatedKeys.next()) {
                        settingsId = generatedKeys.getInt(1);
                    }

                    JSONObject json = new JSONObject();
                    json.put("error", 0);
                    json.put("userId", userId);
                    json.put("peopleId", peopleId);
                    json.put("name", jsonObject.get("name").toString());
                    json.put("localizationId", localizationId);
                    json.put("settingsId", settingsId);
                    json.put("messagesId", messagesId);
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
                    if(ps != null){
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

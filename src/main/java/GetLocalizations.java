/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
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
import java.util.Arrays;
import java.util.Iterator;
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
@WebServlet(name = "GetLocalizations", urlPatterns = {"/GetLocalizations5220"})
public class GetLocalizations extends HttpServlet {

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
            ResultSet rs;

            JSONObject json = new JSONObject();

            if (jsonObject.get("peoplesLocalizationId").toString().length() > 0) {
                JSONArray localizationJSONArray = new JSONArray();

                String getLocalizationsQuery = "select * from localizations where ID in (" + jsonObject.get("peoplesLocalizationId").toString() + ")";
                ps = connection.prepareStatement(getLocalizationsQuery);
                rs = ps.executeQuery();
                while (rs.next()) {
                    JSONObject localization = new JSONObject();
                    localization.put("id", rs.getLong(1));
                    localization.put("longitude", rs.getDouble(2));
                    localization.put("latitude", rs.getDouble(3));
                    localization.put("time", rs.getString(4));
                    localization.put("battery", rs.getInt(5));
                    localization.put("accuracy", rs.getFloat(6));
                    localization.put("peopleId", rs.getLong(7));

                    localizationJSONArray.add(localization);

                }
                json.put("localizations", localizationJSONArray);
            }

            JSONObject jsonSettings = new JSONObject();

            String getSettingsQuery = "select * from settings where ID='" + jsonObject.get("settingId").toString() + "'";
            ps = connection.prepareStatement(getSettingsQuery);
            rs = ps.executeQuery();

            if (rs.next()) {
                jsonSettings.put("familyChange", rs.getInt(2));
                jsonSettings.put("placesChange", rs.getInt(3));
                jsonSettings.put("gpsRefresh", rs.getInt(4));
                jsonSettings.put("notifications", rs.getString(5));
                jsonSettings.put("msgCounter", rs.getInt(7));
                jsonSettings.put("premium", rs.getInt(8));
            }

            if ((Boolean) jsonObject.get("deletedNotification")) {

                Gson gson = new Gson();
                ArrayList serverNotifications = new ArrayList<Notification>(Arrays.asList(gson.fromJson(rs.getString(5), Notification[].class)));

                int notificationSize = serverNotifications.size();

                if (notificationSize > 0) {

                    Notification[] notifications = gson.fromJson(jsonObject.get("notifications").toString(), Notification[].class);

                    for (Iterator<Notification> iterator = serverNotifications.iterator(); iterator.hasNext();) {
                        Notification serverNotification = iterator.next();
                        for (Notification notification : notifications) {
                            if (notification.getTime().equals(serverNotification.getTime())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    if (notificationSize > serverNotifications.size()) {
                        String actualNotifications = gson.toJson(serverNotifications);
                        jsonSettings.put("notifications", actualNotifications);

                        String updateNotificationQuery = "update settings set NOTIFICATIONS='" + actualNotifications + "' where ID='" + jsonObject.get("settingId").toString() + "'";
                        ps = connection.prepareStatement(updateNotificationQuery);
                        ps.executeUpdate();
                    }
                }
            }

            json.put("settings", jsonSettings);
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

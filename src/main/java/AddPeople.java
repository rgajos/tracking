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
import java.sql.Statement;
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
@WebServlet(name = "AddPeople", urlPatterns = {"/AddPeople5220"})
public class AddPeople extends HttpServlet {

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

            
            String checkAddPeopleQuery = "select * from add_people where code='" + jsonObject.get("code").toString() + "'";
            ps = connection.prepareStatement(checkAddPeopleQuery);
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                JSONObject json = new JSONObject();
                json.put("error", 3);
                json.put("desc", "CODE EXIST");
                response.getWriter().write(json.toString());
            }else{
                String insertAddPeopleQuery = "insert into add_people (code, context, settings_id, messages_Id, people_ids) values (?,?,?,?,?)";
                ps = connection.prepareStatement(insertAddPeopleQuery, Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, (Long) jsonObject.get("code"));
                ps.setLong(2, (Long) jsonObject.get("context"));
                ps.setLong(3, (Long) jsonObject.get("settingsId"));
                ps.setLong(4, (Long) jsonObject.get("messagesId"));
                ps.setString(5, (String) jsonObject.get("peopleIds"));
                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();

                if (generatedKeys.next()) {
                    if (generatedKeys.getInt(1) > 0) {
                        JSONObject json = new JSONObject();
                        json.put("error", 0);
                        response.getWriter().write(json.toString());
                    } else {
                        JSONObject json = new JSONObject();
                        json.put("error", 2);
                        json.put("desc", "ERROR ! TRY AGAIN ...");
                        response.getWriter().write(json.toString());
                    }
                } else {
                    JSONObject json = new JSONObject();
                    json.put("error", 2);
                    json.put("desc", "ERROR ! TRY AGAIN ...");
                    response.getWriter().write(json.toString());
                }
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
        }finally {
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

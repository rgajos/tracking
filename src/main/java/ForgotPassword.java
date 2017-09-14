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
import java.util.Random;
import java.util.StringTokenizer;
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
@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword5220"})
public class ForgotPassword extends HttpServlet {

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

            Random random = new Random();
            int code = random.nextInt(1000000) + 2;
            
            String checkEmailQuery = "select EMAIL from user where EMAIL='" + jsonObject.get("email").toString() + "'";
            String setCodeQuery = "update user set RESET_PASSWORD ='"+ String.valueOf(code)+"' where EMAIL ='" + jsonObject.get("email").toString() + "'";

            ps = connection.prepareStatement(setCodeQuery);
            ps.executeUpdate();
            
            ps = connection.prepareStatement(checkEmailQuery);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                
                StringTokenizer st = new StringTokenizer(jsonObject.get("email").toString());

                String email = "";

                while (st.hasMoreElements()) {
                    email += String.valueOf(Character.toChars(Integer.valueOf(st.nextToken())));
                }
                
                SendEmail sendEmail = new SendEmail();
                String result = sendEmail.sendMessage("trackerrgmobile@yahoo.com",
                        email,
                        "Finder24 password reset request confirmation",
                        "We've received your request to reset your password, and would be glad to help. <br><br> In order for us to verify you are the account owner, please click the following link to reset your password. <br><br> <a href = \"http://tracking-tracking.b9ad.pro-us-east-1.openshiftapps.com/ResetPassword?code=" + code +"\"> http://tracking-tracking.b9ad.pro-us-east-1.openshiftapps.com/ResetPassword?code=" + code +" </a> <br><br> If clicking the link above doesn't work, please copy and paste the URL in a new browser window instead. <br><br> If you did not request your password to be reset (or you remembered your password), just ignore this messsage; no changes have been made to your account. <br><br> Sincerely, <br> The RG Mobile Team");

                if(result.equals("ok")){
                    JSONObject json = new JSONObject();
                    json.put("error", 0);
                    json.put("desc", email);
                    response.getWriter().write(json.toString());
                }else{
                    JSONObject json = new JSONObject();
                    json.put("error", 2);
                    json.put("desc", result);
                    response.getWriter().write(json.toString());
                }
            }else{
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

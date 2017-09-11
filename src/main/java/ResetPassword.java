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
@WebServlet(name = "ResetPassword", urlPatterns = {"/ResetPassword"})
public class ResetPassword extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            InitialContext ic = new InitialContext();
            Context initialContext = (Context) ic.lookup("java:comp/env");
            DataSource datasource = (DataSource) initialContext.lookup("jdbc/MySQLDS");
            connection = datasource.getConnection();

            int code = Integer.valueOf(request.getParameter("code"));
            
            if(code < 2){
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Title tracker</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Something goes wrong. Try again.</h1>");
                out.println("</body>");
                out.println("</html>");
                return;
            }
            
            Random random = new Random();
            int randromCode = random.nextInt(10000) + 2;
            
            String text = "np" + randromCode;
            
            String msg = "";

            for (int i = 0; i < text.length(); i++) {
                int stringCode = (int) text.charAt(i);
                if (i == text.length() - 1) {
                    msg += stringCode;
                } else {
                    msg += stringCode + " ";
                }
            }
            
            String setPasswordQuery = "update user set PASSWORD = '" + msg + "',RESET_PASSWORD = '0' where RESET_PASSWORD ='" + code + "'";
            ps = connection.prepareStatement(setPasswordQuery);
            int id = ps.executeUpdate();
            
            if(id > 0){
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Title tracker</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Your new password is: np" + randromCode + ".<br><br> You can change the password in the account settings</h1>");
                out.println("</body>");
                out.println("</html>");
            }else{
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Title tracker</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Something goes wrong. Try again.</h1>");
                out.println("</body>");
                out.println("</html>");  
            }
        } catch (NamingException ex) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Title tracker</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Something goes wrong. Try again.</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Title tracker</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Something goes wrong. Try again.</h1>");
            out.println("</body>");
            out.println("</html>");
        } catch (NumberFormatException ex) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Title tracker</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Something goes wrong. Try again.</h1>");
            out.println("</body>");
            out.println("</html>");
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

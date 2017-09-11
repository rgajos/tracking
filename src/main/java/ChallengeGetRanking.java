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
@WebServlet(name = "ChallengeGetRanking", urlPatterns = {"/ChallengeGetRanking5220"})
public class ChallengeGetRanking extends HttpServlet {

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

            String selectRankingQuery = "";

            Long route = (Long) jsonObject.get("route");
            Long id = (Long) jsonObject.get("id");
            
            switch (route.intValue()) {
                case 1:
                    selectRankingQuery = "select * from challenge_walking_2000 order by time asc";
                    break;
                case 2:
                    selectRankingQuery = "select * from challenge_walking_4000 order by time asc";
                    break;
                case 3:
                    selectRankingQuery = "select * from challenge_walking_6000 order by time asc";
                    break;
                case 4:
                    selectRankingQuery = "select * from challenge_walking_8000 order by time asc";
                    break;
                case 5:
                    selectRankingQuery = "select * from challenge_walking_10000 order by time asc";
                    break;
                case 6:
                    selectRankingQuery = "select * from challenge_walking_12000 order by time asc";
                    break;
                case 7:
                    selectRankingQuery = "select * from challenge_walking_14000 order by time asc";
                    break;
                case 8:
                    selectRankingQuery = "select * from challenge_walking_16000 order by time asc";
                    break;
                case 9:
                    selectRankingQuery = "select * from challenge_walking_18000 order by time asc";
                    break;
                case 10:
                    selectRankingQuery = "select * from challenge_walking_20000 order by time asc";
                    break;
                case 11:
                    selectRankingQuery = "select * from challenge_walking_22000 order by time asc";
                    break;
                case 12:
                    selectRankingQuery = "select * from challenge_walking_24000 order by time asc";
                    break;
                case 13:
                    selectRankingQuery = "select * from challenge_walking_26000 order by time asc";
                    break;
                case 14:
                    selectRankingQuery = "select * from challenge_walking_28000 order by time asc";
                    break;
                case 15:
                    selectRankingQuery = "select * from challenge_walking_30000 order by time asc";
                    break;
                case 16:
                    selectRankingQuery = "select * from challenge_walking_42195 order by time asc";
                    break;
                case 17:
                    selectRankingQuery = "select * from challenge_running_2000 order by time asc";
                    break;
                case 18:
                    selectRankingQuery = "select * from challenge_running_4000 order by time asc";
                    break;
                case 19:
                    selectRankingQuery = "select * from challenge_running_6000 order by time asc";
                    break;
                case 20:
                    selectRankingQuery = "select * from challenge_running_8000 order by time asc";
                    break;
                case 21:
                    selectRankingQuery = "select * from challenge_running_10000 order by time asc";
                    break;
                case 22:
                    selectRankingQuery = "select * from challenge_running_12000 order by time asc";
                    break;
                case 23:
                    selectRankingQuery = "select * from challenge_running_14000 order by time asc";
                    break;
                case 24:
                    selectRankingQuery = "select * from challenge_running_16000 order by time asc";
                    break;
                case 25:
                    selectRankingQuery = "select * from challenge_running_18000 order by time asc";
                    break;
                case 26:
                    selectRankingQuery = "select * from challenge_running_20000 order by time asc";
                    break;
                case 27:
                    selectRankingQuery = "select * from challenge_running_22000 order by time asc";
                    break;
                case 28:
                    selectRankingQuery = "select * from challenge_running_24000 order by time asc";
                    break;
                case 29:
                    selectRankingQuery = "select * from challenge_running_26000 order by time asc";
                    break;
                case 30:
                    selectRankingQuery = "select * from challenge_running_28000 order by time asc";
                    break;
                case 31:
                    selectRankingQuery = "select * from challenge_running_30000 order by time asc";
                    break;
                case 32:
                    selectRankingQuery = "select * from challenge_running_42195 order by time asc";
                    break;
                case 33:
                    selectRankingQuery = "select * from challenge_cycling_5000 order by time asc";
                    break;
                case 34:
                    selectRankingQuery = "select * from challenge_cycling_10000 order by time asc";
                    break;
                case 35:
                    selectRankingQuery = "select * from challenge_cycling_15000 order by time asc";
                    break;
                case 36:
                    selectRankingQuery = "select * from challenge_cycling_20000 order by time asc";
                    break;
                case 37:
                    selectRankingQuery = "select * from challenge_cycling_25000 order by time asc";
                    break;
                case 38:
                    selectRankingQuery = "select * from challenge_cycling_30000 order by time asc";
                    break;
                case 39:
                    selectRankingQuery = "select * from challenge_cycling_35000 order by time asc";
                    break;
                case 40:
                    selectRankingQuery = "select * from challenge_cycling_40000 order by time asc";
                    break;
                case 41:
                    selectRankingQuery = "select * from challenge_cycling_45000 order by time asc";
                    break;
                case 42:
                    selectRankingQuery = "select * from challenge_cycling_50000 order by time asc";
                    break;
                case 43:
                    selectRankingQuery = "select * from challenge_cycling_55000 order by time asc";
                    break;
                case 44:
                    selectRankingQuery = "select * from challenge_cycling_60000 order by time asc";
                    break;
                case 45:
                    selectRankingQuery = "select * from challenge_cycling_65000 order by time asc";
                    break;
                case 46:
                    selectRankingQuery = "select * from challenge_cycling_70000 order by time asc";
                    break;
                case 47:
                    selectRankingQuery = "select * from challenge_cycling_75000 order by time asc";
                    break;
                case 48:
                    selectRankingQuery = "select * from challenge_cycling_100000 order by time asc";
                    break;
            }

            ps = connection.prepareStatement(selectRankingQuery);
            ResultSet rs = ps.executeQuery();
            
            JSONObject jSONObject = new JSONObject();
            
            JSONArray jSONArray = new JSONArray();
            
            int cnt = 0;
            boolean finish = false;
            
            if(id == 0 ){
                finish = true;
            }
            
            while(rs.next()){
                if(cnt < 1001){
                    JSONObject rankingPlace = new JSONObject();
                    rankingPlace.put("place", cnt);
                    rankingPlace.put("time", rs.getLong(3));
                    rankingPlace.put("name", rs.getString(2));
                    rankingPlace.put("country", rs.getString(4));
                    jSONArray.add(rankingPlace);
                }else if(finish){
                    break;
                }
                
                if(!finish && rs.getLong(1) == id){
                    JSONObject myPlaceJSONObject = new JSONObject();
                    myPlaceJSONObject.put("place", cnt);
                    myPlaceJSONObject.put("time", rs.getLong(3));
                    myPlaceJSONObject.put("name", rs.getString(2));
                    myPlaceJSONObject.put("country", rs.getString(4));
                    jSONObject.put("myRankingPlace", myPlaceJSONObject);
                    
                    if(cnt > 999){
                        break;
                    }else{
                        finish = true;
                    }
                }
                
                cnt++;
            }
            jSONObject.put("rankingPlaces", jSONArray);
            jSONObject.put("error", 0);
            response.getWriter().write(jSONObject.toString());

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

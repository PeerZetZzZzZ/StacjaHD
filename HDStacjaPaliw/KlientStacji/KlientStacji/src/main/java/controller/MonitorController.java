/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MonitorMaster;
import model.ajax.Tank;
import model.ajax.TankData;
import model.elasticsearch.ElasticsearchMaster;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author PeerZet
 */
public class MonitorController extends HttpServlet {

    MonitorMaster mm = new MonitorMaster();
    TankData tankDelivery = new TankData();

    private boolean sbAlreadyConnected = false;

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MonitorController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MonitorController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String numerZbiornika = request.getParameter("zbiornik");
        System.out.println("req parametr: " + numerZbiornika);
        if (numerZbiornika == null) {
            if (!sbAlreadyConnected) {
                sbAlreadyConnected = true;
                HashMap<Integer, Tank> tanks = tankDelivery.getTanks();
                StringBuffer sb = new StringBuffer();
                for (Tank tank : tanks.values()) {
                    sb.append("<tank>");
                    sb.append("<id>" + tank.getId() + "</id>");
                    sb.append("<state>" + tank.getState().toString() + "</state>");
                    sb.append("</tank>");
                }
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                System.out.println(sb.toString());
                response.getWriter().write("" + sb);
                sbAlreadyConnected = false;
            }
        } else {
            //req parametr 1
            
        }
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

        String button = request.getParameter("button");
        ElasticsearchMaster master = new ElasticsearchMaster();
        master.searchAnomaly();
        if (button != null) {
            switch (button) {
                case "Process monitor":
                    response.sendRedirect("/KlientStacji/kibana/index.html");
                    break;
            }

        }
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

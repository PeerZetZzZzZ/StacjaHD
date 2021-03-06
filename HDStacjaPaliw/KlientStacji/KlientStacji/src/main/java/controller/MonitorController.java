/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MonitorMaster;
import model.ajax.Tank;
import model.ajax.TankData;

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
        if (!sbAlreadyConnected) {
            String pageCounter = request.getParameter("licznik");
            Integer numberOfPage = Integer.valueOf(pageCounter);
            sbAlreadyConnected = true;
            HashMap<Integer, Tank> tanks = tankDelivery.getTanks();
            StringBuffer sb = new StringBuffer();
            int counter = 0;
            int numberOfLeft = numberOfPage * 10;//maximum 10 results on page
            for (Tank tank : tanks.values()) {
                if (counter >= numberOfLeft) {//if the already leaved the results from 1st page
                    sb.append("<tank>");
                    sb.append("<id>" + tank.getId() + "</id>");
                    sb.append("<state>" + tank.getState().toString() + "</state>");
                    sb.append("</tank>");
                }
                counter++;
            }
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            System.out.println(sb.toString());
            response.getWriter().write("" + sb);
            sbAlreadyConnected = false;
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
        if (button != null) {
            switch (button) {
                case "Process monitor":
                    response.sendRedirect("/KlientStacji/kibana/index.html");
                    break;
                default:
                    if (button.equals("Stan1")) {
                        response.setContentType("text/html;charset=UTF-8");
                        Map<String, String> tankInfo = new HashMap();
                        String tankNumber = button.substring(4, button.length());
                        tankInfo = tankDelivery.getTankInfo(Integer.valueOf(tankNumber));

                        System.out.println(tankInfo.get("idZbiornika"));
                        System.out.println(tankInfo.get("stempelCzasowy"));
                        System.out.println(tankInfo.get("objetoscBrutto"));
                        System.out.println(tankInfo.get("objetoscNetto"));
                        System.out.println(tankInfo.get("temperatura"));
                        request.setAttribute("idZbiornika", tankInfo.get("idZbiornika")); // This will be available as ${message}
                        request.setAttribute("stempelCzasowy", tankInfo.get("stempelCzasowy")); // This will be available as ${message}
                        request.setAttribute("objetoscBrutto", tankInfo.get("objetoscBrutto")); // This will be available as ${message}
                        request.setAttribute("objetoscNetto", tankInfo.get("objetoscNetto")); // This will be available as ${message}
                        request.setAttribute("temperatura", tankInfo.get("temperatura")); // This will be available as ${message}
                        request.getRequestDispatcher("/monitor/zbiornik.jsp").forward(request, response);
                        break;
                    }
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

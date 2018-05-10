/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.ws.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "Dispatcher", value = {"/rwtext/*", "/icons/*", "/fileresource/*", "/bannerDir/*", "/explorer.html"})
public class Dispatcher extends HttpServlet {
    
    private static final long serialVersionUID = -4533674629124450518L;
    
    final static String WPS_SERVLET_FAILURE_CONTEXT_ATTRIBUTE = "org.radixware.ws.servlet.WPSFailure";

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
//            Enumeration<String> names = request.getHeaderNames();
//            while (names.hasMoreElements()) {
//                String name = names.nextElement();
//                System.out.println(name + ": " + request.getHeader(name));
//            }
//            String header = request.getHeader("Authorization");
//            if (header == null) {
//                response.setHeader("WWW-Authenticate", "Negotiate");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                writeWpsInitializationFailure("Authorization requred for this domain", response);
//                return;
//            } else {
//                if (header.startsWith("Basic")) {
//                    response.setHeader("WWW-Authenticate", "Negotiate");
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    writeWpsInitializationFailure("Authorization requred for this domain", response);
//                    return;
//                }
//            }
            final String initFailure = (String)getServletContext().getAttribute(WPS_SERVLET_FAILURE_CONTEXT_ATTRIBUTE);
            if (initFailure!=null){
                writeWpsInitializationFailure(initFailure, response);
                return;
            }
            String initResult = WPSBridge.wpsInit();
            if (initResult != null) {
                writeWpsInitializationFailure(initResult, response);
                return;
            }
        } catch (Throwable ex) {
            writeExceptionScreen(ex, response);
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        try {
            WPSBridge.processRequest(request, response);
        } catch (Throwable ex) {
            Logger.getLogger(Dispatcher.class.getName()).log(Level.SEVERE, "Exception on request processing", ex);
            writeExceptionScreen(ex, response);
        }
    }

    private String readTextResource(String resourseName) {
        InputStream stream = getClass().getResourceAsStream(resourseName);
        if (stream != null) {
            try {
                InputStreamReader reader;
                try {
                    reader = new InputStreamReader(stream, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    return null;
                }
                char chars[] = new char[1024];
                StringBuilder result = new StringBuilder();
                int count = -1;
                try {
                    while ((count = reader.read(chars)) >= 0) {
                        result.append(chars, 0, count);
                    }
                    return result.toString();
                } catch (IOException ex) {
                    return null;
                } finally {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        //
                    }
                }
            } finally {
                try {
                    stream.close();
                } catch (IOException ex) {
                    //
                }
            }
        } else {
            return null;
        }
    }

    private static String stringToXmlString(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.replace("<", "&lt;").replace("<", "&gt;");

    }

    private void writeWpsInitializationFailure(String message, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml;charset=UTF-8");
        String content = readTextResource("failure.html");

        if (content != null) {
            String[] lines = message.split("\n");
            StringBuilder result = new StringBuilder();
            for (String s : lines) {
                result.append("<tr><td>").append(stringToXmlString(s)).append("</td></tr>");
            }
            content = content.replace("%%%", result.toString());
        } else {
            content = "  <body>I can do nothing!!!! </body>";
        }
        PrintWriter out = response.getWriter();
        out.println("<failure>");
        out.println(content);
        out.println("</failure>");

    }

    private void writeExceptionScreen(Throwable exception, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml;charset=UTF-8");
        String content = readTextResource("failure.html");

        if (content != null) {
            StringBuilder result = new StringBuilder();
            result.append("<tr><td>").append(stringToXmlString(exception.getMessage())).append("</td></tr>");
            for (StackTraceElement s : exception.getStackTrace()) {
                result.append("<tr><td>").append(stringToXmlString(s.toString())).append("</td></tr>");
            }
            content = content.replace("%%%", result.toString());
        } else {
            content = "  <body>I can not do enything!!!! </body>";
        }
        PrintWriter out = response.getWriter();
        out.println("<failure>");
        out.println(content);
        out.println("</failure>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
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

    @Override
    public void destroy() {
        WPSBridge.shutdown();
        super.destroy();
    }

    @Override
    public void init(ServletConfig c) throws ServletException {
        try {
            super.init(c);
        } catch (ServletException e) {
            throw e;
        };
    }
}

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

import java.io.*;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class FileUploader extends HttpServlet {
    
    private static final long serialVersionUID = -5602266960933612937L;

    boolean uploadsEnabled = false;
    private final DiskFileItemFactory uploadFactory = new DiskFileItemFactory();
    private static long uploadingLimit;
    
    public static void setSizeLimit(final Integer limit){
        if (limit<0){
            uploadingLimit = Long.MAX_VALUE;
        }else{
            uploadingLimit = limit.longValue()*1024l*1024l;
        }
    }

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
        final String fileId = checkForFileUploads(request);
        if (fileId == null) {
            response.sendError(501);
        }else{
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try{
                out.print(fileId);
            }finally {
                out.close();
            }            
        }
    }

    private String checkForFileUploads(final HttpServletRequest request){
        final List<FileItem> items;
        try {
            if (!uploadsEnabled) {
                return null;
            }
            final ServletFileUpload upload = new ServletFileUpload(uploadFactory);
            items = upload.parseRequest(request);
        }catch(Exception exception){
            log("Error encountered while parsing the request", exception);
            return null;
        }
        for (FileItem item : items) {
            if (item.isFormField()) {
                continue;
            } else {
                final String srcId = item.getFieldName();
                final String fileName = item.getName();
                if (item.getSize()>uploadingLimit){
                    final String exceptionMessagePattern = 
                        "the request was rejected because its size (%1$s) exceeds the configured maximum (%2$s)";
                    final String exceptionMessage = 
                        String.format(exceptionMessagePattern, item.getSize(), uploadingLimit);
                    final FileUploadBase.FileSizeLimitExceededException exception =                             
                        new FileUploadBase.FileSizeLimitExceededException(exceptionMessage, item.getSize(), uploadingLimit);
                    exception.setFileName(fileName);
                    log("Failed to upload file \'"+fileName+"\':", exception);
                    UploadStore.notifyUploadFailed(srcId, exception);
                    return null;
                }                    
                final File file = UploadStore.createFile(UUID.randomUUID().toString());
                final InputStream in;                    
                try{
                    in = item.getInputStream();
                }catch(IOException exception){
                    log("Failed to upload file \'"+fileName+"\':", exception);
                    UploadStore.notifyUploadFailed(srcId, exception);
                    return null;
                }
                final FileOutputStream out;
                try{
                    out = new FileOutputStream(file);
                }catch(IOException exception){
                    log("Failed to upload file \'"+fileName+"\':", exception);
                    try{
                        in.close();
                    }catch(IOException e){                            
                    }                        
                    UploadStore.notifyUploadFailed(srcId, exception);
                    return null;
                }
                try {
                    copyStream(in, out);
                    UploadStore.notifyUploaded(srcId, file);
                    return srcId;
                } catch (IOException e) {
                    log("Failed to upload file \'"+fileName+"\':", e);
                    UploadStore.notifyUploadFailed(srcId, e);
                    return null;
                } catch(FileSizeLimitExceededException e){
                    e.setFileName(fileName);
                    log("Failed to upload file \'"+fileName+"\':", e);
                    UploadStore.notifyUploadFailed(srcId, e);
                    return null;                    
                }finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                    try {
                        out.flush();
                    } catch (IOException e) {
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }

            }
        }
        return null;
    }

    private static void copyStream(final InputStream streamFrom, final OutputStream streamTo) throws IOException, FileSizeLimitExceededException {
        final byte[] buffer = new byte[4096];
        int bytesRead;
        long totalBytesRead = 0;
        while ((bytesRead = streamFrom.read(buffer)) != -1) {
            streamTo.write(buffer, 0, bytesRead);
            totalBytesRead+=bytesRead;
            if (totalBytesRead>uploadingLimit){
                final String exceptionMessagePattern = 
                    "the request was rejected because its size (%1$s) exceeds the configured maximum (%2$s)";
                final String exceptionMessage = 
                    String.format(exceptionMessagePattern, totalBytesRead, uploadingLimit);
                final FileUploadBase.FileSizeLimitExceededException exception =                             
                    new FileUploadBase.FileSizeLimitExceededException(exceptionMessage, totalBytesRead, uploadingLimit);                
                throw exception;
            }
        }
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
            throws ServletException, IOException {
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

    @Override
    public void destroy() {
        UploadStore.dispose();
        super.destroy();
    }

    @Override
    public void init(ServletConfig c) throws ServletException {
        try {
            super.init(c);
            uploadsEnabled = UploadStore.init();
            uploadFactory.setRepository(UploadStore.getRepository());
            uploadFactory.setSizeThreshold(10 * 1024 * 1024);
        } catch (ServletException e) {
            throw e;
        };
    }
}

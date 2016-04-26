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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;


public class FileInput extends UIObject {

    public static abstract class FileRequestCallback {

        private void doneImpl(File file) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                failure(ex);
            }
            try {
                done(stream);
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException ex) {
                }

            }
        }

        public abstract void done(InputStream fileInputStream);

        public abstract void failure(Exception e);
    }

    public static class RequestWrapper {

        private final FileRequestCallback cb;
        private final FileInput context;
        private WpsEnvironment contextEnv;

        private RequestWrapper(FileRequestCallback cb, FileInput context) {
            this.cb = cb;
            this.context = context;
            this.contextEnv = (WpsEnvironment) context.getEnvironment();
        }

        public void upload(File file) {
            if (cb != null) {
                try {
                    cb.doneImpl(file);
                } catch (Throwable ex) {
                } finally {
                    contextEnv.disposeUpload(context.input.getId(), file.getName());
                }
            }
        }

        public void fail(Exception e) {

            if (cb != null) {
                try {
                    cb.failure(e);
                } catch (Throwable ex) {
                } finally {
                    ((WpsEnvironment) context.getEnvironment()).disposeUpload(context.input.getId(), context.uploadId);
                }
            }
        }
    }
    private final String uploadId;
    private String selectedFile = null;
    private Html input = new Html("input");
    //  private Html submit = new Html("button");

    public FileInput() {
        super(new Html("form"));

        //html.layout("$RWT.fileInput.layout");
        html.add(input);
        //    html.add(submit);
        uploadId = UUID.randomUUID().toString();


        //  submit.setCss("display", "none");
        //  submit.setAttr("user-role", "submit-button");
        //  submit.setAttr("type", "submit");

        input.setAttr("type", "file");
        input.setAttr("name", ((WpsEnvironment) getEnvironmentStatic()).getUploadURL(input.getId(), uploadId));
        input.setAttr("onchange", "$RWT.files.processFileInput");

        html.setAttr("action", "file-upload.html");
        html.setAttr("method", "POST");
        html.setAttr("enctype", "multipart/form-data");
    }

    public void openFile(FileRequestCallback callback) {
        requestUpload(new RequestWrapper(callback, this));
    }

    private void requestUpload(RequestWrapper wrapper) {
        ((WpsEnvironment) getEnvironment()).requestUpload(input.getId(), uploadId, wrapper);
        //input.setAttr("name", uploadId);
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("upload-started".equals(actionName)) {
            fireUploadStarted();
        } else if ("file-selected".equals(actionName)) {
            if (!Utils.equals(selectedFile, actionParam)) {
                selectedFile = actionParam;
                fireFileChange();
            }
        }
    }

    public boolean isFileSelected() {
        return selectedFile != null;
    }

    public String getSelectedFileName() {
        return selectedFile;
    }
    
    public void setAcceptedMimeType(String mimeType){
    if (input!=null){
        input.setAttr("accept", mimeType);}
    }

    public String getAcceptedMimeType()
    { 
        String mimeType = input.getAttr("accept");
        return mimeType;
    }

    
    private final List<FileListener> fileListeners = new LinkedList<FileListener>();

    public interface FileListener {

        public void fileSelected(String fileName);
    }

    private void fireFileChange() {
        synchronized (fileListeners) {
            for (FileListener l : fileListeners) {
                l.fileSelected(selectedFile);
            }
        }
    }

    public void addFileListener(FileListener listener) {
        synchronized (fileListeners) {
            if (!fileListeners.contains(listener)) {
                fileListeners.add(listener);
            }
        }
    }

    public void removeFileListener(FileListener listener) {
        synchronized (fileListeners) {

            fileListeners.remove(listener);
        }
    }

    private void fireUploadStarted() {
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        }
        if (input.getId().equals(id)) {
            return this;
        }
//        if (submit.id.equals(id)) {
//            return this;
//        }
        return null;
    }
}

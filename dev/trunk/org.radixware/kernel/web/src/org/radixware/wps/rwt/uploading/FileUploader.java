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

package org.radixware.wps.rwt.uploading;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;


public final class FileUploader {

    public static interface SelectFileListener {

        void fileSelected(final String fileName, final Long fileSize);
    }

    public static interface StartUploadListener {

        boolean beforeStartFileUpload(final String fileName, final Long fileSize);
    }

    public static interface UploadCompleteListener {

        void afterUploadingComplete(final String fileName, final long fileSize, final IUploadedDataReader reader);
    }
    private static final String CONTEXT_ID_ATTR_NAME = "contextId";
    private final UIObject contextObject;
    private final IClientEnvironment environment;
    private List<SelectFileListener> selectFileListeners;
    private List<StartUploadListener> startUploadListeners;
    private List<UploadCompleteListener> uploadCompleteListeners;
    private FileDescriptor selectedFile;
    private IUploadedDataReader reader;
    private boolean autoUpload;
    private Html input;

    public FileUploader(final IClientEnvironment environment,
            final UIObject contextObject,
            final IUploadedDataReader reader) {
        this.environment = environment;
        this.contextObject = contextObject;
        contextObject.getHtml().addClass("rwt-file-upload-holder");
        setUploadedDataReader(reader);
        autoUpload = reader != null;
        createFileInput();
    }

    public void setAcceptedMimeType(final String mimeType) {
        if (mimeType != null && !mimeType.isEmpty()) {
            input.setAttr("accept", mimeType);
        }
    }

    public String getAcceptedMimeType() {
        return input.getAttr("accept");
    }

    public void setMultipleSelect(boolean multiple) {
        if (multiple) {
            input.setAttr("multiple", "");
        } else {
            input.setAttr("multiple", null);
        }
    }

    public boolean getMultipleSelection() {
        if (input.getAttr("multiple") != null) {
            return true;
        }
        return false;
    }

    public void setStartUploadAutomatically(final boolean auto) {
        autoUpload = auto;
    }

    public boolean isUploadAutomaticallyStarted() {
        return autoUpload;
    }

    public void processFileSelectedAjaxAction(final String actionParam) {
        try {
            selectedFile = FileDescriptor.createInstance(this, actionParam);
        } catch (WrongFormatError error) {
            environment.getTracer().error(error);
            return;
        }
        notifyFileSelected(selectedFile.getFileName(), selectedFile.getFileSize());
        if (autoUpload && reader != null) {
            try {
                uploadAndReadSelectedFile(reader, false);
            } catch (IOException exception) {
                environment.processException(exception);
            }
        }
    }

    public void processUploadStartedAjaxAction() {
        contextObject.getHtml().remove(input);
        createFileInput();
    }

    public void processUploadRejectedAjaxAction() {
        contextObject.getHtml().remove(input);
        createFileInput();
    }

    public UploadHandler uploadAsync(final IUploadedDataReader reader) throws UploadException {
        if (beforeUpload()) {
            return ((WpsEnvironment) environment).uploadFile(selectedFile, reader);
        } else {
            return null;
        }
    }

    public boolean uploadAndReadSelectedFile(final IUploadedDataReader reader, final boolean cancellable) throws IOException {
        final IUploadedDataReader finalReader = reader == null ? this.reader : reader;
        final UploadHandler handler;
        try {
            if (finalReader == null) {
                throw new IllegalArgumentException("Reader must be defined.");
            }
            handler = uploadAsync(finalReader);
        } catch (UploadException exception) {
            final String title = environment.getMessageProvider().translate("ExplorerMessage", "Failed to Upload");
            environment.processException(title, exception);
            return false;
        }
        if (handler != null) {
            try {
                if (!handler.waitForReadyAndRead(finalReader, cancellable)) {
                    final String title =
                            environment.getMessageProvider().translate("ExplorerMessage", "Failed to Upload");
                    final String message =
                            environment.getMessageProvider().translate("ExplorerMessage", "File \'" + selectedFile.getFileName() + "\' was not uploaded");
                    environment.messageError(title, message);
                    return false;
                }
                notifyUploadComplete(selectedFile.getFileName(), handler.getFileSize(), finalReader);
            } catch (InterruptedException exception) {
                return false;
            } finally {
                selectedFile = null;
            }
            return true;
        }
        return false;
    }

    private boolean beforeUpload() {
        if (selectedFile == null) {
            throw new IllegalStateException("File was not selected");
        }
        return notifyStartUpload(selectedFile.getFileName(), selectedFile.getFileSize());
    }

    public boolean isFileSelected() {
        return selectedFile != null;
    }

    public String getSelectedFileName() {
        if (selectedFile == null) {
            throw new IllegalStateException("File was not selected");
        }
        return selectedFile.getFileName();
    }

    public Long getSelectedFileSize() {
        if (selectedFile == null) {
            throw new IllegalStateException("File was not selected");
        }
        return selectedFile.getFileSize();
    }

    public void setUploadedDataReader(final IUploadedDataReader reader) {
        this.reader = reader;
    }

    public IUploadedDataReader getUploadDataReader() {
        return reader;
    }

    public UIObject getContextObject() {
        return contextObject;
    }
    private boolean isEnabled = true;

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        input.setAttr("readonly", !isEnabled);
        if (isEnabled) {
            input.setAttr("disabled", null);
            input.removeClass("ui-state-disabled");
        } else {
            input.setAttr("disabled", true);
            input.addClass("ui-state-disabled");
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    private void createFileInput() {
        //.rwt-file-upload
        Html form = new Html("form");
        input = new Html("input");
        form.add(input);
        final String uploadUrl =
                ((WpsEnvironment) environment).getUploadURL(input.getId(), UUID.randomUUID().toString());
        input.setAttr("type", "file");
        input.setAttr("name", uploadUrl);
        input.addClass("rwt-file-upload");
        input.setAttr("onchange", "$RWT.files.processFileChoice");
        input.setAttr(CONTEXT_ID_ATTR_NAME, contextObject.getHtmlId());
        if ("-1".equals(contextObject.getHtml().getAttr("tabIndex"))) {
            input.setAttr("tabIndex", "-1");
        }
        contextObject.setFileUploader(this);
        contextObject.getHtml().add(input);

    }

    public void dispose() {
        contextObject.setFileUploader(null);
        contextObject.getHtml().remove(input);
        selectFileListeners = null;
        startUploadListeners = null;
        input = null;
        reader = null;
    }

    public void addSelectFileListener(final SelectFileListener listener) {
        if (listener != null) {
            if (selectFileListeners == null) {
                selectFileListeners = new LinkedList<>();
                selectFileListeners.add(listener);
            } else if (!selectFileListeners.contains(listener)) {
                selectFileListeners.add(listener);
            }
        }
    }

    public void removeSelectFileListener(final SelectFileListener listener) {
        if (selectFileListeners != null) {
            selectFileListeners.remove(listener);
        }
    }

    public void addStartUploadListener(final StartUploadListener listener) {
        if (listener != null) {
            if (startUploadListeners == null) {
                startUploadListeners = new LinkedList<>();
                startUploadListeners.add(listener);
            } else if (!startUploadListeners.contains(listener)) {
                startUploadListeners.add(listener);
            }
        }
    }

    public void removeStartUploadListener(final StartUploadListener listener) {
        if (startUploadListeners != null) {
            startUploadListeners.remove(listener);
        }
    }

    public void addUploadCompleteListener(final UploadCompleteListener listener) {
        if (listener != null) {
            if (uploadCompleteListeners == null) {
                uploadCompleteListeners = new LinkedList<>();
                uploadCompleteListeners.add(listener);
            } else if (!uploadCompleteListeners.contains(listener)) {
                uploadCompleteListeners.add(listener);
            }
        }
    }

    public void removeUploadCompleteListener(final UploadCompleteListener listener) {
        if (uploadCompleteListeners != null) {
            uploadCompleteListeners.remove(listener);
        }
    }

    private void notifyFileSelected(final String fileName, final Long fileSize) {
        if (selectFileListeners != null) {
            final List<SelectFileListener> listeners = new LinkedList<>(selectFileListeners);
            for (SelectFileListener listener : listeners) {
                listener.fileSelected(fileName, fileSize);
            }
        }
    }

    private boolean notifyStartUpload(final String fileName, final Long fileSize) {
        if (startUploadListeners != null) {
            final List<StartUploadListener> listeners = new LinkedList<>(startUploadListeners);
            for (StartUploadListener listener : listeners) {
                if (!listener.beforeStartFileUpload(fileName, fileSize)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void notifyUploadComplete(final String fileName, final long fileSize, final IUploadedDataReader reader) {
        if (uploadCompleteListeners != null) {
            final List<UploadCompleteListener> listeners = new LinkedList<>(uploadCompleteListeners);
            for (UploadCompleteListener listener : listeners) {
                listener.afterUploadingComplete(fileName, fileSize, reader);
            }
        }
    }
}
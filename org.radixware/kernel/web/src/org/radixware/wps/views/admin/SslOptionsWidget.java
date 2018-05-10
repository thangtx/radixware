/*
 * Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.views.admin;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.eas.connections.ConnectionOptions.SslOptions;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.wps.SslOptionsImpl;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.views.editors.valeditors.ValBoolEditorController;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class SslOptionsWidget extends GroupWidget {

    private final ValBoolEditorController useSslAuth;
    private final ValStrEditorController trustStoreFilePath;
    private final ValBoolEditorController trustStorePathIsRelative;
    private final ValStrEditorController trustStorePassword;
    private final List<SslOptionsChangedListener> sslOptionsChangedListenerList = new LinkedList<>();
    private boolean isChangedByUser = true;
    
    public static abstract class SslOptionsChangedListener {

        public abstract void onSslOptionsChanged(SslOptions sslOptions);
    }

    public SslOptionsWidget(String title, WpsEnvironment env) {
        super(title);
        useSslAuth = new ValBoolEditorController(env);
        trustStoreFilePath = new ValStrEditorController(env);
        trustStorePathIsRelative = new ValBoolEditorController(env);
        trustStorePassword = new ValStrEditorController(env);
        setupUI();
    }

    void setSslOptions(SslOptions options) {
        isChangedByUser = false;
        if (options != null) {
            useSslAuth.setValue(options.useSSLAuth());
            trustStoreFilePath.setValue(options.getTrustStoreFilePath());
            trustStorePathIsRelative.setValue(options.isTrustStorePathRelative());
            char[] pwd = options.getTrustStorePassword();
            trustStorePassword.setValue(pwd == null ? null : String.valueOf(pwd));
        } else {
            useSslAuth.setValue(Boolean.FALSE);
            trustStoreFilePath.setValue(null);
            trustStorePathIsRelative.setValue(Boolean.FALSE);
            trustStorePassword.setValue(null);
        }
        isChangedByUser = true;
    }
    
    
    
    public void addSslOptionsChangedListener(SslOptionsChangedListener listener) {
        sslOptionsChangedListenerList.add(listener);
    }
    
    private void setupUI() {
        setSslOptions(null);
        useSslAuth.setMandatory(true);
        trustStorePathIsRelative.setMandatory(true);
        MessageProvider mp = getEnvironment().getMessageProvider();
        addNewRow(mp.translate("AdminPanel", "Use SSL Authentication:"), useSslAuth);
        addNewRow(mp.translate("AdminPanel", "Truststore FilePath"), trustStoreFilePath);
        addNewRow(mp.translate("AdminPanel", "Truststore path is relative:"), trustStorePathIsRelative);
        addNewRow(mp.translate("AdminPanel", "Truststore password:"), trustStorePassword);
        
        useSslAuth.addValueChangeListener(new ValueEditor.ValueChangeListener<Boolean>() {
            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                if (isChangedByUser) {
                    for (SslOptionsChangedListener sslOptionsListener : sslOptionsChangedListenerList) {
                        sslOptionsListener.onSslOptionsChanged(getSslOptions());
                    }
                }
            }
        });
        
        trustStoreFilePath.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (isChangedByUser) {
                    for (SslOptionsChangedListener sslOptionsListener : sslOptionsChangedListenerList) {
                        sslOptionsListener.onSslOptionsChanged(getSslOptions());
                    }
                }
            }
        });
        
        trustStorePathIsRelative.addValueChangeListener(new ValueEditor.ValueChangeListener<Boolean>() {

            @Override
            public void onValueChanged(Boolean oldValue, Boolean newValue) {
                if (isChangedByUser) {
                    for (SslOptionsChangedListener sslOptionsListener : sslOptionsChangedListenerList) {
                        sslOptionsListener.onSslOptionsChanged(getSslOptions());
                    }
                }
            }
        });
        
        trustStorePassword.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                if (isChangedByUser) {
                    for (SslOptionsChangedListener sslOptionsListener : sslOptionsChangedListenerList) {
                        sslOptionsListener.onSslOptionsChanged(getSslOptions());
                    }
                }
            }
        });
    }
    
    public SslOptionsImpl getSslOptions() {
        String password = trustStorePassword.getValue();
        return new SslOptionsImpl(trustStoreFilePath.getValue(), trustStorePathIsRelative.getValue(), password == null ? null : password.toCharArray(), useSslAuth.getValue());
    } 
    
}

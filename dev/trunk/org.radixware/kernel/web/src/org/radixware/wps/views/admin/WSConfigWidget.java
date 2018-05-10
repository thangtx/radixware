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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.starter.config.ConfigEntry;
import org.radixware.kernel.starter.config.ConfigFileAccessor;
import org.radixware.kernel.starter.config.ConfigFileParseException;
import org.radixware.wps.WebServerRunParams;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractContainer;
import org.radixware.wps.rwt.HorizontalBoxContainer;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;

public class WSConfigWidget extends AbstractContainer {

    private static final String SERVER_SECTION = "WebPresentationServer";
    
    private final MessageProvider mp;
    private final WebServerSettingsWidget webServerSettingsWidget;
    private final TraceSettingsWidget traceSettingsWidget;
    private final KerberosSettingsWidget kerberosSettingsWidget;
    private final CertificateSettingsWidget certificateSettingsWidget;
    private final FileUploadingSettingsWidget fileUploadingSettingsWidget;
    private final BannerSettingsWidget bannerSettingsWidget;
    private final AdminPanelSettingsWidget adminPanelSettingsWidget;
    private final List<ConfigWidget> settingsMap = new LinkedList<>();

    public WSConfigWidget(WpsEnvironment env) {
        super();
        mp = env.getMessageProvider();
        webServerSettingsWidget = new WebServerSettingsWidget(env, mp.translate("AdminPanel", "Web server settings"));
        traceSettingsWidget = new TraceSettingsWidget(env, mp.translate("AdminPanel", "Trace settings"));
        kerberosSettingsWidget = new KerberosSettingsWidget(env, mp.translate("AdminPanel", "Kerberos settings"));
        certificateSettingsWidget = new CertificateSettingsWidget(env, mp.translate("AdminPanel", "Certificate settings"));
        fileUploadingSettingsWidget = new FileUploadingSettingsWidget(env, mp.translate("AdminPanel", "File uploading settings"));
        bannerSettingsWidget = new BannerSettingsWidget(env, mp.translate("AdminPanel", "Banner settings"));
        adminPanelSettingsWidget = new AdminPanelSettingsWidget(env, mp.translate("AdminPanel", "Administrator panel settings"));
        settingsMap.add(webServerSettingsWidget);
        settingsMap.add(traceSettingsWidget);
        settingsMap.add(kerberosSettingsWidget);
        settingsMap.add(certificateSettingsWidget);
        settingsMap.add(fileUploadingSettingsWidget);
        settingsMap.add(bannerSettingsWidget);
        settingsMap.add(adminPanelSettingsWidget);
        setupUI();
    }

    private void setupUI() {
        this.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        VerticalBox mainVb = new VerticalBox();
        Div fileNameLbl = new Div();
        fileNameLbl.setInnerText(mp.translate("AdminPanel", "Config file: ") + WebServerRunParams.getConfigFile());
        AbstractContainer fileNameLblContainer = new AbstractContainer(fileNameLbl);
        fileNameLblContainer.getHtml().setCss("border-bottom", "1px solid grey");
        fileNameLblContainer.getHtml().setCss("text-align", "center");
        mainVb.add(fileNameLblContainer);

        HorizontalBoxContainer horizontalBox = new HorizontalBoxContainer();
        horizontalBox.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        VerticalBox vb = new VerticalBox();
        ValEditorControllerGrid groupBoxContainer = new ValEditorControllerGrid();
        horizontalBox.add(vb);
        horizontalBox.setAutoSize(vb, true);
        vb.getHtml().setCss("overflow-y", "auto");
        vb.add(groupBoxContainer);

        for (ConfigWidget widget : settingsMap) {
            groupBoxContainer.addGroupWidgetRow((GroupWidget) widget);
        }
        add(mainVb);

        VerticalBox bottomBox = new VerticalBox();
        bottomBox.setHeight(90);
        bottomBox.getHtml().setCss("top", "50%");
        bottomBox.getHtml().setCss("margin-top", "-50px");
        bottomBox.getHtml().setCss("position", "absolute");
        PushButton writeToFileBtn = new PushButton(mp.translate("AdminPanel", "Write"));
        writeToFileBtn.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.SAVE));
        writeToFileBtn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {

                try {
                    if (!webServerSettingsWidget.validate() || !kerberosSettingsWidget.validate()) { 
                        return;
                    }
                    ConfigFileAccessor accessor = ConfigFileAccessor.get(WebServerRunParams.getConfigFile(), SERVER_SECTION);
                    final List<ConfigEntry> toWrite = new ArrayList<>();
                    final List<String> toRemove = new ArrayList<>();
                    for (ConfigWidget widget : settingsMap) {
                        widget.write(toRemove, toWrite);
                    }
                    accessor.update(toRemove, toWrite);
                } catch (ConfigFileParseException ex) {
                    Logger.getLogger(WSConfigWidget.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        PushButton applySettingsBtn = new PushButton(mp.translate("AdminPanel", "Apply"));
        applySettingsBtn.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_OK));
        applySettingsBtn.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                if (!webServerSettingsWidget.validate() || !kerberosSettingsWidget.validate()) { 
                        return;
                    }
                for (ConfigWidget widget : settingsMap) {
                    widget.save();
                }
            }
        });

        PushButton readButton = new PushButton(mp.translate("AdminPanel", "Read"));
        readButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.UNDO));
        readButton.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                fillFromFile();
            }
        });
        
        PushButton loadButton = new PushButton(mp.translate("AdminPanel", "Load"));
        loadButton.setIcon(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.REFRESH));
        loadButton.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                loadFromWebServer();
            }
        });
        
        bottomBox.add(writeToFileBtn);
        bottomBox.addSpace(5);
        bottomBox.add(applySettingsBtn);
        bottomBox.addSpace(5);
        bottomBox.add(readButton);
        bottomBox.addSpace(5);
        bottomBox.add(loadButton);
        bottomBox.addSpace(5);
        AbstractContainer container = new AbstractContainer();
        container.setVSizePolicy(SizePolicy.EXPAND);
        container.add(bottomBox);
        horizontalBox.addSpace(5);
        horizontalBox.add(container);
        horizontalBox.addSpace(5);
        container.setWidth(100);
        mainVb.add(horizontalBox);
        fillFromFile();
    }

    private void fillFromFile() {
        for (ConfigWidget widget : settingsMap) {
            widget.reread();
        }
    }
    
    private void loadFromWebServer() {
        for (ConfigWidget widget : settingsMap) {
            widget.load();
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject obj = super.findObjectByHtmlId(id);
        if (obj != null) {
            return obj;
        } else {
            for (ConfigWidget widget : settingsMap) {
                obj = ((UIObject) widget).findObjectByHtmlId(id);
                if (obj != null) {
                    return obj;
                }
            }
        }
        return null;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        for (ConfigWidget widget : settingsMap) {
            ((UIObject)widget).visit(visitor);
        }
    }

}

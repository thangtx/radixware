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

package org.radixware.kernel.reporteditor.editors;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.windows.TopComponent;
import static org.openide.windows.TopComponent.PERSISTENCE_NEVER;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;


public class UserReportEditor extends TopComponent {

    private final UserReport page;
    private final VersionsList versionsList = new VersionsList();
    private final DescriptionPanel descPanel;
    private boolean updating = false;
    private final DefinitionLinkEditPanel link;
    private Definition contextParam;

    public UserReportEditor(final UserReport page) {
        this.page = page;
        this.setLayout(new BorderLayout());
        final JPanel optionsPanel = new JPanel(new BorderLayout());
        add(optionsPanel, BorderLayout.NORTH);
        this.add(versionsList, BorderLayout.CENTER);
        descPanel = new DescriptionPanel(page);
        optionsPanel.add(descPanel, BorderLayout.NORTH);

        final JPanel linkEditPanel = new JPanel();
        optionsPanel.add(linkEditPanel, BorderLayout.CENTER);
        final JLabel label = new JLabel("Context parameter type: ");
        optionsPanel.add(label, BorderLayout.WEST);
        link = new DefinitionLinkEditPanel();
        optionsPanel.add(link, BorderLayout.CENTER);
        
        updateContextParamLink();
        
        link.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                final Definition def = link.getDefinition();
                if (def instanceof AdsEntityObjectClassDef) {
                    page.setContextParamType(AdsTypeDeclaration.Factory.newParentRef((AdsEntityObjectClassDef) def));
                } else {
                    page.setContextParamType(null);
                }
            }
        });


        versionsList.open(page);

        page.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("alive".equals(evt.getPropertyName())) {
                    close();
                }
            }
        });
    }
   
    @Override
    public String getHtmlDisplayName() {
        return page.getName();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();

        try {
            updating = true;
            versionsList.update();
            descPanel.update();
            updateContextParamLink();
        } finally {
            updating = false;
        }

    }
    
    
    private void updateContextParamLink() {
        Definition def = null;
        if (page.getContextParamType() != null) {
            final AdsType type = page.getContextParamType().resolve(page.findModule()).get();
            if (type instanceof AdsDefinitionType) {
                def = ((AdsDefinitionType) type).getSource();
            }
        }
        if (def != null && def != contextParam) {
            contextParam = def;
            link.open(ChooseDefinitionCfg.Factory.newInstance(page.findModule(), AdsVisitorProviders.newEntityObjectTypeProvider(null)), contextParam, contextParam == null ? null : contextParam.getId());
        }
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }
}

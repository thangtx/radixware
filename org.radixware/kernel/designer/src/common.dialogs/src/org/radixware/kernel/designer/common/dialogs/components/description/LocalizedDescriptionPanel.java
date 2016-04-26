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

package org.radixware.kernel.designer.common.dialogs.components.description;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizingStringContext;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringContextFactory;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.EEditorMode;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringEditor.Options;


final class LocalizedDescriptionPanel extends LocalizingStringEditor implements IDescriptionEditor {
    private String title;
    private boolean inherit = false;
    
    public LocalizedDescriptionPanel() {
        this("Description");
    }

    public LocalizedDescriptionPanel(String title) {
        super(new Options().add(Options.COLLAPSABLE_KEY, true)
                .add(Options.TITLE_KEY, title)
                .add(Options.MODE_KEY, EEditorMode.MULTILINE));
        chkInherit = new JCheckBox("Inherit description");
        this.title = title;
    }

    @Override
    public void open(IDescriptionHandleInfo handleInfo) {
        open((ILocalizingStringContext) handleInfo);
        if (inherit){
            initComponent();
        }
    }

    @Override
    public void update() {
        update(getLocalizingStringContext());
        if (inherit){
            initComponent();
        }
    }

    @Override
    public Map<EIsoLanguage, String> getDescription() {
        return getLocalizingStringContext().getValueMap();
    }

    private ILocalizingStringContext getHandleInfo(final ILocalizedDescribable definition, Map<EIsoLanguage, String> values) {
        final HandleInfo handleInfo = new HandleInfo() {
            @Override
            public Definition getAdsDefinition() {
                return definition.getDescriptionLocation();
            }

            @Override
            public Id getTitleId() {
                return definition.getDescriptionId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                definition.setDescriptionId(multilingualStringDef != null ? multilingualStringDef.getId() : null);
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                definition.setDescription(language, newStringValue);
            }

            @Override
            public ELocalizedStringKind getLocalizedStringKind() {
                return ELocalizedStringKind.DESCRIPTION;
            }
        };

        return LocalizingStringContextFactory.newProxyInstance(handleInfo, values);
    }

    private ILocalizingStringContext getHandleInfo(final ILocalizedDescribable definition, boolean proxy) {
        final HandleInfo handleInfo = new HandleInfo() {
            @Override
            public Definition getAdsDefinition() {
                return definition.getDescriptionLocation();
            }

            @Override
            public Id getTitleId() {
                return definition.getDescriptionId();
            }

            @Override
            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                definition.setDescriptionId(multilingualStringDef != null ? multilingualStringDef.getId() : null);
            }

            @Override
            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                definition.setDescription(language, newStringValue);
            }

            @Override
            public ELocalizedStringKind getLocalizedStringKind() {
                return ELocalizedStringKind.DESCRIPTION;
            }
        };

        return proxy ? LocalizingStringContextFactory.newProxyInstance(handleInfo)
                : LocalizingStringContextFactory.newInstance(handleInfo);
    }
    
    private final JCheckBox chkInherit;
    
    public boolean isInhereted(){
        return chkInherit.isSelected();
    }
    
    private void initComponent() {
        if (getEditor() != null){
            final JPanel editorComponent = getEditor().getComponent();
            if (editorComponent instanceof BorderedCollapsablePanel) {
                final BorderedCollapsablePanel cp = (BorderedCollapsablePanel) editorComponent;
                ((BorderedCollapsablePanel.TopComponent) cp.getTopComponent()).addComponent(chkInherit, 0);
                ((BorderedCollapsablePanel.TopComponent) cp.getTopComponent()).addComponent(Box.createRigidArea(new Dimension(5, 0)),1);
                ((BorderedCollapsablePanel.TopComponent) cp.getTopComponent()).setTitle("Define");
            }
        }
    }
    
    @Override
    public void initComponentsForInheriting(ItemListener listener) {
        inherit = true;
        chkInherit.addItemListener(listener);
        chkInherit.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (getEditor() != null){
                    final JPanel editorComponent = getEditor().getComponent();
                    if (editorComponent instanceof BorderedCollapsablePanel) {
                        final BorderedCollapsablePanel cp = (BorderedCollapsablePanel) editorComponent;
                        if (chkInherit.isSelected()){
                            ((BorderedCollapsablePanel.TopComponent) cp.getTopComponent()).setEnabled(false);
                        } else {
                            ((BorderedCollapsablePanel.TopComponent) cp.getTopComponent()).setEnabled(true);
                        }
                    }
                }
                
            }
        });
    }

    @Override
    public void setInherit(boolean inherit) {
        chkInherit.setSelected(inherit);
    }

}

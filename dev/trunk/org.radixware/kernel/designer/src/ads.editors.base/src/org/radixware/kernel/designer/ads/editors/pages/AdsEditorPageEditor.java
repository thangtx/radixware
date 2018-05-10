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
package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsProperiesGroupDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IPagePropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPageEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.*;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty.AnchorProperty.Anchor;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.ESizePolicy;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;

public class AdsEditorPageEditor extends RadixObjectEditor<AdsEditorPageDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsEditorPageDef> {

        @Override
        public IRadixObjectEditor<AdsEditorPageDef> newInstance(AdsEditorPageDef editorPage) {
            return new AdsEditorPageEditor(editorPage);
        }
    }
    private boolean isUpdating;
    private JRadioButton lastSelectedRadioButton = null;

    /**
     * Creates new form AdsEditorPageEditorView
     */ 
    protected AdsEditorPageEditor(AdsEditorPageDef editorPage) {
        super(editorPage);
        initComponents();
        if (editorPage.getOwnerClass() instanceof AdsUserReportClassDef) {
            jPanel1.setVisible(false);
        }
        setupButtonsListeners();
    }

    private void setupButtonsListeners() {
        containerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUpdating) {
                    return;
                }

                if (((AbstractButton) e.getSource()).isSelected()) {
                    final AdsEditorPageDef adsEditorPageDef = getEditorPage();
                    if (adsEditorPageDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                        if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove custom editor page?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            //delete custom page view
                            adsEditorPageDef.getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.EXPLORER, false);
                            adsEditorPageDef.setType(EEditorPageType.CONTAINER);
                            lastSelectedRadioButton = containerButton;
                        } else {
                            //no. make selected previous selected button
                            assert (lastSelectedRadioButton != null);
                            lastSelectedRadioButton.setSelected(true);
                        }
                    } else {
                        adsEditorPageDef.setType(EEditorPageType.CONTAINER);
                        lastSelectedRadioButton = containerButton;
                    }
                }

                updatePageType();
                revalidate();
                repaint();
            }
        });
        customButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUpdating) {
                    return;
                }

                final boolean isSelected = customButton.isSelected();

                if (isSelected) {
                    AdsEditorPageDef adsEditorPageDef = getEditorPage();

//                    if (lastSelectedRadioButton.equals(standardButton)){
//                        //adsEditorPageDef.setColumnCount(1);
//                    }
                    if (!adsEditorPageDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                        //there's no custom editor page; create one
                        AdsCustomPageEditorDef.Factory.newInstance(adsEditorPageDef);
                        adsEditorPageDef.setType(EEditorPageType.CUSTOM);
//                        lastSelectedRadioButton = customButton;
                    } else {
                        adsEditorPageDef.setType(EEditorPageType.CUSTOM);
                        //set custom editor page
//                        lastSelectedRadioButton = customButton;
                    }
                    if (lastSelectedRadioButton == standardButton && DialogUtils.messageConfirmation(NbBundle.getMessage(AdsEditorPageEditor.class, "AdsEditorPageEditor.confirmation.text"))) {
                        prepareExplorerCustomView();
                        prepareWebCustomView();
                    }
                    //set custom editor page
                    lastSelectedRadioButton = customButton;
                }
                updatePageType();
                revalidate();
                repaint();
            }
        });
        standardButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isUpdating) {
                    return;
                }

                boolean isSelected = standardButton.isSelected();

                if (isSelected) {
                    final AdsEditorPageDef adsEditorPageDef = getEditorPage();
                    if (adsEditorPageDef.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                        if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure to remove custom editor page?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                            //delete custom page view
                            adsEditorPageDef.getCustomViewSupport().setUseCustomView(ERuntimeEnvironmentType.EXPLORER, false);
                            adsEditorPageDef.setType(EEditorPageType.STANDARD);
                            lastSelectedRadioButton = standardButton;
                        } else {
                            //no. make selected previous selected button
                            assert (lastSelectedRadioButton != null);
                            lastSelectedRadioButton.setSelected(true);
                        }
                    } else {
                        adsEditorPageDef.setType(EEditorPageType.STANDARD);
                        lastSelectedRadioButton = standardButton;
                    }
                }

                updatePageType();
                revalidate();
                repaint();

            }
        });
    }

    private void updatePageType() {
        final AdsEditorPageDef adsEditorPageDef = getEditorPage();

        final EEditorPageType type = adsEditorPageDef.getType();
        final boolean isFormHandlerClassDef = adsEditorPageDef.getOwnerClass() instanceof AdsFormHandlerClassDef;
        containerButton.setEnabled(!isFormHandlerClassDef);

        if (isUpdating) {
            if (!isFormHandlerClassDef) {
                containerButton.setSelected(type == EEditorPageType.CONTAINER);
            }
            customButton.setSelected(type == EEditorPageType.CUSTOM);
            standardButton.setSelected(type == EEditorPageType.STANDARD);
        }
        final Enumeration<AbstractButton> allRadioButtons = buttonGroup1.getElements();
        while (allRadioButtons.hasMoreElements()) {
            JRadioButton temp = (JRadioButton) allRadioButtons.nextElement();
            if (temp.isSelected()) {
                lastSelectedRadioButton = temp;
                break;
            }
        }

        if (type.equals(EEditorPageType.CONTAINER)) {
            embeddedPanel.open(adsEditorPageDef);
            ((CardLayout) content.getLayout()).show(content, "card2");
        } else {
            standartPanel.update();
            ((CardLayout) content.getLayout()).show(content, "card3");
        }
        revalidate();
        repaint();
    }

    @Override
    public void update() {
        final AdsEditorPageDef adsEditorPageDef = getEditorPage();

        isUpdating = true;
        accessEditor.open(adsEditorPageDef);
        updateOverState();
        descriptionPanel1.update();
        envSelectorPanel1.update();

        updatePageType();
        setReadonly(adsEditorPageDef.isReadOnly());
        isUpdating = false;
    }

    public AdsEditorPageDef getEditorPage() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsEditorPageDef adsEditorPageDef = getEditorPage();

        descriptionPanel1.open(adsEditorPageDef);

        embeddedPanel.open(adsEditorPageDef);
        standartPanel.open(adsEditorPageDef);
        envSelectorPanel1.open(adsEditorPageDef);

        update();

        return super.open(info);
    }

    private void updateOverState() {
        AdsEditorPageDef overwritten = (AdsEditorPageDef) getEditorPage().getHierarchy().findOverwritten().get();
        AdsEditorPageDef overridden = (AdsEditorPageDef) getEditorPage().getHierarchy().findOverridden().get();
        boolean hasOverwritten = overwritten != null;
        boolean hasOverridden = overridden != null;
        if (hasOverwritten) {
            ovrLabel.setEnabled(true);
            ovrEditor.setEnabled(true);
            ovrEditor.open(overwritten, overwritten.getId());
        } else {
            ovrLabel.setEnabled(false);
            ovrEditor.setEnabled(false);
        }
        if (hasOverridden) {
            overridenLabel.setEnabled(true);
            overriddenEditor.setEnabled(true);
            overriddenEditor.open(overridden, overridden.getId());
        } else {
            overridenLabel.setEnabled(false);
            overriddenEditor.setEnabled(false);
        }
    }

    public void setReadonly(boolean readonly) {
        descriptionPanel1.setReadonly(readonly);
        containerButton.setEnabled(!readonly);
        standardButton.setEnabled(!readonly);
        customButton.setEnabled(!readonly);
    }

    private void prepareWebCustomView() {
        AdsEditorPageDef adsEditorPageDef = getEditorPage();
        AdsAbstractUIDef uiDef = adsEditorPageDef.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB);
        AdsRwtWidgetDef widget = (AdsRwtWidgetDef) uiDef.getWidget();
        widget.getProperties().add(new RectProperty("geometry", 0, 0, 600, 400));

        prepareWebCustomViewWidgets(adsEditorPageDef.getProperties(), widget);
        
    }
    
    //TODO 
    public void prepareWebCustomViewWidgets(IPagePropertyGroup group, AdsRwtWidgetDef widget) {
        AdsRwtWidgetDef layout = new AdsRwtWidgetDef(AdsMetaInfo.RWT_PROPERTIES_GRID);
        AdsRwtWidgetDef gridLayout = new AdsRwtWidgetDef(AdsMetaInfo.RWT_LABELED_EDIT_GRID);
        prepareWebLayout(layout);
        prepareWebLayout(gridLayout);
        for (AdsEditorPageDef.PagePropertyRef item : group.list()) {
            AdsDefinition itemDef = item.findItem();
            if (item.getId() != null) {
                AdsRwtWidgetDef editor = new AdsRwtWidgetDef(AdsMetaInfo.RWT_PROP_EDITOR);
                editor.setName(itemDef.getName() + "Widget");
                editor.setWeight(0.0);
                editor.getProperties().add(new IntProperty("gridColumn", item.getColumn()));
                editor.getProperties().add(new IntProperty("gridRow", item.getRow()));
                editor.getProperties().add(new IntProperty("colSpan", item.getColumnSpan()));
                editor.getProperties().add(new PropertyRefProperty("property", item.getId().toString()));
                editor.getProperties().add(new StringProperty("objectName", itemDef.getName() + "Widget"));
                editor.getProperties().add(new RectProperty("geometry", 0, 0, 100, 20));
                layout.getWidgets().add(editor);
            } else if (item.getGroupDef() != null) {
                if (!layout.getWidgets().isEmpty()){
                    gridLayout.getWidgets().add(layout);
                    layout = new AdsRwtWidgetDef(AdsMetaInfo.RWT_PROPERTIES_GRID);
                    prepareWebLayout(layout);
                }
                
                AdsProperiesGroupDef g = item.getGroupDef();
                AdsRwtWidgetDef w = new AdsRwtWidgetDef(AdsMetaInfo.RWT_UI_GROUP_BOX);
                w.setName(itemDef.getName() + "Widget");
                w.setWeight(0.0);
                w.getProperties().add(new IntProperty("gridColumn", item.getColumn()));
                w.getProperties().add(new IntProperty("gridRow", item.getRow()));
                w.getProperties().add(new IntProperty("colSpan", item.getColumnSpan()));
                w.getProperties().add(new StringProperty("objectName", itemDef.getName() + "Widget"));
                if (g.getTitleId() != null){
                    w.getProperties().add(new LocalizedStringRefProperty("title", g.getTitleId().toString()));
                }
                prepareWebCustomViewWidgets(g, w);
                w.getProperties().add(new RectProperty("geometry", 0, 0, 200, 20));
                w.getProperties().add(new BooleanProperty("flat", !g.isShowFrame()));
                
                gridLayout.getWidgets().add(w);
            }
        }
        if (gridLayout.getWidgets().isEmpty()){
            widget.getWidgets().add(layout);
        } else {
            if (!layout.getWidgets().isEmpty()){
                gridLayout.getWidgets().add(layout);
            }
            widget.getWidgets().add(gridLayout);
        }
    }
    
    private void prepareWebLayout(AdsRwtWidgetDef layout) {
        layout.setName("widget");
        layout.setWeight(0.0);
        AnchorProperty anch = new AnchorProperty("anchor");
        anch.setLeft(new Anchor(0, 0, null));
        anch.setTop(new Anchor(0, 0, null));
        anch.setRight(new Anchor(1, 0, null));
        anch.setBottom(new Anchor(1, 0, null));
        layout.getProperties().add(anch);
    }
    
    private void prepareExplorerCustomView() {
        AdsEditorPageDef adsEditorPageDef = getEditorPage();
        AdsAbstractUIDef uiDef = adsEditorPageDef.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER);
        AdsWidgetDef widget = (AdsWidgetDef) uiDef.getWidget();
        widget.getProperties().add(new RectProperty("geometry", 0, 0, 600, 400));

        prepareExplorerCustomViewWidgets(adsEditorPageDef.getProperties(), widget);
    }
    
    public void prepareExplorerCustomViewWidgets(IPagePropertyGroup group, AdsWidgetDef widget) {
        AdsLayout layout = new AdsLayout(AdsMetaInfo.GRID_LAYOUT_CLASS);
        widget.setLayout(layout);

        int maxRow = 0;//, maxCol = 0;
        for (AdsEditorPageDef.PagePropertyRef prop : group.list()) {
            maxRow = Math.max(maxRow, prop.getRow() + 1);
            //maxCol = Math.max(maxCol, prop.getColumn() + prop.getColumnSpan());
        }

        for (AdsEditorPageDef.PagePropertyRef item : group.list()) {
            AdsDefinition propDef = item.findItem();
            if (item.getId() != null) {
                maxRow = Math.max(maxRow, item.getRow() + 1);
                AdsWidgetDef label = new AdsWidgetDef(AdsMetaInfo.PROP_LABEL_CLASS);
                label.setName(propDef.getName() + "PropLabel");
                label.setWeight(0.0);

                label.getProperties().add(new PropertyRefProperty("property", item.getId().toString()));
                label.getProperties().add(new StringProperty("objectName", propDef.getName() + "PropLabel"));
                label.getProperties().add(new SizePolicyProperty("sizePolicy", ESizePolicy.Maximum, ESizePolicy.Fixed));
                label.getProperties().add(new RectProperty("geometry", 0, 0, 100, 20));
                layout.getItems().add(new AdsLayout.WidgetItem(label, item.getRow(), item.getColumn() * 2, 1, 1));

                AdsWidgetDef editor = new AdsWidgetDef(AdsMetaInfo.PROP_EDITOR_CLASS);
                editor.setName(propDef.getName() + "PropEditor");
                editor.setWeight(0.0);
                editor.getProperties().add(new PropertyRefProperty("property", item.getId().toString()));
                editor.getProperties().add(new StringProperty("objectName", propDef.getName() + "PropEditor"));
                editor.getProperties().add(new SizePolicyProperty("sizePolicy", ESizePolicy.Minimum, ESizePolicy.Fixed));
                editor.getProperties().add(new RectProperty("geometry", 0, 0, 100, 20));
                layout.getItems().add(new AdsLayout.WidgetItem(editor, item.getRow(), item.getColumn() * 2 + 1, 1, 2 * item.getColumnSpan() - 1));
            } else if (item.getGroupDef() != null){
                AdsProperiesGroupDef g = item.getGroupDef();
                AdsWidgetDef w = new AdsWidgetDef(AdsMetaInfo.GROUP_BOX_CLASS);
                w.getProperties().add(new StringProperty("objectName", propDef.getName() + "GroupBox"));
                w.getProperties().add(new SizePolicyProperty("sizePolicy", ESizePolicy.Minimum, ESizePolicy.Fixed));
                w.getProperties().add(new RectProperty("geometry", 0, 0, 200, 20));
                if (g.getTitleId() != null){
                    w.getProperties().add(new LocalizedStringRefProperty("title", g.getTitleId().toString()));
                }
                w.getProperties().add(new BooleanProperty("flat", !g.isShowFrame()));
                layout.getItems().add(new AdsLayout.WidgetItem(w, item.getRow(), item.getColumn() * 2, 1, 2));
                prepareExplorerCustomViewWidgets(g, w);
            }

            /*
             if (prop.getColumn() + prop.getColumnSpan() >= maxCol)
             continue;
            
             AdsLayout.SpacerItem spacer = new AdsLayout.SpacerItem(prop.getRow(), prop.getColumn()*3+2, 1, 1);
             spacer.getProperties().add(new EnumValueProperty("orientation", EOrientation.Horizontal.name()));
             spacer.getProperties().add(new EnumValueProperty("sizeType", ESizePolicy.Fixed.name()));
             spacer.getProperties().add(new SizeProperty("sizeHint", 10, 20));
             layout.getItems().add(spacer);
             */
        }
        if (maxRow > 0 && group == getEditorPage().getProperties()) {
            AdsLayout.SpacerItem spacer = new AdsLayout.SpacerItem(maxRow, 0, 1, 1);
            spacer.getProperties().add(new EnumValueProperty("orientation", EOrientation.Vertical.name()));
            spacer.getProperties().add(new EnumValueProperty("sizeType", ESizePolicy.Expanding.name()));
            layout.getItems().add(spacer);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        containerButton = new javax.swing.JRadioButton();
        customButton = new javax.swing.JRadioButton();
        standardButton = new javax.swing.JRadioButton();
        descriptionPanel1 = new org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel();
        content = new javax.swing.JPanel();
        embeddedPanel = new org.radixware.kernel.designer.ads.editors.pages.EmbeddedExplorereItemPanel();
        standartPanel = new org.radixware.kernel.designer.ads.editors.pages.StandartAndCustomPageProperties();
        ovrLabel = new javax.swing.JLabel();
        ovrEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        overridenLabel = new javax.swing.JLabel();
        overriddenEditor = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        jLabel1 = new javax.swing.JLabel();
        accessEditor = new org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel();
        envSelectorPanel1 = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "AdsEditorPageEditor.jPanel1.border.title"))); // NOI18N

        buttonGroup1.add(containerButton);
        containerButton.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "AdsEditorPageEditor.containerButton.text")); // NOI18N

        buttonGroup1.add(customButton);
        customButton.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "AdsEditorPageEditor.customButton.text")); // NOI18N

        buttonGroup1.add(standardButton);
        standardButton.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "AdsEditorPageEditor.standardButton.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(containerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(standardButton)
                .addGap(136, 136, 136))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(containerButton)
                    .addComponent(customButton)
                    .addComponent(standardButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        content.setLayout(new java.awt.CardLayout());
        content.add(embeddedPanel, "card2");
        content.add(standartPanel, "card3");

        ovrLabel.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "OverwrittenTip")); // NOI18N
        ovrLabel.setEnabled(false);

        ovrEditor.setEnabled(false);

        overridenLabel.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "OveriddenTip")); // NOI18N
        overridenLabel.setEnabled(false);

        overriddenEditor.setEnabled(false);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsEditorPageEditor.class, "AccessibilityTip")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(descriptionPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ovrLabel)
                    .addComponent(overridenLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(accessEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overriddenEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                    .addComponent(ovrEditor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(envSelectorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(accessEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ovrEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ovrLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(overridenLabel)
                    .addComponent(overriddenEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(envSelectorPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(content, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
        );

        JScrollPane scrollPane = new JScrollPane(jPanel3);
        add(scrollPane);/*

        add(jPanel3);
        */
    }// </editor-fold>//GEN-END:initComponents
//    private ChooseDefinitionCfg createCfg() {
//        return new ExplorerItemCfg(new AdsEditorPageLookupSupport().getAvailableEmbeddedxplorerItems(getEditorPage()));
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessEditPanel accessEditor;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton containerButton;
    private javax.swing.JPanel content;
    private javax.swing.JRadioButton customButton;
    private org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel descriptionPanel1;
    private org.radixware.kernel.designer.ads.editors.pages.EmbeddedExplorereItemPanel embeddedPanel;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelectorPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel overriddenEditor;
    private javax.swing.JLabel overridenLabel;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel ovrEditor;
    private javax.swing.JLabel ovrLabel;
    private javax.swing.JRadioButton standardButton;
    private org.radixware.kernel.designer.ads.editors.pages.StandartAndCustomPageProperties standartPanel;
    // End of variables declaration//GEN-END:variables
}

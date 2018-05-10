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

/*
 * jPanel29.java
 *
 * Created on Jun 11, 2010, 3:34:06 PM
 */
package org.radixware.kernel.designer.ads.editors.property;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyEditOptions;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.designer.ads.common.dialogs.DeprecatedPanel;
import org.radixware.kernel.designer.common.dialogs.choosetype.ChooseType;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel;
import org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditorModel;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;


class AdsPropertyEditorMainPage extends javax.swing.JPanel {

    private AdsPropertyDef prop = null;
    private boolean isMayModify = false;
    private boolean isReadOnly;
    private LocalizingEditorPanel localizingPanel = null;
    private ChangeListener typeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {

            if (!isMayModify || isReadOnly) {
                return;
            }


            org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel panel = null;
            if (!(prop instanceof AdsFieldPropertyDef)) {
                panel = typeEditPanel;
            }


            initValueEditor.setValue(AdsValAsStr.NULL_VALUE);
            prop.getValue().setType(panel.getCurrentType());
            AdsType t = panel.getCurrentType().resolve(prop).get();
            AdsEnumType et = null;
            if (t instanceof AdsEnumType) {
                et = (AdsEnumType) panel.getCurrentType().resolve(prop).get();
                //initValueEditPanel.setEnum(et.getSource(), null);
            }
            


            prop.getValue().setInitial((AdsValAsStr) null);

            if (prop instanceof IAdsPresentableProperty) {
                IAdsPresentableProperty ipProp = (IAdsPresentableProperty) prop;
                if (ipProp != null) {
                    ServerPresentationSupport presentationSupport = ipProp.getPresentationSupport();
                    if (presentationSupport != null) {
                        PropertyEditOptions currEditOptions = presentationSupport.getPresentation().getEditOptions();
                        currEditOptions.setEditMaskType(null);
                    }
                }
            }
            //   setStrInitVal();
            //mainPanel.refreshPropertyPresentationAndHintPanelsVisible();
            mainPanel.update();//refreshObjectPanel();
            update();
        }
    };
    private ChangeListener publishingPropertyRefListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (prop instanceof AdsParentPropertyDef) {
                parentRefsPanel.setVisible(updateParentRefs());
            }
        }
    };
    private ValueChangeListener initValueListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (!isMayModify || isReadOnly) {
                return;
            }
            //|| initValueEditPanel.getValue() == AdsValAsStr.NULL_VALUE && prop.getValue().getInitial() == AdsValAsStr.NULL_VALUE
            if (initValueEditor.getValue() == null && prop.getValue().getInitial() == null) {
                return;
            }
            if (prop.getValue() != null) {
                if (prop.getValue().getInitial() == null
                        || initValueEditor.getValue() == null
                        || !initValueEditor.getValue().equals(prop.getValue().getInitial())) {
                    prop.getValue().setInitial(initValueEditor.getValue());
                }
            }
        }
    };
    final private AdsPropertyEditorPanel mainPanel;
    private DbTypePanelWrapper dbTypePanel;
    ////////////////////////////////////////////////////////////////////////////
    private volatile JCheckBox isInvisibleForArteCb = null;
    private volatile boolean isInvisibleForArteCbUpdate = false;
    private final JCheckBox chbAbstract;
    private DeprecatedPanel deprecatedPanel;

//    public AdsPropertyEditorPanel getMainPanel() {
//        return mainPanel;
//    }
    public AdsPropertyEditorMainPage(AdsPropertyEditorPanel mainPanel) {
        initComponents();
        this.mainPanel = mainPanel;

        typeEditPanel.addChangeListener(typeListener);
        initValueEditor.addValueChangeListener(initValueListener);

        chbAbstract = accessPanel.addCheckBox("Abstract");
        chbAbstract.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prop.getAccessFlags().setAbstract(chbAbstract.isSelected());
                update();
            }
        });

        accessPanel.getChangeSupport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });

        setBackground(Color.MAGENTA);
        publishingPropertyRefPanel.setClearable(false);
        deprecatedPanel = new DeprecatedPanel(jchkDepricated);
        jPanel1.setLayout(new MigLayout());
        jPanel1.add(jchbIsOverride, "grow 0, shrink");
        jPanel1.add(deprecatedPanel, "grow 0, shrink");
        jPanel1.add(jchkStatic, "wrap");
        jPanel1.add(jchbIsOverride, "grow 0, shrink");
        jPanel1.add(jchkReadOnly, "grow 0, shrink");
        //adsPropertyEditorMainPanel.
    }

    private DescriptionPanel getDescriptionPanel() {
        return (DescriptionPanel) descriptionPanel;
    }

    private void updateIsInvisibleForArteCheckBox() {
        try {
            isInvisibleForArteCbUpdate = true;
            if (isInvisibleForArteCb == null) {
                isInvisibleForArteCb = accessPanel.addCheckBox("Pure SQL (invisible for ARTE)");
                isInvisibleForArteCb.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (isInvisibleForArteCbUpdate) {
                            return;
                        }
                        ((AdsExpressionPropertyDef) prop).setInvisibleForArte(isInvisibleForArteCb.isSelected());
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                mainPanel.update();
                            }
                        });
                    }
                });
            }
            isInvisibleForArteCb.setEnabled(!isReadOnly);
            isInvisibleForArteCb.setSelected(((AdsExpressionPropertyDef) prop).isInvisibleForArte());
        } finally {
            isInvisibleForArteCbUpdate = false;
        }

    }

    public void update() {
        isMayModify = false;
        AdsPropertyDef ownerProp = null;
        final AdsPropertyDef overwritten = prop.getHierarchy().findOverwritten().get();
        boolean isRealOverwriten = overwritten != null;
        if (isRealOverwriten) {
            ownerProp = overwritten;
            overwrittenDefinitionLinkEditPanel.open(overwritten, overwritten.getId());
        }
        overwrittenPanel.setVisible(isRealOverwriten);
        
        if (prop.canChangeClientEnvironment() ) {
            envSelectorPanel.setVisible(true);
            envSelectorPanel.open(prop);
        } else {
            envSelectorPanel.setVisible(false);
        }

        final AdsPropertyDef overridden = prop.getHierarchy().findOverridden().get();
        final boolean isRealOverriden = overridden != null;
        if (ownerProp == null) {
            ownerProp = overridden;
        }
        
        if (isRealOverriden){
            overriddenDefinitionLinkEditPanel.open(overridden, overridden.getId());
        }
        overriddenPanel.setVisible(isRealOverriden);
        
        boolean isRealOver = isRealOverriden || isRealOverwriten;

        final boolean isAbstract = prop.getAccessFlags().isAbstract();
        // setStrInitVal();
        boolean isjPanelInitValVisible;

        boolean isPropSuitableForInitValDefintion = (prop instanceof AdsServerSidePropertyDef)
                || (prop instanceof AdsDynamicPropertyDef)
                || ((prop instanceof AdsPropertyPresentationPropertyDef) && ((AdsPropertyPresentationPropertyDef) prop).isLocal());

        if (!isPropSuitableForInitValDefintion) {
            jPanelTypes.setVisible(false);
            panelInitVal.setVisible(false);
            isjPanelInitValVisible = false;
        } else {
            jPanelTypes.setVisible(true);
            panelInitVal.setVisible(!isAbstract);
            isjPanelInitValVisible = true;
        }
        if (prop.getNature() == EPropNature.EXPRESSION) {
            updateIsInvisibleForArteCheckBox();
        }

        final boolean inInterface = prop.getOwnerClass() instanceof AdsInterfaceClassDef;
        if (isAbstract) {
            if (inInterface) {
                accessPanel.open(prop, EAccess.PRIVATE, EAccess.DEFAULT, EAccess.PROTECTED);
            } else {
                accessPanel.open(prop, EAccess.PRIVATE);
            }
        } else {
            accessPanel.open(prop);
        }

        final boolean isStatic = prop.getAccessFlags().isStatic();
        jchkStatic.setVisible((prop instanceof AdsDynamicPropertyDef || prop instanceof AdsUserPropertyDef)
                && (!isAbstract || isStatic));

        if (jchkStatic.isVisible()) {
            if (prop instanceof AdsDynamicPropertyDef) {
                jchkStatic.setText("Property of class");
                jchkStatic.setSelected(isStatic);
                jchkStatic.setEnabled(!isReadOnly && prop.getNature() != EPropNature.EVENT_CODE);
                jchkStatic.setForeground(isAbstract || inInterface ? Color.RED : Color.BLACK);
            } else {
                jchkStatic.setForeground(Color.black);
                AdsUserPropertyDef uProp = (AdsUserPropertyDef) prop;
                jchkStatic.setText("Audit update");
                jchkStatic.setSelected(uProp.isAuditUpdate());
                boolean isMustDisable = true;


                AdsEntityBasedClassDef clazz = (AdsEntityBasedClassDef) uProp.getOwnerClass();
                DdsTableDef tbl = clazz.findTable(prop);
                if (tbl != null) {
                    boolean isTblAuditUpdateSet = tbl.getAuditInfo().isEnabledForUpdate();
                    if (isTblAuditUpdateSet) {
                        isMustDisable = false;
                    } else if (uProp.isAuditUpdate()) {
                        jchkStatic.setForeground(Color.red);
                        isMustDisable = false;
                    }
                }

                jchkStatic.setEnabled(!isReadOnly && !isMustDisable);

            }

        }

        chbAbstract.setSelected(isAbstract);
        chbAbstract.setEnabled(!inInterface && !isStatic && !isReadOnly);
        chbAbstract.setVisible(prop instanceof AdsDynamicPropertyDef);

        boolean isOverwriteEn = false;

        if (!isReadOnly
                && isRealOverwriten != prop.isOverwrite() //((prop.getHierarchy().findOverwritten() == null) == (prop.isOverwrite()))
                ) {
            isOverwriteEn = true;
        }
        jchbIsOverwrite.setEnabled(isOverwriteEn);
        jchbIsOverwrite.setSelected(prop.isOverwrite());

        if (isRealOverwriten != prop.isOverwrite()) {
            jchbIsOverwrite.setForeground(Color.red);
        } else {
            jchbIsOverwrite.setForeground(Color.black);
        }


        boolean isOverrideEn = false;


        if (!isReadOnly
                && (isRealOverriden != (prop.isOverride()))) {
            isOverrideEn = true;
        }

        jchbIsOverride.setEnabled(isOverrideEn);
        jchbIsOverride.setSelected(prop.isOverride());

        if (isRealOverriden != prop.isOverride()) {
            jchbIsOverride.setForeground(Color.red);
        } else {
            jchbIsOverride.setForeground(Color.black);
        }

        AdsTypeDeclaration base = null;
        if (!prop.isOverwrite()) {
            if (overridden != null) {
                base = overridden.getValue().getType();
            }
        }

        if (base != null) {
            final TypeEditorModel typeEditorModel = new TypeEditorModel(prop.getValue().getType(), new ChooseType.DefaultTypeFilter(prop, prop.getValue(), base));
            typeEditPanel.open(typeEditorModel);
        } else {
            typeEditPanel.open(prop.getValue().getType(), prop, prop.getValue());
        }

        typeEditPanel.isComplete();
        if (dbTypePanel != null) {
            dbTypePanel.update();
        }
        final boolean isEnabledOnlyTypePanel =
                base != null
                || !isRealOver
                || isRealOver && (prop.getValue().getType() == null
                || ownerProp.getValue().getType() == null
                || !ownerProp.getValue().getType().equals(prop.getValue().getType()));

        typeEditPanel.setReadonly(isReadOnly || !isEnabledOnlyTypePanel && prop.getNature() != EPropNature.EVENT_CODE);

        typeEditPanel.setVisible(prop.getNature() != EPropNature.EVENT_CODE);
        typeEditPanel.getParent().setVisible(typeEditPanel.isVisible());
        boolean isUseRadixDefaultValueEditPanel = !(prop instanceof AdsInnateRefPropertyDef);
        panelInitVal.setVisible(isUseRadixDefaultValueEditPanel && isjPanelInitValVisible && !prop.getAccessFlags().isAbstract());

//        typeEditPanel.setReadonly(prop.isOverride() || prop.isOverwrite());

        AdsType t = typeEditPanel.getCurrentType().resolve(prop).get();
        AdsEnumType et = null;
//        if (isUseRadixDefaultValueEditPanel && t instanceof AdsEnumType) {
//            et = (AdsEnumType) typeEditPanel.getCurrentType().resolve(prop);
//
//            initValueEditPanel.setEnum(et.getSource(), prop.getValue().getInitial());
//            //RadixDefaultValue.Factory.newValAsStr(prop.getValue().getInitial()));
//        } else {
//            if (isUseRadixDefaultValueEditPanel) {
//
//                //radixDefaultValueEditPanel.setValue(typeEditPanel.getCurrentType().getTypeId(), RadixDefaultValue.Factory.newValAsStr(prop.getValue().getInitial()));
//                initValueEditPanel.setValue(typeEditPanel.getCurrentType().getTypeId(), prop.getValue().getInitial());
//                jEditorPane1.setText(prop.getValue().getInitial() == null ? "NULL" : prop.getValue().getInitial().toString());
//            }
//        }

        initValueEditor.open(prop.getValue().getInitialValueController());
        initValueEditor.setEnabled(isUseRadixDefaultValueEditPanel && !isReadOnly);
        initValueEditor.setVisible(prop.getNature() != EPropNature.EVENT_CODE);
        jLabel1.setVisible(initValueEditor.isVisible());

        //---------------------------ReadOnly------------------------------
        jchkReadOnly.setSelected(prop.isConst());
        jchkReadOnly.setEnabled(prop.getNature() == EPropNature.DYNAMIC && !isReadOnly);

//        if (isRealOverriden) {
//            if (overridden.isConst() == prop.isConst()) {
//                jchkReadOnly.setEnabled(false);
//            } else {
//                jchkReadOnly.setEnabled(prop.getNature() == EPropNature.DYNAMIC && !isReadOnly);
//            }
//        }

        //---------------------------Depricated--------------------------
        deprecatedPanel.setAccessFlags(prop.getAccessFlags());
        deprecatedPanel.setDefaultListenr();
        deprecatedPanel.setDeprecatedSelected(prop.getAccessFlags().isDeprecated());
        deprecatedPanel.setDeprecatedEnabled(!prop.getNature().equals(EPropNature.FIELD) && !isReadOnly);
        deprecatedPanel.setExpirationReleaseEnabled(jchkDepricated.isEnabled());
        
        getDescriptionPanel().open(prop);
        getDescriptionPanel().setReadonly(isReadOnly);

        parentRefsPanel.setVisible(false);
        publishingPropertyRefPanel.removeChangeListener(publishingPropertyRefListener);
                
        if (prop instanceof AdsDetailColumnPropertyDef) {
            setViewAsAdsDetailColumnPropertyDef();
        } else if (prop instanceof AdsDetailRefPropertyDef) {
            setViewAsAdsDetailRefPropertyDef();
        } else if (prop instanceof AdsDynamicPropertyDef) {
            setViewAsAdsDynamicPropertyDef();
        } else if (prop instanceof AdsExpressionPropertyDef) {
            setViewAsAdsExpressionPropertyDef();
        } else if (prop instanceof AdsTransmittablePropertyDef) {
            setViewAsAdsTransmittablePropertyDef();
        } else if (prop instanceof AdsInnateColumnPropertyDef) {
            setViewAsAdsInnateColumnPropertyDef();
        } else if (prop instanceof AdsInnateRefPropertyDef) {
            setViewAsAdsInnateRefPropertyDef();
        } else if (prop instanceof AdsParentPropertyDef) {
            setViewAsAdsParentPropertyDef();
            parentRefsPanel.setVisible(updateParentRefs());
            publishingPropertyRefPanel.addChangeListener(publishingPropertyRefListener);
        } else if (prop instanceof AdsPropertyPresentationPropertyDef) {
            setViewAsAdsPropertyPresentationPropertyDef();
        } else if (prop instanceof AdsUserPropertyDef) {
            setViewAsAdsUserPropertyDef();
        } else {
            setInfoId();
        }

        final boolean isConst = prop.isConst();
        accessorsAccessPanel.setVisible(!isConst);

        if (!isConst) {
            ((AccessorsAccessPanel) accessorsAccessPanel).open(prop);
        }
        
        jPanel21.setVisible(propertyRefPanel.isVisible() || overwrittenPanel.isVisible() || overriddenPanel.isVisible());
        isMayModify = true;

//        AdsType t2 = typeEditPanel.getCurrentType().resolve(prop);

    }
    
    private boolean updateParentRefs() {
        if (prop instanceof AdsParentPropertyDef) {
            final ArrayList<AdsPropertyDef> pathItems = new ArrayList<>();
            ((AdsParentPropertyDef) prop).getParentInfo().findOriginalProperty(pathItems);
            if (pathItems.isEmpty()) {
                return false;
            }
            parentRefsPanel.removeAll();
            parentRefsPanel.setLayout(new BoxLayout(parentRefsPanel, BoxLayout.PAGE_AXIS));
            for (AdsPropertyDef adsPropertyDef : pathItems) {
                parentRefsPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(jPanel21.getMaximumSize().width, 10)));
                DefinitionLinkEditPanel linkEditPanel = new DefinitionLinkEditPanel();
                linkEditPanel.open(adsPropertyDef, adsPropertyDef.getId());
                parentRefsPanel.add(linkEditPanel);
            }
            
            jPanel21.revalidate();
            jPanel21.repaint();

            return true;
        }
        return false;
    }

    public void open(AdsPropertyDef prop, boolean isReadOnly) {
        this.prop = prop;
        this.isReadOnly = isReadOnly;
        if (prop.getNature() == EPropNature.EXPRESSION || prop.getNature() == EPropNature.FIELD) {
            if (dbTypePanel == null) {
                dbTypePanel = new DbTypePanelWrapper();
                jPanelTypes.add(dbTypePanel, BorderLayout.SOUTH);
            }
            dbTypePanel.open(prop);
        }
        update();

    }

//    public AdsValAsStrEditor getPropertyValueEditor() {
//        return initValueEditor;
//    }
    public TypeEditPanel getTypeEditPanel() {
        return typeEditPanel;
    }

    private void setViewAsAdsDetailColumnPropertyDef() {
        setInfoIdParentRef("Detail column");
    }

    private void setViewAsAdsDetailRefPropertyDef() {
        setInfoIdParentRef("Detail reference");
    }

    private void setViewAsAdsInnateColumnPropertyDef() {
        setInfoIdParentRef("Database column");

    }

    private void setViewAsAdsInnateRefPropertyDef() {
        setInfoIdParentRef("Database reference");
    }

    private void setViewAsAdsParentPropertyDef() {
        setInfoIdParentRef("Parent property");
    }

    private void setViewAsAdsPropertyPresentationPropertyDef() {
        setInfoIdParentRef("Presentation property");
    }

    private void setViewAsAdsDynamicPropertyDef() {
        setInfoId();
    }

    private void setViewAsAdsExpressionPropertyDef() {
        setInfoId();
    }

    private void setViewAsAdsTransmittablePropertyDef() {
        setInfoId();
    }

    private void setViewAsAdsPresentationPropertyDef() {
        setInfoId();
    }

    private void setViewAsAdsUserPropertyDef() {
        setInfoId();
    }

    private void setInfoId() {
        propertyRefPanel.setVisible(false);
    }

    private void setInfoIdParentRef(String sInfo) {

        jInfoLabel1.setText(sInfo + ":");


        jInfoLabel1.setVisible(true);

        if (prop instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) prop).isLocal()) {
            jPanel21.setVisible(false);
        } else {
            jPanel21.setVisible(true);
            publishingPropertyRefPanel.open(prop);
            publishingPropertyRefPanel.setEnabled(!isReadOnly);
        }

    }

//    private void setStrInitVal() {
//        boolean isStr = prop.getValue().getType().getTypeId() != null
//                && prop.getValue().getType().getTypeId().equals(EValType.STR);
//        if (isStr) {
//            AdsType t = prop.getValue().getType().resolve(prop);
//            isStr = !(t instanceof AdsEnumType);
//        }
//        if (isStr) {
//            initValueEditPanel.setVisible(false);
//            //initValueEditPanel.setVisible(true);
//          //  jScrollPane2.setVisible(true);
//            jPanelInitVal.setPreferredSize(new Dimension(0, 33 + 32));
//            jPanel3.setPreferredSize(new Dimension(0, 33 + 28));
//        } else {
//            initValueEditPanel.setVisible(true);
//      //      jScrollPane2.setVisible(false);
//
//            jPanelInitVal.setPreferredSize(new Dimension(0, 32));
//            jPanel3.setPreferredSize(new Dimension(0, 28));
//        }
//    }
//
//    private void editInitStrVal(java.awt.event.KeyEvent evt) {
//        if (!isMayModify || isReadOnly) {
//            return;
//        }
//        isMayModify = false;
//        if (evt.getKeyCode() == KeyEvent.VK_SPACE
//                && evt.getModifiers() == KeyEvent.CTRL_MASK) {
//            prop.getValue().setInitial(null);
//          //  jEditorPane1.setText("NULL");
//        } else if (evt.getKeyCode() > 30 && (evt.getModifiers() & KeyEvent.CTRL_MASK) == 0) {
//            if (prop.getValue().getInitial() == null) {
//                prop.getValue().setInitial(ValAsStr.Factory.loadFrom(""));
//                jEditorPane1.setText("");
//            } else {
//                prop.getValue().setInitial(ValAsStr.Factory.loadFrom(jEditorPane1.getText()));
//            }
//        }
//        isMayModify = true;
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        descriptionPanel = new DescriptionPanel();
        panelInitVal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        initValueEditor = new org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor();
        jPanelTypes = new javax.swing.JPanel();
        typeEditPanel = new org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel();
        jPanel21 = new javax.swing.JPanel();
        propertyRefPanel = new javax.swing.JPanel();
        jInfoLabel1 = new javax.swing.JLabel();
        publishingPropertyRefPanel = new org.radixware.kernel.designer.ads.editors.clazz.property.PublishingPropertyRefPanel();
        parentRefsPanel = new javax.swing.JPanel();
        overwrittenPanel = new javax.swing.JPanel();
        overwrittenLablel = new javax.swing.JLabel();
        overwrittenDefinitionLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        overriddenPanel = new javax.swing.JPanel();
        overriddenLablel = new javax.swing.JLabel();
        overriddenDefinitionLinkEditPanel = new org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel();
        envSelectorPanel = new org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel();
        accessorsAccessPanel = new AccessorsAccessPanel();
        accessPropertyPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        accessPanel = new org.radixware.kernel.designer.ads.common.dialogs.AccessPanel();
        jPanel1 = new javax.swing.JPanel();
        jchbIsOverwrite = new javax.swing.JCheckBox();
        jchbIsOverride = new javax.swing.JCheckBox();
        jchkDepricated = new javax.swing.JCheckBox();
        jchkReadOnly = new javax.swing.JCheckBox();
        jchkStatic = new javax.swing.JCheckBox();

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));

        javax.swing.GroupLayout descriptionPanelLayout = new javax.swing.GroupLayout(descriptionPanel);
        descriptionPanel.setLayout(descriptionPanelLayout);
        descriptionPanelLayout.setHorizontalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        descriptionPanelLayout.setVerticalGroup(
            descriptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        panelInitVal.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
        panelInitVal.setLayout(new javax.swing.BoxLayout(panelInitVal, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jLabel1.text")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 8));
        panelInitVal.add(jLabel1);
        panelInitVal.add(initValueEditor);

        jPanelTypes.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jPanelTypes.border.title"))); // NOI18N
        jPanelTypes.setLayout(new java.awt.BorderLayout());
        jPanelTypes.add(typeEditPanel, java.awt.BorderLayout.CENTER);

        jPanel21.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jPanel21.border.outsideBorder.title")), javax.swing.BorderFactory.createEmptyBorder(0, 2, 5, 2))); // NOI18N
        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.PAGE_AXIS));

        jInfoLabel1.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jInfoLabel1.text")); // NOI18N
        jInfoLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        javax.swing.GroupLayout propertyRefPanelLayout = new javax.swing.GroupLayout(propertyRefPanel);
        propertyRefPanel.setLayout(propertyRefPanelLayout);
        propertyRefPanelLayout.setHorizontalGroup(
            propertyRefPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertyRefPanelLayout.createSequentialGroup()
                .addComponent(jInfoLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(publishingPropertyRefPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        propertyRefPanelLayout.setVerticalGroup(
            propertyRefPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(propertyRefPanelLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(propertyRefPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jInfoLabel1)
                    .addComponent(publishingPropertyRefPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jInfoLabel1.getAccessibleContext().setAccessibleDescription(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jInfoLabel1.AccessibleContext.accessibleDescription")); // NOI18N

        jPanel21.add(propertyRefPanel);

        parentRefsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.parentRefsPanel.border.title"))); // NOI18N
        parentRefsPanel.setLayout(new javax.swing.BoxLayout(parentRefsPanel, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel21.add(parentRefsPanel);

        overwrittenLablel.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.overwrittenLablel.text")); // NOI18N
        overwrittenLablel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        javax.swing.GroupLayout overwrittenPanelLayout = new javax.swing.GroupLayout(overwrittenPanel);
        overwrittenPanel.setLayout(overwrittenPanelLayout);
        overwrittenPanelLayout.setHorizontalGroup(
            overwrittenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overwrittenPanelLayout.createSequentialGroup()
                .addComponent(overwrittenLablel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(overwrittenDefinitionLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        overwrittenPanelLayout.setVerticalGroup(
            overwrittenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overwrittenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(overwrittenLablel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(overwrittenDefinitionLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel21.add(overwrittenPanel);

        overriddenPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));

        overriddenLablel.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.overriddenLablel.text")); // NOI18N
        overriddenLablel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));

        javax.swing.GroupLayout overriddenPanelLayout = new javax.swing.GroupLayout(overriddenPanel);
        overriddenPanel.setLayout(overriddenPanelLayout);
        overriddenPanelLayout.setHorizontalGroup(
            overriddenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overriddenPanelLayout.createSequentialGroup()
                .addComponent(overriddenLablel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(overriddenDefinitionLinkEditPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                .addContainerGap())
        );
        overriddenPanelLayout.setVerticalGroup(
            overriddenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(overriddenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(overriddenLablel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(overriddenDefinitionLinkEditPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel21.add(overriddenPanel);

        envSelectorPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));

        accessorsAccessPanel.setLayout(null);

        accessPropertyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8));
        accessPropertyPanel.setLayout(new java.awt.BorderLayout());

        jLabel5.setLabelFor(accessPanel);
        jLabel5.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jLabel5.text")); // NOI18N
        accessPropertyPanel.add(jLabel5, java.awt.BorderLayout.WEST);
        accessPropertyPanel.add(accessPanel, java.awt.BorderLayout.CENTER);

        jchbIsOverwrite.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jchbIsOverwrite.text")); // NOI18N
        jchbIsOverwrite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbIsOverwriteActionPerformed(evt);
            }
        });

        jchbIsOverride.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jchbIsOverride.text")); // NOI18N
        jchbIsOverride.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbIsOverrideActionPerformed(evt);
            }
        });

        jchkDepricated.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jchkDepricated.text")); // NOI18N
        jchkDepricated.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkDepricatedActionPerformed(evt);
            }
        });

        jchkReadOnly.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jchkReadOnly.text")); // NOI18N
        jchkReadOnly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkReadOnlyActionPerformed(evt);
            }
        });

        jchkStatic.setText(org.openide.util.NbBundle.getMessage(AdsPropertyEditorMainPage.class, "AdsPropertyEditorMainPage.jchkStatic.text")); // NOI18N
        jchkStatic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkStaticActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jchbIsOverwrite)
                    .addComponent(jchbIsOverride))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jchkDepricated)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jchkStatic))
                    .addComponent(jchkReadOnly))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jchkDepricated)
                            .addComponent(jchkStatic))
                        .addComponent(jchkReadOnly))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jchbIsOverwrite)
                        .addComponent(jchbIsOverride)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTypes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelInitVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(envSelectorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(accessPropertyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(accessorsAccessPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(descriptionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(descriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(accessPropertyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accessorsAccessPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(envSelectorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelTypes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInitVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jchbIsOverwriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbIsOverwriteActionPerformed
        prop.setOverwrite(jchbIsOverwrite.isSelected());

        update();
}//GEN-LAST:event_jchbIsOverwriteActionPerformed

    private void jchkDepricatedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkDepricatedActionPerformed
//        if (!isMayModify || isReadOnly) {
//            return;
//        }
//        prop.getAccessFlags().setDeprecated(this.jchkDepricated.isSelected());
}//GEN-LAST:event_jchkDepricatedActionPerformed

    private void jchkStaticActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkStaticActionPerformed
        if (!isMayModify || isReadOnly) {
            return;
        }
        if (prop instanceof AdsDynamicPropertyDef) {
            prop.getAccessFlags().setStatic(jchkStatic.isSelected());
        } else {
            AdsUserPropertyDef uProp = (AdsUserPropertyDef) prop;
            uProp.setAuditUpdate(jchkStatic.isSelected());
        }

        update();

}//GEN-LAST:event_jchkStaticActionPerformed

    private void jchkReadOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkReadOnlyActionPerformed
        if (!isMayModify || isReadOnly) {
            return;
        }

        final boolean readOnly = this.jchkReadOnly.isSelected();
        prop.setConst(readOnly);
        update();
}//GEN-LAST:event_jchkReadOnlyActionPerformed

    private void jchbIsOverrideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbIsOverrideActionPerformed
        prop.setOverride(jchbIsOverride.isSelected());
        update();
}//GEN-LAST:event_jchbIsOverrideActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.ads.common.dialogs.AccessPanel accessPanel;
    private javax.swing.JPanel accessPropertyPanel;
    private javax.swing.JPanel accessorsAccessPanel;
    private javax.swing.JPanel descriptionPanel;
    private org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel envSelectorPanel;
    private org.radixware.kernel.designer.ads.editors.common.adsvalasstr.AdsValAsStrEditor initValueEditor;
    private javax.swing.JLabel jInfoLabel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanelTypes;
    private javax.swing.JCheckBox jchbIsOverride;
    private javax.swing.JCheckBox jchbIsOverwrite;
    private javax.swing.JCheckBox jchkDepricated;
    private javax.swing.JCheckBox jchkReadOnly;
    private javax.swing.JCheckBox jchkStatic;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel overriddenDefinitionLinkEditPanel;
    private javax.swing.JLabel overriddenLablel;
    private javax.swing.JPanel overriddenPanel;
    private org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel overwrittenDefinitionLinkEditPanel;
    private javax.swing.JLabel overwrittenLablel;
    private javax.swing.JPanel overwrittenPanel;
    private javax.swing.JPanel panelInitVal;
    private javax.swing.JPanel parentRefsPanel;
    private javax.swing.JPanel propertyRefPanel;
    private org.radixware.kernel.designer.ads.editors.clazz.property.PublishingPropertyRefPanel publishingPropertyRefPanel;
    private org.radixware.kernel.designer.common.dialogs.choosetype.TypeEditPanel typeEditPanel;
    // End of variables declaration//GEN-END:variables
}

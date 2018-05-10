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
package org.radixware.kernel.designer.ads.editors.property;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.*;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.EventCodeEditPanel;
import org.radixware.kernel.designer.ads.editors.clazz.simple.IAdsPropertiesListProvider;
import org.radixware.kernel.designer.ads.editors.clazz.simple.ObjectTitleFormatPanel;
import org.radixware.kernel.designer.ads.editors.presentations.ConditionsPanel;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.InheritableTitlePanel;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

final class AdsPropertyEditorPanel extends JPanel {

    //-----------------------------------------------------------------------
    private class WindowsTabbedPaneUIEx extends BasicTabbedPaneUI //com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI
    {

        @Override
        protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
            if (tabIndex < 0 || tabIndex >= visibleTab.length || !visibleTab[tabIndex]) {
                return 0;
            }
            return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        }
//        @Override
//        protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
//            //if (!visibleTab[tabPlacement]) return 0;
//
//            return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
//        }
//
//        @Override
//        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
//           // if (!visibleTab[tabPlacement]) return;
//            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
//        }

        @Override
        protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
            if (tabIndex < 0 || tabIndex >= visibleTab.length || !visibleTab[tabIndex]) {
                return 0;
            }
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        }

//        @Override
//        protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
////            if (!visibleTab[tabIndex])
////                 return 0;
//            return super.calculateTabAreaWidth(tabPlacement, vertRunCount, maxTabWidth);
//        }
//
        @Override
        protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
            if (selectedIndex < 0 || selectedIndex >= visibleTab.length || !visibleTab[selectedIndex]) {
                return;
            }
            super.paintTabArea(g, tabPlacement, selectedIndex);
        }

        @Override
        protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
            if (tabIndex < 0 || tabIndex >= visibleTab.length || !visibleTab[tabIndex]) {
                return;
            }
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
        }
    }
    //-----------------------------------------------------------------------
    private static int iLastVisiblePage = 0;
    private static int iLastVisiblePage2 = 0;
    //-----------------------------------------------------------------------
    private AdsPropertyDef prop;
    private AdsPropertyDef ownerProp = null;
    private IAdsPresentableProperty sProp;
    private IAdsPresentableProperty presentableProperty = null;
    private PropertyPresentation propertyPresentation = null;
    private ParentRefPropertyPresentation parentRefPropertyPresentation = null;
    private List<AdsPropertyDef> formatPropList = null;
    //
    private boolean isReadOnly = true;
    private boolean isMayModify = false;
    private boolean isRealOverriden = false;
    private boolean isRealOverwriten = false;
    private boolean isRealOver = false;
    private boolean isCanInherit = false;
    private Restrictions selRest = null;
    private Restrictions edRest = null;
    private boolean isjPresentationPanelFilling = false;
    private boolean visibleTab[] = new boolean[8];
//    private AdsInnateRefPropertyDef innateRefPropertyDef = null;
//    private boolean isBeRemoveTabs = false;
    //-----------------------------------------------------------------------
    private EventCodeEditPanel eventCodes;
    private SqmlEditorPanel expressionPanel = null;
    private OnParentDeletionPanel onParentDeletionPanel = null;
    private AdsProperyEditorValueInitializationModePanel valueInitializationModePanelForUserProps = null;
    private ObjectTitleFormatPanel objectTitleFormatPanel = null;
    private AdsPropertyEditorMainPage adsPropertyEditorMainPanel = null;
    private AdsPropertyEditorFieldParentRefPage adsPropertyEditorFieldParentRefPage = null;
    private AdsPropertyEditorFieldPage adsPropertyEditorFieldPage = null;
    private InheritableTitlePanel inheritableTitlePanel1 = null;
    private InheritableTitlePanel inheritableHintPanel1 = null;
    private AdsValueInheritancePanel valueInheritancePanel = null;
    private AdsPropertyPresentationEditPanel propertyPresentationEditPanel = null;
    private AdsObjectPropertPresentationPanel objectPropertyPresentationPanel = null;
    private ParentSelectorPresentationPanel parentSelectopPresentationPanel = null;
    private ConditionsPanel propertyParentSelectorCondition = null;
    private AdsPanelPropertyRestrictions panelPropertyRestrictions = null;
    //
    private JCheckBox jchb = null;
    private JCheckBox jchbInheritPath = null;
    private JCheckBox jchbInheritParentSelector = null;
    private JCheckBox jchbInheritCondition = null;
    private JCheckBox jchbInheritFormat = null;
    private JTabbedPane presentationTabbedPane = null;
    private JPanel jPresentationPanel = null;
    private JPanel jParentRefPanel = null;
    private JPanel mainTabPanel;
    private JTabbedPane tabbedPane;
    private JPanel expressionTabPanel;
    private JPanel referencePropertiesTabPanel;
    private JPanel presentationTabPanel;
    private JPanel typeTabPanel;
    private JPanel jValueInheritancePanelContent;
    private JScrollPane referencePropertiesTabPanelScrollPane;
    private JScrollPane typeTabPanelScrollPane;
    private JScrollPane mainPanelScrollPane;
    private JPanel jValueInheritancePanel;
    private JPanel eventCodeTab;
    private JCheckBox jIsPresentableCheckBox = null;
    private ChangeListener updateChangedListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (!isMayModify) {
                return;
            }
            update();
        }
    };
    //-----------------------------------------------------------------------

    public AdsPropertyEditorPanel(AdsPropertyDef prop) {
        this.prop = prop;

        createMainTab();

        tabbedPane.setIconAt(0, RadixWareDesignerIcon.EDIT.PROPERTIES.getIcon());
    }

    /**
     *
     * @deprecated unused
     */
    @Deprecated
    public void refreshObjectPanel() {
        if (propertyPresentationEditPanel != null) {
            propertyPresentationEditPanel.update();
        }
        if (objectPropertyPresentationPanel != null) {
            if (prop instanceof IAdsPresentableProperty) {
                presentableProperty = (IAdsPresentableProperty) prop;
                //AdsPresentationPropertyDef ap = null;
                ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();
                if (presentationSupport != null) {
                    propertyPresentation = presentationSupport.getPresentation();
                }
            }
            objectPropertyPresentationPanel.setVisible(
                    propertyPresentation != null
                    && propertyPresentation instanceof ObjectPropertyPresentation);
        }
    }

    public void refreshPropertyPresentationAndHintPanelsVisible() {
        if (propertyPresentation != null) {
            boolean isCan = propertyPresentation.canBePresentable();
            if (inheritableHintPanel1 != null) {
                inheritableHintPanel1.setVisible(isCan);
            }
            if (propertyPresentationEditPanel != null) {
                propertyPresentationEditPanel.setVisible(isCan);
            }
            if (jIsPresentableCheckBox != null) {
                jIsPresentableCheckBox.setVisible(isCan);
            }
        }
    }

    private AdsPropertyDef getPropForPresentation() {
        if (prop.getNature() == EPropNature.PROPERTY_PRESENTATION) {
            AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) prop;
            if (pp.isLocal()) {
                return (AdsPropertyDef) pp.findServerSideProperty();
            } else {
                return prop;
            }
        } else {
            return prop;
        }
    }

    private boolean isLocalPresenation() {
        if (prop.getNature() == EPropNature.PROPERTY_PRESENTATION) {
            AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) prop;
            if (pp.isLocal()) {
                return true;
            }
        }
        return false;
    }

    private void createTabsAndPanels() {
//        isBeRemoveTabs = true;
        for (int i = 0; i < 6; i++) {
            visibleTab[i] = true;
        }

        if (adsPropertyEditorMainPanel == null) {

            JPanel mainPanelContent = new JPanel();
            mainPanelScrollPane.setViewportView(mainPanelContent);
            mainPanelContent.setPreferredSize(new Dimension(10, 540));

            if (mainPanelContent != null) {
                adsPropertyEditorMainPanel = new AdsPropertyEditorMainPage(this);
                if (prop instanceof AdsUserPropertyDef && ((AdsUserPropertyDef) prop).isUserConstraintsAvailable()) {
                    onParentDeletionPanel = new OnParentDeletionPanel();
                    onParentDeletionPanel.setEditable(!isReadOnly && ((AdsUserPropertyDef) prop).isUserConstraintsEditable());
                    onParentDeletionPanel.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if (prop == null || isReadOnly || !(prop instanceof AdsUserPropertyDef)) {
                                return;
                            }
                            ((AdsUserPropertyDef) prop).setParentDeletionOptions(onParentDeletionPanel.getOptions());
                        }
                    });
                } else {
                    onParentDeletionPanel = null;
                }
                mainPanelContent.setLayout(new MigLayout("fill", "[grow]", "[][]push"));
                mainPanelContent.add(adsPropertyEditorMainPanel, "growx, wrap");
                if (onParentDeletionPanel != null) {
                    mainPanelContent.add(onParentDeletionPanel, "growx");
                }
            }
        }

        if (adsPropertyEditorFieldParentRefPage == null) {
            JPanel jPresentationPanelParent3 = new JPanel();
            referencePropertiesTabPanelScrollPane.setViewportView(jPresentationPanelParent3);

            if (jPresentationPanelParent3 != null) {

                adsPropertyEditorFieldParentRefPage = new AdsPropertyEditorFieldParentRefPage();

                GroupLayout jPanel24Layout = new GroupLayout(jPresentationPanelParent3);
                jPresentationPanelParent3.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(adsPropertyEditorFieldParentRefPage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                jPanel24Layout.setVerticalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                        addGroup(jPanel24Layout.createSequentialGroup().
                                addComponent(adsPropertyEditorFieldParentRefPage,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE).
                                addContainerGap(10, 20)));//Short.MAX_VALUE
            }
        }

        if (adsPropertyEditorFieldPage == null) {
            JPanel jPresentationPanelParent3 = new JPanel();
            typeTabPanelScrollPane.setViewportView(jPresentationPanelParent3);
            jPresentationPanelParent3.setPreferredSize(new Dimension(10, 10));

            if (jPresentationPanelParent3 != null && adsPropertyEditorFieldPage == null) {

                adsPropertyEditorFieldPage = new AdsPropertyEditorFieldPage();

                GroupLayout jPanel24Layout = new GroupLayout(jPresentationPanelParent3);
                jPresentationPanelParent3.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(adsPropertyEditorFieldPage, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                jPanel24Layout.setVerticalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).
                        addGroup(jPanel24Layout.createSequentialGroup().
                                addComponent(adsPropertyEditorFieldPage,
                                        GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE).
                                addContainerGap(10, 20)));//Short.MAX_VALUE
            }
        }

        if (expressionPanel == null) {
            if (prop instanceof AdsExpressionPropertyDef) {
                expressionPanel = new SqmlEditorPanel();
                expressionTabPanel.setLayout(new BoxLayout(expressionTabPanel, BoxLayout.Y_AXIS));
                expressionTabPanel.add(expressionPanel);
                AdsExpressionPropertyDef expr = (AdsExpressionPropertyDef) prop;
                expressionPanel.open(expr.getExpresssion());
            } else {
                //jMainTab.removeTabAt(5);
                visibleTab[5] = false;
            }
        }

        boolean createTab = false;
        boolean createjPresentationPanel = false;
        boolean createInheritValuePanel = false;

        if (prop instanceof AdsFieldPropertyDef) {
            if (prop instanceof AdsFieldRefPropertyDef) {
                //jMainTab.removeTabAt(4);
                visibleTab[4] = false;
            } else {
                //jMainTab.removeTabAt(3);
                visibleTab[3] = false;
            }

            //jMainTab.removeTabAt(2);
            visibleTab[2] = false;
            //jMainTab.removeTabAt(1);
            visibleTab[1] = false;
            //jMainTab.removeTabAt(0);
            visibleTab[0] = false;
        } else {
            //jMainTab.removeTabAt(4);
            visibleTab[4] = false;
            //jMainTab.removeTabAt(3);
            visibleTab[3] = false;

            AdsPropertyDef propForPresentation = getPropForPresentation();
            if (!(propForPresentation instanceof IAdsPresentableProperty) || ((IAdsPresentableProperty) propForPresentation).getPresentationSupport() == null) {
                //jMainTab.removeTabAt(2);
                visibleTab[2] = false;
            } else {
                if (propForPresentation.getValue().getType().getTypeId() == null
                        || !prop.getValue().getType().getTypeId().equals(EValType.PARENT_REF)
                        && !prop.getValue().getType().getTypeId().equals(EValType.ARR_REF)) {
                    createjPresentationPanel = true;
                } else {
                    createTab = propForPresentation.getNature() != EPropNature.EVENT_CODE;
                }
            }
            if (!(prop.getOwnerClass() instanceof AdsEntityObjectClassDef) || !(prop instanceof AdsTablePropertyDef)) {
                //jMainTab.removeTabAt(1);
                visibleTab[1] = false;
            } else {
                createInheritValuePanel = true;
            }
        }
        JPanel jPresentationPanelParent = null;
        JPanel jParentRefPanelParent = null;

        if (createjPresentationPanel) {
            jPresentationPanelParent = presentationTabPanel;
        }

        if (presentationTabbedPane != null && presentationTabbedPane.getTabCount() == 3 && !createTab) {//был тип парент реф или арр парент реф - стал какой-то другой
            presentationTabPanel.remove(presentationTabbedPane);
            presentationTabbedPane = null;
            jPresentationPanel = null;
            jParentRefPanel = null;
            isjPresentationPanelFilling = false;
        } else if (createTab && jPresentationPanel != null && jPresentationPanel.getParent().getParent().getParent() == presentationTabPanel) {
            presentationTabPanel.remove(jPresentationPanel.getParent().getParent());
            jPresentationPanel = null;
            jParentRefPanel = null;
            isjPresentationPanelFilling = false;
        }

        if (createTab && presentationTabbedPane == null) {
            presentationTabbedPane = new JTabbedPane();

            presentationTabPanel.setLayout(new BorderLayout());

//            presentationTabPanel.setLayout(new BoxLayout(presentationTabPanel, BoxLayout.Y_AXIS));
            presentationTabPanel.add(presentationTabbedPane, BorderLayout.CENTER);

            final JScrollPane generalTabScrollPane = new JScrollPane();
            presentationTabbedPane.add("General", generalTabScrollPane);
            jPresentationPanelParent = new JPanel();

            generalTabScrollPane.setViewportView(jPresentationPanelParent);
            generalTabScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//AS_NEEDED

            final JScrollPane parentRefScrollPane = new JScrollPane();
            presentationTabbedPane.add("Parent Ref", parentRefScrollPane);

            jParentRefPanelParent = new JPanel();
            parentRefScrollPane.setViewportView(jParentRefPanelParent);
            parentRefScrollPane.setPreferredSize(new Dimension(30, 30));
            jParentRefPanelParent.setPreferredSize(new Dimension(200, 675));

            panelPropertyRestrictions = new AdsPanelPropertyRestrictions();
            presentationTabbedPane.add("Restrictions", panelPropertyRestrictions);

            presentationTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
                @Override
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    if (isMayModify) {
                        iLastVisiblePage2 = presentationTabbedPane.getSelectedIndex();
                    }
                }
            });

        }

        if (jPresentationPanelParent != null && jPresentationPanel == null) {
            jPresentationPanel = new JPanel();

            if (jPresentationPanelParent == presentationTabPanel) {
                JScrollPane presentationScroll = new JScrollPane(jPresentationPanel);
                jPresentationPanelParent.setLayout(new BorderLayout());
                jPresentationPanelParent.add(presentationScroll);
            } else {

                jPresentationPanelParent.setLayout(new BorderLayout());
                jPresentationPanelParent.add(jPresentationPanel, BorderLayout.CENTER);

            }
        }

        if (jParentRefPanelParent != null && jParentRefPanel == null) {
            jParentRefPanel = new JPanel();

            jParentRefPanelParent.setLayout(new BorderLayout());
            jParentRefPanelParent.add(jParentRefPanel);
        }
        //InheritableTitlePanel
        if (jPresentationPanel != null && !isjPresentationPanelFilling /*
                 * && jIsPresentableCheckBox == null
                 */) {

            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            jPresentationPanel.setLayout(gbl);

            int cnt = 0;
            isjPresentationPanelFilling = true;

            if (!(prop instanceof AdsParameterPropertyDef) && !isLocalPresenation()) {
                c.gridwidth = 1;
                c.weightx = 1.0;
                c.gridy = cnt;
                c.insets = new Insets(4, 6, 4, 6);
                jIsPresentableCheckBox = new JCheckBox();
                c.fill = GridBagConstraints.HORIZONTAL;
                gbl.setConstraints(jIsPresentableCheckBox, c);
                jPresentationPanel.add(jIsPresentableCheckBox);
                jIsPresentableCheckBox.setText("Presentable");
                ChangeListener changeListener = new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (!isMayModify || isReadOnly || propertyPresentation == null) {
                            return;
                        }
                        if (propertyPresentation.isPresentable() != jIsPresentableCheckBox.isSelected()) {
                            propertyPresentation.setPresentable(jIsPresentableCheckBox.isSelected());
                            updateEditingPanel();
                        }
                        //update();
                    }
                };
                jIsPresentableCheckBox.addChangeListener(changeListener);
                cnt++;
            }

            {
                inheritableTitlePanel1 = new InheritableTitlePanel();
                c.gridy = cnt;
                c.insets = new Insets(4, 6, 4, 6);
                c.fill = GridBagConstraints.HORIZONTAL;
                gbl.setConstraints(inheritableTitlePanel1, c);
                jPresentationPanel.add(inheritableTitlePanel1);
                cnt++;
            }
            {
                inheritableHintPanel1 = new InheritableTitlePanel("Inherit Hint");
                c.gridy = cnt;
                c.weightx = 1.0;
                c.insets = new Insets(4, 6, 4, 6);
                c.fill = GridBagConstraints.HORIZONTAL;
                gbl.setConstraints(inheritableHintPanel1, c);
                jPresentationPanel.add(inheritableHintPanel1);

                cnt++;
            }

            propertyPresentationEditPanel = new AdsPropertyPresentationEditPanel();
            jchb = new javax.swing.JCheckBox();
            jchb.setText("Inherit editing");
            jchb.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (sProp == null || isReadOnly) {
                        return;
                    }
                    ServerPresentationSupport presentationSupport = sProp.getPresentationSupport();
                    if (presentationSupport != null) {
                        //EditOptions currEditOptions = null;

                        PropertyEditOptions inputEditOptions = null;
                        if (!jchb.isSelected()) {
                            inputEditOptions = presentationSupport.getPresentation().getEditOptions();
                        }

                        presentationSupport.getPresentation().setEditOptionsInherited(jchb.isSelected());

                        if (inputEditOptions != null) {//RADIX-2268
                            PropertyEditOptions outputEditOptions = presentationSupport.getPresentation().getEditOptions();

                            presentationSupport.getPresentation().getEditOptions();

                            outputEditOptions.setCustomDialogId(ERuntimeEnvironmentType.EXPLORER, inputEditOptions.getCustomDialogId(ERuntimeEnvironmentType.EXPLORER));
                            outputEditOptions.setCustomDialogId(ERuntimeEnvironmentType.WEB, inputEditOptions.getCustomDialogId(ERuntimeEnvironmentType.WEB));
                            outputEditOptions.setCustomEditOnly(inputEditOptions.isCustomEditOnly());
                            outputEditOptions.setShowDialogButton(inputEditOptions.isShowDialogButton());
                            outputEditOptions.setMemo(inputEditOptions.isMemo());
                            outputEditOptions.setReadSeparately(inputEditOptions.isReadSeparately());
                            outputEditOptions.setNotNull(inputEditOptions.isNotNull());
                            outputEditOptions.setDuplicatesEnabled(inputEditOptions.isDuplicatesEnabled());
                            outputEditOptions.setStoreEditHistory(inputEditOptions.isStoreEditHistory());
                            outputEditOptions.setEditPossibility(inputEditOptions.getEditPossibility());

                            EditMask inputEditMask = inputEditOptions.getEditMask();
                            EditMask outputEditMask = outputEditOptions.getEditMask();
                            if (inputEditMask == null) {
                                outputEditOptions.setEditMaskType(null);
                            } else {
                                outputEditOptions.setEditMaskType(inputEditMask.getType());
                                outputEditMask = outputEditOptions.getEditMask();
                                if (inputEditMask.getType().equals(EEditMaskType.DATE_TIME)) {
                                    EditMaskDateTime oEM = (EditMaskDateTime) inputEditMask;
                                    EditMaskDateTime iEM = (EditMaskDateTime) outputEditMask;
                                    oEM.setMask(iEM.getMask());
                                    oEM.setMaxValue(iEM.getMaxValue());
                                    oEM.setMinValue(iEM.getMinValue());
                                } else if (inputEditMask.getType().equals(EEditMaskType.ENUM)) {
                                    EditMaskEnum iEM = (EditMaskEnum) inputEditMask;
                                    EditMaskEnum oEM = (EditMaskEnum) outputEditMask;

                                    oEM.setCorrection(iEM.getCorrection());
                                    oEM.setOrderBy(iEM.getOrderBy());
                                    oEM.setCorrectionItems(iEM.getCorrectionItems());
                                } else if (inputEditMask.getType().equals(EEditMaskType.INT)) {
                                    EditMaskInt iEM = (EditMaskInt) inputEditMask;
                                    EditMaskInt oEM = (EditMaskInt) outputEditMask;

                                    oEM.setMaxValue(iEM.getMaxValue());
                                    oEM.setMinValue(iEM.getMinValue());
                                    oEM.setMinLength(iEM.getMinLength());
                                    oEM.setNumberBase(iEM.getNumberBase());
                                    oEM.setPadChar(iEM.getPadChar());
                                    oEM.setStepSize(iEM.getStepSize());
                                    oEM.setTriadDelimiter(iEM.getTriadDelimiter());
                                } else if (inputEditMask.getType().equals(EEditMaskType.LIST)) {
                                    EditMaskList iEM = (EditMaskList) inputEditMask;
                                    EditMaskList oEM = (EditMaskList) outputEditMask;

                                    oEM.clearItems();
                                    for (EditMaskList.Item i : iEM.getItems()) {
                                        oEM.addItem(i);
                                    }
                                } else if (inputEditMask.getType().equals(EEditMaskType.NUM)) {
                                    EditMaskNum iEM = (EditMaskNum) inputEditMask;
                                    EditMaskNum oEM = (EditMaskNum) outputEditMask;
                                    oEM.setMaxValue(iEM.getMaxValue());
                                    oEM.setMinValue(iEM.getMinValue());
                                    oEM.setPrecision(iEM.getPrecision());
                                    oEM.setScale(iEM.getScale());
                                    oEM.setTriadDelimiter(iEM.getTriadDelimiter());
                                } else if (inputEditMask.getType().equals(EEditMaskType.STR)) {
                                    EditMaskStr iEM = (EditMaskStr) inputEditMask;
                                    EditMaskStr oEM = (EditMaskStr) outputEditMask;
                                    oEM.setValidator(iEM.getValidator());
                                    oEM.setIsPassword(iEM.isPassword());
                                    oEM.setMaxLen(iEM.getMaxLen());
                                } else if (inputEditMask.getType().equals(EEditMaskType.TIME_INTERVAL)) {
                                    EditMaskTimeInterval iEM = (EditMaskTimeInterval) inputEditMask;
                                    EditMaskTimeInterval oEM = (EditMaskTimeInterval) outputEditMask;
                                    oEM.setMask(iEM.getMask());
                                    oEM.setMaxValue(iEM.getMaxValue());
                                    oEM.setMinValue(iEM.getMinValue());
                                    oEM.setScale(iEM.getScale());
                                }
                            }

                            if (inputEditOptions.getNullValTitleId() != null) {
                                outputEditOptions.setNullValTitle(EIsoLanguage.ENGLISH, inputEditOptions.getNullValTitle(EIsoLanguage.ENGLISH));
                                outputEditOptions.setNullValTitle(EIsoLanguage.RUSSIAN, inputEditOptions.getNullValTitle(EIsoLanguage.RUSSIAN));
                            }
                        }
                    }
                    update();
                }
            });
            ComponentTitledBorder componentTitledBorder = new ComponentTitledBorder(
                    jchb,
                    propertyPresentationEditPanel,
                    javax.swing.BorderFactory.createTitledBorder(""));

            propertyPresentationEditPanel.setBorder(componentTitledBorder);

            c.gridy = cnt;
            c.weightx = 1.0;
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.HORIZONTAL;

            gbl.setConstraints(propertyPresentationEditPanel, c);
            jPresentationPanel.add(propertyPresentationEditPanel);

            AdsType currType = prop.getValue().getType().resolve(prop).get();
            if (currType instanceof ObjectType) {
                objectPropertyPresentationPanel = new AdsObjectPropertPresentationPanel();

                cnt++;
                c.gridy = cnt;
                c.weightx = 1.0;
                c.insets = new Insets(4, 6, 4, 6);
                c.fill = GridBagConstraints.HORIZONTAL;

                gbl.setConstraints(objectPropertyPresentationPanel, c);
                jPresentationPanel.add(objectPropertyPresentationPanel);

            }
            cnt++;
            c.gridy = cnt;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.BOTH;
            JPanel fill = new JPanel();
            gbl.setConstraints(fill, c);
            jPresentationPanel.add(fill);

        }

        if (createInheritValuePanel && valueInheritancePanel == null) {

            JPanel jParenrInheritValuePanel = new JPanel();

            {
                GroupLayout jInheritValuePanelLayout = new GroupLayout(jParenrInheritValuePanel);
                jParenrInheritValuePanel.setLayout(jInheritValuePanelLayout);
                jInheritValuePanelLayout.setHorizontalGroup(
                        jInheritValuePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 430, Short.MAX_VALUE));
                jInheritValuePanelLayout.setVerticalGroup(
                        jInheritValuePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 29, Short.MAX_VALUE));

                GroupLayout jPanel24Layout = new GroupLayout(jValueInheritancePanelContent);
                jValueInheritancePanelContent.setLayout(jPanel24Layout);
                jPanel24Layout.setHorizontalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jParenrInheritValuePanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
                jPanel24Layout.setVerticalGroup(
                        jPanel24Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel24Layout.createSequentialGroup().addComponent(jParenrInheritValuePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(595, Short.MAX_VALUE)));
            }
            //
            valueInheritancePanel = new AdsValueInheritancePanel();
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            jParenrInheritValuePanel.setLayout(gbl);

            int cnt = 0;
            if (prop instanceof AdsUserPropertyDef) {
                valueInitializationModePanelForUserProps = new AdsProperyEditorValueInitializationModePanel();

                c.gridwidth = 1;
                c.weightx = 1.0;
                c.gridy = cnt;
                c.insets = new Insets(4, 6, 4, 6);
                c.fill = GridBagConstraints.HORIZONTAL;
                gbl.setConstraints(valueInitializationModePanelForUserProps, c);
                cnt++;
                jParenrInheritValuePanel.add(valueInitializationModePanelForUserProps);
            }

            c.gridy = cnt;
            c.weightx = 1.0;
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(valueInheritancePanel, c);

            jParenrInheritValuePanel.add(valueInheritancePanel);

            //jScrollPane2.add(valueInheritancePanel);
            jchbInheritPath = new javax.swing.JCheckBox();
            jchbInheritPath.setText("Allow value inheritance");
            jchbInheritPath.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (sProp == null || isReadOnly) {
                        return;
                    }
                    boolean isSel = jchbInheritPath.isSelected();

                    if (!isSel) {
                        if (prop.getValueInheritanceRules().getPathes().size() > 0) {
                            NotifyDescriptor d = new NotifyDescriptor.Confirmation("Value inheritance pathes list be clearing. Continue?", NotifyDescriptor.Confirmation.YES_NO_OPTION);
                            if (DialogDisplayer.getDefault().notify(d) != NotifyDescriptor.Confirmation.YES_OPTION) {
                                jchbInheritPath.setSelected(true);
                                return;
                            }
                            prop.getValueInheritanceRules().clearPath();
                        }
                    }
                    valueInheritancePanel.setValue(prop, null);
                    prop.getValueInheritanceRules().setInheritable(isSel);
                    update();
                }
            });
            ComponentTitledBorder componentTitledBorder = new ComponentTitledBorder(
                    jchbInheritPath,
                    valueInheritancePanel,
                    javax.swing.BorderFactory.createTitledBorder(""));
            valueInheritancePanel.setBorder(componentTitledBorder);
        }

        if (jParentRefPanel != null //&& jchbInheritParentSelector == null
                ) {
            jParentRefPanel.removeAll();

            jchbInheritParentSelector = new javax.swing.JCheckBox();
            jchbInheritParentSelector.setText("Inherit parent selector");
            jchbInheritParentSelector.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (parentRefPropertyPresentation == null) {
                        return;
                    }
                    parentRefPropertyPresentation.getParentSelect().setParentSelectorInherited(jchbInheritParentSelector.isSelected());
                    update();
                }
            });
            parentSelectopPresentationPanel = new ParentSelectorPresentationPanel();

            parentSelectopPresentationPanel.addChangeListener(updateChangedListener);
            propertyPresentationEditPanel.addChangeListenerEditorPresentation(updateChangedListener);

            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            jParentRefPanel.setLayout(gbl);

            c.gridwidth = 1;
            c.weightx = 1.0;
            c.gridy = 1;
            c.insets = new Insets(4, 6, 4, 6);
            c.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(parentSelectopPresentationPanel, c);
            jParentRefPanel.add(parentSelectopPresentationPanel);
            ComponentTitledBorder componentTitledBorder = new ComponentTitledBorder(
                    jchbInheritParentSelector,
                    parentSelectopPresentationPanel,
                    javax.swing.BorderFactory.createTitledBorder(""));
            parentSelectopPresentationPanel.setBorder(componentTitledBorder);

            c.gridy = 2;
            c.insets = new Insets(4, 6, 4, 6);
            c.weighty = 2.0;
            c.fill = GridBagConstraints.BOTH;

            JComponent p = createSplitterBetweenConditionAndFormatTitle();
            jParentRefPanel.add(p);

            gbl.setConstraints(p, c);

            p.setPreferredSize(
                    new Dimension(
                            0,
                            600));

        }
        //iLastVisiblePage2 =   jTab.getSelectedIndex();
        if (iLastVisiblePage < tabbedPane.getTabCount()) {

            if (!visibleTab[iLastVisiblePage]) {
                for (int i = 0; i < 6; i++) {
                    if (visibleTab[i]) {
                        iLastVisiblePage = i;
                        tabbedPane.setSelectedIndex(i);
                    }
                }
            } else {
                tabbedPane.setSelectedIndex(iLastVisiblePage);
            }
        }
        if (presentationTabbedPane != null && iLastVisiblePage2 < presentationTabbedPane.getTabCount()) {
            //jTab.get settet

            if (prop instanceof IAdsPresentableProperty) {
                presentableProperty = (IAdsPresentableProperty) prop;
                //AdsPresentationPropertyDef ap = null;
                ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();
                if (presentationSupport != null) {
                    propertyPresentation = presentationSupport.getPresentation();
                    if (propertyPresentation.isPresentable()) {
                        presentationTabbedPane.setSelectedIndex(iLastVisiblePage2);
                    }

                }
            }
        }

    }

    private JComponent createSplitterBetweenConditionAndFormatTitle() {
        JPanel result = new JPanel();
        result.setLayout(new BorderLayout(10, 10));

        boolean needsCondition = true;
        if (prop instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) prop).isLocal()) {
            needsCondition = false;
        }

        if (needsCondition) {
            jchbInheritCondition = new javax.swing.JCheckBox();
            jchbInheritCondition.setText("Inherit parent select condition");
            jchbInheritCondition.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (parentRefPropertyPresentation == null) {
                        return;
                    }
                    parentRefPropertyPresentation.getParentSelect().setParentSelectConditionInherited(jchbInheritCondition.isSelected());
                    update();
                }
            });
            propertyParentSelectorCondition = new ConditionsPanel();

            ComponentTitledBorder componentTitledBorder2 = new ComponentTitledBorder(
                    jchbInheritCondition,
                    propertyParentSelectorCondition,
                    javax.swing.BorderFactory.createTitledBorder("")) {
                        @Override
                        public Insets getBorderInsets(Component c) {
                            Insets insets = super.getBorderInsets(c);
                            if (insets == null) {
                                return null;
                            } else {
                                insets.left += 10;
                                insets.right += 10;
                                insets.bottom += 10;
                            }
                            return insets;
                        }
                    };

          //  JScrollPane scroller = new JScrollPane(propertyParentSelectorCondition);
            // scroller.setBorder(componentTitledBorder2);
            propertyParentSelectorCondition.setBorder(componentTitledBorder2);
            result.add(propertyParentSelectorCondition);
        }

        objectTitleFormatPanel = new ObjectTitleFormatPanel();
        jchbInheritFormat = new javax.swing.JCheckBox();
        jchbInheritFormat.setText("Inherit format titles");
        jchbInheritFormat.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (parentRefPropertyPresentation == null) {
                    return;
                }
                parentRefPropertyPresentation.getParentTitle().setParentTitleFormatInherited(jchbInheritFormat.isSelected());
                openFormatLocalizingPanel();
            }
        });

        ComponentTitledBorder componentTitledBorder3 = new ComponentTitledBorder(
                jchbInheritFormat,
                objectTitleFormatPanel,
                javax.swing.BorderFactory.createTitledBorder(""));
        objectTitleFormatPanel.setBorder(componentTitledBorder3);

        result.add(objectTitleFormatPanel, needsCondition ? BorderLayout.SOUTH : BorderLayout.CENTER);

        return result;
    }

    public void open(RadixObject definition, OpenInfo info) {

        if (!(definition instanceof AdsPropertyDef)) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
        prop = (AdsPropertyDef) definition;

        // isSqmlWillBeOpened = false;
        isReadOnly = prop.isReadOnly();

        AdsPropertyDef propForPresentation = getPropForPresentation();

        if (propForPresentation instanceof IAdsPresentableProperty) {
            sProp = (IAdsPresentableProperty) propForPresentation;
            //   localizingPaneList.open(new HandleInfoEx());
        }

        //if (!isBeRemoveTabs) {
//        createTabsAndPanels();***
        //    //    //            for (int i =0;i<6;i++)
        //    //    //                if (!visibleTab[i])
        //    //    //                    jMainTab.setTitleAt(i, null);
        //    jMainTab.getTabComponentAt(0);
        //    visibleTab[i] = true;
        //}
        createTabsAndPanels();
        tabbedPane.setUI(new WindowsTabbedPaneUIEx());
//        removeInvisible();
        tabbedPane.repaint();

        adsPropertyEditorMainPanel.open(prop, isReadOnly);
        adsPropertyEditorFieldParentRefPage.open(prop, isReadOnly);
        adsPropertyEditorFieldPage.open(prop, isReadOnly);

        update();

    }

//    private void removeInvisible() {
//        for (int tabIndex = tabbedPane.getTabCount() - 1; tabIndex >= 0; --tabIndex) {
//            if (!visibleTab[tabIndex]) {
//                tabbedPane.remove(tabIndex);
//            }
//        }
//    }
    public void update() {
        //if (isMayModify)return;

        isMayModify = false;

        createTabsAndPanels();

        if (onParentDeletionPanel != null && prop instanceof AdsUserPropertyDef) {
            onParentDeletionPanel.setOptions(((AdsUserPropertyDef) prop).getParentDeletionOptions());
        }

        adsPropertyEditorMainPanel.update();
        adsPropertyEditorFieldParentRefPage.update();
        adsPropertyEditorFieldPage.update();

        AdsPropertyDef ow2 = prop.getHierarchy().findOverwritten().get();
        isRealOverwriten = ow2 != null;
        if (isRealOverwriten) {
            ownerProp = ow2;
        }

        AdsPropertyDef ow = prop.getHierarchy().findOverridden().get();
        isRealOverriden = ow != null;
        if (ownerProp == null) {
            ownerProp = ow;
        }
        isRealOver = isRealOverriden || isRealOverwriten;
        isCanInherit = isRealOver || prop.getNature() == EPropNature.PARENT_PROP;
        presentableProperty = null;
        propertyPresentation = null;
        parentRefPropertyPresentation = null;

        if (eventCodes != null) {

            eventCodes.open(new MultilingualStringInfo(prop) {
                @Override
                public String getContextDescription() {
                    return "";
                }

                @Override
                public Id getId() {
                    return ((AdsEventCodePropertyDef) prop).getEventId();
                }

                @Override
                public EAccess getAccess() {
                    return EAccess.PUBLIC;
                }

                @Override
                public void updateId(Id newId) {
                    ((AdsEventCodePropertyDef) prop).setEventId(newId);
                }

                @Override
                public boolean isPublished() {
                    return true;
                }

                @Override
                public EMultilingualStringKind getKind() {
                    return EMultilingualStringKind.EVENT_CODE;
                }
                
            });
        }

//        if (prop instanceof AdsInnateRefPropertyDef) {
//            innateRefPropertyDef = (AdsInnateRefPropertyDef) prop;
//        }
        AdsPropertyDef propForPresentation = getPropForPresentation();
        if (propForPresentation instanceof IAdsPresentableProperty) {

            presentableProperty = (IAdsPresentableProperty) propForPresentation;
            //AdsPresentationPropertyDef ap = null;
            ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();
            if (presentationSupport != null) {
                propertyPresentation = presentationSupport.getPresentation();
            }

            if (propertyPresentation != null && propertyPresentation instanceof ParentRefPropertyPresentation) {
                parentRefPropertyPresentation = (ParentRefPropertyPresentation) propertyPresentation;

                if (parentRefPropertyPresentation != null) {
                    selRest = parentRefPropertyPresentation.getParentSelectorRestrictions();
                    edRest = parentRefPropertyPresentation.getParentEditorRestrictions();
                }
            }
        }
        refreshPropertyPresentationAndHintPanelsVisible();
        setPanels();

        if (valueInheritancePanel != null) {
            if (prop instanceof AdsTablePropertyDef) {
                valueInheritancePanel.open((AdsTablePropertyDef) prop);
                valueInheritancePanel.setReadOnly(isReadOnly);
            } else {
                valueInheritancePanel.setReadOnly(true);
            }
            if (valueInitializationModePanelForUserProps != null) {
                valueInitializationModePanelForUserProps.open((AdsTablePropertyDef) prop);
                valueInitializationModePanelForUserProps.setReadOnly(isReadOnly);
            }
            jchbInheritPath.setEnabled(!isReadOnly);
            jchbInheritPath.setSelected(prop.getValueInheritanceRules().getInheritable());
        }
        updateColor();

        if (prop instanceof AdsExpressionPropertyDef) {
            AdsExpressionPropertyDef expr = (AdsExpressionPropertyDef) prop;
            expressionPanel.open(expr.getExpresssion());
        }

        isMayModify = true;
    }
    
    private void updateColor(){
        if (jIsPresentableCheckBox != null) {
            int index = tabbedPane.indexOfComponent(presentationTabPanel);
            if (index > 0) {
                if (!jIsPresentableCheckBox.isSelected()){
                    tabbedPane.setForegroundAt(index, Color.GRAY);
                } else {
                    tabbedPane.setForegroundAt(index, tabbedPane.getForeground());
                }
            }
        }
        if (jchbInheritPath != null) {
            int index = tabbedPane.indexOfComponent(jValueInheritancePanel);
            if (index > 0) {
                if (!jchbInheritPath.isSelected()){
                    tabbedPane.setForegroundAt(index, Color.GRAY);
                } else {
                    tabbedPane.setForegroundAt(index, tabbedPane.getForeground());
                }
            }
        }
//        Sqml sqml = presentation.getCondition().getFrom();
//        if (sqml == null || sqml.getItems().isEmpty()){
//            tabs.setForegroundAt(1, Color.GRAY);
//        } else {
//            tabs.setForegroundAt(1, tabs.getForeground());
//        }
//        sqml = presentation.getCondition().getWhere();
//        if (sqml == null || sqml.getItems().isEmpty()){
//            tabs.setForegroundAt(0, Color.GRAY);
//        } else {
//            tabs.setForegroundAt(0, tabs.getForeground());
//        }
//        Prop2ValueMap prop2ValueMap = presentation.getCondition().getProp2ValueMap();
//        if (prop2ValueMap == null || prop2ValueMap.getItems().isEmpty()){
//            tabs.setForegroundAt(2, Color.GRAY);
//        } else {
//            tabs.setForegroundAt(2, tabs.getForeground());
//        }
    }

    private void updateEditingPanel() {

        if (sProp == null) {
            return;
        }

        if (sProp.getPresentationSupport() != null) {
            ServerPresentationSupport presentationSupport = sProp.getPresentationSupport();
            if (inheritableTitlePanel1 != null) {
                inheritableTitlePanel1.setReadonly(isReadOnly);
                inheritableTitlePanel1.open((AdsDefinition) sProp, false);

            }
            if (inheritableHintPanel1 != null) {
                inheritableHintPanel1.setReadonly(isReadOnly
                        || !presentationSupport.getPresentation().isPresentable());
                inheritableHintPanel1.open((AdsDefinition) sProp, true);
                inheritableHintPanel1.setExpandable(true);
            }
            PropertyEditOptions currEditOptions = null;
            currEditOptions = presentationSupport.getPresentation().getEditOptions();
            if (propertyPresentationEditPanel != null) {
                boolean isBad = !presentationSupport.getPresentation().isMayInheritEditOptions()
                        && presentationSupport.getPresentation().isEditOptionsInherited();
                if (isReadOnly) {
                    jchb.setEnabled(false);
                } else {
                    boolean isEnabled
                            = presentationSupport.getPresentation().isPresentable()
                            && (presentationSupport.getPresentation().isMayInheritEditOptions()
                            || isBad);
                    jchb.setEnabled(isEnabled);
                    Component rrr = jchb;

                    while (rrr != null) {
                        rrr.repaint();
                        rrr = rrr.getParent();
                    }

                }

                if (isBad) {
                    jchb.setForeground(Color.red);
                } else {
                    jchb.setForeground(Color.black);
                }

                jchb.setSelected(presentationSupport.getPresentation().isEditOptionsInherited());

                AdsPropertyDef p = prop;
                if (presentationSupport.getPresentation().isEditOptionsInherited()) {
                    PropertyPresentation pp = sProp.getPresentationSupport().getPresentation().findEditOptionsOwner();
                    if (pp != null) {
                        p = pp.getOwnerProperty();
                    }
                }

                boolean isReadOnlyEx = isReadOnly
                        || presentationSupport.getPresentation().isEditOptionsInherited()
                        || !propertyPresentation.isPresentable();

                boolean isPresentable = propertyPresentation.isPresentable();
                if (presentationTabbedPane != null) {
                    if (presentationTabbedPane.getTabCount() == 3) {
                        presentationTabbedPane.setEnabledAt(1, isPresentable);
                        presentationTabbedPane.setEnabledAt(2, isPresentable);
                    }
                }

                //propertyPresentationEditPanel.setReadOnly(isReadOnlyEx);
                propertyPresentationEditPanel.open(p, currEditOptions, p.getValue().getType());
                propertyPresentationEditPanel.setReadOnly(isReadOnlyEx);

                if (objectPropertyPresentationPanel != null) {
                    objectPropertyPresentationPanel.open(prop/*
                     * , currEditOptions, p.getValue().getType()
                     */);
                    objectPropertyPresentationPanel.setReadOnly(isReadOnly);
                }

                if (jIsPresentableCheckBox != null) {
                    jIsPresentableCheckBox.setSelected(propertyPresentation.isPresentable());
                    jIsPresentableCheckBox.setEnabled(!isReadOnly);
                }
            }
        }
        updateColor();

    }

    private void openFormatLocalizingPanel() {

        if (sProp == null || parentRefPropertyPresentation == null
                || jchbInheritFormat == null || objectTitleFormatPanel == null) {
            return;
        }

        jchbInheritFormat.setSelected(parentRefPropertyPresentation.getParentTitle().isParentTitleFormatInherited());

        AdsObjectTitleFormatDef tf = parentRefPropertyPresentation.getParentTitle().getTitleFormat();
        AdsClassDef cd = null;// = tf == null ? null : tf.getOwnerClass();

        AdsTypeDeclaration decl = prop.getValue().getType();
        if (decl.getTypeId() == EValType.PARENT_REF || decl.getTypeId() == EValType.ARR_REF) {
            AdsType type = decl.resolve(prop).get();
            if (type instanceof AdsClassType) {
                cd = ((AdsClassType) type).getSource();
            }
        } else {
            cd = prop.getOwnerClass();
        }

        jchbInheritFormat.setEnabled(!isReadOnly);
        if (parentRefPropertyPresentation.getParentTitle().isParentTitleFormatInherited() && tf == null) {

            jchbInheritFormat.setForeground(Color.red);

        } else {
            jchbInheritFormat.setForeground(Color.black);
        }

        if (cd == null || tf == null) {
            objectTitleFormatPanel.setReadonly(true);
        } else {
            formatPropList = cd.getProperties().get(EScope.ALL);
            IAdsPropertiesListProvider provider = new IAdsPropertiesListProvider() {
                @Override
                public List<AdsPropertyDef> getAdsPropertiesList() {
                    return formatPropList;
                }
            };
            objectTitleFormatPanel.open(tf, provider);
            objectTitleFormatPanel.setReadonly(isReadOnly || parentRefPropertyPresentation.getParentTitle().isParentTitleFormatInherited());
        }
    }

    private void updateParentTitlePanel() {
        if (parentRefPropertyPresentation == null) {
            return;
        }

        boolean isParentSelectConditionInherited = parentRefPropertyPresentation.getParentSelect().isParentSelectConditionInherited();
        boolean isParentSelectorInherited = parentRefPropertyPresentation.getParentSelect().isParentSelectorInherited();

        parentRefPropertyPresentation.getParentSelect().findParentSelectorPresentation();
        boolean findCondition = !isParentSelectConditionInherited;

        if (parentSelectopPresentationPanel != null) {
            parentSelectopPresentationPanel.setReadOnly(isReadOnly || isParentSelectorInherited);
            parentSelectopPresentationPanel.open(prop, parentRefPropertyPresentation.getParentSelect());
        }

        if (parentRefPropertyPresentation.getParentSelect().getParentSelectorPresentationId() == null && isParentSelectorInherited) {
            jchbInheritParentSelector.setForeground(Color.red);
        } else {
            jchbInheritParentSelector.setForeground(Color.black);
        }
        jchbInheritParentSelector.setSelected(isParentSelectorInherited);

        if (propertyParentSelectorCondition != null) {
            if (jchbInheritCondition != null) {
                if (isParentSelectConditionInherited && !isCanInherit) {
                    jchbInheritCondition.setForeground(Color.red);
                    jchbInheritCondition.setEnabled(!isReadOnly);
                } else {
                    jchbInheritCondition.setEnabled(!isReadOnly && isCanInherit);
                    jchbInheritCondition.setForeground(Color.black);
                }
                jchbInheritCondition.setSelected(isParentSelectConditionInherited);
            }

            ParentRefPropertyPresentation.ParentSelect conditionProvider = null;
            if (isParentSelectConditionInherited) {
                IAdsPresentableProperty aProp = sProp;
                while (aProp != null) {
                    PropertyPresentation aPropertyPresentation = aProp.getPresentationSupport().getPresentation();
                    if (aPropertyPresentation == null || !(aPropertyPresentation instanceof ParentRefPropertyPresentation)) {
                        break;
                    }

                    ParentRefPropertyPresentation aParentRefPropertyPresentation = (ParentRefPropertyPresentation) aPropertyPresentation;
                    if (!findCondition && aParentRefPropertyPresentation.getParentSelect().isParentSelectConditionInherited()) {
                        conditionProvider = aParentRefPropertyPresentation.getParentSelect();
                        findCondition = true;
                        break;
                    }
                    aProp = (IAdsPresentableProperty) ((AdsPropertyDef) aProp).getHierarchy().findOverwritten();
                }
            } else {
                if (!isParentSelectConditionInherited) {
                    conditionProvider = parentRefPropertyPresentation.getParentSelect();
                }
            }

            if (conditionProvider != null) {
                propertyParentSelectorCondition.open(conditionProvider);
            }
            propertyParentSelectorCondition.setReadOnly(isReadOnly || isParentSelectConditionInherited || conditionProvider == null);
        }

    }

    private void setPanels() {
        if (valueInheritancePanel != null) {
            valueInheritancePanel.setValue(prop, prop.getValue().getInitial() == null ? null : prop.getValue().getInitial().getValAsStr());
        }

        if (panelPropertyRestrictions != null) {
            panelPropertyRestrictions.open(prop, parentRefPropertyPresentation, selRest, edRest, isReadOnly);
        }
        if (jchbInheritParentSelector != null) {
            jchbInheritParentSelector.setEnabled(!isReadOnly);
        }

        updateEditingPanel();

        updateParentTitlePanel();

        openFormatLocalizingPanel();
    }

    private void createMainTab() {

        tabbedPane = new JTabbedPane();

        mainTabPanel = new JPanel();
        mainPanelScrollPane = new JScrollPane();
        jValueInheritancePanel = new JPanel();
        jValueInheritancePanelContent = new JPanel();
        presentationTabPanel = new JPanel();
        referencePropertiesTabPanel = new JPanel();
        referencePropertiesTabPanelScrollPane = new JScrollPane();
        typeTabPanel = new JPanel();
        typeTabPanelScrollPane = new JScrollPane();
        expressionTabPanel = new JPanel();

        tabbedPane.setAutoscrolls(true);
        tabbedPane.setMinimumSize(new java.awt.Dimension(0, 0));
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                //jMainTabStateChanged(evt);
                if (isMayModify) {
                    iLastVisiblePage = tabbedPane.getSelectedIndex();
                }
            }
        });

        GroupLayout jMainPanelLayout = new GroupLayout(mainTabPanel);
        mainTabPanel.setLayout(jMainPanelLayout);
        jMainPanelLayout.setHorizontalGroup(
                jMainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(mainPanelScrollPane, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE));
        jMainPanelLayout.setVerticalGroup(
                jMainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(mainPanelScrollPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE));

        tabbedPane.addTab("Main", mainTabPanel);

        GroupLayout jValueInheritancePanelContentLayout = new GroupLayout(jValueInheritancePanelContent);
        jValueInheritancePanelContent.setLayout(jValueInheritancePanelContentLayout);
        jValueInheritancePanelContentLayout.setHorizontalGroup(
                jValueInheritancePanelContentLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 377, Short.MAX_VALUE));
        jValueInheritancePanelContentLayout.setVerticalGroup(
                jValueInheritancePanelContentLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 212, Short.MAX_VALUE));

        GroupLayout jValueInheritancePanelLayout = new GroupLayout(jValueInheritancePanel);
        jValueInheritancePanel.setLayout(jValueInheritancePanelLayout);
        jValueInheritancePanelLayout.setHorizontalGroup(
                jValueInheritancePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jValueInheritancePanelContent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jValueInheritancePanelLayout.setVerticalGroup(
                jValueInheritancePanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jValueInheritancePanelLayout.createSequentialGroup().addComponent(jValueInheritancePanelContent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(354, Short.MAX_VALUE)));

        tabbedPane.addTab("Inheritance", jValueInheritancePanel);

        //jPanel24.setPreferredSize(new java.awt.Dimension(395, 700));
        //jValueInheritancePanel.setPreferredSize(new java.awt.Dimension(395, 700));
        final GroupLayout presentationTabPanelLayout = new GroupLayout(presentationTabPanel);
        presentationTabPanel.setLayout(presentationTabPanelLayout);
        presentationTabPanelLayout.setHorizontalGroup(
                presentationTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 377, Short.MAX_VALUE));
        presentationTabPanelLayout.setVerticalGroup(
                presentationTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 566, Short.MAX_VALUE));

        tabbedPane.addTab("Presentation", presentationTabPanel);

        final GroupLayout referencePropertiesTabPanelLayout = new GroupLayout(referencePropertiesTabPanel);
        referencePropertiesTabPanel.setLayout(referencePropertiesTabPanelLayout);
        referencePropertiesTabPanelLayout.setHorizontalGroup(
                referencePropertiesTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(referencePropertiesTabPanelScrollPane, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE));
        referencePropertiesTabPanelLayout.setVerticalGroup(
                referencePropertiesTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(referencePropertiesTabPanelScrollPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE));

        tabbedPane.addTab("Reference Properties", referencePropertiesTabPanel);

        final GroupLayout typeTabPanelLayout = new GroupLayout(typeTabPanel);
        typeTabPanel.setLayout(typeTabPanelLayout);
        typeTabPanelLayout.setHorizontalGroup(
                typeTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(typeTabPanelScrollPane, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE));
        typeTabPanelLayout.setVerticalGroup(
                typeTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(typeTabPanelScrollPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE));

        tabbedPane.addTab("Type", typeTabPanel);

        final GroupLayout expressionTabPanelLayout = new GroupLayout(expressionTabPanel);
        expressionTabPanel.setLayout(expressionTabPanelLayout);
        expressionTabPanelLayout.setHorizontalGroup(
                expressionTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 377, Short.MAX_VALUE));
        expressionTabPanelLayout.setVerticalGroup(
                expressionTabPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 566, Short.MAX_VALUE));

        tabbedPane.addTab("Expression", expressionTabPanel);

        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE));

        if (prop.getNature() == EPropNature.EVENT_CODE) {
            eventCodeTab = new JPanel();
            tabbedPane.addTab("Event Code", eventCodeTab);
            eventCodeTab.setLayout(new BorderLayout());
            eventCodes = new EventCodeEditPanel();
            eventCodeTab.add(eventCodes, BorderLayout.CENTER);
            visibleTab[6] = true;
        }

        if (prop.getNature() == EPropNature.PROPERTY_PRESENTATION) {
            PropertyDependenciesPanel dependents = new PropertyDependenciesPanel();
            dependents.open((AdsPropertyPresentationPropertyDef) prop);
            tabbedPane.addTab("Dependent items", dependents);
            visibleTab[6] = true;
            visibleTab[7] = true;
        }
    }
}

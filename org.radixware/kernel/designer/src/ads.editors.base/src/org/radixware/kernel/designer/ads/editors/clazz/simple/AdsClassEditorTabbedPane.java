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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef.ClassSource;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.ads.editors.clazz.enumeration.AdsEnumClassEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel.TopComponent;
import org.radixware.kernel.designer.common.dialogs.components.ComponentTitledBorder;
import org.radixware.kernel.designer.common.dialogs.components.TabManager;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfoAdapter;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizingStringContext;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingStringContextFactory;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation;
import org.radixware.kernel.designer.common.editors.AdsDefinitionIconPresentation.IconIdChangeEvent;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


final class AdsClassEditorTabbedPane extends JPanel {

    private enum ETabs {

        GENERAL("General"),
        HEADER("Header"),
        BODY("Body"),
        ACCESS_AREAS("Access Areas"),
        PRESENTATION("Presentation"),
        ENTITY("Entity"),
        PRESENTATIONS("Presentations"),
        STRUCTURE_PARAMETERS("Structure Parameters"),
        RESOURCES("Resources");
        private final String key;

        private ETabs(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private abstract class SourceEditorTab extends TabManager.TabAdapter {

        private final EmbeddedSourceEditor sourcePanel = new EmbeddedSourceEditor();
        private JLabel tabComponent;
        private final String name;

        SourceEditorTab(String name) {
            this.name = name;
        }

        @Override
        public void initTab() {

            getSource().ensureFirst().getItems().getContainerChangesSupport().addEventListener(new RadixObjects.ContainerChangesListener() {
                @Override
                public void onEvent(final ContainerChangedEvent e) {
                    final Jml sourceCode = getSource().ensureFirst();
                    tabComponent.setText(grayIfEmpty(name, sourceCode));
                }
            });

            openSourceEditor(null);

            if (AdsTransparence.isTransparent(sourceClass, true)) {
                sourcePanel.setEditable(false);
            }
        }

        @Override
        public void updateTab() {
            sourcePanel.update();
        }

        @Override
        public void setReadonlyTab(boolean readonly) {
            sourcePanel.setEditable(!readonly);
        }

        @Override
        public String getTabName() {
            return setupTabTitle(name, sourceClass.getHeader().ensureFirst());
        }

        @Override
        public JComponent getTabComponent() {
            return sourcePanel;
        }

        @Override
        protected void added() {
            final int index = tabManager.indexOf(getTabKey());
            tabComponent = new JLabel(grayIfEmpty(name, getSource().ensureFirst()));
            tabManager.getTabbedPane().setTabComponentAt(index, tabComponent);
        }

        void openSourceEditor(OpenInfo openInfo) {
            sourcePanel.open(getSource(), openInfo);
        }

        abstract ClassSource getSource();
    }
    private TabManager tabManager;
    private AdsClassDef sourceClass = null;
    boolean isInitialized = false;

    AdsClassEditorTabbedPane() {
        super();
        setLayout(new BorderLayout());
    }
    
    private void initTabs() {

        final boolean isEntityClass = sourceClass.getClassDefType() == EClassType.ENTITY;
        final boolean isTransparent = AdsTransparence.isTransparent(sourceClass);
        final boolean isExtendable = AdsTransparence.isTransparent(sourceClass, true);

        tabManager = new TabManager(new JTabbedPane());

        add(tabManager.getTabbedPane(), BorderLayout.CENTER);

        tabManager.addTab(new TabManager.TabAdapter() {
            private AdsClassEditorGeneralPanel classEditorGeneralPanel = new AdsClassEditorGeneralPanel();
            private JScrollPane scrollPane = new JScrollPane(classEditorGeneralPanel);

            @Override
            public void initTab() {
                classEditorGeneralPanel.open(sourceClass);
            }

            @Override
            public String getTabName() {
                return wrapToHtml("General");
            }

            @Override
            public String getTabKey() {
                return ETabs.GENERAL.getKey();
            }

            @Override
            public JComponent getTabComponent() {
                return scrollPane;
            }

            @Override
            public void updateTab() {
                classEditorGeneralPanel.update();
            }

            @Override
            public void setReadonlyTab(boolean readonly) {
                classEditorGeneralPanel.setEnabled(!readonly);
            }
        });

        if (!isTransparent || isExtendable) {
            tabManager.addTab(new SourceEditorTab("Header") {
                @Override
                ClassSource getSource() {
                    return sourceClass.getHeader();
                }

                @Override
                public String getTabKey() {
                    return ETabs.HEADER.getKey();
                }
            });

            tabManager.addTab(new SourceEditorTab("Body") {
                @Override
                ClassSource getSource() {
                    return sourceClass.getBody();
                }

                @Override
                public String getTabKey() {
                    return ETabs.BODY.getKey();
                }
            });
        }

        if (!isTransparent) {

            if (sourceClass.getClassDefType() == EClassType.ENTITY) {
                tabManager.addTab(new TabManager.TabAdapter() {
                    private AccessAreasPanel tab = new AccessAreasPanel();
                    private JScrollPane component = new JScrollPane(tab);

                    @Override
                    public void initTab() {
                        tab.open((AdsEntityClassDef) sourceClass);
                    }

                    @Override
                    public void updateTab() {
                        tab.update();
                    }

                    @Override
                    public void setReadonlyTab(boolean readonly) {
                        tab.setReadonly(readonly);
                    }

                    @Override
                    public String getTabName() {
                        return wrapToHtml("Access Areas");
                    }

                    @Override
                    public JComponent getTabComponent() {
                        return component;
                    }

                    @Override
                    public String getTabKey() {
                        return ETabs.ACCESS_AREAS.getKey();
                    }
                });
            }

            if (sourceClass.getClassDefType() == EClassType.APPLICATION
                    || sourceClass.getClassDefType() == EClassType.ENTITY) {
                tabManager.addTab(new TabManager.TabAdapter() {
                    private JPanel entityPanel = new JPanel();
                    private HandleInfo titlesForPluralHandleInfo;
                    private HandleInfo titlesForSingleHandleInfo;
                    private TitleInherited titlesForPluralPanel = null;
                    private TitleInherited titlesForSinglePanel = null;
                    private AdsDefinitionIconPresentation iconPanel = null;
                    private DefaultSelectorPresentationPanel defaultSelectorPresentationPanel = null;
                    private ObjectTitleFormatPanel objectTitleFormatPanel = null;
                    private JScrollPane component = new JScrollPane(entityPanel);
                    private JCheckBox inheritFormatCheckBox = null;
                    private ComponentTitledBorder componentTitledBorder = null;
                    
                    
                    final IAdsPropertiesListProvider adsPropertiesListProvider =  new IAdsPropertiesListProvider() {
                                private final List<EValType> forbiddenValTypes = new ArrayList<>(17);

                                {
                                    forbiddenValTypes.add(EValType.ARR_BIN);
                                    forbiddenValTypes.add(EValType.ARR_BLOB);
                                    forbiddenValTypes.add(EValType.ARR_BOOL);
                                    forbiddenValTypes.add(EValType.ARR_CHAR);
                                    forbiddenValTypes.add(EValType.ARR_CLOB);
                                    forbiddenValTypes.add(EValType.ARR_DATE_TIME);
                                    forbiddenValTypes.add(EValType.ARR_INT);
                                    forbiddenValTypes.add(EValType.ARR_NUM);
                                    //forbiddenValTypes.add(EValType.ARR_OBJECT);
                                    forbiddenValTypes.add(EValType.ARR_REF);
                                    forbiddenValTypes.add(EValType.ARR_STR);

                                    forbiddenValTypes.add(EValType.BLOB);
                                    forbiddenValTypes.add(EValType.BIN);
                                    forbiddenValTypes.add(EValType.JAVA_CLASS);
                                    forbiddenValTypes.add(EValType.JAVA_TYPE);
                                    forbiddenValTypes.add(EValType.NATIVE_DB_TYPE);
                                    forbiddenValTypes.add(EValType.USER_CLASS);
                                    forbiddenValTypes.add(EValType.XML);
                                }
                                    
                                private EValType getValTypeOfProperty(AdsPropertyDef property) {
                                    return property.getValue().getType().getTypeId();
                                }

                                @Override
                                public List<AdsPropertyDef> getAdsPropertiesList() {

                                    final List<AdsPropertyDef> sourceList = sourceClass.getProperties().get(EScope.ALL);
                                    final List<AdsPropertyDef> resultList = new ArrayList<>();

                                    for (AdsPropertyDef xProperty : sourceList) {
                                        if (!forbiddenValTypes.contains(getValTypeOfProperty(xProperty))) {
                                            resultList.add(xProperty);
                                        }
                                    }

                                    return resultList;
                                }
                    };
                    
                    @Override
                    public void initTab() {
                        
                        final boolean isOverwrite = sourceClass.getHierarchy().findOverwritten().get() != null;
                        final boolean readonly = sourceClass.isReadOnly() || isOverwrite;

                        entityPanel.setLayout(new BoxLayout(entityPanel, BoxLayout.Y_AXIS));
                        entityPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

                        // titles panels
                        titlesForSinglePanel = new TitleInherited("Title for singular", false) {
                            @Override
                            public Dimension getMaximumSize() {
                                return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
                            }
                        };

                        titlesForSingleHandleInfo = new HandleInfo() {                          
                            @Override
                            public AdsDefinition getAdsDefinition() {
                                return ((AdsEntityObjectClassDef) sourceClass).getPresentations().findOwnerTitleDefinition();
                            }

                            @Override
                            public Id getTitleId() {
                                return ((AdsEntityObjectClassDef) sourceClass).getPresentations().getObjectTitleId();
                            }

                            @Override
                            protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                                if (multilingualStringDef != null) {
                                    ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitleId(multilingualStringDef.getId());
                                } else {
                                    ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitleId(null);
                                }
                            }

                            @Override
                            protected void onRemoveString() {
                                ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitleId(null);
                            }
                            
                            
                            @Override
                            protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                                ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitle(language, newStringValue);
                            }

                            @Override
                            public IMultilingualStringDef getAdsMultilingualStringDef() {
                                Id titleId = ((AdsEntityObjectClassDef) sourceClass).getPresentations().getObjectTitleId();
                                if (titleId != null) {
                                    return getAdsDefinition().findLocalizedString(titleId);
                                }
                                return super.getAdsMultilingualStringDef();
                            }
                            
                        };
                        
                        titlesForSinglePanel.open(titlesForSingleHandleInfo);
                        titlesForSinglePanel.setAlignmentX(0.0f);
                        entityPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                        entityPanel.add(MainPanel.createTunedEnclosedPanel(titlesForSinglePanel, 2, 2), Box.TOP_ALIGNMENT);

                        if (isEntityClass) {
                            titlesForPluralPanel = new TitleInherited("Title for plural", true) {
                                @Override
                                public Dimension getMaximumSize() {
                                    return new Dimension(super.getMaximumSize().width, getPreferredSize().height);
                                }
                            };
                            titlesForPluralHandleInfo = new HandleInfo() {
                                @Override
                                public AdsDefinition getAdsDefinition() {
                                    return sourceClass;
                                }

                                @Override
                                public Id getTitleId() {
                                    return ((AdsEntityClassDef) sourceClass).getPresentations().getEntityTitleId();
                                }

                                @Override
                                protected void onAdsMultilingualStringDefChange(IMultilingualStringDef multilingualStringDef) {
                                    if (multilingualStringDef != null) {
                                        ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitleId(multilingualStringDef.getId());
                                    } else {
                                        ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitleId(null);
                                    }
                                }

                                @Override
                                protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
                                    ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitle(language, newStringValue);
                                }
                                
                                @Override
                                protected void onRemoveString() {
                                    ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitleId(null);
                                }
                                
                                @Override
                                public IMultilingualStringDef getAdsMultilingualStringDef() {
                                    Id titleId = ((AdsEntityClassDef) sourceClass).getPresentations().getEntityTitleId();
                                    if (titleId != null) {
                                        return getAdsDefinition().findLocalizedString(titleId);
                                    }
                                    return super.getAdsMultilingualStringDef();
                                }
                            };
                            titlesForPluralPanel.open(titlesForPluralHandleInfo);
                            titlesForPluralPanel.setAlignmentX(0.0f);

                            entityPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                            entityPanel.add(MainPanel.createTunedEnclosedPanel(titlesForPluralPanel, 2, 2), Box.TOP_ALIGNMENT);

                            // icon panel
                            iconPanel = new AdsDefinitionIconPresentation();
                            iconPanel.setAlignmentY(0.0F);
                            iconPanel.setAlignmentX(0.0f);
                            Border border = new TitledBorder("Icon");
                            iconPanel.setBorder(BorderFactory.createCompoundBorder(border, 
            BorderFactory.createEmptyBorder(0, 0, 10, 0)));
                            System.out.println("insets : " + iconPanel.getInsets());
                            iconPanel.getIconIdChangeSupport().addEventListener(new AdsDefinitionIconPresentation.IconIdStateChangeListener() {
                                @Override
                                public void onEvent(IconIdChangeEvent e) {
                                    ((AdsEntityClassDef) sourceClass).getPresentations().setIconId(e.iconId);
                                }
                            });
                            iconPanel.open((AdsEntityClassDef) sourceClass, ((AdsEntityClassDef) sourceClass).getPresentations().getIconId());
                            iconPanel.setReadonly(readonly);
                            entityPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                            entityPanel.add(iconPanel);

                            // default selector presentation panel
                            defaultSelectorPresentationPanel = new DefaultSelectorPresentationPanel(((AdsEntityClassDef) sourceClass).getPresentations());
                            defaultSelectorPresentationPanel.setAlignmentX(0.0f);
                            defaultSelectorPresentationPanel.open();
                            entityPanel.add(MainPanel.createTunedEnclosedPanel(defaultSelectorPresentationPanel,
                                    3, 4));

                            // object title format panel
                            objectTitleFormatPanel = new ObjectTitleFormatPanel();
                            objectTitleFormatPanel.setAlignmentX(0.0f);

                            entityPanel.add(MainPanel.createTunedEnclosedPanel(objectTitleFormatPanel, 2, 2));
                            final EntityPresentations entityPresentations = ((AdsEntityClassDef) sourceClass).getPresentations();
                            final AdsObjectTitleFormatDef objectTitleFormat = entityPresentations.getObjectTitleFormat();

                            if (isOverwrite || sourceClass.isOverwrite()) {    
                                initInheritFormatCheckBox(entityPresentations);
                                openTitleFormatPanel();
                            } else {
                                objectTitleFormatPanel.open(objectTitleFormat,adsPropertiesListProvider);
                            }
                        }
                    }
                   
                   private void initInheritFormatCheckBox(final EntityPresentations entityPresentations){
                        inheritFormatCheckBox = new javax.swing.JCheckBox();
                        inheritFormatCheckBox.setSelected(entityPresentations.isObjectTitleFormatInherited());
                        objectTitleFormatPanel.setReadonly(entityPresentations.isObjectTitleFormatInherited());
                        inheritFormatCheckBox.setText("Inherit object format title");
                                
                        inheritFormatCheckBox.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        if (entityPresentations == null) {
                                            return;
                                        }
                                        openTitleFormatPanel();
                                    }
                        });
                        componentTitledBorder = new ComponentTitledBorder(
                                        inheritFormatCheckBox,
                                        objectTitleFormatPanel,
                                        javax.swing.BorderFactory.createTitledBorder(""));
                        objectTitleFormatPanel.setBorder(componentTitledBorder);
                    }
                   private void openTitleFormatPanel(){
                        final EntityPresentations entityPresentations = ((AdsEntityClassDef) sourceClass).getPresentations();
                        
                        if (inheritFormatCheckBox == null){
                            initInheritFormatCheckBox(entityPresentations);
                        }
                        
                        entityPresentations.setTitleFormatInherited(inheritFormatCheckBox.isSelected());
                        AdsObjectTitleFormatDef tf = entityPresentations.getObjectTitleFormat();
                                     
                        if (tf != null){
                            objectTitleFormatPanel.setReadonly(entityPresentations.isObjectTitleFormatInherited());
                            objectTitleFormatPanel.open(tf, adsPropertiesListProvider);
                        }
                        
                        final AdsClassDef overwritten = sourceClass.getHierarchy().findOverwritten().get();
                        if (overwritten != null) {
                            if (!inheritFormatCheckBox.isVisible()){
                                objectTitleFormatPanel.setBorder(componentTitledBorder);
                            }
                            inheritFormatCheckBox.setVisible(true);
                            inheritFormatCheckBox.setForeground(Color.BLACK);
                        } else if (entityPresentations.isObjectTitleFormatInherited()) {
                                inheritFormatCheckBox.setVisible(true);
                                inheritFormatCheckBox.setForeground(Color.RED);
                                
                            } else {
                                inheritFormatCheckBox.setVisible(false);
                                String objectTitleFormatPanelTitle = org.openide.util.NbBundle.getMessage(ObjectTitleFormatPanel.class, "ObjectTitleFormatPanel.border.outsideBorder.title");
                                objectTitleFormatPanel.setBorder(BorderFactory.createTitledBorder(objectTitleFormatPanelTitle));
                            }
                        }
                    
                    @Override
                    public void updateTab() {
                        final boolean isOverwrite = sourceClass.getHierarchy().findOverwritten().get() != null;
                        final boolean readonly = sourceClass.isReadOnly() || isOverwrite;
                       
                        
                        titlesForSinglePanel.update(titlesForSingleHandleInfo);
                        if (isEntityClass) {
                            titlesForPluralPanel.update(titlesForPluralHandleInfo);
                            iconPanel.update();
                            
                            iconPanel.setReadonly(readonly);
                            defaultSelectorPresentationPanel.update();
                            if (isOverwrite || sourceClass.isOverwrite()){
                                openTitleFormatPanel();
                            
                            } else{
                                objectTitleFormatPanel.update();
                            }
                        }
                    }

                    @Override
                    public void setReadonlyTab(boolean readonly) {

                        titlesForSinglePanel.setReadonly(readonly);
                        if (isEntityClass) {
                            titlesForPluralPanel.setReadonly(readonly);
                            iconPanel.setReadonly(readonly);
                            defaultSelectorPresentationPanel.setReadonly(readonly);
                            objectTitleFormatPanel.setReadonly(readonly || sourceClass.isOverwrite());
                        }
                    }

                    @Override
                    public String getTabName() {
                        return wrapToHtml("Presentation");
                    }

                    @Override
                    public JComponent getTabComponent() {
                        return component;
                    }

                    @Override
                    public String getTabKey() {
                        return ETabs.PRESENTATION.getKey();
                    }
                });
            }

            if (sourceClass.getClassDefType() == EClassType.ENTITY_GROUP
                    || sourceClass.getClassDefType() == EClassType.PRESENTATION_ENTITY_ADAPTER) {
                tabManager.addTab(new TabManager.TabAdapter() {
                    private EntityInfoPanel entityInfoPanel = new EntityInfoPanel();

                    @Override
                    public void initTab() {

                        entityInfoPanel.setAlignmentX(0.0f);
                        entityInfoPanel.open((AdsEntityBasedClassDef) sourceClass);
                    }

                    @Override
                    public void updateTab() {
                        entityInfoPanel.update();
                    }

                    @Override
                    public void setReadonlyTab(boolean readonly) {
                        entityInfoPanel.setReadonly(readonly);
                    }

                    @Override
                    public String getTabName() {
                        return wrapToHtml("Entity");
                    }

                    @Override
                    public JComponent getTabComponent() {
                        return entityInfoPanel;
                    }

                    @Override
                    public String getTabKey() {
                        return ETabs.ENTITY.getKey();
                    }
                });
            }
        }

        if (sourceClass.getClassDefType() == EClassType.FORM_HANDLER) {
            tabManager.addTab(new TabManager.TabAdapter() {
                private FormPresentationsPanel tab = new FormPresentationsPanel();

                @Override
                public void initTab() {
                    tab.open(((AdsFormHandlerClassDef) sourceClass).getPresentations());
                }

                @Override
                public void updateTab() {
                    tab.update();
                }

                @Override
                public void setReadonlyTab(boolean readonly) {
                    tab.setReadonly(readonly);
                }

                @Override
                public String getTabName() {
                    return wrapToHtml("Presentations");
                }

                @Override
                public JComponent getTabComponent() {
                    return tab;
                }

                @Override
                public String getTabKey() {
                    return ETabs.PRESENTATIONS.getKey();
                }
            });
        }

        if (sourceClass.getClassDefType() == EClassType.ENUMERATION) {
            tabManager.addTab(new TabManager.TabAdapter() {
                private AdsEnumClassEditorPanel tab = new AdsEnumClassEditorPanel();
                private JScrollPane component = new JScrollPane(tab);

                @Override
                public void initTab() {
                    tab.open((AdsEnumClassDef) sourceClass);
                }

                @Override
                public void updateTab() {
                    tab.update();
                }

                @Override
                public void setReadonlyTab(boolean readonly) {
                    tab.setReadonly(readonly);
                }

                @Override
                public String getTabName() {
                    return wrapToHtml("Structure Parameters ");
                }

                @Override
                public JComponent getTabComponent() {
                    return component;
                }

                @Override
                public String getTabKey() {
                    return ETabs.STRUCTURE_PARAMETERS.getKey();
                }
            });
        }
        if (!sourceClass.isInner() && (!isTransparent || isExtendable)) {
            tabManager.addTab(new TabManager.TabAdapter() {
                private AdsClassResourcesPanel panel = new AdsClassResourcesPanel();
                private JLabel tabComponent;

                @Override
                public String getTabName() {

                    return wrapToHtml("Resources");
                }

                @Override
                public JComponent getTabComponent() {
                    return panel;
                }

                @Override
                public void initTab() {
                    sourceClass.getResources().getContainerChangesSupport().addEventListener(new RadixObjects.ContainerChangesListener() {
                        @Override
                        public void onEvent(final ContainerChangedEvent e) {
                            tabComponent.setText(grayIfEmpty("Resources", sourceClass.getResources()));
                        }
                    });
                    panel.open(sourceClass);
                }

                @Override
                public void updateTab() {
                    panel.update();
                }

                @Override
                protected void added() {
                    final int index = tabManager.indexOf(getTabKey());
                    tabComponent = new JLabel(grayIfEmpty("Resources", sourceClass.getResources()));
                    tabManager.getTabbedPane().setTabComponentAt(index, tabComponent);
                }
            });
        }
    }

    public void open(final AdsClassDef adsClassDef) {
        if (!isInitialized) {
            this.sourceClass = adsClassDef;

            initTabs();

            tabManager.setSelectedTab(ETabs.GENERAL.getKey());

            isInitialized = true;
        }
    }

    public void openHeaderTab(OpenInfo openInfo) {
        tabManager.setSelectedTab(ETabs.HEADER.getKey());
        ((SourceEditorTab) tabManager.getTab(ETabs.HEADER.getKey())).openSourceEditor(openInfo);
    }

    public void openBodyTab(OpenInfo openInfo) {
        tabManager.setSelectedTab(ETabs.BODY.getKey());
        ((SourceEditorTab) tabManager.getTab(ETabs.BODY.getKey())).openSourceEditor(openInfo);
    }

    public void update() {
        tabManager.updateSelectedTab();
        setReadonly(sourceClass.isReadOnly());
    }

    public void setReadonly(final boolean readonly) {

        if (!readonly && sourceClass.isReadOnly()) {
            return;
        }

        for (final TabManager.TabAdapter tab : tabManager.getTabs()) {
            if (!tab.getTabKey().equals("Presentation")) {
                tab.setReadonlyTab(readonly);
            }
        }
    }

    private static String setupTabTitle(final String title, Jml content) {
        if (content != null && content.getItems().size() > 0) {
            return title;
        }
        return getEmptyTitle(title);
    }

    private static String getEmptyTitle(String title) {
        return "<html><body><font color=\"#a9a9a9\">" + title + "</font></body></html>";
    }

    private static String grayIfEmpty(final String title, Jml content) {
        if (content != null && content.getItems().size() > 0) {
            return "<html><body><font color=\"black\">" + title + "</font></body></html>";
        }
        return "<html><body><font color=\"#a9a9a9\">" + title + "</font></body></html>";
    }

    private static String grayIfEmpty(final String title, AdsClassDef.Resources content) {
        if (content != null && !content.isEmpty()) {
            return "<html><body><font color=\"black\">" + title + "</font></body></html>";
        }
        return "<html><body><font color=\"#a9a9a9\">" + title + "</font></body></html>";
    }

    private static String wrapToHtml(final String title) {
        return "<html><body><font color=\"black\">" + title + "</font></body></html>";
    }
     
    private class TitleInherited extends LocalizingEditorPanel{
        private final JCheckBox chkInherit;
        private boolean isUpdate = false;
        private boolean isPluralPanel = false;
        private boolean isReadonly = false;
        private AdsMultilingualStringDef newTitle;
        
        public TitleInherited(String title, boolean pluar){
            this(title, "Inherit " + title.toLowerCase(), pluar);
        }
        
        public TitleInherited(String title, String inheritCheckBoxText, boolean plural){ 
            super(title);
            this.isPluralPanel = plural;
            chkInherit = new JCheckBox(inheritCheckBoxText);
            chkInherit.setFocusable(false);
            if (isPluralPanel){
                isReadonly = ((AdsEntityClassDef) sourceClass).getPresentations().isEntityTitleInherit();
            } else {
                isReadonly = ((AdsEntityObjectClassDef) sourceClass).getPresentations().isObjectTitleInherited();
            }
            chkInherit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!isUpdate) {
                            if (getLocalizingStringContext() == null || !(getLocalizingStringContext() instanceof HandleInfoAdapter)) {
                                return;
                            }
                            final HandleInfoAdapter handleInfoAdapter = (HandleInfoAdapter) getLocalizingStringContext();
                            update(handleInfoAdapter.getHandleInfo());
                        }
                    }
                });
            chkInherit.setSelected(isReadonly);   
        }
        private void updatePluralPanel() {
            boolean isInherit = ((AdsEntityClassDef) sourceClass).getPresentations().isEntityTitleInherit();
            ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitleInherited(chkInherit.isSelected());
            if (!chkInherit.isSelected()) {
                if (isInherit){
                    newTitle = AdsMultilingualStringDef.Factory.newInstance();
                    ((AdsEntityClassDef) sourceClass).getPresentations().setEntityTitleId(newTitle.getId());
                    ((AdsEntityClassDef) sourceClass).getDefinition().findLocalizingBundle().getStrings().getLocal().add(newTitle);
                    isReadonly = false;
                }
            } else {
                final ILocalizingBundleDef bundle = ((AdsEntityClassDef) sourceClass).getDefinition().findLocalizingBundle();
                if (newTitle != null){
                    bundle.getStrings().getLocal().remove(newTitle);
                    newTitle = null;
                }
                isReadonly = true;
            }
        }
        
        private void updateSingularPanel() {
            boolean isInherit = ((AdsEntityObjectClassDef) sourceClass).getPresentations().isObjectTitleInherited();
            ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitleInherited(chkInherit.isSelected());
            if (!chkInherit.isSelected()) {
                if (isInherit){
                    newTitle = AdsMultilingualStringDef.Factory.newInstance();
                    ((AdsEntityObjectClassDef) sourceClass).getPresentations().setObjectTitleId(newTitle.getId());
                    ((AdsEntityObjectClassDef) sourceClass).getDefinition().findLocalizingBundle().getStrings().getLocal().add(newTitle);
                    isReadonly = false;
                }
            } else {
                final ILocalizingBundleDef bundle = ((AdsEntityObjectClassDef) sourceClass).getDefinition().findLocalizingBundle();
                if (newTitle != null){
                    bundle.getStrings().getLocal().remove(newTitle);
                    newTitle = null;
                }
                isReadonly = true;
            }
        }
        
        private void initComponent(){
            final JPanel editorComponent = getEditor().getComponent();
            if (editorComponent instanceof BorderedCollapsablePanel) {
                final BorderedCollapsablePanel cp = (BorderedCollapsablePanel) editorComponent;
                ((TopComponent) cp.getTopComponent()).addComponent(chkInherit, 1);
                ((TopComponent) cp.getTopComponent()).setVisible(false);
            }
        }
        
        private void update(){
            initComponent();
            chkInherit.setVisible(true);
            setReadonly(isReadonly);
        }
        
        public void update(HandleInfo handleInfo){
            isUpdate = true;
            boolean isOverwrite = sourceClass.getHierarchy().findOverwritten().get() != null;
            if (sourceClass.isOverwrite() || isOverwrite){
                if (isPluralPanel){
                    updatePluralPanel();
                } else {
                    updateSingularPanel();
                }
                super.open(handleInfo);
                update();
                
                if (!isOverwrite){
                    chkInherit.setForeground(Color.RED);
                } else {
                    chkInherit.setForeground(Color.BLACK);
                }
                    
                revalidate();
            } else {
                AdsEntityObjectClassDef basis = ((AdsEntityObjectClassDef) sourceClass).findBasis();
                if (!isPluralPanel &&  basis != null) {
                    chkInherit.setVisible(true);
                    updateSingularPanel();
                } else {
                    chkInherit.setVisible(false);
                    chkInherit.setSelected(false);
                }
                super.open(handleInfo);
                if (!isPluralPanel && basis != null) {
                    update();
                } 
                
                if (isPluralPanel){
                    if (((AdsEntityClassDef) sourceClass).getPresentations().isEntityTitleInherit()){
                        updatePluralPanel();
                    }
                } else {
                    if (((AdsEntityObjectClassDef) sourceClass).getPresentations().isObjectTitleInherited()) {
                       updateSingularPanel();
                    }
                }
            }
            
            isUpdate = false;
        }
        
        @Override
        public void open(HandleInfo handleInfo){
            update(handleInfo);
            
        }
        
        public boolean isInhereted(){
            return chkInherit.isSelected();
        }
        
    }
}

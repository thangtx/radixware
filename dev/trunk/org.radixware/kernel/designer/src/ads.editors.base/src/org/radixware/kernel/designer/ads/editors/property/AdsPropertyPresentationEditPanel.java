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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.ObjectType;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.LineSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemClusterizator;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class AdsPropertyPresentationEditPanel extends javax.swing.JPanel {

    private static final class PresentationItem implements ItemClusterizator.IItem, LineSelector.IDefinitionItem {

        final AdsEditorPresentationDef presentation;
        final Id id;

        public PresentationItem(AdsEditorPresentationDef presentation, Id id) {
            this.presentation = presentation;
            this.id = id;
        }

        @Override
        public String getName() {
            if (presentation != null) {
                return presentation.getQualifiedName();
            }

            return "#" + String.valueOf(id);
        }

        @Override
        public Icon getIcon() {
            if (presentation != null) {
                return presentation.getIcon().getIcon();
            }

            return RadixObjectIcon.UNKNOWN.getIcon();
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.presentation);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PresentationItem other = (PresentationItem) obj;
            if (!Objects.equals(this.presentation, other.presentation)) {
                return false;
            }
            return true;
        }

        @Override
        public Definition getDefinition() {
            return presentation;
        }
    }

    private static final class PresentationProvider implements LineSelector.IItemProvider<PresentationItem> {

        private Collection<PresentationItem> items;
        private Collection<PresentationItem> selected;
        private PropertyEditOptions propertyEditOptions;

        public PresentationProvider(AdsClassDef cls, PropertyEditOptions propertyEditOptions) {
            this.propertyEditOptions = propertyEditOptions;
            if (cls instanceof IAdsPresentableClass) {
                if (((IAdsPresentableClass) cls).getPresentations() instanceof EntityObjectPresentations) {
                    final EntityObjectPresentations presentations = (EntityObjectPresentations) ((IAdsPresentableClass) cls).getPresentations();
                    final List<AdsEditorPresentationDef> editPresentations = presentations.getEditorPresentations().get(EScope.ALL);

                    items = createItems(editPresentations);
                    selected = createSelected(propertyEditOptions);
                }
            }
        }

        public PresentationProvider(List<AdsEditorPresentationDef> items, PropertyEditOptions propertyEditOptions) {
            this.items = createItems(items);

            this.propertyEditOptions = propertyEditOptions;
            selected = createSelected(propertyEditOptions);
        }

        private List<PresentationItem> createSelected(PropertyEditOptions options) {

            final List<PresentationItem> items = new ArrayList<>();
            for (final Id id : options.getObjectEditorPresentations()) {
                final AdsEditorPresentationDef presentation = options.findObjectEditorPresentation(id);
                items.add(new PresentationItem(presentation, id));
            }

            return items;
        }

        private List<PresentationItem> createItems(List<AdsEditorPresentationDef> presentations) {
            List<PresentationItem> items = new ArrayList<>();
            for (AdsEditorPresentationDef presentation : presentations) {
                items.add(new PresentationItem(presentation, presentation.getId()));
            }
            return items;
        }

        @Override
        public Collection<PresentationItem> getSelectedItems() {
            return selected;
        }

        @Override
        public void setSelectedItems(Collection<PresentationItem> selected) {
            this.selected = selected;
            propertyEditOptions.setObjectEditorPresentations(getIds(selected));
        }

        Collection<PresentationItem> getAllItems() {
            return items;
        }

        @Override
        public Collection<PresentationItem> selectItems() {


            final List<PresentationItem> all = new ArrayList<>(getAllItems());
            final List<PresentationItem> selected = new ArrayList<>(getSelectedItems());
            final ItemClusterizator.IClusterizatorItemProvider<PresentationItem> provider = new ItemClusterizator.IClusterizatorItemProvider<PresentationItem>() {
                @Override
                public List<PresentationItem> getAllAvaliable() {
                    return all;
                }

                @Override
                public List<PresentationItem> getInherited() {
                    return Collections.EMPTY_LIST;
                }

                @Override
                public List<PresentationItem> getSelected() {
                    return selected;
                }

                @Override
                public List<PresentationItem> getConstSelected() {
                    return Collections.EMPTY_LIST;
                }

                @Override
                public boolean isInherit() {
                    return false;
                }
            };

            final ItemClusterizator.IClusterizatorModel<PresentationItem> model = new ItemClusterizator.AbstractClusterizatorModel<>(provider, false, true);
            final List<PresentationItem> cluster = ItemClusterizator.cluster(model);
            if (cluster != null) {
                return this.selected = cluster;
            }
            return selected;
        }

        @Override
        public String getItemName(PresentationItem item) {
            return item.getName();
        }

        @Override
        public Icon getItemIcon(PresentationItem item) {
            return item.getIcon();
        }

        @Override
        public String getItemToolTip(PresentationItem item) {
            if (item.presentation != null) {
                return item.presentation.getToolTip();
            }
            return item.getName();
        }

        @Override
        public void open(PresentationItem item) {
            if (item.presentation != null) {
                EditorsManager.getDefault().open(item.presentation);
            }
        }
    }

    public AdsPropertyPresentationEditPanel() {
        initComponents();
    }
    private final String ALWAYS = "Always";
    private final String NEVER = "Never";
    private final String ONLY_EXISTING = "Only existing";
    private final String ONLY_IN_EDITOR = "Only in editor";
    private final String ON_CREATE = "Only on create";
    private final String PROGRAMMATICALLY = "Programmaticaly";
    boolean isMayModify = true;
    boolean isReadOnly = false;
    PropertyEditOptions propertyEditOptions;
    AdsPropertyDef prop = null;
    AdsTypeDeclaration type = null;
    ObjectPropertyPresentation objectPropertyPresentation = null;

    public void open(AdsPropertyDef prop, PropertyEditOptions propertyEditOptions, AdsTypeDeclaration type) {
        this.propertyEditOptions = propertyEditOptions;
        this.prop = getPropForPresentation(prop);
        this.type = type;
        update();
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        editOptionsPanel1.setReadOnly(isReadOnly);
        jchbEditingReadSeparately.setEnabled(!isReadOnly);
        jchbEditingDuplicatesEnabled.setEnabled(!isReadOnly);
        chArrayItemIsNotNull.setEnabled(!isReadOnly);
        jcbEditPossibility.setEnabled(!isReadOnly);
        jLabel7.setEnabled(!isReadOnly);
        depParentEditorPresentation.setEnabled(!isReadOnly);
//        lepObjectEditorPresentatin.setEnabled(!isReadOnly);
        chUseMaxSize.setEnabled(!isReadOnly);
        chUseMinSize.setEnabled(!isReadOnly);

        boolean useMaxSize = propertyEditOptions.getMaxArrayItemCount() >= 0;
        boolean useMinSize = propertyEditOptions.getMinArrayItemCount() > 0;

        edMaxSize.setEnabled(!isReadOnly && useMaxSize);
        edMinSize.setEnabled(!isReadOnly && useMinSize);

        edBaseIndex.setEnabled(!isReadOnly);
    }

    public void addChangeListenerEditorPresentation(ChangeListener listener) {
        getDepParentEditorPresentation().addChangeListener(listener);
    }

    public void removeChangeListenerEditorPresentation(ChangeListener listener) {
        getDepParentEditorPresentation().removeChangeListener(listener);
    }

    static List<Id> getIds(Collection<PresentationItem> items) {
        if (items == null) {
            return null;
        }

        final List<Id> ids = new ArrayList<>();
        for (PresentationItem presentation : items) {
            ids.add(presentation.id);
        }
        return ids;
    }

    private LineSelector<PresentationItem> getDepParentEditorPresentation() {
        return (LineSelector<PresentationItem>) depParentEditorPresentation;
    }
    private final ChangeListener arrayMinSizeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isMayModify && !isReadOnly) {
                Integer value = (Integer) edMinSize.getValue();
                propertyEditOptions.setMinArrayItemCount(value);

                updateArrSize(false);
            }
        }
    };
    private final ChangeListener arrayMaxnSizeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isMayModify && !isReadOnly) {
                Integer value = (Integer) edMaxSize.getValue();
                propertyEditOptions.setMaxArrayItemCount(value);

                updateArrSize(true);
            }
        }
    };

    private void updateArrSize(boolean max) {
        final int maxVal = (Integer) edMaxSize.getValue();
        final int minVal = (Integer) edMinSize.getValue();

        if (maxVal < minVal) {
            if (max) {
                if (chUseMinSize.isSelected()) {
                    if (maxVal > 0) {
                        edMinSize.setValue(maxVal);
                    } else {
                        edMaxSize.setValue(1);
                    }
                }
            } else {
                if (chUseMaxSize.isSelected()) {
                    edMaxSize.setValue(minVal);
                }
            }
        }
    }
    private final ChangeListener arrayBaseListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isMayModify && !isReadOnly) {
                Integer value = (Integer) edBaseIndex.getValue();
                propertyEditOptions.setFirstArrayItemIndex(value);
            }
        }
    };
    private boolean opened = false;

    private AdsPropertyDef getPropForPresentation(AdsPropertyDef prop) {
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

    public void update() {

        if (!isMayModify) {
            return;
        }

        if (propertyEditOptions == null) {
            setReadOnly(true);
            return;
        }
        isMayModify = false;
        setReadOnly(false);

        editOptionsPanel1.open(prop, propertyEditOptions, type);
        //ObjectPropertPresentationPanel




        boolean isLocalPresentation = false;
        if (prop.getOwnerDef() instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) prop.getOwnerDef()).isLocal()) {
            isLocalPresentation = true;
        }


        if (prop instanceof AdsParameterPropertyDef || isLocalPresentation)//	RADIX-4469
        {
            jchbEditingReadSeparately.setVisible(false);
            jLabel7.setVisible(false);
            if (!isLocalPresentation) {
                jcbEditPossibility.setVisible(false);
            }
        }

        DefaultComboBoxModel m = (DefaultComboBoxModel) jcbEditPossibility.getModel();
        m.removeAllElements();

        if (prop.getOwnerClass() instanceof AdsFormHandlerClassDef) {
            m.addElement(ALWAYS);
            m.addElement(NEVER);
            m.addElement(PROGRAMMATICALLY);
        } else {
            m.addElement(ALWAYS);
            m.addElement(NEVER);
            m.addElement(ONLY_EXISTING);
            m.addElement(ONLY_IN_EDITOR);
            m.addElement(ON_CREATE);
            m.addElement(PROGRAMMATICALLY);
        }

        String sEditPossibilityEditorValue = "";
        if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.ALWAYS)) {
            sEditPossibilityEditorValue = ALWAYS;
        } else if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.NEVER)) {
            sEditPossibilityEditorValue = NEVER;
        } else if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.ONLY_EXISTING)) {
            sEditPossibilityEditorValue = ONLY_EXISTING;
        } else if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.ONLY_IN_EDITOR)) {
            sEditPossibilityEditorValue = ONLY_IN_EDITOR;
        } else if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.ON_CREATE)) {
            sEditPossibilityEditorValue = ON_CREATE;
        } else if (propertyEditOptions.getEditPossibility().equals(EEditPossibility.PROGRAMMATICALLY)) {
            sEditPossibilityEditorValue = PROGRAMMATICALLY;
        }
        jcbEditPossibility.setSelectedItem(sEditPossibilityEditorValue);



        boolean isArrayProperty = type.getTypeId() != null
                && type.getTypeId().isArrayType();

        pArrayProperty.setVisible(isArrayProperty);

        jchbEditingDuplicatesEnabled.setSelected(propertyEditOptions.isDuplicatesEnabled());
        chArrayItemIsNotNull.setSelected(propertyEditOptions.isArrayElementMandatory());

        boolean useMaxSize = propertyEditOptions.getMaxArrayItemCount() >= 0;
        boolean useMinSize = propertyEditOptions.getMinArrayItemCount() > 0;
        chUseMaxSize.setSelected(useMaxSize);
        chUseMinSize.setSelected(useMinSize);
        edMaxSize.setEnabled(!isReadOnly && useMaxSize);
        edMinSize.setEnabled(!isReadOnly && useMinSize);

        if (!opened) {
            edMinSize.addChangeListener(arrayMinSizeListener);
            edMaxSize.addChangeListener(arrayMaxnSizeListener);
            edBaseIndex.addChangeListener(arrayBaseListener);
        }

        chUseMaxSize.setEnabled(!isReadOnly);
        chUseMinSize.setEnabled(!isReadOnly);

        edMaxSize.setModel(new SpinnerNumberModel(propertyEditOptions.getMaxArrayItemCount() < 0 ? 0 : propertyEditOptions.getMaxArrayItemCount(), 0, Integer.MAX_VALUE, 1));
        edMinSize.setModel(new SpinnerNumberModel(propertyEditOptions.getMinArrayItemCount() < 1 ? 1 : propertyEditOptions.getMinArrayItemCount(), 1, Integer.MAX_VALUE, 1));
        edBaseIndex.setModel(new SpinnerNumberModel(propertyEditOptions.getFirstArrayItemIndex(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

        if (chUseMinSize.isSelected() && chUseMaxSize.isSelected()) {
            if (Objects.equals(0, edMaxSize.getValue())) {
                edMaxSize.setValue(1);
                propertyEditOptions.setMaxArrayItemCount(1);
            }
        }

        jchbEditingReadSeparately.setSelected(propertyEditOptions.isReadSeparately());

        AdsClassDef inheritClassDef = null;
        if (type != null && type.resolve(prop).get() instanceof AdsClassType) {
            AdsClassType cl = (AdsClassType) (type.resolve(prop).get());
            inheritClassDef = cl.getSource();

            boolean isMustDisable = true;
            if (inheritClassDef instanceof IAdsPresentableClass) {
                if (((IAdsPresentableClass) inheritClassDef).getPresentations() instanceof EntityObjectPresentations) {
                    getDepParentEditorPresentation().open(new PresentationProvider(inheritClassDef, propertyEditOptions));
                    isMustDisable = false;
                }
            }
            if (isMustDisable) {
                depParentEditorPresentation.setEnabled(false);
            }
        } else if (prop instanceof AdsInnateRefPropertyDef) {

            final AdsInnateRefPropertyDef innateRefPropertyDef = (AdsInnateRefPropertyDef) prop;
            inheritClassDef = innateRefPropertyDef.findReferencedEntityClass();
            if (inheritClassDef instanceof AdsEntityClassDef) {
                getDepParentEditorPresentation().setReadonly(isReadOnly);
                getDepParentEditorPresentation().open(new PresentationProvider(inheritClassDef, propertyEditOptions));
            } else {
                depParentEditorPresentation.setEnabled(false);
            }
        }
        boolean isShowParentEditorPresentation = prop.getValue().getType().getTypeId() != null
                && (prop.getValue().getType().getTypeId().equals(EValType.PARENT_REF)
                || prop.getValue().getType().getTypeId().equals(EValType.ARR_REF));

        parentEditorPresentationPanel.setVisible(isShowParentEditorPresentation);
        if (isShowParentEditorPresentation) {
            parentEditorPresentationPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.ParentEditorPresentationPanel"))); // NOI18N
        }

        IAdsPresentableProperty sProp = (IAdsPresentableProperty) prop;
        PropertyPresentation propertyPresentation = null;
        if (sProp.getPresentationSupport() != null) {
            propertyPresentation = sProp.getPresentationSupport().getPresentation();
        }
        //propertyPresentation.get
        if (propertyPresentation instanceof ObjectPropertyPresentation) {

            parentEditorPresentationPanel.setBorder(BorderFactory.createTitledBorder(NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.ObjectEditorPresentationPanel")));
            parentEditorPresentationPanel.setVisible(true);
            AdsType currType = prop.getValue().getType().resolve(prop).get();
            if (currType instanceof ObjectType) {
                objectPropertyPresentation = (ObjectPropertyPresentation) sProp.getPresentationSupport().getPresentation();
                ObjectType currObjectType = (ObjectType) currType;
                ExtendableDefinitions<AdsEditorPresentationDef> presentations =
                        currObjectType.getSource().getPresentations().getEditorPresentations();
                List<AdsEditorPresentationDef> list = presentations.get(EScope.ALL);
                getDepParentEditorPresentation().open(new PresentationProvider(list, objectPropertyPresentation.getEditOptions()));

                getDepParentEditorPresentation().setReadonly(isReadOnly);

            } else {
                getDepParentEditorPresentation().setEnabled(false);
            }

        } else {
//            objectPropertPresentationPanel.setVisible(false);
        }

        // ban on ReadSeparately if property has type PARENT_REF or OBJECT
        if (type != null) {
            final EValType typeId = type.getTypeId();
            if (typeId == EValType.PARENT_REF || typeId == EValType.OBJECT) {
                jchbEditingReadSeparately.setVisible(false);
            }
        }

        isMayModify = true;
        opened = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        editOptionsPanel1 = new org.radixware.kernel.designer.common.editors.EditOptionsPanel();
        jPanel2 = new javax.swing.JPanel();
        jchbEditingReadSeparately = new javax.swing.JCheckBox();
        pArrayProperty = new javax.swing.JPanel();
        jchbEditingDuplicatesEnabled = new javax.swing.JCheckBox();
        chArrayItemIsNotNull = new javax.swing.JCheckBox();
        edMinSize = new javax.swing.JSpinner();
        edMaxSize = new javax.swing.JSpinner();
        chUseMinSize = new javax.swing.JCheckBox();
        chUseMaxSize = new javax.swing.JCheckBox();
        edBaseIndex = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jcbEditPossibility = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        parentEditorPresentationPanel = new javax.swing.JPanel();
        depParentEditorPresentation = new LineSelector<>();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 5, 1, 5));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        editOptionsPanel1.setBorder(null);
        add(editOptionsPanel1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 5));

        jchbEditingReadSeparately.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.jchbEditingReadSeparately.text")); // NOI18N
        jchbEditingReadSeparately.setAlignmentY(0.0F);
        jchbEditingReadSeparately.setPreferredSize(null);
        jchbEditingReadSeparately.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingReadSeparatelyActionPerformed(evt);
            }
        });
        jPanel2.add(jchbEditingReadSeparately);

        add(jPanel2);

        pArrayProperty.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.pArrayProperty.border.title"))); // NOI18N

        jchbEditingDuplicatesEnabled.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.jchbEditingDuplicatesEnabled.text")); // NOI18N
        jchbEditingDuplicatesEnabled.setAlignmentY(0.0F);
        jchbEditingDuplicatesEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchbEditingDuplicatesEnabledActionPerformed(evt);
            }
        });

        chArrayItemIsNotNull.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.chArrayItemIsNotNull.text")); // NOI18N
        chArrayItemIsNotNull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chArrayItemIsNotNullActionPerformed(evt);
            }
        });

        chUseMinSize.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.chUseMinSize.text")); // NOI18N
        chUseMinSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chUseMinSizeActionPerformed(evt);
            }
        });

        chUseMaxSize.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.chUseMaxSize.text")); // NOI18N
        chUseMaxSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chUseMaxSizeActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout pArrayPropertyLayout = new javax.swing.GroupLayout(pArrayProperty);
        pArrayProperty.setLayout(pArrayPropertyLayout);
        pArrayPropertyLayout.setHorizontalGroup(
            pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pArrayPropertyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pArrayPropertyLayout.createSequentialGroup()
                        .addComponent(jchbEditingDuplicatesEnabled)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chArrayItemIsNotNull))
                    .addGroup(pArrayPropertyLayout.createSequentialGroup()
                        .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chUseMinSize)
                            .addComponent(chUseMaxSize)
                            .addGroup(pArrayPropertyLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel2)))
                        .addGap(18, 18, 18)
                        .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(edMinSize, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(edMaxSize)
                            .addComponent(edBaseIndex))))
                .addContainerGap(245, Short.MAX_VALUE))
        );
        pArrayPropertyLayout.setVerticalGroup(
            pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pArrayPropertyLayout.createSequentialGroup()
                .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jchbEditingDuplicatesEnabled)
                    .addComponent(chArrayItemIsNotNull))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edMinSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chUseMinSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edMaxSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chUseMaxSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(pArrayPropertyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edBaseIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(3, 3, 3))
        );

        add(pArrayProperty);

        jcbEditPossibility.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbEditPossibilityeditAccess(evt);
            }
        });

        jLabel7.setText(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbEditPossibility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(417, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcbEditPossibility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1);

        parentEditorPresentationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.parentEditorPresentationPanel.border.title"))); // NOI18N
        parentEditorPresentationPanel.setLayout(new javax.swing.BoxLayout(parentEditorPresentationPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout depParentEditorPresentationLayout = new javax.swing.GroupLayout(depParentEditorPresentation);
        depParentEditorPresentation.setLayout(depParentEditorPresentationLayout);
        depParentEditorPresentationLayout.setHorizontalGroup(
            depParentEditorPresentationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );
        depParentEditorPresentationLayout.setVerticalGroup(
            depParentEditorPresentationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        parentEditorPresentationPanel.add(depParentEditorPresentation);

        add(parentEditorPresentationPanel);
        parentEditorPresentationPanel.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(AdsPropertyPresentationEditPanel.class, "AdsPropertyPresentationEditPanel.parentEditorPresentationPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jchbEditingReadSeparatelyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingReadSeparatelyActionPerformed
        if (!isMayModify || isReadOnly) {
            return;
        }

        propertyEditOptions.setReadSeparately(jchbEditingReadSeparately.isSelected());
        update();
}//GEN-LAST:event_jchbEditingReadSeparatelyActionPerformed

    private void jchbEditingDuplicatesEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchbEditingDuplicatesEnabledActionPerformed
        if (!isMayModify || isReadOnly) {
            return;
        }
        propertyEditOptions.setDuplicatesEnabled(jchbEditingDuplicatesEnabled.isSelected());
        update();
}//GEN-LAST:event_jchbEditingDuplicatesEnabledActionPerformed

    private void jcbEditPossibilityeditAccess(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbEditPossibilityeditAccess
        if (!isMayModify || isReadOnly) {
            return;
        }

        Object obj = jcbEditPossibility.getSelectedItem();
        if (propertyEditOptions == null || obj == null) {
            return;
        }
        String s = (String) obj;
        switch (s) {
            case ALWAYS:
                propertyEditOptions.setEditPossibility(EEditPossibility.ALWAYS);
                break;
            case NEVER:
                propertyEditOptions.setEditPossibility(EEditPossibility.NEVER);
                break;
            case ONLY_EXISTING:
                propertyEditOptions.setEditPossibility(EEditPossibility.ONLY_EXISTING);
                break;
            case ONLY_IN_EDITOR:
                propertyEditOptions.setEditPossibility(EEditPossibility.ONLY_IN_EDITOR);
                break;
            case ON_CREATE:
                propertyEditOptions.setEditPossibility(EEditPossibility.ON_CREATE);
                break;
            case PROGRAMMATICALLY:
                propertyEditOptions.setEditPossibility(EEditPossibility.PROGRAMMATICALLY);
                break;
        }
        update();
}//GEN-LAST:event_jcbEditPossibilityeditAccess

    private void chArrayItemIsNotNullActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chArrayItemIsNotNullActionPerformed
        if (!isMayModify || isReadOnly) {
            return;
        }

        propertyEditOptions.setArrayElementMandatory(chArrayItemIsNotNull.isSelected());
        update();
    }//GEN-LAST:event_chArrayItemIsNotNullActionPerformed

    private void chUseMinSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chUseMinSizeActionPerformed
        if (isMayModify && !isReadOnly) {
            if (chUseMinSize.isSelected()) {
                if (propertyEditOptions.getMinArrayItemCount() < 0) {
                    propertyEditOptions.setMinArrayItemCount(1);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            update();
                        }
                    });
                }
            } else {
                propertyEditOptions.setMinArrayItemCount(-1);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        }
    }//GEN-LAST:event_chUseMinSizeActionPerformed

    private void chUseMaxSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chUseMaxSizeActionPerformed
        if (isMayModify && !isReadOnly) {
            if (chUseMaxSize.isSelected()) {
                if (propertyEditOptions.getMaxArrayItemCount() < 0) {
                    propertyEditOptions.setMaxArrayItemCount(0);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            update();
                        }
                    });
                }
            } else {
                propertyEditOptions.setMaxArrayItemCount(-1);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        update();
                    }
                });
            }
        }
    }//GEN-LAST:event_chUseMaxSizeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chArrayItemIsNotNull;
    private javax.swing.JCheckBox chUseMaxSize;
    private javax.swing.JCheckBox chUseMinSize;
    private javax.swing.JPanel depParentEditorPresentation;
    private javax.swing.JSpinner edBaseIndex;
    private javax.swing.JSpinner edMaxSize;
    private javax.swing.JSpinner edMinSize;
    private org.radixware.kernel.designer.common.editors.EditOptionsPanel editOptionsPanel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox jcbEditPossibility;
    private javax.swing.JCheckBox jchbEditingDuplicatesEnabled;
    private javax.swing.JCheckBox jchbEditingReadSeparately;
    private javax.swing.JPanel pArrayProperty;
    private javax.swing.JPanel parentEditorPresentationPanel;
    // End of variables declaration//GEN-END:variables
}

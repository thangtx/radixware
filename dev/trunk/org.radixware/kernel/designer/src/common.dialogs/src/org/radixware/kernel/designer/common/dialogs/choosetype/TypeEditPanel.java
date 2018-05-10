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
 * TypeEditPanel.java
 *
 * Created on Mar 11, 2009, 11:07:22 AM
 */
package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.CheckedNumberSpinnerEditor;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.values.ValueChangeListener;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class TypeEditPanel extends javax.swing.JPanel {

    public final static String LAST_USED_PATH = "TypeEditPanel-LastUsed";
    private final static String LAST_USED_BOX_STATE = "TypeEditPanel-LastUsedBoxState";
    private final static String BOXSTATE = "BoxState";
    private StatefullInnerPane innerpane;
    private Color defaultTextColor;
    private ExtendableTextField natureField;
    private JButton natureBtn;
    private ExtendableTextField typeField;
    private JButton typeBtn;
    private JLabel dimLabel = new JLabel(NbBundle.getMessage(TypeEditPanel.class, "ChooseType-Common-Dimension"));
    private JSpinner dimEditor;
    private JLabel genericLabel = new JLabel(NbBundle.getMessage(TypeEditPanel.class, "TypeEdit-GenericParameters"));
    private ExtendableTextField genericField;
    private JButton genericBtn;
    private JButton openInEditor;
    private JButton selectInTree;
    private JCheckBox usedBox;
    private Border usedBoxBorder = null;
    private LastUsedTypesPanel lastPanel;
    private TypeEditorModel model;
    private AdsType objectType;
    private boolean lastUsedEnabled = false;
    private boolean navigationButtonsEnabled = true;
    private boolean readonly = false;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public TypeEditPanel() {
        this(false, true);
    }

    public TypeEditPanel(boolean lastUsedEnabled, boolean navigationButtonsEnabled) {
        super();

        this.lastUsedEnabled = lastUsedEnabled;
        this.navigationButtonsEnabled = navigationButtonsEnabled;

        innerpane = new StatefullInnerPane();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        innerpane.setAlignmentY(TOP_ALIGNMENT);
        innerpane.setAlignmentX(LEFT_ALIGNMENT);
        setAlignmentX(LEFT_ALIGNMENT);
        setAlignmentY(TOP_ALIGNMENT);

        add(innerpane);

        initDimensionEditor();
        initComponents();
        initNavigationButtons();
    }

    public void open(AdsTypeDeclaration type, final Definition context, IAdsTypedObject parameter) {
        open(new TypeEditorModel(type, new ChooseType.DefaultTypeFilter(context, parameter)));
    }

    public void open(TypeEditorModel model) {
        this.model = model;

        if (lastUsedEnabled && lastPanel != null) {
            lastPanel.open(LAST_USED_PATH, model.getTypeFilter());
        }
        update();
    }

    public AdsTypeDeclaration getCurrentType() {
        return model.getType();
    }

    public boolean isComplete() {

        ETypeNature nature = model.getNature();

        if (nature != null) {
            if (nature == ETypeNature.JAVA_CLASS && !model.getType().isTypeArgument()) {
                String classname = model.getType().getExtStr();
                ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;

                Definition definition = model.getTypeFilter().getContext();
                if (definition instanceof IEnvDependent) {
                    env = ((IEnvDependent) definition).getUsageEnvironment();
                }
                PlatformLib kernelLib = ((AdsSegment) definition.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
                RadixPlatformClass platformClass = kernelLib.findPlatformClass(classname);
                if (platformClass == null) {
                    kernelLib = ((AdsSegment) definition.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(ERuntimeEnvironmentType.COMMON);
                    platformClass = kernelLib.findPlatformClass(classname);
                    if (platformClass == null) {
                        innerpane.state.error(NbBundle.getMessage(TypeEditPanel.class, "TypeEditErrors-ClassLoad") + classname);
                        enableGenericEditor(updateGenericEditing());
                        return false;
                    }
                }
            }
            innerpane.state.ok();
            return true;
        }
        innerpane.state.error(NbBundle.getMessage(TypeEditPanel.class, "TypeEditErrors-NullNature"));
        return false;
    }

    private void updateReadonlyState() {
        natureBtn.setEnabled(!readonly);
        typeBtn.setEnabled(!readonly);
        
        if (!readonly) {
            if (isRefineEnabled()) {
                typeBtn.setEnabled(true);
                natureBtn.setEnabled(false);
            } else {
                typeBtn.setEnabled(true);
                natureBtn.setEnabled(true);
            }
        }
//         && model.getType().isArray()
//         && model.getType().isGeneric()
        dimEditor.setEnabled(!readonly);
        genericBtn.setEnabled(!readonly);
    }

    private AdsTypeDeclaration chooseNature() {
        return ChooseType.getInstance().chooseType(model.getTypeFilter());
    }

    private AdsTypeDeclaration chooseType() {
        return ChooseType.getInstance().editType(model.getNature(), model.getTypeFilter());
    }

    private AdsTypeDeclaration refineType() {
        return ChooseType.getInstance().refineType(model.getType(), model.getTypeFilter());
    }

    private void setNaturalValue(ETypeNature nature) {
        this.model.setNature(nature);
        natureField.setValue(nature.getNatureName());
    }

    private void setType(AdsTypeDeclaration type) {
        model.setType(type);
        update();
        changeSupport.fireChange();
    }

    private void update() {

        boolean enableDimensionEditing = false;
        boolean enableGenericEditing = false;

        final AdsTypeDeclaration type = model.getType();

        if (type != null) {
            if (isRefineEnabled()) {
                typeBtn.setEnabled(true);
                natureBtn.setEnabled(false);
            } else {
                typeBtn.setEnabled(true);
                natureBtn.setEnabled(true);
            }

            Definition context = model.getTypeFilter().getContext();
            String name = type.getQualifiedName(context);
            typeField.setValue(name);
            typeField.setForeground(defaultTextColor);

            natureField.setForeground(defaultTextColor);

            EValType typeid = type.getTypeId();
            objectType = type.resolve(context).get();

            openInEditor.setVisible(navigationButtonsEnabled && objectType instanceof AdsDefinitionType);
            selectInTree.setVisible(navigationButtonsEnabled && objectType instanceof AdsDefinitionType);

            ETypeNature currNature = ETypeNature.getByType(type, context);
            setNaturalValue(currNature);

            if (typeid == EValType.USER_CLASS || typeid == EValType.JAVA_CLASS) {
                enableGenericEditing = updateGenericEditing();
            }
            if (type.isArray()) {
                dimEditor.setValue(type.getArrayDimensionCount());
                enableDimensionEditing = true;
            } else {
                dimEditor.setValue(0);
            }

        } else {
            objectType = null;
            typeField.setValue("<undefined type>");
            typeField.setForeground(Color.RED);
            typeBtn.setEnabled(false);
            natureField.setValue("<undefined type>");
            natureField.setForeground(Color.RED);
        }

        enableDimensionEditor(enableDimensionEditing);
        enableGenericEditor(enableGenericEditing);
        
        Border b = BorderFactory.createEmptyBorder(dimEditor.getPreferredSize().height + 10, 0, 0, 0); //10 - insets between rows
        if (!enableDimensionEditing && !enableGenericEditing) {
            if (usedBoxBorder == null) {
                usedBox.setBorder(b);
            } else {
                usedBox.setBorder(BorderFactory.createCompoundBorder(b, usedBoxBorder));
            }
        } else {
            usedBox.setBorder(usedBoxBorder);
        }

        updateLastUsedList();
    }

    private boolean updateGenericEditing() {
        AdsTypeDeclaration type = model.getType();
        if (type != null && type.getGenericArguments() != null && !type.getGenericArguments().isEmpty() && !type.isArray()) {
            String val = "";
            for (AdsTypeDeclaration.TypeArgument a : type.getGenericArguments().getArgumentList()) {
                val += a.getQualifiedName(model.getTypeFilter().getContext()) + ", ";
            }
            genericField.setValue(val.substring(0, val.length() - 2));
            return !innerpane.state.isErrorneous();
        }
        return false;
    }

    private void updateLastUsedList() {
        usedBox.setVisible(lastUsedEnabled);
        lastPanel.setVisible(lastUsedEnabled && usedBox.isSelected());
    }

    private void enableDimensionEditor(boolean enable) {
        dimLabel.setVisible(enable);
        dimEditor.setVisible(enable);
    }

    private void enableGenericEditor(boolean enable) {
        genericLabel.setVisible(enable);
        genericField.setVisible(enable);
    }

    private void saveLastUsedEnable(boolean enable) {
        Utils.findOrCreatePreferences(LAST_USED_BOX_STATE).putBoolean(BOXSTATE, enable);
    }

    private boolean isLastUsedEnable() {
        try {
            Preferences pref = Utils.findPreferences(LAST_USED_BOX_STATE);
            boolean selection = pref != null ? pref.getBoolean(BOXSTATE, false) : false;
            return selection;
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
            return false;
        }
    }

    private void initNavigationButtons() {
        openInEditor = typeField.addButton();
        openInEditor.setIcon(RadixWareIcons.EDIT.EDIT.getIcon(13, 13));
        openInEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdsType type = TypeEditPanel.this.objectType;
                if (type != null && type instanceof AdsDefinitionType) {
                    final Definition source = ((AdsDefinitionType) type).getSource();
                    if (source != null) {
                        EditorsManager.getDefault().open(source);
                    }
                }
            }
        });

        selectInTree = typeField.addButton();
        selectInTree.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(13, 13));
        selectInTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final AdsType type = TypeEditPanel.this.objectType;
                if (type != null && type instanceof AdsDefinitionType) {
                    final Definition source = ((AdsDefinitionType) type).getSource();
                    if (source != null) {
                        NodesManager.selectInProjects(source);
                    }
                }
            }
        });
    }

    private void initDimensionEditor() {
        int MAX_DIMENSION = 255;
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1, 1, MAX_DIMENSION, 1);
        dimEditor = new JSpinner(spinnerNumberModel);
        dimEditor.setEditor(new CheckedNumberSpinnerEditor(dimEditor));
        dimEditor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Object value = dimEditor.getValue();
                int dimension = Integer.parseInt(value.toString());
                model.setType(model.getType().toArrayType(dimension));
                changeSupport.fireChange();
            }
        });
    }

    private void initComponents() {

        setMaximumSize(new java.awt.Dimension(32767, 122));
        setMinimumSize(new java.awt.Dimension(250, 122));

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        innerpane.removeAll();
        innerpane.setLayout(layout);

        JLabel naturelabel = new JLabel(NbBundle.getMessage(TypeEditPanel.class, "TypeEdit-Nature"));
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 0, 10);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        c.weightx = 0.0;
        c.weighty = 0.0;

        innerpane.add(naturelabel, c);
        natureField = new ExtendableTextField(true);
        natureBtn = natureField.addButton();

        natureBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        natureBtn.addActionListener(new ChooseNatureActionListener());

        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;

        innerpane.add(natureField, c);
        JLabel typelabel = new JLabel(NbBundle.getMessage(TypeEditPanel.class, "TypeEdit-Type"));
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 0.0;
        c.weighty = 0.0;

        innerpane.add(typelabel, c);
        typeField = new ExtendableTextField(true);
        defaultTextColor = typeField.getForeground();
        typeBtn = typeField.addButton();

        typeBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        typeBtn.addActionListener(new ChooseTypeActionListener());

        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;

        innerpane.add(typeField, c);
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 0.0;
        c.weighty = 0.0;

        innerpane.add(dimLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;

        innerpane.add(dimEditor, c);
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 0.0;
        c.weighty = 0.0;

        innerpane.add(genericLabel, c);
        genericField = new ExtendableTextField(true);
        genericBtn = genericField.addButton();

        genericBtn.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
        genericBtn.addActionListener(new ActionListener() {
            private Object getType(AdsType adsType) {
                Object type = null;

                if (adsType instanceof RadixType) {
                    type = ((RadixType) adsType).getTypeId();
                } else if (adsType instanceof AdsEnumType.Array) {
                    AdsEnumType enumtype = (AdsEnumType) adsType;
                    EValType valtype = enumtype.getSource().getItemType();
                    if (valtype == EValType.INT) {
                        type = EValType.ARR_INT;
                    } else if (valtype == EValType.CHAR) {
                        type = EValType.ARR_CHAR;
                    } else if (valtype == EValType.STR) {
                        type = EValType.ARR_STR;
                    }
                } else if (adsType instanceof AdsClassType) {
                    AdsClassDef source = ((AdsClassType) adsType).getSource();
                    if (source instanceof AdsEntityClassDef) {
                        type = model.getType().getTypeId();
                    } else {
                        type = source;
                    }
                } else if (adsType instanceof JavaClassType) {
                    String name = ((JavaClassType) adsType).getJavaClassName();
                    ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;

                    Definition context = model.getTypeFilter().getContext();
                    if (context instanceof IEnvDependent) {
                        env = ((IEnvDependent) context).getUsageEnvironment();
                    }
                    PlatformLib kernelLib = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
                    RadixPlatformClass platformClass = kernelLib.findPlatformClass(name);
                    if (platformClass != null) {
                        type = platformClass;
                    } else {
                        changeSupport.fireChange();
                    }
                } else if (adsType instanceof ArrayType) {
                    type = ((ArrayType) adsType).getItemType();
                    type = getType((AdsType) type);
                }
                return type;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                AdsTypeDeclaration currType = model.getType();
                if (currType != null) {
                    ITypeFilter typeFilter = new ChooseType.DefaultTypeFilter(model.getTypeFilter(), model.getType());
                    AdsTypeDeclaration newType = ChooseType.getInstance().editGenericParameters(currType, typeFilter);
                    if (newType != null) {
                        setType(model.getType().isArray() ? newType.toArrayType(model.getType().getArrayDimensionCount()) : newType);
                    }
                }
            }
        });

        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;
        innerpane.add(genericField, c);

        /*
         *
         */
        usedBox = new javax.swing.JCheckBox("Show Last Used Types");

        usedBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                saveLastUsedEnable(usedBox.isSelected());
            }
        });

        usedBox.setSelected(isLastUsedEnable());
        ActionListener boxListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean selected = usedBox.isSelected();
                saveLastUsedEnable(selected);

                lastPanel.setVisible(selected);
                lastPanel.updateUI();
                TypeEditPanel.this.updateUI();
//                if (selected) {
//                    Rectangle currBounds = TypeEditPanel.this.getBounds();
//                    Rectangle lastBounds = lastPanel.getBounds();
//
//                    if (lastBounds.y == 0 || lastBounds.height == 0) {
//                        int newY = usedBox.getY() + usedBox.getHeight() + 10;
//                        lastBounds.setBounds(newY, usedBox.getX(), usedBox.getWidth(), lastPanel.getPreferredSize().height + 10);
//                    }
//
//                    int forCheck = currBounds.height - (lastBounds.y - currBounds.y);
//                    boolean check = forCheck < lastBounds.height;
//
//                    if (check) {
//                        Rectangle commonBounds = new Rectangle(currBounds.x,
//                            currBounds.y,
//                            currBounds.width,
//                            currBounds.height + (lastBounds.height - forCheck));
//                        TypeEditPanel.this.setBounds(commonBounds);
//                    }
//                }
            }
        };

        usedBox.addActionListener(boxListener);
        c.gridx = 0;
        c.gridy = 4;
        c.insets = new Insets(10, 10, 0, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;

        innerpane.add(usedBox, c);
        usedBoxBorder = usedBox.getBorder();
        lastPanel = new LastUsedTypesPanel();

        lastPanel.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                if (lastPanel.isSetValue()) {
                    setType(lastPanel.getValue());
                }
            }
        });

        c.gridy = 5;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.BOTH;
        innerpane.add(lastPanel, c);
        lastPanel.setVisible(usedBox.isSelected());


        StateDisplayer sd = new StateDisplayer();
        c.gridy = 6;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.weighty = 0.0;

        innerpane.add(sd, c);
    }

    TypeEditorModel getModel() {
        return model;
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
        updateReadonlyState();
    }

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public final void setSelectedAction(Runnable selected) {
        if (lastPanel != null) {
            lastPanel.setSelectedAction(selected);
        }
    }

    private static final class StatefullInnerPane extends JPanel {

        StateManager state = new StateManager(this);
    }

    private final class ChooseNatureActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            AdsTypeDeclaration type = null;
            if (isRefineEnabled()) {
                type = refineType();
            } else {
                type = chooseNature();
            }

            if (type != null) {
                setType(type);
            }
        }
    }

    private final class ChooseTypeActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AdsTypeDeclaration type = null;
            if (isRefineEnabled()) {
                type = refineType();
            } else {
                type = chooseType();
            }

            if (type != null) {
                setType(model.getType().isArray() ? type.toArrayType(model.getType().getArrayDimensionCount()) : type);
            }
        }
    }
    
    private boolean isRefineEnabled() {

        if (model.getTypeFilter() != null && model.getTypeFilter().getContext() instanceof AdsPropertyDef) {
            final AdsPropertyDef prop = (AdsPropertyDef) model.getTypeFilter().getContext();
            return !AdsTypeDeclaration.isObject(model.getTypeFilter().getBaseType()) && !prop.isOverwrite() && prop.isOverride();
        }
        return false;
    }
}

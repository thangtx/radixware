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
package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.openide.DialogDescriptor;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef.IPropertyPresentationAttributesView;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef.PropertyAttributesSet;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyPresentationAttributes;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropertyVisibility;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemCellRenderer;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTable;
import org.radixware.kernel.designer.common.dialogs.components.AdvanceTableModel;
import org.radixware.kernel.designer.common.dialogs.components.InheritableTitlePanel;
import org.radixware.kernel.designer.common.dialogs.components.UpdateLocker;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;

final class PropertyPresentationAttributesPanel extends javax.swing.JPanel {

    public interface ISmartCellEditor {

        void afterInsert(JTable table, int row, int column);
    }

    private static final class CellValue {

        final Object value;
        final boolean inherit;
        final Object inheritValue;
        final boolean inheritRestriction;

        public CellValue(Object inheritValue, Object value, boolean inheritRestriction, boolean inherit) {
            assert value == null ? inherit : true;

            this.value = value;
            this.inherit = inherit;
            this.inheritValue = inheritValue;
            this.inheritRestriction = inheritRestriction;
        }

        public CellValue(Object value, boolean inherit) {
            this(null, value, false, inherit);
        }

        @Override
        public String toString() {
            return inherit ? value + " (inherited)" : String.valueOf(value);
        }
    }

    private static final class PropertyRestrictionsTable extends AdvanceTable<PropertyRestrictionsTableModel> {

        private static final class TitleEditor extends MouseAdapter {

            private static class TitleModalDisplayer extends ModalDisplayer {

                public TitleModalDisplayer(final PropertyAttributesSet attributes) {
                    super(new InheritableTitlePanel("Inherit title"), "Edit Title", new Object[]{DialogDescriptor.OK_OPTION});

                    getComponent().setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
                    getComponent().open(attributes);
                }

                @Override
                public final InheritableTitlePanel getComponent() {
                    return (InheritableTitlePanel) super.getComponent();
                }
            }

            public TitleEditor() {
            }

            public void editTitle(PropertyAttributesSet value) {
                new TitleModalDisplayer(value).showModal();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    final JTable table = (JTable) e.getSource();

                    final int row = table.rowAtPoint(e.getPoint());
                    final int column = table.columnAtPoint(e.getPoint());

                    if (table.convertColumnIndexToModel(column) == 5) {
                        editTitle((PropertyAttributesSet) table.getValueAt(row, column));
                    } else if (table.convertColumnIndexToModel(column) == 0) {
                        final Object value = table.getValueAt(row, column);

                        if (value instanceof RadixObject) {
                            EditorsManager.getDefault().open((RadixObject) value);
                        }
                    }
                }
            }
        }

        private static final class TitleRender extends DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final PropertyAttributesSet attributes = (PropertyAttributesSet) value;

                if (attributes.getTitleId() != null) {
                    final AdsDefinition def = attributes.findTitleOwnerDefinition();
                    if (def != null) {
                        final AdsMultilingualStringDef title = def.findLocalizedString(attributes.getTitleId());
                        if (title != null) {
                            final StringBuilder builder = new StringBuilder();
                            boolean first = true;
                            for (final EIsoLanguage lang : title.getLanguages()) {
                                final String val = title.getValue(lang);
                                if (first) {
                                    first = false;
                                } else {
                                    builder.append("; ");
                                }
                                builder.append(val != null ? val : "");
                            }
                            label.setText(builder.toString());
                            return label;
                        }
                    }
                }
                label.setText(attributes.getTitleId() != null ? "#" + attributes.getTitleId() : "<null>");
                return label;
            }
        }

        private static final class ComboBoxEditor extends DefaultCellEditor implements ISmartCellEditor {

            private static final class CellValueModel extends AbstractListModel<CellValue> implements ComboBoxModel<CellValue> {

                private final CellValue[] values;
                private CellValue selected;

                public CellValueModel(Object inheritValue, Object... values) {
                    this.values = new CellValue[values.length + 1];
                    this.values[0] = new CellValue(inheritValue, true);

                    for (int i = 0; i < values.length; ++i) {
                        this.values[i + 1] = new CellValue(values[i], false);
                    }

                    selected = this.values[0];
                }

                @Override
                public void setSelectedItem(Object obj) {
                    if (obj instanceof CellValue) {
                        selected = (CellValue) obj;
                    } else {
                        // exclude first (inherited)
                        for (int i = 1; i < values.length; i++) {
                            if (Objects.equals(obj, values[i].value)) {
                                selected = values[i];
                                break;
                            }
                        }
                    }
                    fireContentsChanged(this, -1, -1);
                }

                @Override
                public int getSize() {
                    return values.length;
                }

                @Override
                public CellValue getElementAt(int index) {
                    return values[index];
                }

                @Override
                public Object getSelectedItem() {
                    return selected;
                }
            }

            ComboBoxEditor() {
                super(new JComboBox<>());
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                final JComboBox<CellValue> comboBox = (JComboBox<CellValue>) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                setModel(comboBox, (CellValue) value);
                return comboBox;
            }

            @Override
            public void afterInsert(JTable table, int row, int column) {
//                ((JComboBox) editorComponent).showPopup();
            }

            @Override
            public int getClickCountToStart() {
                return 1;
            }

            void setModel(JComboBox comboBox, CellValue value) {

                CellValueModel model;

                if (value.value instanceof Boolean) {
                    model = new CellValueModel(value.inheritValue, Boolean.TRUE, Boolean.FALSE);
                } else if (value.value instanceof EPropertyVisibility) {
                    model = new CellValueModel(value.inheritValue, (Object[]) EPropertyVisibility.values());
                } else if (value.value instanceof EEditPossibility) {
                    model = new CellValueModel(value.inheritValue, (Object[]) EEditPossibility.values());
                } else {//if (value.value instanceof Id) {
                    model = new CellValueModel(value.inheritValue, "id");
                }

                comboBox.setModel(model);
                model.setSelectedItem(value.inherit ? model.getElementAt(0) : value.value);
            }
        }

        private static final class CellRender extends Box implements TableCellRenderer {

            private final JLabel value = new JLabel();
            private final JLabel inherit = new JLabel();

            public CellRender() {
                super(BoxLayout.X_AXIS);

                value.setOpaque(false);
                inherit.setOpaque(false);

                this.setOpaque(true);

                add(value);
                add(Box.createHorizontalStrut(4));
                add(Box.createHorizontalGlue());
                add(inherit);

                setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                final CellValue cellValue = (CellValue) value;

                this.value.setText(String.valueOf(cellValue.value));
                inherit.setText(cellValue.inherit ? "inherited" : "");

                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

                return this;
            }
        }

        public PropertyRestrictionsTable() {
        }
        private final TitleEditor titleEditor = new TitleEditor();

        @Override
        public void open(PropertyRestrictionsTableModel model) {
            super.open(model);

            getColumnModel().getColumn(0).setCellRenderer(new AbstractItemCellRenderer(this) {
                @Override
                public String getObjectName(Object object, int row, int column) {
                    if (object instanceof AdsPropertyDef) {
                        return ((AdsPropertyDef) object).getName();
                    }
                    return "<not found>";
                }

                @Override
                public String getObjectLocation(Object object, int row, int column) {
                    return null;
                }

                @Override
                public RadixIcon getObjectIcon(Object object, int row, int column) {
                    if (object instanceof AdsPropertyDef) {
                        return ((AdsPropertyDef) object).getIcon();
                    }
                    return null;
                }

                @Override
                public RadixIcon getObjectLocationIcon(Object object, int row, int column) {
                    return null;
                }
            });
            getColumnModel().getColumn(5).setCellRenderer(new TitleRender());

            setDefaultRenderer(CellValue.class, new CellRender());
            setDefaultEditor(CellValue.class, new ComboBoxEditor());

            removeMouseListener(titleEditor);
            addMouseListener(titleEditor);
        }

        @Override
        protected AdvanceTableModel getDefauldModel() {
            return new PropertyRestrictionsTableModel(null);
        }

        @Override
        public boolean editCellAt(int row, int column, EventObject e) {
            if (super.editCellAt(row, column, e)) {
                afterInsert(getCellEditor(row, column), row, column);
                return true;
            }
            return false;
        }

        protected void afterInsert(TableCellEditor editor, int row, int column) {
            if (editor instanceof ISmartCellEditor) {
                ((ISmartCellEditor) editor).afterInsert(this, row, column);
            }
        }
    }

    private static class PropertyRestrictionsTableModel extends AdvanceTableModel<
            AdsEditorPresentationDef, PropertyAttributesSet> {

        private List<PropertyAttributesSet> restriction;
        private final List<IColumnHandler> columnHandlers = new ArrayList<>();

        public PropertyRestrictionsTableModel(AdsEditorPresentationDef modelSource) {
            super(modelSource);

            columnHandlers.add(new ColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.Name")) {
                        @Override
                        public Class<?> getColumnClass(int col) {
                            return AdsPropertyDef.class;
                        }

                        @Override
                        public Object getCellValue(int row, int col, Object params) {
                            final SearchResult<AdsPropertyDef> prop = getModelSource().getOwnerClass().getProperties().findById(getRestrictions().get(row).getPropertyId(), ExtendableDefinitions.EScope.ALL);
                            return prop.get();
                        }
                    });

            abstract class RestrictionColumnHandlerAdapter extends ColumnHandlerAdapter {

                public RestrictionColumnHandlerAdapter(String columnName) {
                    super(columnName);
                }

                @Override
                public Class<?> getColumnClass(int col) {
                    return CellValue.class;
                }

                @Override
                public Object getCellValue(int row, int col, Object params) {
                    final PropertyAttributesSet rowSource = getRowSource(row);
                    return new CellValue(getInheritValue(rowSource), getValue(rowSource),
                            false, !rowSource.hasLocal() || getValue(rowSource.getLocal()) == null);
                }

                @Override
                public void setCellValue(Object value, int row, int col, Object params) {
                    final PropertyAttributesSet rowSource = getRowSource(row);
                    final CellValue val = (CellValue) value;

                    if (val.inherit) {
                        if (rowSource.getLocal() != null) {
                            setValue(rowSource.getLocal(), null);
                        }
                    } else {
                        setValue(rowSource.getOrCreateLocal(), val.value);
                    }
                }

                @Override
                public boolean isCellEditable(int row, int col) {
                    return true;
                }

                Object getInheritValue(PropertyAttributesSet rowSource) {
                    for (final AdsPropertyPresentationAttributes restriction : rowSource.getRestrictions()) {
                        if (restriction != rowSource.getLocal()) {
                            if (getValue(restriction) != null) {
                                return getValue(restriction);
                            }
                        }
                    }
                    return null;
                }

                abstract Object getValue(IPropertyPresentationAttributesView rowSource);

                abstract void setValue(AdsPropertyPresentationAttributes rowSource, Object val);
            }

            columnHandlers.add(new RestrictionColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.Presentable")) {
                        @Override
                        Object getValue(IPropertyPresentationAttributesView rowSource) {
                            return rowSource.getPresentable();
                        }

                        @Override
                        void setValue(AdsPropertyPresentationAttributes rowSource, Object val) {
                            rowSource.setPresentable((Boolean) val);
                        }
                    });

            columnHandlers.add(new RestrictionColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.NotNull")) {
                        @Override
                        Object getValue(IPropertyPresentationAttributesView rowSource) {
                            return rowSource.getMandatory();
                        }

                        @Override
                        void setValue(AdsPropertyPresentationAttributes rowSource, Object val) {
                            rowSource.setMandatory((Boolean) val);
                        }
                    });

            columnHandlers.add(new RestrictionColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.Visibility")) {
                        @Override
                        Object getValue(IPropertyPresentationAttributesView rowSource) {
                            return rowSource.getVisibility();
                        }

                        @Override
                        void setValue(AdsPropertyPresentationAttributes rowSource, Object val) {
                            rowSource.setVisibility((EPropertyVisibility) val);
                        }
                    });

            columnHandlers.add(new RestrictionColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.EditPossibility")) {
                        @Override
                        Object getValue(IPropertyPresentationAttributesView rowSource) {
                            return rowSource.getEditPossibility();
                        }

                        @Override
                        void setValue(AdsPropertyPresentationAttributes rowSource, Object val) {
                            rowSource.setEditPossibility((EEditPossibility) val);
                        }
                    });

            columnHandlers.add(new ColumnHandlerAdapter(NbBundle.getMessage(
                    PropertyPresentationAttributesPanel.class, "PropertyRestrictionsTable.Column.Title")) {
                        @Override
                        public Class<?> getColumnClass(int col) {
                            return PropertyAttributesSet.class;
                        }

                        @Override
                        public Object getCellValue(int row, int col, Object params) {
                            return getRestrictions().get(row);
                        }

                        @Override
                        public boolean isCellEditable(int row, int col) {
                            return true;
                        }
                    });
        }

        @Override
        public PropertyAttributesSet getRowSource(int row) {
            return getRestrictions().get(row);
        }

        @Override
        protected IColumnHandler getColumnHandler(Object key) {
            return columnHandlers.get((int) key);
        }

        @Override
        public int getRowCount() {
            return getRestrictions().size();
        }

        @Override
        public int getColumnCount() {
            return columnHandlers.size();
        }

        @Override
        public void updateModel() {
            if (getModelSource() == null) {
                restriction = Collections.EMPTY_LIST;
            } else {
                restriction = getModelSource().getPropertyPresentationAttributesCollection().getAll(ExtendableDefinitions.EScope.ALL);
            }

            fireTableDataChanged();
        }

        @Override
        public List<IColumnHandler> getColumnHandlers() {
            return columnHandlers;
        }

        private Id getPropertyId(int row) {
            if (getRestrictions().size() > row) {
                return getPropId(row);
            }
            assert false;
            Logger.getLogger(PropertyRestrictionsTableModel.class.getName()).log(Level.WARNING, "Index out of bounds: size " + getRestrictions().size() + ", index " + row);
            return null;
        }

        private List<AdsEditorPresentationDef.PropertyAttributesSet> getRestrictions() {
            if (restriction == null) {
                updateModel();
            }
            return restriction;
        }

        Id getPropId(int row) {
            return getRestrictions().get(row).getPropertyId();
        }
    }

    private class ControlsMediator implements ListSelectionListener, TableModelListener {

        final UpdateLocker locker = new UpdateLocker();

        public ControlsMediator() {
            btnRemove.setEnabled(false);
            btnClear.setEnabled(false);

            chbInheritPropertyRestrictions.addItemListener(new java.awt.event.ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent evt) {
                    chbInheritPropertyRestrictionsItemStateChanged();
                }
            });

            btnAdd.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    btnAddActionPerformed();
                }
            });

            btnRemove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    btnRemoveActionPerformed();
                }
            });
            btnClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    btnClearActionPerformed();
                }
            });
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            updateComponentsState();
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            updateComponentsState();
        }

        private void selectNearestRow(int row) {
            final int rowCount = getRestrictionsTable().getModel().getRowCount();

            if (rowCount > 0) {
                final int nearestRow = row < rowCount ? row : rowCount - 1;
                getRestrictionsTable().getSelectionModel().setSelectionInterval(nearestRow, nearestRow);
            }
        }

        private boolean isRemoveEnable() {
            final int[] selectedRows = getRestrictionsTable().getSelectedRows();
            if (selectedRows.length > 0 && getRestrictionsTable().getModel().getRowCount() > 0) {
                final Set<Id> localRestrictedProperties = presentation.getPropertyPresentationAttributesCollection().getPropertyIds(ExtendableDefinitions.EScope.LOCAL);

                for (int row : selectedRows) {
                    if (!localRestrictedProperties.contains(getRestrictionsTable().getModel().getPropertyId(row))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }

        private boolean isClearEnable() {
            final Set<Id> localRestrictedProperties = presentation.getPropertyPresentationAttributesCollection().getPropertyIds(ExtendableDefinitions.EScope.LOCAL);

            return !localRestrictedProperties.isEmpty();
        }

        private void updateComponentsState() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (presentation == null || presentation.isReadOnly()) {
                        btnRemove.setEnabled(false);
                        btnClear.setEnabled(false);
                        btnAdd.setEnabled(false);
                        propRestrictionTable.setEnabled(false);
                        chbInheritPropertyRestrictions.setEnabled(false);
                    }

                    btnRemove.setEnabled(isRemoveEnable());
                    btnClear.setEnabled(isClearEnable());
                }
            });
        }

        private void chbInheritPropertyRestrictionsItemStateChanged() {
            if (!locker.inUpdate()) {
                updateInheritMask();
                update();
            }
        }

        private void btnAddActionPerformed() {
            final Set<Id> usedProperties = presentation.getPropertyPresentationAttributesCollection().getPropertyIds(ExtendableDefinitions.EScope.ALL);
            final ChooseDefinitionMemberCfgs.ChooseClassMembersCfg config = new ChooseDefinitionMemberCfgs.ChooseClassMembersCfg(presentation.getOwnerClass()) {
                @Override
                public List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite) {
                    final List<AdsDefinition> properties = new ArrayList<>();
                    for (final AdsPropertyDef prop : ((AdsClassDef) def).getProperties().get(ExtendableDefinitions.EScope.LOCAL)) {
                        if (!usedProperties.contains(prop.getId()) && prop instanceof IAdsPresentableProperty) {
                            final IAdsPresentableProperty presentableProperty = (IAdsPresentableProperty) prop;
                            if (presentableProperty.getPresentationSupport() != null
                                    && presentableProperty.getPresentationSupport().getPresentation().isPresentable()) {
                                properties.add(prop);
                            }
                        }
                    }
                    return properties;
                }

                @Override
                public List<AdsClassDef> listBaseDefinitions(AdsDefinition def, Collection<AdsDefinition> seen) {
                    final List<AdsClassDef> result = new ArrayList<>(super.listBaseDefinitions(def, seen));
                    if (def == presentation.getOwnerClass()) {
                        result.add(0, (AdsClassDef) def);
                    }
                    return result;
                }
                private boolean first = true;

                @Override
                public boolean breakHierarchy(AdsDefinition def) {
                    if (def == presentation.getOwnerClass()) {
                        if (first) {
                            first = false;
                            return false;
                        }
                        return true;
                    }
                    return false;
                }

                @Override
                public String getTitle() {
                    return "Properties";
                }
            };

            addRestrictions(ChooseDefinitionMembers.choose(config));
            getRestrictionsTable().getModel().updateModel();
        }

        private void btnRemoveActionPerformed() {
            final int[] selectedRows = getRestrictionsTable().getModelSelectedRows();
            removeRestrictions(selectedRows);

            getRestrictionsTable().getModel().updateModel();
            selectNearestRow(selectedRows[0]);
        }

        private void btnClearActionPerformed() {
            clearRestrictions();
            getRestrictionsTable().getModel().updateModel();
            getRestrictionsTable().getSelectionModel().clearSelection();
        }
    }
    private AdsEditorPresentationDef presentation;
    private ControlsMediator controlsMediator;

    public PropertyPresentationAttributesPanel() {
        initComponents();

        controlsMediator = new ControlsMediator();

        getRestrictionsTable().getSelectionModel().addListSelectionListener(controlsMediator);
    }

    public void open(AdsEditorPresentationDef presentation) {
        this.presentation = presentation;

        controlsMediator.locker.enterUpdate();

        getRestrictionsTable().open(new PropertyRestrictionsTableModel(presentation));
        getRestrictionsTable().getModel().addTableModelListener(controlsMediator);

        chbInheritPropertyRestrictions.setSelected(presentation.isPropertyPresentationAttributesInherited());

        controlsMediator.locker.leavUpdate();
        controlsMediator.updateComponentsState();
    }

    public void update() {
        controlsMediator.locker.enterUpdate();

        getRestrictionsTable().getModel().updateModel();

        controlsMediator.locker.leavUpdate();
        controlsMediator.updateComponentsState();
    }

    /*
     *
     */
    private void addRestrictions(List<AdsDefinition> choose) {

        if (!choose.isEmpty()) {
            for (final AdsDefinition prop : choose) {
                presentation.getPropertyPresentationAttributesCollection().add(new AdsPropertyPresentationAttributes(prop.getId()));
            }
        }
    }

    private void removeRestrictions(int[] selectedRows) {
        if (selectedRows.length > 0) {

            final List<Id> remove = new ArrayList<>();

            for (int row : selectedRows) {
                remove.add(getRestrictionsTable().getModel().getPropertyId(row));
            }

            for (final Id id : remove) {
                presentation.getPropertyPresentationAttributesCollection().remove(id);
            }
        }
    }

    private void updateInheritMask() {
        presentation.setPropertyPresentationAttributesInherited(chbInheritPropertyRestrictions.isSelected());
    }

    private void clearRestrictions() {
        presentation.getPropertyPresentationAttributesCollection().clear();
    }

    /*
     *
     */
    private PropertyRestrictionsTable getRestrictionsTable() {
        return (PropertyRestrictionsTable) propRestrictionTable;
    }

    int getRowHeight() {
        if (propRestrictionTable != null) {
            return propRestrictionTable.getRowHeight();
        }

        return 20;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        propRestrictionTable = new PropertyRestrictionsTable();
        leftPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        chbInheritPropertyRestrictions = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        propRestrictionTable.setModel(null);
        jScrollPane1.setViewportView(propRestrictionTable);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        leftPanel.setLayout(new java.awt.GridBagLayout());

        btnAdd.setIcon(RadixWareDesignerIcon.CREATE.ADD.getIcon(13, 13));
        org.openide.awt.Mnemonics.setLocalizedText(btnAdd, org.openide.util.NbBundle.getMessage(PropertyPresentationAttributesPanel.class, "PropertyPresentationAttributesPanel.btnAdd.text")); // NOI18N
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 4, 0);
        leftPanel.add(btnAdd, gridBagConstraints);

        btnRemove.setIcon(RadixWareDesignerIcon.DELETE.DELETE.getIcon(13, 13));
        org.openide.awt.Mnemonics.setLocalizedText(btnRemove, org.openide.util.NbBundle.getMessage(PropertyPresentationAttributesPanel.class, "PropertyPresentationAttributesPanel.btnRemove.text")); // NOI18N
        btnRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 4, 0);
        leftPanel.add(btnRemove, gridBagConstraints);

        btnClear.setIcon(RadixWareDesignerIcon.DELETE.CLEAR.getIcon(13, 13));
        org.openide.awt.Mnemonics.setLocalizedText(btnClear, org.openide.util.NbBundle.getMessage(PropertyPresentationAttributesPanel.class, "PropertyPresentationAttributesPanel.btnClear.text")); // NOI18N
        btnClear.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 4, 0);
        leftPanel.add(btnClear, gridBagConstraints);

        add(leftPanel, java.awt.BorderLayout.LINE_END);

        topPanel.setLayout(new javax.swing.BoxLayout(topPanel, javax.swing.BoxLayout.LINE_AXIS));

        org.openide.awt.Mnemonics.setLocalizedText(chbInheritPropertyRestrictions, org.openide.util.NbBundle.getMessage(PropertyPresentationAttributesPanel.class, "PropertyPresentationAttributesPanel.chbInheritPropertyRestrictions.text")); // NOI18N
        topPanel.add(chbInheritPropertyRestrictions);

        add(topPanel, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRemove;
    private javax.swing.JCheckBox chbInheritPropertyRestrictions;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JTable propRestrictionTable;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}

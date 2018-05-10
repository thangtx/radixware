/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.validators.EValidatorState;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerListDialog;
import org.radixware.kernel.explorer.widgets.propeditors.IDisplayStringProvider;

public class ValListItemEditor extends ValEditor<List<ListWidgetItem>> {

    private ExplorerListDialog dlg;
    private EnumSet<IListWidget.EFeatures> features;
    private List<ListWidgetItem> items = new LinkedList<>();
    private final QToolButton editBtn = new QToolButton(this);
    private String dialogTitle;

    public ValListItemEditor(IClientEnvironment environment, QWidget parent, boolean mandatory, boolean readOnly) {
        super(environment, parent, new EditMaskNone(), mandatory, readOnly);
        editBtn.setText("...");
        editBtn.clicked.connect(this, "edit()");
        editBtn.setObjectName("rx_tbtn_edit");
        this.addButton(editBtn);
        getLineEdit().setReadOnly(true);
    }

    public void setFeatures(EnumSet<IListWidget.EFeatures> features) {
        this.features = features;
    }

    public void addItem(ListWidgetItem item) {
        if (item != null && !items.contains(item)) {
            items.add(item);
        }
    }

    public void setItems(List<ListWidgetItem> items) {
        this.items = items;
    }   

    @SuppressWarnings("Unused")
    private void edit() {
        if (items != null && !items.isEmpty()) {
            dlg = new ExplorerListDialog(getEnvironment(), this);
            dlg.setFeatures(features);
            dlg.setItems(items);
            dlg.setSelectedItems(getValue());
            List<ListWidgetItem> selectedItemsList = getValue();
            if (selectedItemsList != null && !selectedItemsList.isEmpty() && features != null && !features.contains(IListWidget.EFeatures.MULTI_SELECT)) {
                dlg.setCurrent(selectedItemsList.get(0));
                Integer currentRow = dlg.getSelectedRows().get(0);
                dlg.setCurrentRow(currentRow);
            }
            if (dialogTitle != null && !dialogTitle.isEmpty()) {
                dlg.setWindowTitle(dialogTitle);
            }          
           
            int result = dlg.exec();
            if (result == QDialog.DialogCode.Accepted.value()) {
                final ValidationResult validation = calcValidationResult(dlg.getSelectedItems());
                if (validation.getState() == EValidatorState.ACCEPTABLE) {
                    setValue(dlg.getSelectedItems());
                } else {
                    System.err.println("Validation state is inacceptable: " + validation.toString());
                }
            }
        } else {
            getEnvironment().messageWarning("Empty list", "List of values is empty");
        }
    }

    @Override
    protected ValidationResult calcValidationResult(List<ListWidgetItem> value) {
        if (value != null && !value.isEmpty()) {
            for (ListWidgetItem item : value) {
                final ValidationResult itemValidation = validateListItem(item);
                if (itemValidation.getState() != EValidatorState.ACCEPTABLE) {
                    return itemValidation;
                }
            }
        }
        return ValidationResult.ACCEPTABLE;
    }

    protected ValidationResult validateListItem(ListWidgetItem item) {
        return item.getValidationResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String getStringToShow(Object value) {
        IDisplayStringProvider displayProvider = getDisplayStringProvider();
        if (displayProvider != null) {
            return displayProvider.format(getEditMask(), value, getEditMask().toStr(getEnvironment(), value));
        } else {
            if (value != null) {
                StringBuilder sb = new StringBuilder();
                for (ListWidgetItem item : (List<ListWidgetItem>) value) {
                    sb.append(item.getText()).append(";");
                }
                if (!((List<ListWidgetItem>) value).isEmpty()) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                return sb.toString();
            } else {
                return getEditMask().toStr(getEnvironment(), null);
            }
        }
    }
    
    public void setEditorDialogTitle(String title) {
        this.dialogTitle = title;
    }
}

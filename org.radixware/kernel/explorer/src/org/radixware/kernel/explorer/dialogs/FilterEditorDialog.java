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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.views.IFilterEditorDialog;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.KernelIcon;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;

import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.schemas.xscml.Sqml;


public class FilterEditorDialog extends ExplorerDialog implements IFilterEditorDialog {

    private final RadUserFilter filter;
    private final Id contextTableId;
    private final Collection<String> restrictedNames;
    private final QLineEdit leFilterName = new QLineEdit(this);
    private final SqmlEditor conditionEditor;
    private final List<Id> parameterIds = new ArrayList<>();
    private boolean conditionChanged;

    public FilterEditorDialog(final FilterModel filter, final Collection<String> restrictedNames, final boolean showApplyButton, final QWidget parent) {
        super(filter.getEnvironment(), parent, null);
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        this.filter = filter.isUserDefined() ? (RadUserFilter) filter.getFilterDef() : null;
        this.restrictedNames = restrictedNames;
        contextTableId = filter.getFilterContext().ownerGroup.getSelectorPresentationDef().getOwnerClassId();
        conditionEditor = new SqmlEditor(filter.getEnvironment(), this, contextTableId);                
        setupUi(filter.getFilterDef(), showApplyButton);
    }

    private void setupUi(final RadFilterDef filterDef, final boolean showApplyButton) {
        final ExplorerTextOptions readonlyOptions = 
                ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE,ETextOptionsMarker.LABEL);

        final QHBoxLayout hLayout = new QHBoxLayout();
        dialogLayout().addLayout(hLayout);
        final QLabel lbFilterName = new QLabel(Application.translate("SelectorAddons", "&Name:"), this);
        leFilterName.setText(filterDef.getTitle());
        if (filter == null) {
            leFilterName.setReadOnly(true);
            WidgetUtils.applyTextOptions(readonlyOptions, leFilterName);
        }
        lbFilterName.setBuddy(leFilterName);
        hLayout.addWidget(lbFilterName);
        hLayout.addWidget(leFilterName);

        final Splitter splitter;
        final org.radixware.schemas.xscml.Sqml additionalFrom;
        if (filter == null || filter.getBaseFilter() != null) {
            final org.radixware.schemas.xscml.Sqml condition;            
            final QGroupBox gbPredefined;
            final SqmlEditor editor;
            if (filter == null) {
                condition = filterDef.getCondition();
                additionalFrom = filterDef.getConditionFrom();
                gbPredefined = new QGroupBox(Application.translate("SelectorAddons", "Predefined Condition:"), this);
                dialogLayout().addWidget(gbPredefined);
                editor = new SqmlEditor(getEnvironment(), gbPredefined, contextTableId, filterDef.getParameters());
                splitter = null;
            } else {
                condition = filter.getBaseFilter().getCondition();
                additionalFrom = filter.getBaseFilter().getConditionFrom();
                splitter = new Splitter(this,(ExplorerSettings)getEnvironment().getConfigStore());
                gbPredefined = new QGroupBox(Application.translate("SelectorAddons", "Predefined Condition:"), splitter);
                splitter.addWidget(gbPredefined);
                splitter.setOrientation(Qt.Orientation.Vertical);
                splitter.setChildrenCollapsible(false);
                dialogLayout().addWidget(splitter);
                editor = new SqmlEditor(getEnvironment(), gbPredefined, contextTableId, filter.getParameters());
            }

            final QVBoxLayout vLayout = new QVBoxLayout(gbPredefined);

            vLayout.addWidget(editor);
            editor.setImportAccessible(false);
            editor.setExportAccessible(false);
            editor.setReadonly(true);
            editor.setSqml(condition, additionalFrom, null);
        } else {
            splitter = null;
            additionalFrom = null;
        }

        if (filter != null) {
            final QGroupBox gbUser;
            if (splitter != null) {
                gbUser = new QGroupBox(Application.translate("SelectorAddons", "User-Defined Condition:"), splitter);
                splitter.addWidget(gbUser);
                splitter.setRatio(1. / 3.);
            } else {
                gbUser = new QGroupBox(Application.translate("SelectorAddons", "User-Defined Condition:"), this);
                dialogLayout().addWidget(gbUser);
            }
            final QVBoxLayout vLayout = new QVBoxLayout(gbUser);
            vLayout.addWidget(conditionEditor);
            filter.getParameters().saveState();
            for (ISqmlParameter parameter : filter.getParameters().getAll()) {
                parameterIds.add(parameter.getId());
            }

            conditionEditor.setImportAccessible(false);
            conditionEditor.setExportAccessible(false);
            conditionEditor.setSqml(filter.getCondition(), additionalFrom, filter.getParameters());
            conditionEditor.getTextEditor().textChanged.connect(this, "conditionWasChanged()");

        } else {
            conditionEditor.setVisible(false);
        }
        
        if (filter != null) {
            final String title = Application.translate("SelectorAddons", "Editing Filter '%s'");
            setWindowTitle(String.format(title, filterDef.getTitle()));
            ((QAbstractButton)addButton(EDialogButtonType.OK)).clicked.connect(this,"accept()");
            addButton(EDialogButtonType.CANCEL);
        } else {
            final String title = Application.translate("SelectorAddons", "Viewing Filter '%s'");
            setWindowTitle(String.format(title, filterDef.getTitle()));
            addButton(EDialogButtonType.CANCEL);
        }
        rejectButtonClick.connect(this,"reject()");
        if (showApplyButton) {
            final QAbstractButton btnApply = (QAbstractButton)addButton(EDialogButtonType.APPLY);
            btnApply.setIcon(ExplorerIcon.getQIcon(KernelIcon.getInstance(AdsDefinitionIcon.FILTER)));
            btnApply.clicked.connect(this, "onApplyButton()");
        }
    }

    @SuppressWarnings("unused")
    private void conditionWasChanged() {
        conditionChanged = true;
    }

    @SuppressWarnings("unused")
    private void onApplyButton() {
        done(IGroupSetting.APPLY_SETTING_RESULT);
    }

    private boolean wasModified() {
        if (parameterIds.size() != filter.getParameters().size()) {
            return true;
        }
        for (int i = 0; i < parameterIds.size(); i++) {
            if (!parameterIds.get(i).equals(filter.getParameters().get(i).getId())) {
                return true;
            }
        }
        return !leFilterName.text().equals(filter.getTitle())
                || conditionChanged;
    }

    @Override
    public DialogResult execDialog() {
        int result = exec();
        if (result == DialogCode.Accepted.value()){
            return DialogResult.ACCEPTED;
        }
        else if (result == IGroupSetting.APPLY_SETTING_RESULT) {
            return DialogResult.APPLY;
        }else{
            return DialogResult.REJECTED;
        }
    }    

    @Override
    public void done(int result) {
        if (result == QDialog.DialogCode.Rejected.value() && filter != null) {
            final String title = Application.translate("SelectorAddons", "Confirm to Close Editor");
            final String message = Application.translate("SelectorAddons", "Filter \'%s\' has been modified.\nDo you really want to close editor without saving your changes?");
            if (wasModified()) {
                if (Application.messageConfirmation(title, String.format(message, filter.getTitle()))) {
                    filter.getParameters().restoreState();
                } else {
                    return;
                }
            }
        } else if (result != QDialog.DialogCode.Rejected.value() && filter != null) {
            if (leFilterName.text().isEmpty()) {
                final String title = Application.translate("SelectorAddons", "Can't save filter");
                final String message = Application.translate("SelectorAddons", "Please enter filter name");
                Application.messageInformation(title, message);
                leFilterName.setFocus();
            }
            if (restrictedNames != null && restrictedNames.contains(leFilterName.text())) {
                final String title = Application.translate("SelectorAddons", "Can't save filter");
                final String message = Application.translate("SelectorAddons", "Filter with such name is already exist");
                Application.messageInformation(title, message);
                leFilterName.setFocus();
                return;
            }
            filter.setTitle(leFilterName.text());          
            if (!getNormalizedXmlText(conditionEditor.getSqml()).equals(getNormalizedXmlText(filter.getCondition()))) {
                filter.setCondition(conditionEditor.getSqml());
            }
        }
        super.done(result);
    }
    
    private static String getNormalizedXmlText(final Sqml sqml){
        final XmlCursor cur = sqml.newCursor(); 
        try{             
            final String result = Sqml.Factory.parse(cur.getDomNode()).xmlText();
            return result;
        }catch(XmlException exception){
            return sqml.xmlText();
        }
        finally{
            cur.dispose();
        }
    }
}

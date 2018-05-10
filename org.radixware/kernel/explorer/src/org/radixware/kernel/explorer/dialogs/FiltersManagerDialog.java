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

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.core.Qt.ToolButtonStyle;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QSizePolicy;

import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolButton;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFiltersManagerDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.editors.sqmleditor.SqmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.views.selector.FiltersManager;


public class FiltersManagerDialog extends ExplorerDialog implements IFiltersManagerDialog{

    private final Splitter splitter;
    private final QStackedWidget stackedWidget;
    private final SqmlEditor sqmlEditor;
    private final FiltersManager editor;
    private final QToolButton editFilterBtn;
    private final boolean isReadonly;
    private FilterModel currentFilter;

    public FiltersManagerDialog(final IClientEnvironment environment, final QWidget parent, final Filters filters) {
        super(environment, parent, null);
        splitter = new Splitter(this,(ExplorerSettings)getEnvironment().getConfigStore());
        stackedWidget = new QStackedWidget(splitter);
        setAttribute(WidgetAttribute.WA_DeleteOnClose, true);
        sqmlEditor = new SqmlEditor(environment, null, filters.getGroupModel().getSelectorPresentationDef().getOwnerClassId());
        editor = new FiltersManager(environment, this, filters);
        isReadonly = !filters.canCreateNew()
                || getEnvironment().getGroupSettingsStorage().isReadonly();
        editFilterBtn = new QToolButton(sqmlEditor);
        {
            editFilterBtn.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
            editFilterBtn.setToolTip(Application.translate("SelectorAddons", "Edit"));
            editFilterBtn.setToolButtonStyle(ToolButtonStyle.ToolButtonIconOnly);
            editFilterBtn.setIconSize(new QSize(28, 28));
            editFilterBtn.setAutoRaise(true);
            editFilterBtn.clicked.connect(editor, "editCurrentSetting()");
            sqmlEditor.insertToolButton(editFilterBtn);            
        }
        {
            sqmlEditor.setImportAccessible(false);
            sqmlEditor.setExportAccessible(false);
        }
        setupUi();
        onChangeCurrentFilter((FilterModel) editor.getCurrentSetting());
    }

    private void setupUi() {
        sqmlEditor.setReadonly(true);
        stackedWidget.addWidget(sqmlEditor);
        layout().addWidget(splitter);        
        splitter.setOrientation(Orientation.Horizontal);
        splitter.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        splitter.addWidget(editor);
        splitter.addWidget(stackedWidget);
        splitter.setChildrenCollapsible(false);
        splitter.setRatio(1. / 3.);
        addButton(EDialogButtonType.CLOSE).setDefault(true);
        editor.currentSettingChanged.connect(this, "onChangeCurrentFilter(IGroupSetting)");
        setWindowTitle(Application.translate("SelectorAddons", "Filters Manager"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.FILTER));
        rejectButtonClick.connect(this, "reject()");
        editor.applySetting.connect(this, "accept()");
    }

    @SuppressWarnings("unused")
    private void onChangeCurrentFilter(final IGroupSetting setting) {
        if (setting instanceof FilterModel) {
            final FilterModel filter = (FilterModel) setting;
            final RadFilterDef filterDef = filter.getFilterDef();
            sqmlEditor.setSqml(filter.getFinalCondition(), filterDef.getConditionFrom(), filterDef.getParameters());
            sqmlEditor.setVisible(true);
            sqmlEditor.open();
            sqmlEditor.setFocusPolicy(FocusPolicy.NoFocus);
            editFilterBtn.setEnabled(!isReadonly && setting.isUserDefined() && setting.isValid());
        } else {
            sqmlEditor.setVisible(false);
        }
        editor.setFocus();
    }

    @Override
    public void done(final int result) {
        currentFilter = (FilterModel) editor.getCurrentSetting();
        editor.close();
        super.done(result);
    }

    public final FilterModel getCurrentFilter() {
        return currentFilter;
    }
}

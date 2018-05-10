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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QAbstractButton;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadFormDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.widgets.ExplorerDialogButtonBox;
import org.radixware.kernel.explorer.widgets.TabSet;
import org.radixware.kernel.explorer.widgets.commands.CommandToolBar;

public final class StandardForm extends Form {

    private final QMainWindow content = new QMainWindow();
    private final CommandToolBar commandBar;
    private ExplorerDialogButtonBox buttonBox;
    private TabSet tabSet;
    private boolean wasShown;

    public StandardForm(final IClientEnvironment environment) {
        super(environment);
        commandBar = new CommandToolBar(environment);
        final QWidget top = new QWidget(this);
        setLayout(new QVBoxLayout(this));
        layout().addWidget(top);
        top.setLayout(WidgetUtils.createVBoxLayout(top));
        top.layout().addWidget(content);
        content.addToolBar(commandBar);
    }    

    @Override
    public void open(final Model model_) {
        super.open(model_);
        if (model_.getView()==null){//Form was closed in beforeOpenView handler
            return;
        }
        commandBar.setModel(model_);
        {//TabSet opening
            final RadFormDef def = getFormModel().getFormDef();
            List<Id> idsWithLevelOne = new ArrayList<>();

            for (RadEditorPageDef page : def.getEditorPages().getTopLevelPages()) {
                idsWithLevelOne.add(page.getId());
            }

            List<EditorPageModelItem> modelItems = new ArrayList<>();
            for (Id id : idsWithLevelOne) {
                EditorPageModelItem modelItem = getFormModel().getEditorPage(id);
                modelItems.add(modelItem);
            }
            tabSet = new TabSet(model_.getEnvironment(), this, modelItems, model_.getDefinition().getId().toString());
            tabSet.setParent(content);
            tabSet.bind();
            content.setCentralWidget(tabSet);
        }
        {//Buttons setup
            buttonBox = new ExplorerDialogButtonBox(getEnvironment(),this);
            setupButtonBox(buttonBox);
            layout().addWidget(buttonBox);
        }
        content.show();
        opened.emit(this);
    }
    
    public QAbstractButton findButtonByType(final String buttonType) {
        return findButtonByType(buttonBox, buttonType);
    }

    public void setCommandBarHidden(final boolean hidden) {
        commandBar.setPersistentHidden(hidden);
    }

    @Override
    public boolean setFocusedProperty(final Id propertyId) {
        final Property property;
        try {
            property = getFormModel().getProperty(propertyId);
        } catch (RuntimeException ex) {
            final String message =
                    getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot set focus to editor of property #%s: %s\n%s");
            final String reason = ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex);
            final String stack = ClientException.exceptionStackToString(ex);
            getEnvironment().getTracer().debug(String.format(message, String.valueOf(propertyId), reason, stack));
            return false;
        }
        if (property != null && property.getOwner().getView() == this) {
            return tabSet.setFocus(property);
        }
        return false;
    }

    @Override
    public void finishEdit() {
        if (tabSet!=null){
            tabSet.finishEdit();
        }
        super.finishEdit();
    }
    
    @Override
    public QSize sizeHint() {
        if (!wasShown && tabSet!=null){
            tabSet.updateGeometry();
        }
        return super.sizeHint();
    }

    @Override
    protected void showEvent(final QShowEvent event) {
        wasShown = true;
        super.showEvent(event);
    }    
}
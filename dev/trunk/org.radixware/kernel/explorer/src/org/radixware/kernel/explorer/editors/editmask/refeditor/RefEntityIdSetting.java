/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */

package org.radixware.kernel.explorer.editors.editmask.refeditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.SqmlDefinitionsLoader;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;

final class RefEntityIdSetting extends QWidget {

    IClientEnvironment environment;
    EntitySqmlDefEditor sqmlDefEditor;
    public final Signal1<ISqmlDefinition> entityChanged = new Signal1<ISqmlDefinition>();
    private final QToolButton clearBtn;
    class EntitySqmlDefEditor extends ValSqmlDefEditor {

        public EntitySqmlDefEditor(IClientEnvironment environment, QWidget parent, SqmlTreeModel definitionsModel, boolean mandatory, boolean readonly) {
            super(environment, parent, definitionsModel, mandatory, readonly);
        }

        @Override
        public List<ISqmlDefinition> getPath() {
            List<ISqmlDefinition> path = new LinkedList<>();
            if (value instanceof ISqmlSelectorPresentationDef) {
                Id selectorId = value.getId();
                ISqmlTableDef ownerTable = SqmlDefinitionsLoader.getInstance().load(environment).
                        findTableById(environment.getDefManager().getSelectorPresentationDef(selectorId).getTableId());
                path.add(ownerTable);
                path.add(value);
            }
            return path;
        }

    }

    public RefEntityIdSetting(IClientEnvironment environment, QWidget parent, SqmlTreeModel model) {
        super(parent);
        this.environment = environment;
        QHBoxLayout layout = new QHBoxLayout();
        setLayout(layout);
        sqmlDefEditor = new EntitySqmlDefEditor(environment, parent, model, true, false);
        sqmlDefEditor.setDialogTitle("Choose Entity");
        QAction clearAction = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR), null, sqmlDefEditor);
        clearAction.triggered.connect(this, "clear()");
        clearBtn = sqmlDefEditor.addButton(null, clearAction);
        clearBtn.hide();
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        layout.setMargin(0);
        layout.addWidget(sqmlDefEditor);
        sqmlDefEditor.valueChanged.connect(this, "onEntityChanged(Object)");
        
    }

    
    @SuppressWarnings("unused")
    private void onEntityChanged(final Object entity) {
        if (entity == null) {
            clearBtn.hide();
        } else if (clearBtn.isHidden()) {
            clearBtn.show();
        }
        entityChanged.emit((ISqmlDefinition)entity);
    }
    
    public ISqmlDefinition getValue() {
        return sqmlDefEditor.getValue();
    }
    
    public void setValue(ISqmlDefinition def) {
        sqmlDefEditor.setValue(def);
    }
    
    @SuppressWarnings("Unused")
    private void clear() {
        sqmlDefEditor.setValue(null);
        clearBtn.hide();
    }
}

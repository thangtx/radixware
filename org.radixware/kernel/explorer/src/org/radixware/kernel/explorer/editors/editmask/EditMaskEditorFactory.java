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

package org.radixware.kernel.explorer.editors.editmask;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorDialog;
import org.radixware.kernel.common.client.widgets.IEditMaskEditorFactory;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.editmask.consteditor.ConstSetEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.datetimeeditor.DateTimeEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.inteditor.IntEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.numeditor.NumEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.streditor.StrEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.timeintervaleditor.TimeIntervalEditMaskEditorWidget;


public class EditMaskEditorFactory implements IEditMaskEditorFactory {

    @Override
    public IEditMaskEditor newEditMaskEditor(final IClientEnvironment environment, final IWidget parent, final EEditMaskType editMaskType) {
        switch(editMaskType) {
            case DATE_TIME : return new DateTimeEditMaskEditorWidget(environment, (QWidget)parent);
            case INT : return new IntEditMaskEditorWidget(environment, (QWidget)parent);
            case NUM : return new NumEditMaskEditorWidget(environment, (QWidget)parent);
            case STR : return new StrEditMaskEditorWidget(environment, (QWidget)parent);
            case TIME_INTERVAL : return new TimeIntervalEditMaskEditorWidget(environment, (QWidget)parent);
            case ENUM : throw new UnsupportedOperationException("Unsupported for ENUM. Use newEditMaskConstSetEditor(IClientEnvironment, IWidget, RadEnumPresentationDef)");
            case BOOL: throw new UnsupportedOperationException("Unsupported for BOOL. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            case LIST : throw new UnsupportedOperationException("Unsupported for LIST. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            case OBJECT_REFERENCE : throw new UnsupportedOperationException("Unsupported for OBJECT_REFERENCE. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            default : throw new IllegalArgumentException("Unknown argument's value");
        }
    }

    @Override
    public IEditMaskEditor newEditMaskConstSetEditor(final IClientEnvironment environment, final IWidget parent, final RadEnumPresentationDef enumDef) {
        return new ConstSetEditMaskEditorWidget(environment, (QWidget)parent, enumDef);
    }

    @Override
    public IEditMaskEditor newEditMaskEditor(final IClientEnvironment environment, final IWidget parent, final EValType valType) {
        return new EditMaskEditorWidget(environment, (QWidget)parent, valType);
    }

    @Override
    public IEditMaskEditorDialog newEditMaskEditorDialog(final IClientEnvironment environment, final IWidget parent, final EEditMaskType editMaskType) {
        return new EditMaskEditorDialog(environment, parent, editMaskType);
    }

    @Override
    public IEditMaskEditorDialog newEditMaskConstSetEditorDialog(final IClientEnvironment environment, final IWidget parent, final RadEnumPresentationDef enumDef) {
        return new EditMaskEditorDialog(environment, parent, enumDef);
    }

    @Override
    public IEditMaskEditorDialog newEditMaskEditorDialog(final IClientEnvironment environment, final IWidget parent, final EValType valType) {
        return new EditMaskEditorDialog(environment, parent, valType);
    }
    
}

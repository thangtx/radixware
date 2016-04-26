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
package org.radixware.kernel.explorer.editors.valeditors;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;

public abstract class ValEditorFactory {

    private static DefaultValEditorFactory DEFAULT_INSTANCE = new DefaultValEditorFactory();

    private static class DefaultValEditorFactory extends ValEditorFactory {

        @Override
        public ValEditor createValEditor(final EValType valType,
                final EditMask editMask,
                final IClientEnvironment environment,
                final QWidget parentWidget) {
            final EEditMaskType masktype = editMask == null ? null : editMask.getType();
            if (valType != null && valType.isArrayType()) {
                return new ValArrEditor(environment, valType, null, parentWidget, false, false);
            } else if (valType == EValType.BOOL && masktype == null) {
                return new ValBoolEditor(environment, parentWidget);//check constructor
            } else if (valType == EValType.BIN || valType == EValType.BLOB) {
                return new ValBinEditor(environment, parentWidget);
            } else if (valType == EValType.CHAR && masktype == EEditMaskType.STR) {
                return new ValCharEditor(environment, parentWidget);
            } else if (valType == EValType.XML) {
                return new ValXmlEditor(environment, parentWidget);
            } else {
                switch (masktype) {
                    case BOOL: {
                        return new AdvancedValBoolEditor(environment, parentWidget, (EditMaskBool) editMask, false, false);
                    }
                    case DATE_TIME: {
                        return new ValDateTimeEditor(environment, parentWidget, (EditMaskDateTime) editMask, false, false);
                    }
                    case INT: {
                        return new ValIntEditor(environment, parentWidget, (EditMaskInt) editMask, false, false);
                    }
                    case TIME_INTERVAL: {
                        return new ValTimeIntervalEditor(environment, parentWidget, (EditMaskTimeInterval) editMask, false, false);
                    }
                    case LIST: {
                        return new ValListEditor(environment, parentWidget, (EditMaskList) editMask, false, false);
                    }
                    case ENUM: {
                        return new ValConstSetEditor(environment, parentWidget, (EditMaskConstSet) editMask, false, false);
                    }
                    case NUM: {
                        return new ValNumEditor(environment, parentWidget, (EditMaskNum) editMask, false, false);
                    }
                    case STR: {
                        return new ValStrEditor(environment, parentWidget, (EditMaskStr) editMask, false, false);
                    }
                    case FILE_PATH: {
                        return new ValFilePathEditor(environment, parentWidget, (EditMaskFilePath) editMask, false, false);
                    }
                    case OBJECT_REFERENCE: {
                        return new ValRefEditor(environment, parentWidget, (EditMaskRef) editMask, false, false);
                    }

                    default:
                        throw new IllegalArgumentError("Unsupported edit mask type: " + masktype.getName());
                }
            }
        }

    }

    public abstract ValEditor<?> createValEditor(final EValType valType,
            final EditMask editMask,
            final IClientEnvironment environment,
            final QWidget parentWidget);

    public static ValEditorFactory getDefault() {
        return DEFAULT_INSTANCE;
    }
}

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
package org.radixware.wps.views.editors.valeditors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;

public abstract class ValEditorFactory {

    private static DefaultValEditorFactory DEFAULT_FACTORY = new DefaultValEditorFactory();

    private static class DefaultValEditorFactory extends ValEditorFactory {

        @Override
        @SuppressWarnings("unchecked")
        public IValEditor createValEditor(final EValType valType,
                final EditMask editMask,
                final IClientEnvironment env) {

            final EEditMaskType editMaskType = editMask.getType();

            if (valType != null && valType.isArrayType()) {
                return new ValArrEditorController(env, valType, null).getValEditor();
            } else if (valType == EValType.BOOL && editMaskType == null) {
                return new ValBoolEditorController(env).getValEditor();//check 
            } else if (valType == EValType.BIN || valType == EValType.BLOB) {
                return new ValBinEditorController(env).getValEditor();
            } else if (valType == EValType.CHAR && editMaskType == EEditMaskType.STR) {
                return new ValCharEditorController(env).getValEditor();
            } else {
                final InputBoxController valueController;
                switch (editMaskType) {
                    case DATE_TIME:
                        valueController = new ValDateTimeEditorController(env);
                        break;
                    case ENUM:
                        valueController = new ValEnumEditorController(env);
                        break;
                    case INT:
                        valueController = new ValIntEditorController(env);
                        break;
                    case LIST:
                        valueController = new ValListEditorController(env);
                        break;
                    case NUM:
                        valueController = new ValNumEditorController(env);
                        break;
                    case STR:
                        valueController = new ValStrEditorController(env);
                        break;
                    case TIME_INTERVAL:
                        valueController = new ValTimeIntervalEditorController(env);
                        break;
                    case BOOL:
                        valueController = new AdvancedValBoolEditorController(env);
                        break;
                    case FILE_PATH:
                        valueController = new ValFilePathEditorController(env);
                        break;
                    case OBJECT_REFERENCE:
                        valueController = new ValReferenceEditorController(env);
                        break;
                    default:
                        throw new IllegalArgumentError("Unknown edit mask type: " + editMaskType.getName());
                }
                valueController.setEditMask(editMask);
                return valueController.getValEditor();
            }
        }
    }

    public abstract IValEditor<?, ?> createValEditor(final EValType valType,
            final EditMask editMask,
            final IClientEnvironment env);

    public static ValEditorFactory getDefault() {
        return DEFAULT_FACTORY;
    }
}

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

    public static class DefaultValEditorFactory extends ValEditorFactory {
        
        private final LabelFactory labelFactory;
        
        public DefaultValEditorFactory(final LabelFactory labelFactory){
            this.labelFactory = labelFactory;
        }
        
        public DefaultValEditorFactory(){
            this(null);
        }

        @Override
        @SuppressWarnings("unchecked")
        public IValEditor createValEditor(final EValType valType,
                final EditMask editMask,
                final IClientEnvironment env) {

            final EEditMaskType editMaskType = editMask.getType();

            if (valType != null && valType.isArrayType()) {
                return new ValArrEditorController(env, valType, null, labelFactory).getValEditor();
            } else if (valType == EValType.BOOL && editMaskType == null) {
                return new ValBoolEditorController(env, labelFactory).getValEditor();//check 
            } else if (valType == EValType.BIN || valType == EValType.BLOB) {
                return new ValBinEditorController(env, labelFactory).getValEditor();
            } else if (valType == EValType.CHAR && editMaskType == EEditMaskType.STR) {
                return new ValCharEditorController(env, labelFactory).getValEditor();
            } else {
                final InputBoxController valueController;
                switch (editMaskType) {
                    case DATE_TIME:
                        valueController = new ValDateTimeEditorController(env, labelFactory);
                        break;
                    case ENUM:
                        valueController = new ValEnumEditorController(env, null, labelFactory);
                        break;
                    case INT:
                        valueController = new ValIntEditorController(env, labelFactory);
                        break;
                    case LIST:
                        valueController = new ValListEditorController(env, null, labelFactory);
                        break;
                    case NUM:
                        valueController = new ValNumEditorController(env, labelFactory);
                        break;
                    case STR:
                        valueController = new ValStrEditorController(env, labelFactory);
                        break;
                    case TIME_INTERVAL:
                        valueController = new ValTimeIntervalEditorController(env, labelFactory);
                        break;
                    case BOOL:
                        valueController = new AdvancedValBoolEditorController(env, null, labelFactory);
                        break;
                    case FILE_PATH:
                        valueController = new ValFilePathEditorController(env,  null, labelFactory);
                        break;
                    case OBJECT_REFERENCE:
                        valueController = new ValReferenceEditorController(env, labelFactory);
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

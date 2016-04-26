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

/*
 * 9/29/11 12:02 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.designer.common.dialogs.components.values.ContextableValueEditorComponent;
import org.radixware.kernel.designer.common.dialogs.components.values.ContextableValueEditorComponent.DefaultEditorModel;

/**
 * Base component for editors supported by AdsValAsStrEditor
 */
abstract class BaseEditorComponent<TModel extends BaseEditorComponent.BaseEditorModel<?>>
    extends ContextableValueEditorComponent<AdsValAsStr, AdsValAsStr.IValueController> {

    public static abstract class BaseEditorModel<TLocalValue> extends DefaultEditorModel<TLocalValue, AdsValAsStr, AdsValAsStr.IValueController> {

        @Override
        protected AdsValAsStr getValueFromContext(AdsValAsStr.IValueController context) {
            return context != null ? context.getValue() : null;
        }

        @Override
        protected void commitImpl() {
            if (getContext() != null) {
                getContext().setValue(getValue());
            }
        }

        @Override
        public AdsValAsStr getNullValue() {
            return AdsValAsStr.NULL_VALUE;
        }

        @Override
        public boolean isValidValue(AdsValAsStr value) {
            return value != null;
        }
    }

    public <TLocalValue> BaseEditorComponent(TModel model) {
        super(model);
    }

    @Override
    protected TModel getModel() {
        return (TModel) super.getModel();
    }
}

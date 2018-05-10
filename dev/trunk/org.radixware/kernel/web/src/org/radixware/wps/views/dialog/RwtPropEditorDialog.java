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

package org.radixware.wps.views.dialog;

import java.lang.reflect.Constructor;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IPropEditorDialog;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


public abstract class RwtPropEditorDialog extends RwtDialog implements IPropEditorDialog {

    protected RwtPropEditorDialog(IClientEnvironment env, Id id, Id titleId, Id iconId) {
        super(env, id, titleId, iconId);
        setObjectName("rx_prop_dlg_view_#"+id.toString());
    }

    @Override
    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_PROP_EDITOR_MODEL_CLASS);
        try {
            Class<Model> classModel = getEnvironment().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, IPropEditorDialog.class);
            return constructor.newInstance(environment, this);
        } catch (Exception ex) {
            throw new ModelCreationError(ModelCreationError.ModelType.PROPERTY_EDITOR_DIALOG_MODEL, this, null, ex);
        }
    }

    @Override
    public String getTitle() {
        if (titleId == null) {
            return null;
        }
        return super.getTitle();
    }
}

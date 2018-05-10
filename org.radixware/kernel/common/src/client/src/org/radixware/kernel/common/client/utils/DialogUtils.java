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
package org.radixware.kernel.common.client.utils;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author npopov
 */
public class DialogUtils {
    
    private static boolean canRunEditor(Id tableId, String pidAsStr) {
        if (tableId == null || pidAsStr == null) {
            return false;
        }
        return tableId.getPrefix() == EDefinitionIdPrefix.ADS_APPLICATION_CLASS
                || tableId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS
                || tableId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE;
    }
    
    public static void showEntityEditor(Id tableId, String pidAsStr, IClientEnvironment env) throws ServiceClientException, InterruptedException {
        if (!canRunEditor(tableId, pidAsStr)) {
            return;
        }
        
        if (tableId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            tableId = Id.Factory.changePrefix(tableId, EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        }
        final Pid pid = new Pid(tableId, pidAsStr, null);
        final RadClassPresentationDef classDef = env.getApplication().getDefManager().getClassPresentationDef(tableId);
        final EntityModel e = EntityModel.openContextlessModel(env, pid, tableId, classDef.getEditorPresentationIds());
        final org.radixware.kernel.common.client.views.IEntityEditorDialog dlg = env.getApplication().getStandardViewsFactory().newEntityEditorDialog(e);
        if (dlg != null) {
            dlg.execDialog();
        }
    }
    
}

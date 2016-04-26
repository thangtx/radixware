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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.CantOpenSelectorError;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;


import org.radixware.kernel.explorer.views.selector.Selector;


public class AuditLogDialog extends ExplorerDialog {

    private final static Id AUDIT_LOG_CLASS_ID = Id.Factory.loadFrom("aecXXXFKJLLMLNBDKV7ABINU7DB2Q");
    private final static Id AUDIT_LOG_SELECTOR_PRESENTATION_ID = Id.Factory.loadFrom("sprYXXD2YWVBJGFXMNDIGTPHUL5EI");
    private final static Id TABLE_ID_PARAM = Id.Factory.loadFrom("pgp7HEIOQD4GZHVLBLVKTQIZVUXCQ");
    private final static Id OBJECT_PID_PARAM = Id.Factory.loadFrom("pgpNFWQP6O5EREAHKQRM5HKUGECDM");
    private Selector selector;

    private AuditLogDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, null);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
    }

    private void open(final Id tableId, final Pid entityPid) {
        final GroupModel auditLogGroup = GroupModel.openTableContextlessSelectorModel(getEnvironment(), AUDIT_LOG_CLASS_ID, AUDIT_LOG_SELECTOR_PRESENTATION_ID);
        auditLogGroup.getProperty(TABLE_ID_PARAM).setValueObject(tableId.toString());
        auditLogGroup.getProperty(OBJECT_PID_PARAM).setValueObject(entityPid == null ? null : entityPid.toString());

        try {
            selector = (Selector) auditLogGroup.createView();
            selector.open(auditLogGroup);
        } catch (CantOpenSelectorError error) {
            if (error.getCause() instanceof InterruptedException) {
                return;
            } else {
                getEnvironment().processException(error);
            }
            return;
        } catch (Exception ex) {
            getEnvironment().processException(ex);
            return;
        }
        selector.setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.MinimumExpanding);
        selector.setParent(this);
        selector.setObjectName("groupView");
        layout().addWidget(selector);
        addButton(EDialogButtonType.CLOSE);
        rejectButtonClick.connect(this, "close()");
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        //setWindowTitle(ExpValueFormatter.capitalizeIfNecessary(auditLogGroup.getTitle()));
        exec();
    }

    @Override
    public void done(int result) {
        if (selector != null) {
            selector.close(true);
        }
        super.done(result);
    }

    public static void execAuditLogDialog(final IClientEnvironment environment, final QWidget parent, final Id tableId, final Pid entityPid, final String modelTitle) {
        final AuditLogDialog dialog = new AuditLogDialog(environment, parent);
        if (entityPid != null) {
            dialog.setWindowTitle(String.format(environment.getMessageProvider().translate("Editor", "Audit of '%s' Object"), modelTitle));
        } else {
            dialog.setWindowTitle(String.format(environment.getMessageProvider().translate("Editor", "Audit of '%s' Objects"), modelTitle));
        }
        dialog.open(tableId, entityPid);
    }
}

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
package org.radixware.wps.views.selector;

import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class RwtPresentationInfoDialog extends Dialog {

    FormBox layout;

    public RwtPresentationInfoDialog(String title, String classId, String className, String presentationId, String presentationName, String explorerItemId, String pid) {
        setWindowTitle(title);
        layout = new FormBox();
        MessageProvider mp = getEnvironment().getMessageProvider();
        if (classId != null && !classId.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog", "Class id:"), classId);
        }
        
        if (className != null && !className.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog", "Class name:"), className);
        }
        
        addNewRow(mp.translate("PresentationInfoDialog", "Presentation id::"), presentationId);
        addNewRow(mp.translate("PresentationInfoDialog", "Presentation name:"), presentationName);
        if (explorerItemId != null && !explorerItemId.isEmpty()) {
            addNewRow(mp.translate("PresentationInfoDialog", "ExplorerItem id:"), explorerItemId);
        }
        if (pid != null) {
            addNewRow(mp.translate("PresentationInfoDialog", "Pid:"), pid);
        }
        add(layout);
        layout.getHtml().setAttr("isadjustwidth", null);
        layout.setTop(5);
        layout.setLeft(5);
        layout.getAnchors().setBottom(new Anchors.Anchor(1, -5));
        layout.getAnchors().setRight(new Anchors.Anchor(1, -5));
        setMinimumWidth(380);
        addCloseAction(EDialogButtonType.OK);
    }

    private void addNewRow(String label, String value) {
        ValStrEditorController editorController = new ValStrEditorController(getEnvironment());
        editorController.setValue(value);
        editorController.setMandatory(true);
        editorController.setReadOnly(true);
        layout.addLabledEditor(label, (UIObject)editorController.getValEditor());
    }
}

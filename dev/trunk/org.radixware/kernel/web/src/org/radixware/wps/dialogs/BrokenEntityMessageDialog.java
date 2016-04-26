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

package org.radixware.wps.dialogs;

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.StaticText;
import org.radixware.wps.rwt.VerticalBoxContainer;


public class BrokenEntityMessageDialog extends Dialog {

    public BrokenEntityMessageDialog(final WpsEnvironment env, final BrokenEntityModel entityModel) {
        super(env.getDialogDisplayer(), env.getMessageProvider().translate("ExplorerException", "Broken Entity Object"), false);
        setWindowIcon(env.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR));
        final MessageProvider messageProvider = env.getMessageProvider();
        final VerticalBoxContainer mainContainer = new VerticalBoxContainer();
        final Label lbMessage;
        if (entityModel.getPid() == null) {
            lbMessage = new Label(messageProvider.translate("ExplorerException", "Error reading entity object"));
        } else {
            final String messageText = messageProvider.translate("ExplorerException", "Error reading entity object \'%s\'");
            lbMessage = new Label(String.format(messageText, entityModel.getPid().toString()));
        }
        mainContainer.add(lbMessage);

        String detailMessageText = entityModel.getExceptionMessage();
        if (detailMessageText != null && !detailMessageText.isEmpty()) {
            detailMessageText += "\n";
        }
        detailMessageText += entityModel.getExceptionStack();
        final StaticText stDetailMessage = new StaticText();
        stDetailMessage.setText(detailMessageText);
        mainContainer.add(stDetailMessage);
        mainContainer.setAutoSize(stDetailMessage, true);

        add(mainContainer);
        mainContainer.setLeft(1);
        mainContainer.setTop(1);
        mainContainer.getAnchors().setBottom(new Anchors.Anchor(1, -1));
        mainContainer.getAnchors().setRight(new Anchors.Anchor(1, -1));

        setWidth(850);
        setHeight(570);
        addCloseAction(EDialogButtonType.CLOSE);
    }
}

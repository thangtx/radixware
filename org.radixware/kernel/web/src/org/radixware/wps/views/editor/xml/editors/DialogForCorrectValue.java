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

package org.radixware.wps.views.editor.xml.editors;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.editors.xmleditor.model.IXmlValueEditingOptionsProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.XmlModelItem;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.FormBox;


final class DialogForCorrectValue extends Dialog {

    private final XmlValueEditor editor;

    public DialogForCorrectValue(final IClientEnvironment environment,
            final XmlModelItem xmlModel,
            final IXmlValueEditingOptionsProvider editingOptionsProvider) {
        super();
        setWindowTitle(getEnvironment().getMessageProvider().translate("XmlEditor", "Input Correct Value"));
        final FormBox form = new FormBox();
        this.add(form);
        form.getAnchors().setTop(new Anchors.Anchor(0, 1));
        form.getAnchors().setLeft(new Anchors.Anchor(0, 1));
        form.getAnchors().setRight(new Anchors.Anchor(1, 1));
        form.getAnchors().setBottom(new Anchors.Anchor(1, 1));
        editor = new XmlValueEditor(environment, xmlModel, editingOptionsProvider);
        editor.asWidget().setParent(this);
        final String name = xmlModel.getXmlNode().getLocalName();
        form.addLabledEditor(name + ":", editor.asWidget());
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);
    }

    @Override
    protected DialogResult onClose(String action, DialogResult actionResult) {
        if (getValue() == null && actionResult == DialogResult.ACCEPTED) {
            getEnvironment().messageError(getEnvironment().getMessageProvider().translate("XmlEditor", "Wrong Value"), getEnvironment().getMessageProvider().translate("XmlEditor", "Input correctly value"));
            return null;
        }
        return super.onClose(action, actionResult);

    }

    public Object getValue() {
        return editor.getValueObject();
    }
}

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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.client.widgets.IMemoController;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.InputBox.DropDownDelegate;


public class DropDownTextAreaDelegate extends DropDownDelegate<TextArea> {

    private IMemoController memoController;

    public DropDownTextAreaDelegate() {
    }

    public IMemoController getMemoController() {
        return memoController;
    }

    public void setMemoController(IMemoController memoController) {
        this.memoController = memoController;
    }

    @Override
    protected TextArea createUIObject(InputBox box, DisplayController displayController) {
        TextArea memo = new TextArea();
        memo.getHtml().addClass("rwt-ui-border");
        memo.setParent(box);
        memo.setReadOnly(box.isReadOnly());
        memo.setText(memoController != null ? memoController.prepareTextForMemo((String) box.getValue()) : (String) box.getValue());
        memo.setMinimumHeight(80);
        return memo;
    }

    @Override
    protected ToolButton createDropDownButton() {
        final ToolButton listBoxButton = new ToolButton();
        listBoxButton.setText("...");
        return listBoxButton;
    }

    @Override
    protected void updateButton(ToolButton button, InputBox inputBox) {
        button.setVisible(inputBox.getValue() != null || !inputBox.isReadOnly());
    }

    @Override
    protected String getActiveElementName() {
        return "textarea";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterClose(final InputBox box) {
        final TextArea memo = getDropDown();
        box.setValue(memoController != null ? memoController.getFinalText(memo.getText()) : memo.getText());
        super.afterClose(box);
    }
}

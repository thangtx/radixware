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

package org.radixware.wps.rwt.example;

import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TextField;


public class ExampleDialog extends Dialog {

    private Label label = new Label("Some text: ");
    private TextField textField = new TextField("Some text");

    public ExampleDialog(IDialogDisplayer displayer) {
        super(displayer, "Example Dialog");
        this.add(label);
        this.add(textField);
        textField.addTextListener(new TextField.TextChangeListener() {

            @Override
            public void textChanged(String oldText, String newText) {
                label.setText(newText);
            }
        });
        this.addCloseAction("Reject", DialogResult.REJECTED);
        this.addCloseAction("Accept", DialogResult.ACCEPTED);
    }
}

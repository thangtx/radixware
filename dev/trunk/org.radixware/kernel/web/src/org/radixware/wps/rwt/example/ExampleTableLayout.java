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

import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.TextField;
import org.radixware.wps.rwt.UIObject;


public class ExampleTableLayout extends TableLayout {

    public ExampleTableLayout() {
        Row r = addRow();
        Row.Cell c = r.addCell();
        Label label = new Label("This is the very long text label");
        label.setTextWrapDisabled(true);
        c.add(label);
        c = r.addCell();
        final TextField tf1 = new TextField("Text...");
        tf1.setHSizePolicy(SizePolicy.EXPAND);
        c.add(tf1);
        c.setHCoverage(100);




        r = addRow();
        c = r.addCell();
        label = new Label("Short text label");
        label.setTextWrapDisabled(true);
        c.add(label);

        c = r.addCell();
        final TextField tf2 = new TextField("Another text...");
        c.add(tf2);
        tf2.setHSizePolicy(SizePolicy.EXPAND);
        c.setHCoverage(100);

        r = addRow();
        c = r.addCell();
        c.setVSizePolicy(SizePolicy.EXPAND);
        r.setVSizePolicy(SizePolicy.EXPAND);
        c = r.addCell();

        setSizePolicy(SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);


        tf1.addTextListener(new TextField.TextChangeListener() {

            @Override
            public void textChanged(String oldText, String newText) {
                tf2.setText(newText);
            }
        });

        setSizePolicy(SizePolicy.EXPAND, UIObject.SizePolicy.EXPAND);
    }
}

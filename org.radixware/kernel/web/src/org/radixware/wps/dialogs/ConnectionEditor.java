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

import org.radixware.kernel.common.client.eas.connections.ConnectionOptions;
import org.radixware.wps.WpsEnvironment;

import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.GridLayout;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.InvalidStringValueException;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.ValueEditor.ValueChangeListener;


public class ConnectionEditor extends Dialog {

    private static class LabelCell extends GridLayout.Cell {

        public LabelCell(String text) {
            setFitWidth(true);
            Label label = new Label(text);
            label.setTextWrapDisabled(true);
            add(label);
        }
    }

    private static class EditorCell extends GridLayout.Cell {

        public EditorCell(final String value, final ValueChangeListener<String> l) {
            setFitWidth(true);
            final InputBox<String> box = new InputBox<>(new InputBox.ValueController<String>() {

                @Override
                public String getValue(String text) throws InvalidStringValueException {
                    return text;
                }
            });
            add(box);
            box.setValue(value);
            box.addValueChangeListener(l);
        }
    }

    public ConnectionEditor(WpsEnvironment e, final ConnectionOptions options) {
        super(e.getDialogDisplayer(), e.getMessageProvider().translate("ConnectionEditor", "Edit Connection"));
        GridLayout grid = new GridLayout();
        grid.setHCoverage(100);
        add(grid);
        //--------- name------------------------
        GridLayout.Row row = new GridLayout.Row();
        grid.add(row);
        GridLayout.Cell cell = new LabelCell(e.getMessageProvider().translate("ConnectionEditor", "Connection Name:"));
        row.add(cell);
        cell = new EditorCell(options.getName(), new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                options.setName(newValue);
            }
        });
        row.add(cell);
        //--------- user name------------------------
        row = new GridLayout.Row();
        grid.add(row);
        cell = new LabelCell(e.getMessageProvider().translate("ConnectionEditor", "User Name:"));
        row.add(cell);
        cell = new EditorCell(options.getUserName(), new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                options.setUserName(newValue);
            }
        });
        row.add(cell);
        //--------- station name------------------------
        row = new GridLayout.Row();
        grid.add(row);
        cell = new LabelCell(e.getMessageProvider().translate("ConnectionEditor", "Station Name:"));
        row.add(cell);
        cell = new EditorCell(options.getStationName(), new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                options.setStationName(newValue);
            }
        });
        row.add(cell);
        //--------- server address------------------------
        row = new GridLayout.Row();
        grid.add(row);
        cell = new LabelCell(e.getMessageProvider().translate("ConnectionEditor", "Server Address:"));
        row.add(cell);
        cell = new EditorCell(options.getInitialAddressesAsStr(), new ValueChangeListener<String>() {

            @Override
            public void onValueChanged(String oldValue, String newValue) {
                options.setInitialAddressesAsStr(newValue);
            }
        });
        row.add(cell);
        setWidth(300);
        setHeight(200);
        addCloseAction("Ok", DialogResult.ACCEPTED);
        addCloseAction("Cancel", DialogResult.REJECTED);
    }

   
}

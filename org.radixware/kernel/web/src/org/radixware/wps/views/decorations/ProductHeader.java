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

package org.radixware.wps.views.decorations;

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.ButtonBase;
import org.radixware.wps.rwt.ToolBar;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;

import org.radixware.wps.views.RwtAction;


public class ProductHeader extends ProductDecoration {

    private ButtonBase connectButton = new ToolButton();
    private ButtonBase disconnectButton = new ToolButton();

    public ProductHeader(WpsEnvironment env) {
        super(new Div());
        html.setInnerText("Radix Ware");
        setPreferredHeight(20);
        html.setCss("vertical-align", "middle");
        html.addClass("rwt-product-header");

        ToolBar connectionToolBar = new ToolBar();
        connectionToolBar.addAction(new RwtAction(env, ClientIcon.Connection.CONNECT, "Connect..."));
        connectionToolBar.addAction(new RwtAction(env, ClientIcon.Connection.DISCONNECT, "Disconnect..."));

        setSizePolicy(SizePolicy.EXPAND, UIObject.SizePolicy.PREFERRED);
        this.add(connectionToolBar);
//        this.add(connectButton);
//
//
//        connectButton.setToolTip("Connect...");
//
//        connectButton.setText("Connect...");
//
//        connectButton.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Connection.CONNECT));
//        this.add(disconnectButton);
//        disconnectButton.setToolTip("Disconnect");
//
//        disconnectButton.setText("Disconnect");
//        disconnectButton.setIcon(env.getApplication().getImageManager().getIcon(ClientIcon.Connection.DISCONNECT));

//        InputBox spinBox = new InputBox();
//        add(spinBox);
//        spinBox.getHtml().setCss("float", "left");
//        spinBox = new InputBox();
//        add(spinBox);
//        spinBox.getHtml().setCss("float", "left");
//        spinBox.setClearController(new InputBox.ClearController() {
//
//            @Override
//            public Object clear() {
//                return null;
//            }
//        });
//
//        InputBox<Integer> integer = new InputBox<Integer>(new InputBox.DisplayController<Integer>() {
//
//            @Override
//            public String getDisplayValue(Integer value) {
//                return value == null ? "Enter int value..." : value.toString();
//            }
//        });
//        integer.setClearController(new InputBox.ClearController<Integer>() {
//
//            @Override
//            public Integer clear() {
//                return null;
//            }
//        });
//
//        integer.setSpinBoxController(new InputBox.SpinBoxController<Integer>() {
//
//            @Override
//            public Integer getNext(Integer value) {
//                return value == null ? 0 : value.intValue() + 1;
//            }
//
//            @Override
//            public Integer getPrev(Integer value) {
//                return value == null ? 0 : value.intValue() - 1;
//            }
//        });
//        add(integer);
//        integer.getHtml().setCss("float", "left");
//
//        InputBox<String> listBox = new InputBox<String>();
//        listBox.setListBoxController(new InputBox.ListBoxController<String>() {
//
//            @Override
//            public List<String> getValues() {
//                return Arrays.asList(new String[]{"One", "Two", "Three"});
//            }
//        });
//        add(listBox);
        //listBox.getHtml().setCss("float", "left");
    }
}

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

package org.radixware.wps.views.editor;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.UIObject;


public class CustomEditor extends Editor {

    private Container contentPanel = new Container();

    public CustomEditor(IClientEnvironment env) {
        super(env);
        super.add(-1, contentPanel);
        contentPanel.setTop(getMainComponentTop());
        contentPanel.setLeft(0);
        contentPanel.setHCoverage(100);
        contentPanel.getAnchors().setBottom(new Anchors.Anchor(1, -1));
    }

    @Override
    public void add(int index, UIObject child) {
        contentPanel.add(index, child);
    }

    @Override
    public void clear() {
        contentPanel.clear();
    }

    @Override
    public void remove(UIObject child) {
        contentPanel.remove(child);
    }

    @Override
    void addToolBarComponent(int index, UIObject c) {
        super.add(index, c);
    }

    @Override
    UIObject getMainComponent() {
        return contentPanel;
    }

    @Override
    public void open(Model model) {
        getController().open(model);
    }
}

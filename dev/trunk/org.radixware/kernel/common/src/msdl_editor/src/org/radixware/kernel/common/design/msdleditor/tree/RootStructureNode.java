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

package org.radixware.kernel.common.design.msdleditor.tree;

import javax.swing.JPanel;
import org.radixware.kernel.common.design.msdleditor.field.RootPanel;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedEvent;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedListener;
import org.radixware.kernel.common.msdl.RootMsdlScheme;


public class RootStructureNode extends ItemNode implements MsdlFieldStructureChangedListener{
    private RootMsdlScheme rootMsdlScheme;
    private Tree tree;
    public RootStructureNode(Tree tree, RootMsdlScheme definition) {
        this.tree = tree;
        setChildren(new FieldChildren(tree,definition,this));
        this.rootMsdlScheme = definition;
        rootMsdlScheme.getStructureChangedSupport().addEventListener(this);
    }

    @Override
    public String toString() {
        return rootMsdlScheme.getModel().getName();
    }

    @Override
    public JPanel createView() {
        RootPanel rootPanel = new RootPanel();
        rootPanel.open(rootMsdlScheme,null);
        return rootPanel;
    }

    @Override
    public void onEvent(MsdlFieldStructureChangedEvent e) {
        if (e.nameOnly) {
            tree.nameChanged();
        }
        else {
            FieldChildren fc = new FieldChildren(tree,rootMsdlScheme,this);
            setChildren(fc);
            fc.structureChanged();
        }
    }

    public RootMsdlScheme getRootMsdlScheme() {
        return rootMsdlScheme;
    }

}

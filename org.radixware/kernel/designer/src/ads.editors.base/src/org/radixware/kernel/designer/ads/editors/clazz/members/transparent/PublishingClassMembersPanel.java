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

/*
 * PublishingPropertyPanel.java
 *
 * Created on May 18, 2012, 4:19:26 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.awt.BorderLayout;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;


public class PublishingClassMembersPanel extends javax.swing.JPanel {

    private final ClassMemberSelector selector = new ClassMemberSelector(true);

    public PublishingClassMembersPanel() {
        initComponents();

        selector.getSelectorLayout().addFilterComponent(new AccessFilter());
        setLayout(new java.awt.BorderLayout());
        add(selector.getComponent(), BorderLayout.CENTER);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void open(RadixPlatformClass cl, Definition context) {
        selector.open(context, cl);
    }

    public List<ClassMemberItem> getSelectedMembers() {
        return selector.getSelectedItems();
    }
}

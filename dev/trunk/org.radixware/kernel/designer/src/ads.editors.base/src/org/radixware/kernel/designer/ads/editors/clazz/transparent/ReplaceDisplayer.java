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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.ClassMemberItem;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionEvent;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionListener;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class ReplaceDisplayer extends ModalDisplayer implements SelectionListener<ClassMemberItem> {

    public ReplaceDisplayer() {
        super(new ReplaceTransparentPanel());

        getComponent().addSelectionListener(this);
        getComponent().getSelector().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (getDialogDescriptor().isValid()) {
                    close(true);
                }
            }
        });

        setTitle(NbBundle.getMessage(ReplaceDisplayer.class, "ReplaceDisplayer-Title"));
        getDialogDescriptor().setValid(true);
    }

    @Override
    protected void apply() {
        getComponent().removeSelectionListener(this);
    }

    @Override
    public void selectionChanged(SelectionEvent<ClassMemberItem> event) {
        getDialogDescriptor().setValid(event.newValue != null);
    }

    @Override
    public ReplaceTransparentPanel getComponent() {
        return (ReplaceTransparentPanel) super.getComponent();
    }

    public ClassMemberPresenter chooseMemberForReplace() {
        return getComponent().getSelectedPresentation();
    }

    @Override
    protected void beforeShow() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getComponent().requestFocusInWindow();
            }
        });
    }

    public void open(Collection<ClassMemberPresenter> allNotPublished, ClassMemberPresenter source) {
        getComponent().open(allNotPublished, source);
    }
}

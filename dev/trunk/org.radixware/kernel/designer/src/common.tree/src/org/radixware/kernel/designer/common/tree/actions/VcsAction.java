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

package org.radixware.kernel.designer.common.tree.actions;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class VcsAction extends CallableSystemAction implements Presenter.Popup {

    @Override
    public String getName() {
        return "Subversion";
    }

    public VcsAction() {
    }

    @Override
    public void performAction() {
        // nothing - action is menu
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }

    @Override
    public JMenuItem getPopupPresenter() {
//        final Node[] nodes = TopComponent.getRegistry().getActivatedNodes();
//        final VCSContext ctx = VCSContext.forNodes(nodes);
//        final Action[] actions = org.netbeans.modules.subversion.Annotator.getActions(ctx, ActionDestination.PopupMenu);
//        final List<Action> actionsList = new ArrayList<Action>();
//        for (Action action : actions) {
//            actionsList.add(action);
//        }

        final Action[] actions = new Action[]{
            SystemAction.get(RadixSvnStatusAction.class),
            SystemAction.get(RadixSvnDiffAction.class),
            SystemAction.get(RadixSvnUpdateAction.class),
            SystemAction.get(RadixSvnCommitAction.class),
            null,
            SystemAction.get(RadixSvnSearchHistoryAction.class),
            SystemAction.get(RadixSvnRevertAction.class),
            null,
            SystemAction.get(RadixSvnPropertiesAction.class)};

        final JMenu menu = DialogUtils.createMenu(actions, getName());
        return menu;
    }
}

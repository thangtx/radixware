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
package org.radixware.kernel.explorer.webdriver.commands;

import com.trolltech.qt.gui.QWidget;
import org.json.simple.JSONObject;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.views.IEditor;
import org.radixware.kernel.common.client.views.ISelector;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.dialogs.EntityEditorDialog;
import org.radixware.kernel.explorer.dialogs.SelectEntityDialog;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.webdriver.EHttpMethod;
import org.radixware.kernel.explorer.webdriver.WebDrvSession;
import org.radixware.kernel.explorer.webdriver.WebDrvUri;
import static org.radixware.kernel.explorer.webdriver.commands.WindowCmd.getTopLevelWindow;
import org.radixware.kernel.explorer.webdriver.exceptions.WebDrvException;

final class RefreshCmd extends NavigationCmd {

    @Override
    public String getName() {
        return "refresh";
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public EHttpMethod getHttpMethod() {
        return EHttpMethod.POST;
    }

    @Override
    public JSONObject execute(final WebDrvSession session, final WebDrvUri uri, final JSONObject parameter) throws WebDrvException {
        super.execute(session, uri, parameter);
        final QWidget topLevelWindow = getTopLevelWindow();
        IView view = null;
        if (topLevelWindow == session.getEnvironment().getMainWindow()) {
            IExplorerTreeNode n = this.getMainTree(session).getCurrent();
            if(n.isValid() && n.getView() != null && n.getView().getModel() != null)
            {
                view = n.getView().getModel().getView();
            }

        } else if (topLevelWindow instanceof EntityEditorDialog) {
            view = ((EntityEditorDialog) topLevelWindow).getEntityModel().getView();
        } else if (topLevelWindow instanceof SelectEntityDialog) {
            Selector sel = ((SelectEntityDialog) topLevelWindow).getSelector();
            view = sel.getModel().getView();
        }

        if (view != null) {
            Action a = null;
            if (view instanceof ISelector) {
                a = ((ISelector) view).getActions().getRereadAction();
            } else if (view instanceof IEditor) {
                if (((IEditor) view).getEntityModel().isExists()) {
                    a = ((IEditor) view).getActions().getRereadAction();
                }
            }
            if (a != null && a.isVisible() && a.isEnabled()) {
                a.trigger();
            }
        }
        return null;
    }
}

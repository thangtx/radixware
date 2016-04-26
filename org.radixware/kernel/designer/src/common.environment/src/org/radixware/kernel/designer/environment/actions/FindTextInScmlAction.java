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

package org.radixware.kernel.designer.environment.actions;

import java.lang.reflect.InvocationTargetException;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.FindInSources;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.IFindInSourcesCfg;
import org.radixware.kernel.designer.environment.actions.dialogs.FindInScmlCfgDialog;

/**
 * Finds text in scml. Supports regex and wildcads.
 */
public final class FindTextInScmlAction extends CallableSystemAction {

    public FindTextInScmlAction() {
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt F"));
    }

    @Override
    protected String iconResource() {
        return "org/radixware/kernel/common/resources/subversion/search_hist.png";
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(FindTextInScmlAction.class, "CTL_FindTextInScmlAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    @Override
    public void performAction() {
        try {
            final IFindInSourcesCfg[] cfg = new IFindInSourcesCfg[1];
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    cfg[0] = FindInScmlCfgDialog.createCfg();
                }
            });
            if (cfg[0] != null) {
                FindInSources.find(cfg[0]);
            }
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}

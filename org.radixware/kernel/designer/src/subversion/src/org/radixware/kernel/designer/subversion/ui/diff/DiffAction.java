/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2009 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 * 
 * May 2013: adapted to work with RadixWare file-based entities by Compass Plus Limited.
 */

package org.radixware.kernel.designer.subversion.ui.diff;

import org.netbeans.modules.subversion.ui.actions.ContextAction;
import org.netbeans.modules.subversion.util.Context;
import org.netbeans.modules.subversion.*;
import java.io.File;
import org.netbeans.modules.subversion.ui.actions.ActionUtils;
import org.netbeans.modules.subversion.ui.status.SyncFileNode;
import org.netbeans.modules.subversion.util.ClientCheckSupport;
import org.openide.nodes.Node;
import org.openide.util.*;
import org.tigris.subversion.svnclientadapter.ISVNStatus;

/**
 * Diff action shows local changes
 */
public class DiffAction extends ContextAction {

    @Override
    protected String getBaseName(Node[] nodes) {
        return "CTL_MenuItem_Diff";    // NOI18N
    }

    @Override
    protected int getFileEnabledStatus() {
        return getDirectoryEnabledStatus();
    }

    @Override
    protected int getDirectoryEnabledStatus() {
        return FileInformation.STATUS_MANAGED
             & ~FileInformation.STATUS_NOTVERSIONED_EXCLUDED;
    }

    @Override
    protected String iconResource () {
        return "org/netbeans/modules/subversion/resources/icons/diff.png"; // NOI18N
    }

    public static void diff(Context ctx, int type, String contextName, boolean initialStatusRefreshDisabled) {
        MultiDiffPanel panel = new MultiDiffPanel(ctx, type, contextName, initialStatusRefreshDisabled); // spawns bacground DiffPrepareTask
        DiffTopComponent tc = new DiffTopComponent(panel);
        tc.setName(NbBundle.getMessage(DiffAction.class, "CTL_DiffPanel_Title", contextName)); // NOI18N
        tc.open();
        tc.requestActive();
    }

    public static void diff(File file, String rev1, String rev2) {
        MultiDiffPanel panel = new MultiDiffPanel(file, rev1, rev2, false); // spawns bacground DiffPrepareTask
        DiffTopComponent tc = new DiffTopComponent(panel);
        tc.setName(NbBundle.getMessage(DiffAction.class, "CTL_DiffPanel_Title", file.getName())); // NOI18N
        tc.open();
        tc.requestActive();
    }

    public static void diff(File file, ISVNStatus status) {
        MultiDiffPanel panel = new MultiDiffPanel(file, status);
        DiffTopComponent tc = new DiffTopComponent(panel);
        tc.setName(NbBundle.getMessage(DiffAction.class, "CTL_DiffPanel_Title", file.getName())); // NOI18N
        tc.open();
        tc.requestActive();
    }

    @Override
    protected void performContextAction(final Node[] nodes) {
        ClientCheckSupport.getInstance().runInAWTIfAvailable(ActionUtils.cutAmpersand(getRunningName(nodes)), new Runnable() {
            @Override
            public void run() {
                Context ctx = getContext(nodes);
                String contextName = getContextDisplayName(nodes);
                diff(ctx, SvnModuleConfig.getDefault().getLastUsedModificationContext(), contextName, isSvnNodes(nodes));
            }
        });
    }

    /**
     * Returns true if the given nodes are from the versioning view.
     * In such case the deep scan is not required because the files and their statuses should already be known
     * @param nodes
     * @return
     */
    private static boolean isSvnNodes (Node[] nodes) {
        boolean fromSubversionView = true;
        for (Node node : nodes) {
            if (!(node instanceof SyncFileNode)) {
                fromSubversionView = false;
                break;
            }
        }
        return fromSubversionView;
    }   
}

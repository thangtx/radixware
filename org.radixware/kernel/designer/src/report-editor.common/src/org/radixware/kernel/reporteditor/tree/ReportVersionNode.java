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

package org.radixware.kernel.reporteditor.tree;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.designer.ads.editors.clazz.report.tree.AdsReportClassNode;
import org.radixware.kernel.designer.common.tree.actions.DefinitionRenameAction;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class ReportVersionNode extends AdsReportClassNode {

    private final UserReport.ReportVersion version;
    private final ChangeListener versionListener = new ChangeListener() {

        @Override
        public void stateChanged(final ChangeEvent e) {
            updateName();
        }
    };

    private void updateName() {
        updateHtmlDisplayName();
    }

    public ReportVersionNode(final UserReport.ReportVersion version,final AdsReportClassDef report) {
        super(report);
        this.version = version;
        this.version.addChangeListener(versionListener);
        getLookupContent().add(UserExtensionManager.getInstance());
        addCookie(new RemoveVersionAction.Cookie(version));
        addCookie(new MakeVersionCurrentAction.Cookie(version));
        addCookie(new ExportVersionAction.Cookie(version));
    }

    @Override
    public boolean canDestroy() {
        return false;
    }

    @Override
    public Image getIcon(final int type) {
        if (version.isCurrent()) {
            return AdsDefinitionIcon.CLASS_USER_REPORT_CURRENT_VERSION.getImage();
        }
        return super.getIcon(type);
    }

    @Override
    protected DefinitionRenameAction.RenameCookie createRenameCookie() {
        return null;
    }

    @Override
    public void addCustomActions(final List<Action> actions) {
        super.addCustomActions(actions);
        if (!version.isCurrent()) {
            actions.add(SystemAction.get(RemoveVersionAction.class));
            actions.add(SystemAction.get(MakeVersionCurrentAction.class));
        }
        actions.add(null);
        actions.add(SystemAction.get(ExportVersionAction.class));
    }
    
    @Override
    public Transferable clipboardCopy() throws IOException {
        UserExtensionManager.getInstance().removeCopiedReportInfo();
        return super.clipboardCopy();
    }
}

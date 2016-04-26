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
package org.radixware.kernel.designer.ads.build.actions;

import java.util.LinkedList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.check.CheckResultsTopComponent;

public abstract class AbstractBuildAction extends CookieAction {

    private static boolean isSuicideMode = false;

    public static class CompileCookie extends BuildCookie {

        public CompileCookie(final RadixObject radixObject) {
            super(radixObject);
        }

        public CompileCookie(RadixObjectLookupDelegate delegate) {
            super(delegate);
        }
        
    }

    public static class BuildCookie implements Node.Cookie {

        public interface RadixObjectLookupDelegate {

            RadixObject getRadixObject();
        }
        private final transient RadixObject radixObject;
        private final RadixObjectLookupDelegate delegate;

        public BuildCookie(final RadixObject radixObject) {
            this.radixObject = radixObject;
            this.delegate = null;
        }

        public BuildCookie(final RadixObjectLookupDelegate delegate) {
            this.radixObject = null;
            this.delegate = delegate;
        }

        public RadixObject getRadixObject() {
            if (delegate != null) {
                return delegate.getRadixObject();
            } else {
                if (radixObject instanceof AdsReportModelClassDef) {
                    AdsReportClassDef report = ((AdsReportModelClassDef) radixObject).getOwnerClass();
                    if (report instanceof AdsUserReportClassDef) {
                        return report;
                    }
                }
            }
            return radixObject;
        }
    }

    public static void enableSuicideMode() {
        isSuicideMode = true;
    }

    @Override
    protected boolean enable(final Node[] activatedNodes) {
        if (super.enable(activatedNodes)) {
            for (Node node : activatedNodes) {
                final BuildCookie c = node.getCookie(BuildCookie.class);
                RadixObject obj;
                if (c == null || c.getClass() != cookieClasses()[0] || ((obj = c.getRadixObject()) == null) || obj.isReadOnly()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int mode() {
        return MODE_ALL;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true;
    }

    public final void performAction(Node node) {
        final BuildActionExecutor executor = new BuildActionExecutor(new DesignerBuildEnvironment(isSuicideMode, getBuildActionType()));
        build(new Node[]{node}, null, executor, true);
    }

    private BuildOptions build(Node[] nodes, BuildOptions opts, BuildActionExecutor executor, boolean last) {
        final List<RadixObject> contexts = new LinkedList<>();
        BuildOptions.UserModeHandler handler = null;
        for (Node node : nodes) {
            handler = BuildOptions.UserModeHandlerLookup.getUserModeHandler();
            final BuildCookie buildCookie = node.getLookup().lookup(BuildCookie.class);
            if (buildCookie != null) {
                RadixObject obj = buildCookie.getRadixObject();
                if (obj != null) {
                    contexts.add(obj);
                }
            }
        }
        if (handler != null) {
            handler.startBuild();
        }
        try {
            return executor.execute(contexts.toArray(new RadixObject[contexts.size()]), getBuildActionType(), opts, last, handler);
        } finally {
            if (handler != null) {
                handler.finishBuild();
            }
        }
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final BuildActionExecutor executor = new BuildActionExecutor(new DesignerBuildEnvironment(isSuicideMode, getBuildActionType()));

        build(activatedNodes, null, executor, true);

        complete();
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{BuildCookie.class};
    }

    protected abstract EBuildActionType getBuildActionType();

    protected void complete() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();
                if (!checkResults.isEmpty()) {
                    checkResults.open();
                    checkResults.requestVisible();
                    checkResults.requestActive();
                }
            }
        });
    }
}

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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.openide.nodes.Node;
import org.openide.nodes.Node.Cookie;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public abstract class ViewSourceAction extends CookieAction {

    public static abstract class ViewSourceCookie implements Cookie {

        private final IJavaSource def;
        private final ERuntimeEnvironmentType systemComponent;
        private final Set<JavaSourceSupport.CodeType> codeTypes;

        public ViewSourceCookie(final IJavaSource def, final ERuntimeEnvironmentType systemComponent, Set<JavaSourceSupport.CodeType> codeTypes) {
            super();
            this.def = def;
            this.systemComponent = systemComponent;
            this.codeTypes = codeTypes;
        }

        public IJavaSource getDefinition() {
            return def;
        }

        public void viewSource(JavaSourceSupport.CodeType type) {
            DialogUtils.viewSource((RadixObject) this.def, systemComponent, type, -1);
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
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void performAction(final Node[] activatedNodes) {
        for (Node node : activatedNodes) {
            Class<? extends ViewSourceCookie> cookieClass = (Class<? extends ViewSourceCookie>) cookieClasses()[0];
            final ViewSourceCookie viewSourceCookie = node.getCookie(cookieClass);
            if (viewSourceCookie != null) {
                viewSourceCookie.viewSource(null);
            }
        }
    }

    private static class ViewSourceActionImpl implements Action {

        final ViewSourceCookie cookie;
        final JavaSourceSupport.CodeType codeType;

        public ViewSourceActionImpl(ViewSourceCookie cookie, CodeType codeType) {
            this.cookie = cookie;
            this.codeType = codeType;
        }

        @Override
        public Object getValue(String key) {
            if (Action.NAME.equals(key)) {
                return codeType.getName();
            } else {
                return null;
            }
        }

        @Override
        public void putValue(String key, Object value) {
        }

        @Override
        public void setEnabled(boolean b) {
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cookie.viewSource(codeType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JMenuItem getPopupPresenter() {
        final Node[] activatedNodes = getActivatedNodes();
        if (activatedNodes != null) {
            for (Node node : activatedNodes) {
                Class<? extends ViewSourceCookie> cookieClass = (Class<? extends ViewSourceCookie>) cookieClasses()[0];
                final ViewSourceCookie viewSourceCookie = node.getCookie(cookieClass);
                if (viewSourceCookie != null/* && viewSourceCookie.codeTypes.size() > 1*/) {
                    JMenu root = new JMenu(getName());
                    for (CodeType type : viewSourceCookie.codeTypes) {
                        JMenuItem item = new JMenuItem(new ViewSourceActionImpl(viewSourceCookie, type));
                        root.add(item);
                    }
                    return root;
                }
            }
            return super.getPopupPresenter();
        } else {
            return super.getPopupPresenter();
        }
    }
}

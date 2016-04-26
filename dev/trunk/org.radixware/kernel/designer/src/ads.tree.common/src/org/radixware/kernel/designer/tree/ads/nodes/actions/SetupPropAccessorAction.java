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

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.Getter;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.Setter;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public abstract class SetupPropAccessorAction extends AdsDefinitionAction {

    public static abstract class SetupAccessorCookie implements Node.Cookie {

        protected transient final AdsPropertyDef property;

        public SetupAccessorCookie(final AdsPropertyDef property) {
            this.property = property;
        }

        protected abstract void toggleAccessorState();

        protected abstract boolean isAccessorDefined();
    }

    public static class SetupGetterCookie extends SetupAccessorCookie {

        public SetupGetterCookie(final AdsPropertyDef property) {
            super(property);
        }

        @Override
        protected boolean isAccessorDefined() {
            return property.isGetterDefined(EScope.LOCAL);
        }

        @Override
        protected void toggleAccessorState() {
            if (isAccessorDefined() && !DialogUtils.messageConfirmation("Delete getter?")) {
                return;
            }
            property.setGetterDefined(!property.isGetterDefined(EScope.LOCAL));
            if (property.isGetterDefined(EScope.LOCAL)) {
                final Getter getter = property.findGetter(EScope.LOCAL).get();
                if (getter != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            DialogUtils.goToObject(getter);
                        }
                    });
                }
            }
        }
    }

    public static class SetupSetterCookie extends SetupAccessorCookie {

        public SetupSetterCookie(final AdsPropertyDef property) {
            super(property);
        }

        @Override
        protected boolean isAccessorDefined() {
            return property.isSetterDefined(EScope.LOCAL);
        }

        @Override
        protected void toggleAccessorState() {
            if (isAccessorDefined() && !DialogUtils.messageConfirmation("Delete setter?")) {
                return;
            }

            property.setSetterDefined(!property.isSetterDefined(EScope.LOCAL));
            if (property.isSetterDefined(EScope.LOCAL)) {
                final Setter setter = property.findSetter(EScope.LOCAL).get();
                if (setter != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            DialogUtils.goToObject(setter);
                        }
                    });
                }
            }
        }
    }

    public static class SetupPropGetterAction extends SetupPropAccessorAction {

        @Override
        protected Class<? extends SetupAccessorCookie> cookieClass() {
            return SetupGetterCookie.class;
        }

        @Override
        public String getName() {
            return "Use Custom Getter";
        }
    }

    public static class SetupPropSetterAction extends SetupPropAccessorAction {

        @Override
        protected Class<? extends SetupAccessorCookie> cookieClass() {
            return SetupSetterCookie.class;
        }

        @Override
        public String getName() {
            return "Use Custom Setter";
        }
    }

    @Override
    protected boolean calcEnabled(final Node[] nodes) {
        if (nodes.length > 0) {
            final SetupAccessorCookie c = nodes[0].getCookie(cookieClass());
            if (c != null) {
                return !c.property.isReadOnly() && c.property.canDefineAccessors();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected abstract Class<? extends SetupAccessorCookie> cookieClass();

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{cookieClass()};
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            final SetupAccessorCookie c = activatedNodes[0].getCookie(cookieClass());
            if (c != null) {
                c.toggleAccessorState();
            }
        }
    }

    @Override
    public JMenuItem getPopupPresenter() {
        final JCheckBoxMenuItem item = new JCheckBoxMenuItem(this);
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.setHorizontalAlignment(JMenuItem.LEFT);
        item.setIcon(null);
        final Node[] nodes = getActivatedNodes();
        if (nodes.length == 1) {
            final SetupAccessorCookie c = nodes[0].getCookie(cookieClass());
            if (c != null) {
                item.setState(c.isAccessorDefined());
            }
        }
        return item;
    }
}

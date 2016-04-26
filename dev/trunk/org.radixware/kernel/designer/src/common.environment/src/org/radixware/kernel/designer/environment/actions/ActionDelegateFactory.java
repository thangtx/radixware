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

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;


public class ActionDelegateFactory {

    static Action contextAwareDelegate(FileObject fileObject) throws DataObjectNotFoundException, IOException, ClassNotFoundException {
        final Object delegateActionPath = fileObject.getAttribute("delegate");
        if(delegateActionPath == null) {
            throw new NullPointerException("Mandatory attribute \"delegate\" is missed");
        }
        if(!(delegateActionPath instanceof String)) {
            throw new IllegalArgumentException("Attribute \"delegate\" should be a String. Actual type: " + delegateActionPath.getClass().getCanonicalName());
        }
        final Object delegateObject = DataObject.find(FileUtil.getConfigFile((String)delegateActionPath)).
                getLookup().lookup(InstanceCookie.class).instanceCreate();
        if (delegateObject == null) {
            throw new IllegalArgumentException("No such object in SystemFileSystem: " + delegateActionPath);
        }
        final Object iconBase = fileObject.getAttribute("iconBase");
        if (delegateObject instanceof ContextAwareAction) {
            return new ContextAwareDelegate((ContextAwareAction) delegateObject, iconBase == null ? null  : iconBase.toString());
        }
        throw new IllegalArgumentException("Delegate object is not ContextAwareAction");
    }

    private static class ContextAwareDelegate extends AbstractAction implements ContextAwareAction {

        private final ContextAwareAction contextAwareDelegate;
        private final String iconBase;

        public ContextAwareDelegate(final ContextAwareAction delegate, final String iconBase) {
            this.contextAwareDelegate = delegate;
            this.iconBase = iconBase;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            contextAwareDelegate.actionPerformed(e);
        }

        @Override
        public Object getValue(String key) {
            if (key != null && key.equals("iconBase")) {
                return iconBase;
            }
            return contextAwareDelegate.getValue(key);
        }

        @Override
        public void putValue(String key, Object value) {
            contextAwareDelegate.putValue(key, value);
        }

        @Override
        public boolean isEnabled() {
            return contextAwareDelegate.isEnabled();
        }

        @Override
        public void setEnabled(boolean b) {
            contextAwareDelegate.setEnabled(b);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            contextAwareDelegate.removePropertyChangeListener(listener);
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            contextAwareDelegate.addPropertyChangeListener(listener);
        }

        @Override
        public Action createContextAwareInstance(Lookup actionContext) {
            return contextAwareDelegate.createContextAwareInstance(actionContext);
        }
    }
}

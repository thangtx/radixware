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

package org.radixware.kernel.designer.common.dialogs.actions;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.common.dialogs.utils.RadixNbEditorUtils;


/**
 * Register instance of this action to the "Editors/text/x-yourtype/Actions"
 * and put ".shadow" files to the popup menu and toolbar to make hotkey from xml settings work.
 */
public abstract class AbstractRadixContextAwareAction extends AbstractAction implements ContextAwareAction {

    private final Map<JTextComponent, Action> textComponent2contextAwareInstance = new WeakHashMap<JTextComponent, Action>();

    public AbstractRadixContextAwareAction(String name, String iconBase) {
        super(name);
        putValue(AbstractRadixAction.ICON_BASE, iconBase);
        putValue(SHORT_DESCRIPTION, NbBundle.getMessage(this.getClass(), RadixNbEditorUtils.createTooltipKey(name)));
        putValue(AbstractRadixAction.POPUP_TEXT, NbBundle.getMessage(this.getClass(), RadixNbEditorUtils.createTitleKey(name)));
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        final JTextComponent textComponent = extractTextComponentFromEvent(e);
        assert textComponent != null;
        final Action action = textComponent2contextAwareInstance.get(textComponent);
        action.actionPerformed(e);
    }

    @Override
    public final Action createContextAwareInstance(Lookup actionContext) {
        final Action actionImpl = createAction(actionContext);
        fillValues(actionImpl);
        final JTextComponent textComponent = extractTextComponentFromContext(actionContext);
        assert textComponent != null;
        textComponent2contextAwareInstance.put(textComponent, actionImpl);
        return actionImpl;
    }

    private void fillValues(final Action actionImpl) {
        actionImpl.putValue(ACCELERATOR_KEY, getValue(ACCELERATOR_KEY));
        actionImpl.putValue(SHORT_DESCRIPTION, getValue(SHORT_DESCRIPTION));
        actionImpl.putValue("iconBase", getValue("iconBase"));
        actionImpl.putValue(NAME, getValue(NAME));
        actionImpl.putValue("popupText", getValue("popupText"));
    }

    protected abstract Action createAction(Lookup actionContext);

    private static JTextComponent extractTextComponentFromContext(Lookup context) {
        JTextComponent textComponent = context.lookup(JTextComponent.class);
        if (textComponent != null) {
            return textComponent;
        }
        DataObject dataObject = context.lookup(DataObject.class);
        if (dataObject != null) {
            final EditorCookie ec = dataObject.getCookie(EditorCookie.class);
            if (ec != null && ec.getOpenedPanes().length > 0) {
                return ec.getOpenedPanes()[0];
            }
        }
        throw new IllegalStateException("Can't extract text component from actionContext");
    }

    private static JTextComponent extractTextComponentFromEvent(ActionEvent evt) {
        Object source = evt.getSource();
        while (source != null) {
            if (source instanceof JTextComponent) {
                return (JTextComponent) source;
            }
            if (source instanceof Lookup.Provider) {
                final JTextComponent textComponent = ((Lookup.Provider) source).getLookup().lookup(JTextComponent.class);
                if (textComponent != null) {
                    return textComponent;
                }
            }
            if (source instanceof JComponent) {
                if (((JComponent) source).getClientProperty(JTextComponent.class) != null) {
                    return (JTextComponent) ((JComponent) source).getClientProperty(JTextComponent.class);
                }
                if (source instanceof JPopupMenu) {
                    source = ((JPopupMenu) source).getInvoker();
                }
                source = ((JComponent) source).getParent();
            }

        }
        throw new IllegalStateException("Can't extract text component from event");
    }
}

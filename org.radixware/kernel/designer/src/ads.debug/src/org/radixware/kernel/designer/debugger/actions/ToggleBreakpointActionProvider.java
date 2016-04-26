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

package org.radixware.kernel.designer.debugger.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Set;
import javax.swing.text.JTextComponent;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.spi.debugger.ActionsProvider;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.netbeans.spi.debugger.ui.EditorContextDispatcher;
import org.openide.text.Line;
import org.openide.util.WeakListeners;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.debugger.breakpoints.JmlBreakpoint;


@ActionsProvider.Registration(actions = {"toggleBreakpoint"})
public class ToggleBreakpointActionProvider extends ActionsProviderSupport {

    private static final String MIME_TYPE = "text/x-jml";

    private class Listener implements PropertyChangeListener {

        public Listener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            boolean enabled = getCurrentLine() != null;
            setEnabled(ActionsManager.ACTION_TOGGLE_BREAKPOINT, enabled);
        }
    }
    private Listener listener = new Listener();
    private EditorContextDispatcher context = EditorContextDispatcher.getDefault();

    public ToggleBreakpointActionProvider() {
        context.addPropertyChangeListener(MIME_TYPE, WeakListeners.propertyChange(listener, context));
        EditorRegistry.addPropertyChangeListener(listener);
    }

    @Override
    public Set getActions() {
        return Collections.singleton(ActionsManager.ACTION_TOGGLE_BREAKPOINT);
    }

    @Override
    public void doAction(Object action) {
        ScmlInfo line = getCurrentLine();
        if (line == null) {
            return;
        }
        Breakpoint[] breakpoints = DebuggerManager.getDebuggerManager().
                getBreakpoints();
        int i, k = breakpoints.length;
        for (i = 0; i < k; i++) {
            if (breakpoints[i] instanceof JmlBreakpoint && ((JmlBreakpoint) breakpoints[i]).isSameLocation(line.jml, line.line)) {
                DebuggerManager.getDebuggerManager().removeBreakpoint(breakpoints[i]);
                return;
            }
        }

        JmlBreakpoint b = new JmlBreakpoint(line.jml, line.line);
        if (b != null) {
            DebuggerManager.getDebuggerManager().addBreakpoint(b);
        }

    }

    private static final class ScmlInfo {

        final Jml jml;
        final Line line;

        public ScmlInfo(Jml jml, Line line) {
            this.jml = jml;
            this.line = line;
        }
    }

    private static ScmlInfo getCurrentLine() {
        JTextComponent c = EditorRegistry.focusedComponent();
        if (c == null) {
            c = EditorRegistry.lastFocusedComponent();
        }
        if (c instanceof ScmlEditorPane) {
            ScmlEditorPane editor = (ScmlEditorPane) c;
            Line line = editor.getScmlDocument().getLine(editor.getCaret().getDot());            
            Scml scml = editor.getScml();
            if (scml instanceof Jml) {
                return new ScmlInfo((Jml) scml, line);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isEnabled(Object action) {
        return getCurrentLine() != null;
    }
}

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

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.Event;
import java.util.Collections;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.spi.debugger.ActionsProvider;
import org.netbeans.spi.debugger.ActionsProviderListener;
import org.netbeans.spi.debugger.ContextProvider;
import org.openide.util.Cancellable;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.debugger.impl.JVMEventHandler;
import org.radixware.kernel.designer.debugger.RadixDebugger;
import org.radixware.kernel.designer.debugger.RadixDebuggerConnector;


public class StartActionProvider extends ActionsProvider implements Cancellable {

    private RadixDebugger debugger;
    private ContextProvider lookupProvider;

    public StartActionProvider(ContextProvider lookupProvider) {
        debugger = lookupProvider.lookupFirst(null, RadixDebugger.class);
        this.lookupProvider = lookupProvider;
    }

    @Override
    public Set getActions() {
        return Collections.singleton(ActionsManager.ACTION_START);
    }

    @Override
    public void doAction(Object action) {
        System.out.println("Start Debugging");
        RadixDebuggerConnector connector = lookupProvider.lookupFirst(null, RadixDebuggerConnector.class);
        if (connector == null) {
            DialogUtils.messageError("Invalid debugger state. No connector for JVM found");
        }
        VirtualMachine virtualMachine = connector.getVirtualMachine();
        if (virtualMachine != null) {
            debugger.getNameResolver().setBranch(connector.getBranch());
            final Object startLock = new Object();
            JVMEventHandler eventHandler = new JVMEventHandler(debugger, virtualMachine, new JVMEventHandler.EventHandler() {

                @Override
                public boolean processEvent(Event event) {
                    synchronized (startLock) {
                        startLock.notifyAll();
                    }
                    return true;
                }
            }, new Runnable() {

                @Override
                public void run() {
                    debugger.finish();
                }
            });
            synchronized (startLock) {

                debugger.setVirtualMachine(virtualMachine, eventHandler, connector.isExplorerDebug());
                eventHandler.start();
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    debugger.finish();
                }
            });

        }
    }

    @Override
    public boolean isEnabled(Object action) {
        return true;
    }

    @Override
    public void addActionsProviderListener(ActionsProviderListener l) {
    }

    @Override
    public void removeActionsProviderListener(ActionsProviderListener l) {
    }

    @Override
    public boolean cancel() {
        return false;
    }
}

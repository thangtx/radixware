/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.debugger;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.spi.Connection;
import com.sun.tools.jdi.SocketTransportService;
import java.io.IOException;
import org.radixware.kernel.common.repository.Branch;

/**
 *
 * @author akrylov
 */
public class RadixDebuggerAttachConnector extends RadixDebuggerConnector {

    public static final String ID = "RadixWare-Debug-AttachConnector";
    private Branch branch;

    public RadixDebuggerAttachConnector(Branch branch) {       
        this.branch = branch;
    }

    @Override
    public VirtualMachine getVirtualMachine() {
        final VirtualMachineManager vmmanager = Bootstrap.virtualMachineManager();
        com.sun.tools.jdi.SocketTransportService service = new SocketTransportService();
        try {
            Connection connection = service.attach("localhost:9999", 10000, 10000);
            return vmmanager.createVirtualMachine(connection);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public Branch getBranch() {
        return branch;
    }

    @Override
    public boolean isExplorerDebug() {
        return false;
    }

}

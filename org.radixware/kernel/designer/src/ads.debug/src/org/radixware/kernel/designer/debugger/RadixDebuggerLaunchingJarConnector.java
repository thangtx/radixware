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

package org.radixware.kernel.designer.debugger;

import com.sun.jdi.connect.LaunchingConnector;
import org.radixware.kernel.common.repository.Branch;


public class RadixDebuggerLaunchingJarConnector extends RadixDebuggerLaunchingConnector {
    private final String arguments;
    private final String jvmArgs;
    private final String jar;


    RadixDebuggerLaunchingJarConnector(Branch branch, String jar, String jvmArgs, String programArguments, boolean isExplorerDebug, String workDir) {
        super(branch, isExplorerDebug, workDir);
        this.arguments = programArguments;
        this.jar = jar;
        this.jvmArgs = jvmArgs;
    }


    @Override
    protected String createCommandLine(LaunchingConnector launcher, String address) {
        String transportName = launcher.transport().name();
        StringBuilder cmdLine = new StringBuilder();
        cmdLine.append("java ").
                append(jvmArgs).
                append(" -agentlib:jdwp=transport=").
                append(transportName).
                append(",suspend=y").
                append(",address=").
                append(address).
                append(" -jar ").
                append(jar).
                append(" ").
                append(arguments);
        return cmdLine.toString();
    }
}

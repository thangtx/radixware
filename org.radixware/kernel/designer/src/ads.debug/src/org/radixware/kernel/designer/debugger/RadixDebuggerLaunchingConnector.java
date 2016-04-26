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

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector.Argument;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.connect.spi.Connection;
import com.sun.jdi.connect.spi.TransportService;
import com.sun.jdi.connect.spi.TransportService.ListenKey;
import com.sun.tools.jdi.RawCommandLineLauncher;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Map;
import java.util.Random;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public abstract class RadixDebuggerLaunchingConnector extends RadixDebuggerConnector {

    private static class RadixCommandLineLauncher extends RawCommandLineLauncher {

        private class LaunchHelper {

            private final TransportService.ListenKey listenKey;
            private final TransportService transportService;
            private final String[] commandArray;
            private Process process = null;
            private Connection connection = null;
            private IOException acceptException = null;
            private boolean exited = false;

            LaunchHelper(String[] commandArray, TransportService.ListenKey listenKey, TransportService transportService) {
                this.commandArray = commandArray;
                this.listenKey = listenKey;
                this.transportService = transportService;
            }

            String commandString() {
                final StringBuilder str = new StringBuilder();
                for (int i = 0; i < commandArray.length; i++) {
                    if (i > 0) {
                        str.append(" ");
                    }
                    str.append(commandArray[i]);
                }
                return str.toString();
            }

            synchronized void launchAndAccept() throws IOException, VMStartException {

                process = Runtime.getRuntime().exec(commandArray, null, workDir);

                final Thread acceptingThread = acceptConnection();
                final Thread monitoringThread = monitorTarget();
                try {
                    while (connection == null && acceptException == null && !exited) {
                        wait();
                    }

                    if (exited) {
                        throw new VMStartException("VM initialization failed for: " + commandString(), process);
                    }
                    if (acceptException != null) {
                        throw acceptException;
                    }
                } catch (InterruptedException e) {
                    throw new InterruptedIOException("Interrupted during accept");
                } finally {
                    acceptingThread.interrupt();
                    monitoringThread.interrupt();
                }
            }

            Process process() {
                return process;
            }

            Connection connection() {
                return connection;
            }

            synchronized void notifyOfExit() {
                exited = true;
                notify();
            }

            synchronized void notifyOfConnection(Connection connection) {
                this.connection = connection;
                notify();
            }

            synchronized void notifyOfAcceptException(IOException acceptException) {
                this.acceptException = acceptException;
                notify();
            }

            Thread monitorTarget() {
                final Thread thread = new Thread(threadGroup, "launched target monitor") {

                    @Override
                    public void run() {
                        try {
                            process.waitFor();
                            notifyOfExit();
                        } catch (InterruptedException e) {
                            // Connection has been established, stop monitoring
                        }
                    }
                };
                thread.setDaemon(true);
                thread.start();
                return thread;
            }

            Thread acceptConnection() {
                final Thread thread = new Thread(threadGroup, "connection acceptor") {

                    @Override
                    public void run() {
                        try {
                            final Connection connection = transportService.accept(listenKey, 0, 0);
                            notifyOfConnection(connection);
                        } catch (InterruptedIOException e) {
                        } catch (IOException e) {
                            notifyOfAcceptException(e);
                        }
                    }
                };
                thread.setDaemon(true);
                thread.start();
                return thread;
            }
        }

        private ThreadGroup threadGroup;
        private final File workDir;

        public RadixCommandLineLauncher(File workDir) {

            this.workDir = workDir;
            threadGroup = Thread.currentThread().getThreadGroup();

            ThreadGroup parent = threadGroup.getParent();
            while (parent != null) {
                threadGroup = parent;
                parent = threadGroup.getParent();
            }
        }

        @Override
        protected VirtualMachine launch(String[] commandArray, String address, ListenKey listenKey, TransportService ts) throws IOException, VMStartException {
            final VirtualMachineManager vmmanager = Bootstrap.virtualMachineManager();

            final LaunchHelper launchHelper = new LaunchHelper(commandArray, listenKey, ts);
            launchHelper.launchAndAccept();

            return vmmanager.createVirtualMachine(
                    launchHelper.connection(), launchHelper.process());
        }
    }

    public static final String ID = "RadixWare-Debug-LaunchingConnector";
    private final boolean isExplorerDebug;
    private final Branch branch;
    private final File workDir;

    public RadixDebuggerLaunchingConnector(Branch branch, boolean isExplorerDebug, String workDir) {
        this.isExplorerDebug = isExplorerDebug;
        this.branch = branch;
        this.workDir = workDir != null && !workDir.isEmpty() ? new File(workDir) : null;
    }

    @Override
    public boolean isExplorerDebug() {
        return isExplorerDebug;
    }

    protected abstract String createCommandLine(LaunchingConnector launcher, String address);

    @Override
    public VirtualMachine getVirtualMachine() {
        try {
            LaunchingConnector launcher = new RadixCommandLineLauncher(workDir);
            @SuppressWarnings("unchecked")
            Map<String, ? extends Argument> launcherArguments = launcher.defaultArguments();
            Argument address = launcherArguments.get("address");
            String addressString = "localhost:" + String.valueOf(1031 + new Random().nextInt(499));
            address.setValue(addressString);
            Argument command = launcherArguments.get("command");
            command.setValue(createCommandLine(launcher, addressString));
            VirtualMachine machine = launcher.launch(launcherArguments);
            return machine;
        } catch (final IOException | IllegalConnectorArgumentsException | VMStartException ex) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    DialogUtils.messageError(ex.getMessage());
                }
            });

        }
        return null;
    }

    @Override
    public Branch getBranch() {
        return branch;
    }
}

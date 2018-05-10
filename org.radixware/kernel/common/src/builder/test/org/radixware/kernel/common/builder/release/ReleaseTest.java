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
package org.radixware.kernel.common.builder.release;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.*;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.console.ConsoleEnvironment;
import org.radixware.kernel.common.builder.console.ConsoleFlowLogger;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.enums.EReleaseStatus;
import org.radixware.kernel.common.enums.ESvnAuthType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;

public class ReleaseTest {

    private ReleaseTest() {
    }

    static class TestFrame extends JFrame {

        public TestFrame() throws HeadlessException {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setMinimumSize(new Dimension(400, 300));
            setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
            setTitle("ReleaseTest");

            final JButton start = new JButton("Start release");
            final JCheckBox chbCompile = new JCheckBox("Compile");

            start.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    start.setEnabled(false);
                    try {

                        final Branch branch = Branch.Factory.loadFromDir(new File("/home/share/devtrunk/trunk_light"));
                        ReleaseTester releaseTester = new ReleaseTester();
                        ReleaseSettings settings = releaseTester.createSettings(branch);

                        settings.setPerformCleanAndBuild(chbCompile.isSelected());

                        releaseTester.perform(branch, settings, new Runnable() {

                            @Override
                            public void run() {
                                start.setEnabled(true);
                            }
                        });
                    } catch (IOException ex) {
                        System.out.println("Error " + ex.getMessage());
                        ex.fillInStackTrace();
                        ex.printStackTrace();
                    }
                }
            });

            add(chbCompile);
            add(start);

        }
    }

    static class ReleaseTester {

        public ReleaseSettings createSettings(final Branch branch) {
            final ReleaseTestEnvironment releaseTestEnvironment = new ReleaseTestEnvironment();
            final ReleaseSettings settings = new ReleaseSettings(branch,
                    releaseTestEnvironment.getFlowLogger(),
                    new ReleaseTestEnvironment(),
                    false, null);

            settings.setAuthType(ESvnAuthType.SVN_PASSWORD);
            settings.setUserName("michael");
            settings.setClientURI("http://localhost/svn/dev/trunk_light");
            settings.setPerformCleanAndBuild(false);
            settings.setStatus(EReleaseStatus.TEST);

            return settings;
        }

        void perform(final Branch branch, final ReleaseSettings settings, final Runnable callBack) throws IOException {

            final Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    branch.visit(new IVisitor() {

                        @Override
                        public void accept(RadixObject radixObject) {
                        }
                    }, VisitorProviderFactory.createDefaultVisitorProvider());

                    ReleaseBuilder builder = new ReleaseBuilder(settings);
                    try {
                        if (builder.process()) {
                            settings.getLogger().success();
                        } else {
                            settings.getLogger().failure();
                        }
                    } catch (RadixError e) {
                        System.out.println(e.toString());
                        settings.getLogger().fatal(e);
                        settings.getLogger().failure();
                    }

                    SwingUtilities.invokeLater(callBack);
                }
            });

            thread.setDaemon(true);
            thread.start();
        }
    }

    static class ReleaseTestEnvironment extends DefaultBuildEnvironment {

        private IFlowLogger flowLogger = new ConsoleFlowLogger() {

            @Override
            public boolean recoverableError(String message) {
                return confirmation(message);
            }

            @Override
            public boolean confirmation(String message) {
                return JOptionPane.showConfirmDialog(null, message, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
            }
        };

        @Override
        public Logger getLogger() {
            return Logger.getLogger(ConsoleEnvironment.class.getName());
        }

        @Override
        public IFlowLogger getFlowLogger() {
            return flowLogger;
        }

        @Override
        public EBuildActionType getActionType() {
            return EBuildActionType.RELEASE;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TestFrame().setVisible(true);
            }
        });
    }
}

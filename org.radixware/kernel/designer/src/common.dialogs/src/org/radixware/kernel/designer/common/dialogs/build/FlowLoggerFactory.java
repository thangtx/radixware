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

package org.radixware.kernel.designer.common.dialogs.build;

import java.awt.Color;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.util.lookup.Lookups;
import org.openide.windows.*;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public final class FlowLoggerFactory {

    public static final IFlowLogger newBuildLogger(EBuildActionType action) {
        return new BuildFlowLogger(action);
    }

    public static final IFlowLogger newReleaseLogger() {
        return new BuildFlowLogger("Make Release");
    }

    private static class BuildFlowLogger implements IFlowLogger, Cancellable {

        private final String name;
        EBuildActionType action;

        BuildFlowLogger(String name) {
            this.name = name;
            getIO().select();
        }

        BuildFlowLogger(EBuildActionType actions) {
            this.action = actions;
            switch (actions) {
                case RELEASE:
                    name = "Release Build";
                    break;
                case API_COMPARE:
                case API_COMPATIBILTY_CHECK:
                    name = actions.getName();
                    break;
                default:
                    name = "Build";
            }
            getIO().select();
        }
        InputOutput io = null;

        private InputOutput getIO() {
            if (io == null) {
                io = IOProvider.getDefault().getIO(name, false);
                try {
                    io.getOut().reset();
                    io.getIn().reset();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }

            return io;
        }

        @Override
        public boolean fatal(String message) {
            try {
                IOColorLines.println(getIO(), message, Color.RED);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            setActive();
            return false;
        }

        @Override
        public boolean fatal(Exception e) {
            try {
                IOColorLines.println(getIO(), e.getMessage(), Color.RED);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            e.printStackTrace(getIO().getErr());
            setActive();
            return false;
        }

        @Override
        public void error(String message) {
            getIO().getErr().println(message);
            setActive();
        }

        @Override
        public boolean recoverableError(String message) {
            try {
                return DialogUtils.messageErrorWithIgnore(message);
            } finally {

                getIO().getErr().println(clearHtml(message));

                setActive();
            }
        }

        private String clearHtml(String message) {
            if (message.startsWith("<html>")) {
                return message.replace("<table>", "").
                        replace("</table>", "").
                        replace("</tbody>", "").
                        replace("<tbody>", "").
                        replace("<tr>", "").
                        replace("<td>", "").
                        replace("</tr>", "").
                        replace("</td>", "").
                        replace("<html>", "").
                        replace("</html>", "").
                        replace("<b>", "").
                                replace("</b>", "").
                                replace("</br>", "").
                        replace("<br>", "");
            } else {
                return message;
            }
        }

        @Override
        public boolean confirmation(String message) {
            try {
                return DialogUtils.messageConfirmation(message);
            } finally {
                getIO().getOut().println(message);
                setActive();
            }
        }

        @Override
        public void message(String message) {
            getIO().getOut().println(message);
            setActive();
        }

        @Override
        public void message(String reporter, String message) {
            try {
                IOColorLines.println(getIO(), reporter, Color.GRAY.darker());
            } catch (IOException ex) {
               Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            getIO().getOut().println(message);
        }

        @Override
        public void failure() {
            if (IOColorLines.isSupported(getIO())) {
                try {
                    IOColorLines.println(getIO(), failureMsg, Color.RED);
                } catch (IOException ex) {
                    getIO().getOut().println(failureMsg);
                }
            } else {
                getIO().getOut().println(failureMsg);
            }
            setActive();
        }
        String successMsg = "SUCCESSFULL";
        String failureMsg = "FAILED";

        @Override
        public void success() {
            if (IOColorLines.isSupported(getIO())) {
                try {
                    IOColorLines.println(getIO(), successMsg, Color.green);
                } catch (IOException ex) {
                    getIO().getOut().println(successMsg);
                }
            } else {
                getIO().getOut().println(successMsg);
            }
        }

        @Override
        public void finished(String message, long time, boolean success) {
            String finalMessage = name.toUpperCase() + (success ? " SUCCESSFULL " : " FAILED ") + MessageFormat.format("{0,time,mm:ss}", time);
            try {
                IOColorLines.println(getIO(), finalMessage, success ? Color.green : Color.RED);
            } catch (IOException ex) {
                getIO().getOut().println(finalMessage);
            }
        }
        private volatile boolean isCancelled = false;

        @Override
        public Cancellable getCancellable() {
            return this;
        }

        @Override
        public boolean cancel() {
            isCancelled = true;
            return true;
        }

        @Override
        public boolean wasCancelled() {
            return isCancelled;
        }

        @Override
        public void setActive() {
            if (action == null) {
                getIO().select();
            }
        }

        @Override
        public boolean showMessageEditor(JPanel panel, String title) {
            Displayer displayer = new Displayer(panel, title);
            return displayer.showModal();
        }

        @Override
        public void stateMessage(String message) {
            try {
                IOColorLines.println(getIO(), message, Color.blue.darker().darker());
            } catch (IOException ex) {
                getIO().getOut().println(message);
            }
        }

        private static class Listener implements OutputListener {

            private final RadixObject obj;
            private final RadixProblem problem;

            public Listener(RadixObject obj, RadixProblem problem) {
                this.obj = obj;
                this.problem = problem;
            }

            @Override
            public void outputLineSelected(OutputEvent oe) {
            }

            @Override
            public void outputLineAction(OutputEvent oe) {
                DialogUtils.goToObject(obj, new OpenInfo(obj, Lookups.fixed(problem)));
            }

            @Override
            public void outputLineCleared(OutputEvent oe) {
            }
        }

        private RadixObject findDefinition(RadixObject obj) {
            Definition def = obj.getOwnerDefinition();
            return def == null ? obj : def;
        }

        @Override
        public void problem(RadixProblem problem) {
            StringBuilder message = new StringBuilder();
            Color c;
            if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                message.append("[error]   ");
                c = Color.red;
            } else {
                message.append("[warning] ");
                c = Color.red.darker().darker();
            }
            message.append(problem.getMessage());
            message.append(" at");
            try {
                IOColorLines.println(getIO(), message, c);
            } catch (IOException e) {
                getIO().getOut().println(message);
            }
            message.setLength(0);
            message.append("    ").append(findDefinition(problem.getSource()).getQualifiedName());
            try {
                getIO().getOut().println(message.toString(), new Listener(problem.getSource(), problem));
            } catch (IOException e) {
                getIO().getOut().println(message.toString());
            }
        }

        private static class Displayer extends ModalDisplayer {

            public Displayer(JPanel panel, String title) {
                super(panel, title);
                this.getDialogDescriptor().setClosingOptions(getOptions());
            }

            @Override
            public final Object[] getOptions() {
                return new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION};
            }

            @Override
            public void onClosing() {
                super.onClosing();
            }

            @Override
            protected boolean canClose() {
                return true;// DialogUtils.messageConfirmation("Do you really want to cancel?");
            }
        }
    }
}

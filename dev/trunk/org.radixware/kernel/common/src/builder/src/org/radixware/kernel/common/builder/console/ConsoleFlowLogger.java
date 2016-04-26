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
package org.radixware.kernel.common.builder.console;

import java.io.PrintStream;
import java.text.MessageFormat;
import javax.swing.JPanel;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;

public class ConsoleFlowLogger implements IFlowLogger {

    protected void printError(String stirng) {
        System.err.print(stirng);
    }

    protected void printMessage(String stirng) {
        System.err.print(stirng);
    }

    private void printlnError(String stirng) {
        printError(stirng);
        printError("\n");
    }

    private void printlnMessage(String stirng) {
        printMessage(stirng);
        printMessage("\n");
    }

    @Override
    public boolean fatal(String message) {
        printlnError(message);
        return false;
    }

    @Override
    public void stateMessage(String message) {
        message(message);
    }

    @Override
    public void message(String reporter, String message) {
        printMessage("[" + reporter + "]: ");
        printlnMessage(message);
    }

    @Override
    public boolean fatal(Exception e) {
        e.printStackTrace();
        return false;
    }

    @Override
    public void error(String message) {
        printlnError(message);
    }

    @Override
    public boolean recoverableError(String message) {
        printlnError(message);
        return false;
    }

    @Override
    public void message(String message) {
        if (System.getProperty(ConsoleBuilder.BUILD_QUIET) == null) {
            printlnMessage(message);
        }
    }

    @Override
    public void failure() {
        printlnError("FAILURE");
    }

    @Override
    public void success() {
        printlnMessage("SUCCESS");
    }

    @Override
    public Cancellable getCancellable() {
        return null;
    }

    @Override
    public void finished(String message, long time, boolean success) {
        printlnMessage(message + (success ? " SUCCESSFULL " : " FAILED ") + MessageFormat.format("{0,time,mm:ss}", time));
    }

    @Override
    public void setActive() {
    }

    @Override
    public boolean confirmation(String message) {
        printlnMessage(message);
        return false;
    }

    @Override
    public boolean showMessageEditor(JPanel panel, String title) {
        return true;
    }

    @Override
    public void problem(RadixProblem problem) {
        StringBuilder message = new StringBuilder();
        boolean error = false;
        if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
            message.append("[error]   ");
            error = true;
        } else {
            message.append("[warning] ");

        }
        message.append(problem.getMessage());
        message.append("\n    at ").append(problem.getSource().getQualifiedName());
        if (error) {
            printlnError(message.toString());
        } else {
            printlnMessage(message.toString());
        }
    }
}

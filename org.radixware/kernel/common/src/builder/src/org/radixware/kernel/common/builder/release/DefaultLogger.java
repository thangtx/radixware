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

import javax.swing.JPanel;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


class DefaultLogger implements IFlowLogger {

    @Override
    public boolean fatal(String message) {
        System.err.println("Error: " + message);
        return false;
    }

    @Override
    public void message(String reporter, String message) {
        System.out.println(reporter + ":");
        System.out.println(message);
    }

    @Override
    public void stateMessage(String message) {
        message(message);
    }

    @Override
    public void error(String message) {
        System.err.println("Error: " + message);
    }
    private static final BufferedReader STDIN = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));

    @Override
    public boolean recoverableError(String message) {
        System.out.println(message);
        System.out.println("Continue (y/n)?: ");

        while (true) {
            String conf = null;
            try {
                conf = STDIN.readLine();
            } catch (IOException ex) {
                Logger.getLogger(DefaultLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
            if ("y".equals(conf) || "Y".equals(conf) || "yes".equals(conf)) {
                return true;
            } else if ("n".equals(conf) || "N".equals(conf) || "no".equals(conf)) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n' ");
        }
    }

    @Override
    public void message(String message) {
        System.out.println(message);
    }

    @Override
    public void failure() {
        System.out.println("RELEASE FAILED");
    }

    @Override
    public void success() {
        System.out.println("RELEASE SUCCEED");
    }

    @Override
    public boolean fatal(Exception e) {
        System.err.println("Fatal: " + e.getMessage());
        e.printStackTrace();
        return false;
    }

    @Override
    public boolean confirmation(String message) {
        System.out.println(message);
        return false;
    }

    @Override
    public Cancellable getCancellable() {
        return null;
    }

    @Override
    public void finished(String message, long time, boolean success) {
        System.out.println(message + (success ? " SUCCESSFULL " : " FAILED ") + MessageFormat.format("{0,time,mm:ss}", time));
    }

    @Override
    public void setActive() {
    }

    @Override
    public boolean showMessageEditor(JPanel panel, String title) {
        return true;
    }

    @Override
    public void problem(RadixProblem problem) {        
    }
}

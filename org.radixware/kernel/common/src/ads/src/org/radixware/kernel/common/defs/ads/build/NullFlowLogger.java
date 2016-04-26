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

package org.radixware.kernel.common.defs.ads.build;

import javax.swing.JPanel;
import org.radixware.kernel.common.check.RadixProblem;


public class NullFlowLogger implements IFlowLogger {

    @Override
    public boolean fatal(String message) {
        return false;
    }

    @Override
    public void message(String reporter, String message) {
    }

    @Override
    public boolean fatal(Exception e) {
        return false;
    }

    @Override
    public void stateMessage(String message) {
        message(message);
    }

    @Override
    public void error(String message) {
    }

    @Override
    public boolean recoverableError(String message) {
        return false;
    }

    @Override
    public void message(String message) {
    }

    @Override
    public void failure() {
    }

    @Override
    public void success() {
    }

    @Override
    public Cancellable getCancellable() {
        return null;
    }

    @Override
    public void finished(String message, long time, boolean success) {
    }

    @Override
    public void setActive() {
    }

    @Override
    public boolean confirmation(String message) {
        return true;
    }

    @Override
    public boolean showMessageEditor(JPanel panel, String title) {
        return true;
    }

    @Override
    public void problem(RadixProblem problem) {
        
    }
}

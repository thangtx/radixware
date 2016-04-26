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

/**
 * Release process message logger interface
 */
public interface IFlowLogger {

    public interface INotification {

        public String getSenderAddress();

        public void setSenderAddress(String sender);

        public String getSubscribers();

        public String getSubject();

        public void setSubject(String subject);

        public String getBody();

        public void setBody(String body);
    }

    /**
     * Log fatal error message. Must return false
     */
    public boolean fatal(String message);

    public boolean fatal(Exception e);

    /**
     * Log error message. No user interaction
     */
    public void error(String message);

    /**
     * Interacts with user in ignore/cancel style. If cancel alternative is choosen
     * Should return false to interrupt process
     */
    public boolean recoverableError(String message);

    public boolean confirmation(String message);

    /**
     * Log message
     */
    public void message(String message);

    public void stateMessage(String message);

    public void message(String reporter, String message);

    /**
     * Log process failure
     */
    public void failure();

    /**
     * Log process success
     */
    public void success();

    public void finished(String message, long time, boolean success);

    public Cancellable getCancellable();

    public void setActive();

    public boolean showMessageEditor(JPanel panel, String title);

    public void problem(RadixProblem problem);
}

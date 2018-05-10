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

package org.radixware.kernel.server.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EEventSeverity;

public final class TestData extends TestDurationData implements Serializable {

    private static final long serialVersionUID = 12;
    private final String id;
    private final List<TestData> children = new ArrayList<>();
    
    private String title;
    private String error;
    private String stdOut;
    private String notes;
    private String notificationEmail;
    private EEventSeverity result = EEventSeverity.NONE;    

    public TestData(final String id) {
        this.id = id;
    }
    
    public String getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public String getTitle() {
        return title == null ? "#" + id : title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void failed(final String error) {
        this.error = error;
    }

    public String getStdout() {
        return stdOut;
    }

    public void setStdout(String stdout) {
        this.stdOut = stdout;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isOk() {
        return error == null;
    }

    public EEventSeverity getResult() {
        return result;
    }

    public void setResult(EEventSeverity severity) {
        this.result = severity;
    }

    public String getError() {
        return error;
    }

    public List<TestData> getChildren() {
        return children;
    }
}

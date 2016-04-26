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

package org.radixware.kernel.common.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import org.radixware.kernel.common.enums.EMailPriority;

public class Letter {

    public static class Attachment {
        private final File file;
        private final String mimeType;
        
        public Attachment(File file, String mimeType) {
            this.file = file;
            this.mimeType = mimeType;
        }
        
        public File getFile() {
            return file;
        }
        
        public String getMimeType() {
            return mimeType;
        }

        @Override
        public String toString() {
            return "Attachment{" + "file=" + file + ", mimeType=" + mimeType + '}';
        }
    }
    
    ArrayList<Attachment> attachmentsList = new ArrayList<>();
    String textContent = "";
    String subject = "";
    EMailPriority priority = EMailPriority.NORMAL;
    String addressTo;
    String addressFrom;
    Date sendTime;

    public void setTextContent(String text) {
        textContent = text;
    }
    public String getTextContent() {
        return textContent;
    }

    public void setPriority(EMailPriority priority) {
        this.priority = priority;
    }

    public EMailPriority getPriority() {
        return priority;
    }

    public ArrayList<Attachment> getAttachementsList() {
        return attachmentsList;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }
    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }
    public String getAddressFrom() {
        return addressFrom;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject() {
        return subject;
    }

    public Date getSentTime() {
        return sendTime;
    }

    @Override
    public String toString() {
        return "Letter{" + "attachmentsList=" + attachmentsList + ", textContent=" + textContent + ", subject=" + subject + ", priority=" + priority + ", addressTo=" + addressTo + ", addressFrom=" + addressFrom + ", sendTime=" + sendTime + '}';
    }
}

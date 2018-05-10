/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.persocomm;

import org.radixware.schemas.personalcommunications.MessageStatistics;

public final class MessageSendResult {
    public final Long messageId;
    public final long sendBeginTimeNanos;
    
    private MessageStatistics statistics = null;
    private String smppMessageId = null;
    private MessageWithMeta messageWithMeta = null;
    
    public static final MessageSendResult UNKNOWN = new MessageSendResult(null);

    public MessageSendResult(Long messageId, long sendBeginTimeNanos) {
        this.messageId = messageId;
        this.sendBeginTimeNanos = sendBeginTimeNanos;
    }
    
    public MessageSendResult(Long messageId) {
        this(messageId, System.nanoTime());
    }
    
    public MessageStatistics getStatistics() {
        return statistics;
    }

    public MessageSendResult setStatistics(MessageStatistics statistics) {
        this.statistics = statistics;
        return this;
    }

    public String getSmppMessageId() {
        return smppMessageId;
    }

    public MessageSendResult setSmppMessageId(String smppMessageId) {
        this.smppMessageId = smppMessageId;
        return this;
    }

    public MessageWithMeta getMessageWithMeta() {
        return messageWithMeta;
    }

    public MessageSendResult setMessageWithMeta(MessageWithMeta messageWithMeta) {
        this.messageWithMeta = messageWithMeta;
        return this;
    }
}

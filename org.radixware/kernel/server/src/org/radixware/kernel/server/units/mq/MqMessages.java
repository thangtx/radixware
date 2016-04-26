/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.units.mq;

import org.radixware.kernel.server.units.Messages;
import org.radixware.kernel.server.units.persocomm.tools.MultiLangStringWrapper;
import org.radixware.kernel.server.utils.MessagesHelper;

/**
 *
 * @author dsafonov
 */
public final class MqMessages {

    static {
        MessagesHelper.initialize(MqMessages.class, MqMessages.class.getPackage().getName() + ".mess.messages");
    }

    private MqMessages() {
    }

    static volatile String UNIT_TYPE_TITLE;

    static final MultiLangStringWrapper W_MLS_ID_START_OPTIONS = new MultiLangStringWrapper(Messages.MLS_ID_START_OPTIONS);
}

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

package org.radixware.kernel.server.instance;

import org.apache.xmlbeans.XmlObject;


public class UnitCommandResponse {

    final UnitCommand requestCommand;
    final Exception exception;
    final XmlObject response;

    public UnitCommandResponse(final UnitCommand requestCommand, final XmlObject response) {
        this.requestCommand = requestCommand;
        this.response = response;
        this.exception = null;
    }

    public UnitCommandResponse(final UnitCommand requestCommand, final Exception exception) {
        this.requestCommand = requestCommand;
        this.exception = exception;
        this.response = null;
    }

    public UnitCommand getRequestCommand() {
        return requestCommand;
    }

    public Exception getException() {
        return exception;
    }

    public XmlObject getResponse() {
        return response;
    }

    public String asStr() {
        return "Command for unit '" + getRequestCommand().getUnit().getTitle() + "': rq: " + getRequestCommand().getRequestContainer().xmlText() + "\n\nrs: " + getResponse().toString();
    }
}

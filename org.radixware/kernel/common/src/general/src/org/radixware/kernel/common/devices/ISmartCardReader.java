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

package org.radixware.kernel.common.devices;

import java.util.List;

public interface ISmartCardReader {
    List<String> getNames() throws CardException;
    boolean open(String name) throws CardException;
    void close() throws CardException;
    boolean isCardPresent() throws CardException;
    boolean waitForCardAbsent(long timeout) throws CardException;
    boolean waitForCardPresent(long timeout) throws CardException;
    boolean connect(String name, String protocol) throws CardException;
    void disconnect(boolean reset) throws CardException;
    void beginExclusive() throws CardException;
    void endExclusive() throws CardException;
    byte[] getAtr() throws CardException;
    byte[] transmit(byte[] command) throws CardException;    
}
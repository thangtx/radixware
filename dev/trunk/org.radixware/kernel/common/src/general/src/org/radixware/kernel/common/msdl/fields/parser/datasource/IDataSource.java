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

package org.radixware.kernel.common.msdl.fields.parser.datasource;

import java.io.IOException;
import java.util.ArrayList;


public interface IDataSource {
    byte[] get(int len) throws IOException;
    byte get() throws IOException;
    byte[] getAll() throws IOException;
    void setShield(byte b);
    void setShieldedList(ArrayList<Byte> list);
    void setShieldIsHex(boolean isHex);
    boolean hasAvailable() throws IOException;
    boolean hasAvailable(int len) throws IOException;
    void prepend(IDataSourceArray other) throws IOException;
}

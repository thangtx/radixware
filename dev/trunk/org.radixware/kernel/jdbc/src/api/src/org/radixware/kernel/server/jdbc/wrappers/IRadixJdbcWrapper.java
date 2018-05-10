/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc.wrappers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;

public interface IRadixJdbcWrapper {

    ResultSet wrapResultSet(ResultSet rs);

    Blob wrapBlob(Blob blob);

    Clob wrapClob(Clob clob);

    Object wrapObject(Object obj);

    <T> T wrapObject(T obj, Class<T> type);

    Reader wrapReader(Reader r);

    Writer wrapWriter(Writer w);

    InputStream wrapStream(InputStream s);

    OutputStream wrapStream(OutputStream s);

}

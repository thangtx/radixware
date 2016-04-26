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

package org.radixware.kernel.common.client.eas.resources;

import java.io.IOException;

import org.radixware.kernel.common.client.exceptions.TerminalResourceException;
import org.radixware.kernel.common.enums.EFileSeekOriginType;

public interface IFileResource extends ITerminalResource {

    public boolean isOpened();

    public boolean isEof() throws IOException, TerminalResourceException;

    public int read(final byte[] buf) throws IOException, TerminalResourceException;

    public void write(byte[] content) throws IOException, TerminalResourceException;

    public long seek(final long pos, EFileSeekOriginType seekOrigin) throws IOException, TerminalResourceException;

    public void close() throws TerminalResourceException;
}

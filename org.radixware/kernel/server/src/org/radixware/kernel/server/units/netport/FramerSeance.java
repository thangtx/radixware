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
package org.radixware.kernel.server.units.netport;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

final class FramerSeance extends Seance {
//	Framer framer = null;

    FramerSeance(
            final NetPortHandlerUnit unit,
            final NetChannel netChannel,
            final SocketChannel channel) throws IOException, SQLException {
        super(unit, netChannel, channel, netChannel.options.inFrame, netChannel.options.outFrame);
    }
}

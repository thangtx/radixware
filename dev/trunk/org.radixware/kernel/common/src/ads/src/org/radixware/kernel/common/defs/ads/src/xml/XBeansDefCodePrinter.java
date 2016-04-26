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

package org.radixware.kernel.common.defs.ads.src.xml;

import org.radixware.kernel.common.build.xbeans.XBeansJavaAcceptor;
import org.radixware.kernel.common.build.xbeans.XbeansSchemaCodePrinter;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;


class XBeansDefCodePrinter extends XbeansSchemaCodePrinter {

    private final XBeansDefJavaAcceptor acceptor;

    XBeansDefCodePrinter(AbstractXmlDefinition context) {
        acceptor = new XBeansDefJavaAcceptor(context);
    }

    XBeansDefCodePrinter(AbstractXmlDefinition context, XBeansDefJavaAcceptor acceptor) {
        this.acceptor = acceptor;
    }

    @Override
    protected XBeansJavaAcceptor createJavaAcceptor() {
        return acceptor;
    }

    XBeansDefJavaAcceptor getAcceptor() {
        return acceptor;
    }
}

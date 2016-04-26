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

import java.io.IOException;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.build.xbeans.XBeansJavaAcceptor;
import org.radixware.kernel.common.build.xbeans.XBeansTypeAcceptor;
import org.radixware.kernel.common.build.xbeans.XBeansTypeImplAcceptor;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;


class XBeansDefJavaAcceptor implements XBeansJavaAcceptor {

    private final XBeansTypeSystem typeSystem;

    XBeansDefJavaAcceptor(AbstractXmlDefinition context) {
        typeSystem = new XBeansTypeSystem(context);
    }

    XBeansDefJavaAcceptor(XBeansTypeSystem ts) {
        typeSystem = ts;
    }

    @Override
    public XBeansTypeAcceptor createTypeAcceptor() {
        return new XBeansDefTypeAcceptor(typeSystem);
    }

    @Override
    public XBeansTypeImplAcceptor createTypeImplAcceptor() {
        return new XBeansDefTypeImplAcceptor(typeSystem);
    }

    @Override
    public void acceptTopComment(SchemaType sType) throws IOException {
        //do nothing
    }

    @Override
    public void acceptPackageDeclaration(String packageName, boolean isIface) throws IOException {
        if (isIface) {
            typeSystem.setInterfacePackageName(packageName);
        } else {
            typeSystem.setImplementationPackageName(packageName);
        }
    }

    public XBeansTypeSystem getTypeSystem() {
        return typeSystem;
    }
}

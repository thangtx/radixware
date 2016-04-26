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

package org.radixware.kernel.common.build.xbeans;

import java.io.IOException;
import org.apache.xmlbeans.InterfaceExtension;
import org.apache.xmlbeans.SchemaType;


public interface XBeansTypeImplAcceptor {

    public void beginType(SchemaType sType, String shortName, boolean isInner) throws IOException;

    public XBeansTypeImplAcceptor createInnerType();

    public XBeansPropImplAcceptor createPropAcceptor();

    public void endType() throws IOException;

    public void acceptTypeJavaDoc() throws IOException;

    public void acceptClassStart(String baseClass, String[] interfaces) throws IOException;

    public void acceptConstructor() throws IOException;

    public void acceptExtensionImplMethodDecl(String handler, InterfaceExtension.MethodSignature method) throws IOException;

    public void acceptExtensionImplMethodImpl(String handler, InterfaceExtension.MethodSignature method) throws IOException;    
}

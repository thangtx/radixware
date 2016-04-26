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

package org.radixware.kernel.common.client.eas;

import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.schemas.eas.*;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlObjectProcessor;


public class CommandRequestHandle extends RequestHandle {

    private final Class<? extends XmlObject> expectedOutputClass;
    private final Id commandId, propertyId;

    protected CommandRequestHandle(final IClientEnvironment environment, final XmlObject asyncRequest, final Id cmdId, final Id propId, final Class<? extends XmlObject> outputClass) {
        super(environment, asyncRequest, (asyncRequest instanceof CommandRq ? CommandMess.class : ContextlessCommandMess.class));
        commandId = cmdId;
        propertyId = propId;
        expectedOutputClass = outputClass;
    }

    public final Class<? extends XmlObject> getExpectedOutputClass() {
        return expectedOutputClass;
    }

    public final Id getCommandId() {
        return commandId;
    }

    public final Id getPropertyId() {
        return propertyId;
    }

    public final XmlObject getOutput() throws ServiceClientException, InterruptedException {
        final XmlObject out;
        if (getRequest() instanceof CommandRq) {
            final CommandRs response = (CommandRs) getResponse();
            out = XmlObjectProcessor.getXmlObjectFirstChild(response.getOutput());
        } else {
            final ContextlessCommandRs response = (ContextlessCommandRs) getResponse();
            out = XmlObjectProcessor.getXmlObjectFirstChild(response.getOutput());
        }
        if (expectedOutputClass != null) {
            final ClassLoader classLoader = (ClassLoader) environment.getApplication().getDefManager().getClassLoader();
            return XmlObjectProcessor.castToXmlClass(classLoader, out, expectedOutputClass);
        } else {
            return out;
        }
    }
}

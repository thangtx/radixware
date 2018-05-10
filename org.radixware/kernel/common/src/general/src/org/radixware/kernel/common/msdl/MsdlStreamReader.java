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
package org.radixware.kernel.common.msdl;

import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.exceptions.SmioException;
import static org.radixware.kernel.common.msdl.XmlObjectMessagingHandler.clearXml;
import org.radixware.kernel.common.msdl.fields.parser.datasource.DataSourceInputStream;
import org.radixware.kernel.common.msdl.fields.parser.datasource.IDataSource;
import org.radixware.schemas.msdl.Structure;

/**
 *
 * @author npopov
 */
public class MsdlStreamReader implements AutoCloseable {

    private final InputStream is;
    private final IDataSource source;
    private final RootMsdlScheme scheme;

    public MsdlStreamReader(InputStream is, RootMsdlScheme scheme) {
        this.is = is;
        source = new DataSourceInputStream(is);
        this.scheme = scheme;
        resetParser();
    }

    public boolean hasAvailable() throws IOException {
        return source.hasAvailable();
    }

    public boolean readStructuredMessage(XmlObject obj) throws SmioException, IOException {
        if (!hasAvailable()) {
            return false;
        }
        clearXml(obj);
        scheme.getFieldModel().getParser().parse(obj, source);
        return true;
    }

    public void setFictiveParentStructure(Structure defaults) {
        scheme.setFictiveParentStructure(defaults);
    }

    public void setForcedUseFictiveParentStructure(boolean use) {
        scheme.setForcedUseFictiveParentStructure(use);
    }

    private void resetParser() {
        scheme.getFieldModel().clearParser();
    }

    public RootMsdlScheme getRootMsdlScheme() {
        return scheme;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

}

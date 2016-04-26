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

package org.radixware.kernel.common.defs.uds.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;


public class Loader {

    private Loader() {
    }

    public static AdsDefinition loadFromStream(InputStream is, File file, boolean importMode) throws IOException {
        try {
            UdsDefinitionDocument xDoc = UdsDefinitionDocument.Factory.parse(is);
            if (xDoc.getUdsDefinition() != null) {

                AdsDefinition def = loadFromXml(xDoc.getUdsDefinition(), importMode);
                if (def != null) {
                    if (file != null) {
                        def.setFileLastModifiedTime(file.lastModified());
                    } else {
                        def.setFileLastModifiedTime(0l);
                    }
                    def.setEditState(EEditState.NONE);
                    return def;
                }
            }
            throw new RadixError("Definition format is not supported");
        } catch (XmlException ex) {
            return org.radixware.kernel.common.defs.ads.module.Loader.loadFromStream(new FileInputStream(file), file, true,false);
        }
    }

    public static AdsDefinition loadFromRepository(IRepositoryDefinition rep) {
        InputStream is = null;
        try {
            is = rep.getData();
            if (is != null) {                
                return loadFromStream(is, rep.getFile(), false);
            } else {
                throw new IOException("No input stream");
            }

        } catch (IOException ex) {
            throw new RadixError(ex.getMessage(), ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static AdsDefinition loadFromXml(UdsDefinitionDocument.UdsDefinition xDef, boolean importMode) {

        if (xDef != null) {

            if (xDef != null) {
                UdsDefinition def = null;
                if (xDef.getUserFunc() != null) {
                    def = UdsUserFuncDef.Factory.loadFrom(xDef.getUserFunc(), importMode);
                }
                if (def != null) {
                    def.setEditState(EEditState.NONE);
                    return def;
                }

            }
        }
        throw new RadixError("Definition format is not supported");

    }
}

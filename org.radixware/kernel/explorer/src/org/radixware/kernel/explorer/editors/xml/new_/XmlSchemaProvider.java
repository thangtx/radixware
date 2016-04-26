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

package org.radixware.kernel.explorer.editors.xml.new_;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValFilePathEditor;


public class XmlSchemaProvider extends ExplorerDialog implements IXmlSchemaProvider {

    final ValFilePathEditor xmlSchemaFilePathEditor;

    public XmlSchemaProvider(final IClientEnvironment environment) {
        super(environment, null);
        final EditMaskFilePath editMask = new EditMaskFilePath();
        xmlSchemaFilePathEditor = new ValFilePathEditor(environment, this, editMask, false, false);
    }

    @Override
    public String getSchemaForNamespaceUri(final String nameSpaceUri) {
        try {
            final URI uri = new URI(nameSpaceUri);
            return FileUtils.readTextStream(uri.toURL().openStream(), "UTF-8");
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(XmlSchemaProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
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

package org.radixware.kernel.reporteditor.env.saveall;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataObject;

/**
 * DataLoader, converted RadixFileObject into RadixDataObject.
 */
final class RadixDataLoader extends DataLoader {

    private RadixDataLoader() {
        super(RadixDataObject.class.getName());
    }

    @Override
    protected DataObject handleFindDataObject(final FileObject fileObject,final RecognizedFiles recognized) throws IOException {
        recognized.markRecognized(fileObject);
        return new RadixDataObject(fileObject, this);
    }
    private static final RadixDataLoader INSTANCE = new RadixDataLoader();

    public static RadixDataLoader getDefault() {
        return INSTANCE;
    }
}

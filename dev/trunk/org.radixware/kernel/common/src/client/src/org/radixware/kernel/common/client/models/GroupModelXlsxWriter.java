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

package org.radixware.kernel.common.client.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.widgets.selector.ISelectorDataExportOptionsDialog;



public abstract class GroupModelXlsxWriter extends GroupModelWriter{ 

    public GroupModelXlsxWriter(GroupModel groupModel) {
        super(groupModel);
    }

    @Override
    protected final EntityObjectsWriter createEntityObjectsWriter(File file, ISelectorDataExportOptionsDialog dialog, List<SelectorColumnModelItem> columns) throws FileNotFoundException, UnsupportedEncodingException, InvalidFormatException {
        return EntityObjectsWriter.Factory.newXlsxWriter(file, getOptions(), columns);
    }
    
}

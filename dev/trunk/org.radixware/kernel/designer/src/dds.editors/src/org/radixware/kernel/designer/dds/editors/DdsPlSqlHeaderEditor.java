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

package org.radixware.kernel.designer.dds.editors;

import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;


public class DdsPlSqlHeaderEditor extends DdsPlSqlObjectEditor {

    public DdsPlSqlHeaderEditor(DdsPlSqlHeaderDef header) {
        super(header);
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsPlSqlHeaderDef> {

        @Override
        public RadixObjectEditor newInstance(DdsPlSqlHeaderDef header) {
            return new DdsPlSqlHeaderEditor(header);
        }
    }
}

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

import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;


public class DdsPlSqlBodyEditor extends DdsPlSqlObjectEditor {

    public DdsPlSqlBodyEditor(DdsPlSqlBodyDef body) {
        super(body);
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<DdsPlSqlBodyDef> {

        @Override
        public RadixObjectEditor newInstance(DdsPlSqlBodyDef body) {
            return new DdsPlSqlBodyEditor(body);
        }
    }
}

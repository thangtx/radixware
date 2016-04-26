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

package org.radixware.kernel.designer.tree.ads.nodes.defs.msdl;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedEvent;
import org.radixware.kernel.common.msdl.MsdlField.MsdlFieldStructureChangedListener;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class MsdlFieldHtmlNameSupport extends HtmlNameSupport {

    MsdlField field;
    private final MsdlFieldStructureChangedListener l = new MsdlFieldStructureChangedListener() {
        @Override
        public void onEvent(MsdlFieldStructureChangedEvent e) {
            fireChanged();
        }
    };

    public MsdlFieldHtmlNameSupport(MsdlField field) {
        super(field);
        this.field = field;
        field.getStructureChangedSupport().addEventListener(l);
    }

    @Override
    public String getDisplayName() {
        return field.getTreeItemName();
    }

    @Override
    public String getEditorDisplayName() {
        return field.getQualifiedName();
    }

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        /**
         * Registeren in layer.xml
         */
        public Factory() {
        }

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new MsdlFieldHtmlNameSupport((MsdlField) object);
        }
    }
}

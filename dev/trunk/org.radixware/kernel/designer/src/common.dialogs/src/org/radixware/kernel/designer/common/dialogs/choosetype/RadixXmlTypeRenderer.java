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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.awt.Component;
import javax.swing.JList;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;


final class RadixXmlTypeRenderer extends org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer {
    private Object value;

    RadixXmlTypeRenderer(JList list) {
        super(list);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean hasFocus) {
        this.value = object;
        return super.getListCellRendererComponent(list, object, index, isSelected, hasFocus);
    }

    @Override
    public String getObjectName(Object object) {
        if (value instanceof RadixTypeItem) {
            if (((RadixTypeItem) value).isXmlScheme()) {
                return (((RadixTypeItem) value).getDefinition()).getName();
            }
        }
        return value.toString();
    }

    @Override
    public RadixIcon getObjectIcon(Object object) {
        if (value instanceof RadixTypeItem) {
            if (((RadixTypeItem) value).isXmlScheme()) {
                RadixObject def = ((RadixTypeItem) value).getDefinition();
                if (def instanceof AdsXmlSchemeDef) {
                    return AdsDefinitionIcon.XML_SCHEME;
                } else {
                    return AdsDefinitionIcon.MSDL_SCHEME;
                }
            }
        }
        return null;
    }

    @Override
    public String getObjectLocation(Object object) {
        if (value instanceof RadixTypeItem) {
            if (!((RadixTypeItem) value).isXmlScheme()) {
                return ((RadixTypeItem) value).getDefinition().getQualifiedName();
            } else {
                RadixObject def = ((RadixTypeItem) value).getDefinition();
                String module = def.getModule().getName();
                String layer = def.getModule().getSegment().getLayer().getName();
                return layer + ":" + module;
            }
        }
        return "";
    }

    @Override
    public RadixIcon getObjectLocationIcon(Object object) {
        if (value instanceof RadixTypeItem) {
            if (!((RadixTypeItem) value).isXmlScheme()) {
                if (((RadixTypeItem) value).getDefinition() instanceof AdsXmlSchemeDef) {
                    return AdsDefinitionIcon.XML_SCHEME;
                } else {
                    return AdsDefinitionIcon.MSDL_SCHEME;
                }
            }
        }
        return null;
    }

}

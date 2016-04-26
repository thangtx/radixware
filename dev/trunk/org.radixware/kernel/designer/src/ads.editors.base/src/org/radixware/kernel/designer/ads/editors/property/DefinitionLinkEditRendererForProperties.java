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

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;


    public class DefinitionLinkEditRendererForProperties extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list,
            Object object,
            int index,
            boolean isSelected,
            boolean hasFocus) {
            super.getListCellRendererComponent(list, object, index, isSelected, hasFocus);

            if (object instanceof RadixObject){
                RadixObject asRadixObject = (RadixObject)object;
                RadixIcon icon = asRadixObject.getIcon();
                if (icon != null){
                    setIcon(icon.getIcon(13, 13));
                } else {
                    setIcon(RadixWareDesignerIcon.EDIT.NOT_DEFINED.getIcon(13, 13));
                }
                setText(asRadixObject.getQualifiedName());
            } else{
                if (object instanceof Id) {
                    setForeground(Color.RED);
                }
                setIcon(RadixWareDesignerIcon.EDIT.NOT_DEFINED.getIcon(13, 13));
                setText(object != null ? object.toString() : "<Not Defined>");
            }
            return this;
        }
    }
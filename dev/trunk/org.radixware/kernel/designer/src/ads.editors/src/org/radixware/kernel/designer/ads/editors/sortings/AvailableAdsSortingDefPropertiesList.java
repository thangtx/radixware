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

package org.radixware.kernel.designer.ads.editors.sortings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.*;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserCommonComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectsListModel;


class AvailableAdsSortingDefPropertiesList extends RadixObjectChooserCommonComponent {

    private AdsSortingDef sorting;

    public AvailableAdsSortingDefPropertiesList(final AdsSortingDef sorting) {
        super();
        this.sorting = sorting;
        list.setCellRenderer(new AbstractItemRenderer(list) {

            @Override
            public String getObjectName(Object object) {
                if (object != null) {
                    if (object instanceof Id) {
                        AdsDefinition prop = sorting.getOwnerClass().getProperties().findById((Id) object, EScope.ALL).get();
                        if (prop != null) {
                            return prop.getName();
                        } else {
                            return "<Unknowkn: " + ((Id) object).toString() + ">";
                        }
                    }
                }
                return "<Not Defined>";
            }

            @Override
            public RadixIcon getObjectIcon(Object object) {
                if (object != null) {
                    if (object instanceof Id) {
                        AdsDefinition prop = sorting.getOwnerClass().getProperties().findById((Id) object, EScope.ALL).get();
                        if (prop != null) {
                            return prop.getIcon();
                        }
                    }
                }
                return RadixObjectIcon.UNKNOWN;
            }

            @Override
            public String getObjectLocation(Object object) {
                return null;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object) {
                return null;
            }
        });
    }
    public static final Set<EValType> FORBIDDEN = Collections.unmodifiableSet(EnumSet.of(PARENT_REF, ARR_REF, ARR_BIN, ARR_BLOB, ARR_BOOL,
            ARR_CHAR, ARR_CLOB, ARR_DATE_TIME,
            ARR_INT, ARR_NUM, ARR_STR));

    @Override
    public void updateContent() {
        List<AdsPropertyDef> props = sorting.getOwnerClass().getProperties().get(EScope.ALL, sorting.getOrder().newAvailablePropertyFilter());
        List<Id> propsIds = new ArrayList<Id>();
        for (AdsPropertyDef p : props) {
            if (!(p instanceof AdsDynamicPropertyDef)
                    && !FORBIDDEN.contains(p.getTypedObject().getType().getTypeId())) {
                propsIds.add(p.getId());
            }
        }

        RadixObjects<AdsSortingDef.OrderBy> order = sorting.getOrder();
        for (AdsSortingDef.OrderBy o : order) {
            propsIds.remove(o.getPropertyId());
        }

        list.setModel(new RadixObjectsListModel(propsIds, sorting));
    }

    @Override
    public void removeAll(Object[] objects) {
        RadixObjectsListModel model = (RadixObjectsListModel) list.getModel();
        for (Object obj : objects) {
            assert (obj instanceof Id);
            model.removeElement((Id) obj);
        }
    }

    @Override
    public void addAllItems(Object[] objects) {
        RadixObjectsListModel model = (RadixObjectsListModel) list.getModel();
        for (Object obj : objects) {
            if (obj != null){
               assert (obj instanceof Id);
               model.addElement((Id) obj);
            }
        }
    }

    @Override
    public JComponent getLabelComponent() {
        return new javax.swing.JLabel("Available Properties");
    }
}

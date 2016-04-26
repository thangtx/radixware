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

package org.radixware.kernel.common.defs.ads.ui;

import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.ui.Action;


public class AdsUIActionDef extends AdsUIItemDef {

    public AdsUIActionDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET), "action");
    }
    private String menu;

    public AdsUIActionDef(Id id, String name) {
        super(id, name);
    }

    public AdsUIActionDef(Action a) {
        this(Id.Factory.loadFrom(a.getId()), a.getName());
        this.properties.loadFrom(a.getPropertyList());
        this.attributes.loadFrom(a.getAttributeList());
        this.menu = a.getMenu();
    }

    public void appendTo(Action a) {
        a.setId(getId().toString());
        a.setPropertyArray(properties.toXml());
        a.setAttributeArray(attributes.toXml());
        a.setMenu(menu);
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ACTION;
    }

    @Override
    public AdsUIItemDef findWidgetById(Id id) {
        return null;
    }
}

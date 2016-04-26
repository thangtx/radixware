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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsPin extends AdsDefinition {

    private Id origId = null;
    private String icon = null;

    protected AdsPin() {
        super(ObjectFactory.createPinId(), "");
    }

    protected AdsPin(Id id) {
        super(id, "");
    }

    protected AdsPin(final AdsPin pin) {
        super(pin.getId(), pin.getName());
        origId = pin.getOrigId();
        icon = pin.getIconName();
    }

    protected AdsPin(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Pins.Pin xPin) {
        super(xPin);
        origId = xPin.getOrigId();
        icon = xPin.getIcon();
    }

    public void setOrigId(Id origId) {
        if (!Utils.equals(this.origId, origId)) {
            this.origId = origId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setOrigId(AdsBaseObject node) {
        setOrigId(node.getId());
    }

    public Id getOrigId() {
        return origId;
    }

    public void setIconName(String icon) {
        if (!Utils.equals(this.icon, icon)) {
            this.icon = icon;
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getIconName() {
        return icon;
    }

    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node.Pins.Pin xPin, ESaveMode saveMode) {
        super.appendTo(xPin, saveMode);
        xPin.setOrigId(origId);
        xPin.setIcon(icon);
    }

    @Override
    public String toString() {
        return getName().isEmpty() ? "<...>" : getName();
    }

    /*
    @Override
    public boolean equals(Object obj) {
    if (super.equals(obj)) {
    return true;
    }
    if (obj == null) {
    return false;
    }
    if (obj instanceof AdsPin) {
    return getId().toString().equals(((AdsPin) obj).getId().toString());
    }
    return false;
    }

    @Override
    public int hashCode() {
    int hash = 7;
    return hash;
    }
     */
    @Override
    public EDefType getDefinitionType() {
        return EDefType.ALGO_PIN;
    }
    private static final String TYPE_TITLE = "Exit";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}

/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.wps.rwt.events;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;

final class ButtonAndModifiers {

    private final int button;
    private final EnumSet<EKeyboardModifier> modifier;

    public ButtonAndModifiers(int button, EnumSet<EKeyboardModifier> modifier) {
        this.button = button;
        this.modifier = modifier==null ? EnumSet.noneOf(EKeyboardModifier.class) : EnumSet.copyOf(modifier);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("button is ").append(button);
        if (modifier != null) {
            sb.append(", modifiers: ");
            for (EKeyboardModifier modif : modifier) {
                sb.append(modif.toString()).append(" ");
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.button;
        hash = 53 * hash + Objects.hashCode(this.modifier);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ButtonAndModifiers other = (ButtonAndModifiers) obj;
        if (this.button != other.button) {
            return false;
        }
        if (!Objects.equals(this.modifier, other.modifier)) {
            return false;
        }
        return true;
    }
        
    public int getButton() {
        return button;
    }
    
    public EnumSet<EKeyboardModifier> getModifiers() {
        return EnumSet.copyOf(modifier);
    }
}

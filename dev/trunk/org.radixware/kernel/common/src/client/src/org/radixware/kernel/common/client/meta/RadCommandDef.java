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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.types.Id;

/**
 * Класс бесконтекстной команды.
 * Является базовым для команды сущности.
 *
 */
public class RadCommandDef extends TitledDefinition {

    private final ECommandNature nature;
    private final Id iconId;
    private final Id startFormId;
    private final boolean visible;
    private final boolean confirmation;
    private final boolean readOnly;

    public RadCommandDef(
            final Id id,
            final String name,
            final Id ownerId,
            final Id titleId,
            final Id iconId,
            final Id startFormId,
            final ECommandNature nature,
            final boolean visible,
            final boolean confirmation) {
        this(id,name,ownerId,titleId,iconId,startFormId,nature,visible,confirmation,false);
    }
    
    public RadCommandDef(
            final Id id,
            final String name,
            final Id ownerId,
            final Id titleId,
            final Id iconId,
            final Id startFormId,
            final ECommandNature nature,
            final boolean visible,
            final boolean confirmation,
            final boolean readOnly) {
        super(id, name, ownerId, titleId);
        this.startFormId = startFormId;
        this.nature = nature;
        this.iconId = iconId;
        this.visible = visible;
        this.confirmation = confirmation;
        this.readOnly = readOnly;
    }    

    public Icon getIcon() {
        return getIcon(iconId);
    }

    public boolean isVisible() {
        return visible;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }

    public ECommandAccessibility getAccessibility() {
        return ECommandAccessibility.ALWAYS;
    }

    public boolean needForConfirmation() {
        return confirmation;
    }

    public ECommandNature getNature() {
        return nature;
    }

    public Id getStartFormId() {
        return startFormId;
    }

    public Command newCommand(Model owner) {
        return new Command(owner, this);
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "contextless command %s");
        return String.format(desc, super.getDescription());
    }
}

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;


public class RadGroupHandlerDef extends Definition {

    private final Id classPresentationId;
    private final RadCommandDef[] commands;
    private final RadPropertyDef[] properties;

    public RadGroupHandlerDef(
            Id id,
            Id classId,
            final RadCommandDef[] commands,
            final RadPropertyDef[] properties) {
        super(id);
        classPresentationId = classId;
        this.commands = commands;
        this.properties = properties;
    }

    public RadGroupHandlerDef(
            Id id,
            Id classId,
            final RadCommandDef[] commands) {
        this(id, classId, commands, null);
    }

    public final Id getClassPesentationId() {
        return classPresentationId;
    }

    public List<RadCommandDef> getGroupCommands() {
        if (commands != null) {
            return Collections.unmodifiableList(Arrays.asList(commands));
        } else {
            return Collections.emptyList();
        }
    }

    public List<RadPropertyDef> getGroupProperties() {
        if (properties != null) {
            return Collections.unmodifiableList(Arrays.asList(properties));
        } else {
            return Collections.emptyList();
        }
    }
}

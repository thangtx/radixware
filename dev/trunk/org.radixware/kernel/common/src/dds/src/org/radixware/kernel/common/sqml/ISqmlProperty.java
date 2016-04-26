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

package org.radixware.kernel.common.sqml;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.Sqml.Tag;

/**
 * Used to generalize DDS columns and ADS properties.
 * Used for SQML check and translation.
 * Can be DdsColumnDef, AdsPropertyDef or any separate class - so, do not cast to RadixObject!
 */
public interface ISqmlProperty extends IDdsDbDefinition {

    public EValType getValType();

    public DdsTableDef findOwnerTable();

    public Definition getDefinition();

    public void check(Tag source, IProblemHandler problemHandler);

    public Sqml getExpression();
}

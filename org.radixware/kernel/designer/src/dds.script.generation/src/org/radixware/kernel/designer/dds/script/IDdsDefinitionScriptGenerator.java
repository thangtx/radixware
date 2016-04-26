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

package org.radixware.kernel.designer.dds.script;

import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.scml.CodePrinter;


public interface IDdsDefinitionScriptGenerator<T extends DdsDefinition> {

    public boolean isModifiedToDrop(T oldDefinition, T newDefinition);

    public void getDropScript(CodePrinter cp, T definition);

    public void getCreateScript(CodePrinter cp, T definition, IScriptGenerationHandler handler);

    public void getAlterScript(CodePrinter cp, T oldDefinition, T newDefinition);
    
    public void getRunRoleScript(CodePrinter printer,T definition);
}
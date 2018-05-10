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

package org.radixware.kernel.designer.uds.tree;

import java.util.List;
import java.util.Set;
import javax.swing.Action;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.uds.userfunc.UdsUserFuncDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.CompileCookie;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewServerSourceAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewServerSourceAction.ViewServerSourceCookie;
import org.radixware.kernel.designer.uds.tree.actions.ExportBodyAction;
import org.radixware.kernel.designer.uds.tree.actions.ImportBodyAction;
import org.radixware.kernel.designer.uds.tree.actions.UdsReloadAction;


public class UserFuncNode extends RadixObjectNode {

    public UserFuncNode(UdsUserFuncDef radixObject) {
        super(radixObject, Children.LEAF);
        addCookie(new UdsReloadAction.Cookie(radixObject));
        Set<JavaSourceSupport.CodeType> types = radixObject.getJavaSourceSupport().getSeparateFileTypes(ERuntimeEnvironmentType.SERVER);
        addCookie(new ViewServerSourceCookie(radixObject, types));
        addCookie(new CompileCookie(radixObject));
        addCookie(new ImportBodyAction.Cookie(radixObject));
        addCookie(new ExportBodyAction.Cookie(radixObject));
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(UdsReloadAction.class));
        actions.add(null);
        actions.add(SystemAction.get(CompileDefinitionAction.class));
        actions.add(SystemAction.get(ViewServerSourceAction.class));
        actions.add(null);
        actions.add(SystemAction.get(ImportBodyAction.class));
        actions.add(SystemAction.get(ExportBodyAction.class));

    }
}

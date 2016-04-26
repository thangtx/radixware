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

package org.radixware.kernel.explorer.editors.jmleditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.progress.TaskWaiter;


public class LookupDefinitions implements Callable<Collection<DefInfo>> {

    private final AdsUserFuncDef uf;
    private final Set<EDefType> templList;
    private final boolean needExplorerClass;

    public LookupDefinitions(final AdsUserFuncDef uf, final Set<EDefType> templList, final boolean needExplorerClass) {
        this.uf = uf;
        this.templList = templList;
        this.needExplorerClass = needExplorerClass;
    }

    @Override
    public Collection<DefInfo> call() throws Exception {
        final Collection<DefInfo> defs = Lookup.listTopLevelDefinitions(uf, templList);
        final List<DefInfo> res = new ArrayList<>();
        res.addAll(defs);
        if (!needExplorerClass) {
            for (DefInfo def : defs) {
                if (def.getEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                    res.remove(def);
                }
            }
        }
        return res;
    }

    public static Collection<DefInfo> getDefinitionList(final IClientEnvironment environment, final AdsUserFuncDef uf, final Set<EDefType> templList, final boolean needExplorerClass) {
        final TaskWaiter waiter = new TaskWaiter(environment);
        waiter.setMessage(Application.translate("JmlEditor", "Opening Dialog..."));
        final Callable<Collection<DefInfo>> task = new LookupDefinitions(uf, templList, needExplorerClass);
        Collection<DefInfo> defList = null;
        try {
            defList = waiter.runAndWait(task);
        } catch (InterruptedException ex) {
            Logger.getLogger(XscmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(XscmlEditor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiter.close();
        }
        return defList;
    }
}
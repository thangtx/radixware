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
package org.radixware.kernel.explorer.editors.jmleditor.completer;

import java.util.concurrent.Callable;
import org.radixware.kernel.common.builder.completion.CompletionProviderFactoryManager;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.EnvironmentVariables;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CompletionProviderFactory;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionRequestor;
import static org.radixware.kernel.explorer.editors.jmleditor.completer.PreloadCompletionHelper.JML_EDITOR_PRELOAD_COMPLETION_RESULT;

class PreloadCompletionTask implements Callable<Void> {

    private final AdsUserFuncDef userFunc;
    private final IClientEnvironment env;
    private final CompleterProcessor proc;

    PreloadCompletionTask(CompleterProcessor proc, AdsUserFuncDef userFunc, IClientEnvironment env) {
        this.userFunc = userFunc;
        this.env = env;
        this.proc = proc;
    }

    @Override
    public Void call() throws Exception {
        final AdsUserFuncDef tempFunc = AdsUserFuncDef.Lookup.createTempFunc(userFunc);
        final Jml jml = tempFunc.getSource();
        final long t0 = System.currentTimeMillis();
        final int[] itemsCount = new int[] {0};
        try {
            CompletionProviderFactory factory = CompletionProviderFactoryManager.getInstance().first(jml);
            org.radixware.kernel.common.scml.ScmlCompletionProvider provider = factory.findCompletionProvider(jml.getItems().first());
            
            if (provider != null && proc != null) {
                provider.complete(1, proc.createRequestorWrapper(new CompletionRequestor() {
                    @Override
                    public void accept(CompletionItem item) {
                        itemsCount[0]++;
                    }

                    @Override
                    public boolean isAll() {
                        return true;
                    }
                }));
            }
        } finally {
            tempFunc.delete();
            final PreloadCompletionHelper.PreloadCompletionInfo info = EnvironmentVariables.get(JML_EDITOR_PRELOAD_COMPLETION_RESULT, env);
            assert info != null;
            info.setPreloadFinished(true);
            env.getTracer().debug(String.format("JmlEditor. Completion peload %d items for %d ms", itemsCount[0], (System.currentTimeMillis() - t0)));
        }
        return null;
    }
}

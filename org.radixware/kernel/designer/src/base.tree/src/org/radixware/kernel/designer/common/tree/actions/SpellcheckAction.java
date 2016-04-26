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

package org.radixware.kernel.designer.common.tree.actions;

import java.util.LinkedList;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.utils.spellchecker.SimpleTokenizer;
import org.radixware.kernel.utils.spellchecker.Spellchecker;


public class SpellcheckAction extends CookieAction {

    private static class ProgressState {

        private boolean isCancelled = false;

        public ProgressState() {
        }

        void cancel() {
            synchronized (this) {
                isCancelled = true;
            }
        }

        boolean isCancelled() {
            synchronized (this) {
                return isCancelled;
            }
        }
    }

    public static class SpellcheckCookie implements Node.Cookie {

        private final RadixObject radixObject;

        public SpellcheckCookie(RadixObject radixObject) {
            this.radixObject = radixObject;
        }

        private void check() {

            final ProgressState ps = new ProgressState();
            final ProgressHandle handle = ProgressHandleFactory.createHandle("Spell check", new Cancellable() {
                @Override
                public boolean cancel() {
                    ps.cancel();
                    return true;
                }
            });
            if (!RadixMutex.getLongProcessLock().tryLock()) {
                DialogUtils.messageError("Build or check action is alredy running");
                return;
            }
            try {

                handle.start();
                final List<RadixObject> locs = new LinkedList<>();
                radixObject.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof ILocalizedDef) {
                            locs.add(radixObject);
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
                int progress = 0;
                handle.switchToDeterminate(locs.size());
                RadixProblemRegistry.getDefault().clear(locs);
                for (RadixObject ro : locs) {
                    if (ps.isCancelled()) {
                        break;
                    }
                    ILocalizedDef locDef = (ILocalizedDef) ro;
                    List<ILocalizedDef.MultilingualStringInfo> infos = new LinkedList<>();
                    locDef.collectUsedMlStringIds(infos);
                    if (ps.isCancelled()) {
                        break;
                    }
                    if (!infos.isEmpty()) {
                        for (ILocalizedDef.MultilingualStringInfo info : infos) {
                            if (ps.isCancelled()) {
                                break;
                            }
                            IMultilingualStringDef stringDef = info.findString();
                            if (stringDef != null && stringDef.isSpellCheckEnabled()) {
                                List<AdsMultilingualStringDef.StringStorage> strings = stringDef.getValues(EScope.LOCAL);
                                for (final AdsMultilingualStringDef.StringStorage string : strings) {
                                    if (ps.isCancelled()) {
                                        break;
                                    }
                                    final EIsoLanguage lang = string.getLanguage();
                                    final String value = string.getValue();

                                    if (value == null) {
                                        continue;
                                    }

                                    final SimpleTokenizer tokenizer = new SimpleTokenizer(value);
                                    String token;
                                    while ((token = tokenizer.nextWord()) != null) {
                                        if (ps.isCancelled()) {
                                            break;
                                        }

                                        final Spellchecker spellchecker = Spellchecker.getInstance(lang, (RadixObject) stringDef);

                                        if (spellchecker.check(token) != Spellchecker.Validity.VALID) {
                                            RadixProblemRegistry.getDefault().accept(RadixProblem.Factory.newSpellingError(
                                                    ro,
                                                    "Unknown " + lang.getName().toLowerCase() + " word \"" + token + "\" at " + info.getContextDescription(),
                                                    token,
                                                    lang));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    handle.progress(progress++);
                }
            } finally {
                RadixMutex.getLongProcessLock().unlock();
                handle.finish();
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{SpellcheckCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 1) {
            SpellcheckCookie c = activatedNodes[0].getCookie(SpellcheckCookie.class);
            if (c != null) {
                c.check();
            }
        }
    }

    @Override
    public String getName() {
        return "Spell Check";
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
}

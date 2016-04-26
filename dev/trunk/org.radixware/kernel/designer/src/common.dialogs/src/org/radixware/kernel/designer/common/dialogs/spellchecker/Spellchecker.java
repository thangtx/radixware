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

package org.radixware.kernel.designer.common.dialogs.spellchecker;

import javax.swing.text.JTextComponent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class Spellchecker {

    public static abstract class SpellcheckControl {

        private Runnable callback;

        public final void update() {
            if (callback != null) {
                callback.run();
            }
        }

        final void setUpdateCallback(Runnable callback) {
            this.callback = callback;
        }

        public abstract boolean isSpellcheckEnabled();

        public abstract JTextComponent getTextComponent();

        public abstract EIsoLanguage getLanguage();

        public abstract RadixObject getContext();
    }

    private static class DefaultSpellcheckControl extends SpellcheckControl {

        final JTextComponent textComponent;
        final EIsoLanguage lang;
        final RadixObject context;

        public DefaultSpellcheckControl(JTextComponent c, EIsoLanguage lang, RadixObject context) {
            this.textComponent = c;
            this.lang = lang;
            this.context = context;
        }

        @Override
        public boolean isSpellcheckEnabled() {
            if (context instanceof AdsMultilingualStringDef) {
                return ((AdsMultilingualStringDef) context).isSpellCheckEnabled();
            }
            return true;
        }

        @Override
        public JTextComponent getTextComponent() {
            return textComponent;
        }

        @Override
        public EIsoLanguage getLanguage() {
            return lang;
        }

        @Override
        public RadixObject getContext() {
            return context;
        }
    }

    public static SpellcheckControl register(JTextComponent textComponent, EIsoLanguage language, RadixObject context) {
        SpellcheckControl spellcheckControl = new DefaultSpellcheckControl(textComponent, language, context);

        register(spellcheckControl);

        return spellcheckControl;
    }

    public static void register(SpellcheckControl control) {
        TextComponentAdapter.attach(control);
    }
}

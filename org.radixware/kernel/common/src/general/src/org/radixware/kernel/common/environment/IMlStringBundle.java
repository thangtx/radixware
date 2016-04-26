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

package org.radixware.kernel.common.environment;

import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import static org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue;
import org.radixware.kernel.common.meta.ILanguageContext;
import org.radixware.kernel.common.types.Id;

/**
 * Multilingual String Bundle interface
 *
 */
public interface IMlStringBundle {

    public interface IMultilingualString {

        /**
         * @return the kind
         */
        public ELocalizedStringKind getKind();

        /**
         * @return the eventSeverity
         */
        public EEventSeverity getEventSeverity();

        /**
         * @return the eventSource
         */
        public String getEventSource();

        public String getValue(EIsoLanguage language);

        public boolean isChecked(EIsoLanguage language);
    }

    public static final class Lookup {
        /*
         * by BAO public static String getValue(java.lang.Object context, Id
         * bundleId, Id stringId) { if (context == null) { return null; } if
         * (context.getClass().getClassLoader() instanceof IRadixClassLoader) {
         * IRadixClassLoader cl = (IRadixClassLoader)
         * context.getClass().getClassLoader(); IMlStringBundle bundle =
         * cl.getEnvironment().getDefManager().getStringBundleById(bundleId); if
         * (bundle == null) { return ""; } else { return bundle.get(stringId,
         * cl.getEnvironment().getClientLanguage()); } } else { return ""; }
         }
         */

        private static final boolean markUncheckedStrings;

        static {
            markUncheckedStrings = "true".equals(System.getProperty("org.radixware.kernel.client.markUncheckedStrings", "false"));
        }

        public static String getValue(java.lang.Class context, Id bundleId, Id stringId) {
            return getValue(context, bundleId, stringId, null);
        }

        public static String getValue(java.lang.Class context, Id bundleId, Id stringId, EIsoLanguage lang) {
            if (context == null) {
                return null;
            }

            if (context.getClassLoader() instanceof IRadixClassLoader) {
                IRadixClassLoader cl = (IRadixClassLoader) context.getClassLoader();
                IMlStringBundle bundle = cl.getEnvironment().getDefManager().getStringBundleById(bundleId);
                if (bundle == null) {
                    return "";
                } else {
                    if (lang == null) {
                        final Thread thread = Thread.currentThread();
                        if (thread instanceof ILanguageContext) {
                            lang = ((ILanguageContext) thread).getLanguage();
                        }
                        if (lang == null) {
                            lang = cl.getEnvironment().getClientLanguage();
                        }
                        if (lang == null) {
                            lang = EIsoLanguage.ENGLISH;
                        }
                    }
                    String result = bundle.get(stringId, lang);
                    if (markUncheckedStrings) {
                        IMlStringBundle.IMultilingualString strings = bundle.getStringSet(stringId);
                        if (strings != null && !strings.isChecked(lang)) {
                            return "!!!UNCHECKED: " + result;
                        }
                    }
                    return result;

                }
            } else {
                return "";
            }
        }

        public static IMlStringBundle.IMultilingualString getStringSet(java.lang.Class context, Id bundleId, Id stringId) {
            if (context == null) {
                return null;
            }
            if (context.getClassLoader() instanceof IRadixClassLoader) {
                IRadixClassLoader cl = (IRadixClassLoader) context.getClassLoader();
                IMlStringBundle bundle = cl.getEnvironment().getDefManager().getStringBundleById(bundleId);
                if (bundle == null) {
                    return null;
                } else {
                    return bundle.getStringSet(stringId);
                }
            } else {
                return null;
            }
        }
    }

    public String get(Id stringId, EIsoLanguage language);

    public IMlStringBundle.IMultilingualString getStringSet(Id stringId);
}

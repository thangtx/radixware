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
package org.radixware.kernel.radixdoc.html;

import java.util.Map;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.IReferenceResolver;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.LocalizedString;

public final class TransformLib {

    private TransformLib() {
    }

    private static String joinPath(String... parts) {
        final StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (final String part : parts) {
            if (first) {
                first = false;
            } else {
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) != '/') {
                    sb.append('/');
                }
            }
            sb.append(part.trim());
        }

        return sb.toString();
    }

    public static String parsePath(String path, EIsoLanguage language, HtmlRadixdocGenerator generator) {
        if (path == null) {
            return "";
        }

        if (path.length() > 0 && path.charAt(0) == '#') {
            return path;
        }

        final int index = path.indexOf(IReferenceResolver.PATH_DELIMITER);
        final String rootPath = index >= 0 ? path.substring(0, index) : path;
        final String pref = generator.resolvePath(rootPath);

        if (pref == null) {
            return "";
        }

        if (path.lastIndexOf(IReferenceResolver.PATH_DELIMITER) >= 0) {
            return joinPath(pref, language.getValue(), path.replace(IReferenceResolver.PATH_DELIMITER, ".html#"));
        }
        return joinPath(pref, language.getValue(), path + ".html");
    }

    public static String loadString(String entry, String id, HtmlRadixdocGenerator generator, EIsoLanguage language) {
        final String[] ids = id.split(" ");
        String bundleId;
        String strId;
        if (ids.length < 2) {
            return id;
        } else if (ids.length == 2) {
            bundleId = ids[0];
            strId = ids[1];
        } else if (ids.length == 3) {
            final String pathToModule = ids[0];
            entry = pathToModule;
            bundleId = ids[1];
            strId = ids[2];
        } else {
            return id;
        }

        Map<Id, LocalizedString> strings = generator.getFileProvider().getLocalizedBundleStrings(entry, bundleId, language);
        String strVal = loadString(strings, strId, language);

        if (strVal.isEmpty() && language != EIsoLanguage.ENGLISH) {
            strVal = loadStringFromLocalizingLayer(generator.getFileProvider(), entry, bundleId, strId, language);
            
            if (strVal.isEmpty()) {
                strings = generator.getFileProvider().getLocalizedBundleStrings(entry, bundleId, EIsoLanguage.ENGLISH);
                strVal = loadString(strings, strId, EIsoLanguage.ENGLISH);
            }                        
        }
        
        return strVal;        
    }

    private static String loadString(Map<Id, LocalizedString> stringList, String stringId, EIsoLanguage language) {
        if (stringList == null) {
            return "";
        }

        Id id = Id.Factory.loadFrom(stringId);
        if (!stringList.keySet().contains(id)) {
            return "";
        }

        final LocalizedString localizedString = stringList.get(id);
        if (localizedString.getValueList() != null) {
            for (final LocalizedString.Value value : localizedString.getValueList()) {
                if (value.getLanguage() == language) {
                    if (!Utils.emptyOrNull(value.getStringValue())) {
                        return value.getStringValue();
                    }
                }
            }
        }

        return "";
    }

    public static String loadStringFromLocalizingLayer(FileProvider provider, String entry, String bundleId, String stringId, EIsoLanguage language) {        
        String[] entryParts = entry.replace("\\", "/").split("/");        

        if (entryParts.length > 0) {
            String uri = entryParts[0];
            FileProvider.LayerEntry entryLayer = provider.getLayer(uri);
            if (entryLayer != null && entryLayer.hasLocalizingLayer(language)) {
                entryParts[0] = entryLayer.getLocalizingLayer(language);
                String newEntry = constructEntry(entryParts);
                
                Map<Id, LocalizedString> strings = provider.getLocalizedBundleStrings(newEntry, bundleId, language);
                return loadString(strings, stringId, language);
            }
        }
        
        return "";
    }

    public static String constructEntry(String[] entryParts) {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;        
        for (String part : entryParts) {
            if (!isFirst) {
                result.append("/");
            } else {
                isFirst = false;
            }
            
            result.append(part);
        }
        
        return result.toString();
    }

    public static boolean addFile(String source, String fileName, String outName, HtmlRadixdocGenerator generator, EIsoLanguage language) {
        final EFileSource src = EFileSource.getByName(source);
        generator.addCopyFileEntry(src, fileName, language.getValue() + "/" + outName);
        return true;
    }
}

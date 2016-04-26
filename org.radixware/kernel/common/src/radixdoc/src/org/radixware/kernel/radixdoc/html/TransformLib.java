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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.IReferenceResolver;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
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
        String bunbleId;
        String strId;
        if (ids.length < 2) {
            return id;
        } else if (ids.length == 2) {
            bunbleId = ids[0];
            strId = ids[1];
        } else if (ids.length == 3) {
            final String pathToModule = ids[0];
            entry = pathToModule;
            bunbleId = ids[1];
            strId = ids[2];
        } else {
            return id;
        }

        final AdsDefinitionDocument definitionDocument;
        try (final InputStream inputStream = generator.getFileProvider().getLocalizedBandle(entry, bunbleId, language)) {
            if (inputStream != null) {
                definitionDocument = AdsDefinitionDocument.Factory.parse(inputStream);
            } else {
                return "";
            }
        } catch (XmlException | IOException ex) {
            Logger.getLogger(HtmlRadixdocGenerator.class.getName()).log(Level.WARNING, bunbleId + "." + strId, ex);
            return "";
        }

        final AdsDefinitionElementType element = definitionDocument.getAdsDefinition();
        if (element.isSetAdsLocalizingBundleDefinition()) {
            for (final LocalizedString localizedString : element.getAdsLocalizingBundleDefinition().getStringList()) {
                if (Objects.equals(localizedString.getId().toString(), strId)) {
                    for (final LocalizedString.Value value : localizedString.getValueList()) {
                        if (value.getLanguage() == language) {
                            return value.getStringValue();
                        }
                    }
                }
            }
        }
        return "";
    }

    public static boolean addFile(String source, String fileName, String outName, HtmlRadixdocGenerator generator, EIsoLanguage language) {
        final EFileSource src = EFileSource.getByName(source);
        generator.addCopyFileEntry(src, fileName, language.getValue() + "/" + outName);
        return true;
    }
}

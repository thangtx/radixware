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
package org.radixware.kernel.radixdoc.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.radixdoc.ditabridge.PackedZipExecutor;
import org.radixware.kernel.radixdoc.ditagenerator.LocalizationMap;
import org.radixware.kernel.radixdoc.enums.EDecorationProperties;
import org.radixware.kernel.radixdoc.enums.ELocalizationMapKeys;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.kernel.radixdoc.xmlexport.EExportResult;
import org.radixware.schemas.adsdef.LocalizedString;
import org.radixware.schemas.radixdoc.AbstractDefDocItem;
import org.radixware.schemas.radixdoc.AdsXmlSchemeDefDocItem;

public class GeneratorUtils {

    public static String getLocalizedStringValueById(RadixdocGenerationContext context, Id defId, Id stringId, EIsoLanguage lang) {
        if (defId == null) {
            return "";
        }

        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(defId);
        if (docInfo == null) {
            return "";
        }

        Id bundleId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE.getValue() + defId.toString());

        String engValue = "";
        LocalizedString string = context.getLocalizedStringByDefId(defId, bundleId, stringId, lang);
        if (string != null && string.getValueList() != null) {
            for (LocalizedString.Value stringVal : string.getValueList()) {
                if (stringVal.getLanguage() == lang) {
                    return stringVal.getStringValue() == null ? "" : stringVal.getStringValue();
                }
                if (stringVal.getLanguage() == EIsoLanguage.ENGLISH) {
                    engValue = stringVal.getStringValue() == null ? "" : stringVal.getStringValue();
                }
            }
        }
        return engValue;
    }

    public static String getLocalizedStringValueByModulePath(RadixdocGenerationContext context, String modulePath, Id bundleId, Id stringId, EIsoLanguage lang) {
        String formatedPath;
        if (modulePath.contains("\\")) {
            formatedPath = modulePath.replace("\\", "/");
        } else {
            formatedPath = modulePath;
        }

        LocalizedString string = context.getLocalizedStringByModulePath(formatedPath, bundleId, stringId, lang);
        if (string != null && string.getValueList() != null) {
            for (LocalizedString.Value stringVal : string.getValueList()) {
                if (stringVal.getLanguage() == lang) {
                    return stringVal.getStringValue() == null ? "" : stringVal.getStringValue();
                }
            }
        }
        if (lang != EIsoLanguage.ENGLISH) {
            return GeneratorUtils.getLocalizedStringValueByModulePath(context, modulePath, bundleId, stringId, EIsoLanguage.ENGLISH);
        } else {
            return "";
        }
    }

    public static String getBaseVersion(String fullVersion) {
        if (fullVersion == null) {
            return "null";
        }

        Pattern pattern = Pattern.compile("\\d*\\.\\d*\\.\\d*");
        Matcher matcher = pattern.matcher(fullVersion);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return fullVersion;
        }
    }

    public static boolean isDefinitionHasTitle(RadixdocGenerationContext context, Id definitionId) {
        return !getMapName(EIsoLanguage.ENGLISH, context, definitionId).equals(getDefaultMapName(context, definitionId));
    }

    public static boolean isDefinitionHasZIPTitle(RadixdocGenerationContext context, Id definitionId) {
        return !getZIPName(EIsoLanguage.ENGLISH, context, definitionId).equals(getDefaultMapName(context, definitionId));
    }

    public static String getMapName(EIsoLanguage lang, RadixdocGenerationContext context, Id definitionId) {
        return getMapName(lang, context, definitionId, null);
    }

    public static String getMapName(EIsoLanguage lang, RadixdocGenerationContext context, Id definitionId, String layerVersionFull) {
        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(definitionId);
        if (docInfo == null) {
            return GeneratorUtils.getDefaultMapName(context, definitionId, layerVersionFull);
        } else {
            AbstractDefDocItem docItem = docInfo.getDocItem();
            if (docItem instanceof AdsXmlSchemeDefDocItem) {
                AdsXmlSchemeDefDocItem xmlDef = (AdsXmlSchemeDefDocItem) docItem;
                if (xmlDef.getDocumentationTitleId() != null) {
                    String result = getLocalizedStringValueById(context, xmlDef.getId(), xmlDef.getDocumentationTitleId(), lang);
                    if (!Utils.emptyOrNull(result)) {
                        return result;
                    }
                }
            }
        }
        return GeneratorUtils.getDefaultMapName(context, definitionId, layerVersionFull);
    }

    public static String getZIPName(EIsoLanguage lang, RadixdocGenerationContext context, Id definitionId) {
        return getZIPName(lang, context, definitionId, null);
    }

    public static String getZIPName(EIsoLanguage lang, RadixdocGenerationContext context, Id definitionId, String layerVersionFull) {
        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(definitionId);
        if (docInfo == null) {
            return GeneratorUtils.getDefaultMapName(context, definitionId, layerVersionFull);
        } else {
            AbstractDefDocItem docItem = docInfo.getDocItem();
            if (docItem instanceof AdsXmlSchemeDefDocItem) {
                AdsXmlSchemeDefDocItem xmlDef = (AdsXmlSchemeDefDocItem) docItem;
                if (xmlDef.getSchemaZIPTitleId() != null) {
                    String result = getLocalizedStringValueById(context, xmlDef.getId(), xmlDef.getSchemaZIPTitleId(), lang);
                    if (!Utils.emptyOrNull(result)) {
                        return result;
                    }
                }
            }
        }
        return GeneratorUtils.getDefaultMapName(context, definitionId, layerVersionFull);
    }

    public static String getDefaultMapName(RadixdocGenerationContext context, Id definitionId, String layerVersionFull) {
        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(definitionId);
        if (docInfo == null) {
            return "";
        } else {
            String layerVersionStr = Utils.emptyOrNull(layerVersionFull) ? "-" : layerVersionFull;
            String versionString = "v" + RadixdocConventions.RADIXDOC_VERSION_DELIMITER + getBaseVersion(layerVersionStr);
            AbstractDefDocItem docItem = docInfo.getDocItem();

            return docItem.getName() + " (" + versionString + ", " + docInfo.getModuleName() + ", " + docInfo.getLayerUri() + ", " + layerVersionStr + ")";
        }
    }

    public static String getDefaultMapName(RadixdocGenerationContext context, Id definitionId) {
        return GeneratorUtils.getDefaultMapName(context, definitionId, null);
    }

    public static String getLayerVersion(RadixdocGenerationContext context, Id definitionId) {
        String result = "-";

        DefinitionDocInfo docInfo = context.getDefinitionDocInfo(definitionId);
        if (docInfo != null) {
            if (docInfo.getLayerVersion() != null && !docInfo.getLayerVersion().isEmpty()) {
                return GeneratorUtils.getBaseVersion(docInfo.getLayerVersion());
            }

            try (ZipInputStream decorStream = context.getDecorationsStream(docInfo.getLayerUri())) {
                ZipEntry entry = decorStream.getNextEntry();
                while (entry != null) {
                    if (PackedZipExecutor.DECOR_PROPS.equals(entry.getName())) {
                        Properties decor = new Properties();
                        decor.load(decorStream);
                        result = decor.getProperty(EDecorationProperties.LAYER_VERSION_FULL, "-");
                        if (result.isEmpty()) {
                            result = "-";
                        }
                    }
                    entry = decorStream.getNextEntry();
                }

            } catch (IOException ex) {
                Logger.getLogger(GeneratorUtils.class.getName()).log(Level.SEVERE, null, ex);
                if (context.getDocLogger() != null) {
                    context.getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
                }
            }
        }
        return getBaseVersion(result);
    }

    public static ICancellable getMockCancellable() {
        return new ICancellable() {

            @Override
            public boolean cancel() {
                return false;
            }

            @Override
            public boolean wasCancelled() {
                return false;
            }
        };
    }

    public static Map<EExportResult, String> getDefaultResultMessages() {
        Map<EExportResult, String> result = new HashMap<>();
        result.put(EExportResult.FAIL, "EXPORT FAILED!");
        result.put(EExportResult.SUCCESS, "EXPORT COMPLETE!");
        result.put(EExportResult.CANCEL, "EXPORT CANCELLED!");

        return result;
    }

    public static String getDocPartLocalization(final RadixdocGenerationContext context, final ELocalizationMapKeys key, final EIsoLanguage lang) {
        return getLocalizedStringValueByModulePath(context,
                RadixdocConventions.LOCALIZING_MODULE_PATH,
                RadixdocConventions.LOCALIZING_BUNDLE_ID,
                LocalizationMap.get(key), lang);
    }

    public static List<String> getAllowedVersionsList(RadixdocGenerationContext context, DefinitionDocInfo docInfo) {
        if (docInfo == null) {
            return null; //any version allowed
        }

        List<String> result = new ArrayList<>();
        String version = getLayerVersion(context, docInfo.getDocItem().getId());
        if (Utils.emptyOrNull(version) || version.equals("-") || version.equals("null")) {
            return null; //any version allowed
        }

        result.add(version);

        String versionsMappingStr = "";
        String changeLogHistoryDepth = "";

        try (ZipInputStream decorStream = context.getDecorationsStream(docInfo.getLayerUri())) {
            if (decorStream == null) {
                return null;
            }
            
            ZipEntry entry = decorStream.getNextEntry();
            while (entry != null) {
                if (PackedZipExecutor.DECOR_PROPS.equals(entry.getName())) {
                    Properties decor = new Properties();
                    decor.load(decorStream);
                    changeLogHistoryDepth = decor.getProperty(EDecorationProperties.CHANGE_LOG_HISTORY_DEPTH, "");
                    versionsMappingStr = decor.getProperty(EDecorationProperties.VERSIONS_MAPPING, "");
                }
                entry = decorStream.getNextEntry();
            }
        } catch (IOException ex) {
            Logger.getLogger(GeneratorUtils.class.getName()).log(Level.SEVERE, null, ex);
            if (context.getDocLogger() != null) {
                context.getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
        }

        if (Utils.emptyOrNull(changeLogHistoryDepth)) {
            return null; //any version allowed
        }

        Map<String, String> versionsMapping = parseVersionsMappingStr(versionsMappingStr, context.getDocLogger());
        try {
            int lastDot = version.contains(".") ? version.lastIndexOf(".") : version.length() - 1;
            String versionBody = version.substring(0, lastDot + 1);
            String versionTail = version.substring(lastDot + 1);

            int versionsCount = Integer.valueOf(changeLogHistoryDepth);
            int intVersionTail = versionTail.isEmpty() ? Integer.valueOf(versionBody) : Integer.valueOf(versionTail);

            for (int i = 0; i < versionsCount; i++) {
                intVersionTail--;
                
                String prevVersion = versionBody + intVersionTail;
                if (versionsMapping.containsKey(prevVersion)) {
                    String tmpVersion = versionsMapping.get(prevVersion);                    
                    lastDot = tmpVersion.contains(".") ? tmpVersion.lastIndexOf(".") : tmpVersion.length() - 1;
                    versionBody = tmpVersion.substring(0, lastDot + 1);
                    versionTail = tmpVersion.substring(lastDot + 1); 
                    intVersionTail = versionTail.isEmpty() ? Integer.valueOf(versionBody) : Integer.valueOf(versionTail);
                    
                    result.add(tmpVersion);
                } else if (intVersionTail >= 0) {
                    result.add(prevVersion);
                } else {
                    if (context.getDocLogger() != null) {
                        context.getDocLogger().put(EEventSeverity.WARNING,
                                "Can't calculate previous version for v." + versionBody + "0. Change Log can be incomplete. Please make sure that this version is added to the 'versionsMapping' property in the '" + PackedZipExecutor.DECOR_PROPS + "' file");
                    }
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(GeneratorUtils.class.getName()).log(Level.SEVERE, null, ex);
            if (context.getDocLogger() != null) {
                context.getDocLogger().put(EEventSeverity.ERROR, ExceptionTextFormatter.throwableToString(ex));
            }
        }

        return result;
    }

    private static Map<String, String> parseVersionsMappingStr(String versionsMappingStr, IDocLogger logger) {
        Map<String, String> result = new HashMap<>();

        if (Utils.emptyOrNull(versionsMappingStr)) {
            return result;
        }

        String[] entries = versionsMappingStr.split(";");        
        for (String entry : entries) {
            String[] entryParts = entry.split(">");
            if (entryParts.length != 2) {
                if (logger != null) {
                    logger.put(EEventSeverity.WARNING, "Incorrect entry '" + entry + "' found in 'versionsMapping' property in the '" + PackedZipExecutor.DECOR_PROPS + "' file");
                }                
                continue;
            }
            
            result.put(entryParts[0], entryParts[1]);
        }

        return result;
    }
}

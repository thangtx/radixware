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
package org.radixware.kernel.common.builder.check.common;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.IAccessible;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.doc.IRadixDocObject;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.services.IRadixObjectService;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.XPathUtils;
import org.w3c.dom.Element;

/**
 * Radix Object Checker
 *
 */
public abstract class RadixObjectChecker<T extends RadixObject> implements IRadixObjectService {

    public static class DocumentationStatusReport {

        private static class Summary {

            public int undocumentedDefsCount;
            public int totalDefsCount;
            public Map<String, Integer> undocumentedByType = new HashMap<>();
            public Map<String, Integer> totalByType = new HashMap<>();
            public Map<EIsoLanguage, Integer> undocumentedByLang = new HashMap<>();
            public Map<EIsoLanguage, Long> lang2DescriptionLength = new HashMap<>();
        }

        private final Map<Layer, Map<Module, Summary>> module2summary = new HashMap<>();
        private final Map<Layer, Map<Module, Summary>> module2GroupSummary = new HashMap<>();
        private final Set<RadixObject> definitions = new HashSet<>();
        private final Set<EIsoLanguage> languages = new HashSet<>();
        private final CheckOptions checkOptions;

        public DocumentationStatusReport(CheckOptions checkOptions) {
            this.checkOptions = checkOptions;
        }

        public void accept(RadixObject radixObject, IProblemHandler problemHandler) {
            if (!radixObject.needsDocumentation()) {
                return;
            }
            Definition def = radixObject.getDefinition();
            if (def != null) {
                if (def.isPublished()) {
                    languages.addAll(def.getLayer().getLanguages());
                    if (radixObject instanceof IRadixDocObject && def instanceof ILocalizedDef) {
                        register(radixObject);
                        Layer layer = radixObject.getLayer();
                        IRadixDocObject desc = (IRadixDocObject) radixObject;
                        Id id = desc.getDescriptionId();
                        if (id == null) {
                            noDescription(radixObject, problemHandler);
                            if (layer != null) {
                                for (EIsoLanguage l : layer.getLanguages()) {
                                    registerUndocumentedLang(radixObject, l);
                                }
                            }
                        } else {
                            IMultilingualStringDef stringDef = ((ILocalizedDef) def.getDescriptionLocation()).findLocalizedString(id);
                            if (stringDef != null) {
                                if (layer != null) {
                                    boolean allStringsAreEmpty = true;
                                    for (EIsoLanguage l : layer.getLanguages()) {
                                        String text = stringDef.getValue(l);
                                        if (text == null || text.trim().isEmpty()) {
                                            registerUndocumentedLang(radixObject, l);
                                        } else {
                                            allStringsAreEmpty = false;
                                            calcTotalLangDescriptionLength(radixObject, Long.valueOf(text.length()), l);
                                        }
                                    }
                                    if (allStringsAreEmpty) {
                                        noDescription(radixObject, problemHandler);
                                    }
                                }
                            } else {
                                noDescription(radixObject, problemHandler);
                            }
                        }
                    } else if (radixObject instanceof IDescribable) {
                        register(radixObject);
                        IDescribable desc = (IDescribable) radixObject;
                        String text = desc.getDescription();
                        if (text == null || text.trim().isEmpty()) {
                            //error
                            noDescription(radixObject, problemHandler);
                            for (EIsoLanguage l : radixObject.getLayer().getLanguages()) {
                                registerUndocumentedLang(radixObject, l);
                            }
                        } else {
                            if (radixObject.getLayer().getLanguages().contains(EIsoLanguage.RUSSIAN)) {
                                calcTotalLangDescriptionLength(radixObject, Long.valueOf(text.length()), EIsoLanguage.RUSSIAN);
                            } else {
                                calcTotalLangDescriptionLength(radixObject, Long.valueOf(text.length()), radixObject.getLayer().getLanguages().get(0));
                            }
                        }
                    }

                    if (def.getDocGroup() == EDocGroup.XSD && (radixObject.getDefinition() instanceof AdsXmlSchemeDef)) {
                        AdsXmlSchemeDef schema = (AdsXmlSchemeDef) radixObject.getDefinition();
                        List<String> xsdItemsDocList = schema.getDocumentedNodes();
                        registerXmlItems(radixObject);
                        for (String nodePath : xsdItemsDocList) {
                            if (isDocumentedXmlItem(nodePath)) {
                                IMultilingualStringDef mlStr = schema.findLocalizedString(schema.getXmlItemDocEntry(nodePath).getId());
                                for (EIsoLanguage l : radixObject.getLayer().getLanguages()) {
                                    String text = mlStr == null ? null : mlStr.getValue(l);
                                    if (text != null && !text.trim().isEmpty()) {
                                        calcTotalLangDescriptionLength(radixObject, Long.valueOf(text.length()), l);
                                    } else {
                                        registerUndocumentedLang(radixObject, l);
                                    }
                                }
                            }
                        }
                        registerUndocumentedXmlItem(radixObject);
                    }

                }
            }
        }

        private void noDescription(RadixObject context, IProblemHandler problemHandler) {
            if (shouldSkip(context)) {
                return;
            }
            StringBuilder message = new StringBuilder();
            message.append("Undocumented API element: ");
            if (!context.getName().isEmpty()) {
                message.append(context.getName()).append(" - ");
            }
            message.append(context.getTypeTitle().toLowerCase());
            problemHandler.accept(RadixProblem.Factory.newWarning(context, message.toString()));
            registerUndocumented(context);
        }

        private Summary getSummary(RadixObject context) {
            Layer layer = context.getLayer();
            if (layer == null) {
                return null;
            }
            Map<Module, Summary> map = module2summary.get(layer);
            if (map == null) {
                map = new HashMap<>();
                module2summary.put(layer, map);
            }
            Module module = context.getModule();

            Summary summary = map.get(module);
            if (summary == null) {
                summary = new Summary();
                map.put(module, summary);
            }
            return summary;
        }

        private Summary getGroupSummary(RadixObject context) {
            Layer layer = context.getLayer();
            if (layer == null) {
                return null;
            }
            Map<Module, Summary> map = module2GroupSummary.get(layer);
            if (map == null) {
                map = new HashMap<>();
                module2GroupSummary.put(layer, map);
            }
            Module module = context.getModule();

            Summary summary = map.get(module);
            if (summary == null) {
                summary = new Summary();
                map.put(module, summary);
            }
            return summary;
        }

        private void register(RadixObject radixObject) {
            if (shouldSkip(radixObject)) {
                return;
            }
            Summary summary = getSummary(radixObject);
            if (summary != null) {
                summary.totalDefsCount++;
                Integer count = summary.totalByType.get(radixObject.getTypeTitle());
                if (count != null) {
                    count++;
                } else {
                    count = 1;
                }
                summary.totalByType.put(radixObject.getTypeTitle(), count);
            }

            Summary groupSummary = getGroupSummary(radixObject);
            if (groupSummary != null) {
                EDocGroup objDocGroup;
                if (radixObject instanceof IRadixDocObject) {
                    objDocGroup = ((IRadixDocObject) radixObject).getDocGroup();
                } else {
                    if (radixObject.getDefinition() != null) {
                        objDocGroup = radixObject.getDefinition().getDocGroup();
                    } else {
                        return;
                    }
                }
                if (objDocGroup != EDocGroup.NONE && objDocGroup != EDocGroup.ENUM_ITEM) {
                    groupSummary.totalDefsCount++;
                    Integer count = groupSummary.totalByType.get(objDocGroup.getValue());
                    if (count != null) {
                        count++;
                    } else {
                        count = 1;
                    }
                    groupSummary.totalByType.put(objDocGroup.getValue(), count);
                    definitions.add(radixObject);
                }
            }
        }

        private void registerUndocumented(RadixObject radixObject) {
            if (shouldSkip(radixObject)) {
                return;
            }
            Summary summary = getSummary(radixObject);
            if (summary != null) {
                summary.undocumentedDefsCount++;
                Integer count = summary.undocumentedByType.get(radixObject.getTypeTitle());
                if (count != null) {
                    count++;
                } else {
                    count = 1;
                }
                summary.undocumentedByType.put(radixObject.getTypeTitle(), count);
            }

            Summary groupSummary = getGroupSummary(radixObject);
            if (groupSummary != null) {
                EDocGroup objDocGroup;
                if (radixObject instanceof IRadixDocObject) {
                    objDocGroup = ((IRadixDocObject) radixObject).getDocGroup();
                } else {
                    if (radixObject.getDefinition() != null) {
                        objDocGroup = radixObject.getDefinition().getDocGroup();
                    } else {
                        return;
                    }
                }
                if (objDocGroup != EDocGroup.NONE && objDocGroup != EDocGroup.ENUM_ITEM) {
                    groupSummary.undocumentedDefsCount++;
                    Integer count = groupSummary.undocumentedByType.get(objDocGroup.getValue());
                    if (count != null) {
                        count++;
                    } else {
                        count = 1;
                    }
                    groupSummary.undocumentedByType.put(objDocGroup.getValue(), count);
                }
            }
        }

        private void calcTotalLangDescriptionLength(RadixObject radixObject, Long descriptionLength, EIsoLanguage lang) {
            Summary summary = getSummary(radixObject);
            if (summary != null) {
                Long count = summary.lang2DescriptionLength.get(lang);
                if (count != null) {
                    count += descriptionLength;
                } else {
                    count = descriptionLength;
                }
                summary.lang2DescriptionLength.put(lang, count);
            }

            Summary groupSummary = getGroupSummary(radixObject);
            if (groupSummary != null) {
                EDocGroup objDocGroup;
                if (radixObject instanceof IRadixDocObject) {
                    objDocGroup = ((IRadixDocObject) radixObject).getDocGroup();
                } else {
                    if (radixObject.getDefinition() != null) {
                        objDocGroup = radixObject.getDefinition().getDocGroup();
                    } else {
                        return;
                    }
                }
                if (objDocGroup != EDocGroup.NONE && objDocGroup != EDocGroup.ENUM_ITEM) {
                    Long count = groupSummary.lang2DescriptionLength.get(lang);
                    if (count != null) {
                        count += descriptionLength;
                    } else {
                        count = descriptionLength;
                    }
                    groupSummary.lang2DescriptionLength.put(lang, count);
                }
            }
        }

        private int getDefLangDescriptionLength(RadixObject obj, EIsoLanguage lang) {
            int result = 0;
            if (obj instanceof ILocalizedDescribable) {
                ILocalizedDescribable def = (ILocalizedDescribable) obj;
                if (def.getDescriptionId() != null) {
                    IMultilingualStringDef stringDef = def.getDescriptionLocation().findLocalizedString(def.getDescriptionId());
                    if (stringDef != null) {
                        String text = stringDef.getValue(lang);
                        if (!(text == null || text.trim().isEmpty())) {
                            result = text.length();
                        }
                    }
                }
            } else {
                String text = obj.getDescription();
                if (!(text == null || text.trim().isEmpty())) {
                    if (obj.getLayer().getLanguages().contains(EIsoLanguage.RUSSIAN)) {
                        return lang != EIsoLanguage.RUSSIAN ? 0 : text.length();
                    } else {
                        return lang != obj.getLayer().getLanguages().get(0) ? 0 : text.length();
                    }
                }
            }
            return result;
        }

        private int getXsdItemDescriptionLength(AdsXmlSchemeDef owner, String xPath, EIsoLanguage lang) {
            Id mlStringId = owner.getXmlItemDocEntry(xPath) == null ? null : owner.getXmlItemDocEntry(xPath).getId();
            if (mlStringId != null) {
                AdsLocalizingBundleDef bundle = owner.findLocalizingBundle();
                return bundle == null || bundle.getLocalizedStringValue(lang, mlStringId) == null ? 0 : bundle.getLocalizedStringValue(lang, mlStringId).length();
            } else {
                return 0;
            }
        }

        private void registerUndocumentedLang(RadixObject radixObject, EIsoLanguage lang) {
            if (shouldSkip(radixObject)) {
                return;
            }
            Summary summary = getSummary(radixObject);
            if (summary != null) {
                if (lang != null) {
                    Integer count = summary.undocumentedByLang.get(lang);
                    if (count == null) {
                        count = 1;
                    } else {
                        count++;
                    }
                    summary.undocumentedByLang.put(lang, count);
                }
            }

            Summary groupSummary = getGroupSummary(radixObject);
            if (groupSummary != null) {
                EDocGroup objDocGroup;
                if (radixObject instanceof IRadixDocObject) {
                    objDocGroup = ((IRadixDocObject) radixObject).getDocGroup();
                } else {
                    if (radixObject.getDefinition() != null) {
                        objDocGroup = radixObject.getDefinition().getDocGroup();
                    } else {
                        return;
                    }
                }
                if (objDocGroup != EDocGroup.NONE && objDocGroup != EDocGroup.ENUM_ITEM) {
                    if (lang != null) {
                        Integer count = groupSummary.undocumentedByLang.get(lang);
                        if (count == null) {
                            count = 1;
                        } else {
                            count++;
                        }
                        groupSummary.undocumentedByLang.put(lang, count);
                    }
                }
            }
        }

        private boolean shouldSkip(RadixObject radixObject) {
            Definition def = radixObject.getDefinition();
            if (def.isDeprecated()) {
                return true;
            }
            if (def instanceof AdsDefinition) {
                AdsDefinition adsDef = (AdsDefinition) def;
                if (adsDef instanceof IAccessible) {
                    if (!((IAccessible) adsDef).getAccessFlags().isPublic()) {
                        return true;
                    }
                }
                if (adsDef.getDocGroup() == EDocGroup.METHOD || adsDef.getDocGroup() == EDocGroup.PROPERTY) {
                    if (!adsDef.getOwnerDef().isPublished()) {
                        return true;
                    }

                    if (adsDef.getOwnerDef() instanceof IAccessible) {
                        if (!((IAccessible) adsDef.getOwnerDef()).getAccessFlags().isPublic()) {
                            return true;
                        }
                    }
                }
                if (checkOptions.isCheckUDSRelatedDocumentationOnly()) {
                    switch (adsDef.getDefinitionType()) {
                        case CLASS:
                        case CLASS_METHOD:
                        case CLASS_PROPERTY:
                            return !(adsDef.getDocEnvironment() == ERuntimeEnvironmentType.SERVER || adsDef.getDocEnvironment() == ERuntimeEnvironmentType.COMMON);
                        default:
                            return true;
                    }
                } else {
                    return !(adsDef.getDocEnvironment() == ERuntimeEnvironmentType.SERVER || adsDef.getDocEnvironment() == ERuntimeEnvironmentType.COMMON);
                }
            } else {//DDS should be documented in any way
                return false;
            }
        }

        private String getTextualReport(Map<Layer, Map<Module, Summary>> module2summary) {
            StringBuilder sb = new StringBuilder();
            StringBuilder moduleCase = new StringBuilder();
            StringBuilder head = new StringBuilder();

            StringBuilder csvReportTitles = new StringBuilder();
            StringBuilder csvReportValues = new StringBuilder();
            StringBuilder csvReport = new StringBuilder();

            List<Layer> layers = new ArrayList<>(module2summary.keySet());
            Collections.sort(layers, new Comparator<Layer>() {
                @Override
                public int compare(Layer o1, Layer o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Layer l : layers) {
                if (l.isReadOnly()) {
                    continue;
                }
                StringBuilder brief = new StringBuilder();
                brief.append(" Layer ").append(l.getName()).append("\n");
                Map<Module, Summary> map = module2summary.get(l);
                if (map == null || map.isEmpty()) {
                    brief.append(" No information available\n");
                } else {
                    List<Module> modules = new ArrayList<>(map.keySet());
                    Collections.sort(modules, new Comparator<Module>() {

                        @Override
                        public int compare(Module o1, Module o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    Summary perLayerSummary = new Summary();
                    for (Module module : modules) {
                        Summary summary = map.get(module);
                        perLayerSummary.totalDefsCount += summary.totalDefsCount;
                        perLayerSummary.undocumentedDefsCount += summary.undocumentedDefsCount;

                        for (Map.Entry<String, Integer> e : summary.totalByType.entrySet()) {
                            Integer count = perLayerSummary.totalByType.get(e.getKey());
                            if (count == null) {
                                count = e.getValue();
                            } else {
                                count += e.getValue();
                            }
                            perLayerSummary.totalByType.put(e.getKey(), count);

                        }
                        for (Map.Entry<String, Integer> e : summary.undocumentedByType.entrySet()) {
                            Integer count = perLayerSummary.undocumentedByType.get(e.getKey());
                            if (count == null) {
                                count = e.getValue();
                            } else {
                                count += e.getValue();
                            }
                            perLayerSummary.undocumentedByType.put(e.getKey(), count);
                        }
                        for (Map.Entry<EIsoLanguage, Integer> e : summary.undocumentedByLang.entrySet()) {
                            Integer count = perLayerSummary.undocumentedByLang.get(e.getKey());
                            if (count == null) {
                                count = e.getValue();
                            } else {
                                count += e.getValue();
                            }
                            perLayerSummary.undocumentedByLang.put(e.getKey(), count);
                        }
                        for (Map.Entry<EIsoLanguage, Long> e : summary.lang2DescriptionLength.entrySet()) {
                            Long count = perLayerSummary.lang2DescriptionLength.get(e.getKey());
                            if (count == null) {
                                count = e.getValue();
                            } else {
                                count += e.getValue();
                            }
                            perLayerSummary.lang2DescriptionLength.put(e.getKey(), count);
                        }
                        moduleCase.append(" Module ").append(module.getQualifiedName()).append("\n");
                        append(moduleCase, "Total API elements: ", 40).append(summary.totalDefsCount).append("\n");
                        append(moduleCase, "Documented API elements: ", 40).append(summary.totalDefsCount - summary.undocumentedDefsCount).append("\n");
                        append(moduleCase, "Documented: ", 40).append(MessageFormat.format("{0, number,#.##}%\n", ((float) (summary.totalDefsCount - summary.undocumentedDefsCount) / (float) summary.totalDefsCount) * 100f));
                        moduleCase.append("          ------------- Documented by Language -----------------------------\n");
                        for (EIsoLanguage lang : l.getLanguages()) {
                            String langName = lang.getName();
                            Integer count = summary.undocumentedByLang.get(lang) == null ? summary.totalDefsCount : summary.totalDefsCount - summary.undocumentedByLang.get(lang);
                            append(moduleCase, langName + ": ", 40).append(count).append(" / ").append(summary.totalDefsCount).append(" (").
                                    append(summary.totalDefsCount == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", ((float) count) / ((float) summary.totalDefsCount) * 100f)).
                                    append("%, ").append(summary.lang2DescriptionLength.get(lang) == null ? 0 : summary.lang2DescriptionLength.get(lang)).
                                    append(" symbols)\n");
                        }
                        moduleCase.append("          --- Documented by Definition Type   (documented/total (% of documented)) ----\n");
                        List<String> sortedTypes = new ArrayList<>(summary.totalByType.keySet());
                        Collections.sort(sortedTypes);
                        for (String type : sortedTypes) {
                            Integer count = summary.undocumentedByType.get(type) == null ? summary.totalByType.get(type) : summary.totalByType.get(type) - summary.undocumentedByType.get(type);
                            Integer total = summary.totalByType.get(type);
                            if (total != null) {
                                append(moduleCase, type + ": ", 40).append(count).append(" / ").append(total).append(" (").append(total == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", ((float) count) / ((float) total) * 100f)).append("%)\n");
                            }
                        }
                        moduleCase.append(" ---------------------- ------------------------------- ----------------------\n");

                    }
                    String[] layerUriParts = l.getURI().split("\\.");
                    StringBuilder layerReportUri = new StringBuilder();
                    for (int i = 0; i < layerUriParts.length; i++) {
                        if (i != 0) {
                            layerReportUri.append("_");
                        }
                        layerReportUri.append(layerUriParts[i]);
                    }

                    append(brief, "Total API elements: ", 40).append(perLayerSummary.totalDefsCount).append("\n");
                    append(brief, "Documented API elements: ", 40).append(perLayerSummary.totalDefsCount - perLayerSummary.undocumentedDefsCount).append("\n");
                    head.append(brief);
                    sb.append(brief);
                    sb.append("          ------------- Documented by Language -----------------------------\n");
                    int index = 0;
                    for (EIsoLanguage lang : perLayerSummary.undocumentedByLang.keySet()) {
                        csvReportTitles.append(layerReportUri).append("_").append(lang.getValue()).append("_pct").
                                append(",").append(layerReportUri).append("_").append(lang.getValue()).append("_chars").
                                append(",").append(layerReportUri).append("_").append(lang.getValue()).append("_count");
                        String langName = lang.getName();
                        Integer count = perLayerSummary.undocumentedByLang.get(lang) == null ? perLayerSummary.totalDefsCount : perLayerSummary.totalDefsCount - perLayerSummary.undocumentedByLang.get(lang);
                        append(sb, langName + ": ", 40).append(count).append(" / ").append(perLayerSummary.totalDefsCount).
                                append(" (").append(perLayerSummary.totalDefsCount == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", ((float) count) / ((float) perLayerSummary.totalDefsCount) * 100f)).
                                append("%, ").append(perLayerSummary.lang2DescriptionLength.get(lang) == null ? 0 : perLayerSummary.lang2DescriptionLength.get(lang)).
                                append(" symbols)\n");
                        csvReportValues.append(perLayerSummary.totalDefsCount == 0 ? String.valueOf(0) : String.format(Locale.US, "%.2f", ((float) count) / ((float) perLayerSummary.totalDefsCount) * 100f)).
                                append(",").append(perLayerSummary.lang2DescriptionLength.get(lang) == null ? 0 : perLayerSummary.lang2DescriptionLength.get(lang)).
                                append(",").append(count);
                        if (++index != perLayerSummary.undocumentedByLang.keySet().size()) {
                            csvReportTitles.append(",");
                            csvReportValues.append(",");
                        }
                    }
                    sb.append("          --- Documented by Definition Type   (documented/total (% of documented)) ----\n");
                    List<String> sortedTypes = new ArrayList<>(perLayerSummary.totalByType.keySet());
                    Collections.sort(sortedTypes);
                    for (String lang : sortedTypes) {
                        Integer count = perLayerSummary.undocumentedByType.get(lang) == null ? perLayerSummary.totalByType.get(lang) : perLayerSummary.totalByType.get(lang) - perLayerSummary.undocumentedByType.get(lang);
                        Integer total = perLayerSummary.totalByType.get(lang);
                        if (total != null) {
                            append(sb, lang + ": ", 40).append(count).append(" / ").append(total).append(" (").append(total == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", ((float) count) / ((float) total) * 100f)).append("%)\n");
                        }
                    }
                    sb.append(" ---------------------- ------------------------------- ----------------------\n");
                }
            }
            StringBuilder report = new StringBuilder();
            report.append(" ---------------------- API Documentation Status Report ----------------------\n");
            report.append(" ---------------------- ------------------------------- ----------------------\n");
            report.append(" ----------------------          Short Summary          ----------------------\n");

            report.append(head);
            report.append(" ---------------------- ------------------------------- ----------------------\n");
            report.append(" ----------------------        Detailed Summary         ----------------------\n");
            report.append(sb);
            report.append(" ---------------------- ------------------------------- ----------------------\n");
            report.append(" ----------------------        Per-Module Summary       ----------------------\n");
            report.append(moduleCase);
            report.append(" ---------------------- ------------------------------- ----------------------\n");
            csvReport.append(csvReportTitles).append("\n").append(csvReportValues);
            if (checkOptions.getCsvReportPath() != null) {
                try {
                    Files.write(checkOptions.getCsvReportPath(), Arrays.asList(csvReport), Charset.forName("UTF-8"));
                } catch (IOException ex) {
                    Logger.getLogger(RadixObjectChecker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return report.toString();
        }

        private void saveReportDetails() {
            StringBuilder csvDetailsTitles = new StringBuilder();
            StringBuilder csvDetailsValues = new StringBuilder();
            StringBuilder csvDetails = new StringBuilder();

            csvDetailsTitles.append("layerUri,").
                    append("moduleName,").
                    append("moduleId,").
                    append("docGroupName,").
                    append("elementTypeName,").
                    append("elementName,").
                    append("elementId,").
                    append("ownerElementId");
            for (EIsoLanguage lang : languages) {
                csvDetailsTitles.append(",").append(lang.getValue()).append("DocLength");
            }
            csvDetailsTitles.append("\n");

            List<RadixObject> defs = new ArrayList<>(definitions);
            Collections.sort(defs, new Comparator<RadixObject>() {
                @Override
                public int compare(RadixObject o1, RadixObject o2) {
                    String n1 = o1.getLayer().getURI() + o1.getModule().getName() + o1.getTypeTitle() + o1.getName();
                    String n2 = o2.getLayer().getURI() + o2.getModule().getName() + o2.getTypeTitle() + o2.getName();

                    return n1.compareTo(n2);
                }
            });

            for (RadixObject obj : defs) {
                EDocGroup objDocGroup;
                if (obj instanceof IRadixDocObject) {
                    objDocGroup = ((IRadixDocObject) obj).getDocGroup();
                } else {
                    if (obj.getDefinition() != null) {
                        objDocGroup = obj.getDefinition().getDocGroup();
                    } else {
                        return;
                    }
                }

                String id;
                String name;
                if (objDocGroup != EDocGroup.METHOD_PARAMETER) {
                    name = (obj.getName() == null || obj.getName().isEmpty()) ? "null" : obj.getName();
                    if (objDocGroup == EDocGroup.METHOD) {
                        id = obj.getOwnerDefinition().getId().toString() + "/" + obj.getDefinition().getId();
                    } else {
                        id = obj.getDefinition().getId().toString();
                    }
                } else {
                    name = (obj.getName() == null || obj.getName().isEmpty()) ? "returnValue" : obj.getName();

                    Definition owner = obj.getOwnerDefinition();
                    id = owner.getOwnerDefinition().getId().toString() + "/" + owner.getDefinition().getId() + "/" + name;
                }

                csvDetailsValues.append(obj.getLayer().getURI()).append(",").
                        append(obj.getModule().getName()).append(",").
                        append(obj.getModule().getId()).append(",").
                        append(objDocGroup).append(",").
                        append(obj.getTypeTitle()).append(",").
                        append(name).append(",").
                        append(id).append(",").append(obj.getOwnerDefinition() == null ? "null" : obj.getOwnerDefinition().getId());

                for (EIsoLanguage lang : languages) {
                    csvDetailsValues.append(",").append(getDefLangDescriptionLength(obj, lang));
                }
                csvDetailsValues.append("\n");

                if (objDocGroup == EDocGroup.XSD && (obj instanceof AdsXmlSchemeDef)) {
                    AdsXmlSchemeDef schema = (AdsXmlSchemeDef) obj;
                    XmlObject[] simpleTypes = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:simpleType");
                    XmlObject[] complexTypes = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:complexType");
                    XmlObject[] elements = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:element");
                    XmlObject[] attributes = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:attribute");

                    appendNodesDetails(csvDetailsValues, schema, simpleTypes);
                    appendNodesDetails(csvDetailsValues, schema, complexTypes);
                    appendNodesDetails(csvDetailsValues, schema, elements);
                    appendNodesDetails(csvDetailsValues, schema, attributes);
                }
            }

            csvDetails.append(csvDetailsTitles).append(csvDetailsValues);

            try {
                Files.write(checkOptions.getDetailsFilePath(), Arrays.asList(csvDetails), Charset.forName("UTF-8"));
            } catch (IOException ex) {
                Logger.getLogger(RadixObjectChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public final String textualReport() {
            if (checkOptions.getDetailsFilePath() != null) {
                saveReportDetails();
            }
            if (checkOptions.isCheckExactDefTypesDoc()) {
                return new StringBuilder().append(getTextualReport(module2GroupSummary)).append(getTextualReport(module2summary)).toString();
            } else {
                return getTextualReport(module2GroupSummary);
            }
        }

        private StringBuilder append(StringBuilder sb, String text, int len) {
            if (text.length() < len) {
                for (int i = 0; i < len - text.length(); i++) {
                    sb.append(' ');
                }
            }
            sb.append(text);
            return sb;
        }

        private void appendNodesDetails(StringBuilder csvDetailsValues, AdsXmlSchemeDef def, XmlObject[] nodes) {
            for (XmlObject node : nodes) {
                boolean isDetailsNeeded;
                switch (node.getDomNode().getLocalName()) {
                    case "attribute":
                    case "element":
                        isDetailsNeeded = true;
                        break;
                    case "simpleType":
                    case "complexType":
                        isDetailsNeeded = ((Element) node.getDomNode()).hasAttribute("name");
                        break;
                    default:
                        isDetailsNeeded = false;
                }

                if (isDetailsNeeded) {
                    csvDetailsValues.append(def.getLayer().getURI()).append(",").
                            append(def.getModule().getName()).append(",").
                            append(def.getModule().getId()).append(",").
                            append(EDocGroup.XSD_ITEM).append(",").
                            append(node.getDomNode().getLocalName()).append(",").
                            append(XPathUtils.getHumanReadableXPath(XPathUtils.getXPath((Element) node.getDomNode()))).append(",").
                            append(XPathUtils.getXPath((Element) node.getDomNode())).append(",").
                            append(def.getId());
                    for (EIsoLanguage lang : languages) {
                        csvDetailsValues.append(",").append(getXsdItemDescriptionLength(def, XPathUtils.getXPath((Element) node.getDomNode()), lang));
                    }
                    csvDetailsValues.append("\n");
                }
            }
        }

        private boolean isDocumentedXmlItem(String xPath) {
            String[] parts = xPath.split("//");
            return parts[parts.length - 1].contains(":simpleType")
                    || parts[parts.length - 1].contains(":complexType")
                    || parts[parts.length - 1].contains(":element")
                    || parts[parts.length - 1].contains(":attribute");
        }

        private void registerXmlItems(RadixObject radixObject) {
            if (shouldSkip(radixObject)) {
                return;
            }

            AdsXmlSchemeDef schema = (AdsXmlSchemeDef) radixObject.getDefinition();
            XmlObject document = schema.getXmlDocument();
            if (document == null) {
                document = schema.getXmlContent();
            }

            if (document != null) {
                int simpleTypesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:simpleType").length;
                int complexTypesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:complexType").length;
                int elementsCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:element").length;
                int attributesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:attribute").length;

                int totalItemsCount = simpleTypesCount + complexTypesCount + elementsCount + attributesCount;

                Summary groupSummary = getGroupSummary(radixObject);
                if (groupSummary != null) {
                    groupSummary.totalDefsCount += totalItemsCount;
                    Integer count = groupSummary.totalByType.get(EDocGroup.XSD_ITEM.getValue());
                    if (count != null) {
                        count += totalItemsCount;
                    } else {
                        count = totalItemsCount;
                    }
                    groupSummary.totalByType.put(EDocGroup.XSD_ITEM.getValue(), count);
                    definitions.add(radixObject.getDefinition());
                }
            }
        }

        private void registerUndocumentedXmlItem(RadixObject radixObject) {
            if (shouldSkip(radixObject)) {
                return;
            }

            AdsXmlSchemeDef schema = (AdsXmlSchemeDef) radixObject.getDefinition();

            int simpleTypesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:simpleType").length;
            int complexTypesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:complexType").length;
            int elementsCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:element").length;
            int attributesCount = schema.getXmlDocument().selectPath("declare namespace xs='http://www.w3.org/2001/XMLSchema';$this//xs:attribute").length;

            int totalItemsCount = simpleTypesCount + complexTypesCount + elementsCount + attributesCount;

            int documentedItemsCount = 0;
            for (String nodePath : schema.getDocumentedNodes()) {
                if (isDocumentedXmlItem(nodePath)) {
                    documentedItemsCount++;
                }
            }

            int undocumentedItemsCount = totalItemsCount - documentedItemsCount;

            Summary groupSummary = getGroupSummary(radixObject);
            if (groupSummary != null) {
                groupSummary.undocumentedDefsCount += undocumentedItemsCount;
                Integer count = groupSummary.undocumentedByType.get(EDocGroup.XSD_ITEM.getValue());
                if (count != null) {
                    count += undocumentedItemsCount;
                } else {
                    count = undocumentedItemsCount;
                }
                groupSummary.undocumentedByType.put(EDocGroup.XSD_ITEM.getValue(), count);
            }
        }
    }

    private CheckHistory history;
    private CheckOptions checkOptions;

    public CheckHistory getHistory() {
        return history;
    }

    protected void setHistory(CheckHistory history) {
        this.history = history;
    }

    public CheckOptions getCheckOptions() {
        return checkOptions == null ? new CheckOptions() : checkOptions;
    }

    protected void setCheckOptions(CheckOptions checkOptions) {
        this.checkOptions = checkOptions;
    }

    public void check(T radixObject, IProblemHandler problemHandler) {
        final ENamingPolicy policy = radixObject.getNamingPolicy();

        if (policy == ENamingPolicy.IDENTIFIER || policy == ENamingPolicy.UNIQUE_IDENTIFIER) {
            final String name = radixObject.getName();
            if (name == null || name.isEmpty()) {
                error(radixObject, problemHandler, "Object name is not defined");
            } else if (!RadixObjectsUtils.isCorrectName(name)) {
                warning(radixObject, problemHandler, "Illegal name");
            } else if (policy == ENamingPolicy.UNIQUE_IDENTIFIER) {
                final CheckForDuplicationProvider checkForDuplicationProvider = CheckForDuplicationProvider.Factory.newForCheck(radixObject);
                final RadixObject duplicated = checkForDuplicationProvider.findDuplicated(name);
                if (duplicated != null) {
                    warning(radixObject, problemHandler, "Name of " + radixObject.getTypeTitle().toLowerCase() + " '" + name + "' is duplicated with "
                            + duplicated.getTypeTitle().toLowerCase() + " '" + duplicated.getQualifiedName(radixObject) + "'");
                }
            }
        }
        checkDocumentation(radixObject, problemHandler);
    }

    public void checkDocumentation(RadixObject radixObject, IProblemHandler problemHandler) {
        final CheckOptions checkOptions = getCheckOptions();
        if (checkOptions != null && checkOptions.isCheckDocumentation()) {
            Module module = radixObject.getModule();
            if ((module != null && module.isNeedsDoc()) || (radixObject instanceof AdsXmlSchemeDef && radixObject.needsDocumentation())) {
                final CheckHistory history = getHistory();

                if (history != null) {
                    DocumentationStatusReport report = history.findItemByClass(DocumentationStatusReport.class
                    );
                    if (report == null) {
                        report = new DocumentationStatusReport(checkOptions);
                        history.registerItemByClass(report);
                    }

                    report.accept(radixObject, problemHandler);
                }
            }
        }
    }

    public static void error(RadixObject radixObject, IProblemHandler problemHandler, String message) {
        RadixProblem problem = RadixProblem.Factory.newError(radixObject, message);
        if (problemHandler != null) {
            problemHandler.accept(problem);
        }
    }

    public static void warning(RadixObject radixObject, IProblemHandler problemHandler, String message) {
        RadixProblem problem = RadixProblem.Factory.newWarning(radixObject, message);
        if (problemHandler != null) {
            problemHandler.accept(problem);
        }
    }

    public static void warning(RadixObject radixObject, IProblemHandler problemHandler, int code) {
        RadixProblem problem = RadixProblem.Factory.newWarning(radixObject, code);
        if (problemHandler != null) {
            problemHandler.accept(problem);
        }
    }

    public static void warning(RadixObject radixObject, IProblemHandler problemHandler, int code, String... arguments) {
        RadixProblem problem = RadixProblem.Factory.newWarning(radixObject, code, arguments);
        if (problemHandler != null) {
            problemHandler.accept(problem);
        }
    }
}

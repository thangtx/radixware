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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.services.IRadixObjectService;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;

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
        }
        private final Map<Layer, Map<Module, Summary>> module2sumary = new HashMap<>();

        private boolean skipNonUdsCases;

        public DocumentationStatusReport(boolean skipNonUdsCases) {
            this.skipNonUdsCases = skipNonUdsCases;
        }

        public void accept(RadixObject radixObject, IProblemHandler problemHandler) {
            if (!radixObject.needsDocumentation()) {
                return;
            }
            Definition def = radixObject.getDefinition();
            if (def != null) {
                if (def.isPublished()) {
                    if (radixObject instanceof ILocalizedDescribable && def instanceof ILocalizedDef) {
                        register(radixObject);
                        Layer layer = radixObject.getLayer();
                        ILocalizedDescribable desc = (ILocalizedDescribable) radixObject;
                        Id id = desc.getDescriptionId();
                        if (id == null) {
                            noDescription(radixObject, problemHandler);
                            if (layer != null) {
                                for (EIsoLanguage l : layer.getLanguages()) {
                                    registerUndocumentedLang(radixObject, l);
                                }
                            }
                        } else {
                            IMultilingualStringDef stringDef = ((ILocalizedDef) def).findLocalizedString(id);
                            if (stringDef != null) {
                                if (layer != null) {
                                    boolean allStringsAreEmpty = true;
                                    for (EIsoLanguage l : layer.getLanguages()) {
                                        String text = stringDef.getValue(l);
                                        if (text == null || text.trim().isEmpty()) {
                                            registerUndocumentedLang(radixObject, l);
                                        } else {
                                            allStringsAreEmpty = false;
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
                        if (text == null || text.isEmpty()) {
                            //error
                            noDescription(radixObject, problemHandler);
                        }
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
            Map<Module, Summary> map = module2sumary.get(layer);
            if (map == null) {
                map = new HashMap<>();
                module2sumary.put(layer, map);
            }
            Module module = context.getModule();

            Summary summary = map.get(module);
            if (summary == null) {
                summary = new Summary();
                map.put(module, summary);
            }
            return summary;
        }

        private void register(RadixObject obj) {
            if (shouldSkip(obj)) {
                return;
            }
            Summary summary = getSummary(obj);
            if (summary != null) {
                summary.totalDefsCount++;
                Integer count = summary.totalByType.get(obj.getTypeTitle());
                if (count != null) {
                    count++;
                } else {
                    count = 1;
                }
                summary.totalByType.put(obj.getTypeTitle(), count);
            }
        }

        private void registerUndocumented(RadixObject obj) {
            if (shouldSkip(obj)) {
                return;
            }
            Summary summary = getSummary(obj);
            if (summary != null) {
                summary.undocumentedDefsCount++;
                Integer count = summary.undocumentedByType.get(obj.getTypeTitle());
                if (count != null) {
                    count++;
                } else {
                    count = 1;
                }
                summary.undocumentedByType.put(obj.getTypeTitle(), count);
            }
        }

        private boolean shouldSkip(RadixObject obj) {
            if (skipNonUdsCases) {
                Definition def = obj.getDefinition();
                if (def instanceof AdsDefinition) {
                    AdsDefinition adsDef = (AdsDefinition) def;
                    switch (adsDef.getDefinitionType()) {
                        case CLASS:
                        case CLASS_METHOD:
                        case CLASS_PROPERTY:
                            return !(adsDef.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER || adsDef.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON);
                        default:
                            return true;
                    }
                } else {//DDS shoild be documented in any way
                    return false;
                }

            } else {
                return false;
            }
        }

        private void registerUndocumentedLang(RadixObject obj, EIsoLanguage lang) {
            if (shouldSkip(obj)) {
                return;
            }
            Summary summary = getSummary(obj);
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
        }

        public final String textualReport() {
            StringBuilder sb = new StringBuilder();
            StringBuilder moduleCase = new StringBuilder();

            StringBuilder head = new StringBuilder();

            List<Layer> layers = new ArrayList<>(module2sumary.keySet());
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
                Map<Module, Summary> map = module2sumary.get(l);
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

                        moduleCase.append(" Module ").append(module.getQualifiedName()).append("\n");
                        append(moduleCase, "Total API elements: ", 40).append(summary.totalDefsCount).append("\n");
                        append(moduleCase, "Documented API elements: ", 40).append(summary.totalDefsCount - summary.undocumentedDefsCount).append("\n");
                        append(moduleCase, "Documented: ", 40).append(MessageFormat.format("{0, number,#.##}%\n", ((float) (summary.totalDefsCount - summary.undocumentedDefsCount) / (float) summary.totalDefsCount) * 100f));
                        moduleCase.append("          ------------- Undocumented by Language -----------------------------\n");
                        for (EIsoLanguage lang : summary.undocumentedByLang.keySet()) {
                            String langName = lang.getName();
                            Integer count = summary.undocumentedByLang.get(lang);
                            if (count != null) {
                                append(moduleCase, langName + ": ", 40).append(count).append("\n");
                            }
                        }
                        moduleCase.append("          --- Undocumented by Definition Type   (undocumented/total (% of documented)) ----\n");
                        for (String lang : summary.undocumentedByType.keySet()) {
                            Integer count = summary.undocumentedByType.get(lang);
                            Integer total = summary.totalByType.get(lang);
                            if (count != null && total != null) {
                                append(moduleCase, lang + ": ", 40).append(count).append(" / ").append(total).append(" (").append(total == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", 100 - ((float) count) / ((float) total) * 100f)).append("%)\n");
                            }
                        }
                        moduleCase.append(" ---------------------- ------------------------------- ----------------------\n");

                    }
                    append(brief, "Total API elements: ", 40).append(perLayerSummary.totalDefsCount).append("\n");
                    append(brief, "Documented API elements: ", 40).append(perLayerSummary.totalDefsCount - perLayerSummary.undocumentedDefsCount).append("\n");
                    head.append(brief);
                    sb.append(brief);
                    sb.append("          ------------- Undocumented by Language -----------------------------\n");
                    for (EIsoLanguage lang : perLayerSummary.undocumentedByLang.keySet()) {
                        String langName = lang.getName();
                        Integer count = perLayerSummary.undocumentedByLang.get(lang);
                        if (count != null) {
                            append(sb, langName + ": ", 40).append(count).append("\n");
                        }
                    }
                    sb.append("          --- Undocumented by Definition Type   (undocumented/total (% of documented)) ----\n");
                    for (String lang : perLayerSummary.undocumentedByType.keySet()) {
                        Integer count = perLayerSummary.undocumentedByType.get(lang);
                        Integer total = perLayerSummary.totalByType.get(lang);
                        if (count != null && total != null) {                            
                            append(sb, lang + ": ", 40).append(count).append(" / ").append(total).append(" (").append(total == 0 ? String.valueOf(0) : MessageFormat.format("{0, number,#.##}", 100 - ((float) count) / ((float) total) * 100f)).append("%)\n");
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

            return report.toString();
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
            if (module != null && module.isNeedsDoc()) {
                final CheckHistory history = getHistory();
                if (history != null) {
                    DocumentationStatusReport report = history.findItemByClass(DocumentationStatusReport.class);
                    if (report == null) {
                        report = new DocumentationStatusReport(checkOptions.isCheckUDSRelatedDocumentationOnly());
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

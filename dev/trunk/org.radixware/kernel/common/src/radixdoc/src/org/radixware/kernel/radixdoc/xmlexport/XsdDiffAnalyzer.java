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
package org.radixware.kernel.radixdoc.xmlexport;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.VersionNumber;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.EnumDefinition;

public class XsdDiffAnalyzer {

    private static final String TAB_SYMBOL = "&#8201;";
    private static final String TABLE_STYLE = "background-color: black; width: 100%;";

    private static final String DIV_STYLE = "padding: 10px;";
    private static final String FIRST_ROW_STYLE = "background-color: #F8F8F8; font-weight: bold;";
    private static final String ROW_STYLE = "background-color: white;";
    
    private static final String EMPTY_DIV = "<div style='" + DIV_STYLE + "'>" + "</div>";
    private static final String NO_CHANGES_DIV = "<div style='" + DIV_STYLE + "'>" + "<table><tr><td>No Changes</td></tr></table>" + "</div>";

    private static enum RowDiffAction {

        ADDED,
        REMOVED
    }

    private static class EnumDiffTarget {

        private static class Item {

            private final String layerUri;
            private final String moduleName;
            private final String enumName;
            private final String name;
            private final String value;
            private final Id id;

            public Item(String enumName, AdsEnumItemDef source, String enumPath) {
                id = source.getId();
                this.enumName = enumName;
                name = source.getName();
                value = source.getValue().toString();

                String pathParts[] = enumPath.split("/");
                layerUri = pathParts.length > 3 ? pathParts[0] : "";
                moduleName = pathParts.length > 3 ? pathParts[2] : "";
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 97 * hash + Objects.hashCode(this.layerUri);
                hash = 97 * hash + Objects.hashCode(this.id);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Item other = (Item) obj;
                if (!Objects.equals(this.layerUri, other.layerUri)) {
                    return false;
                }
                if (!Objects.equals(this.id, other.id)) {
                    return false;
                }
                return true;
            }

            private String getQualifiedName() {
                return layerUri + "::" + moduleName + "::" + enumName + ":" + name;
            }

            @Override
            public String toString() {
                return getQualifiedName() + " (value='" + value + "')";
            }
        }

        private final String layerUri;
        private final String moduleName;
        private final String enumName;
        private final List<Item> enumItems = new ArrayList<>();

        public EnumDiffTarget(String enumName, String enumPath) {
            this.enumName = enumName;

            String pathParts[] = enumPath.split("/");
            layerUri = pathParts.length > 3 ? pathParts[0] : "";
            moduleName = pathParts.length > 3 ? pathParts[2] : "";
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 47 * hash + Objects.hashCode(this.layerUri);
            hash = 47 * hash + Objects.hashCode(this.moduleName);
            hash = 47 * hash + Objects.hashCode(this.enumName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final EnumDiffTarget other = (EnumDiffTarget) obj;
            if (!Objects.equals(this.layerUri, other.layerUri)) {
                return false;
            }
            if (!Objects.equals(this.moduleName, other.moduleName)) {
                return false;
            }
            if (!Objects.equals(this.enumName, other.enumName)) {
                return false;
            }
            return true;
        }

        private String getQualifiedName() {
            return layerUri + "::" + moduleName + "::" + enumName;
        }
    }

    private static class EnumDiffTargetSorter {

        private final List<String> layersHierarchy;

        public EnumDiffTargetSorter(List<String> layersHierarchy) {
            this.layersHierarchy = new ArrayList<>(layersHierarchy);
        }

        public List<Id> getSortedEnumIds(Set<Id> allEnumIds, final Map<Id, EnumDiffTarget> source, final Map<Id, EnumDiffTarget> other) {
            List<Id> result = new ArrayList<>(allEnumIds);
            Collections.sort(result, new Comparator<Id>() {
                @Override
                public int compare(Id o1, Id o2) {
                    EnumDiffTarget target1 = source.containsKey(o1) ? source.get(o1) : other.get(o1);
                    EnumDiffTarget target2 = source.containsKey(o2) ? source.get(o2) : other.get(o2);

                    if (!target1.layerUri.equals(target2.layerUri)) {
                        return Integer.compare(layersHierarchy.indexOf(target2.layerUri), layersHierarchy.indexOf(target1.layerUri));
                    }
                    return target1.getQualifiedName().compareTo(target2.getQualifiedName());
                }
            });

            return result;
        }

        public void sortEnumDiffTargetItems(List<EnumDiffTarget.Item> items) {
            Collections.sort(items, new Comparator<EnumDiffTarget.Item>() {
                @Override
                public int compare(EnumDiffTarget.Item o1, EnumDiffTarget.Item o2) {
                    if (!o1.layerUri.equals(o2.layerUri)) {
                        return Integer.compare(layersHierarchy.indexOf(o2.layerUri), layersHierarchy.indexOf(o1.layerUri));
                    }

                    try {
                        return Integer.valueOf(o1.value).compareTo(Integer.valueOf(o2.value));
                    } catch (NumberFormatException e) {
                        return o1.value.compareTo(o2.value);
                    }
                }
            });
        }
    }

    public static String getDiffHtml(FileProvider fileProvider, AdsXmlSchemeExportableWrapper sourceSchema, String sourceBranchVersion, String otherBranchVersion) {
        List<String> layerHierarchy = new ArrayList<>();
        Map<String, AdsEnumDef> sourceEnums = collectSourceEnums(sourceSchema, layerHierarchy);
        Map<String, AdsEnumDef> otherEnums = collectOtherEnums(fileProvider, sourceEnums.keySet());

        Map<Id, EnumDiffTarget> sourceTargets = getEnumDiffTargets(sourceEnums);
        Map<Id, EnumDiffTarget> otherTargets = getEnumDiffTargets(otherEnums);

        VersionNumber sourceVersionNumber = VersionNumber.valueOf(sourceBranchVersion);
        VersionNumber otherVersionNumber = VersionNumber.valueOf(otherBranchVersion);

        EnumDiffTargetSorter sorter = new EnumDiffTargetSorter(layerHierarchy);
        
        String diffHtml;
        if (sourceVersionNumber.compareTo(otherVersionNumber) > 0) {
            diffHtml = diff(otherTargets, sourceTargets, otherBranchVersion, sourceBranchVersion, sorter);
        } else {
            diffHtml = diff(sourceTargets, otherTargets, sourceBranchVersion, otherBranchVersion, sorter);
        }                
        
        return diffHtml.replace(EMPTY_DIV, NO_CHANGES_DIV);
    }

    private static Map<String, AdsEnumDef> collectSourceEnums(AdsXmlSchemeExportableWrapper sourceSchema, final List<String> layerHierarchy) {
        final Map<String, AdsEnumDef> result = new HashMap<>();
        final Set<Id> allEnumIds = new HashSet<>();
        final Set<Id> processedSchemasIds = new HashSet<>();
        processedSchemasIds.add(sourceSchema.getId());

        collectAllSchemaEnums(sourceSchema, processedSchemasIds, allEnumIds);

        AdsXmlSchemeDef userObj = (AdsXmlSchemeDef) sourceSchema.getUserObject();
        Layer.HierarchyWalker.walk(userObj.getLayer(), new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                if (!layerHierarchy.contains(layer.getURI())) {
                    layerHierarchy.add(layer.getURI());
                }
                layer.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        AdsEnumDef enumDef = (AdsEnumDef) radixObject;
                        String path = enumDef.getFile().getPath().replace(enumDef.getBranch().getFile().getParent(), "").replace("\\", "/").substring(1);

                        result.put(path, enumDef);
                    }
                }, new AdsVisitorProvider.AdsTopLevelDefVisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return (radixObject instanceof AdsEnumDef) && allEnumIds.contains(((AdsEnumDef) radixObject).getId());
                    }

                    @Override
                    public boolean isContainer(RadixObject radixObject) {
                        return radixObject instanceof Branch || radixObject instanceof Layer || radixObject instanceof AdsSegment || radixObject instanceof Module || radixObject instanceof ModuleDefinitions;
                    }
                });
            }
        });

        return result;
    }

    private static void collectAllSchemaEnums(IExportableXmlSchema schema, final Set<Id> processedSchemasIds, final Set<Id> allEnumIds) {
        AdsXmlSchemeDef userObj = (AdsXmlSchemeDef) schema.getUserObject();

        XmlObject obj = userObj.getXmlDocument();
        if (obj == null) {
            obj = userObj.getXmlContent();
        }

        if (obj == null) {
            return;
        }

        schema.processEnumerations(obj.copy().getDomNode());
        allEnumIds.addAll(schema.getEnumIds());

        for (IExportableXmlSchema child : schema.getLinkedSchemas()) {
            if (child instanceof AdsXmlSchemeExportableWrapper && !processedSchemasIds.contains(child.getId())) {
                processedSchemasIds.add(child.getId());
                collectAllSchemaEnums(child, processedSchemasIds, allEnumIds);
            }
        }
    }

    private static Map<String, AdsEnumDef> collectOtherEnums(FileProvider fileProvider, Set<String> sourceEnumPaths) {
        final Map<String, AdsEnumDef> result = new HashMap<>();
        for (String path : sourceEnumPaths) {
            try {
                InputStream enumXmlStream = fileProvider.getInputStream(path);
                if (enumXmlStream == null) {
                    continue;
                }

                AdsDefinitionDocument xEnumDefDoc = AdsDefinitionDocument.Factory.parse(enumXmlStream);
                EnumDefinition xEnumDef = xEnumDefDoc.getAdsDefinition().getAdsEnumDefinition();
                AdsEnumDef enumDef = AdsEnumDef.Factory.loadFrom(xEnumDef);

                result.put(path, enumDef);
            } catch (XmlException | IOException ex) {
                Logger.getLogger(XsdDiffAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

    private static Map<Id, EnumDiffTarget> getEnumDiffTargets(Map<String, AdsEnumDef> enums) {
        Map<Id, EnumDiffTarget> diffTargets = new HashMap<>();
        for (Map.Entry<String, AdsEnumDef> enumDefEntry : enums.entrySet()) {
            AdsEnumDef enumDef = enumDefEntry.getValue();

            EnumDiffTarget target;
            if (diffTargets.containsKey(enumDef.getId())) {
                target = diffTargets.get(enumDef.getId());
            } else {
                target = new EnumDiffTarget(enumDef.getName(), enumDefEntry.getKey()); // проверить для перегруженых enumo-в из других слоев
                diffTargets.put(enumDef.getId(), target);
            }
            target.enumItems.addAll(enumItemsToDiffTargetItemsList(enumDef.getName(), enumDef.getItems(), enumDefEntry.getKey()));
        }

        return diffTargets;
    }

    private static List<EnumDiffTarget.Item> enumItemsToDiffTargetItemsList(String enumName, AdsEnumDef.Items items, String enumPath) {
        List<EnumDiffTarget.Item> result = new ArrayList<>();
        for (AdsEnumItemDef item : items.get(ExtendableDefinitions.EScope.LOCAL)) {
            result.add(new EnumDiffTarget.Item(enumName, item, enumPath));
        }

        return result;
    }

    private static String diff(Map<Id, EnumDiffTarget> source, Map<Id, EnumDiffTarget> other, String sourceVersion, String ohterVersion, EnumDiffTargetSorter sorter) {
        final StringBuilder resultBuilder = new StringBuilder("<html>");
        resultBuilder.append("<h1 style='text-align: center;'>");
        resultBuilder.append("Changes between ").append(sourceVersion).append(" and ").append(ohterVersion);
        resultBuilder.append("</h1>");
        
        resultBuilder.append("<div style='").append(DIV_STYLE).append("'>");

        Set<Id> allEnumIds = new HashSet<>(source.keySet());
        allEnumIds.addAll(other.keySet());
        List<Id> sortedIds = sorter.getSortedEnumIds(allEnumIds, source, other);

        boolean isFirst = true;
        for (Id enumId : sortedIds) {
            String enumDiffTableHtml = getEnumDiffHtmlTable(source.get(enumId), other.get(enumId), sorter);
            if (!enumDiffTableHtml.isEmpty()) {
                if (!isFirst) {
                    resultBuilder.append("<br>");
                } else {
                    isFirst = false;
                }
            }
            resultBuilder.append(enumDiffTableHtml);
        }

        resultBuilder.append("</div>").append("</html>");
        return resultBuilder.toString();
    }

    private static String getEnumDiffHtmlTable(EnumDiffTarget source, EnumDiffTarget other, EnumDiffTargetSorter sorter) {
        StringBuilder tableBuilder = new StringBuilder("<table style='").append(TABLE_STYLE).append("' cellspacing='1px'>");
        tableBuilder.append("<tr>");
        tableBuilder.append("<td style='").append(FIRST_ROW_STYLE).append("'>").append(source == null ? other.getQualifiedName() : source.getQualifiedName()).append("</td>");
        tableBuilder.append("</tr>");

        tableBuilder.append("<tr>");
        tableBuilder.append("<td style='").append(ROW_STYLE).append("'>");

        if (source != null && other != null) {
            List<EnumDiffTarget.Item> added = new ArrayList<>(other.enumItems);
            List<EnumDiffTarget.Item> removed = new ArrayList<>(source.enumItems);

            added.removeAll(source.enumItems);
            removed.removeAll(other.enumItems);

            sorter.sortEnumDiffTargetItems(added);
            sorter.sortEnumDiffTargetItems(removed);

            if (added.isEmpty() && removed.isEmpty()) {
                return "";
            }

            appendDiffTableRows(tableBuilder, added, RowDiffAction.ADDED);
            appendDiffTableRows(tableBuilder, removed, RowDiffAction.REMOVED);
        } else {
            if (source == null && other != null) {
                sorter.sortEnumDiffTargetItems(other.enumItems);
                appendDiffTableRows(tableBuilder, other.enumItems, RowDiffAction.ADDED);
            }

            if (other == null && source != null) {
                sorter.sortEnumDiffTargetItems(source.enumItems);
                appendDiffTableRows(tableBuilder, source.enumItems, RowDiffAction.REMOVED);
            }
        }

        tableBuilder.append("</td>");
        tableBuilder.append("</tr>");
        tableBuilder.append("</table>");
        return tableBuilder.toString();
    }

    private static void appendDiffTableRows(final StringBuilder tableBuilder, List<EnumDiffTarget.Item> items, RowDiffAction action) {
        final String marker = action == RowDiffAction.ADDED ? "+ " : "- ";
        final String color = action == RowDiffAction.ADDED ? "238C23" : "A12525";

        for (EnumDiffTarget.Item item : items) {
            tableBuilder.append("<font color='").append(color).append("'>");
            tableBuilder.append(TAB_SYMBOL).append(marker).append(item);
            tableBuilder.append("</font>");
            tableBuilder.append("<br>");
        }
    }
}

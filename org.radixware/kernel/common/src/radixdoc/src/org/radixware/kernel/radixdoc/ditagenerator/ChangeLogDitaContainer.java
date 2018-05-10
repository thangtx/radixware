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
package org.radixware.kernel.radixdoc.ditagenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.xmlbeans.XmlDocumentProperties;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.radixdoc.enums.ELocalizationMapKeys;
import org.radixware.kernel.radixdoc.generator.GeneratorUtils;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.schemas.commondef.ChangeLog;
import org.radixware.schemas.commondef.ChangeLogItem;
import org.radixware.schemas.radixdoc.AdsXmlSchemeDefDocItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChangeLogDitaContainer {

    private static final String[] TOPIC_DECLARATION_PARTS = {"topic", "-//OASIS//DTD DITA Topic//EN", "topic.dtd"};

    private final RadixdocGenerationContext context;
    private final Map<ChangeLogItem, DefinitionDocInfo> changeLogItems = new HashMap<>();
    private final Map<String, List<String>> layersAllowedVersion = new HashMap<>();

    private XmlObject changeLogDitaDocument;
    private Element changeLogTableBody;

    public ChangeLogDitaContainer(RadixdocGenerationContext context) {
        this.context = context;
        changeLogDitaDocument = null;
    }

    private void init(EIsoLanguage lang) {
        changeLogDitaDocument = XmlObject.Factory.newInstance();

        XmlDocumentProperties props = changeLogDitaDocument.documentProperties();
        props.setDoctypeName(TOPIC_DECLARATION_PARTS[0]);
        props.setDoctypePublicId(TOPIC_DECLARATION_PARTS[1]);
        props.setDoctypeSystemId(TOPIC_DECLARATION_PARTS[2]);

        Document document = changeLogDitaDocument.getDomNode().getOwnerDocument();

        Element topic = document.createElement("topic");
        changeLogDitaDocument.getDomNode().appendChild(topic);

        topic.setAttribute("id", "changelog");
        topic.appendChild(DitaGeneratorUtils.createElement(document, "title", GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.CHANGE_LOG, lang)));

        Element body = document.createElement("body");
        topic.appendChild(body);

        Element paragraph = document.createElement("p");
        body.appendChild(paragraph);

        Element changeLogTable = document.createElement("table");
        paragraph.appendChild(changeLogTable);
        changeLogTable.setAttribute("pgwide", "0");

        changeLogTable.setAttribute("rowsep", "1");
        changeLogTable.setAttribute("colsep", "1");
        changeLogTable.setAttribute("frame", "all");
        changeLogTable.setAttribute("pgwide", "1");

        int changeLogTableCols = 4;
        String[] columnsWidthPercent = {"15%", "10%", "37.5%", "37.5%"};

        Element tgroup = document.createElement("tgroup");
        changeLogTable.appendChild(tgroup);

        tgroup.setAttribute("cols", String.valueOf(changeLogTableCols));
        for (int i = 0; i < changeLogTableCols; i++) {
            Element colspec = document.createElement("colspec");
            tgroup.appendChild(colspec);

            colspec.setAttribute("colname", "COLSPEC" + i);
            colspec.setAttribute("align", "left");
            colspec.setAttribute("colwidth", columnsWidthPercent[i]);
        }

        Element thead = document.createElement("thead");
        tgroup.appendChild(thead);

        Element theadRow = document.createElement("row");
        thead.appendChild(theadRow);

        theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.DATE, lang)));
        theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.VERSION, lang)));
        theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.SCHEMA, lang)));
        theadRow.appendChild(DitaGeneratorUtils.createTableTitleEntry(document, GeneratorUtils.getDocPartLocalization(context, ELocalizationMapKeys.DESCRIPTION, lang)));

        changeLogTableBody = document.createElement("tbody");
        tgroup.appendChild(changeLogTableBody);
        changeLogTableBody.setAttribute("valign", "middle");
    }

    private void addItemsToTable(EIsoLanguage lang) {
        if (changeLogDitaDocument == null) {
            init(lang);
        }

        Locale locale = new Locale(lang.getValue());

        Document document = changeLogDitaDocument.getDomNode().getOwnerDocument();
        List<ChangeLogItem> sortedChangeLogItems = new ArrayList<>(changeLogItems.keySet());
        Collections.sort(sortedChangeLogItems, new Comparator<ChangeLogItem>() {

            @Override
            public int compare(ChangeLogItem o1, ChangeLogItem o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        for (ChangeLogItem entry : sortedChangeLogItems) {
            DefinitionDocInfo docInfo = changeLogItems.get(entry);
            AdsXmlSchemeDefDocItem schema = (AdsXmlSchemeDefDocItem) docInfo.getDocItem();

            Element row = document.createElement("row");
            changeLogTableBody.appendChild(row);

            row.appendChild(DitaGeneratorUtils.createTableEntry(document, new SimpleDateFormat("dd MMM yyyy", locale).format(entry.getDate().getTime())));
            row.appendChild(DitaGeneratorUtils.createTableEntry(document, entry.getSinceVersion()));
            {
                Element refEntry = document.createElement("entry");
                row.appendChild(refEntry);

                refEntry.setAttribute("valign", "middle");

                String strId = schema.getId().toString();

                String xrefText = docInfo.getQualifiedName(true);
                String xrefHref = strId + ".dita#" + strId;
                Element xref = DitaGeneratorUtils.createXRef(document, xrefText, xrefHref);
                refEntry.appendChild(xref);
            }

            String changeLogDescription = GeneratorUtils.getLocalizedStringValueById(context, schema.getId(), entry.getDescriptionMlsId(), lang);
            row.appendChild(DitaGeneratorUtils.createTableEntry(document, changeLogDescription));
        }
    }

    public boolean isEmpty() {
        return changeLogItems.isEmpty();
    }

    public void addSchemaChangeLog(final DefinitionDocInfo docInfo, final ChangeLog changeLog) {
        if (changeLog != null) {
            List<String> allowedVersions = null;
            if (docInfo != null) {
                if (layersAllowedVersion.containsKey(docInfo.getLayerUri())) {
                    allowedVersions = layersAllowedVersion.get(docInfo.getLayerUri());
                } else {
                    allowedVersions = GeneratorUtils.getAllowedVersionsList(context, docInfo);
                    layersAllowedVersion.put(docInfo.getLayerUri(), allowedVersions);
                }
            }

            for (ChangeLogItem entry : changeLog.getChangeLogItemList()) {
                if (allowedVersions != null && !allowedVersions.contains(entry.getSinceVersion())) {
                    continue;
                }
                changeLogItems.put(entry, docInfo);
            }
        }
    }

    public XmlObject getChangeLogDitaDocument(final EIsoLanguage lang) {
        addItemsToTable(lang);
        return changeLogDitaDocument;
    }
}

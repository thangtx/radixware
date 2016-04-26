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

package org.radixware.kernel.designer.ads.common.upgrade;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.upgrade.IRadixObjectUpgrader;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class AdsSqlClassUpgrader3 implements IRadixObjectUpgrader {

    private static class Field {

        String name;
        int idx;
        String idAsStr;
    }
    private final List<Field> fields = new ArrayList<Field>();

    private Field getFieldByIdx(int idx) {
        for (Field field : fields) {
            if (field.idx == idx) {
                return field;
            }
        }
        throw new RadixError("Unable to upgrader SQL class: field " + idx + " not found.");
    }

    private static String getValue(Element element) {
        String result = "";
        for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
            result += child.getNodeValue();
        }
        return result;
    }

    @Override
    public void firstStep(Element root) {
        final Document doc = root.getOwnerDocument();
        final Node adsClassDefinitionNode = XmlUtils.findChildByLocalName(root, "AdsClassDefinition");

        // collect fields
        fields.clear();
        final Node propertiesNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Properties");
        if (propertiesNode != null) {
            for (Node propertyNode = propertiesNode.getFirstChild(); propertyNode != null; propertyNode = propertyNode.getNextSibling()) {
                if ("Property".equals(propertyNode.getLocalName())) {
                    final Integer idx = XmlUtils.getInteger(propertyNode, "SqlQueryParamIdx");
                    if (idx != null) {
                        final Field field = new Field();
                        field.idAsStr = XmlUtils.getString(propertyNode, "Id");
                        field.idx = idx.intValue();
                        field.name = XmlUtils.getString(propertyNode, "Name");
                        fields.add(field);
                    }
                }
            }
        }

        // upgrade SQML
        final Node sqlNode = XmlUtils.findChildByLocalName(adsClassDefinitionNode, "Sql");

        final Node sourceNode = XmlUtils.findChildByLocalName(sqlNode, "Source");
        if (sourceNode == null) {
            return;
        }

        final SqlProcessor sqlProcessor = new SqlProcessor();
        String lastText = "";
        final String sqlClassIdAsSql = XmlUtils.getString(adsClassDefinitionNode, "Id");
        int fieldIdx = 0;

        final List<Element> items = new ArrayList<Element>();

        for (Node itemNode = sourceNode.getFirstChild(); itemNode != null; itemNode = itemNode.getNextSibling()) {
            if ("Item".equals(itemNode.getLocalName())) {
                final Element itemElement = (Element) itemNode;
                items.add(itemElement);
            }
        }

        for (int i = 0; i < items.size(); i++) {
            final Element itemElement = items.get(i);
            final String sqmlNamespace = itemElement.getNamespaceURI();

            final Element textElement = XmlUtils.findChildByLocalName(itemElement, "Sql");
            if (textElement != null) {
                final String text = getValue(textElement);
                if (text != null) {
                    sqlProcessor.process(text);
                    lastText += text;
                }
                continue;
            }

            if (sqlProcessor.isInTopSelect()) {
                final Element propSqlNameTagElement = XmlUtils.findChildByLocalName(itemElement, "PropSqlName");
                if (propSqlNameTagElement != null) {
                    fieldIdx++;

                    // Some.Prop as ALIAS -> Some.Prop as This.Prop
                    // Some.Prop -> Some.Prop as This.Prop
                    final Field field = getFieldByIdx(fieldIdx);

                    final Element propAliasElement = XmlUtils.findChildByLocalName(propSqlNameTagElement, "PropAlias");
                    if (propAliasElement != null) {
                        propSqlNameTagElement.removeChild(propAliasElement);
                    }

                    final Element nextItemElement = items.get(i + 1);

                    final Element newTextItem = doc.createElementNS(sqmlNamespace, "Item");
                    final Element newTextElement = doc.createElementNS(sqmlNamespace, "Sql");
                    final Node newTextNode = doc.createTextNode(" as ");
                    newTextElement.appendChild(newTextNode);
                    newTextItem.appendChild(newTextElement);
                    sourceNode.insertBefore(newTextItem, nextItemElement);

                    final Element newThisPropItem = doc.createElementNS(sqmlNamespace, "Item");
                    final Element newThisPropElement = doc.createElementNS(sqmlNamespace, "PropSqlName");
                    newThisPropElement.setAttribute("TableId", sqlClassIdAsSql);
                    newThisPropElement.setAttribute("PropId", field.idAsStr);
                    newThisPropElement.setAttribute("Owner", "THIS");
                    newThisPropItem.appendChild(newThisPropElement);
                    sourceNode.insertBefore(newThisPropItem, nextItemElement);
                }
            }

            Element calcFieldTagElement = XmlUtils.findChildByLocalName(itemElement, "CalcField");
            if (calcFieldTagElement == null) {
                calcFieldTagElement = XmlUtils.findChildByLocalName(itemElement, "TypifiedCalcField");
            }
            if (calcFieldTagElement != null) {
                final String name = calcFieldTagElement.getAttribute("Name");

                if (sqlProcessor.isInTopSelect()) {
                    fieldIdx++;

                    final Field field = getFieldByIdx(fieldIdx);
                    final Element newThisPropElement = doc.createElementNS(sqmlNamespace, "PropSqlName");
                    newThisPropElement.setAttribute("TableId", sqlClassIdAsSql);
                    newThisPropElement.setAttribute("PropId", field.idAsStr);
                    newThisPropElement.setAttribute("Owner", "THIS");

                    // 1) Long calculation AS CalcField - replace CalcField by 'This.Prop'
                    // 2) CalcField - replace by CalcField.Text AS 'This.Prop'

                    if (!lastText.trim().toLowerCase().endsWith("as")) {
                        final Element newTextItem = doc.createElementNS(sqmlNamespace, "Item");
                        final Element newTextElement = doc.createElementNS(sqmlNamespace, "Sql");
                        final Node newTextNode = doc.createTextNode(name + " as ");
                        newTextElement.appendChild(newTextNode);
                        newTextItem.appendChild(newTextElement);
                        sourceNode.insertBefore(newTextItem, itemElement);
                    }

                    itemElement.replaceChild(newThisPropElement, calcFieldTagElement);
                } else {
                    // calc field tag in not top of sql - replace by text
                    final Element newTextElement = doc.createElementNS(sqmlNamespace, "Sql");
                    final Node newTextNode = doc.createTextNode(name);
                    newTextElement.appendChild(newTextNode);
                    itemElement.replaceChild(newTextElement, calcFieldTagElement);
                }
            }

            lastText = "";
        }
    }

    @Override
    public void finalStep(RadixObject radixObject) {
        // NOTHING
    }
}

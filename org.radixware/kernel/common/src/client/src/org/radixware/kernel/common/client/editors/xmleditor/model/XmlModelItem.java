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
package org.radixware.kernel.common.client.editors.xmleditor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.editors.xmleditor.view.XmlValueEditingOptions;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.text.ITextOptions;
import org.radixware.kernel.common.client.utils.XmlSchemaTypeSystemCompiler;
import org.radixware.kernel.common.client.editors.xmleditor.IXmlSchemaProvider;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.IXmlSchemaItemWithAttributes;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.IXmlSchemaNamedItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAnyTypeContainerItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaAttributeItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaChoiceItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaContainerItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaElementsListItem;
import org.radixware.kernel.common.client.editors.xmleditor.model.schema.XmlSchemaItem;

public class XmlModelItem {

    private final XmlDocumentItem xmlNode;
    private final XmlModelItem parent;
    private final XmlSchemaItem schemaItem;
    private final IXmlSchemaProvider schemaProvider;
    private int indexForCurrentChoiceItem = -1;
    private int seqForListItem = -1;
    private boolean isConform;
    private boolean emptyContainer;
    private final List<XmlDocumentItem> existingItems = new LinkedList<>();
    private List<XmlModelItem> logicalChildsCache;

    public XmlModelItem(final XmlDocumentItem node,
            final XmlSchemaItem type,
            final XmlModelItem parentItem) {
        this(node, type, parentItem, null);
    }

    public XmlModelItem(final XmlDocumentItem node,
            final XmlSchemaItem type,
            final IXmlSchemaProvider schemaProvider) {
        this(node, type, null, schemaProvider);
    }

    public XmlModelItem(final XmlDocumentItem node,
            final XmlSchemaItem type,
            final XmlModelItem parentItem,
            final IXmlSchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
        parent = parentItem;
        xmlNode = node;
        schemaItem = type;
    }

    private XmlSchemaElementItem typify(final XmlDocumentItem untypified, final String nameSpaceUri) {
        IXmlSchemaProvider xmlSchemaProvider = null;
        for (XmlModelItem modelItem = this; modelItem != null; modelItem = modelItem.parent) {
            if (modelItem.schemaProvider != null) {
                xmlSchemaProvider = modelItem.schemaProvider;
            }
        }
        if (xmlSchemaProvider != null) {
            final String schemaAsStr = xmlSchemaProvider.getSchemaForNamespaceUri(nameSpaceUri);
            if (schemaAsStr != null && !schemaAsStr.isEmpty()) {
                final List<String> errors = new ArrayList<>();
                final SchemaTypeSystem types
                        = XmlSchemaTypeSystemCompiler.compile(Thread.currentThread().getContextClassLoader(), schemaAsStr, errors);
                if (types != null) {
                    try {
                        final QName fullName = untypified.getFullName();
                        final SchemaType xsdDocType = types.findDocumentType(fullName);
                        final SchemaParticle xsdDocParticle = xsdDocType == null ? null : xsdDocType.getContentModel();
                        if (xsdDocParticle != null) {
                            final SchemaType xsdDocContentType = xsdDocParticle.getType();
                            if (xsdDocContentType != null) {
                                types.parse(untypified.getXmlObject().getDomNode(), xsdDocContentType, new XmlOptions());
                            }
                            return new XmlSchemaElementItem(xsdDocParticle, nameSpaceUri);
                        }
                    } catch (XmlException exception) {
                        exception.printStackTrace();//TODO
                    }
                }
            }
        }
        return null;
    }

    public List<XmlModelItem> getLogicalChildItems() {
        if (logicalChildsCache == null) {
            logicalChildsCache = getLogicalChildItemsImpl();
        }
        return Collections.unmodifiableList(logicalChildsCache);
    }

    private List<XmlModelItem> getLogicalChildItemsImpl() {
        final List<XmlModelItem> childItems = new ArrayList<>();
        final List<QName> namesFindInSchemeItems = new ArrayList<>();
        if (schemaItem != null) {
            if (schemaItem instanceof IXmlSchemaItemWithAttributes) {
                final List<XmlModelItem> attrItems = new ArrayList<>();
                for (XmlSchemaAttributeItem attrSchemaItem : ((IXmlSchemaItemWithAttributes) schemaItem).getAttributes()) {
                    List<XmlDocumentItem> childNodeItem
                            = findLogicalChildItemByQName((attrSchemaItem).getFullName());
                    if (!childNodeItem.isEmpty()) {
                        final XmlDocumentItem attrm = childNodeItem.get(0);
                        attrItems.add(new XmlModelItem(attrm, attrSchemaItem, this));
                        namesFindInSchemeItems.add(attrm.getFullName());
                    } else {
                        attrItems.add(new XmlModelItem(null, attrSchemaItem, this));
                    }
                }
                if (getXmlNode() != null) {
                    int startAttrIndex = 0;
                    for (int i = 0; i < getXmlNode().getAttributesCount(); i++) {
                        final XmlDocumentItem attrNode = getXmlNode().getChildItems().get(i);
                        if (!namesFindInSchemeItems.contains(attrNode.getFullName())) {
                            childItems.add(new XmlModelItem(attrNode, null, this));
                        } else {
                            for (int j = startAttrIndex; j < attrItems.size(); j++) {
                                final XmlModelItem attrItem = attrItems.get(j);
                                childItems.add(attrItem);
                                if (attrItem.getXmlNode() != null && attrNode.getFullName().equals(attrItem.getXmlNode().getFullName())) {
                                    startAttrIndex = j + 1;
                                    break;
                                }
                            }
                        }
                    }
                    for (int j = startAttrIndex; j < attrItems.size(); j++) {
                        final XmlModelItem attrItem = attrItems.get(j);
                        childItems.add(attrItem);
                    }
                }
            }//if (schemaItem instanceof IXmlSchemaItemWithAttributes)
            if (schemaItem instanceof XmlSchemaElementsListItem) {
                XmlModelItem parentModelItem = null;
                for (XmlModelItem item = this; item != null; item = item.parent) {
                    if (item.xmlNode != null) {
                        parentModelItem = item;
                        break;
                    }
                }
                final XmlSchemaElementsListItem elementsListItem = (XmlSchemaElementsListItem) schemaItem;
                final XmlSchemaElementItem childSchemaItem
                        = new XmlSchemaElementItem(elementsListItem);
                final QName itemName = elementsListItem.getFullName();
                final List<XmlDocumentItem> invalidItems = new LinkedList<>();
                final List<XmlModelItem> childCandidates = new LinkedList<>();
                final XmlSchemaItem container = getNearestParentContainer();
                boolean found = false;
                int seq = 1;
                int childsCount = 0;
                int maxChilds = elementsListItem.getMaxElementsCount();
                for (XmlDocumentItem childDocumentItem : getChildItemsOfNearestParent()) {
                    if (childDocumentItem.getType() == EXmlItemType.Element) {
                        if (childDocumentItem.getFullName().equals(itemName)) {
                            found = true;
                            childsCount++;
                            if (!parentModelItem.existingItems.contains(childDocumentItem)) {
                                addModelItems(this, invalidItems, childCandidates);
                                if (maxChilds < 0 || childsCount <= maxChilds) {
                                    childCandidates.add(new XmlModelItem(childDocumentItem, childSchemaItem, this));
                                } else {
                                    childCandidates.add(new XmlModelItem(childDocumentItem, null, this));
                                }
                            }
                        } else if (found) {
                            if (findSchemaDeclaration(container.getChildItems(), childDocumentItem.getFullName()) == null) {
                                if (!parentModelItem.existingItems.contains(childDocumentItem)) {
                                    invalidItems.add(childDocumentItem);
                                }
                            } else {
                                if (seq == seqForListItem) {
                                    break;
                                } else {
                                    childCandidates.clear();
                                    found = false;
                                    seq++;
                                }
                            }
                        }
                    }
                }
                for (XmlModelItem childItem : childCandidates) {
                    childItems.add(childItem);
                    if (childItem.getXmlNode() != null) {
                        parentModelItem.existingItems.add(childItem.getXmlNode());
                    }
                }
            } else if (schemaItem instanceof XmlSchemaChoiceItem) {
                XmlModelItem parentModelItem = null;
                for (XmlModelItem item = this; item != null; item = item.parent) {
                    if (item.xmlNode != null) {
                        parentModelItem = item;
                        break;
                    }
                }
                final List<XmlSchemaItem> choiceItems = schemaItem.getChildItems();
                if (emptyContainer) {
                    for (XmlSchemaItem childSchemaItem : choiceItems) {
                        final XmlModelItem childModelItem
                                = new XmlModelItem(null, childSchemaItem, this);
                        childModelItem.emptyContainer = true;
                        childItems.add(childModelItem);
                        return childItems;
                    }
                }
                int bestMatchCount = 0;
                int bestMatchIndex = -1;
                int bestMatchStartIndex = -1;
                for (int i = 0, count = choiceItems.size(); i < count; i++) {
                    final List<QName> choiceSchemaElementsByOrder = getChildSchemaElementsByOrderRecursively(choiceItems.get(i));
                    int matchCounter = 0;
                    int lastIndexInOrder = -1;
                    int startIndex = -1;
                    int itemIndex = 0;
                    for (XmlDocumentItem childDocumentItem : parentModelItem.getXmlNode().getChildItems()) {
                        if (childDocumentItem.getType() == EXmlItemType.Element
                                && !parentModelItem.existingItems.contains(childDocumentItem)) {
                            final QName itemName = childDocumentItem.getFullName();
                            final int index = indexOf(choiceSchemaElementsByOrder, lastIndexInOrder + 1, itemName);
                            if (index > -1) {
                                if (startIndex < 0) {
                                    startIndex = itemIndex;
                                }
                                lastIndexInOrder = index;
                                matchCounter++;
                            }
                            itemIndex++;
                        }
                    }
                    if (bestMatchCount < matchCounter) {
                        bestMatchCount = matchCounter;
                        bestMatchIndex = i;
                        bestMatchStartIndex = startIndex;
                    }
                }
                indexForCurrentChoiceItem = bestMatchIndex;
                final XmlSchemaItem choiceSchemaItem = bestMatchIndex < 0 ? null : choiceItems.get(bestMatchIndex);
                final Map<QName, XmlSchemaItem> choiceVariantSchemaElementsByName;
                if (choiceSchemaItem == null
                        || choiceSchemaItem instanceof XmlSchemaElementItem
                        || choiceSchemaItem instanceof XmlSchemaElementsListItem) {
                    choiceVariantSchemaElementsByName = Collections.<QName, XmlSchemaItem>emptyMap();
                } else {
                    choiceVariantSchemaElementsByName = getChildSchemaElementsRecursively(choiceSchemaItem);
                }
                final Map<QName, XmlSchemaItem> choiceSchemaElementsByName = getChildSchemaElementsRecursively(schemaItem);
                final XmlSchemaItem parentSchemaItem = parentModelItem.getSchemaItem();
                final Map<QName, XmlSchemaItem> childSchemaElementsByName;
                if (parentSchemaItem instanceof XmlSchemaElementItem) {
                    childSchemaElementsByName = Collections.<QName, XmlSchemaItem>emptyMap();
                } else {
                    childSchemaElementsByName = getChildSchemaElementsRecursively(parentSchemaItem);
                }
                final List<XmlDocumentItem> invalidItems = new LinkedList<>();
                int i = 0;
                for (XmlDocumentItem childDocumentItem : parentModelItem.getXmlNode().getChildItems()) {
                    if (childDocumentItem.getType() == EXmlItemType.Element
                            && !parentModelItem.existingItems.contains(childDocumentItem)) {
                        final QName itemName = childDocumentItem.getFullName();
                        if (i == bestMatchStartIndex) {
                            if (!invalidItems.isEmpty()) {
                                parentModelItem.existingItems.addAll(invalidItems);
                                addModelItems(this, invalidItems, childItems);
                            }
                            for (XmlSchemaItem childSchemaItem : choiceItems) {
                                if (choiceSchemaItem == childSchemaItem) {
                                    if (choiceSchemaItem instanceof XmlSchemaElementItem) {
                                        childItems.add(new XmlModelItem(childDocumentItem, choiceSchemaItem, this));
                                        parentModelItem.existingItems.add(childDocumentItem);
                                    } else {
                                        childItems.add(new XmlModelItem(null, childSchemaItem, this));
                                    }
                                } else {
                                    final XmlModelItem choiceVariant
                                            = new XmlModelItem(null, childSchemaItem, this);
                                    choiceVariant.emptyContainer = true;
                                    childItems.add(choiceVariant);
                                }
                            }
                        } else if (i > bestMatchStartIndex && bestMatchStartIndex > -1) {
                            if (childSchemaElementsByName.isEmpty() || childSchemaElementsByName.containsKey(itemName)) {
                                if (!choiceVariantSchemaElementsByName.containsKey(itemName)) {
                                    if (choiceSchemaElementsByName.containsKey(itemName)) {
                                        //more then one variant in choice present
                                        invalidItems.add(childDocumentItem);
                                    }
                                } else {//else choice variant is container
                                    //invalid items must be processed in most inner container
                                    invalidItems.clear();
                                }
                            } else {
                                invalidItems.add(childDocumentItem);
                            }
                        } else if (i < bestMatchStartIndex && bestMatchStartIndex > -1) {
                            if (!childSchemaElementsByName.containsKey(itemName)) {
                                invalidItems.add(childDocumentItem);
                            }
                        }
                        i++;
                    }
                }
                if (!invalidItems.isEmpty()) {
                    parentModelItem.existingItems.addAll(invalidItems);
                    addModelItems(this, invalidItems, childItems);
                }
                if (indexForCurrentChoiceItem < 0) {
                    for (XmlSchemaItem childSchemaItem : schemaItem.getChildItems()) {
                        childItems.add(new XmlModelItem(null, childSchemaItem, this));
                    }
                }
            } else if (schemaItem instanceof XmlSchemaContainerItem) {
                if (parent.elementIsAnyType()) {
                    for (XmlDocumentItem childDocumentItem : getChildItemsOfNearestParent()) {
                        if (childDocumentItem.getType() == EXmlItemType.Element) {
                            final XmlSchemaElementItem elementType;
                            final String nameSpaceUri = childDocumentItem.getFullName().getNamespaceURI();
                            if (nameSpaceUri != null && !nameSpaceUri.isEmpty()) {
                                elementType = typify(childDocumentItem, nameSpaceUri);
                            } else {
                                elementType = null;
                            }
                            childItems.add(new XmlModelItem(childDocumentItem, elementType, this));
                        }
                    }
                } else {
                    if (emptyContainer) {
                        final List<XmlSchemaItem> choiceItems = schemaItem.getChildItems();
                        for (XmlSchemaItem childSchemaItem : choiceItems) {
                            final XmlModelItem childModelItem
                                    = new XmlModelItem(null, childSchemaItem, this);
                            childModelItem.emptyContainer = true;
                            childItems.add(childModelItem);
                            return childItems;
                        }
                    }
                    XmlModelItem parentModelItem = null;
                    for (XmlModelItem item = this; item != null; item = item.parent) {
                        if (item.xmlNode != null) {
                            parentModelItem = item;
                            break;
                        }
                    }
                    final Map<QName, XmlSchemaItem> childSchemaElementsByName = getChildSchemaElements(schemaItem);
                    final List<XmlDocumentItem> invalidItems = new LinkedList<>();
                    int containers_count = 0;
                    QName prevItemName = null;
                    for (XmlDocumentItem childDocumentItem : getChildItemsOfNearestParent()) {
                        if (childDocumentItem.getType() == EXmlItemType.Element
                                && !parentModelItem.existingItems.contains(childDocumentItem)) {
                            final QName itemName = childDocumentItem.getFullName();
                            final XmlSchemaItem childSchemaItem = childSchemaElementsByName.get(itemName);
                            if (prevItemName != null && prevItemName.equals(itemName)) {
                                if (childSchemaItem instanceof XmlSchemaElementsListItem) {
                                    invalidItems.clear();
                                } else {
                                    invalidItems.add(childDocumentItem);
                                }
                            } else if (childSchemaItem != null) {
                                containers_count = addChildSchemaElements(this, prevItemName, itemName, childItems);
                                if (containers_count == 0 && !invalidItems.isEmpty()) {
                                    parentModelItem.existingItems.addAll(invalidItems);
                                    addModelItems(this, invalidItems, childItems);
                                }
                                if (childSchemaItem instanceof XmlSchemaElementsListItem == false) {
                                    if (countOf(childSchemaItem, childItems) == 0) {
                                        final int indexInChildItems = findModelItemBySchemaItem(childItems, childSchemaItem);
                                        if (indexInChildItems > -1) {
                                            childItems.remove(indexInChildItems);
                                        }
                                        final XmlModelItem newItem = new XmlModelItem(childDocumentItem, childSchemaItem, this);
                                        childItems.add(newItem);
                                        parentModelItem.existingItems.add(childDocumentItem);
                                    } else {
                                        invalidItems.add(childDocumentItem);
                                    }
                                } else if (!itemName.equals(prevItemName)) {
                                    final XmlModelItem newItem = new XmlModelItem(null, childSchemaItem, this);
                                    newItem.seqForListItem = countOf(((XmlSchemaElementsListItem) childSchemaItem), childItems) + 1;
                                    childItems.add(newItem);
                                }
                                prevItemName = itemName;
                            } else {
                                if (findSchemaDeclaration(schemaItem.getChildItems(), itemName) == null) {
                                    invalidItems.add(childDocumentItem);
                                }else{
                                    //add this items in inner container
                                    invalidItems.clear();
                                }
                            }
                        }
                    }
                    if (!childSchemaElementsByName.isEmpty()) {
                        containers_count = addChildSchemaElements(this, prevItemName, null, childItems);
                    }
                    if (!invalidItems.isEmpty()) {
                        if (containers_count == 0) {
                            parentModelItem.existingItems.addAll(invalidItems);
                            addModelItems(this, invalidItems, childItems);
                        } else {
                            int remainErrEl = invalidItems.size();
                            final List<XmlDocumentItem> errorItems = new LinkedList<>();
                            final int countChildren = parentModelItem.getXmlNode().getChildCount();
                            for (XmlDocumentItem invalidItem : invalidItems) {
                                if (parentModelItem.getXmlNode().getIndexOfChild(invalidItem) == (countChildren - remainErrEl)) {
                                    errorItems.add(invalidItem);
                                    remainErrEl--;
                                }
                            }
                            parentModelItem.existingItems.addAll(errorItems);
                            addModelItems(this, errorItems, childItems);
                        }
                    }
                }
            } else if (schemaItem instanceof XmlSchemaElementItem) {
                if (isTypified()) {
                    int containers_count = addChildSchemaElements(this, null, null, childItems);
                    if (containers_count == 0 && xmlNode != null) {
                        for (XmlDocumentItem childDocumentItem : xmlNode.getChildItems()) {
                            if (childDocumentItem.getType() == EXmlItemType.Element) {
                                final XmlSchemaItem childSchemaItem
                                        = findSchemaDeclaration(schemaItem.getChildItems(), childDocumentItem.getFullName());
                                if (childSchemaItem instanceof XmlSchemaElementsListItem == false) {
                                    childItems.add(new XmlModelItem(childDocumentItem, null, this));
                                }
                            }
                        }
                    }
                } else if (xmlNode != null) {
                    if (elementIsAnyType()) {
                        childItems.add(new XmlModelItem(null, new XmlSchemaAnyTypeContainerItem(), this));
                    } else {
                        for (XmlDocumentItem childDocumentItem : xmlNode.getChildItems()) {
                            if (childDocumentItem.getType() == EXmlItemType.Element) {
                                childItems.add(new XmlModelItem(childDocumentItem, null, this));
                            }
                        }
                    }
                }
            }
        } else {
            for (XmlDocumentItem childDocumentItem : xmlNode.getChildItems()) {
                childItems.add(new XmlModelItem(childDocumentItem, null, this));
            }
        }
        return childItems;
    }

    private int findModelItemBySchemaItem(final List<XmlModelItem> modelItems, final XmlSchemaItem schemaItem) {
        for (int i = 0, count = modelItems.size(); i < count; i++) {
            if (modelItems.get(i).getSchemaItem() == schemaItem) {
                return i;
            }
        }
        return -1;
    }

    private static int countOf(final XmlSchemaItem item, final List<XmlModelItem> childItems) {
        int counter = 0;
        final QName name = (item != null && item instanceof XmlSchemaElementItem) ? ((XmlSchemaElementItem) item).getFullName() : ((XmlSchemaElementsListItem) item).getFullName();
        for (XmlModelItem child : childItems) {
            if (item == child.getSchemaItem()) {//NOPMD
                if ((item instanceof XmlSchemaElementItem && child.getXmlNode() != null && child.getXmlNode().getFullName().equals(name)) || item instanceof XmlSchemaElementsListItem) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public XmlModelItem findParentChoiceItem() {
        for (XmlModelItem parentItem = getParent(); parentItem != null; parentItem = parentItem.getParent()) {
            if (parentItem.getSchemaItem() instanceof XmlSchemaChoiceItem) {
                return parentItem;
            }
        }
        return null;
    }

    public int getIndexOfCurrentChoiceItem() {
        if (schemaItem instanceof XmlSchemaChoiceItem && indexForCurrentChoiceItem < 0) {
            getLogicalChildItems();
        }
        return indexForCurrentChoiceItem;
    }

    private static void addModelItems(final XmlModelItem parent, final List<XmlDocumentItem> from, final List<XmlModelItem> to) {
        for (XmlDocumentItem xmlItem : from) {
            to.add(new XmlModelItem(xmlItem, null, parent));
        }
        from.clear();
    }

    private static Map<QName, XmlSchemaItem> getChildSchemaElements(final XmlSchemaItem parent) {
        final Map<QName, XmlSchemaItem> result = new HashMap<>();
        final List<XmlSchemaItem> childSchemaItems = parent.getChildItems();
        for (XmlSchemaItem item : childSchemaItems) {
            if (item instanceof XmlSchemaElementItem) {
                result.put(((XmlSchemaElementItem) item).getFullName(), item);
            } else if (item instanceof XmlSchemaElementsListItem) {
                result.put(((XmlSchemaElementsListItem) item).getFullName(), item);
            }
        }
        return result;
    }

    private static Map<QName, XmlSchemaItem> getChildSchemaElementsRecursively(final XmlSchemaItem parent) {
        final Map<QName, XmlSchemaItem> result = new HashMap<>();
        final List<XmlSchemaItem> childSchemaItems = parent.getChildItems();
        for (XmlSchemaItem item : childSchemaItems) {
            if (item instanceof XmlSchemaElementItem) {
                result.put(((XmlSchemaElementItem) item).getFullName(), item);
            } else if (item instanceof XmlSchemaElementsListItem) {
                result.put(((XmlSchemaElementsListItem) item).getFullName(), item);
            } else if (item instanceof XmlSchemaContainerItem) {
                final Map<QName, XmlSchemaItem> children = getChildSchemaElementsRecursively(item);
                for (Map.Entry<QName, XmlSchemaItem> entry : children.entrySet()) {
                    result.put(entry.getKey(), item);
                }
            }
        }
        return result;
    }

    private static List<QName> getChildSchemaElementsByOrderRecursively(final XmlSchemaItem item) {
        final ArrayList<QName> result = new ArrayList<>();
        if (item instanceof XmlSchemaElementItem) {
            result.add(((XmlSchemaElementItem) item).getFullName());
        } else if (item instanceof XmlSchemaElementsListItem) {
            result.add(((XmlSchemaElementsListItem) item).getFullName());
        } else if (item instanceof XmlSchemaContainerItem) {
            for (XmlSchemaItem child : item.getChildItems()) {
                result.addAll(getChildSchemaElementsByOrderRecursively(child));
            }
        }
        return result;
    }

    private static int indexOf(final List<QName> array, final int startFrom, final QName name) {
        for (int i = startFrom, count = array.size(); i < count; i++) {
            if (name.equals(array.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private static int addChildSchemaElements(final XmlModelItem parentModelItem,
            final QName from, final QName to,
            final List<XmlModelItem> dest) {
        int containers_count = 0;
        final List<XmlSchemaItem> childSchemaItems = parentModelItem.schemaItem.getChildItems();
        final int startIndex = from == null ? 0 : findChildSchemaElementIndexByName(childSchemaItems, from) + 1;
        final int endIndex = to == null ? childSchemaItems.size() : findChildSchemaElementIndexByName(childSchemaItems, to);
        for (int i = startIndex; i < endIndex; i++) {
            if (childSchemaItems.get(i) instanceof XmlSchemaContainerItem
                    && childSchemaItems.get(i) instanceof XmlSchemaElementsListItem == false) {
                containers_count++;
                dest.add(new XmlModelItem(null, childSchemaItems.get(i), parentModelItem));
            } else if (childSchemaItems.get(i) instanceof XmlSchemaElementItem
                    || childSchemaItems.get(i) instanceof XmlSchemaElementsListItem) {
                dest.add(new XmlModelItem(null, childSchemaItems.get(i), parentModelItem));
            }
        }
        return containers_count;
    }

    private static int findChildSchemaElementIndexByName(final List<XmlSchemaItem> childSchemaItems,
            final QName name) {
        for (int i = 0; i < childSchemaItems.size(); i++) {
            if (childSchemaItems.get(i) instanceof XmlSchemaElementItem) {
                final XmlSchemaElementItem item = (XmlSchemaElementItem) childSchemaItems.get(i);
                if (name.equals(item.getFullName())) {
                    return i;
                }
            } else if (childSchemaItems.get(i) instanceof XmlSchemaElementsListItem) {
                final XmlSchemaElementsListItem item = (XmlSchemaElementsListItem) childSchemaItems.get(i);
                if (name.equals(item.getFullName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static XmlSchemaItem findSchemaDeclaration(final List<XmlSchemaItem> schemaItems, final QName element) {
        for (XmlSchemaItem item : schemaItems) {
            if (item instanceof XmlSchemaElementsListItem
                    && ((XmlSchemaElementsListItem) item).getFullName().equals(element)) {
                return item;
            } else if (item instanceof XmlSchemaElementItem
                    && ((XmlSchemaElementItem) item).getFullName().equals(element)) {
                return item;
            } else if (item instanceof XmlSchemaContainerItem) {
                final XmlSchemaItem innerItem = findSchemaDeclaration(item.getChildItems(), element);
                if (innerItem != null) {
                    return innerItem;
                }
            }
        }
        return null;
    }

    public List<QName> getExistingAttributes() {
        final List<QName> attributes = new ArrayList<>();
        if (xmlNode != null) {
            for (XmlDocumentItem item : xmlNode.getChildItems()) {
                if (item.getType() == EXmlItemType.Attribute) {
                    attributes.add(item.getFullName());
                }
            }
        }
        return attributes;
    }

    public List<XmlSchemaAttributeItem> getPossibleAttributes() {
        final List<XmlSchemaAttributeItem> attributes = new ArrayList<>();
        if (schemaItem instanceof XmlSchemaElementItem) {
            final List<QName> existingAttrs = getExistingAttributes();
            final List<XmlSchemaAttributeItem> allowedAttributes
                    = ((XmlSchemaElementItem) schemaItem).getAttributes();
            for (XmlSchemaAttributeItem attribute : allowedAttributes) {
                if (!existingAttrs.contains(attribute.getFullName())) {
                    attributes.add(attribute);
                }
            }
        }
        return attributes;
    }

    public List<QName> getExistingElements() {
        final List<QName> elements = new ArrayList<>();
        for (XmlModelItem items : getVisibleChildItems()) {
            XmlDocumentItem item = items.getXmlNode();
            if (item.getType() == EXmlItemType.Element) {
                elements.add(item.getFullName());
            }
        }
        return elements;
    }

    public List<XmlSchemaElementItem> getPossibleElements() {
        final List<XmlSchemaElementItem> elements = new ArrayList<>();
        final List<XmlSchemaElementItem> choiceElements = new ArrayList<>();
        final List<XmlModelItem> childItems = new ArrayList<>();
        final List<QName> namesAddedElements = new ArrayList<>();
        childItems.addAll(getLogicalChildItems());
        while (!childItems.isEmpty()) {
            final XmlModelItem childItem = childItems.remove(0);
            if (childItem.getSchemaItem() instanceof XmlSchemaElementItem && childItem.getXmlNode() == null
                    && ((XmlSchemaElementItem) childItem.getSchemaItem()).getMaxElementsCount() > getCountElementInXml(((XmlSchemaElementItem) childItem.getSchemaItem()).getFullName())
                    && !namesAddedElements.contains(((XmlSchemaElementItem) childItem.getSchemaItem()).getFullName())) {
                elements.add((XmlSchemaElementItem) childItem.getSchemaItem());
                namesAddedElements.add(((XmlSchemaElementItem) childItem.getSchemaItem()).getFullName());
            } else if (childItem.getSchemaItem() instanceof XmlSchemaElementsListItem) {
                final XmlSchemaElementsListItem itemsList = (XmlSchemaElementsListItem) childItem.getSchemaItem();
                final int maxItemsCount = itemsList.getMaxElementsCount();
                if (maxItemsCount < 0) {
                    elements.add(new XmlSchemaElementItem(itemsList));
                } else {
                    int existingItemsCount = childItem.getExistingElements().size();
                    if (existingItemsCount < maxItemsCount) {
                        elements.add(new XmlSchemaElementItem(itemsList));
                    }
                }
            } else if (childItem.getSchemaItem() instanceof XmlSchemaContainerItem) {
                if (childItem.getSchemaItem() instanceof XmlSchemaChoiceItem == false) {
                    childItems.addAll(childItem.getLogicalChildItems());
                } else {
                    if (!childItem.getPossibleElementsInChoice().isEmpty()) {
                        choiceElements.addAll(childItem.getPossibleElementsInChoice());
                        for (XmlSchemaElementItem item : choiceElements) {
                            if (!getExistingElements().contains(item.getFullName())) {
                                elements.add(item);
                            }
                        }
                    }
                }
            }//else childItem.getSchemaItem() is null or XmlSchemaAttributeItem
        }
        return elements;
    }

    public List<XmlSchemaElementItem> getPossibleElementsInChoice() {
        final List<XmlSchemaElementItem> elements = new ArrayList<>();
        int indexChoice = 0;
        for (XmlSchemaItem childItemInChoice : schemaItem.getChildItems()) {
            if ((childItemInChoice instanceof XmlSchemaElementItem || childItemInChoice instanceof XmlSchemaElementsListItem)
                    && indexChoice == indexForCurrentChoiceItem) {
                break;
            } else if (childItemInChoice instanceof XmlSchemaContainerItem && indexChoice == indexForCurrentChoiceItem) {
                for (XmlSchemaItem item : childItemInChoice.getChildItems()) {
                    if (item instanceof XmlSchemaElementItem) {
                        elements.add((XmlSchemaElementItem) item);
                    }
                }
            } else if (indexForCurrentChoiceItem == -1) {
                if (childItemInChoice instanceof XmlSchemaElementItem || childItemInChoice instanceof XmlSchemaElementsListItem) {
                    elements.add((XmlSchemaElementItem) childItemInChoice);
                } else {
                    for (XmlSchemaItem item : childItemInChoice.getChildItems()) {
                        if (item instanceof XmlSchemaElementItem) {
                            elements.add((XmlSchemaElementItem) item);
                        }
                    }
                }
            }
            indexChoice++;
        }
        return elements;
    }

    public int createChildElement(final QName name, final String value, final int index) {
        final int actualIndex;
        if (index == -1 && schemaItem != null) {
            actualIndex = calcChildElementIndex(name);
        } else {
            actualIndex = index;
        }
        findDocumentItem().createChildElement(name, value, actualIndex);
        clearCache();
        return getIndexInTreeByElementIndex(actualIndex);
    }

    private int calcChildElementIndex(final QName name) {
        int actualIndex = 0;
        List<XmlModelItem> childItems = new ArrayList<>(getLogicalChildItems());
        while (!childItems.isEmpty()) {
            final XmlModelItem childItem = childItems.remove(0);
            if (childItem.xmlNode != null
                    && childItem.xmlNode.getType() == EXmlItemType.Element) {
                actualIndex++;
            }
            if (childItem.getSchemaItem() instanceof IXmlSchemaNamedItem
                    && name.equals(((IXmlSchemaNamedItem) childItem.getSchemaItem()).getFullName())) {
                if (childItem.getSchemaItem() instanceof XmlSchemaElementsListItem) {
                    actualIndex += childItem.getLogicalChildItems().size();
                }
                break;
            }
            if (childItem.getSchemaItem() instanceof XmlSchemaContainerItem) {
                childItems.addAll(0, childItem.getLogicalChildItems());

            }
        }
        return actualIndex;
    }

    public int createChildElement(final QName name, final String value) {
        return createChildElement(name, value, -1);
    }

    public int createChildAttribute(final QName name, final String value, final int index) {
        int actualIndex = index;
        if (index == -1 && schemaItem != null) {
            actualIndex = 0;
            for (XmlModelItem childItem : getLogicalChildItems()) {
                if (childItem.xmlNode != null
                        && childItem.xmlNode.getType() == EXmlItemType.Attribute) {
                    actualIndex++;
                }
                if (childItem.getSchemaItem() instanceof XmlSchemaAttributeItem
                        && name.equals(((XmlSchemaAttributeItem) childItem.getSchemaItem()).getFullName())) {
                    break;
                }
            }
        }
        findDocumentItem().createAttribute(name, value, actualIndex);
        clearCache();
        return getAttributeItemByIndex(actualIndex);
    }

    public int createChildAttribute(final QName name, final String value) {
        if ("nil".equals(name.getLocalPart())) {
            return createChildAttribute(new QName("", name.getLocalPart(), "xsi"), value, -1);
        } else {
            clearCache();
            return createChildAttribute(name, value, -1);
        }
    }

    public List<XmlModelItem> getVisibleChildItems() {
        final List<XmlModelItem> visibleChildItems = new ArrayList<>();
        final List<XmlModelItem> childItems = new ArrayList<>(getLogicalChildItems());
        while (!childItems.isEmpty()) {
            final XmlModelItem childItem = childItems.remove(0);
            if (childItem.xmlNode == null) {
                final List<XmlModelItem> logicalChilds = childItem.getLogicalChildItems();
                if (!logicalChilds.isEmpty()) {
                    childItems.addAll(0, logicalChilds);
                }
            } else if (childItem.xmlNode.getType() == EXmlItemType.Attribute
                    && XmlDocumentItem.XSI_NIL.equals(childItem.xmlNode.getFullName())) {
                continue;
            } else {
                if (!visibleChildItems.contains(childItem)) {
                    visibleChildItems.add(childItem);
                }
            }
        }
        return visibleChildItems;
    }

    private int getCountElementInXml(final QName name) {
        int countName = 0;
        for (XmlModelItem visibleElements : getVisibleChildItems()) {
            if ((visibleElements.getSchemaItem() instanceof XmlSchemaElementItem && ((XmlSchemaElementItem) visibleElements.getSchemaItem()).getFullName().equals(name))
                    || (visibleElements.getSchemaItem() == null && visibleElements.getXmlNode() != null && visibleElements.getXmlNode().getFullName().equals(name))) {
                countName++;
            }
        }
        return countName;
    }

    private XmlDocumentItem findDocumentItem() {
        for (XmlModelItem item = this; item != null; item = item.parent) {
            if (item.xmlNode != null) {
                return item.xmlNode;
            }
        }
        return null;
    }

    private List<XmlDocumentItem> getChildItemsOfNearestParent() {
        for (XmlModelItem item = this; item != null; item = item.parent) {
            if (item.schemaItem instanceof XmlSchemaContainerItem == false) {
                if (item.xmlNode == null) {
                    return Collections.<XmlDocumentItem>emptyList();
                } else {
                    return item.xmlNode.getChildItems();
                }
            }
        }
        return Collections.<XmlDocumentItem>emptyList();
    }

    private XmlSchemaItem getNearestParentContainer() {
        for (XmlModelItem item = this.getParent(); item != null; item = item.parent) {
            if (item.schemaItem instanceof XmlSchemaContainerItem) {
                return item.schemaItem;
            }
        }
        return null;
    }

    private List<XmlDocumentItem> findLogicalChildItemByQName(final QName name) {
        final List<XmlDocumentItem> childDocumentItems = new ArrayList<>();
        for (XmlDocumentItem childItem : findDocumentItem().getChildItems()) {
            if (Objects.equals(name, childItem.getFullName())) {
                childDocumentItems.add(childItem);
            }
        }
        if (childDocumentItems.isEmpty()) {
            return Collections.emptyList();
        } else {
            return childDocumentItems;
        }
    }

    private int getAttributeItemByIndex(final int index) {
        final List<XmlModelItem> childItems = getVisibleChildItems();
        int counter = 0;
        for (XmlModelItem childItem : childItems) {
            if (childItem.getXmlNode().getType() == EXmlItemType.Attribute) {
                if (counter == index) {
                    return counter;
                } else {
                    counter++;
                }
            }
        }
        return index == -1 ? counter - 1 : -1;
    }

    private int getIndexInTreeByElementIndex(final int index) {
        final List<XmlModelItem> childItems = getVisibleChildItems();
        int itemsCounter = 0;
        int elementsCounter = 0;
        for (XmlModelItem childItem : childItems) {
            if (childItem.getXmlNode().getType() == EXmlItemType.Element) {
                if (elementsCounter == index) {
                    return itemsCounter;
                } else {
                    elementsCounter++;
                }
            }
            itemsCounter++;
        }
        if (index == -1) {
            return elementsCounter > 0 ? itemsCounter - 1 : -1;
        } else {
            return -1;
        }
    }

    public XmlDocumentItem getXmlNode() {
        return xmlNode;
    }

    public XmlSchemaItem getSchemaItem() {
        return schemaItem;
    }

    public XmlModelItem getParent() {
        return parent;
    }

    public boolean elementIsAnyType() {
        return (getSchemaItem() instanceof XmlSchemaElementsListItem && ((XmlSchemaElementsListItem) getSchemaItem()).isAnyType())
                || (getSchemaItem() instanceof XmlSchemaElementItem && ((XmlSchemaElementItem) getSchemaItem()).isAnyType())
                || (getSchemaItem() instanceof XmlSchemaAnyTypeContainerItem);
    }

    public boolean isValid() {
        XmlModelItem ancestor = parent;
        while (ancestor != null) {
            if (schemaItem == null && ancestor.getSchemaItem() != null && xmlNode != null) {
                return ancestor.elementIsAnyType();
            }
            ancestor = ancestor.getParent();
        }
        return true;
    }

    private boolean isTypified() {
        return schemaItem != null && !elementIsAnyType();
    }

    public ITextOptions getNameTextOptions(final IClientEnvironment environment, final boolean isReadOnly) {
        final EnumSet<ETextOptionsMarker> markers = EnumSet.of(ETextOptionsMarker.EDITOR);
        if (!isValid() || !isConformity()) {
            markers.add(ETextOptionsMarker.INVALID_VALUE);
        }
        if (isReadOnly) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
            getValueTextOptions(environment, isReadOnly);
        }
        return environment.getTextOptionsProvider().getOptions(markers, null);
    }

    public ITextOptions getValueTextOptions(final IClientEnvironment environment, final boolean isReadOnly) {
        final EnumSet<ETextOptionsMarker> markers = EnumSet.of(ETextOptionsMarker.EDITOR);
        if (isReadOnly) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        return environment.getTextOptionsProvider().getOptions(markers, null);
    }

    public boolean canCreateChild() {
        return canCreate(0);
    }

    private boolean canCreate(final int attrOrElem) {
        if (isTypified()) {
            if (isValid() && xmlNode.getType() == EXmlItemType.Element && isConformity()) {
                if (attrOrElem == 1) {//attribute
                    return !getPossibleAttributes().isEmpty();
                } else {//container item or child element
                    return !getPossibleElements().isEmpty();
                }
            } else {
                return false;
            }
        } else {
            return (isValid() && xmlNode.getType() == EXmlItemType.Element && isConformity());
        }
    }

    public boolean canCreateAttribute() {
        return canCreate(1);
    }

    public boolean canRename() {
        return isValid() && isConformity()
                && (schemaItem == null
                || (getParent() != null
                && !elementIsAnyType()
                && xmlNode.getChildItems().isEmpty()
                && xmlNode.getType() == EXmlItemType.Element
                && !getParent().getPossibleElements().isEmpty()
                && !canChoiceScheme()));
    }

    public boolean canDelete() {
        if (parent != null && isValid() && schemaItem != null && isConformity()) {
            if (parent.getSchemaItem() instanceof XmlSchemaElementsListItem) {
                XmlSchemaElementsListItem sc = (XmlSchemaElementsListItem) parent.getSchemaItem();
                return !(sc.getMinElementsCount() > 0 && sc.childWithEqualName(this) <= sc.getMinElementsCount());
            } else if (getSchemaItem() instanceof XmlSchemaAttributeItem) {
                XmlSchemaAttributeItem sc = (XmlSchemaAttributeItem) getSchemaItem();
                return !sc.usedAttribute();
            } else if (getSchemaItem() instanceof XmlSchemaElementItem) {
                XmlSchemaElementItem sc = (XmlSchemaElementItem) getSchemaItem();
                return !(parent.getExistingElements().contains(sc.getFullName()) && parent.getCountElementInXml(sc.getFullName()) <= sc.getMinElementsCount());
            }
        }
        return true;
    }

    public boolean canMoveUp() {
        if (parent != null && isValid() && isConformity()) {
            if (findParentChoiceItem() == null && (getSchemaItem() == null
                    || getSchemaItem() instanceof XmlSchemaAttributeItem != false || !((XmlSchemaContainerItem) parent.getSchemaItem()).isSequence())) {
                return isCorrectMoveUp();
            } else if (parent.getSchemaItem() != null && parent.getSchemaItem() instanceof XmlSchemaContainerItem && ((XmlSchemaContainerItem) parent.getSchemaItem()).isSequence()) {
                return !isCorrectItemInSequence() ? isCorrectMoveUp() : false;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean isCorrectMoveUp() {
        if (parent.getXmlNode() != null) {
            if ((parent.getXmlNode().getChildCount() == 1 && xmlNode.getType() == EXmlItemType.Element)
                    || (parent.getXmlNode().getAttributesCount() == 1) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if ((parent.getXmlNode().getIndexOfChild(xmlNode) == (parent.getXmlNode().getChildCount() - 1) && xmlNode.getType() == EXmlItemType.Element)
                    || (parent.getXmlNode().getIndexOfChild(xmlNode) == (parent.getXmlNode().getAttributesCount() - 1)) && xmlNode.getType() == EXmlItemType.Attribute) {
                return true;
            } else if (parent.getXmlNode().getIndexOfChild(xmlNode) == 0) {
                return false;
            }
            return true;
        } else {
            if ((xmlNode.getParent().getChildCount() == 1 && xmlNode.getType() == EXmlItemType.Element) || (xmlNode.getParent().getAttributesCount() == 1) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if ((xmlNode.getParent().getIndexOfChild(xmlNode) == xmlNode.getParent().getChildCount() && xmlNode.getType() == EXmlItemType.Element)
                    || (xmlNode.getParent().getIndexOfChild(xmlNode) == xmlNode.getParent().getAttributesCount()) && xmlNode.getType() == EXmlItemType.Attribute) {
                return true;
            } else if (xmlNode.getParent().getIndexOfChild(xmlNode) == 0) {
                return false;
            }
            return true;
        }
    }

    public boolean canMoveDown() {
        if (parent != null && isValid() && isConformity()) {
            if (findParentChoiceItem() == null && (getSchemaItem() == null
                    || getSchemaItem() instanceof XmlSchemaAttributeItem != false || !((XmlSchemaContainerItem) parent.getSchemaItem()).isSequence())) {
                return isCorrectMoveDown();
            } else if (parent.getSchemaItem() != null && parent.getSchemaItem() instanceof XmlSchemaContainerItem && ((XmlSchemaContainerItem) parent.getSchemaItem()).isSequence()) {
                return !isCorrectItemInSequence() ? !isCorrectMoveUp() : false;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean isCorrectMoveDown() {
        if (parent.getXmlNode() != null) {
            if ((parent.getXmlNode().getChildCount() == 1 && xmlNode.getType() == EXmlItemType.Element)
                    || (parent.getXmlNode().getAttributesCount() == 1) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if ((parent.getXmlNode().getIndexOfChild(xmlNode) == (parent.getXmlNode().getChildCount() - 1) && xmlNode.getType() == EXmlItemType.Element)
                    || (parent.getXmlNode().getIndexOfChild(xmlNode) == (parent.getXmlNode().getAttributesCount() - 1)) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if (parent.getXmlNode().getIndexOfChild(xmlNode) == 0) {
                return true;
            }
            return true;
        } else {
            if ((xmlNode.getParent().getChildCount() == 1 && xmlNode.getType() == EXmlItemType.Element)
                    || (xmlNode.getParent().getAttributesCount() == 1) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if (((xmlNode.getParent().getIndexOfChild(xmlNode) == xmlNode.getParent().getChildCount() - 1) && xmlNode.getType() == EXmlItemType.Element)
                    || (xmlNode.getParent().getIndexOfChild(xmlNode) == xmlNode.getParent().getAttributesCount() - 1) && xmlNode.getType() == EXmlItemType.Attribute) {
                return false;
            } else if (xmlNode.getParent().getIndexOfChild(xmlNode) == 0) {
                return true;
            }
            return true;
        }
    }

    public boolean isCorrectItemInSequence() {
        if (parent.schemaItem != null && parent.schemaItem instanceof XmlSchemaContainerItem && parent.schemaItem instanceof XmlSchemaChoiceItem == false) {
            final QName itemName = getXmlNode().getFullName();
            if (schemaItem == null) {
                return false;
            }
            int itemIndex = parent.schemaItem.getChildItems().indexOf(schemaItem);
            int indexSchemaItem = 0;
            int lastIndexSchemaItem = -1;
            for (QName existName : parent.getExistingElements()) {
                for (int j = 0, c = parent.schemaItem.getChildItems().size(); j < c; j++) {
                    XmlSchemaItem itemSequence = parent.schemaItem.getChildItems().get(j);
                    if (!itemName.equals(existName)) {
                        if (itemSequence instanceof XmlSchemaElementItem
                                && ((XmlSchemaElementItem) itemSequence).getFullName().equals(existName)) {
                            lastIndexSchemaItem = indexSchemaItem;
                            indexSchemaItem = j;
                        } else if (itemSequence instanceof XmlSchemaElementsListItem
                                && ((XmlSchemaElementsListItem) itemSequence).getFullName().equals(existName)) {
                            lastIndexSchemaItem = indexSchemaItem;
                            indexSchemaItem = j;
                        }
                    } else {
                        return (lastIndexSchemaItem == -1 || itemIndex > indexSchemaItem);
                    }
                }
            }
        }
        return true;
    }

    public boolean isCorrectlySequence() {
        if (schemaItem != null && schemaItem instanceof XmlSchemaContainerItem && schemaItem instanceof XmlSchemaChoiceItem == false) {
            int indexInSchemaLast = 0;
            for (QName existName : getExistingElements()) {
                int indexInSchema = 0;
                for (XmlSchemaItem item : schemaItem.getChildItems()) {
                    if (item instanceof XmlSchemaElementItem && ((XmlSchemaElementItem) item).getFullName().equals(existName)) {
                        if (indexInSchemaLast > indexInSchema) {
                            return false;
                        }
                        indexInSchemaLast = indexInSchema;
                        break;
                    } else if (item instanceof XmlSchemaElementsListItem && ((XmlSchemaElementsListItem) item).getFullName().equals(existName)) {
                        if (indexInSchemaLast > indexInSchema) {
                            return false;
                        }
                        indexInSchemaLast = indexInSchema;
                        break;
                    }
                    indexInSchema++;
                }
            }
        }
        return true;
    }

    public boolean canChoiceScheme() {
        if (schemaItem != null) {
            if (parent != null) {
                XmlModelItem ancestor = parent;
                int level = 0;
                while (ancestor != null && level < 2) {
                    if (ancestor.getSchemaItem() instanceof XmlSchemaChoiceItem) {
                        return true;
                    }
                    ancestor = ancestor.getParent();
                    level++;
                }
            }
        }
        return false;
    }

    public boolean canPasteItem(final QName nameClipboard, final boolean isAttribute) {
        if (xmlNode.getType() == EXmlItemType.Element) {
            if (isAttribute) {
                return !getExistingAttributes().contains(nameClipboard) ? canCreateAttribute() : false;
            } else if (schemaItem != null) {
                if ((schemaItem instanceof XmlSchemaElementsListItem && !(((XmlSchemaElementsListItem) schemaItem).isAnyType())) || (schemaItem instanceof XmlSchemaElementItem && !(((XmlSchemaElementItem) schemaItem).isAnyType()))) {
                    List<QName> possibleNames = new LinkedList<>();
                    for (XmlSchemaElementItem item : getPossibleElements()) {
                        possibleNames.add(item.getFullName());
                    }
                    return canCreateChild() && !possibleNames.isEmpty() && possibleNames.contains(nameClipboard);
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public XmlValueEditingOptions getValueEditingOptions(final IXmlValueEditingOptionsProvider provider) {
        if (xmlNode == null) {
            if (getSchemaItem() == null) {
                return provider.getEditingOptions(null, null, false, true);
            } else {
                return isTypified() ? getSchemaItem().getValueEditingOptions(provider)
                        : provider.getEditingOptions(null, null, false, true);
            }
        } else if (isValid() && isConformity()) {
            return getSchemaItem() == null ? provider.getEditingOptions(null, null, false, true)
                    : getSchemaItem().getValueEditingOptions(provider);
        } else {
            return provider.getEditingOptions(null, null, true, true);
        }
    }

    public boolean isXmlNodeCanHaveValue() {
        if (xmlNode == null) {
            return false;
        } else if (isValid() && isConformity()) {
            return isTypified() ? getSchemaItem().isValueTypeDefined() : true;
        } else {
            return true;
        }
    }

    public void setConformity(final boolean isConform) {
        this.isConform = isConform;
    }

    public boolean isConformity() {
        XmlModelItem item = this;
        for (XmlModelItem parentItem = this; parentItem.getParent() != null; parentItem = parentItem.getParent()) {
            item = parentItem.getParent();
        }
        return item.isConform;
    }

    public void clearCache() {
        existingItems.clear();
        logicalChildsCache = null;
    }
}

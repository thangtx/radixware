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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import org.radixware.kernel.common.utils.namefilter.NameMatcher;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListDataListener;
import org.netbeans.spi.jumpto.type.SearchType;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.namefilter.NameFilterUtils;

/**
 * Choose Radix object list model.
 */
class DefinitionsPanelModel implements ListModel {

    private final List<RadixObject> displayedObjects;
    protected DefinitionsPanelModel() {
        this.displayedObjects = Collections.emptyList();
    }

    protected DefinitionsPanelModel(IChooseDefinitionAdditionTextProvider additionTextProvider,final Collection<? extends RadixObject> availableObjects, final String text, final boolean caseSensitive, EChooseDefinitionDisplayMode displayMode) {
        Collection<? extends RadixObject> filteredObjects = filter(additionTextProvider, availableObjects, text, caseSensitive, displayMode);
        this.displayedObjects = new ArrayList<RadixObject>(filteredObjects);
        if (displayMode == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) {
            RadixObjectsUtils.sortByName(this.displayedObjects);
        } else {
            RadixObjectsUtils.sortByQualifiedName(displayedObjects);
        }
    }

//    private static Collection<? extends RadixObject> filter(final Collection<? extends RadixObject> availableObjects, String text, final boolean caseSensitive, EChooseDefinitionDisplayMode displayMode) {
//
//        if (text == null || text.isEmpty()) {
//            return availableObjects;
//        }
//
//        final boolean exact = text.endsWith(" ");
//
//        text = text.trim();
//
//        if (text.isEmpty()) {
//            return availableObjects;
//        }
//
//        final SearchType searchKind;
//
//        /*
//
//        if (!exact && ((isAllUpper(text) && text.length() > 1) || isCamelCase(text))) {
//        searchKind = SearchType.CAMEL_CASE;
//        } else if (containsWildCard(text) != -1) {
//        text = transformWildCardsToJavaStyle(text);
//        searchKind = caseSensitive ? SearchType.REGEXP : SearchType.CASE_INSENSITIVE_REGEXP;
//        } else if (exact) {
//        searchKind = caseSensitive ? SearchType.EXACT_NAME : SearchType.CASE_INSENSITIVE_EXACT_NAME;
//        } else {
//        searchKind = caseSensitive ? SearchType.PREFIX : SearchType.CASE_INSENSITIVE_PREFIX;
//        }
//         * */
//
//        if (exact) {
//            searchKind = SearchType.EXACT_NAME;
//        } else if ((isAllUpper(text) && text.length() > 1) || isCamelCase(text)) {
//            searchKind = SearchType.CAMEL_CASE;
//        } else if (containsWildCard(text) != -1) {
//            text = transformWildCardsToJavaStyle(text);
//            searchKind = caseSensitive ? SearchType.REGEXP : SearchType.CASE_INSENSITIVE_REGEXP;
//        } else {
//            searchKind = caseSensitive ? SearchType.PREFIX : SearchType.CASE_INSENSITIVE_PREFIX;
//        }
//
//        final NameMatcher matcher = NameMatcherFactory.createNameMatcher(text, searchKind);
//        if (matcher == null) {
//            return Collections.emptyList();
//        }
//
//        final List<RadixObject> result = new ArrayList<RadixObject>(availableObjects.size());
//        for (RadixObject radixObject : availableObjects) {
//            final String name;
//            if (displayMode == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) {
//                name = radixObject.getName();
//            } else {
//                name = radixObject.getQualifiedName();
//            }
//            if (matcher.accept(name)) {
//                result.add(radixObject);
//            }
//        }
//        return result;
//    }
    private static Collection<? extends RadixObject> filter(final IChooseDefinitionAdditionTextProvider additionTextProvider, final Collection<? extends RadixObject> availableObjects, String text, final boolean caseSensitive, EChooseDefinitionDisplayMode displayMode) {

        if (text == null || text.isEmpty()) {
            return availableObjects;
        }

        final boolean exact = text.endsWith(" ");

        text = text.trim();

        if (text.isEmpty()) {
            return availableObjects;
        }

        final SearchType searchKind;

        if (exact) {
            searchKind = (caseSensitive ? SearchType.EXACT_NAME : SearchType.CASE_INSENSITIVE_EXACT_NAME);
        } else if (NameFilterUtils.isAllUpper(text) && text.length() > 1) {
            searchKind = SearchType.CAMEL_CASE;
        } else if (NameFilterUtils.containsWildCard(text) != -1) {
            text = NameFilterUtils.transformWildCardsToJavaStyle(text);
            searchKind = caseSensitive ? SearchType.REGEXP : SearchType.CASE_INSENSITIVE_REGEXP;
        } else {
            searchKind = caseSensitive ? SearchType.PREFIX : SearchType.CASE_INSENSITIVE_PREFIX;
        }

        final NameMatcher matcher = DefinitionsNameMatcherFactory.createNameMatcher(text, SearchTypeConverter.convertNb2RdxSearchType(searchKind), displayMode);
        if (matcher == null) {
            return Collections.emptyList();
        }

        final List<RadixObject> result = new ArrayList<RadixObject>(availableObjects.size());
        for (RadixObject radixObject : availableObjects) {
            final String name;
            if (displayMode == EChooseDefinitionDisplayMode.NAME_AND_LOCATION) {
                name = radixObject.getName();
            } else {
                name = radixObject.getQualifiedName();
            }
            if (matcher.accept(name)) {
                result.add(radixObject);
            }
        }
        if (additionTextProvider!=null)
            return additionTextProvider.filter(result, text, caseSensitive, displayMode);
        return result;
    }

    public static boolean isAllUpper(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isUpperCase(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }
    
    @Override
    public RadixObject getElementAt(int index) {
        return displayedObjects.get(index);
    }

    @Override
    public int getSize() {
        return displayedObjects.size();
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}

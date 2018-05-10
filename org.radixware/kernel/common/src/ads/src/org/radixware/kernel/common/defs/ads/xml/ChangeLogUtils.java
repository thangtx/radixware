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

package org.radixware.kernel.common.defs.ads.xml;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;

/**
 *
 * @author dlastochkin
 */
public class ChangeLogUtils {
    private static final Set<EIsoLanguage> SUPPORTED_LANGUAGES = new HashSet<>();
    static {
        SUPPORTED_LANGUAGES.add(EIsoLanguage.ENGLISH);
        SUPPORTED_LANGUAGES.add(EIsoLanguage.RUSSIAN);
    }
    
    private static final AdsMultilingualStringDef EDITED_FIELD_TEMPLATE = AdsMultilingualStringDef.Factory.newInstance();
    private static final AdsMultilingualStringDef EDITED_FIELDS_TEMPLATE = AdsMultilingualStringDef.Factory.newInstance();
    
    private static final AdsMultilingualStringDef REMOVED_MLS = AdsMultilingualStringDef.Factory.newInstance();
    private static final AdsMultilingualStringDef ADDED_MLS = AdsMultilingualStringDef.Factory.newInstance();
    static {
        EDITED_FIELD_TEMPLATE.setValue(EIsoLanguage.ENGLISH, "Field {0}: {1}");
        EDITED_FIELD_TEMPLATE.setValue(EIsoLanguage.RUSSIAN, "{0}о поле: {1}");
        
        EDITED_FIELDS_TEMPLATE.setValue(EIsoLanguage.ENGLISH, "Fields {0}: {1}");
        EDITED_FIELDS_TEMPLATE.setValue(EIsoLanguage.RUSSIAN, "{0}ы поля: {1}");
        
        REMOVED_MLS.setValue(EIsoLanguage.ENGLISH, "removed");
        REMOVED_MLS.setValue(EIsoLanguage.RUSSIAN, "Удален");
        
        ADDED_MLS.setValue(EIsoLanguage.ENGLISH, "added");
        ADDED_MLS.setValue(EIsoLanguage.RUSSIAN, "Добавлен");        
    }

    public static void addToChangeLog(List<AdsXmlSchemeDef.ChangeLogEntry> changeLog, AdsXmlSchemeDef.ChangeLogEntry entry) {
        boolean isChangeLogContainsEntry = false;
        for (AdsXmlSchemeDef.ChangeLogEntry iter : changeLog) {
            if (entry.getGuid().equals(iter.getGuid())) {
                isChangeLogContainsEntry = true;
                iter.setAuthor(entry.getAuthor());
                iter.setDate(entry.getDate());
                iter.setId(entry.getId());
                iter.setVersion(entry.getVersion());
            }            
        }
        if (!isChangeLogContainsEntry) {
            changeLog.add(entry);
        }
    }

    public static void removeFromChangeLog(List<AdsXmlSchemeDef.ChangeLogEntry> changeLog, String guid) {
        Iterator<AdsXmlSchemeDef.ChangeLogEntry> iter = changeLog.iterator();
        while (iter.hasNext()) {
            if (iter.next().getGuid().equals(guid)) {
                iter.remove();
            }
        }
    }

    public static AdsXmlSchemeDef.ChangeLogEntry getFromChangeLog(List<AdsXmlSchemeDef.ChangeLogEntry> changeLog, String guid) {
        for (AdsXmlSchemeDef.ChangeLogEntry entry : changeLog) {
            if (entry.getGuid().equals(guid)) {
                return entry;
            }
        }
        return null;
    }

    public static void applyChangesToChangeLog(List<AdsXmlSchemeDef.ChangeLogEntry> changeLog, Map<AdsXmlSchemeDef.ChangeLogEntry, RadixObject.EEditState> changes) {
        for (Map.Entry<AdsXmlSchemeDef.ChangeLogEntry, RadixObject.EEditState> entry : changes.entrySet()) {
            switch (entry.getValue()) {
                case NEW:
                case MODIFIED:
                    addToChangeLog(changeLog, entry.getKey());
                    break;
                case NONE:
                    removeFromChangeLog(changeLog, entry.getKey().getGuid());
            }
        }
    }
    
    public static Set<String> getAddedSetElements(Set<String> from, Set<String> to) {
        Set<String> result = new HashSet<>(to);
        result.removeAll(from);
        
        return result;
    }
    
    public static String getEditedElementsDescription(Set<String> addedElements, Set<String> removedElements, EIsoLanguage lang) {
        EIsoLanguage actualLang = SUPPORTED_LANGUAGES.contains(lang) ? lang : EIsoLanguage.ENGLISH;
        
        boolean isFirst = true;
        StringBuilder removedMessageBuilder = new StringBuilder();
        for (String element : removedElements) {
            if (!isFirst) {
                removedMessageBuilder.append(", ");
            } else {
                isFirst = false;
            }

            removedMessageBuilder.append(org.radixware.kernel.common.utils.XPathUtils.getHumanReadableXPath(element));
        }

        isFirst = true;
        StringBuilder addedMessageBuilder = new StringBuilder();
        for (String element : addedElements) {
            if (!isFirst) {
                addedMessageBuilder.append(", ");
            } else {
                isFirst = false;
            }

            addedMessageBuilder.append(org.radixware.kernel.common.utils.XPathUtils.getHumanReadableXPath(element));
        }                
        
        String template = addedElements.size() > 1 ? EDITED_FIELDS_TEMPLATE.getValue(actualLang) : EDITED_FIELD_TEMPLATE.getValue(actualLang);
        
        String removed = removedElements.isEmpty() ? "" : MessageFormat.format(template, REMOVED_MLS.getValue(actualLang), removedMessageBuilder.toString());
        String added = addedElements.isEmpty() ? "" : MessageFormat.format(template, ADDED_MLS.getValue(actualLang), addedMessageBuilder.toString());                
        
        String delimiter = removed.isEmpty() || added.isEmpty() ? "" : "\n";

        return removed + delimiter + added;
    }
}

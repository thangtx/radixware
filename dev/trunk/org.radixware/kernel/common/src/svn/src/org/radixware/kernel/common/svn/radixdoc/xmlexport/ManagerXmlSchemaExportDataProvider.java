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
package org.radixware.kernel.common.svn.radixdoc.xmlexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.XmlDefLinkedSchema;
import org.radixware.schemas.adsdef.XmlDefinition;

public class ManagerXmlSchemaExportDataProvider {
    
    public static class Localization {

        Map<Id, Map<EIsoLanguage, String>> translations = new HashMap<>();

        public void addTranslation(Id stringId, EIsoLanguage lang, String value) {
            Map<EIsoLanguage, String> data = translations.get(stringId);
            if (data == null) {
                data = new HashMap<>();
                translations.put(stringId, data);
            }
            data.put(lang, value);
        }

        public Map<EIsoLanguage, String> getTranslation(Id id) {
            return translations.get(id);
        }
    }

    public static class EnumData {

        String enumName;
        Id id;
        private Map<Id, EnumItem> items = new LinkedHashMap<>();
        private List<EnumItem> sortedItems = null;

        public EnumData(Id id) {
            this.id = id;
        }

        public static class EnumItem {

            String name;
            String value;
            Id titleId;

            public EnumItem(String name, String value, Id titleId) {
                this.name = name;
                this.value = value;
                this.titleId = titleId;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public Id getTitleId() {
                return titleId;
            }
        }

        public String getEnumName() {
            return enumName;
        }

        public void setEnumName(String enumName) {
            this.enumName = enumName;
        }

        public Id getId() {
            return id;
        }

        public void addItem(Id id, String name, String value, Id titleId) {
            EnumItem item = items.get(id);
            if (item != null) {
                if (titleId != null) {
                    item.titleId = titleId;
                }
            } else {
                items.put(id, new EnumItem(name, value, titleId));
            }
        }

        public List<EnumItem> getItems() {
            if (sortedItems == null) {
                sortedItems = new ArrayList<>(items.values());
                items = null;
                Collections.sort(sortedItems, new Comparator<EnumItem>() {
                    @Override
                    public int compare(EnumItem o1, EnumItem o2) {
                        String s1 = o1.name + "-" + o1.value;
                        String s2 = o2.name + "-" + o2.value;
                        return s1.compareTo(s2);
                    }
                });
            }
            return sortedItems;
        }
    }
    
    public static class Namespace2FileName {
        public final String namespace;
        public final String fileName;

        public Namespace2FileName(String namespace, String fileName) {
            this.namespace = namespace;
            this.fileName = fileName;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.namespace);
            hash = 41 * hash + Objects.hashCode(this.fileName);
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
            final Namespace2FileName other = (Namespace2FileName) obj;
            if (!Objects.equals(this.namespace, other.namespace)) {
                return false;
            }
            if (!Objects.equals(this.fileName, other.fileName)) {
                return false;
            }
            return true;
        }
    }
    
    private final Map<Id, AdsDefinitionDocument> allSchemasDefs;
    private final Map<Namespace2FileName, byte[]> allSchemasBytes;        

    private final Map<String, Localization> schemasDoc;
    private final Map<String, String> ns2Location;
    
    private final Set<EIsoLanguage> langs;
    private final Map<String, EnumData> enums;
    private final Map<String, Localization> enumsTitles;

    public ManagerXmlSchemaExportDataProvider(Map<Id, AdsDefinitionDocument> allSchemasDefs, Map<Namespace2FileName, byte[]> allSchemasBytes, Map<String, Localization> schemasDoc, Map<String, String> ns2Location, Set<EIsoLanguage> langs, Map<String, EnumData> enums, Map<String, Localization> enumsTitles) {
        this.allSchemasDefs = allSchemasDefs;
        this.allSchemasBytes = allSchemasBytes;
        this.schemasDoc = schemasDoc;
        this.ns2Location = ns2Location;
        this.langs = langs;
        this.enums = enums;
        this.enumsTitles = enumsTitles;
    }

    public AdsDefinitionDocument getSchemaDefDocument(Id schemaId) {
        return allSchemasDefs.get(schemaId);
    }
    
    private byte[] getSchemaBytesByNamespace(String namespace) {
        for (Namespace2FileName entry : allSchemasBytes.keySet()) {
            if (namespace.equals(entry.namespace)) {
                return allSchemasBytes.get(entry);
            }
        }
                
        return null;
    }        

    public byte[] getSchemaBytes(Id schemaId) {
        AdsDefinitionDocument tmp = allSchemasDefs.get(schemaId);
        
        if (tmp.getAdsDefinition() != null && tmp.getAdsDefinition().getAdsXmlSchemeDefinition() != null) {
            XmlDefinition tmpDef = tmp.getAdsDefinition().getAdsXmlSchemeDefinition();

            byte[] result = allSchemasBytes.get(new Namespace2FileName(tmpDef.getTargetNamespace(), null));
            
            return result == null ? getSchemaBytesByNamespace(tmpDef.getTargetNamespace()) : result;
        }

        return null;
    }        
    
    public byte[] getSchemaBytes(Namespace2FileName ns2FileName) {
        if (allSchemasBytes.containsKey(ns2FileName)) {
            return allSchemasBytes.get(ns2FileName);
        } else {
            return null;
        }
    }

    public List<Id> getLinkedSchemas(Id schemaId) {
        List<Id> result = new ArrayList<>();

        AdsDefinitionDocument schemaDefDocument = getSchemaDefDocument(schemaId);
        byte[] schemaBytes = getSchemaBytes(schemaId);

        if (schemaDefDocument != null && schemaBytes != null) {
            XmlDefinition schemaDef = schemaDefDocument.getAdsDefinition().getAdsXmlSchemeDefinition();
            if (schemaDef.getLinkedSchemas() != null && schemaDef.getLinkedSchemas().getLinkedSchemaList() != null && !schemaDef.getLinkedSchemas().getLinkedSchemaList().isEmpty()) {
                for (XmlDefLinkedSchema linkedSchema : schemaDef.getLinkedSchemas().getLinkedSchemaList()) {
                    if (allSchemasDefs.containsKey(linkedSchema.getId())) {
                        AdsDefinitionDocument linkedSchemaDefDoc = allSchemasDefs.get(linkedSchema.getId());

                        if (linkedSchemaDefDoc.getAdsDefinition() != null && linkedSchemaDefDoc.getAdsDefinition().getAdsXmlSchemeDefinition() != null) {
                            XmlDefinition linkedSchemaDef = linkedSchemaDefDoc.getAdsDefinition().getAdsXmlSchemeDefinition();
                            if (allSchemasBytes.containsKey(new Namespace2FileName(linkedSchemaDef.getTargetNamespace(), null))) {
                                result.add(linkedSchema.getId());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public String getSchemaLocation(Id schemaId) {
        AdsDefinitionDocument schemaDefDocument = getSchemaDefDocument(schemaId);
        if (schemaDefDocument != null) {
            if (schemaDefDocument.getAdsDefinition() != null && schemaDefDocument.getAdsDefinition().getAdsXmlSchemeDefinition() != null) {
                String namespace = schemaDefDocument.getAdsDefinition().getAdsXmlSchemeDefinition().getTargetNamespace();
                return ns2Location.get(namespace);
            }
        }

        return "";
    }
    
    public Map<String, EnumData> getEnums() {
        return enums == null ? null : new HashMap<>(enums);
    }
    
    public Map<String, Localization> getEnumsTitles() {
        return enumsTitles == null ? enumsTitles : new HashMap<>(enumsTitles);
    }
    
    public Map<Id, String> getSchemaLocalizedStrings(Id schemaId, EIsoLanguage lang) {
        Map<Id, String> result = new HashMap<>();
        Localization loc = schemasDoc.get(schemaId.toString());
        if (loc != null) {
            for (Map.Entry<Id, Map<EIsoLanguage, String>> entry : loc.translations.entrySet()) {
                result.put(entry.getKey(), entry.getValue().get(lang));
            }
        }

        return result;
    }

    public Set<EIsoLanguage> getLangs() {
        return langs == null ? null : new HashSet<>(langs);
    }
}

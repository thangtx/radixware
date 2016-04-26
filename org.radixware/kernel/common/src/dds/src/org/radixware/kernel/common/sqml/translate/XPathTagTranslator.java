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

package org.radixware.kernel.common.sqml.translate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlSchema;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.XPathTag;
import org.radixware.kernel.common.types.Id;


class XPathTagTranslator<T extends XPathTag> extends SqmlTagTranslator<T> {

    private static class SchemaInfo {

        private final String namespace;
        private final String prefix;

        public SchemaInfo(String namespace, String prefix) {
            this.namespace = namespace;
            this.prefix = prefix;
        }
    }

    @Override
    public void translate(T tag, CodePrinter cp) {
        final Sqml sqml = tag.getOwnerSqml();
        final ISqmlEnvironment environment = sqml.getEnvironment();

        final Map<Id, SchemaInfo> id2SchemaInfo = new HashMap<Id, SchemaInfo>();
        final List<SchemaInfo> schemaInfos = new ArrayList<SchemaInfo>();

        for (XPathTag.Item item : tag.getItems()) {
            if (!item.isAttribute()) {
                final Id schemaId = item.getSchemaId();
                if (!id2SchemaInfo.containsKey(schemaId)) {
                    final ISqmlSchema schema = environment.findSchemaById(schemaId);
                    if(schema == null){
                        throw new TagTranslateError(tag, "Can not find scheme #" + schemaId);
                    }
                    final String prefix = schemaInfos.isEmpty() ? "ns" : "ns" + schemaInfos.size();
                    final String namespace = schema.getNamespace();
                    if (namespace == null || namespace.isEmpty()) {
                        final Definition def = schema.getDefinition();
                        throw new TagTranslateError(tag, "Schema namespace not defined for '" + (def == null ? String.valueOf(schemaId) : def.getQualifiedName()) + "'.");
                    }
                    final SchemaInfo schemaInfo = new SchemaInfo(namespace, prefix);
                    id2SchemaInfo.put(schemaId, schemaInfo);
                    schemaInfos.add(schemaInfo);
                }
            }
        }

        if (schemaInfos.isEmpty()) {
            throw new TagTranslateError(tag, "XPath tag must contain at least one element.");
        }

        cp.print("'");
        boolean itemPrinted = false;

        for (XPathTag.Item item : tag.getItems()) {
            if (itemPrinted) {
                cp.print("/");
            } else {
                itemPrinted = true;
            }
            if (item.isAttribute()) {
                cp.print("@");
                cp.print(item.getName());
            } else {
                final Id schemaId = item.getSchemaId();
                final SchemaInfo schemaInfo = id2SchemaInfo.get(schemaId);

                cp.print(schemaInfo.prefix);
                cp.print(":");
                cp.print(item.getName());
                if (item.getIndex() != null) {
                    cp.print("[");
                    cp.print(item.getIndex().longValue());
                    cp.print("]");
                }
            }
            final String condition = item.getCondition();
            if (condition != null && !condition.isEmpty()) {
                cp.print("[");
                cp.print(condition);
                cp.print("]");
            }
        }

        cp.print("', '");

        boolean nsPrinted = false;
        for (SchemaInfo schemaInfo : schemaInfos) {
            if (nsPrinted) {
                cp.print(" ");
            } else {
                nsPrinted = true;
            }
            cp.print("xmlns:");
            cp.print(schemaInfo.prefix);
            cp.print("=\"");
            cp.print(schemaInfo.namespace);
            cp.print("\"");
        }

        cp.print("'");
    }
}

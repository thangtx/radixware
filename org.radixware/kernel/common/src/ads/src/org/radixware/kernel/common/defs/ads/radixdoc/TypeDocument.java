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

package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class TypeDocument {

    public static class Entry {

        private final AdsTypeDeclaration declaration;
        private final AdsDefinition def;
        private final String name;

        public Entry(AdsTypeDeclaration declaration, AdsDefinition def) {
            this.declaration = declaration;
            this.def = def;
            this.name = null;
        }

        public Entry(String name) {
            this.def = null;
            this.declaration = null;
            this.name = name;
        }

        public boolean isString() {
            return name != null;
        }

        public AdsDefinition getDefinition() {
            return def;
        }

        public String getString() {
            return name;
        }

        public AdsTypeDeclaration getDeclaration() {
            return declaration;
        }
    }

    protected static List<Entry> parseType(AdsTypeDeclaration declaration, Definition context) {
        if (declaration == null) {
            return Collections.<Entry>emptyList();
        }
        final AdsType type = declaration.resolve(context).get();
        List<Entry> doc = new ArrayList<>();

        if (type instanceof AdsClassType) {
            final AdsClassDef typeSource = ((AdsClassType) type).getSource();
            if (typeSource != null) {
                doc.add(new Entry(declaration, typeSource));
            } else {
                doc.add(new Entry(type.getQualifiedName(context)));
            }
        } else if (type != null) {
            doc.add(new Entry(type.getQualifiedName(context)));
        }

        if (type == null) {
            doc.add(new Entry(declaration.getQualifiedName(context)));
            return doc;
        }

        if (declaration.isGeneric()) {
            AdsTypeDeclaration.TypeArguments genericArguments = declaration.getGenericArguments();
            doc.add(new Entry("<"));
            boolean first = true;
            for (AdsTypeDeclaration.TypeArgument typeArgument : genericArguments.getArgumentList()) {
                if (first) {
                    first = false;
                } else {
                    doc.add(new Entry(", "));
                }
                doc.addAll(parseType(typeArgument.getType(), context));
            }
            doc.add(new Entry(">"));
        }

        return doc;
    }
    
    private final List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }
    
    public TypeDocument addString(String str) {
        entries.add(new Entry(str));
        
        return this;
    }
    
    public TypeDocument addType(AdsTypeDeclaration declaration, Definition context) {
        entries.addAll(parseType(declaration, context));
        
        return this;
    }
}

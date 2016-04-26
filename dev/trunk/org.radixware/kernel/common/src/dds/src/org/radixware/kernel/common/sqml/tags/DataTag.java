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

package org.radixware.kernel.common.sqml.tags;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.Sqml;


public class DataTag extends Sqml.Tag {

    final private Id id;
    
    public DataTag(Definition def) {
        this(def.getId());
    }
    public DataTag(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DataTag newInstance(final Id id) {
            return new DataTag(id);
        }
    }
    
    public Definition findTarget() {
        Definition context = getOwnerDefinition();
        Visitor visitor = new Visitor();
        context.getBranch().visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());
        return visitor.getObject();
/*        
        Definition context = getOwnerDefinition();
        while (context != null) {
            if (context.getId().getPrefix() == EDefinitionIdPrefix.ADS_ALGORITHM_CLASS)
                break;
            context = context.getOwnerDefinition();
        }
        if (context == null)
            return null;
        Visitor visitor = new Visitor();
        context.visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());
        return visitor.getObject();
*/        
    }

    public Definition getTarget() {
        final Definition definition = findTarget();
        if (definition != null) {
            return definition;
        } else {
            throw new DefinitionNotFoundError(id);
        }
    }
    
    private class Visitor implements IVisitor {
        private Definition object = null;

        public Visitor() {
        }

        public Definition getObject() {
            return object;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof Definition) {
                if (((Definition) radixObject).getId().equals(id)) {
                    object = (Definition) radixObject;
                }
            }
        }
    }
        
    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        Definition target = findTarget();
        if (target != null) {
            list.add(target);
        }
    }
        
    public static final String DATA_TAG_TYPE_TITLE = "Data Identifier Tag";
    public static final String DATA_TAG_TYPES_TITLE = "Data Identifier Tags";

    @Override
    public String getTypeTitle() {
        return DATA_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return DATA_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
    }
}

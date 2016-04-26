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

package org.radixware.kernel.designer.common.editors.sqml.vtag;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.api.editor.settings.AttributesUtilities;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.types.Id;


public class ParameterVTag<T extends ParameterTag> extends SqmlVTag<T> {

    public ParameterVTag(T tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        final T tag = getTag();

        final IParameterDef iParam = tag.findParameter();
        final boolean multy = (iParam != null && iParam.isMulty());
        if (multy) {
            cp.print('(');
        }

        final Definition def = (iParam != null ? iParam.getDefinition() : null);
        final AdsDynamicPropertyDef propertyAsParameter = (def instanceof AdsDynamicPropertyDef ? (AdsDynamicPropertyDef) def : null);
        final AdsEntityObjectClassDef entity = (propertyAsParameter != null ? AdsParameterPropertyDef.findEntity(propertyAsParameter) : null);

        if (entity != null) {
            final List<AdsPropertyDef> props = new ArrayList<AdsPropertyDef>();
            final Id propId = tag.getPropId();
            if (propId != null) {
                final AdsPropertyDef prop = (entity != null ? entity.getProperties().findById(propId, EScope.ALL).get() : null);
                props.add(prop);
            } else {
                final DdsTableDef table = (entity != null ? entity.findTable(tag) : null);
                if (table != null) {
                    final DdsPrimaryKeyDef pk = table.getPrimaryKey();
                    for (DdsPrimaryKeyDef.ColumnInfo columnInfo : pk.getColumnsInfo()) {
                        final Id pkColumnId = columnInfo.getColumnId();
                        final AdsPropertyDef prop = entity.getProperties().findById(pkColumnId, EScope.ALL).get();
                        props.add(prop);
                    }
                }
                if (props.isEmpty()) {
                    props.add(null);
                }
            }

            boolean propPrinted = false;
            if (props.size() > 1) {
                cp.print("(");
            }
            for (AdsPropertyDef prop : props) {
                if (propPrinted) {
                    cp.print(", ");
                } else {
                    propPrinted = true;
                }
                cp.print(":");
                if (entity != null) {
                    cp.print(iParam.getName());
                }
                cp.print(".");
                if (prop != null) {
                    cp.print(prop.getName());
                } else {
                    cp.printError();
                }
            }
            if (props.size() > 1) {
                cp.print(")");
            }
        } else if (iParam != null) {
            cp.print(":");
            cp.print(iParam.getName());
        } else {
            cp.print(":");
            cp.printError();
        }

        if (multy) {
            cp.print(",...)");
        }
    }

    @Override
    public AttributeSet updateAttributesBeforeRender(AttributeSet attributes) {
        attributes = super.updateAttributesBeforeRender(attributes);
        if (getTag().isLiteral()) {
            attributes = AttributesUtilities.createImmutable(attributes, AttributesUtilities.createImmutable(StyleConstants.Italic, true));
        }
        return attributes;
    }

    @Override
    public String getTokenName() {
        return "tag-parameter";
    }
}

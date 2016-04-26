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
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.providers.SqmlVisitorProviderFactory;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;

class PropSqlNameTagTranslator<T extends PropSqlNameTag> extends SqmlTagTranslator<T> {

    private final IProblemHandler problemHandler;

    PropSqlNameTagTranslator(IProblemHandler problemHandler) {
        this.problemHandler = problemHandler;
    }

    private static Sqml convertThisPropsToAliasProps(final Sqml sourceSqml, String tableAlias) {
        final Sqml sqml = sourceSqml.getClipboardSupport().copy();
        for (Sqml.Item item : sqml.getItems()) {
            if (item instanceof PropSqlNameTag) {
                final PropSqlNameTag tag = (PropSqlNameTag) item;
                if (tag.getOwnerType() == PropSqlNameTag.EOwnerType.THIS) {
                    tag.setOwnerType(PropSqlNameTag.EOwnerType.TABLE);
                    tag.setTableAlias(tableAlias);
                }
            }
        }
        sqml.setEnvironment(sourceSqml.getEnvironment());
        return sqml;
    }

    private static boolean isRecursion(ISqmlProperty prop, List<Definition> processedDefs) {
        final Sqml expression = prop.getExpression();
        if (expression == null) {
            return false;
        }
        final Definition def = prop.getDefinition();
        if (def == null) {
            return false;
        }
        if (processedDefs.contains(def)) {
            return true;
        }
        processedDefs.add(def);
        for (Sqml.Item item : expression.getItems()) {
            if (item instanceof PropSqlNameTag) {
                final PropSqlNameTag nextTag = (PropSqlNameTag) item;
                final ISqmlProperty nextProp = nextTag.findProperty();
                if (nextProp != null && !nextProp.isGeneratedInDb() && isRecursion(nextProp, processedDefs)) {
                    return true;
                }
            }
        }
        processedDefs.remove(def);
        return false;
    }

    private void checkForRecursion(T tag, ISqmlProperty prop) {
        final List<Definition> defs = new ArrayList<Definition>();
        if (isRecursion(prop, defs)) {
            final StringBuilder sb = new StringBuilder();
            boolean printed = false;
            for (Definition def : defs) {
                if (printed) {
                    sb.append("=>");
                } else {
                    printed = true;
                }
                final String name = def.getQualifiedName(tag);
                sb.append(name);
            }
            throw new TagTranslateError(tag, "Recursion in expressions: '" + sb.toString() + "'.");
        }
    }

    @Override
    public void translate(T tag, CodePrinter cp) {
        final PropSqlNameTag.EOwnerType owner = tag.getOwnerType();
        final ISqmlProperty property = tag.getProperty();
        checkForRecursion(tag, property);
        if (problemHandler != null) {
            property.check(tag, problemHandler);
        }
        final Definition definition = property.getDefinition();

        if (!SqmlVisitorProviderFactory.newPropSqlNameTagProvider(tag.getOwnerSqml(), owner).isTarget(definition)) {
            throw new TagTranslateError(tag, "Illegal property used: '" + definition.getQualifiedName() + "'.");
        }
        checkDepecation(tag, definition);

        String dbName = property.getDbName();
        if (dbName == null) {
            dbName = ":" + property.getName(); // dinamic property in condition from/where that will be transferred as parameter
        }
        switch (owner) {
            case NONE:
            case THIS:
                cp.print(dbName);
                break;
            case TABLE:
                String tableAlias = tag.getTableAlias();
                if (tableAlias == null || tableAlias.isEmpty()) {
                    final DdsTableDef table = property.findOwnerTable();
                    if (table == null) {
                        throw new DefinitionNotFoundError(tag.getPropOwnerId());
                    }
                    tableAlias = table.getDbName();
                }

                if (!property.isGeneratedInDb() && property.getExpression() != null) {
                    // change This.Prop to tableAlias.Prop in expression
                    final Sqml expression = convertThisPropsToAliasProps(property.getExpression(), tableAlias);
                    expression.setEnvironment(tag.getEnvironment());
                    final SqmlTranslator sqmt = SqmlTranslator.Factory.newInstance();
                    boolean addParen = property.getValType() != EValType.ANY;
                    if (addParen) {
                        cp.print('(');
                    }
                    sqmt.translate(expression, cp);
                    if (addParen) {
                        cp.print(')');
                    }
                } else {
                    cp.print(tableAlias);
                    cp.print('.');
                    cp.print(dbName);
                }
                break;
            case CHILD:
                cp.print("Child.");
                cp.print(dbName);
                break;
            case PARENT:
                cp.print("Parent.");
                cp.print(dbName);
                break;
            default:
                throw new TagTranslateError(tag, "Illegal owner of property SQL name tag.");
        }
    }
}

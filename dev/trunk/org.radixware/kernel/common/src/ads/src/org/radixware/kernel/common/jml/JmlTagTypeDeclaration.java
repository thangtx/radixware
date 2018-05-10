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
package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType.Item;
import org.radixware.schemas.xscml.TypeDeclaration;

public class JmlTagTypeDeclaration extends Jml.Tag {

    private AdsTypeDeclaration type;

    JmlTagTypeDeclaration(TypeDeclaration xDecl) {
        super(xDecl);
        this.type = AdsTypeDeclaration.Factory.loadFrom(xDecl);
    }

    public JmlTagTypeDeclaration(AdsTypeDeclaration decl) {
        super(null);
        this.type = decl;
    }

    public AdsTypeDeclaration getType() {
        return type;
    }

    public void setType(AdsTypeDeclaration type) {
        if (type == null) {
            throw new NullPointerException();

        }
        this.type = type;
        setEditState(EEditState.MODIFIED);
    }

    @Override
    public void appendTo(Item item) {
        final TypeDeclaration xDecl = item.addNewTypeDeclaration();
        appendTo(xDecl);
        type.appendTo(xDecl);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag type declaration={0}]", type.toString());
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final List<AdsType> allTypes = type.resolveAllTypes(getOwnerJml().getOwnerDefinition());
        for (final AdsType resolvedType : allTypes) {
            if (resolvedType instanceof AdsDefinitionType) {
                final Definition def = ((AdsDefinitionType) resolvedType).getSource();
                if (def != null) {
                    list.add(def);
                }
            }
        }
    }

    @Override
    public String getToolTip() {
        final Jml jml = getOwnerJml();
        //final JmlEnvironment environment = (jml != null ? jml.getEnvironment() : null);
        //if (environment == null) {
        //return super.getToolTip();
        //}
        if (this.type == null || type.getTypeId() == null || jml == null) {
            return "";
        }
        final AdsType resolvedType = this.type.resolve(jml.getOwnerDefinition()).get();
        if (resolvedType != null) {
            return resolvedType.getToolTip();
        } else {
            final StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("<b><font color=\"#FF0000\">Unresoved Type Reference</font></b>");
            b.append("<br>Type: ");
            b.append(type.getTypeId().name());

            if (type.getPath() != null && !type.getPath().isEmpty()) {
                b.append("<br>Definition: ");
                final List<Id> ids = type.getPath().asList();
                boolean first = true;
                for (Id id : ids) {
                    if (first) {
                        first = false;
                    }
                    b.append(id.toString());
                }
            }
            if (type.getExtStr() != null) {
                b.append("<br>Component: ");
                b.append(type.getExtStr());
            }

            b.append("</html>");
            return b.toString();
        }
    }

    @Override
    public String getDisplayName() {
        final Jml jml = getOwnerJml();
        if (jml != null) {
            //JmlEnvironment jmlEnv = jml.getEnvironment();
            // (jmlEnv != null) {
            final Definition context = jml.getOwnerDefinition();
            if (context != null) {
                final boolean[] unresolved = new boolean[]{false};
                String typeName = type.getQualifiedName(context, unresolved);
                if (unresolved[0]) {
                    return "unknowndef: " + restoreDisplayName(typeName);
                } else {
                    return rememberDisplayName(typeName);
                }
            }
            //}
        }
        return restoreDisplayName("<undefined>");
//        AdsType resolvedType = type.resolve(context);
//        if (resolvedType != null) {
//            return resolvedType.getQualifiedName(context);
//        } else {
//            return "<Unresolved Type Reference>";
//        }
    }
    private static final char[] NEW = new char[]{'w', 'e', 'n'};

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        final AdsType t = getType().check(this, problemHandler, h.getHistory());

        if (t instanceof AdsClassType) {
            final AdsClassDef clazz = ((AdsClassType) t).getSource();
            boolean hasConstructors = false;
            if (clazz != null) {
                hasConstructors = clazz.hasConstructors();
            }
            if (hasConstructors) {
                final int index = getOwnerJml().getItems().indexOf(this);
                if (index > 0) {
                    Scml.Item prev = getOwnerJml().getItems().get(index - 1);
                    if (prev instanceof Scml.Text) {
                        char[] content = ((Scml.Text) prev).getText().toCharArray();
                        boolean isNew = false;
                        int match = 0;
                        for (int i = content.length - 1; i >= 0; i--) {
                            char c = content[i];
                            if (c == ' ') {
                                continue;
                            }
                            if (c == NEW[match]) {
                                match++;
                                if (match == 3) {
                                    isNew = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (isNew) {
                            if (index < getOwnerJml().getItems().size()) {
                                Scml.Item next = getOwnerJml().getItems().get(index + 1);
                                if (next instanceof Scml.Text) {
                                    content = ((Scml.Text) next).getText().toCharArray();
                                    boolean isConstructor = false;
                                    loop:
                                    for (int i = 0; i < content.length; i++) {
                                        char c = content[i];
                                        switch (c) {
                                            case ' ':
                                                continue;
                                            case '(':
                                                isConstructor = true;
                                                break loop;
                                            default:
                                                break loop;
                                        }
                                    }
                                    if (isConstructor) {
                                        warning(problemHandler, "Class " + clazz.getQualifiedName() + " has explicitly defined constructor(s). Replace this class reference with constructor reference");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(this, purpose, JmlTagTypeDeclaration.this) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
                        RadixObjectLocator.RadixObjectData marker = null;
                        if (locator != null) {
                            marker = locator.start(JmlTagTypeDeclaration.this);
                        }
                        try {
                            return writeCode(printer, type, getOwnerJml());
                        } finally {
                            if (marker != null) {
                                marker.commit();
                            }
                            WriterUtils.leaveHumanUnreadableBlock(printer);
                        }
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }
}

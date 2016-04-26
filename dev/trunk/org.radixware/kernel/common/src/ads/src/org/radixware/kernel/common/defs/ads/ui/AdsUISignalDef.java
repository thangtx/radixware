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

package org.radixware.kernel.common.defs.ads.ui;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.schemas.ui.Signal;
import org.radixware.schemas.xscml.TypeDeclaration;


public class AdsUISignalDef extends AdsDefinition implements IJavaSource {

    public class Types extends RadixObjects<AdsTypeDeclaration> {

        private Types() {
            super(AdsUISignalDef.this);
        }

        private void loadFrom(Signal xSignal) {
            List<TypeDeclaration> list = xSignal.getTypeList();
            if (list != null) {
                for (TypeDeclaration xDecl : list) {
                    add(AdsTypeDeclaration.Factory.loadFrom(xDecl));
                }
            }
        }

        private void appendTo(Signal xSignal) {
            if (!isEmpty()) {
                for (AdsTypeDeclaration decl : this) {
                    decl.appendTo(xSignal.addNewType());
                }
            }
        }

        public String getHtmlName(RadixObject context, boolean qName) {
            StringBuilder b = new StringBuilder();
            b.append("Signal");
            if (!isEmpty()) {
                b.append("&lt;");
                boolean isFirst = true;
                for (AdsTypeDeclaration decl : this) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        b.append(",");
                    }
                    b.append(decl.getHtmlName(context, qName));
                }
                b.append("&gt;");
            }
            return b.toString();
        }
    }
    private final Types types = new Types();

    public AdsUISignalDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.SIGNAL), name);
    }

    public AdsUISignalDef(String name, List<AdsTypeDeclaration> types) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.SIGNAL), name);
        for (AdsTypeDeclaration decl : types) {
            this.types.add(decl);
        }
    }

    public AdsUISignalDef(Id id, String name) {
        super(id, name);
    }

    public AdsUISignalDef(Signal xSignal) {
        super(Id.Factory.loadFrom(xSignal.getId()), xSignal.getName());
        types.loadFrom(xSignal);
    }

    public Types getTypes() {
        return types;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CUSTOM_SIGNAL;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        types.visit(visitor, provider);
    }

    public void appendTo(Signal xSignal) {
        xSignal.setId(getId().toString());
        xSignal.setName(getName());
        types.appendTo(xSignal);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        for (AdsTypeDeclaration type : types) {
            AdsType resolvedType = type.resolve(this).get();
            if (resolvedType instanceof AdsDefinitionType) {
                Definition def = ((AdsDefinitionType) resolvedType).getSource();
                if (def != null) {
                    list.add(def);
                }
            }
        }
    }

    private Id getCustomEditorPageId() {
        RadixObject obj = getOwnerDef();
        while (obj != null) {
            if (obj instanceof AdsUIDef) {
                if (obj instanceof AdsCustomPageEditorDef) {
                    return ((AdsCustomPageEditorDef) obj).getId();
                } else {
                    return null;
                }
            }
            obj = obj.getContainer();
        }
        return null;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.SIGNAL;
    }

    private final class AdsSignalJavaSourceSupport extends JavaSourceSupport {

        public AdsSignalJavaSourceSupport() {
            super(AdsUISignalDef.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new CodeWriter(AdsSignalJavaSourceSupport.this, purpose) {

                @Override
                public boolean writeCode(CodePrinter printer) {
                    return false;
                }

                @Override
                public void writeUsage(CodePrinter printer) {
//                    printer.print("getCustomView$$$");
//                    printer.print(AdsUIUtil.getUiDef(AdsUISignalDef.this).getId().toCharArray());
//                    printer.print("().");
//                    printer.print(AdsUISignalDef.this.getId().toString()); // TODO use class' java name
                    //printer.print("getCustomView().");
                    //printer.print(AdsUISignalDef.this.getId().toString()); // TODO use class' java name
                    Definition def = this.getSupport().getCurrentRoot();
                    AdsUIDef ui = (AdsUIDef) AdsUISignalDef.this.getOwnerDefinition();
                    AdsDefinition parent;
                    Id customPageId = null;
                    if (ui.isTopLevelDefinition()) {
                        parent = ui;
                    } else {
                        customPageId = getCustomEditorPageId();
                        if (customPageId != null) {
                            parent = ((AdsCustomPageEditorDef) ui).getOwnerEditorPage().getOwnerDef();
                        } else {
                            parent = ui.getOwnerDef();
                        }
                    }
                    if (def == parent || parent.isParentOf(def)) {

                        if (customPageId != null) {
                            printer.print("getCustomView$$$");
                            printer.print(customPageId.toCharArray());
                            printer.print("().");
                        } else {
                            printer.print("getCustomView$$$");
                            printer.print(AdsUISignalDef.this.getOwnerDef().getId().toCharArray());
                            printer.print("().");
                        }
                    }
                    printer.print(AdsUISignalDef.this.getId().toString()); // TODO use class' java name
                }
            };
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new AdsSignalJavaSourceSupport();
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}

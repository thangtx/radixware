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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;
import org.radixware.kernel.common.defs.ads.clazz.algo.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;


public class JmlTagData extends Jml.Tag {

    final private Id id;

    JmlTagData(JmlType.Item.Data xData) {
        id = xData.getId();
    }

    public JmlTagData(AdsVarObject var) {
        id = var.getId();
    }

    public JmlTagData(AdsAlgoClassDef.Param param) {
        id = param.getId();
    }

    public JmlTagData(AdsAlgoClassDef.Var var) {
        id = var.getId();
    }

    public JmlTagData(AdsIncludeObject.Param param) {
        id = param.getId();
    }

    public JmlTagData(AdsAppObject.Prop prop) {
        id = prop.getId();
    }

    public Id getId() {
        return id;
    }

    public AdsDefinition resolve(AdsDefinition referenceContext) {
        AdsDefinition context = referenceContext;
        while ((context != null) && !(context instanceof AdsAlgoClassDef)) {
            context = context.getOwnerDef();
        }
        if (context == null) {
            return null;
        }
        Visitor visitor = new Visitor();
        context.visit(visitor, VisitorProviderFactory.createDefaultVisitorProvider());
        return visitor.getObject();
    }
    
    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (getOwnerJml() != null) {
            Definition def = resolve(getOwnerJml().getOwnerDef());
            if (def != null)
                list.add(def);
        }
    }

    @Override
    public void appendTo(Item item) {
        Item.Data xData = item.addNewData();
        xData.setId(id);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag data = {0}]", id);
    }

    @Override
    public void check(IProblemHandler problemHandler,Jml.IHistory h) {
        if (getOwnerJml() != null) {
            RadixObject object = resolve(getOwnerJml().getOwnerDef());
            if (object == null) {
                error(problemHandler, MessageFormat.format("Referenced data is not found: #{0}", id));
            }
        }
    }

    private class Visitor implements IVisitor {

        private AdsDefinition object = null;

        public Visitor() {
        }

        public AdsDefinition getObject() {
            return object;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof AdsDefinition) {
                if (((AdsDefinition) radixObject).getId().equals(id)) {
                    object = (AdsDefinition) radixObject;
                }
            }
        }
    }

    @Override
    public String getDisplayName() {
        if (getOwnerJml() == null)
            return "unknown data[" + id + "]";
        AdsDefinition object = resolve(getOwnerJml().getOwnerDef());
        if (object != null) {
            if (object instanceof AdsAppObject.Prop || object instanceof AdsIncludeObject.Param) {
                return object.getOwnerDefinition().getName() + "." + object.getName();
            }
            if (object instanceof AdsAlgoClassDef.Param) {
                return object.getOwnerDef().getName() + "::" + object.getName();
            }
            return object.getName();
        } else {
            return "unknown data[" + id + "]";
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        RadixObject object = resolve(getOwnerJml().getOwnerDef());
                        if (object == null) {
                            printer.printError();
                            return false;
                        }
                        if (object instanceof AdsAlgoClassDef.Param) {
                            AdsAlgoClassDef.Param param = (AdsAlgoClassDef.Param) object;
                            printer.print(param.getId());
                        }
                        if (object instanceof AdsAlgoClassDef.Var) {
                            AdsAlgoClassDef.Var var = (AdsAlgoClassDef.Var) object;
//                            if (var.getId().equals(AdsAlgoClassDef.EXCEPTION_VAR_ID)) {
//                                printer.print(AdsAlgoClassDef.EXCEPTION_VAR_ID);
//                            } else {
                                printer.print(var.getName());
//                            }
                        }
                        if (object instanceof AdsVarObject) {
                            AdsVarObject var = (AdsVarObject) object;
                            printer.print(var.getId());
                        }
                        if (object instanceof AdsAppObject.Prop) {
                            AdsAppObject.Prop prop = (AdsAppObject.Prop) object;
                            AdsAppObject app = (AdsAppObject) prop.getOwnerDefinition();
                            printer.print(prop.getId());
                        }
                        if (object instanceof AdsIncludeObject.Param) {
                            AdsIncludeObject.Param param = (AdsIncludeObject.Param) object;
                            printer.print(param.getId());
                        }
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }
}

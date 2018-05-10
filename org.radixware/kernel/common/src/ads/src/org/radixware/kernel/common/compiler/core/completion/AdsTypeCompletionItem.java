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
package org.radixware.kernel.common.compiler.core.completion;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;

public class AdsTypeCompletionItem extends AbstractCompletionItem {

    private IAdsTypeSource ts;
    private XBeansInterface xmlType;
    private boolean isDirectMemberOfDecl;

    public AdsTypeCompletionItem(IAdsTypeSource ts, XBeansInterface xmlType, boolean isDirectMemberOfDecl, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.ts = ts;
        this.xmlType = xmlType;
        this.isDirectMemberOfDecl = isDirectMemberOfDecl;
        RadixObject obj = (RadixObject) ts;
        if (xmlType != null) {
            this.sortText = xmlType.getName();
            this.leadText = "<b><font color=\"001F3D\">" + this.sortText + "</font></b>";
            this.tailText = obj.getQualifiedName();
            if (xmlType.getOwnerInterface() != null) {
                this.tailText += ":" + xmlType.getOwnerInterface().getQName();
            }
        } else {
            this.sortText = obj.getName();
            if (obj instanceof AdsEntityModelClassDef) {
                this.sortText += "(Editor)";
            } else if (obj instanceof AdsGroupModelClassDef) {
                this.sortText += "(Selector)";
            }

            this.leadText = "<b><font color=\"00001F\">" + this.sortText + "</font></b>";
            this.tailText = obj.getOwnerForQualifedName().getQualifiedName();
        }
    }
    
    @Override
    public RadixIcon getIcon() {
        return getRadixObject().getIcon();
    }

    @Override
    public Scml.Item[] getNewItems() {
        if (xmlType != null) {
            return new Scml.Item[]{
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml((IXmlDefinition) ts, xmlType.getQName()))
            };
        } else {
            if (ts instanceof AdsClassDef) {
                final List<AdsClassDef> classChain = new LinkedList<>();
                AdsClassDef clazz = (AdsClassDef) ts;
                //build chain of classes
                for (;;) {
                    classChain.add(clazz);
                    if (isDirectMemberOfDecl) {
                        break;
                    }
                    clazz = clazz.getOwnerClass();
                    if (clazz == null) {
                        break;
                    }
                    if (!clazz.isNested()) {
                        classChain.add(clazz);
                        break;
                    }
                }

                final List<Scml.Item> result = new LinkedList<>();
                int last = classChain.size() - 1;
                for (int i = last; i >= 0; i--) {
                    clazz = classChain.get(i);
                    if (i < last) {
                        result.add(Scml.Text.Factory.newInstance("."));
                    }
                    result.add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(clazz)));
                }
                return result.toArray(new Scml.Item[result.size()]);
            }
            return new Scml.Item[]{
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(ts))
            };
        }
    }

    @Override
    public RadixObject getRadixObject() {
        return (RadixObject) ts;
    }
}

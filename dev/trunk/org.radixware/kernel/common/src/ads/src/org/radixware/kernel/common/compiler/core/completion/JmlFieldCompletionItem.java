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

import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.resources.icons.RadixIcon;

import org.radixware.kernel.common.scml.Scml;

public class JmlFieldCompletionItem extends AbstractCompletionItem {

    private boolean isStatic;
    private boolean isEnum;
    private AdsDefinition adsEnum;

    public JmlFieldCompletionItem(char[] token, boolean isStatic, boolean isEnum, AdsDefinition adsEnum, String typeName, int relevance, int replaceStart, int replaceEnd) {
        super(relevance, replaceStart, replaceEnd);
        this.isEnum = isEnum;
        this.sortText = String.valueOf(token);
        this.tailText = typeName;
        this.isStatic = isStatic;
        this.adsEnum = adsEnum;

        if (isEnum) {
            if (adsEnum instanceof AdsEnumItemDef || adsEnum instanceof AdsEnumClassFieldDef) {
                this.leadText = "<font color=\"FF00FF\">" + adsEnum.getName() + "</font>";
                this.tailText = adsEnum.getOwnerDef().getQualifiedName();
            } else {
                this.leadText = "<font color=\"FF00FF\">" + this.sortText + "</font>";
            }
        } else {
            if (this.isStatic) {
                this.leadText = "<font color=\"009900\">" + this.sortText + "</font>";
            } else {
                this.leadText = "<font color=\"006666\">" + this.sortText + "</font>";
            }
        }

    }

    @Override
    public RadixIcon getIcon() {
        return isStatic ? AdsDefinitionIcon.Property.PROPERTY_DYNAMIC_STATIC : AdsDefinitionIcon.Property.PROPERTY_DYNAMIC;
    }

    @Override
    public boolean removePrevious(Scml.Item prevItem) {
        if (!(adsEnum instanceof AdsEnumItemDef)) {
            return false;
        }
        if (prevItem instanceof JmlTagTypeDeclaration) {
            final JmlTagTypeDeclaration tag = (JmlTagTypeDeclaration) prevItem;
            final AdsType adsType = tag.getType().resolve(adsEnum).get();
            if (adsType instanceof AdsEnumType) {
                final AdsEnumType et = (AdsEnumType) adsType;
                if (et.getSource() != null && et.getSource().getId() == ((AdsEnumItemDef) adsEnum).getOwnerEnum().getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Scml.Item[] getNewItems() {
        if (adsEnum != null) {
            return new Scml.Item[]{
                JmlTagInvocation.Factory.newInstance(adsEnum)
            };
        } else {
            return new Scml.Item[]{
                Scml.Text.Factory.newInstance(this.sortText)
            };
        }
    }
}

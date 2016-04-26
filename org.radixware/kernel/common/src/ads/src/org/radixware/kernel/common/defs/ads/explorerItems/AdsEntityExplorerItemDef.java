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

package org.radixware.kernel.common.defs.ads.explorerItems;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.radixdoc.SelectorExpItemRadixdoc;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ChildExplorerItems;
import org.radixware.schemas.adsdef.SelectorExplorerItemDefinition;
import org.radixware.schemas.radixdoc.Page;


public class AdsEntityExplorerItemDef extends AdsSelectorExplorerItemDef implements IRadixdocProvider {

    public static final class Factory {

        private Factory() {
        }

        public static AdsEntityExplorerItemDef newInstance(AdsEntityObjectClassDef referencedClass) {
            return new AdsEntityExplorerItemDef(referencedClass);
        }

        public static AdsEntityExplorerItemDef loadFrom(ChildExplorerItems.Item.Entity xDef) {
            return new AdsEntityExplorerItemDef(xDef);
        }

        static AdsEntityExplorerItemDef loadFrom(SelectorExplorerItemDefinition xDef, RadixObject container) {
            final AdsEntityExplorerItemDef result = new AdsEntityExplorerItemDef(xDef);
            result.setContainer(container);
            return result;

        }

        public static AdsEntityExplorerItemDef newTemporaryInstance(RadixObject container) {
            final AdsEntityExplorerItemDef item = newInstance(null);
            item.setContainer(container);
            return item;
        }
    }

    AdsEntityExplorerItemDef(ChildExplorerItems.Item.Entity xDef) {
        this((SelectorExplorerItemDefinition) xDef);
    }

    protected AdsEntityExplorerItemDef(SelectorExplorerItemDefinition xDef) {
        super(xDef);
    }

    AdsEntityExplorerItemDef(AdsEntityObjectClassDef clazz) {
        this(clazz, null);
    }

    AdsEntityExplorerItemDef(AdsEntityObjectClassDef clazz, Id id) {
        super(clazz, id);
    }

    public AdsChildRefExplorerItemDef convert2ChildReference() {
        final SelectorExplorerItemDefinition xDef = ChildExplorerItems.Item.ChildRef.Factory.newInstance();
        this.appendTo(xDef, ESaveMode.NORMAL);
        final AdsChildRefExplorerItemDef childRef = AdsChildRefExplorerItemDef.Factory.loadFrom(xDef, this);
        childRef.setChildReferenceId(null);
        childRef.selectorPresentationId = null;
        return childRef;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.ENTITY_EXPLORER_ITEM;
    }

    @Override
    public ClipboardSupport<AdsEntityExplorerItemDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsEntityExplorerItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                final ChildExplorerItems.Item.Entity xDef = ChildExplorerItems.Item.Entity.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsEntityExplorerItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ChildExplorerItems.Item.Entity) {
                    return Factory.loadFrom((ChildExplorerItems.Item.Entity) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", ChildExplorerItems.Item.Entity.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(AdsChildRefExplorerItemDef.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsEntityExplorerItemDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new SelectorExpItemRadixdoc(getSource(), page, options);
            }
        };
    }
}

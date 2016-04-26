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
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.ParagraphLinkExpItemRadixdoc;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ChildExplorerItems.Item.ParagraphLink;
import org.radixware.schemas.radixdoc.Page;


public class AdsParagraphLinkExplorerItemDef extends AdsExplorerItemDef implements IInheritableTitledDefinition, IRadixdocProvider {

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        AdsParagraphExplorerItemDef p = findReferencedParagraph();
        if (p != null) {
            return p.getClientEnvironment();
        } else {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }

    @Override
    public void setClientEnvironment(ERuntimeEnvironmentType e) {
        //ignore
    }

    public static final class Factory {

        public static AdsParagraphLinkExplorerItemDef newInstance() {
            return new AdsParagraphLinkExplorerItemDef();
        }

        public static AdsParagraphLinkExplorerItemDef newTemporaryInstance(RadixObject container) {
            AdsParagraphLinkExplorerItemDef item = newInstance();
            item.setContainer(container);
            return item;
        }

        public static AdsParagraphLinkExplorerItemDef loadFrom(ParagraphLink xDef) {
            return new AdsParagraphLinkExplorerItemDef(xDef);
        }
    }
    protected EnumSet<EExplorerItemAttrInheritance> inheritanceMask;
    private Id paragraphId;

    protected AdsParagraphLinkExplorerItemDef(ParagraphLink xDef) {
        super(xDef);
        this.inheritanceMask = EExplorerItemAttrInheritance.fromBitField(xDef.getInheritanceMask());

        this.paragraphId = xDef.getParagraphId();
    }

    protected AdsParagraphLinkExplorerItemDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.PARAGRAPH_LINK), "Link");
        this.inheritanceMask = EnumSet.of(EExplorerItemAttrInheritance.TITLE);
        this.paragraphId = null;
    }

    @Override
    public boolean isTitleInherited() {
        return inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE);
    }

    @Override
    public boolean setTitleInherited(boolean inherit) {
        if (inherit) {
            if (!inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE)) {
                inheritanceMask.add(EExplorerItemAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE)) {
                inheritanceMask.remove(EExplorerItemAttrInheritance.TITLE);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    @Override
    public Definition findOwnerTitleDefinition() {
        if (isTitleInherited()) {
            return AdsUtils.findTitleOwnerDefinition(findReferencedParagraph());
        }
        return this;
    }

    @Override
    public Id getTitleId() {
        if (isTitleInherited()) {
            AdsParagraphExplorerItemDef p = findReferencedParagraph();
            if (p != null) {
                return p.getTitleId();
            } else {
                return null;
            }
        }
        return super.getTitleId();
    }

    @Override
    public void setTitleId(Id titleId) {
        assert !isTitleInherited() : "Can't set title id. Title is inherited.";
        if (!isTitleInherited()) {
            super.setTitleId(titleId);
        }
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        if (isTitleInherited()) {
            AdsParagraphExplorerItemDef p = findReferencedParagraph();
            if (p != null) {
                return p.getTitle(language);
            } else {
                return null;
            }
        } else {
            return super.getLocalizedStringValue(language, titleId);
        }
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        if (isTitleInherited()) {
            throw new DefinitionError("Title is inherited. Modification not allowed.", this);
        }
        return super.setTitle(language, title);
    }

    private class ParagraphReference extends DefinitionLink<AdsParagraphExplorerItemDef> {

        @Override
        protected AdsParagraphExplorerItemDef search() {
            AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(AdsParagraphLinkExplorerItemDef.this).findById(paragraphId).get();
            if (def instanceof AdsParagraphExplorerItemDef) {
                return (AdsParagraphExplorerItemDef) def;
            } else {
                Module module = getModule();
                if (module == null) {
                    return null;
                }
                Segment segment = module.getSegment();
                if (segment == null) {
                    return null;
                }
                Layer layer = segment.getLayer();
                if (layer == null) {
                    return null;
                }
                return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<AdsParagraphExplorerItemDef>() {
                    @Override
                    public void accept(Layer.HierarchyWalker.Controller<AdsParagraphExplorerItemDef> controller, Layer layer) {
                        RadixObject result = layer.find(new VisitorProvider() {
                            @Override
                            public boolean isTarget(RadixObject radixObject) {
                                return radixObject instanceof AdsParagraphExplorerItemDef && ((AdsParagraphExplorerItemDef) radixObject).getId() == paragraphId;
                            }
                        });
                        if (result instanceof AdsParagraphExplorerItemDef) {
                            controller.setResultAndStop((AdsParagraphExplorerItemDef) result);
                        }
                    }
                });
            }
        }
    }
    private final ParagraphReference paragraphReference = new ParagraphReference();

    public AdsParagraphExplorerItemDef findReferencedParagraph() {
        return paragraphReference.find();
    }

    public Id getReferencedParagraphId() {
        return paragraphId;
    }

    public void setReferencedParagraphId(Id paragraphId) {
        this.paragraphId = paragraphId;
        fireNameChange();
        setEditState(EEditState.MODIFIED);
    }

    public void appendTo(ParagraphLink xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setInheritanceMask(EExplorerItemAttrInheritance.toBitField(inheritanceMask));
        if (paragraphId != null) {
            xDef.setParagraphId(paragraphId);
        }
    }

    @Override
    public String getName() {
        String ownName = super.getName();
        if (ownName == null || ownName.isEmpty()) {
            AdsParagraphExplorerItemDef ref = findReferencedParagraph();
            if (ref == null) {
                if (paragraphId != null) {
                    return "<" + paragraphId.toString() + ">";
                } else {
                    return "<undefined>";
                }
            } else {
                return ref.getQualifiedName(this);
            }
        } else {
            return ownName;
        }

    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PARAGRAPH_LINK_EXPLORER_ITEM;
    }

    public EnumSet<EExplorerItemAttrInheritance> getInheritanceMask() {
        return EnumSet.copyOf(inheritanceMask);
    }

    @Override
    public ClipboardSupport<AdsParagraphLinkExplorerItemDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsParagraphLinkExplorerItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                ParagraphLink xDef = ParagraphLink.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsParagraphLinkExplorerItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ParagraphLink) {
                    return Factory.loadFrom((ParagraphLink) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", ParagraphLink.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.PARAGRAPH_LINK;
    }

    @Override
    protected void collectDependencesImpl(boolean direct, boolean forModule, List<Definition> list) {
        if (!direct && !forModule) {
            AdsParagraphExplorerItemDef p = findReferencedParagraph();
            if (p != null) {
                list.add(p);
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsParagraphLinkExplorerItemDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ParagraphLinkExpItemRadixdoc(getSource(), page, options);
            }
        };
    }
}
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

package org.radixware.kernel.designer.common.editors.jml;

import org.radixware.kernel.designer.common.dialogs.scmlnb.ReplaceTagGenerator;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ReplaceTagVariant;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEventCodePropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagDbName;
import org.radixware.kernel.common.jml.JmlTagEventCode;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


@MimeRegistration(mimeType = "text/x-jml", service = ReplaceTagVariant.class)
public class ReplaceTagVariantImpl implements ReplaceTagVariant {

    @Override
    public List<ReplaceTagGenerator> getGenerators(ScmlEditorPane pane, Scml.Tag tag) {
        if (tag.isReadOnly()) {
            return Collections.emptyList();
        }

        if (tag instanceof JmlTagTypeDeclaration) {
            JmlTagTypeDeclaration decl = (JmlTagTypeDeclaration) tag;
            AdsType type = decl.getType().resolve(decl.getDefinition()).get();
            if (type instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                if (clazz == null) {
                    return Collections.emptyList();
                }
                Collection<AdsMethodDef> methods = clazz.getConstructors();
                if (methods.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    return openWithMethodReplacementList(pane, decl, methods);
                }
            } else if (type instanceof XmlType) {
                XmlType xType = (XmlType) type;
                AdsDefinition def = xType.getSource();
                if (def instanceof IXmlDefinition) {
                    Collection<String> typeNames = ((IXmlDefinition) def).getSchemaTypeList();
                    if (typeNames.isEmpty()) {
                        return Collections.emptyList();
                    } else {
                        return openWithXmlTypes(pane, decl, (IXmlDefinition) def, typeNames);
                    }
                }
            }
        } else if (tag instanceof JmlTagEventCode) {
            final List<ReplaceTagGenerator> items = new LinkedList<>();
            items.add(new ReplaceEventCodeWithProperty((JmlTagEventCode) tag, pane));
            items.add(new ReplaceEventCodeWithString((JmlTagEventCode) tag, pane));
            items.add(new EditLocalizedString((JmlTagEventCode) tag, pane));

            return items;
        } else if (tag instanceof JmlTagLocalizedString) {
            final List<ReplaceTagGenerator> items = new LinkedList<>();
            items.add(new ReplaceStringWithEventCode((JmlTagLocalizedString) tag, pane));
            items.add(new EditLocalizedString((JmlTagLocalizedString) tag, pane));
            return items;

        } else if (tag instanceof JmlTagInvocation) {
            JmlTagInvocation inv = (JmlTagInvocation) tag;
            Definition def = inv.resolve(inv.getOwnerJml().getOwnerDef());
            if (def instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) def;
                AdsClassDef clazz = method.getOwnerClass();
                if (clazz != null) {
                    if (method.isConstructor()) {
                        Collection<AdsMethodDef> methods = clazz.getConstructors();
                        if (methods.isEmpty()) {
                            return Collections.emptyList();
                        } else {
                            return openWithMethodReplacementList(pane, inv, methods);
                        }
                    } else {
                        final String name = method.getName();
                        List<AdsMethodDef> methods = clazz.getMethods().get(EScope.ALL, new IFilter<AdsMethodDef>() {
                            @Override
                            public boolean isTarget(AdsMethodDef radixObject) {
                                return radixObject.getName().equals(name);
                            }
                        });
                        if (methods.isEmpty() || (methods.size() == 1 && methods.get(0) == method)) {
                            return Collections.emptyList();
                        } else {
                            return openWithMethodReplacementList(pane, inv, methods);
                        }
                    }
                }
            } else if (def instanceof AdsEnumItemDef) {
                List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
                AdsEnumDef enumDef = ((AdsEnumItemDef) def).getOwnerEnum();
                if (enumDef != null) {
                    AdsEnumDef nearest = AdsSearcher.Factory.newAdsEnumSearcher(tag.getModule()).findById(enumDef.getId()).get();
                    if (nearest != null) {
                        enumDef = nearest;
                    }
                }
                for (AdsEnumItemDef item : enumDef.getItems().get(EScope.ALL)) {
                    items.add(new ReplaceEnumItem(tag, pane, item));
                }
                return items;
            } else if (def instanceof AdsEnumClassFieldDef) {
                final List<ReplaceTagGenerator> items = new LinkedList<>();
                final AdsEnumClassDef ownerEnumClass = ((AdsEnumClassFieldDef) def).getOwnerEnumClass();
                for (AdsEnumClassFieldDef item : ownerEnumClass.getFields().get(EScope.ALL)) {
                    items.add(new ReplaceClassEnumItem((Jml.Tag) tag, pane, item));
                }
                return items;
            }
        } else if (tag instanceof JmlTagId) {
            JmlTagId id = (JmlTagId) tag;
            Definition def = id.resolve(id.getOwnerJml().getOwnerDef());
            if (def instanceof AdsMethodDef) {
                return openWithMethodId2SlotConversion(pane, id, (AdsMethodDef) def);
            } else {
                List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
                items.add(new SwapIdSoftness(id, pane));
                return items;
            }
        }
        return Collections.emptyList();
    }

    private List<ReplaceTagGenerator> openWithMethodReplacementList(ScmlEditorPane pane, Jml.Tag tag, Collection<AdsMethodDef> methods) {
        List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
        for (AdsMethodDef method : methods) {
            items.add(new ReplaceClassWithConstructor(tag, pane, method));
        }
        return items;
    }

    private List<ReplaceTagGenerator> openWithMethodId2SlotConversion(ScmlEditorPane pane, JmlTagId tag, AdsMethodDef method) {
        List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
        if (!method.isConstructor()) {
            items.add(new ConvertSlot2Id(tag, pane, method));
        }
        items.add(new SwapIdSoftness(tag, pane));
        return items;
    }

    private List<ReplaceTagGenerator> openWithXmlTypes(ScmlEditorPane pane, Jml.Tag tag, IXmlDefinition xDef, Collection<String> typeNames) {
        List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
        for (String type : typeNames) {
            items.add(new ReplaceXmlTypeReference(tag, pane, type, xDef));
        }
        return items;
    }

    static class SwapIdSoftness extends TagItemCodeGenerator {

        private boolean setSoft;
        private Id[] path;
        private int mode;
        private boolean isDbName = false;

        public SwapIdSoftness(JmlTagId tagItem, ScmlEditorPane pane) {
            super(tagItem, pane);
            isDbName = tagItem instanceof JmlTagDbName;
            this.setSoft = !tagItem.isSoftReference();
            this.mode = tagItem.getMode();
            this.path = tagItem.getPath().asArray();
        }

        @Override
        protected Item[] createNewTag() {
            JmlTagId id = isDbName ? new JmlTagDbName(path) : new JmlTagId(path);
            id.setMode(mode);
            id.setSoftReference(setSoft);
            return new Item[]{id};
        }

        @Override
        public String getDisplayName() {
            if (setSoft) {
                return "Convert to soft ID reference (no module dependences used for resolution)";
            } else {
                return "Convert to hard ID reference (resolved using module dependences)";
            }
        }

        @Override
        protected void afterInsertTags(Scml.Item[] inv) {
            if (inv.length > 0) {
                Module module = inv[0].getModule();
                if (module != null) {
                    module.getDependences().actualize();
                }
            }
        }
    }

    static class ConvertSlot2Id extends TagItemCodeGenerator {

        private final AdsMethodDef method;
        private final boolean isSlot;

        public ConvertSlot2Id(JmlTagId tag, ScmlEditorPane pane, AdsMethodDef method) {
            super(tag, pane);
            this.method = method;
            this.isSlot = tag.getMode() == JmlTagId.Mode.SLOT_DESCRIPTION;
        }

        @Override
        public String getDisplayName() {
            if (isSlot) {
                return "Convert to ID reference";
            } else {
                return "Convert to slot description";
            }
        }

        @Override
        protected Scml.Item[] createNewTag() {
            JmlTagId id = new JmlTagId(method);
            if (isSlot) {
                id.setMode(JmlTagId.Mode.DEFAULT);
            } else {
                id.setMode(JmlTagId.Mode.SLOT_DESCRIPTION);
            }
            return new Scml.Item[]{id};
        }
    }

    static class ReplaceClassWithConstructor extends TagItemCodeGenerator {

        private final AdsMethodDef method;

        public ReplaceClassWithConstructor(Jml.Tag tag, ScmlEditorPane pane, AdsMethodDef method) {
            super(tag, pane);
            this.method = method;
        }

        @Override
        public String getDisplayName() {
            return method.getProfile().getName();
        }

        @Override
        protected Scml.Item[] createNewTag() {
            return new Scml.Item[]{JmlTagInvocation.Factory.newInstance(method)};
        }
    }

    static class ReplaceStringWithEventCode extends TagItemCodeGenerator {

        private final JmlTagLocalizedString stringTag;

        public ReplaceStringWithEventCode(JmlTagLocalizedString stringTag, ScmlEditorPane pane) {
            super(stringTag, pane);
            this.stringTag = stringTag;
        }

        @Override
        protected Scml.Item[] createNewTag() {
            final AdsMultilingualStringDef string = stringTag.findLocalizedString(stringTag.getStringId());
            if (string != null) {
                final AdsEventCodeDef eventCode = AdsMultilingualStringDef.Factory.convertToEventCode(string);
                final AdsLocalizingBundleDef localizingBundle = stringTag.getOwnerJml().getOwnerDef().findExistingLocalizingBundle();

                if (localizingBundle != null) {
                    localizingBundle.getStrings().getLocal().remove(string);
                    localizingBundle.getStrings().getLocal().add(eventCode);
                    return new Item[]{JmlTagEventCode.Factory.newInstance(eventCode)};
                }
            }
            return null;
        }

        @Override
        public String getDisplayName() {
            return "Convert to event code";
        }
    }

    static class ReplaceEventCodeWithString extends TagItemCodeGenerator {

        private JmlTagEventCode eventCodeTag;

        public ReplaceEventCodeWithString(JmlTagEventCode eventCode, ScmlEditorPane pane) {
            super(eventCode, pane);
            this.eventCodeTag = eventCode;
        }

        @Override
        protected Scml.Item[] createNewTag() {
            final AdsEventCodeDef eventCode = (AdsEventCodeDef) eventCodeTag.findLocalizedString(eventCodeTag.getStringId());
            if (eventCode instanceof AdsEventCodeDef) {
                final AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.convertToString(eventCode);
                final AdsLocalizingBundleDef localizingBundle = eventCodeTag.getOwnerJml().getOwnerDef().findExistingLocalizingBundle();

                if (localizingBundle != null) {
                    localizingBundle.getStrings().getLocal().remove(eventCode);
                    localizingBundle.getStrings().getLocal().add(string);
                    return new Item[]{JmlTagLocalizedString.Factory.newInstance(string)};
                }
            }
            return null;
        }

        @Override
        public String getDisplayName() {
            return "Convert to localized string";
        }
    }

    static class EditLocalizedString implements ReplaceTagGenerator {

        private JmlTagLocalizedString string;
        private ScmlEditorPane pane;

        public EditLocalizedString(JmlTagLocalizedString string, ScmlEditorPane pane) {
            this.string = string;
            this.pane = pane;
        }

        @Override
        public String getDisplayName() {
            return "Open editor";
        }

        @Override
        public void invoke() {
            if (pane.getTagEditorFactory() != null) {
                final TagEditor tagEditor = pane.getTagEditorFactory().createTagEditor(string);

                if (tagEditor != null) {
                    pane.editTag(string);
                }
            }
        }
    }

    static class ReplaceEventCodeWithProperty extends TagItemCodeGenerator {

        private JmlTagEventCode eventCode;

        public ReplaceEventCodeWithProperty(JmlTagEventCode eventCode, ScmlEditorPane pane) {
            super(eventCode, pane);
            this.eventCode = eventCode;
        }

        @Override
        protected Scml.Item[] createNewTag() {
            final ChooseEventCodePropPanel panel = new ChooseEventCodePropPanel(eventCode);
            ModalDisplayer displayer = new ModalDisplayer(panel) {
                @Override
                protected boolean canClose() {
                    return panel.isOk();
                }
            };
            if (displayer.showModal()) {
                AdsEventCodePropertyDef prop = panel.apply();
                if (prop != null) {
                    return new Scml.Item[]{
                        new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(prop.getOwnerClass())),
                        Scml.Text.Factory.newInstance("."),
                        JmlTagInvocation.Factory.newInstance(prop)
                    };
                }
            }
            return null;
        }

        @Override
        public String getDisplayName() {
            return "Convert to event code property reference";
        }
    }

    static class ReplaceEnumItem extends TagItemCodeGenerator {

        private final AdsEnumItemDef enumItem;

        public ReplaceEnumItem(Scml.Tag tag, ScmlEditorPane pane, AdsEnumItemDef method) {
            super((Jml.Tag) tag, pane);
            this.enumItem = method;
        }

        @Override
        public String getDisplayName() {
            return enumItem.getOwnerEnum().getQualifiedName(pane.getScml()) + "::" + enumItem.getName();
        }

        @Override
        protected Scml.Item[] createNewTag() {
            return new Scml.Item[]{JmlTagInvocation.Factory.newInstance(enumItem)};
        }
    }

    static class ReplaceClassEnumItem extends TagItemCodeGenerator {

        private final AdsEnumClassFieldDef field;

        public ReplaceClassEnumItem(Jml.Tag tag, ScmlEditorPane pane, AdsEnumClassFieldDef field) {
            super(tag, pane);
            this.field = field;
        }

        @Override
        public String getDisplayName() {
            return field.getOwnerEnumClass().getQualifiedName(pane.getScml()) + "::" + field.getName();
        }

        @Override
        protected Scml.Item[] createNewTag() {
            return new Scml.Item[]{JmlTagInvocation.Factory.newInstance(field)};
        }
    }

    static class ReplaceXmlTypeReference extends TagItemCodeGenerator {

        private final String typeName;
        private final IXmlDefinition xDef;

        public ReplaceXmlTypeReference(Jml.Tag tag, ScmlEditorPane pane, String typeName, IXmlDefinition xDef) {
            super(tag, pane);
            this.typeName = typeName;
            this.xDef = xDef;
        }

        @Override
        protected Scml.Item[] createNewTag() {
            return new Scml.Item[]{
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml(xDef, typeName))
            };
        }

        @Override
        public String getDisplayName() {
            return typeName;
        }
    }
}
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
package org.radixware.kernel.common.builder.completion.jml;

import java.util.*;
import org.radixware.kernel.common.compiler.core.completion.JmlCompletionEngine;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

class JmlCompletionProviderText implements ScmlCompletionProvider {

    private class RadixTypeCompletionItem implements CompletionItem {

        private EValType valType;
        private int start;
        private int end;

        public RadixTypeCompletionItem(EValType valType, int start, int end) {
            this.valType = valType;
            this.start = start;
            this.end = end;
        }

        @Override
        public String getEnclosingSuffix() {
            return null;
        }

        @Override
        public RadixIcon getIcon() {
            return null;
        }

        @Override
        public String getLeadDisplayText() {
            return "<b>" + valType.getName() + "</b>";
        }

        @Override
        public Item[] getNewItems() {
            return new Item[]{new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(valType))};
        }

        @Override
        public RadixObject getRadixObject() {
            return null;
        }

        @Override
        public int getRelevance() {
            return 1000;
        }

        @Override
        public int getReplaceEndOffset() {
            return end;
        }

        @Override
        public int getReplaceStartOffset() {
            return start;
        }

        @Override
        public String getSortText() {
            return valType.getName();
        }

        @Override
        public String getTailDisplayText() {
            return "Radix Built In Type";
        }

        @Override
        public boolean removePrevious(Item prevItem) {
            return false;
        }
    }

    private class ItemImpl implements CompletionItem {

        Definition definition;
        int start, end;
        int priority;
        boolean invoke;
        boolean type;
        boolean isSlot;
        String xmlType;

        public ItemImpl(Definition obj, boolean invoke, boolean isSlot, boolean type, String xmlType, int start, int end) {
            this.definition = obj;
            this.start = start;
            this.end = end;
            this.isSlot = isSlot;
            if (obj.getModule() == jml.getModule()) {
                priority = 600;
            } else if (obj instanceof Module) {
                priority = 400;
            } else {
                priority = 300;
            }
            this.invoke = invoke;
            this.type = type;
            this.xmlType = xmlType;
        }

        @Override
        public String getSortText() {
            return xmlType != null ? xmlType : definition.getName();
        }

        @Override
        public int getRelevance() {
            return priority;
        }

        @Override
        public String getLeadDisplayText() {
            if (xmlType != null) {
                return xmlType;
            } else {
                if (definition instanceof AdsMethodDef) {
                    AdsMethodDef m = (AdsMethodDef) definition;
                    return m.getProfile().getProfileHtml();
                } else {
                    String name = definition.getQualifiedName(definition.getModule());
                    if (definition instanceof AdsEntityModelClassDef) {
                        name += " (Editor)";
                    } else if (definition instanceof AdsGroupModelClassDef) {
                        name += " (Selector)";
                    }
                    return name;
                }
            }
        }

        @Override
        public String getTailDisplayText() {
            if (xmlType != null) {
                return definition.getQualifiedName();
            } else {
                return definition == null || definition.getModule() == null ? "" : definition.getModule().getQualifiedName();
            }
        }

        @Override
        public RadixIcon getIcon() {
            return definition.getIcon();
        }

        @Override
        public RadixObject getRadixObject() {
            return definition;
        }

        @Override
        public Item[] getNewItems() {
            Scml.Item tag = null;
            if (invoke) {
                if (type) {
                    if (definition instanceof IXmlDefinition && xmlType != null) {
                        tag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml((IXmlDefinition) definition, xmlType));
                    } else if (definition instanceof IAdsTypeSource) {
                        tag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) definition));
                    }
                } else {
                    if (definition instanceof AdsMethodDef) {
                        final AdsMethodDef method = (AdsMethodDef) definition;
                        if (method.getProfile().getAccessFlags().isStatic()) {
                            return new Item[]{
                                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(method.getOwnerClass())),
                                Scml.Text.Factory.newInstance("."),
                                JmlTagInvocation.Factory.newInstance(method),
                                Scml.Text.Factory.newInstance("()")
                            };
                        } else {
                            return new Item[]{
                                JmlTagInvocation.Factory.newInstance(method),
                                Scml.Text.Factory.newInstance("()")
                            };
                        }
                    }
                    if (definition instanceof AdsDefinition) {
                        tag = JmlTagInvocation.Factory.newInstance((AdsDefinition) definition);
                    }
                    if (definition instanceof AdsModule) {
                        tag = Scml.Text.Factory.newInstance(definition.getModule().getSegment().getLayer().getName() + "::" + definition.getName());
                    }
                }

            } else {
                if (definition instanceof AdsDefinition) {
                    JmlTagId id = new JmlTagId((AdsDefinition) definition);
                    if (isSlot) {
                        id.setMode(JmlTagId.Mode.SLOT_DESCRIPTION);
                    }
                    tag = id;
                } else if (definition instanceof Module) {
                    tag = new JmlTagId((Module) definition);
                } else if (definition instanceof DdsTableDef) {
                    tag = new JmlTagId((DdsTableDef) definition);
                }

            }
            if (tag != null) {
                return new Item[]{tag};
            } else {
                return new Item[0];
            }
        }

        @Override
        public int getReplaceStartOffset() {
            return start;
        }

        @Override
        public int getReplaceEndOffset() {
            return end;
        }

        @Override
        public String getEnclosingSuffix() {
            return "]";
        }

        @Override
        public boolean removePrevious(final Scml.Item prevItem) {
            if (definition instanceof AdsEnumItemDef) {
                if (prevItem instanceof JmlTagTypeDeclaration) {
                    final AdsType resolvedType = ((JmlTagTypeDeclaration) prevItem).getType().resolve(jml.getOwnerDef()).get();
                    if (resolvedType instanceof AdsDefinitionType && ((AdsDefinitionType) resolvedType).getSource() == ((AdsEnumItemDef) definition).getOwnerEnum()) {
                        return true;
                    }
                }
            } else if (definition instanceof IXmlDefinition && xmlType != null) {
                if (prevItem instanceof JmlTagTypeDeclaration) {
                    final AdsType resolvedType = ((JmlTagTypeDeclaration) prevItem).getType().resolve(jml.getOwnerDef()).get();
                    if (resolvedType instanceof AdsDefinitionType && ((AdsDefinitionType) resolvedType).getSource() == definition) {
                        return true;
                    }
                }
            } else if (definition instanceof AdsWidgetDef || definition instanceof AdsUISignalDef || definition instanceof AdsUIDef) {
                //AdsWidgetDef widget = (AdsWidgetDef) definition;
                if (prevItem instanceof JmlTagTypeDeclaration) {
                    final AdsType resolvedType = ((JmlTagTypeDeclaration) prevItem).getType().resolve(jml.getOwnerDef()).get();
                    if (resolvedType instanceof AdsDefinitionType && ((AdsDefinitionType) resolvedType).getSource().isParentOf(definition)) {
                        return true;
                    }
                } else if (prevItem instanceof JmlTagInvocation) {
                    final Definition def = ((JmlTagInvocation) prevItem).resolve(jml.getOwnerDef());
                    if (def.isParentOf(definition)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    protected transient final Jml.Text item;
    protected transient final Jml jml;

    public JmlCompletionProviderText(final Jml jml, final Jml.Text text) {
        super();
        this.item = text;
        this.jml = jml;
    }

    private List<Module> lookupModules() {
        final AdsDefinition context = jml.getOwnerDef();
        final AdsModule contextModule = context.getModule();

        final Map<Id, Module> modileId2Dependence = new HashMap<>();

        Layer top = contextModule.getSegment().getLayer();
        Layer.HierarchyWalker.walk(top, new Layer.HierarchyWalker.Acceptor<Object>() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer top) {
                for (Module m : top.getAds().getModules().list()) {
                    if (!modileId2Dependence.containsKey(m.getId())) {
                        modileId2Dependence.put(m.getId(), m);
                    }
                }
                for (Module m : top.getDds().getModules().list()) {
                    if (!modileId2Dependence.containsKey(m.getId())) {
                        modileId2Dependence.put(m.getId(), m);
                    }
                }
            }
        });

        final ArrayList<Module> modules = new ArrayList<>(modileId2Dependence.values());

//        contextModule.getDependenceProvider().collect(modileId2Dependence);
//
//        final List<Module> dm = new ArrayList<Module>();
//        for (Module dep : modileId2Dependence.values()) {
//            final Module module = dep.findDependenceModule(context);
//            if (module != null) {
//                dm.add(module);
//            }
//        }
//            if (!dm.contains(jml.getModule())) {
//                dm.add(jml.getModule());
//            }
//            for (Module module : dm) {
//                modules.add(module);
//            }
        return modules;
    }

    private Layer findLayerByName(final String name) {
        Layer l = jml.getModule().getSegment().getLayer();
        return Layer.HierarchyWalker.walk(l, new Layer.HierarchyWalker.Acceptor<Layer>() {
            @Override
            public void accept(HierarchyWalker.Controller<Layer> controller, Layer layer) {
                if (Utils.equals(name, layer.getName())) {
                    controller.setResultAndStop(layer);
                }
            }
        });
    }

    private List<Module> findModulesByName(final Layer layer, final String name) {
        final List<Module> modules = new ArrayList<>();
        final AdsDefinition context = jml.getOwnerDef();
        final AdsModule contextModule = context.getModule();

        final Map<Id, Dependence> modileId2Dependence = new HashMap<>();
        contextModule.getDependenceProvider().collect(modileId2Dependence);

        final List<Module> dm = new ArrayList<>();
        for (Dependence dep : modileId2Dependence.values()) {
            final List<Module> module = dep.findDependenceModule(context);
            if (modules != null) {
                dm.addAll(module);
            }
        }
        if (!dm.contains(jml.getModule())) {
            dm.add(jml.getModule());
        }
        for (Module module : dm) {
            if (layer != null && module.getSegment().getLayer() != layer) {
                continue;
            }
            if (Utils.equals(name, module.getName())) {
                modules.add(module);
            }
        }
        return modules;
    }

    @Override
    public void complete(final int offset2, final CompletionRequestor requestor) {
        //first try to perform native completion
        //determine token
        final char[] text = item.getText().toCharArray();

        final int offset = offset2 > text.length ? text.length : offset2;
        int tokenStart = 0;
        final int tokenEnd = offset - 1;

        boolean isIdRequest = false;
        boolean isSlotRequest = false;
        boolean isRadixToken = false;
        boolean isQualifiedToken = false;

        for (int i = offset - 1; i >= 0; i--) {
            final char c = text[i];
            if (!Character.isJavaIdentifierPart(c)) {//token start

                if (c == '[') {//test for idof
                    if (i >= 4 && text[i - 1] == 'f' && text[i - 2] == 'o' && text[i - 3] == 'd' && text[i - 4] == 'i') {//idof expression found
                        isIdRequest = true;
                    } else if (i >= 4 && text[i - 1] == 't' && text[i - 2] == 'o' && text[i - 3] == 'l' && text[i - 4] == 's') {//slot expression found
                        isIdRequest = true;
                        isSlotRequest = true;
                    }
                } else if (c == ':') {
                    isRadixToken = true;
                    continue;
                } else if (c == '.') {
                    isQualifiedToken = true;
                    continue;
                }
                tokenStart = i + 1;
                break;
            }
        }

        final String matchName = String.valueOf(text, tokenStart, tokenEnd - tokenStart + 1);

        if (!matchName.isEmpty()) {
            for (EValType vt : EValType.values()) {
                if (vt.getName().startsWith(matchName)) {
                    requestor.accept(new RadixTypeCompletionItem(vt, offset - tokenStart, 0));
                }
            }
        }

        if (isRadixToken || isIdRequest) {
            //first looking for module separator

            //determining context object from matchName
            final int scopeEndIndex = matchName.lastIndexOf("::");
            String searchName;
            final List<RadixObject> contextNodes = new ArrayList<>();
            if (scopeEndIndex >= 0) {
                final String scopeSpec = matchName.substring(0, scopeEndIndex);
                searchName = matchName.substring(scopeEndIndex + 2);
                final String[] layerOrModuleSpec = scopeSpec.split("::");

                if (layerOrModuleSpec.length >= 1) {//layer and (or) module specified
                    if (layerOrModuleSpec.length == 1) {//layer or module
                        final Layer layer = findLayerByName(layerOrModuleSpec[0]);
                        if (layer != null) {//layer found
                            contextNodes.add(layer);
                        }
                        final List<Module> modules = findModulesByName(null, layerOrModuleSpec[0]);
                        if (!modules.isEmpty()) {
                            contextNodes.addAll(modules);
                        }
                    } else if (layerOrModuleSpec.length == 2) {//layer and module specified
                        final Layer layer = findLayerByName(layerOrModuleSpec[0]);
                        if (layer == null) {//layer specified but not found
                            return;
                        }
                        final List<Module> modules = findModulesByName(layer, layerOrModuleSpec[1]);
                        if (!modules.isEmpty()) {
                            contextNodes.addAll(modules);
                        }
                    } else {
                        //invalid token
                        return;
                    }
                }
            } else {
                searchName = matchName;
            }

            final boolean invoke = !isIdRequest;
            final int start = isIdRequest ? offset - tokenStart + 5 : offset - tokenStart;
            final int end = 0;

            final List<RadixObject> expandedNodes = new ArrayList<>();
            final int colon = searchName.lastIndexOf(':');

            if (colon >= 0) {//need to determine available contexts
                if (contextNodes.isEmpty()) {//no context to expand
                    if (searchName.charAt(0) == ':') {
                        int itemIndex = jml.getItems().indexOf(item);
                        if (itemIndex > 0) {
                            Scml.Item prevItem = jml.getItems().get(itemIndex - 1);
                            if (prevItem instanceof JmlTagTypeDeclaration) {
                                JmlTagTypeDeclaration decl = (JmlTagTypeDeclaration) prevItem;
                                AdsType type = decl.getType().resolve(jml.getOwnerDef()).get();
                                if (type instanceof AdsDefinitionType) {
                                    AdsDefinitionType dt = (AdsDefinitionType) type;
                                    contextNodes.add(dt.getSource());
                                }
                            } else if (prevItem instanceof JmlTagInvocation) {
                                Definition def = ((JmlTagInvocation) prevItem).resolve(jml.getOwnerDef());
                                if (def != null) {
                                    contextNodes.add(def);
                                }
                            }
                        }
                    } else {
                        contextNodes.addAll(lookupModules());
                    }
                }
                String localNamePart = searchName.substring(0, colon);

                String[] names = localNamePart.split(":");

                for (int i = 0; i < names.length; i++) {
                    final String match = names[i].toLowerCase();
                    expandedNodes.clear();
                    for (RadixObject object : contextNodes) {
                        if (object instanceof IXmlDefinition) {
                            expandedNodes.add(object);
                        } else {
                            final List<RadixObject> objectsToVisit = new LinkedList<>();
                            objectsToVisit.add(object);
                            if (object instanceof AdsDefinition) {
                                AdsDefinition ovr = ((AdsDefinition) object).getHierarchy().findOverwritten().get();
                                while (ovr != null) {
                                    if (!contextNodes.contains(ovr)) {
                                        objectsToVisit.add(ovr);
                                    }
                                    ovr = ovr.getHierarchy().findOverwritten().get();
                                }
                            } else if (object instanceof Module) {
                                Module m = (Module) object;
                                while (m != null) {
                                    if (!contextNodes.contains(m)) {
                                        objectsToVisit.add(m);
                                    }
                                    m = m.findOverwritten();
                                }
                            }
                            for (RadixObject object2visit : objectsToVisit) {
                                final RadixObject container = object2visit;
                                object2visit.visit(new IVisitor() {
                                    @Override
                                    public void accept(final RadixObject object) {
                                        expandedNodes.add(object);
                                    }
                                }, new VisitorProvider() {
                                    @Override
                                    public boolean isTarget(final RadixObject radixObject) {
                                        if (!(radixObject instanceof Definition)) {
                                            return false;
                                        }

                                        if (radixObject == container) {
                                            return false;
                                        }
                                        if (radixObject.getOwnerForQualifedName() == container) {
                                            return match.isEmpty() ? true : Utils.equals(match, radixObject.getName().toLowerCase());
                                        }
                                        return false;
                                    }

                                    @Override
                                    public boolean isContainer(final RadixObject object) {
                                        return object == container || object.getOwnerDefinition() == container;
                                    }
                                });
                            }
                        }
                    }
                    if (expandedNodes.isEmpty()) {
                        return;
                    }
                    contextNodes.clear();
                    //ads first
                    ArrayList<RadixObject> dds = new ArrayList<>();
                    for (RadixObject obj : expandedNodes) {
                        if (obj.getModule() instanceof DdsModule) {
                            dds.add(obj);
                        } else {
                            contextNodes.add(obj);
                        }
                    }
                    contextNodes.addAll(dds);
                }

                acceptContextNodes(contextNodes, searchName.substring(colon + 1), requestor, invoke, isSlotRequest, start, end);
            } else {//no context any name match is available
                if (contextNodes.isEmpty()) {//no context to expand
                    contextNodes.addAll(lookupModules());
                }
                final String localNamePart = colon >= 0 ? searchName.substring(0, colon) : searchName;
                acceptContextNodes(contextNodes, localNamePart, requestor, invoke, isSlotRequest, start, end);
                // return;
            }
            //acceptContextNodes(contextNodes, searchName.substring(colon + 1), requestor, invoke, start, end);
        } else {
            if (isQualifiedToken) {
                //infer qualified type refs
            }
            new JmlCompletionEngine().complete(item, offset, requestor);
            // if (!isIdRequest && !isRadixToken) {
            //    AdsCompletionEngine.complete(jml, item, offset, requestor);
            //}
        }

    }

    public void acceptContextNodes(final List<RadixObject> contextNodes, final String lookupName,
            final CompletionRequestor requestor,
            final boolean invoke, final boolean isSlot, final int start, final int end) {
        final String localNamePart = lookupName.toLowerCase();
        final ERuntimeEnvironmentType contextEnv = jml.getUsageEnvironment();
        for (RadixObject node : contextNodes) {
            if (node instanceof IXmlDefinition && invoke) {
                String match = localNamePart;
                if (localNamePart.isEmpty()) {
                    int index = jml.getItems().indexOf(item);
                    if (index > 0) {
                        Scml.Item prev = jml.getItems().get(index - 1);
                        if (prev instanceof JmlTagTypeDeclaration) {
                            //check there no whitespace symbols between current position and prev item
                            boolean refine = true;
                            int ts = start + end-1;
                            if(ts >= item.getText().length()){
                                ts = item.getText().length()-1;
                            }
                            for (int i = ts; i >= 0; i--) {
                                char c = item.getText().charAt(i);
                                if (c == ' ' || c == '\n' || c == '\t') {
                                    refine = false;
                                    break;
                                }
                            }
                            if (refine) {
                                AdsTypeDeclaration decl = ((JmlTagTypeDeclaration) prev).getType();
                                AdsType resolvedType = decl.resolve(jml.getOwnerDef()).get();
                                if (resolvedType instanceof XmlType) {
                                    AdsDefinition src = ((XmlType) resolvedType).getSource();
                                    if (src == node) {
                                        match = decl.getExtStr() == null ? "" : decl.getExtStr().toLowerCase();
                                    }
                                }
                            }
                        }
                    }
                }

                final IXmlDefinition xDef = (IXmlDefinition) node;
                final Collection<String> types = xDef.getSchemaTypeList();
                for (String type : types) {
                    String t = type.toLowerCase();
                    if (t.startsWith(match) && !t.equals(match)) {
                        requestor.accept(new ItemImpl((Definition) node, invoke, isSlot, true, type, start, end));
                    }
                }
                continue;
            }

            final List<RadixObject> objects = new LinkedList<>();
            objects.add(node);

            if (node instanceof AdsDefinition) {
                final AdsDefinition nodeDef = (AdsDefinition) node;
                AdsDefinition ovr = nodeDef.getHierarchy().findOverwritten().get();
                while (ovr != null) {
                    objects.add(ovr);
                    ovr = ovr.getHierarchy().findOverwritten().get();
                }
            }
            for (RadixObject object : objects) {
                final RadixObject container = object;
                object.visit(new IVisitor() {
                    @Override
                    public void accept(final RadixObject object) {
                        final boolean isType = invoke && object instanceof IAdsTypeSource;
                        requestor.accept(new ItemImpl((Definition) object, invoke, isSlot, isType, null, start, end));
                        if (isType) {
                            if (object instanceof AdsClassDef) {
                                Collection<AdsMethodDef> constructors = ((AdsClassDef) object).getConstructors();
                                for (AdsMethodDef m : constructors) {
                                    requestor.accept(new ItemImpl(m, invoke, isSlot, false, null, start, end));
                                }
                            }
                        }
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(final RadixObject radixObject) {
                        if (invoke) {
                            if (container instanceof Layer && radixObject instanceof AdsModule) {
                                return true;
                            }
                            if (!(radixObject instanceof AdsMethodDef || radixObject instanceof AdsPropertyDef || radixObject instanceof AdsEnumItemDef || radixObject instanceof IAdsTypeSource || radixObject instanceof AdsWidgetDef || radixObject instanceof AdsUISignalDef || radixObject instanceof AdsLayout)) {
                                return false;
                            }

                            if (radixObject instanceof AdsMethodDef && !(((AdsMethodDef) radixObject).getProfile().getAccessFlags().isStatic())) {
                                return false;
                            }
                        }
                        if (!invoke && isSlot) {
                            if (radixObject instanceof AdsMethodDef) {
                                if (((AdsMethodDef) radixObject).isConstructor()) {
                                    return false;
                                } else {
                                    return true;
                                }
                            } else {
                                return false;
                            }
                        }
                        if (!(radixObject instanceof Definition)) {
                            return false;
                        }

                        if (radixObject instanceof AdsLocalizingBundleDef) {
                            return false;
                        }

                        if (radixObject != container && radixObject.getOwnerForQualifedName() != container && radixObject.getOwnerForQualifedName().getOwnerDefinition() != container) {
                            return false;
                        }

                        if (radixObject.getName().toLowerCase().startsWith(localNamePart)) {
                            if (radixObject instanceof AdsDefinition) {
                                ERuntimeEnvironmentType sc = ((AdsDefinition) radixObject).getUsageEnvironment();
                                if (sc == contextEnv || sc == ERuntimeEnvironmentType.COMMON) {
                                    return true;
                                } else {
                                    return (radixObject instanceof AdsPropertyDef);
                                }
                            } else {
                                if (radixObject instanceof DdsColumnDef) {
                                    return false;
                                }
                                return true;
                            }
                        } else {
                            if (radixObject instanceof AdsModelClassDef) {
                                AdsModelClassDef model = (AdsModelClassDef) radixObject;
                                AdsDefinition owner = model.getOwnerDef();
                                if (isTarget(owner)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    }

                    @Override
                    public boolean isContainer(final RadixObject radixObject) {

                        if (radixObject instanceof AdsLocalizingBundleDef) {
                            return false;
                        }

                        if (container instanceof Layer) {
                            return radixObject == container || radixObject.getContainer() == container || (radixObject.getContainer() != null && radixObject.getContainer().getContainer() == container);
                        } else {
                            return true;//radixObject == container || radixObject.getOwnerDefinition() == container;
                        }

                    }
                });
            }
        }
    }
}

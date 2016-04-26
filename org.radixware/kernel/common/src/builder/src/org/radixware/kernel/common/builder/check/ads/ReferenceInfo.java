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

package org.radixware.kernel.common.builder.check.ads;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Value;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSortingDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.uds.module.UdsModule;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.uds.UdsSegment;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


public class ReferenceInfo {

    public static class Usage {

        public enum Kind {

            PUBLISHING,
            OVERRIDE,
            OVERWRITE,
            SIMPLE
        }

        public enum Type {

            SAME_CLASS,
            SAME_MODULE,
            SAME_LAYER,
            OUTER,
            USER_FUNC
        }
        public final RadixObject user;
        public final Definition reference;
        public final Kind kind;
        private EAccess requiredAccess = null;
        private boolean requiredPublished = false;
        private String requiredAccessCause = "Fixed accessibility";

        Usage(Definition reference, RadixObject user) {
            this.user = user;

            if (user instanceof Value || user instanceof Inheritance) {
                kind = Usage.Kind.PUBLISHING;
            } else if (user instanceof SelectorColumn) {
                AdsSelectorPresentationDef spr = ((SelectorColumn) user).getOwnerSelectorPresentation();
                if (spr.isFinal()) {
                    kind = Usage.Kind.SIMPLE;
                } else {
                    kind = Usage.Kind.PUBLISHING;
                }
            } else if (user instanceof AdsSelectorPresentationDef) {
                AdsSelectorPresentationDef spr = (AdsSelectorPresentationDef) user;
                if (reference instanceof AdsFilterDef || reference instanceof AdsSortingDef) {
                    if (spr.isFinal()) {
                        kind = Usage.Kind.SIMPLE;
                    } else {
                        kind = Usage.Kind.PUBLISHING;
                    }
                } else {
                    kind = Usage.Kind.SIMPLE;
                }
            } else if (user instanceof AdsEditorPresentationDef) {
                AdsEditorPresentationDef spr = (AdsEditorPresentationDef) user;
                if (reference instanceof AdsEditorPageDef) {
                    if (spr.isFinal()) {
                        kind = Usage.Kind.SIMPLE;
                    } else {
                        kind = Usage.Kind.PUBLISHING;
                    }
                } else {
                    kind = Usage.Kind.SIMPLE;
                }
            } else {
                kind = Kind.SIMPLE;
            }
            this.reference = reference;
        }

        Usage(Definition reference, RadixObject user, Usage.Kind k) {
            this.user = user;
            kind = k;
            this.reference = reference;
        }

        public boolean isRealReference() {
            if (reference instanceof AdsEntityModelClassDef) {
                if (user == ((AdsEntityModelClassDef) reference).getOwnerEditorPresentation()) {
                    return false;
                }
            }
            if (reference instanceof AdsGroupModelClassDef) {
                if (user == ((AdsGroupModelClassDef) reference).getOwnerSelectorPresentation()) {
                    return false;
                }
            }
            return true;
        }

        public String getDisplayName() {
            return user.getName();
        }

        String getDefinitionDisplayName() {


            return getOwnerDefinition().getQualifiedName();
        }

        public String getTypeDisplayName() {
            if (user instanceof Scml || user instanceof Scml.Item) {
                return "Source code";
            }
            return user.getTypeTitle();
        }

        private Definition getOwnerDefinition() {
            RadixObject obj = user;
            while (obj != null) {
                if (obj instanceof Definition) {
                    return (Definition) obj;
                }
                obj = obj.getContainer();
            }
            return null;
        }

        private Module getTargetModule() {
            return reference.getModule();
        }

        private AdsDefinition getOwnerRoot() {
            return getAccessRoot(user);
        }

        private AdsDefinition getTargetRoot() {
            return getAccessRoot(reference);
        }

        private void calcRequiredAccess() {
            String[] desc = new String[1];
            boolean[] pub = new boolean[1];
            requiredAccess = minimumRequiredAccessForCorrectAPIExporting(reference, user, desc, pub);
            requiredAccessCause = desc[0];
            requiredPublished = pub[0];
        }

        public EAccess getRequiredAccess() {
            synchronized (this) {
                if (requiredAccess == null) {
                    calcRequiredAccess();
                }
                return requiredAccess;
            }
        }

        public boolean getRequiredPublished() {
            synchronized (this) {
                if (requiredAccess == null) {
                    calcRequiredAccess();
                }
                return requiredPublished;
            }
        }

        public String getRequiredAccessDescription() {
            synchronized (this) {
                getRequiredAccess();
                return requiredAccessCause;
            }
        }
    }
    private final Map<Definition, List<Usage>> sameModuleUsers = new HashMap<Definition, List<Usage>>();
    private final Map<Definition, List<Usage>> sameClassUsers = new HashMap<Definition, List<Usage>>();
    private final Map<Definition, List<Usage>> sameLayerUsers = new HashMap<Definition, List<Usage>>();
    private final Map<Definition, List<Usage>> outerUsers = new HashMap<Definition, List<Usage>>();
    private final Map<Definition, List<Usage>> userFuncs = new HashMap<Definition, List<Usage>>();
    private final Definition target;
    private final Module targetModule;
    private final Layer targetLayer;
    private final AdsClassDef targetClass;

    ReferenceInfo(final Definition target) {
        this.target = target;
        this.targetModule = target.getModule();
        this.targetLayer = targetModule.getSegment().getLayer();
        this.targetClass = getOwnerClass(target);
    }

    public Definition getTarget() {
        return target;
    }

    private static AdsClassDef getOwnerClass(final RadixObject object) {
        for (RadixObject obj = object.getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsClassDef) {
                return (AdsClassDef) obj;
            }
        }
        return null;
    }

    public void addUser(final RadixObject user) {
        addUser(user, null);
    }

    public void addUser(final RadixObject user, final Usage.Kind kind) {
        if (!user.isInBranch()) {
            return;
        }
        final Map<Definition, List<Usage>> list;
        if (user.isInBranch()) {
            if (targetClass != null && getOwnerClass(user) == targetClass) {
                list = sameClassUsers;
            } else {
                final Module module = user.getModule();
                if (module == targetModule) {
                    list = sameModuleUsers;
                } else {
                    final Segment segment = module.getSegment();
                    if (segment instanceof UdsSegment) {
                        list = userFuncs;
                    } else {
                        if (segment.getLayer() == targetLayer) {
                            list = sameLayerUsers;
                        } else {
                            list = outerUsers;
                        }
                    }
                }
            }
            final Usage usage = kind == null ? new Usage(target, user) : new Usage(target, user, kind);
            final Definition owner = usage.getOwnerDefinition();
            if (owner != null) {
                List<Usage> usages = list.get(owner);
                if (usages == null) {
                    usages = new LinkedList<Usage>();
                    list.put(owner, usages);
                }
                usages.add(usage);
            }
        }
    }

    public Map<Definition, List<Usage>> getUsages(final Usage.Type type) {
        if (type == null) {
            Map<Definition, List<Usage>> result = new HashMap<Definition, List<Usage>>();
            result.putAll(sameClassUsers);
            Map<Definition, List<Usage>>[] maps = new Map[]{
                sameModuleUsers,
                sameLayerUsers,
                outerUsers,
                userFuncs
            };
            for (Map<Definition, List<Usage>> map : maps) {
                for (Map.Entry<Definition, List<Usage>> e : map.entrySet()) {
                    List<Usage> usages = result.get(e.getKey());
                    if (usages != null) {
                        usages.addAll(e.getValue());
                    } else {
                        result.put(e.getKey(), e.getValue());
                    }
                }
            }
            return result;
        }
        switch (type) {
            case SAME_CLASS:
                return Collections.unmodifiableMap(sameClassUsers);
            case SAME_MODULE:
                return Collections.unmodifiableMap(sameModuleUsers);
            case SAME_LAYER:
                return Collections.unmodifiableMap(sameLayerUsers);
            case USER_FUNC:
                return Collections.unmodifiableMap(userFuncs);
            default:
                return Collections.unmodifiableMap(outerUsers);
        }
    }

    public boolean isUsed(final Usage.Type type) {
        switch (type) {
            case SAME_CLASS:
                return !sameClassUsers.isEmpty();
            case SAME_MODULE:
                return !sameModuleUsers.isEmpty();
            case SAME_LAYER:
                return !sameLayerUsers.isEmpty();
            case USER_FUNC:
                return !userFuncs.isEmpty();
            default:
                return !outerUsers.isEmpty();
        }
    }
    private String usageDescription = null;
    private boolean unused;

    public boolean isUnused() {
        synchronized (this) {
            return unused;
        }
    }

    public String getUsageDescription() {
        isUnused();
        return usageDescription;
    }

    private static EAccess defaultAccess(final RadixObject reference) {
        return reference instanceof AdsDefinition ? ((AdsDefinition) reference).getMinimumAccess() : EAccess.PUBLIC;
    }

    private static boolean defaultPublished(final RadixObject reference) {
        return reference instanceof AdsDefinition ? false : true;
    }

    /**
     * Возвращает модификатор доступа с учетом модификатора родительской
     * дефиниции.
     */
    private static EAccess getAccessFor(AdsDefinition definition) {
        final AdsDefinition ownerDef = definition.getOwnerDef();

        if (ownerDef != null) {
            return EAccess.min(definition.getAccessMode(), ownerDef.getAccessMode());
        }
        return definition.getAccessMode();
    }

    /**
     * Поиск корневого общего объекта для значения (reference) и места его
     * ипользования (user). Это может быть либо Дефиниция, либо Класс, либо
     * Модуль, либо Слой. Если общего объекта не найдено, вернется
     * <tt>null</tt>.
     */
    private static RadixObject findCommonDefinition(final Definition reference, final RadixObject user) {
        if (reference == user) {
            return reference;
        }

        final Definition userDefinition = user.getDefinition();
        if (userDefinition != null) {
            if (userDefinition == reference) {
                return reference;
            }

            final AdsClassDef userClass = RadixObjectsUtils.findContainer(userDefinition, AdsClassDef.class);
            final AdsClassDef referenceClass = RadixObjectsUtils.findContainer(reference, AdsClassDef.class);

            if (userClass != null && referenceClass != null) {
                if (userClass == referenceClass) {
                    return referenceClass;
                }

                final List<AdsClassDef> userClassesChain = userClass.getNestedClassesChain(true, false);
                final List<AdsClassDef> referenceClassesChain = userClass.getNestedClassesChain(true, false);

                for (final AdsClassDef refClass : referenceClassesChain) {
                    for (final AdsClassDef usrClass : userClassesChain) {
                        if (refClass == usrClass) {
                            return refClass;
                        }
                    }
                }
            }
        }

        final Module referenceModule = reference.getModule();
        final Module userModule = user.getModule();
        if (referenceModule == userModule) {
            return referenceModule;
        }
        final Layer referenceLayer = referenceModule.getLayer();
        final Layer userLayer = userModule.getLayer();

        if (referenceLayer == userLayer) {
            return referenceLayer;
        }
        return null;
    }

    public static EAccess minimumRequiredAccessForCorrectAPIExporting(final Definition reference, final RadixObject user, String[] desc, boolean[] published) {
        EAccess[] requiredAccess = new EAccess[]{EAccess.PRIVATE};
        String[] requiredAccessCause = new String[1];
        boolean[] requiredPublished = new boolean[1];

        if (!reference.isInBranch()) {
            requiredAccess[0] = defaultAccess(reference);
            requiredPublished[0] = defaultPublished(reference);
            requiredAccessCause[0] = "Referenced object is not in branch. Accessibility can not be computed correctly";
        } else if (!user.isInBranch()) {
            requiredAccess[0] = defaultAccess(user);
            requiredPublished[0] = defaultPublished(user);
            requiredAccessCause[0] = "Referencing object is not in branch. Accessibility can not be computed correctly";
        } else {

            Module thisModule = getModule(user);
            final Module refModule = getModule(reference);
            if (thisModule == null || thisModule.getLayer() == null || refModule == null || refModule.getLayer() == null) {
                requiredAccess[0] = EAccess.PROTECTED;
                requiredPublished[0] = true;
                requiredAccessCause[0] = "Public and published access is required because of usage from user defined level";
            } else /* if (thisModule instanceof UdsModule) {
             requiredAccess[0] = EAccess.PROTECTED;
             requiredPublished[0] = true;
             requiredAccessCause[0] = "Public and published access is required because of usage from user defined level";
             } else */ {

                final Layer thisLayer = thisModule.getLayer();
                final Layer refLayer = refModule.getLayer();
                final RadixObject commonDefinition = findCommonDefinition(reference, user);

                if (user instanceof Inheritance) {
                    final AdsClassDef clazz = ((Inheritance) user).getOwnerClass();

                    if (commonDefinition instanceof Module) {
                        switch (clazz.getAccessMode()) {
                            case PUBLIC:
                                requiredAccess[0] = EAccess.PUBLIC;
                                requiredPublished[0] = clazz.isPublished();
                                if (clazz.isPublished()) {
                                    requiredAccessCause[0] = "Cross layer share. Public and published access is required, because " + clazz.getQualifiedName() + " is published";
                                } else {
                                    requiredAccessCause[0] = "Same layer share. Public access is required because " + clazz.getQualifiedName() + " is public and not published";
                                }
                                break;
                            default:
                                requiredAccess[0] = EAccess.DEFAULT;
                                requiredPublished[0] = false;
                                requiredAccessCause[0] = "Same module share. Internal access is required";
                                break;
                        }
                    } else if (commonDefinition instanceof Layer) {
                        switch (clazz.getAccessMode()) {
                            case PUBLIC:
                                requiredAccess[0] = EAccess.PUBLIC;
                                requiredPublished[0] = clazz.isPublished();
                                if (clazz.isPublished()) {
                                    requiredAccessCause[0] = "Cross layer share. Public and published access is required, because " + clazz.getQualifiedName() + " is published";
                                } else {
                                    if (thisModule instanceof AdsModule && ((AdsModule) thisModule).isCompanionOf(refModule)) {
                                        requiredAccess[0] = EAccess.DEFAULT;
                                        requiredAccessCause[0] = "Internal access is required because module " + thisModule.getQualifiedName() + " is companion of module " + refModule.getQualifiedName();
                                    } else {
                                        requiredAccessCause[0] = "Same layer share. Public access is required because " + clazz.getQualifiedName() + " is not published";
                                    }
                                }
                                break;
                            default:
                                if (thisModule instanceof AdsModule && ((AdsModule) thisModule).isCompanionOf(refModule)) {
                                    requiredAccess[0] = EAccess.DEFAULT;
                                    requiredPublished[0] = false;
                                    requiredAccessCause[0] = "Internal access is required because module " + thisModule.getQualifiedName() + " is companion of module " + refModule.getQualifiedName();
                                } else {
                                    requiredAccess[0] = EAccess.PUBLIC;
                                    requiredPublished[0] = false;
                                    requiredAccessCause[0] = "Same layer share. Public access is required because " + clazz.getQualifiedName() + " is not published";
                                }
                                break;
                        }
                    } else if (commonDefinition instanceof AdsClassDef) {
                        requiredAccess[0] = EAccess.PRIVATE;
                        requiredPublished[0] = false;
                    } else {
                        requiredAccess[0] = EAccess.PUBLIC;
                        requiredPublished[0] = true;
                        requiredAccessCause[0] = "Cross layer share. Public and published access is required";
                    }
                } else if (user instanceof Value || user instanceof ThrowsListItem || user instanceof AdsCommandDef || user instanceof AdsCommandDef.CommandData || user instanceof MsdlField) {
                    if (reference instanceof AdsDefinition) {
                        final AdsDefinition ref = (AdsDefinition) reference;
                        final AdsDefinition valueOwner = getOwnerDefinition(user);
                        final AdsDefinition thisRoot = getAccessRoot(user);
                        final AdsDefinition refRoot = getAccessRoot(reference);

                        final EAccess valueOwnerAccessMode = getAccessFor(valueOwner);

                        if (thisModule == refModule || ((thisModule instanceof AdsModule) && ((AdsModule) thisModule).isCompanionOf(refModule))) {//same module,no share required
                            switch (valueOwnerAccessMode) {
                                case PRIVATE:
                                    if (thisRoot == refRoot) {
                                        requiredAccess[0] = ref.getMinimumAccess();
                                        requiredPublished[0] = false;
                                        requiredAccessCause[0] = "Same class share. Minimum available access required";
                                    } else {
                                        requiredAccess[0] = EAccess.DEFAULT;
                                        requiredPublished[0] = false;
                                        requiredAccessCause[0] = "Same module share. Internal access required";
                                    }
                                    break;
                                case DEFAULT:
                                    requiredAccess[0] = EAccess.DEFAULT;
                                    requiredPublished[0] = false;
                                    requiredAccessCause[0] = "Same module share. Internal access is required";
                                    break;
                                case PROTECTED:
                                    switch (thisRoot.getAccessMode()) {
                                        case DEFAULT:
                                        case PROTECTED:
                                            requiredAccess[0] = EAccess.DEFAULT;
                                            requiredAccessCause[0] = "Same module share. Internal access is required";
                                            requiredPublished[0] = false;
                                            break;
                                        case PUBLIC:
                                            if (thisRoot.isFinal()) {
                                                requiredAccess[0] = EAccess.DEFAULT;
                                                requiredPublished[0] = false;
                                                requiredAccessCause[0] = "Same module share, because " + thisRoot.getQualifiedName() + " is final and " + valueOwner.getQualifiedName() + " has protected access. Internal access is required";
                                            } else {
                                                if (thisRoot.isPublished() && valueOwner.isPublished()) {
                                                    requiredAccess[0] = EAccess.PROTECTED;
                                                    requiredPublished[0] = true;
                                                    requiredAccessCause[0] = "Cross layer share, because " + thisRoot.getQualifiedName() + " is not final and published. Protected and published access is required";
                                                } else {

                                                    requiredAccess[0] = EAccess.PROTECTED;
                                                    requiredAccessCause[0] = "Same layer share, because " + thisRoot.getQualifiedName() + " is not final. Protected access is required";
                                                    requiredPublished[0] = false;

                                                }
                                            }
                                            break;
                                    }
                                    break;
                                case PUBLIC:
                                    if (valueOwner.isPublished()) {
                                        switch (thisRoot.getAccessMode()) {
                                            case DEFAULT:
                                            case PROTECTED:
                                                requiredAccess[0] = EAccess.DEFAULT;
                                                requiredPublished[0] = false;
                                                requiredAccessCause[0] = "Same module share because " + thisRoot.getQualifiedName() + " has internal access. Internal access is required";
                                                break;
                                            case PUBLIC:
                                                if (thisRoot.isPublished()) {
                                                    requiredAccess[0] = EAccess.PUBLIC;
                                                    requiredPublished[0] = true;
                                                    requiredAccessCause[0] = "Cross layer share, because " + valueOwner.getQualifiedName() + " and " + thisRoot.getQualifiedName() + " are published. Public and published access is required";
                                                } else {
                                                    requiredAccess[0] = EAccess.PUBLIC;
                                                    requiredPublished[0] = false;
                                                    requiredAccessCause[0] = "Same layer share, because " + valueOwner.getQualifiedName() + " and " + thisRoot.getQualifiedName() + " are public. Public access is required";
                                                }
                                                break;
                                        }
                                    } else {
                                        switch (thisRoot.getAccessMode()) {
                                            case DEFAULT:
                                            case PROTECTED:
                                                requiredAccess[0] = EAccess.DEFAULT;
                                                requiredPublished[0] = false;
                                                requiredAccessCause[0] = "Same module share because " + thisRoot.getQualifiedName() + " has internal access. Internal access is required";
                                                break;
                                            case PUBLIC:
                                                requiredAccess[0] = EAccess.PUBLIC;
                                                requiredPublished[0] = false;
                                                requiredAccessCause[0] = "Same layer share. Public access is required";
                                                break;
                                        }
                                    }
                                    break;
                            }
                        } else {
                            if (thisLayer == refLayer) {
                                switch (valueOwner.getAccessMode()) {
                                    case PROTECTED:
                                    case PUBLIC:
                                        if (valueOwnerAccessMode == EAccess.PROTECTED && thisRoot.isFinal()) {
                                            requiredPublished[0] = false;
                                            requiredAccess[0] = EAccess.PUBLIC;
                                            requiredAccessCause[0] = "Same layer share, because " + valueOwner.getQualifiedName() + " has protected access and " + thisRoot.getQualifiedName() + " is final. Public access is required";
                                        } else {
                                            if (thisRoot.isPublished()) {
                                                if (valueOwner.isPublished()) {
                                                    requiredPublished[0] = true;
                                                    requiredAccess[0] = EAccess.PUBLIC;
                                                    requiredAccessCause[0] = "Cross layer share, because " + valueOwner.getQualifiedName() + " and " + thisRoot.getQualifiedName() + " are published. Public and published access is required";
                                                } else {
                                                    requiredPublished[0] = false;
                                                    requiredAccess[0] = EAccess.PUBLIC;
                                                    requiredAccessCause[0] = "Same layer share, because " + valueOwner.getQualifiedName() + " is not published. Public access is required";
                                                }
                                            } else {
                                                requiredPublished[0] = false;
                                                requiredAccess[0] = EAccess.PUBLIC;
                                                requiredAccessCause[0] = "Same layer share, because " + valueOwner.getQualifiedName() + " and " + thisRoot.getQualifiedName() + " are public. Public access is required";
                                            }
                                        }
                                        break;
                                    default:
                                        requiredPublished[0] = false;
                                        requiredAccess[0] = EAccess.PUBLIC;
                                        requiredAccessCause[0] = "Same layer share, because " + valueOwner.getQualifiedName() + " is not published. Public access is required";
                                }
                            } else {
                                requiredAccess[0] = EAccess.PUBLIC;
                                requiredPublished[0] = true;
                                requiredAccessCause[0] = "Cross layer share, because " + valueOwner.getQualifiedName() + " in different layer. Public and published access is required";
                            }
                        }
                    } else {
                        processSimpleUsage(reference, user, thisModule, refModule, thisLayer, refLayer, requiredAccess, requiredAccessCause, requiredPublished);
                    }
                } else {
                    processSimpleUsage(reference, user, thisModule, refModule, thisLayer, refLayer, requiredAccess, requiredAccessCause, requiredPublished);
                }
            }
        }

        if (desc != null && desc.length > 0) {
            desc[0] = requiredAccessCause[0];
        }
        published[0] = requiredPublished[0];
        return requiredAccess[0];
    }

    private static void processSimpleUsage(Definition reference, RadixObject user, Module thisModule, Module refModule, Layer thisLayer, Layer refLayer, EAccess[] requiredAccess, String[] requiredAccessCause, boolean[] requiredPublished) {
        if (thisModule == refModule) {
            Definition thisRoot = getAccessRoot(user);
            Definition refRoot = getAccessRoot(reference);
            if (thisRoot == null || refRoot == null) {
                return;
            }
            if (thisRoot == refRoot) {
                requiredAccess[0] = defaultAccess(reference);
                requiredPublished[0] = defaultPublished(reference);
                requiredAccessCause[0] = "Internal usage";
            } else {
                if (reference instanceof AdsPresentationSlotMethodDef && user instanceof AdsUIConnection) {
                    AdsPresentationSlotMethodDef method = (AdsPresentationSlotMethodDef) reference;
                    Set<AdsUIItemDef> widgets = method.findWidgets();
                    if (widgets != null) {
                        for (AdsUIItemDef widget : widgets) {
                            AdsAbstractUIDef ui = widget.getOwnerUIDef();
                            if (ui != null && ui.isParentOf(method)) {
                                requiredAccess[0] = EAccess.PRIVATE;
                                requiredPublished[0] = false;
                                requiredAccessCause[0] = "Used by widget. Private access allowed";
                                return;
                            }
                        }
                    }

                }
                requiredAccess[0] = EAccess.DEFAULT;
                requiredPublished[0] = false;
                requiredAccessCause[0] = "Same module usage. Internal access required";
            }
        } else {
            if (reference instanceof AdsClassMember) {
                AdsClassMember userMember = findClassMember(user);
                if (userMember != null) {
                    AdsClassDef refClass = ((AdsClassMember) reference).getOwnerClass();
                    AdsClassDef userClass = userMember.getOwnerClass();
                    boolean isSubclass = userClass.getInheritance().isSubclassOf(refClass);
                    if (isSubclass || (userClass.getId() == refClass.getId() && thisLayer != refLayer)) {
                        requiredAccess[0] = EAccess.PROTECTED;
                        requiredPublished[0] = thisLayer != refLayer;
                        if (requiredPublished[0]) {
                            requiredAccessCause[0] = "Different layer " + (isSubclass ? "subclass" : "overwrite") + " usage. Protected and published access is required";
                            requiredPublished[0] = true;
                        } else {
                            requiredAccessCause[0] = "Same layer subclass usage. Protected access is required";
                            requiredPublished[0] = false;
                        }
                        return;
                    }
                }
            }
            if (thisLayer == refLayer) {
                if (thisModule instanceof AdsModule && ((AdsModule) thisModule).isCompanionOf(refModule)) {
                    requiredAccess[0] = EAccess.DEFAULT;
                    requiredPublished[0] = false;
                    requiredAccessCause[0] = "Companion module usage by " + user.getQualifiedName() + ". Public access required";
                } else {
                    requiredAccess[0] = EAccess.PUBLIC;
                    requiredPublished[0] = false;
                    requiredAccessCause[0] = "Cross module usage by " + user.getQualifiedName() + ". Public access required";
                }
            } else {
                requiredAccess[0] = EAccess.PUBLIC;
                requiredPublished[0] = true;
                requiredAccessCause[0] = "Cross layer usage " + user.getQualifiedName() + ". Public and published access required";
            }
        }
    }

    private static void checkInheritableItemsForFinalableUser(Definition reference, RadixObject user, String elementDesc, String userDesk, EAccess[] requiredAccess, String[] requiredAccessCause) {
        AdsClassMember ref = (AdsClassMember) reference;
        AdsClassDef propOwner = ref.getOwnerClass();
        AdsClassMember member = findClassMember(user);

        if (member != null && member.isFinal()) {
            AdsClassDef sprOwner = member.getOwnerClass();
            if (sprOwner == propOwner) {
                requiredAccess[0] = EAccess.PRIVATE;
                requiredAccessCause[0] = elementDesc + " in final " + userDesk + " of same class";
            } else {
                requiredAccess[0] = EAccess.PROTECTED;
                requiredAccessCause[0] = elementDesc + " in final " + userDesk + " of subclass";
            }
        } else {
            requiredAccess[0] = EAccess.PROTECTED;
            requiredAccessCause[0] = elementDesc + " may be inherited in " + userDesk + "s of subclasses";
        }
    }

    private static AdsClassMember findClassMember(RadixObject obj) {
        for (RadixObject o = obj; o != null; o = o.getContainer()) {
            if (o instanceof AdsClassMember) {
                return (AdsClassMember) o;
            }
        }
        return null;
    }

    private static AdsDefinition getAccessRoot(RadixObject obj) {
        RadixObject o = obj;
        while (o != null) {
            if (o instanceof AdsClassDef && !((AdsClassDef) o).isNested()) {
                return (AdsClassDef) o;
            } else if (o instanceof AdsDefinition && ((AdsDefinition) o).isTopLevelDefinition()) {
                return (AdsDefinition) o;
            }
            o = o.getContainer();
        }
        return null;
    }

    private static Module getModule(RadixObject obj) {
        return obj.getModule();
    }

    private static AdsDefinition getOwnerDefinition(RadixObject user) {
        RadixObject obj = user;
        while (obj != null) {
            if (obj instanceof AdsDefinition) {
                return (AdsDefinition) obj;
            }
            obj = obj.getContainer();
        }
        return null;
    }
}

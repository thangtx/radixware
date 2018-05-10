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

package org.radixware.kernel.common.defs.ads.clazz;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.agents.ProxyAgent;
import org.radixware.schemas.adsdef.ClassDefinition;

/**
 * Type of classes, which can be used only for implementation objects, which are
 * not classes, but have a similar behavior. For example, EnumClassFields,
 * PresentationProperties.
 *
 */
public abstract class AdsEmbeddedClassDef extends AdsClassDef {

    public static abstract class ClassAgent<T> extends ProxyAgent<T, AdsEmbeddedClassDef> {

        public static boolean synchronize(AdsClassDef temporary, AdsClassDef actual) {
            if (actual == null) {
                return false;
            }
            if (temporary == null) {
                return true;
            }
            try {
                if (actual != temporary) {
                    final ClipboardSupport.DefaultDuplicationResolver duplicationResolver = new ClipboardSupport.DefaultDuplicationResolver();
                    Transferable transferable = ClipboardSupport.createTransferable(new ArrayList<RadixObject>(temporary.getMethodGroup().getChildGroups().list()), ETransferType.COPY);
                    actual.getMethodGroup().getChildGroups().getClipboardSupport().paste(transferable, duplicationResolver);

                    transferable = ClipboardSupport.createTransferable(new ArrayList<RadixObject>(temporary.getPropertyGroup().getChildGroups().list()), ETransferType.COPY);
                    actual.getPropertyGroup().getChildGroups().getClipboardSupport().paste(transferable, duplicationResolver);

                    transferable = ClipboardSupport.createTransferable(new ArrayList<RadixObject>(temporary.getMethods().getLocal().list()), ETransferType.COPY);
                    actual.getClipboardSupport().paste(transferable, duplicationResolver);

                    transferable = ClipboardSupport.createTransferable(new ArrayList<RadixObject>(temporary.getProperties().getLocal().list()), ETransferType.COPY);
                    actual.getClipboardSupport().paste(transferable, duplicationResolver);
                }
                return true;
            } catch (Exception e) {
                Logger.getLogger(ClassAgent.class.getName()).log(Level.WARNING, null, e);
                return false;
            }
        }
        private final IClassInclusive classInclusive;

        public ClassAgent(IClassInclusive classInclusive) {
            this.classInclusive = classInclusive;
        }

        @Override
        protected boolean sync(AdsEmbeddedClassDef temporary, AdsEmbeddedClassDef actual) {
            synchronize(temporary, actual);
            return true;
        }

        @Override
        protected boolean isUsed(AdsEmbeddedClassDef temporary) {
            return temporary != null && temporary.isUsed();
        }

        @Override
        protected AdsEmbeddedClassDef findActual(boolean create) {
            return classInclusive.getLocalEmbeddedClass(create);
        }
    }

    protected AdsEmbeddedClassDef(Id id, String name) {
        super(id, name);
    }

    protected AdsEmbeddedClassDef(AdsClassDef source) {
        super(source);
    }

    protected AdsEmbeddedClassDef(ClassDefinition xClass) {
        super(xClass);
    }

    protected AdsEmbeddedClassDef(Id id, ClassDefinition xClass) {
        super(id, xClass);
    }

    @Override
    public boolean isNested() {
        return true;
    }

    @Override
    public boolean isInner() {
        return true;
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }

    @Override
    public boolean isSaveable() {
        return false;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        getMethods().visit(visitor, provider);
        getProperties().visit(visitor, provider);
        getMethodGroup().visit(visitor, provider);
        getPropertyGroup().visit(visitor, provider);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (IdPrefixes.isAdsMethodId(id)) {
            return getMethods().findById(id, ExtendableDefinitions.EScope.ALL);
        } else if (IdPrefixes.isAdsPropertyId(id)) {
            return getProperties().findById(id, ExtendableDefinitions.EScope.ALL);
        } else if (IdPrefixes.isAdsNestedClassId(id)) {
            return null;
        } else {
            return super.findComponentDefinition(id);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsClassDef> getHierarchy() {
        return new EmbeddedClassHierarchy();
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        setOverwrite(true);
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        final IAdsTypeSource container = RadixObjectsUtils.findContainer(this.getContainer(), IAdsTypeSource.class);

        if (container != null) {
            return container.getTypeUsageEnvironments();
        }

        return Collections.<ERuntimeEnvironmentType>emptySet();
    }

    public boolean isUsed() {
        return getMethods().getLocal().size() > 0
                || getProperties().getLocal().size() > 0
                || getMethodGroup().getChildGroups().size() > 0
                || getPropertyGroup().getChildGroups().size() > 0;
    }

    public IClassInclusive getHostObject() {
        return RadixObjectsUtils.findContainer(this.getContainer(), IClassInclusive.class);
    }

    private class EmbeddedClassHierarchy extends Hierarchy<AdsClassDef> {

        public EmbeddedClassHierarchy() {
            super(AdsEmbeddedClassDef.this);
        }

        @Override
        public SearchResult<AdsClassDef> findOverridden() {
            return SearchResult.empty();
        }

        @Override
        public SearchResult<AdsClassDef> findOverwritten() {

            if (getHostObject() instanceof AdsDefinition) {
                final SearchResult<AdsDefinition> result = ((AdsDefinition) getHostObject()).getHierarchy().findOverwritten();
                final List<AdsClassDef> selectInLevel = searchInLevel(result.all());

                if (!selectInLevel.isEmpty()) {
                    return SearchResult.list(selectInLevel);
                }
            }
            return SearchResult.<AdsClassDef>empty();
        }
    }

    public static List<AdsClassDef> searchInLevel(List<? extends AdsDefinition> level) {
        final List<AdsClassDef> result = new ArrayList<>();

        for (final AdsDefinition def : level) {
            if (def instanceof IClassInclusive) {
                final AdsClassDef embeddedClass = ((IClassInclusive) def).getLocalEmbeddedClass(false);
                if (embeddedClass != null) {
                    result.add(embeddedClass);
                } else {
                    result.addAll(searchInLevel(def.getHierarchy().findOverwritten().all()));
                }
            }
        }
        return result;
    }
}

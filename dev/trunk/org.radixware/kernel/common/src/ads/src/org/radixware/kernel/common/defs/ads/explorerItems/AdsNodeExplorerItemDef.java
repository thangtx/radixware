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

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ExplorerItemDefinition;

/**
 * Base class for non-paragraph explorer items
 *
 */
public abstract class AdsNodeExplorerItemDef extends AdsExplorerItemDef implements IInheritableTitledDefinition {

    protected Id classId;
    protected EnumSet<EExplorerItemAttrInheritance> inheritanceMask;
    private boolean isVisibleInTree;
    private ERuntimeEnvironmentType clientEnv;

    protected AdsNodeExplorerItemDef(ExplorerItemDefinition xDef) {
        super(xDef);
        this.classId = xDef.getClassId();
        this.inheritanceMask = EExplorerItemAttrInheritance.fromBitField(xDef.getInheritanceMask());
        this.isVisibleInTree = xDef.getIsVisibleInTree();
        if (xDef.getClientEnvironment() != null) {
            this.clientEnv = xDef.getClientEnvironment();
        } else {
            this.clientEnv = ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        ExplorerItems explorerItems = getOwnerExplorerItems();
        if (explorerItems == null) {
            return clientEnv;
        } else {
            Definition def = explorerItems.getOwnerDefinition();
            ERuntimeEnvironmentType ownerEnv = null;
            if (def instanceof AdsEditorPresentationDef) {
                ownerEnv = ((AdsEditorPresentationDef) def).getClientEnvironment();
            } else if (def instanceof AdsParagraphExplorerItemDef) {
                ownerEnv = ((AdsParagraphExplorerItemDef) def).getClientEnvironment();
            }
            if (ownerEnv == null) {
                return clientEnv;
            } else {
                if (ownerEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return clientEnv;
                } else {
                    return ownerEnv;
                }
            }
        }
    }

    @Override
    public boolean canChangeClientEnvironment() {
        ExplorerItems explorerItems = getOwnerExplorerItems();
        if (explorerItems == null) {
            return true;
        } else {
            Definition def = explorerItems.getOwnerDefinition();
            ERuntimeEnvironmentType ownerEnv = null;
            if (def instanceof AdsEditorPresentationDef) {
                ownerEnv = ((AdsEditorPresentationDef) def).getClientEnvironment();
            } else if (def instanceof AdsParagraphExplorerItemDef) {
                ownerEnv = ((AdsParagraphExplorerItemDef) def).getClientEnvironment();
            }
            if (ownerEnv == null) {
                return true;
            } else {
                if (ownerEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public void setClientEnvironment(ERuntimeEnvironmentType e) {
        clientEnv = e;
        setEditState(EEditState.MODIFIED);
    }

    protected AdsNodeExplorerItemDef() {
        this((Id) null);
    }

    protected AdsNodeExplorerItemDef(Id id) {
        super(id == null ? Id.Factory.newInstance(EDefinitionIdPrefix.EXPLORER_ITEM) : id, "EI");
        this.inheritanceMask = EnumSet.allOf(EExplorerItemAttrInheritance.class);
        this.isVisibleInTree = true;
        this.classId = null;
        this.clientEnv = ERuntimeEnvironmentType.COMMON_CLIENT;
    }

    public boolean isRestrictionInherited() {
        return inheritanceMask.contains(EExplorerItemAttrInheritance.RESTRICTION);
    }

    public boolean setRestrictionInherited(boolean inherit) {
        if (inherit) {
            if (!inheritanceMask.contains(EExplorerItemAttrInheritance.RESTRICTION)) {
                inheritanceMask.add(EExplorerItemAttrInheritance.RESTRICTION);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        } else {
            if (inheritanceMask.contains(EExplorerItemAttrInheritance.RESTRICTION)) {
                inheritanceMask.remove(EExplorerItemAttrInheritance.RESTRICTION);
                setEditState(EEditState.MODIFIED);
            }
            return true;
        }
    }

    @Override
    public boolean isTitleInherited() {
        return inheritanceMask.contains(EExplorerItemAttrInheritance.TITLE);
    }

    @Override
    public boolean setTitle(EIsoLanguage language, String title) {
        assert !isTitleInherited() : "Can't set title id. Title is inherited.";
        if (!isTitleInherited()) {
            return super.setTitle(language, title);
        }
        return false;
    }

    @Override
    public void setTitleId(Id titleId) {
        assert !isTitleInherited() : "Can't set title id. Title is inherited.";
        if (!isTitleInherited()) {
            super.setTitleId(titleId);
        }
    }

    private class ReferencedEntityClassLink extends DefinitionLink<AdsEntityObjectClassDef> {

        @Override
        protected AdsEntityObjectClassDef search() {
            AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(AdsNodeExplorerItemDef.this).findById(classId).get();
            if (def instanceof AdsEntityObjectClassDef) {
                return (AdsEntityObjectClassDef) def;
            } else {
                //did not found in dependent modules; perform through-branch search
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

                return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<AdsEntityObjectClassDef>() {
                    @Override
                    public void accept(HierarchyWalker.Controller<AdsEntityObjectClassDef> controller, Layer layer) {
                        RadixObject result = layer.find(new VisitorProvider() {
                            @Override
                            public boolean isTarget(RadixObject radixObject) {
                                return radixObject instanceof AdsEntityObjectClassDef && ((AdsEntityObjectClassDef) radixObject).getId() == classId;
                            }
                        });
                        if (result instanceof AdsEntityObjectClassDef) {
                            controller.setResultAndStop((AdsEntityObjectClassDef) result);
                        }
                    }
                });
            }
        }
    }
    private final ReferencedEntityClassLink entityClassLink = new ReferencedEntityClassLink();

    public AdsEntityObjectClassDef findReferencedEntityClass() {
        return entityClassLink.find();
    }

    public boolean isVisibleInTree() {
        return isVisibleInTree;
    }

    public void setVisibleInTree(boolean isVisible) {
        this.isVisibleInTree = isVisible;
        fireNameChange();
        setEditState(EEditState.MODIFIED);
    }

    public void appendTo(ExplorerItemDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setInheritanceMask(EExplorerItemAttrInheritance.toBitField(inheritanceMask));
        xDef.setIsVisibleInTree(isVisibleInTree);
        if (classId != null) {
            xDef.setClassId(classId);
        }
        if (clientEnv == ERuntimeEnvironmentType.EXPLORER || clientEnv == ERuntimeEnvironmentType.WEB) {
            xDef.setClientEnvironment(clientEnv);
        }
    }

    public Id getClassId() {
        return classId;
    }

    public void setClassId(Id id) {
        this.classId = id;
        setEditState(EEditState.MODIFIED);
    }

    public EnumSet<EExplorerItemAttrInheritance> getInheritanceMask() {
        return EnumSet.copyOf(inheritanceMask);
    }

    @Override
    protected void collectDependencesImpl(final boolean direct, final boolean forModule, final List<Definition> list) {
        if (!direct && !forModule) {
            final AdsEntityObjectClassDef clazz = findReferencedEntityClass();
            if (clazz != null) {
                list.add(clazz);
            }
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.EXPLORER_ITEM;
    }
}

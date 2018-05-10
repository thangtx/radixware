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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType.EntityGroupModelType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.components.selector.DefinitionSelector;


class ChooseRadixModelStep extends ChooseTypeFromListStep<ModelSelector, Definition> {

    @Override
    void open(TypeWizard.Settings settings) {

        Object radixClass = settings.typeModel.getObject();

        assert (radixClass != null
                && (radixClass instanceof IAdsPresentableClass
                || radixClass instanceof AdsParagraphExplorerItemDef
                || radixClass instanceof AdsCustomDialogDef));

        getEditor().open(settings, currentValue);

        fireChange();
    }

    @Override
    void selectionChanged(Definition newValue) {
        isComplete = newValue != null;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(ChooseRadixModelStep.class, "TypeWizard-ChooseRadixModelStep-DisplayName");
    }

    @Override
    protected ModelSelector createEditor() {
        ModelSelector modelSelector = new ModelSelector();
        modelSelector.addSelectionListener(this);
        return modelSelector;
    }

    @Override
    public boolean hasNextStep() {
        return false;
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }

    @Override
    public void apply(TypeWizard.Settings settings) {

//        assert currentValue instanceof AdsModelClassDef;

        if (currentValue instanceof AdsClassDef) {
            settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance((AdsClassDef) currentValue));
        } else if (currentValue instanceof ClassInfo) {
            ClassInfo info = (ClassInfo) currentValue;
            AdsClassDef clazz = info.clazz;
            if (info.isGroup) {
                settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, clazz, EntityGroupModelType.TYPE_SUFFIX, 0));
            } else {
                settings.typeModel.setType(AdsTypeDeclaration.Factory.newInstance(clazz));
            }
        }
    }
}


final class ClassInfo extends org.radixware.kernel.common.defs.Definition {

    AdsClassDef clazz;
    boolean isGroup;

    public ClassInfo(AdsClassDef src, String suffix) {
        super(src.getId(), src.getName() + (suffix == null ? "" : suffix));
        this.clazz = src;
    }

    @Override
    public RadixIcon getIcon() {
        return clazz.getIcon();
    }

    @Override
    public String getToolTip() {
        return clazz.getToolTip();
    }

    @Override
    public String getTypeTitle() {
        return clazz.getTypeTitle();
    }

    @Override
    public String getTypesTitle() {
        return clazz.getTypesTitle();
    }

    @Override
    public Module getModule() {
        return clazz.getModule();
    }

    @Override
    public Definition getOwnerDefinition() {
        return clazz.getOwnerDefinition();
    }

    @Override
    public EDocGroup getDocGroup() {
        return clazz.getDocGroup();
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return clazz.getDocEnvironment();
    }
}
final class ModelSelector extends DefinitionSelector<TypeWizard.Settings> {

    @Override
    protected void updateList() {
        Definition context = (Definition) getContext().typeModel.getObject();

        ChooseDefinitionCfg cfg = null;
        if (context instanceof IAdsPresentableClass) {
            IAdsPresentableClass asClassDef = (IAdsPresentableClass) context;
            ModelVisitor visitor = new ModelVisitor();
            if (asClassDef instanceof AdsEntityObjectClassDef) {
                visitor.accept(new ClassInfo((AdsEntityObjectClassDef) context, "-DefaultModel"));
            }
            if (asClassDef instanceof AdsEntityClassDef) {
                ClassInfo info = new ClassInfo((AdsEntityObjectClassDef) context, "-DefaultGroupModel");
                info.isGroup = true;
                visitor.accept(info);
            }
            asClassDef.getPresentations().visitChildren(visitor, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsModelClassDef;
                }
            });
            cfg = ChooseDefinitionCfg.Factory.newInstance(visitor.models);
        } else if (context instanceof AdsCustomDialogDef) {
            AdsCustomDialogDef dialog = (AdsCustomDialogDef) context;
            List<AdsModelClassDef> pModel = new ArrayList<AdsModelClassDef>();
            pModel.add(dialog.getModelClass());
            cfg = ChooseDefinitionCfg.Factory.newInstance(pModel);
        } else if (context instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef paragraph = (AdsParagraphExplorerItemDef) context;
            List<AdsModelClassDef> pModel = new ArrayList<AdsModelClassDef>();
            pModel.add(paragraph.getModel());
            cfg = ChooseDefinitionCfg.Factory.newInstance(pModel);
        }
        definitionsPanel.open(cfg);
    }

    private static class ModelVisitor implements IVisitor {

        Collection<Definition> models;

        ModelVisitor() {
            models = new ArrayList<Definition>();
        }

        @Override
        public void accept(RadixObject radixObject) {
            models.add((Definition)radixObject);
        }
    }
}

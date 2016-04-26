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

package org.radixware.kernel.common.client.meta.explorerItems;

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EExplorerItemAttrInheritance;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Class for reference to {@link RadParagraphDef}.
 * <p>Explorer item of this type can have own {@link #getTitle()  title}.
 * All other attributes inherits from referenced paragraph.
 */
public final class RadParagraphLinkDef extends RadExplorerItemDef implements IExplorerItemsHolder {

    final private Id paragraphId;
    private RadParagraphDef paragraph = null;
    final boolean isTitleInherited;
    final private Id classId;

    /**
     * Explorer item constructor
     * @param id  explorer item identifier.
     * @param titleId identifier of explorer item`s title to display in explorer tree.
     * @param titleOwnerId  identifier of definition which is owner of this explorer item
     * (it may be {@link RadParagraphDef paragraph} or {@link RadEditorPresentationDef editor presentation} )
     * @param targetParagraphId identifier of referenced paragraph.
     * @param inheritanceMask bit mask to define is title inherited from referenced paragraph or not.
     */
    public RadParagraphLinkDef(
            final Id id,
            final Id ownerId, //owner paragraph or owner editor presentation
            final Id titleId,
            final Id targetParagraphId,
            final long inheritanceMask) {
        super(id, ownerId, titleId, 0, true);
        isTitleInherited = (inheritanceMask & EExplorerItemAttrInheritance.TITLE.getValue().longValue()) != 0;
        paragraphId = targetParagraphId;
        classId = ownerId;
    }

    /**
     * Returns definition of referenced paragraph.
     * @return referenced paragraph.
     */
    public final RadParagraphDef getTarget() {
        if (paragraph == null) {
            paragraph = getDefManager().getParagraphDef(paragraphId);
        }
        return paragraph;
    }

    //реализация ExplorerItem
    @Override
    public IModelDefinition getModelDefinition() {
        return this;
    }

    @Override
    public Id getModelDefinitionId() {
        return paragraphId;
    }

    @Override
    public Id getModelDefinitionClassId() {
        return classId;
    }

    @Override
    public boolean isValid() {
        try {
            return getTarget() != null;
        } catch (DefinitionError err) {
            return false;
        }
    }

    @Override
    public Icon getIcon() {
        return getTarget().getIcon();
    }

    @Override
    public boolean hasTitle() {
        return isTitleInherited ? getTarget().hasTitle() : super.hasTitle();
    }

    @Override
    public String getTitle() {
        if (isTitleInherited) {
            return getTarget().getTitle();
        } else {
            return super.getTitle();
        }
    }

    //реализация методов ModelDefinition
    @Override
    public Id getOwnerClassId() {
        return classId;
    }

    @Override
    public IView createStandardView(IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardParagraphEditor(environment);
    }

    @Override
    public ParagraphModel createModel(IContext.Abstract context) {
        return getTarget().createModel(context);
    }

    @Override
    public RadExplorerItems getChildrenExplorerItems() {
        return getTarget().getChildrenExplorerItems();
    }

    @Override
    public Id[] getChildrenExplorerItemsOrder(Id parentId) {
        return getTarget().getChildrenExplorerItemsOrder(parentId);
    }

    @Override
    public boolean isExplorerItemVisible(Id parentId, Id explorerItemId) {
        return getTarget().isExplorerItemVisible(parentId, explorerItemId);
    }       

    @Override
    public RadCommandDef getCommandDefById(Id id) {
        return getTarget().getCommandDefById(id);
    }

    @Override
    public boolean isCommandDefExistsById(Id id) {
        return getTarget().isCommandDefExistsById(id);
    }

    @Override
    public List<RadCommandDef> getEnabledCommands() {
        return getTarget().getEnabledCommands();
    }

    @Override
    public RadPropertyDef getPropertyDefById(Id id) {
        return getTarget().getPropertyDefById(id);
    }

    @Override
    public boolean isPropertyDefExistsById(Id id) {
        return getTarget().isPropertyDefExistsById(id);
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.NO_RESTRICTIONS;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "paragraph link %s refer to #%s, owner definition is #%s");
        return String.format(desc, super.getDescription(), paragraphId, classId);
    }
}

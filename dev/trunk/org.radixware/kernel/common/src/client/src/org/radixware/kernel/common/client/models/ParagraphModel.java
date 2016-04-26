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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphLinkDef;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.views.IParagraphEditor;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Id;

public abstract class ParagraphModel extends Model {

    public ParagraphModel(IClientEnvironment environment, RadParagraphDef paragraph) {
        super(environment, paragraph);
    }

    public RadParagraphDef getParagraphDef() {
        return (RadParagraphDef) getDefinition();
    }

    public IParagraphEditor getParagraphView() {
        return (IParagraphEditor) getView();
    }

    /**
     * Создать модель параграфа для дефиниции def.
     * @param def - дефиниция параграфа
     * @param holder  Модель дефиниция которой непосредственно является родительской
     * 				  для explorerItemDef
     * @return модель параграфа для дефиниции def
     */
    public static Model openModel(IClientEnvironment environment, RadParagraphDef def, Model holder) {
        if (def == null) {
            throw new IllegalArgumentError("paragraph definition was not defined");
        }
        final ParagraphModel paragraphModel = def.createModel(new IContext.Paragraph(environment, holder, def));
        final String msg = "paragraph model \"%s\" based on definition #%s was created";
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(msg, paragraphModel.getTitle(), def.getId()), EEventSource.EXPLORER);
        return paragraphModel;
    }

    /**
     * Создать модель параграфа для дефиниции на которую ссылается def.
     * @param def -  ссылка на дефиницию параграфа
     * @param holder  Модель дефиниция которой непосредственно является родительской
     * 				  для ссылки def
     * @return модель параграфа
     */
    public static Model openModel(IClientEnvironment environment, RadParagraphLinkDef def, Model holder) {
        if (def == null) {
            throw new IllegalArgumentError("paragraph link definition was not defined");
        }
        final ParagraphModel paragraphModel = def.createModel(new IContext.Paragraph(environment, holder, def));
        paragraphModel.setTitle(def.getTitle());
        final String msg = "paragraph model \"%s\" based on definition #%s was created";
        environment.getTracer().put(EEventSeverity.DEBUG, String.format(msg, paragraphModel.getTitle(), def.getId()), EEventSource.EXPLORER);
        return paragraphModel;
    }
    private List<Id> enabledCommandIds;

    @Override
    public List<Id> getAccessibleCommandIds() {
        if (enabledCommandIds == null) {
            enabledCommandIds = new ArrayList<Id>();
            enabledCommandIds.addAll(super.getAccessibleCommandIds());
            final EntityModel holderEntity = ((IContext.Paragraph) getContext()).getHolderEntityModel();
            if (holderEntity != null) {
                enabledCommandIds.addAll(holderEntity.getAccessibleCommandIds());
            }
        }
        return enabledCommandIds;
    }

    @Override
    public Command getCommand(Id id) {
        final EntityModel holderEntity = ((IContext.Paragraph) getContext()).getHolderEntityModel();
        if (id.getPrefix() != EDefinitionIdPrefix.CONTEXTLESS_COMMAND
                && !getDefinition().isCommandDefExistsById(id)
                && holderEntity != null && holderEntity.getDefinition().isCommandDefExistsById(id)) {
            return holderEntity.getCommand(id);
        }
        return super.getCommand(id);
    }

    @Override
    protected boolean isCommandAccessible(RadCommandDef command) {
        return true;
    }
    /*	@Override
    protected RadExplorerItemDef getChildExplorerItemDef(Id explItemId) {
    return getParagraphDef().getChildrenExplorerItems().findExplorerItem(explItemId);
    }*/
}
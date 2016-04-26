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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;

public final class CantLoadCustomViewError extends RuntimeException implements IClientError {

    public static enum ViewType {

        SELECTOR,
        EDITOR,
        EDITOR_PAGE,
        PARAGRAPH,
        FORM,
        PROP_EDITOR,
        UNKNOWN;


        ViewType() {
        }

    };


    private static final long serialVersionUID = 6732975548946326075L;
    private final ViewType viewType;
    private final String modelInfo;
    private final String definitionInfo;

    public CantLoadCustomViewError(EditorPageModelItem basedOn, Throwable cause) {
        super(cause);
        modelInfo = basedOn.getTitle();
        definitionInfo = basedOn.getDefinition().toString();
        viewType = ViewType.EDITOR_PAGE;
    }

    public CantLoadCustomViewError(RadPropertyDef property, Throwable cause) {
        super(cause);
        modelInfo = property.getTitle();
        definitionInfo = property.toString();
        viewType = ViewType.PROP_EDITOR;
    }

    public CantLoadCustomViewError(Model basedOn, Throwable cause) {
        super(cause);
        modelInfo = basedOn.getTitle();
        definitionInfo = basedOn.getDefinition().toString();
        viewType = getViewTypeForModel(basedOn);
    }

    private static String getViewTitle(final MessageProvider msgProvider, final ViewType viewType){
        switch (viewType){
            case EDITOR:
                return msgProvider.translate("ExplorerError", "editor");
            case SELECTOR:
                return msgProvider.translate("ExplorerError", "selector");
            case EDITOR_PAGE:
                return msgProvider.translate("ExplorerError", "editor page");
            case PARAGRAPH:
                return msgProvider.translate("ExplorerError", "paragraph");
            case FORM:
                return msgProvider.translate("ExplorerError", "form");
            case PROP_EDITOR:
                return msgProvider.translate("ExplorerError", "property editor");
            default:
                return msgProvider.translate("ExplorerError", "view");
        }
    }

    private static ViewType getViewTypeForModel(Model model) {
        if (model instanceof GroupModel) {
            return ViewType.SELECTOR;
        } else if (model instanceof EntityModel) {
            return ViewType.EDITOR;
        } else if (model instanceof ParagraphModel) {
            return ViewType.PARAGRAPH;
        } else if (model instanceof FormModel) {
            return ViewType.FORM;
        } else {
            return ViewType.UNKNOWN;
        }
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String message = mp.translate("ExplorerMessage", "Can't open custom %s for \'%s\'");
        return String.format(message, getViewTitle(mp,viewType), modelInfo);
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Can't Load Custom View");
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getLocalizedMessage(mp) + ":\n"
                + mp.translate("ExplorerError", "Definition:") + " \"" + definitionInfo + "\"\n"
                + mp.translate("ExplorerError", "Model:") + " \"" + modelInfo + "\"\n";
        //super.getDetailMessage();
    }
}

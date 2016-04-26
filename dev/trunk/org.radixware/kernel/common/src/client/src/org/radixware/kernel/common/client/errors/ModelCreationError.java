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
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.models.IContext;

public final class ModelCreationError extends RuntimeException implements IClientError {

    private static final long serialVersionUID = -9072423730565887657L;

    public static enum ModelType {
        ENTITY_MODEL,
        GROUP_MODEL,
        PARAGRAPH_MODEL,
        FORM_MODEL,
        REPORT_MODEL,
        FILTER_MODEL,
        DIALOG_MODEL,
        PROPERTY_EDITOR_DIALOG_MODEL,
        CUSTOM_WIDGET_MODEL,
        ENTITY_MODEL_FOR_NEW,
        UNKNOWN;
    }


    final IModelDefinition modelDefinition;
    final TitledDefinition holderDefinition;
    final String reason;
    final ModelType modelType;
    final IContext.Abstract context;

    public ModelCreationError(final ModelType modelType, final IModelDefinition definition, final IContext.Abstract context, final Throwable cause) {
        super(cause);
        this.modelType = modelType;
        this.context = context;
        modelDefinition = definition;
        holderDefinition = null;
        reason = null;
    }

    public ModelCreationError(final ModelType modelType, final IModelDefinition definition, final IContext.Abstract context, final String reason) {
        super();
        this.modelType = modelType;
        this.context = context;
        this.reason = reason;
        modelDefinition = definition;
        holderDefinition = null;
    }

    public ModelCreationError(final ModelType modelType, final IModelDefinition definition, final TitledDefinition source, final IContext.Abstract context, final Throwable cause) {
        super(cause);
        this.modelType = modelType;
        this.context = context;
        modelDefinition = definition;
        holderDefinition = source;
        reason = null;
    }

    public ModelCreationError(final ModelType modelType, final IModelDefinition definition, final TitledDefinition source, final IContext.Abstract context, final String reason) {
        super();
        this.modelType = modelType;
        this.context = context;
        this.reason = reason;
        modelDefinition = definition;
        holderDefinition = source;
    }

    private static String getModelTypeTitle(final MessageProvider mp, final ModelType modelType){
        switch (modelType){
            case ENTITY_MODEL:
                return mp.translate("ExplorerError", "entity model");
            case GROUP_MODEL:
                return mp.translate("ExplorerError", "group model");
            case PARAGRAPH_MODEL:
                return mp.translate("ExplorerError", "paragraph model");
            case FORM_MODEL:
                return mp.translate("ExplorerError", "form model");
            case REPORT_MODEL:
                return mp.translate("ExplorerError", "report model");
            case FILTER_MODEL:
                return mp.translate("ExplorerError", "filter model");
            case DIALOG_MODEL:
                return mp.translate("ExplorerError", "dialog model");
            case PROPERTY_EDITOR_DIALOG_MODEL:
                return mp.translate("ExplorerError", "property editor dialog model");
            case CUSTOM_WIDGET_MODEL:
                return mp.translate("ExplorerError", "custom widget model");
            case ENTITY_MODEL_FOR_NEW:
                return mp.translate("ExplorerError", "entity model for creating new object");
            default:
                return mp.translate("ExplorerError", "model");
        }
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Failed to Create Model");
    }

    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String modelDefinitionTitle = modelDefinition != null ? modelDefinition.getTitle() : null;
        final String holderDefinitionTitle = holderDefinition != null ? holderDefinition.getTitle() : null;
        String msg;
        if (modelDefinitionTitle != null && !modelDefinitionTitle.isEmpty()
                && (holderDefinitionTitle == null || holderDefinitionTitle.isEmpty())) {
            final String template = mp.translate("ExplorerError", "Failed to create %s by \'%s\'");
            msg = String.format(template, getModelTypeTitle(mp,modelType), modelDefinitionTitle);
        } else if (holderDefinitionTitle != null && !holderDefinitionTitle.isEmpty()
                && (modelDefinitionTitle == null || modelDefinitionTitle.isEmpty())) {
            final String template = mp.translate("ExplorerError", "Failed to create %s for \'%s\'");
            msg = String.format(template, getModelTypeTitle(mp,modelType), holderDefinitionTitle);
        } else if (holderDefinitionTitle != null && !holderDefinitionTitle.isEmpty()
                && modelDefinitionTitle != null && !modelDefinitionTitle.isEmpty()) {
            final String template = mp.translate("ExplorerError", "Failed to create %s by \'%s\' for \'%s\'");
            msg = String.format(template, getModelTypeTitle(mp,modelType), modelDefinitionTitle, holderDefinitionTitle);
        } else {
            final String template = mp.translate("ExplorerError", "Failed to create %s");
            msg = String.format(template, getModelTypeTitle(mp,modelType));
        }

        if (reason != null) {
            msg += ":\n" + reason;
        }
        return msg;
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        String result = getLocalizedMessage(mp) + "\n";
        if (modelDefinition != null) {
            result += mp.translate("ExplorerError", "model definition:") + " " + modelDefinition.toString() + "\n";
        }
        if (holderDefinition != null) {
            result += mp.translate("ExplorerError", "in definition:") + " " + holderDefinition.toString() + "\n";
        }
        if (context != null && context.getDescription() != null && !context.getDescription().isEmpty()) {
            result += mp.translate("ExplorerError", "context:") + " " + context.getDescription() + "\n";
        }
        if (reason != null && !reason.isEmpty()) {
            result += mp.translate("ExplorerError", "reason:") + " " + reason + "\n";
        }
        return result;
    }
}

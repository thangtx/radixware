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
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;

public class ModelError extends RuntimeException implements IClientError {

    private static final long serialVersionUID = 2887086524955935840L;

    public enum ErrorType {
        CANT_READ,
        CANT_UPDATE,
        CANT_CREATE,
        CANT_DELETE,
        CANT_DELETE_GROUP,
        CANT_CHANGE_PRESENTATION;
    }
    private final ErrorType errorType;
    private final String modelInfo;
    private final String definitionInfo;
    private final String reason;
    private String classDefinitionInfo = null;

    public ModelError(ErrorType type, Model model, final String info, Throwable cause) {
        super(cause);
        errorType = type;
        modelInfo = model.getTitle();
        setupClassDefinitionInfo(model);
        definitionInfo = model.getDefinition().toString();
        reason = info;
    }

    public ModelError(ErrorType type, Model model, final String info) {
        errorType = type;
        modelInfo = model.getTitle();
        setupClassDefinitionInfo(model);
        definitionInfo = model.getDefinition().toString();
        reason = info;
    }

    public ModelError(ErrorType type, Model model, Throwable cause) {
        super(cause);
        errorType = type;
        modelInfo = model.getTitle();
        definitionInfo = model.getDefinition().toString();
        reason = cause.getClass().getName();
        setupClassDefinitionInfo(model);
    }

    private static String getErrorTypeTitle(final MessageProvider mp, final ErrorType errorType){
        switch (errorType){
            case CANT_READ:
                return mp.translate("ExplorerError", "Can't Receive Data");
            case CANT_UPDATE:
                return mp.translate("ExplorerError", "Failed to Apply Changes");
            case CANT_CREATE:
                return mp.translate("ExplorerError", "Failed to Create Object");
            case CANT_DELETE:
                return mp.translate("ExplorerError", "Failed to Delete Object");
            case CANT_DELETE_GROUP:
                return mp.translate("ExplorerError", "Failed to Delete Group");
            case CANT_CHANGE_PRESENTATION:
                return mp.translate("ExplorerError", "Failed to Change Presentation");
             default:
                return mp.translate("ExplorerError", "Unknown Error");
        }
    }

    private static String getErrorTypeMessage(final MessageProvider mp, final ErrorType errorType){
        switch (errorType){
            case CANT_READ:
                return mp.translate("ExplorerError", "Error on receiving data for \'%s\'");
            case CANT_UPDATE:
                return mp.translate("ExplorerError", "Error on committing changes for \'%s\'");
            case CANT_CREATE:
                return mp.translate("ExplorerError", "Failed to create object \'%s\'");
            case CANT_DELETE:
                return mp.translate("ExplorerError", "Failed to delete object \'%s\'");
            case CANT_DELETE_GROUP:
                return mp.translate("ExplorerError", "Failed to delete group \'%s\'");
            case CANT_CHANGE_PRESENTATION:
                return mp.translate("ExplorerError", "Failed to change editor presentation for object \'%s\'");
             default:
                return mp.translate("ExplorerError", errorType.name());
        }
    }


    private void setupClassDefinitionInfo(final Model model) {
        if (model instanceof EntityModel) {
            classDefinitionInfo = ((EntityModel) model).getClassPresentationDef().toString();
        } else if (model instanceof GroupModel) {
            classDefinitionInfo = ((GroupModel) model).getSelectorPresentationDef().getClassPresentation().toString();
        }
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return getErrorTypeTitle(mp,errorType);
    }

    public String getMessage(MessageProvider mp) {
        if (reason != null && !reason.isEmpty()) {
            return String.format(getErrorTypeMessage(mp,errorType), modelInfo) + ":\n" + reason;
        } else {
            return String.format(getErrorTypeMessage(mp,errorType), modelInfo);
        }
    }

    @Override
    public String getMessage() {
        return getMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        String msg = getMessage(mp) + ":\n"
                + mp.translate("ExplorerError", "Model:") + " \'" + modelInfo + "\'\n"
                + mp.translate("ExplorerError", "Definition:") + " \'" + definitionInfo + "\'\n";
        if (classDefinitionInfo != null) {
            msg += mp.translate("ExplorerError", "Definition class:") + classDefinitionInfo + "\n";
        }
        return msg;
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        return getMessage(messageProvider);
    }
}

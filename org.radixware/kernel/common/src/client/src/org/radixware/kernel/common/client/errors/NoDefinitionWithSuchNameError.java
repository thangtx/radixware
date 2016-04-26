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
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.exceptions.DefinitionError;

public final class NoDefinitionWithSuchNameError extends DefinitionError implements IClientError {

    private static final long serialVersionUID = 7348317738586228397L;

    public static enum SubDefinitionType {
        PROPERTY,
        COMMAND,
        FILTER,
        SORTING,
        COLOR_SCHEME,
        EDITOR_PAGE,
        EDITOR_PAGE_PROPERTIES_GROUP,
        ENUM_ITEM;
    }

    private final Definition parentDef;
    private final IModelDefinition modelDef;
    private final String name;
    private final SubDefinitionType type;

    public NoDefinitionWithSuchNameError(final Definition def,
            final SubDefinitionType type,
            final String subdefinitionName) {
        super(getSubDefinitionTypeTitle(DefaultMessageProvider.getInstance(), type) + " with name \'" + subdefinitionName + "\' not found in definition " + def.toString());
        parentDef = def;
        modelDef = null;
        name = subdefinitionName;
        this.type = type;
    }

    public NoDefinitionWithSuchNameError(final IModelDefinition def,
            final SubDefinitionType type,
            final String subdefinitionName) {
        super(getSubDefinitionTypeTitle(DefaultMessageProvider.getInstance(), type) + " with name \'" + subdefinitionName + "\' not found in definition " + def.toString());
        parentDef = null;
        modelDef = def;
        name = subdefinitionName;
        this.type = type;
    }

    private static String getSubDefinitionTypeTitle(final MessageProvider mp, final SubDefinitionType defType){

        switch (defType){
            case PROPERTY:
                return mp.translate("ExplorerError", "property definition");
            case COMMAND:
                return mp.translate("ExplorerError", "command");
            case FILTER:
                return mp.translate("ExplorerError", "filter definition");
            case SORTING:
                return mp.translate("ExplorerError", "sorting definition");
            case COLOR_SCHEME:
                return mp.translate("ExplorerError", "color scheme");
            case EDITOR_PAGE:
                return mp.translate("ExplorerError", "editor page");
            case EDITOR_PAGE_PROPERTIES_GROUP:
                return mp.translate("ExplorerError", "editor page properties group definition");
            case ENUM_ITEM:
                return mp.translate("ExplorerError", "enumeration item definition");
            default:
                return defType.name();
        }
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Definition Not Found");
    }

    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg = mp.translate("ExplorerError", "%s with name \'%s\' was not found\n in definition %s");
        final String defInfo = parentDef != null ? parentDef.toString() : modelDef.toString();
        return String.format(msg, getSubDefinitionTypeTitle(mp,type), name, defInfo);
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        final String defInfo = parentDef != null ? parentDef.getDescription() : modelDef.getTitle();
        return getLocalizedMessage(mp) + ":\n"
                + mp.translate("ExplorerError", "Definition:") + " \"" + defInfo + "\"\n";
    }
}

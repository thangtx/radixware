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

package org.radixware.kernel.server.types;

import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.IRadPropReadAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogType;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.server.arte.AdsClassLoader;


public abstract class FormHandler implements IRadClassInstance {

    private final Arte arte;

    public final Arte getArte() {
        return arte;
    }

    public final FormHandler getPrevForm() {
        return prevForm;
    }

    // Published methods
    @Override
    public abstract RadClassDef getRadMeta();

    public FormHandler(final FormHandler prevForm) {
        this.prevForm = prevForm;
        this.arte = ((AdsClassLoader) getClass().getClassLoader()).getArte();
    }

    public static final class NextDialogsRequest {

        public NextDialogsRequest(final MessageBoxRequest messageBox, final FormHandler form) {
            this.messageBox = messageBox;
            this.form = form;
        }
        public final MessageBoxRequest messageBox;
        public final FormHandler form;
    }

    public static final class Context {

        public final Id commandId;
        public final Id propertyId;
        public final Entity entity;
        public final PropValHandlersByIdMap newPropValsById;
        public final EntityGroup entityGroup;

        public Context(final Entity entity, final Id commandId, final Id propertyId, final PropValHandlersByIdMap newPropValById) {
            this.commandId = commandId;
            this.propertyId = propertyId;
            this.entity = entity;
            this.newPropValsById = newPropValById;
            this.entityGroup = null;
        }

        public Context(final EntityGroup entityGroup, final Id commandId) {
            this.entityGroup = entityGroup;
            this.commandId = commandId;
            this.propertyId = null;
            this.entity = null;
            this.newPropValsById = null;
        }

        public Context(final Id commandId) {
            this.entityGroup = null;
            this.commandId = commandId;
            this.propertyId = null;
            this.entity = null;
            this.newPropValsById = null;
        }
    }

    public static final class MessageBoxRequest {

        public MessageBoxRequest(final EDialogType type, final EDialogButtonType continueButtonType, final EDialogButtonType cancelButtonType, final String htmlText, final String text) {
            this.type = type;
            this.cancelButtonType = cancelButtonType;
            this.continueButtonType = continueButtonType;
            this.htmlText = htmlText;
            this.text = text;
        }
        public final EDialogType type;
        public final EDialogButtonType cancelButtonType;
        public final EDialogButtonType continueButtonType;
        public final String htmlText;
        public final String text;
    }
    /**
     * The reference to the previous form. It is null for the first form.
     */
    protected final FormHandler prevForm;

    /**
     * The form submit handler.
     *
     * @param context
     * @return A message or/and the next form reference
     */
    public abstract NextDialogsRequest onSubmit(Context context);

    //props
    @Override
    public final Object getProp(final Id id) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropReadAccessor) {
                return ((IRadPropReadAccessor) prop.getAccessor()).get(this, id);
            } else {
                throw new IllegalUsageError("Property #" + id + " has no read accessor");
            }
        }
        throw new DefinitionNotFoundError(id);
    }

    @Override
    public final void setProp(final Id id, final Object x) {
        if (getRadMeta() != null) {
            final RadPropDef prop = getRadMeta().getPropById(id);
            if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                ((IRadPropWriteAccessor) prop.getAccessor()).set(this, id, x);
            } else {
                throw new IllegalUsageError("Property #" + id + " has no write accessor");
            }
        } else {
            throw new DefinitionNotFoundError(id);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public String onCalcParentTitle(final Id parentTitlePropId, final Entity parent, final String title) {
        return title;
    }

    public FormHandler.NextDialogsRequest execCommand(final Id cmdId, final Id propId, final org.apache.xmlbeans.XmlObject input, final org.apache.xmlbeans.XmlObject output) throws AppException, InterruptedException {
        return null;
    }
}

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

import java.util.Objects;
import org.radixware.kernel.common.client.localization.DefaultMessageProvider;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.EntityModel;

public final class ActivatingPropertyError extends RuntimeException implements IClientError {

    /**
     * 
     */
    private static final long serialVersionUID = -3604099828938667736L;
    final String modelInfo;
    final String definitionInfo;
    final String definitionClassInfo;
    final String propertyInfo;

    public ActivatingPropertyError(final EntityModel model, final RadPropertyDef property, final Throwable ex) {
        super(ex);
        modelInfo = model.getTitle();
        definitionInfo = model.getDefinition().toString();
        definitionClassInfo = model.getClassPresentationDef().getDescription();
        propertyInfo = property.toString();
    }

    public ActivatingPropertyError(final EntityModel model, final RadPropertyDef property) {
        super();
        modelInfo = model.getTitle();
        definitionInfo = model.getDefinition().toString();
        definitionClassInfo = model.getClassPresentationDef().getDescription();
        propertyInfo = property.toString();
    }

    @Override
    public String getTitle(MessageProvider mp) {
        return mp.translate("ExplorerError", "Activating Property Error");
    }

    @Override
    public String getLocalizedMessage(MessageProvider mp) {
        final String msg;
        if (getCause() != null) {
            msg = mp.translate("ExplorerError", "Can't activate property %s in model %s");
        } else {
            msg = mp.translate("ExplorerError", "property %s is not accessible in model %s");
        }
        return String.format(msg, propertyInfo, modelInfo);
    }

    @Override
    public String getDetailMessage(MessageProvider mp) {
        return getLocalizedMessage(mp) + "\n"
                + mp.translate("ExplorerError", "Object:") + " \"" + modelInfo + "\"\n"
                + mp.translate("ExplorerError", "Editor presentation:") + " \"" + definitionInfo + "\"\n"
                + mp.translate("ExplorerError", "Class:") + " \"" + definitionClassInfo + "\"";
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage(DefaultMessageProvider.getInstance());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.modelInfo);
        hash = 73 * hash + Objects.hashCode(this.definitionInfo);
        hash = 73 * hash + Objects.hashCode(this.definitionClassInfo);
        hash = 73 * hash + Objects.hashCode(this.propertyInfo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActivatingPropertyError other = (ActivatingPropertyError) obj;
        if (!Objects.equals(this.modelInfo, other.modelInfo)) {
            return false;
        }
        if (!Objects.equals(this.definitionInfo, other.definitionInfo)) {
            return false;
        }
        if (!Objects.equals(this.definitionClassInfo, other.definitionClassInfo)) {
            return false;
        }
        if (!Objects.equals(this.propertyInfo, other.propertyInfo)) {
            return false;
        }
        return true;
    }
    
    
}

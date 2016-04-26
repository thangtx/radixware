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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.schemas.adsdef.MethodDefinition;


public class AdsMethodThrowsList extends RadixObjects<AdsMethodThrowsList.ThrowsListItem> {

    public static final class Factory {

        public static final AdsMethodThrowsList loadFrom(AdsMethodDef context, MethodDefinition.ThrownExceptions exceptionList) {
            if (exceptionList != null) {
                List<MethodDefinition.ThrownExceptions.Exception> exceptionDecls = exceptionList.getExceptionList();
                if (exceptionDecls != null && exceptionDecls.size() > 0) {
                    AdsMethodThrowsList list = new AdsMethodThrowsList(context);
                    for (MethodDefinition.ThrownExceptions.Exception decl : exceptionDecls) {
                        list.add(ThrowsListItem.Factory.loadFrom(decl));
                    }
                    return list;
                } else {
                    return emptyList(context);
                }
            } else {
                return emptyList(context);
            }
        }

        public static final AdsMethodThrowsList emptyList(AdsMethodDef context) {
            return new AdsMethodThrowsList(context);
        }
    }

    public static class ThrowsListItem extends RadixObject implements ILocalizedDescribable, IDescribable {

        public static final class Factory {

            public static final ThrowsListItem newInstance(AdsTypeDeclaration type) {
                return new ThrowsListItem(type, null, null);
            }

            public static final ThrowsListItem newInstance(AdsTypeDeclaration exception, String description, Id descriptionId) {
                return new ThrowsListItem(exception, description, descriptionId);
            }

            public static final ThrowsListItem loadFrom(MethodDefinition.ThrownExceptions.Exception decl) {
                return new ThrowsListItem(AdsTypeDeclaration.Factory.loadFrom(decl), decl.getDescription(), decl.getDescriptionId());
            }

            public static final ThrowsListItem newTemporaryInstance(RadixObject container, AdsTypeDeclaration exception, Id descriptionId) {
                final ThrowsListItem throwsListItem = newInstance(exception, "", descriptionId);
                throwsListItem.setContainer(container);
                return throwsListItem;
            }
        }

        private final AdsTypeDeclaration exception;
        private String description;
        private Id descriptionId;

        private ThrowsListItem(AdsTypeDeclaration exception, String description, Id descriptionId) {
            this.exception = exception;
            this.description = description;
            this.descriptionId = descriptionId;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public AdsTypeDeclaration getException() {
            return exception;
        }

        @Override
        public void setDescription(String description) {
            this.description = description;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            if (exception != null) {
                exception.visit(visitor, provider);
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            if (exception != null) {
                AdsType type = exception.resolve(getOwnerMethod()).get();
                if (type instanceof AdsDefinitionType) {
                    Definition def = ((AdsDefinitionType) type).getSource();
                    if (def != null) {
                        list.add(def);
                    }
                }
            }
        }

        @Override
        public String getDescription(EIsoLanguage language) {
            return getDescriptionLocation().getLocalizedStringValue(language, getDescriptionId());
        }

        @Override
        public Id getDescriptionId() {
            return descriptionId;
        }

        @Override
        public boolean setDescription(EIsoLanguage language, String description) {
            final Id id = getDescriptionLocation().setLocalizedStringValue(language, getDescriptionId(), description, ELocalizedStringKind.DESCRIPTION);
            setDescriptionId(id);
            return id != null;
        }

        @Override
        public void setDescriptionId(Id id) {
            if (!Objects.equals(id, descriptionId)) {
                this.descriptionId = id;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public AdsDefinition getDescriptionLocation() {
            return RadixObjectsUtils.findContainer(this, AdsDefinition.class);
        }

        public final void removeDescriptionString() {
            final AdsMultilingualStringDef localizedString = getDescriptionLocation().findLocalizedString(descriptionId);
            if (localizedString != null) {
                getDescriptionLocation().findLocalizingBundle().getStrings().getLocal().remove(localizedString);
            }
        }

        public AdsMethodDef getOwnerMethod() {
            if (getContainer() == null) {
                return null;
            }
            return (AdsMethodDef) getContainer().getContainer();
        }

        private void appendTo(MethodDefinition.ThrownExceptions.Exception xDef) {
            if (exception != null) {
                exception.appendTo(xDef);
            }
            if (description != null && !description.isEmpty()) {
                xDef.setDescription(description);
            }

            if (descriptionId != null) {
                xDef.setDescriptionId(descriptionId);
            }
        }
    }

    private AdsMethodThrowsList(AdsMethodDef context) {
        super(context);
    }

    public void appendTo(MethodDefinition.ThrownExceptions xDef) {
        for (ThrowsListItem e : this) {
            e.appendTo(xDef.addNewException());
        }
    }

    void printSignatureString(StringBuilder b) {
        if (!isEmpty()) {
            boolean isFirst = true;
            for (ThrowsListItem e : this) {
                if (isFirst) {
                    b.append(" throws ");
                    isFirst = false;
                } else {
                    b.append(',');
                    b.append(' ');
                }
                if (e.getException() == null) {
                    b.append("UNDEFINED");
                } else {
                    b.append(e.getException().getName(getOwnerMethod()));
                }
            }
        }
    }

    public void printSignatureHtml(StringBuilder b) {
        if (!isEmpty()) {
            boolean isFirst = true;
            for (ThrowsListItem e : this) {
                if (isFirst) {
                    b.append(" throws ");
                    isFirst = false;
                } else {
                    b.append(',');
                    b.append(' ');
                }
                if (e.getException() == null) {
                    b.append("UNDEFINED");
                } else {
                    b.append(e.getException().getHtmlName(getOwnerMethod(), true));
                }
            }
        }
    }

    public AdsMethodDef getOwnerMethod() {
        return (AdsMethodDef) getContainer();
    }
}

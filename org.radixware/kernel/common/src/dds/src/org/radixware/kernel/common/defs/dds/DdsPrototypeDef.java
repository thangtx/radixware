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

package org.radixware.kernel.common.defs.dds;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Метаинформация о прототипе
 * {@link DdsFunctionDef функции} {@link DdsPlSqlObjectDef PL/SQL объекта}.
 * Содержит ссылку на исходную {@link DdsFunctionDef функцию}. Используется для
 * публикации или предварительного объявления {@link DdsFunctionDef функции}
 * внутри {@link DdsPlSqlObjectDef PL/SQL объекта}.
 *
 */
public class DdsPrototypeDef extends DdsPlSqlObjectItemDef {

    protected DdsPrototypeDef() {
        super(EDefinitionIdPrefix.DDS_PROTOTYPE, PROTOTYPE_TYPE_TITLE);
    }

    public DdsPrototypeDef(org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype) {
        super(xPrototype);
        this.functionId = Id.Factory.loadFrom(xPrototype.getFunctionId());
    }

    @Override
    public boolean isDeprecated() {
        DdsFunctionDef func = findFunction();
        if (func != null) {
            return func.isDeprecated();
        } else {
            return super.isDeprecated();
        }
    }
    private Id functionId = null;

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CALC;
    }

    @Override
    public String getName() {
        DdsFunctionDef function = findFunction();
        if (function != null) {
            return function.getName();
        } else {
            return String.valueOf(functionId);
        }
    }

    /**
     * Получить идентификатор исходной {@link DdsFunctionDef функции}.
     */
    public Id getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Id functionId) {
        if (!Utils.equals(this.functionId, functionId)) {
            this.functionId = functionId;
            setEditState(EEditState.MODIFIED);
        }
    }
    /**
     * Find source function.
     *
     * @return function or null if not found.
     */
    private final DefinitionLink<DdsFunctionDef> functionLink = new DefinitionLink<DdsFunctionDef>() {
        @Override
        protected DdsFunctionDef search() {
            DdsPlSqlObjectDef plSqlObject = DdsPrototypeDef.this.getOwnerPlSqlObject();
            if (plSqlObject != null) {
                DdsPlSqlObjectItemDef item = plSqlObject.getBody().getItems().findById(functionId);
                if (item instanceof DdsFunctionDef) {
                    return (DdsFunctionDef) item;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    };

    public DdsFunctionDef findFunction() {
        return functionLink.find();
    }

    /**
     * Find source function.
     *
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsFunctionDef getFunction() {
        DdsFunctionDef function = findFunction();
        if (function == null) {
            throw new DefinitionNotFoundError(functionId);
        }
        return function;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsPrototypeDef newInstance() {
            return new DdsPrototypeDef();
        }

        public static DdsPrototypeDef loadFrom(org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype) {
            return new DdsPrototypeDef(xPrototype);
        }

        public static DdsPrototypeDef newInstance(DdsPrototypeDef src) {
            org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype = org.radixware.schemas.ddsdef.PlSqlPrototype.Factory.newInstance();
            DdsModelWriter.writePrototype(src, xPrototype);
            return loadFrom(xPrototype);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.PROTOTYPE;
    }

    @Override
    public String toString() {
        return super.toString()
                + "; FunctionId: " + String.valueOf(functionId);
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsFunctionDef function = findFunction();
        if (function != null) {
            list.add(function);
        }
    }

    private class DdsPrototypeClipboardSupport extends DdsClipboardSupport<DdsPrototypeDef> {

        public DdsPrototypeClipboardSupport() {
            super(DdsPrototypeDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype = org.radixware.schemas.ddsdef.PlSqlPrototype.Factory.newInstance();
            DdsModelWriter.writePrototype(DdsPrototypeDef.this, xPrototype);
            return xPrototype;
        }

        @Override
        protected DdsPrototypeDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype = (org.radixware.schemas.ddsdef.PlSqlPrototype) xmlObject;
            return DdsPrototypeDef.Factory.loadFrom(xPrototype);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsPrototypeDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.PlSqlPrototype.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsDefinition> getClipboardSupport() {
        final DdsFunctionDef func = findFunction();
        if (func != null) {
            return func.getClipboardSupport();
        } else {
            return new DdsPrototypeClipboardSupport();
        }
    }
    public static final String PROTOTYPE_TYPE_TITLE = "Function Prototype";
    public static final String PROTOTYPE_TYPES_TITLE = "Function Prototypes";

    @Override
    public String getTypeTitle() {
        return PROTOTYPE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return PROTOTYPE_TYPES_TITLE;
    }
}

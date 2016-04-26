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

package org.radixware.kernel.common.sqml.tags;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - вызов PL/SQL функции.
 * Транслируется в вызов PL/SQL функции, опционально с передачей значений параметров по имени.
 */
public class DbFuncCallTag extends Sqml.Tag implements ISqlTag {

    /**
     * Информация о значении параметра тэга {@link DbFuncCallTag}.
     */
    public static class ParamValue extends RadixObject {

        protected ParamValue() {
            super();
        }
        private Id paramId = null;

        /**
         * Получить идентификатор параметра, зачение которого хранится.
         */
        public Id getParamId() {
            return paramId;
        }

        public void setParamId(Id paramId) {
            if (paramId == null) {
                throw new NullPointerException();
            }

            if (!Utils.equals(this.paramId, paramId)) {
                this.paramId = paramId;
                setEditState(EEditState.MODIFIED);
            }
        }
        private String value = null;

        /**
         * Получить значение параметра.
         * Подставляется как есть, может содержать SQL выражение.
         */
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            if (!Utils.equals(this.value, value)) {
                this.value = value;
                setEditState(EEditState.MODIFIED);
            }
        }

        public static final class Factory {

            private Factory() {
            }

            public static ParamValue newInstance() {
                return new ParamValue();
            }
        }

        public DbFuncCallTag getOwnerDbFuncCallTag() {
            for (RadixObject container = getContainer(); container != null; container = container.getContainer()) {
                if (container instanceof DbFuncCallTag) {
                    return (DbFuncCallTag) container;
                }
            }
            return null;
        }
    }

    protected DbFuncCallTag() {
        super();
    }
    private Id functionId = null;

    /**
     * Получить идентификатор вызываемой функции.
     */
    public Id getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Id functionId) {
        if (functionId == null) {
            throw new NullPointerException();
        }

        if (!Utils.equals(this.functionId, functionId)) {
            this.functionId = functionId;
            setEditState(EEditState.MODIFIED);
        }
    }

    private static class Params extends RadixObjects<ParamValue> {

        public Params(RadixObject owner) {
            super(owner);
        }
    }
    private final RadixObjects<ParamValue> params = new Params(this);

    /**
     * Получить информацию о значениях параметров, которые передаются при вызове функции.
     */
    public RadixObjects<ParamValue> getParamValues() {
        return params;
    }
    private boolean paramsDefined = false;

    /**
     * Определен-ли список значений параметров, которые передаются в функцию.
     * Если не определен, то значит значения параметров описаны далее чистым тестом или другими тэгами.
     */
    public boolean isParamsDefined() {
        return paramsDefined;
    }

    public void setParamsDefined(boolean paramsDefined) {
        if (!Utils.equals(this.paramsDefined, paramsDefined)) {
            this.paramsDefined = paramsDefined;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DbFuncCallTag newInstance() {
            return new DbFuncCallTag();
        }
    }

    /**
     * Find function.
     * @return function or null if not found.
     */
    public DdsFunctionDef findFunction() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findFunctionById(functionId);
        } else {
            return null;
        }
    }

    /**
     * Find function.
     * @throws DefinitionNotFoundError if not found.
     */
    public DdsFunctionDef getFunction() {
        DdsFunctionDef function = findFunction();
        if (function == null) {
            throw new DefinitionNotFoundError(functionId);
        }
        return function;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsFunctionDef function = findFunction();
        if (function != null) {
            list.add(function);
            for (ParamValue paramValue : getParamValues()) {
                DdsParameterDef param = function.getParameters().findById(paramValue.getParamId());
                if (param != null) {
                    list.add(param);
                }
            }
        }
    }
    public static final String DB_FUNC_CALL_TAG_TYPE_TITLE = "SQL Function Call Tag";
    public static final String DB_FUNC_CALL_TAG_TYPES_TITLE = "SQL Function Call Tags";

    @Override
    public String getTypeTitle() {
        return DB_FUNC_CALL_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return DB_FUNC_CALL_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>FunctionId: " + String.valueOf(functionId));
    }

    @Override
    public String getToolTip() {
        final DdsFunctionDef function = findFunction();
        if (function != null) {
            return function.getToolTip();
        } else {
            return super.getToolTip();
        }
    }
    private String sql;

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public void setSql(final String sql) {
        if (!Utils.equals(this.sql, sql)) {
            this.sql = sql;
            setEditState(EEditState.MODIFIED);
        }
    }
}

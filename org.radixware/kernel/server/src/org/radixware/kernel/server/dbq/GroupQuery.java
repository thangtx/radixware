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

package org.radixware.kernel.server.dbq;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.enums.OracleTypeNames;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.exceptions.DbQueryBuilderError;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;

public abstract class GroupQuery extends DbQuery {

//Constructors 
    GroupQuery(
            final Arte arte,
            final DdsTableDef table,
            final List<Field> fields,
            final List<Param> params,
            final String querySql) {
        super(arte, table, fields, params, querySql);
    }

    protected final void prepare(final EntityGroup group, final Pid parentPid) throws FilterParamNotDefinedException {
        if (group.getDdsMeta() != table) {
            throw new IllegalUsageError("Wrong group table. Expected " + table.getDbName() + " but got " + group.getDdsMeta());
        }
        prepare();
        if (params == null) {
            return;
        }
        int i = 1;
        for (Param param : params) {
            if (param instanceof InputParentPropParam) {
                if (parentPid == null) {
                    throw new IllegalUsageError("Can't translate \"ParentColumn\" tag cause context not specified", null);
                }
                try {
                    final Entity parent = arte.getEntityObject(parentPid);
                    final Id propId = ((InputParentPropParam) param).propId;
                    final RadPropDef prop = parent.getRadMeta().getPropById(propId);
                    setParam(i, prop.getValType(), prop.getDbType(), parent.getProp(propId), prop.getName());
                } catch (RuntimeException e) {
                    throw new IllegalUsageError("Can't translate \"ParentColumn\":" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            } else if (param instanceof InputGroupPropParam) {
                try {
                    final Id propId = ((InputGroupPropParam) param).propId;
                    final RadPropDef prop = group.getRadMeta().getPropById(propId);
                    final Object val = group.getProp(propId);
                    setParam(i, prop.getValType(), prop.getDbType(), val, prop.getName());
                } catch (RuntimeException e) {
                    throw new IllegalUsageError("Can't translate \"GroupParameter\":" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            } else if (param instanceof FilterParam) {
                setFilterParam(i, (FilterParam) param, group.getFltParamValsById());
            } else if (param instanceof InputRequestedRoleIdsParam) {
                try {
                    query.setString(i, group.getRequestedRoleIds());
                } catch (SQLException e) {
                    throw new DbQueryBuilderError("Can't set InputRequestedRoleIdsParam: " + ExceptionTextFormatter.getExceptionMess(e), e);
                }
            } else if (param instanceof InputChildPropParam) {
                if (!(group.getContext() instanceof EntityGroup.PropContext)) {
                    throw new WrongFormatError("Can't translate \"ChildColumn\" tag cause: wrong context");
                }
                try {
                    final Id propId = ((InputChildPropParam) param).propId;
                    final IRadClassInstance child = ((EntityGroup.PropContext) group.getContext()).getPropOwner();
                    final RadPropDef childProp = child.getRadMeta().getPropById(propId);
                    setParam(i, childProp.getValType(), childProp.getDbType(), child.getProp(propId), childProp.getName());
                } catch (RuntimeException e) {// ChildProp может прийти только из Sqml-я
                    throw new WrongFormatError("Can't translate \"ChildColumn\" tag cause:" + e.getClass().getName() + (e.getMessage() != null ? "\n" + e.getMessage() : ""), e);
                }
            }
            i++;
        }
    }

    private void setFilterParam(final int idx, final FilterParam param, final Map<Id, Object> fltParamValsById) throws FilterParamNotDefinedException {
        setFilterParam(arte, query, idx, param, fltParamValsById);
    }

    public static final void setFilterParam(final Arte arte, final PreparedStatement stmt, final int idx, final FilterParam param, final Map<Id, Object> fltParamValsById) throws FilterParamNotDefinedException {
        boolean defined = false;
        final Id id = param.id;
        Object val = null;
        if (fltParamValsById != null) {
            val = fltParamValsById.get(id);
            if (val != null) {
                defined = true;
            } else {
                defined = fltParamValsById.containsKey(id);
            }
        }
        if (!defined) {
            throw new FilterParamNotDefinedException(id);
        }
        if (param instanceof FilterKeyColumnParam) {
            final FilterKeyColumnParam p = (FilterKeyColumnParam) param;
            if (val == null) {
                setParam(arte, stmt, idx, p.keyColumn.getValType(), p.keyColumn.getDbType(), null, p.keyColumn.getName());
            } else {
                final Pid pid = val instanceof Pid ? (Pid) val : new Pid(arte, p.table, String.valueOf(val));
                if (p.table != pid.getTable()) {
                    throw new IllegalUsageError("Wrong entity: expected #" + p.table.getId() + " but was #" + pid.getTable().getId());
                }
                final Entity referedObj = arte.getEntityObject(pid);
                setParam(arte, stmt, idx, p.keyColumn.getValType(), p.keyColumn.getDbType(), referedObj.getProp(p.keyColumn.getId()), p.keyColumn.getName());
            }
        } else if (param instanceof FilterKeyAsPidStrParam) {
            final String pidStr = (val == null ? null : val.toString());
            setParam(arte, stmt, idx, EValType.STR, OracleTypeNames.VARCHAR2, pidStr, null);
        } else {
            setParam(arte, stmt, idx, val);
        }
    }

    static public class FilterParam extends InputParam {

        protected final Id id;

        public FilterParam(final Id id) {
            this.id = id;
        }

        public Id getId() {
            return id;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof FilterParam) {
                return Utils.equals(id, ((FilterParam) obj).id);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
            return hash;
        }
    }

    static final class InputRequestedRoleIdsParam extends Param {
    }

    static public final class FilterKeyAsPidStrParam extends FilterParam {

        public FilterKeyAsPidStrParam(final Id id) {
            super(id);
        }
    }

    static public final class FilterKeyColumnParam extends FilterParam {
        //Fields

        protected final DdsTableDef table;
        protected final DdsColumnDef keyColumn;
        //Constructor

        public FilterKeyColumnParam(final Id id, final DdsTableDef table, final DdsColumnDef keyColumn) {
            super(id);
            this.table = table;
            this.keyColumn = keyColumn;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj.getClass() == FilterKeyColumnParam.class) {
                final FilterKeyColumnParam param = (FilterKeyColumnParam) obj;
                return Utils.equals(id, param.id) && Utils.equals(table, param.table) && Utils.equals(keyColumn, param.keyColumn);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.table != null ? this.table.hashCode() : 0);
            hash = 37 * hash + (this.keyColumn != null ? this.keyColumn.hashCode() : 0);
            return hash;
        }
    }
}

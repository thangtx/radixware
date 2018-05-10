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

package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

public enum ETimingSection implements IKernelStrEnum {

    RDX("Radix"),
    RDX_ARTE("Radix.Arte"),
    RDX_ARTE_DB("Radix.Arte.Db"),
    RDX_ARTE_DB_CALL("Radix.Arte.Db.Call"),
    RDX_ARTE_DB_LOB_TMP_CREATE("Radix.Arte.Db.Lob.Tmp.Create"),
    RDX_ARTE_DB_LOB_TMP_FREE("Radix.Arte.Db.Lob.Tmp.Free"),
    RDX_ARTE_DB_QRY_PREPARE("Radix.Arte.Db.QryPrepare"),
    RDX_ARTE_DB_QRY_EXEC("Radix.Arte.Db.QryExec"),
    RDX_ARTE_DB_COMMIT("Radix.Arte.Db.Commit"),
    @Deprecated
    RDX_ARTE_DB_READ_CLASSGUID("Radix.Arte.Db.ReadClassGUID"),
    RDX_ARTE_DB_ROLLBACK("Radix.Arte.Db.Rollback"),
    RDX_ARTE_DB_QRY_BUILDER("Radix.Arte.Dbqb"),
    RDX_ARTE_DB_QRY_BUILDER_CONSTR("Radix.Arte.Dbqb.Construct"),
    RDX_ARTE_DB_QRY_BUILDER_PREPARE("Radix.Arte.Dbqb.Prepare"),
    RDX_ARTE_SC_INVOKE("Radix.Arte.ServiceClient.Invoke"),
    RDX_ARTE_WAIT_ACTIVE("Radix.Arte.Wait.Active"),
    RDX_ARTE_RESOURCE_INVOKE("Radix.Arte.Resource.Invoke"),
    RDX_ARTE_USER_FUNC_INVOKE("Radix.Arte.UserFunc.Invoke"),
    RDX_SQLCLASS("Radix.SqlClass"),
    RDX_SQLCLASS_QRYPREPARE("Radix.SqlClass.QryPrepare"),
    RDX_SQLCLASS_QRYEXEC("Radix.SqlClass.QryExec"),
    RDX_SQLCLASS_BATCHEXEC("Radix.SqlClass.BatchExec"),
    RDX_ENTITY("Radix.Entity"),
    RDX_ENTITY_DB("Radix.Entity.Db"),
    RDX_ENTITY_DB_SELECT("Radix.Entity.Db.Select"),
    RDX_ENTITY_DB_READ("Radix.Entity.Db.Read"),
    RDX_ENTITY_DB_READ_PK("Radix.Entity.Db.Read.Pk"),
    RDX_ENTITY_DB_LOCK("Radix.Entity.Db.Lock"),
    RDX_ENTITY_DB_WRITE("Radix.Entity.Db.Write"),
    RDX_ENTITY_DB_CREATE("Radix.Entity.Db.Write.Create"),
    RDX_ENTITY_DB_UPDATE("Radix.Entity.Db.Write.Update"),
    RDX_ENTITY_DB_CREATE_BATCH("Radix.Entity.Db.Write.Create.Batch"),
    RDX_ENTITY_DB_UPDATE_BATCH("Radix.Entity.Db.Write.Update.Batch"),
    RDX_ENTITY_DB_DELETE("Radix.Entity.Db.Write.Delete");
    private final String value;

    private ETimingSection(final String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getName() {
        return value;
    }

    public static ETimingSection getForValue(final String value) {
        for (ETimingSection dbOption : ETimingSection.values()) {
            if (dbOption.getValue().equals(value)) {
                return dbOption;
            }
        }
        throw new NoConstItemWithSuchValueError("ETimingSection has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }
}

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

/**
 * Опции, добавляемые при генерации скрипта создания {@link DdsReferenceDef} и {@linkplain DdsCheckConstraint} в базе данных.
 * См. документацию Oracle для большей информации.
 * См. <a href="http://download.oracle.com/docs/cd/B28359_01/server.111/b28286/clauses002.htm#i1002273" target="new">документацию Oracle</a> для большей информации.
 */
public enum EDdsConstraintDbOption implements IKernelStrEnum {

    /**
     * <code>RELY</code> and <code>NORELY</code> are valid only when you are modifying an existing constraint (in the <code>ALTER</code> <code>TABLE</code> ... <code>MODIFY</code> constraint syntax). These parameters specify whether a constraint in <code>NOVALIDATE</code> mode is to be taken into account for query rewrite. Specify <code>RELY</code> to activate an existing constraint in <code>NOVALIDATE</code> mode for query rewrite in an unenforced query rewrite integrity mode. The constraint is in <code>NOVALIDATE</code> mode, so Oracle does not enforce it. The default is <code>NORELY</code>.</p>
     * <p>Unenforced constraints are generally useful only with materialized views and query rewrite. Depending on the <a class="olink REFRN10177" href="../../server.111/b28320/initparams190.htm#REFRN10177"><code>QUERY_REWRITE_INTEGRITY</code></a> mode, query rewrite can use only constraints that are in <code>VALIDATE</code> mode, or that are in <code>NOVALIDATE</code> mode with the <code>RELY</code> parameter set, to determine join information.</p>
     * <p><b>Restriction on the RELY Clause</b><br>&nbsp;You cannot set a nondeferrable <code>NOT</code> <code>NULL</code> constraint to <code>RELY</code>.</p>
     */
    RELY("Rely"),
    /**
     * <p><b>ENABLE Clause&nbsp;</b><br>Specify <code>ENABLE</code> if you want the constraint to be applied to the data in the table.</p>
     * <p>If you enable a unique or primary key constraint, and if no index exists on the key, then Oracle Database creates a unique index. Unless you specify <code>KEEP INDEX</code> when subsequently disabling the constraint, this index is dropped and the database rebuilds the index every time the constraint is reenabled.</p>
     * <p>You can also avoid rebuilding the index and eliminate redundant indexes by creating new primary key and unique constraints initially disabled. Then create (or use existing) nonunique indexes to enforce the constraint. Oracle does not drop a nonunique index when the constraint is disabled, so subsequent <code>ENABLE</code> operations are facilitated.</p>
     * <ul>
     * <li>
     * <p><code>ENABLE</code> <code>VALIDATE</code> specifies that all old and new data also complies with the constraint. An enabled validated constraint guarantees that all data is and will continue to be valid.</p>
     * <p>If any row in the table violates the integrity constraint, then the constraint remains disabled and Oracle returns an error. If all rows comply with the constraint, then Oracle enables the constraint. Subsequently, if new data violates the constraint, then Oracle does not execute the statement and returns an error indicating the integrity constraint violation.</p>
     * <p>If you place a primary key constraint in <code>ENABLE</code> <code>VALIDATE</code> mode, then the validation process will verify that the primary key columns contain no nulls. To avoid this overhead, mark each column in the primary key <code>NOT</code> <code>NULL</code> before entering data into the column and before enabling the primary key constraint of the table.</p>
     * </li>
     * <li>
     * <p><code>ENABLE</code> <code>NOVALIDATE</code> ensures that all new DML operations on the constrained data comply with the constraint. This clause does not ensure that existing data in the table complies with the constraint.</p>
     * </li>
     * </ul>
     * <p>If you specify neither <code>VALIDATE</code> nor <code>NOVALIDATE</code>, then the default is <code>VALIDATE</code>.</p>
     * <p>If you change the state of any single constraint from <code>ENABLE</code> <code>NOVALIDATE</code> to <code>ENABLE</code> <code>VALIDATE</code>, then the operation can be performed in parallel, and does not block reads, writes, or other DDL operations.</p>
     * <p>Restriction on the ENABLE Clause&nbsp;</span>You cannot enable a foreign key that references a disabled unique or primary key.</p>
     * <p><b>DISABLE Clause&nbsp;</b><br>Specify <code>DISABLE</code> to disable the integrity constraint. Disabled integrity constraints appear in the data dictionary along with enabled constraints. If you do not specify this clause when creating a constraint, then Oracle automatically enables the constraint.</p>
     * <ul>
     * <li>
     * <p><code>DISABLE</code> <code>VALIDATE</code> disables the constraint and drops the index on the constraint, but keeps the constraint valid. This feature is most useful in data warehousing situations, because it lets you load large amounts of data while also saving space by not having an index. This setting lets you load data from a nonpartitioned table into a partitioned table using the <code><span class="codeinlineitalic">exchange_partition_clause</span></code> of the <code>ALTER</code> <code>TABLE</code> statement or using SQL*Loader. All other modifications to the table (inserts, updates, and deletes) by other SQL statements are disallowed.</p>
     * </li>
     * <li>
     * <p><code>DISABLE</code> <code>NOVALIDATE</code> signifies that Oracle makes no effort to maintain the constraint (because it is disabled) and cannot guarantee that the constraint is true (because it is not being validated).</p>
     * <p>You cannot drop a table whose primary key is being referenced by a foreign key even if the foreign key constraint is in <code>DISABLE</code> <code>NOVALIDATE</code> state. Further, the optimizer can use constraints in <code>DISABLE</code> <code>NOVALIDATE</code> state.</p>
     * </li>
     * </ul>
     * <p>If you specify neither <code>VALIDATE</code> nor <code>NOVALIDATE</code>, then the default is <code>NOVALIDATE</code>.</p>
     * <p>If you disable a unique or primary key constraint that is using a unique index, then Oracle drops the unique index. </p>
     */
    DISABLE("Disable"),
    /**
     * The behavior of VALIDATE and NOVALIDATE always depends on whether the constraint is enabled or disabled, either explicitly or by default. 
     * Therefore they are described in the context of "ENABLE" and "DISABLE".
     */
    NOVALIDATE("Novalidate"),
    /**
     * The <code>DEFERRABLE</code> and <code>NOT</code> <code>DEFERRABLE</code> parameters indicate whether or not, in subsequent transactions, constraint checking can be deferred until the end of the transaction using the <code>SET</code> <code>CONSTRAINT</code>(<code>S</code>) statement. If you omit this clause, then the default is <code>NOT</code> <code>DEFERRABLE</code>.</p>
     * <ul>
     * <li>
     * <p>Specify <code>NOT</code> <code>DEFERRABLE</code> to indicate that in subsequent transactions you cannot use the <code>SET</code> <code>CONSTRAINT</code>[<code>S</code>] clause to defer checking of this constraint until the transaction is committed. The checking of a <code>NOT</code> <code>DEFERRABLE</code> constraint can never be deferred to the end of the transaction.</p>
     * <p>If you declare a new constraint <code>NOT</code> <code>DEFERRABLE</code>, then it must be valid at the time the <code>CREATE</code> <code>TABLE</code> or <code>ALTER</code> <code>TABLE</code> statement is committed or the statement will fail.</p>
     * </li>
     * <li>
     * <p>Specify <code>DEFERRABLE</code> to indicate that in subsequent transactions you can use the <code>SET</code> <code>CONSTRAINT</code>[<code>S</code>] clause to defer checking of this constraint until after the transaction is committed. This setting in effect lets you disable the constraint temporarily while making changes to the database that might violate the constraint until all the changes are complete.</p>
     * </li>
     * </ul>
     * <p>You cannot alter the deferrability of a constraint. Whether you specify either of these parameters, or make the constraint <code>NOT</code> <code>DEFERRABLE</code> implicitly by specifying neither of them, you cannot specify this clause in an <code>ALTER</code> <code>TABLE</code> statement. You must drop the constraint and re-create it.</p>
     */
    DEFERRABLE("Deferrable"),
    /**
     * The <code>INITIALLY</code> clause establishes the default checking behavior for constraints that are <code>DEFERRABLE</code>. The <code>INITIALLY</code> setting can be overridden by a <code>SET</code> <code>CONSTRAINT</code>(<code>S</code>) statement in a subsequent transaction.</p>
     * <ul>
     * <li>
     * <p>Specify <code>INITIALLY</code> <code>IMMEDIATE</code> to indicate that Oracle should check this constraint at the end of each subsequent SQL statement. If you do not specify <code>INITIALLY</code> at all, then the default is <code>INITIALLY</code> <code>IMMEDIATE</code>.</p>
     * <p>If you declare a new constraint <code>INITIALLY</code> <code>IMMEDIATE</code>, then it must be valid at the time the <code>CREATE</code> <code>TABLE</code> or <code>ALTER</code> <code>TABLE</code> statement is committed or the statement will fail.</p>
     * </li>
     * <li>
     * <p>Specify <code>INITIALLY</code> <code>DEFERRED</code> to indicate that Oracle should check this constraint at the end of subsequent transactions.</p>
     * </li>
     * </ul>
     * <p>This clause is not valid if you have declared the constraint to be <code>NOT</code> <code>DEFERRABLE</code>, because a <code>NOT</code> <code>DEFERRABLE</code> constraint is automatically <code>INITIALLY</code> <code>IMMEDIATE</code> and cannot ever be <code>INITIALLY</code> <code>DEFERRED</code>.</p>
     */
    INITIALLY_DEFERRED("Initially Deferred");
    private final String value;

    private EDdsConstraintDbOption(String value) {
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

    public static EDdsConstraintDbOption getForValue(final String value) {
        for (EDdsConstraintDbOption dbOption : EDdsConstraintDbOption.values()) {
            if (dbOption.getValue().equals(value)) {
                return dbOption;
            }
        }
        throw new NoConstItemWithSuchValueError("EDdsReferenceDbOption has no item with value: " + String.valueOf(value),value);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}

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

package org.radixware.kernel.common.builder.check.ads.clazz;

import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class AdsEntityClassChecker extends AdsEntityObjectClassChecker<AdsEntityClassDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEntityClassDef.class;
    }

    @Override
    protected void checkSuperclassDetails(AdsEntityClassDef clazz, AdsClassDef resolvedBaseClass, IProblemHandler problemHandler) {
        super.checkSuperclassDetails(clazz, resolvedBaseClass, problemHandler);
        if (!(resolvedBaseClass.getTransparence() != null && resolvedBaseClass.getTransparence().isTransparent() && AdsEntityClassDef.PLATFORM_CLASS_NAME.equals(resolvedBaseClass.getTransparence().getPublishedName()))) {
            error(clazz, problemHandler, "Entity class must be based on publishing of " + AdsEntityClassDef.PLATFORM_CLASS_NAME);
        }
    }

    @Override
    protected void nullSuperclassReferenceDetails(AdsEntityClassDef clazz, IProblemHandler problemHandler) {
        super.nullSuperclassReferenceDetails(clazz, problemHandler);
        error(clazz, problemHandler, "Entity class must be based on publishing of " + AdsEntityClassDef.PLATFORM_CLASS_NAME);
    }

    @Override
    protected void unresolvedSuperclassReferenceDetails(AdsEntityClassDef clazz, AdsType ref, IProblemHandler problemHandler) {
        super.unresolvedSuperclassReferenceDetails(clazz, ref, problemHandler);
        AdsClassDef anc = findBaseHandler(clazz, AdsEntityClassDef.PREDEFINED_ID);
        if (anc == null) {
            error(clazz, problemHandler, "Entity class must be based on publishing of " + AdsEntityClassDef.PLATFORM_CLASS_NAME);
        } else {
            error(clazz, problemHandler, "Entity class must extend " + anc.getQualifiedName());
        }
    }

    @Override
    public void check(AdsEntityClassDef clazz, IProblemHandler problemHandler) {
        super.check(clazz, problemHandler);
        DdsTableDef table = clazz.findTable(clazz);
        if (table != null) {
            if (!AdsUtils.isEntityClassAllowed(table)) {
                error(clazz, problemHandler, "Table " + table.getQualifiedName() + " is detail table. Entity classes are not allowed");
            } else {
                DdsPrimaryKeyDef pk = table.getPrimaryKey();
                if (pk.getColumnsInfo().isEmpty()) {
                    warning(clazz, problemHandler, "No primary key columns defined for" + (table instanceof DdsViewDef ? " view " : " table ") + table.getQualifiedName());
                }
                for (ColumnInfo info : pk.getColumnsInfo()) {
                    Id id = info.getColumnId();
                    AdsPropertyDef prop = clazz.getProperties().findById(id, EScope.LOCAL_AND_OVERWRITE).get();
                    if (prop == null) {
                        DdsColumnDef col = info.findColumn();
                        StringBuilder message = new StringBuilder();
                        message.append("Primary key column ");
                        if (col == null) {
                            message.append("#");
                            message.append(id);
                        } else {
                            message.append(col.getQualifiedName());
                        }
                        message.append(" should be published");
                        warning(clazz, problemHandler, message.toString());
                    }
                }
            }
            if (!clazz.isFinal()) {
                if (!table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)) {
                    warning(clazz, problemHandler, "Application classes are not allowed for table " + table.getQualifiedName() + ". Class should be final");
                }
            }

            checkDefaultConstructor(clazz, problemHandler);

            checkClassGuidColumn(clazz, table, problemHandler);
        }
    }

    private void checkClassGuidColumn(AdsEntityClassDef clazz, DdsTableDef table, IProblemHandler problemHandler) {

        if (clazz.isPolymorphic()) {
            final DdsColumnDef classGuidColumn = table.findClassGuidColumn();
            if (classGuidColumn != null) {
                final SearchResult<AdsPropertyDef> result = clazz.getProperties().findById(classGuidColumn.getId(), ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);

                if (result.isEmpty()) {
                    error(clazz, problemHandler, "Polymorphic application class should publish class guid column");
                }
            }
        }
    }

    private void checkDefaultConstructor(AdsEntityClassDef clazz, IProblemHandler problemHandler) {


        if (clazz.hasConstructors() && fingDefaultConstructor(clazz) == null) {
            error(clazz, problemHandler, "Entity class must have default constructor without parameters");
        }
    }
}

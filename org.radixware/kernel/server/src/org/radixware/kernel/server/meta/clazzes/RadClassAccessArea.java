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

package org.radixware.kernel.server.meta.clazzes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;

public final class RadClassAccessArea {

    private final List<Partition> partitions;

    public RadClassAccessArea(final Partition[] partitions) {
        super();
        if (partitions != null && partitions.length != 0) {
            this.partitions = Collections.unmodifiableList(Arrays.asList(partitions));
        } else {
            this.partitions = Collections.emptyList();
        }
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    protected final void appendValSql(final StringBuilder sql) {
        sql.append("TRdxAcsArea(TRdxAcsCoordinates(");
        boolean isFirtsPart = true;
        for (Partition partition : getPartitions()) {
            if (isFirtsPart) {
                isFirtsPart = false;
            } else {
                sql.append(',');
            }
            partition.appendValSql(sql);
        }
        sql.append("))");
    }

    public static final class Partition {

        private final Id familyId;
        private final List<Id> referenceIds;
        private final Id propId;
        private RadClassApJoinItem joinItem = null;

        public Partition(final Id familyId, final Id[] referenceIds, final Id propId) {
            super();
            this.familyId = familyId;
            this.propId = propId;
            if (referenceIds != null && referenceIds.length != 0) {
                this.referenceIds = Collections.unmodifiableList(Arrays.asList(referenceIds));
            } else {
                this.referenceIds = Collections.emptyList();
            }
        }

        protected final void appendValSql(final StringBuilder sql) {
            sql.append("TRdxAcsCoordinate(0,\'");
            sql.append(getFamilyId());
            sql.append("\', ");
            getJoinItem().appendPropDbPresentationSql(sql, getPropId());
            sql.append(')');
        }

        /**
         * @return the familyId
         */
        public Id getFamilyId() {
            return familyId;
        }

        /**
         * @return the referenceIds
         */
        public List<Id> getReferenceIds() {
            return referenceIds;
        }

        /**
         * @return the propId
         */
        public Id getPropId() {
            return propId;
        }

        void setJoinItem(RadClassApJoinItem join) {
            joinItem = join;
        }

        /**
         * @return the joinItem
         */
        private RadClassApJoinItem getJoinItem() {
            return joinItem;
        }
    }
}

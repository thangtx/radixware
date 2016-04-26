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

package org.radixware.kernel.common.defs.dds.utils;

import org.radixware.kernel.common.defs.dds.*;

/**
 * Вычислитель имени табличного пространства для {@link DdsTableDef таблиц} и {@link DdsIndexDef индексов}.
 * Вспомогательный класс, реализующий логику наследования
 * табличного пространства внутри {@link DdsModelDef DDS моделей}.
 */
public class TablespaceCalculator {

    /**
     * Вычисляет имя табличного пространства для указанной {@link DdsTableDef таблицы}
     * с учетом наследования от {@link DdsModelDef моделей}, где она лежит.
     * @param table таблица, для которой требуется вычислить имя табличного пространства.
     * @return имя табличного пространства или пустую строку, если табличное пространство не задано.
     */
    public static String calcTablespaceForTable(DdsTableDef table) {
        String tablespace = table.getTablespace();
        if (tablespace != null) {
            return tablespace;
        } else {
            DdsModelDef ownerModel = table.getOwnerModel();
            if (ownerModel != null) {
                tablespace = ownerModel.getDbAttributes().getDefaultTablespaceForTables();
                if (tablespace != null) {
                    return tablespace;
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }
    }

    /**
     * Вычисляет имя табличного пространства для указанного {@link DdsIndexDef индекса}
     * с учетом наследования {@link DdsModelDef модели}, где он лежит.
     * @param index индекс, для которого требуется вычислить имя табличного пространства.
     * @return имя табличного пространства или пустую строку, если табличное пространство не задано.
     */
    public static String calcTablespaceForIndex(DdsIndexDef index) {
        String tablespace = index.getTablespaceDbName();
        if (tablespace != null) {
            return tablespace;
        } else {
            DdsTableDef ownerTable = index.getOwnerTable();
            // it is impossible to specify tablespace for indices of global temporary tables
            if (ownerTable != null && !ownerTable.isGlobalTemporary()) {
                DdsModelDef ownerModel = ownerTable.getOwnerModel();
                if (ownerModel != null) {
                    tablespace = ownerModel.getDbAttributes().getDefaultTablespaceForIndices();
                    if (tablespace != null) {
                        return tablespace;
                    }
                }
            }
            return "";
        }
    }
}

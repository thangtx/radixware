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
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг - имя sequence'а в базе данных.
 */
public class SequenceDbNameTag extends Sqml.Tag implements ISqlTag {

    /**
     * Постфикс подставляемый тэгу {@link SequenceDbNameTag} при трансляции.
     */
    public static enum Postfix {

        /**
         * Запрос текущего значение sequence'а.
         */
        CUR_VAL,
        /**
         * Запрос последующего значение sequence'а.
         */
        NEXT_VAL,
        /**
         * Не подставлять постфикс, оставлять только имя sequence'а.
         */
        NONE
    }

    protected SequenceDbNameTag() {
        super();
    }
    private Id sequenceId = null;

    /**
     * Получить идентификатор sequence'а.
     */
    public Id getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Id sequenceId) {
        if (!Utils.equals(this.sequenceId, sequenceId)) {
            this.sequenceId = sequenceId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Postfix postfix = Postfix.NONE;

    /**
     * Получить постфикс тэга.
     */
    public Postfix getPostfix() {
        return postfix;
    }

    public void setPostfix(Postfix postfix) {
        if (!Utils.equals(this.postfix, postfix)) {
            this.postfix = postfix;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static SequenceDbNameTag newInstance() {
            return new SequenceDbNameTag();
        }
    }

    /**
     * Find sequence.
     * @return sequence or null if not found.
     */
    public DdsSequenceDef findSequence() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findSequenceById(sequenceId);
        } else {
            return null;
        }
    }

    /**
     * Find sequence.
     * @throw DefinitionNotFoundError if not found.
     */
    public DdsSequenceDef getSequence() {
        DdsSequenceDef sequence = findSequence();
        if (sequence == null) {
            throw new DefinitionNotFoundError(sequenceId);
        }
        return sequence;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        DdsSequenceDef sequence = findSequence();
        if (sequence != null) {
            list.add(sequence);
        }
    }
    public static final String SEQUENCE_SQL_NAME_TAG_TYPE_TITLE = "Sequence SQL Name Tag";
    public static final String SEQUENCE_SQL_NAME_TAG_TYPES_TITLE = "Sequence SQL Name Tags";

    @Override
    public String getTypeTitle() {
        return SEQUENCE_SQL_NAME_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return SEQUENCE_SQL_NAME_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Sequence Id: " + String.valueOf(getSequenceId()));
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

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

package org.radixware.kernel.common.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.Guid;

/**
 * Definition identifier. Used for unique definition identification. Pattern:
 * Immutable.
 *
 */
public class Id extends Object implements Comparable<Id>, Serializable {

    public static boolean isCorrectId(char[] id) {
        if (id == null || id.length != Id.DEFAULT_ID_AS_STR_LENGTH) {
            return false;
        }
        final String prefix = String.valueOf(id, 0, 3);
        for (EDefinitionIdPrefix e : EDefinitionIdPrefix.values()) {
            if (e.getValue().equals(prefix)) {
                return true;
            }
        }
        return false;
    }

    public static final int DEFAULT_ID_AS_STR_LENGTH = 29;
    private static final long serialVersionUID = 1L;
    private final String idAsStr; // can't contain null
    private EDefinitionIdPrefix idPrefix = null; // for optimization

    /**
     * Get identifier prefix.
     */
    public EDefinitionIdPrefix getPrefix() {
        // it is possible to use synchronized or volatile,
        // but it is nothing if prefix will calculated twice,
        // this allows to not use synchronization (for optimization).
        if (idPrefix == null) {
            EDefinitionIdPrefix calcedIdPrefix = null;
            if (idAsStr.length() >= 3) {
                final String prefixAsStr = idAsStr.substring(0, 3);
                try {
                    calcedIdPrefix = EDefinitionIdPrefix.getForValue(prefixAsStr);
                } catch (NoConstItemWithSuchValueError error) {
                    // NOTHING - somewhere idAsStr is not GUID, but class name or method name, or method profile... :-(
                }
            }
            this.idPrefix = calcedIdPrefix;
        }
        return this.idPrefix;
    }

    /**
     * Get indentitifier value as string. Used for storage.
     *
     * @return string in format &lt;prefix&gt;&lt;UUID in base 32 encoding&gt -
     * valid java identifier for example tblABH7M5H7NTH7ETX7EDH7RTH2MT
     */
    @Override
    public String toString() {
        return idAsStr;
    }

    public char[] toCharArray() {
        return idAsStr.toCharArray();
    }

    protected Id(final String idAsStr) {
        this.idAsStr = idAsStr;
    }

    @Override
    public int compareTo(Id o) {
        if (o == this) {
            return 0;
        }
        return this.idAsStr.compareTo(o.idAsStr);
    }

    public static final class Factory {

        private static final ConcurrentHashMap<String, Id> ids = new ConcurrentHashMap<String, Id>();

        private Factory() {
        }
        @SuppressWarnings("PMD")
        private static Id getId(final String idAsStr) {
            final Id newId = new Id(idAsStr);
            //constructor of  string is used because of idAsStr param is often result of
            //substring() function, so too much memory will be occupied by map key
            //constructiong new string will resize internal char buffer to actual string length
            final Id oldId = ids.putIfAbsent(new String(idAsStr), newId);
            return (oldId != null ? oldId : newId);
        }

        /**
         * Create new random identifier with specified prefix.
         */
        public static Id newInstance(final EDefinitionIdPrefix idPrefix) {
            final String idPrefixAsStr = idPrefix.getValue();
            final String guid = Guid.generateGuid(null);
            final String idAsStr = idPrefixAsStr + guid;
            final Id id = getId(idAsStr);
            return id;
        }

        /**
         * Create new identifier with specified prefix using given name for uuid
         * generation.
         */
        public static Id newInstance(final EDefinitionIdPrefix idPrefix, final String name) {
            final String idPrefixAsStr = idPrefix.getValue();
            final String guid = Guid.generateGuid(name);
            final String idAsStr = idPrefixAsStr + guid;
            final Id id = getId(idAsStr);
            return id;
        }

        /**
         * Restore indentifier from string. Used for definition loading.
         *
         * @return new instance of Id or null if idAsStr is null or empty.
         */
        public static Id loadFrom(final String idAsStr) {
            if (idAsStr == null || idAsStr.isEmpty()) {
                return null;
            }
            final Id id = getId(idAsStr);
            return id;
        }

        /**
         * Restore list of indentifier from list of string.
         *
         * @return empty list if source list is empty, null if source list is
         * null, list of identifiers otherwise.
         */
        public static List<Id> loadFrom(final List<String> idsAsStr) {
            if (idsAsStr == null) {
                return null;
            }

            if (idsAsStr.isEmpty()) {
                return Collections.emptyList();
            }

            final List<Id> result = new ArrayList<>(idsAsStr.size());
            for (String idAsStr : idsAsStr) {
                final Id id = loadFrom(idAsStr);
                result.add(id);
            }
            return result;
        }

        /**
         * Create new identifier by changing of existing identifier prefix.
         */
        public static Id changePrefix(final Id sourceId, final EDefinitionIdPrefix newIdPrefix) {
            final String newIdAsStr = newIdPrefix.getValue() + sourceId.idAsStr.substring(3);
            final Id newId = getId(newIdAsStr);
            return newId;
        }

        public static Id append(final Id id, final String postfix) {
            return loadFrom(id.idAsStr + postfix);
        }
    }

    protected Object readResolve() {
        return Id.Factory.loadFrom(idAsStr);
    }
}

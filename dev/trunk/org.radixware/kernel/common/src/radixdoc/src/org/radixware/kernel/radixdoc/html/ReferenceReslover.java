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

package org.radixware.kernel.radixdoc.html;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


final class ReferenceReslover implements IResolver {

    static class ExternalEntry implements IResolver {

        private final String path;

        public ExternalEntry(String path) {
            this.path = path;
        }

        @Override
        public String resolve(String ref) {
            return path + "/" + ref;
        }
    }

    private static class LeafEntry implements IResolver {

        private final Set<String> entries = new HashSet<>();

        @Override
        public String resolve(String ref) {
            return entries.contains(ref) ? "../../../.." : null;
        }

        void add(String ref) {
            entries.add(ref);
        }
    }


    private final IResolver external;
    private final Map<String, IResolver> entries = new HashMap<>();
    private int count = 0;

    public ReferenceReslover(IResolver external) {
        this.external = external;
    }

    private String getRootPath(String ref) {
        final int divPsition = ref.indexOf("/");
        return divPsition >= 0 ? ref.substring(0, divPsition) : ref;
    }

    private IResolver findEntry(String ref) {
        return entries.get(getRootPath(ref));
    }

    @Override
    public String resolve(String ref) {
        final IResolver entry = findEntry(ref);
        if (entry != null) {
            return entry.resolve(ref);
        } else if (external != null) {
            return external.resolve(getRootPath(ref));
        }

        return null;
    }

    void add(String ref) {
        IResolver entry = findEntry(ref);

        if (entry == null) {
            final String rootPath = getRootPath(ref);
            entry = createEntry(rootPath);
            addEntry(rootPath, entry);
        }

        if (entry instanceof LeafEntry) {
            ((LeafEntry) entry).add(ref);
            ++count;
        }
    }

    private IResolver createEntry(String name) {
        return new LeafEntry();
    }

    void addEntry(String name, IResolver entry) {
        entries.put(name, entry);
    }

    public int getCount() {
        return count;
    }
}

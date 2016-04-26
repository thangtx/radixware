/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author akrylov
 */
public class SvnProperties {

    public static class Value {

        private final String value;
        private final byte[] data;

        public Value(String value, byte[] data) {
            this.value = value;
            this.data = data;
        }

        public String getValue() {
            return value;
        }

        public byte[] getBytes() {
            if (value != null) {
                try {
                    return value.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    //should never occurs
                }
            }
            return data;
        }

    }
    private final Map<String, Value> props = new HashMap<>();

    public Value get(String name) {
        return props.get(name);
    }

    public void set(String name, Value value) {
        props.put(name, value);
    }

    public Map<String, Value> map() {
        return Collections.unmodifiableMap(props);
    }
}

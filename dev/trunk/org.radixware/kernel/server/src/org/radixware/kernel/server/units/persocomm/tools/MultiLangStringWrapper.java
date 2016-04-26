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
package org.radixware.kernel.server.units.persocomm.tools;

public class MultiLangStringWrapper {
    private final String    parent;
    private final String    id;

    public MultiLangStringWrapper(final String parentAndId) {
        if (parentAndId == null || parentAndId.isEmpty()) {
            throw new IllegalArgumentException("Identifier value can't be null or empty");
        }
        else {
            final String[]  parts = parentAndId.split("\\-");
            
            if (parts.length != 2) {
                throw new IllegalArgumentException("Identifier value need to have X*-X* format");
            }
            else if (parts[0] == null || parts[0].isEmpty()) {
                throw new IllegalArgumentException("Parent value can't be null or empty");
            }
            else if (parts[1] == null || parts[1].isEmpty()) {
                throw new IllegalArgumentException("Id value can't be null or empty");
            }
            else if (!parts[1].startsWith("mls")) {
                throw new IllegalArgumentException("Id value need to start with 'mls' prefix!");
            }
            else {
                this.parent = parts[0]; this.id = parts[1];
            }
        }
        
    }
    
    public MultiLangStringWrapper(final String parent, final String id) {
        if (parent == null || parent.isEmpty()) {
            throw new IllegalArgumentException("Parent value can't be null or empty");
        }
        else if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id value can't be null or empty");
        }
        else if (!id.startsWith("mls")) {
            throw new IllegalArgumentException("Id value need to start with 'mls' prefix!");
        }
        else {
            this.parent = parent;   this.id = id;        
        }
    }
    
    @Override
    public String toString() {
        return parent+'-'+id;
    }
    
    public String getMlsId(){return parent+'-'+id;}
}

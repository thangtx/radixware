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

package org.radixware.kernel.common.build.xbeans;

import java.util.List;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlListImpl;

public class XmlIntEnumList extends XmlListImpl {

    private static final long serialVersionUID = -2409849867270355413L;
    private final TypeMapper mapperTo, mapperFrom;

    @SuppressWarnings("unchecked")
    public XmlIntEnumList(Class enumClass, SchemaType type, boolean complex) {
        super(type, complex);
        mapperTo = new IntEnum2Long(enumClass);
        mapperFrom = new InverseTypeMapper(mapperTo);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set_list(List list) {
        List<Long> lst = new ListView(mapperFrom, list);
        super.set_list(lst);
    }

    @Override
    public List getListValue() {
        return new ListView(mapperTo, super.getListValue());
    }
}

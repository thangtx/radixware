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

package org.radixware.kernel.common.rpc;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlrpc.common.TypeConverter;
import org.apache.xmlrpc.common.TypeConverterFactoryImpl;


public class XmlRpcRadixTypeConverterFactory extends TypeConverterFactoryImpl{
    
    private final TypeConverter xmlTypeConverter = new TypeConverter() {

        @Override
        public boolean isConvertable(final Object o) {
            return o==null || (o instanceof XmlObject);
        }

        @Override
        public Object convert(final Object o) {
            return o;
        }

        @Override
        public Object backConvert(final Object o) {
            return o;
        }
    };

    @Override
    public TypeConverter getTypeConverter(final Class type) {
        return XmlObject.class.isAssignableFrom(type) ? xmlTypeConverter : super.getTypeConverter(type);
    }       
}

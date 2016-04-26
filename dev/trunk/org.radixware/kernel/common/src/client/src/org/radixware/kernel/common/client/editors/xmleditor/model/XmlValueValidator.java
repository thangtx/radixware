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

package org.radixware.kernel.common.client.editors.xmleditor.model;

import java.math.BigDecimal;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.GDate;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.common.PrefixResolver;
import org.apache.xmlbeans.impl.common.ValidationContext;
import org.apache.xmlbeans.impl.values.JavaBase64HolderEx;
import org.apache.xmlbeans.impl.values.JavaBooleanHolderEx;
import org.apache.xmlbeans.impl.values.JavaDecimalHolderEx;
import org.apache.xmlbeans.impl.values.JavaDoubleHolderEx;
import org.apache.xmlbeans.impl.values.JavaFloatHolderEx;
import org.apache.xmlbeans.impl.values.JavaHexBinaryHolderEx;
import org.apache.xmlbeans.impl.values.JavaQNameHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.impl.values.JavaUriHolderEx;
import org.apache.xmlbeans.impl.values.XmlDateImpl;
import org.apache.xmlbeans.impl.values.XmlDurationImpl;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;


public final class XmlValueValidator {

    private final static class ValidationContextImpl implements ValidationContext {

        private String invalidValueReason = null;

        @Override
        public void invalid(final String string) {
            invalidValueReason = string;
        }

        @Override
        public void invalid(final String string, final Object[] os) {
            invalidValueReason = string;
        }

        public boolean isValid() {
            return invalidValueReason == null;
        }

        public String getInvalidValueReason() {
            return invalidValueReason;
        }
    }
    final public static XmlValueValidator INSTANCE = new XmlValueValidator();

    private XmlValueValidator() {
    }

    public ValidationResult validate(final String xmlValueAsStr, final SchemaType schemaType) {
        if (xmlValueAsStr == null){
            return ValidationResult.ACCEPTABLE;
        }
        final ValidationContextImpl _vc = new ValidationContextImpl();
        
        assert schemaType.getSimpleVariety() == SchemaType.ATOMIC;

        switch (schemaType.getPrimitiveType().getBuiltinTypeCode()) {
            case SchemaType.BTC_ANY_SIMPLE: {
                break;
            }
            case SchemaType.BTC_STRING: {
                JavaStringEnumerationHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                break;
            }
            case SchemaType.BTC_DECIMAL: {
                JavaDecimalHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (_vc.isValid()) {
                    final BigDecimal _decimalValue = new BigDecimal(xmlValueAsStr);
                    JavaDecimalHolderEx.validateValue(_decimalValue, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_BOOLEAN: {
                JavaBooleanHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                break;
            }
            case SchemaType.BTC_FLOAT: {                
                final float f =
                        JavaFloatHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (_vc.isValid()) {
                    JavaFloatHolderEx.validateValue(f, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_DOUBLE: {
                final double d =
                        JavaDoubleHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (_vc.isValid()) {
                    JavaDoubleHolderEx.validateValue(d, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_QNAME: {
                final PrefixResolver prefixResolver = new PrefixResolver() {
                    @Override
                    public String getNamespaceForPrefix(final String string) {
                        return null;
                    }
                };
                final QName n =
                        JavaQNameHolderEx.validateLexical(
                        xmlValueAsStr, schemaType, _vc, prefixResolver);
                if (_vc.isValid()) {
                    JavaQNameHolderEx.validateValue(n, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_ANY_URI: {
                JavaUriHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                break;
            }
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_G_YEAR_MONTH:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH: {
                final GDate d = XmlDateImpl.validateLexical(xmlValueAsStr, schemaType, _vc);                                
                if (d != null) {
                    XmlDateImpl.validateValue(d, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_DURATION: {
                final GDuration d = XmlDurationImpl.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (d != null) {
                    XmlDurationImpl.validateValue(d, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_BASE_64_BINARY: {
                final byte[] v =
                        JavaBase64HolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (v != null) {
                    JavaBase64HolderEx.validateValue(v, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_HEX_BINARY: {
                final byte[] v =
                        JavaHexBinaryHolderEx.validateLexical(xmlValueAsStr, schemaType, _vc);
                if (v != null) {
                    JavaHexBinaryHolderEx.validateValue(v, schemaType, _vc);
                }
                break;
            }
            case SchemaType.BTC_NOTATION:
                break;
            default:
                throw new IllegalArgumentException("Unexpected primitive type code");
        }
        if (_vc.isValid()){
            return ValidationResult.ACCEPTABLE;
        }else{
            return ValidationResult.Factory.newInvalidResult(_vc.getInvalidValueReason());
        }
    }
}
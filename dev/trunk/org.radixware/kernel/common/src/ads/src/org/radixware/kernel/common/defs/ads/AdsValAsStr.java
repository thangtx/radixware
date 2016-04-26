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

/*
 * 9/19/11 11:27 AM
 */
package org.radixware.kernel.common.defs.ads;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlDisplayer;
import org.radixware.kernel.common.scml.LiteralWriter;
import org.radixware.kernel.common.scml.Scml.Text;


public abstract class AdsValAsStr extends RadixObject {

    private static final String FALSE_LITERAL = "false";
    private static final String TRUE_LITERAL = "true";

    public static final class Factory {

        private Factory() {
        }

        public static AdsValAsStr newInstance(Jml jml) {

            return jml == null ? NULL_VALUE : new AdsValAsJml(jml);
        }

        public static AdsValAsStr newInstance(ValAsStr val) {

            return val == null ? NULL_VALUE : new AdsValAsString(val);
        }

        public static AdsValAsStr newInstance(String val) {

            return val == null ? NULL_VALUE : new AdsValAsString(ValAsStr.Factory.loadFrom(val));
        }

        public static AdsValAsStr loadFrom(RadixObject context, org.radixware.schemas.adsdef.AdsValAsStr val) {
            if (val != null) {
                if (val.isSetJml()) {
                    return new AdsValAsJml(context, val);
                } else if (val.isSetValAsStr()) {
                    return new AdsValAsString(val);
                }
            }
            return NULL_VALUE;
        }

        public static AdsValAsStr newCopy(AdsValAsStr src) {
            switch (src.getValueType()) {
                case JML:
                    return newInstance(Jml.Factory.newCopy(src.getJml().getContainer(), src.getJml()));
                case VAL_AS_STR:
                    return newInstance(src.getValAsStr());
                default:
                    return NULL_VALUE;
            }
        }

        public static AdsValAsStr newCopy(AdsValAsStr src, RadixObject context) {
            switch (src.getValueType()) {
                case JML:
                    return newInstance(Jml.Factory.newCopy(context, src.getJml()));
                case VAL_AS_STR:
                    return newInstance(src.getValAsStr());
                default:
                    return NULL_VALUE;
            }
        }
    }

    public static final class Converter {

        private Converter() {
        }

        public static String literalToStr(String val) {
            if (val == null) {
                return "";
            }

            StringBuilder sb = new StringBuilder(val);

            String[][] chars = {{"\\\\", "\\"}, {"\\b", ""}, {"\\t", ""}, {"\\n", ""}, {"\\r", ""}, {"\\f", ""}, {"\\\\u", "\\u"}, {"\\\"", "\""}, {"\\\'", "\'"}};

            int pos = 0;
            while (true) {
                int start, min = sb.length();
                String[] minCh = chars[0];
                for (String[] ch : chars) {
                    start = sb.indexOf(ch[0], pos);

                    if (start > -1 && min > start) {
                        min = start;
                        minCh = ch;
                    }
                }
                if (min == sb.length()) {
                    break;
                }
                sb.replace(min, min + minCh[0].length(), minCh[1]);
                pos = min + minCh[1].length();
            }

            return sb.toString();
        }

        public static AdsValAsStr convertTo(EValueType toType, IValueController controller) {

            AdsValAsStr value = null;
            AdsTypeDeclaration type = null;
            Definition context = null;

            if (controller != null) {
                value = controller.getValue();
                type = controller.getContextType();
                context = controller.getContextDefinition();
            }

            return convertTo(toType, value, type, context);
        }

        public static AdsValAsStr convertTo(EValueType toType, AdsValAsStr value, AdsTypeDeclaration type, Definition context) {

            if (toType == null) {
                return null;
            }

            if (value != null && value.typeEquals(toType)) {
                return value;
            }

            switch (toType) {
                case JML:
                    return convertToJml(value, type, context);
                case VAL_AS_STR:
                    return convertToValAsStr(value, type);
                default:
                    return AdsValAsStr.NULL_VALUE;
            }
        }

        private static AdsValAsStr getJmlNull(Definition context, AdsTypeDeclaration type) {
            Jml jml = Jml.Factory.newInstance(context, "");

            if (type != null && !type.isArray()) {
                EValType valType = type.getTypeId();
                if (valType != null) {
                    switch (valType) {
                        case INT:
                        case NUM:
                            jml.getItems().add(Text.Factory.newInstance("0"));
                            break;
                        case BOOL:
                            jml.getItems().add(Text.Factory.newInstance(FALSE_LITERAL));
                            break;
                        case JAVA_TYPE:
                            if ("int".equals(type.getExtStr())) {
                                jml.getItems().add(Text.Factory.newInstance("0"));
                            } else if ("double".equals(type.getExtStr())) {
                                jml.getItems().add(Text.Factory.newInstance("0.0"));
                            } else if ("float".equals(type.getExtStr())) {
                                jml.getItems().add(Text.Factory.newInstance("0.0f"));
                            } else if ("char".equals(type.getExtStr())) {
                                jml.getItems().add(Text.Factory.newInstance("\'\\0\'"));
                            } else if ("boolean".equals(type.getExtStr())) {
                                jml.getItems().add(Text.Factory.newInstance(FALSE_LITERAL));
                            }
                            break;
                        case STR:
                        default:
                            jml.getItems().add(Text.Factory.newInstance("null"));
                    }
                    return Factory.newInstance(jml);
                }
            }
            jml.getItems().add(Text.Factory.newInstance("null"));
            return Factory.newInstance(jml);
        }

        private static AdsValAsStr getValAsStrNull(AdsTypeDeclaration type) {
            if (type != null && !type.isArray() && type.isPure()) {
                EValType valType = type.getTypeId();
                if (valType != null && !type.isArray()) {
                    switch (valType) {
                        case INT:
                        case NUM:
                        case BOOL:
                            return Factory.newInstance("0");
                        case STR:
                        default:
                            return Factory.newInstance("");
                    }
                }
            }
            return Factory.newInstance("");
        }

        private static AdsValAsStr convertToJml(AdsValAsStr value, AdsTypeDeclaration type, Definition context) {
            if (value == null || value == NULL_VALUE || type == null
                    || value.typeEquals(EValueType.NULL) || value.getValueType() == null) {
                return getJmlNull(context, type);
            }

            assert !value.typeEquals(EValueType.JML) : "Convert value into the same type";
            if (value.typeEquals(EValueType.JML)) {
                return value;
            }

            if (type.isArray()) {
                return getJmlNull(context, type);
            }

            Jml jmlValue = Jml.Factory.newInstance(context, "");

            EValType valType = type.getTypeId();
            try {
                switch (valType) {
                    case STR:
                        jmlValue.getItems().add(Text.Factory.newInstance('\"' + LiteralWriter.str2Literal(value.toString()) + '\"'));
                        break;
                    case NUM:
                    case INT:
                    case BOOL:
                        jmlValue.getItems().add(Text.Factory.newInstance(value.getValAsStr().toObject(valType).toString()));
                        break;
                    case CHAR:
                        jmlValue.getItems().add(Text.Factory.newInstance("\'" + LiteralWriter.str2Literal(value.getValAsStr().toObject(valType).toString()) + "\'"));
                        break;
                    default:
                }
            } catch (WrongFormatError e) {
                return getJmlNull(context, type);
            } catch (Exception e) {
                return getJmlNull(context, type);
            }
            return AdsValAsStr.Factory.newInstance(jmlValue);
        }

        private static AdsValAsStr convertToValAsStr(AdsValAsStr value, AdsTypeDeclaration type) {

            if (value == null || value == AdsValAsStr.NULL_VALUE || type == null
                    || value.typeEquals(EValueType.NULL) || value.getValueType() == null) {
                return getValAsStrNull(type);
            }

            assert !value.typeEquals(EValueType.VAL_AS_STR) : "Convert value into the same type";
            if (value.typeEquals(EValueType.VAL_AS_STR)) {
                return value;
            }

            String strValue = value.toString().trim();
            int first, last;

            EValType valType = type.getTypeId();
            switch (valType) {
                case CHAR:
                    first = strValue.indexOf('\'');
                    last = strValue.lastIndexOf('\'');
                    if (first >= 0 && last >= 0) {
                        strValue = literalToStr(strValue.substring(first + 1, last));
                    }
                    break;
                case STR:
                    if ("null".equals(strValue)) {
                        strValue = "";
                    } else {
                        first = strValue.indexOf('\"');
                        last = strValue.lastIndexOf('\"');
                        if (first >= 0 && last >= 0) {
                            strValue = literalToStr(strValue.substring(first + 1, last));
                        }
                    }
                    break;
                case BOOL:
                    strValue = FALSE_LITERAL.equalsIgnoreCase(strValue) || "0".equals(strValue) ? "0" : "1";
                    break;
                default:
                    break;
            }

            ValAsStr valAsStr = null;
            try {
                valAsStr = ValAsStr.Factory.loadFrom(strValue);
                valAsStr.toObject(valType);
            } catch (WrongFormatError e) {
                return getValAsStrNull(type);
            } catch (Exception e) {
                return getValAsStrNull(type);
            }

            return AdsValAsStr.Factory.newInstance(valAsStr);
        }
    }

    public static final class DefaultPresenter {

        private DefaultPresenter() {
        }
        public static final String VAL_AS_STR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        public static final String DATE_FORMAT = "dd MMMMM yyyy HH:mm:ss";
        private static final DateFormat VAL_AS_STR_DATE_FORMATTER = new SimpleDateFormat(VAL_AS_STR_DATE_FORMAT);
        private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

        public static String getAsString(IValueController controller) {
            if (controller != null) {
                return getAsString(controller.getValue(), controller.getContextDefinition(), controller.getContextType());
            }
            return AdsValAsStr.NULL_VALUE.toString();
        }

        public static String getAsString(AdsValAsStr value, Definition context, AdsTypeDeclaration contextType) {

            if (value == null) {
                return AdsValAsStr.NULL_VALUE.toString();
            }

            if (contextType != null) {
                AdsType type = contextType.resolve(context).get();

                if (type instanceof AdsEnumType) {
                    AdsEnumDef enumDef = ((AdsEnumType) type).getSource();
                    for (AdsEnumItemDef item : enumDef.getItems().list(EScope.LOCAL_AND_OVERWRITE)) {
                        if (item.getValue().equals(value.getValAsStr())) {
                            return item.getName();
                        }
                    }
                }

                if (value.typeEquals(EValueType.VAL_AS_STR)) {

                    if (contextType.getTypeId() == EValType.BOOL) {
                        if (value.toString().equals("1")) {
                            return TRUE_LITERAL;
                        } else if (value.toString().equals("0")) {
                            return FALSE_LITERAL;
                        }
                    } else if (contextType.getTypeId() == EValType.DATE_TIME) {

                        try {
                            return DATE_FORMATTER.format(VAL_AS_STR_DATE_FORMATTER.parse(value.toString()));
                        } catch (ParseException ex) {
                            return value.toString();
                        }
                    }
                }
            }

            return value.toString();
        }
    }

    public enum EValueType {

        JML, VAL_AS_STR, NULL
    }

    public interface IValueController {

        boolean isValueTypeAvailable(EValueType type);

        AdsTypeDeclaration getContextType();

        Definition getContextDefinition();

        void setValue(AdsValAsStr value);

        AdsValAsStr getValue();

        String getValuePresentation();
    }
    public static final AdsValAsStr NULL_VALUE = new AdsValAsStr() {
        @Override
        public void appendTo(org.radixware.schemas.adsdef.AdsValAsStr val) {
            //ignore
        }

        @Override
        public Jml getJml() {
            return null;
        }

        @Override
        public ValAsStr getValAsStr() {
            return null;
        }

        @Override
        public EValueType getValueType() {
            return EValueType.NULL;
        }

        @Override
        public String toString() {
            return "<Not Defined>";
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public int hashCode() {
            return 777;
        }
    };

    private static final class AdsValAsJml extends AdsValAsStr {

        private Jml jml;

        public AdsValAsJml(Jml jml) {
            super();

            this.jml = jml;
        }

        public AdsValAsJml(RadixObject context, org.radixware.schemas.adsdef.AdsValAsStr val) {
            super();

            jml = Jml.Factory.loadFrom(context, val.getJml(), "");
        }

        @Override
        public Jml getJml() {
            return jml;
        }

        @Override
        public void appendTo(org.radixware.schemas.adsdef.AdsValAsStr val) {
            jml.appendTo(val.addNewJml(), AdsDefinition.ESaveMode.NORMAL);
        }

        @Override
        public ValAsStr getValAsStr() {
            return null;
        }

        @Override
        public String toString() {
            return JmlDisplayer.toReadableString(jml);
        }

        @Override
        public EValueType getValueType() {
            return EValueType.JML;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj instanceof AdsValAsJml) {
                return JmlDisplayer.toReadableString(jml).equals(JmlDisplayer.toReadableString(((AdsValAsJml) obj).getJml()));
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.jml != null ? this.jml.hashCode() : 0);
            return hash;
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);

            if (jml != null) {
                jml.visit(visitor, provider);
            }
        }
    }

    private static final class AdsValAsString extends AdsValAsStr {

        private final ValAsStr value;

        public AdsValAsString(ValAsStr value) {
            super();
            this.value = value;
        }

        public AdsValAsString(org.radixware.schemas.adsdef.AdsValAsStr val) {
            super();

            value = ValAsStr.Factory.loadFrom(val.getValAsStr());
        }

        @Override
        public ValAsStr getValAsStr() {
            return value;
        }

        @Override
        public void appendTo(org.radixware.schemas.adsdef.AdsValAsStr val) {
            val.setValAsStr(value.toString());
        }

        @Override
        public Jml getJml() {
            return null;
        }

        @Override
        public EValueType getValueType() {
            return EValueType.VAL_AS_STR;
        }

        @Override
        public String toString() {
            return value.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof AdsValAsString) {
                return value.equals(((AdsValAsString) obj).value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(AdsValAsStr src, EValType type) {
            try {
                if (super.equals(src, type)) {
                    return true;
                } else if (src.getValueType() == EValueType.VAL_AS_STR && src.getValAsStr() != null) {
                    Object valObject = value.toObject(type);
                    if (valObject != null) {
                        return valObject.equals(src.getValAsStr().toObject(type));
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean typeEquals(EValueType type) {
        return getValueType() == type;
    }

    public abstract void appendTo(org.radixware.schemas.adsdef.AdsValAsStr val);

    public abstract Jml getJml();

    public abstract ValAsStr getValAsStr();

    public abstract EValueType getValueType();

    public boolean equals(AdsValAsStr src, EValType type) {
        return equals(src);
    }
}

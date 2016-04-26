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

package org.radixware.kernel.designer.common.dialogs.choosetype;

import java.util.*;
import javax.swing.AbstractListModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import static org.radixware.kernel.common.enums.EValType.*;
import org.radixware.kernel.designer.common.dialogs.utils.SearchFieldAdapter;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public class RadixTypeItem implements Comparable<RadixTypeItem> {

    private static final Comparator<RadixTypeItem> TYPE_NAME_COMPARATATOR;

    static {
        TYPE_NAME_COMPARATATOR = new Comparator<RadixTypeItem>() {

            static final int UPPER_ELEMENT = -1;
            static final int LOWER_ELEMENT = 1;

            @Override
            public int compare(RadixTypeItem t1, RadixTypeItem t2) {
                int result = t1.toString().compareTo(t2.toString());
                if (t1.isSimple() && t2.isSimple()) {
                    if (t2.getType().equals(USER_CLASS) 
                            || (t2.getType().equals(XML) && !t1.getType().equals(USER_CLASS))) {
                        result = LOWER_ELEMENT;
                    } else if ((t1.getType().equals(USER_CLASS))
                        || (t1.getType().equals(XML) && !t2.getType().equals(USER_CLASS))) {
                        result = UPPER_ELEMENT;
                    } else {
                        result = t1.getSimpleTypeVal().compareTo(t2.getSimpleTypeVal());
                        if (result == 0) {
                            result = t1.compareTo(t2);
                        }
                    }
                }

                if (t1.isEnumeration() || t1.isModel()) {
                    if (t2.isSimple() && t2.getType().equals(USER_CLASS)) {
                        result = LOWER_ELEMENT;
                    } else {
                        result = UPPER_ELEMENT;
                    }
                } else if (t1.isArrEnumeration()) {
                    result = LOWER_ELEMENT;
                } else if (t2.isEnumeration() || t2.isModel()) {
                    if (t1.isSimple() && t1.getType().equals(USER_CLASS)) {
                        result = UPPER_ELEMENT;
                    } else {
                        result = LOWER_ELEMENT;
                    }
                } else if (t2.isArrEnumeration()) {
                    result = UPPER_ELEMENT;
                }

                if (result == 0 && t1.getDefinition() != null && t2.getDefinition() != null) {
                    result = t1.getDefinition().getQualifiedName().compareTo(t2.getDefinition().getQualifiedName());
                }
                return result;
            }
        };
    }
    public static final Set<EValType> STRICTLY_REFINABLE = Collections.unmodifiableSet(EnumSet.of(OBJECT, PARENT_REF, ARR_REF, USER_CLASS, XML));
    private final String ENUMERATION = NbBundle.getMessage(RadixTypeItem.class, "TypeNaturePanel-Enumeration");
    private final String ARR_ENUMERATION = NbBundle.getMessage(RadixTypeItem.class, "TypeNaturePanel-Arr-Enumeration");
    private final String MODEL = NbBundle.getMessage(RadixTypeItem.class, "TypeNaturePanel-ModelItem");
    private EValType type;
    private boolean refinable = false;
    private RadixObject definition;
    private String xmltype;
    private boolean isEnumeration = false;
    private boolean isArrEnumeration = false;
    private boolean isModel = false;

    private RadixTypeItem() {
    }

    private RadixTypeItem(RadixObject definition) {
        this.definition = definition;
    }

    private RadixTypeItem(EValType type, boolean refinable) {
        this.type = type;
        this.refinable = refinable;
    }

    private RadixTypeItem(String xmltype, IXmlDefinition scheme) {
        this.xmltype = xmltype;
        this.definition = (RadixObject) scheme;
    }

    @Override
    public int hashCode() {
        if (this.isSimple()) {
            int res = 0;
            Long res1 = type != null ? type.getValue() : 0;
            int res2 = this.toString().hashCode();
            res += res1;
            res += res2;
            return res;
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RadixTypeItem)) {
            return false;
        }

        return super.equals(obj);
    }

    @Override
    public int compareTo(RadixTypeItem other) {
        int result = other.toString().compareTo(this.toString());
        return result;
    }

    @Override
    public String toString() {
        if (type != null) {
            return (/*
                 * refinable ||
                 */STRICTLY_REFINABLE.contains(type)) ? type.getName() + " ..." : type.getName();
        }

        if (isEnumeration) {
            return ENUMERATION + "...";
        }

        if (isArrEnumeration) {
            return ARR_ENUMERATION + "...";
        }

        if (isModel) {
            return MODEL + "...";
        }

        if (definition != null) {
            if (xmltype != null && !xmltype.isEmpty()) {
                return xmltype;
            } else {
                return definition.getName();
            }
        }

        if (xmltype != null && !xmltype.isEmpty()) {
            return xmltype;
        }
        return "<undefined>";
    }

    public String getXmltype() {
        return xmltype;
    }

    public boolean isSimple() {
        return type != null;
    }

    public boolean isXmlType() {
        return xmltype != null && !xmltype.isEmpty();
    }

    public boolean isXmlScheme() {
        return (definition != null && (definition instanceof IXmlDefinition)) && !isXmlType();
    }

    public boolean isEnumeration() {
        return isEnumeration;
    }

    void setAsEnumeration() {
        isEnumeration = true;
    }

    public boolean isArrEnumeration() {
        return isArrEnumeration;
    }

    void setAsArrEnumeration() {
        isArrEnumeration = true;
    }

    public boolean isModel() {
        return isModel;
    }

    void setAsModel() {
        this.isModel = true;
    }

    public EValType getType() {
        return type;
    }

    public RadixObject getDefinition() {
        return definition;
    }

    public Long getSimpleTypeVal() {
        return type.getValue();
    }

    public boolean isRefinable() {
        return refinable;
    }
    private static final Set<EValType> SUITABLE_TYPES = Collections.unmodifiableSet(EnumSet.of(
        BIN, BLOB, BOOL, CHAR, CLOB, DATE_TIME, INT, NUM, STR, XML, OBJECT, PARENT_REF,
        ARR_BIN, ARR_BLOB, ARR_BOOL, ARR_CHAR, ARR_CLOB, ARR_DATE_TIME,
        ARR_INT, ARR_NUM, ARR_STR, ARR_REF, USER_CLASS));
    static final Set<EValType> ENUM_TYPES = Collections.unmodifiableSet(EnumSet.of(
        CHAR, INT, NUM, STR));
    private static final Set<EValType> ARR_ENUM_TYPES = Collections.unmodifiableSet(EnumSet.of(
        ARR_INT, ARR_CHAR, ARR_NUM, ARR_STR));

    static TypeListModel getXmlSchemeTypesModel(Definition context) {
        Set<RadixTypeItem> res = new HashSet<RadixTypeItem>();

        assert context instanceof IXmlDefinition;

        if (context instanceof IXmlDefinition) {
            Collection<String> types = ((IXmlDefinition) context).getSchemaTypeList();
            for (String s : types) {
                if (s.startsWith(".")) {
                    s = s.substring(1);
                }
                res.add(new RadixTypeItem(s, (IXmlDefinition) context));
            }
        }

        return new TypeListModel(res);
    }

    static TypeListModel getXmlSchemesModel(Definition context) {
        Set<RadixTypeItem> res = new HashSet<RadixTypeItem>();

        Collection<Definition> definitions = DefinitionsUtils.collectTopAround(context, new XmlChooseProvider());
        for (Definition definition : definitions) {
            res.add(new RadixTypeItem(definition));
        }
        return new TypeListModel(res);
    }

    public static TypeListModel getSystemTypesModel(IAdsTypedObject param, Definition context) {
        Set<RadixTypeItem> types = new HashSet<RadixTypeItem>();
        boolean enumTypesIncluded = false;
        boolean arrEnumTypesIncluded = false;
        boolean modelIncluded = false;
        ArrayList<EValType> enumRestrictions = new ArrayList<EValType>();
        for (EValType e : SUITABLE_TYPES) {
            if (param != null) {
                if (param.isTypeAllowed(e)) {
                    final boolean isRefineAllowed = param.isTypeRefineAllowed(e);
                    if (isRefineAllowed) {
                        if (ENUM_TYPES.contains(e)) {
                            enumRestrictions.add(e);
                            if (!enumTypesIncluded) {
                                enumTypesIncluded = true;
                            }
                        }
                        if (ARR_ENUM_TYPES.contains(e)) {
                            enumRestrictions.add(e);
                            if (!arrEnumTypesIncluded) {
                                arrEnumTypesIncluded = true;
                            }
                        }
                    }

                    if (e.equals(USER_CLASS) && context != null) {
                        if (!modelIncluded) {
                            if (context instanceof IEnvDependent) {
                                modelIncluded = ((IEnvDependent) context).getUsageEnvironment().isClientEnv();
                            }
                        }
                    }
                    types.add(new RadixTypeItem(e, isRefineAllowed));
                }
            } else {
                types.add(new RadixTypeItem(e, false));
            }
        }

        if (enumTypesIncluded) {
            final RadixTypeItem enumItem = new RadixTypeItem();
            enumItem.setAsEnumeration();
            types.add(enumItem);
        }

        if (arrEnumTypesIncluded) {
            final RadixTypeItem arrEnumItem = new RadixTypeItem();
            arrEnumItem.setAsArrEnumeration();
            types.add(arrEnumItem);
        }
        if (modelIncluded) {
            final RadixTypeItem modelItem = new RadixTypeItem();
            modelItem.setAsModel();
            types.add(modelItem);
        }

        return enumRestrictions.size() > 0 ? new TypeListModel(types, enumRestrictions) : new TypeListModel(types);
    }

    public static TypeListModel getModelFor(Set<RadixTypeItem> items) {
        return new TypeListModel(items);
    }

    public static final class TypeListModel extends AbstractListModel {

        private Set<RadixTypeItem> currentTypes;
        private ArrayList<EValType> enumRestrictions = new ArrayList<EValType>();

        TypeListModel(Set<RadixTypeItem> types, ArrayList<EValType> enumRestrictions) {
            this(types);
            this.enumRestrictions = enumRestrictions;
        }

        TypeListModel(Set<RadixTypeItem> types) {
            currentTypes = new TreeSet<RadixTypeItem>(RadixTypeItem.TYPE_NAME_COMPARATATOR);
            currentTypes.addAll(types);
        }

        EValType getEnumRestriction() {
            if (enumRestrictions.size() == 1) {
                return enumRestrictions.get(0);
            }
            return null;
        }

        @Override
        public Object getElementAt(int index) {
            return currentTypes.toArray()[index];
        }

        @Override
        public int getSize() {
            return currentTypes.size();
        }

        Set<RadixTypeItem> getItems() {
            return Collections.unmodifiableSet(currentTypes);
        }

        Set<RadixTypeItem> findMatches(String text) {
            String lc = text.toLowerCase();
            Set<RadixTypeItem> matches = new TreeSet<RadixTypeItem>(RadixTypeItem.TYPE_NAME_COMPARATATOR);
            for (RadixTypeItem item : currentTypes) {
                String tlc = item.toString().toLowerCase();
                if (SearchFieldAdapter.isFitingToken(lc, tlc)) {
                    matches.add(item);
                }
            }
            return matches;
        }
    }

    private static class XmlChooseProvider extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject obj) {
            return obj instanceof IXmlDefinition;
        }

        @Override
        public boolean isContainer(RadixObject object) {
            return true;
        }
    }
}

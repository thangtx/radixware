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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.text.Collator;
import java.util.*;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import org.apache.commons.lang.StringEscapeUtils;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon.Property;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Field;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector.IItemInfo;


public class AdsClassMembersUtils {

    @Deprecated
    public static class TransparentMethodItem implements Comparable<TransparentMethodItem> {

        public static final String EMPTY_LIST = NbBundle.getMessage(AdsClassMembersUtils.class, "MethodUtils-EmptyList");
        public static final Comparator<TransparentMethodItem> METHODNAME_COMPARATOR;

        static {
            METHODNAME_COMPARATOR = new Comparator<TransparentMethodItem>() {
                @Override
                public int compare(TransparentMethodItem item1, TransparentMethodItem item2) {
                    int result = Collator.getInstance().compare(item1.toString(), item2.toString());
                    return result;
                }
            };
        }
        private AdsClassMembersUtils.TransparentMethodInfo info;

        public TransparentMethodItem(AdsClassMembersUtils.TransparentMethodInfo info) {
            this.info = info;
        }

        public AdsClassMembersUtils.TransparentMethodInfo getMethodInfo() {
            return info;
        }

        @Override
        public String toString() {
            return info != null ? info.toString() : "<Not Defined>";
        }

        public Method getMethod() {
            return info.getMethod();
        }

        @Override
        public int compareTo(TransparentMethodItem item) {
            int result = item.toString().compareTo(toString());
            return result;
        }

        public static MethodItemModel getModelFor(Set<TransparentMethodItem> items) {
            Set<TransparentMethodItem> res = new HashSet<TransparentMethodItem>();
            if (items != null) {
                res.addAll(items);
            }
            return new MethodItemModel(res);
        }

        public static MethodItemModel getModelFor(HashSet<AdsClassMembersUtils.TransparentMethodInfo> items) {
            Set<TransparentMethodItem> res = new HashSet<TransparentMethodItem>();
            if (items != null) {
                for (AdsClassMembersUtils.TransparentMethodInfo m : items) {
                    res.add(new TransparentMethodItem(m));
                }
            }
            return new MethodItemModel(res);
        }

        public static AbstractListModel getEmptyModel() {
            DefaultListModel lm = new DefaultListModel();
            lm.addElement(EMPTY_LIST);
            return lm;
        }
    }

    @Deprecated
    public static class MethodItemModel extends AbstractListModel {

        private Set<TransparentMethodItem> currentcontent;

        public MethodItemModel() {
            currentcontent = new TreeSet<TransparentMethodItem>(TransparentMethodItem.METHODNAME_COMPARATOR);
        }

        public MethodItemModel(Set<TransparentMethodItem> items) {
            currentcontent = new TreeSet<TransparentMethodItem>(TransparentMethodItem.METHODNAME_COMPARATOR);
            currentcontent.addAll(items);
        }

        @Override
        public Object getElementAt(int index) {
            return currentcontent.toArray()[index];
        }

        @Override
        public int getSize() {
            return currentcontent.size();
        }

        public void addMethodItem(TransparentMethodItem item) {
            if (item != null
                    && !currentcontent.contains(item)) {
                int size = currentcontent.size();
                currentcontent.add(item);
                fireContentsChanged(this, 0, size);
            }
        }

        public void addMethodItems(Collection<TransparentMethodItem> items) {
            if (items != null) {
                int size = currentcontent.size();
                currentcontent.addAll(items);
                fireContentsChanged(this, 0, size);
            }
        }

        public void removeMethodItem(TransparentMethodItem item) {
            if (item != null) {
                int size = currentcontent.size();
                currentcontent.remove(item);
                fireContentsChanged(this, 0, size);
            }
        }

        public void removeMethodItems(Collection<TransparentMethodItem> items) {
            if (items != null) {
                int size = currentcontent.size();
                currentcontent.removeAll(items);
                fireContentsChanged(this, 0, size);
            }
        }
    }

    public static final class TransparentMethodInfo implements IItemInfo {

        private RadixPlatformClass owner;
        private Method method;
        private Definition context;
        private String name_params;
        private String throwsName;
        private String short_name;
        private String mods;
        private String returnvalue;
        private RadixIcon icon = AdsDefinitionIcon.Method.METHOD;

        public TransparentMethodInfo(Method method, Definition context) {
            this.context = context;
            this.method = method;
            init();
        }

        public TransparentMethodInfo(RadixPlatformClass owner, Method method, Definition context) {
            this.method = method;
            this.context = context;
            this.owner = owner;
            init();
        }

        @Override
        public String getShortName() {
            return short_name;
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getHtmlName() {
            return "<html><body><nobr>" + StringEscapeUtils.escapeHtml(mods + " " + returnvalue) + " <b>" + StringEscapeUtils.escapeHtml(name_params) + "</b> " + StringEscapeUtils.escapeHtml(throwsName) + "</nobr></body></html>";
        }

        public Method getMethod() {
            return method;
        }

        @Override
        public Icon getIcon() {
            return icon.getIcon();
        }

        @Override
        public String getName() {
            return mods + " " + returnvalue + " " + name_params + " " + throwsName;
        }

        private void init() {
            final EAccess access = method.getAccess();
            mods = access != null ? access.getName() : "<undefined access modifier>";

            if (method.isAbstract()) {
                mods += " abstract";
                icon = AdsDefinitionIcon.Method.METHOD_ABSTRACT;
            }
            if (method.isFinal()) {
                mods += " final";
            }
            if (method.isStatic()) {
                mods += " static";
                icon = AdsDefinitionIcon.Method.METHOD_STATIC;
            }
            if (!method.isConstructor()) {
                name_params = method.getName();
                short_name = method.getName();
            } else {
                name_params = owner != null ? owner.getDeclaration().getName(context) : context.getName();
                short_name = name_params;
                icon = AdsDefinitionIcon.Method.CONSTRUCTOR;
            }
            name_params += "(";
            AdsTypeDeclaration[] params = method.getParameterTypes();
            if (params != null && params.length > 0) {
                boolean isFirst = true;

                for (int i = 0; i < params.length; ++i) {
                    final AdsTypeDeclaration a = params[i];
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        name_params += ", ";
                    }
                    if (i == params.length - 1 && method.isVarargs() && a.isArray()) {
                        name_params += a.toArrayType(a.getArrayDimensionCount() - 1).getName(context) + "...";
                    } else {
                        name_params += a.getName(context);
                    }
                }
            }
            name_params += ")";

            throwsName = "";
            AdsTypeDeclaration[] exceptions = method.getExceptions();
            if (exceptions != null && exceptions.length > 0) {
                throwsName += "throws ";
                boolean isFirst = true;
                for (AdsTypeDeclaration a : exceptions) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        throwsName += ", ";
                    }
                    throwsName += a.getName(context);
                }
            }
            if (!method.isConstructor()) {
                returnvalue = method.getReturnType().getName(context);
            } else {
                returnvalue = "";
            }
        }
    }

    public static final class TransparentFieldInfo implements IItemInfo {

        private Field field;
        private Definition context;
        private String shortName;
        private String mods;
        private String type;
        private String name;
        private RadixIcon icon = AdsDefinitionIcon.Property.PROPERTY_DYNAMIC;

        public TransparentFieldInfo(Field field, Definition context) {
            this.context = context;
            this.field = field;
            init();
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            return icon.getIcon();
        }

        @Override
        public String getHtmlName() {
            return "<html><body><nobr>" + StringEscapeUtils.escapeHtml(mods + " " + type) + " <b>" + StringEscapeUtils.escapeHtml(name) + "</b></nobr></body></html>";
        }

        @Override
        public String getShortName() {
            return shortName;
        }

        @Override
        public String getName() {
            return mods + " " + type + " " + name;
        }

        private void init() {
            final EAccess access = field.getAccess();
            mods = access != null ? access.getName() : "<undefined access modifier>";

            if (field.isFinal()) {
                mods += " final";
            }
            if (field.isStatic()) {
                mods += " static";
            }

            icon = initIcon();

            name = field.getName();
            shortName = field.getName();

            type = field.getValueType().getName(context);
        }

        private RadixIcon initIcon() {
            switch (field.getAccess()) {
                case DEFAULT:
                    return field.isStatic()
                            ? Property.PROPERTY_DYNAMIC_STATIC_INTERNAL
                            : Property.PROPERTY_DYNAMIC_INTERNAL;
                case PRIVATE:
                    return field.isStatic()
                            ? Property.PROPERTY_DYNAMIC_STATIC_PRIVATE
                            : Property.PROPERTY_DYNAMIC_PRIVATE;
                case PROTECTED:
                    return field.isStatic()
                            ? Property.PROPERTY_DYNAMIC_STATIC_PROTECTED
                            : Property.PROPERTY_DYNAMIC_PROTECTED;
                case PUBLIC:
                    return field.isStatic()
                            ? Property.PROPERTY_DYNAMIC_STATIC
                            : Property.PROPERTY_DYNAMIC;
            }
            return Property.PROPERTY_DYNAMIC;
        }
    }

    /*
     *
     * Methods
     *
     */
    @Deprecated
    private static MethodParameter containsType(AdsDefinition context, List<MethodParameter> params, AdsTypeDeclaration type) {
        for (MethodParameter mp : params) {
            AdsTypeDeclaration mpType = mp.getType();
            if (mpType.equalsTo(context, type)) {
                return mp;
            } else {
                if (mpType.isGeneric() && type.isGeneric()) {
                    AdsType mpadsType = mpType.resolve(context).get();
                    AdsType tadsType = type.resolve(context).get();
                    if (Utils.equals(mpadsType, tadsType)) {
                        return mp;
                    }
                }
            }
        }
        return null;
    }

    @Deprecated
    private static Set<RadixPlatformClass> getAllSuperClasses(RadixPlatformClass childClass, PlatformLib lib) {
        final Set<RadixPlatformClass> res = new HashSet<>();
        AdsTypeDeclaration superDeclaration = childClass.getSuperclass();
        boolean stop = false;

        while (superDeclaration != null && !stop) {
            final String superName = superDeclaration.getExtStr();
            final RadixPlatformClass superClass = lib.findPlatformClass(superName);
            if (superClass != null) {
                res.add(superClass);
                superDeclaration = superClass.getSuperclass();
            } else {
                stop = true;
            }
        }
        return res;
    }

    @Deprecated
    public static Collection<Method> getMethods(AdsClassDef adsclass, RadixPlatformClass platformClass, boolean isPublished, AdsTransparentMethodDef adsMethod) {

        assert false : "deprecate";

//        if (platformClass != null) {
//
//            final List<AdsMethodDef> adsMethods =  adsclass.getMethods().get(EScope.ALL, new AdsClassMembersUtils.TransparencyFilter<AdsMethodDef>());
//            final Set<Method> published = new HashSet<>();
//            final Method[] platformMethods = platformClass.getMethods();
//
//            for (final AdsMethodDef adsm : adsMethods) {
//                final Method method = platformClass.findMethodByProfile(adsm);
//                if (method != null) {
//                    published.add(method);
//                }
//            }
//
//            final Set<Method> methods = new HashSet<>();
//            for (final Method m : platformMethods) {
//                if (m.getAccess() == EAccess.PRIVATE || m.getAccess() == EAccess.DEFAULT) {
//                    continue;
//                }
//                if (!published.contains(m)) {
//                    if (isPublished) {
//                        if (adsMethod.isConstructor() == m.isConstructor()) {
//                            methods.add(m);
//                        }
//                    } else {
//                        methods.add(m);
//                    }
//                }
//            }
//            return methods;
//        }
        return Collections.<Method>emptyList();
    }
//    public static Collection<Field> getFields(AdsClassDef adsclass, RadixPlatformClass platformClass) {
//        final Collection<Field> fields = new ArrayList<>();
//
//        final List<AdsPropertyDef> adsProperties = adsclass.getProperties().get(EScope.ALL, new AdsClassMembersUtils.TransparencyFilter<AdsPropertyDef>());
//        final Set<Field> published = new HashSet<>();
//        final Field[] platformFields = platformClass.getFields();
//
//        for (final AdsPropertyDef prop : adsProperties) {
//            Field field = platformClass.findFieldByProfile(prop);
//            if (field != null) {
//                published.add(field);
//            }
//        }
//
//        for (final Field field : platformFields) {
//            if (field.getAccess() == EAccess.PRIVATE || field.getAccess() == EAccess.DEFAULT || !field.isStatic()) {
//                continue;
//            }
//            if (!published.contains(field)) {
//                fields.add(field);
//            }
//        }
//
//        return fields;
//    }
}

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
package org.apache.xmlbeans.impl.schema;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaAttributeGroup;
import org.apache.xmlbeans.SchemaComponent;
import org.apache.xmlbeans.SchemaGlobalAttribute;
import org.apache.xmlbeans.SchemaGlobalElement;
import org.apache.xmlbeans.SchemaIdentityConstraint;
import org.apache.xmlbeans.SchemaModelGroup;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;

public class AdsStscUtils {

    public static StscState create() {
        StscState state = StscState.start();
        state.setDependencies(new SchemaDependencies());
        return state;
    }

    public static String getTypeNamespace(SchemaType schemaType) {
        return schemaType == null ? null : (schemaType instanceof SchemaTypeImpl ? ((SchemaTypeImpl) schemaType).getContainer().getNamespace() : null);
    }
//
//    public static SchemaTypeSystem getTypeSystem(String xmlns) {
//
//        StscState state = StscState.get();
//        SchemaContainer container = state.getContainer(xmlns);
//        String[] names = xmlns.split("/");
//        String name = names[names.length - 1];
//        int dot = name.indexOf('.');
//        if (dot > 0) {
//            name = name.substring(0, dot);
//        }
//        SchemaTypeSystemImpl impl3 = new FilteredSts(name, xmlns);
//        container.setTypeSystem(impl3);
//        impl3.loadFromStscState(state);
//        return impl3;
//    }
//
//    public static SchemaType findGlobalType(QName qname) {
//        return StscState.get().findGlobalType(qname, null, null);
//    }
//
//    public static SchemaGlobalElement findGlobalElement(QName qname) {
//        return StscState.get().findGlobalElement(qname, null, null);
//    }
//
//    private static class FilteredSts extends SchemaTypeSystemImpl {
//
//        private String ns;
//
//        public FilteredSts(String nameForSystem, String ns) {
//            super(nameForSystem);
//            this.ns = ns;
//        }
//
//        private <T extends SchemaComponent> T[] filter(T[] src, Class<T> clazz) {
//            List<T> list = new LinkedList<>();
//            for (T s : src) {
//                if (s.getTypeSystem() == this) {
//                    list.add(s);
//                    continue;
//                } else {
//                    System.err.println("Type system mismatch: " + s.getTypeSystem());
//                }
//
////                SchemaContainer container = null;
////                if (s instanceof SchemaTypeImpl) {
////                    container = ((SchemaTypeImpl) s).getContainer();
////                } else if (s instanceof SchemaGlobalElementImpl) {
////                    container = ((SchemaGlobalElementImpl) s).getContainer();
////                } else if (s instanceof SchemaGlobalAttributeImpl) {
////                    container = ((SchemaGlobalAttributeImpl) s).getContainer();
////                } else if (s instanceof SchemaModelGroupImpl) {
////                    container = ((SchemaModelGroupImpl) s).getContainer();
////                } else if (s instanceof SchemaAttributeGroupImpl) {
////                    container = ((SchemaAttributeGroupImpl) s).getContainer();
////                } else if (s instanceof SchemaAnnotationImpl) {
////                    container = ((SchemaAnnotationImpl) s).getContainer();
////                } else if (s instanceof SchemaIdentityConstraintImpl) {
////                    container = ((SchemaIdentityConstraintImpl) s).getContainer();
////                }
//////                if (s.getTypeSystem() != this) {
//////                    continue;
//////                }
////                if (container == null) {
////                    continue;
////                }
////
////
////                if (container.getNamespace().equals(ns)) {
////                    list.add(s);
////                } else {
////                     
////                }
//            }
//            return list.toArray((T[]) Array.newInstance(clazz, list.size()));
//        }
//        private SchemaType[] fgt = null;
//
//        @Override
//        public SchemaType[] globalTypes() {
//            if (fgt == null) {
//                fgt = filter(super.globalTypes(), SchemaType.class);
//            }
//            return fgt;
//        }
//        private SchemaType[] rgt = null;
//
//        @Override
//        public SchemaType[] redefinedGlobalTypes() {
//            if (rgt == null) {
//                rgt = filter(super.redefinedGlobalTypes(), SchemaType.class);
//            }
//            return rgt;
//        }
//        private SchemaType[] dt = null;
//
//        @Override
//        public SchemaType[] documentTypes() {
//            if (dt == null) {
//                dt = filter(super.documentTypes(), SchemaType.class);
//            }
//            return dt;
//        }
//        private SchemaType[] at = null;
//
//        @Override
//        public SchemaType[] attributeTypes() {
//            if (at == null) {
//                at = filter(super.attributeTypes(), SchemaType.class);
//            }
//            return at;
//        }
//
//        @Override
//        public SchemaGlobalElement[] globalElements() {
//            return filter(super.globalElements(), SchemaGlobalElement.class);
//        }
//
//        @Override
//        public SchemaGlobalAttribute[] globalAttributes() {
//            return filter(super.globalAttributes(), SchemaGlobalAttribute.class);
//        }
//
//        @Override
//        public SchemaModelGroup[] redefinedModelGroups() {
//            return filter(super.redefinedModelGroups(), SchemaModelGroup.class);
//        }
//
//        @Override
//        public SchemaAttributeGroup[] attributeGroups() {
//            return filter(super.attributeGroups(), SchemaAttributeGroup.class);
//        }
//
//        @Override
//        public SchemaAttributeGroup[] redefinedAttributeGroups() {
//            return filter(super.redefinedAttributeGroups(), SchemaAttributeGroup.class);
//        }
//
//        @Override
//        public SchemaAnnotation[] annotations() {
//            return filter(super.annotations(), SchemaAnnotation.class);
//        }
//
//        @Override
//        public SchemaIdentityConstraint[] identityConstraints() {
//            return filter(super.identityConstraints(), SchemaIdentityConstraint.class);
//        }
//    }
}

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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;


@RadixObjectCheckerRegistration
public class AdsClassCatalogChecker extends AdsDefinitionChecker<AdsClassCatalogDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsClassCatalogDef.class;
    }

//    private static String sameName(AdsClassCatalogDef.Topic t1, AdsClassCatalogDef.Topic t2, Set<EIsoLanguage> languages) {
//        for (EIsoLanguage lang : languages) {
//            String n1 = t1.getTitle(lang);
//            String n2 = t2.getTitle(lang);
//            if (Utils.equals(n1, n2)) {
//                return lang.getValue() + ":" + n1;
//            }
//        }
//        return null;
//    }
//
//    private static String calcPureTopicName(AdsClassCatalogDef.Topic t, EIsoLanguage lang) {
//        StringBuilder sb = new StringBuilder();
//        if (t.getParentTopicId() == null) {
//            String result = t.getTitle(lang);
//            if (result == null) {
//                result = "-";
//            }
//            return result;
//        } else {
//            AdsClassCatalogDef.Topic p = t.findParentTopic();
//            String parentName = "";
//            if (p != null) {
//                parentName = calcPureTopicName(p, lang);
//            }
//            String result = t.getTitle(lang);
//            if (result == null) {
//                result = "-";
//            }
//            return parentName + "/" + result;
//        }
//    }
//
//    private static Set<EIsoLanguage> getLanguages(RadixObject r) {
//        Module m = r.getModule();
//        if (m == null) {
//            return Collections.emptySet();
//        }
//        Segment s = m.getSegment();
//        if (s == null) {
//            return Collections.emptySet();
//        }
//        Layer l = s.getLayer();
//        if (l == null) {
//            return Collections.emptySet();
//        }
//        return l.getLanguages();
//    }
//
//    private static String calcTopicName(AdsClassCatalogDef.Topic t, EIsoLanguage lang) {
//        return lang + ":" + calcPureTopicName(t, lang);
//    }
//
//    private static int getDept(AdsClassCatalogDef.Topic topic) {
//        int dept = 1;
//        AdsClassCatalogDef.Topic parent = topic.findParentTopic();
//        while (parent != null) {
//            dept++;
//            parent = parent.findParentTopic();
//        }
//        return dept;
//    }
//
//    private static void checkForTopicDuplication(AdsClassCatalogDef start, IProblemHandler ph) {
//        if (start instanceof AdsClassCatalogDef.Overwrite) {
//            return;
//        }
//        AdsClassCatalogDef ovr = start.getHierarchy().findOverridden();
//        List<AdsClassCatalogDef> all = new LinkedList<AdsClassCatalogDef>();
//        while (ovr != null) {
//            all.add(ovr);
//            ovr = ovr.getHierarchy().findOverridden();
//        }
//        if (all.isEmpty()) {
//            return;
//        }
//
//        List<AdsClassCatalogDef.Topic> roots = new LinkedList<AdsClassCatalogDef.Topic>();
//
//        for (AdsClassCatalogDef cc : all) {
//            for (AdsClassCatalogDef.Topic t : cc.getTopicList()) {
//                roots.add(t);
//            }
//        }
//        if (roots.isEmpty()) {
//            return;
//        }
//
//        Set<EIsoLanguage> languages = getLanguages(start);
//        for (EIsoLanguage lang : languages) {
//            Map<String, Object> sameNames = new HashMap<String, Object>(1);
//            //in first - look for roots with same names
//
//            List<AdsClassCatalogDef.Topic> topics = start.getTopicList();
//            Collections.sort(topics, new Comparator<AdsClassCatalogDef.Topic>() {
//
//                @Override
//                public int compare(Topic o1, Topic o2) {
//                    int d1 = getDept(o1);
//                    int d2 = getDept(o2);
//                    return d1 == d2 ? 0 : d1 > d2 ? -1 : 1;
//                }
//            });
//
//            Set<AdsClassCatalogDef.Topic> reported = new HashSet<AdsClassCatalogDef.Topic>();
//            for (AdsClassCatalogDef.Topic t : topics) {
//                if (t.getOwnerClassCatalog() != start) {
//                    continue;
//                }
//                if (reported.contains(t)) {
//                    continue;
//                }
//                final String lookupName = calcTopicName(t, lang);
//                for (AdsClassCatalogDef.Topic another : roots) {
//                    if (another.getId() == t.getId()) {//overwrite
//                        continue;
//                    }
//                    final String name = calcTopicName(another, lang);
//                    if (Utils.equals(name, lookupName)) {
//
//                        warning(start, ph, "Topic title for language \"" + lang.getName() + "\": " + calcPureTopicName(t, lang) + " is duplicated with title of another topic in class catalog " + another.getOwnerClassCatalog().getQualifiedName());
//                        reported.add(t);
//                        AdsClassCatalogDef.Topic parent = t.findParentTopic();
//                        while (parent != null) {
//                            reported.add(parent);
//                            parent = parent.findParentTopic();
//                        }
//                    }
//                }
//            }
//
//        }
//    }

    @Override
    public void check(AdsClassCatalogDef classCatalog, IProblemHandler problemHandler) {
        AdsEntityObjectClassDef ownerClass = classCatalog.getOwnerClass();
        if (!ownerClass.isPolymorphic()) {
            error(classCatalog, problemHandler, "Class " + ownerClass.getName() + " is not polymorphic. Class catalogs are not allowed");
        }
        ArrayList<Id> notFoundTopics = new ArrayList<Id>();
        for (AdsClassCatalogDef.Topic topic : classCatalog.getTopicList()) {
            if (notFoundTopics.contains(topic.getParentTopicId())) {
                continue;
            }
            if (topic.getParentTopicId() != null) {
                AdsClassCatalogDef.Topic parent = topic.findParentTopic();
                if (parent == null) {
                    if (!notFoundTopics.contains(topic.getParentTopicId())) {
                        notFoundTopics.add(topic.getParentTopicId());
                    }
                } else {
                    AdsUtils.checkAccessibility(classCatalog, parent, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(classCatalog, parent, problemHandler);
                }
            }
        }

        //checkForTopicDuplication(classCatalog, problemHandler);

        List<AdsClassCatalogDef.ClassReference> refs = classCatalog.getClassRefList();
        for (AdsClassCatalogDef.ClassReference ref : refs) {
            if (notFoundTopics.contains(ref.getParentTopicId())) {
                continue;
            }

            if (ref.getParentTopicId() != null) {
                AdsClassCatalogDef.Topic parent = ref.findParentTopic();
                if (parent == null) {
                    if (!notFoundTopics.contains(ref.getParentTopicId())) {
                        notFoundTopics.add(ref.getParentTopicId());
                    }
                } else {
                    AdsUtils.checkAccessibility(classCatalog, parent, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(classCatalog, parent, problemHandler);
                }
            }
        }

        for (Id id : notFoundTopics) {
            error(classCatalog, problemHandler, "Topic #" + id.toString() + " is referenced as parent topic but not found");
        }

        if (classCatalog instanceof AdsClassCatalogDef.Overwrite) {
            AdsClassCatalogDef.Overwrite ovr = (AdsClassCatalogDef.Overwrite) classCatalog;
            for (AdsClassCatalogDef.ClassReference ref : ovr.getClassRefList()) {
                AdsClassDef clazz = ref.findReferencedClass();
                if (clazz == null) {
                    error(ref, problemHandler, "Referenced class #" + ref.getClassId() + " not found");
                } else {
                    if (ref.isTitleInherited() && clazz.getTitleId() == null) {
                        warning(ref, problemHandler, "Title is inherited from referenced class" + clazz.getQualifiedName() + ", but didn't defined there");
                    }

                    AdsUtils.checkAccessibility(classCatalog, clazz, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(classCatalog, clazz, problemHandler);
                }

            }
        } else {
            AdsClassCatalogDef.Virtual virt = (AdsClassCatalogDef.Virtual) classCatalog;
            AdsClassCatalogDef.ClassReference ref = virt.getClassReference();
            if (ref != null) {
                AdsClassDef clazz = ref.findReferencedClass();
                if (clazz != null) {
                    if (ref.isTitleInherited() && clazz.getTitleId() == null) {
                        warning(ref, problemHandler, "Title is inherited from class" + clazz.getQualifiedName() + ", but didn't defined there");
                    }
                }
            }
        }
    }
}

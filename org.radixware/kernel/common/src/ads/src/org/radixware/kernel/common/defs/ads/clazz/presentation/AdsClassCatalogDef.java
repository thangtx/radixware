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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Hierarchy;
import org.radixware.kernel.common.defs.ads.*;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassTitledMember;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MemberHierarchyIterator;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassCatalogsRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsClassCatalogWriter;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.ClassRef;
import org.radixware.schemas.radixdoc.Page;

/**
 * Defines class placement in class catalogs of basis entity
 *
 */
public abstract class AdsClassCatalogDef extends AdsPresentationsMember implements IJavaSource, IOverridable, IOverwritable,IRadixdocProvider {

    public static final class Factory {

        public static AdsClassCatalogDef newInstance() {
            return new Virtual();
        }

        public static AdsClassCatalogDef newNestedInstance() {
            return new Overwrite();
        }

        public static AdsClassCatalogDef loadFrom(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
            if (xDef.getIsOverwrite()) {
                return new Overwrite(xDef);
            } else {
                return new Virtual(xDef);
            }
        }

        public static AdsClassCatalogDef newOverride(AdsClassCatalogDef source) {
            return new Virtual(source);
        }

        public static AdsClassCatalogDef newOverwrite(AdsClassCatalogDef source) {
            return new Overwrite(source);
        }
    }

    @Override
    public void afterOverride() {
        getTopics().clear();
    }

    @Override
    public void afterOverwrite() {
        getTopics().clear();
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    public interface IClassCatalogItem {

        public double getOrder();

        public void setOrder(double order);

        /**
         * Returns id of parent topic for the topic
         */
        public Id getParentTopicId();

        /**
         * Updates parent topic id
         */
        public void setParentTopicId(Id id);

        /**
         * Looks for parent topic
         */
        public Topic findParentTopic();
    }

    private static class TopicLocator {

        AdsClassCatalogDef cc;

        TopicLocator(AdsClassCatalogDef cc) {
            this.cc = cc;
        }

        private Topic findTopic(Id id) {

            return findTopic(cc, id);
        }

        private Topic findTopic(AdsClassCatalogDef c, Id id) {
            HierarchyIterator<AdsClassCatalogDef> iter = c.getHierarchyIterator(HierarchyIterator.Mode.FIND_ALL);
            while (iter.hasNext()) {
                AdsClassCatalogDef next = iter.next().first();
                Topic t = next.getTopics().findById(id);
                if (t != null) {

                    return t;
                }

            }
            return null;
        }
    }

    /**
     * Class catalog topic - visual hierarchy modeling tool
     */
    public static class Topic extends AdsClassTitledMember implements IClassCatalogItem {

        public static final class Factory {

            public static Topic newInstance() {
                return new Topic();
            }
        }
        private Id parentTopicId;
        private double order;

        private Topic() {
            super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CREATION_CLASS_CATALOG_TOPIC), "Topic", null);
            this.parentTopicId = null;
        }

        private Topic(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic xDef) {
            super(xDef);
            this.parentTopicId = xDef.getParentTopicId();
            this.order = xDef.getOrder();
        }

        private void appendTo(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic xDef, ESaveMode saveMode) {
            super.appendTo(xDef, saveMode);
            if (parentTopicId != null) {
                xDef.setParentTopicId(parentTopicId);
            }
            xDef.setOrder(order);
        }

        @Override
        public Id getParentTopicId() {
            return parentTopicId;
        }

        public AdsClassCatalogDef getOwnerClassCatalog() {
            return (AdsClassCatalogDef) getOwnerDef();
        }

        @Override
        public void setParentTopicId(Id parentTopicId) {
            this.parentTopicId = parentTopicId;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public boolean isPublished() {
            return getOwnerClassCatalog().isPublished();
        }

        @Override
        public AdsEntityObjectClassDef getOwnerClass() {
            return (AdsEntityObjectClassDef) super.getOwnerClass();
        }

        @Override
        public ClipboardSupport<Topic> getClipboardSupport() {
            return new AdsClipboardSupport<Topic>(this) {

                @Override
                protected XmlObject copyToXml() {
                    org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic xDef = org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic.Factory.newInstance();
                    appendTo(xDef, ESaveMode.NORMAL);
                    return xDef;
                }

                @Override
                protected Topic loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic) {
                        return new Topic((org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic) xmlObject);
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }
            };
        }

        @Override
        public boolean isSuitableContainer(AdsDefinitions collection) {
            return collection instanceof Topics;
        }

        @Override
        public EDefType getDefinitionType() {
            return EDefType.CLASS_CATALOG_TOPIC;
        }

        @Override
        public String getTypeTitle() {
            return "Class Catolog Topic";
        }

        @Override
        public String getTypesTitle() {
            return "Class Catolog Topics";
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.CLASS_CATALOG;
        }

        @Override
        public double getOrder() {
            return order;
        }

        @Override
        public void setOrder(double order) {
            this.order = order;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public Topic findParentTopic() {
            return new TopicLocator(getOwnerClassCatalog()).findTopic(parentTopicId);
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.FREE;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            Topic p = findParentTopic();
            if (p != null) {
                list.add(p);
            }
        }

        @Override
        public String toString() {
            return getTitle(EIsoLanguage.ENGLISH) + " : " + getId();
        }
    }

    /**
     * Reference to current class. Defines order of the class in class catalog
     */
    public static abstract class ClassReference extends RadixObject implements IAdsClassMember, ILocalizedDef, IClassCatalogItem {

        public static final class Factory {

            public static ClassReference newInstance(AdsEntityObjectClassDef referencedClass) {
                return new OtherReference(referencedClass);
            }
        }
        private Id topicId;
        private Id titleId;
        private double order;

        @Override
        public AdsClassDef getOwnerClass() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsClassDef) {
                    return (AdsClassDef) owner;
                }
            }
            return null;
        }

        public abstract AdsClassDef findReferencedClass();

        private ClassReference(ClassRef xDef) {
            if (xDef != null) {
                this.titleId = xDef.getTitleId();
                this.topicId = xDef.getTopicId();
                this.order = xDef.getOrder();
            } else {
                this.titleId = null;
                this.topicId = null;
                this.order = 1D;
            }
        }

        @Override
        public double getOrder() {
            return order;
        }

        @Override
        public void setOrder(double order) {
            this.order = order;
            setEditState(EEditState.MODIFIED);
        }

        public abstract Id getClassId();

        /**
         * Returns parent topic id for the self reference
         */
        @Override
        public Id getParentTopicId() {
            return topicId;
        }

        public abstract AdsClassCatalogDef getOwnerClassCatalog();

        /**
         * Moves the refernce into a topic with given id
         */
        @Override
        public void setParentTopicId(Id topicId) {
            this.topicId = topicId;
            setEditState(EEditState.MODIFIED);
        }

        /**
         * Returns title of class refernce By default returns title of class
         */
        public String getTitle(EIsoLanguage language) {
            if (titleId == null) {
                return getOwnerClass().getTitle(language);
            } else {
                return getOwnerClass().getLocalizedStringValue(language, titleId);
            }
        }

        /**
         * Sets value to the title on requested langage
         *
         * @throws {@linkplain DefinitionError} if title is inherited from class
         */
        public void setTitle(EIsoLanguage language, String value) {
            if (this.titleId == null) {
                throw new RadixObjectError("Title is inherited from class.", this);
            }
            this.titleId = getOwnerClass().setLocalizedStringValue(language, titleId, value);
        }

        public boolean isTitleInherited() {
            return titleId == null;
        }

        public Id getTitleId() {
            return titleId;
        }

        public void setTitleId(Id id) {
            if (this.titleId == null) {
                throw new RadixObjectError("Title is inherited from class.", this);
            } else {
                titleId = id;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            ids.add(new MultilingualStringInfo(ClassReference.this) {

                @Override
                public Id getId() {
                    return titleId;
                }

                @Override
                public void updateId(Id newId) {
                    titleId = newId;
                }

                @Override
                public EAccess getAccess() {
                    return getOwnerClassCatalog().getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    return "Class Catalog Title";
                }

                @Override
                public boolean isPublished() {
                    return getOwnerClassCatalog().isPublished();
                }
            });
        }

        @Override
        public AdsMultilingualStringDef findLocalizedString(Id stringId) {
            return getOwnerClass().findLocalizedString(stringId);
        }

        /**
         * Makes title inherited according to parameter inherit Returns true if
         * real title inheritance is the same withgiven parameter
         */
        public boolean inheritTitle(boolean inherit) {
            if (inherit) {
                if (titleId == null) {
                    return true;
                } else {
                    titleId = null;
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            } else {
                if (titleId != null) {
                    return true;
                } else {
                    titleId = getOwnerClass().cloneLocalizedString(getOwnerClass().getTitleId());
                    if (titleId != null) {
                        setEditState(EEditState.MODIFIED);
                    }
                    return titleId != null;
                }
            }
        }

        protected void appendTo(ClassRef xDef) {
            if (titleId != null) {
                xDef.setTitleId(titleId);
            }
            if (topicId != null) {
                xDef.setTopicId(topicId);
            }
            xDef.setOrder(order);
        }

        @Override
        public Topic findParentTopic() {
            return new TopicLocator(getOwnerClassCatalog()).findTopic(topicId);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            AdsClassDef clazz = findReferencedClass();
            if (clazz != null) {
                list.add(clazz);
            }
            Topic t = findParentTopic();
            if (t != null) {
                list.add(t);
            }
        }
    }

    /**
     * This class reference. Used to register class in regular class catalog
     */
    private static class SelfReference extends ClassReference {

        public SelfReference(AdsClassCatalogDef owner, ClassRef xDef) {
            super(xDef);
            setContainer(owner);
        }

        @Override
        public AdsClassDef findReferencedClass() {
            return getOwnerClass();
        }

        @Override
        public Id getClassId() {
            return getOwnerClass().getId();
        }

        @Override
        public AdsClassCatalogDef getOwnerClassCatalog() {
            return (AdsClassCatalogDef) getContainer();
        }
    }

    /**
     * Reference to any class. Used to add class reference into overwrite class
     * catalog instance
     */
    private static class OtherReference extends ClassReference {

        private Id classId;

        protected OtherReference(ClassRef xDef) {
            super(xDef);
            if (xDef != null) {
                this.classId = xDef.getClassId();
            } else {
                this.classId = null;
            }
        }

        protected OtherReference(AdsEntityObjectClassDef refClass) {
            super(null);
            this.classId = refClass.getId();
        }

        @Override
        public AdsClassDef findReferencedClass() {
            AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerClass()).findById(classId).get();
            if (def instanceof AdsClassDef) {
                return (AdsClassDef) def;
            } else {
                return null;
            }
        }

        protected void setClassId(Id id) {
            this.classId = id;
            setEditState(EEditState.MODIFIED);
        }

        @Override
        protected void appendTo(ClassRef xDef) {
            super.appendTo(xDef);
            if (classId != null) {
                xDef.setClassId(classId);
            }
        }

        @Override
        public Id getClassId() {
            return classId;
        }

        @Override
        public AdsClassCatalogDef getOwnerClassCatalog() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsClassCatalogDef) {
                    return (AdsClassCatalogDef) owner;
                }
            }
            return null;
        }
    }

    public static class Overwrite extends AdsClassCatalogDef {

        @Override
        public boolean isVirtual() {
            return false;
        }

        private class RefList extends RadixObjects<ClassReference> {

            public RefList(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
                super(Overwrite.this);
                if (xDef != null) {
                    List<ClassRef> list = xDef.getClassRefList();
                    if (list != null && !list.isEmpty()) {
                        for (ClassRef r : list) {
                            this.add(new OtherReference(r));
                        }
                    }
                }
            }

            private void appendTo(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
                for (ClassReference ref : this) {
                    ref.appendTo(xDef.addNewClassRef());
                }
            }

            public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
                for (ClassReference ref : this) {
                    final ClassReference r = ref;
                    ids.add(new MultilingualStringInfo(r) {

                        @Override
                        public Id getId() {
                            return r.titleId;
                        }

                        @Override
                        public void updateId(Id newId) {
                            r.titleId = newId;
                        }

                        @Override
                        public EAccess getAccess() {
                            return getOwnerDef().getAccessMode();
                        }

                        @Override
                        public String getContextDescription() {
                            return "Class Reference Title";
                        }

                        @Override
                        public boolean isPublished() {
                            return getOwnerDef().isPublished();
                        }
                    });
                }
            }
        }
        private RefList refList;

        private Overwrite(AdsClassCatalogDef source) {
            super(source);
            refList = new RefList(null);
            isOverwrite = true;
        }

        private Overwrite() {
            super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CREATION_CLASS_CATALOG), "newClassCatalog");
            refList = new RefList(null);
            isOverwrite = true;
        }

        private Overwrite(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
            super(xDef);
            refList = new RefList(xDef);
            isOverwrite = true;
        }

        public RadixObjects<ClassReference> getClassReferences() {
            return refList;
        }

        @Override
        public void appendTo(ClassCatalog xDef, ESaveMode saveMode) {
            super.appendTo(xDef, saveMode);
            refList.appendTo(xDef);
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            refList.visit(visitor, provider);
        }

        @Override
        public List<ClassReference> getClassRefList() {
            return new ArrayList<>(refList.list());

        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            super.collectUsedMlStringIds(ids);
            refList.collectUsedMlStringIds(ids);
        }

        @Override
        public ClipboardSupport<? extends AdsClassCatalogDef> getClipboardSupport() {
            return new AdsClipboardSupport<Overwrite>(this) {

                @Override
                protected XmlObject copyToXml() {
                    org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef = org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Factory.newInstance();
                    appendTo(xDef, ESaveMode.NORMAL);
                    return xDef;
                }

                @Override
                protected Overwrite loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog) {
                        return new Overwrite((org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog) xmlObject);
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }
            };
        }
    }

    public static class Virtual extends AdsClassCatalogDef {

        private SelfReference ref = null;

        private Virtual() {
            super();
        }

        private Virtual(AdsClassCatalogDef source) {
            super(source);
        }

        @Override
        public boolean isVirtual() {
            return true;
        }

        @Override
        public void afterOverride() {
            super.afterOverride();
            ref = null;
        }

        private Virtual(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
            super(xDef);
            if (xDef != null) {
                List<ClassRef> list = xDef.getClassRefList();
                if (list != null && !list.isEmpty()) {
                    this.ref = new SelfReference(this, list.get(0));
                }
            }
        }

        public ClassReference getClassReference() {
            synchronized (this) {
                return ref;
            }
        }

        public boolean isClassReferenceDefined() {
            synchronized (this) {
                return ref != null;
            }
        }

        public ClassReference defineClassReference() {
            synchronized (this) {
                this.ref = new SelfReference(this, null);
                setEditState(EEditState.MODIFIED);
                return this.ref;
            }
        }

        public void undefClassReference() {
            synchronized (this) {
                setEditState(EEditState.MODIFIED);
                this.ref = null;
            }
        }

        @Override
        public void appendTo(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef, ESaveMode saveMode) {
            synchronized (this) {
                super.appendTo(xDef, saveMode);
                if (ref != null) {
                    ref.appendTo(xDef.addNewClassRef());
                }
            }
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            synchronized (this) {
                super.visitChildren(visitor, provider);
                if (ref != null) {
                    ref.visit(visitor, provider);
                }
            }
        }

        @Override
        public List<ClassReference> getClassRefList() {
            if (ref == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(getClassReference());
            }
        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            super.collectUsedMlStringIds(ids);
            if (ref != null) {
                ref.collectUsedMlStringIds(ids);
            }
        }

        @Override
        public ClipboardSupport<? extends AdsClassCatalogDef> getClipboardSupport() {
            return new AdsClipboardSupport<Virtual>(this) {

                @Override
                protected XmlObject copyToXml() {
                    org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef = org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Factory.newInstance();
                    appendTo(xDef, ESaveMode.NORMAL);
                    return xDef;
                }

                @Override
                protected Virtual loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog) {
                        return new Virtual((org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog) xmlObject);
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }

                @Override
                protected boolean isIdChangeRequired(RadixObject copyRoot) {
                    if (copyRoot instanceof AdsApplicationClassDef) {
                        AdsDefinition ovr = ((AdsClassCatalogDef.Virtual) radixObject).getHierarchy().findOverridden().get();
                        if (ovr != null) {
                            return false;
                        }
                    }
                    return super.isIdChangeRequired(copyRoot);
                }
            };
        }
    }

    private class Topics extends AdsDefinitions<Topic> {

        private Topics(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
            super(AdsClassCatalogDef.this);
            if (xDef != null) {
                List<org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic> list = xDef.getTopicList();
                if (list != null && !list.isEmpty()) {
                    for (org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog.Topic t : list) {
                        add(new Topic(t));
                    }
                }
            }
        }

        private void appendTo(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef, ESaveMode saveMode) {
            AdsClassCatalogDef ovr = null;
            if (AdsClassCatalogDef.this instanceof Virtual) {
                ovr = AdsClassCatalogDef.this.getHierarchy().findOverridden().get();
            }
            TopicLocator locator = null;
            if (ovr != null) {
                locator = new TopicLocator(ovr);
            }
            loop:
            for (Topic t : this) {
                AdsClassCatalogDef.Topic ovrt = null;
                if (locator != null) {
                    ovrt = locator.findTopic(t.getId());
                }
                if (ovrt != null) {
                    if (ovrt.getParentTopicId() == t.getParentTopicId() && ovrt.getOrder() == t.getOrder()) {

                        AdsMultilingualStringDef s1 = t.findLocalizedString(t.getTitleId());
                        AdsMultilingualStringDef s2 = ovrt.findLocalizedString(ovrt.getTitleId());


                        if (s1 != null && s2 != null) {
                            List<AdsMultilingualStringDef.StringStorage> ss1 = s1.getValues(EScope.LOCAL_AND_OVERWRITE);
                            boolean equal = true;

                            for (AdsMultilingualStringDef.StringStorage ss : ss1) {
                                String value = s2.getValue(ss.getLanguage());
                                if (!Utils.equals(value, ss.getValue())) {
                                    equal = false;
                                    break;
                                }
                            }
                            if (equal) {
                                continue loop;
                            }
                        }
                    }
                }
                t.appendTo(xDef.addNewTopic(), saveMode);
            }
        }
    }
    private final Topics topics;

    private AdsClassCatalogDef(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef) {
        super(xDef);
        this.topics = new Topics(xDef);
    }

    private AdsClassCatalogDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CREATION_CLASS_CATALOG), "ClassCatalog", null);
        this.topics = new Topics(null);
    }

    private AdsClassCatalogDef(AdsClassCatalogDef source) {
        this(source.getId(), source.getName());
    }

    private AdsClassCatalogDef(Id id, String name) {
        super(id, name, null);
        this.topics = new Topics(null);
    }

    public abstract boolean isVirtual();

    public Definitions<Topic> getTopics() {
        return topics;
    }

    public List<Topic> getTopicList() {
        return topics.list();
    }

    public abstract List<ClassReference> getClassRefList();

    @Override
    public AdsEntityObjectClassDef getOwnerClass() {
        return (AdsEntityObjectClassDef) super.getOwnerClass();
    }

    public void appendTo(org.radixware.schemas.adsdef.ClassDefinition.Presentations.ClassCatalogs.ClassCatalog xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        topics.appendTo(xDef, saveMode);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        topics.visit(visitor, provider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsClassCatalogDef> getHierarchy() {
        return new MemberHierarchy<AdsClassCatalogDef>(this) {

            @Override
            protected AdsClassCatalogDef findMember(ClassPresentations clazz, Id id) {
                if (clazz instanceof EntityObjectPresentations) {
                    return ((EntityObjectPresentations) clazz).getClassCatalogs().getLocal().findById(id);
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_CATALOG;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsClassCatalogWriter(this, AdsClassCatalogDef.this, purpose);
            }
        };
    }

    public Collection<AdsClassCatalogDef> getAll() {
        final Module curModule = getModule();
        final Segment curSegment = (curModule != null ? curModule.getSegment() : null);
        final Layer curLayer = (curSegment != null ? curSegment.getLayer() : null);
        final Branch branch = (curLayer != null ? curLayer.getBranch() : null);

        if (branch != null) {
            final ArrayList<AdsClassCatalogDef> all = new ArrayList<>();
            final Id entityId = getOwnerClass().getEntityId();

            // do not used branch.visit() for optimization
            for (Layer layer : branch.getLayers()) {
                final AdsSegment ads = (AdsSegment) layer.getAds();
                for (AdsModule module : ads.getModules()) {
                    for (AdsDefinition def : module.getDefinitions()) {
                        if (def instanceof AdsEntityObjectClassDef) {
                            final AdsEntityObjectClassDef eoc = (AdsEntityObjectClassDef) def;
                            for (AdsClassCatalogDef cc : eoc.getPresentations().getClassCatalogs().getLocal()) {
                                if (cc.getId() == getId()) {
                                    AdsEntityObjectClassDef clazz = cc.getOwnerClass();
                                    if (clazz != null && clazz.getEntityId() == entityId) {
                                        all.add(cc);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return all;
        } else {
            return Collections.singleton(this);
        }
    }

//    public Assembly assemble() {
//        Assembly a = new Assembly();
//        a.assemble(null, null);
//        return a;
//    }
//
//    public void check(IProblemHandler handler, Inheritance.ClassHierarchySupport support) {
//        Assembly a = new Assembly();
//        a.assemble(handler, support);
//    }
    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof ClassCatalogs;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CLASS_CATALOG;
    }

    private class HI extends MemberHierarchyIterator<AdsClassCatalogDef> {

        HI(EScope scope, HierarchyIterator.Mode mode) {
            super(AdsClassCatalogDef.this, scope, mode);
        }

        @Override
        public AdsClassCatalogDef findInClass(AdsClassDef clazz) {
            if (clazz instanceof AdsEntityObjectClassDef) {
                return ((AdsEntityObjectClassDef) clazz).getPresentations().getClassCatalogs().findById(AdsClassCatalogDef.this.getId(), EScope.LOCAL).get();
            } else {
                return null;
            }
        }
    }

    HierarchyIterator<AdsClassCatalogDef> getHierarchyIterator(HierarchyIterator.Mode mode) {
        return new HI(EScope.ALL, mode);
    }

    @Override
    public String getTypeTitle() {
        return getTypeName() +  " Class Catalog";
    }

    @Override
    public String getTypesTitle() {
        return getTypeName() + " Class Catalogs";
    }

    private String getTypeName() {
        return isVirtual() ? "Dynamic" : "Static";
    }
    @Override
      public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassCatalogDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ClassCatalogsRadixdoc(AdsClassCatalogDef.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }
}

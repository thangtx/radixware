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

package org.radixware.kernel.common.client.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParentRefExplorerItemDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadSelectorUserExplorerItemDef;
import org.radixware.kernel.common.client.meta.filters.RadContextFilter;
import org.radixware.kernel.common.client.meta.filters.RadFilterParamValue;
import org.radixware.kernel.common.client.models.items.Command;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArrRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyReference;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.enums.EEditPossibility;

import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.eas.Context;

public interface IContext {

    public abstract Restrictions getRestrictions();

    public abstract Model getHolderModel(); //модель, в которую входит данная в качестве child

    public static abstract class Abstract implements IContext {

        protected final IClientEnvironment environment;

        public Abstract(IClientEnvironment environment) {
            this.environment = environment;
        }

        public Model getOwnerModel() {
            return getHolderModel();
        }

        public EntityModel getHolderEntityModel() { //модель сущности, в которую входит данная в качестве child
            Model m = getHolderModel();
            while (m != null) {
                if (m instanceof EntityModel) {
                    return (EntityModel) m;
                }
                m = m.getContext().getHolderModel();
            }
            return null;
        }

        public abstract String getDescription();

        public String getSettingsGroupPrefix() {
            return "";
        }

        public IClientEnvironment getEnvironment() {
            return environment;
        }

        public abstract org.radixware.schemas.eas.Context toXml();
    }

//------------------------------------------ ENTITY --------------------------------------------
    public static abstract class Entity extends Abstract {

        public static class Factory {

            private Factory() {
            }

            public static Entity readFromSettings(final IClientEnvironment environment, final ClientSettings settings) throws XmlException {
                settings.beginGroup("context");
                try {
                    final String contextType = settings.readString("contextType");
                    if (contextType == null || contextType.isEmpty()) {
                        throw new WrongFormatError("Cannot read context type");
                    }
                    if (contextType.equals(ReferencedChoosenEntityEditing.class.getSimpleName())) {
                        final String contextXml = settings.readString("contextXml");
                        if (contextXml == null || contextXml.isEmpty()) {
                            throw new WrongFormatError("Cannot read context xml");
                        }
                        final org.radixware.schemas.eas.Context context =
                                org.radixware.schemas.eas.Context.Factory.parse(contextXml);
                        final List<Pid> parentPids = new ArrayList<>();
                        final int recordsCount = settings.beginReadArray("parentEntities");
                        try {
                            for (int i = 0; i < recordsCount; i++) {
                                settings.setArrayIndex(i);
                                parentPids.add(settings.readPid("parentEntityPid"));
                            }
                        } finally {
                            settings.endArray();
                        }

                        return new ReferencedChoosenEntityEditing(environment, context, settings.readBoolean("isReadonly"), parentPids);
                    } else if (contextType.equals(ChoosenEntityEditing.class.getSimpleName())) {
                        return new ChoosenEntityEditing(environment, settings);
                    }
                    throw new WrongFormatError("Unknown context type '" + contextType + "'");
                } finally {
                    settings.endGroup();
                }
            }
            
            public static Entity loadFromStr(final IClientEnvironment environment, final String contextAsStr) throws XmlException{
                final ArrStr fields = ArrStr.fromValAsStr(contextAsStr);
                final String contextType = fields.size()==0 ? null : fields.get(0);
                if (contextType==null || contextType.isEmpty()){
                    throw new WrongFormatError("Failed to get context type");
                }
                if (contextType.equals(ChoosenEntityEditing.class.getSimpleName())){
                    return new ChoosenEntityEditing(environment, fields);
                }else if (contextType.equals(ReferencedChoosenEntityEditing.class.getSimpleName())){
                    return new ReferencedChoosenEntityEditing(environment, fields);
                }else{
                    throw new WrongFormatError("Unknown context type '" + contextType + "'");
                }
            }
        }
        
        private IPresentationChangedHandler handler;

        public Entity(IClientEnvironment environment) {
            super(environment);
        }

        public IPresentationChangedHandler getPresentationChangedHandler() {
            return handler;
        }

        public void setPresentationChangedHandler(IPresentationChangedHandler presentationChangedHandler) {
            handler = presentationChangedHandler;
        }
        final protected List<Pid> parentEntities = new ArrayList<>();

        final protected void findParentEntity(final GroupModel group) {
            if (group != null && (group.getContext() instanceof ChildTableSelect)) {
                final EntityModel parentEntity = ((ChildTableSelect) group.getContext()).getHolderEntityModel();
                if (parentEntity != null) {
                    parentEntities.add(parentEntity.getPid());
                    if (parentEntity.getContext() instanceof SelectorRow) {
                        final SelectorRow entityContext = (SelectorRow) parentEntity.getContext();
                        findParentEntity(entityContext.parentGroupModel);
                    } else if (parentEntity.getContext() instanceof InSelectorEditing) {
                        final InSelectorEditing entityContext = (InSelectorEditing) parentEntity.getContext();
                        findParentEntity(entityContext.getGroupModel());
                    }
                }
            }
        }

        public boolean isChildEntity() {
            return false;
        }
        
        public String saveToString(){
            throw new AbstractMethodError("This method is not implemented for "+getClass().getSimpleName());
        }
    }

//	Бесконтекстный модальный редактор
    public static class ContextlessEditing extends Entity {
        
        private final IExplorerItemView explorerItemView;
        private final Model holderModel;

        public ContextlessEditing(final IClientEnvironment environment) {
            this(environment,null,null);
        }
        
        public ContextlessEditing(final IClientEnvironment environment, final IExplorerItemView explorerItemView) {
            this(environment,explorerItemView,null);
        }
        
        public ContextlessEditing(final IClientEnvironment environment, final IExplorerItemView explorerItemView, final Model holderModel) {
            super(environment);
            this.explorerItemView = explorerItemView;
            this.holderModel = holderModel;
        }        
        
        public IExplorerItemView getExplorerItemView(){
            return explorerItemView;
        }

        @Override
        public Restrictions getRestrictions() {
            return Restrictions.NO_RESTRICTIONS;//Ограничения определяются только презентацией редактора
        }

        @Override
        public Model getHolderModel() {
            return holderModel;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            return null;
        }

        @Override
        public String getDescription() {
            return "contextless editing";
        }
    }

    //	Модальный редактор для свойства ParentRef, Object, ArrParentRef, ArrObject
    public static class ReferencedEntityEditing extends ContextlessEditing {

        final public PropertyReference property;

        public ReferencedEntityEditing(final PropertyReference prop) {
            super(prop.getEnvironment());
            property = prop;
            if (!(property.getOwner() instanceof EntityModel) && !(property.getOwner() instanceof FormModel)) {
                throw new IllegalUsageError(String.format("Can`t create ReferencedEntityEditing context based on property of %s", prop.getOwner().getClass().getSimpleName()));
            }
        }

        @Override
        public String getDescription() {
            return "property reference: " + property.getDefinition().toString()
                    + "\nin definition: " + property.getOwner().getDefinition().toString();
        }

        @Override
        public Restrictions getRestrictions() {
            if (property instanceof PropertyObject) {
                if (property.isReadonly() || !property.hasOwnValue()) {
                    return Restrictions.Factory.newInstance(EnumSet.of(ERestriction.DELETE, ERestriction.UPDATE), null);
                } else {
                    return Restrictions.DELETE_RESTRICTION;
                }
            } else {
                if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                    final RadParentRefPropertyDef propertyDef = (RadParentRefPropertyDef) property.getDefinition();
                    return Restrictions.Factory.sum(Restrictions.DELETE_RESTRICTION, propertyDef.getParentEditorRestrictions());
                } else {
                    return Restrictions.DELETE_RESTRICTION;
                }
            }
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            if (getOwnerModel() instanceof EntityModel) {
                final EntityModel entity = (EntityModel) getOwnerModel();
                final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = result.addNewObjectProperty();
                final Id presentationId = entity.getEditorPresentationDef().getId();
                objectProperty.setEditorPresentationId(presentationId);
                objectProperty.setPropertyId(property.getId());
                Context xmlctx = ((IContext.Entity) entity.getContext()).toXml();
                if (xmlctx != null) {
                    objectProperty.setObjectContext(xmlctx);
                }
                objectProperty.setObject(entity.writeToXml(null, true));
                final org.radixware.schemas.eas.Object object = objectProperty.addNewObject();
                object.setClassId(entity.getClassId());
                if (entity.getPid() != null) {
                    object.setPID(entity.getPid().toString());
                }
            } else if (getOwnerModel() instanceof FormModel) {
                final FormModel form = (FormModel) getOwnerModel();
                final org.radixware.schemas.eas.Context.FormProperty formProperty = result.addNewFormProperty();
                formProperty.setPropertyId(property.getId());
                formProperty.setForm(form.toXml());
            } else if (getOwnerModel() instanceof ReportParamDialogModel){
                final ReportParamDialogModel report = (ReportParamDialogModel)getOwnerModel();
                final org.radixware.schemas.eas.Context.ReportProperty reportProperty = result.addNewReportProperty();
                reportProperty.setPropertyId(property.getId());
                reportProperty.setReport(report.toXml());
            }
            final GroupModel groupModel;
            if (property instanceof PropertyRef && ((PropertyRef)property).canOpenGroupModel()){
                groupModel = ((PropertyRef)property).openGroupModel();
            }else{
                groupModel = null;
            }
            if (groupModel!=null && !groupModel.getActiveProperties().isEmpty()){
                groupModel.writePropertiesToXml(result.addNewGroupProperties());
            }
            return result;
        }

        @Override
        public Model getOwnerModel() {
            return property.getOwner();
        }
    }

//	Редактор в дереве для свойства ParentRef, Object, ArrParentRef, ArrObject
    public static class ReferencedChoosenEntityEditing extends ContextlessEditing {
        /*При подъеме версии модели сущности может потребоваться переоткрыть
        модель и восстановить контекст => в этом контексте нельзя хранить
        ссылку на свойство или дефиницию свойства. Также в новой версии может
        измениться набор ограничений => ограничения вычисляются
         */

        final public Id propertyId;
        final public boolean isParentRef, isPropertyReadonly;
        final public Id ownerClassId;
        final public Pid ownerPid;
        final public String description;
        final private org.radixware.schemas.eas.Context contextAsXml;
        //final org.radixware.schemas.eas.Context contextAsXml;

        public ReferencedChoosenEntityEditing(PropertyReference prop) {
            super(prop.getEnvironment());
            propertyId = prop.getDefinition().getId();
            isPropertyReadonly = 
                prop instanceof PropertyObject ? prop.isReadonly() || !prop.hasOwnValue() : prop.isReadonly();
            isParentRef = (prop instanceof PropertyRef);

            contextAsXml = org.radixware.schemas.eas.Context.Factory.newInstance();
            if (prop.getOwner() instanceof EntityModel) {
                final EntityModel entity = (EntityModel) prop.getOwner();                
                if (entity.isNew()) {
                    throw new IllegalUsageError("This context cannot be used for not existing entity");
                }
                final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = contextAsXml.addNewObjectProperty();
                final Id presentationId = entity.getEditorPresentationDef().getId();
                objectProperty.setEditorPresentationId(presentationId);
                final IContext.Entity entityContext = (IContext.Entity) entity.getContext();
                if (entityContext.toXml() != null) {
                    objectProperty.setObjectContext(entityContext.toXml());
                }
                objectProperty.setPropertyId(propertyId);
                final org.radixware.schemas.eas.Object object = objectProperty.addNewObject();
                ownerClassId = entity.getClassId();
                ownerPid = entity.getPid();
                object.setClassId(ownerClassId);
                object.setPID(ownerPid.toString());

                if (entityContext instanceof SelectorRow) {
                    findParentEntity(((SelectorRow) entityContext).parentGroupModel);
                } else if (entityContext instanceof InSelectorEditing) {
                    findParentEntity(((InSelectorEditing) entityContext).getGroupModel());
                } else if ((entityContext instanceof ChoosenEntityEditing) || (entityContext instanceof ReferencedChoosenEntityEditing)) {
                    parentEntities.addAll(entityContext.parentEntities);
                }
            } else {
                throw new IllegalUsageError(String.format("Can`t create ReferencedChoosenEntityEditing context based on property of %s", prop.getOwner().getClass().getSimpleName()));
            }
            
            if (prop instanceof PropertyRef && ((PropertyRef)prop).canOpenGroupModel()){
                final GroupModel group =  ((PropertyRef)prop).openGroupModel();
                if (!group.getActiveProperties().isEmpty()){
                    group.writePropertiesToXml(contextAsXml.addNewGroupProperties());
                }
            }

            description = "property reference: " + prop.getDefinition().toString()
                    + "\nin definition: " + prop.getOwner().getDefinition().toString();
        }

        protected ReferencedChoosenEntityEditing(final IClientEnvironment environment, final org.radixware.schemas.eas.Context xml, final boolean isReadonly, final List<Pid> parentPids) {
            super(environment);
            contextAsXml = xml;
            final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = xml.getObjectProperty();
            if (objectProperty == null) {
                throw new WrongFormatError("Cannot parse context");
            }
            propertyId = objectProperty.getPropertyId();
            if (propertyId == null) {
                throw new WrongFormatError("Cannot parse context: propertyId is not defined");
            }
            final org.radixware.schemas.eas.Object object = objectProperty.getObject();
            if (object == null) {
                throw new WrongFormatError("Cannot parse context: object is not defined");
            }
            ownerClassId = object.getClassId();
            if (ownerClassId == null) {
                throw new WrongFormatError("Cannot parse context: object classId is not defined");
            }            
            final String ownerPidAsStr = object.getPID();
            if (ownerPidAsStr == null || ownerPidAsStr.isEmpty()) {
                throw new WrongFormatError("Cannot parse context: object Pid is not defined");
            }
            final RadClassPresentationDef ownerClassDef = environment.getDefManager().getClassPresentationDef(ownerClassId);
            final Id presentationId = objectProperty.getEditorPresentationId();
            final RadEditorPresentationDef presentationDef = environment.getDefManager().getEditorPresentationDef(presentationId);
            ownerPid = new Pid(ownerClassDef.getTableId(), ownerPidAsStr);
            final RadPropertyDef propertyDef = ownerClassDef.getPropertyDefById(propertyId);
            final RadPropertyPresentationAttributes propertyPresentationAttrs = 
                    presentationDef.getPropertyAttributesByPropId(propertyId, ownerClassDef);            
            isPropertyReadonly = isReadonly
                    || propertyPresentationAttrs.getEditPossibility() == EEditPossibility.NEVER
                    || propertyPresentationAttrs.getEditPossibility() == EEditPossibility.ON_CREATE
                    || presentationDef.getRestrictions().getIsUpdateRestricted();
            isParentRef = propertyDef.getType() == EValType.PARENT_REF;

            description = "property reference: " + propertyDef.toString()
                    + "\nin definition: " + propertyDef.toString();
            parentEntities.addAll(parentPids);
        }

        protected ReferencedChoosenEntityEditing(final IClientEnvironment environment, final ArrStr fields) throws XmlException{
            super(environment);
            if (!getClass().getSimpleName().equals(fields.get(0))){
                throw new WrongFormatError("Failed to restore context from string");
            }
            final String contextAsXmlText = fields.get(1);
            if (contextAsXmlText == null || contextAsXmlText.isEmpty()) {
                throw new WrongFormatError("Cannot read context: group context is not defined");
            }
            contextAsXml = org.radixware.schemas.eas.Context.Factory.parse(contextAsXmlText);
            final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = contextAsXml.getObjectProperty();
            if (objectProperty == null) {
                throw new WrongFormatError("Cannot parse context");
            }
            propertyId = objectProperty.getPropertyId();
            if (propertyId == null) {
                throw new WrongFormatError("Cannot parse context: propertyId is not defined");
            }
            final org.radixware.schemas.eas.Object object = objectProperty.getObject();
            if (object == null) {
                throw new WrongFormatError("Cannot parse context: object is not defined");
            }
            ownerClassId = object.getClassId();
            if (ownerClassId == null) {
                throw new WrongFormatError("Cannot parse context: object classId is not defined");
            }            
            final String ownerPidAsStr = object.getPID();
            if (ownerPidAsStr == null || ownerPidAsStr.isEmpty()) {
                throw new WrongFormatError("Cannot parse context: object Pid is not defined");
            }
            final RadClassPresentationDef ownerClassDef = environment.getDefManager().getClassPresentationDef(ownerClassId);
            final Id presentationId = objectProperty.getEditorPresentationId();
            final RadEditorPresentationDef presentationDef = environment.getDefManager().getEditorPresentationDef(presentationId);
            ownerPid = new Pid(ownerClassDef.getTableId(), ownerPidAsStr);
            final RadPropertyDef propertyDef = ownerClassDef.getPropertyDefById(propertyId);
            final RadPropertyPresentationAttributes propertyPresentationAttrs = 
                    presentationDef.getPropertyAttributesByPropId(propertyId, ownerClassDef);            
            isPropertyReadonly = Boolean.getBoolean(fields.get(2))
                    || propertyPresentationAttrs.getEditPossibility() == EEditPossibility.NEVER
                    || propertyPresentationAttrs.getEditPossibility() == EEditPossibility.ON_CREATE
                    || presentationDef.getRestrictions().getIsUpdateRestricted();
            isParentRef = propertyDef.getType() == EValType.PARENT_REF;

            description = "property reference: " + propertyDef.toString()
                    + "\nin definition: " + propertyDef.toString();
            final ArrStr arrParentEntitiesPid = ArrStr.fromValAsStr(fields.get(3));
            for (String pidAsStr: arrParentEntitiesPid){
                parentEntities.add(Pid.fromStr(pidAsStr));
            }
        }

        private RadParentRefPropertyDef getPropertyDef() {
            final RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(ownerClassId);
            return (RadParentRefPropertyDef) classDef.getPropertyDefById(propertyId);
        }

        public boolean ownerEntityIsChildFor(final Pid parentEntityPid) {
            return parentEntities.contains(parentEntityPid);
        }

        @Override
        public Restrictions getRestrictions() {            
            if (!isParentRef) {
                //Только для PropertyObject
                if (isPropertyReadonly) {
                    return Restrictions.Factory.newInstance(EnumSet.of(ERestriction.DELETE, ERestriction.UPDATE), null);
                } else {
                    return Restrictions.DELETE_RESTRICTION;
                }
            }
            final RadParentRefPropertyDef propertyDef = getPropertyDef();
            return Restrictions.Factory.sum(Restrictions.DELETE_RESTRICTION, propertyDef.getParentEditorRestrictions());
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
           return contextAsXml;
        }

        public void writeToSettings(final ClientSettings settings) {
            settings.beginGroup("context");
            try {
                settings.writeString("contextType", getClass().getSimpleName());
                settings.writeString("contextXml", contextAsXml.xmlText());
                settings.writeBoolean("isReadonly", isPropertyReadonly);
                settings.beginWriteArray("parentEntities");
                try {
                    for (int i = 0; i < parentEntities.size(); i++) {
                        settings.setArrayIndex(i);
                        settings.writePid("parentEntityPid", parentEntities.get(i));
                    }
                } finally {
                    settings.endArray();
                }
            } finally {
                settings.endGroup();
            }
        }
        
        @Override
        public String saveToString(){
            final ArrStr fields = new ArrStr();
            fields.add(getClass().getSimpleName());
            fields.add(contextAsXml.xmlText());
            fields.add(String.valueOf(isPropertyReadonly));
            final ArrStr arrParentEntitiesPid = new ArrStr();
            for (Pid pid: parentEntities){
                arrParentEntitiesPid.add(pid.toStr());
            }
            fields.add(arrParentEntitiesPid.toString());
            return fields.toString();            
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

//	Встроенный в редактор или вытащенный в дерево редактор родительского объекта
    public static class ParentEntityEditing extends Entity {

        final public Model holder;
        final public EntityModel childEntity;
        final public RadParentRefExplorerItemDef explorerItemDef;

        public ParentEntityEditing(EntityModel childEntity, Model holder, RadParentRefExplorerItemDef explorerItemDef) {
            super(childEntity.getEnvironment());
            if (childEntity.isNew()) {
                throw new IllegalUsageError("This context cannot be used for not existing child entity");
            }
            if (!childEntity.isExists()) {
                throw new IllegalUsageError("This context cannot be used for not existing child entity", new ObjectNotFoundError(childEntity));
            }
            this.childEntity = childEntity;
            this.holder = holder;
            this.explorerItemDef = explorerItemDef;
        }

        @Override
        public Restrictions getRestrictions() {
            return Restrictions.Factory.sum(explorerItemDef.getRestrictions(), EnumSet.of(ERestriction.DELETE, ERestriction.CREATE));
        }

        @Override
        public Model getHolderModel() {
            return holder;
        }

        @Override
        public EntityModel getHolderEntityModel() {
            return childEntity;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.TreePath.EdPresExplrItem explorerItem =
                    result.addNewTreePath().addNewEdPresExplrItem();
            final RadEditorPresentationDef presentation = childEntity.getEditorPresentationDef();
            explorerItem.setClassId(childEntity.getClassId());
            explorerItem.setEditorPresentationId(presentation.getId());
            explorerItem.setExplorerItemId(explorerItemDef.getId());
            explorerItem.setParentPid(childEntity.getPid().toString());
            return result;
        }

        @Override
        public String getDescription() {
            return "parent entity by explorer item: #" + explorerItemDef.getId()
                    + "for " + childEntity.getTitle();
        }

        @Override
        public String getSettingsGroupPrefix() {
            return explorerItemDef.getId() + "_";
        }
    }

//	Редактор вытащенного в дерево из селектора объекта
    public static class ChoosenEntityEditing extends Entity {
        /*При подъеме версии модели сущности может потребоваться переоткрыть
        модель и восстановить контекст => в этом контексте нельзя хранить
        ссылку на модель группы
         */

        final public Id selectorPresentationId;
        final private org.radixware.schemas.eas.Context contextAsXml;        
        final private boolean isChildEntity;
        final private String description;
        final private String settingsPrefix;
        //final private Restrictions restrictions;

        //редактор вытащен из селектора
        public ChoosenEntityEditing(final EntityModel rowModel) {
            super(rowModel.getEnvironment());
            final IContext.SelectorRow rowContext = (IContext.SelectorRow) rowModel.getContext();
            final GroupModel groupModel = rowContext.parentGroupModel;

            //restrictions = rowModel.getContext().getRestrictions();
            findParentEntity(groupModel);
            selectorPresentationId = groupModel.getSelectorPresentationDef().getId();
            final IContext.Group groupModelContext = (IContext.Group)groupModel.getContext();            
            contextAsXml = groupModelContext.toXml();//NOPMD call of overridable method of constructed instance
            if (groupModelContext instanceof ContextlessSelect==false && !groupModel.getActiveProperties().isEmpty()){
                groupModel.writePropertiesToXml(contextAsXml.addNewGroupProperties());
            }
            isChildEntity = groupModelContext instanceof ChildTableSelect;
            description = "choosen entity from " + groupModel.getTitle();
            settingsPrefix = groupModelContext.getSettingsGroupPrefix();//NOPMD call of overridable method of constructed instance
        }

        protected ChoosenEntityEditing(final IClientEnvironment environment, final ClientSettings settings) throws XmlException {
            super(environment);
            selectorPresentationId = settings.readId("selectorPresentationId");
            if (selectorPresentationId == null) {
                throw new WrongFormatError("Cannot read context: selector presentation id is not defined");
            }
            final String contextAsXmlText = settings.readString("groupContext");
            if (contextAsXmlText == null || contextAsXmlText.isEmpty()) {
                throw new WrongFormatError("Cannot read context: group context is not defined");
            }
            contextAsXml = org.radixware.schemas.eas.Context.Factory.parse(contextAsXmlText);
            settingsPrefix = settings.readString("settingsPrefix", null);
            if (settingsPrefix == null || settingsPrefix.isEmpty()) {
                throw new WrongFormatError("Cannot read context: settings prefix is not defined");
            }
            isChildEntity = settings.readBoolean("isChildEntity", false);
            description = settings.readString("describtion", "choosen entity");
            final int recordsCount = settings.beginReadArray("parentEntities");
            try {
                for (int i = 0; i < recordsCount; i++) {
                    settings.setArrayIndex(i);
                    parentEntities.add(settings.readPid("parentEntityPid"));
                }
            } finally {
                settings.endArray();
            }
        }
        
        protected ChoosenEntityEditing(final IClientEnvironment environment, final ArrStr fields) throws XmlException{
            super(environment);
            if (!getClass().getSimpleName().equals(fields.get(0))){
                throw new WrongFormatError("Failed to restore context from string");
            }
            selectorPresentationId = Id.Factory.loadFrom(fields.get(1));            
            if (selectorPresentationId == null) {
                throw new WrongFormatError("Cannot read context: selector presentation id is not defined");
            }
            final String contextAsXmlText = fields.get(2);
            if (contextAsXmlText == null || contextAsXmlText.isEmpty()) {
                throw new WrongFormatError("Cannot read context: group context is not defined");
            }
            contextAsXml = org.radixware.schemas.eas.Context.Factory.parse(contextAsXmlText);
            settingsPrefix = fields.get(3);
            if (settingsPrefix == null || settingsPrefix.isEmpty()) {
                throw new WrongFormatError("Cannot read context: settings prefix is not defined");
            }
            isChildEntity = Boolean.getBoolean(fields.get(4));
            description = fields.get(5);
            final ArrStr arrParentEntityPid = ArrStr.fromValAsStr(fields.get(6));
            for (String pidAsStr: arrParentEntityPid){
                parentEntities.add(Pid.fromStr(pidAsStr));
            }
        }

        @Override
        public Restrictions getRestrictions() {
            //return Restrictions.NO_RESTRICTIONS;
            //RADIX-1462
            final Restrictions selectorRestrictions = environment.getDefManager().getSelectorPresentationDef(selectorPresentationId).getRestrictions();
            final EnumSet<ERestriction> restrictions = EnumSet.noneOf(ERestriction.class);
            if (selectorRestrictions.getIsUpdateRestricted()) {
                restrictions.add(ERestriction.UPDATE);
            }
            if (selectorRestrictions.getIsDeleteRestricted()) {
                restrictions.add(ERestriction.DELETE);
            }
            return Restrictions.Factory.newInstance(restrictions, null);
        }

        @Override
        public Model getHolderModel() {
            return null;
            //return groupModel;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            return contextAsXml;
        }

        @Override
        public boolean isChildEntity() {
            return isChildEntity;
        }

        public boolean isChildEntity(final Pid parentEntityPid) {
            return parentEntities.contains(parentEntityPid);
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getSettingsGroupPrefix() {
            return settingsPrefix;
        }

        public void writeToSettings(final ClientSettings settings) {
            settings.beginGroup("context");
            try {
                settings.writeString("contextType", getClass().getSimpleName());
                settings.writeId("selectorPresentationId", selectorPresentationId);
                settings.writeString("groupContext", contextAsXml.xmlText());
                settings.writeString("settingsPrefix", settingsPrefix);
                settings.writeBoolean("isChildEntity", isChildEntity);
                settings.writeString("describtion", description);
                settings.beginWriteArray("parentEntities");
                try {
                    for (int i = 0; i < parentEntities.size(); i++) {
                        settings.setArrayIndex(i);
                        settings.writePid("parentEntityPid", parentEntities.get(i));
                    }
                } finally {
                    settings.endArray();
                }
            } finally {
                settings.endGroup();
            }
        }
        
        @Override
        public String saveToString(){
            final  ArrStr fields = new ArrStr();
            fields.add(getClass().getSimpleName());
            fields.add(selectorPresentationId.toString());
            fields.add(contextAsXml.xmlText());
            fields.add(settingsPrefix);
            fields.add(String.valueOf(isChildEntity));
            fields.add(description);
            final ArrStr arrParentEntitiesPid = new ArrStr();
            for (Pid pid: parentEntities){
                arrParentEntitiesPid.add(pid.toStr());
            }
            fields.add(arrParentEntitiesPid.toString());
            return fields.toString();
        }
        
    }

//	Редактор текущего объекта - модальный или встроенный в селектор
    public static class InSelectorEditing extends Entity {

        final public EntityModel rowModel;
        final private IContext.SelectorRow selectorRowCtx;

        public InSelectorEditing(final EntityModel rowModel) {
            super(rowModel.getEnvironment());
            this.rowModel = rowModel;
            selectorRowCtx = null;
        }

        public InSelectorEditing(final GroupModel group) {
            super(group.getEnvironment());
            //RADIX-3734
            rowModel = null;
            selectorRowCtx = new IContext.SelectorRow(group);
        }

        public IContext.SelectorRow getSelectorRowContext() {
            return rowModel == null ? selectorRowCtx : (SelectorRow) rowModel.getContext();
        }

        public GroupModel getGroupModel() {
            return getSelectorRowContext().parentGroupModel;
        }

        @Override
        public Restrictions getRestrictions() {
            return getSelectorRowContext().getRestrictions(); //RADIX-1462
        }

        @Override
        public Model getHolderModel() {
            return getGroupModel();
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context resultXml = getSelectorRowContext().toXml();
            if (!getGroupModel().getActiveProperties().isEmpty()){
                getGroupModel().writePropertiesToXml(resultXml.addNewGroupProperties());
            }
            return resultXml;
        }

        @Override
        public boolean isChildEntity() {
            return getSelectorRowContext().isChildEntity();
        }

        @Override
        public String getDescription() {
            if (rowModel == null) {
                return "editing in selector";
            } else {
                return "editing \"" + rowModel.getTitle() + "\" in selector";
            }
        }

        @Override
        public String getSettingsGroupPrefix() {
            return ((IContext.Group) getGroupModel().getContext()).getSettingsGroupPrefix();
        }
    }

//	Строка селектора
    public static class SelectorRow extends Entity {

        final public GroupModel parentGroupModel;
        final private Restrictions restrictions;

        public SelectorRow(GroupModel parentGroupModel) {
            super(parentGroupModel.getEnvironment());
            this.parentGroupModel = parentGroupModel;
            //RADIX-2232
            //There no reason to ask for restrictions directly form parentGroupModel here
            //because it can change in user-code.
            //Final restrictions for this context will be calculated at
            //org.radixware.kernel.explorer.types.EntityRestrictions#isUpdateRestricted() /
            //org.radixware.kernel.explorer.types.EntityRestrictions#isDeleteRestricted()
            //Radix-3641
            //It is necessary to use property-ref selector restrictions when selector was opened
            //with ParentSelect, FormSelect or ReportSelect context.
            //Property-ref editor restrictions applyes only for editor opened directly from property-ref 
            //and may by inaccessible if editor presentation was not defined in property-ref.
            final IReferenceEditing referenceEditing;
            final IContext.Group ownGroupContext = parentGroupModel.getGroupContext();
            if (ownGroupContext instanceof IReferenceEditing) {
                referenceEditing = (IReferenceEditing)ownGroupContext;
            }else if (ownGroupContext.getRootGroupContext() instanceof IReferenceEditing){
                referenceEditing = (IReferenceEditing)ownGroupContext.getRootGroupContext();
            }else{
                referenceEditing = null;
            }
            if (referenceEditing==null){
                restrictions = Restrictions.NO_RESTRICTIONS;                
            } else {
                restrictions = referenceEditing.getParentSelectorRestrictions();
            }
        }

        @Override
        public Restrictions getRestrictions() {
            return restrictions;
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Model getOwnerModel() {
            return parentGroupModel;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context contextXml = parentGroupModel.getContext().toXml();                
            if (!parentGroupModel.getActiveProperties().isEmpty()){
                parentGroupModel.writePropertiesToXml(contextXml.addNewGroupProperties());
            }
            return contextXml;
        }

        @Override
        public boolean isChildEntity() {
            return parentGroupModel.getContext() instanceof ChildTableSelect;
        }

        @Override
        public String getDescription() {
            return "entity of \"" + parentGroupModel.getTitle() + "\" group";
        }

        @Override
        public String getSettingsGroupPrefix() {
            return ((IContext.Group) parentGroupModel.getContext()).getSettingsGroupPrefix();
        }
    }

//	Редактирование заготовки при создании объекта без контекста
    public static class ContextlessCreating extends Entity {
        
        private final Model holderModel;

        public ContextlessCreating(final IClientEnvironment environment) {
            super(environment);
            holderModel = null;
        }
        
        public ContextlessCreating(final Model holderModel){
            super(holderModel.getEnvironment());
            this.holderModel = holderModel;
        }

        @Override
        public Restrictions getRestrictions() {
            return Restrictions.Factory.newInstance(EnumSet.of(ERestriction.DELETE, ERestriction.CREATE), null);
        }

        @Override
        public Model getHolderModel() {
            return holderModel;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            return null;
        }

        @Override
        public String getDescription() {
            return "contextless entity creating";
        }
    }

//	Редактирование заготовки при создании объекта для свойства Object, ArrObject
    public static class ObjectPropCreating extends ContextlessCreating {

        final public EntityModel propOwner;
        final public Id propId;

        public ObjectPropCreating(EntityModel propOwner, Id propId) {
            super(propOwner.getEnvironment());
            if (propId == null) {
                throw new NullPointerException();
            }
            this.propOwner = propOwner;
            this.propId = propId;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = result.addNewObjectProperty();
            final org.radixware.schemas.eas.Context ownerContextAsXml = ((IContext.Entity) propOwner.getContext()).toXml();
            final RadEditorPresentationDef presentation = propOwner.getEditorPresentationDef();
            objectProperty.setEditorPresentationId(presentation.getId());
            objectProperty.setPropertyId(propId);
            if (ownerContextAsXml != null) {
                objectProperty.setObjectContext(ownerContextAsXml);
            }
            objectProperty.setObject(propOwner.writeToXml(null, true));
            return result;
        }

        @Override
        public Model getOwnerModel() {
            return propOwner;
        }

        @Override
        public String getDescription() {
            return "creating new object as value of property #" + propId + "\nin \"" + propOwner.getTitle() + "\"";
        }
    }

//	Редактирование заготовки при создании объекта в селекторе
    public static class InSelectorCreating extends ContextlessCreating {

        final public GroupModel group;

        public InSelectorCreating(GroupModel group) {
            super(group.getEnvironment());
            this.group = group;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context xmlContext = group.getContext().toXml();
            if (!group.getActiveProperties().isEmpty()){
                group.writePropertiesToXml(xmlContext.addNewGroupProperties());
            }
            return xmlContext;
        }

        @Override
        public String getDescription() {
            return "creating new entity in selector \"" + group.getTitle() + "\"";
        }

        @Override
        public String getSettingsGroupPrefix() {
            return ((IContext.Group) group.getContext()).getSettingsGroupPrefix();
        }

        @Override
        public Model getOwnerModel() {
            return group;
        }
    }

//	------------------------------------------ GROUP --------------------------------------------
    public static abstract class Group extends Abstract {
        
        private IContext.Group rootGroupContext;//root group model context in selector tree
        private Restrictions restrictions;       

        public Group(final IClientEnvironment environment) {
            super(environment);
            
        }
        
        protected Group (final Group copy){
            this(copy.getEnvironment());
            rootGroupContext = copy.rootGroupContext;
            restrictions = copy.restrictions;
        }
        
        public void setRootGroupContext(final IContext.Group rootGroupContext){
            this.rootGroupContext = rootGroupContext;
        }
        
        public IContext.Group getRootGroupContext(){
            return rootGroupContext;
        }
        
        @Override
        public final Restrictions getRestrictions() {
            if (restrictions==null){
                restrictions = calculateRestrictions();
            }
            if (rootGroupContext==null || restrictions==null){
                return restrictions;
            }else {
                return Restrictions.Factory.sum(restrictions, rootGroupContext.getRestrictions());
            }            
        }
        
        protected abstract Restrictions calculateRestrictions();
        
        public abstract IContext.Group copy();
        
        public abstract RadSelectorPresentationDef getSelectorPresentationDef();
    }
    
    interface IReferenceEditing{
        Restrictions getParentSelectorRestrictions();
    }

    //Бесконтекстный селектор
    public static class ContextlessSelect extends Group {

        private final Model holder;
        private final Id classId;
        private final Id selectorPresentationId;
        public final PropertyRef property;

        public ContextlessSelect(final IClientEnvironment environment,
                                 final RadSelectorPresentationDef selectorPresentation) {
            super(environment);
            holder = null;
            property = null;
            classId = selectorPresentation.getOwnerClassId();
            selectorPresentationId = selectorPresentation.getId();
        }

        public ContextlessSelect(final Model holderModel,
                                 final RadSelectorPresentationDef selectorPresentation) {
            super(holderModel.getEnvironment());
            holder = holderModel;
            classId = selectorPresentation.getOwnerClassId();
            selectorPresentationId = selectorPresentation.getId();            
            property = null;
        }

        public ContextlessSelect(final PropertyRef propRef,
                                 final RadSelectorPresentationDef selectorPresentation) {
            super(propRef.getEnvironment());
            holder = null;
            classId = selectorPresentation.getOwnerClassId();
            selectorPresentationId = selectorPresentation.getId();            
            property = propRef;
        }
        
        protected ContextlessSelect(final ContextlessSelect copy){
            super(copy);
            this.holder = copy.holder;
            this.classId = copy.classId;
            this.selectorPresentationId = copy.selectorPresentationId;
            this.property = copy.property;
        }

        @Override
        public EntityModel getHolderEntityModel() {
            return null;
        }

        @Override
        public Model getHolderModel() {
            return property==null ? holder : property.getOwner();
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context contextXml = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.Selector selectorContext = contextXml.addNewSelector();
            selectorContext.setClassId(classId);
            selectorContext.setPresentationId(selectorPresentationId);            
            return contextXml;
        }
        
        @Override
        protected Restrictions calculateRestrictions(){
            if (property != null) {//DBP-1676
                if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                    final RadParentRefPropertyDef pd = (RadParentRefPropertyDef) property.getDefinition();
                    return Restrictions.Factory.sum(pd.getParentSelectorRestrictions(), Restrictions.EDITOR_RESTRICTION);
                } else {
                    return Restrictions.CONTEXTLESS_SELECT;
                }
            } else {
                return Restrictions.CONTEXTLESS_SELECT;
            }            
        }

        @Override
        public String getDescription() {
            String message = "contextless selector";
            if (property != null) {
                message += " by property " + property.getDefinition().toString();//NOPMD
            }
            return message;
        }

        @Override
        public ContextlessSelect copy() {
            return new ContextlessSelect(this);
        }

        @Override
        public RadSelectorPresentationDef getSelectorPresentationDef() {
            return environment.getDefManager().getSelectorPresentationDef(selectorPresentationId);
        }
    }

    //Независимая таблица в дереве или встроенная в редактор
    public static class TableSelect extends Group {

        private final Model holderModel;
        private final EntityModel parentEntity;
        final public RadExplorerItemDef explorerItemDef;

        public TableSelect(IClientEnvironment environment, EntityModel parent, Model holder, RadExplorerItemDef explorerItemDef) {
            super(environment);
            parentEntity = parent;
            holderModel = holder;
            this.explorerItemDef = explorerItemDef;
        }
        
        protected TableSelect(final TableSelect copy){
            super(copy);
            parentEntity = copy.parentEntity;
            holderModel = copy.holderModel;
            explorerItemDef = copy.explorerItemDef;
        }

        @Override
        public EntityModel getHolderEntityModel() {
            return parentEntity;
        }

        @Override
        protected Restrictions calculateRestrictions() {
            return explorerItemDef.getRestrictions();
        }

        @Override
        public Model getHolderModel() {
            return holderModel;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {            
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.TreePath treePath = result.addNewTreePath();
            final Id explorerItemId;
            if (explorerItemDef instanceof RadSelectorUserExplorerItemDef){
                explorerItemId = ((RadSelectorUserExplorerItemDef)explorerItemDef).getTargetExplorerItem().getId();
            }else{
                explorerItemId = explorerItemDef.getId();
            }

            if (parentEntity != null) {
                final org.radixware.schemas.eas.Context.TreePath.EdPresExplrItem explorerItem = treePath.addNewEdPresExplrItem();
                explorerItem.setClassId(parentEntity.getClassId());
                final RadEditorPresentationDef presentation = parentEntity.getEditorPresentationDef();
                explorerItem.setEditorPresentationId(presentation.getId());
                explorerItem.setExplorerItemId(explorerItemId);
                if (parentEntity.getPid() != null) {
                    explorerItem.setParentPid(parentEntity.getPid().toString());
                }
            } else {
                Model paragraphModel = holderModel;
                while (paragraphModel.getContext().getHolderModel() != null) {
                    paragraphModel = paragraphModel.getContext().getHolderModel();
                }
                final org.radixware.schemas.eas.Context.TreePath.RootItem rootItem = treePath.addNewRootItem();
                rootItem.setRootId(paragraphModel.getDefinition().getOwnerClassId());
                rootItem.setExplorerItemId(explorerItemId);
            }
            
            final RadContextFilter contextFilter = getFilter();
            if (contextFilter!=null){
                writeContextFilterToXml(contextFilter, treePath.addNewFilter());
            }
            return result;
        }
        
        private static void writeContextFilterToXml(final RadContextFilter filter, 
                                                    final org.radixware.schemas.eas.Filter filterXml){
            filterXml.setAdditionalCondition(filter.getCondition());
            final Collection<RadFilterParamValue> paramValues = filter.getParameterValues();
            if (!paramValues.isEmpty()){
                final org.radixware.schemas.eas.Filter.Parameters parametersXml = filterXml.addNewParameters();
                for (RadFilterParamValue value: paramValues){
                    writeFilterParameterValueToXml(value, parametersXml.addNewItem());
                }
            }
        }
        
        private static void writeFilterParameterValueToXml(final RadFilterParamValue paramValue,
                                                           final org.radixware.schemas.eas.Filter.Parameters.Item paramValueXml){
            paramValueXml.setId(paramValue.getParamId());
            switch(paramValue.getType()){
                case INT:{
                    final Long value;
                    if (paramValue.getValue()==null){
                        value=null;
                    }else{
                        value = (Long)paramValue.getValue().toObject(EValType.INT);
                    }
                    if (value==null){
                        paramValueXml.setNilInt();
                    }else{
                        paramValueXml.setInt(value);
                    }
                    break;
                }
                case NUM:{
                    final BigDecimal value;
                    if (paramValue.getValue()==null){
                        value=null;
                    }else{
                        value = (BigDecimal)paramValue.getValue().toObject(EValType.NUM);
                    }
                    if (value==null){
                        paramValueXml.setNilNum();
                    }else{
                        paramValueXml.setNum(value);
                    }
                    break;
                }
                case DATE_TIME:{
                    final Timestamp value;
                    if (paramValue.getValue()==null){
                        value=null;
                    }else{
                        value = (Timestamp)paramValue.getValue().toObject(EValType.DATE_TIME);
                    }                    
                    paramValueXml.setDateTime(value);
                    break;
                }
                case PARENT_REF:{
                    final String refAsStr;
                    if (paramValue.getValue()==null){
                        refAsStr=null;
                    }else{
                        refAsStr = (String)paramValue.getValue().toObject(EValType.STR);
                    }                 
                    final Reference ref = refAsStr==null ? null : Reference.fromValAsStr(refAsStr);
                    final Pid value = ref==null ? null : ref.getPid();
                    if (value==null){
                        paramValueXml.setNilPID();
                    }else{
                        final org.radixware.schemas.eas.FilterParamValue.PID pidXml = paramValueXml.addNewPID();
                        pidXml.setEntityId(value.getTableId());
                        pidXml.setStringValue(XmlUtils.getSafeXmlString(value.toString()));
                    }
                    break;
                }
                default:{
                    final String value = paramValue.getValue()==null ? null : paramValue.getValue().toString();
                    if (value==null){
                        paramValueXml.setNilStr();
                    }else{
                        paramValueXml.setStr(XmlUtils.getSafeXmlString(value));
                    }
                    break;
                }
            }
        }
        
        public RadContextFilter getFilter(){
            if (explorerItemDef instanceof RadSelectorExplorerItemDef){
                return ((RadSelectorExplorerItemDef)explorerItemDef).getContextFilter();
            }else{
                return null;
            }
        }
        
        public RadSortingDef getSorting(){
            if (explorerItemDef instanceof RadSelectorExplorerItemDef){
                return ((RadSelectorExplorerItemDef)explorerItemDef).getInitialSorting();
            }
            return null;
        }

        @Override
        public String getDescription() {
            final StringBuilder description = new StringBuilder(32);
            description.append("table explroer item #");
            description.append(explorerItemDef.getId().toString());            
            if (parentEntity != null) {
                description.append("in parent entity: ");
                description.append(parentEntity.getDefinition().getTitle());
                description.append(" \"");
                description.append(parentEntity.getTitle());
                description.append('\"');                
            }
            return description.toString();
        }

        @Override
        public String getSettingsGroupPrefix() {
            return explorerItemDef.getId() + "_";
        }

        @Override
        public TableSelect copy() {
            return new TableSelect(this);
        }

        @Override
        public RadSelectorPresentationDef getSelectorPresentationDef() {
            return (RadSelectorPresentationDef)explorerItemDef.getModelDefinition();
        }                
    }

// Дочерняя таблица	в дереве или встроенная в редактор
    public static class ChildTableSelect extends TableSelect {

        public ChildTableSelect(IClientEnvironment environment, EntityModel parentEntity, Model holderModel, RadChildRefExplorerItemDef explorerItemDef) {
            super(environment, parentEntity, holderModel, explorerItemDef);
        }
        
        ChildTableSelect(IClientEnvironment environment, EntityModel parentEntity, Model holderModel, RadSelectorUserExplorerItemDef explorerItemDef) {
            super(environment, parentEntity, holderModel, explorerItemDef);
        } 
        
        protected ChildTableSelect(final ChildTableSelect copy){
            super(copy);
        }

        @Override
        public String getDescription() {
            final StringBuilder description = new StringBuilder(32);
            description.append("table explroer item #");
            description.append(explorerItemDef.getId().toString());            
            if (getHolderEntityModel() != null) {
                description.append("in parent entity: ");
                description.append(getHolderEntityModel().getDefinition().getTitle());
                description.append(" \"");
                description.append(getHolderEntityModel().getTitle());
                description.append('\"');                
            }
            return description.toString();
        }
        
        @Override
        public ChildTableSelect copy() {
            return new ChildTableSelect(this);
        }        
    }

// Список для выбора родителя
    public static class ParentSelect extends Group implements IReferenceEditing{

        final public EntityModel childEntity;
        final public Property property;

        public ParentSelect(EntityModel childEntity, PropertyArrRef childProperty) {
            super(childEntity.getEnvironment());
            this.childEntity = childEntity;
            this.property = childProperty;
        }

        public ParentSelect(EntityModel childEntity, PropertyRef childProperty) {
            super(childEntity.getEnvironment());
            this.childEntity = childEntity;
            this.property = childProperty;
        }
        
        protected ParentSelect(final ParentSelect copy){
            super(copy);
            childEntity = copy.childEntity;
            property = copy.property;
        }

        @Override
        protected Restrictions calculateRestrictions() {
            if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                final RadParentRefPropertyDef pd = (RadParentRefPropertyDef) property.getDefinition();
                return Restrictions.Factory.sum(pd.getParentSelectorRestrictions(), Restrictions.EDITOR_RESTRICTION);
            } else {
                return Restrictions.EDITOR_RESTRICTION;
            }
        }
        
        @Override
        public Restrictions getParentSelectorRestrictions() {
            if (property.getDefinition() instanceof RadParentRefPropertyDef) {                
                return ((RadParentRefPropertyDef) property.getDefinition()).getParentSelectorRestrictions();
            } else {
                return Restrictions.NO_RESTRICTIONS;
            }
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Model getOwnerModel() {
            return childEntity;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.ObjectProperty objectProperty = result.addNewObjectProperty();
            final Id presentationId = childEntity.getEditorPresentationDef().getId();
            objectProperty.setPropertyId(property.getId());
            objectProperty.setEditorPresentationId(presentationId);
            Context xmlctx = ((IContext.Entity) childEntity.getContext()).toXml();
            if (xmlctx != null) {
                objectProperty.setObjectContext(xmlctx);
            }
            objectProperty.setObject(childEntity.writeToXml(null, true));
            return result;
        }

        @Override
        public String getDescription() {
            return "select value of property " + property.getDefinition().toString()
                    + "\nin entity \"" + childEntity.getTitle() + "\"";
        }

        @Override
        public ParentSelect copy() {
            return new ParentSelect(this);
        }                

        @Override
        public RadSelectorPresentationDef getSelectorPresentationDef() {
            if (property instanceof PropertyRef){
                return ((PropertyRef)property).getParentSelectorPresentation();
            }else if (property instanceof PropertyArrRef){
                return ((PropertyArrRef)property).getDefinition().getParentSelectorPresentation();
            }else{
                return null;
            }
        }
    }

    //Селектор открытый для свойства формы
    public static class FormSelect extends Group implements IReferenceEditing{

        final public FormModel ownerForm;
        final public Property property;

        public FormSelect(FormModel ownerForm, PropertyArrRef childProperty) {
            super(ownerForm.getEnvironment());
            this.ownerForm = ownerForm;
            this.property = childProperty;
        }

        public FormSelect(FormModel ownerForm, PropertyRef childProperty) {
            super(ownerForm.getEnvironment());
            this.ownerForm = ownerForm;
            this.property = childProperty;
        }
        
        protected FormSelect(final FormSelect copy){
            super(copy);
            ownerForm = copy.ownerForm;
            property = copy.property;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.FormProperty formProperty = result.addNewFormProperty();
            formProperty.setPropertyId(property.getId());
            formProperty.setForm(ownerForm.toXml());
            return result;
        }

        @Override
        protected Restrictions calculateRestrictions() {
            final RadParentRefPropertyDef pd;
            pd = (RadParentRefPropertyDef) property.getDefinition();
            return Restrictions.Factory.sum(pd.getParentSelectorRestrictions(), Restrictions.EDITOR_RESTRICTION);
        }

        @Override
        public Restrictions getParentSelectorRestrictions() {            
            final RadParentRefPropertyDef pd = (RadParentRefPropertyDef) property.getDefinition();
            return pd.getParentSelectorRestrictions();
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Model getOwnerModel() {
            return ownerForm;
        }

        @Override
        public String getDescription() {
            return "select value of property " + property.getDefinition().toString()
                    + "\nin form \"" + ownerForm.getTitle() + "\"";
        }

        @Override
        public FormSelect copy() {
            return new FormSelect(this);
        }        
        
        @Override
        public RadSelectorPresentationDef getSelectorPresentationDef() {
            if (property instanceof PropertyRef){
                return ((PropertyRef)property).getParentSelectorPresentation();
            }else if (property instanceof PropertyArrRef){
                return ((PropertyArrRef)property).getDefinition().getParentSelectorPresentation();
            }else{
                return null;
            }
        }        
    }
    
    //Селектор открытый для свойства отчета
    public static class ReportSelect extends Group implements IReferenceEditing{

        final public ReportParamDialogModel ownerReport;
        final public Property property;

        public ReportSelect(ReportParamDialogModel ownerReport, PropertyArrRef childProperty) {
            super(ownerReport.getEnvironment());
            this.ownerReport = ownerReport;
            this.property = childProperty;
        }

        public ReportSelect(ReportParamDialogModel ownerReport, PropertyRef childProperty) {
            super(ownerReport.getEnvironment());
            this.ownerReport = ownerReport;
            this.property = childProperty;
        }
        
        protected ReportSelect(final ReportSelect copy){
            super(copy);
            ownerReport = copy.ownerReport;
            property = copy.property;
        }

        @Override
        public org.radixware.schemas.eas.Context toXml() {
            final org.radixware.schemas.eas.Context result = org.radixware.schemas.eas.Context.Factory.newInstance();
            final org.radixware.schemas.eas.Context.ReportProperty reportProperty = result.addNewReportProperty();
            reportProperty.setPropertyId(property.getId());
            reportProperty.setReport(ownerReport.toXml());
            return result;
        }

        @Override
        protected Restrictions calculateRestrictions() {
            final RadParentRefPropertyDef pd;
            pd = (RadParentRefPropertyDef) property.getDefinition();
            return Restrictions.Factory.sum(pd.getParentSelectorRestrictions(), Restrictions.EDITOR_RESTRICTION);
        }
        
        @Override
        public Restrictions getParentSelectorRestrictions() {            
            final RadParentRefPropertyDef pd = (RadParentRefPropertyDef) property.getDefinition();
            return pd.getParentSelectorRestrictions();
        }        

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Model getOwnerModel() {
            return ownerReport;
        }

        @Override
        public String getDescription() {
            return "select value of property " + property.getDefinition().toString()
                    + "\nin report \"" + ownerReport.getTitle() + "\"";
        }

        @Override
        public ReportSelect copy() {
            return new ReportSelect(this);
        }
        
        @Override
        public RadSelectorPresentationDef getSelectorPresentationDef() {
            if (property instanceof PropertyRef){
                return ((PropertyRef)property).getParentSelectorPresentation();
            }else if (property instanceof PropertyArrRef){
                return ((PropertyArrRef)property).getDefinition().getParentSelectorPresentation();
            }else{
                return null;
            }
        }        
    }

//--------------------------------- PARAGRAPH ---------------------------------------------
    //Параграф в дереве или встроенный
    public static class Paragraph extends Abstract {

        final public Model holderModel;
        final public RadExplorerItemDef explorerItemDef;

        public Paragraph(IClientEnvironment environment, Model holderModel, RadExplorerItemDef explorerItemDef) {
            super(environment);
            this.holderModel = holderModel;
            this.explorerItemDef = explorerItemDef;
        }

        @Override
        public Model getHolderModel() {
            return holderModel;
        }

        @Override
        public Restrictions getRestrictions() {
            return null;
        }

        @Override
        public String getDescription() {
            return "paragraph #" + explorerItemDef.getId();
        }

        @Override
        public Context toXml() {
            return null;
        }
    }

//	--------------------------------- FORM ---------------------------------------------
    public static class Form extends Abstract {

        /**
         * Команда, которая запустила первую форму в цепочке
         */
        public final Command startCommand;
        /*
         *Если startCommand - команда свойства - идентификатор этого свойства
         */
        public final Id commandPropertyId;
        /**
         * презентационная модель, из которой запущена форма.
         */
        public final Model parentModel;
        /**
         * предыдущая форма в цепочке
         */
        public final FormModel previousForm;

        public Form(Command command, final Id propertyId) {
            super(command.getOwner().getEnvironment());
            startCommand = command;
            commandPropertyId = propertyId;
            parentModel = startCommand.getOwner();
            previousForm = null;
        }

        public Form(FormModel form) {
            super(form.getEnvironment());
            IContext.Form formContext = (IContext.Form) form.getContext();
            startCommand = formContext.startCommand;
            commandPropertyId = formContext.commandPropertyId;
            parentModel = formContext.parentModel;
            previousForm = form;
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Model getOwnerModel() {
            return parentModel;
        }

        @Override
        public Restrictions getRestrictions() {
            return Restrictions.NO_RESTRICTIONS;
        }

        @Override
        public String getDescription() {
            return "form started by command #" + startCommand.getId()
                    + "\n in model \"" + parentModel.getTitle() + "\""
                    + previousForm != null ? "\n previous form: #" + previousForm.getDefinition().getId() : "";
        }

        @Override
        public Context toXml() {
            return null;
        }
    }
//	--------------------------------- PROPERTY EDITOR ---------------------------------------------

    public static class PropEditorContext extends Abstract {

        final public Property property;
        final public int arrIdx;

        public PropEditorContext(Property property, int idx) {
            super(property.getOwner().getEnvironment());
            this.property = property;
            arrIdx = idx;
        }

        @Override
        public Model getHolderModel() {
            return property.getOwner();
        }

        @Override
        public Restrictions getRestrictions() {
            return ((EntityModel) property.getOwner()).getRestrictions();
        }

        @Override
        public String getDescription() {
            return "editing of property #" + property.getId()
                    + "\n in \"" + property.getOwner().getTitle() + "\"";
        }

        @Override
        public Context toXml() {
            return null;
        }
    }

//	--------------------------------- DIALOG ---------------------------------------------
    public static class Dialog extends Abstract {

        public Dialog(IClientEnvironment environment) {
            super(environment);
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Restrictions getRestrictions() {
            return null;
        }

        @Override
        public String getDescription() {
            return "dialog";
        }

        @Override
        public Context toXml() {
            return null;
        }
    }

//	--------------------------------- FILTER ---------------------------------------------
    public static class Filter extends Abstract {

        public final GroupModel ownerGroup;

        public Filter(GroupModel group) {
            super(group.getEnvironment());
            ownerGroup = group;
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Restrictions getRestrictions() {
            return null;
        }

        @Override
        public String getDescription() {
            return "filter in presentation "
                    + ownerGroup.getSelectorPresentationDef().toString();
        }

        @Override
        public Model getOwnerModel() {
            return ownerGroup;
        }

        @Override
        public Context toXml() {
            return null;
        }
    }

//	--------------------------------- REPORT ---------------------------------------------
    public static class Report extends Abstract {

        public final Model owner;//модель из которой запустился отчет
        public final Model reportPubModel;

        public Report(Model owner,Model reportPubModel) {
            super(owner.getEnvironment());
            this.owner = owner;
            this.reportPubModel=reportPubModel;
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Restrictions getRestrictions() {
            return null;
        }

        @Override
        public String getDescription() {
            return "filter in presentation " + owner.getDefinition().toString();
        }

        @Override
        public Model getOwnerModel() {
            return owner;
        }
        
        public Model getReportPubModel() {
            return reportPubModel;
        }

        @Override
        public Context toXml() {
            return null;
        }
    }

    public static class CustomWidget extends Abstract {

        public CustomWidget(IClientEnvironment environment) {
            super(environment);
        }

        @Override
        public Model getHolderModel() {
            return null;
        }

        @Override
        public Restrictions getRestrictions() {
            return null;
        }

        @Override
        public String getDescription() {
            return "custom widget";
        }

        @Override
        public Context toXml() {
            return null;
        }
    }
}

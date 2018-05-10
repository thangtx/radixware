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
package org.radixware.kernel.server.arte.services.eas;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.enums.EAccessAreaType;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDrcServerResource;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EPaginationMethod;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.sqml.tags.DbFuncCallTag;
import org.radixware.kernel.common.sqml.tags.ParameterAbstractTag;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.arte.services.eas.ExplorerAccessService.SessionOptions;
import org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.services.Service.RequestTraceBuffer;
import org.radixware.kernel.server.exceptions.EntityObjectNotExistsError;
import org.radixware.kernel.server.meta.clazzes.IRadPropWriteAccessor;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;
import org.radixware.kernel.server.meta.clazzes.RadParentPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadUserPropDef;
import org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadCommandDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef;
import org.radixware.kernel.server.meta.presentations.RadExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadSortingDef;
import org.radixware.kernel.common.trace.TraceItem;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.server.SrvRunParams;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Cache;
import org.radixware.kernel.server.exceptions.PrimaryKeyModificationError;
import org.radixware.kernel.server.exceptions.WrongPidFormatError;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.presentations.IRadFilter;
import org.radixware.kernel.server.meta.presentations.RadConditionDef;
import org.radixware.kernel.server.meta.presentations.RadParagraphExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadParentRefExplorerItemDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorExplorerItemDef;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.EntityGroup;
import org.radixware.kernel.server.types.EntitySelection;
import org.radixware.kernel.server.types.FormHandler;
import org.radixware.kernel.server.types.IRadClassInstance;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.types.PresentationEntityAdapter;
import org.radixware.kernel.server.types.PropValHandler;
import org.radixware.kernel.server.types.PropValHandlersByIdMap;
import org.radixware.kernel.server.types.Report;
import org.radixware.kernel.server.types.Restrictions;
import org.radixware.kernel.server.types.presctx.EntityPropertyPresentationContext;
import org.radixware.kernel.server.types.presctx.FormPropertyPresentationContext;
import org.radixware.kernel.server.types.presctx.UnknownPresentationContext;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.kernel.server.types.presctx.ReportPropertyPresentationContext;
import org.radixware.schemas.eas.ActionTypeEnum;
import org.radixware.schemas.eas.Actions;
import org.radixware.schemas.eas.ClassRequest;
import org.radixware.schemas.eas.Context.TreePath;
import org.radixware.schemas.eas.ContextRequest;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.EntityRequest;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.eas.ExplorerItemList;
import org.radixware.schemas.eas.Filter;
import org.radixware.schemas.eas.FilterParamTypeEnum;
import org.radixware.schemas.eas.Form;
import org.radixware.schemas.eas.GroupRequest;
import org.radixware.schemas.eas.NextDialogRequest;
import org.radixware.schemas.eas.NextDialogRequest.MessageBox;
import org.radixware.schemas.eas.ObjectOrGroupRequest;
import org.radixware.schemas.eas.ObjectReference;
import org.radixware.schemas.eas.Presentation;
import org.radixware.schemas.eas.Property;
import org.radixware.schemas.eas.Property.InheritableValue;
import org.radixware.schemas.eas.Property.InheritableValue.Path;
import org.radixware.schemas.eas.PropertyList;
import org.radixware.schemas.eas.Request;
import org.radixware.schemas.eas.Response;
import org.radixware.schemas.eas.SelectRq;
import org.radixware.schemas.eas.Sorting;
import org.radixware.schemas.eas.TraceLevelEnum;
import org.radixware.schemas.eas.TransactionEnum;

abstract class SessionRequest extends EasRequest {

    //private static final String CONTEXT_DEF_NAME    = "Context";
    private static final String PRES_DEF_NAME = "Presentation";
    //private static final String ROOT_DEF_NAME       = "Root";
    private static final int MAX_TRACE_ITEMS_TO_WRITE = 5000;    
    
    protected SessionRequest() {}
    
    SessionRequest(final ExplorerAccessService presenter) {
        super(presenter);
    }	// Service DB queries
    private ExplorerAccessService.SessionOptions sessionOptions = null;

    
    public void setSessionOptions(final SessionOptions sessionOptions) {
        this.sessionOptions = sessionOptions;
    }

    protected String getParentTitle(final IRadClassInstance ptOwner, final Id ptPropId, final Entity parent, RadEntityTitleFormatDef titleFormat) {
        String title;
        if (parent != null) {
            if (titleFormat != null) {
                title = parent.calcTitle(titleFormat);
            } else {
                title = parent.calcTitle();
            }
        } else {
            if (titleFormat != null) {
                title = titleFormat.getNullTitle(getArte());
            } else {
                title = null;
            }
        }
        return ptOwner.onCalcParentTitle(ptPropId, parent, title);
    }

    @Override
    protected String getUsrDbTraceProfile() {
        if (sessionOptions == null) {
            throw new IllegalUsageError("Request is not connected to session");
        }
        return sessionOptions.getUsrDbTraceProfile();
    }

    protected boolean getUsrActionsIsTraced() {
        if (sessionOptions == null) {
            throw new IllegalUsageError("Request is not connected to session");
        }
        return sessionOptions.getUsrActionsIsTraced();
    }

    private String getScp() {
        if (sessionOptions == null) {
            throw new IllegalUsageError("Request is not connected to session");
        }
        return sessionOptions.getScp();
    }

    private void assertSubquriesAllowed(final Sqml c) throws ServiceProcessClientFault {
        //RADIX-4446: FIXME uncomment following and check if subqueries allowed by ACS
        /*
         if (c == null || c.getItems().isEmpty())
         return;
         final StringBuilder tmpStr = new StringBuilder();
         for (Item item : c.getItems()){
         if (item instanceof Text){
         tmpStr.append(((Text)item).getText());
         } else {
         tmpStr.append(' ');
         }
         }
         if (tmpStr.length() > 0 && tmpStr.toString().toLowerCase().contains("select")) {
         throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_SUBQUERIES_ARE_FORBIDDEN, null);
         }
         */
    }

    Context getContext(final org.radixware.schemas.eas.Context xml) throws InterruptedException {
        if (xml == null) {
            return null;
        }
        if (xml.isSetFormProperty()) {
            return new FormPropContext(this, xml.getFormProperty(), xml.getGroupProperties());
        } else if (xml.isSetReportProperty()) {
            return new ReportPropContext(this, xml.getReportProperty(), xml.getGroupProperties());
        } else if (xml.isSetObjectProperty()) {
            return new ObjPropContext(this, xml.getObjectProperty(), xml.getGroupProperties());
        } else if (xml.isSetTreePath()) {
            final TreePath xTreePath = xml.getTreePath();
            if (xTreePath.isSetRootItem()) {
                return new RootItemContext(this, xTreePath.getRootItem(), xml.getGroupProperties());
            } else if (xTreePath.isSetEdPresExplrItem()) {
                return new EdPresExplrItemContext(this, xTreePath.getEdPresExplrItem(), xml.getGroupProperties());
            }
        } else if (xml.isSetSelector()) {
            return new SelectorContext(this, xml.getSelector(), xml.getGroupProperties());
        }
        return null;
    }

    protected void postProcess(final XmlObject request, final Response response){
        if (response!=null){
            final byte[] challenge
                    = presenter.getChallenge(request, getArte().getEasSessionId().longValue(), true, sessionOptions.getCurrentChallenge());
            response.setChallenge(challenge);
            if (sessionOptions.getSecurityToken() != null) {
                response.setSecurityToken(sessionOptions.getSecurityToken());
            }
            if (getScp() != null) {
                response.setScpName(getScp());
            }
        }
        super.postProcess(request, response);
    }

    protected final static class PresentationOptions {
        // Public fields

        private final SessionRequest rq;
        public final Context context;
        public final RadSelectorPresentationDef selectorPresentation;
        public final RadEditorPresentationDef editorPresentation;

        // Constructor
        public PresentationOptions(
                final SessionRequest rq,
                final Context context,
                final RadSelectorPresentationDef selectorPresentation,
                final RadEditorPresentationDef editorPresentation) {
            this.rq = rq;
            this.context = context;
            this.selectorPresentation = selectorPresentation;
            this.editorPresentation = editorPresentation;
        }

        protected final void assertEdPresIsAccessible(final Entity object)
                throws ServiceProcessClientFault {
            if (editorPresentation != null && getEdPresTotalRestrictions(object).getIsAccessRestricted()) {
                throwEdPresIsNotAccessible();
            }
        }
        
        protected final void assertEdPresIsAccessible(final PresentationEntityAdapter adapter){
            if (editorPresentation!=null && adapter.getAdditionalRestrictions(editorPresentation).getIsAccessRestricted()){
                throwEdPresIsNotAccessible();
            }
        }
        
        private void throwEdPresIsNotAccessible(){
            throw EasFaults.newDefinitionAccessViolationFault(rq.getArte(),
                    Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_ED_PRES,
                    "\"" + editorPresentation.getName() + "\"(#" + editorPresentation.getId() + ")",
                    EDefType.EDITOR_PRESENTATION,
                    new Id[]{editorPresentation.getId()});            
        }

        private Restrictions getEdPresTotalRestrictions(final Entity object) {
            return editorPresentation.getTotalRestrictions(object.getCurUserApplicableRoleIds());
        }
        
        static final private Restrictions CONTEXTLESS_SEL_RESTR = Restrictions.Factory.newInstance(ERestriction.UPDATE.getValue().longValue() | ERestriction.CREATE.getValue().longValue() | ERestriction.DELETE.getValue().longValue() | ERestriction.DELETE_ALL.getValue().longValue() | ERestriction.NOT_READ_ONLY_COMMANDS.getValue().longValue(), null, null, null);

        protected final Restrictions getSelRestrictions(final EntityGroup entGrp) throws ServiceProcessClientFault {
            final List<Id> roleIds = getContextCurUserApplicableRoleIds();
            final Restrictions contextRestr;
            if (entGrp.isContextDefined()) {
                contextRestr = context == null ? Restrictions.ZERO : context.getRestrictions();
            } else {
                contextRestr = CONTEXTLESS_SEL_RESTR;
            }
            final Restrictions predefinedRestrictions = 
                Restrictions.Factory.sum(contextRestr, selectorPresentation.getTotalRestrictions(roleIds));
            final Restrictions additionalRestrictions = entGrp.getAdditionalRestrictions(selectorPresentation, roleIds);
            if (additionalRestrictions==Restrictions.ZERO){
                return predefinedRestrictions;
            }else{
                return Restrictions.Factory.sum(predefinedRestrictions, additionalRestrictions);
            }
        }

        protected List<Id> getContextCurUserApplicableRoleIds() {
            return rq.getContextCurUserApplicableRoleIds(context);
        }

        protected final void assertSelPresIsAccessibile(final EntityGroup entGrp) throws ServiceProcessClientFault {
            if (getSelRestrictions(entGrp).getIsAccessRestricted()) {
                throw EasFaults.newDefinitionAccessViolationFault(rq.getArte(),
                        Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_SEL_PRES,
                        "\"" + selectorPresentation.getName() + "\"(#" + selectorPresentation.getId() + ")",
                        EDefType.SELECTOR_PRESENTATION,
                        new Id[]{selectorPresentation.getId()});
            }
        }

        protected Id getClassCatalogId() {
            final RadClassPresentationDef.ClassCatalog classCatalog = context != null ? context.getClassCatalog() : null;
            if (classCatalog != null) {
                return classCatalog.getId();
            }
            if (selectorPresentation != null) {
                return selectorPresentation.getClassCatalogId();
            }
            return null;
        }

        protected void initEntGrp(final EntityGroup<? extends Entity> entGrp, 
                                               final Sqml addCond, 
                                               final Sqml addFrom, 
                                               final Map<Id, Object> fltParamValsById, 
                                               final List<RadSortingDef.Item> orderBy, 
                                               final Sqml hint) {
            if (context != null) {
                context.writeGroupProperties(entGrp);
            }
            entGrp.set(Context.asEntGroupContext(context), 
                             selectorPresentation, 
                             addCond, 
                             addFrom,
                             fltParamValsById, 
                             orderBy, 
                             hint);
        }
        
        protected void initEntGrp(final EntityGroup<? extends Entity> entGrp, final EntityGroup.Parameters params) {
            if (context != null) {
                context.writeGroupProperties(entGrp);
            }
            entGrp.set(params);
        }        
    }

    // Subclasses
    protected static final class Group { // group scope and options structure

        final IRadFilter filter;
        final RadSortingDef sorting;
        final EntityGroup entityGroup;

        protected Group(final EntityGroup entityGroup, final IRadFilter filter, final RadSortingDef sorting) {
            this.entityGroup = entityGroup;
            this.filter = filter;
            this.sorting = sorting;            
        }

        public String getFilterInfo() {
            return filter == null ? "null" : filter.getInfo();
        }
    }
    
    private static final class GroupFilter implements EntityGroup.EntityFilter{
        
        private final Collection<String> filteredObjects;
        private final ESelectionMode mode;
        
        public GroupFilter(final Collection<String> filteredObjects, final ESelectionMode filteringMode){
            this.filteredObjects = filteredObjects;
            mode = filteringMode;
        }
        
        @Override
        public boolean omitEntity(final Entity entity){
            if (mode==ESelectionMode.INCLUSION){
                return !filteredObjects.contains(entity.getPid().toString());
            }else{
                return filteredObjects.contains(entity.getPid().toString());
            }
        }        
    }

    private static final class FilterCondition {

        private IRadFilter filter;
        private Sqml condition;
        private Sqml from;
        private Map<Id, Object> paramValues;

        public FilterCondition(final Sqml condition,
                                         final Sqml from,
                                         final Map<Id, Object> paramValues, 
                                         final IRadFilter filter) {
            this.condition = condition;
            this.from = from;
            if (paramValues == null) {
                this.paramValues = new HashMap<>(8);
            } else {
                this.paramValues = paramValues;
            }
            this.filter = filter;
        }

        public IRadFilter getFilter() {
            return filter;
        }

        public Sqml getConditionSqml() {
            return condition;
        }
        
        public Sqml getFromSqml(){
            return from;
        }

        public Map<Id, Object> getParamValues() {
            return Collections.unmodifiableMap(paramValues);
        }

        public void merge(final FilterCondition other) {
            filter = other.getFilter();
            if (other.getConditionSqml() != null && !other.getConditionSqml().getItems().isEmpty()) {
                if (condition == null) {
                    condition = other.getConditionSqml();
                } else {
                    condition.getItems().add(Sqml.Text.Factory.newInstance(" and "));
                    for (int i = 0, count = other.getConditionSqml().getItems().size(); i < count; i++) {
                        final Sqml.Item item = other.getConditionSqml().getItems().remove(0);
                        condition.getItems().add(item);
                    }
                }
                if (!other.paramValues.isEmpty()) {
                    if (paramValues == null) {
                        paramValues = other.paramValues;
                    } else {
                        paramValues.putAll(other.paramValues);
                    }
                }
            }
            if (other.getFromSqml()!=null && !other.getFromSqml().getItems().isEmpty()){
                if (from==null || from.getItems().isEmpty()){
                    from = other.getFromSqml();
                }else{
                    from.getItems().add(Sqml.Text.Factory.newInstance(","));
                    for (int i = 0, count = other.getFromSqml().getItems().size(); i < count; i++) {
                        final Sqml.Item item = other.getFromSqml().getItems().remove(0);
                        from.getItems().add(item);
                    }
                }
            }
        }
    }

    protected int writeCommonDisActions(final Restrictions presRestr, final Restrictions totalRestr, final Actions disActions) {
        int cnt = 0;
        if (!presRestr.getIsDeleteRestricted() && totalRestr.getIsDeleteRestricted()) {
            //enabled in presentation but disabled by access control system
            disActions.addNewItem().setType(ActionTypeEnum.DELETE);
            cnt++;
        }
        if (!presRestr.getIsUpdateRestricted() && totalRestr.getIsUpdateRestricted()) {
            //enabled in presentation but disabled by access control system
            disActions.addNewItem().setType(ActionTypeEnum.UPDATE);
            cnt++;
        }
        if (!presRestr.getIsCreateRestricted() && totalRestr.getIsCreateRestricted()) {
            //enabled in presentation but disabled by access control system
            disActions.addNewItem().setType(ActionTypeEnum.CREATE);
            cnt++;
        }
        if (!presRestr.getIsViewRestricted() && totalRestr.getIsViewRestricted()) {
            //enabled in presentation but disabled by access control system
            disActions.addNewItem().setType(ActionTypeEnum.VIEW);
            cnt++;
        }
        return cnt;
    }

    protected final class PropVal {

        final RadPropDef prop;
        final org.radixware.schemas.eas.Property xmlProp;
        final boolean readOnly;
        final boolean isOwnVal;

        protected PropVal(final RadPropDef prop,
                final org.radixware.schemas.eas.Property xmlProp,
                final boolean readOnly, final boolean isOwnVal) {
            this.prop = prop;
            this.xmlProp = xmlProp;
            this.readOnly = readOnly;
            this.isOwnVal = isOwnVal;
        }

        private final Id getValEntityId() {
            RadPropDef refProp = prop;
            while (refProp instanceof RadParentPropDef) {
                final RadParentPropDef p = (RadParentPropDef) refProp;
                refProp = getArte().getDefManager().getClassDef(p.getJoinedClassId()).getPropById(p.getJoinedPropId());
            }
            if (refProp instanceof IRadRefPropertyDef) {
                return ((IRadRefPropertyDef) refProp).getDestinationEntityId();
            }
            return null;
        }

        protected final void writeTo(
                final PresentationEntityAdapter entity,
                final RadEditorPresentationDef edPres,
                final EReadonlyPropModificationPolicy readonlyPropModificationPolicy) throws InterruptedException {
            if (isOwnVal) {
                if (!(prop.getAccessor() instanceof IRadPropWriteAccessor)) {
                    return;
                }
                if (prop.getValType() == EValType.PARENT_REF && !(prop instanceof RadParentPropDef)) {
                    if (!xmlProp.getRef().isSetBrokenRef()) {//RADIX-4636
                        setParentRefProp(entity, prop, xmlProp.getRef().getPID(), xmlProp.getRef().getClassId(), edPres, readonlyPropModificationPolicy);
                    }
                } else {
                    final Object val = 
                        EasValueConverter.easPropXmlVal2ObjVal(getArte(), xmlProp, prop.getValType(), getValEntityId(), true);                    
                    entity.setProp(prop.getId(), val);
                }
            } else {
                entity.setPropHasOwnVal(prop.getId(), false);
            }
        }

        protected final void writeTo(final PropValHandlersByIdMap newPropVals) {
            final Object val = 
                EasValueConverter.easPropXmlVal2ObjVal(getArte(), xmlProp, prop.getValType(), getValEntityId(), true);
            newPropVals.put(prop.getId(), new PropValHandler(isOwnVal, val));
        }
    }

    // Methods
    protected final void prepare(final Request rqXml) throws ServiceProcessServerFault,
            ServiceProcessClientFault {        
        sessionOptions = null;        
        if (rqXml.isSetSetSavepoint()) // TODO ? SavePoints in EAS
        {
            throw new RadixError("TODO ? SavePoints in EAS");		// final Element rqParams = DasRequest.getRequestParams(request);
            // SessionID
        }
        final Long sessionId = Long.valueOf(rqXml.getSessionId());
        // UserDef version
        final long explorerDefVer;
        if (SrvRunParams.getIsEasVerChecksOn()) {
            if (presenter.getArte().getExplicitRequestVersion() > 0) {
                explorerDefVer = presenter.getArte().getExplicitRequestVersion();
            } else {
                explorerDefVer = 0;
            }
            final boolean isForcedVer = SessionRequest.isForcedVersion(rqXml);

            if (!isForcedVer && explorerDefVer != presenter.getArte().getLatestCachedVersion()) {
                throw new ServiceProcessClientFault(
                        ExceptionEnum.INVALID_DEFINITION_VERSION.toString(),
                        String.valueOf(presenter.getArte().getLatestCachedVersion()), null, null);
            }
        } else {
            explorerDefVer = presenter.getArte().getLatestCachedVersion();
        }
        if (rqXml.getAuthType() == null) {
            throw EasFaults.newParamRequiedFault("AuthType", rqXml.getDomNode().getNodeName());
        }
        sessionOptions = presenter.connectToSession(this, sessionId, explorerDefVer, rqXml.getPwdToken(), rqXml.getChallenge(), rqXml.getAuthType());
    }

    private final static boolean isForcedVersion(final Request rqXml) {
        return rqXml.isSetDefinitionsVer() && rqXml.getDefinitionsVer().isSetForced() && rqXml.getDefinitionsVer().getForced();
    }

    protected static final Id getEntityId(final EntityRequest rqParams)
            throws ServiceProcessFault {
        final Definition entXml = rqParams.getEntity();
        if (entXml != null) {
            return entXml.getId();
        } else {
            throw EasFaults.newParamRequiedFault("Entity", rqParams.getDomNode().getNodeName());
        }

    }

    protected static final Id getClassId(final ClassRequest rqParams)
            throws ServiceProcessFault {
        final Definition classXml = rqParams.getClass1();
        if (classXml != null) {
            return classXml.getId();
        } else {
            throw EasFaults.newParamRequiedFault("Class", rqParams.getDomNode().getNodeName());
        }

    }

    protected final DdsTableDef getEntityTable(final EntityRequest rqParams)
            throws ServiceProcessFault {
        final Id entityId = getEntityId(rqParams);
        try {
            return getArte().getDefManager().getTableDef(entityId);
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Entity", rqParams.getDomNode().getNodeName(), entityId);
        }
    }

    protected final RadClassDef getClassDef(final ClassRequest rqParams) throws ServiceProcessFault {
        final Id classId = getClassId(rqParams);
        try {
            return getArte().getDefManager().getClassDef(classId);
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Class", rqParams.getDomNode().getNodeName(), classId);
        }
    }

    protected final RadSelectorPresentationDef getSelPres(
            final Definition presXml, final RadClassDef classDef)
            throws ServiceProcessFault {
        try {
            return classDef.getPresentation().getSelectorPresentationById(presXml.getId());
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault("Selector presentation",
                    presXml.getDomNode().getParentNode().getNodeName(), presXml.getId());
        }
    }

    protected final RadEditorPresentationDef getEdPres(final Definition presXml, final RadClassDef classDef) throws ServiceProcessFault {
        try {
            return classDef.getPresentation().getEditorPresentationById(presXml.getId());
        } catch (DefinitionNotFoundError e) {
            throw EasFaults.newDefWithIdNotFoundFault(PRES_DEF_NAME, presXml.getDomNode().getParentNode().getNodeName(), presXml.getId());
        }
    }

    protected final PresentationOptions getPresentationOptions(final ContextRequest rqParams,
            final RadClassDef classDef,
            final boolean isGroupRq,
            final boolean bSelPresMandatory,
            final Definition presXml) throws ServiceProcessFault, InterruptedException {
        final Context context = getContext(rqParams.getContext());
        return getPresentationOptions(context, classDef, isGroupRq, bSelPresMandatory, presXml, rqParams.getDomNode().getNodeName());
    }
    
    protected final PresentationOptions getPresentationOptions(final Context context,            
            final RadClassDef classDef,
            final boolean isGroupRq,
            final boolean bSelPresMandatory,
            final Definition presXml,
            final String domNode) throws ServiceProcessFault, InterruptedException {
        //RADIX-2990: uregister all temporary modified object to prevent from illegal save
        getArte().getCache().visitAllUsedExistingEntities(null, new Cache.EntityVisitor() {
            @Override
            public void visit(final Entity ent) {
                if (ent.isModified()) {
                    getArte().unregisterExistingEntityObject(ent);
                }
            }
        });
        getArte().getCache().visitAllNewEntities(null, new Cache.EntityVisitor() {
            @Override
            public void visit(final Entity ent) {
                getArte().unregisterNewEntityObject(ent);
            }
        });

        if (context != null) {
            context.checkAccessible();
        }
        RadEditorPresentationDef edPres = null;
        RadSelectorPresentationDef selPres = context == null ? null : context.getSelectorPresentation();
        if (presXml != null) {
            if (isGroupRq || presXml.getId().getPrefix() == EDefinitionIdPrefix.SELECTOR_PRESENTATION) {
                if (selPres == null) {
                    selPres = getSelPres(presXml, classDef);
                }
            } else {
                edPres = getEdPres(presXml, classDef);
            }
        } else if (isGroupRq && bSelPresMandatory && selPres == null) {
            throw EasFaults.newParamRequiedFault(PRES_DEF_NAME, domNode);
        }
        return new PresentationOptions(this, context, selPres, edPres);
    }    
    
    protected static PresentationContext getPresentationContext(final Arte arte, final Context context, final EntityGroup group) {
        if (context == null) {
            return UnknownPresentationContext.INSTANCE;
        }
        final EntityGroup entityGroup;
        if (group == null) {
            if (context.getSelectorPresentation() == null) {
                entityGroup = null;
            } else {
                entityGroup = arte.getGroupHander(context.getClassDef().getEntityId(), true);
                context.writeGroupProperties(entityGroup);
            }
        } else {
            entityGroup = group;
        }
        return context.getPresentationContext(entityGroup);
    }

    private static final Sqml createTmpSqml() {
        return Sqml.Factory.newInstance();
    }

    private CommonSelectorFilter getCommonFilter(final RadClassDef classDef,
            final Id selPresentationId,
            final org.radixware.schemas.eas.Filter fltXml) {
        try {
            final long lastUpdateTime = fltXml.getLastUpdateTime() == null ? 0 : fltXml.getLastUpdateTime().longValue();
            return presenter.getCommonSelectorFilters().find(getArte(),
                    classDef,
                    selPresentationId,
                    fltXml.getId(),
                    lastUpdateTime);
        } catch (SQLException exception) {
            final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(exception);
            getArte().getTrace().put(
                    Messages.MLS_ID_EAS_UNABLE_TO_LOAD_COMMON_FILTERS,
                    new ArrStr(classDef.getEntityId().toString(),
                            selPresentationId.toString(),
                            preprocessedExStack));
            throw new ServiceProcessClientFault(
                    ExceptionEnum.SERVER_MALFUNCTION.toString(), exception.getMessage(), exception, preprocessedExStack);
        }
    }

    protected final Group getGroup(final Request rqParams,
            final RadClassDef classDef, final PresentationOptions context,
            final boolean bIgnoreFilter) throws ServiceProcessFault {
        return getGroup(rqParams, classDef, context, bIgnoreFilter, true);
    }

    protected final Group getGroup(final Request rqParams,
            final RadClassDef classDef, final PresentationOptions context,
            final boolean bIgnoreFilter, final boolean checkForContextlessUsage) throws ServiceProcessFault {
        final RadSelectorPresentationDef selPres = context.selectorPresentation;
        final RadSelectorPresentationDef.Addons selAddons = selPres.getFiltersAndSortings(); // not null                
        Sqml groupCondition = null;
        Sqml groupFrom = null;
        FilterCondition filterCondition = null;
        List<RadSortingDef.Item> orderBy = null;
        final Sqml hint;
        if (!bIgnoreFilter) {
            final org.radixware.schemas.eas.Context contextXml;
            final org.radixware.schemas.eas.ClassFilters classFiltersXml;
            final Filter fltXml;

            if (rqParams instanceof ObjectOrGroupRequest){
                contextXml = ((ObjectOrGroupRequest) rqParams).getContext();
                fltXml = ((ObjectOrGroupRequest) rqParams).getFilter();
                classFiltersXml = ((ObjectOrGroupRequest) rqParams).getClassFilters();
            }else if (rqParams instanceof GroupRequest){
                contextXml = ((GroupRequest) rqParams).getContext();
                fltXml = ((GroupRequest) rqParams).getFilter();
                classFiltersXml = ((GroupRequest) rqParams).getClassFilters();
            }else{
                throw new IllegalArgumentException("Unsupported request "+rqParams.getClass().getName());
            }

            if (contextXml != null && contextXml.isSetTreePath()) {
                final org.radixware.schemas.eas.Context.TreePath.FilterList filterList = contextXml.getTreePath().getFilterList();
                if (filterList!=null && filterList.getFilterList()!=null){
                    for (org.radixware.schemas.eas.Context.TreePath.FilterList.Filter contextFltXml: filterList.getFilterList()){                                                
                        if (filterCondition == null) {
                            filterCondition = getFilterCondition(contextFltXml, classDef, selPres, true, contextFltXml.getExplorerItemId());
                        } else {
                            filterCondition.merge(getFilterCondition(contextFltXml, classDef, selPres,  true, contextFltXml.getExplorerItemId()));
                        }                        
                    }
                }
            }
            
            if (classFiltersXml!=null){
                if (classDef.getTableDef().findClassGuidColumn()==null){                    
                    final String message = 
                        "Table \'"+classDef.getTableDef().getName()+"\' does not contains classGuid column. Unable to use class condition";
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.INVALID_REQUEST.toString(),
                            message, null, null);
                }
                final FilterCondition classFilterCondition = getClassFilterCondition(classFiltersXml, classDef);
                if (classFilterCondition!=null){
                    if (filterCondition == null){
                        filterCondition = classFilterCondition;
                    }else{
                        filterCondition.merge(classFilterCondition);
                    }
                }
            }
            
            if (fltXml != null) {
                if (filterCondition == null) {
                    filterCondition = getFilterCondition(fltXml, classDef, selPres, false, null);
                } else {
                    filterCondition.merge(getFilterCondition(fltXml, classDef, selPres, false, null));
                }
            }
            if (filterCondition == null && selAddons.isFilterObligatory() /*RADIX-2927: && selAddons.getDefaultFilterId() != null*/) {
                //RADIX-2927
                throw new ServiceProcessClientFault(
                        ExceptionEnum.FILTER_IS_OBLIGATORY.toString(),
                        "Filter is obligatory", null, null);
//				try {
//					filter = context.selectorPresentation.getClassPresentation().getFilterById(selAddons.getDefaultFilterId());
//				} catch (DefinitionNotFoundError e) {
//					final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
//					throw new ServiceProcessClientFault(
//							ExceptionEnum.FILTER_NOT_FOUND.toString(),
//							"Default filter not found", e, preprocessedExStack);
//				}
                //end of RADIX-2927 fix
            }
            
            groupCondition = filterCondition == null ? null : filterCondition.getConditionSqml();
            groupFrom = filterCondition == null ? null : filterCondition.getFromSqml();
        }
        final IRadFilter filter = filterCondition == null ? null : filterCondition.getFilter();        
        final Sorting srtXml;
        if (rqParams instanceof SelectRq){
            srtXml = ((SelectRq) rqParams).getSorting();
        }else if (rqParams instanceof ObjectOrGroupRequest){
            srtXml = ((ObjectOrGroupRequest) rqParams).getSorting();
        }else{
            srtXml = null;
        }
        EPaginationMethod paginationMethod = EPaginationMethod.ABSOLUTE;
        RadSortingDef sorting;
        if (srtXml != null && srtXml.getId() != null) {
            try {
                sorting = classDef.getPresentation().getSortingById(srtXml.getId());
            } catch (DefinitionNotFoundError e) {
                final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
                throw new ServiceProcessClientFault(
                        ExceptionEnum.SORTING_NOT_FOUND.toString(), e.getMessage(), e, preprocessedExStack);
            }
            orderBy = sorting.getOrderBy();
            paginationMethod = sorting.getPaginationMethod();
            if (filter == null && !selAddons.isBaseSortingEnabledById(sorting.getId()) || filter != null && !filter.isBaseSortingEnabledById(sorting.getId())) {
                throw new ServiceProcessClientFault(
                        ExceptionEnum.INVALID_SORTING.toString(),
                        "Sorting is not applicable for current filter", null, null);
            }
        } else if (selAddons.getDefaultSortingId() != null
                && !(srtXml != null && srtXml.isSetAdditionalSortingColumns() && !srtXml.getAdditionalSortingColumns().getItemList().isEmpty())) {
            sorting = classDef.getPresentation().getSortingById(selAddons.getDefaultSortingId());
            if (filter != null ? !filter.isBaseSortingEnabledById(sorting.getId())
                    : !selAddons.isBaseSortingEnabledById(sorting.getId())) {
                if (filter != null) {
                    sorting = filter.getDefaultSorting(classDef.getPresentation());
                } else {
                    sorting = selAddons.getDefaultSorting(classDef.getPresentation());
                }
            }
            if (sorting != null) {
                orderBy = sorting.getOrderBy();
                paginationMethod = sorting.getPaginationMethod();
            }
        }else{
            sorting = null;
        }
        if (rqParams instanceof SelectRq) {// add <SortingColumns> to orderBy
            //final SelectRq selRq = (SelectRq) rqParams;
            if (srtXml != null && srtXml.isSetAdditionalSortingColumns() && !srtXml.getAdditionalSortingColumns().getItemList().isEmpty()) {
                if (orderBy == null && (filter != null ? !filter.isAnyCustomSortingEnabled()
                        : selAddons == null || !selAddons.isCustomSortingEnabled())) {
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.INVALID_SORTING.toString(),
                            "Custom sortings are not allowed in current context",
                            null, null);
                }
                if (!getArte().getRights().getCurUserCanAccess(
                        EDrcServerResource.EAS_SORTING_CREATION)) {
                    throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_CREATE_CUST_SRT, null);
                }
                final List<RadSortingDef.Item> resOrderBy = new ArrayList<RadSortingDef.Item>();
                if (orderBy != null) {
                    resOrderBy.addAll(orderBy);
                }
                for (Sorting.AdditionalSortingColumns.Item srtItemXml : srtXml.getAdditionalSortingColumns().getItemList()) {
                    resOrderBy.add(new RadSortingDef.Item(srtItemXml.getId(), srtItemXml.getOrder()));
                }
                orderBy = Collections.unmodifiableList(resOrderBy);
            }
        }

        final Sqml dirtyHint;
        if (sorting != null) {
            final Sqml srtHint = sorting.getHint();
            if (filter != null) {
                dirtyHint = filter.getSortingHintById(sorting.getId(), srtHint);
            } else {
                dirtyHint = srtHint != null ? srtHint : selAddons.getDefaultHint();
            }
        } else {
            dirtyHint = selAddons.getDefaultHint();
        }
        hint = dirtyHint == null ? null : Sqml.Factory.loadFrom("", dirtyHint);

        if (!bIgnoreFilter) {// Condition
            final boolean isSetCondition;
            
            if (rqParams instanceof GroupRequest ? ((GroupRequest) rqParams).isSetCondition() : ((ObjectOrGroupRequest) rqParams).isSetCondition()) {
                final Sqml c = Sqml.Factory.loadFrom("SelectRq.Condition", rqParams instanceof GroupRequest ? ((GroupRequest) rqParams).getCondition()
                        : ((ObjectOrGroupRequest) rqParams).getCondition());
                if (!c.getItems().isEmpty()) {
                    if (filter == null && !selAddons.isCustomFilterEnabled()) {
                        throw new ServiceProcessClientFault(
                                ExceptionEnum.APPLICATION_ERROR.toString(),
                                "Custom filters are not allowed in current context",
                                null, null);
                    }
                    //RADIX-3321: uncomment following check
                    //if (!getArte().getRights().getCurUserCanAccess(EDrcServerResource.EAS_FILTER_CREATION)) {
                    //    getArte().getTrace().put(EEventSeverity.WARNING, "Unauthorized custom filter usage", EEventSource.EAS);
                    //throw EasFaults.newAccessViolationFault("create custom filter");
                    //}
                    assertSubquriesAllowed(c);
                    if (groupCondition == null) {
                        groupCondition = createTmpSqml();
                        groupCondition.getItems().add(Sqml.Text.Factory.newInstance("("));
                    } else {
                        groupCondition.getItems().add(Sqml.Text.Factory.newInstance(" and ("));
                    }
                    try {
                        groupCondition.appendFrom(c);
                    } catch (XmlException e) {
                        throw new ServiceProcessServerFault(
                                ExceptionEnum.FORMAT_ERROR.toString(),
                                "Error on additional select condition SQML processing: "
                                + ExceptionTextFormatter.getExceptionMess(e),
                                e, null);
                    }
                    groupCondition.getItems().add(Sqml.Text.Factory.newInstance("\n)"));
                }
            }
        }
        final PropertyList groupPropsXml;
        if (rqParams instanceof GroupRequest) {
            groupPropsXml = ((GroupRequest) rqParams).getGroupProperties();
        } else {
            groupPropsXml = ((ObjectOrGroupRequest) rqParams).getGroupProperties();
        }
        final EntityGroup entGrp = getArte().getGroupHander(classDef.getEntityId());
        if (groupPropsXml != null && groupPropsXml.getItemList() != null && !groupPropsXml.getItemList().isEmpty()) {
            final PropValHandlersByIdMap propVals = new PropValHandlersByIdMap();
            for (PropertyList.Item propValXml : groupPropsXml.getItemList()) {
                final PropVal propVal = getPropVal(propValXml, entGrp.getRadMeta());
                propVal.writeTo(propVals);
            }
            for (Map.Entry<Id, PropValHandler> e : propVals.entrySet()) {
                entGrp.setProp(e.getKey(), e.getValue().getValue());
            }
        }
        if (rqParams instanceof ObjectOrGroupRequest){
            final ObjectOrGroupRequest groupRequest = (ObjectOrGroupRequest)rqParams;
            if (groupRequest.isSetSelectedObjects()){
                entGrp.setEntitySelection(EntitySelection.Factory.loadFromXml(groupRequest.getSelectedObjects()));
            }
        }
        final Map<Id, Object> conditionParamValsById
                = filterCondition == null ? null : filterCondition.getParamValues();
        
        Pid previousEntityPid = null;
        Id previousEntityClassId = null;
        if (paginationMethod == EPaginationMethod.RELATIVE) {
            if (rqParams instanceof SelectRq) {
                final ObjectReference prevRef = ((SelectRq) rqParams).getPreviousObjectReference();
                final String pidStr = prevRef == null ? null : prevRef.getPID();
                if (pidStr == null && ((SelectRq) rqParams).getStartIndex() > 1) {
                    throw new IllegalStateException("PreviousObjectPid is not defined for relative pagination from index " + ((SelectRq) rqParams).getStartIndex());
                }
                if (pidStr != null && ((SelectRq) rqParams).getStartIndex() > 1) {
                     previousEntityPid = new Pid(getArte(), context.selectorPresentation.getClassPresentation().getClassDef().getEntityId(), pidStr);
                     previousEntityClassId = prevRef.getClassId();
                }
            }
        }

        
        final EntityGroup.Parameters params = new EntityGroup.Parameters(Context.asEntGroupContext(context.context),
                context.selectorPresentation,
                groupCondition,
                groupFrom,
                conditionParamValsById,
                orderBy,
                hint,
                paginationMethod,
                previousEntityPid,
                previousEntityClassId
        );
        context.initEntGrp(entGrp, params);
        context.assertSelPresIsAccessibile(entGrp);
        if (checkForContextlessUsage
                && context.getSelRestrictions(entGrp).getIsContextlessUsageRestricted()
                && !entGrp.isContextDefined()) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_FOR_CONTEXTLESS_USAGE_OF_SEL_PRES, "\"" + selPres.getName() + "\"(#" + selPres.getId() + ")");
        }
        
        return new Group(entGrp, filter, sorting);
    }
    
    private static FilterCondition getClassFilterCondition(final org.radixware.schemas.eas.ClassFilters xmlFilters,
                                                                                  final RadClassDef classDef){
        if (xmlFilters==null || xmlFilters.getItemList()==null || xmlFilters.getItemList().isEmpty()){
            return null;
        }else{            
            final Sqml classFilterCondition = createTmpSqml();
            final Map<Id,Object> paramValues = new HashMap<>(3);            
            ParameterTag classGuidParam;            
            classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance("( "));
            for (org.radixware.schemas.eas.ClassFilters.Item item: xmlFilters.getItemList()){
                if (item.getClassId()!=null){
                    if (paramValues.size()>0){
                        classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance(" OR "));
                    }
                    classGuidParam = createClassGuidParameterTag();
                    paramValues.put(classGuidParam.getParameterId(), item.getClassId().toString());
                    if (item.isSetIncludeDescendants() && item.getIncludeDescendants()){
                        classFilterCondition.getItems().add(createIsClassExtendsCallTag());
                        classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance("("));
                        classFilterCondition.getItems().add(createClassGuidColumnTag(classDef));
                        classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance(", "));
                        classFilterCondition.getItems().add(classGuidParam);
                        classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance(")=1"));
                    }else{
                        classFilterCondition.getItems().add(createClassGuidColumnTag(classDef));
                        classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance("="));
                        classFilterCondition.getItems().add(classGuidParam);
                    }
                }
            }
            classFilterCondition.getItems().add(Sqml.Text.Factory.newInstance(" )"));
            if (paramValues.isEmpty()){
                return null;
            }else{
                return new FilterCondition(classFilterCondition, null, paramValues, null);
            }
        }
    }
    
    private static PropSqlNameTag createClassGuidColumnTag(final RadClassDef classDef){
        final PropSqlNameTag classGuidProp = PropSqlNameTag.Factory.newInstance();
        classGuidProp.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
        classGuidProp.setPropOwnerId(classDef.getTableDef().getId());
        classGuidProp.setPropId(classDef.getTableDef().getClassGuidColumn().getId());        
        return classGuidProp;
    }
    
    private static DbFuncCallTag createIsClassExtendsCallTag(){
        final DbFuncCallTag funcCall = DbFuncCallTag.Factory.newInstance();
        funcCall.setFunctionId(Id.Factory.loadFrom("dfn3OXAHT4FYLORDBBJABIFNQAABA"));
        return funcCall;
    }
    
    private static ParameterTag createClassGuidParameterTag(){
        final ParameterTag parameter = ParameterTag.Factory.newInstance();
        parameter.setParameterId(Id.Factory.newInstance(EDefinitionIdPrefix.PARAMETER));
        parameter.setLiteral(false);
        return parameter;
    }

    private FilterCondition getFilterCondition(final Filter xmlFilter,
                                                                  final RadClassDef classDef,
                                                                  final RadSelectorPresentationDef selPres,
                                                                  final boolean isContextFilter,                                                                   
                                                                  /*not null only if isContextFilter = true*/
                                                                  final Id explorerItemId) {
        Sqml filterCondition = null;
        Sqml filterFrom = null;
        IRadFilter filter = null;
        Map<Id, Object> fltParamValsById = null;
        Sqml fltAddCond = null;
        if (xmlFilter.getId() != null) {
            if (classDef.getPresentation().isFilterExistsById(xmlFilter.getId())) {
                try {
                    filter = classDef.getPresentation().getFilterById(xmlFilter.getId());
                } catch (DefinitionNotFoundError e) {
                    final String preprocessedExStack = ExceptionTextFormatter.exceptionStackToString(e);
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.FILTER_NOT_FOUND.toString(), e.getMessage(), e, preprocessedExStack);
                }
                if (filter.getAdditionalFrom()!=null){
                    filterFrom = createTmpSqml();
                    try {
                        filterFrom.appendFrom(filter.getAdditionalFrom());
                    }catch (XmlException e) {
                        throw new ServiceProcessServerFault(
                                            ExceptionEnum.FORMAT_ERROR.toString(),
                                            "Error on filter from SQML processing: "
                                            + ExceptionTextFormatter.getExceptionMess(e),
                                            e, null);
                    }                    
                }
            } else {
                final CommonSelectorFilter commonFilter = getCommonFilter(classDef, selPres.getId(), xmlFilter);
                if (commonFilter != null && !commonFilter.isSameFiler(xmlFilter)) {
                    final org.radixware.schemas.eas.CommonFilter commonFilterXml
                            = org.radixware.schemas.eas.CommonFilter.Factory.newInstance();
                    commonFilter.addToXml(commonFilterXml);
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.FILTER_IS_OBSOLETE.toString(),
                            commonFilterXml.xmlText(),
                            null, null);
                }
                filter = commonFilter;
            }
            if (filter == null) {
                final DefinitionNotFoundError error = new DefinitionNotFoundError(xmlFilter.getId());
                throw new ServiceProcessClientFault(
                        ExceptionEnum.FILTER_NOT_FOUND.toString(), error.getMessage(), error, null);
            }
        }
        if (xmlFilter.isSetAdditionalCondition()) {
            final RadSelectorPresentationDef.Addons selAddons = selPres.getFiltersAndSortings(); // not null                
            if (filter == null && !selAddons.isCustomFilterEnabled()) {
                throw new ServiceProcessClientFault(
                        ExceptionEnum.APPLICATION_ERROR.toString(),
                        "Custom filters are not allowed in current context",
                        null, null);
            }
            //RADIX-3321: uncomment following check
            //if (!getArte().getRights().getCurUserCanAccess(EDrcServerResource.EAS_FILTER_CREATION)) {
            //getArte().getTrace().put(EEventSeverity.WARNING, "Unauthorized custom filter usage", EEventSource.EAS);
            //throw EasFaults.newAccessViolationFault("create custom filter");
            //}
            if (!isContextFilter || (selAddons.isCustomFilterEnabled() && filterFrom==null)){
                fltAddCond = Sqml.Factory.loadFrom("GrpRq.Flt.AddCond", xmlFilter.getAdditionalCondition());
                assertSubquriesAllowed(fltAddCond);
            }
        }
        final PropertyList paramsXml = xmlFilter.getParameters();
        if (paramsXml != null) {
            final List<PropertyList.Item> paramXmls = paramsXml.getItemList();
            for (PropertyList.Item paramXml : paramXmls) {
                final Object paramVal = EasValueConverter.easPropXmlVal2ObjVal(getArte(), paramXml, null);
                if (fltParamValsById == null) {
                    fltParamValsById = new HashMap<>();
                }
                fltParamValsById.put(paramXml.getId(), paramVal);                
            }
        }
        if (filter != null && filter.getCondition() != null && (!isContextFilter || fltAddCond==null)) {
            filterCondition = createTmpSqml();
            filterCondition.getItems().add(Sqml.Text.Factory.newInstance("("));
            try {
                filterCondition.appendFrom(filter.getCondition());
            } catch (XmlException e) {
                throw new ServiceProcessServerFault(
                        ExceptionEnum.FORMAT_ERROR.toString(),
                        "Error on filter condition SQML processing: "
                        + ExceptionTextFormatter.getExceptionMess(e),
                        e, null);
            }
            filterCondition.getItems().add(Sqml.Text.Factory.newInstance("\n)"));
            if (isContextFilter && fltParamValsById!=null){                
                final String idPostfix;
                if (explorerItemId==null){
                    idPostfix = "-"+Id.Factory.newInstance(EDefinitionIdPrefix.EXPLORER_ITEM).toString();
                }else{
                    idPostfix = "-"+explorerItemId.toString();                    
                }
                changeFilterConditionParamIds(filterCondition, fltParamValsById, idPostfix);
                if (filterFrom!=null){
                    changeFilterConditionParamIds(filterFrom, fltParamValsById, idPostfix);
                }
            }
        }
        if (fltAddCond != null) {
            if (filterCondition == null) {
                filterCondition = createTmpSqml();
                filterCondition.getItems().add(Sqml.Text.Factory.newInstance("("));
            } else {
                filterCondition.getItems().add(Sqml.Text.Factory.newInstance(" and ("));
            }
            try {
                filterCondition.appendFrom(fltAddCond);
            } catch (XmlException e) {
                throw new ServiceProcessServerFault(
                        ExceptionEnum.FORMAT_ERROR.toString(),
                        "Error on additional filter condition SQML processing: "
                        + ExceptionTextFormatter.getExceptionMess(e),
                        e, null);
            }
            filterCondition.getItems().add(Sqml.Text.Factory.newInstance("\n)"));
        }

        if (filterCondition != null) {

            // check all filter parameters are defined            
            for (Sqml.Item item : filterCondition.getItems()) {
                if (item instanceof ParameterTag) {
                    final Id paramId = ((ParameterTag) item).getParameterId();
                    if (fltParamValsById == null || !fltParamValsById.containsKey(paramId)) {
                        throw new ServiceProcessClientFault(
                                ExceptionEnum.MISSING_FILTER_PARAM.toString(),
                                "Filter parameter #" + paramId + " is not defined", null, null);
                    }
                    ((ParameterTag) item).setParameterId(paramId);
                }
            }

            return new FilterCondition(filterCondition, filterFrom, fltParamValsById, (isContextFilter ? null : filter));
        } else {
            return new FilterCondition(null, filterFrom, null, (isContextFilter ? null : filter));
        }
    }
    
    private void changeFilterConditionParamIds(final Sqml condition, 
                                                                      final Map<Id, Object> fltParamValsById, 
                                                                      final String idPostfix){
        final RadixObjects<Scml.Item> items = condition.getItems(); 
        for (Scml.Item item: items){
            if (item instanceof ParameterAbstractTag){
                final ParameterAbstractTag tag = (ParameterAbstractTag)item;
                final Id initialId = tag.getParameterId();
                final Id uniqueId = Id.Factory.append(initialId, idPostfix);
                if (fltParamValsById.containsKey(initialId)){
                    tag.setParameterId(uniqueId);
                    final Object paramValue = fltParamValsById.remove(initialId);
                    fltParamValsById.put(uniqueId, paramValue);
                }else if (fltParamValsById.containsKey(uniqueId)){
                    tag.setParameterId(uniqueId);
                }else{
                    throw new ServiceProcessClientFault(
                            ExceptionEnum.MISSING_FILTER_PARAM.toString(),
                            "Filter parameter #" + initialId + " is not defined", null, null);                    
                }
            }
        }
    }

    protected final PropVal getPropVal(
            final org.radixware.schemas.eas.Property xmlProp,
            final RadClassDef classDef) {
        final Id propId = xmlProp.getId();
        final RadPropDef prop = classDef.getPropById(propId);
        final boolean readOnly;
        final boolean isOwnVal;

        if (xmlProp.isSetIsOwnVal()) {
            isOwnVal = xmlProp.getIsOwnVal();
        } else {
            isOwnVal = true;
        }
        if (xmlProp.isSetReadOnly()) {
            readOnly = xmlProp.getReadOnly();
        } else {
            readOnly = false;
        }
        return new PropVal(prop, xmlProp, readOnly, isOwnVal);
    }

    protected static interface PropValLoadFilter {

        boolean skip(PropVal val);
    }
    protected static final PropValLoadFilter NULL_PROP_LOAD_FILTER = new PropValLoadFilter() {
        @Override
        public boolean skip(final PropVal val) {
            return false;
        }
    };

    protected void loadPropsFromXml(
            final List<PropertyList.Item> propItemXmls,
            final PresentationEntityAdapter entity,
            final RadEditorPresentationDef edPres,
            final List<RadPropDef> updatedProps,
            final List<Id> readOnlyPropIds,
            final PropValLoadFilter loadFilter,
            final EReadonlyPropModificationPolicy readonlyPropModificationPolicy) throws ServiceProcessFault, InterruptedException {
        try {
            final ArrayList<PropVal> suspendedPropVals = new ArrayList<>(propItemXmls.size());
            final ArrayList<PropVal> suspendedPropVals2 = new ArrayList<>(propItemXmls.size());
            final ArrayList<Id> ignoringPropertyIds = new ArrayList<>();
            final Collection<RadPropDef> usedProps = edPres.getUsedPropDefs(entity.getEntity().getPresentationMeta());
            final boolean ignoreErrorOnSetReadOnlyProp
                    = readonlyPropModificationPolicy == EReadonlyPropModificationPolicy.ALLOWED && readOnlyPropIds != null;
            if (readOnlyPropIds != null) {
                readOnlyPropIds.clear();
            }
            for (PropertyList.Item propItemXml : propItemXmls) {
                final PropVal propVal = getPropVal(propItemXml, entity.getRawEntity().getRadMeta());
                if (!usedProps.contains(propVal.prop)) {
                    throw EasFaults.newDefinitionAccessViolationFault(getArte(),
                            Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_PROPERTY,
                            "\"" + propVal.prop.getName() + "\" (#" + String.valueOf(propVal.prop.getId()) + ") " + MultilingualString.get(getArte(), Messages.MLS_OWNER_ID, Messages.MLS_ID_IN_PRESENTATION) + " \"" + edPres.getName() + "\" (#" + String.valueOf(edPres.getId()) + ")",
                            EDefType.CLASS_PROPERTY,
                            new Id[]{edPres.getClassPresentation().getId(), propVal.prop.getId()});
                }
                if (!entity.getRawEntity().isInDatabase(false) && propVal.prop.getValType() == EValType.OBJECT && (!propVal.xmlProp.isNilRef() || !propVal.xmlProp.isSetRef())) {
                    continue; //object property should not be edited until create
                }
                if (loadFilter.skip(propVal)) {
                    continue;
                }
                if (propVal.prop.getValType() == EValType.PARENT_REF) {

                    if (!propVal.isOwnVal
                            && propVal.prop instanceof RadInnateRefPropDef
                            && propVal.prop.getId().getPrefix() != EDefinitionIdPrefix.ADS_USER_PROP) {
                        final RadInnateRefPropDef refProp = (RadInnateRefPropDef) propVal.prop;
                        //RADIX-7492: Если ссылка наследуется, то значения свойств в соответствующем внешнем ключе 
                        //скорее всего логические (тоже унаследованные) и должны быть проигнорированы.
                        for (DdsReferenceDef.ColumnsInfoItem rp : refProp.getReference().getColumnsInfo()) {
                            ignoringPropertyIds.add(rp.getChildColumnId());
                        }
                    }

                    if (propVal.xmlProp.isNilRef()
                            || propVal.xmlProp.getRef().getPID() == null
                            || propVal.xmlProp.getRef().getPID().isEmpty()) // first - null refs (setting ref to null can broke other not null ref using a same FK columnn)
                    {
                        if (updatedProps != null) {
                            updatedProps.add(propVal.prop);
                        }
                        if (readOnlyPropIds != null && propVal.readOnly) {
                            readOnlyPropIds.add(propVal.prop.getId());
                        }
                        writePropertyValue(propVal, entity, edPres, readonlyPropModificationPolicy, ignoreErrorOnSetReadOnlyProp);
                    } else {
                        suspendedPropVals.add(propVal); //second - filled ref
                    }
                } else {
                    suspendedPropVals2.add(propVal); // than other properties
                }
            }
            for (PropVal val : suspendedPropVals) {
                if (updatedProps != null) {
                    updatedProps.add(val.prop);
                }
                if (readOnlyPropIds != null && val.readOnly) {
                    readOnlyPropIds.add(val.prop.getId());
                }
                writePropertyValue(val, entity, edPres, readonlyPropModificationPolicy, ignoreErrorOnSetReadOnlyProp);
            }
            for (PropVal val : suspendedPropVals2) {
                if (!ignoringPropertyIds.contains(val.prop.getId())) {
                    if (updatedProps != null) {
                        updatedProps.add(val.prop);
                    }
                    if (readOnlyPropIds != null && val.readOnly) {
                        readOnlyPropIds.add(val.prop.getId());
                    }
                    writePropertyValue(val, entity, edPres, readonlyPropModificationPolicy, ignoreErrorOnSetReadOnlyProp);
                }
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Error on writing properties values to entity object");
        }
    }

    private void writePropertyValue(final PropVal value,
            final PresentationEntityAdapter entity,
            final RadEditorPresentationDef edPres,
            final EReadonlyPropModificationPolicy readonlyPropModificationPolicy,
            final boolean ignoreErrorOnChangeReadOnlyProp) throws InterruptedException {
        try {
            value.writeTo(entity, edPres, readonlyPropModificationPolicy);
        } catch (PrimaryKeyModificationError error) {
            if (value.readOnly && ignoreErrorOnChangeReadOnlyProp) {
                final RadPropDef propDef = value.prop;
                //RADIX-9603: Ignoring this error cause it may be client data was obsolete.                
                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(error);
                getArte().getTrace().put(Messages.MLS_ID_EAS_ERR_ON_SET_PROP_VAL,
                        new ArrStr(propDef.getName(), propDef.getId().toString(), exceptionStack)
                );
            } else {
                throw error;
            }
        }
    }

    protected final void writeCurData2Entity(final PresentationEntityAdapter entity,
            final RadEditorPresentationDef edPres,
            final org.radixware.schemas.eas.Object curDataXml,
            final List<Id> readOnlyPropIds,
            final PropValLoadFilter loadFilter)
            throws ServiceProcessFault, InterruptedException {
        if (curDataXml == null || !curDataXml.isSetProperties()) {
            return;
        }
        loadPropsFromXml(curDataXml.getProperties().getItemList(), entity, edPres, null, readOnlyPropIds, loadFilter, EReadonlyPropModificationPolicy.ALLOWED);
    }

    protected final void writeCurData2Map(
            final RadClassDef classDef,
            final RadEditorPresentationDef edPres,
            final org.radixware.schemas.eas.Object curDataXml,
            final PropValHandlersByIdMap curPropValsById,
            final PropValLoadFilter loadFilter) throws ServiceProcessFault, InterruptedException {
        if (curDataXml == null || !curDataXml.isSetProperties()) {
            return;
        }
        writeCurData2Map(classDef, edPres.getUsedPropDefs(classDef.getPresentation()), curDataXml.getProperties(), curPropValsById, loadFilter);
    }

    protected final void writeCurData2Map(
            final RadClassDef classDef,
            final Collection<RadPropDef> usedProps,
            final org.radixware.schemas.eas.PropertyList propListXml,
            final PropValHandlersByIdMap curPropValsById,
            final PropValLoadFilter loadFilter) throws ServiceProcessFault, InterruptedException {
        final List<PropertyList.Item> propItemXmls = propListXml.getItemList();
        PropVal val;
        try {
            for (PropertyList.Item propItemXml : propItemXmls) {
                val = getPropVal(propItemXml, classDef);
                if (loadFilter.skip(val)) {
                    continue;
                }
                if (!usedProps.contains(val.prop)) {
                    throw EasFaults.newDefinitionAccessViolationFault(getArte(),
                            Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_PROPERTY,
                            "\"" + val.prop.getName() + "\" (#" + String.valueOf(val.prop.getId()) + ")",
                            EDefType.CLASS_PROPERTY,
                            new Id[]{classDef.getId(), val.prop.getId()});
                }
                val.writeTo(curPropValsById);
            }
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't read current data");
        }
    }

    static final void writeTrace(final Response response,
            final RequestTraceBuffer traceBuffer) {
        final List<TraceItem> trace = traceBuffer.asList();
        if (!trace.isEmpty()) {
            final org.radixware.schemas.eas.Trace traceXml = response.addNewTrace();
            org.radixware.schemas.eas.Trace.Item itemXml;
            int i = 0;
            while (i < trace.size()) {
                boolean forceBreak = false;
                TraceItem item;
                if (i == MAX_TRACE_ITEMS_TO_WRITE && System.getProperty("radix.trace.disable.truncate") == null) {
                    item = new TraceItem(null, EEventSeverity.WARNING, null, new ArrStr("Trace contains to many records and has been truncated"), EEventSource.ARTE.getValue(), false);
                    forceBreak = true;
                } else {
                    item = trace.get(i);
                }
                i++;
                itemXml = traceXml.addNewItem();
                if (item.severity.equals(EEventSeverity.ALARM)) {
                    itemXml.setLevel(TraceLevelEnum.ALARM);
                } else if (item.severity.equals(EEventSeverity.ERROR)) {
                    itemXml.setLevel(TraceLevelEnum.ERROR);
                } else if (item.severity.equals(EEventSeverity.WARNING)) {
                    itemXml.setLevel(TraceLevelEnum.WARNING);
                } else if (item.severity.equals(EEventSeverity.EVENT)) {
                    itemXml.setLevel(TraceLevelEnum.EVENT);
                } else {
                    itemXml.setLevel(TraceLevelEnum.DEBUG);
                }
                itemXml.setStringValue(XmlUtils.getSafeXmlString(item.toString()));
                if (forceBreak) {
                    break;
                }
            }
        }
    }
    private static final QName tranQName = new QName(XSD, "Transaction");

    static final int getTranWordIntValue(final XmlObject request) {
        final XmlCursor rqCursor = request.newCursor();
        try {
            if (rqCursor.toFirstChild()) {
                final XmlObject[] tran = rqCursor.getObject().selectChildren(
                        tranQName);
                if (tran != null && tran.length > 0) {
                    final XmlCursor trCursor = tran[0].newCursor();
                    try {
                        trCursor.toFirstChild();
                        return TransactionEnum.Enum.forString(
                                trCursor.getTextValue()).intValue();
                    } finally {
                        trCursor.dispose();
                    }
                }
            }
            return TransactionEnum.INT_NO;
        } finally {
            rqCursor.dispose();
        }
    }
    private static final QName traceProfileQName = new QName(XSD,
            "TraceProfile");

    static final String getTraceProfile(final XmlObject request) {
        final XmlCursor rqCursor = request.newCursor();
        try {
            if (rqCursor.toFirstChild()) {
                final XmlObject[] traceProfile = rqCursor.getObject().selectChildren(traceProfileQName);
                if (traceProfile != null && traceProfile.length > 0) {
                    final XmlCursor trCursor = traceProfile[0].newCursor();
                    try {
                        trCursor.toFirstChild();
                        return trCursor.getTextValue();
                    } finally {
                        trCursor.dispose();
                    }
                }
            }
            return null;
        } finally {
            rqCursor.dispose();
        }
    }

    protected static final FilterParamTypeEnum.Enum dbpValType2FilterParamType(
            final EValType dbpType) {
        switch (dbpType) {
            case STR:
            case CHAR:
            case CLOB:
                return FilterParamTypeEnum.STR;
            case NUM:
                return FilterParamTypeEnum.NUM;
            case INT:
                return FilterParamTypeEnum.INT;
            case BOOL:
                return FilterParamTypeEnum.BOOL;
            case BLOB:
            case BIN:
                return FilterParamTypeEnum.BLOB;
            case PARENT_REF:
                return FilterParamTypeEnum.PARENT_TITLE;
            case DATE_TIME:
                return FilterParamTypeEnum.DATE;
            default:
                throw new RadixError("Value type \"" + dbpType.getName() + "\" is not supported for filter's parameter", null);
        }
    }
    static final private Id USER_FUNC_TAB_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");

    protected final void addPropXml(
            final PropertyList propsXml,
            final PresentationEntityAdapter presEntAdapter,
            final RadPresentationDef pres,
            final Context context,
            final RadPropDef propDef,
            final boolean bWithLob) {
        final List<DdsReferenceDef> contextRefs = new LinkedList<>();
        final Set<Id> contextPropIds = new HashSet<>();
        if (context instanceof EdPresExplrItemContext) {
            final RadExplorerItemDef ei = ((EdPresExplrItemContext) context).getExplorerItem();
            if (ei instanceof RadChildRefExplorerItemDef) {
                final DdsReferenceDef contextRef = ((RadChildRefExplorerItemDef) ei).getChildReference();
                contextRefs.add(contextRef);
                for (DdsReferenceDef.ColumnsInfoItem p : contextRef.getColumnsInfo()) {
                    contextPropIds.add(p.getChildColumnId());
                }
            }
        }
        if (context != null) {
            final Set<Id> propIds = context.getContextProperties().getPropIds();
            for (Id contextPropId : propIds) {
                final RadPropDef contextProp = context.getClassDef().getPropById(contextPropId);
                if (contextProp instanceof IRadRefPropertyDef) {
                    final DdsReferenceDef contextRef = ((IRadRefPropertyDef) contextProp).getReference();
                    contextRefs.add(contextRef);
                    for (DdsReferenceDef.ColumnsInfoItem p : contextRef.getColumnsInfo()) {
                        contextPropIds.add(p.getChildColumnId());
                    }
                } else {
                    contextPropIds.add(contextPropId);
                }
            }
        }

        final PropertyList.Item propXml = propsXml.addNewItem();
        Object val = null;
        ParentInfo ptValInfo = null;
        boolean isReadonly = false;
        final Entity entity = presEntAdapter.getRawEntity();
        final boolean isReferenceType = propDef.getValType() == EValType.PARENT_REF || propDef.getValType() == EValType.OBJECT;
        if (bWithLob || isReferenceType || !pres.getPropIsReadSeparatelyByPropId(entity.getPresentationMeta(), propDef.getId())) {
            try {
                if (isReferenceType) {
                    try {
                        if ((propDef instanceof IRadRefPropertyDef) && contextRefs.contains(((IRadRefPropertyDef) propDef).getReference())) {
                            isReadonly = true; // It's a context property. It must be readonly.
                        }
                        val = presEntAdapter.getProp(propDef.getId());
                        ptValInfo = getParentInfo(entity, propDef, (Entity) val, pres);
                    } catch (EntityObjectNotExistsError e) {
                        ptValInfo = getParentInfo(entity, propDef, e, pres);
                    } catch (WrongPidFormatError error) {
                        ptValInfo = getParentInfo(entity, propDef, new EntityObjectNotExistsError(error), pres);
                    }
                } else {
                    isReadonly = contextPropIds.contains(propDef.getId());
                    val = presEntAdapter.getProp(propDef.getId());
                }
            } catch (RuntimeException exception) {
                propsXml.removeItem(propsXml.sizeOfItemArray() - 1);
                final String exceptionStack = ExceptionTextFormatter.exceptionStackToString(exception);
                getArte().getTrace().put(Messages.MLS_ID_EAS_ERR_ON_GET_PROP_VAL,
                        new ArrStr(propDef.getName(), propDef.getId().toString(), exceptionStack)
                );
                return;
            }
        }
        if (propDef.getValType() == EValType.ARR_REF) {
            EasValueConverter.objVal2EasPropXmlVal(val, ptValInfo, propDef.getValType(), propXml, new EasValueConverter.IParentInfoProducer() {
                @Override
                public ParentInfo getParentInfo(Entity value) {
                    return SessionRequest.this.getParentInfo(entity, propDef, value, pres);
                }
            });
        } else {
            EasValueConverter.objVal2EasPropXmlVal(val, ptValInfo, propDef.getValType(), propXml);
        }

        propXml.setId(propDef.getId());
        if (isReadonly) {
            propXml.setReadOnly(isReadonly);
        }

        if (!entity.getIsPropValDefined(propDef.getId())) {
            propXml.setIsDefined(false);
        }

        final boolean usingFirstPossibleInheritancePath;
        if (propDef.getIsValInheritable()) {
            RadPropDef.ValInheritancePath inheritancePath = entity.getPropValInheritancePath(propDef.getId());
            if (inheritancePath==null){
                inheritancePath = getFirstPossiblePropValInheritancePath(entity, propDef.getId());
                usingFirstPossibleInheritancePath = inheritancePath!=null;
            }else{
                usingFirstPossibleInheritancePath = false;
            }
            if (inheritancePath != null) {
                final InheritableValue inhertValXml = propXml.addNewInheritableValue();
                final Path pathXml = inhertValXml.addNewPath();
                Path.Item itemXml;
                Path.Item.ReferenceProperty refPropXml;
                ObjectReference refValXml;
                RadPropDef destProp;
                Entity item = entity;
                for (final Id refPropId : inheritancePath.getRefPropIds()) {
                    itemXml = pathXml.addNewItem();
                    destProp = item.getRadMeta().getPropById(refPropId);
                    refPropXml = itemXml.addNewReferenceProperty();
                    refPropXml.setId(destProp.getId());
                    refPropXml.setName(destProp.getName());
                    final Entity prevItem = item;
                    item = (Entity) item.getProp(refPropId);
                    refValXml = itemXml.addNewReferenceValue();
                    refValXml.setClassId(item.getRadMeta().getId());
                    refValXml.setPID(item.getPid().toString());
                    refValXml.setTitle(prevItem.onCalcParentTitle(refPropId, item, item.calcTitle()));
                }
                destProp = item.getRadMeta().getPropById(inheritancePath.getDestPropId());
                final Property destPropXml = inhertValXml.addNewProperty();
                destPropXml.setId(destProp.getId());
                if (bWithLob || !pres.getPropIsReadSeparatelyByPropId(entity.getPresentationMeta(), propDef.getId())) {
                    Object inhertVal = null;
                    ParentInfo inhertPtValInfo = null;
                    if (destProp.getValType() == EValType.PARENT_REF || destProp.getValType() == EValType.OBJECT) {
                        try {
                            inhertVal = item.getProp(destProp.getId());
                            inhertPtValInfo = getParentInfo(entity, propDef, (Entity) inhertVal, pres);
                        } catch (EntityObjectNotExistsError e) {
                            inhertPtValInfo = getParentInfo(entity, propDef, e, pres);
                        } catch (WrongPidFormatError error) {
                            inhertPtValInfo = getParentInfo(entity, propDef, new EntityObjectNotExistsError(error), pres);
                        }
                    } else {
                        inhertVal = item.getProp(destProp.getId());
                    }
                    EasValueConverter.objVal2EasPropXmlVal(inhertVal, inhertPtValInfo, destProp.getValType(), destPropXml);
                }
            }
        }else{
            usingFirstPossibleInheritancePath = false;
        }
        
        if (usingFirstPossibleInheritancePath){
            if (isInheritedValue(entity, propDef)){
                propXml.setIsOwnVal(false);
            }
        }else{
            if (!entity.getPropHasOwnVal(propDef.getId())) {
                propXml.setIsOwnVal(false);
            }        
        }
    }
    
    private boolean isInheritedValue( final Entity entity, final RadPropDef prop) {
        final Id propId = prop.getId();
        if (propId.getPrefix() == EDefinitionIdPrefix.ADS_USER_PROP){
            return !entity.getPropHasOwnVal(propId);
        } else {
            return prop.getIsValInheritable() 
                      && (prop instanceof RadInnatePropDef
                          || prop instanceof RadInnateRefPropDef
                          || prop instanceof RadDetailPropDef 
                          || prop instanceof RadSqmlPropDef)
                      && prop.getValIsInheritMark(getArte(), entity.getNativePropOwnVal(propId));
        }
    }
    
    protected final ValInheritancePath getFirstPossiblePropValInheritancePath(final Entity entity, final Id id) {        
        final RadPropDef prop = entity.getRadMeta().getPropById(id);
        NEXT_PATH:
        for (RadPropDef.ValInheritancePath path : prop.getValInheritPathes()) {
            Entity parent = entity;
            for (final Id refPropId : path.getRefPropIds()) {
                parent = (Entity) parent.getProp(refPropId);
                if (parent == null) {
                    continue NEXT_PATH;
                }
            }
            return path;
        }
        return null;
    }

    protected ParentInfo getParentInfo(final IRadClassInstance ptOwner, final RadPropDef ptProp, final Entity parent, final RadPresentationDef pres) {
        return getParentInfoImpl(ptOwner, ptProp, parent, null, pres);
    }

    protected ParentInfo getParentInfo(final IRadClassInstance ptOwner, final RadPropDef ptProp, final EntityObjectNotExistsError brokenRefErr, final RadPresentationDef pres) {
        return getParentInfoImpl(ptOwner, ptProp, null, brokenRefErr, pres);
    }

    private ParentInfo getParentInfoImpl(IRadClassInstance ptOwner, RadPropDef ptProp, final Entity parent, final EntityObjectNotExistsError brokenRefErr, final RadPresentationDef pres) {
        while (ptProp instanceof RadParentPropDef) {
            final RadParentPropDef joinProp = (RadParentPropDef) ptProp;
            for (Id refId : joinProp.getRefPropIds()) {
                try{
                    ptOwner = (Entity) ptOwner.getProp(refId);
                }catch(EntityObjectNotExistsError error){
                    return ParentInfo.Factory.createParentInfo(null, null, false);
                }
                if (ptOwner == null) {
                    break;
                }
            }
            if (ptOwner == null) {
                return ParentInfo.Factory.createParentInfo(null, null, false);
            }
            ptProp = ptOwner.getRadMeta().getPropById(joinProp.getJoinedPropId());
        }
        final RadParentTitlePropertyPresentationDef propPres;
        final RadClassDef ownerClassDef = ptOwner.getRadMeta();
        final RadClassPresentationDef ownerClassPres = ownerClassDef.getPresentation();
        final String title;
        final RadEntityTitleFormatDef titleFormat;
        if (pres == null) {
            propPres = (RadParentTitlePropertyPresentationDef) ownerClassPres.getPropPresById(ptProp.getId());
            titleFormat = propPres.getTitleFormat(ownerClassPres);
        } else {
            propPres = (RadParentTitlePropertyPresentationDef) pres.getPropPresById(ownerClassPres, ptProp.getId());
            titleFormat = pres.getPropParentTitleFormat(ownerClassPres, ptProp.getId());
        }
        if (brokenRefErr == null) {
            title = getParentTitle(ptOwner, ptProp.getId(), parent, titleFormat);
        } else {
            title = null;
        }

        final EnumSet<EReferencedObjectActions> allowedActions = EnumSet.noneOf(EReferencedObjectActions.class);
        final boolean isSelectable;
        if (ptProp.getValType() == EValType.PARENT_REF) { //RADIX-4882
            final IRadRefPropertyDef propertyRef = (IRadRefPropertyDef) ptProp;
            final RadSelectorPresentationDef ptSelPres
                    = PropContext.calcSelectorPresentation(propertyRef, ownerClassDef, propPres);
            final RadConditionDef.Prop2ValueCondition contextProps
                    = RadConditionDef.Prop2ValueCondition.fromParentRefProperty(propertyRef, ownerClassDef, propPres, ptSelPres);
            final RadClassDef destClassDef = ownerClassDef.getRelease().getClassDef(propertyRef.getDestinationClassId());
            final List<Id> roleIds = getCurUserRoleIds(contextProps, destClassDef);
            final EntityGroup entitGroup = getArte().getGroupHander(destClassDef.getEntityId());
            final EntityGroup.PropContext groupContext = 
                new EntityGroup.PropContext(destClassDef, propertyRef, ownerClassPres, propPres, ptOwner, null);            
            entitGroup.set(groupContext, ptSelPres, null, null, null, null, null);
            isSelectable = !ptSelPres.getTotalRestrictions(roleIds).getIsAccessRestricted()
                                   && !entitGroup.getAdditionalRestrictions(ptSelPres, roleIds).getIsAccessRestricted();
            if (brokenRefErr == null && parent != null && propPres != null) {//RADIX-5098 
                allowedActions.addAll(calcAllowedActionsForReferencedObject(ptOwner, parent, propPres, ownerClassPres));
            }
        } else {
            isSelectable = false;
            // editor presentation list is not presented in RadParentTitlePropertyPresentationDef
            // so let's assume that editor should be accessible for all editable objects
            if (brokenRefErr == null && parent != null) {
                allowedActions.addAll(calcAllowedActionsForReferencedObject(ptOwner, parent, propPres, ownerClassPres));
            }
        }

        if (brokenRefErr != null) {
            return ParentInfo.Factory.createBrokenRefInfo(brokenRefErr, isSelectable);
        } else {
            return ParentInfo.Factory.createParentInfo(title, allowedActions, isSelectable);
        }
    }

    private static EnumSet<EReferencedObjectActions> calcAllowedActionsForReferencedObject(final IRadClassInstance ptOwner,
            final Entity object,
            final RadParentTitlePropertyPresentationDef propPres,
            final RadClassPresentationDef ownerClassPres) {
        final EnumSet<EReferencedObjectActions> allowedActions = EnumSet.noneOf(EReferencedObjectActions.class);
        final PresentationEntityAdapter presAdapter = object.getArte().getCache().getPresentationAdapter(object);
        final PresentationContext context;
        if (ptOwner instanceof Report){
            context = new ReportPropertyPresentationContext((Report)ptOwner, propPres.getPropId(), null, null);
        }else if (ptOwner instanceof FormHandler){
            context = new FormPropertyPresentationContext((FormHandler)ptOwner, propPres.getPropId(), null, null);
        }else if (ptOwner instanceof Entity){
            context = new EntityPropertyPresentationContext((Entity)ptOwner, propPres.getPropId(), null, null);
        }else{
            context = UnknownPresentationContext.INSTANCE;
        }
        presAdapter.setPresentationContext(context);
        final Collection<RadEditorPresentationDef> presentations
                = propPres.getParentEditorPresentations(ownerClassPres);
        final RadEditorPresentationDef p
                = presentations.isEmpty() ? null : presAdapter.selectEditorPresentation(presentations);
        if (p != null) {
            final Restrictions additionalRestrictions = presAdapter.getAdditionalRestrictions(p);            
            final Restrictions parentRestrictions;
            if (additionalRestrictions==Restrictions.ZERO){
                parentRestrictions = p.getTotalRestrictions(object);
            }else{
                parentRestrictions = Restrictions.Factory.sum(p.getTotalRestrictions(object), additionalRestrictions);
            }
            if (!parentRestrictions.getIsAccessRestricted()) {
                allowedActions.add(EReferencedObjectActions.ACCESS);
                if (!parentRestrictions.getIsViewRestricted()) {
                    ERuntimeEnvironmentType presentationRuntimeEnvironment = p.getRuntimeEnvironmentType();
                    if (presentationRuntimeEnvironment == ERuntimeEnvironmentType.COMMON_CLIENT
                            || presentationRuntimeEnvironment == object.getArte().getClientEnvironment()) {//RADIX-8123
                        allowedActions.add(EReferencedObjectActions.VIEW);
                    }
                }
                if (!parentRestrictions.getIsUpdateRestricted()) {
                    allowedActions.add(EReferencedObjectActions.MODIFY);
                }
                if (!parentRestrictions.getIsDeleteRestricted()) {
                    allowedActions.add(EReferencedObjectActions.DELETE);
                }
            }
        }
        return allowedActions;
    }
    
    protected final void initNewObject(final Entity obj,
            final Context context,
            final RadEditorPresentationDef presentation,
            final PropValHandlersByIdMap initialPropValsById, final Entity src,
            final EEntityInitializationPhase phase) throws ServiceProcessFault, InterruptedException{
        initNewObject(obj, context, presentation, initialPropValsById, src, phase, Collections.<Id>emptyList());
    }

    protected final void initNewObject(final Entity obj,
            final Context context,
            final RadEditorPresentationDef presentation,
            final PropValHandlersByIdMap initialPropValsById, final Entity src,
            final EEntityInitializationPhase phase,
            final Collection<Id> ignoreUserObjects)
            throws ServiceProcessFault, InterruptedException {
        try {
            if (context != null
                    && context.getContextReferenceRole() == Context.EContextRefRole.CHILDREN_SCOPE
                    && context.getContextObjectPid() != null
                    && context.getContextReference() != null) {
                obj.setParentRef(context.getContextReference(), getArte().getEntityObject(context.getContextObjectPid()));
            }
            writeContextProperties(obj, context);
            final PresentationContext presContext = getPresentationContext(getArte(), context, null);
            final PresentationEntityAdapter entityAdapter = getArte().getPresentationAdapter(obj);
            entityAdapter.setPresentationContext(presContext);
            entityAdapter.init(initialPropValsById, src, phase);
            final RadClassDef classDef = obj.getRadMeta();
            for (RadPropDef prop : classDef.getProps()) {
                if (prop instanceof RadUserPropDef
                    && !ignoreUserObjects.contains(prop.getId())
                    && (presentation.getPropIsNotNullByPropId(obj.getPresentationMeta(), prop.getId()) || (prop.getValType()==EValType.OBJECT && (initialPropValsById==null || !initialPropValsById.containsKey(prop.getId()))))
                    && !obj.getIsPropValDefined(prop.getId())) {
                    if (src != null) {
                        if (src.getProp(prop.getId()) != null) {
                            obj.copyPropVal(prop, src);
                        }
                    } else if (prop.getInitVal(getArte()) != null) {
                        obj.setProp(prop.getId(), prop.getInitVal(getArte()));
                    }
                }
            }

        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e,
                    "Can't init a new object");
        }
    }

    private void writeContextProperties(final Entity obj, final Context context) {
        if (context != null) {
            final RadConditionDef.Prop2ValueCondition contextProperties = context.getContextProperties();
            final RadClassDef classDef = obj.getRadMeta();
            for (Id propertyId : contextProperties.getPropIds()) {
                final Object propertyVal = contextProperties.getPropVal(getArte(), classDef.getPropById(propertyId));
                obj.setProp(propertyId, propertyVal);
            }
        }
    }

    public final RadEditorPresentationDef getActualEditorPresentation(
            final PresentationEntityAdapter<? extends Entity> entityAdapter,
            final List<RadEditorPresentationDef> edPresenations,
            final boolean bFaultsIsOn)
            throws ServiceProcessClientFault {
        final RadEditorPresentationDef resPres = entityAdapter.selectEditorPresentation(edPresenations);
        if (resPres == null && bFaultsIsOn) {
            throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_ACCESS_ANY_CNTX_ED_PRES, null);
        }
        return resPres;
    }

    protected final DdsReferenceDef getParentRef(final RadPropDef ptProp) {
        if (ptProp instanceof RadInnateRefPropDef) {
            final RadInnateRefPropDef pt = (RadInnateRefPropDef) ptProp;
            return pt.getReference();
        } else if (ptProp instanceof RadDetailParentRefPropDef) {
            final RadDetailParentRefPropDef pt = (RadDetailParentRefPropDef) ptProp;
            return pt.getReference();
        }
        return null;
    }

    static enum EReadonlyPropModificationPolicy {

        FORBIDDEN,
        ALLOWED
    }

    protected final DdsReferenceDef setParentRefProp(
            final PresentationEntityAdapter entity,
            final RadPropDef ptProp, 
            final String parentPidStr,
            final Id classId,
            final RadEditorPresentationDef edPres,
            final EReadonlyPropModificationPolicy readonlyPropModificationPolicy) throws InterruptedException {
        final DdsReferenceDef ptRef = getParentRef(ptProp);
        final Id destEntId;
        if (ptProp instanceof IRadRefPropertyDef) {
            final IRadRefPropertyDef usrRef = (IRadRefPropertyDef) ptProp;
            destEntId = usrRef.getDestinationEntityId();
        } else {
            // unsupported parent modification (parent of parent)
            final Entity oldParent = (Entity) entity.getProp(ptProp.getId());
            // if not modified just return
            if (oldParent == null) {
                if (parentPidStr == null || parentPidStr.length() == 0) {
                    return null;
                }
            } else {
                if (oldParent.getPid().toString().equals(parentPidStr)) {
                    return null;
                }
            }
            throw new ServiceProcessClientFault(ExceptionEnum.APPLICATION_ERROR.toString(),
                    "Unsupported parent title property type: " + ptProp.getClass().getName(), null, null);
        }
        try {
            final Entity newParent;
            if (parentPidStr != null && parentPidStr.length() > 0) {
                final Pid pid = new Pid(getArte(), destEntId, parentPidStr);                
                final DdsTableDef table = pid.getTable();
                if (table.getExtOptions().contains(EDdsTableExtOption.ENABLE_APPLICATION_CLASSES)){
                    final String classIdStr = classId==null ? null : classId.toString();
                    newParent = getArte().getEntityObject(pid, classIdStr);
                }else{
                    newParent = getArte().getEntityObject(pid);
                }
            } else {
                newParent = null;
            }
            final Collection<Id> readonlyFkPropIds = new ArrayList<>(5);
            if (ptRef != null
                    && readonlyPropModificationPolicy == EReadonlyPropModificationPolicy.FORBIDDEN) {
                //Assert that readonly foreign key property will not be modified by setting new parent
                Object newVal, oldVal;
                for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
                    final Id refPropPresPropId;
                    final Entity destEntity;
                    if (ptProp instanceof RadDetailPropDef) {
                        final RadDetailPropDef dacDetRefProp = (RadDetailPropDef) ptProp;
                        final DdsReferenceDef mdRef = dacDetRefProp.getDetailReference();
                        refPropPresPropId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, entity.getRawEntity().getRadMeta(),
                                refProp.getChildColumnId());
                        destEntity = entity.getRawEntity().getDetailRef(mdRef);
                    } else {
                        refPropPresPropId = refProp.getChildColumnId();
                        destEntity = entity.getRawEntity();
                    }
                    oldVal = destEntity.getProp(refProp.getChildColumnId());
                    if (newParent != null) {
                        Id parentPropId = refProp.getParentColumnId();
                        final DefManager defManager = getArte().getDefManager();
                        final DdsReferenceDef mdRef = defManager.getMasterReferenceDef(defManager.getTableDef(ptRef.getParentTableId()));
                        if (mdRef != null) {
                            parentPropId = defManager.getMasterPropIdByDetailPropId(mdRef, newParent.getRadMeta(), parentPropId);
                        }
                        newVal = newParent.getProp(parentPropId);
                    } else {
                        newVal = null;
                    }
                    //newVal = newParent.getProp(refProp.getParentColumnId());
                    if (!Utils.equals(oldVal, newVal)) {
                        if (refPropPresPropId != null && isPropReadonly(entity.getRawEntity(), edPres,
                                refPropPresPropId)) {
                            if (newParent == null) {
                                readonlyFkPropIds.add(refPropPresPropId);
                            } else {
                                final RadPropDef prop = entity.getRawEntity().getRadMeta().getPropById(refPropPresPropId);
                                throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_FOREIGN_KEY_PROP, "\"" + entity.getRawEntity().getRadMeta().getPropById(
                                        prop.getId()).getName() + "\" (#" + prop.getId() + ")");
                            }
                        }
                    }
                }
            }
            try {
                for (Id id : readonlyFkPropIds) {
                    entity.getRawEntity().setPersistenPropIsReadonly(id, true);
                }
                entity.setProp(ptProp.getId(), newParent);
            } finally {
                for (Id id : readonlyFkPropIds) {
                    entity.getRawEntity().setPersistenPropIsReadonly(id, false);
                }
            }

//			if (parentPidStr != null && parentPidStr.length() > 0) {
//				final Entity newParent = getArte().getEntityObject(
//						new Pid(getArte(), destEntId, parentPidStr));
//				if (ptRef != null && entity.getRawEntity().isInDatabase(false)) {
//					//Assert that reaonly foreign key property will not be modified by setting new parent
//					Object newVal, oldVal;
//					for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
//						final Id refPropPresPropId;
//						final Entity destEntity;
//						if (ptProp instanceof RadDetailPropDef) {
//							final RadDetailPropDef dacDetRefProp = (RadDetailPropDef) ptProp;
//							final DdsReferenceDef mdRef = dacDetRefProp.getDetailReference();
//							refPropPresPropId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, entity.getRawEntity().getRadMeta(),
//									refProp.getChildColumnId());
//							destEntity = entity.getRawEntity().getDetailRef(mdRef);
//						} else {
//							refPropPresPropId = refProp.getChildColumnId();
//							destEntity = entity.getRawEntity();
//						}
//						oldVal = destEntity.getProp(refProp.getChildColumnId());
//						Id parentPropId = refProp.getParentColumnId();
//						final DdsReferenceDef mdRef = getArte().getDefManager().getMasterReferenceDef(ptRef.getParentTable());
//						if (mdRef != null) {
//							parentPropId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, newParent.getRadMeta(), parentPropId);
//						}
//						newVal = newParent.getProp(parentPropId);
//						//newVal = newParent.getProp(refProp.getParentColumnId());
//						if (oldVal == null ? newVal != null : !oldVal.equals(newVal)) {
//							if (refPropPresPropId != null && isPropReadonly(entity.getRawEntity(), edPres,
//									refPropPresPropId)) {
//								final RadPropDef prop = entity.getRawEntity().getRadMeta().getPropById(refPropPresPropId);
//								throw EasFaults.newAccessViolationFault(getArte(), Messages.MLS_ID_INSUF_PRIV_TO_UPDATE_FOREIGN_KEY_PROP, "\"" + entity.getRawEntity().getRadMeta().getPropById(
//										prop.getId()).getName() + "\" (#" + prop.getId() + ")");
//							}
//						}
//					}
//				}
//				entity.setProp(ptProp.getId(), newParent);
//			} else {
//				if (ptRef != null) {// ???? ???
//					boolean isFkCleared = false;
//					if (ptProp instanceof RadDetailPropDef) {
//						final RadDetailPropDef dacDetRefProp = (RadDetailPropDef) ptProp;
//						final DdsReferenceDef mdRef = dacDetRefProp.getDetailReference();
//						for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
//							final Object oldVal = entity.getRawEntity().getDetailRef(mdRef).getProp(refProp.getChildColumnId());
//							if (oldVal == null) {
//								isFkCleared = true;
//							} else {
//								final Id refPropPresPropId = getArte().getDefManager().getMasterPropIdByDetailPropId(mdRef, entity.getRawEntity().getRadMeta(),
//										refProp.getChildColumnId());
//								if (refPropPresPropId == null || !isPropReadonly(entity.getRawEntity(), edPres, refPropPresPropId)) {
//									entity.getRawEntity().getDetailRef(mdRef).setProp(
//											refProp.getChildColumnId(), null);
//									isFkCleared = true;
//								}
//							}
//						}
//					} else {
//						for (DdsReferenceDef.ColumnsInfoItem refProp : ptRef.getColumnsInfo()) {
//							final Object oldVal = entity.getProp(refProp.getChildColumnId());
//							if (oldVal == null) {
//								isFkCleared = true;
//							} else if (!isPropReadonly(entity.getRawEntity(), edPres,
//									refProp.getChildColumnId())) {
//								entity.setProp(refProp.getChildColumnId(), null);
//								isFkCleared = true;
//							}
//						}
//					}
//					if (!isFkCleared) {
//						throw new ServiceProcessClientFault(
//								ExceptionEnum.APPLICATION_ERROR.toString(),
//								"Can't set parent reference '" + ptProp.getName() + "' to NULL.\nForeign key properties are readonly.",
//								null, null);
//					}
//				} else {
//					entity.setProp(ptProp.getId(), null);
//				}
//			}
        } catch (Throwable e) {
            throw EasFaults.exception2Fault(getArte(), e, "Can't set parent");
        }
        return ptRef;
    }

    protected final boolean isPropReadonly(final Entity entity, final RadPresentationDef pres, final Id propId) {
        final EEditPossibility fkColEdPsblty = pres.getPropEditPossibilityByPropId(entity.getPresentationMeta(), propId);
        return (fkColEdPsblty == EEditPossibility.NEVER) || (entity.isInDatabase(false) ? fkColEdPsblty == EEditPossibility.ON_CREATE
                : fkColEdPsblty == EEditPossibility.ONLY_EXISTING);
    }

    protected final int writeDisbaledObjActions(final Actions disActions, final PresentationEntityAdapter presEntAdapter, final RadPresentationDef pres, final PresentationOptions presOptions, final EntityGroup entGrp) throws InterruptedException, AppException {
        int cnt = 0;
        final Restrictions presRestr = pres.getDefRestrictions();
        final Restrictions restr;
        final Entity entity = presEntAdapter.getRawEntity();
        if (pres instanceof RadEditorPresentationDef) {
            final Restrictions additionalRestrictions = presEntAdapter.getAdditionalRestrictions((RadEditorPresentationDef) pres);
            if (additionalRestrictions==Restrictions.ZERO){
                restr = ((RadEditorPresentationDef) pres).getTotalRestrictions(entity);
            }else{
                restr = Restrictions.Factory.sum( ((RadEditorPresentationDef) pres).getTotalRestrictions(entity), additionalRestrictions);
            }
        } else {
            restr = presOptions.getSelRestrictions(entGrp);
        }

        for (RadCommandDef cmd : entity.getRadMeta().getPresentation().getCommands()) {
            if ((cmd.scope == ECommandScope.OBJECT || cmd.scope == ECommandScope.PROPERTY)
                    && !presRestr.getIsCommandRestricted(cmd) && // enabled in presentation
                    (restr.getIsCommandRestricted(cmd) || // but disabled by access control system
                    presEntAdapter.isCommandDisabled(cmd.getId())) // or by user handler
                    ) {
                final Actions.Item it = disActions.addNewItem();
                it.setType(ActionTypeEnum.COMMAND);
                it.setId(cmd.getId());
                cnt++;
            }
        }
        cnt += writeCommonDisActions(presRestr, restr, disActions);
        return cnt;
    }

    protected final int writeDisbaledGrpActions(final Actions disActions, final EntityGroup entGrp, final PresentationOptions presOptions) throws InterruptedException, AppException {
        int cnt = 0;
        final Restrictions restr = presOptions.getSelRestrictions(entGrp);
        final Restrictions presRestr = presOptions.selectorPresentation.getDefRestrictions();
        if (entGrp.getRadMeta().getPresentation() != null) {
            for (RadCommandDef cmd : entGrp.getRadMeta().getPresentation().getCommands()) {
                if ((cmd.scope == ECommandScope.GROUP) && // is group command
                        !presRestr.getIsCommandRestricted(cmd) && // enabled in presentation
                        (restr.getIsCommandRestricted(cmd) || // but disabled by access control system
                        entGrp.isCommandDisabled(cmd.getId())) // or disable by handler
                        ) {
                    final Actions.Item it = disActions.addNewItem();
                    it.setType(ActionTypeEnum.COMMAND);
                    it.setId(cmd.getId());
                    cnt++;
                }
            }
        }
        if (!presRestr.getIsDeleteAllRestricted() && restr.getIsDeleteAllRestricted()) {
            //enabled in presentation but disabled by access control system
            disActions.addNewItem().setType(ActionTypeEnum.DELETE_ALL);
            cnt++;
        }

        cnt += writeCommonDisActions(presRestr, restr, disActions);
        return cnt;
    }
    private static final Class[] formHndlConstrParamTypes = new Class[]{FormHandler.class};

    private FormHandler getFormHandler(final Id formId, final FormHandler prevFormHandler) {
        final Id fhId = Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_FORM_HANDLER_CLASS.getValue() + formId.toString().substring(3));
        try {
            return (FormHandler) getArte().getDefManager().newClassInstance(fhId, new Object[]{prevFormHandler});
        } catch (DefinitionNotFoundError e) {
            final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.DEFINITION_NOT_FOUND.toString(), "Handler for the #" + formId + " form is not defined ", e, preprocessedExStack);
        }
    }

    protected final FormHandler getFormHandler(final Form formXml, final boolean loadProps) throws InterruptedException {
        final FormHandler prevForm = formXml.isSetPrevForm() ? getFormHandler(formXml.getPrevForm(), true) : null;
        final FormHandler form = getFormHandler(formXml.getId(), prevForm);
        if (loadProps && formXml.isSetProperties()) {
            final PropValHandlersByIdMap propValsById = new PropValHandlersByIdMap();
            writeCurData2Map(form.getRadMeta(), form.getRadMeta().getProps(), formXml.getProperties(), propValsById, NULL_PROP_LOAD_FILTER);
            for (Map.Entry<Id, PropValHandler> propVal : propValsById.entrySet()) {
                final Id propId = propVal.getKey();
                final RadPropDef prop = form.getRadMeta().getPropById(propId);
                if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                    form.setProp(propId, propVal.getValue().getValue());
                }
            }
        }
        return form;
    }

    protected final Report getReport(final org.radixware.schemas.eas.Report reportXml, final boolean loadProps) throws InterruptedException {
        final Id reportId = reportXml.getId();
        final Report report;
        try {
            if (reportId != null && reportId.getPrefix() == EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                try {
                    report = (Report) getArte().getDefManager().getClass(reportId).newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new DefinitionNotFoundError(reportId, ex);
                }
            } else {
                report = (Report) getArte().getDefManager().newClassInstance(reportId, new Object[]{});
            }
        } catch (DefinitionNotFoundError e) {
            final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.DEFINITION_NOT_FOUND.toString(), "Report #" + reportId + " was not found ", e, preprocessedExStack);
        }

        if (loadProps && reportXml.isSetProperties()) {
            final PropValHandlersByIdMap propValsById = new PropValHandlersByIdMap();
            writeCurData2Map(report.getRadMeta(), report.getRadMeta().getProps(), reportXml.getProperties(), propValsById, NULL_PROP_LOAD_FILTER);
            for (Map.Entry<Id, PropValHandler> propVal : propValsById.entrySet()) {
                final Id propId = propVal.getKey();
                final RadPropDef prop = report.getRadMeta().getPropById(propId);
                if (prop.getAccessor() instanceof IRadPropWriteAccessor) {
                    report.setProp(propId, propVal.getValue().getValue());
                }
            }
        }
        return report;
    }

    protected void writeTo(final FormHandler.NextDialogsRequest dlg, final NextDialogRequest nextDialogXml) {
        if (dlg.messageBox != null) {
            final MessageBox mbXml = nextDialogXml.addNewMessageBox();
            if (dlg.messageBox.type != null) {
                mbXml.setType(dlg.messageBox.type);
            }
            mbXml.setText(dlg.messageBox.text);
            mbXml.setHtmlText(dlg.messageBox.htmlText);
            if (dlg.messageBox.cancelButtonType != null) {
                mbXml.setCancelButtonType(dlg.messageBox.cancelButtonType);
            }
            if (dlg.messageBox.continueButtonType != null) {
                mbXml.setContinueButtonType(dlg.messageBox.continueButtonType);
            }
        }
        if (dlg.form != null) {
            write(dlg.form, nextDialogXml.addNewForm());
        }
    }

    private final void write(final FormHandler form, final Form formXml) {
        final RadClassDef def = form.getRadMeta();
        formXml.setId(form.getRadMeta().getId());
        final Collection<RadPropDef> props = def.getProps();
        if (props.isEmpty()) {
            return;
        }
        final PropertyList propsXml = formXml.addNewProperties();
        PropertyList.Item propXml;
        for (RadPropDef prop : props) {
            final RadPropDef propDef = prop;
            final RadPropertyPresentationDef propPres = def.getPresentation().getPropPresById(prop.getId());
            if (propPres == null) {
                continue;
            }
            propXml = propsXml.addNewItem();
            propXml.setId(prop.getId());
            ParentInfo ptParentInfo = null;
            Object val = null;
            if (prop.getValType() == EValType.PARENT_REF || prop.getValType() == EValType.OBJECT) {
                try {
                    final Entity parent = (Entity) form.getProp(prop.getId());
                    ptParentInfo = getParentInfo(form, prop, parent, null);
                    val = parent;
                } catch (EntityObjectNotExistsError e) {
                    ptParentInfo = getParentInfo(form, prop, e, null);
                } catch (WrongPidFormatError error) {
                    ptParentInfo = getParentInfo(form, prop, new EntityObjectNotExistsError(error), null);
                }
            } else {
                val = form.getProp(prop.getId());
            }
            if (prop.getValType() == EValType.ARR_REF) {
                EasValueConverter.objVal2EasPropXmlVal(val, null, prop.getValType(), propXml, new EasValueConverter.IParentInfoProducer() {
                    @Override
                    public ParentInfo getParentInfo(final Entity value) {
                        return SessionRequest.this.getParentInfo(form, propDef, value, null);
                    }
                });
            }else{
                EasValueConverter.objVal2EasPropXmlVal(val, ptParentInfo, prop.getValType(), propXml);
            }
        }
    }

    /**
     *
     * @param arte
     * @param context
     * @param allUserApplicableRoleIds - if not null should be an singleton
     * array for caching getAllUserApplicableRoleIds(arte) results
     * @return
     */
    final List<Id> getContextCurUserApplicableRoleIds(final Context context) {
        final List<Id> rolesByCondition;
        if (context == null || !context.getContextProperties().hasAccessAreas(getArte(), context.getClassDef())) {
            rolesByCondition = null;
        } else {
            rolesByCondition = getCurUserRoleIds(context.getContextProperties(), context.getClassDef());
        }
        boolean bUseContextRestrictions = false;
        if (context instanceof EdPresExplrItemContext) {
            final EdPresExplrItemContext treeContext = (EdPresExplrItemContext) context;
            if (treeContext.getContextReferenceRole() == Context.EContextRefRole.CHILDREN_SCOPE) {
                final DdsReferenceDef ref = treeContext.getContextReference();
                if (ref != null) {
                    final RadClassDef childClassDef = getArte().getDefManager().getClassDef(RadClassDef.getEntityClassIdByTableId(ref.getChildTableId()));
                    bUseContextRestrictions = childClassDef.getAccessAreaType() == EAccessAreaType.INHERITED && (childClassDef.getAccessAreaInheritRef() == ref) && childClassDef.hasPartitionRights() && !childClassDef.hasOwnAccessAreas();
                }
            }
        }
        if (bUseContextRestrictions) {
            if (rolesByCondition == null) {
                return getArte().getEntityObject(context.getContextObjectPid()).getCurUserApplicableRoleIds();
            } else {
                final List<Id> contextRoleIds
                        = new LinkedList<>(getArte().getEntityObject(context.getContextObjectPid()).getCurUserApplicableRoleIds());
                contextRoleIds.retainAll(rolesByCondition);
                return Collections.unmodifiableList(contextRoleIds);
            }
        } else {
            return rolesByCondition == null ? getCurUserAllRolesInAllAreas() : rolesByCondition;
        }
    }

    protected void writeAccessibleExplorerItems(final Entity entity, final RadEditorPresentationDef presentation, final Restrictions additionalRestrictions, final ExplorerItemList xItems) throws ServiceProcessClientFault, InterruptedException {
        final Id presentationId = presentation.getId();
        final Restrictions presentationRestrictions = presentation.getTotalRestrictions(entity);
        final Restrictions totalRestrictions;
        if (additionalRestrictions==Restrictions.ZERO){
            totalRestrictions = presentationRestrictions;
        }else{
            totalRestrictions = Restrictions.Factory.sum(presentationRestrictions,additionalRestrictions);
        }
        final Map<RadExplorerItemDef, Boolean> processedParags = new HashMap<>();
        for (RadExplorerItemDef ei : presentation.getChildren()) {
            processEi(entity, presentationId, totalRestrictions, ei, processedParags, xItems);
        }
    }

    private boolean processEi(final Entity entity, final Id presId, final Restrictions presRestr, final RadExplorerItemDef ei, final Map<RadExplorerItemDef, Boolean> processedParags, final ExplorerItemList xItems) throws ServiceProcessClientFault, InterruptedException {
        try {
            if (ei instanceof RadParagraphExplorerItemDef) {
                if (processedParags.containsKey(ei)) {
                    return processedParags.get(ei);
                }
                processedParags.put(ei, true);
                boolean res = false;
                for (RadExplorerItemDef child : ((RadParagraphExplorerItemDef) ei).getChildren()) {
                    if (processEi(entity, presId, presRestr, child, processedParags, xItems)) {
                        res = true;
                    }
                }
                if (res || !presRestr.getIsChildRestricted(ei.getId())) {//children are visible or pargraph itself is not restricted
                    xItems.addNewItem().setId(ei.getId());
                } else {
                    processedParags.put(ei, false);
                }
                return res;
            }
            if (presRestr.getIsChildRestricted(ei.getId())) {
                return false;//RADIX-3315
            }
            if (ei instanceof RadParentRefExplorerItemDef) {
                final RadParentRefExplorerItemDef parentEi = (RadParentRefExplorerItemDef) ei;
                try {
                    final Entity parent = entity.getParentRef(parentEi.getParentReferenceId());
                    if (parent == null) {
                        return false;
                    }
                    final PresentationEntityAdapter presAdapter = getArte().getPresentationAdapter(parent);
                    final RadEditorPresentationDef parentPres = getActualEditorPresentation(presAdapter, parentEi.getParentPresentations(), false);
                    if (parentPres == null) {
                        return false;
                    }
                    final ExplorerItemList.Item xItem = xItems.addNewItem();
                    xItem.setId(ei.getId());
                    final ExplorerItemList.Item.Object parentXml = xItem.addNewObject();
                    try {
                        parentXml.setTitle(parent.calcTitle(parentPres.getObjectTitleFormat()));
                    } catch (Throwable e) {
                        if (e instanceof InterruptedException || Thread.currentThread().isInterrupted()) {
                            throw new InterruptedException();
                        }
                        getArte().getTrace().put(EEventSeverity.WARNING, "Exception on parent's title calculation: " + getArte().getTrace().exceptionStackToString(e), EEventSource.EAS);
                        parentXml.setTitle("Err: \"" + ExceptionTextFormatter.getExceptionMess(e) + "\"");
                    }
                    final Presentation presXml = parentXml.addNewPresentation();
                    presXml.setId(parentPres.getId());
                    presXml.setClassId(parent.getPresentationMeta().getEditorPresentationById(parentPres.getId()).getClassPresentation().getClassId());
                    parentXml.setPID(parent.getPid().toString());
                    return true;
                } catch (EntityObjectNotExistsError e) {
                    return false;
                }
            } else if (ei instanceof RadSelectorExplorerItemDef) {
                final RadSelectorExplorerItemDef selExplItem = (RadSelectorExplorerItemDef) ei;
                final RadSelectorPresentationDef selPres = selExplItem.getSelectorPresentation();
                final EdPresExplrItemContext context = new EdPresExplrItemContext(
                        this, entity.getRadMeta().getId(), presId, ei.getId(), entity.getPid(), null);
                final List<Id> applicableRoleIds = getContextCurUserApplicableRoleIds(context);
                final RadClassDef selClassDef = selExplItem.getSelectionClassDef();
                final EntityGroup group = getArte().getGroupHander(selClassDef.getEntityId());
                group.set(context.buildEntGroupContext(), selPres, null, null, null, null, null);
                if (!selPres.getTotalRestrictions(applicableRoleIds).getIsAccessRestricted()
                    && !group.getAdditionalRestrictions(selPres, applicableRoleIds).getIsAccessRestricted()) {
                    xItems.addNewItem().setId(ei.getId());
                    return true;
                }
            }
            return false;
        } catch (DefinitionNotFoundError error) {//RADIX-6254
            final String message = "Ignoring unlinkable explorer item #%s:\n%s";
            getArte().getTrace().put(EEventSeverity.DEBUG, String.format(message, ei.getId().toString(), getArte().getTrace().exceptionStackToString(error)), EEventSource.EAS);
            return false;
        }
    }

}

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

package org.radixware.kernel.server.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.trace.RadixTraceOptions;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.release.EntityUserReference;
import org.radixware.kernel.server.arte.AdsIndices;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.DefManager;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.exceptions.DeleteCascadeConfirmationRequiredException;
import org.radixware.kernel.server.exceptions.DeleteCascadeRestrictedException;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.presentations.RadClassPresentationDef;
import org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef;
import org.radixware.kernel.server.trace.TraceTarget;
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.kernel.server.types.presctx.UnknownPresentationContext;


public class PresentationEntityAdapter<T extends Entity> {
    
    private final static EnumSet<ERestriction> VIEW_ONLY_RESTRICTIONS =
            EnumSet.of(ERestriction.CREATE, ERestriction.UPDATE, ERestriction.DELETE, ERestriction.ANY_COMMAND);    
    private final static List<ERestriction> EDITOR_RESTRICTIONS = 
                Arrays.asList(ERestriction.VIEW, ERestriction.CREATE, ERestriction.UPDATE, ERestriction.DELETE, ERestriction.ANY_COMMAND);

    public static final class EditorPresentationCandidate {

        private final RadEditorPresentationDef replacedPres;
        private final RadEditorPresentationDef pres;
        private final Restrictions totalRestrictions;

        public EditorPresentationCandidate(final RadEditorPresentationDef replacedPres, final RadEditorPresentationDef edPres, final Restrictions totalRestrictions) {
            this.replacedPres = replacedPres;
            this.pres = edPres;
            this.totalRestrictions = totalRestrictions;
        }

        public RadEditorPresentationDef getReplacedPresentation() {
            return replacedPres;
        }

        public RadEditorPresentationDef getPresentation() {
            return pres;
        }

        public Restrictions getTotalRestrictions() {
            return totalRestrictions;
        }

        public boolean isViewOnly() {
            return PresentationEntityAdapter.isViewOnly(totalRestrictions);
        }        
    }
    
    private static final class PresCalculationLogger{
        
        private final static EEventSeverity MESSAGE_SEVERITY = EEventSeverity.DEBUG;
        private final static String SECTION_SPACE="  ";
        private final static Id MLS_OWNER_ID = Id.Factory.loadFrom("adcXCB5KK6HMJH7NP6E642OHPOMXY");
        
        private final Arte arte;
        private final StringBuilder logMessageBuilder;
        private final String className;
        private final Id classId;
        private final Pid entityPid;
        private boolean newLine;
        private int sectionNumber;        
        
        public PresCalculationLogger(final Entity entity){
            arte = entity.getArte();
            className = entity.getRadMeta().getQualifiedName();
            classId = entity.getRadMeta().getId();
            entityPid = entity.getPid();
            boolean logEnabled = false;
            final List<TraceTarget> traceTargets = arte.getTrace().getTargets();
            TraceProfile traceProfile;
            for (TraceTarget traceTarget: traceTargets){
                traceProfile = traceTarget.getProfile();
                if (!traceProfile.getUseInheritedSeverity(EEventSource.EAS.getValue())
                    && traceProfile.getEventSourceSeverity(EEventSource.EAS.getValue())==MESSAGE_SEVERITY
                    && traceTarget.getProfile().hasOption(EEventSource.EAS.getValue(), RadixTraceOptions.LOG_PRES_CALC)){
                    logEnabled = true;
                    break;
                }
            }
            logMessageBuilder = logEnabled ? new StringBuilder(128) : null;
        }
        
        public void enterSection(){
            sectionNumber++;
        }
        
        public void leaveSection(){
            if (sectionNumber>0){
                sectionNumber--;                
            }
            endLine();
        }
                
        private void checkNewLine(){
            if (newLine){
                logMessageBuilder.append('\n');
                for (int i=0; i<sectionNumber; i++){
                    logMessageBuilder.append(SECTION_SPACE);
                }
                newLine=false;                
            }
        }
        
        public void append(final Id messageId, final String ... args){
            if (logMessageBuilder!=null){
                checkNewLine();
                final String messageTemplate = MultilingualString.get(arte, MLS_OWNER_ID, messageId);
                logMessageBuilder.append(String.format(messageTemplate, (Object[]) args));
            }
        }
        
        public void append(final char c){
            if (logMessageBuilder!=null){
                checkNewLine();
                logMessageBuilder.append(c);
            }
        }
        
        public void endLine(){
            newLine = true;
        }
        
        public void append(final RadEditorPresentationDef presentation){
            if (logMessageBuilder!=null){
                checkNewLine();
                logMessageBuilder.append(presentation.getClassPresentation().getName());
                logMessageBuilder.append("::");
                logMessageBuilder.append(presentation.getName());
                logMessageBuilder.append(" (#");
                logMessageBuilder.append(presentation.getId().toString());
                logMessageBuilder.append(')');
            }            
        }
        
        public void flush(){
            if (logMessageBuilder!=null){
                arte.getTrace().put(Messages.MLS_ID_EAS_ED_PRES_CALCULATION, Arrays.asList(className, classId.toString(), entityPid.toString(), logMessageBuilder.toString()));
            }
        }
    }
    
    private Entity entity;
    private PresentationContext context;
    private PresCalculationLogger presCalcLogger;

    public PresentationEntityAdapter(final Entity entity) {
        this.entity = entity;
    }
    
    public void setPresentationContext(final PresentationContext context){
        this.context = context;
    }
    
    public final PresentationContext getPresentationContext(){
        return context==null ? UnknownPresentationContext.INSTANCE : context;
    }

    public void setProp(final Id id, final Object val) {
        entity.setProp(id, val);
    }

    public Object getProp(final Id id) {
        return entity.getProp(id);
    }

    public void setPropHasOwnVal(final Id id, final boolean hasVal) {
        entity.setPropHasOwnVal(id, hasVal);
    }
    
    public void createAsUserPropertyValue(final PresentationEntityAdapter ownerEntityAdapter, final Id propertyId){
        ownerEntityAdapter.setProp(propertyId, entity);
        ownerEntityAdapter.update();
    }

    protected void doUpdate() {
        entity.update();
    }

    public final void update() {
        doUpdate();
    }        

    public static Collection<RadEditorPresentationDef> getActualEditorPresentations(Entity entity) {
        PresentationEntityAdapter adapter = new PresentationEntityAdapter(entity);
        return adapter.doGetActualEditorPresentations(entity, null);
    }

    public static Collection<RadEditorPresentationDef> getActualEditorPresentations(Entity entity, Collection<Id> allowedPresentations) {
        PresentationEntityAdapter adapter = new PresentationEntityAdapter(entity);
        return adapter.doGetActualEditorPresentations(entity, allowedPresentations);
    }

    protected Collection<RadEditorPresentationDef> doGetActualEditorPresentations(final Entity entity, final Collection<Id> allowedPresentations) {
        final PresCalculationLogger logger = new PresCalculationLogger(entity);
        List<RadEditorPresentationDef> result = new LinkedList<>();

        List<RadEditorPresentationDef> desiredPresentations;        
        
        if (allowedPresentations == null) {            
            logger.append(Messages.MLS_ID_EAS_USING_ALL_ED_PRES);
            logger.endLine();
            desiredPresentations = entity.getRadMeta().getPresentation().getEditorPresentations();
        } else if (allowedPresentations.isEmpty()) {
            desiredPresentations = Collections.emptyList();
        }else{            
            logger.append(Messages.MLS_ID_EAS_DESIRED_ED_PRESS);
            desiredPresentations = new LinkedList<>();
            for (Id id : allowedPresentations) {
                logger.endLine();
                try {
                    RadEditorPresentationDef epr = entity.getRadMeta().getPresentation().getEditorPresentationById(id);
                    desiredPresentations.add(epr);
                    logger.append(epr);
                } catch (DefinitionNotFoundError e) {                    
                    logger.append(Messages.MLS_ID_EAS_PRES_NOT_FOUND, id.toString());
                }
            }
        }
        logger.endLine();
        if (desiredPresentations.isEmpty()) {
            logger.append(Messages.MLS_ID_EAS_NO_ED_PRES);
            logger.flush();
            return Collections.emptySet();
        }

        final AccessibleEdPresIterator iterator = new AccessibleEdPresIterator(desiredPresentations.iterator(), logger);
        final ERuntimeEnvironmentType clientEnvironment = entity.getArte().getClientEnvironment();
        logger.enterSection();
        while (iterator.hasNext()) {
            RadEditorPresentationDef candidate = iterator.next().getPresentation();
            if (candidate.hasCompatibleClientRuntimeEnvironmentType(clientEnvironment)) {
                result.add(candidate);
            }else{                
                logger.append(Messages.MLS_ID_EAS_PRES_INCOMPATIBLE, pres2Str(candidate));
                logger.endLine();
            }
        }
        logger.leaveSection();
        if (result.isEmpty()){
            logger.append(Messages.MLS_ID_EAS_EMPTY_RESULT);
        }else{
            logger.append(Messages.MLS_ID_EAS_RESULT_ED_PRES_LIST);
            for (RadEditorPresentationDef presentation: result){
                logger.endLine();
                logger.append(presentation);
            }
        }
        logger.flush();        
        return result;
    }
    
    public RadEditorPresentationDef doSelectEditorPresentation(final Iterator<PresentationEntityAdapter.EditorPresentationCandidate> accessiblePresIter) {
        final PresCalculationLogger logger = presCalcLogger==null ? new PresCalculationLogger(entity) : presCalcLogger;
        final RadEditorPresentationDef presentation = doSelectEditorPresentation(accessiblePresIter, logger);
        logger.flush();
        return presentation;
    }    

    private RadEditorPresentationDef doSelectEditorPresentation(final Iterator<PresentationEntityAdapter.EditorPresentationCandidate> accessiblePresIter,
                                                                                                final PresCalculationLogger logger) {
        //see RADIX-6054 and RADIX-8169
        final ERuntimeEnvironmentType clientEnvironment = entity.getArte().getClientEnvironment();
        EditorPresentationCandidate currentCandidate;
        RadEditorPresentationDef selectedPresentation = null;
        int selectedCandidateScore = -1, currentCandidateScore;
        logger.enterSection();
        while (accessiblePresIter.hasNext()) {
            currentCandidate = accessiblePresIter.next();
            currentCandidateScore = 
                calcScoreForEditorPresentationCandidate(currentCandidate, clientEnvironment, logger);
            if (currentCandidateScore > 11) {//maximum score
                selectedPresentation = currentCandidate.getPresentation();
                break;
            }
            if (currentCandidateScore > selectedCandidateScore) {
                selectedCandidateScore = currentCandidateScore;
                selectedPresentation = currentCandidate.getPresentation();
            }
        }
        logger.leaveSection();
        if (selectedPresentation==null){
            logger.append(Messages.MLS_ID_EAS_ED_PRES_NOT_CHOSEN);
        }else{
            logger.append(Messages.MLS_ID_EAS_CHOSEN_ED_PRES, pres2Str(selectedPresentation));
        }
        logger.endLine();
        return selectedPresentation;
    }

    private int calcScoreForEditorPresentationCandidate(final EditorPresentationCandidate candidate, 
                                                                                           final ERuntimeEnvironmentType desiredEnvironmentType,
                                                                                           final PresCalculationLogger logger) {        
        int score;
        final Restrictions addintionalRestrictions = getAdditionalRestrictions(candidate.getPresentation());
        if (candidate.getTotalRestrictions().getIsViewRestricted()
            || addintionalRestrictions.getIsViewRestricted()) {
            score = 0;
            final Id messageId;
            if (addintionalRestrictions.getIsViewRestricted()){
                messageId = Messages.MLS_ID_EAS_CANT_VIEW_PRES_ADD;
            }else{
                messageId = Messages.MLS_ID_EAS_CANT_VIEW_PRES;
            }
            logger.append(messageId, pres2Str(candidate.getPresentation()));
        } else if (candidate.isViewOnly()
                     || isViewOnly(addintionalRestrictions)) {
            score = 1;            
            final Id messageId;
            if (isViewOnly(addintionalRestrictions)){
                messageId = Messages.MLS_ID_EAS_CANT_MODIFY_PRES_ADD;
            }else{
                messageId = Messages.MLS_ID_EAS_CANT_MODIFY_PRES;
            }
            logger.append(messageId, pres2Str(candidate.getPresentation()));
        } else {
            final Restrictions totalRestrictions;
            if (addintionalRestrictions==Restrictions.ZERO){
                totalRestrictions = candidate.getTotalRestrictions();
            }else{
                totalRestrictions = Restrictions.Factory.sum(candidate.getTotalRestrictions(), addintionalRestrictions);
            }
            {                
                final String actions = printActions(totalRestrictions, true);
                logger.append(Messages.MLS_ID_EAS_PRES_ALLOWED_ACTIONS, pres2Str(candidate.getPresentation()), actions);
            }
            if (addintionalRestrictions!=Restrictions.ZERO){
                logger.endLine();
                final String actions = printActions(addintionalRestrictions, false);
                logger.append(Messages.MLS_ID_EAS_PRES_RESTRICTED_ACTIONS, actions);
            }
            score = 2;
        }
        if (candidate.getPresentation().hasCompatibleClientRuntimeEnvironmentType(desiredEnvironmentType)) {
            score += 10;
        }else{
            logger.append(' ');
            logger.append(Messages.MLS_ID_EAS_PRES_INCOMPATIBLE, pres2Str(candidate.getPresentation()));
        }
        logger.endLine();
        return score;
    }
    
    private static boolean isViewOnly(final Restrictions r){
        final EnumSet<ERestriction> restrictions = ERestriction.fromBitField(r.getBitMask());
        if (restrictions.containsAll(VIEW_ONLY_RESTRICTIONS)) {
            return r.getEnabledCommandIds() == null || r.getEnabledCommandIds().isEmpty();
        } else {
            return false;
        }        
    }
        
    private static String printActions(final Restrictions r, final boolean printAllowed){
        final StringBuilder strBuilder = new StringBuilder(32);
        boolean firstAction = true;
        final EnumSet<ERestriction> restrictions = ERestriction.fromBitField(r.getBitMask());
        boolean isActionAllowed;
        for (ERestriction restriction: EDITOR_RESTRICTIONS){
            if (!restrictions.contains(restriction)) {
                isActionAllowed = true;
            } else if (restriction==ERestriction.ANY_COMMAND) {
                isActionAllowed = r.getEnabledCommandIds() != null && !r.getEnabledCommandIds().isEmpty();
            }else{
                isActionAllowed = false;
            }
            if (isActionAllowed==printAllowed){
                if (firstAction){
                    firstAction = false;
                }else{
                    strBuilder.append(", ");
                }
                if (restriction==ERestriction.ANY_COMMAND){
                    strBuilder.append("RUN COMMAND");
                }else{
                    strBuilder.append(restriction.name());
                }
            }
        }
        return strBuilder.toString();
    }    

    /**
     *
     * @param bDeleteCascade
     */
    public void doCheckCascadeBeforeDelete(final boolean bDeleteCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        defaultCheckCascadeBeforeDelete(bDeleteCascade);
    }

    protected void doDelete(final boolean bDeleteCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        doCheckCascadeBeforeDelete(bDeleteCascade);
        entity.delete();
    }

    public final void delete(final boolean bDeleteCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        doDelete(bDeleteCascade);
    }

    public boolean isCommandDisabled(final Id cmdId) throws AppException, InterruptedException {
        return false;
    }

    public FormHandler.NextDialogsRequest execCommand(final Id cmdId, final Id propId, final org.apache.xmlbeans.XmlObject input, final PropValHandlersByIdMap newPropValsById, final org.apache.xmlbeans.XmlObject output) throws AppException, InterruptedException {
        return entity.execCommand(cmdId, propId, input, newPropValsById, output);
    }

    protected Entity doCreate(final Entity src) {
        entity.create(src);
        return entity;
    }

    public final Entity create(final Entity src) {
        final Entity entityBak = entity;
        entity = doCreate(src);
        if (entityBak != entity) {
            entityBak.discard();
        }
        return entity;
    }

    protected void doInit(final PropValHandlersByIdMap initialPropValsById, final Entity src, final EEntityInitializationPhase phase) {
        final InitializingEntityController controller = new InitializingEntityController(phase);
        if (src != null) {
            controller.setSrcEntity(src, null);
        }
        if (initialPropValsById != null) {
            controller.setInitialPropVals(initialPropValsById);
        }
        entity.init(controller);
    }

    public final void init(final PropValHandlersByIdMap initialPropValsById, final Entity src, final EEntityInitializationPhase phase) {
        doInit(initialPropValsById, src, phase);
    }

    public final Entity getRawEntity() {
        return entity;
    }

    @SuppressWarnings("unused")
    public boolean beforeUpdatePropObject(final Id propId, final Entity val) {
        return true;
    }

    @SuppressWarnings("unused")
    public void afterUpdatePropObject(final Id propId, final Entity val) {
    }

    public ESelectorRowStyle calcSelectorRowStyle() {
        return ESelectorRowStyle.NORMAL;
    }

    @SuppressWarnings("unchecked")
    public T getEntity() {
        return (T) getRawEntity();
    }

    private void defaultCheckCascadeBeforeDelete(final boolean bCascade) throws DeleteCascadeRestrictedException, DeleteCascadeConfirmationRequiredException {
        final Set<String> subDelEntitiesSet = new HashSet<>();
        final Set<String> subNullEntitiesSet = new HashSet<>();
        defaultCheckCascadeBeforeDelete(entity, bCascade, subDelEntitiesSet, subNullEntitiesSet);
        for (DdsReferenceDef detRef : entity.getRadMeta().getDetailsRefs()) {
            defaultCheckCascadeBeforeDelete(entity.getDetailRef(detRef), bCascade, subDelEntitiesSet, subNullEntitiesSet);
        }
        if (!bCascade && (!subNullEntitiesSet.isEmpty() || !subDelEntitiesSet.isEmpty())) {
            throw new DeleteCascadeConfirmationRequiredException(entity.getArte(), subDelEntitiesSet, subNullEntitiesSet);
        }
    }

    private void defaultCheckCascadeBeforeDelete(final Entity entityToBeChecked, final boolean bCascade, final Set<String> subDelEntitiesSet, final Set<String> subNullEntitiesSet) throws DeleteCascadeRestrictedException {
        checkDatabaseReferences(entityToBeChecked, bCascade, subDelEntitiesSet, subNullEntitiesSet);
        checkUserReferences(entityToBeChecked, bCascade, subNullEntitiesSet);
    }

    private void checkDatabaseReferences(final Entity entityToBeChecked, final boolean bCascade, final Set<String> subDelEntitiesSet, final Set<String> subNullEntitiesSet) throws DeleteCascadeRestrictedException {
        for (DdsReferenceDef childRef : entityToBeChecked.getDdsMeta().collectIncomingReferences()) {
            if ((childRef.getDeleteMode() == EDeleteMode.CASCADE
                    || childRef.getDeleteMode() == EDeleteMode.SET_NULL)
                    && (childRef.isConfirmDelete() && !bCascade)
                    || childRef.getDeleteMode() == EDeleteMode.RESTRICT
                    && childRef.getRestrictCheckMode() != DdsReferenceDef.ERestrictCheckMode.NEVER) {
                Entity childEntity = null;
                boolean childExistsButCouldntBeLoaded = false;
                try {
                    childEntity = entityToBeChecked.getAnyChildEntity(childRef);
                } catch (Entity.ErrorOnEntityCreation ex) {
                    //child object exists, but couldn't be loaded for some reason
                    childExistsButCouldntBeLoaded = true;
                }
                if (childEntity != null || childExistsButCouldntBeLoaded) {
                    final String childTitleForConfirmation = calcChildTitleForConfirmation(childRef.getChildTableId());
                    if (childRef.getDeleteMode() == EDeleteMode.CASCADE) {
                        subDelEntitiesSet.add(childTitleForConfirmation);
                    } else if (childRef.getDeleteMode() == EDeleteMode.SET_NULL) {
                        subNullEntitiesSet.add(childTitleForConfirmation);
                    } else {
                        throwDeletionRestrictedException(childEntity != null ? calcChildTitleForException(childEntity) : childTitleForConfirmation, childRef.getChildTableId());
                    }
                }
            }
        }
    }

    private String calcChildTitleForException(final Entity childEntity) {
        return "\"" + childEntity.calcDescriptiveTitle() + "\" (" + childEntity.getRadMeta().getTitle() + ")";
    }

    public String calcChildTitleForConfirmation(final Id childEntityId) {
        String childTitle;
        final DefManager defManager = entity.getArte().getDefManager();
        DdsTableDef childTable = defManager.getTableDef(childEntityId);
        final DdsReferenceDef mdRef = defManager.getMasterReferenceDef(childTable);
        if (mdRef != null) {
            childTable = defManager.getTableDef(mdRef.getParentTableId());
        }
        try {
            childTitle = "\"" + defManager.getClassDef(RadClassDef.getEntityClassIdByTableId(childTable.getId())).getTitle() + "\"";
        } catch (DefinitionNotFoundError e) {
            childTitle = null;
        }
        if (childTitle == null || childTitle.length() == 0) {
            childTitle = "\"" + childTable.getName() + "\" (#" + childTable.getId() + ")";
        }
        return childTitle;
    }

    private void throwDeletionRestrictedException(final String childTitle, final Id childId) throws DeleteCascadeRestrictedException {
        final String exMess = MessageFormat.format(getMessageString(Messages.MLS_ID_CANT_DELETE_USED_OBJECT), childTitle);
        throw new DeleteCascadeRestrictedException(exMess, childTitle, childId);
    }

    private void checkUserReferences(final Entity entityToBeChecked, final boolean bCascade, final Set<String> modifiedObjectTypes) throws DeleteCascadeRestrictedException {
        final AdsIndices indices = entityToBeChecked.getArte().getDefManager().getReleaseCache().getRelease().getRepository().getAdsIndices();
        Collection<EntityUserReference> references = indices.getEntityUserReferences().get(entityToBeChecked.getEntityId());
        if (references == null || references.isEmpty()) {
            return;
        }
        final UserReferenceObject restrictingReference = findFirstRestrictingEntityId(entityToBeChecked, references);
        if (restrictingReference != null) {
            throwDeletionRestrictedException(calcChildTitleForException(entityToBeChecked.getArte().getEntityObject(new Pid(entityToBeChecked.getArte(), restrictingReference.childEntityId, restrictingReference.pidAsStr))), restrictingReference.getChildEntityId());
        }
        if (bCascade) {//confirmation recieved
            return;
        }
        populateConfirmationList(entityToBeChecked, modifiedObjectTypes, references);
    }

    private void populateConfirmationList(Entity entityToBeChecked, Set<String> modifiedObjectTypes, Collection<EntityUserReference> references) {
        final StringBuilder sb = new StringBuilder();
        final String readParentRefsSql = buildReadUserChilds(entityToBeChecked, EValType.PARENT_REF, references, false, EDeleteMode.SET_NULL, EDeleteMode.REMOVE_VALUE);
        if (readParentRefsSql != null && !readParentRefsSql.isEmpty()) {
            sb.append(readParentRefsSql);
        }
        final String readArrRefsSql = buildReadUserChilds(entityToBeChecked, EValType.ARR_REF, references, false, EDeleteMode.SET_NULL, EDeleteMode.REMOVE_VALUE);
        if (readArrRefsSql != null && !readArrRefsSql.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(" union ");
            }
            sb.append(readArrRefsSql);
        }
        final List<UserReferenceObject> refObjects = readRefObjects(sb.toString(), entityToBeChecked.getArte(), false);
        if (refObjects == null || refObjects.isEmpty()) {
            return;
        }

        for (final UserReferenceObject refObject : refObjects) {
            modifiedObjectTypes.add(calcChildTitleForConfirmation(refObject.getChildEntityId()));
        }
    }

    private UserReferenceObject findFirstRestrictingEntityId(final Entity parentEntity, final Collection<EntityUserReference> references) {
        final UserReferenceObject fromParentRef = findFirstRestrictingEntityId(parentEntity, references, EValType.PARENT_REF);
        if (fromParentRef != null) {
            return fromParentRef;
        }
        return findFirstRestrictingEntityId(parentEntity, references, EValType.ARR_REF);
    }

    private UserReferenceObject findFirstRestrictingEntityId(final Entity parentEntity, final Collection<EntityUserReference> references, EValType refType) {
        final String findRestrictingEntitySql = buildReadUserChilds(parentEntity, refType, references, true, EDeleteMode.RESTRICT);
        if (findRestrictingEntitySql == null || findRestrictingEntitySql.isEmpty()) {
            return null;
        }
        List<UserReferenceObject> objects = readRefObjects(findRestrictingEntitySql, parentEntity.getArte(), true);
        if (objects.isEmpty()) {
            return null;
        }
        return objects.get(0);
    }

    private List<UserReferenceObject> readRefObjects(final String sql, final Arte arte, final boolean pidSelected) {
        if (sql == null || sql.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            final PreparedStatement stmt;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                stmt = arte.getDbConnection().get().prepareStatement(sql);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            try {

                final ResultSet rs;
                arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                try {
                    rs = stmt.executeQuery();
                } finally {
                    arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
                }
                try {
                    final List<UserReferenceObject> objects = new ArrayList<>();
                    while (rs.next()) {
                        objects.add(new UserReferenceObject(Id.Factory.loadFrom(rs.getString(1)), pidSelected ? rs.getString(2) : null));
                    }
                    return objects;
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
            throw new DatabaseError("Unable to check existence of user references to this object", ex);
        }
    }

    private String buildReadUserChilds(final Entity parentEntity, final EValType refType, final Collection<EntityUserReference> references, final boolean onlyFirst, EDeleteMode... deleteModes) {
        final StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (!onlyFirst) {
            sb.append(" distinct ");
        }
        sb.append(" ownerEntityId ");
        if (onlyFirst) {
            sb.append(", ownerPid ");
        }
        sb.append(" from ");
        if (refType == EValType.PARENT_REF) {
            sb.append("rdx_upvalref");
        } else {
            sb.append("rdx_upvalclob");
        }
        sb.append(" where defId in (");
        boolean hasSuchRefs = false;
        final Collection<EDeleteMode> modes = Arrays.asList(deleteModes);
        for (EntityUserReference ref : references) {
            if (modes.contains(ref.getDeleteMode()) && ref.getType() == refType) {
                if (hasSuchRefs) {
                    sb.append(", ");
                } else {
                    hasSuchRefs = true;
                }
                sb.append("'");
                sb.append(ref.getUserPropId().toString());
                sb.append("'");
            }
        }
        if (!hasSuchRefs) {
            return null;
        }
        sb.append(") and ");
        if (refType == EValType.PARENT_REF) {
            sb.append("val=");
            sb.append("'");
            sb.append(maskSingleQuotes(parentEntity.getPid().toString()));
            sb.append("'");
        } else {
            sb.append("rdx_array.searchRef(val, ");
            sb.append("'");
            sb.append(parentEntity.getEntityId());
            sb.append("', '");
            sb.append(maskSingleQuotes(parentEntity.getPid().toString()));
            sb.append("', 1) > 0");
        }
        if (onlyFirst) {
            sb.append(" and ROWNUM < 2");
        }
        return sb.toString();
    }
    
    private static String maskSingleQuotes(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.replace("'", "''");
    }

    public final RadEditorPresentationDef selectEditorPresentation(final Iterable<RadEditorPresentationDef> desiredPresentations) {
        presCalcLogger = new PresCalculationLogger(entity);
        try{
            printDesiredEditorPresentations(desiredPresentations, presCalcLogger);
            return
                doSelectEditorPresentation(new AccessibleEdPresIterator(desiredPresentations.iterator(), presCalcLogger));
        }finally{
            try{
                presCalcLogger.flush();
            }finally{
                presCalcLogger = null;
            }
        }        
    }
    
    public Restrictions getAdditionalRestrictions(final RadEditorPresentationDef presentation){
        getPresentationContext();
        return Restrictions.ZERO;
    }
    
    private static void printDesiredEditorPresentations(final Iterable<RadEditorPresentationDef> presentations, final PresCalculationLogger logger){        
        final Iterator<RadEditorPresentationDef> it = presentations.iterator();
        if (it.hasNext()){
            it.next();
            if (it.hasNext()){
                logger.append(Messages.MLS_ID_EAS_DESIRED_ED_PRESS);
                logger.endLine();
            }else{
                logger.append(Messages.MLS_ID_EAS_DESIRED_ED_PRES);
            }
            boolean firstPresentation = true;
            logger.enterSection();
            for (RadEditorPresentationDef presentation: presentations){
                if (firstPresentation){                
                    firstPresentation = false;
                }else{
                    logger.endLine();
                }
                logger.append(presentation);
            }
            logger.leaveSection();
        }else{
            logger.append(Messages.MLS_ID_EAS_NO_ED_PRES_TO_CHOOSE);
            logger.endLine();
        }
    }
    
    private static String pres2Str(final RadEditorPresentationDef presentation){
        return presentation.getClassPresentation().getName() + "::" + presentation.getName() +  " (#" + presentation.getId() + ")";
    }

    private final class AccessibleEdPresIterator implements Iterator<PresentationEntityAdapter.EditorPresentationCandidate> {

        private PresentationEntityAdapter.EditorPresentationCandidate nextPres = null;
        private final Iterator<RadEditorPresentationDef> allPres;
        private final PresCalculationLogger logger;

        public AccessibleEdPresIterator(final Iterator<RadEditorPresentationDef> allPres, final PresCalculationLogger logger) {
            this.allPres = allPres;
            this.logger = logger;            
        }

        @Override
        public boolean hasNext() {
            if (nextPres != null) {
                return true;
            }
            try {
                nextPres = next();
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        @Override
        public PresentationEntityAdapter.EditorPresentationCandidate next() {
            if (nextPres != null) {
                final PresentationEntityAdapter.EditorPresentationCandidate p = nextPres;
                nextPres = null;
                return p;
            }
            while (allPres.hasNext()) {
                final RadEditorPresentationDef replacedPres = allPres.next();
                final RadEditorPresentationDef p = getActualEditorPresentation(replacedPres);                
                final Restrictions totalRestrictions = p.getTotalRestrictions(entity.getCurUserApplicableRoleIds());
                final Id messageId;
                if (totalRestrictions.getIsAccessRestricted()){
                    messageId = Messages.MLS_ID_EAS_CANT_ACCESS_PRES;
                }else if (getAdditionalRestrictions(p).getIsAccessRestricted()){
                    messageId = Messages.MLS_ID_EAS_CANT_ACCESS_PRES_ADD;
                }else{
                    messageId=null;
                }
                if (messageId==null){
                    return new PresentationEntityAdapter.EditorPresentationCandidate(replacedPres, p, totalRestrictions);
                }else{
                    logger.enterSection();
                    logger.append(messageId, pres2Str(p));
                    logger.leaveSection();
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The iterator is readonly");
        }
        
        private static final int MAX_REPLACE_ITER_COUNT = 100;

        private RadEditorPresentationDef getActualEditorPresentation(final RadEditorPresentationDef origPres) {
            final RadClassPresentationDef classPres = entity.getPresentationMeta();
            Id origPresId = null;
            Id replacePresId = origPres.getId();
            final StringBuilder pathBuilder = new StringBuilder();
            int i = 0;            
            do {
                if (i>0){
                    pathBuilder.append('#');
                    pathBuilder.append(origPresId.toString());
                    pathBuilder.append("->#");
                    pathBuilder.append(replacePresId.toString());                    
                }
                if (i++ > MAX_REPLACE_ITER_COUNT) {
                    throw new IllegalUsageError("Editor presentations replacement operation count limit(" + String.valueOf(MAX_REPLACE_ITER_COUNT) + ") is exceeded.");
                }
                origPresId = replacePresId;
                replacePresId = classPres.getActualPresentationId(origPresId);
            } while (!replacePresId.equals(origPresId));           
            final RadEditorPresentationDef replacePres;            
            if (replacePresId.equals(origPres.getId())){
                replacePres = origPres;
            }else{
                replacePres = classPres.getEditorPresentationById(replacePresId);
                if (i>2){                    
                    logger.append(Messages.MLS_ID_EAS_PRES_REPLACED_WITH_PATH, pres2Str(origPres), pres2Str(replacePres), pathBuilder.toString() );
                }else{
                    logger.append(Messages.MLS_ID_EAS_PRES_REPLACED, pres2Str(origPres), pres2Str(replacePres) );
                }
                logger.endLine();
            }
            origPresId = replacePresId;
            replacePresId = entity.onCalcEditorPresentation(origPresId);
            final RadEditorPresentationDef finalPres;
            if (replacePresId.equals(origPresId)){
                finalPres = replacePres;
            }else{
                finalPres = classPres.getEditorPresentationById(replacePresId);
                logger.append(Messages.MLS_ID_EAS_PRES_REPLACED_IN_HANDLER, pres2Str(replacePres), pres2Str(finalPres) );
            }           
            return finalPres;
        }
    }

    private String getMessageString(final Id stringId) {
        return MultilingualString.get(entity.getArte(), Messages.MLS_OWNER_ID, stringId);
    }

    private static class UserReferenceObject {

        private final Id childEntityId;
        private final String pidAsStr;

        public UserReferenceObject(final Id childEntityId, final String pidAsStr) {
            this.childEntityId = childEntityId;
            this.pidAsStr = pidAsStr;
        }

        public Id getChildEntityId() {
            return childEntityId;
        }

        public String getPidAsStr() {
            return pidAsStr;
        }
    }
}

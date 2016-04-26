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
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.AppException;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
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
import org.radixware.kernel.server.types.presctx.PresentationContext;
import org.radixware.kernel.server.types.presctx.UnknownPresentationContext;


public class PresentationEntityAdapter<T extends Entity> {

    public static final class EditorPresentationCandidate {

        private final static EnumSet<ERestriction> VIEW_ONLY_RESTRICTIONS =
                EnumSet.of(ERestriction.CREATE, ERestriction.UPDATE, ERestriction.DELETE, ERestriction.ANY_COMMAND);
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
            final EnumSet<ERestriction> restrictions = ERestriction.fromBitField(totalRestrictions.getBitMask());
            if (restrictions.containsAll(VIEW_ONLY_RESTRICTIONS)) {
                return totalRestrictions.getEnabledCommandIds() == null || totalRestrictions.getEnabledCommandIds().isEmpty();
            } else {
                return false;
            }
        }
    }
    
    private Entity entity;
    private PresentationContext context;

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

    protected Collection<RadEditorPresentationDef> doGetActualEditorPresentations(Entity entity, Collection<Id> allowedPresentations) {

        List<RadEditorPresentationDef> result = new LinkedList<>();

        List<RadEditorPresentationDef> desiredPresentations;
        if (allowedPresentations == null) {
            desiredPresentations = entity.getRadMeta().getPresentation().getEditorPresentations();
        } else {
            desiredPresentations = new LinkedList<>();
            for (Id id : allowedPresentations) {
                try {
                    RadEditorPresentationDef epr = entity.getRadMeta().getPresentation().getEditorPresentationById(id);
                    desiredPresentations.add(epr);
                } catch (DefinitionNotFoundError e) {
                }
            }
        }
        if (desiredPresentations.isEmpty()) {
            return Collections.emptySet();
        }

        AccessibleEdPresIterator iterator = new AccessibleEdPresIterator(desiredPresentations.iterator());
        final ERuntimeEnvironmentType clientEnvironment = entity.getArte().getClientEnvironment();
        while (iterator.hasNext()) {
            RadEditorPresentationDef candidate = iterator.next().getPresentation();
            if (candidate.hasCompatibleClientRuntimeEnvironmentType(clientEnvironment)) {
                result.add(candidate);
            }
        }

        return result;
    }

    public RadEditorPresentationDef doSelectEditorPresentation(final Iterator<PresentationEntityAdapter.EditorPresentationCandidate> accessiblePresIter) {
        //see RADIX-6054 and RADIX-8169
        final ERuntimeEnvironmentType clientEnvironment = entity.getArte().getClientEnvironment();
        EditorPresentationCandidate currentCandidate;
        RadEditorPresentationDef selectedPresentation = null;
        int selectedCandidateScore = -1, currentCandidateScore;
        while (accessiblePresIter.hasNext()) {
            currentCandidate = accessiblePresIter.next();
            currentCandidateScore = calcScoreForEditorPresentationCandidate(currentCandidate, clientEnvironment);
            if (currentCandidateScore > 11) {//maximum score
                return currentCandidate.getPresentation();
            }
            if (currentCandidateScore > selectedCandidateScore) {
                selectedCandidateScore = currentCandidateScore;
                selectedPresentation = currentCandidate.getPresentation();
            }
        }
        return selectedPresentation;
    }

    private static int calcScoreForEditorPresentationCandidate(final EditorPresentationCandidate candidate, final ERuntimeEnvironmentType desiredEnvironmentType) {
        int score;
        if (candidate.getTotalRestrictions().getIsViewRestricted()) {
            score = 0;
        } else if (candidate.isViewOnly()) {
            score = 1;
        } else {
            score = 2;
        }
        if (candidate.getPresentation().hasCompatibleClientRuntimeEnvironmentType(desiredEnvironmentType)) {
            score += 10;
        }
        return score;
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
        return "\"" + childEntity.calcTitle() + "\" (" + childEntity.getRadMeta().getTitle() + ")\"";
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
            sb.append(parentEntity.getPid().toString());
            sb.append("'");
        } else {
            sb.append("rdx_array.searchRef(val, ");
            sb.append("'");
            sb.append(parentEntity.getEntityId());
            sb.append("', '");
            sb.append(parentEntity.getPid().toString());
            sb.append("', 1) > 0");
        }
        if (onlyFirst) {
            sb.append(" and ROWNUM < 2");
        }
        return sb.toString();
    }

    public final RadEditorPresentationDef selectEditorPresentation(final Iterable<RadEditorPresentationDef> desiredPresentations) {
        return doSelectEditorPresentation(new AccessibleEdPresIterator(desiredPresentations.iterator()));
    }

    private final class AccessibleEdPresIterator implements Iterator<PresentationEntityAdapter.EditorPresentationCandidate> {

        private PresentationEntityAdapter.EditorPresentationCandidate nextPres = null;
        private final Iterator<RadEditorPresentationDef> allPres;

        public AccessibleEdPresIterator(final Iterator<RadEditorPresentationDef> allPres) {
            this.allPres = allPres;
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
                if (!totalRestrictions.getIsAccessRestricted()) {
                    return new PresentationEntityAdapter.EditorPresentationCandidate(replacedPres, p, totalRestrictions);
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
            Id origPresId;
            Id replacePresId = origPres.getId();
            int i = 0;
            do {
                if (i++ > MAX_REPLACE_ITER_COUNT) {
                    throw new IllegalUsageError("Editor presentations replacement operation count limit(" + String.valueOf(MAX_REPLACE_ITER_COUNT) + ") is exceeded.");
                }
                origPresId = replacePresId;
                replacePresId = classPres.getActualPresentationId(origPresId);
            } while (!replacePresId.equals(origPresId));
            replacePresId = entity.onCalcEditorPresentation(replacePresId);
            return classPres.getEditorPresentationById(replacePresId);
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

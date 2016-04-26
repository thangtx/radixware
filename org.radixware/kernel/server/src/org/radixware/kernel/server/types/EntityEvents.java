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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EAutoUpdateReason;
import org.radixware.kernel.common.enums.EEntityInitializationPhase;
import org.radixware.kernel.common.exceptions.AppException;


public class EntityEvents {
// Published Events

    @SuppressWarnings("unused")
    protected boolean beforeInit(final PropValHandlersByIdMap initialPropValsById, final Entity src, final EEntityInitializationPhase phase) {
        return true;
    }

    ;
    @SuppressWarnings("unused")
    protected void afterInit(final Entity src, final EEntityInitializationPhase phase) {
    }

    @SuppressWarnings("unused")
    protected boolean beforeCreate(final Entity src) {
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterCreate(final Entity src) {
    }

    @SuppressWarnings("unused")
    protected void afterRead() {
    }

    @SuppressWarnings("unused")
    protected boolean beforeUpdate() {
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterUpdate() {
    }

    @SuppressWarnings("unused")
    protected boolean beforeUpdatePropObject(final Id propId, final Entity val) {
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterUpdatePropObject(final Id propId, final Entity val) {
    }

    @SuppressWarnings("unused")
    protected boolean beforeDelete() {
        return true;
    }

    @SuppressWarnings("unused")
    protected void afterDelete() {
    }

    @SuppressWarnings("unused")
    protected String onCalcTitle(final String title) {
        return title;
    }

    @SuppressWarnings("unused")
    public String onCalcParentTitle(final Id parentTitlePropId, final Entity parent, final String title) {
        return title;
    }

    @SuppressWarnings("unused")
    protected boolean beforeAutoUpdate(final EAutoUpdateReason r) {
        return true;
    }

    @SuppressWarnings("unused")
    public FormHandler.NextDialogsRequest execCommand(final Id cmdId, final Id propId, final org.apache.xmlbeans.XmlObject input, final PropValHandlersByIdMap newPropValsById, final org.apache.xmlbeans.XmlObject output) throws AppException, InterruptedException {
        return null;
    }

    @SuppressWarnings("unused")
    public Id onCalcEditorPresentation(final Id presentationId) {
        return presentationId;
    }

    @SuppressWarnings("unused")
    public void onSetSavepoint(final String id) {
    }

    @SuppressWarnings("unused")
    public void beforeRollbackToSavepoint(final String id) {
    }

    @SuppressWarnings("unused")
    public void afterRollbackToSavepoint(final String id) {
    }

    @SuppressWarnings("unused")
    protected boolean onAutonomousStoreAfterRollback(final String savePointId) {
        return false;
    }
//    @SuppressWarnings("unused")
//    protected void onCalcCurUserApplicableRoleIds(List<Id> roleIds){}

    @SuppressWarnings("unused")
    protected void afterCommit(boolean newObject) {
    }
}

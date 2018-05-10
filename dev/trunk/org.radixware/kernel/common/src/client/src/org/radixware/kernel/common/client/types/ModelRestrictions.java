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

package org.radixware.kernel.common.client.types;

import java.util.ArrayList;
import java.util.EnumSet;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.EntityModel;

import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;

public abstract class ModelRestrictions extends Restrictions {

    private final EnumSet<ERestriction> PROGRAMMATICALLY_RESTRICTIONS = 
        EnumSet.of(ERestriction.CHANGE_POSITION, ERestriction.NOT_READ_ONLY_COMMANDS, ERestriction.MULTIPLE_SELECTION,
                   ERestriction.MULTIPLE_DELETE, ERestriction.SELECT_ALL, ERestriction.MULTIPLE_CREATE, ERestriction.CALC_STATISTIC);
    
    private final EnumSet<ERestriction> predefinedRestrictions;
    private final EnumSet<ERestriction> serverRestrictions = EnumSet.noneOf(ERestriction.class);    
    private final MessageProvider messageProvider;

    public ModelRestrictions(final Model m) {
        super(Restrictions.Factory.sum(m.getDefinition().getRestrictions(), m.getContext().getRestrictions()));
        messageProvider = m.getEnvironment().getMessageProvider();
        restrictions.removeAll(PROGRAMMATICALLY_RESTRICTIONS);
        if (m instanceof EntityModel) //Ограничение на создание в entityModel проверяется только сервером
        {
            restrictions.remove(ERestriction.CREATE);
        }
        predefinedRestrictions = EnumSet.copyOf(restrictions);
    }

    protected void setMask(final ERestriction restriction, final boolean flag) {
        if (flag) {
            restrictions.add(restriction);
        } else {
            if (predefinedRestrictions.contains(restriction)) {
                throw new IllegalUsageError(messageProvider.translate("ExplorerException", "Can't remove predefined restriction!"));
            }
            if (serverRestrictions.contains(restriction)) {
                throw new IllegalUsageError(messageProvider.translate("ExplorerException", "Can't remove server restriction!"));
            }
            restrictions.remove(restriction);
        }
        refreshView(restriction==ERestriction.UPDATE);
    }

    public void setServerRestrictions(final EnumSet<ERestriction> srvRestrictions) {        
        if (!serverRestrictions.equals(srvRestrictions)){
            final boolean updateChange = 
                restrictionDiffer(serverRestrictions, srvRestrictions, ERestriction.UPDATE);
            serverRestrictions.clear();
            serverRestrictions.addAll(srvRestrictions);            
            refreshView(updateChange);
        }
    }
    
    public void setNotReadOnlyCommandsRestricted(boolean flag){
        setMask(ERestriction.NOT_READ_ONLY_COMMANDS,flag);
    }    
    
    private boolean isRestrictedInView(final ERestriction restriction){
        final IView view = getView();
        final ViewRestrictions restrictions = view==null ? null : view.getRestrictions();
        if (restrictions==null){
            return false;
        }
        return restrictions.isRestrictedInView(restriction);
    }
        
    private boolean isCommandRestrictedInView(final Id commandId, final boolean isReadOnly){
        final IView view = getView();
        if (view==null){
            return false;
        }
        return view.getRestrictions().isCommandRestrictedInView(commandId, isReadOnly);
    }
    
    boolean isRestrictedInModel(final ERestriction restriction){
        return restrictions.contains(restriction) || serverRestrictions.contains(restriction);
    }
        
    boolean isCommandRestrictedInModel(final Id commandId, final boolean isReadOnly){
        if ((restrictions.contains(ERestriction.NOT_READ_ONLY_COMMANDS) && !isReadOnly) || 
            restrictions.contains(ERestriction.ANY_COMMAND)) {
            return (enabledCommandIds == null) || !enabledCommandIds.contains(commandId);
        } else {
            return false;
        }
    }

    @Override
    @Deprecated
    public boolean getIsCommandRestricted(final Id cmdId) {
        return isCommandRestrictedInModel(cmdId, false) || isCommandRestrictedInView(cmdId, false);
    }
    
    @Override
    public boolean getIsCommandRestricted(final Id cmdId, final boolean isReadOnly) {
        return isCommandRestrictedInModel(cmdId, isReadOnly) || isCommandRestrictedInView(cmdId, isReadOnly);
    }

    public boolean canBeAllowed(final ERestriction restriction) {
        return !predefinedRestrictions.contains(restriction)
                && !serverRestrictions.contains(restriction);
    }

    abstract void refreshView(final boolean refreshPropEditors);
    
    abstract protected IView getView();
    
    @Override
    protected boolean isRestricted(final ERestriction restriction){
        return isRestrictedInModel(restriction) || isRestrictedInView(restriction);
    }

    public void add(final Restrictions from) {        
        Restrictions resultRestrictions = Restrictions.Factory.sum(this, from);
        restrictions.clear();
        restrictions.addAll(resultRestrictions.restrictions);
        if (from instanceof ModelRestrictions) {
            serverRestrictions.addAll(((ModelRestrictions) from).serverRestrictions);
        }
        if (resultRestrictions.enabledCommandIds != null) {
            enabledCommandIds = new ArrayList<>(resultRestrictions.enabledCommandIds);
        } else {
            enabledCommandIds = null;
        }
    }

    public void clear() {
        serverRestrictions.clear();
    }        
}

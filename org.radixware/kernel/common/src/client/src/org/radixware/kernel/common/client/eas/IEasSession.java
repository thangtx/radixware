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

package org.radixware.kernel.common.client.eas;

import java.util.Collection;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.CommandRs;
import org.radixware.schemas.eas.ContextlessCommandRs;
import org.radixware.schemas.eas.CreateRs;
import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.GetObjectTitlesRs;
import org.radixware.schemas.eas.ListEdPresVisibleExpItemsRs;
import org.radixware.schemas.eas.ListInstantiatableClassesRs;
import org.radixware.schemas.eas.PrepareCreateRs;
import org.radixware.schemas.eas.ReadRs;
import org.radixware.schemas.eas.SelectRs;
import org.radixware.schemas.eas.SetParentRs;
import org.radixware.schemas.eas.UpdateRs;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.schemas.eas.DeleteRs;


public interface IEasSession {
    
    public static abstract class Listener{
        
        public void beforeProcessSyncRequest(final RequestHandle handle){
            
        }
        
        public void afterSyncRequestProcessed(final RequestHandle handle){
            
        }
    }

    public boolean isBusy();  

//    public List<Id> login(final Id explorerRootId)
//            throws ServiceClientException, InterruptedException;

    public ReadRs read(Pid pid,
            Id classId,
            Collection<Id> presentations,
            IContext.Entity entityContext,
            boolean withAccessibleExplorerItems)
            throws ServiceClientException, InterruptedException;

    public ReadRs readProp(Pid pid,
            Id classId,
            Collection<Id> presentations,
            IContext.Entity entityContext,
            Id propId)
            throws ServiceClientException, InterruptedException;

    public SetParentRs setParent(Model childModel,
            Id parentPropId,
            Pid parentPid)
            throws ServiceClientException, InterruptedException;

    public UpdateRs update(EntityModel entity)
            throws ServiceClientException, InterruptedException;

    public PrepareCreateRs prepareCreate(
            final Id creationPresentationId,
            final Id classId,
            final Pid srcPid,
            final IContext.Entity ctx,
            final Collection<Property> predefinedVals)
            throws ServiceClientException, InterruptedException;

    public PrepareCreateRs prepareCreate(
            final List<Id> creationPresentationIds,
            final Id classId,
            final Pid srcPid,
            final IContext.Entity ctx,
            final Collection<Property> predefinedVals)
            throws ServiceClientException, InterruptedException;

    public CreateRs create(EntityModel entity)
            throws ServiceClientException, InterruptedException;

    public void deleteObject(Pid pid,
            Id classId,
            Id presentationId,
            IContext.Entity entityContext,
            boolean cascade) throws ServiceClientException, InterruptedException;        

    public SelectRs select(
            final GroupModel group,
            final int startIndex,
            final int rowCount,
            final boolean withSelectorAddons,
            final boolean withInstantiatableClasses)
            throws ServiceClientException, InterruptedException;

    public ListInstantiatableClassesRs listInstantiatableClasses(
            final Id entityId,
            org.radixware.schemas.eas.Context context)
            throws ServiceClientException, InterruptedException;

    public ListEdPresVisibleExpItemsRs listEdPresVisibleExpItems(final EntityModel entity) throws ServiceClientException, InterruptedException;
    
    public List<Id> listVisibleExplorerItems(final Id explorerRootId) throws ServiceClientException, InterruptedException;

    public void deleteContext(final GroupModel group, final boolean cascade)
            throws ServiceClientException, InterruptedException;
    
    public DeleteRs deleteSelectedObjects(final GroupModel group, final EntityObjectsSelection selection, final boolean cascade)
            throws ServiceClientException, InterruptedException;

    public ContextlessCommandRs executeCommand(
            final FormModel form,
            final Id cmdId,
            final XmlObject rq)
            throws ServiceClientException, InterruptedException;

    public CommandRs executeCommand(
            final Model model,
            final FormModel form,
            final Id cmdId,
            final Id propertyId,
            XmlObject rq) throws ServiceClientException, InterruptedException;

    public XmlObject executeContextlessCommand(
            final Id cmdId,
            final XmlObject rq,
            final Class<? extends XmlObject> responceClass)
            throws ServiceClientException, InterruptedException;

    public String getEntityTitleByPid(final Id tableId,
            final Id presentationId,
            String pid)
            throws ServiceClientException, InterruptedException;
    
    public GetObjectTitlesRs getEntityTitles(final Id tableId, 
                                             final Id defaultPresentationId, 
                                             final GetObjectTitlesRq.Objects objects
                                             )      
            throws ServiceClientException, InterruptedException;
    
    public GetObjectTitlesRs getEntityTitles(final Id tableId,
                                             final IContext.Abstract defaultContext,
                                             final GetObjectTitlesRq.Objects objects
                                             )      
            throws ServiceClientException, InterruptedException;    

    public PasswordRequirements getPasswordRequirements() throws ServiceClientException, InterruptedException;
    
    public void changePassword(final String oldPassword, final String newPassword) throws ServiceClientException, InterruptedException;
    
    @SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation")
    public void test() throws ServiceClientException, InterruptedException;

    public void breakRequest();

    public void sendAsync(CommandRequestHandle handle);
    
    public void sendAsync(CommandRequestHandle handle, int timeoutSec);

    public void sendAsync(RequestHandle handle);
    
    public void sendAsync(RequestHandle handle, int timoutSec);
    
    public LocalTracer getSessionTrace();
    
    public void addListener(final Listener listener);
    
    public void removeListener(final Listener listener);
    
    public IEasSession createBackgroundSession();
    
    public void close(boolean forced);
    
    public boolean isOpened();
    
    public DatabaseInfo getDatabaseInfo();
    
    public boolean isSessionKeyAccessible();
    
    public byte[] encryptBySessionKey(byte[] data);
    
    public byte[] decryptBySessionKey(byte[] data);
}

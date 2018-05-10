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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.EntityObjectTitlesProvider;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.ObjectReference;
import org.radixware.schemas.eas.PresentableObject;

public class Reference implements Serializable {

    private static final long serialVersionUID = -5501522557341935085L;
    private String title;
    private final String brokenTitle;
    private transient Id classId;
    private transient Pid pid;
    private final EnumSet<EReferencedObjectActions> allowedActions;

    protected Reference() {
        this((Pid) null, "", null, EnumSet.allOf(EReferencedObjectActions.class), null);
    }
    
    public Reference(final EntityModel model){
        this(model.getPid(),model.getTitle(),null,EnumSet.allOf(EReferencedObjectActions.class), model.getClassId());
        if (!model.canOpenEntityView()){
            allowedActions.remove(EReferencedObjectActions.VIEW);
        }
        if (model.getRestrictions().getIsUpdateRestricted()){
            allowedActions.remove(EReferencedObjectActions.MODIFY);
        }
        if (model.getRestrictions().getIsDeleteRestricted()){
            allowedActions.remove(EReferencedObjectActions.DELETE);
        }
    }

    public Reference(final Pid pid, final String title) {
        this(pid, title, null, EnumSet.allOf(EReferencedObjectActions.class), null);
    }

    public Reference(final Id entityId, final String pidAsStr, final String title) {
        this(new Pid(entityId, pidAsStr), title, null, EnumSet.allOf(EReferencedObjectActions.class), null);
    }

    public Reference(final Pid pid) {
        this(pid, null, null, EnumSet.allOf(EReferencedObjectActions.class), null);
    }

    public Reference(final Reference source) {
        this(source.pid, source.title, source.brokenTitle, source.allowedActions, source.getClassId());
    }

    public Reference(final Pid pid, final String title, final String brokenTitle) {
        this(pid, title, brokenTitle, EnumSet.allOf(EReferencedObjectActions.class), null);
    }

    public Reference(final Pid pid, final String title, final String brokenTitle, final boolean objectRestricted, final boolean viewRestricted) {
        this(pid, title, brokenTitle, EnumSet.allOf(EReferencedObjectActions.class), null);
        if (objectRestricted){
            allowedActions.clear();
        }else if (viewRestricted){
            allowedActions.remove(EReferencedObjectActions.VIEW);
        }
    }
    
    public Reference(final Pid pid, final String title, final String brokenTitle, final EnumSet<EReferencedObjectActions> actions) {
        this(pid,title,brokenTitle,actions,null);
    }
    
    public Reference(final Pid pid, final String title, final String brokenTitle, final EnumSet<EReferencedObjectActions> actions, final Id classId) {
        this.pid = pid;
        this.title = title;
        this.classId = classId;
        this.brokenTitle = brokenTitle;
        this.allowedActions = actions==null ? EnumSet.noneOf(EReferencedObjectActions.class) : EnumSet.copyOf(actions);        
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String newTitle) {
        title = newTitle;
    }

    public Pid getPid() {
        return pid;
    }

    public boolean isValid() {
        return pid != null && brokenTitle == null;
    }

    public boolean isBroken() {
        return brokenTitle != null;
    }
    
    public Id getClassId(){
        return classId;
    }

    public boolean isEntityModelRestricted() {
        return !allowedActions.contains(EReferencedObjectActions.ACCESS);
    }
    
    public boolean isEditorRestricted(){
        return !allowedActions.contains(EReferencedObjectActions.ACCESS) 
                || !allowedActions.contains(EReferencedObjectActions.VIEW);
    }
    
    public boolean isDeleteRestricted(){
        return !allowedActions.contains(EReferencedObjectActions.ACCESS) 
                || !allowedActions.contains(EReferencedObjectActions.DELETE);        
    }
    
    public boolean isModificationRestricted(){
        return !allowedActions.contains(EReferencedObjectActions.ACCESS) 
                || !allowedActions.contains(EReferencedObjectActions.MODIFY);
    }

    public static Reference fromValAsStr(final String str) {
        final ArrStr arr = ArrStr.fromValAsStr(str);
        if (arr.size()<2){
            throw new WrongFormatError("Can't restore object PID from string", null);
        }
        final Pid pid = new Pid(Id.Factory.loadFrom(arr.get(0)), arr.get(1));
        if (arr.size()==3){
            return new Reference(pid,null,null,null,Id.Factory.loadFrom(arr.get(2)));
        }
        if (arr.size()==4){
            return new ResolvableReference(pid, Id.Factory.loadFrom(arr.get(2)), Id.Factory.loadFrom(arr.get(3)));
        }
        return new Reference(pid);
    }
    
    public static Reference fromXml(final PresentableObject xml, final DefManager defManager, final Id tableId){
        if (xml==null){
            return null;
        }else if (!xml.isSetPID()){
            return new Reference((Pid) null, xml.getTitle(), null);
        }else{
            final Id classId = xml.getClassId();
            final Id tblId;
            if (defManager!=null && classId!=null){
                RadClassPresentationDef classDef;
                try{
                    classDef = defManager.getClassPresentationDef(classId);
                }catch(DefinitionError error){
                    classDef = null;
                }
                tblId = classDef==null ? tableId : classDef.getTableId();
            }else{
                tblId = tableId;
            }
            if (tblId==null){
                throw new IllegalArgumentException("Failed to convert XML to reference: table identifier is required");
            }
            final EnumSet<EReferencedObjectActions> allowedActions =  EnumSet.allOf(EReferencedObjectActions.class);
            if (xml.getDisabledActions()!=null){
                final List<org.radixware.schemas.eas.Actions.Item> disabledActions = xml.getDisabledActions().getItemList();
                if (disabledActions!=null && !disabledActions.isEmpty()){
                    for (org.radixware.schemas.eas.Actions.Item action: disabledActions){
                        if (action.getType()==org.radixware.schemas.eas.ActionTypeEnum.DELETE){
                            allowedActions.remove(EReferencedObjectActions.DELETE);
                        }else if (action.getType()==org.radixware.schemas.eas.ActionTypeEnum.UPDATE){
                            allowedActions.remove(EReferencedObjectActions.MODIFY);
                        }else if (action.getType()==org.radixware.schemas.eas.ActionTypeEnum.VIEW){
                            allowedActions.remove(EReferencedObjectActions.VIEW);
                        }
                    }
                }
            }
            
            final Pid pid = new Pid(tblId, xml.getPID());
            return new Reference(pid, xml.getTitle(), null, allowedActions, classId);
        }
    }
    
    public static Reference fromXml(final ObjectReference xml, final DefManager defManager, final Id tableId){
        if (xml==null){
            return null;
        }else{
            if (xml.isSetPID()) {
                final Id tblId;
                final Id classId;
                if (xml.isSetTableId()){
                    tblId = xml.getTableId();
                    classId = null;
                }else if (xml.isSetClassId() && defManager!=null){
                    RadClassPresentationDef classDef;
                    try{
                        classDef = defManager.getClassPresentationDef(xml.getClassId());
                    }catch(DefinitionError error){
                        classDef = null;
                    }
                    classId = classDef==null ? null : classDef.getId();
                    tblId  = classDef==null ? tableId : classDef.getTableId();                    
                }else{
                    tblId = tableId;
                    classId = null;
                }
                if (tblId==null){
                    throw new WrongFormatError("Cannot convert to reference: table identifier is required.\n"+xml.xmlText());
                }
                return new Reference(new Pid(tableId, xml.getPID()),
                        xml.getTitle(),
                        xml.getBrokenRef(),
                        EReferencedObjectActions.fromBitMask(xml.getAllowedActionsBitMask()),
                        classId);
            } else if (xml.isSetTitle() && xml.getTitle() != null) {
                //В презентационных атрибутах свойства может быть задан
                //формат заголовка при нулевом значении
                return new Reference((Pid) null, xml.getTitle(), xml.getBrokenRef());
            } else if (xml.isSetBrokenRef() && xml.getBrokenRef() != null) {//RADIX-3199
                //У ссылки на несуществующий объект может быть задано сообщение об ошибке
                return new Reference((Pid) null, null, xml.getBrokenRef());
            } else{
                return null;
            }
        }
    }
    
    public static Reference copy(final Reference source){
        if (source==null){
            return null;
        }
        if (source instanceof ResolvableReference){
            return new ResolvableReference((ResolvableReference)source);
        }
        return new Reference(source);
    }    
    
    @Override
    public boolean equals(final Object obj) {        
        if (this == obj) {
            return true;
        }
        if (obj instanceof Reference) {
            if (this.pid != null) {
                return this.pid.equals(((Reference) obj).getPid());
            } else {
                return ((Reference) obj).getPid() == null;
            }
        }
        if (obj instanceof Pid) {
            return this.pid.equals((Pid) obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.pid != null ? this.pid.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (isBroken()) {
            return brokenTitle;
        }
        return (title == null && pid != null ? pid.toString() : title);
    }
    
    public String toValAsStr(){
        if (classId==null){
            return new ArrStr(pid.getTableId().toString(), pid.toString()).toString();
        }else{
            return new ArrStr(pid.getTableId().toString(), pid.toString(), classId.toString()).toString();
        }
    }

    @SuppressWarnings("unused")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        if (pid == null) {
            stream.writeBoolean(false);
        } else {
            stream.writeBoolean(true);
            stream.writeObject(pid.getTableId().toString());
            stream.writeObject(pid.toString());
            if (classId==null){
                stream.writeBoolean(false);
            }else{
                stream.writeBoolean(true);
                stream.writeObject(classId.toString());
            }
        }
    }

    @SuppressWarnings("unused")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final boolean isValid = stream.readBoolean();
        if (isValid) {
            final String tableId = (String) stream.readObject();
            pid = new Pid(Id.Factory.loadFrom(tableId), (String) stream.readObject());            
            boolean hasClassId;
            try{
                hasClassId = stream.readBoolean();
            }catch(IOException exception){
                hasClassId = false;
            }
            if (hasClassId){
                try{
                    classId = Id.Factory.loadFrom((String)stream.readObject());
                }catch(IOException exception){
                    classId = null;
                }
            }else{
                classId = null;
            }
        }
    }
    
    public static boolean exactlyMatch(final Reference ref1, final Reference ref2){
        if (Objects.equals(ref1, ref2)) {
            return ref1 == null || 
                        (Objects.equals(ref1.getTitle(), ref2.getTitle()) && 
                        ref1.isEntityModelRestricted() == ref2.isEntityModelRestricted() && 
                        ref1.isEditorRestricted() == ref2.isEditorRestricted() &&
                        Objects.equals(ref1.classId, ref2.classId)
                        );
        }else{        
            return false;
        }
    }
    
    public Reference actualizeTitle(final IClientEnvironment environment, final Id tableId) throws InterruptedException, ServiceClientException{
        return actualizeTitle(environment, tableId, (Id)null);
    }    
    
    public Reference actualizeTitle(final IClientEnvironment environment, final Id tableId, final Id presentationId) throws InterruptedException, ServiceClientException{
        final EntityObjectTitlesProvider titlesProvider = new EntityObjectTitlesProvider(environment, tableId, presentationId);
        return actualizeTitle(titlesProvider, tableId);
    }    
    
    public Reference actualizeTitle(final IClientEnvironment environment, final Id tableId, final IContext.Abstract context) throws InterruptedException, ServiceClientException{
        final EntityObjectTitlesProvider titlesProvider = new EntityObjectTitlesProvider(environment, tableId, context);
        return actualizeTitle(titlesProvider, tableId);

    }
    
    private Reference actualizeTitle(final EntityObjectTitlesProvider titlesProvider, final Id tableId) throws InterruptedException, ServiceClientException{
        if (getPid()==null){
            return Reference.copy(this);
        }
        if (!Objects.equals(getPid().getTableId(), tableId)) {
            final String message = "Object %1$s belongs to a different entity (%2$s)";
            throw new IllegalArgumentException(String.format(message, getPid().toString(), tableId));
        }        
        titlesProvider.addEntityObjectReference(this);
        return titlesProvider.getTitles().getEntityObjectReference(getPid());
    }
    
    public ObjectReference writeToXml(final ObjectReference xml){
        return writeToXml(xml, true);
    }
    
    public ObjectReference writeToXml(final ObjectReference xml, final boolean writeTableId){
        final ObjectReference refXml = xml==null ? ObjectReference.Factory.newInstance() : xml;
        final Pid pid = getPid();
        if (pid != null) {
            refXml.setPID(pid.toString());
            if (classId==null){
                if (writeTableId){
                    refXml.setTableId(pid.getTableId());
                }
            }else{
                refXml.setClassId(classId);
            }
        }
        if (isBroken()) {
            refXml.setBrokenRef("");
        }
        return refXml;
    }
}

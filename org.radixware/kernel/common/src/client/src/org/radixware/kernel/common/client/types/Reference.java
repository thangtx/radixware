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
import java.util.Objects;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.enums.EReferencedObjectActions;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;

public class Reference implements Serializable {

    private static final long serialVersionUID = -5501522557341935085L;
    private String title;
    private final String brokenTitle;
    private transient Pid pid;
    private final EnumSet<EReferencedObjectActions> allowedActions;

    protected Reference() {
        this((Pid) null, "", null, EnumSet.allOf(EReferencedObjectActions.class));
    }
    
    public Reference(final EntityModel model){
        this(model.getPid(),model.getTitle(),null,EnumSet.allOf(EReferencedObjectActions.class));
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
        this(pid, title, null, EnumSet.allOf(EReferencedObjectActions.class));
    }

    public Reference(final Id entityId, final String pidAsStr, final String title) {
        this(new Pid(entityId, pidAsStr), title, null, EnumSet.allOf(EReferencedObjectActions.class));
    }

    public Reference(final Pid pid) {
        this(pid, null, null, EnumSet.allOf(EReferencedObjectActions.class));
    }

    public Reference(final Reference source) {
        this(source.pid, source.title, source.brokenTitle, source.allowedActions);
    }

    public Reference(final Pid pid, final String title, final String brokenTitle) {
        this(pid, title, brokenTitle, EnumSet.allOf(EReferencedObjectActions.class));
    }

    public Reference(final Pid pid, final String title, final String brokenTitle, final boolean objectRestricted, final boolean viewRestricted) {
        this(pid, title, brokenTitle, EnumSet.allOf(EReferencedObjectActions.class));
        if (objectRestricted){
            allowedActions.clear();
        }else if (viewRestricted){
            allowedActions.remove(EReferencedObjectActions.VIEW);
        }
    }
    
    public Reference(final Pid pid, final String title, final String brokenTitle, final EnumSet<EReferencedObjectActions> actions) {
        this.pid = pid;
        this.title = title;
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
        if (arr.size()==4){
            return new ResolvableReference(pid, Id.Factory.loadFrom(arr.get(2)), Id.Factory.loadFrom(arr.get(3)));
        }
        return new Reference(pid);
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
        return new ArrStr(pid.getTableId().toString(), pid.toString()).toString();
    }

    @SuppressWarnings("unused")
    private void writeObject(final ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        if (pid != null) {
            stream.writeBoolean(true);
            stream.writeObject(pid.getTableId().toString());
            stream.writeObject(pid.toString());
        } else {
            stream.writeBoolean(false);
        }
    }

    @SuppressWarnings("unused")
    private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        final boolean isValid = stream.readBoolean();
        if (isValid) {
            final String tableId = (String) stream.readObject();
            pid = new Pid(Id.Factory.loadFrom(tableId), (String) stream.readObject());
        }
    }
    
    public static boolean exactlyMatch(final Reference ref1, final Reference ref2){
        if (Objects.equals(ref1, ref2)) {
            return ref1 == null || 
                        (Objects.equals(ref1.getTitle(), ref2.getTitle()) && 
                        ref1.isEntityModelRestricted() == ref2.isEntityModelRestricted() && 
                        ref1.isEditorRestricted() == ref2.isEditorRestricted()
                        );
        }else{        
            return false;
        }
    }
}

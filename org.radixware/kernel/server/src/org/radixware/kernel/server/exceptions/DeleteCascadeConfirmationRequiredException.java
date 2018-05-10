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

package org.radixware.kernel.server.exceptions;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.server.types.Messages;
import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.arte.Arte;


public class DeleteCascadeConfirmationRequiredException extends RadixPublishedException {
    
    private static final long serialVersionUID = 6180703982051475431L;

    private final Set<String> subDelEntitiesSet;
    private final Set<String> subNullEntitiesSet;

    public DeleteCascadeConfirmationRequiredException(final String mess){
            super(mess);
            subDelEntitiesSet = null;
            subNullEntitiesSet = null;
    }        
    
    public DeleteCascadeConfirmationRequiredException(final Arte arte, final Set<String> subDelEntitiesSet, final Set<String> subNullEntitiesSet){
        this(arte, Collections.<String>emptySet(), subDelEntitiesSet, subNullEntitiesSet);
    }

    public DeleteCascadeConfirmationRequiredException(final Arte arte, final Set<String> messagesList, final Set<String> subDelEntitiesSet, final Set<String> subNullEntitiesSet){
        super(buildMessage(arte, messagesList, subDelEntitiesSet, subNullEntitiesSet));
        this.subDelEntitiesSet = subDelEntitiesSet==null || subDelEntitiesSet.isEmpty() ? null : new HashSet<>(subDelEntitiesSet);
        this.subNullEntitiesSet = subNullEntitiesSet==null || subNullEntitiesSet.isEmpty() ? null : new HashSet<>(subNullEntitiesSet);
    }
    
    

    private static String buildMessage(final Arte arte, final Set<String> messagesSet, final Set<String> subDelEntitiesSet, final Set<String> subNullEntitiesSet){
        final StringBuilder messageBuilder = new StringBuilder();
        final String clearRefsStr = listViaComma(subNullEntitiesSet);
        final String deleteStr = listViaComma(subDelEntitiesSet);
        if (!clearRefsStr.isEmpty()) {
            final String messageTemplate = getMessageString(arte,Messages.MLS_ID_CONFIRM_TO_CLEAR_REFS_TO_OBJECT);
            messageBuilder.append(MessageFormat.format(messageTemplate, clearRefsStr));
        }
        if (!deleteStr.isEmpty()) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n");
            }
            final String messageTemplate = getMessageString(arte,Messages.MLS_ID_CONFIRM_TO_DELETE_SUBOBJECT);
            messageBuilder.append(MessageFormat.format(messageTemplate, deleteStr));
        }
        if (messagesSet!=null){
            for (String message: messagesSet){
                if (messageBuilder.length() > 0) {
                    messageBuilder.append("\n");
                }
                messageBuilder.append(message);
            }
        }
        return messageBuilder.toString();
    }
    
    private static String getMessageString(final Arte arte, final Id stringId) {
        return MultilingualString.get(arte, Messages.MLS_OWNER_ID, stringId);
    }
        
    private static String listViaComma(final Set<String> strSet) {
        final StringBuilder sb = new StringBuilder();
        for (String str : strSet) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(str);
        }
        return sb.toString();
    }
    
    public final Set<String> getSubDelEntities(){
        return subDelEntitiesSet==null ? Collections.<String>emptySet() : Collections.<String>unmodifiableSet(subDelEntitiesSet);
    }
    
    public final Set<String> getSubNullEntities(){
        return subNullEntitiesSet==null ? Collections.<String>emptySet() : Collections.<String>unmodifiableSet(subNullEntitiesSet);
    }
}
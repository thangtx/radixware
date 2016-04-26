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

package org.radixware.kernel.common.client.exceptions;

import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.IClientError;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.schemas.eas.CommonFilter;

/**
 * Класс-обертка над ServiceCallFault для случая
 * когда getFaultString().equals(ExceptionEnum.FILTER_IS_OBLIGATORY.toString())
 * Содержит человеко-читаемое сообщение об ошибке.
 * Позволяет получить обновленный фильтр в виде xml
 */
public final class CommonFilterIsObsoleteException extends ServiceCallFault implements IClientError{
    
    private CommonFilter filter;
    private final String oldFilterTitle;
    private final boolean parametersChanged;
    static final long serialVersionUID = -5489232261153455224L;
    
    public CommonFilterIsObsoleteException(final IClientEnvironment environment, final ServiceCallFault source, final RadCommonFilter currentFilter){
        super(source.getFaultCode(), source.getFaultString(), source.getDetail());        
        oldFilterTitle = currentFilter.getTitle();       
        try{
            filter = CommonFilter.Factory.parse(source.getMessage());            
        }
        catch(XmlException ex){
            filter = null;
        }
        parametersChanged = filter==null ? false : !currentFilter.hasSameParameters(filter.getParameters());
    }
    
    public CommonFilter getNewFilter(){
        return filter;
    }
    
    public boolean isParametersChanged(){
        return parametersChanged;
    }
    
    @Override
    public String getTitle(MessageProvider messageProvider) {
        return messageProvider.translate("ExplorerError", "Current Filter is Obsolete");
    }

    @Override
    public String getLocalizedMessage(MessageProvider messageProvider) {
        final String message = messageProvider.translate("ExplorerError", "Filter '%s' was modified. It is necessary to reread data with new filter");
        return String.format(message, oldFilterTitle);
    }

    @Override
    public String getDetailMessage(MessageProvider messageProvider) {
        return getLocalizedMessage(messageProvider);
    }
}

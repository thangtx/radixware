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

package org.radixware.kernel.common.html;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class CssDistributor implements ICssStyledItem{
    
    private final ICssStyledItem provider;
    private final Collection<ICssStyledItem> consumers = new LinkedList<>();
    
    public CssDistributor(final ICssStyledItem cssProvider, final ICssStyledItem...cssConsumers){
        this.provider = cssProvider;
        this.consumers.addAll(Arrays.asList(cssConsumers));
    }

    @Override
    public void addClass(final String clazz) {
        for (ICssStyledItem consumer: consumers){
            consumer.addClass(clazz);
        }
    }

    @Override
    public boolean containsClass(final String clazz) {
        return provider.containsClass(clazz);
    }

    @Override
    public List<String> getClasses() {
        return provider.getClasses();
    }

    @Override
    public String getCss(final String name) {
        return provider.getCss(name);
    }

    @Override
    public String getStyle() {
        return provider.getStyle();
    }

    @Override
    public void removeClass(final String clazz) {
        for (ICssStyledItem consumer: consumers){
            consumer.removeClass(clazz);
        }        
    }

    @Override
    public void resetCss() {
        for (ICssStyledItem consumer: consumers){
            consumer.resetCss();
        }       
    }    

    @Override
    public void setCss(final String name, final String value) {
        for (ICssStyledItem consumer: consumers){
            consumer.setCss(name,value);
        }               
    }    
}
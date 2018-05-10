/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.defs.SqmlDefinitionsProvider;
import org.radixware.kernel.common.exceptions.DefinitionError;


public class SqmlDefinitionsLoader {
    
    private static final SqmlDefinitionsLoader INSTANCE = new SqmlDefinitionsLoader();
    private static final String SQML_PROVIDER_CLASS_SYS_PROP_NAME="org.radixware.kernel.client.SqmlDefinitionsProvider";
    
    private volatile ISqmlDefinitionsProvider provider;
    
    private SqmlDefinitionsLoader(){
    }

    public static SqmlDefinitionsLoader getInstance(){
        return INSTANCE;
    }
    
    public ISqmlDefinitions load(final IClientEnvironment environment) throws DefinitionError, CancellationException{
        if (provider==null){
            provider = createSqmlProvider(environment);
        }
        final MessageProvider mp = environment.getMessageProvider();
        environment.getTracer().debug(mp.translate("TraceMessage", "Loading SQML definitions"));
        final long time = System.currentTimeMillis();
        final ISqmlDefinitions definitions;
        try{
            definitions = provider.load(environment);
        }catch(CancellationException exception){
            environment.getTracer().debug(mp.translate("TraceMessage", "Loading SQML definitions was cancelled"));            
            throw exception;
        }finally{
            provider = null;
        }
        final long elapsedTime = System.currentTimeMillis() - time;
        final String message = 
            environment.getMessageProvider().translate("TraceMessage", "Loading SQML definitions was finished. Elapsed time %1$s ms");            
        environment.getTracer().debug(String.format(message, String.valueOf(elapsedTime)));
        return definitions;        
    }
    
    @SuppressWarnings("unchecked")
    private ISqmlDefinitionsProvider createSqmlProvider(final IClientEnvironment environment){
        final String sqmlProviderClassName = System.getProperty(SQML_PROVIDER_CLASS_SYS_PROP_NAME);        
        if (sqmlProviderClassName==null || sqmlProviderClassName.isEmpty()){
            return createDefaultSqmlProvider();
        }else{
            final Class sqmlDefinitionsProviderClass;
            try {
                sqmlDefinitionsProviderClass = getClass().getClassLoader().loadClass(sqmlProviderClassName);
            } catch (ClassNotFoundException ex) {
                final String message = environment.getMessageProvider().translate("TraceMessage", "SQML definitions provider \'%1$s\' was not found. Using default provider.");
                environment.getTracer().warning(String.format(message,sqmlProviderClassName));
                return createDefaultSqmlProvider();
            }
            if (ISqmlDefinitionsProvider.class.isAssignableFrom(sqmlDefinitionsProviderClass)){
                final Constructor constructor;
                try{
                    constructor = sqmlDefinitionsProviderClass.getConstructor(new Class[]{});
                }catch(NoSuchMethodException exception){
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Unable to find default constructor to create an instance of  \'%1$s\'  class. Using default SQML provider.");
                    environment.getTracer().warning(String.format(message,sqmlProviderClassName));
                    return createDefaultSqmlProvider();                    
                }
                try{
                    return (ISqmlDefinitionsProvider)constructor.newInstance(new Object[]{});
                }catch(IllegalAccessException | IllegalArgumentException exception){
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Unable to find default constructor to create an instance of  \'%1$s\'  class. Using default SQML provider.");
                    environment.getTracer().warning(String.format(message,sqmlProviderClassName));
                    return createDefaultSqmlProvider();
                }catch(InvocationTargetException | InstantiationException exception){
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to create an instance of \'%1$s\'  class. Using default SQML provider.");
                    environment.getTracer().error(String.format(message,sqmlProviderClassName),exception);
                    return createDefaultSqmlProvider();
                }
            }else{
                final String message = environment.getMessageProvider().translate("TraceMessage", "Class \'%1$s\' does not implements ISqmlDefinitionsProvider interface. Using default provider.");
                environment.getTracer().warning(String.format(message,sqmlProviderClassName));
                return createDefaultSqmlProvider();                
            }
        }        
    }
    
    private static ISqmlDefinitionsProvider createDefaultSqmlProvider(){
        return new SqmlDefinitionsProvider();
    }
    
    public void cancelLoading(){
        if (provider!=null){
            provider.cancelLoading();
        }
    }
}

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

package org.radixware.kernel.common.client.trace;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.trace.TraceProfile;

/**
 * Класс-контроллер диалога трассы клиента.
 * Содержит независимую от GUI реализацию управления виджетом редактора профиля трассировки.
 * Отвечает за изменение профиля клиентской трассы при производимых пользователем изменениях в редакторе,
 * а также содержит методы актуализации редактора.
 */
public final class TraceDialogController {
    
    private final IClientEnvironment environment;
    private final ITraceDialogPresenter presenter;
    private final ITraceProfileEditor traceProfileEditor;    
    private final ClientTracer.TraceLevelChangeListener traceProfileChangeListener = 
        new ClientTracer.TraceLevelChangeListener(){
            @Override
            public void traceLevelChanged(final String traceProfileAsStr) {
                traceProfileEditor.setTraceProfile(traceProfileAsStr);
            }        
        };
 
    /**
     * Конструктор
     * @param environment инстанция окружения клиента
     * @param presenter инстанция реализации графического представления диалога трассы.
     */
    public TraceDialogController(final IClientEnvironment environment, final ITraceDialogPresenter presenter){
        this.environment = environment;
        this.presenter = presenter;
        traceProfileEditor = presenter.createTraceProfileEditor(environment);        
        traceProfileEditor.addListener(new ITraceProfileEditor.IEventSeverityChangeListener() {
            @Override
            public void afterChangeEventSeverity(final String eventSource, final String newEventSeverity, final String options) {                
                final EEventSeverity eventSeverity;
                try{
                    eventSeverity = newEventSeverity==null ? null : EEventSeverity.getForName(newEventSeverity);
                }
                catch(NoConstItemWithSuchValueError error){
                    environment.getTracer().error(error);
                    return;
                }
                environment.getTracer().removeTraceLevelChangeListener(traceProfileChangeListener);
                try{
                    final TraceProfile profile = environment.getTracer().getProfile();
                    if (eventSource==null){
                        profile.setDefaultSeverity(eventSeverity);
                    }else if (eventSeverity==null){
                        profile.setUseInheritedSeverity(eventSource);
                    }else{
                        profile.set(eventSource, eventSeverity, options);
                    }
                }
                finally {
                    environment.getTracer().addTraceLevelChangeListener(traceProfileChangeListener);
                }
            }
        });   
        presenter.setProfileEditorEnabled(false);
    }
    
    /**
     * Актуализирует редактор профиля трассировки.
     * Метод обновляет в редакторе текущий набор источников событий, проверяет разрешена ли трассировка 
     * на сервере и если она запрещена, то устанавливает набор источников событий запрещенных к редактированию.
     */
    public void updateEventSources(){
        traceProfileEditor.rereadEventSources(environment.getTracer().getProfile().toString());
        if (environment.getTracer().isServerTraceAllowed()){
            traceProfileEditor.setRestrictedEventSources(null);
        }else{
            final List<String> restrictedEventSources = new LinkedList<>();
            for (String eventSource: traceProfileEditor.getEventSources()){
                if (!eventSource.startsWith(EEventSource.CLIENT.getValue()) && eventSource.indexOf(".")<0){
                    restrictedEventSources.add(eventSource);
                }
            }
            traceProfileEditor.setRestrictedEventSources(restrictedEventSources);
        }
        environment.getTracer().addTraceLevelChangeListener(traceProfileChangeListener);
        presenter.setProfileEditorEnabled(true);
    }
    
    /**
     * Обрабатывает событие разрыва соединения с сервером.
     * Прекращает обработку событий редактирования профиля трассы и 
     * вызывает метод {@link ITraceDialogPresenter#setProfileEditorEnabled(boolean) } с параметром <code>false</code>.
     */
    public void onDisconnect(){
        environment.getTracer().removeTraceLevelChangeListener(traceProfileChangeListener);
        presenter.setProfileEditorEnabled(false);
    }
}
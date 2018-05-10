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

package org.radixware.kernel.common.client.editors.traceprofile;

import java.util.*;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.kernel.common.types.Id;

/**
 * Класс-контроллер дерева профиля трассировки.
 * Содержит частичную реализацию большинства методов интерфейса {@link ITraceProfileEditor редактора профиля трассировки}, не связанную с GUI.
 * На основе иерархии источников событий класс строит древовидную структуру {@link TraceProfileTreeNode узлов}, 
 * образуя дерево профиля трассировки.
 * На первом уровне дерева находится узел, содержищий минимальный уровень важности сообщения, используемый 
 * источниками событий верхнего уровня по умолчанию, на уровнях ниже расолагаются узлы, содержищие минимальный уровень 
 * важности сообщения для конкретного источника событий.
 * @param <T> класс графического компонента, который используется для представления узлов дерева профиля трассировки
 * @see ITraceProfileEditor
 * @see TraceProfileTreeNode
 */
public final class TraceProfileTreeController<T extends IWidget> {
    
    private final static Id EVENT_SOURCE_ENUM_ID = Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY");    
    public static final String OPTOINS_ARE_UNSUPPORTED = "XXX:options_unsupported";
    private static final EnumSet<EEventSource> EVENT_SOURCE_WITH_EXT_TRACE_OPTIONS = 
        EnumSet.of(EEventSource.ARTE_DB, EEventSource.EAS);
    
    /**
     * Уровень важности сообщения.
     * Инстанция класса позволяет получить имя и заголовок для уровня важности сообщения, а также его пиктограмму.
     */
    public static final class EventSeverity{
        private final String value;
        private final String title;
        private final Icon icon;
        
        private EventSeverity(final RadEnumPresentationDef.Item item){
            value = item.getName();
            title = item.getTitle()==null || item.getTitle().isEmpty() ? item.getName() : item.getTitle();
            icon = item.getIcon();
        }
        
        private EventSeverity(final EEventSeverity eventSeverity, final IClientEnvironment environment){
            value = eventSeverity.getName();
            switch(eventSeverity){
                case ALARM:
                    title = environment.getMessageProvider().translate("TraceDialog", "Alarm");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ALARM);
                    break;
                case ERROR:
                    title = environment.getMessageProvider().translate("TraceDialog", "Error");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR);
                    break;
                case WARNING:
                    title = environment.getMessageProvider().translate("TraceDialog", "Warning");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.WARNING);
                    break;
                case EVENT:
                    title = environment.getMessageProvider().translate("TraceDialog", "Event");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.EVENT);
                    break;
                case DEBUG:
                    title = environment.getMessageProvider().translate("TraceDialog", "Debug");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.DEBUG);
                    break;
                case NONE:
                    title = environment.getMessageProvider().translate("TraceDialog", "No tracing");
                    icon = environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.NO_TRACING);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown event severity \'"+eventSeverity.getName()+"\'");
            }
        }

        /**
         * Возвращает пиктограмму для данного уровня важности сообщения.
         * @return пиктограмма
         */
        public Icon getIcon() {
            return icon;
        }

        /**
         * Возвращает заголовок данного уровня важности сообщения.
         * @return заголовок уровня важности сообщения. Не может быть <code>null</code>.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Возвращает наименование данного уровня важности сообщения.
         * @return Имя уровня важности сообщения. Не может быть <code>null</code>.
         */        
        public String getValue() {
            return value;
        }                
    }
    
    private static final class EventSeverityItemsProvider{
        
        private final static Object INSTANCE_SEMAPHORE = new Object();
        private final static Map<IClientEnvironment, EventSeverityItemsProvider> INSTANCE_BY_ENVIRONMENT = new WeakHashMap<>();
        private final static Id EVENT_SEVERITY_ENUM_ID = Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4");
        
        private final Object semaphore = new Object();
        private final List<EventSeverity> eventSeverityItemsCache = new LinkedList<>();
        private final IClientEnvironment environment;
        private EIsoLanguage cacheLanguage;
        private long cacheVersion = -1;
        
    
        private EventSeverityItemsProvider(final IClientEnvironment environment){
            this.environment = environment;
        }
        
        public List<EventSeverity> getEventSeverityItemsByOrder(){
            synchronized(semaphore){
                if (eventSeverityItemsCache.isEmpty() || 
                    cacheLanguage!=environment.getLanguage() || 
                    cacheVersion!=environment.getDefManager().getAdsVersion().getNumber()){
                    try{
                        final RadEnumPresentationDef enumDef =
                            environment.getDefManager().getEnumPresentationDef(EVENT_SEVERITY_ENUM_ID);
                        final RadEnumPresentationDef.Items items = enumDef.getItems();
                        items.sort(RadEnumPresentationDef.ItemsOrder.BY_VALUE);
                        eventSeverityItemsCache.clear();
                        for (RadEnumPresentationDef.Item enumItemDef: items){
                            eventSeverityItemsCache.add(new EventSeverity(enumItemDef));
                        }
                        cacheVersion = environment.getDefManager().getAdsVersion().getNumber();
                        cacheLanguage = environment.getLanguage();
                    }
                    catch(DefinitionError definitionError){
                        eventSeverityItemsCache.clear();
                        cacheVersion = environment.getDefManager().getAdsVersion().getNumber();
                        cacheLanguage = environment.getLanguage();
                        for (int i=EEventSeverity.DEBUG.getValue().intValue(); i<=EEventSeverity.NONE.getValue().intValue(); i++){
                            try{
                                eventSeverityItemsCache.add(new EventSeverity(EEventSeverity.getForValue(Long.valueOf(i)), environment));
                            }
                            catch(NoConstItemWithSuchValueError error){//NOPMD
                            }
                        }
                        final ExceptionMessage message = new ExceptionMessage(environment,definitionError);
                        environment.getTracer().warning(message.getTrace());
                    }
                }
                return Collections.unmodifiableList(eventSeverityItemsCache);  
            }
        }
        
        public static EventSeverityItemsProvider getInstance(final IClientEnvironment environment){
            synchronized(INSTANCE_SEMAPHORE){
                EventSeverityItemsProvider instance = INSTANCE_BY_ENVIRONMENT.get(environment);
                if (instance==null){
                    instance = new EventSeverityItemsProvider(environment);
                    INSTANCE_BY_ENVIRONMENT.put(environment,instance);
                }
                return instance;
            }
        }
        
        public static void releaseInstance(final IClientEnvironment environment){
            synchronized(INSTANCE_SEMAPHORE){
                INSTANCE_BY_ENVIRONMENT.remove(environment);
            }
        }
        
        public static void releaseInstances(final IClientApplication application){
            synchronized(INSTANCE_SEMAPHORE){
                final List<IClientEnvironment> environments = new LinkedList<>();
                for(IClientEnvironment environment: INSTANCE_BY_ENVIRONMENT.keySet()){
                    if (environment.getApplication()==application){
                        environments.add(environment);
                    }
                }
                for (IClientEnvironment environment: environments){
                    INSTANCE_BY_ENVIRONMENT.remove(environment);
                }
            }
        }
    }
                
    private final IClientEnvironment environment;
    private final ITraceProfileTreePresenter<T> presenter;
    private final Map<String,TraceProfileTreeNode<T>> treeNodesByEventSource = new HashMap<>();
    private final Map<String,EventSeverity> eventSeverityByName = new HashMap<>();    
    private List<ITraceProfileEditor.IEventSeverityChangeListener> listeners;
    private List<String> restrictedEventSources = new ArrayList<>();
    private boolean isEdited;
        
    private TraceProfileTreeNode<T> rootNode;
    
    /**
     * Конструктор класса.
     * @param environment окружение клиента
     * @param presenter реализация интерфейса графического представления узлов дерева профиля трассировки
     */
    public TraceProfileTreeController(final IClientEnvironment environment, final ITraceProfileTreePresenter<T> presenter){
        this.environment = environment;
        this.presenter = presenter;
        open();
        updateNodesPresentation();
    }
    
    private void open(){
        fillEventSeverityMap();        
        fillTreeNodes();        
    }
        
    /**
     * Возвращает упорядоченный список всех уровней важности события.
     * Элементы списка располагаются в порядке возрастания значения уровня важности сообщения.
     * Первый элемент соответствует уровню важности {@link EEventSeverity#DEBUG "Отладка"}, последний элемент соответствует
     * служебному уровню важности {@link EEventSeverity#NONE "Без трассировки"}.
     * <p>
     * Для получения набора уровней важности событий используется дефиниция набора перечислений Radix::Arte::EventSeverity,
     * а если ее получить не удается, то используется набор перечислений {@link EEventSeverity}.
     * @param environment окружение клиента.
     * @return список содержащий все уровни важности события. Не может быть <code>null</code>
     */
    public static List<EventSeverity> getEventSeverityItemsByOrder(final IClientEnvironment environment){        
        if (environment.getApplication().isReleaseRepositoryAccessible()){
            return EventSeverityItemsProvider.getInstance(environment).getEventSeverityItemsByOrder();
        }
        final List<EventSeverity> eventSeverityList = new LinkedList<>();
        if (eventSeverityList.isEmpty()){
            for (int i=EEventSeverity.DEBUG.getValue().intValue(); i<=EEventSeverity.NONE.getValue().intValue(); i++){
                try{
                    eventSeverityList.add(new EventSeverity(EEventSeverity.getForValue(Long.valueOf(i)), environment));
                }
                catch(NoConstItemWithSuchValueError error){//NOPMD
                    
                }
            }
        }
        return eventSeverityList;
    }
    
    private void fillEventSeverityMap(){
        final List<EventSeverity> eventSeverityItems = getEventSeverityItemsByOrder(environment);
        for (EventSeverity eventSeverity: eventSeverityItems){
            eventSeverityByName.put(eventSeverity.getValue(), eventSeverity);
        }
    }

    private void fillTreeNodes(){
        final String defaultEventSeverity = 
            environment.getMessageProvider().translate("TraceDialog", "Default");
        rootNode = new TraceProfileTreeNode<>(null, defaultEventSeverity, null);
        rootNode.setEventSeverity(eventSeverityByName.get(EEventSeverity.NONE.getName()));        
        if (environment.getApplication().isReleaseRepositoryAccessible()){
            try{
                final RadEnumPresentationDef enumDef =
                    environment.getDefManager().getEnumPresentationDef(EVENT_SOURCE_ENUM_ID);
                fillTreeNodesFromEnumPresentation(enumDef);
            }
            catch(DefinitionError error){
                final ExceptionMessage message = new ExceptionMessage(environment,error);
                environment.getTracer().warning(message.getTrace());            
                fillTreeNodesFromKernelEnum();
            }
        }
        else{
            fillTreeNodesFromKernelEnum();
        }
        rootNode.sortByTitle();
        rootNode.createPresentations(presenter);
    }
    
    private void fillTreeNodesFromEnumPresentation(final RadEnumPresentationDef enumDef){
        for (RadEnumPresentationDef.Item enumItemDef: enumDef.getItems()){  
            final TraceProfileTreeNode<T> node = createNodes((String)enumItemDef.getValue());
            final String fullTitle = 
                enumItemDef.getTitle()==null || enumItemDef.getTitle().isEmpty() ? enumItemDef.getName() : enumItemDef.getTitle();            
            final int titlePos = fullTitle.lastIndexOf(" - ");            
            final String nodeTitle = titlePos<0 ? fullTitle : fullTitle.substring(titlePos+3);
            node.setTitle(nodeTitle);            
        }
    }
    
    private void fillTreeNodesFromKernelEnum(){
        final EnumSet<EEventSource> allEventSources = EnumSet.allOf(EEventSource.class);
        for (EEventSource enumItem: allEventSources){
            createNodes(enumItem.getValue());
        }
    }
    
    private TraceProfileTreeNode<T> createNodes(final String eventSources){
        TraceProfileTreeNode<T> parentNode = rootNode;
        String eventSource;
        final StringBuilder eventSourceFullNameBuilder = new StringBuilder();
        for (StringTokenizer tokens = new StringTokenizer(eventSources, "."); tokens.hasMoreTokens();){
            eventSource = tokens.nextToken();
            if (eventSourceFullNameBuilder.length()==0){
                eventSourceFullNameBuilder.append(eventSource);
            }else{
                eventSourceFullNameBuilder.append('.');
                eventSourceFullNameBuilder.append(eventSource);
            }
            TraceProfileTreeNode<T> currentNode = treeNodesByEventSource.get(eventSourceFullNameBuilder.toString());
            if (currentNode==null){
                final String eventSourceFullName = eventSourceFullNameBuilder.toString();
                currentNode = new TraceProfileTreeNode<>(eventSourceFullName, eventSource, parentNode);
                treeNodesByEventSource.put(eventSourceFullName, currentNode);
            }
            parentNode = currentNode;            
        }
        return parentNode;
    }
    
    private void updateNodesPresentation(){
        presenter.presentWidget(rootNode);
        for (TraceProfileTreeNode<T> node: treeNodesByEventSource.values()){
            presenter.presentWidget(node);
        }
    }
    
    /**
     * Устанавливает профиль трассировки. Метод позволяет установить текущие значения уровней важности событий 
     * {@link TraceProfileTreeNode узлам дерева профиля трассировки}.
     * В параметре передается строка, в которой указаны имена источников событий и уровень важности их сообщений.
     * Если для источника событий уровень важности сообщения не был указан явно, то его значение берется из первого
     * вышестоящего источника событий с явно указанным уровнем важности. 
     * В начале строки указывается уровень важности по умолчанию для источников событий верхнего уровня иерархии.
     * Общий формат строки с профилем трассировки:
     * <p>
     * {@code <Default event severity name>[;<event source name>=<event severity name>[;<event source name>=<event severity name>[;...]]]}
     * </p>
     * Пример строки с профилем трассировки:
     * <p>
     * {@code Error;Arte.DefManager=Debug}
     * </p>
     * Если в качестве параметра передано <code>null</code> или пустая строка, то уровень важности сообщений 
     * у всех источников событий будет сброшен (отнаследован), а уровень важности сообщений, который используется по умолчанию
     * источниками событий верхнего уровня, будет принят равным {@link org.radixware.kernel.common.enums.EEventSeverity.EEventSeverity#NONE "None"}.
     * Неизвестные источники событий пропускаются.
     * <p>
     * Сразу после вызова данного метода, метод {@link #isEdited() isEdited} будет возвращать <code>false</code>.
     * После установки новых значений уровней важности для всех узлов в дереве провиля трассировки будет вызван метод 
     * {@link ITraceProfileTreePresenter#presentWidget(TraceProfileTreeNode) }.
     * </p>
     * @param profileAsStr строковое представление профиля трассировки
     * @throws  org.radixware.kernel.common.exceptions.WrongFormatError если переданная строка имеет неправильный формат или 
     * не найдено описание для указанного в ней ровеня важности сообщения.
     * @see #getProfile() 
     */    
    public void setProfile(final String profileAsStr){
        setProfileImpl(profileAsStr, true);
    }
    
    private void setProfileImpl(final String profileAsStr, final boolean updatePresentation){
        isEdited = false;
        rootNode.resetChildrenEventSeverity();
        if (profileAsStr==null || profileAsStr.isEmpty()){            
            rootNode.setEventSeverity(eventSeverityByName.get(EEventSeverity.NONE.getName()));
        }
        else{
            final StringTokenizer tokens = new StringTokenizer(profileAsStr, ";");
            if (!tokens.hasMoreTokens()) {
                throw new WrongFormatError("Wrong format of trace profile's string. String is \"" + profileAsStr + "\"", null);
            }
            rootNode.setEventSeverity(getEventSeverityByName(tokens.nextToken(),profileAsStr));
            notifyListeners(rootNode);
            while (tokens.hasMoreTokens()) {
                final String token = tokens.nextToken();
                if (token.length() == 0) {
                    break;
                }
                final int i = token.indexOf('=');
                if (i <= 0) {
                    throw new WrongFormatError("Wrong format of trace profile's item. Event source string and '=' are required. Item is \"" + profileAsStr + "\"", null);
                }
                final String source = token.substring(0, i);
                final String severityAndOptsStr = token.substring(i + 1);
                int indexOfOptsStart = severityAndOptsStr.indexOf('[');
                final String severityStr;
                final String optsStr;
                if (indexOfOptsStart > 0) {
                    severityStr = severityAndOptsStr.substring(0, indexOfOptsStart);
                    optsStr = severityAndOptsStr.substring(indexOfOptsStart + 1, severityAndOptsStr.length() - 1);
                } else {
                    severityStr = severityAndOptsStr;
                    optsStr = "";
                }
                final TraceProfileTreeNode<T> node = treeNodesByEventSource.get(source);
                if (node==null){
                    final String message = environment.getMessageProvider().translate("TraceMessage", "Omitting unknown event source \'%s\' in trace profile \'%s\'");
                    environment.getTracer().warning(String.format(message, source, profileAsStr));
                    continue;
                }
                node.setEventSeverity(getEventSeverityByName(severityStr, profileAsStr));
                node.setOptions(new TraceProfile.EventSourceOptions(optsStr));
            }
        }
        if (updatePresentation){
            updateNodesPresentation();
        }
    }    
    
    /**
     * Возвращает строковое представление профиля трассировки. Метод позволяет получить текущие значения уровней важности событий, явно заданные
     * с помощью вызова метода {@link #setTraceProfile(java.lang.String) setTraceProfile} или при помощи GUI-средств редактора.
     * В начале строки всегда указан уровень важности по умолчанию для источников событий верхнего уровня иерархии.
     * Общий формат строки с профилем трассировки:
     * <p>
     * {@code <Default event severity name>[;<event source name>=<event severity name>[;<event source name>=<event severity name>[;...]]]}
     * </p>
     * Пример строки с профилем трассировки:
     * <p>
     * {@code Error;Arte.DefManager=Debug}
     * </p>
     * @return строковое представление профиля трассировки. Значение не может быть <code>null</code> или пустой строкой.
     * @see #setProfile(java.lang.String) 
     */    
    public String getProfile(){
        return rootNode.getTraceProfileAsStr();
    }
    
    private EventSeverity getEventSeverityByName(final String name, final String profileStr){
        EventSeverity eventSeverity = eventSeverityByName.get(name);
        if (eventSeverity==null){
            throw new WrongFormatError("Unknown event severity \'"+name+"\' in trace profile \'"+profileStr+"\'");
        }
        return eventSeverity;
    }

    /**
     * Добавляет обработчик изменения уровня важности события пользователем.
     * @param listener инстанция обработчика, который необходимо зарегистрировать
     * @see #removeListener(IEventSeverityChangeListener) 
     */    
    public void addListener(final ITraceProfileEditor.IEventSeverityChangeListener listener){
        if (listeners==null){
            listeners = new ArrayList<>();
        }
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }
    
    /**
     * Удаляет обработчик изменения уровня важности события пользователем.
     * @param listener инстанция обработчика, зарегистрированного в методе {@link #addListener(IEventSeverityChangeListener) }
     * @see #addListener(IEventSeverityChangeListener) 
     */    
    public void removeListener(final ITraceProfileEditor.IEventSeverityChangeListener listener){
        if (listeners!=null){
            listeners.remove(listener);
        }
    }
    
    private void notifyListeners(final TraceProfileTreeNode node){
        if (listeners!=null){
            for (ITraceProfileEditor.IEventSeverityChangeListener listener: listeners){
                listener.afterChangeEventSeverity(node.getEventSource(), node.eventSeverityWasInherited() ? null : node.getEventSeverity().getValue(), node.getOptions().toString());
            }
        }
    }

    /**
     * Изменение минимального уровня важности сообщения у указанного источника событий.
     * Служит для обработки события изменения уровня важности сообщения пользователем при помощи графических компонент.
     * Этот метод устанавливает в узле дерева профиля трассировки, соответствующего указанному источнику событий, новое значение 
     * миниального уровня важности события или устанавливает признак наследования значения из вышестоящего узла.
     * Для тех узлов дерева профиля трассировки, состояние которых было изменено будет вызван метод
     * {@link ITraceProfileTreePresenter#presentWidget(TraceProfileTreeNode) }. 
     * По окончании работы метода срабатывают 
     * {@link ITraceProfileEditor.IEventSeverityChangeListener обработчики изменения уровня важности события пользователем}.
     * @param eventSource имя источника событий или <code>null</code>, если требуется изменить 
     * минимальный уровень важности сообщения, используемый источниками событий верхнего уровня по умолчанию.
     * @param newValue имя нового значения минимального уровня важности сообщения или <code>null</code>, если
     * для данного источника событий нужно использовать значение уровеня важности, взятое из источника событий верхнего уровня
     * (т.е. использовать наследование значения).
     */
    public void changeEventSeverity(final String eventSource, final String newValue, final String options){        
        final TraceProfileTreeNode<T> node = eventSource==null ? rootNode : treeNodesByEventSource.get(eventSource);
        final EventSeverity eventSeverity = newValue==null ? null : eventSeverityByName.get(newValue);
        
        final boolean optionsChanged = !OPTOINS_ARE_UNSUPPORTED.equals(options) && !node.getOptions().toString().equals(options);
        final boolean severityChanged = node.eventSeverityWasInherited() ? eventSeverity!=null : node.getEventSeverity()!=eventSeverity;
        if (optionsChanged || severityChanged) {
            isEdited = true;
            node.changeEventSeverity(eventSeverity, OPTOINS_ARE_UNSUPPORTED.equals(options) ? null : options, presenter);
            notifyListeners(node);
        }        
    }
    
    /**
     * Возвращает признак того, что уровень важности сообщения был изменен пользователем.
     * @return <code>true</code>, если пользователь изменил уровень важности события для хотябы одного источника, иначе - <code>false</code>
     */    
    public boolean isEdited(){
        return isEdited;
    }
    
    /**
     * Устанавливает набор источников событий, с запрещенной трассировкой. 
     * Метод позволяет указать источники событий, сообщения от которых не должны попадать в трассу.
     * Для всех источников событий, имена которых указаны в наборе, уровень важности событий будет установлен равным
     * {@link org.radixware.kernel.common.enums.EEventSeverity.EEventSeverity#NONE None (без трассировки)} и запрещено его изменение,
     * у всех вложенных источников событий уровень важности будет помечен как унаследованный и его редактирование также будет запрещено.
     * Вызов этого метода не влияет на результат, возвращаемый методом {@link #isEdited() isEdited}.
     * @param eventSources набор c именами источников событий
     */    
    public void setRestrictedEventSources(final Collection<String> eventSources){
        final boolean restrictionsChanged = 
            (eventSources==null && !restrictedEventSources.isEmpty()) || 
            (eventSources!=null && (eventSources.size()!=restrictedEventSources.size() || !restrictedEventSources.containsAll(eventSources) ));
        if (restrictionsChanged){
            final EventSeverity noTracing = eventSeverityByName.get(EEventSeverity.NONE.getName());
            TraceProfileTreeNode<T> node;
            for (Map.Entry<String,TraceProfileTreeNode<T>> entry: treeNodesByEventSource.entrySet()){
                node = entry.getValue();
                if (eventSources!=null && eventSources.contains(entry.getKey())){
                    if (!node.isReadOnly() || node.getEventSeverity()!=noTracing){
                        node.setReadOnly(true);
                        node.setEventSeverity(noTracing);
                        node.resetChildrenEventSeverity();
                    }
                }
                else if (node.isReadOnly()){
                    node.setReadOnly(false);
                    node.setEventSeverity(null);
                    node.resetChildrenEventSeverity();                    
                }
            }
            updateNodesPresentation();
            restrictedEventSources.clear();
            if (eventSources!=null){
                restrictedEventSources.addAll(eventSources);
            }
        }
    }
    
    /**
     * Пересоздает узлы дерева.
     * Метод вызывает {@link ITraceProfileTreePresenter#destroyPresentations()} после чего
     * перечитывает набор источников событий, который мог измениться в результате подъема версии, и
     * по этому набору создает новые инстанции узлов дерева профиля трассировки.
     * Текущий набор источников событий, для которых трассировка запрещена, остается без изменений.
     * В параметре метода можно указать новый профиль трассировки, который будет установлен после обновления набора источников событий.
     * Если параметр равен <codr>null</code>, то текущие значения уровней важности событий остаются без изменений.
     * @param profileAsStr строковое представление профиля трассировки, который следует установить после обновления набора источников событий.
     * Если значение равно <codr>null</code>, то будет установлен тот профиль трассировки, который был на момент вызова метода.
     */
    public void rereadEventSources(final String profileAsStr){
        final String currentProfile = profileAsStr==null ? getProfile() : profileAsStr;
        final boolean wasEdited = isEdited;
        final List<String> currentRestrictedEventSources = 
                new LinkedList<>(restrictedEventSources);
        presenter.destroyPresentations();
        treeNodesByEventSource.clear();
        eventSeverityByName.clear();
        restrictedEventSources.clear();        
        open();
        setProfileImpl(currentProfile,false);
        isEdited = wasEdited;
        if (!currentRestrictedEventSources.isEmpty()){
            setRestrictedEventSources(currentRestrictedEventSources);
        }else{
            updateNodesPresentation();
        }        
    }
    
    /**
     * Проверка на наличие дополнительных настроек трассировки.
     * Метод позволяет проверить поддерживает ли указанный источник событий дополнительные настройки трассировки.
     * @param eventSource источник событий
     * @return <code>true</code>, если указанный источник событий поддерживает дополнительные настройки трассировки.
     */
    public static boolean eventSourceHasExtOptions(final String eventSource){
        if (eventSource!=null){
            try{
                return EVENT_SOURCE_WITH_EXT_TRACE_OPTIONS.contains(EEventSource.getForValue(eventSource));
            }catch(NoConstItemWithSuchValueError error){
                return false;
            }
            
        }
        return false;
    }
    
    /**
     * Редактирование дополнительных настроек трассировки.
     * Метод открывает редактор дополнительных настроек трассировки для указанного источника событий,
     * если данный источник событий поддерживает дополнительные настройки.
     * Вызывается при двойном клике по ячейке в колонке дополнительных настроек дерева профиля трассировки.
     * @param eventSource источник событий
     */
    public void editExtOptions(final String eventSource){
        if (eventSource!=null){
            EEventSource eventSourceEnumItem;
            try{
                eventSourceEnumItem = EEventSource.getForValue(eventSource);
            }catch(NoConstItemWithSuchValueError error){
                return;
            }
            final TraceProfileTreeNode<T> node = treeNodesByEventSource.get(eventSource);
            if (node!=null && !node.eventSeverityWasInherited()){
                TraceProfile.EventSourceOptions currentOptions = node.getOptions();
                if (currentOptions==null){
                    currentOptions = new TraceProfile.EventSourceOptions(Collections.<String,Object>emptyMap());
                }
                EEventSeverity eventSeverity;
                if (node.getEventSeverity()==null){
                    eventSeverity = EEventSeverity.NONE;
                }else{
                    final String severityName = node.getEventSeverity().getValue();
                    try{
                        eventSeverity = EEventSeverity.getForName(severityName);
                    }catch(NoConstItemWithSuchValueError error){
                        eventSeverity = EEventSeverity.NONE;
                    }
                }
                final ITraceProfileEventSourceOptionsEditor editor = 
                    presenter.createEventSourceOptionsEditor(eventSourceEnumItem, eventSeverity, currentOptions);
                if (editor!=null && editor.execDialog()==IDialog.DialogResult.ACCEPTED){
                    final TraceProfile.EventSourceOptions newOptions = editor.getOptions();
                    if (!currentOptions.toString().equals(newOptions.toString())){
                        isEdited = true;
                        final TraceProfileTreeController.EventSeverity currentSeverity;
                        if (node.eventSeverityWasInherited()){
                            currentSeverity = null;
                        }else{
                            currentSeverity = node.getEventSeverity();
                        }
                        node.changeEventSeverity(currentSeverity, newOptions.toString(), presenter);
                        notifyListeners(node);
                    }
                }
            }
        }      
    }
    
    /**
     * Возвращает набор имен всех источников событий, отображаемых на данный момент в редакторе.
     * @return набор имен всех источников событий. Не может быть <code>null</code>.
     */    
    public Collection<String> getEventSources(){
        return Collections.<String>unmodifiableCollection(treeNodesByEventSource.keySet());
    }
    
    /**
     * Возвращает корневой узел дерева профиля трассировки.
     * @return узел, содержищий минимальный уровень важности сообщения, используемый 
     * источниками событий верхнего уровня по умолчанию. Не может быть <code>null</code>.
     */
    public TraceProfileTreeNode<T> getRootNode(){
        return rootNode;
    }

    /**
     * Возвращает окружение клиента.
     * @return окружение клиента, переданное в {@link #TraceProfileTreeController(IClientEnvironment, ITraceProfileTreePresenter)  конструкторе}.
     * Не может быть <code>null</code>.
     */
    public IClientEnvironment getEnvironment(){
        return environment;
    }
    
    public static void clearCache(final IClientEnvironment environment){
        EventSeverityItemsProvider.releaseInstance(environment);
    }
    
    public static void clearCache(final IClientApplication app){
        EventSeverityItemsProvider.releaseInstances(app);
    }
}
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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ResponseListener;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.ResolvableReference;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.GetObjectTitlesRs;

/**
 * Класс позволяет выполнить запрос на сервер для получения заголовков,
 * указанных объектов в заданной таблице. Позволяет сформировать список
 * идентификаторов объектов, а также задать презентацию редактора или селектора,
 * которая будет использоваться при вычислении заголовка конкретного объекта.
 *
 */
public final class EntityObjectTitlesProvider {

    private static class InternalResponseListener extends ResponseListener {

        public InternalResponseListener(final IClientEnvironment environment) {
            super(environment);
        }

        public void clear() {
            closeActiveRequestHandles(true);
        }
    }
    private final Id tableId;
    private final Id defaultPresentationId;
    private final IContext.Abstract defaultContext;
    private final IClientEnvironment environment;
    private GetObjectTitlesRq.Objects objects = GetObjectTitlesRq.Objects.Factory.newInstance();
    //private final List<ObjectTitleRequestParams> objects = new ArrayList<ObjectTitleRequestParams>();
    private EntityObjectTitles objectTitles;
    private final InternalResponseListener responseListener;
    private GetObjectTitlesRequestHandle asyncRequestHandle;

    /**
     * Конструктор создает инстанцию для получения заголовков объекторв в
     * заданной таблице.
     *
     * @param environment - Окружение
     * @param tableId - Идентификатор таблицы.
     */
    public EntityObjectTitlesProvider(final IClientEnvironment environment, final Id tableId) {
        this(environment, tableId, (Id)null);
    }

    /**
     * Конструктор создает инстанцию для получения заголовков объекторв в
     * заданной таблице. Позволяет указать презентацию редактора или селектора,
     * которая будет по умолчанию использоваться для получения формата заголовка
     * объекта.
     *
     * @param environment - Окружение
     * @param tableId- Идентификатор таблицы.
     * @param presentationId - Идентификатор презентации редактора или
     * селектора.
     */
    public EntityObjectTitlesProvider(final IClientEnvironment environment, final Id tableId, final Id presentationId) {
        this.tableId = tableId;
        this.defaultPresentationId = presentationId;
        defaultContext = null;
        this.environment = environment;
        responseListener = new InternalResponseListener(environment);
    }
    
    /**
     * Конструктор создает инстанцию для получения заголовков объекторв в
     * заданной таблице. Позволяет указать контекст, который будет по умолчанию использоваться 
     * для получения формата заголовка объекта.
     * @param environment - Окружение
     * @param tableId- Идентификатор таблицы.
     * @param context - Контекст
     */
    public EntityObjectTitlesProvider(final IClientEnvironment environment, final Id tableId, final IContext.Abstract context) {
        this.tableId = tableId;
        this.defaultPresentationId = null;
        defaultContext = context;
        this.environment = environment;
        responseListener = new InternalResponseListener(environment);
    }    

    private int findObjectRequest(final Pid pid) {
        for (int i = 0, size = objects.sizeOfItemArray(); i < size; i++) {
            if (objects.getItemList().get(i).getPID().equals(pid.toString())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return Количество объектов в запросе
     */
    public int size() {
        return objects.sizeOfItemArray();
    }

    /**
     * @return Возвращает true, когда в запросе нет объектов
     */
    public boolean isEmpty() {
        return objects.sizeOfItemArray() == 0;
    }

    /**
     * Очистить список объектов в запросе. Метод очистищает результаты
     * предыдущего запроса, а также прерывает выполнение асинхронного запроса на
     * получение заголовков.
     */
    public void clear() {
        objects = GetObjectTitlesRq.Objects.Factory.newInstance();
        invalidate();
    }

    /**
     * Добавить идентификатор объекта, для получения его заголовка в презентации
     * по умолчанию. Метод очистищает результаты предыдущего запроса, а также
     * прерывает выполнение асинхронного запроса на получение заголовков.
     *
     * @param pid - Идентификатор объекта
     */
    public void addEntityObjectPid(final Pid pid) {
        final int index = findObjectRequest(pid);
        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
            if (item.isSetContext()) {
                item.unsetContext();
            }
        }
        item.setPID(pid.toString());
        invalidate();
    }

    /**
     * Добавить идентификатор объекта, для получения его заголовка в указанной
     * презентации редактора или селектора. Метод очистищает результаты
     * предыдущего запроса, а также прерывает выполнение асинхронного запроса на
     * получение заголовков.
     *
     * @param pid - Идентификатор объекта
     * @param presentationId - Идентификатор презентации редактора или селектора
     */
    public void addEntityObjectPid(final Pid pid, final Id presentationId) {
        int index = findObjectRequest(pid);
        final RadPresentationDef presentation;
        if (presentationId.getPrefix() == EDefinitionIdPrefix.SELECTOR_PRESENTATION) {
            presentation = environment.getDefManager().getSelectorPresentationDef(presentationId);
        } else if (presentationId.getPrefix() == EDefinitionIdPrefix.EDITOR_PRESENTATION) {
            presentation = environment.getDefManager().getEditorPresentationDef(presentationId);
        } else {
            throw new DefinitionError("Can't find presentation #" + presentationId.toString());
        }
        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
        }        
        item.setPID(pid.toString());
        item.setContext(createContextForPresentation(presentation.getOwnerClassId(), presentationId));
        invalidate();
    }

    /**
     * Добавить идентификатор объекта, для получения его заголовка в указанной
     * презентации редактора или селектора. Метод очистищает результаты
     * предыдущего запроса, а также прерывает выполнение асинхронного запроса на
     * получение заголовков.
     *
     * @param pid - Идентификатор объекта
     * @param presentationId - Идентификатор презентации редактора или селектора
     * @param presentationClassId - Идентификатор класса, в котором находится
     * презентации редактора или селектора
     */
    public void addEntityObjectPid(final Pid pid, final Id presentationId, final Id presentationClassId) {
        final int index = findObjectRequest(pid);
        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
        }
        item.setPID(pid.toString());
        item.setContext(createContextForPresentation(presentationClassId, presentationId));
        invalidate();
    }

    /**
     * Добавить идентификатор объекта, для получения его заголовка в указанной
     * презентации редактора или селектора. Метод очистищает результаты
     * предыдущего запроса, а также прерывает выполнение асинхронного запроса на
     * получение заголовков.
     *
     * @param pid - Идентификатор объекта
     * @param editorPresentation - Презентация редактора или селектора
     */
    public void addEntityObjectPid(final Pid pid, final RadPresentationDef presentation) {
        final int index = findObjectRequest(pid);
        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
        }
        item.setPID(pid.toString());
        item.setContext(createContextForPresentation(presentation.getOwnerClassId(), presentation.getId()));
        invalidate();
    }
    
    /**
     * Добавить идентификатор объекта, для получения его заголовка в указанном контексте.
     * Метод очистищает результаты предыдущего запроса, а также прерывает выполнение 
     * асинхронного запроса на получение заголовков.
     *
     * @param pid - Идентификатор объекта
     * @param context - Контекст использования объекта
     */    
    public void addEntityObjectPid(final Pid pid, final IContext.Abstract context) {
        final int index = findObjectRequest(pid);
        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
        }
        item.setPID(pid.toString());
        item.setContext(context.toXml());
        invalidate();        
    }
    
    private org.radixware.schemas.eas.Context createContextForPresentation(final Id classId, final Id presentationId){        
        final org.radixware.schemas.eas.Context context = org.radixware.schemas.eas.Context.Factory.newInstance();
        if (presentationId.getPrefix() == EDefinitionIdPrefix.SELECTOR_PRESENTATION) {
            final org.radixware.schemas.eas.Context.Selector xmlPresentation = context.addNewSelector();
            xmlPresentation.setClassId(classId);
            xmlPresentation.setPresentationId(presentationId);
        }else if (presentationId.getPrefix() == EDefinitionIdPrefix.EDITOR_PRESENTATION) {
            final org.radixware.schemas.eas.Context.Editor xmlPresentation = context.addNewEditor();
            xmlPresentation.setClassId(classId);
            xmlPresentation.setPresentationId(presentationId);            
        }else{
            throw new IllegalArgumentException("Identifier of editor or selector presentation expected");
        }
        return context;
    }

    /**
     * Добавить ссылку на объект, для получения его заголовка. Если Reference
     * является инстанцией ResolvableReference, то заголовок будет расчитан в
     * соответствующей презентации редактора или селектора Метод очистищает
     * результаты предыдущего запроса, а также прерывает выполнение асинхронного
     * запроса на получение заголовков.
     *
     * @param reference - ссылка на объект.
     */
    public void addEntityObjectReference(final Reference reference) {
        final int index = findObjectRequest(reference.getPid());
        final Pid pid = reference.getPid();
        final Id editorPresentationId, classId;
        if (reference instanceof ResolvableReference) {
            editorPresentationId = ((ResolvableReference) reference).getEditorPresentationId();
            classId = ((ResolvableReference) reference).getClassId();
        } else {
            editorPresentationId = null;
            classId = null;
        }

        final GetObjectTitlesRq.Objects.Item item;
        if (index < 0) {
            item = objects.addNewItem();
        } else {
            item = objects.getItemList().get(index);
        }

        item.setPID(pid.toString());
        if (editorPresentationId != null && classId != null) {
            item.setContext(createContextForPresentation(classId, editorPresentationId));
        }
        invalidate();
    }

    /**
     * Не запрашивать заголовок объекта с указанным идентификатором. Метод
     * очистищает результаты предыдущего запроса, а также прерывает выполнение
     * асинхронного запроса на получение заголовков.
     *
     * @param pid - Идентификатор объекта
     */
    public void removeEntityObjectPid(final Pid pid) {
        int index = findObjectRequest(pid);
        if (index >= 0) {
            objects.removeItem(index);
            invalidate();
        }
    }

    /**
     * Метод позволяет проверить будет ли запрошен заголовок объекта с указанным
     * идентификатором.
     *
     * @param pid - Идентификатор объекта
     * @return true, если для объекта с указанным pid будет запрошен заголовок
     */
    public boolean containsEntityObjectPid(final Pid pid) {
        return findObjectRequest(pid) >= 0;
    }

    /**
     * Позволяет очистить результаты предыдущего запроса, а также прерывает
     * выполнение асинхронного запроса на получение заголовков. Последующий
     * вызов метода getTitles приведет к выполнению запроса.
     */
    public void invalidate() {
        objectTitles = null;
        responseListener.clear();
        asyncRequestHandle = null;
    }

    /**
     * Выполняет запрос на сервер для получения заголовков, указанных объектов и
     * возвращает результат в виде инстанции класса EntityObjectTitles.
     * Результат выполнения запроса кешируется и храниться до тех пор пока не
     * будет изменен список объектов, заголовки которых нужно узнать или не
     * будет вызван метод invalidate
     *
     * @return Результат выполнения запроса на получение заголовка объектов.
     * @throws InterruptedException
     * @throws ServiceClientException
     */
    public EntityObjectTitles getTitles() throws InterruptedException, ServiceClientException {
        if (objectTitles == null) {
            if (asyncRequestHandle != null) {
                if (asyncRequestHandle.isDone()) {
                    try {
                        return asyncRequestHandle.getEntityObjectTitles();
                    } finally {
                        asyncRequestHandle = null;
                    }
                } else {
                    responseListener.clear();
                    asyncRequestHandle = null;
                }
            }
            if (objects == null || objects.getItemList() == null || objects.getItemList().isEmpty()) {
                objectTitles = EntityObjectTitles.EMPTY;
            } else {
                final GetObjectTitlesRs response;
                if (defaultContext==null){
                    response =
                        environment.getEasSession().getEntityTitles(tableId,
                        defaultPresentationId,
                        objects);
                }else{
                    response =
                        environment.getEasSession().getEntityTitles(tableId,
                        defaultContext,
                        objects);                    
                }
                objectTitles = new EntityObjectTitles(environment, response, objects);
            }
        }
        return objectTitles;
    }

    /**
     * Выполняет отложенный запрос на получение списка указанных объектов.
     *
     * @param timeoutSec ограничение на время выполнения запроса в секундах.
     * Если параметр меньше или равен 0, то ограничения нет.
     * @return хэндл запроса
     */
    public GetObjectTitlesRequestHandle getTitlesAsync(final int timeoutSec) {
        responseListener.clear();
        if (defaultContext==null){
            asyncRequestHandle =
                    RequestHandle.Factory.createForGetObjectTitles(environment,
                    tableId,
                    defaultPresentationId,
                    objects);
        }else{
            asyncRequestHandle =
                    RequestHandle.Factory.createForGetObjectTitles(environment,
                    tableId,
                    defaultContext,
                    objects);            
        }
        asyncRequestHandle.addListener(responseListener);
        environment.getEasSession().sendAsync(asyncRequestHandle, timeoutSec);
        return asyncRequestHandle;
    }

    /**
     * Выполняет отложенный запрос на получение списка указанных объектов.
     *
     * @return Хэндл запроса.
     */
    public GetObjectTitlesRequestHandle getTitlesAsync() {
        return getTitlesAsync(0);
    }
}

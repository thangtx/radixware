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
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.ObjectNotAccessibleError;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.client.types.ResolvableReference;
import org.radixware.schemas.eas.GetObjectTitleResultStateEnum;
import org.radixware.schemas.eas.GetObjectTitlesRq;
import org.radixware.schemas.eas.GetObjectTitlesRs;

/**
 * Класс содержит результаты выполнения запроса на получение заголовков объектов.
 */
public class EntityObjectTitles {
    
    public static EntityObjectTitles EMPTY = new EntityObjectTitles(){

        @Override
        public Reference getEntityObjectReference(Pid pid) {
            throw new IllegalArgumentException("No object with pid='"+pid.toString()+"' found");
        }

        @Override
        public String getEntityObjectTitle(Pid pid) throws ObjectNotFoundError, ObjectNotAccessibleError {
            throw new IllegalArgumentException("No object with pid='"+pid.toString()+"' found");
        }

        @Override
        public boolean isEntityObjectAccessible(Pid pid) {
            throw new IllegalArgumentException("No object with pid='"+pid.toString()+"' found");
        }

        @Override
        public boolean isEntityObjectExists(Pid pid) {
            throw new IllegalArgumentException("No object with pid='"+pid.toString()+"' found");
        }
        
    };
    
    private final GetObjectTitlesRs.ObjectTitles objectTitles;
    private final GetObjectTitlesRq.Objects requestedObjects;
    private final IClientEnvironment environment;
    
    private EntityObjectTitles(){
        objectTitles = null;
        requestedObjects = null;
        environment = null;
    }
    
    EntityObjectTitles(final IClientEnvironment environment, final GetObjectTitlesRs response, final GetObjectTitlesRq.Objects objects){
        objectTitles = response.getObjectTitles();
        requestedObjects = objects;
        this.environment = environment;
    }
    
    private GetObjectTitlesRs.ObjectTitles.Item getItemByPid(final Pid pid){
        if (objectTitles!=null && objectTitles.getItemList()!=null){
            for (GetObjectTitlesRs.ObjectTitles.Item item: objectTitles.getItemList()){
                if (pid.toString().equals(item.getPID())){
                    return item;
                }
            }
        }
        throw new IllegalArgumentException("No object with pid='"+pid.toString()+"' found");
    }
    
    private GetObjectTitlesRq.Objects.Item getRequestedObjectByPid(final Pid pid){
        if (requestedObjects!=null){
            for (GetObjectTitlesRq.Objects.Item item: requestedObjects.getItemList()){
                if (item.getPID().equals(pid.toString())){
                    return item;
                }
            }
        }
        throw new IllegalArgumentException("No requested object with pid='"+pid.toString()+"' found");
    }
    
    /**
     * Метод позволяет проверить наличие объекта в Б.Д. на момент выполнения запроса.
     * Метод возвращает true, если объект присутствует в Б.Д., но на получение его заголовка недостаточно прав.
     * @param pid - Идентификатор объекта
     * @return true если объект с указанным идентификатором присутствует в Б.Д. (на момент выполнения запроса) и false, 
     * если объект с указанным идентификатором не найден в Б.Д. 
     * @throws IllegalArgumentException  - Идентификатор объекта отсутствует в резултатах выполнения запроса
     */
    public boolean isEntityObjectExists(final Pid pid){
        return getItemByPid(pid).getState()!=GetObjectTitleResultStateEnum.OBJECT_NOT_FOUND;
    }

    /**
     * Метод позволяет проверить наличие прав на получение заголовка объекта в Б.Д. на момент выполнения запроса.
     * Метод возвращает true, если был успешно получен заголовок указанного объекта.
     * @param pid - Идентификатор объекта
     * @return true если объект с указанным идентификатором присутствует в Б.Д. 
     * и достаточно прав на получение его заголовка. В противном случае метод возвращает false.
     * @throws IllegalArgumentException  - Идентификатор объекта отсутствует в резултатах выполнения запроса
     */
    public boolean isEntityObjectAccessible(final Pid pid){
        return getItemByPid(pid).getState()==GetObjectTitleResultStateEnum.OK;
    }    

    /**
     * Метод позволяет получить заголовок объекта, с указанным идентификатором, из результатов запроса.
     * @param pid - Идентификатор объекта
     * @return Заголовок объекта из результатов запроса на получение заголовков для указанных объектов.
     * @throws ObjectNotFoundError - Объект с указанным идентификатором отсутсвует в Б.Д.
     * @throws ObjectNotAccessibleError  - Недостаточно прав на получение заголовка объекта с указанным идентификатором
     * @throws IllegalArgumentException  - Идентификатор объекта отсутствует в резултатах выполнения запроса
     */
    public String getEntityObjectTitle(final Pid pid) throws ObjectNotFoundError, ObjectNotAccessibleError{
        final GetObjectTitlesRs.ObjectTitles.Item item = getItemByPid(pid);
        switch (item.getState().intValue()){
            case GetObjectTitleResultStateEnum.INT_OBJECT_NOT_FOUND:
                throw new ObjectNotFoundError(environment,pid);
            case GetObjectTitleResultStateEnum.INT_ACCESS_DENIED:
                throw new ObjectNotAccessibleError(environment,pid);
            default:
                return item.getTitle();
        }
    }

    /**
     * Метод позволяет получить ссылку на объект, с указанным идентификатором, на основе результатов запроса.
     * Если на момент выполнения запроса объект отсутствовал в Б.Д. или не было прав на получение его заголовка, 
     * то у полученной ссылки метод isBroken() будет возвращать true.
     * @param pid - Идентификатор объекта
     * @return Ссылка  на объект.
     * @throws IllegalArgumentException  - Идентификатор объекта отсутствует в резултатах выполнения запроса
     */    
    public Reference getEntityObjectReference(final Pid pid){
        final GetObjectTitlesRs.ObjectTitles.Item objectTitlesItem = getItemByPid(pid);
        final GetObjectTitlesRq.Objects.Item objectItem = getRequestedObjectByPid(pid);
        switch (objectTitlesItem.getState().intValue()){
            case GetObjectTitleResultStateEnum.INT_OBJECT_NOT_FOUND:
                return new Reference(pid,pid.toString(),pid.toString());
            case GetObjectTitleResultStateEnum.INT_ACCESS_DENIED:
                return new Reference(pid,pid.toString(),pid.toString());
            default:
                if (objectItem.isSetContext() && objectItem.getContext().isSetEditor()){
                    final org.radixware.schemas.eas.Context.Editor xmlPresentation = objectItem.getContext().getEditor();
                    final ResolvableReference reference = 
                        new ResolvableReference(pid, xmlPresentation.getClassId(), xmlPresentation.getPresentationId());
                    reference.setTitle(objectTitlesItem.getTitle());
                    return reference;
                }
                else{
                    return new Reference(pid, objectTitlesItem.getTitle());
                }
        }
    }
}

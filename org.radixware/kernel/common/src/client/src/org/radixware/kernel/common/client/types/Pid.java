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

import java.util.ArrayList;
import org.radixware.kernel.common.client.eas.IEasSession;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.Id;

/**
 * Класс идентификатора сущности. Каждая сущность внутри одной группы имеет свой
 * уникальный идентификатор.
 *
 *
 */
public class Pid extends org.radixware.kernel.common.types.Pid{

    /**
     * Конструирует идентификатор сущности по его строковому представлению
     *
     * @param classDef - Презентация класса, которому принадлежит сущность
     * @param str - строковое представление идентификатора
     */
    public Pid(final RadClassPresentationDef classDef, final String str) {
        this(classDef.getTableId(), str);        
    }

    /**
     * Конструирует идентификатор сущности по его строковому представлению
     *
     * @param id - идентификатор таблицы, сущности или прикладного класса
     * @param str - строковое представление идентификатора
     * @param defManager - Менеджер дефиниций. Используется если передан
     * идентификатор прикладного класса
     */    
    public Pid(final Id id, final String str, final DefManager defManager) {
        this(getTableId(id, defManager),str);
    }

    /**
     * Конструирует идентификатор сущности по его строковому представлению
     *
     * @param tableId - идентификатор таблицы, данные из которой предоставляет
     * сущность
     * @param str - строковое представление идентификатора
     */
    public Pid(final Id tableId, final String str) {
        super(getTableId(tableId), str);
        checkRawPidFormat(str);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param key - массив значений первичных ключей.
     */
    public Pid(final Id tableId, final ArrayList<Object> key) {
        super(getTableId(tableId),key);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal1 - значение ключа
     * @param propVal2 - значение ключа
     * @param propVal3 - значение ключа
     */
    public Pid(
            final Id tableId,
            final Object propVal1,
            final Object propVal2,
            final Object propVal3) {
        super(getTableId(tableId),propVal1,propVal2,propVal3);        
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значениям
     * первичных ключей.
     *
     * @param tableId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal1 - значение ключа
     * @param propVal2 - значение ключа
     */
    public Pid(
            final Id tableId,
            final Object propVal1,
            final Object propVal2) {
        super(getTableId(tableId),propVal1,propVal2);
    }

    /**
     * Конструирует идентификатор сущности по идентификатору таблицы и значению
     * первичного ключа.
     *
     * @param entityId - идентификатор таблицы, данные которой предоставляет
     * сущность
     * @param propVal - значение ключа
     */
    public Pid(final Id entityId, final Object propVal) {
        super(getTableId(entityId),propVal);
    }

    public Pid(final Pid pid) {
        super(pid);
        checkRawPidFormat(toString());
    }

    public static Pid fromStr(String asStrWithTableId) {
        int index = asStrWithTableId.indexOf("\n");
        if (index < 0) {
            return null;
        } else {
            String tableIdAsStr = asStrWithTableId.substring(0, index);
            String pidAsStr = asStrWithTableId.substring(index + 1);
            return new Pid(Id.Factory.loadFrom(tableIdAsStr), pidAsStr);
        }

    }

    /**
     * Два идентификатора сущности считаются одинаковыми, когда совпадают их
     * строковые представления и идентификаторы их таблиц.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Pid) {
            return super.equals(o);
        }
        if (o instanceof Reference) {
            Reference ref = (Reference) o;
            return ref.getPid() != null && getTableId().equals(ref.getPid().getTableId()) && toString().equals(ref.getPid().toString());
        }
        return false;
    }

    /**
     * Метод позволяет получить заголовок сущности в заданной
     * {@link org.radixware.kernel.explorer.meta.RadEditorPresentationDef презентации}.
     * При выполнении метода отправляется запрос на сервер.
     *
     * @param presentationId - идентификатор
     * {@link org.radixware.kernel.explorer.meta.RadEditorPresentationDef презентации редактора}.
     * @return заголовок сущности с данным идентификатором.
     * @throws ServiceClientException ошибка при выполнении запроса (например,
     * не найдена сущность с заданным идентификатором или нет прав на ее чтение)
     * @throws InterruptedException операция получения заголовка была прервана
     * @see ISession#getEntityTitleByPid(String, String)
     */
    public String getEntityTitleInPresentation(final IEasSession session, final Id presentationId) throws ServiceClientException, InterruptedException {
        return session.getEntityTitleByPid(getTableId(), presentationId, toString());
    }

    /**
     * Метод позволяет получить заголовок сущности в null null     {@link org.radixware.kernel.explorer.meta.RadEditorPresentationDef презентации редактора},
     *  {@link org.radixware.kernel.explorer.meta.RadClassPresentationDef#getDefaultSelectorPresentation()  дефолтной презентации селектора}.
     * При выполнении метода отправляется запрос на сервер.
     *
     * @param presentationId - идентификатор
     * {@link org.radixware.kernel.explorer.meta.RadEditorPresentationDef презентации редактора}.
     * @return заголовок сущности с данным идентификатором.
     * @throws ServiceClientException ошибка при выполнении запроса (например,
     * не найдена сущность с заданным идентификатором или нет прав на ее чтение)
     * @throws InterruptedException операция получения заголовка была прервана
     * @see Session#getEntityTitleByPid(String, String)
     */
    public String getDefaultEntityTitle(final IEasSession session) throws ServiceClientException, InterruptedException {
        return session.getEntityTitleByPid(getTableId(), null, toString());
    }
    
    private static void checkRawPidFormat(final String rawPid){
        if (rawPid == null || rawPid.length() == 0) {            
            throw new WrongFormatError("Wrong Pid string format. Pid string is \"" + String.valueOf(rawPid) + "\"", null);
        }        
        int start = 0;
        int i = rawPid.indexOf(PK_VAL_DELIMETER);
        while (i != -1) {
            if (i < 1 || rawPid.charAt(i - 1) != '\\') {
                start = i + 1;
            }
            i = rawPid.indexOf(PK_VAL_DELIMETER, i + 1);
        }
        if (start >= rawPid.length()) {
            throw new WrongFormatError("Wrong Pid string format. Pid string is \"" + String.valueOf(rawPid) + "\"", null);
        }
    }
    
    private static Id getTableId(final Id definitionId){
        if (definitionId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
            return Id.Factory.changePrefix(definitionId, EDefinitionIdPrefix.DDS_TABLE);
        } else if (definitionId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            return definitionId;
        } else{
            throw new IllegalArgumentException("Identifier \'"+String.valueOf(definitionId)+"\' is not table identifier");
        }
    }
    
    private static Id getTableId(final Id definitionId, final DefManager defManager){
        if (definitionId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
            return Id.Factory.changePrefix(definitionId, EDefinitionIdPrefix.DDS_TABLE);
        } else if (definitionId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
            return definitionId;
        } else {
            if (defManager == null) {
                if (definitionId.getPrefix() == null) {
                    throw new IllegalArgumentException("Identifier of table or entity class expected but unknown identifier \"" + definitionId.toString() + "\" got");
                }
                throw new IllegalArgumentException("Identifier of table or entity class expected but " + definitionId.getPrefix().name() + " identifier got");
            } else if (definitionId.getPrefix() != EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
                throw new IllegalArgumentException("Identifier of table or application class expected but " + definitionId.getPrefix().name() + " identifier got");
            }
            //look for entity class
            Id currentId = definitionId;
            for (;;) {
                currentId = defManager.getRepository().getAncestorId(currentId);
                if (currentId != null && currentId.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS) {
                    return Id.Factory.changePrefix(currentId, EDefinitionIdPrefix.DDS_TABLE);
                } else {
                    if (currentId == null || currentId.getPrefix() != EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
                        throw new IllegalArgumentException("No entity class found for application class with identider " + definitionId.toString());
                    }
                }
            }
        }        
    }    
}
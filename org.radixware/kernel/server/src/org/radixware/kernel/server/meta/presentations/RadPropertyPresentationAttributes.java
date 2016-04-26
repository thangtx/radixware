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

package org.radixware.kernel.server.meta.presentations;

import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.types.Id;

/**
 * Класс презентационных атрибутов свойства. Набор атрибутов свойства объекта
 * сущности, перекрытых в презентации редактора. Если метод данного класса
 * возвращает
 * <code>null</code>, это означает что значение атрибута наследуется из базовой
 * презентации редактора или из дефиниции свойства.
 *
 */
public class RadPropertyPresentationAttributes {

    private final Id propertyId;
    private final EEditPossibility editPossibility;
    private final Boolean presentable;
    private final Boolean mandatory;

    /**
     * /**
     * Конструктор класса. Вызов генерируется radix-дизайнером. Если какой-либо
     * из атрибутов, переданных в конструкторе, равен
     * <code>null</code>, то фактическое значение атрибута будет взято из
     * {@link RadEditorPresentationDef#getBasePresentation() базовой презентации редактора},
     * а если ее нет, то из
     * {@link org.radixware.kernel.server.meta.clazzes.RadPropDef соответствующей дефиниции свойства}.
     *
     * @param propertyId идентификатор свойства, презентационные атрибуты,
     * которого содержаться в данном наборе. Не может быть <code>null</code>
     * @param presentable указание на то, является ли данное свойство
     * презентационным (передается ли его значение на клиент)
     * @param editPossibility настройка, определяющая возможность изменять
     * значение свойства на стороне клиента
     * @param mandatory указание на то, может ли клиент
     * установить <code>null</code> в качестве значения свойства
     */
    public RadPropertyPresentationAttributes(final Id propertyId,
            final Boolean presentable,
            final EEditPossibility editPossibility,
            final Boolean mandatory) {
        this.propertyId = propertyId;
        this.presentable = presentable;
        this.editPossibility = editPossibility;
        this.mandatory = mandatory;
    }

    /**
     * Получение идентификатора свойства. Метод возвращает идентификатор
     * дефиниции свойства объекта сущности, для которого задан данный набор
     * атрибутов.
     *
     * @return идентификатор свойства. Не может быть <code>null</code>
     */
    public Id getPropertyId() {
        return propertyId;
    }

    /**
     * Получение настройки, определяющей возможность изменять значение свойства.
     *
     * @return значение настройки, определяющей возможность изменять значение
     * свойства или <code>null</code>, если значение атрибута не задано
     * (наследуется)
     */
    public EEditPossibility getEditPossibility() {
        return editPossibility;
    }

    /**
     * Получение признака, определяющего возможность задавать свойству значение
     * <code>null</code>
     *
     * @return <code>true</code>, если свойство не может иметь
     * значение <code>null</code> (должно быть всегда задано),
     * <code>false</code>, если может и <code>null</code>, значение настройки не
     * определено (наследуется).
     */
    public Boolean isMandatory() {
        return mandatory;
    }

    /**
     * Получение признака, определяющего возможность передачи значения свойства
     * на клиент
     *
     * @return <code>true</code>, если значение свойства может быть передано на
     * клиент, <code>false</code>, если передача значения свойства на клиент
     * запрещена и <code>null</code>, если значение настройки не определено
     * (наследуется).
     */
    public Boolean isPresentable() {
        return presentable;
    }
}
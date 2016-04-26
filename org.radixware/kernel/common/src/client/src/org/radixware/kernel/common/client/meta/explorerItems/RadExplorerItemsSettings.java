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

package org.radixware.kernel.common.client.meta.explorerItems;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;

/**
 * Набор настроек отображения элементов проводника.
 * Задает порядок отображения и/или видимость подэлементов внутри определенного элемента верхнего уровня.
 */
public final class RadExplorerItemsSettings {
    
    /**
     * Класс для хранения настроек видимости элемента проводника.
     */
    public final static class ItemVisibility{
        /**
         * Идентификатор элемента проводника.
         */
        public final Id explorerItemId;
        /**
         * Признак того, что элемент должен быть показан в дереве проводника.
         */
        public final boolean isVisible;
        
        /**
         * Конструктор класса.
         * @param itemId идентификатор элемента проводника
         * @param isVisible Если равен <code>true<code>, то данный элемент должен отображаться в дереве проводника.
         * Если <code>false<code> - то элемент должен быть скрыт.
         */
        public ItemVisibility(final Id itemId, final boolean isVisible){
            explorerItemId = itemId;
            this.isVisible = isVisible;
        }
    }
    
    public static final RadExplorerItemsSettings EMPTY = new RadExplorerItemsSettings();
    
    private final Id parentId;
    private final Id[] order;
    private final Map<Id,Boolean> visibility;
    
    /**
     * Конструктор класса.
     * @param parentId идентификатор дефиниции верхнего уровня, к подэлементам котой применяются данные настройки.
     * Не может быть <code>null</code>.
     * @param order массив идентификаторов, который задает порядок отображения элементов проводника. 
     * Если равен <code>null</code>, то порядок отображения элементов в данном наборе настроек не определен.
     * @param visible массив настроек видимости подэлементов. 
     * Если равен <code>null</code>, то видимость элементов не контроллируется данным набором настроек.
     */
    public RadExplorerItemsSettings(final Id parentId, final Id[] order, final ItemVisibility[] itemsVisibility){
        this.parentId = parentId;
        this.order = order;
        if (itemsVisibility==null){
            visibility = Collections.emptyMap();
        }else{
            visibility = new HashMap<>(itemsVisibility.length*2);
            for (ItemVisibility itemVisibility: itemsVisibility){
                visibility.put(itemVisibility.explorerItemId, Boolean.valueOf(itemVisibility.isVisible));
            }
        }
    }
    
    private RadExplorerItemsSettings(){
        parentId = null;
        order = null;
        visibility = Collections.emptyMap();
    }
    
    /**
    * Возвращает идентификатор дефиниции, к подэлементам которой применяются данные настройки. 
    * Если метод возвращает идентификатор презентации редактора, то настройки применяются к дочернем элементам проводника верхнего уровня.
    * Если метод возвращает идентификатор параграфа, то настройки применяются к его дочерним элементам (нерекурсивно).
    * @return идентификатор дефиниции. Не может быть <code>null</code>.
    */
    final public Id getParentExplorerItemId(){
        return parentId;
    }
    
    /**
     * Возвращает массив идентификаторов элементов в порядке их отображения в дереве проводника.
     * Результат работы метода определяет порядок следования элементов проводника в дереве.
     * Если метод возвращает <code>null</code>, то порядок отображения элементов в данном наборе настроек 
     * не определен (но может быть определен в базовом классе дефиниции). Элементы проводника, 
     * идентификаторы которых отсутствуют в данном массиве, отображаются всегда после элементов проводника, 
     * идентификаторы которых в нем есть.
     * @return упорядоченный массив идентификаторов проводника. Может быть <code>null</code>.
     */
    final public Id[] getItemsOrder(){
        return order;
    }
    
    /**
     * Позволяет определить заданы ли настройки видимости для указанного элемента проводника в этом наборе.
     * Метод возвращает <code>true<code> если данный набор содержит настройки видимости элемента проводника 
     * с идентификатором <code>explorerItemId<code>. В этом случае значение настройки видимости 
     * можно получить, вызвав метод {@link #isItemVisible(Id) }.  Если метод возвращает <code>false<code>, то видимость 
     * указанного элемента проводника не контроллируется данным набором (но может быть задана в базовом
     * классе дефиниции) и вызов метода {@link #isItemVisible(Id) } с темже параметром приведет к генерации 
     * исключения <code>IllegalArgumentException<code>
     * @param explorerItemId идентификатор элемента проводника. Не может быть <code>null<code>.
     * @return <code>true<code> если в данном наборе содержится настройки видимости указанного 
     * элемента проводника и <code>false<code> в противном случае
     * @see #isItemVisible(Id)
     */
    final public boolean isItemVisibilityDefined(final Id explorerItemId){
        return visibility.containsKey(explorerItemId);
    }
    
    /**
     * Возвращает значение настройки видимости указанного элемента проводника в дереве.
     * @param explorerItemId идентификатор элемента проводника
     * @return <code>true<code> если элемент проводника должен быть показан в дереве проводника и 
     * <code>false<code> в противном случае
     * @throws IllegalArgumentException генерируется если данный набор не содержит настроек видимости 
     * для указанного элемента проводника
     * @see #isItemVisibilityDefined(Id)
     */
    final public boolean isItemVisible(final Id explorerItemId) throws IllegalArgumentException{
        final Boolean isVisible = visibility.get(explorerItemId);
        if (isVisible==null){
            throw new IllegalArgumentException("Visibility settings was not defined for explorer item with id #"+explorerItemId.toString());
        }else{
            return isVisible.booleanValue();
        }
    }

}

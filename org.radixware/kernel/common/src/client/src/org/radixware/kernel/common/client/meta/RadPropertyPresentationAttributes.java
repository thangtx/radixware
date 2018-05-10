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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EPropertyVisibility;
import org.radixware.kernel.common.types.Id;

/**
 * Класс презентационных атрибутов свойства.
 * Набор атрибутов свойства объекта сущности, перекрытых в презентации редактора.
 * Eсли инстанция этого класса получена из
 * {@link RadEditorPresentationDef#getPropertyAttributesByPropId(org.radixware.kernel.common.types.Id) презентации редактора},
 * то методы будут возвращать конечные не <code>null</code> значения атрибутов свойства (с учетом всех линий наследования), иначе
 * значения, которые были переданы в конструкторе.
 */
public final class RadPropertyPresentationAttributes extends Definition{

    private Id propertyId;
    private EPropertyVisibility visibility;
    private EEditPossibility editPossibility;
    private Boolean mandatory;
    private Id titleId;
    private Id titleOwnerId;
    private TitledDefinition titleSource;
    private RadEditorPresentationDef owner;
    private RadClassPresentationDef contextClassDef;

    /**
     * Конструктор класса.
     * Вызов генерируется radix-дизайнером.
     * Если какой-либо из атрибутов, переданных в конструкторе, равен <code>null</code>, то фактическое значение атрибута будет взято из
     * {@link RadEditorPresentationDef#getBasePresentation() базовой презентации редактора}, а если ее нет, то из 
     * {@link RadPropertyDef соответствующей дефиниции свойства}.
     * @param propertyId идентификатор свойства, презентационные атрибуты, которого содержаться в данном наборе. Не может быть <code>null</code>
     * @param visibility константа, определяющая видимость редактора свойства (или <code>null</code>, если значение атрибута наследуется)
     * @param editPossibility константа определяющая возможность редактирования свойства (или <code>null</code>, если значение атрибута наследуется)
     * @param mandatory указание на возможность устанавливать свойству значение равное <code>null</code>. Если равно <code>true</code>, то значение свойства не может быть равно <code>null</code>, 
     * если равно <code>false</code>, то может, если данный параметр равен <code>null</code>, то значение атрибута наследуется.
     * @param titleId идентификатор мультиязыковой строки, являющийся заголовком свойства (или <code>null</code>, если заголовок наследуется)
     * @param titleOwnerId идентификатор дефиниции, в которой содержится набор мультиязыковых строк, содержащий заголовок свойства (или <code>null</code>, если заголовок наследуется)
     */
    public RadPropertyPresentationAttributes(final Id propertyId,
                                             final EPropertyVisibility visibility,
                                             final EEditPossibility editPossibility,
                                             final Boolean mandatory,
                                             final Id titleId,
                                             final Id titleOwnerId){
        this(propertyId, visibility, editPossibility, mandatory, titleId, titleOwnerId, null, null);
    }
    
    RadPropertyPresentationAttributes(final RadPropertyDef property){
        this(property.getId(),
             EPropertyVisibility.ALWAYS,
             property.getEditPossibility(),
             property.isMandatory(),
             null,null,null,null);
        titleSource = property;
    }    
    
    RadPropertyPresentationAttributes(final RadPropertyPresentationAttributes source,
                                      final RadEditorPresentationDef owner,
                                      final RadClassPresentationDef contextClassDef){
        this(source.propertyId, 
             source.visibility, 
             source.editPossibility, 
             source.mandatory, 
             source.titleId, 
             source.titleOwnerId, owner, contextClassDef);
    }    
    
    private RadPropertyPresentationAttributes(final Id propertyId,
                                              final EPropertyVisibility visibility,
                                              final EEditPossibility editPossibility,
                                              final Boolean mandatory,
                                              final Id titleId,
                                              final Id titleOwnerId,
                                              final RadEditorPresentationDef owner,
                                              final RadClassPresentationDef contextClassDef){
        super(propertyId);
        this.propertyId = propertyId;
        this.visibility = visibility;
        this.editPossibility = editPossibility;
        this.mandatory = mandatory;
        this.titleId = titleId;
        this.titleOwnerId = titleOwnerId;        
        this.owner = owner;
        this.contextClassDef = contextClassDef;
    }    

    /**
     * Получение идентификатора свойства.
     * Метод возвращает идентификатор дефиниции свойства объекта сущности, 
     * для которого задан данный набор атрибутов.
     * @return идентификатор свойства. Не может быть <code>null</code>
     */
    public Id getPropertyId() {
        return propertyId;
    }
    
    private RadPropertyPresentationAttributes getInheritedAttributes(){
        return owner.getBasePresentation().getPropertyAttributesByPropId(propertyId,contextClassDef);
    }

    /**
     * Получение настройки, определяющей возможность изменять значение свойства.
     * @return значение настройки, определяющей возможность изменять значение свойства или <code>null</code>, 
     * если значение атрибута не задано
     */
    public EEditPossibility getEditPossibility() {
        if (editPossibility==null && owner!=null && contextClassDef!=null){
            if (owner.getInheritanceMask().isPresentationPropertyAttributesInherited() &&
                owner.getBasePresentation()!=null && owner.getBasePresentation().isPropertyDefExistsById(propertyId)
               ){
                editPossibility = getInheritedAttributes().getEditPossibility();
            }else{
                editPossibility = owner.getPropertyDefById(propertyId).getEditPossibility();
            }
        }
        return editPossibility;
    }

    /**
     * Получение признака, определяющего возможность задавать свойству значение <code>null</code>
     * @return <code>true</code>, если свойство не может иметь значение <code>null</code> (должно быть всегда задано), 
     * <code>false</code>, если может и <code>null</code>, значение настройки не определено.
     */ 
    public Boolean isMandatory() {
        if (mandatory==null && owner!=null && contextClassDef!=null){
            if (owner.getInheritanceMask().isPresentationPropertyAttributesInherited() &&
                owner.getBasePresentation()!=null && owner.getBasePresentation().isPropertyDefExistsById(propertyId)
               ){
                mandatory = getInheritedAttributes().isMandatory();
            }else{
                mandatory = owner.getPropertyDefById(propertyId).isMandatory();
            }
        }
        return mandatory;
    }
    
    /**
     * Получение настройки, определяющей видимость редактора значения свойства
     * @return значение настройки или <code>null</code>, если значение атрибута не задано
     */ 
    public EPropertyVisibility getVisibility() {
        if (visibility==null && owner!=null && contextClassDef!=null){
            if (owner.getInheritanceMask().isPresentationPropertyAttributesInherited() &&
                owner.getBasePresentation()!=null && owner.getBasePresentation().isPropertyDefExistsById(propertyId)
               ){
                visibility = getInheritedAttributes().getVisibility();
            }else{
                visibility = EPropertyVisibility.ALWAYS;
            }            
        }
        return visibility;
    }    
    
    private boolean isTitleDefined(){
        return titleId!=null && titleOwnerId!=null;
    }
    
    private TitledDefinition getTitleSource(){
        if (titleSource==null){
            if (isTitleDefined()){
                titleSource = new TitledDefinition(getId(), "", titleOwnerId, titleId){};
            }else if (owner!=null && contextClassDef!=null){
                if (owner.getInheritanceMask().isPresentationPropertyAttributesInherited() &&
                    owner.getBasePresentation()!=null && owner.getBasePresentation().isPropertyDefExistsById(propertyId)
                   ){
                    titleSource = getInheritedAttributes().getTitleSource();
                }else{
                    titleSource = owner.getPropertyDefById(propertyId);
                }
            }
        }
        return titleSource;
    }
    
    /**
     * Получение заголовка свойства
     * @return заголовок свойства или <code>null</code>, если значение атрибута не задано
     */
    public String getTitle(){
        return getTitleSource()==null ? null : getTitleSource().getTitle();
    }
    
    public String getTitle(final IClientEnvironment environment){
        final TitledDefinition source = getTitleSource();
        if (source instanceof RadPropertyDef){
            return ((RadPropertyDef)source).getTitle(environment);
        }else{
            return source==null ? null : source.getTitle();
        }        
    }
    
}
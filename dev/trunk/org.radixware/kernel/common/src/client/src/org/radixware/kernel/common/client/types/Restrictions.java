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
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;

/**
 * Набор ограничений
 * (Radix::Explorer.Types::Restrictions).
 * Класс предоставляет информацию о запретах на выполнение стандартных операций 
 * в {@link org.radixware.kernel.common.client.models.Model radix-модели} 
 * и ее {@link org.radixware.kernel.common.client.views.IView представлении}.<p>
 * Для получения инстанции класса следует использовать вложенный класс-фабрику.
 * Статические поля-инстанции служат для наиболее часто используемых наборов.
 */
@AdsPublication("adcWFM6T6SQZ5E4HPADANCL7ENYHQ")
public class Restrictions {

    protected final EnumSet<ERestriction> restrictions = EnumSet.noneOf(ERestriction.class);
    protected List<Id> enabledCommandIds;
    /**
     * Инстанция класса, которая используется при отсутствии ограничений
     */
    public final static Restrictions NO_RESTRICTIONS = Factory.newInstance(EnumSet.noneOf(ERestriction.class), null);
    /**
     * Инстанция класса, которая используется при наличии только одного ограничения на выполнение операции удаления
     */
    public final static Restrictions DELETE_RESTRICTION = Factory.newInstance(ERestriction.DELETE);
    /**
     * Инстанция класса, которая используется при наличии только одного ограничения на открытие в 
     * селекторе вложенного редактора объекта
     */    
    public final static Restrictions EDITOR_RESTRICTION = Factory.newInstance(ERestriction.EDITOR);    
    /**
     * Инстанция класса, которая используется в случае запрета на выполнение операций создания, удаления и модификации
     */
    public final static Restrictions READ_ONLY = 
        Factory.newInstance(EnumSet.of(ERestriction.CREATE, ERestriction.DELETE, ERestriction.UPDATE, ERestriction.NOT_READ_ONLY_COMMANDS), null);
    
    public final static Restrictions CONTEXTLESS_SELECT = 
        Factory.newInstance(EnumSet.of(ERestriction.COPY, ERestriction.CREATE, ERestriction.DELETE_ALL,
                ERestriction.MOVE, ERestriction.MULTIPLE_COPY), null);

    Restrictions(
            final EnumSet<ERestriction> restrictions) {
        this.restrictions.addAll(restrictions);
        this.restrictions.remove(ERestriction.CHANGE_POSITION);
    }

    Restrictions(
            final EnumSet<ERestriction> restrictions,
            final List<Id> enabledCommandIds // not null if ANY_COMMAND set
            ) {
        this.restrictions.addAll(restrictions);
        this.restrictions.remove(ERestriction.CHANGE_POSITION);
        if (enabledCommandIds != null) {
            this.enabledCommandIds = new ArrayList<>(enabledCommandIds);
        } else {
            this.enabledCommandIds = null;
        }
    }

    Restrictions(
            final EnumSet<ERestriction> restrictions,
            final Id[] enabledCommandIds // not null if ANY_COMMAND set
            ) {
        this.restrictions.addAll(restrictions);
        this.restrictions.remove(ERestriction.CHANGE_POSITION);
        if (enabledCommandIds != null) {
            this.enabledCommandIds = Arrays.asList(enabledCommandIds);
        } else {
            this.enabledCommandIds = null;
        }
    }

    Restrictions(final Restrictions restrictions) {
        this.restrictions.addAll(restrictions.restrictions);
        if (restrictions.enabledCommandIds != null) {
            this.enabledCommandIds = new ArrayList<>();
            this.enabledCommandIds.addAll(restrictions.enabledCommandIds);
        } else {
            this.enabledCommandIds = null;
        }
    }
    
    boolean isRestricted(final ERestriction restriction){
        return restrictions.contains(restriction);
    }
    
    /**
     * Проверка наличия ограничения на выполнение указанной команды.
     * Метод возвращает <code>true</code>, если выполнение команды запрещено и <code>false</code>, если нет.
     * @param cmdId идентификатор команды
     * @return <code>true</code> если выполнение команды запрещено
     */
    public boolean getIsCommandRestricted(final Id cmdId) {
        if (restrictions.contains(ERestriction.ANY_COMMAND)) {
            return (enabledCommandIds == null) || !enabledCommandIds.contains(cmdId);
        } else {
            return false;
        }
    }
    
    public boolean getIsNotReadOnlyCommandsRestricted(){
        return isRestricted(ERestriction.NOT_READ_ONLY_COMMANDS);
    }

    /**
     * Получение списка идентификаторов разрешенных команд.
     * Метод возвращает список идентификаторов команд, выполнение которых не запрещено данным набором ограничений.
     * Если данный набор не содержит ограничений на выполнение каких-либо команд, то метод возвращает <code>null</code>,
     * если набор запрещает выполнение любой команды, то метод возвращает пустой список.
     * @return список идентификаторов команд или <code>null</code>, 
     * если данный набор не содержит ограничений на выполнение каких-либо команд
     */
    public List<Id> getEnabledCommandIDs() {
        if (enabledCommandIds != null) {
            return Collections.unmodifiableList(enabledCommandIds);
        } else {
            return null;
        }
    }

    /**
     * Проверка наличия ограничения на выполнение операции копирования.
     * Метод возвращает <code>true</code>, если операция копирования запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если копирование запрещено
     */
    public boolean getIsCopyRestricted() {
        return isRestricted(ERestriction.COPY);
    }

    /**
     * Проверка наличия ограничения на выполнение операции множественного копирования.
     * Метод возвращает <code>true</code>, если операция множественного копирования запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если множественное копирование запрещено
     */
    public boolean getIsMultipleCopyRestricted() {
        return isRestricted(ERestriction.MULTIPLE_COPY);
    }

    /**
     * Проверка наличия ограничения на выполнение операции создания.
     * Метод возвращает <code>true</code>, если операция создания запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если создание запрещено
     */
    public boolean getIsCreateRestricted() {
        return isRestricted(ERestriction.CREATE);
    }

    /**
     * Проверка наличия ограничения на выполнение операции удаления.
     * Метод возвращает <code>true</code>, если операция удаления запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если удаление запрещено
     */
    public boolean getIsDeleteRestricted() {
        return isRestricted(ERestriction.DELETE);
    }

    /**
     * Проверка наличия ограничения на выполнение операции множественного удаления.
     * Метод возвращает <code>true</code>, если операция множественного удаления запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если множественное удаление запрещено
     */    
    public boolean getIsDeleteAllRestricted() {
        return isRestricted(ERestriction.DELETE_ALL);
    }

    /**
     * Проверка наличия ограничения на выполнение операции перемещения.
     * Метод возвращает <code>true</code>, если операция перемещения запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если перемещение запрещено
     */
    public boolean getIsMoveRestricted() {
        return isRestricted(ERestriction.MOVE);
    }

    /**
     * Проверка наличия ограничения на выполнение операции модификации.
     * Метод возвращает <code>true</code>, если операция модификации запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если модификация запрещена
     */    
    public boolean getIsUpdateRestricted() {
        return isRestricted(ERestriction.UPDATE);
    }

    /**
     * Проверка наличия ограничения на выполнение операции изменения текущего объекта.
     * Метод возвращает <code>true</code>, если операция изменения текущего запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если изменение текущего объекта запрещено
     */        
    public boolean getIsChangePositionRestricted() {
        return isRestricted(ERestriction.CHANGE_POSITION);
    }

    /**
     * Проверка наличия ограничения на открытие вложенного редактора объекта.
     * Метод возвращает <code>true</code>, если открытие вложенного редактора объекта запрещено и <code>false</code>, если нет.
     * @return <code>true</code> если открытие редактора запрещено
     */
    public boolean getIsEditorRestricted() {
        return isRestricted(ERestriction.EDITOR);
    }

    /**
     * Проверка наличия ограничения на выполнение операции множественной вставки в дерево проводника.
     * Метод возвращает <code>true</code>, если операция множественной вставки в дерево запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если множественная вставка в дерево запрещена
     */
    public boolean getIsInsertAllIntoTreeRestricted() {
        return isRestricted(ERestriction.INSERT_ALL_INTO_TREE);
    }

    /**
     * Проверка наличия ограничения на выполнение операции вставки в дерево проводника.
     * Метод возвращает <code>true</code>, если операция вставки в дерево запрещена и <code>false</code>, если нет.
     * @return <code>true</code> если вставка в дерево запрещена
     */
    public boolean getIsInsertIntoTreeRestricted() {
        return isRestricted(ERestriction.INSERT_INTO_TREE);
    }

    /**
     * Проверка наличия ограничения на открытие модального диалога редактора объекта.
     * Метод возвращает <code>true</code>, если открытие модального диалога редактора объекта запрещено и <code>false</code>, если нет.
     * @return <code>true</code> если открытие модального диалога редактора запрещено
     */    
    public boolean getIsRunEditorRestricted() {
        return isRestricted(ERestriction.RUN_EDITOR);
    }

    /**
     * Проверка наличия ограничения на создание диалога (представления).
     * Метод возвращает <code>true</code>, если создание диалога (набора графических элементов) запрещено и <code>false</code>, если нет.
     * @return <code>true</code> если создание диалога запрещено
     */
    public boolean getIsViewRestricted(){
        return isRestricted(ERestriction.VIEW);
    }

    /**
     * Проверка наличия ограничения на бесконтекстное использование.
     * Метод возвращает <code>true</code>, если запрещено использование модели без указания ее логического положения в дереве проводника.
     * @return <code>true</code> если бесконтекстное использование запрещено
     */
    public boolean getIsContextlessUsageRestricted() {
        return isRestricted(ERestriction.CONTEXTLESS_USAGE);
    }

    /**
     * Проверка наличия ограничения на выбор объектов в селекторе.
     * Метод возвращает <code>true</code>, если запрещено выделение объектов в селекторе и <code>false</code>, если нет.
     * @return <code>true</code> если выбор объектов запрещен
     */
    public boolean getIsMultipleSelectionRestricted(){
        return isRestricted(ERestriction.MULTIPLE_SELECTION);
    }

    /**
     * Проверка наличия ограничения на выбор всех объектов селектора.
     * Метод возвращает <code>true</code>, если запрещен режим выделения всех объектов селектора (в том числе еще не загруженных) и <code>false</code>, если нет.
     * @return <code>true</code> если запрещен режим выбора всех объектов.
     */    
    public boolean getIsSelectAllRestricted(){
        return isRestricted(ERestriction.SELECT_ALL);
    }
    
    /**
     * Проверка наличия ограничения на пакетное удаление объектов.
     * Метод возвращает <code>true</code>, если удаление выбранных в селекторе объектов запрещено и <code>false</code>, если нет.
     * @return <code>true</code> если запрещено пакетное удаление объектов
     */    
    public boolean getIsMultipleDeleteRestricted(){
        return isRestricted(ERestriction.MULTIPLE_DELETE);
    }

    /**
     * Класс-фабрика для получения инстанций
     */
    public static final class Factory {

        /**
         * Создание инстанции набора ограничений.
         * Метод создает набор ограничений, содержащий одно ограничение на выполнение стандартной операции.
         * @param restriction ограничение
         * @return набор ограничений
         */
        public static Restrictions newInstance(final ERestriction restriction) {
            return newInstance(EnumSet.of(restriction), null);
        }

        public static Restrictions newInstance(
                final EnumSet<ERestriction> restrictions,
                final List<Id> enabledCommandIds) {
            if (enabledCommandIds != null) {
                return new Restrictions(restrictions, enabledCommandIds);
            }
            final Long bitMaskLong = ERestriction.toBitField(restrictions);

            Restrictions result = hash.get(bitMaskLong);
            if (result == null) {
                result = new Restrictions(restrictions);
                hash.put(bitMaskLong, result);
            }
            return result;
        }

        public static Restrictions sum(final Restrictions r1, final Restrictions r2) {
            if (r1 == null && r2 == null) {
                return newInstance(EnumSet.noneOf(ERestriction.class), null);
            }
            if (r1 == null) {
                return r2;
            }
            if (r2 == null) {
                return r1;
            }

            final EnumSet<ERestriction> sumRestrictions = EnumSet.copyOf(r1.restrictions);
            sumRestrictions.addAll(r2.restrictions);

            ArrayList<Id> enabledCommands = null;

            if (sumRestrictions.contains(ERestriction.ANY_COMMAND) && (r1.enabledCommandIds != null || r2.enabledCommandIds != null)) {
                enabledCommands = new ArrayList<>();
                Restrictions src;
                Restrictions dst;
                if (r1.enabledCommandIds != null) {
                    src = r1;
                    dst = r2;
                } else {
                    src = r2;
                    dst = r1;
                }
                for (Id id : src.enabledCommandIds) {
                    if (dst.enabledCommandIds == null || dst.enabledCommandIds.contains(id)) {
                        enabledCommands.add(id);
                    }
                }
            }
            return new Restrictions(sumRestrictions, enabledCommands);
        }

        public static Restrictions sum(final Restrictions r1, final EnumSet<ERestriction> r2) {
            if (r1 == null && r2 == null) {
                return newInstance(EnumSet.noneOf(ERestriction.class), null);
            }
            if (r1 == null) {
                return newInstance(r2, null);
            }
            if (r2 == null) {
                return r1;
            }

            final EnumSet<ERestriction> sumRestrictions = EnumSet.copyOf(r1.restrictions);
            sumRestrictions.addAll(r2);

            List<Id> enabledCommands = r1.getEnabledCommandIDs();

            if (sumRestrictions.contains(ERestriction.ANY_COMMAND) && enabledCommands == null) {
                enabledCommands = new ArrayList<>();
            }

            return new Restrictions(sumRestrictions, enabledCommands);
        }

        public static Restrictions sum(final EnumSet<ERestriction> r1, final Restrictions r2) {
            return sum(r2, r1);
        }

        public static Restrictions sum(final Restrictions r1, final ERestriction r2) {
            if (r2 != null) {
                return sum(r1, EnumSet.of(r2));
            } else {
                return r1;
            }
        }

        public static Restrictions sum(final ERestriction r1, final Restrictions r2) {
            return sum(r2, r1);
        }
        
        
        private static final HashMap<Long, Restrictions> hash = new HashMap<>();
    }
    
    static boolean restrictionDiffer(final EnumSet<ERestriction> set1, final EnumSet<ERestriction> set2, final ERestriction restriction){        
        return set1.contains(restriction) ? !set2.contains(restriction) : set2.contains(restriction);
    }
}

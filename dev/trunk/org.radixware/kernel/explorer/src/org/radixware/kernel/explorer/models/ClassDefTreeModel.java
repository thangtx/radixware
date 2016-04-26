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

package org.radixware.kernel.explorer.models;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QTreeModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadReferenceDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

/**
 * Qt-модель древовидного представления {@link RadClassPresentationDef дефиниций классов}
 * и их элементов.
 *
 */
public class ClassDefTreeModel extends QTreeModel {

    //ItemDataRole для заголовка класса (DBP-1648)
    public static final int USER_CLASS_TITLE_ROLE = Qt.ItemDataRole.UserRole + 1;
    ;

    private boolean expandClasses = true;
    private boolean showConstants;
    private boolean showReferences = true;
    private boolean showRootClassDef = true;
    //private boolean showPropType = false;
    private boolean showOnlyRef = false;
    private boolean showReferencesInSeparateItem = true;
    private EDefinitionDisplayMode displayMode = EDefinitionDisplayMode.SHOW_SHORT_NAMES;
    private final List<Id> classDefs = new ArrayList<Id>();
    private final Map<Id, RadEnumPresentationDef.Items> orderedItems = new HashMap<Id, RadEnumPresentationDef.Items>();
    private final RadClassPresentationDef root;
    //private final Map<String,String> classTitles = new HashMap<String,String>();
    private final List<String> classDefTitles = new ArrayList<String>();
    private final static String REFERENCES_ITEM_TITLE = Application.translate("ClassDefTreeModel", "Referenced objects");
    private final IClientEnvironment environment;

    /**
     * Конструктор модели.
     * Если задан класс rootClassDef в модели будут доступны только элементы
     * этого и связанных с ним классов {@link #setShowReferences(boolean)}, в противном случае на верхнем уровне
     * модели будут присутствовать дефиниции всех классов текущей версии.
     * @param rootClassDef дефиниция класса - элемент первого уровня модели. Может быть null.
     */
    public ClassDefTreeModel(IClientEnvironment environment, RadClassPresentationDef rootClassDef) {
        root = rootClassDef;
        this.environment = environment;
    }

    /**
     * Метод возвращает дочерний, по отношению к parent, элемент с индексом row
     * Элемент parent может быть
     * @param parent элемент верхнего уровня
     * @param row индекс дочернего элемента
     * @return дочерний элемент
     */
    @Override
    public Object child(Object parent, int row) {
        if (parent == null) {
            int idx = row;
            if (root != null) {
                if (showRootClassDef) {
                    if (idx == 0) {
                        return root;
                    } else {
                        idx--;
                    }
                } else {
                    final int rootChildCount = childsCountInClass(root);
                    if (row < rootChildCount) {
                        return childInClass(root, row);
                    } else {
                        idx -= rootChildCount;
                    }
                }
            }
            try {
                return environment.getApplication().getDefManager().getClassPresentationDef(classDefs.get(idx));
            } catch (DefinitionError ex) {
                return classDefs.get(idx);
            }
        } else if (parent instanceof RadClassPresentationDef) {
            return childInClass((RadClassPresentationDef) parent, row);
        } else if (parent instanceof RadPropertyDef) {
            RadEnumPresentationDef constSet = ((RadPropertyDef) parent).getConstSet();
            if (constSet != null) {
                return getEnumItemForRow(constSet, row);
            }
        } else if (parent instanceof RadReferenceDef) {
            final RadReferenceDef reference = (RadReferenceDef) parent;
            try {
                return childInClass(reference.getReferencedClassDef(), row);
            } catch (DefinitionError ex) {
                return reference.referencedTableId;
            }
        } else if (parent instanceof List) {
            List references = (List) parent;
            if (row < references.size()) {
                final RadReferenceDef ref = (RadReferenceDef) references.get(row);
                try {
                    ref.getReferencedClassDef();
                } catch (DefinitionError ex) {
                    return ref.referencedTableId;
                }
                return references.get(row);
            }
        }
        return null;
    }

    /**
     * Метод возвращает количество дочерних элементов parent
     * @param parent элемент верхнего уровня
     * @return количество подэлементов parent
     */
    @Override
    public int childCount(Object parent) {
        if (parent == null) {
            if (root != null) {
                return classDefs.size() + (showRootClassDef ? 1 : childsCountInClass(root));
            } else {
                return classDefs.size();
            }
        } else if ((parent instanceof RadClassPresentationDef) && expandClasses && !showOnlyRef) {
            return childsCountInClass((RadClassPresentationDef) parent);
        } else if ((parent instanceof RadPropertyDef) && showConstants
                && ((RadPropertyDef) parent).getConstSet() != null && !showOnlyRef) {
            return ((RadPropertyDef) parent).getConstSet().getItems().size();
        } else if (parent instanceof RadReferenceDef) {
            try {
                return childsCountInClass(((RadReferenceDef) parent).getReferencedClassDef());
            } catch (DefinitionError ex) {
                return 0;
            }
        } else if (parent instanceof List) {
            List references = (List) parent;
            return references.size();
        }
        return 0;
    }

    private RadEnumPresentationDef.Item getEnumItemForRow(final RadEnumPresentationDef enumDef, final int row) {
        if (!orderedItems.containsKey(enumDef.getId())) {
            RadEnumPresentationDef.Items items = enumDef.getItems();
            orderedItems.put(enumDef.getId(), items);
        }
        return orderedItems.get(enumDef.getId()).getItem(row);
    }

    private int childsCountInClass(RadClassPresentationDef classDef) {
        final int propertiesCount = classDef.getProperties().size();
        if (showReferences) {
            if (showReferencesInSeparateItem && !classDef.getReferences().isEmpty()) {
                return propertiesCount + 1;
            } else {
                return propertiesCount + classDef.getReferences().size();
            }
        } else {
            return propertiesCount;
        }
    }

    private Object childInClass(RadClassPresentationDef classDef, int idx) {
        if (idx >= classDef.getProperties().size()) {
            if (showReferencesInSeparateItem) {
                return classDef.getReferences();
            } else {
                final RadReferenceDef ref = classDef.getReferences().get(idx - classDef.getProperties().size());
                try {
                    ref.getReferencedClassDef();
                } catch (DefinitionError err) {
                    return ref.referencedTableId;
                }
                return ref;
            }
        }
        return classDef.getProperties().get(idx);
    }

    /**
     * Возвращает заголовок для элемента item.
     * Если установлен режим отображения SHOW_NAMES метод возвращает имена дефиниций,
     * если SHOW_TITLES - заголовки.
     * @param item - элемент модели.
     * @return заголово элемента.
     */
    @Override
    public String text(Object item) {
        if (item instanceof RadClassPresentationDef) {
            RadClassPresentationDef classDef = (RadClassPresentationDef) item;
            final String name = classDef.getName() != null ? classDef.getName() : "#" + classDef.getId();
            return getDisplaiedName(name, classDef.getGroupTitle(), classDef.hasGroupTitle());
        } else if (item instanceof RadPropertyDef) {
            RadPropertyDef propertyDef = (RadPropertyDef) item;
            final String name = propertyDef.getName() != null ? propertyDef.getName() : "#" + propertyDef.getId();
            return getDisplaiedName(name, propertyDef.getTitle(), propertyDef.hasTitle());
        } else if (item instanceof RadEnumPresentationDef.Item) {
            RadEnumPresentationDef.Item constSetItem = (RadEnumPresentationDef.Item) item;
            boolean hasTitle = constSetItem.getTitle() != null && !constSetItem.getTitle().equals("<No Title>");
            return getDisplaiedName(constSetItem.getName(), constSetItem.getTitle(), hasTitle);
        } else if (item instanceof RadReferenceDef) {
            RadReferenceDef ref = (RadReferenceDef) item;
            try {
                final String name = ref.getReferencedClassDef().getName() != null ? ref.getReferencedClassDef().getName() : "#" + ref.getReferencedClassDef().getId();
                final String title = ref.getReferencedClassDef().getGroupTitle();
                return getDisplaiedName(name, title, ref.getReferencedClassDef().hasGroupTitle())
                        + getRefPropsName(ref.getChildColumnNames());
            } catch (DefinitionError ex) {
                return "??? <" + ref.referencedTableId + "> ???";
            }
        } else if (item instanceof List) {
            return REFERENCES_ITEM_TITLE;
        } else if (item instanceof Id) {
            return "??? <" + ((Id) item).toString() + "> ???";
        }
        return "??? <" + item.getClass().getSimpleName() + "> ???";
    }

    private String getDisplaiedName(String name, String title, boolean hasTitle) {
        if (displayMode == EDefinitionDisplayMode.SHOW_TITLES) {
            return hasTitle ? title : name;
        } else if ((displayMode == EDefinitionDisplayMode.SHOW_SHORT_NAMES) && (name.indexOf("::") != -1)) {
            int start = 0;
            if ((start = name.lastIndexOf("::")) != -1) {
                name = name.substring(start + 2, name.length());
            }
        }
        return name;
    }

    private String getRefPropsName(List<String> columns) {
        if (columns.size() == 0) {
            return "";
        }
        String res = " (";
        for (int i = 0; i < columns.size(); i++) {
            res = res.concat(columns.get(i));
            if (i < (columns.size() - 1)) {
                res = res.concat(", ");
            }
        }
        return res.concat(")");
    }

    /**
     * Включить в модель подэлементы дефиниций классов.
     * Если параметр равен false в модели будет только один уровень -
     * дефиниции классов. По умолчанию равен true.
     * @param flag если равен true в модели присутствуют дочерние элементы классов.
     * @see #expandClasses()
     */
    public final void setExpandClasses(boolean flag) {
        if (flag != expandClasses) {
            expandClasses = flag;
            reset();
        }
    }

    /**
     * Отображать элементы в наборах констант.
     * В случае когда для свойства класса задан набор констант метод позволяет
     * включить в модель его элементы в качестве дочерних элементов этого свойства.
     * @param flag - если равен true в модели будут присутствовать элементы из наборов констант, иначе - нет.
     * @see PropertyDef#getConstSet()
     * @see #showConstants()
     */
    public final void setShowConstants(boolean flag) {
        if (flag != showConstants) {
            showConstants = flag;
            reset();
        }
    }

    /* public final void setShowPropType(boolean flag){
    if (flag!=showPropType){
    showPropType = flag;
    reset();
    }
    }*/
    /**
     * Отображать дефиниции связанных классов.
     * В случае когда для таблицы, по которой создан класс, заданы связи с другими таблицами,
     * метод позволяет включить эти классы этих таблиц в модель.
     * @param flag - если равен true в модели будут присутствовать связанные классы
     */
    public final void setShowReferences(boolean flag) {
        if (flag != showReferences) {
            showReferences = flag;
            reset();
        }
    }

    public final void setShowReferencesInSeparateItem(boolean flag) {
        if (flag != showReferencesInSeparateItem) {
            showReferencesInSeparateItem = flag;
            reset();
        }
    }

    public final void setshowOnlyRef(boolean flag) {
        if (root != null) {
            if (flag != showOnlyRef) {
                showOnlyRef = flag;
                reset();
            }
        }
    }

    /**
     *
     * @param flag
     */
    public final void setShowRootClass(boolean flag) {
        if (root != null) {
            if (flag != showRootClassDef) {
                showRootClassDef = flag;
                reset();
            }
        }
    }

    public final void setDisplayMode(final EDefinitionDisplayMode mode) {
        if (mode != displayMode) {
            displayMode = mode;
            //this.reset();
        }
    }

    public final boolean expandClasses() {
        return expandClasses;
    }

    public final boolean showConstants() {
        return showConstants;
    }
    //public final boolean showPropType()  {return showPropType; }

    public final boolean showReferences() {
        return showReferences;
    }

    public final boolean showReferencesInSeparateItem() {
        return showReferencesInSeparateItem;
    }

    public final boolean showOnlyRef() {
        return showOnlyRef;
    }

    public final boolean showRootClass() {
        return root != null && showRootClassDef;
    }

    public final EDefinitionDisplayMode displayMode() {
        return displayMode;
    }

    public final RadClassPresentationDef getRootClassDef() {
        return root;
    }

    public void fillClassDefs(List<Id> defList) {
        if (defList != null) {
            classDefs.clear();
            classDefs.addAll(defList);
            //Пользовательские заголовки отсутствуют
            classDefTitles.clear();
            classDefTitles.addAll(Collections.nCopies(classDefs.size(), (String) null));
            reset();
        }
    }

    public void addClassDef(final Id classDefId) {
        classDefs.add(classDefId);
        classDefTitles.add(null);
        reset();
    }

    public void addClassDef(final Id classDefId, final String userTitle) {
        classDefs.add(classDefId);
        classDefTitles.add(userTitle);
        reset();
    }

    public void clearClassDefs() {
        classDefs.clear();
        classDefTitles.clear();
        reset();
    }
    private final static QIcon TABLE_ICON = ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE);
    private final static QIcon PROPERTY_ICON = ExplorerIcon.getQIcon(ClientIcon.Definitions.PROPERTY);
    private final static QIcon STRING_CONSTSET_ICON = ExplorerIcon.getQIcon(ClientIcon.Definitions.STRING_CONSTSET);
    private final static QIcon INTEGER_CONSTSET_ICON = ExplorerIcon.getQIcon(ClientIcon.Definitions.INTEGER_CONSTSET);
    private final static QIcon CONSTSET_ICON = ExplorerIcon.getQIcon(ClientIcon.Definitions.CONSTSET);

    @Override
    public QIcon icon(Object item) {
        if (item instanceof RadClassPresentationDef) {
            //RadClassPresentationDef classDef = (RadClassPresentationDef)item;
            QIcon icon = (QIcon) ((RadClassPresentationDef) item).getIcon();
            return icon == null ? TABLE_ICON : icon;
        } else if (item instanceof RadPropertyDef) {
            RadPropertyDef prop = (RadPropertyDef) item;
            return getPropIcon(prop.getType());
        } else if (item instanceof RadEnumPresentationDef.Item) {
            RadEnumPresentationDef.Item constSetItem = (RadEnumPresentationDef.Item) item;
            QIcon icon = (QIcon) constSetItem.getIcon();
            if (icon == null) {
                switch (constSetItem.getValType()) {
                    case INT:
                        icon = INTEGER_CONSTSET_ICON;
                        break;
                    case STR:
                        icon = STRING_CONSTSET_ICON;
                        break;
                    default:
                        icon = CONSTSET_ICON;
                }
            }
            return icon;
        } else if (item instanceof RadReferenceDef) {
            RadReferenceDef ref = (RadReferenceDef) item;
            try {
                return (QIcon) ref.getReferencedClassDef().getIcon();
            } catch (DefinitionError ex) {
                return super.icon(item);
            }
        } else {
            return super.icon(item);
        }
    }

    public String userClassTitle(QModelIndex index) {//DBP-1648
        return (String) data(index, USER_CLASS_TITLE_ROLE);
    }

    @Override
    public Object data(QModelIndex index, int role) {
        if (role == ItemDataRole.FontRole) {
            return font(indexToValue(index), (QFont) super.data(index, role));
        } else if (role == Qt.ItemDataRole.DisplayRole && index.parent() == null) {
            //DBP-1648 (только для верхнего уровня)
            final Object value = indexToValue(index);
            if (value instanceof RadClassPresentationDef) {
                final String title = userClassTitle(index);
                if (title != null && !title.isEmpty()) {
                    return title;
                }
                //иначе работает метод text()
            }
        } else if (role == USER_CLASS_TITLE_ROLE) {
            //DBP-1648 получить пользовательский заголовок класса
            QModelIndex idx = index;
            while (idx.parent() != null) {
                idx = idx.parent();//первый уровень.
            }
            final Object value = indexToValue(idx);
            if (value instanceof RadClassPresentationDef) {
                if (root == null && classDefTitles.size() > idx.row()) {
                    return classDefTitles.get(idx.row());
                } else if (root != null) {
                    int row = idx.row();
                    if (showRootClassDef && row > 0) {
                        row--;
                        if (classDefTitles.size() > row) {
                            return classDefTitles.get(row);
                        }
                    } else if (!showRootClassDef && row > childsCountInClass(root)) {
                        row -= childsCountInClass(root);
                        if (classDefTitles.size() > row) {
                            return classDefTitles.get(row);
                        }
                    }
                }
            }
            return null;
        }
        return super.data(index, role);
    }

    private QFont font(Object item, QFont defaultFont) {
        if (item instanceof List) {
            final QFont fontForReferencesItem = new QFont(defaultFont);
            fontForReferencesItem.setBold(true);
            return fontForReferencesItem;
        }
        return defaultFont;
    }
    private final static Map<EValType, QIcon> PROPERTY_ICONS_BY_TYPE = new EnumMap<EValType, QIcon>(EValType.class);

    {
        PROPERTY_ICONS_BY_TYPE.put(EValType.STR, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.STR));
        PROPERTY_ICONS_BY_TYPE.put(EValType.CLOB, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.CLOB));
        PROPERTY_ICONS_BY_TYPE.put(EValType.CHAR, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.CHAR));
        PROPERTY_ICONS_BY_TYPE.put(EValType.BOOL, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.BOOL));
        PROPERTY_ICONS_BY_TYPE.put(EValType.INT, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.INT));
        PROPERTY_ICONS_BY_TYPE.put(EValType.NUM, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.NUM));
        PROPERTY_ICONS_BY_TYPE.put(EValType.DATE_TIME, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.DATE_TIME));
        PROPERTY_ICONS_BY_TYPE.put(EValType.XML, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.XML));
        PROPERTY_ICONS_BY_TYPE.put(EValType.BIN, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.BIN));
        PROPERTY_ICONS_BY_TYPE.put(EValType.BLOB, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.BLOB));
        PROPERTY_ICONS_BY_TYPE.put(EValType.PARENT_REF, ExplorerIcon.getQIcon(ClientIcon.ValueTypes.PARENT_REF));
    }

    private QIcon getPropIcon(EValType type) {
        if (PROPERTY_ICONS_BY_TYPE.containsKey(type)) {
            return PROPERTY_ICONS_BY_TYPE.get(type);
        } else {
            return PROPERTY_ICON;
        }
    }
}
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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.StringCellValue;
import org.radixware.kernel.designer.common.editors.EnumItemDomainsEditor;


public class EnumerationTableModel extends AbstractTableModel {

    private AdsEnumDef enumDef;
    private EIsoLanguage[] languagesArray;

    private class EnumVisibleItem {

        EnumVisibleItem(AdsEnumItemDef enumValue, Boolean visible) {
            this.enumValue = enumValue;
            this.visible = visible;
        }
        AdsEnumItemDef enumValue;
        Boolean visible;
    }
    //private TreeMap<AdsEnumItemDef, Boolean> view;
    private List<EnumVisibleItem> view;
    private int hiddenRows = 0;
    private volatile int viewSize = 0;

    public EnumerationTableModel(final AdsEnumDef enumDef) {
        super();
        this.enumDef = enumDef;
        this.indexedComparator = new IndexedComparator(enumDef);
        this.currentComparator = indexedComparator;

        columnsNames = new ArrayList<String>();
        columnsNames.add(NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Nn"));
        columnsNames.add(NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Name"));
        columnsNames.add(NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Value"));

        final Layer layer = enumDef.getModule().getSegment().getLayer();
        languagesArray = new EIsoLanguage[layer.getLanguages().size()];
        layer.getLanguages().toArray(languagesArray);

        for (EIsoLanguage language : languagesArray) {
            columnsNames.add(language.getName() + " " + NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Title"));
        }

        columnsNames.add(NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Domain"));
        columnsNames.add(NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-Icon"));

        reload();

        DOMAIN_COLUMN = VALUE_COLUMN + languagesArray.length + 1;
        ICON_COLUMN = DOMAIN_COLUMN + 1;
    }

    public EIsoLanguage getColumnLanguage(int col) {
        if (col >= 3 && col < 3 + languagesArray.length) {
            return languagesArray[col - 3];
        } else {
            return null;
        }
    }

    public void addHiddenItem(AdsEnumItemDef item) {
        boolean isFind = false;
        for (EnumVisibleItem i : view) {
            if (i.enumValue == item) {
                i.visible = false;
                isFind = true;
                break;
            }
        }
        if (!isFind) {
            view.add(new EnumVisibleItem(item, Boolean.FALSE));
        }
        hiddenRows++;
    }

    private boolean containsKeyInTheView(AdsEnumItemDef item) {
        for (EnumVisibleItem currItem : view) {
            if (currItem.enumValue == item) {
                return true;
            }
        }
        return false;
    }

    public void clearHiddenIndexes() {
        List<AdsEnumItemDef> viewCopy = enumDef.getItems().get(EScope.ALL);
        for (EnumVisibleItem item : view) {
            item.visible = true;
        }
        for (AdsEnumItemDef item : viewCopy) {
            if (!containsKeyInTheView(item)) {
                view.add(new EnumVisibleItem(item, Boolean.TRUE));
            }
        }
        hiddenRows = 0;
    }

    @Override
    public Class getColumnClass(int c) {
        if (c == DOMAIN_COLUMN) {
            return String.class;
        } else if (c == ICON_COLUMN) {
            return Icon.class;
        } else if (c > VALUE_COLUMN && c < DOMAIN_COLUMN) {
            return String.class;
        } else if (c == INDEX_COLUMN) {
            return Integer.class;
        } else if (c == NAME_COLUMN) {
            return StringCellValue.class;//String.class;
        } else {
            return Object.class;
        }
    }

    @Override
    public String getColumnName(int c) {
        return columnsNames.get(c);
    }

    @Override
    public int getColumnCount() {
        return columnsNames.size();
    }

    public int getViewItemsCount() {
        return viewSize;
    }

    @Override
    public synchronized int getRowCount() {
//        int hiddenCount = 0;
//        Collection<Boolean> values = view.values();
//        for (Boolean entry : values) {
//            if (entry != null &&
//                    !entry) {
//                hiddenCount++;
//            }
//        }
//        return view.size() - hiddenCount;
        return viewSize - hiddenRows;
    }

    public void sort(int column, SortOrder sortOrder) {
        if (column > -1 && column <= ICON_COLUMN) {
            this.currentSortOrder = sortOrder;
            if (column == INDEX_COLUMN) {
                reload();
            } else if (column == NAME_COLUMN) {
                reloadComparable(new NameComparator(sortOrder));
                fireTableDataChanged();
            } else if (column == VALUE_COLUMN) {
                reloadComparable(new ValueComparator(sortOrder));
                fireTableDataChanged();
            } else if (column == DOMAIN_COLUMN) {
                reloadComparable(new DomainComparator(sortOrder));
                fireTableDataChanged();
            } else if (column > VALUE_COLUMN && column < DOMAIN_COLUMN) {
                reloadComparable(new TitleComparator(sortOrder, column - VALUE_COLUMN - 1));
                fireTableDataChanged();
            }
        }
    }

    public int getActualIndex(int row) {
        int visibleIndex = 0;
        int index = 0;
        int size = viewSize;
        while (index <= size - 1
                && visibleIndex <= row) {
            EnumVisibleItem entry = view.get(index);
            if (entry.visible) {
                if (visibleIndex == row) {
                    return index;
                } else {
                    visibleIndex++;
                    index++;
                }
            } else {
                index++;
            }
        }
        return 0;
    }
    private final String NOT_DEFINED = NbBundle.getMessage(EnumerationTableModel.class, "ItemTableModel-NotDefined");

    private int getUnorderedItemIndex(AdsEnumItemDef item, int row) {
        List<AdsEnumItemDef> viewCopy_ = enumDef.getItems().get(EScope.ALL);
        List<EnumVisibleItem> viewCopy = new ArrayList<EnumVisibleItem>(viewCopy_.size());
        Collections.sort(viewCopy, indexedComparator);
        final int index = viewCopy.indexOf(item);
        return index > -1 ? index + 1 : row + 1;
    }

    @Override
    public Object getValueAt(int row, int column) {
        AdsEnumItemDef item = getViewItemByRow(row);

        if (column == INDEX_COLUMN) {
            List<Id> viewOrder = enumDef.getViewOrder().getOrderedItemIds();
            final int order = viewOrder.indexOf(item.getId());
            return order > -1 ? order + 1 : getUnorderedItemIndex(item, row);
        } else if (column == NAME_COLUMN) {
            String name = item.getName();
            return new StringCellValue(name);//name != null ? name : "";
        } else if (column == VALUE_COLUMN) {
            final ValAsStr valAsStr = item.getValue();
            if (valAsStr == null) {
                return NOT_DEFINED;
            } else {
                try {
                    return valAsStr.toObject(enumDef.getItemType());
                } catch (WrongFormatError e) {
                    return NOT_DEFINED;
                }
            }
        } else if (column > VALUE_COLUMN && column < DOMAIN_COLUMN) {
            return new StringCellValue(item.getTitle(languagesArray[column - VALUE_COLUMN - 1]));
        } else if (column == DOMAIN_COLUMN) {
            return EnumItemDomainsEditor.getDomainListAsStr(item);
            //return item.getDomainIds().isEmpty() ? NOT_DEFINED : "...";
        } else if (column == ICON_COLUMN) {
            return item.getIconId();
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        AdsEnumItemDef item = getViewItemByRow(row);

        if (column == NAME_COLUMN) {
            if (value instanceof String) {
                item.setName((String) value);
            } else if (value instanceof StringCellValue) {
                item.setName(((StringCellValue) value).getName());
            }
            fireTableCellUpdated(row, column);
        } else if (column == VALUE_COLUMN) {
            item.setValue(ValAsStr.Factory.newInstance(value, enumDef.getItemType()));
            fireTableCellUpdated(row, column);
        } else if (column > VALUE_COLUMN && column < DOMAIN_COLUMN) {
            final EIsoLanguage curLanguage = languagesArray[column - VALUE_COLUMN - 1];
            if (value instanceof String) {
                item.setTitle(curLanguage, (String) value);
            } else if (value instanceof StringCellValue) {
                item.setTitle(curLanguage, ((StringCellValue) value).getName());
            }
            fireTableCellUpdated(row, column);
        } else if (column == ICON_COLUMN) {
            assert (value == null || value instanceof Id);
            item.setIconId(value == null ? null : (Id) value);
            fireTableCellUpdated(row, column);
        }

    }

    public int getRowByViewItem(AdsEnumItemDef item) {
        for (int i = 0, size = getRowCount() - 1; i <= size; i++) {
            if (getViewItemByRow(i).getId().equals(item.getId())) {
                return i;
            }
        }
        return -1;
    }

    public Collection<AdsEnumItemDef> getStrictItemsCollection() {
        Collection<AdsEnumItemDef> coll = new ArrayList<AdsEnumItemDef>();
        for (EnumVisibleItem item : view) {
            coll.add(item.enumValue);
        }
        return coll;
    }

    public AdsEnumItemDef getViewItemByIndex(int index) {
        if (index > -1 && index < viewSize) {
            return view.get(index).enumValue;
        }
        return null;
    }

    public AdsEnumItemDef getViewItemByRow(int curRow) {
        final int actualRow = getActualIndex(curRow);
        if (actualRow > -1 && actualRow < viewSize) {
            return view.get(actualRow).enumValue;
        }
        return null;
    }

    public List<Integer> getColumnsLanguagesIndexes() {
        final ArrayList<Integer> indexes = new ArrayList<Integer>(DOMAIN_COLUMN - VALUE_COLUMN);
        for (int i = VALUE_COLUMN + 1; i < DOMAIN_COLUMN; ++i) {
            indexes.add(Integer.valueOf(i));
        }

        return indexes;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        AdsEnumItemDef item = getViewItemByRow(row);

        if (enumDef.isReadOnly()) {
            return false;
        }

        if (item.getOwnerEnum().equals(enumDef)) {
            if (column == NAME_COLUMN) {
                return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.NAME);
            } else if (column == VALUE_COLUMN) {
                return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.VALUE);
            } else if (column == DOMAIN_COLUMN) {
                return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.DOMAIN);
            } else if (column == ICON_COLUMN) {
                return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.ICON);
            } else if (column > VALUE_COLUMN && column < DOMAIN_COLUMN) {
                return item.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.TITLE);
            }
        }

        return false;
    }

    //MODEL UTILS
    public boolean isItemRemovable(int row) {
        final AdsEnumItemDef curItem = getViewItemByRow(row);
        if (curItem == null) {
            return false;
        }
        return (curItem.getOwnerEnum() == enumDef) && curItem.getEditPossibility().contains(AdsEnumItemDef.EditPossibility.REMOVE);
    }

    public void addItem(int index, AdsEnumItemDef item) {
        enumDef.getItems().getLocal().add(index, item);

        final int viewCount = enumDef.getViewOrder().getOrderedItemIds().size();

        if (index < viewCount) {
            int orderIndex = enumDef.getViewOrder().getOrderedItemIds().indexOf(item.getId());
            while (orderIndex >= index) {
                enumDef.getViewOrder().moveUp(item);
                orderIndex = enumDef.getViewOrder().getOrderedItemIds().indexOf(item.getId());
            }
        }

        reload();
    }

    public void addItem(AdsEnumItemDef item) {
        enumDef.getItems().getLocal().add(item);
        reload();
    }

    public void removeRow(int index) {
        final AdsEnumItemDef item = getViewItemByRow(index);
        item.delete();
        reload();
    }

    public void overwriteItem(AdsEnumItemDef item) {
        final AdsEnumItemDef newItem = enumDef.getItems().overwrite(item);
        //int ind=0;
        for (EnumVisibleItem i : view) {
            if (i.enumValue == item) {
                i.enumValue = newItem;
                i.visible = Boolean.TRUE;
                //view.remove(ind);
                //ind++;
            }
        }
        //view.remove(item);
        //view.put(newItem, Boolean.TRUE);
        //view.add(new EnumVisibleItem(newItem, Boolean.TRUE) );


        reload();
    }

    public void deoverwriteItem(AdsEnumItemDef viewItem) {
        viewItem.delete();
        reload();
    }

    public void synchronizeAdsDefinitionsItems() {
        enumDef.syncPublishedItems();
        reload();
    }

    public boolean isNativeEnum() {
        return !enumDef.isPlatformEnumPublisher() && !enumDef.isOverwrite();
    }

    public boolean isDeprecatedItem(int row) {
        return getViewItemByRow(row).isDeprecated();
    }

    public boolean isOverwrittenItem(int row) {
        AdsEnumItemDef item = getViewItemByRow(row);
        return item.isOverwrite() && item.getOwnerEnum().equals(enumDef);
    }

    public boolean isAddedItem(int row) {
        AdsEnumItemDef item = getViewItemByRow(row);
        return (!item.isPlatformItemPublisher())
                && (item.getOwnerEnum().equals(enumDef) && !item.isOverwrite());
    }
    private Comparator<EnumVisibleItem> currentComparator;
    private SortOrder currentSortOrder = SortOrder.UNSORTED;

    public Comparator<EnumVisibleItem> getCurrentComparator() {
        return this.currentComparator;
    }

    public SortOrder getCurrentSotrOrder() {
        return this.currentSortOrder;
    }

    private void reloadComparable(Comparator<EnumVisibleItem> comparator) {
        this.currentComparator = comparator;
        final List<AdsEnumItemDef> items = enumDef.getItems().list(EScope.ALL);

        view = new ArrayList<EnumVisibleItem>();
        //TreeMap<AdsEnumItemDef, Boolean>(comparator);
        for (int i = 0, size = items.size() - 1; i <= size; i++) {
            view.add(new EnumVisibleItem(items.get(i), Boolean.TRUE));
        }
        Collections.sort(view, comparator);
        hiddenRows = 0;
        viewSize = items.size();
        // viewSize = view.size();
    }

    public void reloadWithDefaultComparator() {
        this.currentComparator = indexedComparator;
        reloadComparable(indexedComparator);
        fireTableDataChanged();
    }

    public final void reload() {
        reloadComparable(currentComparator);
        fireTableDataChanged();
    }
    private ArrayList<String> columnsNames;
    public final static int INDEX_COLUMN = 0;
    public final static int NAME_COLUMN = 1;
    public final static int VALUE_COLUMN = 2;
    public int DOMAIN_COLUMN, ICON_COLUMN;
    private IndexedComparator indexedComparator;

    private class NameComparator implements Comparator<EnumVisibleItem> {

        private SortOrder sortOrder = SortOrder.UNSORTED;

        public NameComparator(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(EnumVisibleItem o1, EnumVisibleItem o2) {
            int result = 0;
            if (sortOrder.equals(SortOrder.DESCENDING) || sortOrder.equals(sortOrder.UNSORTED)) {
                result = Collator.getInstance().compare(o1.enumValue.getName(), o2.enumValue.getName());
            } else {
                result = Collator.getInstance().compare(o2.enumValue.getName(), o1.enumValue.getName());
            }
            return result;
        }
    }

    private class DomainComparator implements Comparator<EnumVisibleItem> {

        private SortOrder sortOrder = SortOrder.UNSORTED;

        public DomainComparator(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(EnumVisibleItem o1, EnumVisibleItem o2) {
            String asStr1 = EnumItemDomainsEditor.getDomainListAsStr(o1.enumValue);
            String asStr2 = EnumItemDomainsEditor.getDomainListAsStr(o2.enumValue);
            int result = 0;
            if (sortOrder.equals(SortOrder.DESCENDING) || sortOrder.equals(sortOrder.UNSORTED)) {
                result = Collator.getInstance().compare(asStr1, asStr2);
            } else {
                result = Collator.getInstance().compare(asStr2, asStr1);
            }
            return result;
        }
    }

    private class TitleComparator implements Comparator<EnumVisibleItem> {

        private SortOrder sortOrder = SortOrder.UNSORTED;
        EIsoLanguage lng;

        public TitleComparator(SortOrder sortOrder, int titleIndex) {
            this.sortOrder = sortOrder;
            lng = (EIsoLanguage) enumDef.getModule().getSegment().getLayer().getLanguages().toArray()[titleIndex];
        }

        @Override
        public int compare(EnumVisibleItem o1, EnumVisibleItem o2) {
            //o1.enumValue.get



            String asStr1 = o1.enumValue.getTitle(lng);
            if (asStr1 == null) {
                asStr1 = "";
            }
            String asStr2 = o2.enumValue.getTitle(lng);
            if (asStr2 == null) {
                asStr2 = "";
            }

            int result = 0;
            if (sortOrder.equals(SortOrder.DESCENDING) || sortOrder.equals(sortOrder.UNSORTED)) {
                result = Collator.getInstance().compare(asStr1, asStr2);
            } else {
                result = Collator.getInstance().compare(asStr2, asStr1);
            }
            return result;
        }
    }

    private class ValueComparator implements Comparator<EnumVisibleItem> {

        private SortOrder sortOrder = SortOrder.UNSORTED;

        public ValueComparator(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
        }

        @Override
        public int compare(EnumVisibleItem o1, EnumVisibleItem o2) {

            switch (enumDef.getItemType()) {
                case NUM:
                    BigDecimal numVal1 = (BigDecimal) o1.enumValue.getValue().toObject(EValType.NUM);
                    BigDecimal numVal2 = (BigDecimal) o2.enumValue.getValue().toObject(EValType.NUM);

                    return sortOrder == SortOrder.DESCENDING || sortOrder == sortOrder.UNSORTED  
                            ? numVal1.compareTo(numVal2) 
                            : numVal2.compareTo(numVal1);
                case INT:
                    Long longVal1 = (Long) o1.enumValue.getValue().toObject(EValType.INT);
                    Long longVal2 = (Long) o2.enumValue.getValue().toObject(EValType.INT);

                    return sortOrder == SortOrder.DESCENDING || sortOrder == sortOrder.UNSORTED 
                            ? Long.compare(longVal1, longVal2) 
                            : Long.compare(longVal2, longVal1);
                case STR:
                case CHAR:
                    String asStr1 = o1.enumValue.getValue().toString();
                    String asStr2 = o2.enumValue.getValue().toString();

                    return sortOrder == SortOrder.DESCENDING || sortOrder == sortOrder.UNSORTED
                            ? Collator.getInstance().compare(asStr1, asStr2)
                            : Collator.getInstance().compare(asStr2, asStr1);

            }
            return 0;
        }
    }

    private static final class IndexedComparator implements Comparator<EnumVisibleItem> {

        private final AdsEnumDef adsEnum;

        public IndexedComparator(AdsEnumDef adsEnumDefinition) {
            this.adsEnum = adsEnumDefinition;
        }

        @Override
        public int compare(EnumVisibleItem left, EnumVisibleItem right) {
            //ищем индексы item-ов с теми же id-ами и сравниваем по ним
            final java.util.List<AdsEnumItemDef> order = adsEnum.getViewOrder().getOrder();

            AdsEnumItemDef item1 = null;
            AdsEnumItemDef item2 = null;

            for (AdsEnumItemDef item : order) {
                if (item.getId().equals(left.enumValue.getId())) {
                    item1 = item;
                    break;
                }
            }

            for (AdsEnumItemDef item : order) {
                if (item.getId().equals(right.enumValue.getId())) {
                    item2 = item;
                    break;
                }
            }

            if (item1 == null && item2 == null) {
                Integer lIndex = adsEnum.getItems().get(EScope.ALL).indexOf(left);
                Integer rIndex = adsEnum.getItems().get(EScope.ALL).indexOf(right);
                return lIndex.compareTo(rIndex);
            } else if (item1 == null && item2 != null) {
                return 1;
            } else if (item2 == null && item1 != null) {
                return -1;
            } else {

                final Integer leftIndex = Integer.valueOf(order.indexOf(item1));
                final Integer rightIndex = Integer.valueOf(order.indexOf(item2));
                return leftIndex.compareTo(rightIndex);
            }
        }
    }
}

package org.radixware.kernel.designer.ads.reports;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportForm;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormattedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportWidgetContainer;
import org.radixware.kernel.common.enums.EReportCellType;

public class AdsReportDialogsUtils {
    
    public static void setNewContainerName(AdsReportForm form, AdsReportWidgetContainer container) {
        if (form == null || container == null) {
            return;
        }
        container.setName(findNewCellName(form, container));
    }
    
    public static String findNewCellName(AdsReportForm form, AdsReportWidgetContainer container) {
        if (form == null || container == null) {
            return "";
        }
        final List<String> list = new LinkedList<>();
        form.visitChildren(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                String name = radixObject.getName();
                if (name != null && !name.isEmpty()) {
                    list.add(name);
                }
            }
        }, new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsReportWidgetContainer;
            }
        });
        return findCellName("CellContainer", 0, list);
    }

    public static void setNewCellName(AdsReportForm form, AdsReportCell cell) {
        if (form == null || cell == null) {
            return;
        }
        cell.setName(findNewCellName(form, cell));
    }
    
    public static String findNewCellName(AdsReportForm form, AdsReportCell cell) {
        if (form == null || cell == null) {
            return "";
        }
        final EReportCellType cellType = cell.getCellType();
        final List<String> list = new LinkedList<>();
        form.visitChildren(new IVisitor() {

            @Override
            public void accept(RadixObject radixObject) {
                String name = radixObject.getName();
                if (name != null && !name.isEmpty()) {
                    list.add(name);
                }
            }
        }, new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsReportCell && ((AdsReportCell) radixObject).getCellType() == cellType;
            }
        });
        String name;
        switch (cellType) {
            case PROPERTY:
                AdsReportPropertyCell propertyCell = (AdsReportPropertyCell)cell;
                name = propertyCell.getDefaultName();
                if (name == null){
                    return String.valueOf(propertyCell.getPropertyId());
                }
                break;
            case SPECIAL: case SUMMARY:
                AdsReportFormattedCell formattedCell = (AdsReportFormattedCell) cell;
                name = formattedCell.getDefaultName();
                break;
            default:
                name = cellType.getName();
        }
        return findCellName(name, 0, list);
    }

    private static String findCellName(String name, int number, final List<String> list) {
        String currentName = number == 0 ? name : (name + number);

        for (String cellName : list) {
            if (cellName.equals(currentName)) {
                return findCellName(name, ++number, list);
            }
        }

        return currentName;
    }

    public static final String IGNORE_ZEBRA = "cellIgnoreZebraCell";
    public static final String INHERIT_BACKROUND = "cellInheritBackgroundColor";
    public static final String BACKGROUND_COLOR = "cellBackgroundColor";
    public static final String LINE_SPACING = "cellLineSpacing";
    public static final String BORDER_CHANGE = "reportBorderChange";
}

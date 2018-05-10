package org.radixware.kernel.common.defs.ads.clazz.sql.report.utils;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.Utils;

public class AdsReportMarginTxt {
    private final RadixObject owner;
    private int topRows;
    private int leftCols;
    private int bottomRows;
    private int rightCols;

    public AdsReportMarginTxt() {
        this(null);
    }

    public AdsReportMarginTxt(RadixObject owner) {
        this(owner, 0);
    }
    
    public AdsReportMarginTxt(int defaultMargin) {
        this(null, defaultMargin);
    }
    
    public AdsReportMarginTxt(RadixObject owner, int defaultMargin) {
        this(owner, defaultMargin, defaultMargin, defaultMargin, defaultMargin);
    }
    
    public AdsReportMarginTxt(int topRows,  int leftCols, int bottomRows, int rightCols) {
        this(null, topRows, leftCols, bottomRows, rightCols);
    }

    public AdsReportMarginTxt(RadixObject owner, int topRows, int leftCols, int bottomRows, int rightCols) {
        this.owner = owner;
        this.topRows = topRows;
        this.leftCols = leftCols;
        this.bottomRows = bottomRows;
        this.rightCols = rightCols;
    }
    
    public void setMargin(int margin){
        setMargin(margin, true);
    }

    public void setMargin(int margin, boolean update){
        setMargin(margin, margin, margin, margin, update);
    }
    
    public void setMargin(int topRows,  int leftCols, int bottomRows, int rightCols){
        setMargin(topRows, leftCols, bottomRows, rightCols, true);
    }
    
    public void setMargin(int topRows,  int leftCols, int bottomRows, int rightCols, boolean update){
        setTopRows(topRows, update);
        setLeftCols(leftCols, update);
        setBottomRows(bottomRows, update);
        setRightCols(rightCols, update);
    }

    public int getTopRows() {
        return topRows;
    }

    public void setTopRows(int topRows) {
        setTopRows(topRows, true);
    }
    
    public void setTopRows(int topRows, boolean update) {
        if (!Utils.equals(this.topRows, topRows)) {
            this.topRows = topRows;
            updateOwnerState(update);
        }
    }

    public int getBottomRows() {
        return bottomRows;
    }

    public void setBottomRows(int bottomRows) {
        setBottomRows(bottomRows, true);
    }
    
    public void setBottomRows(int bottomRows, boolean update) {
        if (!Utils.equals(this.bottomRows, bottomRows)) {
            this.bottomRows = bottomRows;
            updateOwnerState(update);
        }
    }

    public int getLeftCols() {
        return leftCols;
    }

    public void setLeftCols(int leftCols) {
        setLeftCols(leftCols, true);
    }
    
    public void setLeftCols(int leftCols, boolean update) {
        if (!Utils.equals(this.leftCols, leftCols)) {
            this.leftCols = leftCols;
            updateOwnerState(update);
        }
    }

    public int getRightCols() {
        return rightCols;
    }

    public void setRightCols(int rightCols) {
        setRightCols(rightCols, true);
    }
    
    public void setRightCols(int rightCols, boolean update) {
        if (!Utils.equals(this.rightCols, rightCols)) {
            this.rightCols = rightCols;
            updateOwnerState(update);
        }
    }
    
    private void updateOwnerState(boolean update){
        if (update && owner != null){
            owner.setEditState(RadixObject.EEditState.MODIFIED);
        }
    }

    @Override
    public String toString() {
        return getMarginString();
    }
    
    public String getMarginString(){
        StringBuilder sb = new StringBuilder();
        sb.append(topRows);
        sb.append(", ");
        sb.append(leftCols);
        sb.append(", ");
        sb.append(bottomRows);
        sb.append(", ");
        sb.append(rightCols);
        return sb.toString();
    }
    
    public AdsReportMarginTxt copy(boolean needOwner) {
        return new AdsReportMarginTxt (needOwner ? owner : null, getTopRows(), getLeftCols(), getBottomRows(), getRightCols());
    }
}

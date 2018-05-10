package org.radixware.kernel.common.defs.ads.clazz.sql.report.utils;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.utils.Utils;

public class AdsReportMarginMm {
    private final RadixObject owner;
    private double topMm; 
    private double bottomMm;
    private double leftMm;
    private double rightMm;

    public AdsReportMarginMm() {
        this((RadixObject) null);
    }

    public AdsReportMarginMm(RadixObject owner) {
        this(owner, 0);
    }
        
    public AdsReportMarginMm(double defaultMargin) {
        this(null, defaultMargin);
    }
    
    public AdsReportMarginMm(RadixObject owner, double defaultMargin) {
        this(owner, defaultMargin, defaultMargin, defaultMargin, defaultMargin);
    }
    
    public AdsReportMarginMm(double topMm,  double leftMm, double bottomMm, double rightMm) {
        this(null, topMm, leftMm, bottomMm, rightMm);
    }

    public AdsReportMarginMm(RadixObject owner, double topMm, double leftMm, double bottomMm, double rightMm) {
        this.owner = owner;
        this.topMm = topMm;
        this.leftMm = leftMm;
        this.bottomMm = bottomMm;
        this.rightMm = rightMm;
    }
    
    public void setMargin(double margin){
        setMargin(margin, true);
    }

    public void setMargin(double margin, boolean update){
        setMargin(margin, margin, margin, margin, update);
    }
    
    public void setMargin(double topMm,  double leftMm, double bottomMm, double rightMm){
        setMargin(topMm, leftMm, bottomMm, rightMm, true);
    }
    
    public void setMargin(double topMm,  double leftMm, double bottomMm, double rightMm, boolean update){
        setTopMm(topMm, update);
        setLeftMm(leftMm, update);
        setBottomMm(bottomMm, update);
        setRightMm(rightMm, update);
    }

    public double getTopMm() {
        return topMm;
    }

    public void setTopMm(double topMm) {
        setTopMm(topMm, true);
    }
    
    public void setTopMm(double topMm, boolean update) {
        if (!Utils.equals(this.topMm, topMm)) {
            this.topMm = topMm;
            updateOwnerState(update);
        }
    }

    public double getBottomMm() {
        return bottomMm;
    }

    public void setBottomMm(double bottomMm) {
        setBottomMm(bottomMm, true);
    }
    
    public void setBottomMm(double bottomMm, boolean update) {
        if (!Utils.equals(this.bottomMm, bottomMm)) {
            this.bottomMm = bottomMm;
            updateOwnerState(update);
        }
    }

    public double getLeftMm() {
        return leftMm;
    }

    public void setLeftMm(double leftMm) {
        setLeftMm(leftMm, true);
    }
    
    public void setLeftMm(double leftMm, boolean update) {
        if (!Utils.equals(this.leftMm, leftMm)) {
            this.leftMm = leftMm;
            updateOwnerState(update);
        }
    }

    public double getRightMm() {
        return rightMm;
    }

    public void setRightMm(double rightMm) {
        setRightMm(rightMm, true);
    }
    
    public void setRightMm(double rightMm, boolean update) {
        if (!Utils.equals(this.rightMm, rightMm)) {
            this.rightMm = rightMm;
            updateOwnerState(update);
        }
    }
    
    private void updateOwnerState(boolean update){
        if (update && owner != null){
            owner.setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String toString() {
        return getMarginString();
    }
    
    public String getMarginString(){
        StringBuilder sb = new StringBuilder();
        sb.append(topMm);
        sb.append(", ");
        sb.append(leftMm);
        sb.append(", ");
        sb.append(bottomMm);
        sb.append(", ");
        sb.append(rightMm);
        return sb.toString();
    }
    
    public AdsReportMarginMm copy(boolean needOwner) {
        return new AdsReportMarginMm (needOwner ? owner : null, getTopMm(), getLeftMm(), getBottomMm(), getRightMm());
    }
}

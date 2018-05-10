package org.radixware.kernel.common.defs.ads.clazz.sql.report.html;


public class AttrInfo {

    private String value;
    private int length;

    public AttrInfo(String value, int length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }
}

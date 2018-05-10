/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.enums;

import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;

/**
 *
 * @author dlastochkin
 */
public enum EXmlSchemaLinkMode implements IKernelStrEnum {

    MANUAL("Manual"),
    IMPORT("Import");
    private final String value;

    private EXmlSchemaLinkMode(final String value) {
        this.value = value;
    }

    public static EXmlSchemaLinkMode getForValue(final String value) {
        for (EXmlSchemaLinkMode status : EXmlSchemaLinkMode.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new NoConstItemWithSuchValueError("EXmlSchemaLinkMode has no item with value: " + String.valueOf(value), value);
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
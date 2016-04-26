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
package org.radixware.kernel.designer.environment.merge;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.enums.EIsoLanguage;

public class AdsMergeChangesOptions extends AbstractMergeChangesOptions {

    private int srcFormatVersion;
    private int destFormatVersion;
    private List<EIsoLanguage> srcLangList;
    private List<EIsoLanguage> destLangList;
    private List<EIsoLanguage> commonLangList;

    private List<AdsMergeChangesItemWrapper> list;

    private final List<Definition> natureDefList;

    private String fromBranchShortName;
    private String toBranchShortName;

    public AdsMergeChangesOptions(final List<Definition> natureDefList) {
        this.natureDefList = new ArrayList(natureDefList);
    }

    public List<Definition> getNatureDefList() {
        return natureDefList;
    }

    @Override
    public String getToBranchShortName() {
        return toBranchShortName;
    }

    public void setToBranchShortName(String toBranchShortName) {
        this.toBranchShortName = toBranchShortName;
    }

    @Override
    public String getFromBranchShortName() {
        return fromBranchShortName;
    }

    public void setFromBranchShortName(String trunkShortName) {
        this.fromBranchShortName = trunkShortName;
    }

    public List<AdsMergeChangesItemWrapper> getList() {
        return list;
    }

    public void setList(List<AdsMergeChangesItemWrapper> list) {
        this.list = list;
    }

    public int getFromFormatVersion() {
        return srcFormatVersion;
    }

    public void setFromFormatVersion(int srcFormatVersion) {
        this.srcFormatVersion = srcFormatVersion;
    }

    public int getToFormatVersion() {
        return destFormatVersion;
    }

    public void setToFormatVersion(int destFormatVersion) {
        this.destFormatVersion = destFormatVersion;
    }

    public List<EIsoLanguage> getSrcLangList() {
        return srcLangList;
    }

    public void setSrcLangList(List<EIsoLanguage> srcLangList) {
        this.srcLangList = srcLangList;
    }

    public List<EIsoLanguage> getDestLangList() {
        return destLangList;
    }

    public void setDestLangList(List<EIsoLanguage> destLangList) {
        this.destLangList = destLangList;
    }

    public List<EIsoLanguage> getCommonLangList() {
        return commonLangList;
    }

    public void setCommonLangList(List<EIsoLanguage> commonLangList) {
        this.commonLangList = commonLangList;
    }

    @Override
    public String getNameByIndex(final int index) {
        return getList().get(index).getDef().getQualifiedName();
    }

    @Override
    public String getFromPathByIndex(int index) {
        return getList().get(index).getFromPath();
    }

    @Override
    public String getToPathByIndex(int index) {
        return getList().get(index).getToPath();
    }
}

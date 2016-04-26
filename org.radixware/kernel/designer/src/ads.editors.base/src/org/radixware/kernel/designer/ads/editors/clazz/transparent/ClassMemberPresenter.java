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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.text.Collator;
import java.util.Comparator;
import javax.swing.Icon;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.designer.common.dialogs.components.selector.MultipleListItemSelector;


abstract class ClassMemberPresenter<SourceType, PlatformType> implements Comparable<ClassMemberPresenter>  {

    final static Comparator<ClassMemberPresenter> COMPARATOR = new Comparator<ClassMemberPresenter>() {

        @Override
        public int compare(ClassMemberPresenter o1, ClassMemberPresenter o2) {
            String o1str = o1.getName();
            String o2str = o2.getName();

            int res = o1str.compareTo(o2str);
            return res == 0 ? Collator.getInstance().compare(o1.toString(), o2.toString()) : res;
        }
    };

    private EPresenterState state;

    private boolean saveNamesWhenPublish = false;
    private boolean published;

    private SourceType source;
    private final PlatformType platformSource;
    private final Definition context;

    protected final MultipleListItemSelector.IItemInfo memberInfo;

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public ClassMemberPresenter(SourceType source, PlatformType platformSource,
        Definition context, boolean published, MultipleListItemSelector.IItemInfo memberInfo) {

        this.source = source;
        this.platformSource = platformSource;
        this.context = context;
        this.published = published;
        this.memberInfo = memberInfo;
    }

    @Override
    public int compareTo(ClassMemberPresenter o) {

        assert o != null : "Compare with null";

        if (o != null) {
            return toString().compareTo(o.toString());
        }
        return 1;
    }

    @Override
    public String toString() {
        return "classMember";
    }

    private void fireChange() {
        changeSupport.fireChange();
    }

    public boolean isCorrect() {
        return true;
    }

    public final boolean isPublished() {
        return published;
    }

    public final void setState(EPresenterState state) {

        assert !(isPublished() && state == EPresenterState.PUBLISH);

        this.state = state;
        fireChange();
    }

    public final EPresenterState getState() {
        return state;
    }

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public final void setSaveNames(boolean save) {
        this.saveNamesWhenPublish = save;
    }

    public final boolean isSaveNames() {
        return saveNamesWhenPublish;
    }

    public final SourceType getSource() {
        return source;
    }

    public final PlatformType getPlatformSource() {
        return platformSource;
    }

    public final Definition getContext() {
        return context;
    }

    public final boolean isMarkedToBePublished() {
        return getState() == EPresenterState.PUBLISH || getState() == EPresenterState.REPLACE;
    }

    /**
     * Can whether be replaced by target
     * @return true if can replaced by other presenter, false otherwise
     */
    public boolean isApplicable(ClassMemberPresenter<SourceType, PlatformType> target) {
        return true;
    }

    /**
     * Can be replaced by other presenter
     * @return true if can be replaced by other presenter, false otherwise
     */
    public boolean isChangeable() {
        return true;
    }

    public String getName() {
        if (memberInfo != null) {
            return memberInfo.toString();
        }
        return "<Not Defined>";
    }

    public Icon getIcon() {
        if (memberInfo != null) {
            return memberInfo.getIcon();
        }
        return null;
    }

    abstract void apply(Link link, AdsClassDef owner);
}

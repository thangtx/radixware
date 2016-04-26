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

package org.radixware.kernel.common.defs.ads.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.IEditorPagesContainer;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage.CommandInfo;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.enums.*;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomParagEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtWidgetDef;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.schemas.ui.Anchors;
import org.radixware.schemas.ui.BooleanValue;
import org.radixware.schemas.ui.Color;
import org.radixware.schemas.ui.Date;
import org.radixware.schemas.ui.DateTime;
import org.radixware.schemas.ui.EmbeddedEditorParams;
import org.radixware.schemas.ui.EmbeddedSelectorParams;
import org.radixware.schemas.ui.Font;
import org.radixware.schemas.ui.IdList;
import org.radixware.schemas.ui.Property;
import org.radixware.schemas.ui.Rect;
import org.radixware.schemas.ui.Size;
import org.radixware.schemas.ui.SizePolicy;
import org.radixware.schemas.ui.Time;


public abstract class AdsUIProperty extends RadixObject {

    protected AdsUIProperty(String name) {
        super(name);
    }

    public AdsAbstractUIDef findOwnerUiDef() {
        return AdsUIUtil.getUiDef(this);
    }

    public AdsClassDef findOwnerClass() {
        return AdsUIUtil.getOwnerClassDef(this);
    }

    public AdsPresentationDef findOwnerPresentation() {
        return AdsUIUtil.getOwnerPresentation(this);
    }

    public AdsModule findOwnerModule() {
        return AdsUIUtil.getModule(this);
    }

    protected AdsLocalizingBundleDef findLocalizingBundle() {
        return AdsUIUtil.getLocalizingBundle(this);
    }

    public void appendTo(Property xDef) {
        xDef.setName(getName());
        appendToImpl(xDef);
    }

    public abstract void appendToImpl(Property xDef);

    public AdsUIProperty duplicate() {
        Property xProp = Property.Factory.newInstance();
        appendTo(xProp);
        return Factory.loadFrom(xProp);
    }

    boolean shouldStore() {
        return isPersistent();
    }

    public static class AccessProperty extends AdsUIProperty {

        public static final String NAME = "access";
        private WeakReference<AdsWidgetDef> widget;

        public AccessProperty(AdsWidgetDef widget) {
            super(NAME);
            if (widget != null) {
                this.widget = new WeakReference<AdsWidgetDef>(widget);
            }
        }

        public EAccess getAccess() {
            if (widget != null) {
                AdsWidgetDef w = widget.get();
                if (w != null) {
                    return w.getAccessMode();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        public void setAccess(EAccess access) {
            if (widget != null) {
                AdsWidgetDef w = widget.get();
                if (w != null) {
                    w.setAccessMode(access);
                }
            }
        }

        @Override
        public void appendToImpl(Property xDef) {
            //do nothing
        }

        @Override
        protected boolean isPersistent() {
            return false;
        }

        @Override
        public AdsUIProperty duplicate() {
            if (widget != null) {
                return new AccessProperty(widget.get());
            } else {
                return new AccessProperty(null);
            }
        }
    }

    public static class BooleanProperty extends AdsUIProperty {

        public boolean value;

        public BooleanProperty(String name, boolean val) {
            super(name);
            this.value = val;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setBool(value);
        }
    }

    public static class StringProperty extends AdsUIProperty {

        public String value;

        public StringProperty(String name, String val) {
            super(name);
            this.value = val;
        }

        public StringProperty(String name) {
            super(name);
            this.value = null;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setString(value);
        }
    }

    public static final class BooleanValueProperty extends AdsUIProperty {

        public Boolean value;

        public BooleanValueProperty(String name) {
            this(name, (Boolean) null);
        }

        public BooleanValueProperty(String name, Boolean value) {
            super(name);
            assert name != null : "Value is null";
            this.value = value;
        }

        public BooleanValueProperty(String name, BooleanValue.Enum value) {
            this(name, (value != BooleanValue.NULL ? (value == BooleanValue.TRUE ? Boolean.TRUE : Boolean.FALSE) : null));
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setBoolValue(value != null ? (value ? BooleanValue.TRUE : BooleanValue.FALSE) : BooleanValue.NULL);
        }
    }

    public static final class AnchorProperty extends AdsUIProperty {

        public static class Anchor {

            public float part;
            public int offset;
            public final Id refId;

            public Anchor(float part, int offset, Id refId) {
                this.part = part;
                this.offset = offset;
                this.refId = refId;
            }
        }
        private Anchor top, left, right, bottom;

        public AnchorProperty(String name) {
            this(name, null);
        }

        public AnchorProperty(String name, Anchors xDef) {
            super(name);
            if (xDef != null) {
                if (xDef.getTop() != null) {
                    this.top = new Anchor(xDef.getTop().getPart(), xDef.getTop().getOffset(), xDef.getTop().getRef());
                }
                if (xDef.getLeft() != null) {
                    this.left = new Anchor(xDef.getLeft().getPart(), xDef.getLeft().getOffset(), xDef.getLeft().getRef());
                }
                if (xDef.getRight() != null) {
                    this.right = new Anchor(xDef.getRight().getPart(), xDef.getRight().getOffset(), xDef.getRight().getRef());
                }
                if (xDef.getBottom() != null) {
                    this.bottom = new Anchor(xDef.getBottom().getPart(), xDef.getBottom().getOffset(), xDef.getBottom().getRef());
                }
            }
        }

        @Override
        public void appendToImpl(Property xDef) {

            Anchors anchors = xDef.addNewAnchor();
            if (top != null) {
                org.radixware.schemas.ui.Anchor xAnchor = anchors.addNewTop();
                xAnchor.setPart(top.part);
                xAnchor.setOffset(top.offset);
                xAnchor.setRef(top.refId);
            }
            if (left != null) {
                org.radixware.schemas.ui.Anchor xAnchor = anchors.addNewLeft();
                xAnchor.setPart(left.part);
                xAnchor.setOffset(left.offset);
                xAnchor.setRef(left.refId);
            }
            if (right != null) {
                org.radixware.schemas.ui.Anchor xAnchor = anchors.addNewRight();
                xAnchor.setPart(right.part);
                xAnchor.setOffset(right.offset);
                xAnchor.setRef(right.refId);
            }
            if (bottom != null) {
                org.radixware.schemas.ui.Anchor xAnchor = anchors.addNewBottom();
                xAnchor.setPart(bottom.part);
                xAnchor.setOffset(bottom.offset);
                xAnchor.setRef(bottom.refId);
            }

        }

        public Anchor getBottom() {
            return bottom;
        }

        public void setBottom(Anchor bottom) {
            this.bottom = bottom;
            setEditState(EEditState.MODIFIED);
        }

        public Anchor getLeft() {
            return left;
        }

        public void setLeft(Anchor left) {
            this.left = left;
            setEditState(EEditState.MODIFIED);
        }

        public Anchor getRight() {
            return right;
        }

        public void setRight(Anchor right) {
            this.right = right;
            setEditState(EEditState.MODIFIED);
        }

        public Anchor getTop() {
            return top;
        }

        public void setTop(Anchor top) {
            this.top = top;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static class EnumValueProperty extends AdsUIProperty {

        private static final HashMap<String, Class<? extends UIEnum>> hash = new HashMap<>();

        static {
            register("sizeType", ESizePolicy.class);
            register("orientation", EOrientation.class);
            register("arrowType", EArrowType.class);
            register("cursor", ECursorShape.class);
            register("toolButtonStyle", EToolButtonStyle.class);
            register("popupMode", EPopupMode.class);
            register("contextMenuPolicy", EContextMenuPolicy.class);
            register("layoutDirection", ELayoutDirection.class);
            register("tabPosition", ETabPosition.class);
            register("tabShape", ETabShape.class);
            register("elideMode", ETextElideMode.class);
            register("verticalScrollBarPolicy", EScrollBarPolicy.class);
            register("horizontalScrollBarPolicy", EScrollBarPolicy.class);
            register("horizontalScrollBarPolicy", EScrollBarPolicy.class);
            register("dragDropMode", EDragDropMode.class);
            register("dragDropMode", EDragDropMode.class);
            register("selectionMode", ESelectionMode.class);
            register("selectionBehavior", ESelectionBehavior.class);
            register("horizontalScrollMode", EScrollMode.class);
            register("verticalScrollMode", EScrollMode.class);
            register("gridStyle", EPenStyle.class);
            register("viewMode", EViewMode.class);
            register("frameShadow", EFrameShadow.class);
            register("frameShape", EFrameShape.class);
            register("reactionToIntermediateInput", EReactionToIntermediateInput.class);
            register("checkState", ECheckState.class);
            register("arrayType", EArrayClassName.class);
            register("layoutSizeConstraint", ESizeConstraint.class);
            register("resizeMode", EResizeMode.class);
        }

        public static void register(String name, Class<? extends UIEnum> clazz) {
            hash.put(name, clazz);
        }
        public UIEnum value = null;

        public EnumValueProperty(String name, String asStr) {
            super(name);

            Class clazz = hash.get(name);
            assert clazz != null : "property " + name + " not registered";

            try {
                @SuppressWarnings("unchecked")
                Method m = clazz.getMethod("getForValue", new Class[]{String.class});
                value = (UIEnum) m.invoke(null, new Object[]{asStr});
            } catch (Throwable ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                assert false : "reflection error: " + ex.toString();
            }
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setEnum(value != null ? value.getQualifiedValue() : null);
        }

        @SuppressWarnings("unchecked")
        public final <T extends UIEnum> T getValue() {
            return (T) value;
        }
    }

    public static class FloatProperty extends AdsUIProperty {

        public float value;

        public FloatProperty(String name, float value) {
            super(name);
            this.value = value;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setFloat(value);
        }
    }

    public static class IdListProperty extends AdsUIProperty {

        private List<Id> ids = new LinkedList<Id>();

        public IdListProperty(String name) {
            super(name);
        }

        public IdListProperty(String name, IdList xList) {
            super(name);
            for (Id id : xList.getIdList()) {
                ids.add(id);
            }
        }

        @Override
        public void appendToImpl(Property xDef) {
            IdList list = xDef.addNewIdList();
            for (Id id : ids) {
                list.getIdList().add(id);
            }
        }

        public List<Id> getIds() {
            return ids;
        }

        public void setIds(List<Id> ids) {
            this.ids.clear();
            this.ids.addAll(ids);
        }
    }

    public static class DoubleProperty extends AdsUIProperty {

        public double value;

        public DoubleProperty(String name, double value) {
            super(name);
            this.value = value;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setDouble(value);
        }
    }

    public static class ImageProperty extends AdsUIProperty {

        private Id imageId;

        public ImageProperty(String name, String iconId) {
            this(name, Id.Factory.loadFrom(iconId));
        }

        public ImageProperty(String name, Id imageId) {
            super(name);
            this.imageId = imageId;
        }

        public ImageProperty(String name) {
            super(name);
            this.imageId = null;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setImageRef(imageId != null ? imageId.toString() : null);
        }

        public Id getImageId() {
            return imageId;
        }

        public void setImageId(Id imageId) {
            if (this.imageId != imageId) {
                this.imageId = imageId;
                setEditState(EEditState.MODIFIED);
            }

        }

        public AdsImageDef findImage() {
            if (imageId == null) {
                return null;
            }
            return AdsSearcher.Factory.newImageSearcher(findOwnerUiDef()).findById(imageId).get();
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsImageDef imageDef = findImage();
            if (imageDef != null) {
                list.add(imageDef);
            }
        }
    }

    public static class LongProperty extends AdsUIProperty {

        public long value;

        public LongProperty(String name, long value) {
            super(name);
            this.value = value;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setLong(value);
        }
    }

    public static class RectProperty extends AdsUIProperty {

        public int x, y, width, height;

        public RectProperty(String name) {
            super(name);
        }

        public RectProperty(String name, int x, int y, int width, int height) {
            super(name);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public RectProperty(String name, Rectangle rectangle) {
            this(name, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }

        private RectProperty(String name, Rect xRect) {
            super(name);
            x = xRect.getX();
            y = xRect.getY();
            width = xRect.getWidth();
            height = xRect.getHeight();
        }

        @Override
        public void appendToImpl(Property xDef) {
            Rect xRect = xDef.addNewRect();
            xRect.setX(x);
            xRect.setY(y);
            xRect.setWidth(width);
            xRect.setHeight(height);
        }

        public Rectangle getRectangle() {
            return new Rectangle(x, y, width, height);
        }
    }

    public static class SetProperty extends AdsUIProperty {

        private static final HashMap<String, Class<? extends UIEnum>> hash = new HashMap<String, Class<? extends UIEnum>>();

        static {
            register("alignment", EAlignment.class);
            register("textAlignment", EAlignment.class);
            register("standardButtons", EStandardButton.class);
        }

        public static void register(String name, Class<? extends UIEnum> clazz) {
            hash.put(name, clazz);
        }
        public String value;

        public SetProperty(String name, String value) {
            super(name);
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public UIEnum[] getValues() {
            Class clazz = hash.get(getName());
            assert clazz != null : "property " + getName() + " not registered";

            String[] items = value.split("\\|");
            UIEnum[] values = new UIEnum[items.length];

            try {
                Method m = clazz.getMethod("getForValue", new Class[]{String.class});
                for (int i = 0; i < items.length; i++) {
                    values[i] = (UIEnum) m.invoke(null, new Object[]{items[i].trim()});
                }
            } catch (Throwable ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                assert false : "reflection error: " + ex.toString();                
            }

            return values;
        }

        public void setValues(UIEnum[] values) {
            value = "";
            for (int i = 0; i < values.length; i++) {
                value += values[i].getQualifiedValue();
                if (i < values.length - 1) {
                    value += " | ";
                }
            }
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setSet(value);
        }
    }

    public static class SizeProperty extends AdsUIProperty {

        public int width = 40, height = 40;

        public SizeProperty(String name, int width, int height) {
            super(name);
            setSize(width, height);
        }

        public SizeProperty(String name, Size xSize) {
            super(name);
            setSize(xSize.getWidth(), xSize.getHeight());
        }

        public void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void appendToImpl(Property xDef) {
            Size xSize = xDef.addNewSize();
            xSize.setWidth(width);
            xSize.setHeight(height);
        }

        public Dimension getDimension() {
            return new Dimension(width, height);
        }
    }

    public static class SizePolicyProperty extends AdsUIProperty {

        public ESizePolicy hSizeType, vSizeType;

        public SizePolicyProperty(String name, SizePolicy sp) {
            super(name);
            hSizeType = ESizePolicy.getForValue(sp.getHSizeType2());
            vSizeType = ESizePolicy.getForValue(sp.getVSizeType2());
        }

        public SizePolicyProperty(String name, ESizePolicy hSizeType, ESizePolicy vSizeType) {
            super(name);
            this.hSizeType = hSizeType;
            this.vSizeType = vSizeType;
        }

        @Override
        public void appendToImpl(Property xDef) {
            SizePolicy sp = xDef.addNewSizePolicy();
            sp.setHSizeType2(hSizeType != null ? hSizeType.getQualifiedValue() : null);
            sp.setVSizeType2(vSizeType != null ? vSizeType.getQualifiedValue() : null);
        }
    }

    public static class TypeDeclarationProperty extends AdsUIProperty {

        private AdsTypeDeclaration typeDeclaration = AdsTypeDeclaration.UNDEFINED;

        public TypeDeclarationProperty(String name) {
            super(name);
        }

        public TypeDeclarationProperty(String name, AdsTypeDeclaration typeDecl) {
            super(name);
            setType(typeDecl);
        }

        public AdsTypeDeclaration getType() {
            return typeDeclaration;
        }

        public final void setType(AdsTypeDeclaration type) {
            if (type == null) {
                typeDeclaration = AdsTypeDeclaration.UNDEFINED;
            } else {
                typeDeclaration = type;
            }
            setEditState(EEditState.MODIFIED);
        }

        @Override
        public void appendToImpl(Property xDef) {
            typeDeclaration.appendTo(xDef.addNewTypeDeclaration());
        }
    }

    public static class IntProperty extends AdsUIProperty {

        public int value;

        public IntProperty(String name, int value) {
            super(name);
            this.value = value;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setInt(value);
        }
    }

    public static class PropertyRefProperty extends AdsUIProperty {

        private Id propId;

        public PropertyRefProperty(String name, String propId) {
            super(name);
            this.propId = Id.Factory.loadFrom(propId);
        }

        public PropertyRefProperty(String name) {
            super(name);
            this.propId = null;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setPropertyRef(propId != null ? propId.toString() : null);
        }

        public Id getPropertyId() {
            return propId;
        }

        public void setPropertyId(Id propId) {
            this.propId = propId;
        }

        public IModelPublishableProperty findProperty() {
            if (propId == null) {
                return null;
            }

            final IModelPublishableProperty.Provider provider = getProvider(this);
            //AdsUIUtil.getModelPublishablePropertyProvider(this);
            if (provider != null) {
                final IModelPublishableProperty prop = provider.getModelPublishablePropertySupport().findById(propId, EScope.ALL);
                if (prop != null) {
                    return prop;
                }
            }
            AdsAbstractUIDef uiDef = AdsUIUtil.getUiDef(this);
            if (uiDef != null) {
                AdsModelClassDef modelClass = uiDef.getModelClass();
                if (modelClass != null) {
                    return modelClass.getProperties().findById(propId, EScope.ALL).get();
                }
            }
            //AdsModelClassDef model = AdsUIUtil.getModelByUI(AdsUIUtil.getUiDef(this));
            //if (model != null)
            //    return model.getProperties().findById(propId, EScope.ALL);
            return null;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            IModelPublishableProperty prop = findProperty();
            if (prop instanceof Definition) {
                list.add((Definition) prop);
            }
        }

        public static IModelPublishableProperty.Provider getProvider(RadixObject node) {
            IModelPublishableProperty.Provider provider = AdsUIUtil.getModelPublishablePropertyProvider(node);
            if (AdsUIUtil.inCustomSelector(node)) {
                if (provider instanceof AdsEntityClassDef) {
                    return ((AdsEntityClassDef) provider).findEntityGroup();
                } else if (provider instanceof AdsApplicationClassDef) {
                    final AdsEntityClassDef rootBasis = ((AdsApplicationClassDef) provider).findRootBasis();
                    if (rootBasis != null) {
                        return rootBasis.findEntityGroup();
                    }
                }
            }
            if (provider == null) {
                AdsAbstractUIDef ui = AdsUIUtil.getUiDef(node);
                if (ui != null) {
                    AdsModelClassDef modelClass = ui.getModelClass();
                    if (modelClass instanceof IModelPublishableProperty.Provider) {
                        provider = (IModelPublishableProperty.Provider) modelClass;
                    }
                }
            }
            return provider;
        }
    }

    public static class UrlProperty extends AdsUIProperty {

        String url;

        public UrlProperty(String name, String url) {
            super(name);
            this.url = url;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setUrl(url);
        }
    }

    public static class LocalizedStringRefProperty extends AdsUIProperty implements ILocalizedDef {

        private Id stringId;

        public LocalizedStringRefProperty(String name, AdsMultilingualStringDef stringDef) {
            this(name, String.valueOf(stringDef.getId()));
        }

        public LocalizedStringRefProperty(String name, String stringId) {
            super(name);
            this.stringId = Id.Factory.loadFrom(stringId);
        }

        public LocalizedStringRefProperty(String name) {
            super(name);
            this.stringId = null;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setMlstringid(stringId != null ? stringId.toString() : null);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsMultilingualStringDef mls = findLocalizedString();
            if (mls != null) {
                list.add(mls);
            }
        }

        @Override
        public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
            ids.add(new MultilingualStringInfo(this) {
                @Override
                public Id getId() {
                    return stringId;
                }

                @Override
                public void updateId(Id newId) {
                    stringId = newId;
                }

                @Override
                public EAccess getAccess() {
                    AdsAbstractUIDef def = findOwnerUiDef();
                    if (def == null) {
                        return EAccess.DEFAULT;
                    }
                    return def.getAccessMode();
                }

                @Override
                public String getContextDescription() {
                    Definition ownerDefinition = LocalizedStringRefProperty.this.getOwnerDefinition();
                    String className;
                    if (ownerDefinition instanceof AdsWidgetDef) {
                        className = ((AdsWidgetDef) ownerDefinition).getClassName();
                    } else if (ownerDefinition instanceof AdsRwtWidgetDef) {
                        className = ((AdsRwtWidgetDef) ownerDefinition).getClassName();
                    } else {
                        className = "";
                    }

                    if (className.equals(AdsMetaInfo.PUSH_BUTTON_CLASS)) {
                        return "Button" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TOOL_BUTTON_CLASS)) {
                        return "Tool Button" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.RADIO_BUTTON_CLASS)) {
                        return "Radio Button" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.CHECK_BOX_CLASS)) {
                        return "Check Box" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS)) {
                        return "Dialog Button Box" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.LABEL_CLASS)) {
                        return "Label" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.PROGRESS_BAR_CLASS)) {
                        return "Progress bar" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.COMBO_BOX_CLASS)) {
                        return "Combo Box" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.LINE_EDIT_CLASS)) {
                        return "Edit Line" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TEXT_EDIT_CLASS)) {
                        return "Text Edit" + " " + getPtopName();
                    } else if ((className.equals(AdsMetaInfo.SPIN_BOX_CLASS)) || (className.equals(AdsMetaInfo.DOUBLE_SPIN_BOX_CLASS))) {
                        return "Spin Box" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.DATE_EDIT_CLASS)) {
                        return "Data Edit" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.DATE_TIME_EDIT_CLASS)) {
                        return "Data Time Edit" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TIME_EDIT_CLASS)) {
                        return "Time Edit" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.LIST_WIDGET_CLASS)) {
                        //return getPtopName() + " in " + "List Widget";
                        return "List Widget " + getTitle() + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.LIST_WIDGET_ITEM_CLASS)) {
                        return "List Item " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TREE_WIDGET_CLASS)) {
                        // return getPtopName() + " in " + "Tree Widget";
                        return "Tree Widget " + getTitle() + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TREE_WIDGET_ITEM_CLASS)) {
                        return "Tree Item" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TABLE_WIDGET_CLASS)) {
                        //return getPtopName() + " in " + "Table Widget";
                        return "Table Widget " + getTitle() + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TABLE_WIDGET_ITEM_CLASS)) {
                        return "Table Item" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.GROUP_BOX_CLASS)) {
                        return "Group Box" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TAB_WIDGET_CLASS)) {
                        return "Tab" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.SPLITTER_CLASS)) {
                        return "Splitter" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.ADVANCED_SPLITTER_CLASS)) {
                        return "Advance Splitter" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.FRAME_CLASS)) {
                        return "Frame" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.SCROLL_AREA_CLASS)) {
                        return "Scroll Area " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.STACKED_WIDGET_CLASS)) {
                        return "Stacked Widget" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.TOOL_BAR_CLASS)) {
                        return "Tool Bar" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.PROP_LABEL_CLASS)) {
                        return "Property Label" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.PROP_EDITOR_CLASS)) {
                        return "Property Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS)) {
                        return "Command Button" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.EMBEDDED_EDITOR_CLASS)) {
                        return "Embedded Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.EMBEDDED_SELECTOR_CLASS)) {
                        return "Embedded Slector" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.EDITOR_PAGE_CLASS)) {
                        return "Editor Page" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.CUSTOM_WIDGET)) {
                        return "Custom Widget" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.WIDGET_CLASS)) {
                        return "Widget" + " " + getPtopName();

                    } else if (className.equals(AdsMetaInfo.VAL_BIN_EDITOR_CLASS)) {
                        return "Val Bin Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_BOOL_EDITOR_CLASS)) {
                        return "Val Bool Editor" + " " + getPtopName();//boolean editor widget
                    } else if (className.equals(AdsMetaInfo.VAL_CHAR_EDITOR_CLASS)) {
                        return "Val Char Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_STR_EDITOR_CLASS)) {
                        return "Val Str Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_INT_EDITOR_CLASS)) {
                        return "Val Int Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_NUM_EDITOR_CLASS)) {
                        return "Val Num Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_CONST_SET_EDITOR_CLASS)) {
                        return "Val Const Set Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_LIST_EDITOR_CLASS)) {
                        return "Val List Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_TIMEINTERVAL_EDITOR_CLASS)) {
                        return "Val Time Interval Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_FILE_PATH_EDITOR_CLASS)) {
                        return "Val File Path Editor" + " " + getPtopName();
                    } else if (className.equals(AdsMetaInfo.VAL_REF_EDITOR_CLASS)) {
                        return "Val Ref Editor" + " " + getPtopName();
                    }

                    return getPtopName();
                }

                private String getTitle() {
                    RadixObject obj = LocalizedStringRefProperty.this.getOwnerForQualifedName();
                    if ((obj != null) && ((obj instanceof AdsItemWidgetDef.Row)
                            || (obj instanceof AdsItemWidgetDef.Column) || (obj instanceof AdsItemWidgetDef.WidgetItem))) {
                        if (obj instanceof AdsItemWidgetDef.WidgetItem) {
                            return "Item";
                        }
                        return obj.getTypeTitle();
                    }
                    return "";
                }

                private String getPtopName() {
                    String propName = LocalizedStringRefProperty.this.getName();
                    if (propName.equals("toolTip")) {
                        return "Tool Tip";
                    } else if (propName.equals("statusTip")) {
                        return "Status Tip";
                    } else if (propName.equals("windowTitle")) {
                        return "Window Title";
                    } else if (propName.equals("inputMask")) {
                        return "Input Mask";
                    } else if (propName.equals("suffix")) {
                        return "Suffix";
                    } else if (propName.equals("prefix")) {
                        return "Prefix";
                    } else if (propName.equals("specialValueText")) {
                        return "Special Value Text";
                    } else if (propName.equals("title")) {
                        return "Title";
                    } else if (propName.equals("label")) {
                        return "Label";
                    }
                    return "Text";
                }

                @Override
                public boolean isPublished() {
                    AdsAbstractUIDef def = findOwnerUiDef();
                    if (def == null) {
                        return false;
                    }
                    return def.isPublished();
                }

                @Override
                public EMultilingualStringKind getKind() {
                    String propName = LocalizedStringRefProperty.this.getName();
                    if (propName.equals("toolTip") || propName.equals("statusTip") || propName.equals("specialValueText")) {
                        return EMultilingualStringKind.TOOLTIP;
                    }
                    return EMultilingualStringKind.TITLE;
                }

            });
        }

        public Id getStringId() {
            return stringId;
        }

        public void setStringId(Id stringId) {
            this.stringId = stringId;
            setEditState(EEditState.MODIFIED);
        }

        public AdsMultilingualStringDef findLocalizedString() {
            return findLocalizedString(stringId);
        }

        @Override
        public AdsMultilingualStringDef findLocalizedString(Id stringId) {
            AdsLocalizingBundleDef bundle = findLocalizingBundle();
            if (bundle != null) {
                return bundle.getStrings().findById(stringId, EScope.ALL).get();
            }
            return null;
        }
    }

    public abstract static class EmbeddedWidgetOpenParams extends AdsUIProperty {

        protected Id propId;
        protected Id explorerItemId;

        public EmbeddedWidgetOpenParams(String name, Id propId, Id explorerItemId) {
            super(name);
            this.propId = propId;
            this.explorerItemId = explorerItemId;
        }

        public AdsPropertyDef findProperty() {
            if (propId == null) {
                return null;
            }
            AdsClassDef clazz = findOwnerClass();
            if (clazz != null) {
                return clazz.getProperties().findById(propId, EScope.ALL).get();
            } else {
                return null;
            }
        }

        public boolean isEmpty() {
            return propId == null && explorerItemId == null;
        }

        /*
         * public AdsEntityObjectClassDef findOwnerClass() { AdsUIDef uiDef =
         * findOwnerUiDef(); if (uiDef instanceof AdsCustomEditorDef) { return
         * ((AdsCustomEditorDef) uiDef).getOwnerClass(); } else if (uiDef
         * instanceof AdsCustomSelectorDef) { return ((AdsCustomSelectorDef)
         * uiDef).getOwnerClass(); } else { return null; } }
         */
        public AdsExplorerItemDef findExplorerItem() {
            if (explorerItemId == null) {
                return null;
            }
            AdsClassDef clazz = findOwnerClass();
            if (clazz != null && clazz instanceof AdsEntityObjectClassDef) {
                for (AdsEditorPresentationDef epr : ((AdsEntityObjectClassDef) clazz).getPresentations().getEditorPresentations().get(EScope.ALL)) {
                    AdsExplorerItemDef ei = epr.getExplorerItems().findChildExplorerItem(explorerItemId);
                    if (ei != null) {
                        return ei;
                    }
                }
                return null;
            } else {

                AdsParagraphExplorerItemDef ownerParagraph = null;
                final AdsAbstractUIDef ownerUiDef = findOwnerUiDef();

                if (ownerUiDef instanceof AdsCustomParagEditorDef) {
                    ownerParagraph = ((AdsCustomParagEditorDef) ownerUiDef).getOwnerParagraph();
                } else if (ownerUiDef instanceof AdsRwtCustomParagEditorDef) {
                    ownerParagraph = ((AdsRwtCustomParagEditorDef) ownerUiDef).getOwnerParagraph();
                }

                if (ownerParagraph != null) {
                    final AdsExplorerItemDef item = ownerParagraph.getExplorerItems().findChildExplorerItem(explorerItemId);
                    if (item != null) {
                        return item;
                    }
                }
                return null;
            }
        }

        public void setExplorerItemId(Id explorerItemId) {
            this.explorerItemId = explorerItemId;
        }

        public Id getExplorerItemId() {
            return explorerItemId;
        }

        public void setPropertyId(Id propId) {
            this.propId = propId;
        }

        public Id getPropertyId() {
            return propId;
        }
    }

    public static class EmbeddedEditorOpenParamsProperty extends EmbeddedWidgetOpenParams {

        private class PresentationInfo {

            private Id classId;
            private Id presentationId;

            PresentationInfo() {
                classId = null;
                presentationId = null;
            }

            PresentationInfo(EmbeddedEditorParams.EditorPresentation xDef) {
                classId = Id.Factory.loadFrom(xDef.getClassId());
                presentationId = Id.Factory.loadFrom(xDef.getPresentationId());
            }

            private void appendTo(EmbeddedEditorParams.EditorPresentation xDef) {
                if (classId != null) {
                    xDef.setClassId(classId.toString());
                }
                if (presentationId != null) {
                    xDef.setPresentationId(presentationId.toString());
                }
            }
        }
        private PresentationInfo editorPresentation;

        public EmbeddedEditorOpenParamsProperty(String name, EmbeddedEditorParams xDef) {
            super(name, Id.Factory.loadFrom(xDef.getProperty()), Id.Factory.loadFrom(xDef.getExplorerItem()));
            if (xDef.getEditorPresentation() != null) {
                this.editorPresentation = new PresentationInfo(xDef.getEditorPresentation());
            }
        }

        public EmbeddedEditorOpenParamsProperty(String name) {
            super(name, null, null);

        }

        @Override
        public boolean isEmpty() {
            return super.isEmpty() && (editorPresentation == null || editorPresentation.classId == null || editorPresentation.presentationId == null);
        }

        @Override
        public void appendToImpl(Property xDef) {
            EmbeddedEditorParams params = xDef.addNewEmbeddedEditorOpenParams();
            if (propId != null) {
                params.setProperty(propId.toString());
            } else if (explorerItemId != null) {
                params.setExplorerItem(explorerItemId.toString());
            } else if (editorPresentation != null) {
                editorPresentation.appendTo(params.addNewEditorPresentation());
            }
        }

        public AdsEntityObjectClassDef findClass() {
            if (editorPresentation == null) {
                return null;
            } else {
                if (editorPresentation.classId == null) {
                    return null;
                } else {
                    AdsAbstractUIDef owner = findOwnerUiDef();
                    if (owner != null) {
                        AdsClassDef cd = AdsSearcher.Factory.newAdsClassSearcher(owner).findById(editorPresentation.classId).get();
                        if (cd instanceof AdsEntityObjectClassDef) {
                            return (AdsEntityObjectClassDef) cd;
                        }
                    }
                }
            }
            return null;
        }

        public AdsEditorPresentationDef findEditorPresentation() {
            if (editorPresentation == null) {
                return null;
            } else {
                if (editorPresentation.presentationId == null) {
                    return null;
                } else {
                    AdsEntityObjectClassDef clazz = findClass();
                    if (clazz != null) {
                        return clazz.getPresentations().getEditorPresentations().findById(editorPresentation.presentationId, EScope.ALL).get();
                    } else {
                        return null;
                    }
                }
            }
        }

        public void setEditorPresentation(Id classId, Id presentationId) {
            if (editorPresentation == null) {
                editorPresentation = new PresentationInfo();
            }
            editorPresentation.presentationId = presentationId;
            editorPresentation.classId = classId;
        }

        public Id getEditorPresentationId() {
            if (editorPresentation == null) {
                return null;
            } else {
                return editorPresentation.presentationId;
            }
        }

        public Id getClassId() {
            if (editorPresentation == null) {
                return null;
            } else {
                return editorPresentation.classId;
            }
        }
    }

    public static class EmbeddedSelectorOpenParamsProperty extends EmbeddedWidgetOpenParams {

        public EmbeddedSelectorOpenParamsProperty(String name, EmbeddedSelectorParams xDef) {
            super(name, Id.Factory.loadFrom(xDef.getProperty()), Id.Factory.loadFrom(xDef.getExplorerItem()));
        }

        public EmbeddedSelectorOpenParamsProperty(String name) {
            super(name, null, null);
        }

        @Override
        public void appendToImpl(Property xDef) {
            EmbeddedSelectorParams params = xDef.addNewEmbeddedSelectorOpenParams();
            if (propId != null) {
                params.setProperty(propId.toString());
            } else if (explorerItemId != null) {
                params.setExplorerItem(explorerItemId.toString());
            }
        }
    }

    public static class ColorProperty extends AdsUIProperty {

        public java.awt.Color color = java.awt.Color.BLACK;

        public ColorProperty(String name, Color c) {
            super(name);
            color = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }

        @Override
        public void appendToImpl(Property xDef) {
            Color c = xDef.addNewColor();
            c.setRed(color.getRed());
            c.setGreen(color.getGreen());
            c.setBlue(color.getBlue());
            c.setAlpha(color.getAlpha());
        }
    }

    public static class BrushProperty extends AdsUIProperty {

        public java.awt.Color color = java.awt.Color.BLACK;

        public BrushProperty(String name, Color c) {
            super(name);
            color = new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        }

        @Override
        public void appendToImpl(Property xDef) {
            Color c = xDef.addNewBrush();
            c.setRed(color.getRed());
            c.setGreen(color.getGreen());
            c.setBlue(color.getBlue());
            c.setAlpha(color.getAlpha());
        }
    }

    public static class CursorProperty extends AdsUIProperty {

        public int cursorType;

        public CursorProperty(String name, int type) {
            super(name);
            this.cursorType = type;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setCursor(cursorType);
        }
    }

    public static class CommandRefProperty extends AdsUIProperty {

        private Id commandId;

        public CommandRefProperty(String name, Id commandId) {
            super(name);
            this.commandId = commandId;
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setCommandRef(commandId != null ? commandId.toString() : null);
        }

        public Id getCommandId() {
            return commandId;
        }

        public void setCommandId(Id commandId) {
            this.commandId = commandId;
            setEditState(EEditState.MODIFIED);
        }

        public AdsCommandDef findCommand() {
            if (commandId == null) {
                return null;
            }

            AdsCommandDef commandDef = null;
//            AdsClassDef clazz = findOwnerClass();
//            if (clazz != null && clazz instanceof IAdsPresentableClass) {
//                commandDef = ((IAdsPresentableClass) clazz).getPresentations().getCommands().findById(commandId, EScope.ALL);
//            }
//            if (commandDef == null) {
//                commandDef = AdsSearcher.Factory.newAdsContextlessCommandSearcher(findOwnerModule()).findById(commandId);
//            }

            final AdsPresentationDef presentation = findOwnerPresentation();
            final ContextlessCommandUsage.IContextlessCommandsUser contextlessCommandsUser = RadixObjectsUtils.findContainer(this, ContextlessCommandUsage.IContextlessCommandsUser.class);

            if (presentation != null) {
                commandDef = presentation.getCommandsLookup().findById(commandId, EScope.ALL).get();
            }

            if (commandDef == null && contextlessCommandsUser != null) {
                for (final CommandInfo commandInfo : contextlessCommandsUser.getUsedContextlessCommands().getCommandInfos()) {
                    if (Objects.equals(commandInfo.commmandId, commandId)) {
                        commandDef = commandInfo.findCommand();
                        break;
                    }
                }
            }

            if (commandDef == null && commandId.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
                commandDef = AdsSearcher.Factory.newAdsContextlessCommandSearcher(findOwnerModule()).findById(commandId).get();
            }
            return commandDef;
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsCommandDef commandDef = findCommand();
            if (commandDef != null) {
                list.add(commandDef);
            }
        }
    }

    public static final class EnumRefProperty extends AdsUIProperty {

        private Id enumId;

        public EnumRefProperty(String name, String enumId) {
            super(name);
            this.enumId = Id.Factory.loadFrom(enumId);
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setEnumRef(enumId != null ? enumId.toString() : null);
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsEnumDef enumDef = findEnum();
            if (enumDef != null) {
                list.add(enumDef);
            }
        }

        public Id getEnumId() {
            return enumId;
        }

        public void setEnumId(Id commandId) {
            this.enumId = commandId;
        }

        public AdsEnumDef findEnum() {
            if (enumId == null) {
                return null;
            }
            DefinitionSearcher<AdsEnumDef> searcher = AdsSearcher.Factory.newAdsEnumSearcher((AdsModule) this.getModule());
            AdsEnumDef enumDef = searcher.findById(enumId).get();
            return enumDef;
        }
    }

    public static class CursorShapeProperty extends AdsUIProperty {

        public ECursorShape shape;

        public CursorShapeProperty(String name, String shape) {
            super(name);
            this.shape = ECursorShape.getForValue(shape);
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setCursorShape(shape.getQualifiedValue());
        }
    }

    public static class DateProperty extends AdsUIProperty {

        public int year, month, day;

        public DateProperty(String name, Date xDate) {
            super(name);
            year = xDate.getYear();
            month = xDate.getMonth();
            day = xDate.getDay();
        }

        @Override
        public void appendToImpl(Property xDef) {
            Date xDate = xDef.addNewDate();
            xDate.setYear(year);
            xDate.setMonth(month);
            xDate.setDay(day);
        }
    }

    public static class TimeProperty extends AdsUIProperty {

        public int hour, minute, second;

        public TimeProperty(String name, Time xTime) {
            super(name);
            hour = xTime.getHour();
            minute = xTime.getMinute();
            second = xTime.getSecond();
        }

        @Override
        public void appendToImpl(Property xDef) {
            Time xTime = xDef.addNewTime();
            xTime.setHour(hour);
            xTime.setMinute(minute);
            xTime.setSecond(second);
        }
    }

    public static class DateTimeProperty extends AdsUIProperty {

        public int year, month, day, hour, minute, second;

        public DateTimeProperty(String name, DateTime xDateTime) {
            super(name);
            year = xDateTime.getYear();
            month = xDateTime.getMonth();
            day = xDateTime.getDay();
            hour = xDateTime.getHour();
            minute = xDateTime.getMinute();
            second = xDateTime.getSecond();
        }

        @Override
        public void appendToImpl(Property xDef) {
            DateTime xDateTime = xDef.addNewDateTime();
            xDateTime.setYear(year);
            xDateTime.setMonth(month);
            xDateTime.setDay(day);
            xDateTime.setHour(hour);
            xDateTime.setMinute(minute);
            xDateTime.setSecond(second);
        }
    }

    public static class FontProperty extends AdsUIProperty {

        boolean antiAliasing;
        boolean bold;
        String family;
        boolean italic;
        boolean kerning;
        int pointSize;
        boolean strikeOut;
        boolean underline;
        String styleStrategy;
        int weight;

        public FontProperty(String name, Font font) {
            super(name);
            this.antiAliasing = font.getAntialiasing();
            this.bold = font.getBold();
            this.family = font.getFamily();
            this.italic = font.getItalic();
            this.kerning = font.getKerning();
            this.pointSize = font.getPointSize();
            this.strikeOut = font.getStrikeOut();
            this.styleStrategy = font.getStyleStrategy();
            this.underline = font.getUnderline();
            this.weight = font.getWeight();
        }

        @Override
        public void appendToImpl(Property xDef) {
            Font font = xDef.addNewFont();
            font.setAntialiasing(antiAliasing);
            font.setBold(bold);
            font.setFamily(family);
            font.setItalic(italic);
            font.setKerning(kerning);
            font.setPointSize(pointSize);
            font.setStrikeOut(strikeOut);
            font.setStyleStrategy(styleStrategy);
            font.setUnderline(underline);
            font.setWeight(weight);
        }
    }

    public static class EditorPageRefProperty extends AdsUIProperty {

        private Id pageId;

        public EditorPageRefProperty(String name, String pageId) {
            super(name);
            this.pageId = Id.Factory.loadFrom(pageId);
        }

        @Override
        public void appendToImpl(Property xDef) {
            xDef.setEditorPageRef(pageId != null ? pageId.toString() : null);
        }

        public Id getEditorPageId() {
            return pageId;
        }

        public void setEditorPageId(Id pageId) {
            this.pageId = pageId;
        }

        private IEditorPagesContainer findPagesContainer() {
            for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
                if (obj instanceof IEditorPagesContainer) {
                    return (IEditorPagesContainer) obj;
                }
                if(obj instanceof IAdsFormPresentableClass){
                    return ((IAdsFormPresentableClass)obj).getPresentations();
                }
            }
            return null;
        }

        public AdsEditorPageDef findEditorPage() {
            final IEditorPagesContainer clazz = findPagesContainer();
            if (clazz == null) {
                return null;
            } else {
                return clazz.getEditorPages().findById(pageId, EScope.ALL).get();
            }
//            if (clazz != null) {
//                if (clazz instanceof AdsEntityObjectClassDef) {
//                    for (AdsEditorPresentationDef epr : ((AdsEntityObjectClassDef) clazz).getPresentations().getEditorPresentations().get(EScope.ALL)) {
//                        final AdsEditorPageDef page = epr.getEditorPages().findById(pageId, EScope.ALL);
//                        if (page != null) {
//                            return page;
//                        }
//                    }
//                    return null;
//                } else if (clazz instanceof AdsFormHandlerClassDef) {
//                    return ((AdsFormHandlerClassDef) clazz).getPresentations().getEditorPages().findById(pageId, EScope.ALL);
//                } else if (clazz instanceof AdsReportClassDef) {
//                    return ((AdsReportClassDef) clazz).getPresentations().getEditorPages().findById(pageId, EScope.ALL);
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
        }
        /*
         * public AdsEditorPageDef findEditorPage() { AdsUIDef uiDef =
         * findOwnerUiDef(); AdsClassDef clazz = null; if (uiDef instanceof
         * AdsCustomEditorDef) { clazz = ((AdsCustomEditorDef)
         * uiDef).getOwnerClass(); } else if (uiDef instanceof
         * AdsCustomFormDialogDef) { clazz = ((AdsCustomFormDialogDef)
         * uiDef).getOwnerClass(); } else if (uiDef instanceof
         * AdsCustomSelectorDef) { clazz = ((AdsCustomSelectorDef)
         * uiDef).getOwnerClass(); } if (clazz != null) { if (clazz instanceof
         * AdsEntityObjectClassDef) { for (AdsEditorPresentationDef epr :
         * ((AdsEntityObjectClassDef)
         * clazz).getPresentations().getEditorPresentations().get(EScope.ALL)) {
         * AdsEditorPageDef page = epr.getEditorPages().findById(pageId,
         * EScope.ALL); if (page != null) { return page; } } return null; } else
         * if (clazz instanceof AdsFormHandlerClassDef) { return
         * ((AdsFormHandlerClassDef)
         * clazz).getPresentations().getEditorPages().findById(pageId,
         * EScope.ALL); } else { return null; } } else { return null; } }
         */

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            AdsEditorPageDef editrPage = findEditorPage();
            if (editrPage != null) {
                list.add(editrPage);
            }
        }
    }

    public static class Factory {

        public static final AdsUIProperty loadFrom(Property xDef) {
            if (xDef.isSetBool()) {
                return new BooleanProperty(xDef.getName(), xDef.getBool());
            } else if (xDef.isSetColor()) {
                return new ColorProperty(xDef.getName(), xDef.getColor());
            } else if (xDef.isSetBrush()) {
                return new BrushProperty(xDef.getName(), xDef.getBrush());
            } else if (xDef.isSetCursor()) {
                return new CursorProperty(xDef.getName(), xDef.getCursor());
            } else if (xDef.isSetCursorShape()) {
                return new CursorShapeProperty(xDef.getName(), xDef.getCursorShape());
            } else if (xDef.isSetDate()) {
                return new DateProperty(xDef.getName(), xDef.getDate());
            } else if (xDef.isSetDateTime()) {
                return new DateTimeProperty(xDef.getName(), xDef.getDateTime());
            } else if (xDef.isSetDouble()) {
                return new DoubleProperty(xDef.getName(), xDef.getDouble());
            } else if (xDef.isSetEnum()) {
                return new EnumValueProperty(xDef.getName(), xDef.getEnum());
            } else if (xDef.isSetFloat()) {
                return new FloatProperty(xDef.getName(), xDef.getFloat());
            } else if (xDef.isSetFont()) {
                return new FontProperty(xDef.getName(), xDef.getFont());
            } else if (xDef.isSetImageRef()) {
                return new ImageProperty(xDef.getName(), xDef.getImageRef());
            } else if (xDef.isSetRect()) {
                return new RectProperty(xDef.getName(), xDef.getRect());
            } else if (xDef.isSetSet()) {
                return new SetProperty(xDef.getName(), xDef.getSet());
            } else if (xDef.isSetSize()) {
                return new SizeProperty(xDef.getName(), xDef.getSize());
            } else if (xDef.isSetSizePolicy()) {
                return new SizePolicyProperty(xDef.getName(), xDef.getSizePolicy());
            } else if (xDef.isSetString()) {
                return new StringProperty(xDef.getName(), xDef.getString());
            } else if (xDef.isSetInt()) {
                return new IntProperty(xDef.getName(), xDef.getInt());
            } else if (xDef.isSetTime()) {
                return new TimeProperty(xDef.getName(), xDef.getTime());
            } else if (xDef.isSetPropertyRef()) {
                return new PropertyRefProperty(xDef.getName(), xDef.getPropertyRef());
            } else if (xDef.isSetUrl()) {
                return new UrlProperty(xDef.getName(), xDef.getUrl().toString());
            } else if (xDef.isSetMlstringid()) {
                return new LocalizedStringRefProperty(xDef.getName(), xDef.getMlstringid());
            } else if (xDef.isSetEmbeddedEditorOpenParams()) {
                return new EmbeddedEditorOpenParamsProperty(xDef.getName(), xDef.getEmbeddedEditorOpenParams());
            } else if (xDef.isSetEmbeddedSelectorOpenParams()) {
                return new EmbeddedSelectorOpenParamsProperty(xDef.getName(), xDef.getEmbeddedSelectorOpenParams());
            } else if (xDef.isSetEditorPageRef()) {
                return new EditorPageRefProperty(xDef.getName(), xDef.getEditorPageRef());
            } else if (xDef.isSetCommandRef()) {
                return new CommandRefProperty(xDef.getName(), Id.Factory.loadFrom(xDef.getCommandRef()));
            } else if (xDef.isSetBoolValue()) {
                return new BooleanValueProperty(xDef.getName(), xDef.getBoolValue());
            } else if (xDef.isSetLong()) {
                return new LongProperty(xDef.getName(), xDef.getLong());
            } else if (xDef.isSetEnumRef()) {
                return new EnumRefProperty(xDef.getName(), xDef.getEnumRef());
            } else if (xDef.isSetAnchor()) {
                return new AnchorProperty(xDef.getName(), xDef.getAnchor());
            } else if (xDef.isSetIdList()) {
                return new IdListProperty(xDef.getName(), xDef.getIdList());
            } else if (xDef.isSetTypeDeclaration()) {
                return new TypeDeclarationProperty(xDef.getName(), AdsTypeDeclaration.Factory.loadFrom(xDef.getTypeDeclaration()));
            }

            throw new DefinitionError("Unsupported property type");
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}

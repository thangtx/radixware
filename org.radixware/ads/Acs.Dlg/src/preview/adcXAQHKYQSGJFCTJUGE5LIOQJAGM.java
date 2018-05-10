
/* Radix::Acs.Dlg::RoleArrPropEditorWeb - Web Executable*/

/*Radix::Acs.Dlg::RoleArrPropEditorWeb-Web Dynamic Class*/

package org.radixware.ads.Acs.Dlg.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleArrPropEditorWeb")
public published class RoleArrPropEditorWeb  extends org.radixware.wps.views.editor.property.PropArrEditor<org.radixware.kernel.common.types.ArrStr>  {

	public static class ValEditorFactoryImplArrStr<T extends org.radixware.kernel.common.types.Arr> extends org.radixware.wps.views.editor.property.PropArrEditor.ValEditorFactoryImpl<T> {

	    private final Explorer.Models.Properties::PropertyArr prop;

	    public ValEditorFactoryImplArrStr(final Explorer.Models.Properties::PropertyArr property) {
	        super(property);
	        this.prop = property;
	    }

	    protected org.radixware.wps.rwt.InputBox.DisplayController<T> createDisplayControllerWrapper(final org.radixware.wps.rwt.InputBox.DisplayController<T> defaultController) {
	        return new PropertyDisplayControllerArrStr<T>(defaultController, prop);
	    }

	    public class PropertyDisplayControllerArrStr<Type> implements org.radixware.wps.rwt.InputBox.DisplayController<Type> {

	        final org.radixware.wps.rwt.InputBox.DisplayController<Type> defaultDisplayController;
	        final Explorer.Models.Properties::Property property;

	        public PropertyDisplayControllerArrStr(final org.radixware.wps.rwt.InputBox.DisplayController<Type> displayController, final Explorer.Models.Properties::Property property) {
	            defaultDisplayController = displayController;
	            this.property = property;
	        }

	        @Override
	        public String getDisplayValue(Type value, boolean isFocused, boolean isReadOnly) {
	            ArrStr guids = (ArrStr) value;

	            if (guids != null && !guids.isEmpty()) {
	                return RoleEditorUtils.RoleTitleCache.getTitleByGuids(guids, getEnvironmentStatic());
	            }
	            
	            final org.radixware.kernel.common.enums.EEditMaskType maskType = property.getEditMask().getType();
	            final boolean isEditMaskCompatible = maskType == org.radixware.kernel.common.enums.EEditMaskType.BOOL || maskType == org.radixware.kernel.common.enums.EEditMaskType.LIST || maskType == org.radixware.kernel.common.enums.EEditMaskType.ENUM;
	            final org.radixware.kernel.common.enums.EValType valType = property.getType();
	            final boolean isValTypeCompatible = valType.isArrayType() || valType == org.radixware.kernel.common.enums.EValType.OBJECT || valType == org.radixware.kernel.common.enums.EValType.XML;
	            final String defaultDisplayString = defaultDisplayController.getDisplayValue(value, isFocused, isReadOnly);
	            if (isEditMaskCompatible || isValTypeCompatible || isReadOnly) {
	                return property.getOwner().getDisplayString(
	                        property.getId(), value, defaultDisplayString, !property.hasOwnValue());
	            } else {
	                return defaultDisplayString;
	            }
	        }
	    }
	}

	/*Radix::Acs.Dlg::RoleArrPropEditorWeb:Nested classes-Nested Classes*/

	/*Radix::Acs.Dlg::RoleArrPropEditorWeb:Properties-Properties*/

	/*Radix::Acs.Dlg::RoleArrPropEditorWeb:Methods-Methods*/

	/*Radix::Acs.Dlg::RoleArrPropEditorWeb:RoleArrPropEditorWeb-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleArrPropEditorWeb:RoleArrPropEditorWeb")
	public published  RoleArrPropEditorWeb (org.radixware.kernel.common.client.models.items.properties.PropertyArr<? extends org.radixware.kernel.common.types.Arr> property) {
		super(property, new ValEditorFactoryImplArrStr<ArrStr>(property));

	}


}

/* Radix::Acs.Dlg::RoleArrPropEditorWeb - Web Meta*/

/*Radix::Acs.Dlg::RoleArrPropEditorWeb-Web Dynamic Class*/

package org.radixware.ads.Acs.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleArrPropEditorWeb_mi{
}

/* Radix::Acs.Dlg::RoleArrPropEditorWeb - Localizing Bundle */
package org.radixware.ads.Acs.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleArrPropEditorWeb - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(RoleArrPropEditorWeb - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcXAQHKYQSGJFCTJUGE5LIOQJAGM"),"RoleArrPropEditorWeb - Localizing Bundle",$$$items$$$);
}

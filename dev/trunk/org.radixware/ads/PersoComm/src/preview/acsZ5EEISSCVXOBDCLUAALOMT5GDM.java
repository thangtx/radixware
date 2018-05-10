
/* Radix::PersoComm::ChannelKind - Common Executable*/

/*Radix::PersoComm::ChannelKind-Enumeration*/

package org.radixware.ads.PersoComm.common;

public enum ChannelKind implements org.radixware.kernel.common.types.IKernelStrEnum,org.radixware.kernel.common.types.ITitledEnum{
	aciL2BEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA"),"Email","Email",org.radixware.kernel.common.enums.EPersoCommChannelKind.EMAIL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3PCRRHKEVXOBDCLUAALOMT5GDM")),
	aciL6BEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL6BEJTYZVPORDJHCAANE2UAFXA"),"File","File",org.radixware.kernel.common.enums.EPersoCommChannelKind.FILE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TCRRHKEVXOBDCLUAALOMT5GDM")),
	aciLQ3IFOMEAZCUJNQW2WSFKVSL3E(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E"),"Sms","Sms",org.radixware.kernel.common.enums.EPersoCommChannelKind.SMS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N6C7IOLYZA73OC3QJXPXXFUVY")),
	aci3WL3JJLD6VHW5JZ2VK5HIMI37Q(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci3WL3JJLD6VHW5JZ2VK5HIMI37Q"),"Twitter","Twitter",org.radixware.kernel.common.enums.EPersoCommChannelKind.TWITTER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AOSLOTEKNDPBP5NHDO4RYDD4A")),
	aciMSDTUC6JNJAHBGOFJPY2KL75GI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciMSDTUC6JNJAHBGOFJPY2KL75GI"),"Ivr","Ivr",org.radixware.kernel.common.enums.EPersoCommChannelKind.IVR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7PVS35P7JGUBP5OFC7EMJMA6A")),
	aciELLRJMJQ7RD4PPDOVGVJHQQRFI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciELLRJMJQ7RD4PPDOVGVJHQQRFI"),"Mail","Mail",org.radixware.kernel.common.enums.EPersoCommChannelKind.MAIL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQHB2SBUXRAC5NNZLISYNIHA6E")),
	aci2LCUUG4UXRFOXIBCLCWDDO76R4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci2LCUUG4UXRFOXIBCLCWDDO76R4"),"GCM","Gcm",org.radixware.kernel.common.enums.EPersoCommChannelKind.GCM,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSH32ELMWNBIBGS5C74BOYQUUY")),
	aciPDEYGFEGUVC2FIIUAMMLAGC6OU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPDEYGFEGUVC2FIIUAMMLAGC6OU"),"APNS","Apns",org.radixware.kernel.common.enums.EPersoCommChannelKind.APNS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLML4AYXSY5C3XBOHMRFSTJSETQ")),
	aciFY4JJPNTENBNHOR3A5UCTXOLOU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciFY4JJPNTENBNHOR3A5UCTXOLOU"),"WNS","Wns",org.radixware.kernel.common.enums.EPersoCommChannelKind.WNS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNEGQ7CW3YNCKBBDXAIJOINUHCE")),
	aciTUI7OXAVUFBVZHUDZUHA26BAMM(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTUI7OXAVUFBVZHUDZUHA26BAMM"),"DeliveryAck","DeliveryAck",org.radixware.kernel.common.enums.EPersoCommChannelKind.DELIVERY_ACK,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2WIKBRHCVBP7MGFEBVGEXIRGM"));

	private final org.radixware.kernel.common.types.Id id;
	private final String name;
	private final org.radixware.kernel.common.types.Id titleId;
	private Str val;






	private ChannelKind(org.radixware.kernel.common.types.Id id, String name,
	Str value,org.radixware.kernel.common.enums.EPersoCommChannelKind publishedItem,org.radixware.kernel.common.types.Id titleId){
		this.id = id;
		this.name = name;
		this.titleId = titleId;
		this.publishedItem = publishedItem;
		this.val = value;
	}
	public String getName(){return name;}
	public Str getValue(){return val;}
	public boolean isInDomain(org.radixware.kernel.common.types.Id domainId){return getItemMeta(id).getIsInDomain($env_instance_storage_for_internal_usage$,domainId);}
	public boolean isInDomains( java.util.List<org.radixware.kernel.common.types.Id>ids){return getItemMeta(id).getIsInDomains($env_instance_storage_for_internal_usage$,ids);}
	/*Use {@linkplain #getValue()} instead*/
	@Deprecated
	public Str get(){return getValue();}
	public String getTitle(){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsZ5EEISSCVXOBDCLUAALOMT5GDM.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsZ5EEISSCVXOBDCLUAALOMT5GDM"),titleId);
	}
	public String getTitle(org.radixware.kernel.common.enums.EIsoLanguage lang){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsZ5EEISSCVXOBDCLUAALOMT5GDM.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsZ5EEISSCVXOBDCLUAALOMT5GDM"),titleId,lang);
	}
	private final org.radixware.kernel.common.enums.EPersoCommChannelKind publishedItem;
	public static final org.radixware.kernel.common.enums.EPersoCommChannelKind downcast(acsZ5EEISSCVXOBDCLUAALOMT5GDM item){
	return item.publishedItem;
	}
	public static final acsZ5EEISSCVXOBDCLUAALOMT5GDM valueOf(org.radixware.kernel.common.enums.EPersoCommChannelKind pitem){
		for(acsZ5EEISSCVXOBDCLUAALOMT5GDM item : values()){
			if(item.publishedItem==pitem)return item;
		}
		return null;
	}
	private org.radixware.kernel.common.meta.RadEnumDef.Item getItemMeta(org.radixware.kernel.common.types.Id id){
		org.radixware.kernel.common.meta.RadEnumDef.Item im = (org.radixware.kernel.common.meta.RadEnumDef.Item)org.radixware.ads.PersoComm.common.ChannelKind_mi.rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);
		if(im == null)
			throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
		else
			return im;
		}

	public static final ChannelKind getForValue(final Str val) {
			for (ChannelKind t : ChannelKind.values()) {
				if (t.getValue() == null && val == null || t.getValue().equals(val)) {
				return t;}
		}
		throw new org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError("Radix::PersoComm::ChannelKind has no item with value: " + String.valueOf(val),val);}
	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.PersoComm.common.ChannelKind_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.ads.PersoComm.common.ChannelKind>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.ads.PersoComm.common.ChannelKind> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.ads.PersoComm.common.ChannelKind item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.ads.PersoComm.common.ChannelKind[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.ads.PersoComm.common.ChannelKind.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::PersoComm::ChannelKind - Common Meta*/

/*Radix::PersoComm::ChannelKind-Enumeration*/

package org.radixware.ads.PersoComm.common;
public class ChannelKind_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::PersoComm::ChannelKind:Email-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL2BEJTYZVPORDJHCAANE2UAFXA"),"Email",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Email"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3PCRRHKEVXOBDCLUAALOMT5GDM"),null,0,null,false,org.radixware.ads.PersoComm.common.ChannelKind.Email),

						/*Radix::PersoComm::ChannelKind:File-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciL6BEJTYZVPORDJHCAANE2UAFXA"),"File",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("File"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TCRRHKEVXOBDCLUAALOMT5GDM"),null,1,null,false,org.radixware.ads.PersoComm.common.ChannelKind.File),

						/*Radix::PersoComm::ChannelKind:Sms-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLQ3IFOMEAZCUJNQW2WSFKVSL3E"),"Sms",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Sms"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N6C7IOLYZA73OC3QJXPXXFUVY"),null,2,null,false,org.radixware.ads.PersoComm.common.ChannelKind.Sms),

						/*Radix::PersoComm::ChannelKind:Twitter-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci3WL3JJLD6VHW5JZ2VK5HIMI37Q"),"Twitter",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Twitter"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AOSLOTEKNDPBP5NHDO4RYDD4A"),null,3,null,false,org.radixware.ads.PersoComm.common.ChannelKind.Twitter),

						/*Radix::PersoComm::ChannelKind:Ivr-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciMSDTUC6JNJAHBGOFJPY2KL75GI"),"Ivr",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Ivr"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7PVS35P7JGUBP5OFC7EMJMA6A"),null,4,null,false,org.radixware.ads.PersoComm.common.ChannelKind.Ivr),

						/*Radix::PersoComm::ChannelKind:Mail-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciELLRJMJQ7RD4PPDOVGVJHQQRFI"),"Mail",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Mail"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQHB2SBUXRAC5NNZLISYNIHA6E"),null,5,null,false,org.radixware.ads.PersoComm.common.ChannelKind.Mail),

						/*Radix::PersoComm::ChannelKind:GCM-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci2LCUUG4UXRFOXIBCLCWDDO76R4"),"GCM",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Gcm"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSH32ELMWNBIBGS5C74BOYQUUY"),null,6,null,false,org.radixware.ads.PersoComm.common.ChannelKind.GCM),

						/*Radix::PersoComm::ChannelKind:APNS-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPDEYGFEGUVC2FIIUAMMLAGC6OU"),"APNS",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Apns"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLML4AYXSY5C3XBOHMRFSTJSETQ"),null,7,null,false,org.radixware.ads.PersoComm.common.ChannelKind.APNS),

						/*Radix::PersoComm::ChannelKind:WNS-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciFY4JJPNTENBNHOR3A5UCTXOLOU"),"WNS",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Wns"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNEGQ7CW3YNCKBBDXAIJOINUHCE"),null,8,null,false,org.radixware.ads.PersoComm.common.ChannelKind.WNS),

						/*Radix::PersoComm::ChannelKind:DeliveryAck-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTUI7OXAVUFBVZHUDZUHA26BAMM"),"DeliveryAck",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("DeliveryAck"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2WIKBRHCVBP7MGFEBVGEXIRGM"),null,9,null,false,org.radixware.ads.PersoComm.common.ChannelKind.DeliveryAck)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsZ5EEISSCVXOBDCLUAALOMT5GDM"),
				"ChannelKind",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::PersoComm::ChannelKind - Localizing Bundle */
package org.radixware.ads.PersoComm.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ChannelKind - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Twitter");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Twitter");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AOSLOTEKNDPBP5NHDO4RYDD4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Email");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3PCRRHKEVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3TCRRHKEVXOBDCLUAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SMS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"SMS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4N6C7IOLYZA73OC3QJXPXXFUVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Mail");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Mail");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQHB2SBUXRAC5NNZLISYNIHA6E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Apple Push Notification Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Apple Push Notification Service");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLML4AYXSY5C3XBOHMRFSTJSETQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Windows Push Notification Services");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Windows Push Notification Services");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNEGQ7CW3YNCKBBDXAIJOINUHCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type of the personal communication channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип канала персональной коммуникации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPDIGUP6LDJCCPAC5U6ZKA3QXPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delivery Acknowledgment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Delivery Acknowledgment");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW2WIKBRHCVBP7MGFEBVGEXIRGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"IVR");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"IVR");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX7PVS35P7JGUBP5OFC7EMJMA6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Google Cloud Messaging");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Google Cloud Messaging");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXSH32ELMWNBIBGS5C74BOYQUUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ChannelKind - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsZ5EEISSCVXOBDCLUAALOMT5GDM"),"ChannelKind - Localizing Bundle",$$$items$$$);
}


/* Radix::Arte::EventSource - Common Executable*/

/*Radix::Arte::EventSource-Enumeration*/

package org.radixware.ads.Arte.common;

public enum EventSource implements org.radixware.kernel.common.types.IKernelStrEnum,org.radixware.kernel.common.types.ITitledEnum{
	aciSONQPFPV2RGHXDKGFXGBWUQNWA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciSONQPFPV2RGHXDKGFXGBWUQNWA"),"MqProducer","App.MqProducer",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2C5W26CXO5GHTB6ILC6CKPP7II")),
	aciCPKKO6PCCFBO3KYIZPGYQ4M6CM(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCPKKO6PCCFBO3KYIZPGYQ4M6CM"),"License","App.License",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJR6UK7DWZNBDPJSHHCXL5EKALQ")),
	aci6GKQ2BYOTRFG7D7DJ5SYZSLMSE(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6GKQ2BYOTRFG7D7DJ5SYZSLMSE"),"ServiceBus","App.ServiceBus",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDEFTPVRARADBE3C2BULDQ43R4")),
	aci6SAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6SAUJTYZVPORDJHCAANE2UAFXA"),"AasClient","Server.AasClient",org.radixware.kernel.common.enums.EEventSource.AAS_CLIENT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMH6WLKFG2DOBDEZ6AAMPGXSZKU")),
	aciN2VHAOVOQZDNVHXKVMQMGRENZA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciN2VHAOVOQZDNVHXKVMQMGRENZA"),"Task","App.Task",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUSKNGZ7JBDDMPZFEIBPLYFE4")),
	aci6OAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6OAUJTYZVPORDJHCAANE2UAFXA"),"ArteUnit","Server.Unit.Arte",org.radixware.kernel.common.enums.EEventSource.ARTE_UNIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHQX466IPPOBDCUSAAN7YHKUNI")),
	aciBGBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBGBEJTYZVPORDJHCAANE2UAFXA"),"InstanceService","Server.Instance.Service",org.radixware.kernel.common.enums.EEventSource.INSTANCE_SERVICE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci7CAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7CAUJTYZVPORDJHCAANE2UAFXA"),"ArteCommunicator","Arte.Communicator",org.radixware.kernel.common.enums.EEventSource.ARTE_COMMUNICATOR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciBOBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBOBEJTYZVPORDJHCAANE2UAFXA"),"Unit","Server.Unit",org.radixware.kernel.common.enums.EEventSource.UNIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci72AUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci72AUJTYZVPORDJHCAANE2UAFXA"),"NetPortHandler","Server.Unit.NetPortHandler",org.radixware.kernel.common.enums.EEventSource.NET_PORT_HANDLER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciB2BEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciB2BEJTYZVPORDJHCAANE2UAFXA"),"Aas","Arte.Aas",org.radixware.kernel.common.enums.EEventSource.AAS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciPIBLCGW5JZD3FBY4L3V5ZMETUQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPIBLCGW5JZD3FBY4L3V5ZMETUQ"),"Upgrade","App.Upgrade",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMKZ4OZW55CQZKUQ3HNRLP56LE")),
	aciHCNUIO6QGRAM5FRWH3Q63GGW2I(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciHCNUIO6QGRAM5FRWH3Q63GGW2I"),"Server","Server",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZEYYCMWDFGZHGZJZVSGBDAR6I")),
	aciCFUQ37CISJBKXJDFGRS6QDCUTQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCFUQ37CISJBKXJDFGRS6QDCUTQ"),"AppCfgPackage","App.CfgPackage",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGUDMSBN3Y5BPRFYXODNFBAUAUQ")),
	aci76AUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci76AUJTYZVPORDJHCAANE2UAFXA"),"JobQueue","Arte.JobQueue",org.radixware.kernel.common.enums.EEventSource.JOB_QUEUE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciDTHY35JS7NBT3K4JJDONYAW77Y(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciDTHY35JS7NBT3K4JJDONYAW77Y"),"JobExecutor","Server.Unit.JobExecutor",org.radixware.kernel.common.enums.EEventSource.JOB_EXECUTOR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJVKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciAFISM25PDBA6VMFI5ETD2SS4CU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAFISM25PDBA6VMFI5ETD2SS4CU"),"Instance","Server.Instance",org.radixware.kernel.common.enums.EEventSource.INSTANCE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci5EGXKNLPZVCN5NRGBAN43RQFNA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5EGXKNLPZVCN5NRGBAN43RQFNA"),"AppPersoComm","App.PersoComm",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPI3MOQSBLVDURA2URFGYBUQOIM")),
	aci7KAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7KAUJTYZVPORDJHCAANE2UAFXA"),"Entity","Arte.Entity",org.radixware.kernel.common.enums.EEventSource.ENTITY,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci62AUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci62AUJTYZVPORDJHCAANE2UAFXA"),"DefManager","Arte.DefManager",org.radixware.kernel.common.enums.EEventSource.DEF_MANAGER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci7GAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7GAUJTYZVPORDJHCAANE2UAFXA"),"DbQueryBuilder","Arte.Dbqb",org.radixware.kernel.common.enums.EEventSource.DB_QUERY_BUILDER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKVKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciAOBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAOBEJTYZVPORDJHCAANE2UAFXA"),"ClientSession","Client.Session",org.radixware.kernel.common.enums.EEventSource.CLIENT_SESSION,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciAKBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAKBEJTYZVPORDJHCAANE2UAFXA"),"Client","Client",org.radixware.kernel.common.enums.EEventSource.CLIENT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciBCBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBCBEJTYZVPORDJHCAANE2UAFXA"),"ArteDb","Arte.Db",org.radixware.kernel.common.enums.EEventSource.ARTE_DB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci6WAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6WAUJTYZVPORDJHCAANE2UAFXA"),"Arte","Arte",org.radixware.kernel.common.enums.EEventSource.ARTE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKFKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciA6BEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciA6BEJTYZVPORDJHCAANE2UAFXA"),"AppDb","App.Db",org.radixware.kernel.common.enums.EEventSource.APP_DB,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci7WAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7WAUJTYZVPORDJHCAANE2UAFXA"),"AppClass","App.Class",org.radixware.kernel.common.enums.EEventSource.APP_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci7SAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7SAUJTYZVPORDJHCAANE2UAFXA"),"App","App",org.radixware.kernel.common.enums.EEventSource.APP,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci7OAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7OAUJTYZVPORDJHCAANE2UAFXA"),"AlgoExecutor","Arte.AlgorithmExecutor",org.radixware.kernel.common.enums.EEventSource.ALGO_EXECUTOR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5KFKRK4OXOBDFKUAAMPGXUWTQ")),
	aciINPXZJH7XRGKFDAU2UBDJLYBVI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciINPXZJH7XRGKFDAU2UBDJLYBVI"),"AppSysEventNotify","App.SysEventNotify",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMV2YUXM4V7OBDNVPAAMPGXSZKU")),
	aciBDR6OQMDWJHFRBABRQBE26DNYU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBDR6OQMDWJHFRBABRQBE26DNYU"),"TestCase","App.TestCase",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJKFKRK4OXOBDFKUAAMPGXUWTQ")),
	aci5WZEMWLZPBBKRIFNG6NBQVOOBM(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5WZEMWLZPBBKRIFNG6NBQVOOBM"),"MqProcessor","App.MqProcessor",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNTYUWOUTZHWTJI2ARJO57OTFY")),
	aci72EL5SVYFJFK7IJ7P2N723NJ44(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci72EL5SVYFJFK7IJ7P2N723NJ44"),"ExplorerApp","Client.Explorer.App",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETREIVZDTHORDGZJABIFNQAABA")),
	aciVHTSMNFUNNBTJFRXGWTPCZA2FI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciVHTSMNFUNNBTJFRXGWTPCZA2FI"),"UserFunc","App.UserFunc",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLUJR4PB4UJHIHGLNXOPG76RFRE")),
	aci2DKEMN7EXZGPVP4XROUJ6ZOCPE(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci2DKEMN7EXZGPVP4XROUJ6ZOCPE"),"Todo","App.Todo",org.radixware.kernel.common.enums.EEventSource.TODO,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TRVQFA325DFFMMGDI4PBK4B3I")),
	aciC5TVCZNJYNE2JCHXN3NOIAS23I(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciC5TVCZNJYNE2JCHXN3NOIAS23I"),"Workflow","App.Workflow",org.radixware.kernel.common.enums.EEventSource.WORKFLOW,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOQRND3U6PFF4FKNV4ZLTRFO53M")),
	aciWXJXSR6MNNCX7LRIYI3X7TV2AI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciWXJXSR6MNNCX7LRIYI3X7TV2AI"),"PersoCommUnit","Server.Unit.PersoComm",org.radixware.kernel.common.enums.EEventSource.PERSOCOMM_UNIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDK5AOXXD5DFZAJWOMYBYPSHMM")),
	aci476M3WW3XNGJFFLCDS5TIN3KI4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci476M3WW3XNGJFFLCDS5TIN3KI4"),"ExplorerDefManager","Client.Explorer.DefManager",org.radixware.kernel.common.enums.EEventSource.CLIENT_DEF_MANAGER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG7L7LZTG3ZH3HHFNPREZRXU62E")),
	aci6SM7K5JS5JBERB7BHS7UJIEPKQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6SM7K5JS5JBERB7BHS7UJIEPKQ"),"Explorer","Client.Explorer",org.radixware.kernel.common.enums.EEventSource.EXPLORER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNOAB7GMENB7JORIN7ACTA2USY")),
	aciE4J4DRLDS5CRBMJ5CX6AMDOKPQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciE4J4DRLDS5CRBMJ5CX6AMDOKPQ"),"Eas","Arte.Eas",org.radixware.kernel.common.enums.EEventSource.EAS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIN2XVN7MXJBOBC6L2NMSLCGB4E")),
	aciA5UFXOQYZVGD3HR3MME5ZDOC5U(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciA5UFXOQYZVGD3HR3MME5ZDOC5U"),"ArteReports","Arte.Reports",org.radixware.kernel.common.enums.EEventSource.ARTE_REPORTS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKZ5TWIGOJDIDEVUVAAVAJTU6M")),
	aciF4ZMNBLP6JER7DPCAVRMJYTUCQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciF4ZMNBLP6JER7DPCAVRMJYTUCQ"),"APP_AUDIT","App.Audit",org.radixware.kernel.common.enums.EEventSource.APP_AUDIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4K3TBJHTVHLPBCOCOTIMASGP4")),
	aci6IWH4AIYYVHPLMR6Z536K2CXGQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6IWH4AIYYVHPLMR6Z536K2CXGQ"),"ARTE_PROFILER","Arte.Profiler",org.radixware.kernel.common.enums.EEventSource.ARTE_PROFILER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKH3PCQYXZB7ZHFYDENOLYXEWE")),
	aciNPTF7TX6NFEYPEKVGNABOPZLII(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciNPTF7TX6NFEYPEKVGNABOPZLII"),"ARTE_POOL","Arte.Pool",org.radixware.kernel.common.enums.EEventSource.ARTE_POOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MKXQAYR7FFFVK3RO54G2PBX4E")),
	aci3C35HCUWOVHZHBCM5ZUMXW343I(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci3C35HCUWOVHZHBCM5ZUMXW343I"),"NetHubHandler","Server.Unit.NetHubHandler",org.radixware.kernel.common.enums.EEventSource.NET_HUB_HANDLER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREEUEGL6NJEDDP3OJUWBXTMHF4")),
	aci4VSRGAWJBFEH3IAVZIWZJQVMTI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci4VSRGAWJBFEH3IAVZIWZJQVMTI"),"SystemMonitoring","App.SystemMonitoring",org.radixware.kernel.common.enums.EEventSource.SYSTEM_MONITORING,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSFVHVCIP7BEQLM2BZNSQIFON7A")),
	aciCZ46CUNDRNEEPGKRWARIHH7NAI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCZ46CUNDRNEEPGKRWARIHH7NAI"),"JmsHandler","Server.Unit.JmsHandler",org.radixware.kernel.common.enums.EEventSource.JMS_HANDLER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFXKCTRRJFEYJHJB24VVSCHFIY")),
	aciTWSHJ6RA4VCRXBFFQNQKLMTUBU(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTWSHJ6RA4VCRXBFFQNQKLMTUBU"),"SnmpAgent","Server.Unit.SnmpAgent",org.radixware.kernel.common.enums.EEventSource.SNMP_AGENT_UNIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWAWTEXEUNEU5A6PLMW3ZSSVMI")),
	aciRPPNN7ZBFJCMJFDGGEF3JFM2X4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciRPPNN7ZBFJCMJFDGGEF3JFM2X4"),"IAD","Client.Explorer.IAD",org.radixware.kernel.common.enums.EEventSource.IAD,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LN356QPKREGTNHGTEJKMKRHW4")),
	aciORVH767MWVAX7KUPYFZLLFKAGA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciORVH767MWVAX7KUPYFZLLFKAGA"),"ARTE_TRACE","Arte.Trace",org.radixware.kernel.common.enums.EEventSource.ARTE_TRACE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTTHBNO4CNFIXFQLGGR6BEYSUA")),
	aci7SDSE7MFJRBNJOQS77VGBYNLGM(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7SDSE7MFJRBNJOQS77VGBYNLGM"),"MqHandlerUnit","Server.Unit.MqHandler",org.radixware.kernel.common.enums.EEventSource.MQ_HANDLER_UNIT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWKPZEVZEBETHDGYBQ5J5BSUXU")),
	aciG4HYHXQKXFFKVMRYG5WOYWVD6I(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciG4HYHXQKXFFKVMRYG5WOYWVD6I"),"Server.ChannelPort","Server.ChannelPort",org.radixware.kernel.common.enums.EEventSource.SERVER_CHANNEL_PORT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3TRU7WILVCGTA6V6N7S5YUBA4")),
	aci6GAUJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6GAUJTYZVPORDJHCAANE2UAFXA"),"NetPortHandlerPort","Server.Unit.NetPortHandler.Port",org.radixware.kernel.common.enums.EEventSource.UNIT_PORT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOJ6IRIPHFG4XE6GQ5GV4QN6EQ")),
	aciBSBEJTYZVPORDJHCAANE2UAFXA(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBSBEJTYZVPORDJHCAANE2UAFXA"),"JobScheduler","Server.Unit.JobScheduler",org.radixware.kernel.common.enums.EEventSource.JOB_SCHEDULER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCGKV72XT5ENZJXZYAMWKRI7ZE")),
	aciBNDXSOJD2NH65HJI224DJANWT4(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBNDXSOJD2NH65HJI224DJANWT4"),"NetPortHandlerService","Server.Unit.NetPortHandler.Service",org.radixware.kernel.common.enums.EEventSource.NET_PORT_HANDLER_SERVICE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPQULGD5RNCTVDCPKBULU5MOR4")),
	aciLB3UEQY3QFDT7N5HD7RDRNXPME(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLB3UEQY3QFDT7N5HD7RDRNXPME"),"NetPortHandlerQueue","Server.Unit.NetPortHandler.Queue",org.radixware.kernel.common.enums.EEventSource.NET_PORT_HANDLER_QUEUE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4NGYK7BPJAZ7DO575ATTO452I")),
	aciZXFR5Y5GEBCDDJQIVTPHSNW5YQ(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciZXFR5Y5GEBCDDJQIVTPHSNW5YQ"),"WebDriver","Client.Explorer.WebDriver",org.radixware.kernel.common.enums.EEventSource.WEB_DRIVER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAS2VZTXMVDVXG77R3KR2UUGBA")),
	aciTRQPBF2UC5FADK7PBRIY472CGI(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTRQPBF2UC5FADK7PBRIY472CGI"),"ARTE_ACTIVITY","Arte.Activity",org.radixware.kernel.common.enums.EEventSource.ARTE_ACTIVITY,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVYZMPNP5RBIJFCDOHGYHR7774")),
	aci5LVANENNZFD6TMJ7PKGKQBI7BE(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5LVANENNZFD6TMJ7PKGKQBI7BE"),"NotificationSender","Arte.NotificationSender",org.radixware.kernel.common.enums.EEventSource.NOTIFICATION_SENDER,org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2NDSJEIQLVA6BL5NW46V6GOFBM"));

	private final org.radixware.kernel.common.types.Id id;
	private final String name;
	private final org.radixware.kernel.common.types.Id titleId;
	private Str val;






	private EventSource(org.radixware.kernel.common.types.Id id, String name,
	Str value,org.radixware.kernel.common.enums.EEventSource publishedItem,org.radixware.kernel.common.types.Id titleId){
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
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsTNLJBZADHTNRDIPGABQAQNO6EY.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsTNLJBZADHTNRDIPGABQAQNO6EY"),titleId);
	}
	public String getTitle(org.radixware.kernel.common.enums.EIsoLanguage lang){
		return titleId==null?null:org.radixware.kernel.common.environment.IMlStringBundle.Lookup.getValue(acsTNLJBZADHTNRDIPGABQAQNO6EY.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsTNLJBZADHTNRDIPGABQAQNO6EY"),titleId,lang);
	}
	private final org.radixware.kernel.common.enums.EEventSource publishedItem;
	public static final org.radixware.kernel.common.enums.EEventSource downcast(acsTNLJBZADHTNRDIPGABQAQNO6EY item){
	return item.publishedItem;
	}
	public static final acsTNLJBZADHTNRDIPGABQAQNO6EY valueOf(org.radixware.kernel.common.enums.EEventSource pitem){
		for(acsTNLJBZADHTNRDIPGABQAQNO6EY item : values()){
			if(item.publishedItem==pitem)return item;
		}
		return null;
	}
	private org.radixware.kernel.common.meta.RadEnumDef.Item getItemMeta(org.radixware.kernel.common.types.Id id){
		org.radixware.kernel.common.meta.RadEnumDef.Item im = (org.radixware.kernel.common.meta.RadEnumDef.Item)org.radixware.ads.Arte.common.EventSource_mi.rdxMeta.getItems().findItemById(id,org.radixware.kernel.common.defs.ExtendableDefinitions.EScope.ALL);
		if(im == null)
			throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(id);
		else
			return im;
		}

	public static final EventSource getForValue(final Str val) {
			for (EventSource t : EventSource.values()) {
				if (t.getValue() == null && val == null || t.getValue().equals(val)) {
				return t;}
		}
		throw new org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError("Radix::Arte::EventSource has no item with value: " + String.valueOf(val),val);}
	public static final org.radixware.kernel.common.meta.RadEnumDef getRadMeta(){ return org.radixware.ads.Arte.common.EventSource_mi.rdxMeta;}
	@Deprecated
	public static final org.radixware.kernel.common.meta.RadEnumDef meta = getRadMeta();

		public static class Arr extends org.radixware.kernel.common.types.Arr<org.radixware.ads.Arte.common.EventSource>{
			public static final long serialVersionUID = 0L;
			public Arr(){super();}
			public Arr(java.util.Collection<org.radixware.ads.Arte.common.EventSource> collection){
				if(collection==null)throw new NullPointerException();
				for(org.radixware.ads.Arte.common.EventSource item : collection){
					add(item);
				}
			}
			public Arr(org.radixware.ads.Arte.common.EventSource[] array){
				if(array==null)throw new NullPointerException();
				for(int i = 0; i < array.length; i ++){
					add(array[i]);
				}
			}
			public Arr(org.radixware.kernel.common.types.ArrStr array){
				if(array == null) throw new NullPointerException();
				for(Str item : array){
					add(item==null?null:org.radixware.ads.Arte.common.EventSource.getForValue(item));
				}
			}
			public org.radixware.kernel.common.enums.EValType getItemValType(){ 
				return org.radixware.kernel.common.enums.EValType.STR;
			}
		}
}

/* Radix::Arte::EventSource - Common Meta*/

/*Radix::Arte::EventSource-Enumeration*/

package org.radixware.ads.Arte.common;
public class EventSource_mi{






	public static final org.radixware.kernel.common.meta.RadEnumDef rdxMeta;
	static{ 
	@SuppressWarnings("deprecation")
	org.radixware.kernel.common.meta.RadEnumDef.Item[] item_meta_arr = 
				new org.radixware.kernel.common.meta.RadEnumDef.Item[]{

						/*Radix::Arte::EventSource:MqProducer-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciSONQPFPV2RGHXDKGFXGBWUQNWA"),"MqProducer",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.MqProducer"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2C5W26CXO5GHTB6ILC6CKPP7II"),null,6,null,false,org.radixware.ads.Arte.common.EventSource.MqProducer),

						/*Radix::Arte::EventSource:License-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCPKKO6PCCFBO3KYIZPGYQ4M6CM"),"License",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.License"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJR6UK7DWZNBDPJSHHCXL5EKALQ"),null,10,null,false,org.radixware.ads.Arte.common.EventSource.License),

						/*Radix::Arte::EventSource:ServiceBus-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6GKQ2BYOTRFG7D7DJ5SYZSLMSE"),"ServiceBus",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.ServiceBus"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDEFTPVRARADBE3C2BULDQ43R4"),null,2,null,false,org.radixware.ads.Arte.common.EventSource.ServiceBus),

						/*Radix::Arte::EventSource:AasClient-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6SAUJTYZVPORDJHCAANE2UAFXA"),"AasClient",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.AasClient"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMH6WLKFG2DOBDEZ6AAMPGXSZKU"),null,19,null,false,org.radixware.ads.Arte.common.EventSource.AasClient),

						/*Radix::Arte::EventSource:Task-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciN2VHAOVOQZDNVHXKVMQMGRENZA"),"Task",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Task"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUSKNGZ7JBDDMPZFEIBPLYFE4"),null,3,null,false,org.radixware.ads.Arte.common.EventSource.Task),

						/*Radix::Arte::EventSource:ArteUnit-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6OAUJTYZVPORDJHCAANE2UAFXA"),"ArteUnit",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.Arte"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHQX466IPPOBDCUSAAN7YHKUNI"),null,24,null,false,org.radixware.ads.Arte.common.EventSource.ArteUnit),

						/*Radix::Arte::EventSource:InstanceService-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBGBEJTYZVPORDJHCAANE2UAFXA"),"InstanceService",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Instance.Service"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRKFKRK4OXOBDFKUAAMPGXUWTQ"),null,21,null,false,org.radixware.ads.Arte.common.EventSource.InstanceService),

						/*Radix::Arte::EventSource:ArteCommunicator-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7CAUJTYZVPORDJHCAANE2UAFXA"),"ArteCommunicator",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Communicator"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKFKRK4OXOBDFKUAAMPGXUWTQ"),null,36,null,false,org.radixware.ads.Arte.common.EventSource.ArteCommunicator),

						/*Radix::Arte::EventSource:Unit-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBOBEJTYZVPORDJHCAANE2UAFXA"),"Unit",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVKFKRK4OXOBDFKUAAMPGXUWTQ"),null,22,null,false,org.radixware.ads.Arte.common.EventSource.Unit),

						/*Radix::Arte::EventSource:NetPortHandler-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci72AUJTYZVPORDJHCAANE2UAFXA"),"NetPortHandler",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.NetPortHandler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJKFKRK4OXOBDFKUAAMPGXUWTQ"),null,28,null,false,org.radixware.ads.Arte.common.EventSource.NetPortHandler),

						/*Radix::Arte::EventSource:Aas-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciB2BEJTYZVPORDJHCAANE2UAFXA"),"Aas",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Aas"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZKFKRK4OXOBDFKUAAMPGXUWTQ"),null,38,null,false,org.radixware.ads.Arte.common.EventSource.Aas),

						/*Radix::Arte::EventSource:Upgrade-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPIBLCGW5JZD3FBY4L3V5ZMETUQ"),"Upgrade",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Upgrade"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMKZ4OZW55CQZKUQ3HNRLP56LE"),null,11,null,false,org.radixware.ads.Arte.common.EventSource.Upgrade),

						/*Radix::Arte::EventSource:Server-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciHCNUIO6QGRAM5FRWH3Q63GGW2I"),"Server",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZEYYCMWDFGZHGZJZVSGBDAR6I"),null,18,null,false,org.radixware.ads.Arte.common.EventSource.Server),

						/*Radix::Arte::EventSource:AppCfgPackage-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCFUQ37CISJBKXJDFGRS6QDCUTQ"),"AppCfgPackage",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.CfgPackage"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGUDMSBN3Y5BPRFYXODNFBAUAUQ"),null,4,null,false,org.radixware.ads.Arte.common.EventSource.AppCfgPackage),

						/*Radix::Arte::EventSource:JobQueue-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci76AUJTYZVPORDJHCAANE2UAFXA"),"JobQueue",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.JobQueue"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNKFKRK4OXOBDFKUAAMPGXUWTQ"),null,39,null,false,org.radixware.ads.Arte.common.EventSource.JobQueue),

						/*Radix::Arte::EventSource:JobExecutor-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciDTHY35JS7NBT3K4JJDONYAW77Y"),"JobExecutor",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.JobExecutor"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJVKFKRK4OXOBDFKUAAMPGXUWTQ"),null,32,null,false,org.radixware.ads.Arte.common.EventSource.JobExecutor),

						/*Radix::Arte::EventSource:Instance-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAFISM25PDBA6VMFI5ETD2SS4CU"),"Instance",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Instance"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRKFKRK4OXOBDFKUAAMPGXUWTQ"),null,20,null,false,org.radixware.ads.Arte.common.EventSource.Instance),

						/*Radix::Arte::EventSource:AppPersoComm-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5EGXKNLPZVCN5NRGBAN43RQFNA"),"AppPersoComm",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.PersoComm"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPI3MOQSBLVDURA2URFGYBUQOIM"),null,17,null,false,org.radixware.ads.Arte.common.EventSource.AppPersoComm),

						/*Radix::Arte::EventSource:Entity-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7KAUJTYZVPORDJHCAANE2UAFXA"),"Entity",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Entity"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZKFKRK4OXOBDFKUAAMPGXUWTQ"),null,40,null,false,org.radixware.ads.Arte.common.EventSource.Entity),

						/*Radix::Arte::EventSource:DefManager-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci62AUJTYZVPORDJHCAANE2UAFXA"),"DefManager",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.DefManager"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJKFKRK4OXOBDFKUAAMPGXUWTQ"),null,41,null,false,org.radixware.ads.Arte.common.EventSource.DefManager),

						/*Radix::Arte::EventSource:DbQueryBuilder-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7GAUJTYZVPORDJHCAANE2UAFXA"),"DbQueryBuilder",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Dbqb"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKVKFKRK4OXOBDFKUAAMPGXUWTQ"),null,42,null,false,org.radixware.ads.Arte.common.EventSource.DbQueryBuilder),

						/*Radix::Arte::EventSource:ClientSession-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAOBEJTYZVPORDJHCAANE2UAFXA"),"ClientSession",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Session"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZKFKRK4OXOBDFKUAAMPGXUWTQ"),null,54,null,false,org.radixware.ads.Arte.common.EventSource.ClientSession),

						/*Radix::Arte::EventSource:Client-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciAKBEJTYZVPORDJHCAANE2UAFXA"),"Client",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVKFKRK4OXOBDFKUAAMPGXUWTQ"),null,49,null,false,org.radixware.ads.Arte.common.EventSource.Client),

						/*Radix::Arte::EventSource:ArteDb-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBCBEJTYZVPORDJHCAANE2UAFXA"),"ArteDb",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Db"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNKFKRK4OXOBDFKUAAMPGXUWTQ"),null,45,null,false,org.radixware.ads.Arte.common.EventSource.ArteDb),

						/*Radix::Arte::EventSource:Arte-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6WAUJTYZVPORDJHCAANE2UAFXA"),"Arte",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKFKFKRK4OXOBDFKUAAMPGXUWTQ"),null,35,null,false,org.radixware.ads.Arte.common.EventSource.Arte),

						/*Radix::Arte::EventSource:AppDb-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciA6BEJTYZVPORDJHCAANE2UAFXA"),"AppDb",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Db"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJKFKRK4OXOBDFKUAAMPGXUWTQ"),null,15,null,false,org.radixware.ads.Arte.common.EventSource.AppDb),

						/*Radix::Arte::EventSource:AppClass-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7WAUJTYZVPORDJHCAANE2UAFXA"),"AppClass",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Class"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFKFKRK4OXOBDFKUAAMPGXUWTQ"),null,1,null,false,org.radixware.ads.Arte.common.EventSource.AppClass),

						/*Radix::Arte::EventSource:App-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7SAUJTYZVPORDJHCAANE2UAFXA"),"App",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBKFKRK4OXOBDFKUAAMPGXUWTQ"),null,0,null,false,org.radixware.ads.Arte.common.EventSource.App),

						/*Radix::Arte::EventSource:AlgoExecutor-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7OAUJTYZVPORDJHCAANE2UAFXA"),"AlgoExecutor",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.AlgorithmExecutor"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5KFKRK4OXOBDFKUAAMPGXUWTQ"),null,43,null,false,org.radixware.ads.Arte.common.EventSource.AlgoExecutor),

						/*Radix::Arte::EventSource:AppSysEventNotify-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciINPXZJH7XRGKFDAU2UBDJLYBVI"),"AppSysEventNotify",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.SysEventNotify"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMV2YUXM4V7OBDNVPAAMPGXSZKU"),null,5,null,false,org.radixware.ads.Arte.common.EventSource.AppSysEventNotify),

						/*Radix::Arte::EventSource:TestCase-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBDR6OQMDWJHFRBABRQBE26DNYU"),"TestCase",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.TestCase"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJKFKRK4OXOBDFKUAAMPGXUWTQ"),null,9,null,false,org.radixware.ads.Arte.common.EventSource.TestCase),

						/*Radix::Arte::EventSource:MqProcessor-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5WZEMWLZPBBKRIFNG6NBQVOOBM"),"MqProcessor",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.MqProcessor"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNTYUWOUTZHWTJI2ARJO57OTFY"),null,7,null,false,org.radixware.ads.Arte.common.EventSource.MqProcessor),

						/*Radix::Arte::EventSource:ExplorerApp-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci72EL5SVYFJFK7IJ7P2N723NJ44"),"ExplorerApp",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Explorer.App"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETREIVZDTHORDGZJABIFNQAABA"),null,50,null,false,org.radixware.ads.Arte.common.EventSource.ExplorerApp),

						/*Radix::Arte::EventSource:UserFunc-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciVHTSMNFUNNBTJFRXGWTPCZA2FI"),"UserFunc",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.UserFunc"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLUJR4PB4UJHIHGLNXOPG76RFRE"),null,8,null,false,org.radixware.ads.Arte.common.EventSource.UserFunc),

						/*Radix::Arte::EventSource:Todo-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci2DKEMN7EXZGPVP4XROUJ6ZOCPE"),"Todo",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Todo"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TRVQFA325DFFMMGDI4PBK4B3I"),null,16,null,false,org.radixware.ads.Arte.common.EventSource.Todo),

						/*Radix::Arte::EventSource:Workflow-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciC5TVCZNJYNE2JCHXN3NOIAS23I"),"Workflow",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Workflow"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOQRND3U6PFF4FKNV4ZLTRFO53M"),null,14,null,false,org.radixware.ads.Arte.common.EventSource.Workflow),

						/*Radix::Arte::EventSource:PersoCommUnit-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciWXJXSR6MNNCX7LRIYI3X7TV2AI"),"PersoCommUnit",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.PersoComm"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDK5AOXXD5DFZAJWOMYBYPSHMM"),null,23,null,false,org.radixware.ads.Arte.common.EventSource.PersoCommUnit),

						/*Radix::Arte::EventSource:ExplorerDefManager-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci476M3WW3XNGJFFLCDS5TIN3KI4"),"ExplorerDefManager",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Explorer.DefManager"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG7L7LZTG3ZH3HHFNPREZRXU62E"),null,51,null,false,org.radixware.ads.Arte.common.EventSource.ExplorerDefManager),

						/*Radix::Arte::EventSource:Explorer-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6SM7K5JS5JBERB7BHS7UJIEPKQ"),"Explorer",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Explorer"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNOAB7GMENB7JORIN7ACTA2USY"),null,52,null,false,org.radixware.ads.Arte.common.EventSource.Explorer),

						/*Radix::Arte::EventSource:Eas-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciE4J4DRLDS5CRBMJ5CX6AMDOKPQ"),"Eas",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Eas"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIN2XVN7MXJBOBC6L2NMSLCGB4E"),null,37,null,false,org.radixware.ads.Arte.common.EventSource.Eas),

						/*Radix::Arte::EventSource:ArteReports-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciA5UFXOQYZVGD3HR3MME5ZDOC5U"),"ArteReports",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Reports"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKZ5TWIGOJDIDEVUVAAVAJTU6M"),null,44,null,false,org.radixware.ads.Arte.common.EventSource.ArteReports),

						/*Radix::Arte::EventSource:APP_AUDIT-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciF4ZMNBLP6JER7DPCAVRMJYTUCQ"),"APP_AUDIT",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.Audit"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4K3TBJHTVHLPBCOCOTIMASGP4"),null,12,null,false,org.radixware.ads.Arte.common.EventSource.APP_AUDIT),

						/*Radix::Arte::EventSource:ARTE_PROFILER-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6IWH4AIYYVHPLMR6Z536K2CXGQ"),"ARTE_PROFILER",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Profiler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKH3PCQYXZB7ZHFYDENOLYXEWE"),null,47,null,false,org.radixware.ads.Arte.common.EventSource.ARTE_PROFILER),

						/*Radix::Arte::EventSource:ARTE_POOL-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciNPTF7TX6NFEYPEKVGNABOPZLII"),"ARTE_POOL",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Pool"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MKXQAYR7FFFVK3RO54G2PBX4E"),null,48,null,false,org.radixware.ads.Arte.common.EventSource.ARTE_POOL),

						/*Radix::Arte::EventSource:NetHubHandler-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci3C35HCUWOVHZHBCM5ZUMXW343I"),"NetHubHandler",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.NetHubHandler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREEUEGL6NJEDDP3OJUWBXTMHF4"),null,25,null,false,org.radixware.ads.Arte.common.EventSource.NetHubHandler),

						/*Radix::Arte::EventSource:SystemMonitoring-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci4VSRGAWJBFEH3IAVZIWZJQVMTI"),"SystemMonitoring",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("App.SystemMonitoring"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSFVHVCIP7BEQLM2BZNSQIFON7A"),null,13,null,false,org.radixware.ads.Arte.common.EventSource.SystemMonitoring),

						/*Radix::Arte::EventSource:JmsHandler-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciCZ46CUNDRNEEPGKRWARIHH7NAI"),"JmsHandler",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.JmsHandler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFXKCTRRJFEYJHJB24VVSCHFIY"),null,26,null,false,org.radixware.ads.Arte.common.EventSource.JmsHandler),

						/*Radix::Arte::EventSource:SnmpAgent-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTWSHJ6RA4VCRXBFFQNQKLMTUBU"),"SnmpAgent",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.SnmpAgent"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWAWTEXEUNEU5A6PLMW3ZSSVMI"),null,33,null,false,org.radixware.ads.Arte.common.EventSource.SnmpAgent),

						/*Radix::Arte::EventSource:IAD-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciRPPNN7ZBFJCMJFDGGEF3JFM2X4"),"IAD",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Explorer.IAD"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LN356QPKREGTNHGTEJKMKRHW4"),null,53,null,false,org.radixware.ads.Arte.common.EventSource.IAD),

						/*Radix::Arte::EventSource:ARTE_TRACE-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciORVH767MWVAX7KUPYFZLLFKAGA"),"ARTE_TRACE",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Trace"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTTHBNO4CNFIXFQLGGR6BEYSUA"),null,46,null,false,org.radixware.ads.Arte.common.EventSource.ARTE_TRACE),

						/*Radix::Arte::EventSource:MqHandlerUnit-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7SDSE7MFJRBNJOQS77VGBYNLGM"),"MqHandlerUnit",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.MqHandler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWKPZEVZEBETHDGYBQ5J5BSUXU"),null,27,null,false,org.radixware.ads.Arte.common.EventSource.MqHandlerUnit),

						/*Radix::Arte::EventSource:Server.ChannelPort-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciG4HYHXQKXFFKVMRYG5WOYWVD6I"),"Server.ChannelPort",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.ChannelPort"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3TRU7WILVCGTA6V6N7S5YUBA4"),null,34,null,false,org.radixware.ads.Arte.common.EventSource.Server.ChannelPort),

						/*Radix::Arte::EventSource:NetPortHandlerPort-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6GAUJTYZVPORDJHCAANE2UAFXA"),"NetPortHandlerPort",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.NetPortHandler.Port"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOJ6IRIPHFG4XE6GQ5GV4QN6EQ"),null,29,null,false,org.radixware.ads.Arte.common.EventSource.NetPortHandlerPort),

						/*Radix::Arte::EventSource:JobScheduler-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBSBEJTYZVPORDJHCAANE2UAFXA"),"JobScheduler",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.JobScheduler"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCGKV72XT5ENZJXZYAMWKRI7ZE"),null,31,null,false,org.radixware.ads.Arte.common.EventSource.JobScheduler),

						/*Radix::Arte::EventSource:NetPortHandlerService-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciBNDXSOJD2NH65HJI224DJANWT4"),"NetPortHandlerService",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.NetPortHandler.Service"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPQULGD5RNCTVDCPKBULU5MOR4"),null,30,null,false,org.radixware.ads.Arte.common.EventSource.NetPortHandlerService),

						/*Radix::Arte::EventSource:NetPortHandlerQueue-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciLB3UEQY3QFDT7N5HD7RDRNXPME"),"NetPortHandlerQueue",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Server.Unit.NetPortHandler.Queue"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4NGYK7BPJAZ7DO575ATTO452I"),null,55,null,false,org.radixware.ads.Arte.common.EventSource.NetPortHandlerQueue),

						/*Radix::Arte::EventSource:WebDriver-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciZXFR5Y5GEBCDDJQIVTPHSNW5YQ"),"WebDriver",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Client.Explorer.WebDriver"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAS2VZTXMVDVXG77R3KR2UUGBA"),null,56,null,false,org.radixware.ads.Arte.common.EventSource.WebDriver),

						/*Radix::Arte::EventSource:ARTE_ACTIVITY-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aciTRQPBF2UC5FADK7PBRIY472CGI"),"ARTE_ACTIVITY",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.Activity"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVYZMPNP5RBIJFCDOHGYHR7774"),null,57,null,false,org.radixware.ads.Arte.common.EventSource.ARTE_ACTIVITY),

						/*Radix::Arte::EventSource:NotificationSender-Enumeration Item*/

						new org.radixware.kernel.common.meta.RadEnumDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5LVANENNZFD6TMJ7PKGKQBI7BE"),"NotificationSender",org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Arte.NotificationSender"),org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2NDSJEIQLVA6BL5NW46V6GOFBM"),null,58,null,false,org.radixware.ads.Arte.common.EventSource.NotificationSender)
				};
	 rdxMeta = new org.radixware.kernel.common.meta.RadEnumDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY"),
				"EventSource",
				org.radixware.kernel.common.enums.EValType.STR,false,item_meta_arr,$env_instance_storage_for_internal_usage$);
	}

}

/* Radix::Arte::EventSource - Localizing Bundle */
package org.radixware.ads.Arte.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EventSource - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Message Queue Producer");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Отправитель очереди сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2C5W26CXO5GHTB6ILC6CKPP7II"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Notification Sender");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Рассыльщик уведомлений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2NDSJEIQLVA6BL5NW46V6GOFBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Explorer - Image acquiring devices");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Проводник - Устройства захвата изображения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LN356QPKREGTNHGTEJKMKRHW4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Pool");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Пул");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6MKXQAYR7FFFVK3RO54G2PBX4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Task to Be Done");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Нереализованная функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TRVQFA325DFFMMGDI4PBK4B3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Network Port Handler - Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обработчик сетевых соединений - Сервис");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAPQULGD5RNCTVDCPKBULU5MOR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Explorer - WebDriver");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Проводник - Вебдрайвер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAS2VZTXMVDVXG77R3KR2UUGBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enumeration containing the event sources");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перечисление, содержащее источники событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDQR23D56NFEFOYFAAWWKEQZEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHQX466IPPOBDCUSAAN7YHKUNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Отчеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKZ5TWIGOJDIDEVUVAAVAJTU6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Explorer - Application Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Проводник - Прикладной класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETREIVZDTHORDGZJABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Explorer - Definition Manager");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Проводник - Менеджер дефиниций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG7L7LZTG3ZH3HHFNPREZRXU62E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Profiler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Профилер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGKH3PCQYXZB7ZHFYDENOLYXEWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Configuration Package");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Пакет конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGUDMSBN3Y5BPRFYXODNFBAUAUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Personal Communications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Персональные оповещения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDK5AOXXD5DFZAJWOMYBYPSHMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Network Port Handler - Port");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обработчик сетевых соединений - Порт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHOJ6IRIPHFG4XE6GQ5GV4QN6EQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Service Bus");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Конвейер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDEFTPVRARADBE3C2BULDQ43R4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Explorer Access Service (EAS)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Explorer Access Service (EAS)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIN2XVN7MXJBOBC6L2NMSLCGB4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Test Case");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Тест");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJJKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Explorer");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Проводник");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJNOAB7GMENB7JORIN7ACTA2USY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - License");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Лицензия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJR6UK7DWZNBDPJSHHCXL5EKALQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Инстанция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Job Executor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Исполнитель заданий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJVKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Algorithm Executor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Исполнитель алгоритмов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5KFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE (Application Runtime Environment)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE (Среда исполнения приложений)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKFKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Definitions Manager");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Менеджер дефиниций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Communicator");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Коммуникатор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Trace");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Трасса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKTTHBNO4CNFIXFQLGGR6BEYSUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - SQL Query Builder");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Конструктор SQL запросов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKVKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Entity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Сущность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKZKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLFKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Network Port Handler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обработчик сетевых соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Job");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Задание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Message Queue Processor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Обработчик очереди сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNTYUWOUTZHWTJI2ARJO57OTFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - User-Defined Function");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Пользовательская функция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLUJR4PB4UJHIHGLNXOPG76RFRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client - Session");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Клиент - Сессия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - AAS Client");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Клиент сервиса AAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMH6WLKFG2DOBDEZ6AAMPGXSZKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Database");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - База данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Database");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - База данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMNKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Instance - Control Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Инстанция - Сервис управления");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMRKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - System Event Notifier");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Служба оповещения о системных событиях");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMV2YUXM4V7OBDNVPAAMPGXSZKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Application Core Access Service (AAS)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Application Core Access Service (AAS)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZKFKRK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Activity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Активность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNVYZMPNP5RBIJFCDOHGYHR7774"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - SNMP Agent");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - SNMP Aгент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWAWTEXEUNEU5A6PLMW3ZSSVMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Message Queue Handler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обслуживание очереди сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNWKPZEVZEBETHDGYBQ5J5BSUXU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Audit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Аудит");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4K3TBJHTVHLPBCOCOTIMASGP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Workflow");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Workflow");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOQRND3U6PFF4FKNV4ZLTRFO53M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOZEYYCMWDFGZHGZJZVSGBDAR6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Personal Communications");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Персональные коммуникации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPI3MOQSBLVDURA2URFGYBUQOIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Network Hub Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Сетевой канал-концентратор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREEUEGL6NJEDDP3OJUWBXTMHF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Task");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Задача");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUSKNGZ7JBDDMPZFEIBPLYFE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - JMS Handler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обслуживание очередей JMS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFXKCTRRJFEYJHJB24VVSCHFIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - System Monitoring");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Мониторинг системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSFVHVCIP7BEQLM2BZNSQIFON7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Job Scheduler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Планировщик заданий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTCGKV72XT5ENZJXZYAMWKRI7ZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Port");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Порт");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU3TRU7WILVCGTA6V6N7S5YUBA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server - Unit - Network Port Handler - Queue");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервер - Модуль - Обработчик сетевых соединений - Очередь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU4NGYK7BPJAZ7DO575ATTO452I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application - Upgrade");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приложение - Обновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMKZ4OZW55CQZKUQ3HNRLP56LE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(EventSource - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacsTNLJBZADHTNRDIPGABQAQNO6EY"),"EventSource - Localizing Bundle",$$$items$$$);
}

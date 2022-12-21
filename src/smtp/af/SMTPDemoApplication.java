package smtp.af;

import java.util.List;

import ec02.af.abstracts.AbstractAF;
import ec02.af.data.AFDataFactory;
import ec02.af.exception.ActionProcessException;
import ec02.af.utils.AFLog;
import ec02.data.abstracts.AStdCDRData;
import ec02.data.abstracts.AStdEDRData;
import ec02.data.enums.EStdLogDelimiter;
import ec02.data.interfaces.ECDialogue;
import ec02.data.interfaces.EquinoxPropertiesAF;
import ec02.data.interfaces.EquinoxRawData;
import ec02.data.interfaces.InstanceData;
import ec02.data.interfaces.StdCDRData;
import ec02.data.interfaces.StdEDRFactory;

/** TODO : Guide Example code for annotation feature.  
 
import ec02.tools.utils.annotation.equinox.Alarm;
import ec02.tools.utils.annotation.equinox.EquinoxAlarms;
import ec02.tools.utils.annotation.equinox.EquinoxConfiguration;
import ec02.tools.utils.annotation.equinox.EquinoxEC02;
import ec02.tools.utils.annotation.equinox.EquinoxLogs;
import ec02.tools.utils.annotation.equinox.EquinoxStats;
import ec02.tools.utils.annotation.equinox.Log;
import ec02.tools.utils.annotation.equinox.Properties;
import ec02.tools.utils.annotation.equinox.Stat;

@EquinoxConfiguration(applicationName = "TMApplication")

@EquinoxEC02(afLibrary="TMApplication.jar"
					  , warmConfiguration = {@Properties(name = "timeout", value = "4")
													,@Properties(name = "machine-name", value = "Anno-1")
												    ,@Properties(name = "machine-group", value = "AnnoApp") 
												  	})

@EquinoxStats({@Stat(name = "AF-ANNO Success")
					  ,@Stat(name = "AF-ANNO Error") })

@EquinoxAlarms({@Alarm(name = "AF-ANNO Fail", id = "001")
						,@Alarm(name = "AF-ANNO Error", id = "002") })

@EquinoxLogs({@Log(name = "AF-ANNO SummaryLog")
					  ,@Log(name = "AF-ANNO DetailLog") })
 */
public class SMTPDemoApplication extends AbstractAF {
	
	@Override
	public ECDialogue actionProcess(EquinoxPropertiesAF equinoxPropertiesAF
			, List<EquinoxRawData> eqxRawDataList, InstanceData instance) throws ActionProcessException {
		AFLog.d("[Start Process]");
		AFLog.d("[CURRENT STATE] : " + equinoxPropertiesAF.getState());
		//EQX4Wrapper.extractRawData(eqxRawDataList);
		ApplicationStateManager sm = new ApplicationStateManager(equinoxPropertiesAF.getState());
		AFLog.d("[End Process]");
		return sm.doProcess(this, instance, eqxRawDataList);
	}

	@Override
	public boolean verifyAFConfiguration(String arg0) {
		return true;
	}
	
	class MyCDR extends AStdCDRData {

		@Override
		public void createRecord(InstanceData arg0) {
			// TODO Auto-generated method stub
			this.setOriginatingServiceName("Server");
		}

		@Override
		public void createRecordPartialEnd(InstanceData arg0) {
			// TODO Auto-generated method stub
			this.setOriginatingServiceName("Server");
		}

		@Override
		public void createRecordPartialIntermediate(InstanceData arg0) {
			// TODO Auto-generated method stub
			this.setOriginatingServiceName("Server");
		}

		@Override
		public void createRecordPartialStart(InstanceData arg0) {
			// TODO Auto-generated method stub
			this.setOriginatingServiceName("Server");
		}
		
	}
	
	@Override
	public StdCDRData initializedCallDetailRecord() {
		MyCDR cdr = new MyCDR();
		
		cdr.setDelimiter(EStdLogDelimiter.COMMA);
		cdr.setApplicationName("EQX");
		cdr.setComponentName("TEST");
		return cdr;
	}

	
	class MyEDR1 extends AStdEDRData {
		@Override
		public void createRecord(InstanceData instance) {
			// TODO
			this.setRecordName("m1");
			this.setInvokeId("Invoked");
			this.setDestinationServiceName("Server");
		}

	}
	
	class MyEDR2 extends AStdEDRData {
		@Override
		public void createRecord(InstanceData instance) {
			// TODO
			this.setRecordName("m2");
			this.setInvokeId("Invoked");
			this.setDestinationServiceName("Server");
		}
	}

	
	@Override
	public StdEDRFactory initializedEventDetailRecord() {
		StdEDRFactory ef = AFDataFactory.createEDRFactory();
		
		MyEDR1 edr1 = new MyEDR1();
		MyEDR2 edr2 = new MyEDR2();
		
		ef.setDelimiter(EStdLogDelimiter.COMMA);
		ef.setApplicationName("EQX");
		ef.setComponentName("TEST");
		
		ef.addRecordType("m1", edr1);
		ef.addRecordType("m2", edr2);
		
		return ef;
	}
}

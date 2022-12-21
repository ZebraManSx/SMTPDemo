package smtp.states;

import java.util.HashMap;
import java.util.List;

import smtp.af.EApplicationState;

import ec02.af.abstracts.AbstractAF;
import ec02.af.data.AFDataFactory;
import ec02.af.interfaces.IAFState;
import ec02.af.utils.AFLog;
import ec02.data.interfaces.ECDialogue;
import ec02.data.interfaces.EquinoxRawData;
import ec02.data.interfaces.IMessageBuilder;
import ec02.data.interfaces.InstanceData;
import ec02.exception.BuilderParserException;

public class ApplicationStateW_SMTP_RESPONSE implements IAFState {
	//private TextManagement textManagement = TextManagement.getInstance();
	
	@Override
	public ECDialogue doAction(AbstractAF abstractAF, InstanceData instance, List<EquinoxRawData> eqxRawDataList) {
		AFLog.d("Hello, I am : " +  abstractAF.getEquinoxProperties().getApplicationName());
		//String incomingMsg = null;
		 
		//Map<String, List<String>> warmConfigurations = abstractAF.getEquinoxUtils().getHmWarmConfig();
		
		String instanceMessage = instance.toString();
		AFLog.d("instance is :"+instanceMessage);
		
		String valueDelimiter = "#=#";
		String[] bb = instanceMessage.split("\\|");
		HashMap<String,String> insAttributes = new HashMap<String, String>(); 
		for(String cc:bb) {
			String[] dd = cc.split(valueDelimiter);
			insAttributes.put(dd[0],dd[1]);
		}
		
		//ArrayList<String[]> ERDHeaderObj = null;
		HashMap<String, String> optionalAttribute = null;
		//String msg_name =  insAttributes.get("name");
		String msg_ctype  =  insAttributes.get("ctype");
		String msg_invoke  =  insAttributes.get("invoke");
		String[] msg_to =  insAttributes.get("orig").split("\\.");
	
		AFLog.d("Parse [instance] msg_ctype is :"+msg_ctype); 
		
		EApplicationState nextState = EApplicationState.IDLE;
		int timeout = 10; 
		for (EquinoxRawData eqxRawData : eqxRawDataList) {  
			if(eqxRawData.getRet().equals("0") ) {
				try {
					//send message
					String descSMTPResp = "";
					if(eqxRawData.getRawDataAttribute("desc")!=null){
						descSMTPResp =eqxRawData.getRawDataAttribute("desc");
					}
					String respMsg  = "<SMTP name=\"Demo\" value=\"Success\" desc=\""+descSMTPResp+"\" />";
		     		abstractAF.getEquinoxUtils().sendHTTPResponseMessage(new RawMessage(respMsg)
			   					, ec02.data.enums.EEquinoxRawData.CTypeHTTP.TEXT_XML
			   					, msg_invoke 
			   					, msg_to[2],msg_to[3] 
			   					, optionalAttribute);
		     		
					}catch(Exception ex) {
						AFLog.e(ex);
					}
			}else {
				try {
		     		//send message
		     		abstractAF.getEquinoxUtils().sendHTTPResponseMessage(new RawMessage("<SMTP name=\"Demo\" value=\"Error|Reject|Abort\" />")
			   					, ec02.data.enums.EEquinoxRawData.CTypeHTTP.TEXT_XML
			   					, msg_invoke 
			   					, msg_to[2],msg_to[3] 
			   					, optionalAttribute);
		     		
				}catch(Exception ex) {
					AFLog.e(ex);
				}
			}
		}
		//abstractAF.getEquinoxUtils().setInstanceMessage(instanceMessage.toString());
  		
  		//createECDialogue
  		return AFDataFactory.createECDialogue(AFDataFactory.createEquinoxProperties(nextState.name(), String.valueOf(timeout)));
	}

	class RawMessage implements IMessageBuilder {
		private StringBuilder data = new StringBuilder(""); 

		RawMessage(String _data) {
			this.data.append(_data);
		}

		@Override
		public String buildMessage() throws BuilderParserException { 
			return this.data.toString();
		}
	}
	
	@Override
	public ECDialogue doAged(AbstractAF arg0, InstanceData arg1, List<EquinoxRawData> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECDialogue doShutdown(AbstractAF arg0, InstanceData arg1, List<EquinoxRawData> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ECDialogue doTimeout(AbstractAF arg0, InstanceData arg1, List<EquinoxRawData> arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

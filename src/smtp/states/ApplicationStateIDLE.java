package smtp.states;
     
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ec02.af.abstracts.AbstractAF;
import ec02.af.data.AFDataFactory;
import ec02.af.interfaces.IAFState; 
import ec02.af.utils.AFLog;
import ec02.data.interfaces.ECDialogue; 
import ec02.data.interfaces.EquinoxRawData;
import ec02.data.interfaces.IMessageBuilder;
import ec02.data.interfaces.InstanceData;
import ec02.exception.BuilderParserException;
import smtp.af.EApplicationState; 

//import smtp.utilities.*;

public class ApplicationStateIDLE implements IAFState { 
	//private TextManagement textManagement = TextManagement.getInstance();
	 
	@Override
	public ECDialogue doAction(AbstractAF abstractAF, InstanceData instanceData, List<EquinoxRawData> eqxRawDataList) {
		AFLog.d("Hello, I am : " +  abstractAF.getEquinoxProperties().getApplicationName());
		//String incomingMsg = null;
		//String[] to = null;//new String[4]; //[index 0:ApplicationName, 1: RA , 2:ServiceName ,3: Instance Name] 
 		/*String name = "" ; //protocol
		String type=  "" ;//request | response 
		String ctype =  "" ; //cType of protocol
		String invoke =  "" ; //request-id 
		String val =  "" ; //raw data
		*/
		//TODOne : for write log
		//final String logFileId= abstractAF.getEquinoxProperties().getApplicationName()+"_"+System.nanoTime();
		 
		Map<String, List<String>> warmConfigurations = abstractAF.getEquinoxUtils().getHmWarmConfig();
		for (Entry<String, List<String>> entry : warmConfigurations.entrySet()) {
			AFLog.d("key is:"+entry.getKey());
			for(String val : entry.getValue()) {
				AFLog.d("\n\t|--- value is:"+val);
			}
		}
		

		EApplicationState nextState = EApplicationState.IDLE;
		int timeout = 10;
		String smtpPattern1 = "(?is)(<SMTP name=\"Demo\"\\s{0,}value=\")(.*?)(\"\\s{0,}/>)"; 
		 
		
		
		HashMap<String, String> optionalAttribute = null;
		StringBuilder instanceMessage = new StringBuilder("");
		String valueDelimiter = "#=#";

		
  		for (EquinoxRawData eqxRawData : eqxRawDataList) {		
			Pattern p1 = Pattern.compile(smtpPattern1);
			Matcher m1 = p1.matcher(eqxRawData.getRawDataMessage());
			
			if(m1.find()) {
				try {
	        		optionalAttribute = new HashMap<String,String>();
	        		 
	        		//send message 
	        		StringBuilder message = new StringBuilder("");
	        		
	        		/**
	        		 <subjectValue value="Hello SMTP Demo" />
        			
        			<toName1 value="" />
        			<toEmail1 value="" />
					<toName2 value="" />
        			<toEmail2value="" />
        			<toName3 value="" />
        			<toEmail3value="" />
        			
        			<ccName1 value="" />
        			<ccEmail1 value="" />
					<ccName2 value="" />
        			<ccEmail2 value="" />
        			<ccName3 value="" />
        			<ccEmail3 value="" />
        			
        			<bccName1 value="" />
        			<bccEmail1 value="" />
					<bccName2 value="" />
        			<bccEmail2 value="" />
        			<bccName3 value="" />
        			<bccEmail3 value="" />
        			
        			<attachName1 value="" />
        			<attachPath1 value="" />
					<attachName2 value="" />
        			<attachPath2 value="" />
        			<attachName3 value="" />
        			<attachPath3 value="" />
        			
        			<bodyValue value="Hi SMTP this is your msg : 1234 Test !@#$..." />
        			
	        		 */
	        		message.append("<EQXMail>")
	                .append("<Subject value=\"").append(warmConfigurations.get("subjectValue").get(0)).append("\" />")
	                .append("<Content-Type value=\"").append(warmConfigurations.get("contentTypeofBody").get(0)).append("\" />")
	                .append("<To name=\"").append(warmConfigurations.get("toName1").get(0)).append("\" email=\"").append(warmConfigurations.get("toEmail1").get(0)).append("\" />")
	                .append("<To name=\"").append(warmConfigurations.get("toName2").get(0)).append("\" email=\"").append(warmConfigurations.get("toEmail2").get(0)).append("\" />")
	                .append("<To name=\"").append(warmConfigurations.get("toName3").get(0)).append("\" email=\"").append(warmConfigurations.get("toEmail3").get(0)).append("\" />")

	                .append("<CC name=\"").append(warmConfigurations.get("ccName1").get(0)).append("\" email=\"").append(warmConfigurations.get("ccEmail1").get(0)).append("\" />")
	                .append("<CC name=\"").append(warmConfigurations.get("ccName2").get(0)).append("\" email=\"").append(warmConfigurations.get("ccEmail2").get(0)).append("\" />")
	                .append("<CC name=\"").append(warmConfigurations.get("ccName3").get(0)).append("\" email=\"").append(warmConfigurations.get("ccEmail3").get(0)).append("\" />")

	                .append("<BCC name=\"").append(warmConfigurations.get("bccName1").get(0)).append("\" email=\"").append(warmConfigurations.get("bccEmail1").get(0)).append("\" />")
	                .append("<BCC name=\"").append(warmConfigurations.get("bccName2").get(0)).append("\" email=\"").append(warmConfigurations.get("bccEmail2").get(0)).append("\" />")
	                .append("<BCC name=\"").append(warmConfigurations.get("bccName3").get(0)).append("\" email=\"").append(warmConfigurations.get("bccEmail3").get(0)).append("\" />")
 
	                .append("<Attach name=\"").append(warmConfigurations.get("attachName1").get(0)).append("\" path=\"").append(warmConfigurations.get("attachPath1").get(0)).append("\" />")
	                .append("<Attach name=\"").append(warmConfigurations.get("attachName2").get(0)).append("\" path=\"").append(warmConfigurations.get("attachPath2").get(0)).append("\" />")
	                .append("<Attach name=\"").append(warmConfigurations.get("attachName3").get(0)).append("\" path=\"").append(warmConfigurations.get("attachPath3").get(0)).append("\" />")
	            
	                .append("<Body value=\"").append(warmConfigurations.get("bodyValue").get(0)).append("\" />")
	                .append("</EQXMail>");
	            
	        		String invoke = "1";
	        		String serviceName = "0";
	        		String instance = "0";
	        		
	        		abstractAF.getEquinoxUtils().sendSMTPRequestMessage(new RawMessage(message.toString())
	   					, ec02.data.enums.EEquinoxRawData.CTypeSMTP.TEXT_XML
	   					, invoke 
	   					, serviceName,instance
	   					, optionalAttribute); 
	        		
	        		nextState = EApplicationState.W_SMTP_RESPONSE;
	        		
	        		instanceMessage = new StringBuilder("");
		         		instanceMessage.append("name").append(valueDelimiter).append(eqxRawData.getName())
		         		.append("|orig").append(valueDelimiter).append(eqxRawData.getOrig())
		         		.append("|invoke").append(valueDelimiter).append(eqxRawData.getInvoke())
		         		.append("|ctype").append(valueDelimiter).append(eqxRawData.getCType());

				} catch (Exception e) {
					AFLog.e(e);
				}
			}else {
				AFLog.d("3.received SMTP Demo Unknown....");
			}
		}// end for
		
  	
  		abstractAF.getEquinoxUtils().setInstanceMessage(instanceMessage.toString());
  		
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
		return null;
	}
	@Override
	public ECDialogue doShutdown(AbstractAF arg0, InstanceData arg1, List<EquinoxRawData> arg2) { 
		return null;
	}
	@Override
	public ECDialogue doTimeout(AbstractAF arg0, InstanceData arg1, List<EquinoxRawData> arg2) { 
		return null;
	}
  
	public static void main2(String[] args) {
		String[] aa = new String[] { "name1", "valu1" };
		String[] bb = new String[] { "name2", "valu2" };
		String[] cc = new String[] { "name3", "valu4" };
		Vector<String[]> v = new Vector<String[]>();
		v.add(aa);
		v.add(bb);
		v.add(cc);

		// for loop
		for (int i = 0; i < v.size(); i++) {

			System.out.print(v.get(i)[0] + "="+v.get(i)[1]);
		}
		
		ArrayList<String[]>arrys = new ArrayList<String[]>();
		arrys.add(aa);
		arrys.add(bb);
		arrys.add(cc);
		
		for(String[] attrs:arrys) {
			
			System.out.print(attrs[0] + "="+attrs[1]);
		}

		System.exit(0);
	}
	
	public static void main(String[] args) {
		String sipUEPattern1 = "(?is)(<Header name=\"From\" value=\"&lt;sip:)(.*?)(@)(ims\\.mnc)(\\d+)(\\.mcc)(\\d+)(\\.)(.*?)(&gt;)";
		String aa = " <Header name=\"From\" value=\"&lt;sip:520030107073610@ims.mnc003.mcc520.3gppnetwork.org&gt;;tag=d2bd0679\" />\n" + 
				"                        <Header name=\"To\" value=\"&lt;sip:520030107073610@ims.mnc003.mcc520.3gppnetwork.org&gt;\" />\n";
				;
		Pattern p1 = Pattern.compile(sipUEPattern1);
		Matcher m1 = p1.matcher(aa);
		
		if(m1.find()) {
			System.out.println(m1.group(2));
			System.out.println(m1.group(5));
			System.out.println(m1.group(7));
			System.out.println(m1.group(9));
		}
		
		System.exit(0);
	}
}

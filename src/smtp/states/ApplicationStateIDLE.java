package smtp.states;
     
import java.io.BufferedReader;
import java.io.FileReader; 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry; 
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
		AFLog.d("[SMTP Demo test with read file] Hello, I am : " +  abstractAF.getEquinoxProperties().getApplicationName());
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
		String smtpPattern1 = "(?is)(<SMTP>)(.*?)(</SMTP>)"; 
		 
		
		
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
	        		StringBuilder raES28message = new StringBuilder("");
	        		
	        		String fileName = m1.group(2);
					AFLog.d("smtp read file is : " +fileName); 
					try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
						String line;
						while ((line = br.readLine()) != null) {
							raES28message.append(line);
						}
					}catch(Exception ex) {
						AFLog.e(ex);
					}
					AFLog.d("build smtp message  for rawdata to ES28 is : " + raES28message); 
	            
	        		String invoke = "1";
	        		String serviceName = "0";
	        		String instance = "0";
	        		
	        		abstractAF.getEquinoxUtils().sendSMTPRequestMessage(new RawMessage(raES28message.toString())
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
  
	/*public static void main2(String[] args) {
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
	}*/
	
	public static void main(String[] args) {
		String aa = " <demo><SMTP>D:\\AIS Work\\EC02 Workspace\\SMTPDemo\\conf\\test1.txt</SMTP></demo>";
				
		String smtpPattern =  "(?is)(<SMTP>)(.*?)(</SMTP>)"; 
		Pattern p1 = Pattern.compile(smtpPattern);
		Matcher m1 = p1.matcher(aa);
		StringBuilder raES28message = new StringBuilder("");
		
			if(m1.find()) {
				String fileName = m1.group(2);
				System.out.println("fileName is : " +fileName); 
				try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
					String line;
					while ((line = br.readLine()) != null) {
						raES28message.append(line);
					}
				}catch(Exception ex) {
					System.out.println(ex);
				}
				System.out.println("build smtp message  for rawdata to ES28 is : " + raES28message); 
			}
		
		
		System.exit(0);
	}
}

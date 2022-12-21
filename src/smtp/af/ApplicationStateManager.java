package smtp.af;
 
import smtp.af.EApplicationState;
import smtp.states.*;
import ec02.af.abstracts.AbstractAFStateManager;


public class ApplicationStateManager extends AbstractAFStateManager{
	public ApplicationStateManager(String state) {
		this.afState = null;
		EApplicationState eTMApplicationState=Enum.valueOf(EApplicationState.class, state.toUpperCase()); 
		switch(eTMApplicationState){
			case IDLE:
				this.afState=new ApplicationStateIDLE();
				break;
			case W_SMTP_RESPONSE:
				this.afState=new ApplicationStateW_SMTP_RESPONSE();
				break;
		default:
			break;
		}
	}
}

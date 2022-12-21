package smtp.utilities;
  
public class TextManagement {
	
	private static TextManagement textManagement ;
	
	/** A private Constructor prevents any other class from instantiating. */
	private TextManagement() {
	}
	
	/** Make the Access method Synchronized to prevent Thread Problems.*/
	public static synchronized TextManagement getInstance() {
		if (textManagement == null) {
			textManagement = new TextManagement();
		}
		return  textManagement;
	}
	
	/**Override the Object clone method to prevent cloning*/
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public String convertEscapeCharacterToText(final String _escapeCharacter){
        String text = null;
        try{
            text =_escapeCharacter.replaceAll("&(?!(?:\\#|amp|quot|lt|gt|nbsp|\\d+);)", "&amp;");
            text =text.replaceAll("\"", "&quot;");
            text =text.replaceAll("'", "&apos;");
            text =text.replaceAll("<", "&lt;");
            text =text.replaceAll(">", "&gt;");
        }catch(Exception ex){
            text = null;
        }
        return (text== null)?_escapeCharacter:text;
    }
	
	public String convertTextToEscapeCharacter(final String _text) {
		String escapeCharacter = null;
		try {
			escapeCharacter = _text.replaceAll("(&quot;)", "\"")
					.replaceAll("(&apos;)", "'").replaceAll("(&lt;)", "<")
					.replaceAll("(&gt;)", ">").replaceAll("(&amp;)", "&");
		} catch (Exception ex) {
			escapeCharacter = null;
		}
		return (escapeCharacter == null) ? escapeCharacter : escapeCharacter;
	}
}

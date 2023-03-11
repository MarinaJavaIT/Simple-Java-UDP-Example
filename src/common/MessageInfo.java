package common;

import java.io.Serializable;

public class MessageInfo implements Serializable {
	public static final long serialVersionUID = 52L;
	public int totalMessages;
	public int messageNumber;
	public String data;

	public MessageInfo(String data, int total, int msgNum) {
		this.data=data;
		totalMessages = total;
		messageNumber = msgNum;
	}
	public MessageInfo(String msg) throws Exception {
		String[] fields = msg.split(";");
		if (fields.length!=2)
			throw new Exception("MessageInfo: Invalid string for message construction: " + msg);
		totalMessages = Integer.parseInt(fields[0]);
		messageNumber = Integer.parseInt(fields[1]);
	}
	public String toString(){
		return new String(data+totalMessages+";"+ messageNumber +"\n");
	}


}

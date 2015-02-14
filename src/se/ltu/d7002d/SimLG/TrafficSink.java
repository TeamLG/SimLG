package se.ltu.d7002d.SimLG;

/**
 * This class implements a traffic Sink that registers incoming & receiving
 * messages and prepares statistics to be printed, it contains different
 * functions to calculate the number of sent messages, received messages,dropped
 * messages in percentage,elapsed time, and received messages per millisecond.
 */

public class TrafficSink {

	private int RcvdMsgCounter=0;
	private int sentMessages =0;
	private float averagePerms=0.0;
	private int timeBetweenSending=10;// time between messages

	public int countMessage(){
		RcvdMsgCounter+=1;
		return RcvdMsgCounter=0;
	}

	public void getsentMsgs(){

		sentMessages += 1;
	}

	public float droppedMsgs(){

		return (sentMessages - RcvdMsgCounter);
	}

	public int getTime(){
		timeBetweenSending += 10;
		return timeBetweenSending;
	}

	public double getAverage(){
		averagePerms=countMessage()/getTime();
		return averagePerms;
	}

	public void printStatistics() {

		System.out.println("                                   ");
		System.out.println("Summary Statistics  ");
		System.out.println("                                   ");
		System.out.println("Received messages :" + RcvdMsgCounter);
		System.out.println("Sent messages : " + sentMessages);
		System.out.println("Dropped messages:" + droppedMsgs() / 100 + "%");
		System.out.println("Elapsed time: " + getTime() + "μs");
		System.out.println("Avg.receivedMsg/μs:" + getAverage());

	}

}

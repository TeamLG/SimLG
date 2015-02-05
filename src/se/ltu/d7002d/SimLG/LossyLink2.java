package se.ltu.d7002d.SimLG;

import java.util.Random;

/**
 * This class creates a lossy link which infers delay and Jitter and extends the
 * Link class.
 * 
 * @author Bambanza
 */
public class LossyLink2 extends Link {

	private double delay = 0;
	private double jitter = 0;
	private double packetLossProbability = 0.0;
	private int now = 0;
	private SimEnt _connectorA = null;
	private SimEnt _connectorB = null;

	public void setConnector(SimEnt connectTo) {
		if (_connectorA == null)
			_connectorA = connectTo;
		else
			_connectorB = connectTo;
	}

	/**
	 * we have three parameters which may cause a link to be lossy by creating a
	 * Delay,Jitter and a probability for packet loss
	 * 
	 * @param delay
	 *            represents the Time delay period
	 * @param Jitter
	 *            represents the Jitter that is calculated with a Gaussian
	 *            Distribution.
	 * @param probability
	 *            represents The probability of a Packet to get lost.
	 */
	public  LossyLink2(int delayTime, int jitterTime, double probability) {
		super();
		this.delay = delayTime;
		this.packetLossProbability = probability;
		this.jitter = jitterTime;
	}

	private double jitter() {
		Random randomno = new Random();
		return (jitter * randomno.nextGaussian());
	}

	public void recv(SimEnt src, Event ev)

	{
		if (ev instanceof Message) {
			if ((new Random().nextInt(100)) <= packetLossProbability) {
				System.out.println("Link recv msg, but got lost");
			}

			else {

				System.out.println("Link recv msg, passes it through");
				if (src == _connectorA) {
					send(_connectorB, ev, now + delay + jitter());
				} else {
					send(_connectorA, ev, now + delay + jitter());
				}
			}
		}
	}
}

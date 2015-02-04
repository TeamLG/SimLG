package se.ltu.d7002d.SimLG;

import java.util.Random;

/**
 * This class implements a link with loss, jitter and/or delay. Jitter follows a
 * gaussian (normal) distribution.
 * 
 * @author davnym-4
 * 
 */
public class LossyLink extends Link {
	private SimEnt _connectorA = null;
	private SimEnt _connectorB = null;
	private int _now = 0;
	private double _packetLossRisk = 0;
	private double _maximumGaussianJitter = 0;
	private double _packetDelay = 0;

	/**
	 * Creates a LossyLink object with a specific risk of packet loss, specific
	 * maximum amount of gaussian jitter, and specific delay.
	 * 
	 * @param risk
	 *            The risk of a packet being lost, from 0 to 1
	 * @param maxJitter
	 *            The maximal jitter, in time units
	 * @param delay
	 *            Constant delay, in time units
	 */
	public LossyLink(double risk, double maxJitter, double delay) {
		super();
		this._packetLossRisk = risk;
		this._maximumGaussianJitter = maxJitter;
		this._packetDelay = delay;
	}

	private double getJitter() {
		Random random = new Random();
		return Math.abs(_maximumGaussianJitter * random.nextGaussian());
	}

	public void recv(SimEnt src, Event ev) {
		Random random = new Random();
		if (ev instanceof Message) {
			if (random.nextDouble() <= _packetLossRisk) {
				Utils.logMessage(((Message) ev).source(),
						((Message) ev).destination(),
						"LossyLink recv msg, but it was lost");
			} else {
				Utils.logMessage(((Message) ev).source(),
						((Message) ev).destination(),
						"LossyLink recv msg, passes it through");
				if (src == _connectorA) {
					send(_connectorB, ev, _now + _packetDelay + getJitter());
				} else {
					send(_connectorA, ev, _now + _packetDelay + getJitter());
				}
			}
		}
	}

	public void setConnector(SimEnt connectTo) {
		if (_connectorA == null)
			_connectorA = connectTo;
		else
			_connectorB = connectTo;
	}
}
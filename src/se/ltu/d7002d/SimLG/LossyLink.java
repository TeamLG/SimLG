package se.ltu.d7002d.SimLG;

import java.util.Random;

/**
 * This class implements a link with packet loss, jitter and/or delay. Jitter
 * follows a gaussian (normal) distribution.
 * 
 * @author davnym-4
 * @author Bambanza
 * 
 */
public class LossyLink extends Link {
	private SimEnt _connectorA = null;
	private SimEnt _connectorB = null;
	private int _now = 0;
	private double _packetLossProbability = 0;
	private double _standardDeviation = 0;
	private double _packetDelay = 0;
	private Random _random = null;

	/**
	 * Creates a LossyLink object with some probability of packet loss, a
	 * maximum amount of gaussian jitter, and some delay.
	 * 
	 * @param probability
	 *            The probability of a packet being lost, from 0 to 1.
	 * @param standardDeviation
	 *            The standard deviation from the delay.
	 * @param delay
	 *            Constant delay, in time units.
	 */
	public LossyLink(double probability, double standardDeviation, double delay) {
		super();
		_packetLossProbability = probability;
		if (_packetLossProbability < 0)
			_packetLossProbability = 0;
		else if (_packetLossProbability > 1)
			_packetLossProbability = 1;
		/*
		 * Ensure that delay is positive, unless it's a time traveling link.
		 */
		_standardDeviation = standardDeviation;
		_packetDelay = delay;
		if (_packetDelay < 0)
			_packetDelay = 0;
		_random = new Random();
	}

	/**
	 * Creates a LossyLink object with some probability of packet loss, a
	 * maximum amount of gaussian jitter, and some delay.
	 * 
	 * @param tries
	 *            The average number of tries necessary to incur one packet
	 *            loss.
	 * @param standardDeviation
	 *            The standard deviation from the delay.
	 * @param delay
	 *            Constant delay, in time units.
	 */
	public LossyLink(int tries, double standardDeviation, double delay) {
		super();
		if (tries <= 0)
			_packetLossProbability = 1;
		else
			_packetLossProbability = 1.0 / tries;
		/*
		 * Ensure that delay is positive, unless it's a time traveling link.
		 */
		_standardDeviation = standardDeviation;
		_packetDelay = delay;
		if (_packetDelay < 0)
			_packetDelay = 0;
		_random = new Random();
	}

	public void recv(SimEnt src, Event ev) {
		Random random = new Random();
		if (ev instanceof Message) {
			if (random.nextDouble() <= _packetLossProbability) {
				Utils.logMessage(((Message) ev).source(),
						((Message) ev).destination(),
						"LossyLink recv msg, but it was lost");
			} else {
				Utils.logMessage(((Message) ev).source(),
						((Message) ev).destination(),
						"LossyLink recv msg, passes it through");
				double delay = _packetDelay + _random.nextGaussian() * _standardDeviation;
				if (src == _connectorA) {
					send(_connectorB, ev, _now + (delay >= 0 ? delay : 0));
				} else {
					send(_connectorA, ev, _now + (delay >= 0 ? delay : 0));
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
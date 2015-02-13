package se.ltu.d7002d.SimLG;

import java.util.Random;

/**
 * Implements a traffic generator with an exponential distribution for intermessage
 * gaps (resulting in a Poisson distribution for the number of messages per unit of time).
 * 
 * @author davnym-4
 * 
 */
public class TrafficGeneratorPoisson extends TrafficGeneratorBase {
	private Random _random = null;

	/**
	 * Construct a traffic generator with default values for generating
	 * intermessage gaps with an exponential distribution.
	 */
	public TrafficGeneratorPoisson() {
		super();
		_random = new Random();
	}

	/**
	 * Construct a traffic generator with specific values for generating
	 * intermessage gaps with an exponentialdistribution.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param meanIntermessageGap
	 *            The expected time to wait after a message has been sent.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorPoisson(int numberOfMessages,
			double meanIntermessageGap, long messageStartSequence) {
		super();
		_numberOfMessages = numberOfMessages;
		_intermessageGap = meanIntermessageGap;
		_messageStartSequence = messageStartSequence;
		_random = new Random();
	}

	@Override
	protected void sendMessage(SimEnt messenger, Message message) {
		internalEventHandler.send(_initialSender, messenger, message, 0);
		Utils.logMessage(((Node) _initialSender).getAddr(), _finalReceiver,
				"Node " + ((Node) _initialSender).getAddr().dotAddress()
						+ " sent message with seq: " + _messageSequence);
		_numberOfMessagesSent += 1;
		_messageSequence += 1;
		if (_numberOfMessagesSent < _numberOfMessages) {
			double delay = -_intermessageGap * Math.log(_random.nextDouble());
			queueNext(delay);
		} else
			_sending = false;
	}
}

package se.ltu.d7002d.SimLG;

import java.util.Random;

/**
 * Implements a traffic generator with normal distribution for intermessage
 * gaps.
 * 
 * @author davnym-4
 * 
 */
public class TrafficGeneratorNormal extends TrafficGeneratorBase {
	private Random _random = null;

	/**
	 * Construct a traffic generator with default values for generating
	 * intermessage gaps with a normal distribution.
	 */
	public TrafficGeneratorNormal() {
		super();
		_random = new Random();
	}

	/**
	 * Construct a traffic generator with specific values for generating
	 * intermessage gaps with a normal distribution.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param meanIntermessageGap
	 *            The expected time to wait after a message has been sent.
	 * @param standardDeviation
	 *            The standard deviation from the expected time.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorNormal(int numberOfMessages,
			double meanIntermessageGap, double standardDeviation,
			long messageStartSequence) {
		super();
		_numberOfMessages = numberOfMessages;
		_intermessageGap = meanIntermessageGap;
		_standardDeviationScale = standardDeviation;
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
			double delay = _intermessageGap + _random.nextGaussian()
					* _standardDeviationScale;
			queueNext(delay >= 0 ? delay : 0);
		} else
			_sending = false;
	}
}

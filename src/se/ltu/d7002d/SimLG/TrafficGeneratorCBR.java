package se.ltu.d7002d.SimLG;

/**
 * Implements a constant bit rate traffic generator.
 * 
 * @author davnym-4
 * 
 */
public class TrafficGeneratorCBR extends TrafficGeneratorBase {

	/**
	 * Construct a CBR traffic generator with default values.
	 */
	public TrafficGeneratorCBR() {
		super();
	}

	/**
	 * Construct a CBR traffic generator with specific values.
	 * 
	 * @param numberOfMessages
	 *            The amount of messages to send before stopping.
	 * @param intermessageGap
	 *            The time to wait after a message has been sent.
	 * @param messageStartSequence
	 *            The message sequence number to start with.
	 */
	public TrafficGeneratorCBR(int numberOfMessages, double intermessageGap,
			long messageStartSequence) {
		super();
		this._numberOfMessages = numberOfMessages;
		this._intermessageGap = intermessageGap;
		this._messageStartSequence = messageStartSequence;
	}

	@Override
	protected void sendMessage(SimEnt messenger, Message message) {
		internalEventHandler.send(_initialSender, messenger, message, 0);
		Utils.logMessage(((Node) _initialSender).getAddr(), _finalReceiver,
				"Node " + ((Node) _initialSender).getAddr().dotAddress()
						+ " sent message with seq: " + _messageSequence);
		_numberOfMessagesSent += 1;
		_messageSequence += 1;
		if (_numberOfMessagesSent < _numberOfMessages)
			queueNext(_intermessageGap);
		else
			_sending = false;
	}
}

package se.ltu.d7002d.SimLG;

/**
 * Base class for traffic generators. Contains default values that may be
 * overridden/overwritten.
 * 
 * @author davnym-4
 * 
 */
public abstract class TrafficGeneratorBase {
	protected InternalEventHandler internalEventHandler = null;
	protected int _numberOfMessages = 10;
	protected long _messageStartSequence = 0;
	protected long _messageSequence = 0;
	protected double _interstreamGap = 0.0;
	protected double _intermessageGap = 5.0;
	protected double _standardDeviationScale = 1.0;
	protected int _numberOfMessagesSent = 0;
	protected boolean _sending = false;
	protected SimEnt _initialSender = null;
	protected NetworkAddr _finalReceiver = null;

	/**
	 * A class for exposing/using the SimEnt send and receive functionality.
	 */
	protected class InternalEventHandler extends SimEnt {
		public TrafficGeneratorBase owner = null;

		public void recv(SimEnt source, Event event) {
			if (event instanceof TimerEvent)
				owner.send(this, null);
		}
	}

	public TrafficGeneratorBase() {
		internalEventHandler = new InternalEventHandler();
		internalEventHandler.owner = this;
	}

	/**
	 * The class to contain adapted code for sending messages.
	 * 
	 * @param messenger
	 *            The immediate message recipient.
	 * @param message
	 *            The message to send.
	 */
	protected abstract void sendMessage(SimEnt messenger, Message message);

	/**
	 * The class for nodes to call when initiating traffic generation.
	 * 
	 * @param source
	 *            The source of the traffic.
	 * @param destination
	 *            The destination of the traffic.
	 */
	protected void send(SimEnt source, NetworkAddr destination) {
		if (_sending && source instanceof InternalEventHandler) {
			Message message = new Message(((Node) _initialSender).getAddr(),
					_finalReceiver, _messageSequence);
			sendMessage(((Node) _initialSender).getPeer(), message);
		} else if (source instanceof Node) {
			_initialSender = source;
			_finalReceiver = destination;
			_messageSequence = _messageStartSequence;
			_sending = true;
			queueNext(_interstreamGap);
		}
	}

	/**
	 * Called when readying the next message to send.
	 * 
	 * @param eventDelay
	 *            Time to delay the execution of the next event.
	 */
	protected void queueNext(double eventDelay) {
		internalEventHandler.send(internalEventHandler, new TimerEvent(),
				eventDelay);
	}

	/**
	 * Stop traffic generation.
	 */
	protected void stop() {
		_sending = false;
	}
}

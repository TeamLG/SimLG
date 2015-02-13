package se.ltu.d7002d.SimLG;

// This class implements an event that send a Message, currently the only
// fields in the message are who the sender is, the destination and a sequence 
// number

public class Message implements Event {
	private NetworkAddr _source;
	private NetworkAddr _destination;
	private long _seq = 0;

	Message(NetworkAddr from, NetworkAddr to, long _messageSequence) {
		_source = from;
		_destination = to;
		_seq = _messageSequence;
	}

	public NetworkAddr destination() {
		return _destination;
	}

	public void entering(SimEnt locale) {
	}

	public long seq() {
		return _seq;
	}

	public NetworkAddr source() {
		return _source;
	}
}

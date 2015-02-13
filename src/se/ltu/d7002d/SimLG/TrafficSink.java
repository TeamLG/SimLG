package se.ltu.d7002d.SimLG;

/**
 * This class implements a traffic Sink that registers incoming packets and
 * prepares statistics to be printed, it contains
 * 
 * @author Bambanza
 *
 */

public class TrafficSink extends Node {

	private int sentPackets = 0;
	private int receivedPackets = 0;
	private int time = 0;
	private TrafficSink myTrafficSink;

	/**
	 * Creates an object with an number of sent packets by the sending node,time
	 * and the number received packets
	 * 
	 * @param sentPackets
	 *            Represents the number of sent packets
	 * @param receivedPackets
	 *            Represents the number of received packets
	 * 
	 * 
	 */
	public TrafficSink(int network, int node, int sentPackets,
			int receivedPackets, int time) {
		super(network, node);
		this.sentPackets = sentPackets;
		this.receivedPackets = receivedPackets;
		myTrafficSink = new TrafficSink(network, node, sentPackets,
				sentPackets, time);
	}

	public int getReceivedPackets(int receivedPackets) {
		return receivedPackets;
	}

	public int getTime(int time) {

		time = _timeBetweenSending;

		return time;
	}

	public int getSentPackets(int sentPackets) {

		return sentPackets;
	}

	public void printStatistics() {
		System.out.println("Number of sent packets =" + sentPackets);
		System.out.println("received packets at" + time);
		System.out.println("Number of receved packets =" + receivedPackets);

	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			if (_stopSendingAfter > _sentmsg) {
				_sentmsg++;
				NetworkAddr source = new NetworkAddr(_id.networkId(),
						_id.nodeId());
				NetworkAddr destination = new NetworkAddr(_toNetwork, _toHost);
				send(_peer, new Message(_id, destination, _seq), 0);
				send(this, new TimerEvent(), _timeBetweenSending);
				Utils.logMessage(source, destination,
						"Node " + source.dotAddress()
								+ " sent message with seq: " + _seq);
				_seq++;

			}
		}
		if (ev instanceof Message) {
			receivedPackets++;
			myTrafficSink.getReceivedPackets(receivedPackets);
			Utils.logMessage(
					((Message) ev).source(),
					((Message) ev).destination(),
					"Node " + _id.networkId() + "." + _id.nodeId()
							+ " receives message with seq: "
							+ ((Message) ev).se
							q());

		}
	}

	/**
	 * This method is called upon that an event destined for this node triggers.
	 * 
	 */
	public void StartSending(int network, int node, int number,
			int timeInterval, int startSeq) {
		_stopSendingAfter = number;
		_timeBetweenSending = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		sentPackets++;
		myTrafficSink.getSentPackets(sentPackets);
		send(this, new TimerEvent(), 0);
	}
}

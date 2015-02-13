package se.ltu.d7002d.SimLG;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	private NetworkAddr _id;
	private SimEnt _peer;
	private int _sentmsg = 0;
	private int _seq = 0;
	private TrafficGeneratorBase _trafficGenerator = null;

	private int _stopSendingAfter = 0; // messages

	// Sets the peer to communicate with. This node is single homed

	private int _timeBetweenSending = 10; // time between messages

	private int _toNetwork = 0;

	// **********************************************************************************
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators

	private int _toHost = 0;

	public Node(int network, int node) {
		super();
		_id = new NetworkAddr(network, node);
		_trafficGenerator = new TrafficGeneratorCBR();
	}

	public Node(int network, int node, TrafficGeneratorBase trafficGenerator) {
		super();
		_id = new NetworkAddr(network, node);
		if (null == trafficGenerator)
			_trafficGenerator = new TrafficGeneratorCBR();
		else
			_trafficGenerator = trafficGenerator;
	}

	public Node(NetworkAddr networkAddress, TrafficGeneratorBase trafficGenerator) {
		super();
		if (null != networkAddress)
			_id = networkAddress;
		else
			_id = new NetworkAddr(0, 0);
		if (null == trafficGenerator)
			_trafficGenerator = new TrafficGeneratorCBR();
		else
			_trafficGenerator = trafficGenerator;
	}

	public NetworkAddr getAddr() {
		return _id;
	}

	public void recv(SimEnt src, Event ev) {
		if (ev instanceof TimerEvent) {
			if (_stopSendingAfter > _sentmsg) {
				_sentmsg++;
				NetworkAddr source = new NetworkAddr(_id.networkId(), _id.nodeId());
				NetworkAddr destination = new NetworkAddr(_toNetwork, _toHost);
				  send(_peer, new Message(_id, destination, _seq), 0);
				  send(this, new TimerEvent(), _timeBetweenSending);
				Utils.logMessage(source, destination, "Node " + source.dotAddress() + " sent message with seq: " + _seq);
				_seq++;
			}
		}
		if (ev instanceof Message) {
			Utils.logMessage(
					((Message) ev).source(),
					((Message) ev).destination(),
					"Node " + _id.networkId() + "." + _id.nodeId()
							+ " receives message with seq: "
							+ ((Message) ev).seq());
		}
	}

	public void setPeer(SimEnt peer) {
		_peer = peer;

		if (_peer instanceof Link) {
			((Link) _peer).setConnector(this);
		}
	}

	public SimEnt getPeer() {
		return _peer;
	}

	// **********************************************************************************

	// This method is called upon that an event destined for this node triggers.

	public void StartSending(int network, int node, int number, int timeInterval, int startSeq) {
		_stopSendingAfter = number;
		_timeBetweenSending = timeInterval;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(), 0);
	}

	public void setTrafficGenerator(TrafficGeneratorBase trafficGenerator) {
		if (null != trafficGenerator)
			_trafficGenerator = trafficGenerator;
	}

	public void startSending(NetworkAddr destination) {
		_trafficGenerator.send(this, destination);
	}

	public void stopSending(NetworkAddr destination) {
		_trafficGenerator.stop();
	}
}

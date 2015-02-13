package se.ltu.d7002d.SimLG;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main(String[] args) {
		// Creates two links
		Link link1 = new LossyLink(10, 2, 1);
		Link link2 = new LossyLink(0.10, 2, 1);

		// Create two end hosts that will be
		// communicating via the router
		NetworkAddr host1addr = new NetworkAddr(1, 1);
		NetworkAddr host2addr = new NetworkAddr(2, 2);
		Node host1 = new Node(host1addr, null);
		Node host2 = new Node(host2addr, null);

		// Connect links to hosts
		host1.setPeer(link1);
		host2.setPeer(link2);

		// Creates as router and connect
		// links to it. Information about
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router routeNode = new Router(2);
		routeNode.connectInterface(0, link1, host1);
		routeNode.connectInterface(1, link2, host2);

		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1.
		// Sequence starts with number 1
		host1.setTrafficGenerator(new TrafficGeneratorCBR(3, 5, 1));
		host1.startSending(host2addr);
		// host2 will send 2 messages with time interval 10 to network 1, node
		// 1. Sequence starts with number 10
		host2.setTrafficGenerator(new TrafficGeneratorCBR(2, 10, 10));
		host2.startSending(host1addr);

		// Start the simulation engine and of we go!
		Thread t = new Thread(SimEngine.instance());

		t.start();
		try {
			t.join();
		} catch (Exception e) {
			System.out
					.println("The motor seems to have a problem, time for service?");
		}

	}
}

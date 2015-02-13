package se.ltu.d7002d.SimLG;

// All entities like, nodes, switch, router, link etc that handles events
// need to inherit from this class

public abstract class SimEnt {

	protected SimEnt() {
	}

	// Called when erasing an entity like node or link etc. The SimEngine is
	// called in case
	// that de-registration of the entity is needed

	protected void destructor() {
		// no op, can be added in child classes
	}

	// To be implemented in child classes if cleaning up is needed when the
	// entity is killed

	protected final void eraseScheduledEvent(EventHandle handleToEvent) {
		SimEngine.instance().deregister(handleToEvent);
	}

	// This method schedules a coming event in the SimEngine

	protected final void kill() {
		this.destructor();
	}

	// Erases a scheduled event from the SimEngine

	public abstract void recv(SimEnt source, Event event);

	// To be implemented in child classes acting on events/messages received

	protected final EventHandle send(SimEnt destination, Event event,
			double delayExecution) {
		// this object is the registrator/source submitting the event
		return SimEngine.instance().register(this, destination, event,
				delayExecution);
	}

	protected final EventHandle send(SimEnt source, SimEnt destination, Event event,
			double delayExecution) {
		return SimEngine.instance().register(source, destination, event,
				delayExecution);
	}

}

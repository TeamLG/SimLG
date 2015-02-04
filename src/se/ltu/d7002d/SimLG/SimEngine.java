package se.ltu.d7002d.SimLG;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.TreeMap;

// This class implements the simulation engine
// As long as there are events in the queue, the simulation 
// will run. When empty, the engine stops

public final class SimEngine implements Runnable {

	private static SimEngine _instance;
	public static double getTime() {
		BigDecimal time = new BigDecimal(_simTime);
		int precision = 6;
		return time.round(new MathContext(precision)).doubleValue();
	}
	private final TreeMap _simTimeTree = new TreeMap();
	private boolean _quit = false;

	// This method is called to when scheduling an event for some target.
	// Examples of events are messages,
	// timer events etc.

	private static double _simTime = 0;

	public static SimEngine instance() {
		if (_instance == null) {
			_instance = new SimEngine();
		}
		return _instance;

	}

	// To erase a scheduled event, this method can be used

	public void deregister(EventHandle handle) {

		_simTimeTree.remove(handle._simSlot);
	}

	// To force a stop of the motor, even when events are still
	// present in the event list. This method can be used

	public EventHandle register(SimEnt registrator, SimEnt target, Event event,
			double delayedExecution) {
		double scheduleForTime = getTime() + delayedExecution;
		EventHandle handle = new EventHandle(registrator, target, event,
				new SimTimeSlot(scheduleForTime));
		_simTimeTree.put(handle._simSlot, handle);
		return handle;
	}

	// To empty all events in the queue and restart the engine
	// this method can be used. You however need to add a new
	// event directly otherwise the engine will stop due to no events

	public void reset() {
		_simTimeTree.clear();
		_simTime = 0;
		_quit = false;
	}

	// We can only have one engine in the simulator so this method
	// sees to that. In other words we have implemented a singleton

	public void run() {
		EventHandle handleToNextEvent = null;
		SimTimeSlot nextEventToExecute = null;

		do {
			if (_simTimeTree.size() == 0)
				_quit = true;
			else {
				nextEventToExecute = (SimTimeSlot) _simTimeTree.firstKey();
				handleToNextEvent = (EventHandle) _simTimeTree
						.get(nextEventToExecute);
				_simTime = nextEventToExecute._msek;
				handleToNextEvent._event.entering(handleToNextEvent._target);
				handleToNextEvent._target.recv(handleToNextEvent._registrator,
						handleToNextEvent._event);
				deregister(handleToNextEvent);
			}
		} while (!_quit);
		reset();
	}

	// This is the motor itself, is fetches events from the event list as long
	// as there
	// still are events present or until the stop method has been called

	public void stop() {
		_quit = true;
	}
}
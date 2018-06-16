package at.crimsonbit.nodesystem.events;

public class GClock {

	private long _tick;
	
	public GClock(long tick) {
		this._tick = tick;
	}

	public void changeTick(long tick) {
		this._tick = tick;
	}
	
	
	
}

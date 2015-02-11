package se.ltu.d7002d.SimLG;

public class Utils {
	public static void logMessage(NetworkAddr from, NetworkAddr to,
			String message) {
		if (null != to) {
			Object[] x = { new Double(SimEngine.getTime()), from.dotAddress(),
					to.dotAddress() };
			String tag = String.format("[%8.3f][%s->%s] ", x);
			System.out.println(tag + message);
		}
	}
}

package tt;

import org.zhao.common.zmq.queue.ZQueueConfig;
import org.zhao.common.zmq.queue.Zqueue;

public class t {

	public static void main(String[] args) {
		Zqueue<String> zq = ZQueueConfig.createQueue("mk");
		for (int i = 1; i <= 1000; i++) {
			zq.flush("s_"+i);
		}
		
		Zqueue<String> zq2 = ZQueueConfig.createQueue("mk");
		for (int i = 1; i <= 1000; i++) {
			System.out.println(zq.pop());
		}
	}
}

package com.theyanaga.factories;

import com.theyanaga.observers.QueueObserver;
import com.theyanaga.observers.BasicQueueObserver;

public class QueueObserverFactory {
	static QueueObserver singleton;
	public static QueueObserver getSingeton() {
		if (singleton == null) {
			singleton = new BasicQueueObserver();
			
		}
		return singleton;
	}

}

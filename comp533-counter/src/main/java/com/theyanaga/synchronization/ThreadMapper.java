package com.theyanaga.synchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadMapper {
	static Map<Thread, String> threadToRole = new HashMap();
	static Map<String, List<Thread>> roleToThreadHistory = new HashMap();
	static Map<String, List<Thread>> threadToRoleHistory = new HashMap();

	public static void map (String aRole, Thread aThread) {
		threadToRole.put(aThread, aRole);
		List<Thread> aThreads = roleToThreadHistory.get(aRole);
		if (aThreads == null) {
			aThreads = new ArrayList();
		}
		if (!aThreads.contains(aThread)) {
			aThreads.add(aThread);
		}
	}
	

}

package com.theyanaga.synchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadMapper {
	static Map<Thread, String> threadToRole = new HashMap();
	static Map<String, List<Thread>> roleToThreadHistory = new HashMap();
	static Map<Thread, List<String>> threadToRoleHistory = new HashMap();
	static Map<String, Blocker> roleToBlocker = new HashMap();
	static Blocker releaseBlocker = new BasicBlocker();

	public static Blocker getReleaseBlocker() {
		return releaseBlocker;
	}

	public static Map<String, Blocker> getRoleToBlocker() {
		return roleToBlocker;
	}

	public static void map (String aRole, Thread aThread) {
		threadToRole.put(aThread, aRole);
		List<Thread> aThreads = roleToThreadHistory.get(aRole);
		if (aThreads == null) {
			aThreads = new ArrayList();
			roleToThreadHistory.put(aRole, aThreads);
		}
		if (!aThreads.contains(aThread)) {
			aThreads.add(aThread);
			if (aThreads.size() > 1) {
				System.out.println("Different server threads:" +aThreads + " assigned to same client thread:" + aRole);
			}
		}
		List<String> aRoles = threadToRoleHistory.get(aThread);
		if (aRoles == null) {
			aRoles = new ArrayList();
			threadToRoleHistory.put(aThread, aRoles);
		}
		if (!aRoles.contains(aRole)) {
			aRoles.add(aRole);
			if (aRoles.size() > 1) {
				System.out.println("Different client threads:" +aRoles + " assigned to same server thread:" + aThread);
			}
		}
		Blocker aBlocker = roleToBlocker.get(aRole);
		if (aBlocker == null) {
			aBlocker = new BasicBlocker();
			roleToBlocker.put(aRole, aBlocker);
		}
	}

	public static Map<Thread, String> getThreadToRole() {
		return threadToRole;
	}

	public static Map<String, List<Thread>> getRoleToThreadHistory() {
		return roleToThreadHistory;
	}

	public static Map<Thread, List<String>> getThreadToRoleHistory() {
		return threadToRoleHistory;
	}
	
	public static void printCurrentRoles() {
		System.out.println(roleToBlocker.keySet());
	}
	
	public static void printThreadRoleHistory() {
		for (String aRole:roleToThreadHistory.keySet()) {
			System.out.println (aRole + ":" + roleToThreadHistory.get(aRole));
		}
	}
	
	public static void printMonitorOccupent() {
		System.out.print("Monitor occupant:");

		if (!releaseBlocker.hasBlocked()) {
			System.out.println("None");
		} else {
			Thread aMonitorThread = releaseBlocker.getBlockedThread();
			String aRole = threadToRole.get(aMonitorThread);
			System.out.println(aRole);
		}
	}
	

}

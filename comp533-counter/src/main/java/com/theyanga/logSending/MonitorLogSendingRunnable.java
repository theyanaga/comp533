package com.theyanga.logSending;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;


public class MonitorLogSendingRunnable implements Runnable, WindowListener {
	
	ArrayBlockingQueue<MonitorSendingData> logQueue = new ArrayBlockingQueue<>(100);
	private static MonitorLogSendingRunnable instance;
	public static MonitorLogSendingRunnable getInstance() {
		if (instance == null) {
			instance = new MonitorLogSendingRunnable(); 
		}
		return instance;
	}
	private MonitorLogSendingRunnable() {
//		instance = this;
	}
//	public void addToQueue(MonitorSendingData log) {
//		logQueue.add(log);
//	}
//	
////	public void addToQueue(boolean isTests, String aLogFileName, String log, String assignment, int intr) {
////		logQueue.add(new SendingData(isTests, aLogFileName, log,assignment,intr));
////	}
//	
//	public void addToQueue(MonitorLogEntryKind aLogEntryKind, String aLogFileName, String log, String assignment, int intr) {
//		logQueue.add(new MonitorSendingData(aLogEntryKind, aLogFileName, log,assignment,intr));
//	}
	
	private boolean end=false;
	public synchronized void endProcess(boolean b) {
		end=b;
		if(logQueue.isEmpty())
			logQueue.add(null);
	}
	
	@Override
	public void run() {
			for(;;) 
				try {
					Thread.sleep(getTimeDelay());				
					
					if(end)
						break;
					MonitorLogSender.sendToServer();
//					System.out.println("Log Sent " + System.currentTimeMillis());
				}  catch (Exception e) {
//					  System.err.println("Error sending log: "+e.getMessage());
				}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		MonitorLogSender.appendStatistics();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		MonitorLogSender.appendStatistics();

		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
//	private String assignment;
//	
//	public String getAssignment() {
//		return assignment;
//	}
//	public void setAssignment(String newVal) {
//		assignment = newVal;
//	}
//
//	private String logId;	
//	public synchronized  String getLogId() { 
//		return logId;
//	}	
//	public synchronized  void setLogId(String newVal) { 
//		 logId = newVal;
//	}
//	
//	private Path fileToSend;
//
//	public synchronized  Path getFileToSend() { 
//		return fileToSend;
//	}
//
//	public synchronized void setFileToSend(Path newVal) {
//		fileToSend = newVal;
//	}
	
	private long timeDelay = 5000;
	public synchronized  long getTimeDelay() { 
		return timeDelay;
	}

	public synchronized void setTimeDelay(long newVal) {
		timeDelay = newVal;
	}
	
	
	public static Thread runInThread() {
		Thread retVal = new Thread(getInstance());
		retVal.setName("Log Sending Runnable");
		retVal.start();
		return retVal;
	}
	
	
	

}

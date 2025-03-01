package com.theyanga.logSending;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.Calendar;


public class MonitorLogSender {
	
	private static long totalLogSizeSent = 0;
	private static long totalTimeTaken = 0;
	private static long totalSends = 0;
	private static final String TIME_STATISTICS_FILE_NAME = "timeStatistics.csv";
	private static final String reportURL=
			"https://us-east-1.aws.data.mongodb-api.com/app/rest-api-vsfoo/endpoint/add_log?db=studies&collection=dewan-localchecks";
	private static final String password = "sYCUBa*shZKU4F-yxHrTk8D7FHo4xbBBV.-BK!-L";
//	private static final String reportURL="https://us-south.functions.appdomain.cloud/api/v1/web/ORG-UNC-dist-seed-james_dev/cyverse/add-cyverse-log";
	private static String lastLogFilePath = null;
	
	private static File lastLogDirectory = null;
	
//	private static final String uuidFile="LogsUUID.txt";
//	private static final File fileStore;
//	
//	static {
//		File searchLoc = new File(System.getProperty("user.home")+"/helper-config/");
//		if(searchLoc.exists())
//			fileStore=new File(System.getProperty("user.home")+"/helper-config/"+uuidFile);
//		else
//			fileStore=new File("./Logs/LocalChecks/"+uuidFile);
//	}
	public static void appendStatistics()  {
		if (totalSends > 0) {
		appendStatistics(totalSends + "," + totalLogSizeSent + "," + totalTimeTaken);
		}
	}

	public static void appendStatistics(final String aStats)  {
		PrintWriter out = null;
		try {
		if (lastLogDirectory == null) {
			return;
		}
		File aStatsFile = new File(lastLogDirectory, TIME_STATISTICS_FILE_NAME);
		
		
		    out = new PrintWriter(new BufferedWriter(new FileWriter(aStatsFile, true)));
		    out.println(aStats);
		} catch (IOException e) {
		    System.err.println(e);
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}
	
	public static void sendToServer(MonitorSendingData sd) throws Exception {
//		sendToServer(sd.isTests(), sd.getLogFileName(), sd.getLog(),sd.getAssignment(),sd.getIteration());
		sendToServer(sd.getLogEntryKind(), sd.getLogFileName(), sd.getLog(),sd.getAssignment(),sd.getIteration());

	}
	
	private static void maybeUpdateLogDirectory(String aLogFilePath) {
		if (aLogFilePath.equals(lastLogFilePath)) {
			return;
		}
		File aLogFile = new File(aLogFilePath);
		lastLogFilePath = aLogFilePath;
		lastLogDirectory = aLogFile.getParentFile();
	}
	
	
	
	public static void sendToServer() {
		Path aFileToSendPath = getFileToSend();
		if (aFileToSendPath == null) {
			return;
		}
		maybeReadLastLineOfLogFile(getLogFileName());
		File aFiletoSend = aFileToSendPath.toFile();
		MonitorLogEntryKind aLogEntryKind = MonitorLogEntryKind.SOURCE;
		String aFileToSendName = aFileToSendPath.toString();
		String aFileToSendText = toText(aFiletoSend);
		maybeLoadSavedSets();
		int aSessionId = numTotalRuns;
		String anAssignment = getAssignment();
		try {
			sendToServer(aLogEntryKind, aFileToSendName, aFileToSendText, anAssignment, aSessionId);
			setFileToSend(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
//	public static void sendToServer(boolean anIsTests, String aLogFilePath, String log, String assignment, int sessionId) throws Exception{		
	public static void sendToServer(MonitorLogEntryKind aLogEntryKind, String aLogFilePath, String log, String assignment, int sessionId) throws Exception{		
	
//		if (anIsTests) {
		if (aLogEntryKind == MonitorLogEntryKind.TEST) {
			maybeUpdateLogDirectory(aLogFilePath);
		}
		File aFile = new File(aLogFilePath);
		long aStartTime = System.currentTimeMillis();
		MonitorJSONObject message = new MonitorJSONObject();
		String aLogId = aLogEntryKind + " " + LogNameManager.getLoggedName()+" "+ " "+sessionId + " " + System.currentTimeMillis()+ " " + aFile.getName();

//		String aLogId = anIsTests + " " + LogNameManager.getLoggedName()+" "+ " "+sessionId + " " + System.currentTimeMillis()+ " " + aFile.getName();
//		message.put("log_id",System.currentTimeMillis()+"-"+sessionId);
		message.put("log_id",aLogId);

		message.put("session_id",Integer.toString(sessionId));
		message.put("machine_id",LogNameManager.getLoggedName());

//		message.put("machine_id",getHashMachineId());
		message.put("log_type","LocalChecksLog");
		message.put("course_id",assignment);
		message.put("password",password);
		//message.put("course_id",determineSemester());
//		String BS = "\\\\\\";
//		if (aLogEntryKind != LogEntryKind.SCHEMA) {
		String BS = " B*S ";
		log = log.replaceAll("\n",  BS + "n");
		log = log.replaceAll("\r", BS + "r");
		log = log.replaceAll("	", BS + "t");
		log = log.replaceAll("\t", BS + "t");
		log = log.replaceAll("\f", BS + "f");
		log = log.replaceAll("\"",  BS + "q");
		log = log.replaceAll("\\=", BS + "=" );
		log = log.replaceAll("\\-", BS + "-" );
		log = log.replaceAll("\\+", BS + "+" );
//		}

//		log = log.replaceAll("\\", "BSBS");


		

//		log = log.replaceAll("\\", "");
		MonitorJSONObject logJSON = new MonitorJSONObject();
//		log = JSON
		logJSON.put("json", log);
		

		
		message.put("log", logJSON);
		if (log.length() == 0) {
			return;
		}
//		System.out.println("Posting message:" +message );
		MonitorJSONObject ret = post(message,reportURL);
//		System.out.println("Return value from post:" + ret);
		if(ret==null) {
			Thread.sleep(5000);
			post(message,reportURL);
		}
		long anEndTime = System.currentTimeMillis();
		long aSendTime = anEndTime - aStartTime;
		totalSends++;
		totalLogSizeSent += log.length();
		totalTimeTaken += aSendTime;
		
	}
	

	
	@SuppressWarnings("unused")
	private static String determineSemester() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		Calendar compare = Calendar.getInstance();
		compare.set(year, Calendar.MAY, 5);
		if(c.before(compare))
			return "Spring"+year;
		compare.set(year, Calendar.JUNE, 20);
		if(c.before(compare))
			return "SummerI"+year;
		compare.set(year, Calendar.AUGUST, 5);
		if(c.before(compare))
			return "SummerII"+year;
		return "Fall"+year;
	}
	static boolean logErrorMessageSent = false;
	public static MonitorJSONObject post(MonitorJSONObject request, String urlString) {
		BufferedReader reader;
		String line;
		StringBuffer sb = new StringBuffer();
		int status = 500;
		MonitorJSONObject body = new MonitorJSONObject();
		try {
			body.put("body", request);
		} catch (Exception e1) {
			System.err.println(e1.getMessage());
//			e1.printStackTrace();
		}

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Length", (body.toString().length()+2)+"");
			OutputStream os = conn.getOutputStream();
			byte[] input = body.toString().getBytes();
//			System.out.println(body.toString(4));
			os.write(input, 0, input.length);
			os.write("\r\n".getBytes());
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			status = conn.getResponseCode();
			
			if (status > 299) {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
			} else {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				reader.close();
			}
			conn.disconnect();
		} catch (Exception e) {
			if (!logErrorMessageSent) {
			System.err.println("Error sending logs:\n"+e.getMessage());
			logErrorMessageSent = true;
			}
			return null;
//			e.printStackTrace();
		} 
		try {
			return new MonitorJSONObject();
		} catch (Exception e) {
		}
		return null;
	}
	
private static String assignment;
	
	public static String getAssignment() {
		return assignment;
	}
	public static void setAssignment(String newVal) {
		assignment = newVal;
	}

//	private static String logFileName =	"Logs/LocalChecks/comp533s25_assignment02_S25Assignment0_2SuiteWithHintsFineGrained.csv";
	private static String logFileName;

//	private static String assignment = "comp533s25_assignment02_S25Assignment0_2SuiteWithHints";
	public static synchronized  String getLogFileName() { 
		return logFileName;
	}	
	public static synchronized  void setLogFileName(String newVal) { 
		logFileName = newVal;
		int anIndexOfSlash = logFileName.lastIndexOf('/');
		if (anIndexOfSlash != -1) {
			int anIndexOfDot = logFileName.indexOf('.');
			if (anIndexOfDot != -1) {
			assignment = logFileName.substring(anIndexOfSlash + 1, anIndexOfDot);
			}
		}
	}
	
	private static Path fileToSend;

	public static synchronized  Path getFileToSend() { 
		return fileToSend;
	}

	public static synchronized void setFileToSend(Path newVal) {
		fileToSend = newVal;
	}
	

	static public String toText(File f) {
		if (f == null) {
			// System.out.println();
			return "Source of class not in main directory";
		}
		StringBuffer sb = new StringBuffer();
		try {
			DataInputStream dataIn = new DataInputStream(new FileInputStream(f));
			for (;;) {
				String nextLine = dataIn.readLine();
				if (nextLine == null)
					break;
				// System.out.println("new line" + nextLine);
				// sb.append(nextLine+'\n');
				append(sb, nextLine);
				sb.append("\n");
			}
			dataIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	static public void append(StringBuffer sb, String s) {
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '\t') {
				sb.append("    ");
			} else
				sb.append(s.charAt(i));
	}
	static String lastLine, lastLineNormalized;
	static String[] normalizedLastLines = null;

	
	// Logs/LocalChecks/comp533s25_assignment02_S25Assignment0_2SuiteWithHintsFineGrained.csv

	public static boolean maybeReadLastLineOfLogFile(String aLogFileName) {
		if (lastLine != null) {
			return true;
		}
		try {
		File aFile = new File(aLogFileName);
		if (!aFile.exists()) {
			return true;
		}
		lastLine = tail(aFile, 1).trim();
		if (lastLine.startsWith("#")) {
			System.err.println ("Corrupt log file " + aLogFileName + " has only header");
			System.out.println("Deleting file:" + aLogFileName);
			aFile.delete();
			return false;
		}
//		String lastLineNormalized = lastLine.replaceAll("+|-", ""); // normalize it
		lastLineNormalized = lastLine.replaceAll("\\+|-", ""); // normalize it
		normalizedLastLines = lastLineNormalized.split(",");
		
		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static final int RUN_INDEX = 0; 
	static  int numTotalRuns = 0;

	protected static void maybeLoadSavedSets() {
		String aRunsString = normalizedLastLines[RUN_INDEX];

		numTotalRuns = Integer.parseInt(aRunsString) + 1;
		
	}
	public static String tail( File file, int lines) {
		if (file == null) {
			System.err.println("Null log file returning empty string");
			return "";
		}
	    java.io.RandomAccessFile fileHandler = null;
	    try {
	        fileHandler = 
	            new java.io.RandomAccessFile( file, "r" );
	        long fileLength = fileHandler.length() - 1;
	        StringBuilder sb = new StringBuilder();
	        int line = 0;

	        for(long filePointer = fileLength; filePointer != -1; filePointer--){
	            fileHandler.seek( filePointer );
	            int readByte = fileHandler.readByte();

	             if( readByte == 0xA ) {
	                if (filePointer < fileLength) {
	                    line = line + 1;
	                }
	            } else if( readByte == 0xD ) {
	                if (filePointer < fileLength-1) {
	                    line = line + 1;
	                }
	            }
	            if (line >= lines) {
	                break;
	            }
	            sb.append( ( char ) readByte );
	        }

	        String lastLine = sb.reverse().toString();
	        return lastLine;
	    } catch( java.io.FileNotFoundException e ) {
	    	System.err.println(e);
//	        e.printStackTrace();
	        return null;
	    } catch( java.io.IOException e ) {
	    	System.err.println(e);
//	        e.printStackTrace();
	        return null;
	    }
	    finally {
	        if (fileHandler != null )
	            try {
	                fileHandler.close();
	            } catch (IOException e) {
	            }
	    }
	}
}

package domain;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class ChessLogger {

	private static final String FILE_NAME = "chesslog.txt";

	private static ChessLogger instance = null;

	private ChessLogger() {
		// Empty by design
	}

	public static ChessLogger getInstance() {
		if (instance == null) {
			instance = new ChessLogger();
			instance.createNewChessLog();
		}
		return instance;
	}

	private void createNewChessLog() {
		try {
			FileWriter fw = new FileWriter(FILE_NAME, false);
			doTheWriting(fw, "");
		}
		catch (Exception ex) {
			System.out.println("WRONG " + ex.toString());
		}
	}

	public void appendToChessLog(StringBuilder log) {
		try {
			log.append("\n");
			FileWriter fw = new FileWriter(FILE_NAME, true);
			doTheWriting(fw, log.toString());
		}
		catch (Exception ex) {
			System.out.println("WRONG " + ex.toString());
		}
	}

	public void appendToChessLog(String log) {
		try {
			log = log + "\n";
			FileWriter fw = new FileWriter(FILE_NAME, true);
			doTheWriting(fw, log);
		}
		catch (Exception ex) {
			System.out.println("WRONG " + ex.toString());
		}
	}

	private void doTheWriting(FileWriter theWriter, String log) {
		try {
			BufferedWriter bw = new BufferedWriter(theWriter);
			bw.write(log);
			bw.close();
		}
		catch (Exception ex) {
			System.out.println("WRONG " + ex.toString());
		}
	}
}

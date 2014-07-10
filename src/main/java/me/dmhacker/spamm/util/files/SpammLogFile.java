package me.dmhacker.spamm.util.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpammLogFile {


	private String header;
	private File file;
	private List<String> log;
	
	public SpammLogFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		this.file = file;
		this.header = "[Spamm] ";
		this.log = new ArrayList<String>();
	}
	
	public void addLines(List<String> lines) {
		this.log.addAll(lines);
		save();
	}
	
	public void addLine(String line) {
		this.log.add(line);
		save();
	}
	
	public void removeLine(int index) {
		this.log.remove(index);
		save();
	}
	
	public void setLine(int index, String line) {
		this.log.set(index, line);
		save();
	}
	
	public void insertLine(int index, String line) {
		this.log.add(index, line);
		save();
	}
	
	public void insertLines(int index, List<String> lines) {
		this.log.addAll(index, lines);
		save();
	}
	
	public void save(){
		try {
			FileWriter filewriter = new FileWriter(file.getAbsolutePath());
			BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
			for (String l : log) {
				bufferedwriter.write(header+l);
				bufferedwriter.newLine();
			}
			bufferedwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(){
		try {
			FileReader filereader = new FileReader(file.getAbsolutePath());
			BufferedReader bufferedreader = new BufferedReader(filereader);
			bufferedreader.readLine();
			
			log = new ArrayList<String>();
			String line = "";
			
			while ((line = bufferedreader.readLine()) != null) {
				log.add(line);
			}
			bufferedreader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public File getFile(){
		return file;
	}
	
	public List<String> getLog(){
		List<String> clonelog = new ArrayList<String>();
		clonelog.addAll(log);
		return clonelog;
	}
}

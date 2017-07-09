package app.ReadWriteFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class File {
	
	private final String filename;
	
	public File(String filename){
		this.filename=filename;
	}

	public List<String> readFileList(){
		List<String> lines = new ArrayList<String>();
		try{

			FileReader fr = new FileReader(this.getFileName());
			BufferedReader br = new BufferedReader(fr);
			lines=br.lines().collect(Collectors.toList());
			br.close();
			if(lines.size()!=0){
				return lines;
			} 
			lines.add("This File is Empty");
			return lines;
		} catch(IOException e) {
			lines.add(e.getMessage());
			return lines;
		}

	}
	

	public String readFile(){
		try{

			FileReader fr = new FileReader(this.getFileName());
			BufferedReader br = new BufferedReader(fr);
			String line=br.readLine();
			br.close();
			if(line.length()!=0){
				return line;
			} else {
			line=line+"This File is Empty";
			}
			return line;
		} catch(IOException e) {
			return e.getMessage();
		}

	}
	
//	public int readFileSpy(){
//		String contents = this.readFile();
//		
//		if(contents!=null){
//			return 1;
//		}
//		return 0;
//		
//	}
	
	
	public void writeFile(String input,boolean append){
			try{
				
				FileWriter fw = new FileWriter(this.getFileName(),append);
				BufferedWriter bw = new BufferedWriter(fw);
				
				bw.write(input);
				bw.close();
				
				System.out.println("Done");
				} catch(IOException e) {
					e.printStackTrace();
				}
	}
//	
//	public int WriteFileSpy(String input){
//		String contents = this.readFile();
//		
//		if(contents!=null){
//			return 1;
//		}
//		return 0;
//		
//	}
	
	public String getFileName(){
		return this.filename;
	}
	
	

}

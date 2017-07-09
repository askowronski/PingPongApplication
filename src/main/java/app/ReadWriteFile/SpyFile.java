package app.ReadWriteFile;

import app.ReadWriteFile.File;

public class SpyFile extends File {
	
	public boolean check;
	
	public SpyFile(String FileName){
		super(FileName);
		this.check=false;
	}
	
	public boolean spyReadFile(){
		String contents=this.readFile();
		
		if(contents!=null){
		this.setCheck(true);
		return true;
		}
		return false;
	}
	
	public void setCheck(boolean result){
		this.check=result;
	}

}

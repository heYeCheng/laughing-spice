package newClaw;

public class proxyState {
	public boolean proFlag1=true;
	public boolean proFlag2=true;
	public int currentPro=1;
	
	public void setFlag1(boolean value){
		this.proFlag1=value;
	}
	
	public void setFlag2(boolean value){
		this.proFlag2=value;
	}
	
    public void setProxy(int currentPro){
		this.currentPro=currentPro;
	}
    
    public boolean getFlag1(){
    	return this.proFlag1;
    }
    
    public boolean getFlag2(){
    	return this.proFlag2;
    }
    
    public int getProxy(){
    	return this.currentPro;
    }
}

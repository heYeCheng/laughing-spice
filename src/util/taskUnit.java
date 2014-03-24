package util;

/**
 * 分发任务的包
 * @author Hery
 *
 */
public class taskUnit implements java.io.Serializable {  
    private static final long serialVersionUID = 1L;
    private int pack_id;
    /*存储城市id与计数列表*/
    private int id_counts[][];     
    public taskUnit(int pack_id) {  
        this.pack_id=pack_id;
    }
    public taskUnit(int size,int pack_id) {
    	this.pack_id=pack_id;
        this.id_counts=new int[size][2];
    }
    public taskUnit(int pack_id,int[][] id_counts) {
    	this.pack_id=pack_id;
        this.id_counts=id_counts;
    }
	public int[][] getId_counts() {
		return id_counts;
	}
	public void setId_counts(int[][] id_counts) {
		this.id_counts = id_counts;
	}  	
}  
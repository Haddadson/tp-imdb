import java.util.LinkedList;

public class TabelaHash {
     
	public TabelaHash() {
		SIZE = 8000;
		hashtable = (LinkedList<EntidadePalavrasHash>[])new LinkedList[SIZE];
	}
	
	public TabelaHash(int tam) {
		SIZE = tam;
		hashtable = (LinkedList<EntidadePalavrasHash>[])new LinkedList[SIZE];
	}
	
    private int SIZE;
     
    private LinkedList<EntidadePalavrasHash>[] hashtable;
    
    public int getSIZE() {
		return SIZE;
	}

	public void setSIZE(int size) {
		SIZE = size;
	}

	public LinkedList<EntidadePalavrasHash>[] getHashtable() {
		return hashtable;
	}

	public void setHashtable(LinkedList<EntidadePalavrasHash>[] hashtable) {
		this.hashtable = hashtable;
	}

	public void add(EntidadePalavrasHash value) {
        int hash = hash(value.getPalavra());
        if (hashtable[hash] == null) {
            hashtable[hash] = new LinkedList<>();
        }
        LinkedList<EntidadePalavrasHash> bucket = hashtable[hash];
        bucket.add(value);
    }

    public int hash(String value) {
        int hash = value.hashCode();
        return hash % SIZE * (hash >= 0 ? 1 : -1);
    }
}
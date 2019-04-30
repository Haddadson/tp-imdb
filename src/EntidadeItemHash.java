import java.util.LinkedList;

public class EntidadeItemHash {
	private int hashCode;
	private LinkedList<EntidadePalavrasHash> palavras;
	
	public EntidadeItemHash() {
		palavras = new LinkedList<EntidadePalavrasHash>();
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}

	public LinkedList<EntidadePalavrasHash> getPalavras() {
		return palavras;
	}

	public void setPalavras(LinkedList<EntidadePalavrasHash> palavras) {
		this.palavras = palavras;
	}
	public void adicionarPalavraEmLista(EntidadePalavrasHash palavra) {
		this.palavras.add(palavra);
	}
}

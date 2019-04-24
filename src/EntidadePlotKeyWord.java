import java.util.ArrayList;

public class EntidadePlotKeyWord {
	private String palavra;
	private ArrayList<Integer> registrosEmQueAparece;
	
	public EntidadePlotKeyWord() {
		registrosEmQueAparece = new ArrayList<Integer>();
	}
	
	public String getPalavra() {
		return palavra;
	}
	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}
	public ArrayList<Integer> getRegistrosEmQueAparece() {
		return registrosEmQueAparece;
	}
	public void setRegistrosEmQueAparece(ArrayList<Integer> registrosEmQueAparece) {
		this.registrosEmQueAparece = registrosEmQueAparece;
	}	
	
	public void adicionarRegistroEmQuePalavraApareceu(int registro) {
		this.registrosEmQueAparece.add(registro);
	}
}

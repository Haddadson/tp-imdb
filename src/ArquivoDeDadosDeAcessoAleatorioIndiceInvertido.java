import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArquivoDeDadosDeAcessoAleatorioIndiceInvertido {

	private RandomAccessFile file;
	private int numReg;
	private int tamReg;
	private int tamHead;
	private static final int STRING_MAX_TAM = 100;

	public ArquivoDeDadosDeAcessoAleatorioIndiceInvertido() {
		this.file = null;
		this.numReg = -1; // número de registro (-1: não há registros)
		this.tamReg = (Integer.SIZE / 8) + STRING_MAX_TAM + ((Integer.SIZE / 8) * 200);
		this.tamHead = 4;
	}

	public int getTamReg() {
		return tamReg;
	}

	public int getTamHead() {
		return tamHead;
	}

	public void openFile(String path) {
		File f = new File(path);
		// se arquivo no existe definie numReg como 0. Se o arquivo já existe, mantenha
		// como -1
		if (!f.exists()) {
			try {
				f.createNewFile();
				this.numReg = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			file = new RandomAccessFile(f, "rw");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		// se numReg é igual a -1, há registros dentro do arquivo, então verifica a
		// quantidade de registros
		if (this.numReg == -1)
			this.setNumReg();
	}

	private void setNumReg() {
		try {
			this.numReg = file.readInt();
		} catch (IOException e) {
			System.out.println("Error!");
			System.exit(0);
		}
	}

	public int getNumReg() {
		return numReg;
	}

	public void openFileReadOnly(String path) {
		try {
			file = new RandomAccessFile(new File(path), "r");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		this.setNumReg();
	}

	public void setData(EntidadePlotKeyWord plotKeyWord) {
		int pos = this.tamHead + (this.numReg * this.tamReg);

		// calcula ponteiro para a primeira posição vazia do arquivo
		try {
			file.seek(pos);
			file.writeInt(this.numReg);
			file.writeUTF(plotKeyWord.getPalavra());
			file.seek(pos + STRING_MAX_TAM);
			for (int i = 0; i <= 200; i++) {
				if (i < plotKeyWord.getRegistrosEmQueAparece().size()) {
					file.writeInt(plotKeyWord.getRegistrosEmQueAparece().get(i));
					//System.out.println("Gravando em = " + plotKeyWord.getRegistrosEmQueAparece().get(i));
				}

				else
					file.writeInt(-1);
			}

			file.seek(0);
			this.numReg += 1;
			file.writeInt(this.numReg);
			//System.out.println("Gravou em: " + (this.tamHead + (this.tamReg * this.numReg)));
		} catch (IOException e) {
			System.out.println("Error - Escrita Indice Invertido");
			System.exit(0);
		}
	}

	public EntidadePlotKeyWord getData(int key) {

		if (key >= this.numReg)
			return null;

		//System.out.println("get data");

		int pos = this.tamHead + (key * this.tamReg);

		EntidadePlotKeyWord plotKeyWord = new EntidadePlotKeyWord();

		try {

			//System.out.println("Entrou no try do get data. Valor de pos: " + pos);
			file.seek(pos);
			plotKeyWord.setId(file.readInt());
			plotKeyWord.setPalavra(file.readUTF());
			file.seek(pos + STRING_MAX_TAM);
			for (int i = 0; i < 200; i++) {
				int registro = file.readInt();
				if (registro != -1) {
					plotKeyWord.adicionarRegistroEmQuePalavraApareceu(registro);
				}
			}
			file.seek(0);

		} catch (IOException e) {
			System.out.println("Error - Leitura Indice Invertido");
			System.exit(0);
		}

		return plotKeyWord;
	}

	public void closeFile(String path) {
		try {
			file.close();
		} catch (IOException e) {
			System.out.println("Error");
			System.exit(0);
		}
	}
}

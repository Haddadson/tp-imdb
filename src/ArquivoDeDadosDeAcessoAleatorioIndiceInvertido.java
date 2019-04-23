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
		this.tamReg = STRING_MAX_TAM;
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

	public void openFileReadOnly(String path) {
		try {
			file = new RandomAccessFile(new File(path), "r");
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		this.setNumReg();
	}

	public int getNumReg() {
		return numReg;
	}

	public void setData(String palavra) {
		int pos = this.tamHead + (this.numReg * this.tamReg);

		// calcula ponteiro para a primeira posição vazia do arquivo
		try {
			file.seek(pos);
			file.writeUTF(palavra);
			file.seek(pos + STRING_MAX_TAM);
			file.seek(0);
			this.numReg += 1;
			file.writeInt(this.numReg);
			System.out.println("Gravou em: " + (this.tamHead + (this.tamReg * this.numReg)));
		} catch (IOException e) {
			System.out.println("Error!");
			System.exit(0);
		}
	}

	public String getData(int key) {

		if (key >= this.numReg)
			return null;

		System.out.println("get data");

		int pos = this.tamHead + (key * this.tamReg);

		String palavra = null;

		try {

			System.out.println("Entrou no try do get data. Valor de pos: " + pos);
			file.seek(pos);
			palavra = file.readUTF();
			file.seek(pos + STRING_MAX_TAM);
		
			file.seek(0);

		} catch (IOException e) {
			System.out.println("Error");
			System.exit(0);
		}

		return palavra;
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

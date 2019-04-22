import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;

public class ArquivoDeDadosDeAcessoAleatorioFilmes {

	private RandomAccessFile file;
	private int numReg;
	private int tamReg;
	private int tamHead;
	private static final int STRING_MAX_TAM = 100;

	public ArquivoDeDadosDeAcessoAleatorioFilmes() {
		this.file = null;
		this.numReg = -1; // número de registro (-1: não há registros)
		this.tamReg = ((Integer.SIZE / 8) * 10) + ((Long.SIZE / 8) * 4) + (STRING_MAX_TAM * 12) + ((Float.SIZE / 8) * 2);
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

	public void setData(EntidadeFilme a) {
		int pos = this.tamHead + (this.numReg * this.tamReg);
		
		// calcula ponteiro para a primeira posição vazia do arquivo
		try {
			file.seek(pos);
			 file.writeUTF(a.getColor()); 
		      file.seek(pos + STRING_MAX_TAM); 
		      file.writeUTF(a.getDirectorName()); 
		      file.seek(pos + (STRING_MAX_TAM * 2)); 
		      file.writeInt(a.getNumCriticForReviews());
		      file.writeInt(a.getDuration());
		      file.writeInt(a.getDirectorFacebookLikes());
		      file.writeInt(a.getActor3FacebookLikes());
		      file.writeUTF(a.getActor2Name()); 
		      file.seek(pos + (STRING_MAX_TAM * 3) + ((Integer.SIZE / 8) * 4));
		      file.writeInt(a.getActor1FacebookLikes());
		      file.writeLong(a.getGross());
		      file.writeUTF(a.getGenres()); 
		      file.seek(pos + (STRING_MAX_TAM * 4) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8)); 
		      file.writeUTF(a.getActor1Name());
		      file.seek(pos + (STRING_MAX_TAM * 5) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8));
		      file.writeUTF(a.getMovieTitle());
		      file.seek(pos + (STRING_MAX_TAM * 6) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8));
		      file.writeLong(a.getNumVotedUsers());
		      file.writeLong(a.getCastTotalFacebookLikes());
		      file.writeUTF(a.getActor3Name());
		      file.seek(pos + (STRING_MAX_TAM * 7) + ((Integer.SIZE / 8) * 5) + ((Long.SIZE / 8) * 3));
		      file.writeInt(a.getFaceNumberInPoster());
		      file.writeUTF(a.getPlotKeywords());
		      file.seek(pos + (STRING_MAX_TAM * 8) + ((Integer.SIZE / 8) * 6) + ((Long.SIZE / 8) * 3));
		      file.writeUTF(a.getMovieImdbLink());
		      file.seek(pos + (STRING_MAX_TAM * 9) + ((Integer.SIZE / 8) * 6) + ((Long.SIZE / 8) * 3));
		      file.writeInt(a.getNumUserForReviews());
		      file.writeUTF(a.getLanguage());
		      file.seek(pos + (STRING_MAX_TAM * 10) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3)); 
		      file.writeUTF(a.getCountry());
		      file.seek(pos + (STRING_MAX_TAM * 11) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3));
		      file.writeUTF(a.getContentRating());
		      file.seek(pos + (STRING_MAX_TAM * 12) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3));
		      file.writeLong(a.getBudget());
		      file.writeInt(a.getTitleYear());
		      file.writeInt(a.getActor2FacebookLikes());
		      file.writeFloat(a.getImdbScore());
		      file.writeFloat(a.getAspectRatio());
		      file.writeInt(a.getMovieFacebookLikes());

			file.seek(0);
			this.numReg += 1;
			file.writeInt(this.numReg);
			System.out.println("Gravou em: " + (this.tamHead + (this.tamReg * this.numReg)));
		} catch (IOException e) {
			System.out.println("Error!");
			System.exit(0);
		}
	}

	public EntidadeFilme getData(int key) {

		if (key >= this.numReg)
			return null;
		
		System.out.println("get data");

		int pos = this.tamHead + (key * this.tamReg);

		EntidadeFilme p = new EntidadeFilme();

		try {
			
			System.out.println("Entrou no try do get data. Valor de pos: " + pos);
			file.seek(pos);
			
			  p.setColor(file.readUTF());
		      file.seek(pos + STRING_MAX_TAM); 
		      p.setDirectorName(file.readUTF()); 
		      file.seek(pos + STRING_MAX_TAM * 2); 
		      p.setNumCriticForReviews(file.readInt());
		      p.setDuration(file.readInt());
		      p.setDirectorFacebookLikes(file.readInt());
		      p.setActor3FacebookLikes(file.readInt());
		      p.setActor2Name(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 3) + ((Integer.SIZE / 8) * 4));
		      p.setActor1FacebookLikes(file.readInt());
		      p.setGross(file.readLong());
		      p.setGenres(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 4) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8));
		      p.setActor1Name(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 5) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8));
		      p.setMovieTitle(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 6) + ((Integer.SIZE / 8) * 5) + (Long.SIZE / 8));
		      p.setNumVotedUsers(file.readLong());
		      p.setCastTotalFacebookLikes(file.readLong());
		      p.setActor3Name(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 7) + ((Integer.SIZE / 8) * 5) + ((Long.SIZE / 8) * 3)); 
		      p.setFaceNumberInPoster(file.readInt());
		      p.setPlotKeywords(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 8) + ((Integer.SIZE / 8) * 6) + ((Long.SIZE / 8) * 3));
		      p.setMovieImdbLink(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 9) + ((Integer.SIZE / 8) * 6) + ((Long.SIZE / 8) * 3));
		      p.setNumUserForReviews(file.readInt());
		      p.setLanguage(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 10) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3));;
		      p.setCountry(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 11) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3));
		      p.setContentRating(file.readUTF());
		      file.seek(pos + (STRING_MAX_TAM * 12) + ((Integer.SIZE / 8) * 7) + ((Long.SIZE / 8) * 3));
		      p.setBudget(file.readLong());
		      p.setTitleYear(file.readInt());
		      p.setActor2FacebookLikes(file.readInt());
		      p.setImdbScore(file.readFloat());
		      p.setAspectRatio(file.readFloat());
		      p.setMovieFacebookLikes(file.readInt());
	
		      file.seek(0);
			
		} catch (IOException e) {
			System.out.println("Error");
			System.exit(0);
		}

		return p;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Program {

	public static void main(String[] args) {
		FileReader arq;
		try {
			ArquivoDeDadosDeAcessoAleatorioFilmes arqReader = new ArquivoDeDadosDeAcessoAleatorioFilmes();
			arq = new FileReader("src/movie_metadata.csv");
			arqReader.openFile("src/dados_filmes.bin");
			BufferedReader leitor = new BufferedReader(arq);
			String linha = leitor.readLine();
			int iteracao = 0;

			

			if(arqReader.getNumReg() == 0) {
				while(linha != null && !linha.isEmpty() && iteracao <= 40) {
					linha = leitor.readLine();
					String[] propriedadesObjeto = linha.split(",");
					EntidadeFilme filme = mapearObjeto(propriedadesObjeto);
					if (filme != null)
						arqReader.setData(filme);
					iteracao++;
					System.out.println(iteracao);
				}	
			}
			
			 ArrayList<EntidadeFilme> listaFilmes = LerDadosFilmesImdb(arqReader);
			
			leitor.close();
			arqReader.closeFile("src/dados_filmes.bin");
			for (EntidadeFilme filmeImdb : listaFilmes) {
				System.out.println(filmeImdb.getMovieTitle());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private static EntidadeFilme mapearObjeto(String[] propriedadesObjeto) {
		EntidadeFilme filme = new EntidadeFilme();
		try {
			filme.setColor(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[0]));
			filme.setDirectorName(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[1]));
			filme.setNumCriticForReviews(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[2]));
			filme.setDuration(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[3]));
			filme.setDirectorFacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[4]));
			filme.setActor3FacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[5]));
			filme.setActor2Name(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[6]));
			filme.setActor1FacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[7]));
			filme.setGross(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[8]));
			filme.setGenres(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[9]));
			filme.setActor1Name(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[10]));
			filme.setMovieTitle(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[11]));
			filme.setNumVotedUsers(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[12]));
			filme.setCastTotalFacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[13]));
			filme.setActor3Name(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[14]));
			filme.setFaceNumberInPoster(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[15]));
			filme.setPlotKeywords(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[16]));
			filme.setMovieImdbLink(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[17]));
			filme.setNumUserForReviews(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[18]));
			filme.setLanguage(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[19]));
			filme.setCountry(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[20]));
			filme.setContentRating(validarPropriedadeStringNulaOuVazia(propriedadesObjeto[21]));
			filme.setBudget(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[22]));
			filme.setTitleYear(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[23]));
			filme.setActor2FacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[24]));
			filme.setImdbScore(validarPropriedadeFloatNulaOuVazia(propriedadesObjeto[25]));
			filme.setAspectRatio(validarPropriedadeFloatNulaOuVazia(propriedadesObjeto[26]));
			filme.setMovieFacebookLikes(validarPropriedadeInteiroNulaOuVazia(propriedadesObjeto[27]));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return filme;
	}

	private static int validarPropriedadeInteiroNulaOuVazia(String prop) {
		return (prop != null && !prop.isEmpty()) ? Integer.parseInt(prop) : -1;
	}

	private static float validarPropriedadeFloatNulaOuVazia(String prop) {
		return (prop != null && !prop.isEmpty()) ? Float.parseFloat(prop) : -1;
	}

	private static String validarPropriedadeStringNulaOuVazia(String prop) {
		return (prop != null && !prop.isEmpty()) ? prop.trim() : prop;
	}

	private static ArrayList<EntidadeFilme> LerDadosFilmesImdb(ArquivoDeDadosDeAcessoAleatorioFilmes arqReader) {
		int i =0;
		EntidadeFilme filme;
		ArrayList<EntidadeFilme> listaFilmes = new ArrayList<>();
		do {
			filme = arqReader.getData(i);
			if (filme != null)
				listaFilmes.add(filme);
			i++;
		} while(filme != null);
		
		return listaFilmes;
	}
}

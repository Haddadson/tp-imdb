import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Program {

	public static void main(String[] args) {
		FileReader arq;
		BufferedReader leitor;
		ArrayList<EntidadeFilme> listaFilmes;

		try {
			ArquivoDeDadosDeAcessoAleatorioFilmes arqReader = new ArquivoDeDadosDeAcessoAleatorioFilmes();
			arq = new FileReader("src/movie_metadata.csv");
			arqReader.openFile("src/dados_filmes.bin");
			leitor = new BufferedReader(arq);
			String linha = leitor.readLine();
			int iteracao = 0;

			if (arqReader.getNumReg() == 0) {
				while (linha != null && !linha.isEmpty() && iteracao <= 40) { // Flag temporário para testes
					linha = leitor.readLine();
					if (linha != null && !linha.isEmpty()) {
						String[] propriedadesObjeto = linha.split(",");
						EntidadeFilme filme = mapearObjeto(propriedadesObjeto);
						if (filme != null)
							arqReader.setData(filme);
						iteracao++;
						System.out.println(iteracao);
					}
				}
			}

			listaFilmes = LerDadosFilmesImdb(arqReader);

			leitor.close();
			arqReader.closeFile("src/dados_filmes.bin");

			ArrayList<String> stopWords = ObterListaStopWords();

			ArrayList<EntidadePlotKeyWord> palavrasIndiceInvertido = ObterPalavrasIndiceInvertido(listaFilmes, stopWords);
			
			ArquivoDeDadosDeAcessoAleatorioIndiceInvertido arqReaderIndice = new ArquivoDeDadosDeAcessoAleatorioIndiceInvertido();
			arqReaderIndice.openFile("src/lista_palavras_indice.bin");
			arqReaderIndice.setQtdPalavras(palavrasIndiceInvertido.size());
			
			for(EntidadePlotKeyWord plotKeyWord : palavrasIndiceInvertido) {
				arqReaderIndice.setData(plotKeyWord);
			}
			
			ArrayList<EntidadePlotKeyWord> teste = LerDadosIndiceInvertido(arqReaderIndice);
			
			for (EntidadeFilme filmeImdb : listaFilmes) {
				System.out.println(filmeImdb.getMovieTitle());
			}
			
			for(EntidadePlotKeyWord plot : teste) {
				System.out.println(plot.getPalavra());
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
		int i = 0;
		EntidadeFilme filme;
		ArrayList<EntidadeFilme> listaFilmes = new ArrayList<>();
		do {
			filme = arqReader.getData(i);
			if (filme != null)
				listaFilmes.add(filme);
			i++;
		} while (filme != null);

		return listaFilmes;
	}

	private static ArrayList<String> ObterListaStopWords() throws IOException {
		ArrayList<String> resultado = new ArrayList<String>();
		FileReader arq = new FileReader("src/stopwords.txt");
		BufferedReader leitor = new BufferedReader(arq);
		String palavra;

		do {
			palavra = leitor.readLine();
			if (palavra != null && !palavra.isEmpty())
				resultado.add(palavra);
		} while (palavra != null && !palavra.isEmpty());

		leitor.close();
		arq.close();

		return resultado;
	}

	private static ArrayList<EntidadePlotKeyWord> ObterPalavrasIndiceInvertido(ArrayList<EntidadeFilme> listaFilmes,
			ArrayList<String> stopWords) {
		
		ArrayList<EntidadePlotKeyWord> listaPalavras = new ArrayList<EntidadePlotKeyWord>();
		EntidadePlotKeyWord plotKeyWord;
		for (EntidadeFilme filme : listaFilmes) {
			String[] plotKeyWordsString = filme.getPlotKeywords().split("[( )|(\\Q|\\E)]+");
			List<String> plotKeyWords = Arrays.asList(plotKeyWordsString);
			for (String palavra : plotKeyWords) {
				if(palavra != null && !palavra.isEmpty() && !stopWords.contains(palavra)) {
					if(!contemPalavra(listaPalavras, palavra)) {
						plotKeyWord = new EntidadePlotKeyWord();
						plotKeyWord.setPalavra(palavra);
						plotKeyWord.adicionarRegistroEmQuePalavraApareceu(filme.getNumRegistro());
						listaPalavras.add(plotKeyWord);
					}
					else {
						listaPalavras.stream().filter(o -> o.getPalavra().equals(palavra)).forEach(
				            o -> {
				               o.getRegistrosEmQueAparece().add(filme.getNumRegistro());
				            }
					    );
					}
				}
					
			}
		}
		Collections.sort(listaPalavras, new ComparadorEntidadePlotKeyWord());
		return listaPalavras;
	}
	
	private static boolean contemPalavra(ArrayList<EntidadePlotKeyWord> listaPesquisa, String palavra){
		ArrayList<EntidadePlotKeyWord> lista = new ArrayList<EntidadePlotKeyWord>(listaPesquisa);
	    return lista.stream().filter(o -> o.getPalavra().equals(palavra)).findFirst().isPresent();
	}
	
	private static ArrayList<EntidadePlotKeyWord> LerDadosIndiceInvertido(ArquivoDeDadosDeAcessoAleatorioIndiceInvertido arqReader) {
		int i = 1;
		EntidadePlotKeyWord plotKeyWord;
		ArrayList<EntidadePlotKeyWord> indiceInvertido = new ArrayList<>();
		do {
			plotKeyWord = arqReader.getData(i);
			if (plotKeyWord != null)
				indiceInvertido.add(plotKeyWord);
			i++;
		} while (plotKeyWord != null);

		return indiceInvertido;
	}
}

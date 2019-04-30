import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		FileReader arq;
		BufferedReader leitor;
		ArrayList<EntidadeFilme> listaFilmes;
		Scanner scan = new Scanner(System.in);

		// TODO: refatorar codigo
		try {
			ArquivoDeDadosDeAcessoAleatorioFilmes arqReader = new ArquivoDeDadosDeAcessoAleatorioFilmes();
			ArquivoDeDadosDeAcessoAleatorioIndiceInvertido arqReaderIndice = new ArquivoDeDadosDeAcessoAleatorioIndiceInvertido();
			ArquivoDeDadosDeAcessoAleatorioHash arqReaderHash = new ArquivoDeDadosDeAcessoAleatorioHash();

			arq = new FileReader(Constantes.CAMINHO_DADOS_FILMES);
			arqReader.openFile(Constantes.CAMINHO_DADOS_FILMES_BIN);
			arqReaderIndice.openFile(Constantes.CAMINHO_INDICE_INVERTIDO);
			arqReaderHash.openFile(Constantes.CAMINHO_HASH);

			leitor = new BufferedReader(arq);
			String linha = leitor.readLine();

			if (arqReader.getNumReg() == 0) {
				System.out.println("Adicionando filmes...");
				while (linha != null && !linha.isEmpty()) {
					linha = leitor.readLine();
					if (linha != null && !linha.isEmpty()) {
						String[] propriedadesObjeto = linha.split(",");
						EntidadeFilme filme = mapearObjeto(propriedadesObjeto);
						if (filme != null)
							arqReader.setData(filme);
					}
				}
			}

			listaFilmes = LerDadosFilmesImdb(arqReader);

			if (arqReaderIndice.getNumReg() == 0) {
				System.out.println("Gerando indice invertido...");
				ArrayList<String> stopWords = ObterListaStopWords();

				ArrayList<EntidadePlotKeyWord> palavrasIndiceInvertido = ObterPalavrasIndiceInvertido(listaFilmes,
						stopWords);

				for (EntidadePlotKeyWord plotKeyWord : palavrasIndiceInvertido) {
					arqReaderIndice.setData(plotKeyWord);
				}
			}

			ArrayList<EntidadePlotKeyWord> listaPalavrasIndiceInvertido = LerDadosIndiceInvertido(arqReaderIndice);
			TabelaHash tabelaHash = new TabelaHash(listaPalavrasIndiceInvertido.size());

			if (arqReaderHash.getNumReg() == 0) {
				System.out.println("Gerando tabela hash...");
				for (EntidadePlotKeyWord plot : listaPalavrasIndiceInvertido) {
					tabelaHash.add(new EntidadePalavrasHash(plot.getId(), plot.getPalavra()));
				}

				for (int i = 0; i < tabelaHash.getSIZE(); i++) {
					if (tabelaHash.getHashtable()[i] != null && !tabelaHash.getHashtable()[i].isEmpty()) {
						arqReaderHash.setData(i, tabelaHash.getHashtable()[i]);
					}
				}
			}

			String[] arrayPalavrasPesquisa;
			String continuar;
			do {
				System.out.println("Digite palavras para encontrar filmes relacionados (separar por |): ");
				arrayPalavrasPesquisa = scan.nextLine().split(Constantes.REGEX_PLOT_KEYWORDS);
				List<String> listaPalavrasPesquisa = Arrays.asList(arrayPalavrasPesquisa);
				for (String palavraPesquisa : listaPalavrasPesquisa) {
					EntidadeItemHash item = arqReaderHash.getData(tabelaHash.hash(palavraPesquisa));
					if (item != null && !item.getPalavras().isEmpty()) {
						final String palavraPesquisaBuscaStream = palavraPesquisa;
						EntidadePalavrasHash palavraHash = item.getPalavras().stream()
								.filter(o -> o.getPalavra().equals(palavraPesquisaBuscaStream)).findFirst()
								.orElse(null);
						if (palavraHash != null) {
							int codigo = palavraHash.getId();
							EntidadePlotKeyWord plotKey = arqReaderIndice.getData(codigo);
							ArrayList<EntidadeFilme> listaFilmesEncontrados = new ArrayList<EntidadeFilme>();
							for (int key : plotKey.getRegistrosEmQueAparece()) {
								EntidadeFilme filmeEncontrado = arqReader.getData(key);
								if (filmeEncontrado != null)
									listaFilmesEncontrados.add(filmeEncontrado);
							}

							if (!listaFilmesEncontrados.isEmpty()) {
								for (EntidadeFilme filmeBuscado : listaFilmesEncontrados) {
									System.out.println("\n---------------------------\n");
									System.out.println("Titulo: " + filmeBuscado.getMovieTitle());
									System.out.println("Diretor: " + filmeBuscado.getDirectorName());
									System.out.println("Ano do filme: " + filmeBuscado.getTitleYear());
									System.out.println("Plot KeyWords: " + filmeBuscado.getPlotKeywords());
									System.out.println("\n---------------------------\n");
								}
							}
						} else {
							System.out.println("\n---------------------------\n");
							System.out.println("Nenhum filme encontrado");
							System.out.println("\n---------------------------\n");
						}
					} else {
						System.out.println("\n---------------------------\n");
						System.out.println("Nenhum filme encontrado");
						System.out.println("\n---------------------------\n");
					}
				}

				System.out.println("Deseja continuar? (s/n)");
				continuar = scan.nextLine();
			} while (continuar.equalsIgnoreCase("s"));

			scan.close();
			leitor.close();
			arqReader.closeFile(Constantes.CAMINHO_DADOS_FILMES_BIN);
			arqReaderIndice.closeFile(Constantes.CAMINHO_INDICE_INVERTIDO);
			arqReaderHash.closeFile(Constantes.CAMINHO_HASH);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Obrigado por usar o sistema! Encerrando...");
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
		FileReader arq = new FileReader(Constantes.CAMINHO_STOP_WORDS);
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
			String[] plotKeyWordsString = filme.getPlotKeywords().split(Constantes.REGEX_PLOT_KEYWORDS);
			List<String> plotKeyWords = Arrays.asList(plotKeyWordsString);
			for (String palavra : plotKeyWords) {
				if (palavra != null && !palavra.isEmpty() && !stopWords.contains(palavra)) {
					if (!contemPalavra(listaPalavras, palavra)) {
						plotKeyWord = new EntidadePlotKeyWord();
						plotKeyWord.setPalavra(palavra);
						plotKeyWord.adicionarRegistroEmQuePalavraApareceu(filme.getNumRegistro());
						listaPalavras.add(plotKeyWord);
					} else {
						listaPalavras.stream().filter(o -> o.getPalavra().equals(palavra)).forEach(o -> {
							o.getRegistrosEmQueAparece().add(filme.getNumRegistro());
						});
					}
				}

			}
		}
		Collections.sort(listaPalavras, new ComparadorEntidadePlotKeyWord());
		return listaPalavras;
	}

	private static boolean contemPalavra(ArrayList<EntidadePlotKeyWord> listaPesquisa, String palavra) {
		ArrayList<EntidadePlotKeyWord> lista = new ArrayList<EntidadePlotKeyWord>(listaPesquisa);
		return lista.stream().filter(o -> o.getPalavra().equals(palavra)).findFirst().isPresent();
	}

	private static ArrayList<EntidadePlotKeyWord> LerDadosIndiceInvertido(
			ArquivoDeDadosDeAcessoAleatorioIndiceInvertido arqReader) {
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

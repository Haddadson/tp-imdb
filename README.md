# TP - Índice Invertido IMDB

Trabalho prático referente à disciplina de Laboratório de Computação III do curso de Engenharia de Software - PUC Minas.

### Proposta

Codificar e testar a implementação de um índice invertido de um arquivo de dados utilizando uma tabela hash em disco como índice. Você pode escolher se deseja usar encadeamento exterior para resolver as eventuais colisões (veja código no final do enunciado) ou utilizar o endereçamento aberto (open-addressing ou overflow progressivo).

Para o arquivo de dados, a base de dados que será utilizada para a prática é a base de dados públicos do IMDB. A base de dados contém informações de vários filmes já lançados. Deve-se utilizar [esta base de dados](https://github.com/Haddadson/tp-imdb/blob/master/src/movie_metadata.csv) para os experimentos (o arquivo está no formato CSV).

Deve-se criar um arquivo de dados (use a biblioteca Random Access File do Java) para armazenar cada registro do arquivo da base do IMDB. As consultas serão realizadas pelo campo “plot_keywords”. Este campo contém palavras que representam uma breve descrição de cada filme. Para permitir a consulta neste registro, você deverá criar um arquivo invertido com todas as palavras contidas na lista de palavras presentes neste campo. Atente-se para eliminar palavras muito comuns (Stop words - veja que as palavras estão na língua inglesa). 

Você também deve criar um índice utilizando hash para o seu arquivo invertido. Conforme estudando em sala, a consulta por palavra(s) deverá ser realizada no índice e não diretamente no arquivo invertido. Para representar
a palavra em forma de número para aplicar a função hash, você pode utilizar qualquer estratégia.

Você deverá codificar os seguintes métodos:
1. inicializar (construtora) : responsável por estabelecer um estado inicial "bem definido" para a estrutura de dados;
2. insere (novoElemento, chave) : responsável por armazenar um novo elemento
juntamente com o valor de sua chave (que será utilizado mais tarde para localizar o elemento);
3. pesquisa (chave) : responsável por localizar no conjunto o elemento cuja chave for passada como parâmetro e retorná-lo para o "cliente" do TAD; 
4. imprime () : responsável por imprimir os elementos da tabela hash, bem como suas chaves, na ordem em que se encontram armazenados.

Além disso, você deve codificar um programa para teste das implementações realizadas. Seu programa deve criar e inserir e pesquisar (não é necessário implementar a remoção) uma mesma série de elementos. Por fim, modifique suas implementações de modo a contabilizar o número de comparações realizadas durante as operações de pesquisa.

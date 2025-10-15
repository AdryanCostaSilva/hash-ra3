/*
 Curso: Bacharelado em Ciencia da Computacao
 Disciplina: Resolucao de Problemas Estruturados em Computacao
 Professor: Andrey Cabral Meira
 Periodo: 4
 Turma: B
 Integrantes:
  - Adryan Costa Silva
  - Hassan Ali El Gazouini
  - Hussein Ali El Gazouini
  - Murilo Zimerman Fortaleza
*/

import java.util.Random;


// Node

class Node {
    Registro registro; // cada node guarda um Registro
    Node next;         // ponteiro pro proximo node na lista encadeada

    // construtor simples: guarda o registro e aponta pra null (fim de lista por padrao)
    public Node(Registro registro) {
        this.registro = registro;
        this.next = null;
    }
}


// DoubleHash - sondagem dupla (double hashing)

class DoubleHash{

	private Registro list[];     // referencia pra tabela de registros (array)
	private Integer value;       // variavel auxiliar (nao muito usada)
	private int index;           // indice atual (usado dentro dos calculos)
	private long startTime = 0;  // controle de tempo
	private long elapsedTime = 0;
	private double finalTime;
	private int minGap;          // menor intervalo de posicoes vazias
	private int maxGap;          // maior intervalo de posicoes vazias
	private double avgGap;       // media dos intervalos vazios

	private long collisionCount = 0; // conta colisoes durante insercoes

	// construtor: recebe a tabela (array) que vai usar
	public DoubleHash(Registro list[]){
		this.list = list;
	}

	// getters/setters varios (so pra organização)
	public Integer getValue(){return value;}
	public void setValue(Integer value){this.value = value;}
	public int getIndex(){return index;}
	public void setIndex(int index){this.index = index;}
	public long getStartTime(){return startTime;}
	public void setStartTime(long startTime){this.startTime = startTime;}
	public long getElapsedTime(){return elapsedTime;}
	public void setElapsedTime(long elapsedTime){this.elapsedTime = elapsedTime;}
	public double getFinalTime(){return finalTime;}
	public void setFinalTime(double finalTime){this.finalTime = finalTime;}

	/*
	 * insert:
	 *  - gera uma chave aleatoria (key)
	 *  - calcula hash1 e hash2
	 *  - tenta inserir em (hash1 + i * hash2) % size
	 *  - se ja tiver ocupado, incrementa i e tenta de novo (double hashing)
	 *  - conta colisoes e mede o tempo de insercao
	 */
	public void insert(Random random, Registro list[], int size) {
		// gera uma chave "grande" aleatoria (8-9 digitos)
		int key = random.nextInt(100000000, 1000000000);
		int hash1 = key % size;                     // hash basico modulo
		int hash2 = 1 + (key % (size - 1));         // segunda funcao hash (garante passo != 0)
		int index = hash1;
		int i = 0;

		setStartTime(System.nanoTime()); // inicia cronometro

		// enquanto a posicao estiver ocupada, calcula nova posicao usando hash2
		while (list[index] != null) {
			collisionCount++;          // registrou uma colisao
			i++;
			setIndex((int)((hash1 + (long)i * hash2) % size)); // formula do double hashing
			index = getIndex();

			// se tentou size vezes, a tabela esta cheia ou deu ruim
			if (i >= size) {
				System.out.printf("Could not insert %d - table full after %d collisions.\n", key, i);
				return;
			}
		}

		// achou espaco vazio -> insere o registro com a chave gerada
		list[index] = new Registro(key);

		// atualiza tempos
		setElapsedTime(System.nanoTime() - getStartTime());
		setFinalTime(getElapsedTime() / 1_000_000_000.0);

		System.out.printf("Inserted %d at index %d after %d collisions.\n", key, index, i);
	}

	/*
	 * calculateGaps:
	 *  - percorre o array e mede sequencias consecutivas de posicoes nulas (gaps)
	 *  - calcula min, max e media desses gaps
	 *  - util pra ver como a tabela esta fragmentada (distribuicao)
	 */
	public void calculateGaps(Registro list[], int size) {
        int currentGap = 0;
        int totalGap = 0;
        int gapCount = 0;

        minGap = size; // inicializa grande pra reduzir depois
        maxGap = 0;

        for (int i = 0; i < size; i++) {
            if (list[i] == null) {
                currentGap++;
            } else {
                // quando encontrar ocupacao e havia um gap contando
                if (currentGap > 0) {
                    gapCount++;
                    totalGap += currentGap;
                    if (currentGap > maxGap) maxGap = currentGap;
                    if (currentGap < minGap) minGap = currentGap;
                    currentGap = 0;
                }
            }
        }

        // se terminou o array em um gap, conta ele tambem
        if (currentGap > 0) {
            gapCount++;
            totalGap += currentGap;
            if (currentGap > maxGap) maxGap = currentGap;
            if (currentGap < minGap) minGap = currentGap;
        }

        if (gapCount == 0) minGap = 0;
        avgGap = gapCount == 0 ? 0 : (double) totalGap / gapCount;
    }

	/*
	 * search:
	 *  - procura uma chave usando exatamente a mesma sequencia de indices
	 *    que a insercao (hash1 + i * hash2) % size
	 *  - retorna true se encontrou, false se achar posicao nula ou varrer tudo
	 */
	public boolean search(int key, Registro[] list, int size) {
		int hash1 = key % size;
		int hash2 = 1 + (key % (size - 1));
		int index = hash1;
		int i = 0;

		while (list[index] != null) {
			if (list[index].getElemento() == key) {
				return true; // encontrado
			}
			i++;
			index = (hash1 + i * hash2) % size;
			if (i >= size) break; // percorreu toda a tabela, para
		}
		return false; // nao encontrou
	}

	// getters das metricas
	public int getMaxGap() { return maxGap; }
    public int getMinGap() { return minGap; }
    public double getAvgGap() { return avgGap; }
	public long getCollisionCount() { return collisionCount; }

	// imprime a tabela (debug/analise)
	public void print(Registro list[], int size){
		System.out.printf("DoubleHash list:\n");
		for(int i = 0; i < size; i++){
			if(list[i] == null){
				System.out.printf("Null\n");
			}
			else{
				System.out.printf("Value: %d\n", list[i].getElemento());
			}
		}
	}
}


// MultiplicationHash - metodo por multiplicacao + listas encadeadas p/ colisao

class MultiplicationHash{
	private Registro list[]; // nao usado diretamente aqui, mantido por compatibilidade
	private Node[] table;     // tabela de listas encadeadas (cada posicao e uma lista)
	private int size;         // tamanho da tabela
	private long collisionCount = 0; // conta nos de colisao (numero de nos extras)
	private int maxGap;
	private int minGap;
	private double avgGap;
	private int longestList = 0;

	// construtor: recebe um array so pra pegar o tamanho e inicializar a tabela de nodes
	public MultiplicationHash(Registro list[]){
		this.size = list.length;
		table = new Node[this.size];
	}

	/*
	 * insert:
	 *  - gera chave aleatoria
	 *  - usa a constante A de Knuth para multiplicacao
	 *  - calcula o indice com (int)(size * frac)
	 *  - se posicao vazia -> insere direto
	 *  - se ocupada -> insere no inicio da lista ligada (push)
	 *  - conta quantos elementos estavam na lista pra atualizar colisoes
	 */
	public void insert(Random random){
		int key = random.nextInt(100000000, 1000000000);
		double A = (Math.sqrt(5) - 1) / 2; // Constante De Knuths A = 0.6180339887...
    	double frac = (key * A) - Math.floor(key * A); // pega o valor fracionario de K * A
    	int hash = (int)(size * frac); // m  vezes o valor fracionario que seria o indice na tablea usamos o (int) para arredondar para baixo por exeplo 0.628 seria 6 se m = 10
		int steps = 0;
		Node current = table[hash];

		Node newNode = new Node(new Registro(key));

		 if (table[hash] == null) {
            // posicao vazia, insere sem colisao
            table[hash] = newNode;
            System.out.printf("Inserted %d at position %d\n", key, hash);
        } else {
            // colisao: adiciona no inicio da lista ligada
            newNode.next = table[hash];
            table[hash] = newNode;
			// conta quantos nodes tinham antes pra considerar colisao
			while (current != null) {
 		   		steps++;
    			current = current.next;
			}
			collisionCount += steps;
            System.out.printf("Collision! Inserted %d at position %d (linked list)\n", key, hash);
        }
	}

	/*
	 * search:
	 *  calcula mesmo hash da multiplicacao
	 *  percorre a lista ligada da posicao procurando a chave
	 */
	public boolean search(int key) {
		double A = (Math.sqrt(5) - 1) / 2;
		double frac = (key * A) - Math.floor(key * A);
		int hash = (int)(size * frac);

		Node current = table[hash];
		while (current != null) {
			if (current.registro.getElemento() == key) {
				return true; // encontrado na lista ligada
			}
			current = current.next;
		}
		return false; // nao esta la
	}
	
	/*
	 * printTop3LongestLists:
	 *  - calcula o comprimento de cada lista na tabela
	 *  - mostra os 3 indices com maiores listas 
	 */
	public void printTop3LongestLists() {
		int[] lengths = new int[size]; // armazena tamanhos

		for (int i = 0; i < size; i++) {
			int count = 0;
			Node current = table[i];
			while (current != null) {
				count++;
				current = current.next;
			}
			lengths[i] = count;
		}

		// pega top 3
		for (int n = 0; n < 3; n++) {
			int maxLen = 0;
			int maxIndex = -1;
			for (int i = 0; i < size; i++) {
				if (lengths[i] > maxLen) {
					maxLen = lengths[i];
					maxIndex = i;
				}
			}

			if (maxIndex != -1 && maxLen > 0) {
				System.out.printf("Top %d: Index %d, Length %d\n", n + 1, maxIndex, maxLen);
				lengths[maxIndex] = 0; // zera pra nao repetir
			}
		}
	}

	// getter pra colisoes (numero total de nos que indicam colisao)
	public long getCollisionCount() {
		return collisionCount;
	}

	// getter pra tabela (acesso externo)
	public Node[] getTable(){
		return table;
	}

	public int getLongestList(){
		return longestList;
	}

	public int getMaxGap() {
		return maxGap; 
	}
	public int getMinGap() {
		 return minGap; 
	}
	public double getAvgGap() {
		 return avgGap;
	}

	/*
	 * calculateGaps para tabela de listas:
	 *  igual aos outros: mede sequencias de posicoes nulas (onde nao existe nenhuma lista)
	 *   isso mostra "buracos" na tabela de buckets
	 */
	public void calculateGaps() {
		int currentGap = 0;
		int totalGap = 0;
		int gapCount = 0;

		int min = size;
		int max = 0;

		for (int i = 0; i < size; i++) {
			if (table[i] == null) {
				currentGap++;
			} else {
				if (currentGap > 0) {
					gapCount++;
					totalGap += currentGap;
					if (currentGap > max) max = currentGap;
					if (currentGap < min) min = currentGap;
					currentGap = 0;
				}
			}
		}

		// se terminou com gap aberto, conta ele
		if (currentGap > 0) {
			gapCount++;
			totalGap += currentGap;
			if (currentGap > max) max = currentGap;
			if (currentGap < min) min = currentGap;
		}

		if (gapCount == 0) min = 0;
		double avg = gapCount == 0 ? 0 : (double) totalGap / gapCount;

		this.maxGap = max;
		this.minGap = min;
		this.avgGap = avg;
	}
}


// QuadraticHash - sondagem quadratica (probing com i^2)

class QuadraticHash {
    private Registro list[];
    private int size;
	private long collisionCount = 0;
	private int minGap;
	private int maxGap;
	private double avgGap;

    public QuadraticHash(Registro list[]) {
        this.list = list;
        this.size = list.length;
    }

    /*
     * insert:
     *   gera a chave
     *   calcula hash base = key % size
     *   tenta posicoes (hash + i^2) % size ate achar espaco
     *   conta colisoes e evita loop infinito testando i >= size
     */
    public void insert(Random random) {
        int key = random.nextInt(100000000, 1000000000);
        int hash = key % size;  // funcao hash inicial
        int i = 0; // contador de tentativas

 		while (list[(int)((hash + (long)i * i) % size)] != null) {
			collisionCount++;
            i++;

            if (i >= size) {
                System.out.printf("The table is full. Could not insert %d.\n", key);
                return;
            }			
        }

        int pos = (hash + i * i) % size;  
        list[pos] = new Registro(key);
        System.out.printf("Inserted %d at position %d after %d attempts.\n", key, pos, i);
    }

	// calcula gaps igual aos outros 
	public void calculateGaps(Registro list[], int size) {
        int currentGap = 0;
        int totalGap = 0;
        int gapCount = 0;

        minGap = size; // start large
        maxGap = 0;

        for (int i = 0; i < size; i++) {
            if (list[i] == null) {
                currentGap++;
            } else {
                if (currentGap > 0) {
                    gapCount++;
                    totalGap += currentGap;
                    if (currentGap > maxGap) maxGap = currentGap;
                    if (currentGap < minGap) minGap = currentGap;
                    currentGap = 0;
                }
            }
        }

        // trailing nulls
        if (currentGap > 0) {
            gapCount++;
            totalGap += currentGap;
            if (currentGap > maxGap) maxGap = currentGap;
            if (currentGap < minGap) minGap = currentGap;
        }

        if (gapCount == 0) minGap = 0;
        avgGap = gapCount == 0 ? 0 : (double) totalGap / gapCount;
    }

	// busca usando a mesma sequencia i^2
	public boolean search(int key) {
		int hash = key % size;
		int i = 0;

		while (list[(int)((hash + (long)i * i) % size)] != null) {
			int pos = (int)((hash + (long)i * i) % size);
			if (list[pos].getElemento() == key) {
				return true; // achou
			}
			i++;
			if (i >= size) break; // terminou de percorrer
		}
		return false;
	}
	
	public int getMaxGap() { return maxGap; }
    public int getMinGap() { return minGap; }
    public double getAvgGap() { return avgGap; }

	// getter para colisoes
	public long getCollisionCount() {
		return collisionCount;
	}

	// retorna o array de registros usado
    public Registro[] getTable() {
        return list;
    }
}


// Main, executa os testes comparativos entre as 3 tecnicas

public class Main{
	public static void main(String[]args){

		Random random = new Random();
		random.setSeed(999); // seed fixa pra resultados repetiveis
		int size = 10000;
		int numKeysToInsert = 9000;
		Registro list[] = new Registro[size]; // tabela usada pelo double hash

		// Double Hash
		DoubleHash doubleHash = new DoubleHash(list);

		long StartTimer2 = System.nanoTime();

		for (int i = 0; i < numKeysToInsert; i++) {
    		doubleHash.insert(random, list, size);
		}

		long EndTimer2 = System.nanoTime();
		long duracao2 = EndTimer2 - StartTimer2;
		double millis2 = duracao2 / 1_000_000.0;

		doubleHash.print(list, size);
		doubleHash.calculateGaps(list, size);
		// imprime metricas de tempo e colisao 
		System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracao2, millis2);
		System.out.printf("Total collisions: %d\n", doubleHash.getCollisionCount());
		System.out.printf("Largest empty gap: %d\n", doubleHash.getMaxGap());
		System.out.printf("Smallest empty gap: %d\n", doubleHash.getMinGap());
		System.out.printf("Average empty gap size: %.2f\n", doubleHash.getAvgGap());

		// busca todos os elementos inseridos pelo double hash
		System.out.println("\nDouble hash iniciando busca de todos os elementos...");
		long startSearchDouble = System.nanoTime();

		int foundDouble = 0;
		for (int i = 0; i < size; i++) {
			if (list[i] != null) {
				int key = list[i].getElemento();
				if (doubleHash.search(key, list, size)) foundDouble++;
			}
		}

		long endSearchDouble = System.nanoTime();
		double durationDoubleMs = (endSearchDouble - startSearchDouble) / 1_000_000.0;
		System.out.printf("Tempo total de busca: %.3f ms\n", durationDoubleMs);
		System.out.printf("Elementos encontrados: %d\n", foundDouble);

		//Hash de multiplicacao
		MultiplicationHash multiplicationHash = new MultiplicationHash(list);
		System.out.printf("\nMultiplication hash:\n");
		random.setSeed(999); // reseta seed pra gerar as mesmas chaves

		long StartTimer = System.nanoTime();

		for (int i = 0; i < numKeysToInsert; i++){
			multiplicationHash.insert(random);
		}

		long EndTimer = System.nanoTime();
		long duracao = EndTimer - StartTimer;
		double millis = duracao / 1_000_000.0;
		
		// imprime cada bucket como lista ligada (debug)
		for(int i = 0; i < size; i++){
			Node node = multiplicationHash.getTable()[i];
			System.out.printf("[%d]: ", i);
			while(node != null){
				System.out.printf("%d -> ", node.registro.getElemento());
				node = node.next;
			}
			System.out.println("null");
		}
		System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracao, millis);
		System.out.printf("Total collisions (linked list nodes): %d\n", multiplicationHash.getCollisionCount());

		multiplicationHash.calculateGaps();
		System.out.printf("Largest empty gap: %d\n", multiplicationHash.getMaxGap());
		System.out.printf("Smallest empty gap: %d\n", multiplicationHash.getMinGap());
		System.out.printf("Average empty gap size: %.2f\n", multiplicationHash.getAvgGap());
		
		multiplicationHash.printTop3LongestLists();

		// busca em toda a tabela de multiplicacao
		System.out.println("\nHash multiplicacao iniciando busca de todos os elementos...");
		long startSearchM = System.nanoTime();

		int foundM = 0;
		for (int i = 0; i < size; i++) {
			Node node = multiplicationHash.getTable()[i];
			while (node != null) {
				int key = node.registro.getElemento();
				if (multiplicationHash.search(key)) foundM++;
				node = node.next;
			}
		}

		long endSearchM = System.nanoTime();
		double durationMMs = (endSearchM - startSearchM) / 1_000_000.0;
		System.out.printf("Tempo total de busca: %.3f ms\n", durationMMs);
		System.out.printf("Elementos encontrados: %d\n", foundM);

		// Hash quadratico
		Registro quadraticList[] = new Registro[size];
		QuadraticHash quadraticHash = new QuadraticHash(quadraticList);
		System.out.printf("\nQuadratic hash:\n");
	
		random.setSeed(999);
	
		long StartTimerQ = System.nanoTime();
		for (int i = 0; i < numKeysToInsert; i++) {
    		quadraticHash.insert(random);
		}
		long EndTimerQ = System.nanoTime();
		long duracaoQ = EndTimerQ - StartTimerQ;
		double millisQ = duracaoQ / 1_000_000.0;
		
		for (int i = 0; i < size; i++) {
			Registro reg = quadraticHash.getTable()[i];
			System.out.printf("[%d]: %s\n", i, reg != null ? reg.getElemento() : "null");
		}
		System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracaoQ, millisQ);

		quadraticHash.calculateGaps(quadraticList, size);
		System.out.printf("Total collisions (linked list nodes): %d\n", quadraticHash.getCollisionCount());	
		System.out.printf("Largest empty gap: %d\n", quadraticHash.getMaxGap());
		System.out.printf("Smallest empty gap: %d\n", quadraticHash.getMinGap());
		System.out.printf("Average empty gap size: %.2f\n", quadraticHash.getAvgGap());

		// busca no hash quadratico
		System.out.println("\nHash quadratico iniciando busca de todos os elementos...");
		long startSearchQ = System.nanoTime();

		int foundQ = 0;
		for (int i = 0; i < size; i++) {
			if (quadraticList[i] != null) {
				int key = quadraticList[i].getElemento();
				if (quadraticHash.search(key)) foundQ++;
			}
		}

		long endSearchQ = System.nanoTime();
		double durationQMs = (endSearchQ - startSearchQ) / 1_000_000.0;
		System.out.printf("Tempo total de busca: %.3f ms\n", durationQMs);
		System.out.printf("Elementos encontrados: %d\n", foundQ);

	}
}

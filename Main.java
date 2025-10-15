/*
	Curso: Bacharelado em Ciência da Computação
	Disciplina: Resolução de Problemas Estruturados em Computação
	Professor: Andrey Cabral Meira
	Período: 4
	Turma: B
	Integrantes:
		-Adryan Costa Silva
		-Hassan Ali El Gazouini
		-Hussein Ali El Gazouini
		-Murilo Zimerman Fortaleza
*/
import java.util.Random;

class Node {
    Registro registro; // cada Node armazena um objeto Registro
    Node next;

    // Construtor recebe um Registro
    public Node(Registro registro) {
        this.registro = registro; 
        this.next = null;         // inicial mente nao aponta para utro node
    }
}



class DoubleHash{
	private Registro list[];
	private Integer value;
	private int index;
	private long startTime = 0;
	private long elapsedTime = 0;
	private double finalTime;
	public DoubleHash(Registro list[]){
		this.list = list;
	}
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

	public void insert(Random random, Registro list[], int size){
		setValue(random.nextInt(100000000, 1000000000));
		int hash1 = value % size;
		int hash2 = 1 + (value % (size -1));
		setIndex(hash1);
		int i = 0;
		setStartTime(System.nanoTime());
		while(list[index] != null){
			System.out.printf("Tried to insert %d in position %d, but found a collision.\n", value, index);
			i++;
			setIndex((int)((hash1 + (long)i * hash2) % size));
			if(i >= size){
				System.out.printf("The table is full.\n");
				return;
			}
		}
		list[index] = new Registro(value);
		setElapsedTime((getElapsedTime() + System.nanoTime()) - getStartTime());
		System.out.printf("Value %d added into position %d.\n", value, index);
		setFinalTime(getElapsedTime() /  1_000_000_000.0);
	}
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






class MultiplicationHash{
	private Registro list[];
	private Node[] table;
	private int size;


	public MultiplicationHash(Registro list[]){
		// this.list = list;
		this.size = list.length;
		table = new Node[this.size];
	}

	public void insert(Random random){
		int key = random.nextInt(100000000, 1000000000);
		double A = (Math.sqrt(5) - 1) / 2; // Constante De Knuths A = 0.6180339887...
    	double frac = (key * A) - Math.floor(key * A); // pega o valor fracionario de K * A
    	int hash = (int)(size * frac); // m  vezes o valor fracionario que seria o indice na tablea usamos o (int) para arredondar para baixo por exeplo 0.628 seria 6 se m = 10

		Node newNode = new Node(new Registro(key));

		 if (table[hash] == null) {
            table[hash] = newNode; // espaco vazio, insere sem colisao
            System.out.printf("Inserted %d at position %d\n", key, hash);
        } else {
            // se tiver colisao e adicionada no mesmo indice numa lista encadeada
            newNode.next = table[hash];
            table[hash] = newNode;
            System.out.printf("Collision! Inserted %d at position %d (linked list)\n", key, hash);
        }
	}
	// metodo getter para a tabela
	public Node[] getTable(){
		return table;
	}
}

class QuadraticHash {
    private Registro list[];
    private int size;

    public QuadraticHash(Registro list[]) {
        this.list = list;
        this.size = list.length;
    }

    public void insert(Random random) {
        int key = random.nextInt(100000000, 1000000000);
        int hash = key % size;  // funcao hash inicial
        int i = 0; // contador de tentativas
		int collisionCount = 0;


 		while (list[(int)((hash + (long)i * i) % size)] != null) {
            System.out.printf("Tried to insert %d in position %d, but found a collision.\n", 
			
            key, (int)((hash + (long)i * i) % size));
            i++;

			if (list[i] != null) {
				collisionCount++;
			}

            if (i >= size) {
                System.out.printf("The table is full. Could not insert %d.\n", key);
                return;
            }

			
        }

        int pos = (hash + i * i) % size;  
        list[pos] = new Registro(key);
        System.out.printf("Inserted %d at position %d after %d attempts.\n", key, pos, i);
    }

	// metodo getter para a tabela
    public Registro[] getTable() {
        return list;
    }
}

// class Cuckoo{
// 	private Registro list[];
// 	public Cuckoo(Registro List[]){
// 		this.list = list;
// 	}
// 	public void insert(Random random, Integer list[], int size){
// 		int key = random.nextInt(100000000, 1000000000);
// 		int hash1 = (3 * key + 1) % size;
// 		int hash2 = (5 * key + 7) % size;
// 	}
// }


public class Main{
	public static void main(String[]args){
		Random random = new Random();
		random.setSeed(999);
		int size = 100000;
		Registro list[] = new Registro[size];
		DoubleHash doubleHash = new DoubleHash(list);
		for(int i = 0; i< size; i++){
			doubleHash.insert(random, list, size);
		}
		doubleHash.print(list, size);	
		System.out.printf("Elapsed time to insert using Double Hash: %f seconds\n", doubleHash.getFinalTime());



		// MultiplicationHash multiplicationHash = new MultiplicationHash(list);
		// System.out.printf("\nMultiplication hash:\n");
		// random.setSeed(999); // resetamos a seed para gerar os mesmos numeros do comeco

		// long StartTimer = System.nanoTime();

		// for (int i = 0; i < size; i++){
		// 	multiplicationHash.insert(random);
		// }

		// long EndTimer = System.nanoTime();
		// long duracao = EndTimer - StartTimer;
		// double millis = duracao / 1_000_000.0;
		
		// for(int i = 0; i < size; i++){
		// 	Node node = multiplicationHash.getTable()[i];
		// 	System.out.printf("[%d]: ", i);
		// 	while(node != null){
		// 		System.out.printf("%d -> ", node.registro.getElemento());
		// 		node = node.next;
		// 	}
		// 	System.out.println("null");
		// }
		// System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracao, millis);
		System.out.printf("Total collisions (linked list nodes): %d\n", collisionCount);





		// 	Registro quadraticList[] = new Registro[size];
		// 	QuadraticHash quadraticHash = new QuadraticHash(quadraticList);
		// 	System.out.printf("\nQuadratic hash:\n");

		// 	random.setSeed(999);

		// 	long StartTimerQ = System.nanoTime();
		// 	for (int i = 0; i < size; i++) {
		// 		quadraticHash.insert(random);
		// 	}
		// 	long EndTimerQ = System.nanoTime();
		// 	long duracaoQ = EndTimerQ - StartTimerQ;
		// 	double millisQ = duracaoQ / 1_000_000.0;
			
		// 	for (int i = 0; i < size; i++) {
		// 		Registro reg = quadraticHash.getTable()[i];
		// 		System.out.printf("[%d]: %s\n", i, reg != null ? reg.getElemento() : "null");
		// 	}
		// 	System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracaoQ, millisQ);

	}
}

import java.util.Random;

class Node {
    Registro registro; 
    Node next;

    public Node(Registro registro) {
        this.registro = registro; 
        this.next = null;         
    }
}

class DoubleHash{
	private Registro list[];
	private Integer value;
	private int index;
	private long startTime = 0;
	private long elapsedTime = 0;
	private double finalTime;
	private int minGap;
	private int maxGap;
	private double avgGap;
	private long collisionCount = 0;

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

	public void insert(Random random, Registro list[], int size) {
		int key = random.nextInt(100000000, 1000000000);
		int hash1 = key % size;
		int hash2 = 1 + (key % (size - 1));
		int index = hash1;
		int i = 0;

		setStartTime(System.nanoTime());

		while (list[index] != null) {
			collisionCount++;
			i++;
			setIndex((int)((hash1 + (long)i * hash2) % size));
			index = getIndex();

			if (i >= size) {
				System.out.printf("Could not insert %d — table full after %d collisions.\n", key, i);
				return;
			}
		}

		list[index] = new Registro(key);

		setElapsedTime(System.nanoTime() - getStartTime());
		setFinalTime(getElapsedTime() / 1_000_000_000.0);

		System.out.printf("Inserted %d at index %d after %d collisions.\n", key, index, i);
	}

	public void calculateGaps(Registro list[], int size) {
        int currentGap = 0;
        int totalGap = 0;
        int gapCount = 0;

        minGap = size;
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

        if (currentGap > 0) {
            gapCount++;
            totalGap += currentGap;
            if (currentGap > maxGap) maxGap = currentGap;
            if (currentGap < minGap) minGap = currentGap;
        }

        if (gapCount == 0) minGap = 0;
        avgGap = gapCount == 0 ? 0 : (double) totalGap / gapCount;
    }

	public boolean search(int key, Registro[] list, int size) {
		int hash1 = key % size;
		int hash2 = 1 + (key % (size - 1));
		int index = hash1;
		int i = 0;

		while (list[index] != null) {
			if (list[index].getElemento() == key) {
				return true;
			}
			i++;
			index = (hash1 + i * hash2) % size;
			if (i >= size) break;
		}
		return false;
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

	public long getCollisionCount() {
		return collisionCount;
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
	private long collisionCount = 0;
	private int maxGap;
	private int minGap;
	private double avgGap;
	private int longestList = 0;

	public MultiplicationHash(Registro list[]){
		this.size = list.length;
		table = new Node[this.size];
	}

	public void insert(Random random){
		int key = random.nextInt(100000000, 1000000000);
		double A = (Math.sqrt(5) - 1) / 2;
    	double frac = (key * A) - Math.floor(key * A);
    	int hash = (int)(size * frac);
		int steps = 0;
		Node current = table[hash];

		Node newNode = new Node(new Registro(key));

		 if (table[hash] == null) {
            table[hash] = newNode;
            System.out.printf("Inserted %d at position %d\n", key, hash);
        } else {
            newNode.next = table[hash];
            table[hash] = newNode;
			while (current != null) {
 		   		steps++;
    			current = current.next;
			}
			collisionCount += steps;
            System.out.printf("Collision! Inserted %d at position %d (linked list)\n", key, hash);
        }
	}

	public boolean search(int key) {
		double A = (Math.sqrt(5) - 1) / 2;
		double frac = (key * A) - Math.floor(key * A);
		int hash = (int)(size * frac);

		Node current = table[hash];
		while (current != null) {
			if (current.registro.getElemento() == key) {
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	public void printTop3LongestLists() {
		int[] lengths = new int[size];

		for (int i = 0; i < size; i++) {
			int count = 0;
			Node current = table[i];
			while (current != null) {
				count++;
				current = current.next;
			}
			lengths[i] = count;
		}

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
				lengths[maxIndex] = 0;
			}
		}
	}

	public long getCollisionCount() {
		return collisionCount;
	}

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

    public void insert(Random random) {
        int key = random.nextInt(100000000, 1000000000);
        int hash = key % size;
        int i = 0;

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

	public void calculateGaps(Registro list[], int size) {
        int currentGap = 0;
        int totalGap = 0;
        int gapCount = 0;

        minGap = size;
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

        if (currentGap > 0) {
            gapCount++;
            totalGap += currentGap;
            if (currentGap > maxGap) maxGap = currentGap;
            if (currentGap < minGap) minGap = currentGap;
        }

        if (gapCount == 0) minGap = 0;
        avgGap = gapCount == 0 ? 0 : (double) totalGap / gapCount;
    }

	public boolean search(int key) {
		int hash = key % size;
		int i = 0;

		while (list[(int)((hash + (long)i * i) % size)] != null) {
			int pos = (int)((hash + (long)i * i) % size);
			if (list[pos].getElemento() == key) {
				return true;
			}
			i++;
			if (i >= size) break;
		}
		return false;
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

	public long getCollisionCount() {
		return collisionCount;
	}

    public Registro[] getTable() {
        return list;
    }
}

public class Main{
	public static void main(String[]args){

		Random random = new Random();
		random.setSeed(999);
		int size = 10000;
		int numKeysToInsert = 9000;
		Registro list[] = new Registro[size];

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
		System.out.printf("Tempo total de insercao: %d ns (%.3f ms)\n", duracao2, millis2);
		System.out.printf("Total collisions: %d\n", doubleHash.getCollisionCount());
		System.out.printf("Largest empty gap: %d\n", doubleHash.getMaxGap());
		System.out.printf("Smallest empty gap: %d\n", doubleHash.getMinGap());
		System.out.printf("Average empty gap size: %.2f\n", doubleHash.getAvgGap());

		System.out.println("\nDouble hash Iniciando busca de todos os elementos...");
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

		MultiplicationHash multiplicationHash = new MultiplicationHash(list);
		System.out.printf("\nMultiplication hash:\n");
		random.setSeed(999);

		long StartTimer = System.nanoTime();

		for (int i = 0; i < numKeysToInsert; i++){
			multiplicationHash.insert(random);
		}

		long EndTimer = System.nanoTime();
		long duracao = EndTimer - StartTimer;
		double millis = duracao / 1_000_000.0;
		
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

		System.out.println("\n Hash multiplicacao Iniciando busca de todos os elementos...");
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

		System.out.println("\nHash quadratico Iniciando busca de todos os elementos...");
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

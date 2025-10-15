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
			setIndex((hash1 + i * hash2) % size);
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
	public MultiplicationHash(Registro List[]){
		this.list = list;
	}
	public void insert(Random random, Integer list[], int size){
		int value = random.nextInt(100000000, 1000000000);
	}
}

class Cuckoo{
	private Registro list[];
	public Cuckoo(Registro List[]){
		this.list = list;
	}
	public void insert(Random random, Integer list[], int size){
		int value = random.nextInt(100000000, 1000000000);
	}
}

public class Main{
	public static void main(String[]args){
		Random random = new Random();
		random.setSeed(999);
		int size = 100;
		Registro list[] = new Registro[size];
		DoubleHash doubleHash = new DoubleHash(list);
		for(int i = 0; i< size; i++){
			doubleHash.insert(random, list, size);
		}
		System.out.printf("Elapsed time to insert using Double Hash: %f seconds\n", doubleHash.getFinalTime());
		doubleHash.print(list, size);	
	}
}

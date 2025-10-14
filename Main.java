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
	public DoubleHash(Registro List[]){
		this.list = list;
	}
	public void insert(Random random, Registro list[], int size){
		int value = random.nextInt(100000000, 1000000000);
		int hash1 = value % size;
		int hash2 = 1 + (value % (size -1));
		int index = hash1;
		int i = 0;
		while(list[index] != null){
			System.out.printf("Tried to insert %d in position %d, but found a collision.\n", value, index);
			i++;
			index = (hash1 + i * hash2) % size;

			if(i >= size){
				System.out.printf("The table is full.\n");
				return;
			}
		}
		list[index] = new Registro(value);
		System.out.printf("Value %d added into position %d.\n", value, index);
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
		int size = 7;
		Registro list[] = new Registro[size];
		DoubleHash doubleHash = new DoubleHash(list);
		for(int i = 0; i < size; i++){
			doubleHash.insert(random, list, size);
		}
		System.out.printf("List:\n");
		for(int i = 0; i < size; i++){
			System.out.printf("%d\n", list[i].getElemento());
		}
	}
}

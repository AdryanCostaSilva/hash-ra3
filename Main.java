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


public class Main{
	public static void main(String[]args){
		Random random = new Random();
		random.setSeed(999);
		int list[] = new int[10];
		for(int i = 0; i < 10; i++){
			int n = random.nextInt(100000000, 1000000000);
			list[i] = n;
		}
		for(int i = 0; i < 10; i++){
			System.out.printf("%d\n", list[i]);
		}
	}
}

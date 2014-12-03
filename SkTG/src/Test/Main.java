package Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import KTOptPrs.Grammar;

public class Main {

	public static void main(String[] args) throws IOException {
		/*Grammar g = new Grammar(new File("C:\\Users\\Sina\\Desktop\\SkTG\\src\\Test\\test-grammar2.txt"));
		System.out.println(g);
		BigDecimal result = g.parse(new File("C:\\Users\\Sina\\Desktop\\SkTG\\src\\Test\\input2.txt"), Arrays.asList(0, 2, 3));*/
		
		Grammar g = new Grammar(new File("C:\\Users\\Sina\\Desktop\\SkTG\\src\\Test\\test-grammar.txt"));
		System.out.println(g);
		BigDecimal result = g.parse(new File("C:\\Users\\Sina\\Desktop\\SkTG\\src\\Test\\input.txt"), Arrays.asList(0, 1, 2, 5));
		
		System.out.println("Cliques Structure:\n" + g.getCliques() + "\n");
		
		for (String key : g.getDTable().keySet()){
			System.out.println("Key: " + key + "\tValue: " + g.getDTable().get(key));
		}
		
		System.out.println("\nProbability: " + result);
	}

}

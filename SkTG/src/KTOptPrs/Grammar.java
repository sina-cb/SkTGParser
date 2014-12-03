package KTOptPrs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
	
	List<Rule> rules = new ArrayList<>();
	List<Rule> aRules = new ArrayList<>();
	List<Rule> dRules = new ArrayList<>();
	List<Rule> bcRules = new ArrayList<>();
	
	HashMap<String, BigDecimal> dTable = new HashMap<>();

	List<Integer> tempFinal;
	
	Set<Integer> finalCliques = new LinkedHashSet<>();
	Set<List<Integer>> finalCliquesString = new LinkedHashSet<>();
	
	String input = null;
	
	public Grammar(File input) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		
		while(line != null){
			Rule rule = new Rule(line.split("\\t+"));
			
			if (rule.RHS2 == null){
				if (rule.LHS.isStartRule()){
					aRules.add(rule);
				}else{
					dRules.add(rule);
				}
			}else{
				bcRules.add(rule);
			}
			
			line = br.readLine();
		}
		br.close();
		
		// Order of DRules in the main Rules list is trivial
		rules.addAll(dRules);
		
		Comparator<Rule> comparator = new Comparator<Rule>() {
			@Override
			public int compare(Rule rule1, Rule rule2) {
				
				if (rule2.RHS1.toString().equals(rule1.LHS.toString())){
					return -1;
				}
				
				if (rule2.RHS2.toString().equals(rule1.LHS.toString())){
					return -1;
				}
				
				return 0;
			}
		};
		
		// Orders in BCRules are important
		Collections.sort(bcRules, comparator);
		rules.addAll(bcRules);
		
		// Order of ARules in the main Rules list is trivial
		rules.addAll(aRules);
	}

	@Override
	public String toString() {
		String result = "";
		for (Rule r : rules){
			result = result + r + "\n";
		}
		return result;
	}

	public BigDecimal parse(File file, List<Integer> startingIndexes) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String input = br.readLine();
		br.close();
		
		BigDecimal result = parse(input, startingIndexes);
		return result;
	}
	
	public BigDecimal parse(String input, List<Integer> startingIndexes){
		this.input = input;
		
		BigDecimal result = f(aRules.get(0).LHS, startingIndexes);

		for (int i = 0; i < input.length(); i++){
			if (!finalCliques.contains(i)){
				return new BigDecimal(0);
			}
		}
		
		return result;
	}
	
	//static int callNum = 0;
	
	public BigDecimal f (AlternatingString alpha, List<Integer> k){
		//int localCall = callNum;
		//System.out.println("Grammar.f() - Start(" + callNum + ")");
		//callNum++;
		
		if (alpha.isAllMasking()){
			for (int i = 0; i < k.size(); i++){
				if (this.input.charAt(k.get(i)) != alpha.terminal.get(i).charAt(0))
					return new BigDecimal(0);
			}
			//System.out.println("Grammar.f() - End(" + localCall + ")");
			tempFinal = k;
			return new BigDecimal(1);
		}else if(alpha.isStartRule()){
			BigDecimal max = new BigDecimal(0);
			for (Rule r : aRules) {
				String key = findKeyFor(r.RHS1, k);
				
				BigDecimal temp;
				if (!dTable.containsKey(key)){
					temp = f(r.RHS1, k);
					dTable.put(key, temp);
				}else{
					temp = dTable.get(key);
				}
				
				temp = temp.multiply(r.prob);
				
				if (temp.compareTo(max) > 0)
					max = temp;
			}
			//System.out.println("Grammar.f() - End(" + localCall + ")");
			return max;
		}else{
			BigDecimal max = new BigDecimal(0);
			
			List<Rule> rulesWithAlpha = new ArrayList<>();
			for (Rule r : rules) {
				if (r.LHS.equals(alpha)){
					rulesWithAlpha.add(r);
				}
			}
			
			for (Rule rule : rulesWithAlpha) {
				if (rule.RHS2 == null){
					String key = findKeyFor(rule.RHS1, k);
					
					BigDecimal temp;
					if (!dTable.containsKey(key)){
						temp = f(rule.RHS1, k);
						dTable.put(key, temp);
					}else{
						temp = dTable.get(key);
					}
					
					temp = temp.multiply(rule.prob);
					
					if (temp.compareTo(max) > 0){
						max = temp;
						finalCliques.addAll(tempFinal);
						finalCliquesString.add(tempFinal);
					}
				}else{
					int insertionIndex = rule.insertionIndex;
					int deletionIndex = rule.deletionIndex;
					
					int startK = -1;
					if (insertionIndex == 0){
						startK = -1;
					}else{
						startK = k.get(insertionIndex - 1);
					}
					
					int endK = -1;
					if (insertionIndex >= k.size()){
						endK = input.length();
					}else{
						endK = k.get(insertionIndex);
					}
					
					
					for (int i = startK + 1; i < endK; i++){
						List<Integer> kPrime = new ArrayList<>();
						for (int h = 0; h < k.size(); h++){
							if (h == deletionIndex){
								kPrime.add(i);
							}else{
								kPrime.add(k.get(h));
							}
						}
						Collections.sort(kPrime);
						
						String key = findKeyFor(rule.RHS1, kPrime);
						BigDecimal temp;
						if (!dTable.containsKey(key)){
							temp = f(rule.RHS1, kPrime);
							dTable.put(key, temp);
						}else{
							temp = dTable.get(key);
						}
						
						temp = temp.multiply(rule.prob);
						
						if (temp.compareTo(max) > 0){
							key = findKeyFor(rule.RHS2, k);
							BigDecimal temp2;
							if (!dTable.containsKey(key)){
								temp2 = f(rule.RHS2, k);
								dTable.put(key, temp2);
							}else{
								temp2 = dTable.get(key);
							}
							
							max = temp.multiply(temp2);
						}
					}
				}
			}
			
			//System.out.println("Grammar.f() - End(" + localCall + ")");
			return max;
		}
		
	}
	
	private String findKeyFor(AlternatingString rhs1, List<Integer> k) {
		String key = rhs1.toString() + "#";
		for (Integer tempK : k){
			key = key + tempK + ",";
		}
		key = key.substring(0, key.length() - 1);
		return key;
	}

	public Set<List<Integer>> getCliques(){
		return finalCliquesString;
	}
	
	public HashMap<String, BigDecimal> getDTable(){
		return dTable;
	}
	
}

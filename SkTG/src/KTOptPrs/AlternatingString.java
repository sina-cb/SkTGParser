package KTOptPrs;

import java.util.ArrayList;
import java.util.List;

public class AlternatingString {

	boolean allMasking = true;
	boolean startRule = false;
	List<String> nonTerminal = new ArrayList<>();
	List<String> terminal = new ArrayList<>();
	
	public AlternatingString (String input){

		if (input.equals("S")){
			startRule = true;
			nonTerminal.add("S");
			allMasking = false;
			return;
		}
		
		boolean capital = true;
		for (int i = 0; i<input.length(); i++){
			if (capital){
				if (input.charAt(i) >= 'a' && input.charAt(i) <= 'z'){
					nonTerminal.add(null);
					i--;
				} else {
					nonTerminal.add(input.charAt(i) + "");
					if (input.charAt(i) != 'M')
						allMasking = false;
				}
				capital = false;
			} else {
				terminal.add(input.charAt(i) + "");
				capital = true;
			}
		}
		if (capital)
			nonTerminal.add(null);
	}

	public boolean isAllMasking() {
		return allMasking;
	}

	public void setAllMasking(boolean allMasking) {
		this.allMasking = allMasking;
	}

	public boolean isStartRule() {
		return startRule;
	}

	public void setStartRule(boolean startRule) {
		this.startRule = startRule;
	}

	public List<String> getNonTerminal() {
		return nonTerminal;
	}

	public void setNonTerminal(List<String> nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	public List<String> getTerminal() {
		return terminal;
	}

	public void setTerminal(List<String> terminal) {
		this.terminal = terminal;
	}
	
	@Override
	public String toString() {
		String result = "";
		int i = 0;
		while (true){
			if (i >= nonTerminal.size() || i >= terminal.size()){
				break;
			}
			
			if (nonTerminal.get(i) != null){
				result = result + nonTerminal.get(i);
			}
			result = result + terminal.get(i);
			
			i++;
		}
		
		if (nonTerminal.get(i) != null)
			result = result + nonTerminal.get(i);
		
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		AlternatingString input = (AlternatingString) obj;
		
		if (input.nonTerminal.size() != nonTerminal.size()){
			return false;
		}
		
		if (input.terminal.size() != terminal.size()){
			return false;
		}
		
		for (int i = 0; i < this.terminal.size(); i++) {
			if (!input.terminal.get(i).equals(this.terminal.get(i)))
				return false;
		}
		
		for (int i = 0; i < this.nonTerminal.size(); i++) {
			if (	(input.nonTerminal.get(i) == null && this.nonTerminal.get(i) != null) || 
					(input.nonTerminal.get(i) != null && this.nonTerminal.get(i) == null) || 
					(!input.nonTerminal.get(i).equals(this.nonTerminal.get(i))) 
				)
			{
				return false;
			}
		}
		
		return true; 
	}
	
}

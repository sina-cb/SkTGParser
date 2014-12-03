package KTOptPrs;

import java.math.BigDecimal;

public class Rule {

	BigDecimal prob = new BigDecimal(-1);
	
	AlternatingString LHS = null;
	AlternatingString RHS1 = null;
	AlternatingString RHS2 = null;
	
	String type = "";

	int insertionIndex = -1;
	int deletionIndex = -1;
	
	public Rule(String[] input){
		
		LHS = new AlternatingString(input[0]);
		
		// input[1] == ":";
		
		RHS1 = new AlternatingString(input[2]);
		
		if (input[3].equals(":")){
			prob = new BigDecimal(input[4]);
			type = "D";
		}else{
			RHS2 = new AlternatingString(input[3]);
			// input[4] == ":"
			prob = new BigDecimal(input[5]);
			type = "BC";
			insertionIndex = Integer.parseInt(input[7]);
			deletionIndex = Integer.parseInt(input[9]);
		}
		
		if (LHS.isStartRule()){
			type = "A";
		}
		
	}
	
	@Override
	public String toString() {
		
		String result = "";
		
		
		result = result + LHS + "\t";
		result = result + ":" + "\t";
		result = result + RHS1 + "\t";
		
		if (RHS2 == null){
			result = result + ":" + "\t";
			result = result + prob + "\t";
		}else{
			result = result + RHS2 + "\t";
			result = result + ":" + "\t";
			result = result + prob + "\t";
		}
		
		return result;
	}
	
}

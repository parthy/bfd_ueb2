/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mci_tlx;

import java.util.HashMap;

/**
 *
 * @author parthy
 */
class TestResult {

    private String name;
    /**
     * Hashmap: z.B. MD -> 50.0
     */
    private HashMap<String, Float> demands;

    /**
     *		Wert:   0       1
     * ---------------------------
     * Weights:	    0	PD <-> MD
     *		    1	TD <-> MD
     *		    2	OP <-> MD
     *		    3	FR <-> MD
     *		    4	EF <-> MD
     *		    5	TD <-> PD
     *		    6	OP <-> PD
     *		    7	FR <-> PD
     *		    8	EF <-> PD
     *		    9	TD <-> OP
     *		    10	TD <-> FR
     *		    11	TD <-> EF
     *		    12	OP <-> FR
     *		    13	OP <-> EF
     *		    14	EF <-> FR
     */
    private int[] weights;

    public TestResult(String name) {
	this.name = name;
	this.demands = new HashMap<String, Float>();
	this.weights = new int[15];
    }

    public void addDemand(String name, float value) {
	this.demands.put(name, value);
    }

    public void addWeight(int idx, int value) {
	this.weights[idx] = value;
    }

    public String printResult() {
	return "";
    }

    public int calculateTLX() {
	int tlx = 0;
	
	int weightMD = weights[0] + weights[1] + weights[2] + weights[3] + weights[4];
	int weightPD = (weights[0]+1)%2 + weights[5] + weights[6] + weights[7] + weights[8];
	int weightTD = (weights[1]+1)%2 + (weights[5]+1)%2 + (weights[9]+1)%2 + (weights[10]+1)%2 + (weights[11]+1)%2;
	int weightOP = (weights[2]+1)%2 + (weights[6]+1)%2 + weights[9] + (weights[12]+1)%2 + (weights[13]+1)%2;
	int weightEF = (weights[4]+1)%2 + (weights[8]+1)%2 + (weights[14]+1)%2 + weights[11] + weights[13];
	int weightFR = (weights[3]+1)%2 + (weights[7]+1)%2 + weights[10] + weights[12] + weights[14];
	
	float rateMD = weightMD * demands.get("MD");
	float ratePD = weightPD * demands.get("PD");
	float rateTD = weightTD * demands.get("TD");
	float rateOP = weightOP * demands.get("OP");
	float rateEF = weightEF * demands.get("EF");
	float rateFR = weightFR * demands.get("FR");
	
	tlx = Math.round((rateMD + ratePD + rateTD + rateOP + rateEF + rateFR)/15);
	
	return tlx;
    }

    public String toString() {
	String ret = "";

	ret += name + ": ";
	/*ret += demands;

	for(int i=0; i<weights.length; i++) {
	    ret += " ; " + weights[i];
	}*/

	ret += "TLX: " + calculateTLX();

	return ret;
    }
}

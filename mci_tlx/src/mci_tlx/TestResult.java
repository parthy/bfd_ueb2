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
	return 0;
    }

    public String toString() {
	String ret = "";

	ret += name + ": ";
	ret += demands;

	for(int i=0; i<weights.length; i++) {
	    ret += " ; " + weights[i];
	}

	return ret;
    }
}

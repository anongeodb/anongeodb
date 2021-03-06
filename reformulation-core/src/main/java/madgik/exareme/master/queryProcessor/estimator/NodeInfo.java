/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madgik.exareme.master.queryProcessor.estimator;

import madgik.exareme.master.queryProcessor.estimator.db.AttrInfo;
import madgik.exareme.master.queryProcessor.estimator.db.RelInfo;
import madgik.exareme.master.queryProcessor.estimator.histogram.Bucket;

/**
 * @author jim
 */
public final class NodeInfo {
	// private final int nodeId;
	private double numberOfTuples;
	private double tupleLength; // in bytes
	private RelInfo resultRel;

	public NodeInfo(int nodeId, double numberOfTuples, double tupleLength, RelInfo resultRel) {
		// this.nodeId = nodeId;
		this.numberOfTuples = numberOfTuples;
		this.tupleLength = tupleLength;
		this.resultRel = resultRel;
	}

	public NodeInfo() {

	}

	/* getters and setters */
	// public int getNodeId() {
	// return nodeId;
	// }

	public double getNumberOfTuples() {
		return numberOfTuples;
	}

	public double getTupleLength() {
		return tupleLength;
	}

	
	public void setNumberOfTuples(double numberOfTuples) {
		this.numberOfTuples = numberOfTuples;
	}

	public void setTupleLength(double tupleLength) {
		this.tupleLength = tupleLength;
	}

	public RelInfo getResultRel() {
		return resultRel;
	}

	public void setResultRel(RelInfo resultRel) {
		this.resultRel = resultRel;
	}

	/* interface methods */
	public double outputRelSize() {
		return this.numberOfTuples * this.tupleLength;
	}

    public void applySelectivity(double selectivity) {
		this.numberOfTuples = this.numberOfTuples * selectivity;
		for(AttrInfo attInfo:this.resultRel.getAttrIndex().values()) {
			for(Bucket b:attInfo.getHistogram().getBucketIndex().values()) {
				b.setFrequency(b.getFrequency()*selectivity);
			}
		}
		this.resultRel.setNumberOfTuples(this.numberOfTuples);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madgik.exareme.master.queryProcessor.estimator.metadata;

import madgik.exareme.master.queryProcessor.estimator.db.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author jim
 */
public final class Metadata {
	public static final double NETWORK_RATE = 10485760.0;
	// public static final double CPU_GHZ = 3.0; // GHz 1/3 * 10^-9 =
	// 0.0000000000033
	public static final double CPU_CYCLE_TIME = 0.00000000033;
	// seconds, the cpu clockeed in 3.0 Ghz
	public static final int NUMBER_OF_VIRTUAL_MACHINES = 10;
	public static final int MIN_NUMBER_OF_VIRTUAL_MACHINES = 1;
	public static final double PAGE_SIZE = 4096.0; // bytes
	// public static final double PAGE_IO_TIME = 0.000048828;//disk with 80
	// MB/s. it takes 0.000048828 seconds (~= 48microsec) for a single page io
	// public static final double PAGE_IO_TIME = 0.0008192;//0.00015625
	public static final double PAGE_IO_TIME = 0.0000028;// 100MB/sec
	public static final double PAGE_IO_TIME_SCAN = 0.000009;// 100MB/sec
	public static final int NUMBER_SIZE = 8; // bytes
	public static final int TEXT_SIZE = 200;// bytes
	public static final double INDEX_UTILIZATION = 0.01;
	public static final double SPATIAL_INDEX_CREATION = 0.00007;

	private static Schema schema;

	/* private constructor - singleton pattern */
	private Metadata() {
	}

	/* getters */
	public Schema getSchema() {
		return schema;
	}

	/* interface methods */

	public static void feedMetadata(Schema fullSchema) {
		schema = fullSchema;
	}

}

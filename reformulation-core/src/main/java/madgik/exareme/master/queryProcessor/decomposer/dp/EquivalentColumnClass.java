package madgik.exareme.master.queryProcessor.decomposer.dp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unibz.krdb.obda.model.Function;
import madgik.exareme.master.queryProcessor.decomposer.query.Column;

public class EquivalentColumnClass {
	
	private Set<Column> cols;
	private Map<Integer, Column> tables;
	private boolean used;
	private boolean isSpatial;
	private Function spatialJoin;



	public EquivalentColumnClass(Set<Column> cols) {
		super();
		this.cols = cols;
		used=false;
		computeTables();
		isSpatial = false;
		spatialJoin = null;
	}


	public void setSpatial( boolean b) {
		isSpatial = b;
	}

	public Function getSpatialJoin() {
		return spatialJoin;
	}

	public void setSpatialJoin(Function spatialJoin) {
		this.spatialJoin = spatialJoin;
	}

	private void computeTables() {
		this.tables=new HashMap<Integer, Column>();
		for(Column c:this.cols){
			tables.put(c.getAlias(), c);
		}
		
		
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public Column getColumnForTable(int rj) {
		return tables.get(rj);
	}
	public void addConnectedTables(Map<Integer, Set<Integer>> connectedTables) {
		
		for(Integer t1:tables.keySet()){
			for(Integer t2:tables.keySet()){
				if(t1==t2)continue;
				connectedTables.get(t1).add(t2);
			}
		}
		
	}


	public boolean isSpatial() {
		return isSpatial;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package madgik.exareme.master.queryProcessor.decomposer.dag;

import madgik.exareme.master.queryProcessor.decomposer.query.NonUnaryWhereCondition;
import madgik.exareme.master.queryProcessor.decomposer.query.Operand;
import madgik.exareme.master.queryProcessor.decomposer.query.Table;
import madgik.exareme.master.queryProcessor.decomposer.util.Util;
import madgik.exareme.master.queryProcessor.estimator.NodeInfo;

import java.util.*;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Node {

	public static final int OR = 0;
	public static final int AND = 1;
	public static final int JOIN = 0;
	public static final int PROJECT = 1;
	public static final int UNION = 2;
	public static final int GROUPBY = 3;
	public static final int ORDERBY = 4;
	public static final int LIMIT = 5;
	public static final int SELECT = 6;
	public static final int REPARTITION = 7;
	public static final int BROADCAST = 9;
	public static final int BASE = 8;
	public static final int NO_OP = 10;
	public static final int RIGHTBROADCASTJOIN = 11;
	public static final int LEFTBROADCASTJOIN = 12;
	public static final int REPARTITIONJOIN = 13;
	public static final int UNIONALL = 14;
	public static final int NESTED = 15;
	public static final int BASEPROJECT = 16;
	public static final int LEFTJOIN = 17;
	public static final int RIGHTJOIN = 18;
	public static final int JOINKEY = 19;
	public static final int CENTRALIZEDJOIN = 13;
	
	
	// private boolean isBaseTable;
	private int type;
	// private int parentCounter;
	// private int hashID;
	private List<Node> children;
	private List<Node> parents;
	private Set<String> descendantBaseTables;
	private int opCode;
	private Object o;
	private boolean expanded;
	private boolean hashNeedsRecomputing;
	private boolean commutativity = true;
	private boolean swap = true;

	// private PartitionCols lastPartition;
	// private boolean prune;
	private boolean isMaterialised;

	// private Set<Column> redundantRepartitions;

	public Node(int type) {
		this.hashNeedsRecomputing = true;
		this.type = type;
		this.opCode = -1;
		// parentCounter=0;
		children = new ArrayList<Node>();
		parents = new ArrayList<Node>();
		// isBaseTable=false;
		this.o = new Object();
		this.expanded = false;
		
		// prune = false;
		// lastPartition = new PartitionCols();
		isMaterialised = false;
		descendantBaseTables = new HashSet<String>();
		// redundantRepartitions=new HashSet<Column>();
	}

	public Node(int type, int opCode) {
		hashNeedsRecomputing = true;
		// parentCounter=0;
		this.opCode = opCode;
		this.type = type;
		this.o = new Object();
		children = new ArrayList<Node>();
		parents = new ArrayList<Node>();
		this.expanded = false;
	
		// prune = false;
		// lastPartition = new PartitionCols();
		isMaterialised = false;
		descendantBaseTables = new HashSet<String>();
		// redundantRepartitions=new HashSet<Column>();
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
		this.setHashNeedsRecomputing();

	}

	private NodeInfo nodeInfo;

	

	


	/**
	 * @param hashID
	 *            the hashID to set
	 */
	// public void setHashID(int hashID) {
	// this.hashID = hashID;
	// }
	public void addChild(Node v) {
		if (this.children.contains(v)) {
			// System.out.println("replicate child??");
			return;
		}
		this.children.add(v);
		v.parents.add(this);

		setHashNeedsRecomputing();

		// }
	}

	public void setHashNeedsRecomputing() {

		if (this.hashNeedsRecomputing) {
			return;
		}
		hashNeedsRecomputing = true;
		// for(Node c:this.parents){
		// c.setHashNeedsRecomputing();
		// }

	}

	public void addChildAt(Node v, int i) {
		if (this.children.contains(v)) {
			// System.out.println("replicate child??");
			return;
		}
		this.children.add(i, v);
		v.parents.add(this);
		// if (!(this.getObject() instanceof Table && i > 0)) {
		this.setHashNeedsRecomputing();
		// }
	}

	public Node getChildAt(int i) {
		return this.children.get(i);
	}

	public List<Node> getChildren() {
		return Collections.unmodifiableList(this.children);
	}

	public Object getObject() {
		return this.o;
	}

	// public void setIsBaseTable(boolean b){
	// this.isBaseTable=b;
	// }
	public void setObject(Object obj) {
		this.o = obj;
		this.setHashNeedsRecomputing();
	}

	public void setOperator(int i) {
		this.opCode = i;
		this.setHashNeedsRecomputing();
	}

	public StringBuilder dotPrint(Set<Node> visited) {
		StringBuilder result = new StringBuilder();
		HashSet<String> shapes = new HashSet<String>();
		LinkedList<Node> queue = new LinkedList<Node>();

		queue.offer(this); // Place start node in queue
		shapes.add(this.dotShape());
		while (!queue.isEmpty()) {
			Node v = queue.getFirst();
			// Update neighbors
			if (!v.getChildren().isEmpty()) {
				for (int i = 0; i < v.getChildren().size(); i++) {
					Node c = v.getChildren().get(i);
					if (visited.contains(c)) {
						continue;
					}
					// visited.add(c);
					queue.add(c);
					shapes.add(c.dotShape());
					result.append(v.dotString());
					result.append(" -> ");
					result.append(c.dotString());
					// result.append("[label=\"").append(i).append("\"]");
					result.append(";\n");
				}

			}
			// else {
			// result.append("type:"+v.type+" opCode:"+v.opCode+"
			// object:"+v.o.toString());
			// }

			queue.removeFirst();
		}
		result.append(" }");

		result.insert(0, " } ");
		Iterator<String> it = shapes.iterator();
		while (it.hasNext()) {
			result.insert(0, it.next());
		}
		result.insert(0, "strict digraph G{ {");
		return result;
	}

	private String dotString() {
		/*
		 * if(this.o instanceof Selection){ return "SELECTION"; } else if(this.o
		 * instanceof Projection){ return "PROJECTION"; } else{
		 */
		return String.valueOf(this.hashCode());
		// }
	}

	private String dotShape() {
		String shape = "oval";
		if (this.type == 0) {
			shape = "box";
		}

		// if(prunningCounter>0){
		// return String.valueOf(this.hashCode()) + "[label= \"" + o.toString()
		// + " d:" + prunningCounter + " p:" + parents.size() + "\"]" +
		// " [shape=" + shape + "]";
		// return String.valueOf(this.hashCode()) + "[label= \"" + o.toString()
		// + " pr:" + this.prune + "\"]" + " [shape=" + shape + "]";
		String color = "black";
		
		
		String materialized = "";
		if (this.isMaterialised) {
			materialized = " [xlabel=\"M\"] ";
		}
		// return String.valueOf(this.hashCode()) + "[label= \"" + o.toString()
		// +" pr:" + this.pi + " cols:" + this.partitionRecord + "\"]"
		// +" [color="+color+ " [shape=" + shape + "]";
		// return String.valueOf(this.hashCode()) + "[label= \"" + o.toString()
		// + "\"]"+materialized +" [color="+color+ "] [shape=" + shape + "]";

		// }

		String object = o.toString();
		String fillcolor = "";
		if (this.nodeInfo != null)
			 object += " card:" + this.getNodeInfo().getNumberOfTuples();
		if (o instanceof Table) {

			Table t = (Table) o;
			
			 
		}
		if (this.opCode == LEFTBROADCASTJOIN) {
			object += "L:";
		}
		if (this.opCode == RIGHTBROADCASTJOIN) {
			object += "R:";
		}
		if (this.opCode == PROJECT) {
			object = "Project";
		}
		return String.valueOf(this.hashCode()) + "[label= \"" + object + "\"" + fillcolor + "]" + " [shape=" + shape
				+ "]";
		// return String.valueOf(this.hashCode()) + "[label= \"" + object + "::"
		// + this.descendantBaseTables.toString() + " id:"
		// + this.getHashId() + "\"]" + " [shape=" + shape + "]";
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public int getOpCode() {
		return this.opCode;
	}

	public int removeChild(Node lt) {
		int res = this.children.indexOf(lt);
		boolean r = this.children.remove(lt);
		lt.parents.remove(this);
		if (r) {
			// if (!(this.getObject() instanceof Table && res > 0)) {
			this.setHashNeedsRecomputing();
			// }
		} else {
			System.out.println("child not exists!!");
		}
		return res;
	}

	public void removeChildAt(int i) {
		Node toRemove = this.getChildAt(i);
		toRemove.parents.remove(this);
		this.removeChild(toRemove);
		this.setHashNeedsRecomputing();
	}


	

	public void removeAllChildren() {
		for (Node c : this.children) {
			c.parents.remove(this);
			// if(c.parents.isEmpty()){
			// System.out.println("remove?");
			// }
		}
		this.children = new ArrayList<Node>();
		this.setHashNeedsRecomputing();
	}

	

	int getNoOfParents() {
		return this.parents.size();
	}

	

	public Node getFirstParent() {
		return this.parents.get(0);
	}

	public List<Node> getParents() {
		return this.parents;
	}

	public int getFirstIndexOfChild(Node c) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i).equals(c)) {
				return i;
			}
		}
		return -1;
	}



	public boolean isMaterialised() {
		return isMaterialised;
	}

	public void setMaterialised(boolean isMaterialised) {
		this.isMaterialised = isMaterialised;
	}

	public void setPlanMaterialized(Iterator<Integer> planIterator) {
		this.setMaterialised(true);
		// if(planIterator.hasNext()){
		if (!this.children.isEmpty()) {
			Node op = children.get(planIterator.next());
			for (Node eq2 : op.getChildren()) {
				eq2.setPlanMaterialized(planIterator);
			}
		}
	}

	




	public int[] getAlgorithmicImplementations() {
		if (this.o instanceof NonUnaryWhereCondition) {
			int[] result = new int[1];
			// result[0]=RIGHTBROADCASTJOIN;
			// result[1]=LEFTBROADCASTJOIN;
			boolean centralised = false;
			/*
			 * if(this.children.size()==2){ for(Node c:children){
			 * if(c.nodeInfo!=null &&
			 * c.nodeInfo.getNumberOfTuples()*c.getNodeInfo().getTupleLength()>
			 * 300000){ centralised=false; break; } } }
			 */

			if (!centralised) {
				result[0] = REPARTITIONJOIN;
			} else {
				result[0] = CENTRALIZEDJOIN;
			}
			return result;
		} else {
			int[] result = new int[1];
			result[0] = this.opCode;
			return result;
		}
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;

	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	public boolean isDescendantOfBaseTable(String alias) {
		return this.descendantBaseTables.contains(alias);
	}

	public void addDescendantBaseTable(String alias) {
		this.descendantBaseTables.add(alias);
	}

	public Set<String> getDescendantBaseTables() {
		return this.descendantBaseTables;
	}

	public void addAllDescendantBaseTables(Set<String> aliases) {
		this.descendantBaseTables.addAll(aliases);
	}

	public int count(int i) {
		if (this.isMaterialised) {
			return 0;
		} else {
			if (this.type == Node.OR) {
				i++;
			}
			this.isMaterialised = true;
			for (Node c : this.children) {
				i += c.count(0);
			}
		}
		// TODO Auto-generated method stub
		return i;
	}




	@Override
	public String toString() {
		return "Node [opCode=" + opCode + ", o=" + o + "]";
	}

	

	public boolean isCommutativity() {
		return commutativity;
	}

	public void setCommutativity(boolean commutativity) {
		this.commutativity = commutativity;
	}

	public boolean isSwap() {
		return swap;
	}

	public void setSwap(boolean swap) {
		this.swap = swap;
	}

	


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package madgik.exareme.master.queryProcessor.decomposer.query;

import java.util.List;

import com.google.common.hash.HashCode;

public interface Operand extends Cloneable {

	public List<Column> getAllColumnRefs();

	/*
	 * changes the old column in every member of the operand with the new.
	 * Convinient way to change a column instead of deep copying the whole
	 * object
	 */
	public void changeColumn(Column oldCol, Column newCol);

	public Operand clone() throws CloneNotSupportedException;

}

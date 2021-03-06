package tool.compiler.java.ast;

import polyglot.ast.Node;
import polyglot.ast.NodeOps;
import tool.compiler.java.visit.EquGenerator;

public interface EquGenOps extends NodeOps {
	
	EquGenerator equGenEnter(EquGenerator v);
	Node equGenLeave(EquGenerator v);
	public String getKind();
	
}
package tool.compiler.java.visit;

import polyglot.types.Type;

public class MetaSetVariable extends AbsObjSet {
	
	private static long idGen = 1;
	private static final long NOID = -1;
	
	/**
	 * @param type
	 */
	public MetaSetVariable(Type type) {
		setType(type);
		generateID();
	}
	
	/**
	 * @see tool.compiler.java.visit.AbsObjSet#kind()
	 */
	@Override
	protected String kind() {
//		return "Χ";	// Capital Chi
//		return "χ";	// Small Letter Chi
		return "Chi";
	}
	
	/**
	 * @return the ID
	 */
	public String getID() {
		if(idNum() != NOID) {
			return super.getID();
		} else {
			return null;
		}
	}
	
	/**
	 * generate ID Number
	 */
	@Override
	protected long generateIDNum() {
		if(getType() != null && !getType().isVoid() && !getType().isNull()) {
			return idGen++;
		} else {
			return NOID;
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			if(getType().isNull()) {
				return "null";
			}
			
			String result = getType() + "{";
			if(!getType().isVoid()) {
				result += super.toString();
			} 
			return result + "}";
		} catch (NullPointerException e) {
			return "The type field of MetaSetVariable is null.";
		}
	}
}

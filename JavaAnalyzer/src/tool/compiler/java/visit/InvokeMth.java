package tool.compiler.java.visit;

import polyglot.ext.jl5.types.JL5ConstructorInstance;
import polyglot.ext.jl5.types.JL5MethodInstance;
import polyglot.ext.jl5.types.JL5ProcedureInstance;
import polyglot.types.Type;
import tool.compiler.java.util.CollUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
 */
public class InvokeMth implements Constraint {
	
	// fields
	
	/* ### Abstract Fields ###
	 * Type c;						// C
	 * SetVariable x;				// X
	 * MethodInstance m;			// method name m
	 * ArrayList<Type> ds;			// D1, ... , Dn
	 * ArrayList<SetVariable> xs;	// X1, ..., Xn
	 * (effect)						// effect
	 * Type e;						// E
	 * SetVariable y;				// Y
	 */
	
	/* ### Actual Fields ### */
	private AbsObjSet cx;				// C, X
	private JL5ProcedureInstance m;		// m
	private ArrayList<AbsObjSet> dxs;	// Ds, Xs ( D1{X1}, ..., Dn{Xn} )
	private AbsObjSet ey;				// E, Y
	
	
	// constructors
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(AbsObjSet cx, JL5ProcedureInstance m, Collection<? extends AbsObjSet> dxs, AbsObjSet ey) {
		super();
		this.cx = cx;
		this.m = m;
		if(dxs != null) {
			this.dxs = new ArrayList<AbsObjSet>(dxs);
		} else {
			this.dxs = null;
		}
		this.ey = ey;
	}
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(AbsObjSet cx, JL5MethodInstance m, Collection<? extends AbsObjSet> dxs, AbsObjSet ey) {
		this(cx, (JL5ProcedureInstance) m, dxs, ey);
	}
	
	/**
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param m		set m
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 */
	public InvokeMth(AbsObjSet cx, JL5ConstructorInstance m, Collection<? extends AbsObjSet> dxs, AbsObjSet ey) {
		this(cx, (JL5ProcedureInstance) m, dxs, ey);
	}
	
	
	// substitution methods
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 * @param cx	set C, X	( C{X} )
	 * @param dxs	set Ds, Xs	( D1{X1}, ..., Dn{Xn} )
	 * @param ey	set E, Y	( E{Y} )
	 * @return		Substituted New Constraint
	 */
	public InvokeMth subst(TypedSetVariable cx, Collection<TypedSetVariable> dxs, TypedSetVariable ey) {
		if(!this.cx.equalsForType(cx)) {
			throw new IllegalArgumentException("The Type Mismatch for cx. "
					+ "(orig: " + this.cx.getType() + ", subst: " + cx.getType() + ")");
		}
		
		if(this.dxs.size() != dxs.size()) {
			throw new IllegalArgumentException("The Size Mismatch for dxs.");
		}
		
		int i = 0;
		for(TypedSetVariable dx : dxs) {
			AbsObjSet thisdx = this.dxs.get(i);
			if(!thisdx.equalsForType(dx)) {
				throw new IllegalArgumentException("The Type Mismatch for dx" + ++i + ". "
						+ "(orig: " + thisdx.getType() + ", subst: " + dx.getType() + ")");
			}
			i++;
		}
		
		if(!this.ey.equalsForType(ey)) {
			throw new IllegalArgumentException("The Type Mismatch for ey. "
					+ "(orig: " + this.ey.getType() + ", subst: " + ey.getType() + ")");
		}
		
		return new InvokeMth(cx, this.m, dxs, ey);
	}
	
	/**
	 * Substitute TypedSetVariable for AbsObjSet<br>
	 * C{X}.f <: D{Y}
	 * @param cxdxsey	C{X} and D{Y}	(The size is 2)
	 * @return			Substituted New Constraint
	 */
	@Override
	public Constraint subst(Collection<TypedSetVariable> cxdxsey) {
		int size = 2 + this.dxs.size();
		if(cxdxsey.size() != size) {
			throw new IllegalArgumentException("The Size of tsvs must be " + size + ".");
		}
		LinkedList<TypedSetVariable> dxs = new LinkedList<>(cxdxsey);
		TypedSetVariable cx = dxs.removeFirst();
		TypedSetVariable ey = dxs.removeLast();
		return subst(cx, dxs, ey);
	}
	
	
	// getter methods
	
	/**
	 * @return the C{X}
	 */
	public AbsObjSet getCX() {
		return cx;
	}
	
	/**
	 * @return the C
	 */
	public Type getC() {
		return cx.getType();
	}
	
	/**
	 * @return the X
	 */
	public String getX() {
		return cx.getID();
	}
	
	/**
	 * @return the m
	 */
	public JL5ProcedureInstance getM() {
		return m;
	}
	
	/**
	 * @return D1{X1}, ..., Dn{Xn}
	 */
	public List<AbsObjSet> getDXs() {
		return dxs;
	}
	
	/**
	 * @param i	index
	 * @return Di{Xi}
	 */
	public AbsObjSet getDX(int i) {
		return dxs.get(i);
	}
	
	/**
	 * @return the E{Y}
	 */
	public AbsObjSet getEY() {
		return ey;
	}
	
	/**
	 * @return the E
	 */
	public Type getE() {
		return ey.getType();
	}
	
	/**
	 * @return the Y
	 */
	public String getY() {
		return ey.getID();
	}
	
	
	@Override
	public ArrayList<AbsObjSet> getAllAbsObjSet() {
		ArrayList<AbsObjSet> abss = new ArrayList<>();
		abss.add(cx);
		abss.addAll(dxs);
		abss.add(ey);
		return abss;
	}
	
	@Override
	public boolean contains(AbsObjSet aos) {
		if (cx.equals(aos)) {
			return true;
		}
		if (dxs.contains(aos)) {
			return true;
		}
		if (ey.equals(aos)) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Form:	C{X}.m <: (D1{X1}, ..., Dn{Xn}) -- effect --> E{Y}
	 */
	@Override
	public String toString() {
		return getCX() + "." + getName()
				+ " <: " + CollUtil.getStringOf(getDXs(), '(', ')') 
				+ " -- " + "effect" + " --> "	+ getEY();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cx == null) ? 0 : cx.hashCode());
		result = prime * result + ((m == null) ? 0 : m.hashCode());
		if(dxs != null) {
			for(AbsObjSet dx : dxs) {
				result = prime * result + ((dx == null) ? 0 : dx.hashCode());
			}
		}
		result = prime * result + ((ey == null) ? 0 : ey.hashCode());
		return result;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		InvokeMth other = (InvokeMth) obj;
		if (cx == null) {
			if (other.cx != null) {
				return false;
			}
		} else if (!cx.equals(other.cx)) {
			return false;
		}
		if (m == null) {
			if (other.m != null) {
				return false;
			}
		} else if (!m.equals(other.m)) {
			return false;
		}
		if (dxs == null) {
			if (other.dxs != null) {
				return false;
			}
		} else if (!dxs.equals(other.dxs)) {
			return false;
		}
		if (ey == null) {
			if (other.ey != null) {
				return false;
			}
		} else if (!ey.equals(other.ey)) {
			return false;
		}
		return true;
	}
	
	
	protected final String getName() {
		return getM() instanceof JL5MethodInstance ? ((JL5MethodInstance)getM()).name() : getM().container().toString();
	}
	
	public boolean isConstructor() {
		return m instanceof JL5ConstructorInstance;
	}
}
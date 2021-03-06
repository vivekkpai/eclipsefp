/**
 * (c) 2011, Alejandro Serrano
 * Released under the terms of the EPL.
 */
package net.sf.eclipsefp.haskell.browser.items;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a function signature.
 * 
 * @author Alejandro Serrano
 */
public class Function extends Declaration {
	String signature;

	public Function(String doc, String name, String signature) {
		this.setDoc(doc);
		this.setName(name);
		this.setType(DeclarationType.FUNCTION);
		this.signature = signature;
	}

	public Function(String name, JSONObject o) throws JSONException {
		this.setDoc(o);
		this.setType(DeclarationType.FUNCTION);
		this.setName(name);
		this.signature = o.getString("signature");
	}

	public String getSignature() {
		return this.signature;
	}
	
	@Override
	public String getCompleteDefinition() {
		StringBuilder builder = new StringBuilder(this.getName());
		builder.append(" :: ");
		builder.append(this.getSignature());
		return builder.toString();
	}
	
	@Override
	public String getShownName() {
		return this.name + " :: " + this.signature;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.eclipsefp.haskell.browser.items.Documented#isType()
	 */
	@Override
	public boolean isType() {
		return false;
	}
}

/** 
 * Copyright (c) 2013 by JP Moresmau
 * This code is made available under the terms of the Eclipse Public License,
 * version 1.0 (EPL). See http://www.eclipse.org/legal/epl-v10.html
 */
package net.sf.eclipsefp.haskell.style;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author JP Moresmau
 *
 */
public class AllTests_Suite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new JUnit4TestAdapter(SHConfigurationTest.class));
		return suite;
	}
}
// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
// Copyright (c) 2011 by Alejandro Serrano
package net.sf.eclipsefp.haskell.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.eclipsefp.haskell.core.expressions.HaskellPropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author The mighty PDE wizard
 */
public class HaskellCorePlugin extends Plugin {

  // extension points
	public static final String ID_EXT_POINT_CABAL_CONTRIBUTORS = "cabalContributors"; //$NON-NLS-1$
	public static final String ID_PROBLEM_MARKER = "net.sf.eclipsefp.haskell.core.problem"; //$NON-NLS-1$
	public static final String ID_PROJECT_PROBLEM_MARKER = "net.sf.eclipsefp.haskell.core.projectProblem"; //$NON-NLS-1$
	public static final String ID_HLINT_MARKER = "net.sf.eclipsefp.haskell.core.hlint"; //$NON-NLS-1$
	public static final String ID_SCION_MARKER = "net.sf.eclipsefp.haskell.core.scionProblem"; //$NON-NLS-1$
	public static final String ID_ALEX_MARKER = "net.sf.eclipsefp.haskell.core.alex"; //$NON-NLS-1$
	public static final String ID_HAPPY_MARKER = "net.sf.eclipsefp.haskell.core.happy"; //$NON-NLS-1$
	public static final String ID_UUAGC_MARKER = "net.sf.eclipsefp.haskell.core.uuagc"; //$NON-NLS-1$

	public static final String ATT_HLINT_SUGGESTION = "suggestion"; //$NON-NLS-1$

	//private static final String ATT_ID = "id"; //$NON-NLS-1$
	public static final String ATT_CLASS = "class"; //$NON-NLS-1$

	private static HaskellCorePlugin plugin;

	/**
	 * maintains the set of files opened in editors, so that we know that the editor will manage a lot: triggering the synchronize, etc
	 */
	private static Set<IFile> modifiedByEditors=Collections.synchronizedSet( new HashSet<IFile>() );

	public HaskellCorePlugin() {
	  plugin = this;
	}


	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);

		Platform.getAdapterManager().registerAdapters(new HaskellPropertyTester(), IResource.class);
	}

	/**
	 * <p>
	 * returns the shared instance.
	 * </p>
	 */
	public static HaskellCorePlugin getDefault() {
		return plugin;
	}


  public static Set<IFile> getModifiedByEditors() {
    return modifiedByEditors;
  }

	public static String getPluginId() {
	  HaskellCorePlugin thePlugin = getDefault();

	  if (thePlugin != null) {
	    Bundle theBundle = thePlugin.getBundle();
	    if (theBundle != null) {
	      return theBundle.getSymbolicName();
	    }
	  }

	  // Otherwise...
		return "net.sf.eclipsefp.haskell.core.test"; //$NON-NLS-1$
	}


	// logging and tracing
	// ////////////////////

	/**
	 * Logs a message to the plugin's error log
	 *
	 * @param message	Message to be logger
	 * @param severity	One of IStatus.ERROR, IStatus.CANCEL, IStatus.INFO,
	 * 					IStatus.OK or IStatus.WARNING
	 */
	public static void log( final String message, final int severity ) {
    String id = getPluginId();
    Status status = new Status( severity, id, IStatus.OK, message, null );
    getDefault().getLog().log( status );
  }

  public static void log( final String message, final Throwable thr ) {
    String id = getPluginId();
    Status status = new Status( IStatus.ERROR, id, IStatus.OK, message, thr );
    getDefault().getLog().log( status );
  }

  public static void log( final Throwable thr ) {
    String msg = thr.getMessage() == null ? "[No detail]" : thr.getMessage(); //$NON-NLS-1$
    log( msg, thr );
  }

  public static boolean isTracing( final String optionId ) {
    String option = getPluginId() + "/" + optionId; //$NON-NLS-1$
    String value = Platform.getDebugOption( option );
    return value != null && value.trim().equalsIgnoreCase( "true" ); //$NON-NLS-1$
  }

	public static void dump( final List<String> cmdLine ) {
    StringBuffer sb = new StringBuffer();
    Iterator<String> iter = cmdLine.iterator();
    while( iter.hasNext() ) {
      sb.append( iter.next() );
      sb.append( " " ); //$NON-NLS-1$
    }
    System.out.println( sb.toString() );
  }


	// helping methods
	// ////////////////


	public IConfigurationElement[] getExtensions(final String key) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		return registry.getConfigurationElementsFor(getPluginId(), key);
	}

	/** Get an instance-scoped preference store for the plug-in */
	public static final IEclipsePreferences instanceScopedPreferences() {
	  return InstanceScope.INSTANCE.getNode( getPluginId() );
	}

	/**
	 * populate a template with variables
	 * @param pref the preference to use to get the template
	 * @param extraVars the extra variables
	 * @return
	 */
	public static String populateTemplate(final String pref,final Map<String,String> extraVars){
	  IStringVariableManager mgr=VariablesPlugin.getDefault().getStringVariableManager();
    IEclipsePreferences coreNode = instanceScopedPreferences();
    // get template
    String template=coreNode.get( pref, "" ); //$NON-NLS-1$

    try {
      IValueVariable[] vars = new IValueVariable[0];
      // add extra vars
      if (extraVars!=null){
        vars = new IValueVariable[extraVars.size()];
        int a=0;
        for (String key:extraVars.keySet()){
          String val=extraVars.get( key );
          vars[a++]=mgr.newValueVariable( key, "",true,val );//$NON-NLS-1$

        }
        mgr.addVariables( vars );
      }
      try {
        // perform the substitution
        return mgr.performStringSubstitution( template );
      } finally {
        // remove variables because otherwise we can't reset them
        mgr.removeVariables( vars );
      }
    } catch (CoreException ce){
      HaskellCorePlugin.log( ce );
    }
    return "";//$NON-NLS-1$
	}
}
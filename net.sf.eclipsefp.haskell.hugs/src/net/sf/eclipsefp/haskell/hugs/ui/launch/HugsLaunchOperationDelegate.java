// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.hugs.ui.launch;

import java.util.ArrayList;
import java.util.List;
import net.sf.eclipsefp.haskell.core.HaskellCorePlugin;
import net.sf.eclipsefp.haskell.debug.core.internal.launch.IInteractiveLaunchOperationDelegate;
import net.sf.eclipsefp.haskell.hugs.HugsPlugin;
import net.sf.eclipsefp.haskell.hugs.core.Util;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

/** <p>implements a delegate for launching HUGS.</p>
  *
  * @author Leif Frenzel
  */
public class HugsLaunchOperationDelegate
       implements IInteractiveLaunchOperationDelegate {

  // interface methods of IInteractiveLaunchOperationDelegate
  ///////////////////////////////////////////////////////////

  @Override
  public String[] createArguments( final IProject hsProject,
                                   final IFile[] selectedFiles,final String mode ) {
    List<String> cmdLine = new ArrayList<>();

    String libPath = Util.constructLibPath( selectedFiles );
    if( !libPath.equals( "" ) ) {
      cmdLine.add( libPath );
    }
    addAll( cmdLine, selectedFiles );
    String[] result = new String[ cmdLine.size() ];
    cmdLine.toArray( result );
    if( HugsPlugin.isTracing() ) {
      System.out.println( "Launching HUGS with arguments:" );
      HaskellCorePlugin.dump( cmdLine );
    }
    return result;
  }

  private void addAll( final List<String> cmdLine,
                       final IFile[] selectedFiles ) {
    for( int i = 0; i < selectedFiles.length; i++ ) {
      String path = selectedFiles[ i ].getLocation().toOSString();
      cmdLine.add( "\"" + path + "\"" );
    }
  }

  @Override
  public String getExecutable() {
    return Util.getCompilerExecutable();
  }

  @Override
  public String getReloadCommand() {
    return ":reload";
  }

}

// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.ui.dialog;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

import net.sf.eclipsefp.haskell.ui.util.DefaultStatus;


/** <p>validates the selection: only source folders can be selected (the 
  * exception is when the project is used as source folder).</p>
  * 
  * @author Leif Frenzel
  */
class SourceFolderValidator implements ISelectionStatusValidator {

  private static final IStatus OK = new DefaultStatus( IStatus.OK );
  private static final IStatus ERROR = new DefaultStatus( IStatus.ERROR );
  
  
  // interface methods of ISelectionStatusValidator
  /////////////////////////////////////////////////
  
  @Override
  public IStatus validate( final Object[] selection ) {
    IStatus result = ERROR;
    if( isOk( selection ) ) {
      result = OK;
    }
    // TODO check the case where the project is the source folder
    return result;
  }
  
  
  // helping methods
  //////////////////
  private boolean isOk( final Object[] selection ) {
    return    selection != null 
           && selection.length > 0 
           && selection[ 0 ] instanceof IFolder;
  }
}
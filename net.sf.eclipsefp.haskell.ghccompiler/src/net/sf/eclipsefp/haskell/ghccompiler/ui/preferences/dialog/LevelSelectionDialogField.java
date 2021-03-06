// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.ghccompiler.ui.preferences.dialog;

import net.sf.eclipsefp.common.ui.dialog.DialogField;
import net.sf.eclipsefp.haskell.ghccompiler.core.GhcParameter;
import net.sf.eclipsefp.haskell.ghccompiler.ui.internal.util.UITexts;
import net.sf.eclipsefp.haskell.ghccompiler.ui.preferences.ParamsUITexts;
import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

/** <p>dialog field for the selection of compiler optimization levels.</p>
  *
  * @author Leif Frenzel
  */
public class LevelSelectionDialogField extends DialogField {

  private int selected = -1;
  // ui elements
  private Button[] buttons;

  public LevelSelectionDialogField( final Composite parent ) {
    super( parent );
    setLayout( new GridLayout( 1, false ) );
    createLabel();
    createButtons();
  }


  // interface methods of DialogField
  ///////////////////////////////////

  @Override
  public Object getInfo() {
    return new Integer( selected );
  }

  @Override
  public void setInfo( final Object info ) {
    int newSelected = ( ( Integer )info ).intValue();
    Assert.isTrue( newSelected >= -1 && newSelected <= 2 );
    selected = newSelected;
    applySelection();
  }


  // UI creation methods
  //////////////////////

  private Button createButton( final Composite parent, final String prefKey ) {
    String text = ParamsUITexts.getShortDescription( prefKey );
    String tooltip = text + "\n" + ParamsUITexts.getOption( prefKey ); //$NON-NLS-1$
    return createButton( parent, text, tooltip );
  }

  private Button createButton( final Composite parent,
                               final String text,
                               final String tooltip) {
    Button result = new Button( parent, SWT.RADIO | SWT.LEFT );
    result.setText( text );
    result.setToolTipText( tooltip );
    return result;
  }

  private void createButtons() {

    String text = UITexts.levelSelectionDialogField_zeroText;
    String tooltip = UITexts.levelSelectionDialogField_zeroTooltip;

    buttons = new Button[ 4 ];
    buttons[ 0 ] = createButton( this, text, tooltip );
    buttons[ 1 ] = createButton( this, GhcParameter.OPT_O0.getName() );
    buttons[ 2 ] = createButton( this, GhcParameter.OPT_O1.getName() );
    buttons[ 3 ] = createButton( this, GhcParameter.OPT_O2.getName() );
    SelectionListener li = new SelectionAdapter() {
      @Override
      public void widgetSelected( final SelectionEvent evt ) {
        Widget widget = evt.widget;
        int newSelected = -1;
        if( widget == buttons[ 1 ] ) {
          newSelected = 0;
        } else if( widget == buttons[ 2 ] ) {
          newSelected = 1;
        } else if( widget == buttons[ 3 ] ) {
          newSelected = 2;
        }
        selected = newSelected;
        notifyListeners( new Integer( newSelected ) );
      }
    };
    for( int i = 0; i < buttons.length; i++ ) {
      buttons[ i ].addSelectionListener( li );
    }
  }

  private void createLabel() {
    Label label = new Label( this, SWT.WRAP );
    label.setText( UITexts.levelSelectionDialogField_levelInfo );
  }


  // helping methods
  //////////////////

  private void applySelection() {

    for (int a=0;a<buttons.length;a++){
      buttons[a].setSelection(a==selected+1);
    }

  }
}
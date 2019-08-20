/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Hitachi Vantara : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.di.ui.trans.steps.StanfordNer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.StanfordNer.StanfordNerPluginMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.ComboVar;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import java.util.*;
import java.util.List;

public class StanfordNerPluginDialog extends BaseStepDialog implements StepDialogInterface {

    private static Class<?> PKG = StanfordNerPluginDialog.class; // for i18n purposes, needed by Translator2!!


    private Label wlServerHost;
    private TextVar wServerHost;

    private Label wlServerPort;
    private TextVar wServerPort;

    private Label wlNname;
    private TextVar wNname;

    private Label wlOrganization;
    private TextVar wOrganization;

    private Label wlLocaltion;
    private TextVar wLocaltion;

    private Label wlContentField;
    private ComboVar wContentField;

    private Label wlUrls;
    private TextVar wUrls;

    private StanfordNerPluginMeta input;

    private boolean gotPreviousFields = false;

    private String[] fieldNames;

    private Map<String, Integer> inputFields;

    public StanfordNerPluginDialog( Shell parent, Object in, TransMeta transMeta, String sname ) {
        super( parent, (BaseStepMeta) in, transMeta, sname );
        input = (StanfordNerPluginMeta) in;
        inputFields = new HashMap<String, Integer>();
    }
    @Override
    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN );
        props.setLook( shell );
        setShellImage( shell, input );

        ModifyListener lsMod = new ModifyListener() {
            public void modifyText( ModifyEvent e ) {
                input.setChanged();
            }
        };
        changed = input.hasChanged();

        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout( formLayout );
        shell.setText("命名实体提取" );

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        // Filename line
        wlStepname = new Label( shell, SWT.RIGHT );
        wlStepname.setText( "步骤名称：" );
        props.setLook( wlStepname );
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment( 0, 0 );
        fdlStepname.right = new FormAttachment( middle, -margin );
        fdlStepname.top = new FormAttachment( 0, margin );
        wlStepname.setLayoutData( fdlStepname );
        wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        wStepname.setText( stepname );
        props.setLook( wStepname );
        wStepname.addModifyListener( lsMod );
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment( middle, 0 );
        fdStepname.top = new FormAttachment( 0, margin );
        fdStepname.right = new FormAttachment( 100, 0 );
        wStepname.setLayoutData( fdStepname );
        Control lastControl = wStepname;

        wlServerHost = new Label( shell, SWT.RIGHT );
        wlServerHost.setText( "ServerHost："  );
        props.setLook( wlServerHost );
        FormData fdlServerHost = new FormData();
        fdlServerHost.left = new FormAttachment( 0, 0 );
        fdlServerHost.right = new FormAttachment( middle, -margin );
        fdlServerHost.top = new FormAttachment( lastControl, margin );
        wlServerHost.setLayoutData( fdlServerHost );
        wServerHost = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wServerHost );
        wServerHost.addModifyListener( lsMod );
        FormData fdServerHost= new FormData();
        fdServerHost.left = new FormAttachment( middle, 0 );
        fdServerHost.top = new FormAttachment( lastControl, margin );
        fdServerHost.right = new FormAttachment( 100, 0 );
        wServerHost.setLayoutData( fdServerHost );
        lastControl = wServerHost;

        wlServerPort = new Label( shell, SWT.RIGHT );
        wlServerPort.setText( "ServerPort："  );
        props.setLook( wlServerPort );
        FormData fdlServerPort = new FormData();
        fdlServerPort.left = new FormAttachment( 0, 0 );
        fdlServerPort.right = new FormAttachment( middle, -margin );
        fdlServerPort.top = new FormAttachment( lastControl, margin );
        wlServerPort.setLayoutData( fdlServerPort );
        wServerPort = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wServerPort );
        wServerPort.addModifyListener( lsMod );
        FormData fdServerPort = new FormData();
        fdServerPort.left = new FormAttachment( middle, 0 );
        fdServerPort.top = new FormAttachment( lastControl, margin );
        fdServerPort.right = new FormAttachment( 100, 0 );
        wServerPort.setLayoutData( fdServerPort );
        lastControl = wServerPort;

        wlNname = new Label( shell, SWT.RIGHT );
        wlNname.setText("人名："  );
        props.setLook( wlNname );
        FormData fdlNname = new FormData();
        fdlNname.left = new FormAttachment( 0, 0 );
        fdlNname.right = new FormAttachment( middle, -margin );
        fdlNname.top = new FormAttachment( lastControl, margin );
        wlNname.setLayoutData( fdlNname );
        wNname = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wNname );
        wNname.addModifyListener( lsMod );
        FormData fdNname = new FormData();
        fdNname.left = new FormAttachment( middle, 0 );
        fdNname.top = new FormAttachment( lastControl, margin );
        fdNname.right = new FormAttachment( 100, 0 );
        wNname.setLayoutData( fdNname );
        lastControl = wNname;

        wlOrganization = new Label( shell, SWT.RIGHT );
        wlOrganization.setText( "组织："  );
        props.setLook( wlOrganization );
        FormData fdlOrganization = new FormData();
        fdlOrganization.left = new FormAttachment( 0, 0 );
        fdlOrganization.right = new FormAttachment( middle, -margin );
        fdlOrganization.top = new FormAttachment( lastControl, margin );
        wlOrganization.setLayoutData( fdlOrganization );
        wOrganization = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wOrganization );
        wOrganization.addModifyListener( lsMod );
        FormData fdOrganization = new FormData();
        fdOrganization.left = new FormAttachment( middle, 0 );
        fdOrganization.top = new FormAttachment( lastControl, margin );
        fdOrganization.right = new FormAttachment( 100, 0 );
        wOrganization.setLayoutData( fdOrganization );
        lastControl = wOrganization;

        wlLocaltion = new Label( shell, SWT.RIGHT );
        wlLocaltion.setText( "地址：");
        props.setLook( wlLocaltion );
        FormData fdlLocaltion = new FormData();
        fdlLocaltion.left = new FormAttachment( 0, 0 );
        fdlLocaltion.right = new FormAttachment( middle, -margin );
        fdlLocaltion.top = new FormAttachment( lastControl, margin );
        wlLocaltion.setLayoutData( fdlLocaltion );
        wLocaltion = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wLocaltion );
        wLocaltion.addModifyListener( lsMod );
        FormData fdLocaltion = new FormData();
        fdLocaltion.left = new FormAttachment( middle, 0 );
        fdLocaltion.top = new FormAttachment( lastControl, margin );
        fdLocaltion.right = new FormAttachment( 100, 0 );
        wLocaltion.setLayoutData( fdLocaltion );
        lastControl = wLocaltion;

        wlUrls = new Label( shell, SWT.RIGHT );
        wlUrls.setText( "URL：");
        props.setLook( wlUrls );
        FormData fdlUrls = new FormData();
        fdlUrls.left = new FormAttachment( 0, 0 );
        fdlUrls.right = new FormAttachment( middle, -margin );
        fdlUrls.top = new FormAttachment( lastControl, margin );
        wlUrls.setLayoutData( fdlUrls );
        wUrls = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
        props.setLook( wUrls );
        wUrls.addModifyListener( lsMod );
        FormData fdUrls = new FormData();
        fdUrls.left = new FormAttachment( middle, 0 );
        fdUrls.top = new FormAttachment( lastControl, margin );
        fdUrls.right = new FormAttachment( 100, 0 );
        wUrls.setLayoutData( fdUrls );
        lastControl = wUrls;

        wlContentField = new Label( shell, SWT.RIGHT );
        wlContentField.setText( "选择识别字段：");
        props.setLook( wlContentField );
        FormData fdlContentField = new FormData();
        fdlContentField.left = new FormAttachment( 0, 0 );
        fdlContentField.right = new FormAttachment( middle, -margin );
        fdlContentField.top = new FormAttachment( lastControl, margin );
        wlContentField.setLayoutData( fdlContentField );
        wContentField = new ComboVar( transMeta, shell, SWT.BORDER | SWT.READ_ONLY );
        wContentField.setEditable( true );
        props.setLook( wContentField );
        wContentField.addModifyListener( lsMod );
        FormData fdContentField = new FormData();
        fdContentField.left = new FormAttachment( middle, 0 );
        fdContentField.top = new FormAttachment( lastControl, margin );
        fdContentField.right = new FormAttachment( 100, -margin );
        wContentField.setLayoutData( fdContentField );
        wContentField.addFocusListener( new FocusListener() {
            public void focusLost( org.eclipse.swt.events.FocusEvent e ) {
            }

            public void focusGained( org.eclipse.swt.events.FocusEvent e ) {
                Cursor busy = new Cursor( shell.getDisplay(), SWT.CURSOR_WAIT );
                shell.setCursor( busy );
                setStreamFields();
                shell.setCursor( null );
                busy.dispose();
            }
        } );

        final Runnable runnable = new Runnable() {
            public void run() {
                StepMeta stepMeta = transMeta.findStep( stepname );
                if ( stepMeta != null ) {
                    try {
                        RowMetaInterface row = transMeta.getPrevStepFields( stepMeta );

                        // Remember these fields...
                        for ( int i = 0; i < row.size(); i++ ) {
                            inputFields.put( row.getValueMeta( i ).getName(), Integer.valueOf( i ) );
                        }

                        setComboBoxes();
                    } catch ( KettleException e ) {
                        logError( BaseMessages.getString( PKG, "run 异常" ) );
                    }
                }
            }
        };
        new Thread( runnable ).start();
        wOK = new Button( shell, SWT.PUSH );
        wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
        wCancel = new Button( shell, SWT.PUSH );
        wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

        setButtonPositions( new Button[] { wOK, wCancel }, margin, null );

        // Add listeners
        lsOK = new Listener() {
            public void handleEvent( Event e ) {
                ok();
            }
        };
        lsCancel = new Listener() {
            public void handleEvent( Event e ) {
                cancel();
            }
        };
        wOK.addListener( SWT.Selection, lsOK );
        wCancel.addListener( SWT.Selection, lsCancel );
        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected( SelectionEvent e ) {
                ok();
            }
        };

        wStepname.addSelectionListener( lsDef );

        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener( new ShellAdapter() {
            public void shellClosed( ShellEvent e ) {
                cancel();
            }
        } );

        // Set the shell size, based upon previous time...
        setSize();

        getData();
        input.setChanged( changed );

        shell.open();
        while ( !shell.isDisposed() ) {
            if ( !display.readAndDispatch() ) {
                display.sleep();
            }
        }
        return stepname;
    }

    protected void setComboBoxes() {
        // Something was changed in the row.
        //
        final Map<String, Integer> fields = new HashMap<String, Integer>();

        // Add the currentMeta fields...
        fields.putAll( inputFields );

        Set<String> keySet = fields.keySet();
        List<String> entries = new ArrayList<String>( keySet );

        fieldNames = entries.toArray( new String[entries.size()] );

        Const.sortStrings( fieldNames );
    }


    private void setStreamFields() {
        if ( !gotPreviousFields ) {
            String urlfield = wContentField.getText();
            wContentField.removeAll();
            wContentField.setItems( fieldNames );
            if ( urlfield != null ) {
                wContentField.setText( urlfield );
            }

            gotPreviousFields = true;
        }
    }

    public void getData() {
        if ( isDebug() ) {
            logDebug( "getting fields info..." );
        }

        wServerPort.setText( Const.NVL( input.getServerPort(), "" )  );
        wServerHost.setText( Const.NVL( input.getServerHost(), "" )  );
        wNname.setText( Const.NVL( input.getPeopleName(), "" ) );
        wOrganization.setText( Const.NVL( input.getOrganization(), "" ) );
        wLocaltion.setText( Const.NVL( input.getLocaltion(), "" ) );
        wContentField.setText( Const.NVL( input.getContent(), "" ) );
        wUrls.setText( Const.NVL( input.getURLs(), "" ) );

        wStepname.selectAll();
        wStepname.setFocus();
    }

    private void cancel() {
        stepname = null;
        input.setChanged( changed );
        dispose();
    }

    private void ok() {
        if ( Utils.isEmpty( wStepname.getText() ) ) {
            return;
        }

        stepname = wStepname.getText(); // return value

        try {
            getInfo( new StanfordNerPluginMeta() ); // to see if there is an exception
            getInfo( input ); // to put the content on the input structure for real if all is well.
            dispose();
        } catch ( KettleException e ) {
            new ErrorDialog(
                    shell, BaseMessages.getString( PKG, "ok 异常" ), BaseMessages
                    .getString( PKG, "ok 异常" ), e );
        }
    }

    private void getInfo( StanfordNerPluginMeta meta ) throws KettleException {
        meta.setServerHost(wServerHost.getText());
        meta.setServerPort(wServerPort.getText());
        meta.setPeopleName(wNname.getText());
        meta.setOrganization(wOrganization.getText());
        meta.setLocaltion(wLocaltion.getText());
        meta.setContent(wContentField.getText());
        meta.setURLs(wUrls.getText());
    }
}

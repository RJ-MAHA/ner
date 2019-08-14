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

package org.pentaho.di.trans.steps.StanfordNer;

import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.*;
import org.pentaho.di.core.annotations.*;
import org.pentaho.di.core.database.*;
import org.pentaho.di.core.exception.*;
import org.pentaho.di.core.row.*;
import org.pentaho.di.core.row.value.*;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.core.variables.*;
import org.pentaho.di.core.xml.*;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.*;
import org.pentaho.di.trans.step.*;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.*;

import java.util.List;
import java.util.*;

/*
 * Created on 02-jun-2003
 *
 */

@Step( id = "StanfordNerStep",
      image = "lirenjun.svg",
      i18nPackageName = "org.pentaho.di.trans.steps.StanfordNer",
      name = "StanfordNerStep.name",
      description = "StanfordNerStep.description",
      categoryDescription = "StanfordNerStep.categoryDescription" )
public class StanfordNerPluginMeta extends BaseStepMeta implements StepMetaInterface {
  private static Class<?> PKG = StanfordNerPluginMeta.class;

  private String serverHost;
  private String serverPort;
  private String peopleName;
  private String organization;
  private String localtion;
  private String Content;


  public void allocate( int nrkeys ) {
    serverHost = new String();
    serverPort = new String();
    peopleName = new String();
    organization = new String();
    localtion = new String();
    Content = new String();

  }

  public String getContent() {
    return Content;
  }

  public void setContent(String content) {
    Content = content;
  }

  @Override
  public void setDefault() {
    serverHost = null;
    serverPort = null;
    peopleName = null;
    organization = null;
    localtion = null;
    Content = null;
    int nrkeys = 0;
    allocate( nrkeys );
  }

  @Override
  public Object clone() {
    StanfordNerPluginMeta retval = (StanfordNerPluginMeta) super.clone();
    retval.allocate( 1 );
    retval.serverHost = serverHost;
    retval.serverPort = serverPort;
    retval.peopleName = peopleName;
    retval.organization = organization;
    retval.localtion = localtion;
    retval.Content = Content;
    return retval;
  }


  private void readData( Node stepnode ) throws KettleXMLException {
    try {

      allocate( 1 );
      serverHost = Const.NVL( XMLHandler.getTagValue( stepnode, "serverHost" ), "" );
      serverPort = Const.NVL( XMLHandler.getTagValue( stepnode, "serverPort" ), "" );
      peopleName = Const.NVL( XMLHandler.getTagValue( stepnode, "peopleName" ), "" );
      organization = Const.NVL( XMLHandler.getTagValue( stepnode, "organization" ), "" );
      localtion = Const.NVL( XMLHandler.getTagValue( stepnode, "localtion" ), "" );
      Content = Const.NVL( XMLHandler.getTagValue( stepnode, "Content" ), "" );
    } catch ( Exception e ) {
      throw new KettleXMLException( BaseMessages.getString(
              PKG, "readData 异常" ), e );
    }
  }

  @Override
  public void getFields( RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
                         VariableSpace space, Repository repository, IMetaStore metaStore ) throws KettleStepException {
    if ( peopleName != null ) {
      ValueMetaInterface v =
              new ValueMetaString( space.environmentSubstitute( peopleName ) );
      v.setLength( 100, -1 );
      v.setOrigin( name );
      inputRowMeta.addValueMeta( v );
    }
    if ( organization != null ) {
      ValueMetaInterface v =
              new ValueMetaString( space.environmentSubstitute( organization ) );
      v.setLength( 100, -1 );
      v.setOrigin( name );
      inputRowMeta.addValueMeta( v );
    }
    if ( localtion != null ) {
      ValueMetaInterface v =
              new ValueMetaString( space.environmentSubstitute( localtion ) );
      v.setLength( 100, -1 );
      v.setOrigin( name );
      inputRowMeta.addValueMeta( v );
    }
//    if ( serverHost != null ) {
//      ValueMetaInterface v =
//              new ValueMetaString( space.environmentSubstitute( serverHost ) );
//      v.setLength( 100, -1 );
//      v.setOrigin( name );
//      inputRowMeta.addValueMeta( v );
//    }
//    if ( serverPort != null ) {
//      ValueMetaInterface v =
//              new ValueMetaString( space.environmentSubstitute( serverPort ) );
//      v.setLength( 100, -1 );
//      v.setOrigin( name );
//      inputRowMeta.addValueMeta( v );
//    }
  }


  @Override
  public String getXML() {
    StringBuilder retval = new StringBuilder( 400 );
    retval.append( "    " ).append( XMLHandler.addTagValue( "serverHost", serverHost ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "serverPort", serverPort ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "peopleName", peopleName ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "organization", organization ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "localtion", localtion ) );
    retval.append( "    " ).append( XMLHandler.addTagValue( "Content", Content ) );
    return retval.toString();
  }



  @Override
  public void readRep( Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases ) throws KettleException {
    try {
      allocate( 1 );
      serverHost = rep.getStepAttributeString( id_step, "serverHost" );
      serverPort = rep.getStepAttributeString( id_step, "serverPort" );
      peopleName = rep.getStepAttributeString( id_step, "peopleName" );
      organization = rep.getStepAttributeString( id_step, "organization" );
      localtion = rep.getStepAttributeString( id_step, "localtion" );
      Content = rep.getStepAttributeString( id_step, "Content" );
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString(
              PKG, "readRep 异常" ), e );
    }
  }




  @Override
  public void saveRep( Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step ) throws KettleException {
    try {
      rep.saveStepAttribute( id_transformation, id_step,  "serverHost", serverHost );
      rep.saveStepAttribute( id_transformation, id_step,  "serverPort", serverPort );
      rep.saveStepAttribute( id_transformation, id_step,  "peopleName", peopleName );
      rep.saveStepAttribute( id_transformation, id_step,  "organization", organization );
      rep.saveStepAttribute( id_transformation, id_step,  "localtion", localtion );
      rep.saveStepAttribute( id_transformation, id_step,  "Content", Content );
    } catch ( Exception e ) {
      throw new KettleException( BaseMessages.getString(
              PKG, "saveRep 异常" )
              + id_step, e );
    }
  }

  @Override
  public void loadXML( Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore ) throws KettleXMLException {
    readData( stepnode );
  }
  @Override
  public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int i, TransMeta transMeta, Trans trans) {
    return new StanfordNerPlugin( stepMeta, stepDataInterface, i, transMeta, trans );
  }

  @Override
  public StepDataInterface getStepData() {
    return new StanfordNerPluginData();
  }


  public String getPeopleName() {
    return peopleName;
  }

  public void setPeopleName(String peopleName) {
    this.peopleName = peopleName;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getLocaltion() {
    return localtion;
  }

  public void setLocaltion(String localtion) {
    this.localtion = localtion;
  }


  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public String getServerPort() {
    return serverPort;
  }

  public void setServerPort(String serverPort) {
    this.serverPort = serverPort;
  }
}

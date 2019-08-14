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

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;




/*
 * Created on 2-jun-2003
 *
 */

public class StanfordNerPlugin extends BaseStep implements StepInterface {
  private StanfordNerPluginData data;
  private StanfordNerPluginMeta meta;

  public StanfordNerPlugin(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis ) {
    super( s, stepDataInterface, c, t, dis );
  }

  @Override
  public boolean processRow( StepMetaInterface smi, StepDataInterface sdi ) throws KettleException {
    meta = (StanfordNerPluginMeta) smi;
    data = (StanfordNerPluginData) sdi;

    Object[] r = getRow();    // get row, blocks when needed!
    if ( r == null ) { // no more input to be expected...
      setOutputDone();
      return false;
    }

    if ( first ) {
      first = false;
      data.inputRowMeta = getInputRowMeta();
      data.outputRowMeta = getInputRowMeta().clone();
      meta.getFields( data.outputRowMeta, getStepname(), null, null, this, repository, metaStore );

    }
    data.ServerHost = environmentSubstitute( meta.getServerHost() );
    data.ServerPort = environmentSubstitute( meta.getServerPort() );
    String Content = (String) r[data.inputRowMeta.indexOfValue(meta.getContent())];
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json;charset=UTF-8");
    HttpEntity<MultiValueMap<String, String>> Entity = new HttpEntity(Content, headers);
    String SERV_NER_URL = "http://"+data.ServerHost+":"+data.ServerPort+"?properties=%7B%22annotators%22%3A%20%22tokenize%2Cssplit%2Cner%2Cregexner%22%2C%20%22date%22%3A%20%222019-08-12T17%3A44%3A24%22%7D&pipelineLanguage=zh";

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<JSONObject> response = restTemplate
            .postForEntity(SERV_NER_URL, Entity, JSONObject.class);

//    Object extraValue = meta.getValue().getValueData();

//    Object[] outputRow = RowDataUtil.addValueData( r, data.outputRowMeta.size() - 1, extraValue );

//    putRow( data.outputRowMeta, outputRow );     // copy row to possible alternate rowset(s).
    JSONObject body = response.getBody();
    ArrayList<HashMap> sentences = (ArrayList<HashMap>) body.get("sentences");
    ArrayList<HashMap> hashMap = (ArrayList) sentences.get(0).get("entitymentions");
    ArrayList<String> persons =new ArrayList<>();
    ArrayList<String> ORGANIZATION =new ArrayList<>();
    ArrayList<String> LOCATION_DEFINE =new ArrayList<>();
//    java.util.HashMap entitymentions = (java.util.HashMap) ((java.util.HashMap)((ArrayList)( response.getBody()).get("sentences")).get(0)).get("entitymentions");
    for(HashMap hp:hashMap){
      if(hp.get("ner").toString().equals("PERSON")){
        persons.add(hp.get("text").toString());
      }
      if(hp.get("ner").toString().equals("ORGANIZATION")){
        ORGANIZATION.add(hp.get("text").toString());
      }
//      if(hp.get("ner").toString().equals("LOCATION_DEFINE")){
//        LOCATION_DEFINE.add(hp.get("text").toString());
//      }
    }

    String SERV_LOCAL_URL = "http://"+data.ServerHost+":"+data.ServerPort+"/tokensregex?pattern=(?$local[{ner:/GPE|LOCATION|FACILITY|CITY|STATE_OR_PROVINCE/}]+[{ner:/DATE/}]?[{ner:/NUMBER|MISC|DATE|ORDINAL/}]?[{word:/线|号|室|弄|栋|橦|房间|楼|、/}]?[{tag:/NR|AD|M|JJ|NN/}]?[{tag:/NR|AD|M|JJ|NN/}]?[{tag:/NR|AD|M|JJ|NN/}]?[{ner:/NUMBER|MISC|DATE|ORDINAL/}]?[{word:/号 线|号|室|弄|栋|橦|房间|楼|、/}]?[{ner:/NUMBER|MISC|DATE|ORDINAL/}]?[{word:/号 线|号|室|弄|栋|橦|房间|楼|、/}]?[{ner:/NUMBER|MISC|DATE|ORDINAL/}]?[{word:/号 线|号|室|弄|栋|橦|房间|楼|、/}]?)&properties=%7B%22annotators%22%3A%20%22tokenize%2Cssplit%2Cner%22%2C%20%22date%22%3A%20%222019-08-13T23%3A22%3A20%22%7D&pipelineLanguage=zh";
    ResponseEntity<String> response_local = restTemplate.postForEntity(SERV_LOCAL_URL, Entity, String.class);
    JSONObject local_json = (JSONObject) JSONObject.parse(response_local.getBody());
    ArrayList<HashMap> tokens = (ArrayList) sentences.get(0).get("tokens");
    for(Object lj :(com.alibaba.fastjson.JSONArray)local_json.get("sentences")){
      JSONObject lj1 = (JSONObject) lj;
      if(lj1.size()>1){
        Set<String> iterator = lj1.keySet();
        for(String js : iterator){
          if(!js.equals("length")) {
            JSONObject value = (JSONObject) lj1.get(js);
            Integer begin = Integer.valueOf(value.get("begin").toString());
            Integer end = Integer.valueOf(value.get("end").toString());
            String local_s = "";
            for(; begin<end; begin++){
              String local = tokens.get(begin).get("word").toString();
                local_s += local;
            }
            if(local_s.length() > 2) {
              LOCATION_DEFINE.add(local_s);
            }
            String a = "1";
          }
        }
      }
    }
    RowDataUtil.addValueData( r, 2, persons.toString() );
    RowDataUtil.addValueData( r, 3, ORGANIZATION.toString() );
    RowDataUtil.addValueData( r, 4, LOCATION_DEFINE.toString() );
    Object[] outputRow =r;
    putRow(data.outputRowMeta,outputRow);
    if ( checkFeedback( getLinesRead() ) ) {
      logBasic( "Linenr " + getLinesRead() );  // Some basic logging every 5000 rows.
    }

    return true;
  }

  @Override
  public boolean init( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (StanfordNerPluginMeta) smi;
    data = (StanfordNerPluginData) sdi;

    return super.init( smi, sdi );
  }

  @Override
  public void dispose( StepMetaInterface smi, StepDataInterface sdi ) {
    meta = (StanfordNerPluginMeta) smi;
    data = (StanfordNerPluginData) sdi;

    super.dispose( smi, sdi );
  }

  //
  // Run is were the action happens!
  public void run() {
    logBasic( "Starting to run..." );
    try {
      while ( processRow( meta, data ) && !isStopped() ) {
        // Process rows
      }
    } catch ( Exception e ) {
      logError( "Unexpected error : " + e.toString() );
      logError( Const.getStackTracker( e ) );
      setErrors( 1 );
      stopAll();
    } finally {
      dispose( meta, data );
      logBasic( "Finished, processing " + getLinesRead() + " rows" );
      markStop();
    }
  }
}

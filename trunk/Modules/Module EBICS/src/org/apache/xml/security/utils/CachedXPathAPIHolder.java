/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.xml.security.utils;

import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Document;

/**
 * @author Raul Benito
 */
public class CachedXPathAPIHolder {

    static ThreadLocal<CachedXPathAPI> local = new ThreadLocal<CachedXPathAPI>();
    static ThreadLocal<Document> localDoc = new ThreadLocal<Document>();

    /**
     * Sets the doc for the xpath transformation. Resets the cache if needed
     * @param doc
     */
    public static void setDoc(Document doc) {                    
        if (localDoc.get() != doc) {
            CachedXPathAPI cx = local.get();
            if (cx == null) {
                cx = new CachedXPathAPI();
                local.set(cx);
                localDoc.set(doc);
                return;
            }
            //Different docs reset.
            cx.getXPathContext().reset();
            localDoc.set(doc);                     
        }		
    }

    /**
     * @return the cachexpathapi for this thread
     */
    public static CachedXPathAPI getCachedXPathAPI() {        
        CachedXPathAPI cx = local.get();        
        if (cx == null) {
            cx = new CachedXPathAPI();
            local.set(cx);
            localDoc.set(null);            
        }
        return cx;
    }
}

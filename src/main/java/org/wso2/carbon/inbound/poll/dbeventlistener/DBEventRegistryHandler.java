/*
* Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.wso2.carbon.inbound.poll.dbeventlistener;

import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.config.Entry;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.registry.AbstractRegistry;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Read/Write operations with registry
 */
public class DBEventRegistryHandler {
    private static final Log log = LogFactory.getLog(DBEventRegistryHandler.class.getName());
    private AbstractRegistry registry;

    public DBEventRegistryHandler(SynapseEnvironment synEnv) {
        registry = (AbstractRegistry) synEnv.getSynapseConfiguration().getRegistry();
    }

    public String readFromRegistry(String resourcePath) {
        if (log.isDebugEnabled()) {
            log.info("Reading the registry property from the path " + resourcePath);
        }

        String obj = null;
        if (registry != null) {
            Object registryResource = registry.getResource(new Entry(resourcePath), null);
            if (registryResource != null) {
                obj = ((OMTextImpl) registryResource).getText();
            } else {
                if (log.isDebugEnabled()) {
                    log.info("Getting the default timestamp as the property is not set in the registry.");
                }
                String defaultTimestamp = getDefaultTimestamp();
                writeToRegistry(resourcePath, defaultTimestamp, true);
                return defaultTimestamp;
            }
        } else {
            log.error("Error while accessing the registry");
        }
        return obj;
    }

    public void writeToRegistry(String resourceID, Object date, boolean isNew) {
        if (log.isDebugEnabled()) {
            log.info("Reading the registry property " + date + " to the path " + resourceID);
        }
        if (isNew) {
            registry.newResource(resourceID, false);
        }
        registry.updateResource(resourceID, date);
    }

    /**
     * @return default timestamp
     */
    private String getDefaultTimestamp() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DBEventConstants.REGISTRY_TIME_FORMAT);
        cal.add(Calendar.MONTH, -1);
        return sdf.format(cal.getTime());
    }
}
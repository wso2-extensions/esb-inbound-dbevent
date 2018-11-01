/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.inbound.poll.unit.test.dbeventlistener;

import org.apache.synapse.MessageContext;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.core.axis2.Axis2SynapseEnvironment;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.inbound.poll.dbeventlistener.DBEventConstants;
import org.wso2.carbon.inbound.poll.dbeventlistener.DBEventPollingConsumer;

import java.sql.ResultSet;
import java.util.Properties;

public class DBEventPollingConsumerTest {
    Properties properties;
    private DBEventPollingConsumer dbEventPollingConsumer;
    private ResultSet resultSet;

    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(DBEventConstants.DB_DRIVER, "com.mysql.jdbc.Driver");
        properties.setProperty(DBEventConstants.DB_URL, "jdbc:mysql://localhost/test");
        properties.setProperty(DBEventConstants.DB_USERNAME, "root");
        properties.setProperty(DBEventConstants.DB_PASSWORD, "");
        properties.setProperty(DBEventConstants.DB_FILTERING_CRITERIA, "criteria");
        properties.setProperty(DBEventConstants.DB_FILTERING_COLUMN_NAME, "column1");
        properties.setProperty(DBEventConstants.TABLE_PRIMARY_KEY, "ID");
        properties.setProperty(DBEventConstants.DB_TABLE, "tables");
        SynapseConfiguration config = new SynapseConfiguration();
        SynapseEnvironment env = new Axis2SynapseEnvironment(config);
        dbEventPollingConsumer = new DBEventPollingConsumer(properties, "test", env, 1000, "seq1", "error1", false,
                false);
        resultSet = Mockito.mock(ResultSet.class);
    }

    @Test
    public void getColumnValueTest() throws Exception {
        Mockito.when(resultSet.getString("column1")).thenReturn("column1Value");
        String result = Whitebox.invokeMethod(dbEventPollingConsumer, "getColumnValue", resultSet, "column1", 1);
        Assert.assertEquals(result, "column1Value");
    }

    @Test
    public void getColumnValueTestWithUnknownType() throws Exception {
        String result = Whitebox.invokeMethod(dbEventPollingConsumer, "getColumnValue", resultSet, "column1", 100);
        Assert.assertNull(result);
    }

    @Test
    public void buildQueryTest() throws Exception {
        String result = Whitebox
                .invokeMethod(dbEventPollingConsumer, "buildQuery", "table1", "criteria", "column1", "timestamp");
        Assert.assertEquals(result, "SELECT * FROM table1");
    }

    @Test
    public void buildQueryTestWithFilteringByBoolean() throws Exception {
        String result = Whitebox
                .invokeMethod(dbEventPollingConsumer, "buildQuery", "table1", "byBooleanColumn", "column1",
                        "timestamp");
        Assert.assertEquals(result, "SELECT * FROM table1 WHERE column1='true'");
    }

    @Test
    public void buildQueryTestWithFilteringByTimeStamp() throws Exception {
        String result = Whitebox
                .invokeMethod(dbEventPollingConsumer, "buildQuery", "table1", "byLastUpdatedTimestampColumn", "column1",
                        "timestamp");
        Assert.assertEquals(result, "SELECT * FROM table1 WHERE column1 > 'timestamp' ORDER BY column1 ASC ");
    }

    @Test
    public void isRollbackTest() throws Exception {
        org.apache.axis2.context.MessageContext mc = new org.apache.axis2.context.MessageContext();
        SynapseConfiguration config = new SynapseConfiguration();
        SynapseEnvironment env = new Axis2SynapseEnvironment(config);
        MessageContext messageContext = new Axis2MessageContext(mc, config, env);
        messageContext.setProperty(DBEventConstants.SET_ROLLBACK_ONLY, "true");
        Assert.assertTrue(Whitebox.invokeMethod(dbEventPollingConsumer, "isRollback", messageContext));
    }

    @Test
    public void isRollbackTestWithNoRollback() throws Exception {
        org.apache.axis2.context.MessageContext mc = new org.apache.axis2.context.MessageContext();
        SynapseConfiguration config = new SynapseConfiguration();
        SynapseEnvironment env = new Axis2SynapseEnvironment(config);
        MessageContext messageContext = new Axis2MessageContext(mc, config, env);
        Assert.assertFalse(Whitebox.invokeMethod(dbEventPollingConsumer, "isRollback", messageContext));
    }
}
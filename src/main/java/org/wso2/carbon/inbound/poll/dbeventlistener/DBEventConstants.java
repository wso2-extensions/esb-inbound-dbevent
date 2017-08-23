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

public class DBEventConstants {
    public static final String DB_USERNAME = "username";
    public static final String DB_PASSWORD = "password";
    public static final String DB_URL = "url";
    public static final String DB_DRIVER = "driverName";
    public static final String DB_TABLE = "tableName";
    public static final String DB_FILTERING_CRITERIA = "filteringCriteria";
    public static final String DB_FILTERING_COLUMN_NAME = "filteringColumnName";
    public static final String DB_FILTERING_BY_TIMESTAMP = "byLastUpdatedTimestampColumn";
    public static final String DB_FILTERING_BY_BOOLEAN = "byBooleanColumn";
    public static final String DB_DELETE_AFTER_POLL = "deleteAfterPoll";
    public static final String REGISTRY_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String SET_ROLLBACK_ONLY = "SET_DB_ROLLBACK_ONLY";
    public static final String REGISTRY_PATH = "registryPath";
    public static final String TABLE_PRIMARY_KEY = "primaryKey";
    public static final String CONNECTION_VALIDATION_QUERY = "connectionValidationQuery";
}


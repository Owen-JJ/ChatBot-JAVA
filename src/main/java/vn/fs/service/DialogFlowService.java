package vn.fs.service;

import com.google.cloud.dialogflow.v2.QueryResult;

public interface DialogFlowService {
    QueryResult runIntent(String requestText) throws Exception;
}

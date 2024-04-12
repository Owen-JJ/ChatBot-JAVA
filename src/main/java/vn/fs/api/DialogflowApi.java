package vn.fs.api;

import com.google.cloud.dialogflow.v2.QueryResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.fs.service.DialogFlowService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/dialogflow")
public class DialogflowApi {

    @Autowired
    private  DialogFlowService dialogFlowService;

    @PostMapping("/requestText")
    public ResponseEntity<ResponseObject> requestText(@RequestBody RequestObject request) {
        try {
            QueryResult result = dialogFlowService.runIntent(request.getRequestText());
            return ResponseEntity.ok(new ResponseObject(result.getFulfillmentText(),
                    result.getQueryText(), result.getIntent().getDisplayName()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Getter
    static class RequestObject {
        private String projectId;
        private String requestText;

        public RequestObject() {}

        public RequestObject(String projectId, String requestText) {
            this.projectId = projectId;
            this.requestText = requestText;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

        public void setRequestText(String requestText) {
            this.requestText = requestText;
        }
    }

    @Setter
    @Getter
    static class ResponseObject {
        private String responseMessage;
        private String originalQuery;
        private String intent;

        public ResponseObject() {}

        public ResponseObject(String responseMessage, String originalQuery, String intent) {
            this.responseMessage = responseMessage;
            this.originalQuery = originalQuery;
            this.intent = intent;
        }
    }
}

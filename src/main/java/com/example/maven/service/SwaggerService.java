package com.example.maven.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.maven.Pipeline;
import com.example.maven.config.DroolsConfig;
import com.example.maven.domain.APIDto;
import com.example.maven.domain.ApiResponse;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.kie.api.runtime.KieSession;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@Singleton
public class SwaggerService {
    @Inject
   private DroolsConfig droolsConfig;

    public List<ApiResponse> getRules(String swaggerUr,String drlPath) {
        List<APIDto> apiDtos = extractAPIInformation(swaggerUr,drlPath);
        return apiDtos.stream().map(it -> {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setUrl(it.getUrl());
            apiResponse.setRequestType(it.getType());
            apiResponse.setMessages(it.getMessages());
            apiResponse.setScore(it.getScore());
            return apiResponse;
        }).collect(Collectors.toList());
    }

    public List<APIDto> extractAPIInformation(String swaggerUrl,String drlPath) {
        List<APIDto> apiDtos = new ArrayList<>();
        try {
            HashMap response = new ObjectMapper().readValue(new File(swaggerUrl), HashMap.class);
            Map<String, Object> paths = (Map<String, Object>) response.get("paths");
            for (Map.Entry<String, Object> entry : paths.entrySet()) {
                Map<String, Object> apiTypes = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> api : apiTypes.entrySet()) {
                    APIDto apiDto = new APIDto();
                    apiDto.setType(api.getKey());
                    apiDto.setUrl(entry.getKey());
                    apiDto.setResponseCodes(getResponseCode((Map<String, Object>) api.getValue()));
                    KieSession kieSession = droolsConfig.getKieContainer(drlPath).newKieSession();
                    kieSession.insert(apiDto);
                    kieSession.fireAllRules();
                    kieSession.dispose();
                    apiDtos.add(apiDto);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return apiDtos;
    }

//    private static void isNounOrProun(String url) {
//        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
//        CoreDocument coreDocument = new CoreDocument(url);
//        stanfordCoreNLP.annotate(coreDocument);
//        List<CoreLabel> coreLabels = coreDocument.tokens();
//        for (CoreLabel coreLabel : coreLabels) {
//            String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//            System.out.println(coreLabel.originalText() + "=" + pos);
//        }
//    }

    private List<String> getResponseCode(Map<String, Object> map) {
        Map<String, Object> responses = (Map<String, Object>) map.get("responses");
        return responses.entrySet().stream().map(it -> it.getKey()).collect(Collectors.toList());
    }
}

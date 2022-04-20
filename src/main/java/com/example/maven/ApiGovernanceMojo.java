package com.example.maven;

import com.example.maven.domain.ApiResponse;
import com.example.maven.service.RuleService;
import com.example.maven.service.SwaggerService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.inject.Inject;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Mojo(name = "validate", defaultPhase = LifecyclePhase.INITIALIZE)
public class ApiGovernanceMojo extends AbstractMojo {
    @Inject
    private RuleService ruleService;
    @Inject
    private SwaggerService swaggerService;
    @Parameter(property = "percentage", defaultValue = "0")
    private Integer percentage;

    @Parameter(property = "filePath", defaultValue = "openapi.json")
    private String filePath;

    public void execute() throws MojoExecutionException {

        try {
            Map<String, String> map = ruleService.loadRuleFromDBIntoDrlFile();
            String drlPath = map.get("drlFilePath");
            getLog().info("Drl File Path=" + drlPath);
            File file = new File(filePath);
            getLog().info("File Path" + file.getAbsolutePath());
            List<ApiResponse> apiDtoList = swaggerService.getRules(file.getAbsolutePath(), drlPath);
            if (CollectionUtils.isNotEmpty(apiDtoList)) {
                getLog().info("Before Filtered List Size=" + apiDtoList.size());
                String numOfRule = map.get("numOfRule");

                //apiDtoList = apiDtoList.stream().filter(it -> it.getScore() < percentage).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(apiDtoList)) {
                    int totalScore = apiDtoList.stream().mapToInt(ApiResponse::getScore).sum();
                    getLog().info("Total Score=" + totalScore);
                    getLog().info("Total Num of Rule=" +  numOfRule);
                    double actualPercentage = (totalScore / Integer.parseInt(numOfRule)) * 100;
//                      for (ApiResponse apiResponse : apiDtoList) {
//                        getLog().info(apiResponse.toString());
//                    }

                    getLog().info("Actual Percentage=" + actualPercentage);
                    getLog().info("Minimum Expected Percentage=" + percentage);
                    if (actualPercentage < percentage) {
                        throw new MojoExecutionException("Score not up to the mark:");
                    }
                }

            } else
                throw new MojoExecutionException("API is not found in this Open APi Spec Documentation");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new MojoExecutionException("Error while loading Rule From Database");
        }
    }
}

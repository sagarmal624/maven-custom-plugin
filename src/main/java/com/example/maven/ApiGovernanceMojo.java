package com.example.maven;

import com.example.maven.service.SwaggerService;
import com.example.maven.domain.ApiResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


@Mojo(name = "validate", defaultPhase = LifecyclePhase.INITIALIZE)
public class ApiGovernanceMojo extends AbstractMojo {

    @Inject
    SwaggerService swaggerService;
    @Parameter(property = "score", defaultValue = "0")
    private Integer score;

    @Parameter(property = "filePath", defaultValue = "openapi.json")
    private String filePath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        File file = new File(filePath);
        getLog().info("File Path" + file.getAbsolutePath());
        List<ApiResponse> apiDtoList = swaggerService.getRules(file.getAbsolutePath());
        if (CollectionUtils.isNotEmpty(apiDtoList)) {
            apiDtoList = apiDtoList.stream().filter(it -> it.getScore() < score).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(apiDtoList)) {
                for (ApiResponse apiResponse : apiDtoList) {
                    getLog().info(apiResponse.toString());
                }
                throw new MojoExecutionException("This API is not have an expected score:");
            }

        } else
            throw new MojoExecutionException("API is not found in this Open APi Spec Documentation");
    }
}

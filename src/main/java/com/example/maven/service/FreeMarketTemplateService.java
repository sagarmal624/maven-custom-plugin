package com.example.maven.service;

import com.example.maven.domain.DroolsFile;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Named
@Singleton
public class FreeMarketTemplateService {

    public String generateDrl(DroolsFile droolsFile) {
        String path = null;
        try {
            Configuration cfg = new Configuration(new Version("2.3.23"));
            cfg.setClassForTemplateLoading(FreeMarketTemplateService.class, "/");
            cfg.setDefaultEncoding("UTF-8");

            Template template = cfg.getTemplate("drools.ftl");

            Map<String, Object> templateData = new HashMap<>();
            templateData.put("droolsFile", droolsFile);

            try (StringWriter out = new StringWriter()) {
                template.process(templateData, out);
                String drlContent = out.getBuffer().toString();
                Path tempFile = Files.createTempFile(null, null);
                Files.write(tempFile, drlContent.getBytes(StandardCharsets.UTF_8));
                path = System.getProperty("java.io.tmpdir") + "drools.drl";
                FileWriter myWriter = new FileWriter(path);
                myWriter.write(drlContent);
                myWriter.close();
                out.flush();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
        return path;
    }
}
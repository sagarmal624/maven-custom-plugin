package com.example.maven.config;

import org.drools.core.io.impl.ClassPathResource;
import org.drools.core.io.impl.FileSystemResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;

import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DroolsConfig {

    private final KieServices kieServices = KieServices.Factory.get();

//    public static void main(String[] args) {
//        KieContainer  kt=new DroolsConfig().getKieContainer("/var/folders/km/q8jcsx652qs_lkmr2z9b1vc40000gn/T//drools.drl");
//        System.out.println(kt);
//    }
    public KieContainer getKieContainer(String filePath) {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(new FileSystemResource(filePath));
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId());
    }


}